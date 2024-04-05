package com.nl.lovely.request;

import com.nl.lovely.enums.RoleType;
import jakarta.persistence.Lob;
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
    byte[] photo;
    String location;
    String gender;
    String age;
    String likeGender;
    Integer maxAge;
    Integer minAge;
}
