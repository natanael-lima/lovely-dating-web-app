package com.nl.lovely.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDetailDTO {
	 	 Long id;
		 String phone;
	     String gender;
	     LocalDate birthDate;
	     String description;
	     String work;
	     byte[] photo;
	     String photoFileName;
	     LocalDateTime timestamp;
}
