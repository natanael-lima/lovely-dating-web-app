package com.nl.lovely.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.nl.lovely.entity.Chat;
import com.nl.lovely.entity.ChatMessage;
import com.nl.lovely.entity.Match;
import com.nl.lovely.entity.Message;
import com.nl.lovely.service.ChatService;
import com.nl.lovely.service.MatchService;
import com.nl.lovely.service.MessageService;

@Controller
public class WebSocketController {
	
	/*@MessageMapping("/chat/{roomId}")
	@SendTo("/topic/{roomId}")
	public ChatMessage chat(@DestinationVariable String roomId, ChatMessage message) {
		System.out.println(message);
		return new ChatMessage(message.getMessage(), message.getUser());
	}*/

	@Autowired
    private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private ChatService chatService;
	
	@Autowired
	private MessageService messageService;

    @MessageMapping("/chat/{chatId}")
    public void chat(@DestinationVariable Long chatId, Message message) {
        // Aquí deberías obtener la conversación (chat) correspondiente al chatId y asignarla al mensaje.
        // Luego, puedes guardar el mensaje en la base de datos y enviarlo a través del WebSocket.
        
        // Por ejemplo:
        Chat chat = chatService.findChatById(chatId); // Suponiendo que tienes un servicio para obtener la conversación por su ID
        message.setChat(chat);
        // Guardar el mensaje en la base de datos
        messageService.saveMessage(message);

        messagingTemplate.convertAndSend("/topic/chat/" + chatId, message);
    }
	
}
