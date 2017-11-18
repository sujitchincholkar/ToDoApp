package com.bridgelabz.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
				return new ResponseEntity<CustomResponse>(customResponse, HttpStatus.CONFLICT);
			}
		} 
		else {
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

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ResponseEntity logout(HttpSession session) {
		session.removeAttribute(session.getId());

		return ResponseEntity.ok("Logged out");
	}

	@RequestMapping(value = "/forgetpassword", method = RequestMethod.POST)
	public ResponseEntity<String> forgetPassword(@RequestBody User userData, HttpServletRequest request,
			HttpServletResponse response) {

		String email = userData.getEmail();
		User user = userService.getUserByEmail(email);

		if (user != null) {
			String token = tokenService.generateToken(email, user.getUserId());
			String url = url = "http://localhost:8080/ToDo/resetpassword/" + token;
			HashMap map = new HashMap();
			map.put("to", user.getEmail());
			map.put("message", url);
			producer.send(map);
			return ResponseEntity.ok("Redirect");
		} 
		else {
			return ResponseEntity.ok("User Does not exist");
		}
	}

	@RequestMapping(value = "/resetpassword/{token:.+}", method = RequestMethod.POST)
	public ResponseEntity<String> resetPassword(@PathVariable("token") String token, HttpServletRequest request,
			HttpServletResponse response, @RequestBody User userData) throws IOException {
		/*
		 * String headerToken=request.getHeader("pwtoken");
		 * if(headerToken!=null){
		 */
		int userId = tokenService.verifyToken(token);

		if (userId > 0) {
			User user = userService.getUserById(userId);
			String password = userData.getPassword();
			if (password.length() >= 8) {
				password = passwordChecker.encodePassword(password);
				user.setPassword(password);
				if (userService.updateUser(user)) {
					return ResponseEntity.ok("password changed");
				} else {
					return ResponseEntity.ok("Some problem occured");
				}
			} else {
				return ResponseEntity.ok("password is short");
			}

		} else {
			return ResponseEntity.ok("Invalid token");
		}
		/*
		 * }else{ if(tokenService.verifyToken(token)>0){
		 * response.setHeader("pwtoken", token);
		 * //response.sendRedirect("www.google.com"); return
		 * ResponseEntity.ok("User redirect"); }else{ return
		 * ResponseEntity.ok("Invalid token"); } }
		 */

	}

	@RequestMapping(value = "/isactive", method = RequestMethod.GET)
	public ResponseEntity<String> isActivated(HttpSession session) {

		User user = (User) session.getAttribute(session.getId());

		if (user != null)
			return ResponseEntity.ok().body(user.getFullName());
		return ResponseEntity.ok("user is not logged in");
	}
}
