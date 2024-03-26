package com.nl.lovely.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nl.lovely.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
	public Optional<User> findByUsername(String username);
	public boolean existsByUsername(String username);
}
