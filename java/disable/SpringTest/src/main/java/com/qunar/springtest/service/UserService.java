package com.qunar.springtest.service;

import com.qunar.springtest.dao.UserDAO;
import com.qunar.springtest.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Author: libin.chen@qunar.com  Date: 14-5-27 22:09
 */
@Service
public class UserService {

    private UserDAO userDAO;

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void createUser(User user) {
        this.userDAO.addUser(user);
    }

}
