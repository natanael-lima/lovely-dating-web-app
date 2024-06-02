package com.nl.lovely.service.imp;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nl.lovely.dto.UserCompleteDTO;
import com.nl.lovely.dto.UserDTO;
import com.nl.lovely.entity.User;
import com.nl.lovely.entity.UserProfile;
import com.nl.lovely.enums.RoleType;
import com.nl.lovely.exception.NotFoundException;
import com.nl.lovely.repository.UserProfileRepository;
import com.nl.lovely.repository.UserRepository;
import com.nl.lovely.request.UserRequest;
import com.nl.lovely.response.UserResponse;
import com.nl.lovely.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService{
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserProfileRepository userProfileRepository;
	

	@Override
	public Optional<User> findByUsername(String username) {
		// TODO Auto-generated method stub
		Optional<User> result= userRepository.findByUsername(username);
		return result;
	}
	
	@Transactional
	public UserResponse updateUser(UserRequest userRequest) {
		// TODO Auto-generated method stub
		User user = User.builder()
		        .id(userRequest.getId())
		        .lastname(userRequest.getLastname())
		        .name(userRequest.getName())
		        .role(RoleType.USER)
		        .build();
		        
		        userRepository.updateUser(user.getId(), user.getLastname(), user.getName());

		        return new UserResponse("El usuario se actualizo satisfactoriamente");
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
	
	@Override
	public void deleteUser(Long id) {
		// TODO Auto-generated method stub
		userRepository.deleteById(id);
	}

	@Override
	public Optional<User> findUserById(Long id) {
		// TODO Auto-generated method stub
		return userRepository.findById(id);
	}

	@Override
	public List<UserDTO> getUsersByMatch(Long userId) {
		// TODO Auto-generated method stub
		List<User> users = userProfileRepository.findUsersByMatch(userId);
		return users.stream()
                .map(this::convertToDTO)
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
	
	private UserCompleteDTO convertDTO(Optional<UserProfile> userOptional) throws Exception {
		if (userOptional.isPresent()) {
	        UserProfile user = userOptional.get(); // Extraer el objeto UserProfile de Optional
	        return UserCompleteDTO.builder()
	                  .id(user.getId())
	                  .username(user.getUser().getUsername())
	                  .lastname(user.getUser().getLastname())
	                  .name(user.getUser().getName())
	                  .photo(user.getPhoto())
	                  .photoFileName(user.getPhotoFileName())
	                  .location(user.getLocation())
	                  .gender(user.getGender())
	                  .age(user.getAge())
	                  .likeGender(user.getLikeGender())
	                  .maxAge(user.getMaxAge())
	                  .minAge(user.getMinAge())
	                  .build();
	    } else {
	        // Manejo de error si el usuario no está presente
	        throw new Exception("User not found");
	    }
    }

	@Override
	public UserCompleteDTO getUserDTO(Long id) throws Exception {
		// TODO Auto-generated method stub
	   Optional<UserProfile> user = userProfileRepository.findById(id);
	   
		return convertDTO(user);
	}
	

}
