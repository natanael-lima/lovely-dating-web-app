package com.nl.lovely.service.imp;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.nl.lovely.dto.PreferenceDTO;
import com.nl.lovely.dto.ProfileDTO;
import com.nl.lovely.dto.ProfileDetailDTO;
import com.nl.lovely.dto.UserDTO;
import com.nl.lovely.entity.Preference;
import com.nl.lovely.entity.ProfileDetail;
import com.nl.lovely.entity.User;
import com.nl.lovely.enums.ActionType;
import com.nl.lovely.enums.RoleType;
import com.nl.lovely.exception.NotFoundException;
import com.nl.lovely.repository.UserRepository;
import com.nl.lovely.response.ApiResponse;
import com.nl.lovely.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService{
	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	public ProfileDTO getProfileById(Long id) throws Exception{
		// TODO Auto-generated method stub
	    Optional<User> profile = userRepository.findById(id); 
		return convertDtoOptional(profile) ;
	}
	
	// Método para convertir User a ProfileDTO
	private ProfileDTO convertToDTOProfile(User profile) {
        return ProfileDTO.builder()
                .id(profile.getId())
                .username(profile.getUsername())
                .lastname(profile.getLastname())
                .name(profile.getName())
                .role(profile.getRole())
                .preference(convertToPreferenceDTO(profile.getPreference())) // Convertir entidad a DTO
                .profileDetail(convertToProfileDetailDTO(profile.getProfileDetail())) // Convertir entidad a DTO
                .build();
    }

	private ProfileDTO convertDtoOptional(Optional<User> profileOptional) throws Exception {
	    if (profileOptional.isPresent()) {
	    	User profile = profileOptional.get();

	        // Accede explícitamente a ProfileDetail para forzar la inicialización
	        //ProfileDetail profileDetail = profile.getProfileDetail();

	        // Construye el ProfileDTO con todos los campos necesarios
	        ProfileDTO dto = ProfileDTO.builder()
	            .id(profile.getId())
	            .username(profile.getUsername())
	            .lastname(profile.getLastname())
	            .name(profile.getName())
	            .role(profile.getRole())
	            .preference(convertToPreferenceDTO(profile.getPreference())) // Convertir entidad a DTO
	            .profileDetail(convertToProfileDetailDTO(profile.getProfileDetail())) // Convertir entidad a DTO
	            .build();

	        return dto;
	    } else {
	        throw new Exception("Profile not found");
	    }
	}
	
	private PreferenceDTO convertToPreferenceDTO(Preference preference) {
	    if (preference == null) {
	        return null;
	    }
	    return PreferenceDTO.builder()
	            .id(preference.getId())
	            .maxAge(preference.getMaxAge())
	            .minAge(preference.getMinAge())
	            .likeGender(preference.getLikeGender())
	            .location(preference.getLocation())
	            .distance(preference.getDistance())
	            .interests(preference.getInterests())
	            .build();
	}

	private ProfileDetailDTO convertToProfileDetailDTO(ProfileDetail profileDetail) {
	    if (profileDetail == null) {
	        return null;
	    }
	    return ProfileDetailDTO.builder()
	            .id(profileDetail.getId())
	            .phone(profileDetail.getPhone())
	            .gender(profileDetail.getGender())
	            .birthDate(profileDetail.getBirthDate())
	            .description(profileDetail.getDescription())
	            .work(profileDetail.getWork())
	            .photo(profileDetail.getPhoto())
	            .photoFileName(profileDetail.getPhotoFileName())
	            .timestamp(profileDetail.getTimestamp())
	            .build();
	}

	@Transactional
	public ApiResponse updateUserData(ProfileDTO request) {
		// TODO Auto-generated method stub
		User user = User.builder()
		        .id(request.getId())
		        .lastname(request.getLastname())
		        .name(request.getName())
		        .role(RoleType.USER)
		        .build();
		        userRepository.updateUserQuery(user.getId(), user.getLastname(), user.getName());
		        return new ApiResponse("El usuario se actualizo satisfactoriamente");
	}
	
	@Override
	public ApiResponse updateProfileWithPhoto(ProfileDetailDTO profileRequest, MultipartFile file) throws Exception {
		// TODO Auto-generated method stub
		
		User user = userRepository.findByProfileDetailId(profileRequest.getId())
                .orElseThrow(() -> new Exception("Usuario no encontrado para ProfileDetail con ID: " + profileRequest.getId()));

        ProfileDetail profileDetail = user.getProfileDetail();
        if (profileDetail == null) {
            throw new Exception("ProfileDetail no encontrado para el usuario con ID: " + user.getId());
        }

	    // Actualización de campos del ProfileDetail
	    profileDetail.setPhone(profileRequest.getPhone());
	    profileDetail.setGender(profileRequest.getGender());
	    profileDetail.setBirthDate(profileRequest.getBirthDate());
	    profileDetail.setDescription(profileRequest.getDescription());
	    profileDetail.setWork(profileRequest.getWork());
	    
	    
	    // Guardar la imagen nueva si se proporciona
	    if (file != null && !file.isEmpty()) {
	    	// Procesar la nueva imagen
 	   	    System.out.print("entre photo update");
 	   	    // Obtener los bytes del archivo
             byte[] pdfBytes = file.getBytes();
             // Guardar el contenido del archivo
             profileDetail.setPhoto(pdfBytes);
             // Establecer el nombre del archivo
             profileDetail.setPhotoFileName(file.getOriginalFilename());
             
             // Llamar al método de actualización con foto
     	    userRepository.updateProfileDetailWithPhoto(
                     user.getId(),
                     profileDetail.getPhone(),
                     profileDetail.getGender(),
                     profileDetail.getBirthDate(),
                     profileDetail.getDescription(),
                     profileDetail.getWork(),
                     profileDetail.getPhoto(),
                     profileDetail.getPhotoFileName()
             );
     	    
	    } else {
	    	 System.out.println("update only data");
	        // Llamar al método de actualización sin foto
	    	userRepository.updateProfileDetail(
	                user.getId(),
	                profileDetail.getPhone(),
	                profileDetail.getGender(),
	                profileDetail.getBirthDate(),
	                profileDetail.getDescription(),
	                profileDetail.getWork()
	        );
	    }
	 		return new ApiResponse("El perfil se actualizo satisfactoriamente");
	}
	
	
	/*@Override
	public Optional<User> findByUsername(String username) {
		// TODO Auto-generated method stub
		Optional<User> result= userRepository.findByUsername(username);
		return result;
	}
	@Override
	public Optional<User> findUserById(Long id) {
		// TODO Auto-generated method stub
		return userRepository.findById(id);
	}*/
	
	@Override
	public Optional<User> findByUsername(String username) {
		// TODO Auto-generated method stub
		return userRepository.findByUsername(username);
	}
	@Override
	public User findByUsername2(String username) {
		// TODO Auto-generated method stub
		return userRepository.findByUsername2(username);
	}
	
	@Override
	public User findUserById(Long id) {
		// TODO Auto-generated method stub
		return userRepository.findById(id)
	            .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
	}

	@Override
	public UserDTO getUser(Long id) {
	        
		User user= userRepository.findById(id).orElse(null);
	       
	        if (user!=null)
	        {
	            UserDTO userDTO = UserDTO.builder()
	            .id(user.getId())
	            .username(user.getUsername())
	            .lastname(user.getLastname())
	            .name(user.getName())
	            .build();
	            return userDTO;
	        }
			return null;      
	}
	
	@Transactional
	@Override
	public  ApiResponse deleteUserComplete(Long id) {
		try {
	        User user = userRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
	        // Manejar manualmente las relaciones si es necesario
	        /*user.getPreference().setUser(null);
	        user.setPreference(null);
	        
	        // Eliminar Preference
	        if (user.getPreference() != null) {
	            preferenceRepository.delete(user.getPreference());
	            user.setPreference(null);
	        }

	        // Eliminar ProfileDetail
	        if (user.getProfileDetail() != null) {
	            profileDetailRepository.delete(user.getProfileDetail());
	            user.setProfileDetail(null);
	        }

	        // Actualizar el usuario sin las relaciones
	        userRepository.save(user);

	        // Ahora eliminar el usuario
	        userRepository.delete(user);*/
	        userRepository.deleteById(id);
	        return new ApiResponse("El usuario se eliminó correctamente");
	    } catch (Exception e) {
	        // Log the exception
	        throw new RuntimeException("Error al eliminar el usuario: " + e.getMessage());
	    }
		
	}

	

	@Override
	public List<ProfileDTO> getUsersByMatch(Long id) {
		// TODO Auto-generated method stub
		List<User> users = userRepository.findUsersByMatch(id);
		return users.stream()
                .map(this::convertToDTOProfile)
                .collect(Collectors.toList());
	}
	
	private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                      .id(user.getId())
                      .username(user.getUsername())
                      .lastname(user.getLastname())
                      .name(user.getName())
                      .build();
    }
	
	//----------------------------------------------------------------------------------------------------------------------
	
	@Override
	public ProfileDetailDTO getUserProfileDetail(Long id) {
		User u= userRepository.findById(id).orElse(null);
	       
        if (u!=null)
        {
        	ProfileDetailDTO dto = ProfileDetailDTO.builder()
            .id(u.getProfileDetail().getId())
            .phone(u.getProfileDetail().getPhone())
            .gender(u.getProfileDetail().getGender())
            .birthDate(u.getProfileDetail().getBirthDate())
            .description(u.getProfileDetail().getDescription())
            .work(u.getProfileDetail().getWork())
            .photo(u.getProfileDetail().getPhoto())
            .photoFileName(u.getProfileDetail().getPhotoFileName())
            .timestamp(u.getProfileDetail().getTimestamp())
            .build();
            return dto;
        }
		return null;   
	}


	private ProfileDTO getProfileDTO(Long id) {
		User u= userRepository.findById(id).orElse(null);
	       
        if (u!=null)
        {
        	ProfileDTO dto = ProfileDTO.builder()
            .id(u.getId())
            .username(u.getUsername())
            .lastname(u.getLastname())
            .name(u.getName())
            .preference(convertToPreferenceDTO(u.getPreference())) // Convertir entidad a DTO
            .profileDetail(convertToProfileDetailDTO(u.getProfileDetail())) // Convertir entidad a DTO
            .build();
            return dto;
        }
		return null;   
	}
	

	// Método para obtener una lista de perfiles de usuario que cumplen con las preferencias del usuario actual
	@Override
	public List<ProfileDTO> getFilteredUserProfiles(Long id) {
	    // Verificar si el perfil de usuario actualmente logueado está disponible
	    if (id == null) {
	        return Collections.emptyList();
	    }

	    ProfileDTO currentUser = getProfileDTO(id);
	    
	    if (currentUser == null) {
	        return Collections.emptyList();
	    }
	    /*//obtener edad de un usuario en numero
	    int currentYear = LocalDate.now().getYear();
	    int userBirthYear = currentUser.getProfileDetail().getBirthDate().getYear();
	    int age = currentYear - userBirthYear;*/
	    
        // Obtener las preferencias del usuario actual
        Integer userMinAge = currentUser.getPreference().getMinAge();
        Integer userMaxAge = currentUser.getPreference().getMaxAge();
	    String userLikeGender = currentUser.getPreference().getLikeGender();
	    String currentUserGender = currentUser.getProfileDetail().getGender();
	    User userFilt = convertToEntity(currentUser);

	    // Definir las acciones excluidas
	    List<ActionType> excludedActions = Arrays.asList(ActionType.LIKE, ActionType.DISLIKE);
	    

	    // Obtener la lista de todos los perfiles de usuario que cumplen con las preferencias del usuario actual
	    List<User> filteredUserProfiles = userRepository.findFilteredUsers(userLikeGender, userMinAge, userMaxAge, userFilt,currentUserGender, excludedActions);
 
	    // Convertir la lista de UserProfile a una lista de UserProfileDTO V2
	    return filteredUserProfiles.stream()
	                               .map(this::convertToDTOProfile)
	                               .collect(Collectors.toList());
	    
	    // Convertir la lista de UserProfile a una lista de UserProfileDTO usando un bucle for V1
	    //List<UserProfileDTO> filteredUserProfilesDTO = new ArrayList<>();
	    //for (UserProfile profile : filteredUserProfiles) {
	    //    filteredUserProfilesDTO.add(convertToDTO(profile));
	    //}
		//return filteredUserProfilesDTO;
	}
	
	
		

		
	public User convertToEntity(ProfileDTO profileDTO) {
	    User user = new User();
	    user.setId(profileDTO.getId());
	    user.setUsername(profileDTO.getUsername());
	    user.setLastname(profileDTO.getLastname());
	    user.setName(profileDTO.getName());
	    user.setRole(profileDTO.getRole());
	    
	    // Convertir DTOs anidados a entidades
	    user.setPreference(convertToPreferenceEntity(profileDTO.getPreference())); // Convertir DTO Preference a entidad
	    user.setProfileDetail(convertToProfileDetailEntity(profileDTO.getProfileDetail())); // Convertir DTO ProfileDetail a entidad
	    
	    return user;
	}

	private Preference convertToPreferenceEntity(PreferenceDTO preferenceDTO) {
	    if (preferenceDTO == null) {
	        return null;
	    }
	    Preference preference = new Preference();
	    preference.setId(preferenceDTO.getId());
	    preference.setMaxAge(preferenceDTO.getMaxAge());
	    preference.setMinAge(preferenceDTO.getMinAge());
	    preference.setLikeGender(preferenceDTO.getLikeGender());
	    preference.setLocation(preferenceDTO.getLocation());
	    preference.setDistance(preferenceDTO.getDistance());
	    preference.setInterests(preferenceDTO.getInterests());
	    
	    return preference;
	}

	private ProfileDetail convertToProfileDetailEntity(ProfileDetailDTO profileDetailDTO) {
	    if (profileDetailDTO == null) {
	        return null;
	    }
	    ProfileDetail profileDetail = new ProfileDetail();
	    profileDetail.setId(profileDetailDTO.getId());
	    profileDetail.setPhone(profileDetailDTO.getPhone());
	    profileDetail.setGender(profileDetailDTO.getGender());
	    profileDetail.setBirthDate(profileDetailDTO.getBirthDate());
	    profileDetail.setDescription(profileDetailDTO.getDescription());
	    profileDetail.setWork(profileDetailDTO.getWork());
	    profileDetail.setPhoto(profileDetailDTO.getPhoto());
	    profileDetail.setPhotoFileName(profileDetailDTO.getPhotoFileName());
	    profileDetail.setTimestamp(profileDetailDTO.getTimestamp());
	    
	    return profileDetail;
	}

	
	
	
	

	/*@Override
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
	}*/
	
	
	// NO USADOS POR COMMENT POR LA DUDAS 
	/*@Override
	public UserProfileResponse updateProfileAndPhoto(UserProfileDTO profileRequest, MultipartFile file) throws Exception {
		// TODO Auto-generated method stub
		
		UserProfile profileUser = userProfileRepository.findById(profileRequest.getId())
	            .orElseThrow(() -> new Exception("UserProfile no encontrado"));
		
		// Obtener el usuario asociado al perfil
	    //Optional<User> userOptional = userService.findUserById(profileRequest.getUserId());
	    //User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
	   
	
	    // Actualización de campos del userprofile
	    profileUser.setLocation(profileRequest.getLocation());
	    profileUser.setGender(profileRequest.getGender());
	    profileUser.setAge(profileRequest.getAge());
	    profileUser.setLikeGender(profileRequest.getLikeGender());
	    profileUser.setMaxAge(profileRequest.getMaxAge());
	    profileUser.setMinAge(profileRequest.getMinAge());
	    
	    // Guardar la imagen nueva si se proporciona
	    if (file != null && !file.isEmpty()) {
	    	// Procesar la nueva imagen
 	   	    System.out.print("entre photo update");
 	   	    // Obtener los bytes del archivo
             byte[] pdfBytes = file.getBytes();
             // Guardar el contenido del archivo
             profileUser.setPhoto(pdfBytes);
             // Establecer el nombre del archivo
             profileUser.setPhotoFileName(file.getOriginalFilename());
	    }
	    
	 // Llamar al método de actualización del repositorio
	 		userProfileRepository.updatePhotoAndProfile(
	 				profileUser.getId(),
	 				profileUser.getPhoto(),
	 				profileUser.getPhotoFileName(),
	 				profileUser.getLocation(),
	 				profileUser.getGender(),
	 				profileUser.getAge(),
	 				profileUser.getLikeGender(),
	 				profileUser.getMaxAge(),
	 				profileUser.getMinAge()
	    );
	    
	 		return new UserProfileResponse("El perfil se actualizo satisfactoriamente");
	}
	
	
	
	@Override
	public UserProfileResponse updateProfileDate(UserProfileDTO dto) throws Exception {
		// TODO Auto-generated method stub
		UserProfile profile = userProfileRepository.findById(dto.getId())
	            .orElseThrow(() -> new Exception("UserProfile no encontrado"));
		 
	    // Actualización de campos del producto
		profile.setLocation(dto.getLocation());
		profile.setGender(dto.getGender());
		profile.setAge(dto.getAge());
		profile.setLikeGender(dto.getLikeGender());
		profile.setMaxAge(dto.getMaxAge());
		profile.setMinAge(dto.getMinAge());
		
		System.out.print("entre json update");
	    // Actualización de la categoría si se proporciona
	    //if (dto.getCategoria() != null && dto.getCategoria() != null) {
	    //    Category categoria = categoryRepository.findByName(dto.getCategoria())
	    //            .orElseThrow(() -> new Exception("La categoría con nombre " + dto.getCategoria() + " no existe"));
	    //    producto.setCategoria(categoria);
	    //}
	    	    
	    // Llamar al método de actualización del repositorio
		userProfileRepository.updateProfileData(
				profile.getId(),
				profile.getLocation(),
				profile.getGender(),
				profile.getAge(),
				profile.getLikeGender(),
				profile.getMaxAge(),
				profile.getMinAge()
	    );
	    return new UserProfileResponse("El Profile se actualizó satisfactoriamente");
	}*/
	
	
	/*@Override
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
	            .timestamp(userP.getTimestamp())
	            .build();
	}
	
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
	    userProfile.setTimestamp(userProfileDTO.getTimestamp());
	    
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
	}*/

}
