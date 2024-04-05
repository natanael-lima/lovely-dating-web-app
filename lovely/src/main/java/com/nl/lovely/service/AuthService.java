package com.nl.lovely.service;

import org.springframework.web.multipart.MultipartFile;

import com.nl.lovely.request.LoginRequest;
import com.nl.lovely.request.RegisterRequest;
import com.nl.lovely.response.AuthResponse;

public interface AuthService {
	
	public AuthResponse login (LoginRequest request);
	
	public AuthResponse register(RegisterRequest request);
}
