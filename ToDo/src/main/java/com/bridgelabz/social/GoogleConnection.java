package com.bridgelabz.social;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GoogleConnection 
{	
	@Value("${google.clientId}")
	public   String CLIENT_Id ;
	@Value("${google.secret_Id}")
	public   String Secret_Id ;
	
	public static final String Redirect_URI = "http://localhost:8080/ToDo/googlelogin";
	public String Gmail_GET_USER_URL = "https://www.googleapis.com/plus/v1/people/me";

	/**
	 * @return
	 */
	public String getURI(){
		String googleLoginURL="";
		try {
		googleLoginURL = "https://accounts.google.com/o/oauth2/auth?client_id=" + CLIENT_Id + "&redirect_uri="
					+ URLEncoder.encode(Redirect_URI, "UTF-8") + "&state=123&response_type=code&scope=profile email&approval_prompt=force&access_type=offline";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return googleLoginURL;
	}
	
	/**
	 * @param code
	 * @return
	 */
	public String getAccessToken(String code) {
		String accessTokenURL = "https://accounts.google.com/o/oauth2/token";

		ResteasyClient restCall = new ResteasyClientBuilder().build();

		ResteasyWebTarget target = restCall.target(accessTokenURL);

		Form form = new Form();
		form.param("client_id", CLIENT_Id);
		form.param("client_secret", Secret_Id);
		form.param("redirect_uri", Redirect_URI);
		form.param("code", code);
		form.param("grant_type", "authorization_code");
		Response response = target.request().accept(MediaType.APPLICATION_JSON).post(Entity.form(form));

		String token = response.readEntity(String.class);
		System.out.println(token);
		ObjectMapper mapper=new ObjectMapper();
		String accessToken = null;
		try {
			accessToken = mapper.readTree(token).get("access_token").asText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		restCall.close();
		return accessToken;
	}
	
	/**
	 * @param accessToken
	 * @return
	 */
	public JsonNode getUserProfile(String accessToken) {

		System.out.println("gmail details " + Gmail_GET_USER_URL);
		ResteasyClient restCall = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = restCall.target(Gmail_GET_USER_URL);
		System.out.println(accessToken);
		String headerAuth = "Bearer " + accessToken;
		Response response = target.request().header("Authorization", headerAuth)
				.accept(MediaType.APPLICATION_JSON)
				.get();

		String profile=response.readEntity(String.class);
		ObjectMapper mapper=new ObjectMapper();
		JsonNode googleprofile = null;
		try {
			googleprofile = mapper.readTree(profile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		restCall.close();
		return googleprofile;
	}
	
}
