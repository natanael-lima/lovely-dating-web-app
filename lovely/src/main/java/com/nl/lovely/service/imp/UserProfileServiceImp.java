package com.nl.lovely.service.imp;

<<<<<<< HEAD
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
=======
import java.util.ArrayList;
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nl.lovely.dto.UserDTO;
import com.nl.lovely.dto.UserProfileDTO;
import com.nl.lovely.entity.User;
import com.nl.lovely.entity.UserProfile;
import com.nl.lovely.enums.ActionType;
import com.nl.lovely.enums.RoleType;
import com.nl.lovely.exception.NotFoundException;
import com.nl.lovely.repository.UserProfileRepository;
import com.nl.lovely.repository.UserRepository;
import com.nl.lovely.request.UserProfilePhotoRequest;
import com.nl.lovely.request.UserProfileRequest;
import com.nl.lovely.request.UserRequest;
import com.nl.lovely.response.UserProfileResponse;
import com.nl.lovely.response.UserResponse;
import com.nl.lovely.service.UserProfileService;
import com.nl.lovely.service.UserService;

import jakarta.transaction.Transactional;

@Service
public class UserProfileServiceImp implements UserProfileService {
	
	@Autowired
	private UserProfileRepository userProfileRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
    private UserService userService;
	
	@Transactional
	public UserProfileResponse updateUserProfileData(UserProfileRequest profileRequest) {
		// TODO Auto-generated method stub
		// Obtener el usuario asociado al perfil
	    Optional<User> userOptional = userService.findUserById(profileRequest.getUserId());
	    User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
	    
		UserProfile userProfile = UserProfile.builder()
				        .id(profileRequest.getId())
				        .user(user) // Establecer el usuario asociado al perfil
				        //.photo(profileRequest.getPhoto())
				        //.photoFileName(profileRequest.getPhotoFileName())
				        .location(profileRequest.getLocation())
				        .gender(profileRequest.getGender())
				        .age(profileRequest.getAge())
				        .likeGender(profileRequest.getLikeGender())
				        .maxAge(profileRequest.getMaxAge())
				        .minAge(profileRequest.getMinAge())
				        .build();
				        
		userProfileRepository.updateProfileData(userProfile.getId(), userProfile.getLocation(), userProfile.getGender(),userProfile.getAge(),userProfile.getLikeGender(),userProfile.getMaxAge(),userProfile.getMinAge());

		 return new UserProfileResponse("El perfil se actualizo satisfactoriamente");
	}
	
	@Transactional
	public UserProfileResponse updateUserProfilePhoto(UserProfilePhotoRequest profileRequest) {
		// TODO Auto-generated method stub
		// Obtener el usuario asociado al perfil
	    Optional<User> userOptional = userService.findUserById(profileRequest.getUserId());
	    User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
	    
		UserProfile userProfile = UserProfile.builder()
				        .id(profileRequest.getId())
				        .user(user) // Establecer el usuario asociado al perfil
				        .photo(profileRequest.getPhoto())
				        .photoFileName(profileRequest.getPhotoFileName())
				        .build();
				        
		userProfileRepository.updateProfileImage(userProfile.getId(), userProfile.getPhoto(),userProfile.getPhotoFileName());

<<<<<<< HEAD
		 return new UserProfileResponse("El imagen se actualizo satisfactoriamente");
=======
		 return new UserProfileResponse("El imagen se registró satisfactoriamente");
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
	}
	
	
	@Override
	public UserProfileDTO getUserProfile(Long id) {
		UserProfile userP= userProfileRepository.findById(id).orElse(null);
	       
        if (userP!=null)
        {
        	// Obtener el ID del usuario asociado al perfil
            Long userId = userP.getUser().getId();
            
        	UserProfileDTO userProfileDTO = UserProfileDTO.builder()
            .id(userP.getId())
            .userId(userId)
            .photo(userP.getPhoto())
            .photoFileName(userP.getPhotoFileName())
            .location(userP.getLocation())
            .gender(userP.getGender())
            .age(userP.getAge())
            .likeGender(userP.getLikeGender())
            .maxAge(userP.getMaxAge())
            .minAge(userP.getMinAge())
            .build();
            return userProfileDTO;
        }
		return null;   
	}

