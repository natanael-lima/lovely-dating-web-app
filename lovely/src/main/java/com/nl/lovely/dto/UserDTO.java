package com.nl.lovely.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
     Long id;
     String username;
     String lastname;
	 String name;
	 
	}