package com.qunar.fresh.librarysystem.dao;

import com.qunar.fresh.librarysystem.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA. User: he.chen Date: 14-4-12 Time: 上午10:59 To change this template use File | Settings |
 * File Templates.
 */
@Repository
public interface UserDao {
    public User getUserByRtx(@Param("userRtx") String userRtx);

    public void insertUser(@Param("list") List<User> userList);

    public void updateUser(@Param("user") User user);

    public List<User> fetchAllStaffs();

    public void insertUserAllInfo(@Param("user") User user);

    public void loginUser(@Param("token") String token, @Param("userRtx") String userRtx);

    public String checkUserLogin(@Param("token") String token);
}
