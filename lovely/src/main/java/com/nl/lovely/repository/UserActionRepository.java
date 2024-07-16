package com.nl.lovely.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.nl.lovely.entity.User;
import com.nl.lovely.entity.UserAction;
import com.nl.lovely.enums.ActionType;

public interface UserActionRepository extends JpaRepository<UserAction,Long>{
	//boolean existsByLikerAndTargetAndActionType(UserProfile liker, UserProfile target, ActionType actionType);
	
	@Query("SELECT CASE WHEN COUNT(ua) > 0 THEN true ELSE false END FROM UserAction ua WHERE ua.liker = :liker AND ua.target = :target AND ua.actionType = :actionType")
    boolean existsByLikerAndTargetAndActionType(@Param("liker") User liker, @Param("target") User target, @Param("actionType") ActionType actionType);

}
