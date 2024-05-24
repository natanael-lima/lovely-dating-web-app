package com.nl.lovely.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nl.lovely.entity.User;
import com.nl.lovely.entity.UserProfile;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
	Optional<User> findByUsername(String username);
	public boolean existsByUsername(String username);
	//Optional<User> findByUserId(Long userId);
	@Modifying()
	@Query("update User set lastname=:lastname, name=:name where id = :id")
    void updateUser(@Param(value = "id") Long id,   @Param(value = "lastname") String lastname, @Param(value = "name") String name);
}
