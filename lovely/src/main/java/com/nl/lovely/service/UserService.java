package com.nl.lovely.service;

import java.util.List;
import java.util.Optional;

import com.nl.lovely.entity.User;

public interface UserService {
	
	public User saveUser(User user);
	
	public void deleteUser(Long id);
	
	public User updateUser(User user);
	
	public List<User> getRandomProfiles (int count);
	
	public User getRandomProfile();
	
	//public User findUserById(Long id);
	
	public Optional<User> findByUsername (String username);
	
	//public boolean checkEmail(String email);

	
	//public String getUserRole (String username);
	
	//public boolean existsAdminRole();
}	
