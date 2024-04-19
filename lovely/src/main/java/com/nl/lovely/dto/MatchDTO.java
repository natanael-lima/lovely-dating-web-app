package com.nl.lovely.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchDTO {
	private Long id;
    private LocalDateTime matchedAt;
    private Long profile1;
    private Long profile2;
}
