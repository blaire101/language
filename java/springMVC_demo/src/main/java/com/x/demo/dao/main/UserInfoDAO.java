package com.x.demo.dao.main;

import com.x.demo.model.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInfoDAO {

    void addUserInfoList(List<UserInfo> userInfoList);

    List<UserInfo> getALlUserInfoList();

    UserInfo getUserInfoById(@Param("id") Long id);

//    void getUserInfoByNameAndAge(@Param("first_name") String firstName, @Param("last_name") String lastName, @Param("age") Integer age);


}

