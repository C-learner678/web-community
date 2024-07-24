package com.jlu.webcommunity.core;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jlu.webcommunity.core.constant.RedisConstant;
import com.jlu.webcommunity.entity.User;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class JwtUtil {
    // 先填充JwtProperties类的属性，再注入到JwtUtil
    @Component
    @Data
    @ConfigurationProperties("jwt")
    public static class JwtProperties {
        @Value("${jwt.issuer}")
        private String issuer;
        @Value("${jwt.secret}")
        private String secret;
        @Value("${jwt.expire}")
        private Long expire;
    }
    private final JwtProperties jwtProperties;
    private Algorithm algorithm;
    private JWTVerifier verifier;

    @Autowired
    private RedisClient redisClient;

    public JwtUtil(JwtProperties jwtProperties){
        this.jwtProperties = jwtProperties;
        this.algorithm = Algorithm.HMAC256(jwtProperties.getSecret());
        this.verifier = JWT.require(algorithm).withIssuer(jwtProperties.getIssuer()).build();
    }

    //生成jwt，并把jwt与用户的关系保存到redis里
    //jwt本身有有效期，但无法主动删除，因此需要redis
    public String createSession(User user){
        HashMap<String, Object> sessionInfo = new HashMap<>();
        sessionInfo.put("userId", user.getId());
        sessionInfo.put("userName", user.getName());
        sessionInfo.put("userRole", user.getRole());
        String token = JWT.create().withIssuer(jwtProperties.getIssuer())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getExpire() * 1000))
                .withPayload(sessionInfo)
                .sign(algorithm);
        redisClient.set(RedisConstant.userSessionKey + token, String.valueOf(user.getId()), jwtProperties.getExpire());
        return token;
    }

    //用户退出登录，删除会话
    public void removeSession(String token){
        redisClient.del(RedisConstant.userSessionKey + token);
    }

    //根据会话token获取用户信息
    public User getUserBySession(String token){
        DecodedJWT jwt = verifier.verify(token);
        Map<String, Claim> map1 = jwt.getClaims();
        Long userId = map1.get("userId").asLong();
        String userName = map1.get("userName").asString();
        String userRole = map1.get("userRole").asString();
        User user = new User();
        user.setId(userId);
        user.setName(userName);
        user.setRole(userRole);
        String userId2 = (String) redisClient.get(RedisConstant.userSessionKey + token);
        if(userId2 == null){
            return null;
        }
        Long userId3 = Long.valueOf(userId2);
        if(!Objects.equals(userId, userId3)){
            return null;
        }
        return user;
    }
}
