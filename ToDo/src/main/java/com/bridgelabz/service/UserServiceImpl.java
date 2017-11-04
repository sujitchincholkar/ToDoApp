package com.bridgelabz.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.bridgelabz.dao.UserDao;
import com.bridgelabz.model.User;

public class UserServiceImpl implements UserService {
	@Autowired
	UserDao userDao;
	public int saveUserData(User user) {
		return userDao.saveUser(user);
	}

	public User getUserById(int id) {
		// TODO Auto-generated method stub
		return userDao.getUserById(id);
	}

	public User getUserByEmail(String email) {
		// TODO Auto-generated method stub
		return userDao.getUserByEmail(email);
	}

	public boolean updateUser(User user) {
		
		return userDao.updateUser(user);
	}
	
}
