package com.nl.lovely.service;

import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;
import com.nl.lovely.dto.ProfileDTO;
import com.nl.lovely.dto.ProfileDetailDTO;
import com.nl.lovely.dto.UserDTO;
import com.nl.lovely.entity.User;
import com.nl.lovely.request.UserRequest;
import com.nl.lovely.response.ApiResponse;
import com.nl.lovely.response.UserProfileResponse;
import com.nl.lovely.response.UserResponse;

public interface UserService {
	
	public ProfileDTO getProfileById(Long id) throws Exception;
	
	public ApiResponse updateUserData(ProfileDTO request);
	
	public ApiResponse updateProfileWithPhoto(ProfileDetailDTO request, MultipartFile file) throws Exception;
	
	public ApiResponse deleteUserComplete(Long id);
	
	public void changePassword(Long userId, String currentPassword, String newPassword) throws Exception;
	//----------------------------------------------------------------
	public User findUserById(Long id);
	
	public Optional<User> findByUsername (String username);
	
	public User findByUsername2 (String username);
	
	public UserDTO getUser(Long id);

	public List<ProfileDTO> getUsersByMatch(Long id);
	
	public ProfileDetailDTO getUserProfileDetail(Long id);

	public List<ProfileDTO> getFilteredUserProfiles(Long id);
	
	
	//--------------------------------------------------------------
	//public UserProfileResponse saveProfile(MultipartFile photo,String photoFileName,String location,String gender,String age,String likeGender,Integer maxAge,Integer minAge,Long userId);
	//public List<UserProfileDTO> getRandomProfiles (int count);
	//public UserProfileDTO getRandomProfile(Long userId);
	//public Long getUserProfileByUserId(Long id);
	//public UserProfileResponse updateProfileAndPhoto(UserProfileDTO profileRequest, MultipartFile file) throws Exception;
	//public UserProfileResponse updateProfileDate(UserProfileDTO profileRequest) throws Exception;
	//public Optional<User> findByUsername (String username);
	//public Optional<User> findUserById(Long id);
	
}	
