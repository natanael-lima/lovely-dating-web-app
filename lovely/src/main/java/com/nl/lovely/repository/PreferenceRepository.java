package com.nl.lovely.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.nl.lovely.entity.Preference;

import jakarta.transaction.Transactional;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference,Long> {
		//@Modifying
		//@Transactional
		//@Query("update Preference p set p.location = :location, p.likeGender = :likeGender, p.maxAge = :maxAge, p.minAge = :minAge, p.distance = :distance, p.interests = :interests where p.user.id = :id")
		//void updatePreference(@Param("id") Long id, @Param("maxAge") Integer maxAge, @Param("minAge") Integer minAge, @Param("likeGender") String likeGender, @Param("location") String location, @Param("distance") String distance, @Param("interests") List<String> interests);


	 	@Modifying
	    @Transactional
	    @Query("update Preference p set p.location = :location, p.likeGender = :likeGender, p.maxAge = :maxAge, p.minAge = :minAge, p.distance = :distance where p.user.id = :id")
	    void updatePreference(@Param("id") Long id, @Param("maxAge") Integer maxAge, @Param("minAge") Integer minAge, @Param("likeGender") String likeGender, @Param("location") String location, @Param("distance") Integer distance);
}
