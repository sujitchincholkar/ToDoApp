package com.bridgelabz.service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MissingClaimException;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenServiceImpl implements TokenService {
	private String key="mykey";
	public String generateToken(String email, int id) {
		String token="";
		try {
			long currentTime=System.currentTimeMillis();
			long expireTime=currentTime+(1000*60*60);
			Date date=new Date(currentTime);
			Date expireDate=new Date(expireTime);
		  
		    token =Jwts.builder().setId(String.valueOf(id))
                    .setIssuedAt(date)
                    .signWith(SignatureAlgorithm.HS256,key).
                    setExpiration(expireDate)
                    .compact();
		}  catch (JWTCreationException exception){
		    //Invalid Signing configuration / Couldn't convert Claims.
		}
		return token;
	}

	public int verifyToken(String token) {
		int id=0;
		try{ 
		   
		    Claims claims = Jwts.parser()         
		       .setSigningKey(key)
		       .parseClaimsJws(token).getBody();
		    
		    	id=Integer.parseInt(claims.getId());
		    	System.out.println(id);
		   
			} catch (MissingClaimException e) {
				e.printStackTrace();
			    return id;

			} catch (Exception e) {
				e.printStackTrace();
				 return id;
			}
			return id;
		}
}
	

