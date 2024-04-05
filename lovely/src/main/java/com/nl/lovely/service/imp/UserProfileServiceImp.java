package com.nl.lovely.service.imp;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.nl.lovely.dto.UserDTO;
import com.nl.lovely.dto.UserProfileDTO;
import com.nl.lovely.entity.User;
import com.nl.lovely.entity.UserProfile;
import com.nl.lovely.enums.RoleType;
import com.nl.lovely.exception.NotFoundException;
import com.nl.lovely.repository.UserProfileRepository;
import com.nl.lovely.repository.UserRepository;
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
	public UserProfileResponse updateUserProfile(UserProfileRequest profileRequest) {
		// TODO Auto-generated method stub
		// Obtener el usuario asociado al perfil
	    Optional<User> userOptional = userService.findUserById(profileRequest.getUserId());
	    User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
	    
		UserProfile userProfile = UserProfile.builder()
				        .id(profileRequest.getId())
				        .user(user) // Establecer el usuario asociado al perfil
				        //.photo(profileRequest.getPhoto())
				        .location(profileRequest.getLocation())
				        .gender(profileRequest.getGender())
				        .age(profileRequest.getAge())
				        .likeGender(profileRequest.getLikeGender())
				        .maxAge(profileRequest.getMaxAge())
				        .minAge(profileRequest.getMinAge())
				        .build();
				        
		userProfileRepository.updateUserProfile(userProfile.getId(), userProfile.getLocation(), userProfile.getGender(),userProfile.getAge(),userProfile.getLikeGender(),userProfile.getMaxAge(),userProfile.getMinAge());

		 return new UserProfileResponse("El perfil se registró satisfactoriamente");
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


	

	

}
