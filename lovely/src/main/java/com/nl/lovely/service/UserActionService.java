package com.nl.lovely.service;

import com.nl.lovely.entity.User;
import com.nl.lovely.entity.UserProfile;

public interface UserActionService {
	
	public boolean verificaLiked(UserProfile liker, UserProfile target);
	
	public void registrarLike(UserProfile liker, UserProfile target);
	
	public void registrarDislike(UserProfile disliker, UserProfile target);
}
