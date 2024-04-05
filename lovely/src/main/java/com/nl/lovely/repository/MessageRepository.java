package com.nl.lovely.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nl.lovely.entity.Message;
@Repository
public interface MessageRepository extends JpaRepository<Message,Long>{

}
