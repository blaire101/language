package com.qunar.fresh.librarysystem.filter;

import com.qunar.fresh.librarysystem.service.UserService;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA. User: he.chen Date: 14-4-3 Time: 上午11:02 To change this template use File | Settings |
 * File Templates.
 */
public class LoginFilter implements Filter {
    private UserService userService;
    private static final String TOKEN_ATTRIBUTE = "token";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        userService = (UserService) WebApplicationContextUtils.getWebApplicationContext(
                filterConfig.getServletContext()).getBean(UserService.class);

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String cookieValue = getTokenCookie(httpServletRequest);
        boolean isLogin = userService.checkLogin(cookieValue, httpServletRequest);
        if (cookieValue == null || !isLogin) {
            String uri = httpServletRequest.getRequestURI();
            if (uri.contains("jsp")) {
                httpServletResponse.sendRedirect("/login.jsp");
            } else {
                httpServletResponse.setStatus(401);
            }
            return;
        }

        chain.doFilter(httpServletRequest, httpServletResponse);
        return;
    }

    private String getTokenCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return "";
        }
        for (Cookie cookie : cookies) {
            if (TOKEN_ATTRIBUTE.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    @Override
    public void destroy() {

    }
}
