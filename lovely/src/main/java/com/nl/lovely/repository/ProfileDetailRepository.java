package com.nl.lovely.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.nl.lovely.entity.ProfileDetail;
import com.nl.lovely.entity.User;
import com.nl.lovely.enums.ActionType;

import jakarta.transaction.Transactional;

@Repository
public interface ProfileDetailRepository extends JpaRepository<ProfileDetail,Long>{
	

	// Versión sin imagen
	@Modifying
	@Transactional
	@Query("update ProfileDetail set phone = :phone, gender = :gender, birthDate = :birthDate, description = :description, work = :work where user.id = :userId")
	void updateProfileDetail(@Param("userId") Long userId, @Param("phone") String phone, @Param("gender") String gender, @Param("birthDate") LocalDate birthDate, @Param("description") String description, @Param("work") String work);

	// Versión con imagen
	@Modifying
	@Transactional
	@Query("update ProfileDetail set phone = :phone, gender = :gender, birthDate = :birthDate, description = :description, work = :work, photo = :photo, photoFileName = :photoFileName where user.id = :userId")
	void updateProfileDetailWithPhoto(@Param("userId") Long userId, @Param("phone") String phone, @Param("gender") String gender, @Param("birthDate") LocalDate birthDate, @Param("description") String description, @Param("work") String work, @Param("photo") byte[] photo, @Param("photoFileName") String photoFileName);

	
}
