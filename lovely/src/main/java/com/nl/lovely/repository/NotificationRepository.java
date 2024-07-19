package com.nl.lovely.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nl.lovely.entity.Notification;
import com.nl.lovely.entity.User;
@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long>{
	List<Notification> findByReceiverId(Long id);
	
	public Boolean existsByReceiverIdAndIsUnread(Long receiverId, Boolean isUnread);
}
