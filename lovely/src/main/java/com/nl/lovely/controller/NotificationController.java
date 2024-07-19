package com.nl.lovely.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nl.lovely.dto.NotificationDTO;
import com.nl.lovely.dto.ProfileDTO;
import com.nl.lovely.entity.User;
import com.nl.lovely.repository.UserRepository;
import com.nl.lovely.response.ApiResponse;
import com.nl.lovely.service.NotificationService;
import com.nl.lovely.service.UserService;

@CrossOrigin(origins = "http://localhost:4200") // Reemplaza esto con el dominio de tu frontend
@RestController
@RequestMapping("/api/notification")
public class NotificationController {
	@Autowired
	private NotificationService notificationService;
	@Autowired
    private UserService userService;
	@Autowired
    private UserRepository userRepository;
	
	//************************** API para actualizar los datos del usuario. ************************** new
    @PostMapping("/register-notification-message/{id}")
    public ResponseEntity<ApiResponse> registrationMessage(@PathVariable Long id)
    {  // Registrar notifiacion para el usuario que le envio el mensaje si sender envia en las notifiaciones del otro usuario debe estar registrado que envio porque eso save notifiacion
    	Long currentId = getCurrentUserId();
        User sender = userService.findUserById(currentId);
        User receiver = userService.findUserById(id);
        
        notificationService.saveNotifyMessage(sender,receiver);
        return ResponseEntity.ok( new ApiResponse ("Mensaje Registrado"));
    }
    //************************** API para obtener todas las notificaciones de un user.**************************
    @GetMapping("/filter-my-notification")
    public ResponseEntity<List<NotificationDTO>> getFilterMyNotifications() {
    	Long userId = getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<NotificationDTO> listNotifications = notificationService.getNotificationsForUser(userId);
        if (listNotifications != null) {
            return ResponseEntity.ok(listNotifications);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    //************************** API para actualizar los datos del usuario. ************************** new
    @PutMapping("/mask-notification/{id}")
    public ResponseEntity<ApiResponse> maskNotificationAsRead(@PathVariable Long id, @RequestBody NotificationDTO notification)
    {  

        return ResponseEntity.ok(notificationService.maskAsReadNotification(id,notification));
    }
    // Nuevo endpoint para verificar notificaciones no leídas
    @GetMapping("/has-unread-notifications")
    public ResponseEntity<Boolean> hasUnreadNotifications() {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean hasUnread = notificationService.hasUnreadNotifications(userId);
        return ResponseEntity.ok(hasUnread);
    }
    
    // Método auxiliar para obtener el ID del usuario actual
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findByUsername(username).orElse(null);
            if (user != null) {
            	 System.out.println("id de user: "+user.getId());
                return user.getId();
               
            }
        }
        return null;
    }
}  
    
	
