package com.nl.lovely.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nl.lovely.entity.Chat;
import com.nl.lovely.exception.NotFoundException;
import com.nl.lovely.repository.ChatRepository;
import com.nl.lovely.service.ChatService;
@Service
public class ChatServiceImp implements ChatService{
    @Autowired
	private ChatRepository chatRepository;
    
	@Override
	public Chat saveChat(Chat chat) {
		// TODO Auto-generated method stub
		return chatRepository.save(chat);
	}

	@Override
	public Chat findChatById(Long id) {
		// TODO Auto-generated method stub
		return chatRepository.findById(id).orElseThrow(() -> new NotFoundException("Chat not found with id: " + id));
	}

	@Override
	public void deleteChat(Long id) {
		// TODO Auto-generated method stub
		chatRepository.deleteById(id);
	}

}
