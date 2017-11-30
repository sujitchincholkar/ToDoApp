package com.bridgelabz.dao;

import java.util.Set;

import com.bridgelabz.model.Label;
import com.bridgelabz.model.User;

public interface UserDao {
	int saveUser(User user);
	boolean updateUser(User user);
	boolean deleteUser(User user);
	User getUserById(int id);
	User getUserByEmail(String email);
	public int addLabel(Label label);
	public boolean deleteLable(Label label);
	public boolean updateLable(Label label);
	public Set<Label> getAllLabels(int userId);
	
}
