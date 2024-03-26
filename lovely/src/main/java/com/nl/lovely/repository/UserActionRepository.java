package com.nl.lovely.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.nl.lovely.entity.UserAction;
import com.nl.lovely.entity.UserProfile;
import com.nl.lovely.enums.ActionType;

public interface UserActionRepository extends JpaRepository<UserAction,Long>{
	boolean existsByLikerAndTargetAndActionType(UserProfile liker, UserProfile target, ActionType actionType);
}
