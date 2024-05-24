package com.nl.lovely.service;

import java.util.List;
import java.util.Optional;

import com.nl.lovely.dto.UserCompleteDTO;
import com.nl.lovely.dto.UserDTO;
import com.nl.lovely.entity.User;
import com.nl.lovely.request.UserRequest;
import com.nl.lovely.response.UserResponse;

public interface UserService {
	
	public UserDTO getUser(Long id);
	
	public UserResponse updateUser(UserRequest userRequest);
	
	public void deleteUser(Long id);
	
	public Optional<User> findByUsername (String username);

	public Optional<User> findUserById(Long id);
	
<<<<<<< HEAD
	public List<UserDTO> getUsersByMatch(Long userId);
	
	public UserCompleteDTO getUserDTO(Long id) throws Exception;
=======
	 public List<UserDTO> getUsersByMatch(Long userId);
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
	
	//public boolean checkEmail(String email);

	
	//public String getUserRole (String username);
	
	//public boolean existsAdminRole();
}	
