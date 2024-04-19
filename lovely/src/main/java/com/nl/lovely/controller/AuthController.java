package com.nl.lovely.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.nl.lovely.entity.User;
import com.nl.lovely.repository.UserRepository;
import com.nl.lovely.request.LoginRequest;
import com.nl.lovely.request.RegisterRequest;
import com.nl.lovely.response.AuthResponse;
import com.nl.lovely.service.AuthService;
import com.nl.lovely.service.UserService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:4200") // Reemplaza esto con el dominio de tu frontend
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	
    @Autowired
    private AuthService authService;
    
    @Autowired
    private UserRepository userRepository;
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
    	return ResponseEntity.ok(authService.login(request));
    }
	 
    @PostMapping(value="registration-user")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request)
    {	if (userRepository.existsByUsername(request.getUsername())) {
	        // El nombre de usuario ya está en uso, devolver un mensaje de error
	        return ResponseEntity.badRequest().body(new AuthResponse("El nombre de usuario ya está en uso"));
	    } else {
	    	return ResponseEntity.ok(authService.register(request));
    	   }
    }
  
}
