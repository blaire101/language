package com.qunar.fresh.librarysystem.controller;

import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import com.qunar.fresh.librarysystem.resulttype.JsonResult;
import com.qunar.fresh.librarysystem.service.UserService;
import com.qunar.fresh.librarysystem.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * Created with IntelliJ IDEA. User: he.chen Date: 14-4-2 Time: 上午10:14 To change this template use File | Settings |
 * File Templates.
 */
@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    private static final String RTX_ATTRIBUTE = "userRtx";
    private static final String LIB_ATTRIBUTE = "userLibId";

    @RequestMapping("/login.do")
    @ResponseBody
    public Object login(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getParameter("token");
        if (token != null) {
            String userRtx = userService.setLogin(token, request, response);
            if (!Strings.isNullOrEmpty(userRtx)) {
                try {
                    response.sendRedirect(userService.getRedirectUrl(userRtx));
                } catch (IOException e) {
                    logger.error("重定向错误", e);
                }
            }
            return null;
        }
        try {
            response.sendRedirect("login.jsp");
        } catch (IOException e) {
            logger.error("重定向错误", e);
        }
        return null;
    }

    @RequestMapping("/logout.do")
    @ResponseBody
    public Object logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.removeAttribute(RTX_ATTRIBUTE);
        session.removeAttribute(LIB_ATTRIBUTE);
        String logoutToken = Hashing.md5().hashString(Long.toString(new Date().getTime()), Charset.defaultCharset())
                .toString();
        userService.setLoginStatus(logoutToken, UserUtils.getUserRtx(request));
        JsonResult jsonResult = new JsonResult(true);
        jsonResult.put("msg", "退出成功");
        return JsonResult.statusJson(0, "", jsonResult.getJsonDataMap());
    }

    @RequestMapping("/loginInfo.do")
    @ResponseBody
    public Object loginInfo(HttpServletRequest request) {
        userService.getUserLoginInfo(request);
        return JsonResult.statusJson(0, "", userService.getUserLoginInfo(request));
    }

}
