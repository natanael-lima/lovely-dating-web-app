package com.nl.lovely.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nl.lovely.entity.Message;
import com.nl.lovely.repository.ChatRepository;
import com.nl.lovely.repository.MessageRepository;
import com.nl.lovely.service.MessageService;
@Service
public class MessageServiceImp implements MessageService{

	 @Autowired
	private MessageRepository messageRepository;
	
	@Override
	public Message saveMessage(Message message) {
		// TODO Auto-generated method stub
		return messageRepository.save(message);
	}

	@Override
	public Message findMessageById(Long id) {
		// TODO Auto-generated method stub
		return messageRepository.findById(id).orElseThrow();
	}

}
