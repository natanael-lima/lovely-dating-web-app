package com.nl.lovely.service.imp;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nl.lovely.entity.User;
import com.nl.lovely.entity.UserProfile;
import com.nl.lovely.exception.NotFoundException;
import com.nl.lovely.repository.UserProfileRepository;
import com.nl.lovely.repository.UserRepository;
import com.nl.lovely.service.UserProfileService;

@Service
public class UserProfileServiceImp implements UserProfileService {
	
	@Autowired
	private UserProfileRepository userProfileRepository;
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserProfile saveUserProfile(UserProfile user) {
		// TODO Auto-generated method stub
		return userProfileRepository.save(user);
	}

	@Override
	public void deleteUserProfile(Long id) {
		// TODO Auto-generated method stub
		userProfileRepository.deleteById(id);
	}

	@Override
	public UserProfile updateUserProfile(UserProfile user) {
		// TODO Auto-generated method stub
		return userProfileRepository.save(user);
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
