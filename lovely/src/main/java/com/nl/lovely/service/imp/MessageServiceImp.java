package com.nl.lovely.service.imp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nl.lovely.dto.MessageDTO;
import com.nl.lovely.entity.Chat;
import com.nl.lovely.entity.Message;
import com.nl.lovely.entity.User;
import com.nl.lovely.repository.MessageRepository;
import com.nl.lovely.service.ChatService;
import com.nl.lovely.service.MessageService;
import com.nl.lovely.service.UserService;
@Service
public class MessageServiceImp implements MessageService{

	@Autowired
	private MessageRepository messageRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private ChatService chatService;
	
	@Override
	public MessageDTO saveMessage(MessageDTO messageDTO) {
		Message message = convertToEntity(messageDTO);
	    message = messageRepository.save(message);

	    // Aquí puedes volver a convertir el objeto Message actualizado a MessageDTO si es necesario
	    MessageDTO savedMessageDTO = convertToDTO(message);

	    return savedMessageDTO;
	}
	
	private MessageDTO convertToDTO(Message message) {
		Long senderId = message.getSender() != null ? message.getSender().getId() : null;
	    Long chatId = message.getChat() != null ? message.getChat().getId() : null;

	    return MessageDTO.builder()
	            .id(message.getId())
	            .content(message.getContent())
	            .timestamp(message.getTimestamp())
	            .senderId(senderId)
	            .chatId(chatId)
	            .build();
	}
	private Message convertToEntity(MessageDTO messageDTO) {
	    Message message = new Message();
	    
	    // Aquí puedes asignar los valores del DTO al objeto Message
	    message.setId(messageDTO.getId());
	    message.setContent(messageDTO.getContent());
	    message.setTimestamp(messageDTO.getTimestamp());
	    
	    // Aquí debes establecer las relaciones, como el sender y el chat
	    User sender = userService.findUserById(messageDTO.getSenderId());
	    if(sender == null) {
	        throw new RuntimeException("Sender with ID " + messageDTO.getSenderId() + " not found");
	    }
	    message.setSender(sender);
	    
	    Chat chat = chatService.findChatById(messageDTO.getChatId());
	    if(chat == null) {
	        throw new RuntimeException("Chat with ID " + messageDTO.getChatId() + " not found");
	    }
	    message.setChat(chat);
	    
	    // Aquí debes establecer las relaciones, como el sender y el chat
	    //UserProfile sender = new UserProfile(); // Supongamos que tienes una clase User
	    //sender.setId(messageDTO.getSenderId());
	    //message.setSender(sender);
	    
	    //Chat chat = new Chat(); // Supongamos que tienes una clase Chat
	    //chat.setId(messageDTO.getChatId());
	    //message.setChat(chat);

	    return message;
	}


	@Override
	public List<MessageDTO> findMessagesByChatId(Long chatId) {
		 List<Message> messages = messageRepository.findByChatId(chatId);
		    List<MessageDTO> messageDTOs = new ArrayList<>();

		    for (Message message : messages) {
		        messageDTOs.add(convertToDTO(message));
		    }

		    System.out.println("Chat ID: " + chatId + ", Messages: " + messageDTOs);
		    return messageDTOs;
	}

}
