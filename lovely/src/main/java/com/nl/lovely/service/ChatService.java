package com.nl.lovely.service;

import com.nl.lovely.dto.ChatDTO;
import com.nl.lovely.entity.Chat;

public interface ChatService {
	
	public Chat saveChat(Chat chat);
	
	public Chat findChatById(Long id);
	
	public void deleteChat(Long id);
	
	public ChatDTO findChatByMatchId(Long matchId); 
}	
