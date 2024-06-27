package com.nl.lovely.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCompleteDTO {
		Long id;
		String username;
	    String lastname;
		String name;
	    byte[] photo;
	    String photoFileName;
	    String location;
	    String gender;
	    String age;
	    String likeGender;
	    Integer maxAge;
	    Integer minAge;
}
