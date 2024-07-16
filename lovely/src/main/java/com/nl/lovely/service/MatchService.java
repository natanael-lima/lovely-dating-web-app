package com.nl.lovely.service;

import java.util.List;

import com.nl.lovely.dto.MatchDTO;
import com.nl.lovely.entity.User;
import com.nl.lovely.enums.ActionType;

public interface MatchService {
	
	public void deleteMatch(Long id);
	
	public void handleLike(User liker, User target);
	
	public void handleDislike(User disliker, User target);
	
	public void processAction(User liker, User target, ActionType actionType);
	
	public List<MatchDTO> findAllMatchByUserProfile(Long profileId);
	
	public MatchDTO findMatchByProfileIds(Long profileId1, Long profileId2);
	
	public boolean confirmMatch(User profile1, User profile2);
}
