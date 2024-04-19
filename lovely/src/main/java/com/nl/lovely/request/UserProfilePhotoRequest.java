package com.nl.lovely.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfilePhotoRequest {
	Long id; // ID del perfil del usuario
	Long userId; // ID del usuario logueado 
    byte[] photo;
	String photoFileName;
}

	
	
 
