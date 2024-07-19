package com.nl.lovely.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
	 Long id;
     String content;
     LocalDateTime time;
     Boolean isUnread;
     ProfileDTO receiver;
     ProfileDTO sender;

}
