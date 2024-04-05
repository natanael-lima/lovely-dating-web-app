package com.nl.lovely.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nl.lovely.dto.UserDTO;
import com.nl.lovely.dto.UserProfileDTO;
import com.nl.lovely.entity.User;
import com.nl.lovely.request.UserProfileRequest;
import com.nl.lovely.request.UserRequest;
import com.nl.lovely.response.UserProfileResponse;
import com.nl.lovely.response.UserResponse;
import com.nl.lovely.service.UserProfileService;
import com.nl.lovely.service.UserService;


@CrossOrigin(origins = "http://localhost:4200") // Reemplaza esto con el dominio de tu frontend
@RestController
@RequestMapping("/api/user")
public class UserController {
	@Autowired
    private UserService userService;
	@Autowired
    private UserProfileService userProfileService;

	
    @GetMapping("/current")
	public UserDetails getCurrentUser() {
	        // Obtener los detalles del usuario actualmente autenticado
	        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	
	@GetMapping(value = "{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id)
    {
        UserDTO userDTO = userService.getUser(id);
        if (userDTO==null)
        {
           return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userDTO);
    }
	@GetMapping(value = "/profile/{id}")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable Long id)
    {
        UserProfileDTO userProfileDTO = userProfileService.getUserProfile(id);
        if (userProfileDTO==null)
        {
           return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userProfileDTO);
    }

    @PutMapping("/updateUser")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest userRequest)
    {
        return ResponseEntity.ok(userService.updateUser(userRequest));
    }
	
    /*@PutMapping("/updateProfile")
    public ResponseEntity<UserProfileResponse> updateUserProfile(@RequestBody UserProfileRequest req, @AuthenticationPrincipal UserDetails userDetails)
    {	// Verificar si el usuario está autenticado
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // Obtener el ID del usuario logueado
        Long userId = ((UserDetailsImpl) userDetails).getId();
        // Actualizar el perfil de usuario asociado al usuario logueado
        UserProfileResponse response = userProfileService.updateUserProfile(userId, req);
        
        // Devolver la respuesta
        return ResponseEntity.ok(response);
        //return ResponseEntity.ok(userProfileService.updateUserProfile(req));
    }*/
    
    @PutMapping("/updateProfile2")
    public ResponseEntity<UserProfileResponse> updateUserProfile2(@RequestBody UserProfileRequest req)
    {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> userOptional = userService.findByUsername(username);
        User authenticatedUser = userOptional.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

     // Obtener el ID del usuario autenticado
        Long authenticatedUserId = authenticatedUser.getId();
        System.out.println("ID del usuario autenticado: " + authenticatedUserId);

        // Obtener el ID del usuario asociado al UserProfile que se está intentando actualizar
        Long profileUserId = req.getUserId();
        System.out.println("ID del usuario asociado al UserProfile: " + profileUserId);
       
        // Verificar si el ID del usuario autenticado coincide con el ID del usuario asociado al UserProfile
        if (!authenticatedUserId.equals(profileUserId)) {
            // Si los IDs no coinciden, devolvemos un mensaje de error indicando que el usuario no tiene permiso para actualizar el perfil de otro usuario
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UserProfileResponse("No tienes permiso para actualizar el perfil de otro usuario"));
        }

        // Si los IDs coinciden, entonces el usuario autenticado tiene permiso para actualizar su propio perfil
        return ResponseEntity.ok(userProfileService.updateUserProfile(req));
    }
    
    
    
	
	@PostMapping("/demo")
    public String welcome() {
        // Lógica para verificar las credenciales y establecer el estado de inicio de sesión
        //user.setLoggedIn(true);
        // Devuelve una respuesta adecuada para Angular
        return "Welcome user";
    }
    
  	
	
    @GetMapping("/random-user")
    public ResponseEntity<User> getRandomUser() {
        User randomUser = userService.getRandomProfile();
        return ResponseEntity.ok(randomUser);
    }
    
    @GetMapping("/random")
    public List<User> getRandomUsers() {
        return userService.getRandomProfiles(3); // Por ejemplo, obtenemos 10 perfiles aleatorios
    }
    
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

}
