package com.x.demo.service;

import com.x.demo.dao.main.UserDAO;
import com.x.demo.model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service
public class UserService {

    private static UserService instance = new UserService();

    public static UserService getInstance() {
        return instance;
    }

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    private UserService() {
    }

    @Resource
    private UserDAO userDAO;

    public boolean addUserInfo(String firstName, String lastName, Integer age) {
        UserInfo userInfo = new UserInfo(firstName, lastName, age);
        boolean flag = true;
        try {
            userDAO.addUserInfo(userInfo);
        } catch (Exception e) {
            logger.error("insert user exist error {} !", e.getMessage());
            flag = false;
        }
        return flag;
    }


    public boolean addUserInfo(UserInfo userInfo) {
        boolean flag = true;
        try {
            userDAO.addUserInfo(userInfo);
        } catch (Exception e) {
            logger.error("insert user exist error {} !", e.getMessage());
            flag = false;
        }
        return flag;
    }

    public UserInfo getUserInfoById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        return this.userDAO.getUserInfoById(id);
    }
}