	@Override
	public Long  getUserProfileByUserId(Long userId) {
		// Buscar el UserProfile por el ID del usuario
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("UserProfile not found for userId: " + userId));

        // Mapear el UserProfile a un DTO para enviarlo al frontend
        return userProfile.getId();
	}
	
	@Override
	public void deleteUserProfile(Long id) {
		// TODO Auto-generated method stub
		userProfileRepository.deleteById(id);
	}


	@Override
	public UserProfile findUserById(Long id) {
		// TODO Auto-generated method stub
		return userProfileRepository.findById(id)
	            .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
	}

	@Override
	public UserProfile findByUsername(String username) {
		// TODO Auto-generated method stub
		return userProfileRepository.findByUsername(username);
	}

	@Override
	public List<UserProfileDTO> getRandomProfiles(int count) {
	    List<UserProfile> allUsers = userProfileRepository.findAll();
	    List<UserProfileDTO> userProfileDTOs = new ArrayList<>();

	    if (allUsers.size() <= count) {
	        // Si hay menos perfiles que el número deseado, devolvemos todos los perfiles
	        for (UserProfile userP : allUsers) {
	            UserProfileDTO userProfileDTO = convertToDTO(userP);
	            userProfileDTOs.add(userProfileDTO);
	        }
	    } else {
	        // Usamos una muestra aleatoria de la lista de perfiles
	        Random random = new Random();
	        Set<Integer> selectedIndexes = new HashSet<>();
	        while (selectedIndexes.size() < count) {
	            int randomIndex = random.nextInt(allUsers.size());
	            if (!selectedIndexes.contains(randomIndex)) {
	                selectedIndexes.add(randomIndex);
	                UserProfileDTO userProfileDTO = convertToDTO(allUsers.get(randomIndex));
	                userProfileDTOs.add(userProfileDTO);
	            }
	        }
	    }
	    return userProfileDTOs;
	}

	// Método para convertir UserProfile a UserProfileDTO
	private UserProfileDTO convertToDTO(UserProfile userP) {
	    Long userId = userP.getUser().getId();
	    return UserProfileDTO.builder()
	            .id(userP.getId())
	            .userId(userId)
	            .photo(userP.getPhoto())
	            .photoFileName(userP.getPhotoFileName())
	            .location(userP.getLocation())
	            .gender(userP.getGender())
	            .age(userP.getAge())
	            .likeGender(userP.getLikeGender())
	            .maxAge(userP.getMaxAge())
	            .minAge(userP.getMinAge())
	            .build();
	}
