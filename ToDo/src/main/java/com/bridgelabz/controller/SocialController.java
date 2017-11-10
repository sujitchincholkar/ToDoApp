package com.bridgelabz.controller;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.dao.UserDao;
import com.bridgelabz.model.User;
import com.bridgelabz.service.TokenService;
import com.bridgelabz.service.UserService;
import com.bridgelabz.social.FBConnnection;
import com.bridgelabz.social.GoogleConnection;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
public class SocialController {
	/*@Autowired
	TokenService tokenservice;*/
	@Autowired
	GoogleConnection googleConnection;
	@Autowired
	FBConnnection fbConnection;
	@Autowired
	UserService userService;
	@Autowired
	TokenService tokenService;
	@RequestMapping(value = "/loginWithGoogle")
	public void googleConnection(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println(" in googleLoginURL  ");
		//String unid = UUID.randomUUID().toString();
		
		//request.getSession().setAttribute("STATE", unid);
		String googleLoginURL = googleConnection.getURI();
		System.out.println("googleLoginURL  " + googleLoginURL);
		response.sendRedirect(googleLoginURL);
	}
	@RequestMapping(value = "/googlelogin")
	public void connectGoogle(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException{
		String error = request.getParameter("error");
		System.out.println(error);
		String code = request.getParameter("code");
		System.out.println(code);
		String googleAccessToken = googleConnection.getAccessToken(code);
		System.out.println(googleAccessToken);
		JsonNode profileData=googleConnection.getUserProfile(googleAccessToken);
		User user=new User();
		if(profileData.get("displayName").asText()!=null){
			user.setFullName(profileData.get("displayName").asText());
			user.setActivated(true);
			user.setEmail(profileData.get("emails").get(0).get("value").asText());
			user.setProfileUrl(profileData.get("image").get("url").asText());
			User existingUser=userService.getUserByEmail(user.getEmail());
			if(existingUser==null){
				int id=userService.saveUserData(user);
					if(id>0){
						String token=tokenService.generateToken("", id);
						response.setHeader("Authorization", token);
						response.sendRedirect("/home.html");
					}else{
						response.sendRedirect("/");
						request.getRequestDispatcher("home.html").
						forward(request, response);
					}
			}else{
				String token=tokenService.generateToken("",existingUser.getUserId());
				response.setHeader("Authorization",token);
				response.sendRedirect("/home.html");
			}
		}else{
			 System.out.println("data is not received");
			}
		
	}
	@RequestMapping(value = "/loginWithFB")
	public void facebookConnection(HttpServletRequest request, HttpServletResponse response) throws IOException {
	
		String fbLoginURL = fbConnection.getURI();
		System.out.println("fbLoginURL  " + fbLoginURL);
		response.sendRedirect(fbLoginURL);
	}
	@RequestMapping(value = "/connectFB")
	public void connectFacebook(HttpServletRequest request,HttpServletResponse response){
		String error = request.getParameter("error");
		System.out.println(error);
		String code = request.getParameter("code");
		String fbAccessToken = fbConnection.getAccessToken(code);
		System.out.println(fbAccessToken);
		JsonNode profileData=fbConnection.getUserProfile(fbAccessToken);
		User user=new User();
		user.setFullName(profileData.get("name").asText());
		profileData.get("email").asText();
		String profileImage=profileData.get("picture").get("data").get("url").asText();
		System.out.println(	profileData.get("email").asText());
		System.out.println(profileImage);
	}
	
}
