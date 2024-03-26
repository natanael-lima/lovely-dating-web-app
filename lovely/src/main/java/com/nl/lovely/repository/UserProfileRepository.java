package com.nl.lovely.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nl.lovely.entity.User;
import com.nl.lovely.entity.UserProfile;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile,Long>{
	@Query("SELECT u.profile FROM User u WHERE u.username = :username")
    UserProfile findByUsername(@Param("username") String username);
}
