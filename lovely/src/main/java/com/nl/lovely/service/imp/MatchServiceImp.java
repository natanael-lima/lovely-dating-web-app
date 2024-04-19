package com.nl.lovely.service.imp;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nl.lovely.dto.MatchDTO;
import com.nl.lovely.dto.MessageDTO;
import com.nl.lovely.entity.Chat;
import com.nl.lovely.entity.Match;
import com.nl.lovely.entity.Message;
import com.nl.lovely.entity.UserAction;
import com.nl.lovely.entity.UserProfile;
import com.nl.lovely.enums.ActionType;
import com.nl.lovely.repository.ChatRepository;
import com.nl.lovely.repository.MatchRepository;
import com.nl.lovely.repository.UserActionRepository;
import com.nl.lovely.repository.UserProfileRepository;
import com.nl.lovely.service.MatchService;
import com.nl.lovely.service.UserActionService;

@Service
public class MatchServiceImp implements MatchService {
	
	@Autowired
    private UserActionService userActionService;
	@Autowired
	private MatchRepository matchRepository;
	@Autowired
    private UserActionRepository userActionRepository;
	@Autowired
    private UserProfileRepository userProfileRepository;
	@Autowired
	private ChatRepository chatRepository;
	
	@Override
	public void deleteMatch(Long id) {
		// TODO Auto-generated method stub
					matchRepository.deleteById(id);
	}
	
	//registro de match si hay coincidencia
	@Override
	public void handleLike(UserProfile liker, UserProfile target) {
		// TODO Auto-generated method stub
		userActionService.registrarLike(liker, target);

        // Verificar si hay una coincidencia recíproca
        if (userActionService.verificaLiked(target, liker)) {
            //Crear un nuevo match si hay una coincidencia recíproca
        	if (liker == null || target == null) {
    	        throw new IllegalArgumentException("El profile1 y el profile2 no pueden ser nulos");
    	    }

    	    Match matching = new Match();
    	    matching.setMatchedAt(LocalDateTime.now());
    	    matching.setProfile1(liker);
    	    matching.setProfile2(target);
    	   
    	    matchRepository.save(matching);
        }
	}
	@Override
	public void handleDislike(UserProfile disliker, UserProfile target) {
		// TODO Auto-generated method stub
		userActionService.registrarDislike(disliker, target);
	}

	@Override
	public void processAction(UserProfile liker, UserProfile target, ActionType actionType) {
		// TODO Auto-generated method stub
		UserAction action = new UserAction();
        action.setLiker(liker);
        action.setTarget(target);
        action.setActionType(actionType);

        // Guardar la acción
        userActionRepository.save(action);

        target.getActionsReceived().add(action);
        liker.getActionsForMe().add(action);
        
        userProfileRepository.save(target);
        
       //Crear un nuevo match si hay una coincidencia recíproca
        if (userActionService.verificaLiked(target, liker)) {
            
        	if (liker == null || target == null) {
    	        throw new IllegalArgumentException("El profile1 y el profile2 no pueden ser nulos");
    	    }

    	    Match matching = new Match();
    	    matching.setMatchedAt(LocalDateTime.now());
    	    matching.setProfile1(liker);
    	    matching.setProfile2(target);
    	   
    	    matchRepository.save(matching);
    	    Chat chat = new Chat();
    	    chat.setMatch(matching);
    	    chatRepository.save(chat);
        }
	}

	
	private MatchDTO convertToDTO(Match match) {
	    return MatchDTO.builder()
	            .id(match.getId())
	            .matchedAt(match.getMatchedAt())
	            .profile1(match.getProfile1() != null ? match.getProfile1().getId() : null)
	            .profile2(match.getProfile2() != null ? match.getProfile2().getId() : null)
	            .build();
	}
	private Match convertToEntity(MatchDTO matchDTO) {
	    Match match = new Match();
	    
	    // Aquí puedes asignar los valores del DTO al objeto Match
	    match.setId(matchDTO.getId());
	    match.setMatchedAt(matchDTO.getMatchedAt());
	    
	    // Aquí debes establecer las relaciones, como profile1 y profile2
	    /*UserProfile profile1 = userProfileService.findUserById(matchDTO.getProfile1());
	    if(profile1 == null) {
	        throw new RuntimeException("Profile1 with ID " + matchDTO.getProfile1() + " not found");
	    }
	    match.setProfile1(profile1);
	    
	    UserProfile profile2 = userProfileService.findUserById(matchDTO.getProfile2());
	    if(profile2 == null) {
	        throw new RuntimeException("Profile2 with ID " + matchDTO.getProfile2() + " not found");
	    }
	    match.setProfile2(profile2);*/

	    return match;
	}
	
	@Override
	public List<MatchDTO> findAllMatchByUserProfile(Long profileId) {
		// TODO Auto-generated method stub
		 List<Match> matchsAll = matchRepository.findAllByProfile1IdOrProfile2Id(profileId);
		 
		 
		 List<MatchDTO> matchsAllDTO = new ArrayList<>();
		 for (Match matche : matchsAll) {
			 matchsAllDTO.add(convertToDTO(matche));
		    }

		
		return matchsAllDTO;
	}

	@Override
	public MatchDTO getMatchByUserProfile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MatchDTO findMatchByProfileIds(Long profileId1, Long profileId2) {
		Match match = matchRepository.findMatchByProfileIds(profileId1, profileId2)
		        .orElseThrow(() -> new RuntimeException("Match not found for profile IDs: " + profileId1 + " and " + profileId2));
		
		return  convertToDTO(match);
	}

	
}