package com.nl.lovely.service.imp;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nl.lovely.entity.User;
import com.nl.lovely.controller.AuthResponse;
import com.nl.lovely.controller.LoginRequest;
import com.nl.lovely.controller.RegisterRequest;
import com.nl.lovely.enums.RoleType;
import com.nl.lovely.repository.UserRepository;
import com.nl.lovely.service.AuthService;
import com.nl.lovely.service.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {

	@Autowired
	private final UserRepository	userRepository;
	
	@Autowired
	private final JwtService jwtService;
	
	private final PasswordEncoder passwordEncoder;
	
	private final AuthenticationManager authenticationManager;
	
	@Override
	public AuthResponse login(LoginRequest request) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user=userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token=jwtService.getToken(user);
        return AuthResponse.builder()
            .token(token)
            .build();

	}

	@Override
	public AuthResponse register(RegisterRequest request) {
		// Construir el objeto User con la imagen y otros datos
		User user = User.builder()
		        .username(request.getUsername())
		        .password(passwordEncoder.encode(request.getPassword()))
		        .lastname(request.getLastname())
		        .name(request.getName())
		        .loggedIn(false)
		        .role(RoleType.USER)
		        .build();

		// Guardar el usuario en la base de datos
		userRepository.save(user);

		// Crear y devolver la respuesta de autenticación
		return AuthResponse.builder()
		        .token(jwtService.getToken(user))
		        .build();
	}

}
