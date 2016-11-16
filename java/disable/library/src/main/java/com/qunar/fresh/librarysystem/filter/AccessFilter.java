package com.qunar.fresh.librarysystem.filter;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.qunar.fresh.librarysystem.service.UserService;
import com.qunar.fresh.librarysystem.utils.UserUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA. User: he.chen Date: 14-4-2 Time: 下午1:48 To change this template use File | Settings |
 * File Templates.
 */
public class AccessFilter implements Filter {

    private Multimap<String, String> roleDeniedMultimap = HashMultimap.create();
    private UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        userService = (UserService) WebApplicationContextUtils.getWebApplicationContext(
                filterConfig.getServletContext()).getBean(UserService.class);
        roleDeniedMultimap.put("reader", "admin");
        roleDeniedMultimap.put("reader", "superAdmin");
        roleDeniedMultimap.put("admin", "superAdmin");
        roleDeniedMultimap.put("superAdmin", "admin");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String role = userService.getUserRole(UserUtils.getUserRtx(httpServletRequest));
        System.out.println(role);
        Collection<String> deniedUris = roleDeniedMultimap.get(role);
        for (String deniedUri : deniedUris) {
            if (httpServletRequest.getRequestURI().startsWith("/" + deniedUri)) {
                httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }
        System.out.println("AccessFilter Success");
        chain.doFilter(httpServletRequest, httpServletResponse);
    }

    @Override
    public void destroy() {

    }
}
