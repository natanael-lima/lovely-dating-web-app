package com.nl.lovely.service;

import java.util.List;
import java.util.Optional;

import com.nl.lovely.dto.UserDTO;
import com.nl.lovely.dto.UserProfileDTO;
import com.nl.lovely.entity.UserProfile;
import com.nl.lovely.request.UserProfileRequest;
import com.nl.lovely.request.UserRequest;
import com.nl.lovely.response.UserProfileResponse;
import com.nl.lovely.response.UserResponse;

public interface UserProfileService {
	
	public UserProfileDTO getUserProfile(Long id);
	
	public UserProfileResponse updateUserProfile(UserProfileRequest profile);
	
	public void deleteUserProfile(Long id);
	
	public UserProfile findUserById(Long id);
	
	public UserProfile findByUsername (String username);
	
	//public List<UserProfile> getRandomProfiles (int count);
	
	//public UserProfile getRandomProfile();

	
	//public boolean checkUsername(String username);
	
	//public String getUserRole (String username);
	
	//public boolean existsAdminRole();
}
