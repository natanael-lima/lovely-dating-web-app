package com.nl.lovely.service;

import com.nl.lovely.entity.User;
import com.nl.lovely.entity.UserProfile;
import com.nl.lovely.enums.ActionType;

public interface MatchService {
	
	//public void confirmMatch(UserProfile profile1, UserProfile profile2);
	
	public void deleteMatch(Long id);
	
	public void handleLike(UserProfile liker, UserProfile target);
	
	public void handleDislike(UserProfile disliker, UserProfile target);
	
	public void processAction(UserProfile liker, UserProfile target, ActionType actionType);
}
