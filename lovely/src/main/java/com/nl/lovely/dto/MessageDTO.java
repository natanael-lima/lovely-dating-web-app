package com.nl.lovely.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MessageDTO {

	private Long id;
    private String content;
    private LocalDateTime timestamp;
    private Long senderId;
    private Long chatId;

}
