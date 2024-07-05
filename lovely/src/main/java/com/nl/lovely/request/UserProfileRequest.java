package com.nl.lovely.request;

import java.time.LocalDateTime;

import com.nl.lovely.enums.RoleType;
import jakarta.persistence.Lob;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileRequest {
	
	Long id; // ID del perfil del usuario
	Long userId; // ID del usuario logueado 
    String location;
    String gender;
    String age;
    String likeGender;
    Integer maxAge;
    Integer minAge;
    LocalDateTime timestamp;
}
