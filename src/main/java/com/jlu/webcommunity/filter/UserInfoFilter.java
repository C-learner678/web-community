package com.jlu.webcommunity.filter;

import com.jlu.webcommunity.dao.UserDao;
import com.jlu.webcommunity.entity.User;
import com.jlu.webcommunity.filter.context.UserContext;
import com.jlu.webcommunity.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/*", filterName = "userInfoFilter", asyncSupported = true)
public class UserInfoFilter implements Filter {
    @Autowired
    private UserDao userDao;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            //从请求头获取token，解密出用户ID，查询用户信息，把用户信息保存到UserInfoContext
            UserContext.UserData userData = new UserContext.UserData();
            String token = ((HttpServletRequest) servletRequest).getHeader("userToken");
            userData.setToken(token);
            if (token != null && !token.isEmpty()) {
                try {
                    User user = jwtUtil.getUserBySession(token);
                    userData.setId(user.getId());
                    userData.setRole(user.getRole());
                    userData.setName(user.getName());
                }catch (Exception e){
                    log.error("token错误：" + e.getMessage());
                    userData.setId(null);
                    userData.setRole(null);
                    userData.setName(null);
                }
            }
            UserContext.addUserData(userData);
            log.info("请求路径：" + ((HttpServletRequest) servletRequest).getRequestURI() + "\n" +
                    "用户ID：" + UserContext.getUserData().getId() + " 角色：" + UserContext.getUserData().getRole());
            //通过Filter
            filterChain.doFilter(servletRequest, servletResponse);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            //清除
            UserContext.clear();
        }
    }

    @Override
    public void destroy() {
    }
}
