package com.bridgelabz.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.bridgelabz.dao.UserDao;
import com.bridgelabz.model.Label;
import com.bridgelabz.model.User;

public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;
	
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

	public int addLabel(Label label) {
		return userDao.addLabel(label);
		
	}

	public boolean deleteLable(Label label) {
		// TODO Auto-generated method stub
		return userDao.deleteLable(label);
	}

	public boolean updateLable(Label label) {
		// TODO Auto-generated method stub
		return userDao.updateLable(label);
	}

	public Set<Label> getAllLabels(int userId) {
		// TODO Auto-generated method stub
		return userDao.getAllLabels(userId);
	}

	public List<User> getUserList() {
		// TODO Auto-generated method stub
		return userDao.getUserList();
	}
	
}
