package com.bridgelabz.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.dao.UserDao;
import com.bridgelabz.model.CustomResponse;
import com.bridgelabz.model.Label;
import com.bridgelabz.model.User;
import com.bridgelabz.service.Mailer;
import com.bridgelabz.service.Producer;
import com.bridgelabz.service.TokenService;
import com.bridgelabz.service.UserService;
import com.bridgelabz.util.PasswordChecker;
import com.bridgelabz.util.Validator;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private Validator validator;

	@Autowired
	private Producer producer;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private PasswordChecker passwordChecker;

	static Logger logger = Logger.getLogger(UserController.class);

	@RequestMapping(value = "/Register", method = RequestMethod.POST)
	public ResponseEntity<CustomResponse> registerUser(@RequestBody User user, HttpServletRequest request) {
		CustomResponse customResponse = new CustomResponse();
		System.out.println(user.getPassword());
		if (validator.userValidate(user)) {

			logger.info("User register");

			user.setActivated(false);
			user.setPassword(passwordChecker.encodePassword(user.getPassword()));

			int id = userService.saveUserData(user);

			if (id > 0) {

				String token = tokenService.generateToken(user.getEmail(), id);
				String url = String.valueOf(request.getRequestURL());
				url = url.substring(0, url.lastIndexOf("/")) + "/activate/" + token;
				// System.out.println(""+url);

				HashMap<String, String> map = new HashMap<String, String>();
				map.put("to", user.getEmail());
				map.put("message", url);
				producer.send(map);

				customResponse.setMessage("Success");
				return new ResponseEntity<CustomResponse>(customResponse, HttpStatus.OK);
			} 
			else {
				customResponse.setMessage("");
				return new ResponseEntity<CustomResponse>(customResponse, HttpStatus.CONFLICT);
			}
		} 
		else {
			customResponse.setMessage("Invalid data");
			return new ResponseEntity<CustomResponse>(customResponse, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/Login", method = RequestMethod.POST)
	public ResponseEntity<CustomResponse> logIn(@RequestBody User userData, HttpServletRequest request,
			HttpServletResponse response) {

		String email = userData.getEmail();
		CustomResponse customResponse = new CustomResponse();
		String password = userData.getPassword();

		User user = userService.getUserByEmail(email);

		if (userService.getUserByEmail(email) != null) {

			if (passwordChecker.checkPassword(password, user.getPassword())) {
				if (user.isActivated()) {
					logger.warn("User logged in" + user.getUserId());
					String token = tokenService.generateToken(user.getEmail(), user.getUserId());
					response.setHeader("Authorization", token);
					/*
					 * HttpSession session=((HttpServletRequest)
					 * request).getSession();
					 * session.setAttribute(session.getId(), user);
					 */
					customResponse.setMessage("login successfull");
					return new ResponseEntity<CustomResponse>(customResponse, HttpStatus.OK);
				} 
				else {
					customResponse.setMessage("User is not activated");
					return new ResponseEntity<CustomResponse>(customResponse, HttpStatus.BAD_REQUEST);
				}
			}
			else {
				customResponse.setMessage("Password incorrect");
				return new ResponseEntity<CustomResponse>(customResponse, HttpStatus.BAD_REQUEST);
			}
		} 
		else {
			customResponse.setMessage("User doent't exist");
			return new ResponseEntity<CustomResponse>(customResponse, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/activate/{token:.+}", method = RequestMethod.GET)
	public ResponseEntity activateUser(@PathVariable("token") String token, HttpServletResponse response)
			throws IOException {

		int id = tokenService.verifyToken(token);

		if (id > 0) {

			User user = userService.getUserById(id);
			if (user != null) {
				user.setActivated(true);

				if (userService.updateUser(user)) {
					logger.info("User activated " + id);

					return ResponseEntity.ok("User Activated");
				} 
				else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Problem occurred");
				}
			}
			else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Does not Exist");
			}
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sorry token is expired or invalid");
		}
	}
	
	@RequestMapping(value = "/getuser", method = RequestMethod.GET)
	public ResponseEntity<User> getUser(HttpServletRequest request){
		String token = request.getHeader("Authorization");
		User user = userService.getUserById(tokenService.verifyToken(token));
		if(user!=null){
			return ResponseEntity.ok(user);
		}else{
			return ResponseEntity.ok(user);
		}
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ResponseEntity logout(HttpSession session) {
		session.removeAttribute(session.getId());

		return ResponseEntity.ok("Logged out");
	}

	@RequestMapping(value = "/forgetpassword", method = RequestMethod.POST)
	public ResponseEntity<CustomResponse> forgetPassword(@RequestBody User userData, HttpServletRequest request,
			HttpServletResponse response) {
		CustomResponse customResponse=new CustomResponse();
		String email = userData.getEmail();
		User user = userService.getUserByEmail(email);

		if (user != null) {
			String token = tokenService.generateToken(email, user.getUserId());
			String url = url = "http://localhost:8080/ToDo/#!/resetpassword/" + token;
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("to", user.getEmail());
			map.put("message", url);
			producer.send(map);
			customResponse.setMessage("Mail send");
			return ResponseEntity.ok(customResponse);
		} 
		else {
			customResponse.setMessage("User Does not exist");
			return ResponseEntity.ok(customResponse);
		}
	}

	@RequestMapping(value = "/resetpassword/{token:.+}", method = RequestMethod.POST)
	public ResponseEntity<CustomResponse> resetPassword(@PathVariable("token") String token, HttpServletRequest request,
			HttpServletResponse response, @RequestBody User userData) throws IOException {
		/*
		 * String headerToken=request.getHeader("pwtoken");
		 * if(headerToken!=null){
		 */
		 CustomResponse customResponse=new CustomResponse();
		 
		int userId = tokenService.verifyToken(token);

		if (userId > 0) {
			User user = userService.getUserById(userId);
			String password = userData.getPassword();
			if (password.length() >= 8) {
				password = passwordChecker.encodePassword(password);
				user.setPassword(password);
				if (userService.updateUser(user)) {
					customResponse.setMessage("password changed");
					return ResponseEntity.ok(customResponse);
				} else {
					customResponse.setMessage("Some problem occured");
					return ResponseEntity.status(HttpStatus.CONFLICT).body(customResponse);
				}
			} else {
				customResponse.setMessage("password is short");
				return ResponseEntity.status(HttpStatus.CONFLICT).body(customResponse);
			}

		} else {
			customResponse.setMessage("Invalid token");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(customResponse);
		}
		/*
		 * }else{ if(tokenService.verifyToken(token)>0){
		 * response.setHeader("pwtoken", token);
		 * //response.sendRedirect("www.google.com"); return
		 * ResponseEntity.ok("User redirect"); }else{ return
		 * ResponseEntity.ok("Invalid token"); } }
		 */

	}
	
	@RequestMapping(value = "/changeprofilePic", method = RequestMethod.POST)
	public ResponseEntity<CustomResponse> updateUser(@RequestBody User user,HttpServletRequest request){
		CustomResponse response=new CustomResponse();
		String token =request.getHeader("Authorization");
		User oldUser=userService.getUserById(tokenService.verifyToken(token));
		if(oldUser!=null && user!=null){
			oldUser.setProfileUrl(user.getProfileUrl());
			if(userService.updateUser(oldUser)){
				response.setMessage("Profile pic changed");
				return ResponseEntity.ok(response);
			}else{
				response.setMessage("Some error occurred");
				return ResponseEntity.ok(response);
			}
		}else{
			response.setMessage("Token expired");
			return ResponseEntity.ok(response);
		}
		
	}
	
	@RequestMapping(value = "/addLabelInUser", method = RequestMethod.POST)
	public ResponseEntity<CustomResponse> addLabel(@RequestBody Label label,HttpServletRequest request){
		CustomResponse response=new CustomResponse();
		String token =request.getHeader("Authorization");
		User user=userService.getUserById(tokenService.verifyToken(token));
		if(user!=null){
			
			Set<Label> labels=userService.getAllLabels(user.getUserId());
			if(labels!=null){
		    	Iterator<Label> itr = labels.iterator();
			    while(itr.hasNext())
			     {
			    	System.out.println("inside");
			      	Label oldLabel=(Label) itr.next();
		         	if(oldLabel.getLabelName().equals(label.getLabelName())){
		     	    	response.setMessage("label already exist");
					   return ResponseEntity.ok(response); 
		         	}
			     }
				}
			
			label.setUser(user);
			int id=userService.addLabel(label);
			if(id>0){
				response.setMessage("Label added");
				return ResponseEntity.ok(response);
			}else{
				 response.setMessage("Problem occured");
				 return ResponseEntity.ok(response);
			}
		}else{
			response.setMessage("Token expired");
			 return ResponseEntity.ok(response);
		  
		}
	}
	
	@RequestMapping(value = "/deleteLabel", method = RequestMethod.POST)
	public ResponseEntity<CustomResponse> deleteLabel(@RequestBody Label label,HttpServletRequest request){
		CustomResponse response=new CustomResponse();
		String token =request.getHeader("Authorization");
		User user=userService.getUserById(tokenService.verifyToken(token));
		if(user!=null){
			userService.deleteLable(label);
			response.setMessage("Label added");
			return ResponseEntity.ok(response);
		}else{
		   response.setMessage("Problem occured");
		   return ResponseEntity.ok(response);
		}
	}
	
	@RequestMapping(value = "/updateLabel", method = RequestMethod.POST)
	public ResponseEntity<CustomResponse> update(@RequestBody Label label,HttpServletRequest request){
		CustomResponse response=new CustomResponse();
		String token =request.getHeader("Authorization");
		User user=userService.getUserById(tokenService.verifyToken(token));
		if(user!=null){
			userService.deleteLable(label);
			response.setMessage("Label added");
			return ResponseEntity.ok(response);
		}else{
		   response.setMessage("Problem occured");
		   return ResponseEntity.ok(response);
		}
	}
	
	
	
}
