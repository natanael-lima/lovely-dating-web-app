package com.nl.lovely.dto;

import java.util.List;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreferenceDTO {
		Long id;
	 	Integer maxAge;
	    Integer minAge;
	    String likeGender;
	    String location;
	    Integer distance;
	    @Size(max = 4, message = "You can select up to 4 interests only")
	    List<String> interests;
}
