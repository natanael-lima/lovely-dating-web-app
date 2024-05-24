package com.nl.lovely.service;

import java.util.List;
import java.util.Optional;

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

public interface UserProfileService {
	
	public UserProfileDTO getUserProfile(Long id);
	
	public Long getUserProfileByUserId(Long userId);
	
<<<<<<< HEAD
	public UserProfileResponse saveProfile(MultipartFile photo,String photoFileName,String location,String gender,String age,String likeGender,Integer maxAge,Integer minAge,Long userId);
	
=======
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
	public UserProfileResponse updateUserProfileData(UserProfileRequest profile);
	
	public UserProfileResponse updateUserProfilePhoto(UserProfilePhotoRequest profileRequest);
	
	public void deleteUserProfile(Long id);
	
	public UserProfile findUserById(Long id);
	
	public UserProfile findByUsername (String username);
	
	public List<UserProfileDTO> getRandomProfiles (int count);
	
	public UserProfileDTO getRandomProfile(Long userId);
	
	//public List<UserProfile> getAllUsersByProfileId(Long userId);
	
	//public List<UserProfile> getRandomProfiles (int count);
	
	//public UserProfile getRandomProfile();

	//public boolean checkUsername(String username);
	
	//public String getUserRole (String username);
	
	//public boolean existsAdminRole();
}
