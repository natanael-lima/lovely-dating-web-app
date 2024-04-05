package com.nl.lovely.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
	    Long id;
	    String username;
	    String lastname;	
	    String name;
	    
}
