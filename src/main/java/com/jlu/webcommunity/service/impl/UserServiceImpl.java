package com.jlu.webcommunity.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jlu.webcommunity.core.constant.MessageTypeConstant;
import com.jlu.webcommunity.core.constant.RedisConstant;
import com.jlu.webcommunity.core.constant.RocketmqConstant;
import com.jlu.webcommunity.core.rocketmq.RocketmqBody;
import com.jlu.webcommunity.core.rocketmq.RocketmqProducer;
import com.jlu.webcommunity.dao.UserInfoDao;
import com.jlu.webcommunity.entity.UserInfo;
import com.jlu.webcommunity.entity.dto.user.LoginDto;
import com.jlu.webcommunity.entity.dto.user.ModifyPasswordDto;
import com.jlu.webcommunity.entity.dto.user.ModifyUserStatusDto;
import com.jlu.webcommunity.entity.dto.user.RegisterDto;
import com.jlu.webcommunity.entity.vo.CaptchaVo;
import com.jlu.webcommunity.dao.UserDao;
import com.jlu.webcommunity.entity.User;
import com.jlu.webcommunity.entity.vo.GetCurrentUserVo;
import com.jlu.webcommunity.entity.vo.LoginVo;
import com.jlu.webcommunity.entity.vo.RegisterVo;
import com.jlu.webcommunity.core.filter.context.UserContext;
import com.jlu.webcommunity.service.UserService;
import com.jlu.webcommunity.core.JwtUtil;
import com.jlu.webcommunity.core.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RocketmqProducer rocketmqProducer;

    //登录：本项目管理员和普通用户均可登录。后台项目只允许管理员登录。
    //比较前端传来的验证码值与redis中保存的是否一致
    @Override
    public LoginVo login(LoginDto loginDto, boolean useCaptcha) {
        LoginVo result = new LoginVo();
        if(useCaptcha) {
            String key = RedisConstant.captchaKey + loginDto.getCaptchaKey();
            String captcha = loginDto.getCaptcha();
            if (!redisClient.hasKey(key)) {
                result.setStatus(2); //验证码过期
                return result;
            }
            if (!redisClient.get(key).equals(captcha)) {
                redisClient.del(key);
                result.setStatus(3); //验证码错误
                return result;
            }
            redisClient.del(key);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", loginDto.getName());
        queryWrapper.eq("password", loginDto.getPassword());
        User user = userDao.getOne(queryWrapper);
        if(user == null){
            result.setStatus(1); //用户名或密码错误
            return result;
        }
        // 更新登录时间
        user.setUpdateTime(new Date());
        userDao.updateById(user);
        // 生成token
        String token = jwtUtil.createSession(user);
        result.setStatus(0);
        result.setToken(token);
        return result;
    }

    //注册
    @Override
    @Transactional
    public RegisterVo register(RegisterDto registerDto) {
        RegisterVo result = new RegisterVo();
        String key = RedisConstant.captchaKey + registerDto.getCaptchaKey();
        String captcha = registerDto.getCaptcha();
        if (!redisClient.hasKey(key)) {
            result.setStatus(2); //验证码过期
            return result;
        }
        if (!redisClient.get(key).equals(captcha)) {
            redisClient.del(key);
            result.setStatus(3); //验证码错误
            return result;
        }
        redisClient.del(key);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", registerDto.getName());
        User user = userDao.getOne(queryWrapper);
        if(user != null){
            result.setStatus(1);
            return result;
        }
        User user1 = new User();
        user1.setName(registerDto.getName());
        user1.setPassword(registerDto.getPassword());
        user1.setRole("user");
        if(userDao.getBaseMapper().insertUser(user1) > 0){
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(user1.getId());
            userInfo.setInfo("");
            userInfo.setFrontName(user1.getName());
            if(userInfoDao.getBaseMapper().insertUserInfo(userInfo) > 0){
                // 设置用户统计
                redisClient.hSet(RedisConstant.userStatisticKey + user1.getId(),
                        RedisConstant.userFollowNumKey, 0);
                redisClient.hSet(RedisConstant.userStatisticKey + user1.getId(),
                        RedisConstant.userFollowerNumKey, 0);
                // 注册完成后自动登录
                String token = jwtUtil.createSession(user1);
                result.setStatus(0);
                result.setToken(token);
            }else{
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 回滚
                result.setStatus(1);
            }
        }else{
            result.setStatus(1);
        }
        return result;
    }

    @Override
    public void logout() {
        jwtUtil.removeSession(UserContext.getUserData().getToken());
    }

    //获取验证码：随机生成一个key，和验证码的值一起存到redis，再把key和验证码图片传到前端
    @Override
    public CaptchaVo getCaptcha() {
        String key = RandomUtil.randomString(20);
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(80, 40, 4, 10);
        CaptchaVo captchaVO = new CaptchaVo();
        captchaVO.setCaptchaImage(lineCaptcha.getImageBase64());
        captchaVO.setKey(key);
        redisClient.set(RedisConstant.captchaKey + key, lineCaptcha.getCode(),120);
        log.info("验证码：" + lineCaptcha.getCode());
        return captchaVO;
    }

    @Override
    public boolean modifyPassword(ModifyPasswordDto modifyPasswordDto){
        Long userId = UserContext.getUserData().getId();
        User user = userDao.getById(userId);
        if(!Objects.equals(modifyPasswordDto.getOldPassword(), user.getPassword())){
            return false;
        }
        user.setPassword(modifyPasswordDto.getNewPassword());
        user.setUpdateTime(new Date());
        userDao.updateById(user);
        return true;
    }

    @Override
    public GetCurrentUserVo getCurrentUser() {
        Long userId = UserContext.getUserData().getId();
        String name = UserContext.getUserData().getName();
        String role = UserContext.getUserData().getRole();
        UserInfo userInfo = userInfoDao.getById(userId);
        String frontName = userInfo.getFrontName();
        String info = userInfo.getInfo();
        String avatar = userInfo.getAvatar();
        GetCurrentUserVo vo = new GetCurrentUserVo();
        vo.setUserId(userId);
        vo.setName(name);
        vo.setRole(role);
        vo.setFrontName(frontName);
        vo.setInfo(info);
        vo.setAvatar(avatar);
        return vo;
    }

    @Override
    public boolean modifyUserStatus(ModifyUserStatusDto modifyUserStatusDto) {
        User user = userDao.getById(modifyUserStatusDto.getUserId());
        if(user == null){
            return false;
        }
        user.setBanned(modifyUserStatusDto.getBan());
        userDao.updateById(user);
        if(modifyUserStatusDto.getBan()) {
            redisClient.sAdd(RedisConstant.banUsersKey, String.valueOf(modifyUserStatusDto.getUserId()));
            RocketmqBody body = new RocketmqBody();
            body.setUserId(modifyUserStatusDto.getUserId());
            body.setType(MessageTypeConstant.BAN_USER);
            rocketmqProducer.syncSend(body, RocketmqConstant.topic);
        }else{
            redisClient.sRemove(RedisConstant.banUsersKey, String.valueOf(modifyUserStatusDto.getUserId()));
            RocketmqBody body = new RocketmqBody();
            body.setUserId(modifyUserStatusDto.getUserId());
            body.setType(MessageTypeConstant.CANCEL_BAN_USER);
            rocketmqProducer.syncSend(body, RocketmqConstant.topic);
        }
        return true;
    }

    @Override
    public boolean isUserBanned(Long id) {
        return redisClient.sIsMember(RedisConstant.banUsersKey, String.valueOf(id));
    }

    @Override
    public void setAllUserStatus(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("banned", true);
        List<User> userList = userDao.list(queryWrapper);
        redisClient.del(RedisConstant.banUsersKey);
        for(User user: userList){
            redisClient.sAdd(RedisConstant.banUsersKey, String.valueOf(user.getId()));
        }
    }

    @PostConstruct
    public void init(){
        this.setAllUserStatus();
    }
}
