package com.nl.lovely.service;

import java.util.List;
import com.nl.lovely.dto.NotificationDTO;
import com.nl.lovely.entity.User;
import com.nl.lovely.response.ApiResponse;

public interface NotificationService {
	
	public void deleteNotification(Long id);
	public void saveNotifyMatch(User matchedUser, User currentUser);
	public void saveNotifyMessage(User sender, User receiver);
	
	public ApiResponse maskAsReadNotification(Long id,NotificationDTO notification);
	public List<NotificationDTO> getNotificationsForUser(Long userId);
	public boolean hasUnreadNotifications(Long receiverId);
}
