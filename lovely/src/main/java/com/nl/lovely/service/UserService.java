package com.nl.lovely.service;

import java.util.List;
import java.util.Optional;

import com.nl.lovely.dto.UserDTO;
import com.nl.lovely.entity.User;
import com.nl.lovely.request.UserRequest;
import com.nl.lovely.response.UserResponse;

public interface UserService {
	
	
	public UserDTO getUser(Long id);
	
	public UserResponse updateUser(UserRequest userRequest);
	
	public void deleteUser(Long id);
	
	public Optional<User> findByUsername (String username);
	
	public List<User> getRandomProfiles (int count);
	
	public User getRandomProfile();

	public Optional<User> findUserById(Long id);
	
	//public boolean checkEmail(String email);

	
	//public String getUserRole (String username);
	
	//public boolean existsAdminRole();
}	
