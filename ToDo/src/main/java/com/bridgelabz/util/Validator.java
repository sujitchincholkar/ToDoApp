package com.bridgelabz.util;

import org.springframework.beans.factory.annotation.Autowired;
import com.bridgelabz.dao.UserDao;
import com.bridgelabz.model.User;

public class Validator {
	@Autowired
	UserDao userDao;
	public  boolean userValidate(User user){
		boolean isValid=true;
		String nameValidator="^[a-zA-Z\\s]{3,}?$";
		String emailPattern = "^[A-Za-z0-9._]+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$";
		String  mobilePattern="^((\\+)?(\\d{2}[-]))?(\\d{10})?$";
		if(user.getFullName()==null||!user.getFullName().matches(nameValidator)){ 	
			isValid=false;
		}
		if(user.getEmail()==null||!user.getEmail().matches(emailPattern)){
			isValid=false;
		}
		if(user.getContactNo()==null||!user.getContactNo().matches(mobilePattern)){
			isValid=false;
		}
		if(user.getPassword()==null||(user.getPassword().length()<8)){
			isValid=false;
		}
		if(userDao.getUserByEmail(user.getEmail())!=null){
			isValid=false;
		}
		return isValid;
	}
}
