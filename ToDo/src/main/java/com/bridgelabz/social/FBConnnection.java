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

public class FBConnnection {
	
	@Value("${facebook.AppId}")
	public  String APP_ID;
	
	@Value("${facebook.secret_Id}")
	public  String Secret_Id;
	
	public static final String Redirect_URI = "http://localhost:8080/ToDo/connectFB";
	
	/**
	 * @return
	 */
	public String getURI(){
		String facebookLoginURL="";
		try {
			facebookLoginURL = "http://www.facebook.com/dialog/oauth?" + "client_id=" + APP_ID + "&redirect_uri="
					+ URLEncoder.encode(Redirect_URI, "UTF-8") + "&state=123&response_type=code"
					+ "&scope=public_profile,email";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return facebookLoginURL;
	}
	
	/**
	 * @param code
	 * @return
	 */
	public String getAccessToken(String code) {
		String accessTokenURL="";
		try {
			accessTokenURL = "https://graph.facebook.com/v2.9/oauth/access_token?" + "client_id=" + APP_ID
				+ "&redirect_uri=" + URLEncoder.encode(Redirect_URI, "UTF-8") + "&client_secret=" + Secret_Id + "&code="
				+ code;
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ResteasyClient restCall = new ResteasyClientBuilder().build();

		ResteasyWebTarget target = restCall.target(accessTokenURL);

		Form form = new Form();
		form.param("client_id", APP_ID);
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
		} catch (IOException e) {
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

		String fbgetUserURL = "https://graph.facebook.com/v2.9/me?access_token=" + accessToken
				+ "&fields=id,name,email,picture";
		ResteasyClient restCall = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = restCall.target(fbgetUserURL);

		String headerAuth = "Bearer " + accessToken;
		Response response = target.request().header("Authorization", headerAuth)
				.accept(MediaType.APPLICATION_JSON)
				.get();

		
		String profile=response.readEntity(String.class);
		ObjectMapper mapper=new ObjectMapper();
		JsonNode fbProfile = null;
		try {
			fbProfile = mapper.readTree(profile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		restCall.close();
		return fbProfile;
	}
}
