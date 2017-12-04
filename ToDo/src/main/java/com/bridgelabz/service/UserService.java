package com.bridgelabz.service;

import java.util.List;
import java.util.Set;

import com.bridgelabz.model.Label;
import com.bridgelabz.model.User;

public interface UserService {
	public int saveUserData(User user);
	public User getUserById(int id);
	public User getUserByEmail(String email);
	public boolean updateUser(User user);
	public int addLabel(Label label);
	public boolean deleteLable(Label label);
	public boolean updateLable(Label label);
	public Set<Label> getAllLabels(int userId);
	public List<User> getUserList();
}
