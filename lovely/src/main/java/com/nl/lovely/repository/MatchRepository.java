package com.nl.lovely.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nl.lovely.entity.Match;

@Repository
public interface MatchRepository extends JpaRepository<Match,Long> {

}
