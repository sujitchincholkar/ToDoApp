package com.bridgelabz.controller;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.dao.UserDao;
import com.bridgelabz.model.CustomResponse;
import com.bridgelabz.model.User;
import com.bridgelabz.service.TokenService;
import com.bridgelabz.service.UserService;
import com.bridgelabz.social.FBConnnection;
import com.bridgelabz.social.GoogleConnection;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
public class SocialController {

	@Autowired
	private GoogleConnection googleConnection;
	
	@Autowired
	private FBConnnection fbConnection;

	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenService tokenService;

	@RequestMapping(value = "/loginWithGoogle")
	public void googleConnection(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String googleLoginURL = googleConnection.getURI();
		response.sendRedirect(googleLoginURL);
	}
	
	@RequestMapping(value = "/googlelogin")
	public void connectGoogle(HttpServletRequest request,HttpServletResponse response,HttpSession session)
			throws IOException, ServletException{
		
		String error = request.getParameter("error");
	
		if(error!=null){
			response.sendRedirect("Login");
		}
		
		String code = request.getParameter("code");
		String googleAccessToken = googleConnection.getAccessToken(code);
	
		JsonNode profileData=googleConnection.getUserProfile(googleAccessToken);
		User user=new User();
		
		if(profileData.get("displayName")!=null){
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
						
						session.setAttribute("token", token);
						
						response.sendRedirect("http://localhost:8080/ToDo/#!/dummy");
					}
					else{
						response.sendRedirect("http://localhost:8080/ToDo/#!/home");

					}
			}
			else{
				String token=tokenService.generateToken("",existingUser.getUserId());
				
				URL url=new URL(request.getRequestURL()+"");
				System.out.println(url.getHost()+url.getPort()+url.getPath());
				session.setAttribute("token", token);
				response.sendRedirect("http://localhost:8080/ToDo/#!/dummy");
			}
		}
		else{
				
			}
		
	}
	@RequestMapping(value = "/loginWithFB")
	public void facebookConnection(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
	
		String fbLoginURL = fbConnection.getURI();
		System.out.println("fbLoginURL  " + fbLoginURL);
		response.sendRedirect(fbLoginURL);
	}
	
	@RequestMapping(value = "/connectFB")
	public void connectFacebook(HttpServletRequest request,HttpServletResponse response,HttpSession session) throws IOException, ServletException{
		
		String error = request.getParameter("error");
		
		String code = request.getParameter("code");
		String fbAccessToken = fbConnection.getAccessToken(code);
		
		JsonNode profileData=fbConnection.getUserProfile(fbAccessToken);
		
		User user=new User();
		if(profileData.get("name")!=null)
		{
			user.setFullName(profileData.get("name").asText());
			user.setActivated(true);
			user.setEmail(profileData.get("email").asText());
			user.setProfileUrl(profileData.get("picture").get("data").get("url").asText());
			
			User existingUser=userService.getUserByEmail(user.getEmail());
			
			if(existingUser==null){
				int id=userService.saveUserData(user);
					
					if(id>0){
						String token=tokenService.generateToken("", id);
					
						response.setHeader("Authorization", token);
						session.setAttribute("token", token);
						response.sendRedirect("http://localhost:8080/ToDo/#!/dummy");
					}
					else{
					
						response.sendRedirect("http://localhost:8080/ToDo/#!/login");
					}
			}
			else{
				String token=tokenService.generateToken("",existingUser.getUserId());
			
				session.setAttribute("token", token);
				response.sendRedirect("http://localhost:8080/ToDo/#!/dummy");
			}
		}
		else{
			 System.out.println("data is not received");
		}
	}
	
	@RequestMapping(value="/gettoken")
	public ResponseEntity<CustomResponse> getToken(HttpSession session){
		CustomResponse response=new CustomResponse();
		response.setMessage((String) session.getAttribute("token"));
		session.removeAttribute("token");
		return  ResponseEntity.ok(response);
	}
	
}