<<<<<<< HEAD
	
	// Método para convertir UserProfileDTO a UserProfile
	private UserProfile convertToEntity(UserProfileDTO userProfileDTO) {
	    UserProfile userProfile = new UserProfile();
	    User user = new User(); // Asegúrate de tener el constructor adecuado o el método para obtener el usuario
	    user.setId(userProfileDTO.getUserId());
	    
	    userProfile.setId(userProfileDTO.getId());
	    userProfile.setUser(user);
	    userProfile.setPhoto(userProfileDTO.getPhoto());
	    userProfile.setPhotoFileName(userProfileDTO.getPhotoFileName());
	    userProfile.setLocation(userProfileDTO.getLocation());
	    userProfile.setGender(userProfileDTO.getGender());
	    userProfile.setAge(userProfileDTO.getAge());
	    userProfile.setLikeGender(userProfileDTO.getLikeGender());
	    userProfile.setMaxAge(userProfileDTO.getMaxAge());
	    userProfile.setMinAge(userProfileDTO.getMinAge());
	    
	    return userProfile;
	}


	@Override
	public UserProfileDTO getRandomProfile(Long userId) {
	    // Verificar si el perfil de usuario actualmente logueado está disponible
		if (userId == null) {
            return null;
        }

		UserProfileDTO currentUserProfile = getUserProfile(userId);
        if (currentUserProfile == null) {
            return null;
        }

        Integer userMinAge = currentUserProfile.getMinAge();
        Integer userMaxAge = currentUserProfile.getMaxAge();
        String userLikeGender = currentUserProfile.getLikeGender();
        
        UserProfile userFilt = convertToEntity(currentUserProfile);

        // Definir las acciones excluidas
        List<ActionType> excludedActions = Arrays.asList(ActionType.LIKE, ActionType.DISLIKE);

        // Obtener la lista de todos los perfiles de usuario que cumplen con las preferencias del usuario actual
        List<UserProfile> filteredUserProfiles = userProfileRepository.findFilteredUserProfiles(
                userLikeGender, userMinAge, userMaxAge, userFilt, excludedActions);


	    // Verificar si hay perfiles de usuario que cumplen con las preferencias del usuario actual
	    if (filteredUserProfiles.isEmpty()) {
	        return null; // Devolver null si no hay perfiles de usuario que cumplan con las preferencias
	    }

	    // Obtener un índice aleatorio dentro del rango de la lista de perfiles de usuario filtrados
	    Random random = new Random();
	    int randomIndex = random.nextInt(filteredUserProfiles.size());

	    // Obtener el perfil de usuario aleatorio en función del índice generado
	    UserProfile randomUserProfile = filteredUserProfiles.get(randomIndex);

	    // Convertir el perfil de usuario a un UserProfileDTO y devolverlo
	    return convertToDTO(randomUserProfile);
	}

	@Override
	public UserProfileResponse saveProfile(MultipartFile photo,
            String photoFileName,
            String location,
            String gender,
            String age,
            String likeGender,
            Integer maxAge,
            Integer minAge,
            Long userId) {
		
			// Buscar el usuario por ID
			User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
				
			try {
				UserProfile userProfile = new UserProfile();
				userProfile.setPhoto(photo.getBytes());
				userProfile.setPhotoFileName(photoFileName);
				userProfile.setLocation(location);
				userProfile.setGender(gender);
				userProfile.setAge(age);
				userProfile.setLikeGender(likeGender);
				userProfile.setMaxAge(maxAge);
				userProfile.setMinAge(minAge);
				userProfile.setUser(user); // Asegúrate de establecer el usuario en el perfil
				
				// Guardar el perfil de usuario en la base de datos
				userProfileRepository.save(userProfile);
				
			return new UserProfileResponse("El perfil se registró satisfactoriamente");
				} catch (IOException e) {
			throw new RuntimeException("Error al guardar la foto del perfil", e);
				}
			}
	
=======

	@Override
	public UserProfileDTO getRandomProfile(Long userId) {
	    // Verificar si el perfil de usuario actualmente logueado está disponible
		if (userId == null) {
            return null;
        }

		UserProfileDTO currentUserProfile = getUserProfile(userId);
        if (currentUserProfile == null) {
            return null;
        }

        Integer userMinAge = currentUserProfile.getMinAge();
        Integer userMaxAge = currentUserProfile.getMaxAge();
        String userLikeGender = currentUserProfile.getLikeGender();

	    // Obtener la lista de todos los perfiles de usuario que cumplen con las preferencias del usuario actual
	    List<UserProfile> filteredUserProfiles = userProfileRepository.findFilteredUserProfiles(userLikeGender,userMinAge, userMaxAge);

	    // Verificar si hay perfiles de usuario que cumplen con las preferencias del usuario actual
	    if (filteredUserProfiles.isEmpty()) {
	        return null; // Devolver null si no hay perfiles de usuario que cumplan con las preferencias
	    }

	    // Obtener un índice aleatorio dentro del rango de la lista de perfiles de usuario filtrados
	    Random random = new Random();
	    int randomIndex = random.nextInt(filteredUserProfiles.size());

	    // Obtener el perfil de usuario aleatorio en función del índice generado
	    UserProfile randomUserProfile = filteredUserProfiles.get(randomIndex);

	    // Convertir el perfil de usuario a un UserProfileDTO y devolverlo
	    return convertToDTO(randomUserProfile);
	}
	
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
	
}
