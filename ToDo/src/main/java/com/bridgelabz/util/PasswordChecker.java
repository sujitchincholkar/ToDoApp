package com.bridgelabz.util;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordChecker {
	
	public String encodePassword(String password){
		BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
		String encodedPassword=encoder.encode(password);
		return encodedPassword;
	}

	public boolean checkPassword(String plainPassword,String encryptesPassword){
		boolean isValid=false;
		try{
			isValid=BCrypt.checkpw(plainPassword, encryptesPassword);
			return isValid;
		}catch(Exception e){
			e.printStackTrace();
			return isValid;
		}
		
		
	}
}
