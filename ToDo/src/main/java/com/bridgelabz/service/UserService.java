package com.bridgelabz.service;

import com.bridgelabz.model.User;

public interface UserService {
	public int saveUserData(User user);
	public User getUserById(int id);
	public User getUserByEmail(String email);
	public boolean updateUser(User user);
}
