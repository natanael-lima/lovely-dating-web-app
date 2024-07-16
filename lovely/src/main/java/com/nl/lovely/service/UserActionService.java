package com.nl.lovely.service;

import com.nl.lovely.entity.User;

public interface UserActionService {
	
	public boolean verificaLiked(User liker, User target);
	
	public void registrarLike(User liker, User target);
	
	public void registrarDislike(User disliker, User target);
}
