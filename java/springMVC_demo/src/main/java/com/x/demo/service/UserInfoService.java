package com.x.demo.service;

import com.x.demo.dao.main.UserInfoDAO;
import com.x.demo.model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;


@Service
public class UserInfoService {

    private static UserInfoService instance = new UserInfoService();

    public static UserInfoService getInstance() {
        return instance;
    }

    private static Logger logger = LoggerFactory.getLogger(UserInfoService.class);

    private UserInfoService() {
    }

    @Resource
    private UserInfoDAO userInfoDAO;

    public boolean addUserInfoList(List<UserInfo> userInfoList) {
        boolean flag = true;
        try {
            userInfoDAO.addUserInfoList(userInfoList);
        } catch (Exception e) {
            logger.error("insert user exist error {} !", e.getMessage());
            flag = false;
        }
        return flag;
    }

    public List<UserInfo> getAllUserList() {
        return userInfoDAO.getALlUserInfoList();
    }

    public UserInfo getUserInfoById(Long id) {

        return this.userInfoDAO.getUserInfoById(id);
    }
}
