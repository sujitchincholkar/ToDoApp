package com.bridgelabz.service;

public interface TokenService {
	public String generateToken(String email,int id);
	public int verifyToken(String token);
}
