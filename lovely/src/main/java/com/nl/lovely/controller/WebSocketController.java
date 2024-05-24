package com.nl.lovely.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nl.lovely.dto.MessageDTO;
import com.nl.lovely.entity.Chat;
import com.nl.lovely.entity.Match;
import com.nl.lovely.entity.Message;
import com.nl.lovely.entity.UserProfile;
import com.nl.lovely.service.ChatService;
import com.nl.lovely.service.MatchService;
import com.nl.lovely.service.MessageService;
import com.nl.lovely.service.UserProfileService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:4200") // Reemplaza esto con el dominio de tu frontend
@RestController
@RequestMapping()
@RequiredArgsConstructor
public class WebSocketController {
	
	@Autowired
	private ChatService chatService;
	
	@Autowired
	private UserProfileService userProfileService;
	
	@Autowired
	private MessageService messageService;

<<<<<<< HEAD
	// API para chat en tiempo real usando websocket.
=======
	
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
    @MessageMapping("/chat/{chatId}/{userId}")
    @SendTo("/topic/{chatId}")
    public MessageDTO  chat3(@DestinationVariable Long chatId, @DestinationVariable Long userId, MessageDTO messageDTO) {
        // Obtener el chat correspondiente al chatId
        Chat chat = chatService.findChatById(chatId);
        // Asignar el chat al mensaje
        messageDTO.setChatId(chat.getId());
        // Asignar el ID del remitente al mensaje
        UserProfile sender = userProfileService.findUserById(userId);
        messageDTO.setSenderId(sender.getId());
        messageDTO.setTimestamp(LocalDateTime.now());
        System.out.println("backend message:"+messageDTO.getContent());
        // Guardar el mensaje en la base de datos
        messageService.saveMessage(messageDTO);
        // Devolver el mensaje para enviarlo al topic correspondiente al chatId
        return messageDTO;
    }

<<<<<<< HEAD
    // API para conseguir los mensajes de un chat.
=======
    
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
    @GetMapping("/api/chats/{chatId}/messages")
    public ResponseEntity<List<MessageDTO>> getMessagesForChat(@PathVariable Long chatId) {
        //return messageService.findMessagesByChatId(chatId);
        try {
        	List<MessageDTO> messages = messageService.findMessagesByChatId(chatId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            // Manejo de errores
        	 e.printStackTrace();
        	 return ResponseEntity.badRequest().build();
        
        }
        
    }
    
    /*@MessageMapping("/chat/{roomId}")
	@SendTo("/topic/{roomId}")
	public ChatMessage chat(@DestinationVariable String roomId, ChatMessage message) {
		System.out.println(message);
		return new ChatMessage(message.getMessage(), message.getUser());
	}*/

}
