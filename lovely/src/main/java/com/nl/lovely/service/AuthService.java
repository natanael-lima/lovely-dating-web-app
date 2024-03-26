package com.nl.lovely.service;

import org.springframework.web.multipart.MultipartFile;

import com.nl.lovely.controller.AuthResponse;
import com.nl.lovely.controller.LoginRequest;
import com.nl.lovely.controller.RegisterRequest;

public interface AuthService {
	
	public AuthResponse login (LoginRequest request);
	
	public AuthResponse register(RegisterRequest request);
}
