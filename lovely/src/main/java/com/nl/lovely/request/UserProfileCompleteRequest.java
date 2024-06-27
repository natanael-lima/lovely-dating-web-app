package com.nl.lovely.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileCompleteRequest {
	
	Long id; // ID del perfil del usuario
	Long userId; // ID del usuario logueado 
	byte[] photo;
	String photoFileName;
    String location;
    String gender;
    String age;
    String likeGender;
    Integer maxAge;
    Integer minAge;
}
