package com.qunar.fresh.librarysystem.service;

import com.qunar.fresh.librarysystem.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: he.chen Date: 14-4-2 Time: 下午12:23 To change this template use File | Settings |
 * File Templates.
 */
public interface UserService {
    public String setLogin(String token, HttpServletRequest request, HttpServletResponse response);

    public String getUserRole(String userRtx);

    public String getRedirectUrl(String userRtx);

    public User getUserByRtx(String userRtx);

    public void insertUser(User user);

    public boolean checkLogin(String cookieValue, HttpServletRequest request);

    public void setLoginStatus(String loginToken, String userRtx);

    public int getUserLibId(String userRtx);

    public Map<String, Object> getUserLoginInfo(HttpServletRequest request);
}
