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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GoogleConnection {
	public static final String CLIENT_Id = "163222559830-8ngujo28m9q6lrc7coduu568017i47lg.apps.googleusercontent.com";
	public static final String Secret_Id = "fo6-1nItfoWZ5xeLz4UVVFeS";
	public static final String Redirect_URI = "http://localhost:8080/ToDo/googlelogin";
	public String Gmail_GET_USER_URL = "https://www.googleapis.com/plus/v1/people/me";
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
	public JsonNode getUserProfile(String accessToken) {

		System.out.println("gmail details " + Gmail_GET_USER_URL);
		ResteasyClient restCall = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = restCall.target(Gmail_GET_USER_URL);

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
