package com.nl.lovely.service;

import java.util.List;

import com.nl.lovely.dto.MatchDTO;
import com.nl.lovely.entity.Match;
import com.nl.lovely.entity.UserProfile;
import com.nl.lovely.enums.ActionType;

public interface MatchService {
	
	public boolean confirmMatch(UserProfile profile1, UserProfile profile2);
	
	public void deleteMatch(Long id);
	
	public void handleLike(UserProfile liker, UserProfile target);
	
	public void handleDislike(UserProfile disliker, UserProfile target);
	
	public void processAction(UserProfile liker, UserProfile target, ActionType actionType);
	
	public List<MatchDTO> findAllMatchByUserProfile(Long profileId);
	
	public MatchDTO getMatchByUserProfile();
	
	public MatchDTO findMatchByProfileIds(Long profileId1, Long profileId2);
}
