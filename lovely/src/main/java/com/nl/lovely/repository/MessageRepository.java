package com.nl.lovely.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nl.lovely.dto.MessageDTO;
import com.nl.lovely.entity.Message;
@Repository
public interface MessageRepository extends JpaRepository<Message,Long>{
	
	@Query("SELECT m FROM Message m WHERE m.chat.id = :chatId")
	List<Message> findByChatId(@Param("chatId") Long chatId);

}
