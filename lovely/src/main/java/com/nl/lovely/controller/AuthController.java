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
import com.nl.lovely.service.AuthService;
import com.nl.lovely.service.UserService;

import lombok.RequiredArgsConstructor;

//@CrossOrigin(origins = "http://localhost:4200") // Reemplaza esto con el dominio de tu frontend
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
    	return ResponseEntity.ok(authService.login(request));
    }
	 
    @PostMapping(value="registration-user")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request)
    {
    
        return ResponseEntity.ok(authService.register(request));
    }
    

    
    /*
     * @PostMapping(value="registration-user")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request,@RequestPart("imageFile") MultipartFile imageFile, @RequestPart("user") User user)
    {
    	/*try {
            if (imageFile != null && !imageFile.isEmpty()) {
                user.setFotoPerfil(imageFile.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Manejar el error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        //User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(authService.register(request));
    }
    
     * 
     * @PostMapping(value = "/registration-user", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> createUser(@RequestPart("imageFile") MultipartFile imageFile, @RequestPart("user") User user) {
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                user.setFotoPerfil(imageFile.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Manejar el error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }*/
	   
  	
}
