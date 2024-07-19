package com.nl.lovely.service.imp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nl.lovely.dto.NotificationDTO;
import com.nl.lovely.dto.PreferenceDTO;
import com.nl.lovely.dto.ProfileDTO;
import com.nl.lovely.dto.ProfileDetailDTO;
import com.nl.lovely.entity.Match;
import com.nl.lovely.entity.Notification;
import com.nl.lovely.entity.Preference;
import com.nl.lovely.entity.ProfileDetail;
import com.nl.lovely.entity.User;
import com.nl.lovely.repository.NotificationRepository;
import com.nl.lovely.response.ApiResponse;
import com.nl.lovely.service.NotificationService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class NotificationServiceImp implements NotificationService{
	@Autowired
    private NotificationRepository notificationRepository;
	
	@Override
	public void saveNotifyMatch(User matchedUser, User currentUser) {
		// TODO Auto-generated method stub
		 String content = "Tienes un nuevo match con ";// + matchedUser.getName();

		 Notification notification = Notification.builder()
		            .content(content)
		            .time(LocalDateTime.now())
		            .isUnread(true)
		            .receiver(currentUser)
		            .sender(matchedUser)
		            .build();

		    notificationRepository.save(notification);
	}

	@Override
	public void saveNotifyMessage(User sender, User receiver) {
		// Verificar que sender y receiver no sean el mismo usuario
	    if (sender.equals(receiver)) {
	        return; // No registrar notificación si sender y receiver son el mismo
	    }
	    
		// String messageContent en parametro para ver el mensaje
		String content = "Nuevo mensaje de "; //+ sender.getName();
		//String content = "Tienes un nuevo mensaje de " + sender.getName() + ": " + messageContent;
		
		 Notification notification = Notification.builder()
		            .content(content)
		            .time(LocalDateTime.now())
		            .isUnread(true)
		            .receiver(receiver)
		            .sender(sender)
		            .build();

	    notificationRepository.save(notification);
	}
	@Override
	public void deleteNotification(Long id) {
		// TODO Auto-generated method stub
		 Optional<Notification> noti = notificationRepository.findById(id);
	     if (noti.isPresent()) {
	    	 notificationRepository.delete(noti.get());
	     } else {
	            throw new EntityNotFoundException("Notification not found with id " + id);
	     }
	}
	@Override
	public List<NotificationDTO> getNotificationsForUser(Long userId) {
		
		 List<Notification> notifications = notificationRepository.findByReceiverId(userId);;

		 return notifications.stream()
		            .map(this::convertToDTONotification)
		            .collect(Collectors.toList());
	}
	
	// Método para convertir Notification
	private NotificationDTO convertToDTONotification(Notification notification) {
	        return NotificationDTO.builder()
	        		.id(notification.getId())
	                .content(notification.getContent())
	                .time(notification.getTime())
	                .isUnread(notification.getIsUnread())
	                .receiver(convertToProfileDTO(notification.getReceiver()))
	                .sender(convertToProfileDTO(notification.getSender()))
	                .build();
	}
	// Método para convertir User a ProfileDTO
	private ProfileDTO convertToProfileDTO(User profile) {
	        return ProfileDTO.builder()
	                .id(profile.getId())
	                .username(profile.getUsername())
	                .lastname(profile.getLastname())
	                .name(profile.getName())
	                .role(profile.getRole())
	                .preference(convertToPreferenceDTO(profile.getPreference())) // Convertir entidad a DTO
	                .profileDetail(convertToProfileDetailDTO(profile.getProfileDetail())) // Convertir entidad a DTO
	                .build();
	   }
	
	private PreferenceDTO convertToPreferenceDTO(Preference preference) {
	    if (preference == null) {
	        return null;
	    }
	    return PreferenceDTO.builder()
	            .id(preference.getId())
	            .maxAge(preference.getMaxAge())
	            .minAge(preference.getMinAge())
	            .likeGender(preference.getLikeGender())
	            .location(preference.getLocation())
	            .distance(preference.getDistance())
	            .interests(preference.getInterests())
	            .build();
	}

	private ProfileDetailDTO convertToProfileDetailDTO(ProfileDetail profileDetail) {
	    if (profileDetail == null) {
	        return null;
	    }
	    return ProfileDetailDTO.builder()
	            .id(profileDetail.getId())
	            .phone(profileDetail.getPhone())
	            .gender(profileDetail.getGender())
	            .birthDate(profileDetail.getBirthDate())
	            .description(profileDetail.getDescription())
	            .work(profileDetail.getWork())
	            .photo(profileDetail.getPhoto())
	            .photoFileName(profileDetail.getPhotoFileName())
	            .timestamp(profileDetail.getTimestamp())
	            .build();
	}

	@Override
	public ApiResponse maskAsReadNotification(Long id, NotificationDTO notification) {
		// TODO Auto-generated method stub
		Notification noti = notificationRepository.findById(id)
                 .orElseThrow(() -> new RuntimeException("Notification no encontrado"));
	
		 	// Actualizar solo los campos que pueden ser modificados
		 	noti.setIsUnread(false);
		   // Guardar el usuario en la base de datos

		 	notificationRepository.save(noti);
	        return new ApiResponse("El usuario se actualizo satisfactoriamente");
	}
	 // Nuevo método para verificar notificaciones no leídas
    public boolean hasUnreadNotifications(Long receiverId) {
        return notificationRepository.existsByReceiverIdAndIsUnread(receiverId, true);
    }
	
}
