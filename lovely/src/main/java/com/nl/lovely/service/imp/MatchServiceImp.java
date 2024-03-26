package com.nl.lovely.service.imp;

import java.time.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nl.lovely.entity.Match;
import com.nl.lovely.entity.UserAction;
import com.nl.lovely.entity.UserProfile;
import com.nl.lovely.enums.ActionType;
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

	
}