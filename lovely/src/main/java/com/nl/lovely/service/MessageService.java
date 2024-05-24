package com.nl.lovely.service;

import java.util.List;

import com.nl.lovely.dto.MessageDTO;
import com.nl.lovely.entity.Message;

public interface MessageService {

	public MessageDTO saveMessage(MessageDTO message);
	
	public List<MessageDTO> findMessagesByChatId(Long chatId);
	
}
