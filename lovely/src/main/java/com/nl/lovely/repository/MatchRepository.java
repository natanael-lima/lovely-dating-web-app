package com.nl.lovely.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.nl.lovely.entity.Match;

@Repository
public interface MatchRepository extends JpaRepository<Match,Long> {
	
	@Query("SELECT m FROM Match m WHERE m.profile1.id = :profileId OR m.profile2.id = :profileId")
	List<Match> findAllByProfile1IdOrProfile2Id(Long profileId);
	
	 @Query("SELECT m FROM Match m WHERE (m.profile1.id = :profileId1 AND m.profile2.id = :profileId2) OR (m.profile1.id = :profileId2 AND m.profile2.id = :profileId1)")
	 Optional<Match> findMatchByProfileIds(@Param("profileId1") Long profileId1, @Param("profileId2") Long profileId2);
	
}
