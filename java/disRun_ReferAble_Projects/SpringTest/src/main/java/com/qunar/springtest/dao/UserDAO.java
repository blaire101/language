package com.qunar.springtest.dao;

import com.qunar.springtest.model.User;
import org.springframework.stereotype.Repository;

/**
 * Author: libin.chen@qunar.com  Date: 14-5-27 20:54
 */
@Repository
public interface UserDAO {
    public void addUser(User user);
}
