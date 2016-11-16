package com.qunar.fresh.librarysystem.service.impl;

import java.nio.charset.Charset;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Strings;
import com.google.common.collect.*;
import com.google.common.hash.*;
import com.qunar.fresh.librarysystem.resulttype.JsonResult;
import com.qunar.fresh.librarysystem.dao.UserDao;
import com.qunar.fresh.librarysystem.model.User;
import com.qunar.fresh.librarysystem.model.enums.AdminUserStatus;
import com.qunar.fresh.librarysystem.utils.EncodeUtils;
import com.qunar.fresh.librarysystem.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qunar.fresh.librarysystem.dao.ManagerDao;
import com.qunar.fresh.librarysystem.model.Manager;
import com.qunar.fresh.librarysystem.service.UserService;
import com.qunar.security.QSSO;

/**
 * Created with IntelliJ IDEA. User: he.chen Date: 14-4-2 Time: 上午10:18 To change this template use File | Settings |
 * File Templates.
 */
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private ManagerDao managerDao;
    private static final Logger logger = LoggerFactory.getLogger("servicelogger");
    private static final String LIB_ATTRIBUTE = "userLibId";
    private static final String TOKEN_ATTRIBUTE = "token";
    private static final String NAME_ATTRIBUTE = "name";
    private Map<String, String> roleUrlMap;
    private static int SESSION_ACTION_TIME = 60 * 60;

    /**
     * 登录成功后设置登录信息
     * 
     * @param token
     * @param request
     * @param response
     * @return
     */
    public String setLogin(String token, HttpServletRequest request, HttpServletResponse response) {
        String userRtx = "";
        try {
            userRtx = QSSO.getUser(token);
        } catch (Exception e) {
            logger.error("QSSO 获取user异常", e);
            return userRtx;
        }

        StringBuilder loginToken = new StringBuilder();
        loginToken.append(Hashing.md5().hashString(userRtx, Charset.defaultCharset()).toString());
        loginToken.append(Long.toString(new Date().getTime()));
        setLoginStatus(loginToken.toString(), userRtx);

        Cookie tokenCookie = new Cookie(TOKEN_ATTRIBUTE, loginToken.toString());
        Cookie nameCookie = new Cookie(NAME_ATTRIBUTE, EncodeUtils.encodeString(userRtx));

        tokenCookie.setMaxAge(SESSION_ACTION_TIME);
        nameCookie.setMaxAge(SESSION_ACTION_TIME);
        response.addCookie(tokenCookie);
        response.addCookie(nameCookie);
        Manager manager = managerDao.fetchManager(userRtx, AdminUserStatus.ISVALID);
        if (manager != null) {
            Cookie libCookie = new Cookie(LIB_ATTRIBUTE, EncodeUtils.encodeInt(manager.getLibId()));
            libCookie.setMaxAge(SESSION_ACTION_TIME);
            response.addCookie(libCookie);
        }
        logger.info("用户登录，设置登录信息到cookie和数据库，userRtx:{}", userRtx);
        return userRtx;

    }

    public void setLoginStatus(String loginToken, String userRtx) {
        userDao.loginUser(loginToken, userRtx);
    }

    /**
     * 检验是否登录
     * 
     * @param cookieValue
     * @param request
     * @return
     */
    public boolean checkLogin(String cookieValue, HttpServletRequest request) {
        logger.info("检验是否登录,{},{}", cookieValue, request);
        String userRtx = userDao.checkUserLogin(cookieValue);
        String cookieRtx = UserUtils.getUserRtx(request);
        Integer coolieLibId = UserUtils.getUserLib(request);
        Integer libId = getUserLibId(userRtx);

        if (userRtx != null && userRtx.equals(cookieRtx) && libId.equals(coolieLibId)) {
            return true;
        }
        return false;
    }

    public int getUserLibId(String userRtx) {
        logger.info("获取用户的libId");
        int libId = 0;
        if (Strings.isNullOrEmpty(userRtx)) {
            return libId;
        }
        Manager manager = managerDao.fetchManager(userRtx, AdminUserStatus.ISVALID);
        if (manager != null) {
            libId = manager.getLibId();
        }
        return libId;
    }

    /**
     * 获取登录用户的角色，读者，普通管理员和超级管理员
     * 
     * @param userRtx
     * @return
     */
    @Override
    public String getUserRole(String userRtx) {
        String userRole = "reader";
        Manager manager = managerDao.fetchManager(userRtx, AdminUserStatus.ISVALID);
        if (manager != null && manager.getStatus() == 1) {
            if (manager.getSuper() == 0) {
                userRole = "admin";
            } else if (manager.getSuper() == 1) {
                userRole = "superAdmin";
            }

        }
        logger.info("获取用户角色，userRtx:{}", userRole);
        return userRole;
    }

    /**
     * 获取登录用户应该跳转的页面
     * 
     * @param userRtx
     * @return
     */
    @Override
    public String getRedirectUrl(String userRtx) {
        String userRole = getUserRole(userRtx);
        String url = roleUrlMap.get(userRole);
        logger.info("获取用户登录后url {}", url);
        return url;
    }

    /**
     * 获取登录用户的rtx
     * 
     * @param userRtx
     * @return
     */
    @Override
    public User getUserByRtx(String userRtx) {
        User user = userDao.getUserByRtx(userRtx);
        return user;
    }

    /**
     * 向数据中插入一条用户信息
     * 
     * @param user
     */
    public void insertUser(User user) {
        userDao.insertUserAllInfo(user);
    }

    public Map<String, Object> getUserLoginInfo(HttpServletRequest request) {
        String cookieValue = getTokenCookie(request);
        boolean isLogin = checkLogin(cookieValue, request);
        if (cookieValue == null || !isLogin) {
            JsonResult jsonResult = new JsonResult(false);
            return jsonResult.getJsonDataMap();
        } else {
            JsonResult jsonResult = new JsonResult(true);
            jsonResult.put("userRtx", UserUtils.getUserRtx(request));
            return jsonResult.getJsonDataMap();
        }
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
        return "";
    }

    public void setRoleUrlMap(Map<String, String> roleUrlMap) {
        if (roleUrlMap != null) {
            this.roleUrlMap = ImmutableMap.copyOf(roleUrlMap);
        }
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public ManagerDao getManagerDao() {
        return managerDao;
    }

    public void setManagerDao(ManagerDao managerDao) {
        this.managerDao = managerDao;
    }
}
