package com.nl.lovely.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nl.lovely.entity.UserAction;
import com.nl.lovely.entity.User;
import com.nl.lovely.enums.ActionType;
import com.nl.lovely.repository.UserActionRepository;
import com.nl.lovely.service.UserActionService;

@Service
public class UserActionServiceImp implements UserActionService{
	
	@Autowired
    private UserActionRepository userActionRepository;
	
	// Verifica si el usuario dado ha dado "like" al objetivo dado
	@Override
	public boolean verificaLiked(User liker, User target) {
		
        return userActionRepository.existsByLikerAndTargetAndActionType(liker, target, ActionType.LIKE);
	}
	//Registra la acción de "like" en la base de datos
	@Override
	public void registrarLike(User liker, User target) {
		if (liker == null || target == null) {
	        throw new IllegalArgumentException("El liker y el target no pueden ser nulos");
	    }

	    UserAction likeAction = new UserAction();
	    likeAction.setActionType(ActionType.LIKE);
	    likeAction.setLiker(liker);
	    likeAction.setTarget(target);
	    
	    liker.getActionsForMe().add(likeAction);
	    target.getActionsReceived().add(likeAction);
        
	    userActionRepository.save(likeAction);
		
	}
	// Registra la acción de "dislike" en la base de datos
	@Override
	public void registrarDislike(User disliker, User target) {
		
		if (disliker == null || target == null) {
	        throw new IllegalArgumentException("El disliker y el target no pueden ser nulos");
	    }

	    UserAction dislikeAction = new UserAction();
	    dislikeAction.setActionType(ActionType.DISLIKE);
	    dislikeAction.setLiker(disliker);
	    dislikeAction.setTarget(target);
	    
	    disliker.getActionsForMe().add(dislikeAction);
	    target.getActionsReceived().add(dislikeAction);
	    userActionRepository.save(dislikeAction);
		
	}
	
	
	
	
}
