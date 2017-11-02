package com.bridgelabz.dao;

import com.bridgelabz.model.User;

public interface UserDao {
	int saveUser(User user);
	boolean updateUser(User user);
	boolean deleteUser(User user);
	User getUserById(int id);
	User getUserByEmail(String email);
}
