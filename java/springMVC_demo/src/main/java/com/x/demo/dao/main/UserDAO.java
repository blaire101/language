package com.x.demo.dao.main;

import com.x.demo.model.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDAO {

    UserInfo getUserInfoById(@Param("id") String id);

    void addUserInfoByParam(@Param("first_name") String firstName, @Param("last_name") String lastName, @Param("age") Integer age);

    void addUserInfo(UserInfo userInfo);

    List<UserInfo> getALlUserInfoList(@Param("last_name") String lastName, @Param("age") Integer age);

}

