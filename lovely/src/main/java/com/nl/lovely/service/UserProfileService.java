package com.nl.lovely.service;

import java.util.List;
import java.util.Optional;

import com.nl.lovely.entity.UserProfile;

public interface UserProfileService {
	
	public UserProfile saveUserProfile(UserProfile user);
	
	public void deleteUserProfile(Long id);
	
	public UserProfile updateUserProfile(UserProfile user);
	
	public UserProfile findUserById(Long id);
	
	public UserProfile findByUsername (String username);
	
	//public List<UserProfile> getRandomProfiles (int count);
	
	//public UserProfile getRandomProfile();

	
	//public boolean checkUsername(String username);
	
	//public String getUserRole (String username);
	
	//public boolean existsAdminRole();
}
