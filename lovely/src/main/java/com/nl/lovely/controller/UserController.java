package com.nl.lovely.controller;

import java.io.IOException;
<<<<<<< HEAD
import java.util.HashMap;
=======
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
import java.util.List;
import java.util.Map;
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

<<<<<<< HEAD
import com.nl.lovely.dto.UserCompleteDTO;
=======
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
import com.nl.lovely.dto.UserDTO;
import com.nl.lovely.dto.UserProfileDTO;
import com.nl.lovely.entity.User;
import com.nl.lovely.entity.UserProfile;
<<<<<<< HEAD
import com.nl.lovely.repository.UserRepository;
import com.nl.lovely.request.RegisterRequest;
import com.nl.lovely.request.UserProfileCompleteRequest;
=======
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
import com.nl.lovely.request.UserProfilePhotoRequest;
import com.nl.lovely.request.UserProfileRequest;
import com.nl.lovely.request.UserRequest;
import com.nl.lovely.response.AuthResponse;
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
	
<<<<<<< HEAD
	@Autowired
    private UserRepository userRepository;

    @GetMapping("/checkUsername")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam String username) {
    	 System.out.println("Buscando usuario: '" + username + "'");
        boolean exists = userRepository.findByUsername(username).isPresent();
        System.out.println("Usuario encontrado: " + exists);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
    
	// API para obtener el usuario logueado actual.
=======
	
	// Metodo para obtener el usuario logueado actual
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
    @GetMapping("/current")
	public UserDetails getCurrentUser() {
	        // Obtener los detalles del usuario actualmente autenticado
	        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
<<<<<<< HEAD
   // API para obtener el usuario by ID por paramaetro.
=======
   // Metodo para obtener el usuario by id por paramaetro
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
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
	
<<<<<<< HEAD
	// API para obtener todos los usuarios con los que hizo match por id especifico.
=======
	// Metodo para obtener todos los usuarios de los matchs de un usuario en especifico
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
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
	
<<<<<<< HEAD
	// API para obtener un UserProfile by ID.
=======
	// Metodo para obtener un userProfile by Id
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
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
	
<<<<<<< HEAD
	// API para obtener un UserProfile by UserId logueado actualemtne.
=======
	// Metodo para obtener un userProfile by Id de user logueado actualemtne
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
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
<<<<<<< HEAD
	
	// API para actualizar los datos del usuario.
=======

>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
    @PutMapping("/updateUser")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest userRequest)
    {
        return ResponseEntity.ok(userService.updateUser(userRequest));
    }
<<<<<<< HEAD
    
    // API que registra un nuevo profile.
    @PostMapping(value="registration-profile/{userId}")
    public ResponseEntity<?> registerProfile(@RequestParam("photo") MultipartFile photo,
            @RequestParam("photoFileName") String photoFileName,
            @RequestParam("location") String location,
            @RequestParam("gender") String gender,
            @RequestParam("age") String age,
            @RequestParam("likeGender") String likeGender,
            @RequestParam("maxAge") Integer maxAge,
            @RequestParam("minAge") Integer minAge,
            @PathVariable Long userId) {
			UserProfileResponse response = userProfileService.saveProfile(photo, photoFileName, location, gender, age, likeGender, maxAge, minAge, userId);
			return ResponseEntity.ok(response);
}
    
    
    /*public ResponseEntity<UserProfileResponse> registerProfile(@RequestBody UserProfileDTO req, @PathVariable Long userId)
    {	
    		UserProfileResponse response = userProfileService.saveProfile(req, userId);
    		return ResponseEntity.ok()
    							 .body(new UserProfileResponse("Profile saved successfully"));
    }*/
    
    
     // API para actualizar los datos del perfil.
=======
    
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
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
<<<<<<< HEAD
=======
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
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
    }
    
    // API para actualizar la foto de perfil del usuario.
    @PutMapping(value="/updateProfilePhoto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserProfileResponse> updateUserProfilePhoto(@RequestPart UserProfilePhotoRequest req, @RequestPart(value = "photoFile", required = false) MultipartFile photoFile) {
        // Tu lógica existente para cargar el archivo y actualizar el perfil
    
<<<<<<< HEAD
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
    
    // API para obtener perfiles de usuario aletorios, una lista.
=======
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
    @GetMapping("/random-users")
    public ResponseEntity<List<UserProfileDTO>> getRandomUser() {
    	List<UserProfileDTO> randomUser = userProfileService.getRandomProfiles(3);
        return ResponseEntity.ok(randomUser);
    }
<<<<<<< HEAD
    // API para obtener perfil de usuario aleatorio.
=======
    
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
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
    
<<<<<<< HEAD
    // Metodo para obtener el id del user logueado.
=======
    
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
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
    
<<<<<<< HEAD
    // API para eliminar un usuario.
=======
    
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
<<<<<<< HEAD
    // prueba
    @GetMapping("/complete/{id}")
    public ResponseEntity <UserCompleteDTO> obtenerUser() throws Exception {
    	UserCompleteDTO dto = userService.getUserDTO(getCurrentUserId());
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
=======
    
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
}
