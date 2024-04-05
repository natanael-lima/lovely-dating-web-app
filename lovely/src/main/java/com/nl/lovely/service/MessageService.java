package com.nl.lovely.service;

import com.nl.lovely.entity.Message;

public interface MessageService {

	public Message saveMessage(Message message);
	
	public Message findMessageById(Long id);
	
}
