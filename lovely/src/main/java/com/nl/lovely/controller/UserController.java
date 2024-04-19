package com.nl.lovely.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nl.lovely.dto.UserDTO;
import com.nl.lovely.dto.UserProfileDTO;
import com.nl.lovely.entity.User;
import com.nl.lovely.entity.UserProfile;
import com.nl.lovely.request.UserProfilePhotoRequest;
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
	
	
	// Metodo para obtener el usuario logueado actual
    @GetMapping("/current")
	public UserDetails getCurrentUser() {
	        // Obtener los detalles del usuario actualmente autenticado
	        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
   // Metodo para obtener el usuario by id por paramaetro
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
	
	// Metodo para obtener todos los usuarios de los matchs de un usuario en especifico
	@GetMapping(value = "/all/{id}")
    public ResponseEntity<List<UserDTO>> getUsersByMatch(@PathVariable Long id)
    {
		List<UserDTO> lista = userService.getUsersByMatch(id);
        if (lista.isEmpty())
        {
           return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lista);
    }
	
	// Metodo para obtener un userProfile by Id
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
	
	// Metodo para obtener un userProfile by Id de user logueado actualemtne
	@GetMapping(value = "/currentProfile/{id}")
    public ResponseEntity<UserProfileDTO> getIdByProfile(@PathVariable Long id)
    {
		Long idUser = userProfileService.getUserProfileByUserId(id);
		UserProfileDTO userProfileDTO = userProfileService.getUserProfile(idUser);
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
    
    @PutMapping("/updateProfile")
    public ResponseEntity<UserProfileResponse> updateUserProfileData(@RequestBody UserProfileRequest req)
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
        return ResponseEntity.ok(userProfileService.updateUserProfileData(req));
    }
    
    //, consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    @PutMapping(value="/updateProfilePhoto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserProfileResponse> updateUserProfilePhoto(@RequestPart UserProfilePhotoRequest req, @RequestPart(value = "photoFile", required = false) MultipartFile photoFile) {
        // Tu lógica existente para cargar el archivo y actualizar el perfil
    
        if (!photoFile.isEmpty()) {
            try {
                // Obtener los bytes del archivo
                byte[] pdfBytes = photoFile.getBytes();
                // Guardar el contenido del archivo
                req.setPhoto(pdfBytes);
                // Establecer el nombre del archivo
                req.setPhotoFileName(photoFile.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
                // Manejar el error, por ejemplo, redirigiendo a una página de error
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UserProfileResponse("No se puedo actualizar la img"));
            }
        }
        return ResponseEntity.ok(userProfileService.updateUserProfilePhoto(req));
    }
    
    
    @GetMapping("/random-users")
    public ResponseEntity<List<UserProfileDTO>> getRandomUser() {
    	List<UserProfileDTO> randomUser = userProfileService.getRandomProfiles(3);
        return ResponseEntity.ok(randomUser);
    }
    
    @GetMapping("/random-user")
    public ResponseEntity<UserProfileDTO> getRandomUsers() {
    	Long userId = getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserProfileDTO randomProfile = userProfileService.getRandomProfile(userId);
        if (randomProfile != null) {
            return ResponseEntity.ok(randomProfile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findByUsername(username).orElse(null);
            if (user != null) {
            	 System.out.println("id de user: "+user.getId());
                return user.getId();
               
            }
        }
        return null;
    }
    
    
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
    
}
