package com.bridgelabz.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.dao.UserDao;
import com.bridgelabz.model.User;
import com.bridgelabz.valdation.Validator;

@RestController
public class UserController {
	@Autowired
	UserDao userdao;
	@Autowired
	Validator validator;

	@RequestMapping(value = "/Register", method = RequestMethod.POST)
	public ResponseEntity registerUser(@RequestBody User user) {

		if (validator.userValidate(user)) {
			user.setActivated(false);
			int id = userdao.saveUser(user);
			if (id > 0) {
				return new ResponseEntity<String>("Success", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Unsucessful", HttpStatus.CONFLICT);
			}
		} else {
			return new ResponseEntity<String>("Invalid data", HttpStatus.BAD_REQUEST);
		}
	}
	@RequestMapping(value = "/Login", method = RequestMethod.POST)
	public ResponseEntity logIn(@RequestBody User userData,HttpServletRequest request){
		String email=userData.getEmail();
		String password=userData.getPassword();
		System.out.println(email);
		User user=userdao.getUserByEmail(email);
		if(userdao.getUserByEmail(email)!=null){
			if(user.getPassword().equals(password)){
				HttpSession session=((HttpServletRequest) request).getSession();
				session.setAttribute("id", user.getUserId());
				return ResponseEntity.ok("Login Successful");
			}else{
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Invalid Username and password");
			}
			
		}else{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
		}
		
	}
}
