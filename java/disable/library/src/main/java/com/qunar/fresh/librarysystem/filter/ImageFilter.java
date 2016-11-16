package com.qunar.fresh.librarysystem.filter;

import java.io.BufferedOutputStream;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.qunar.fresh.librarysystem.io.ResourceAccess;

/**
 * 访问图片时通过此过虑器返回图片资源，如果从图片仓库里读取失败，则调用chain.doFilter将控制权返还给Servlet容器
 * 
 * @author hang.gao
 * 
 */
public class ImageFilter implements Filter {

    /**
	 * 
	 */
    private ServletContext servletContext;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        servletContext = filterConfig.getServletContext();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        ResourceAccess resourceAccess = WebApplicationContextUtils.getWebApplicationContext(servletContext).getBean(
                ResourceAccess.class);
        BufferedOutputStream out = null;
        try {
            resourceAccess.loadPrivateAndStore(extractImageFileName(httpServletRequest),
                    out = new BufferedOutputStream(response.getOutputStream()));
        } catch (Throwable t) {
            // 将控制权返还给Servlet容器
            chain.doFilter(request, response);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 从请求中获取图片的文件名
     * 
     * @param httpServletRequest http请求对象
     * @return 请求的文件名
     */
    private String extractImageFileName(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getRequestURI().substring(httpServletRequest.getRequestURI().lastIndexOf('/') + 1);
    }

    @Override
    public void destroy() {
    }

}
