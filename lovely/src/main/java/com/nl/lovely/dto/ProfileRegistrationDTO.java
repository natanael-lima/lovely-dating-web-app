package com.nl.lovely.dto;

import com.nl.lovely.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRegistrationDTO {
	 Long id;
     String username;
     String password;
     String lastname;
	 String name;
	 RoleType role; 
	 PreferenceDTO preference;
	 ProfileDetailDTO profileDetail;

}
