package com.nl.lovely.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.nl.lovely.entity.User;
<<<<<<< HEAD
import com.nl.lovely.entity.UserProfile;
=======
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
import com.nl.lovely.repository.UserRepository;
import com.nl.lovely.request.LoginRequest;
import com.nl.lovely.request.RegisterRequest;
import com.nl.lovely.response.AuthResponse;
import com.nl.lovely.response.UserProfileResponse;
import com.nl.lovely.service.AuthService;
import com.nl.lovely.service.UserProfileService;
import com.nl.lovely.service.UserService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:4200") // Reemplaza esto con el dominio de tu frontend
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	
    @Autowired
    private AuthService authService;
<<<<<<< HEAD
    @Autowired
    private UserRepository userRepository;

    // API que autentica el usuario que se loguea.
=======
    
    @Autowired
    private UserRepository userRepository;
    
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
    	return ResponseEntity.ok(authService.login(request));
    }
<<<<<<< HEAD
    
    // API que registra un nuevo usuario.
    @PostMapping(value="registration-user", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AuthResponse> registerUser(@ModelAttribute RegisterRequest request, @RequestPart("photoFile") MultipartFile photoFile)
	{	
    	if (userRepository.existsByUsername(request.getUsername())) {
            // El nombre de usuario ya está en uso, devolver un mensaje de error
            return ResponseEntity.badRequest().body(new AuthResponse("El nombre de usuario ya está en uso"));
        } else {
            try {
                byte[] photoBytes = null;
                String photoFileName = null;

                if (!photoFile.isEmpty()) {
                    // Obtener los bytes del archivo
                    photoBytes = photoFile.getBytes();
                    // Establecer el nombre del archivo
                    photoFileName = photoFile.getOriginalFilename();
                }

                // Crear un objeto UserProfile con los datos del registro y la ruta/nombre de la imagen guardada
                UserProfile userProfile = UserProfile.builder()
                        .photo(photoBytes)
                        .photoFileName(photoFileName)
                        .location(request.getProfile().getLocation())
        	            .gender(request.getProfile().getGender())
        	            .age(request.getProfile().getAge())
        	            .likeGender(request.getProfile().getLikeGender())
        	            .maxAge(request.getProfile().getMaxAge())
        	            .minAge(request.getProfile().getMinAge())
        	            .build();

                // Llamar al método register del authService y pasarle el objeto RegisterRequest y el objeto UserProfile
                return ResponseEntity.ok(authService.register(request, userProfile));
            } catch (IOException e) {
                // Ocurrió un error al procesar la imagen de perfil
                return ResponseEntity.badRequest().body(new AuthResponse("Error al procesar la imagen de perfil"));
            }
        }
=======
	 
    @PostMapping(value="registration-user")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request)
    {	if (userRepository.existsByUsername(request.getUsername())) {
	        // El nombre de usuario ya está en uso, devolver un mensaje de error
	        return ResponseEntity.badRequest().body(new AuthResponse("El nombre de usuario ya está en uso"));
	    } else {
	    	return ResponseEntity.ok(authService.register(request));
    	   }
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
    }
    
    
    
    
    /*
    @PostMapping(value="registration-user")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody RegisterRequest request)
    {	if (userRepository.existsByUsername(request.getUsername())) {
	        // El nombre de usuario ya está en uso, devolver un mensaje de error
	        return ResponseEntity.badRequest().body(new AuthResponse("El nombre de usuario ya está en uso"));
	    } else {
	    	return ResponseEntity.ok(authService.register(request));
    	   }
    }*/
  
}
