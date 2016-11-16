package com.qunar.springtest.dao.impl;

import com.qunar.springtest.dao.UserDAO;
import com.qunar.springtest.model.User;
import org.springframework.stereotype.Component;

@Component("u")
public class UserDAOImpl implements UserDAO {

	private int daoId;
	private String daoStatus;
	
	public int getDaoId() {
		return daoId;
	}

	public void setDaoId(int daoId) {
		this.daoId = daoId;
	}

	public String getDaoStatus() {
		return daoStatus;
	}

	public void setDaoStatus(String daoStatus) {
		this.daoStatus = daoStatus;
	}

	public void addUser(User user) {
        System.out.println("user saved!");
    }
	
	@Override
	public String toString() {
        return this.daoId + ":" + this.daoStatus;
	}
}
