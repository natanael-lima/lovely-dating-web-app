package com.nl.lovely.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.nl.lovely.entity.UserProfile;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile,Long>{
	@Query("SELECT u.profile FROM User u WHERE u.username = :username")
    UserProfile findByUsername(@Param("username") String username);
	Optional<UserProfile> findByUserId(Long userId);
	@Modifying()
	@Query("update UserProfile set location=:location, gender=:gender,  age=:age,  likeGender=:likeGender ,  maxAge=:maxAge ,  minAge=:minAge  where id = :id")
    void updateUserProfile(@Param(value = "id") Long id,  @Param(value = "location") String location, @Param(value = "gender") String gender, @Param(value = "age") String age, @Param(value = "likeGender") String likeGender, @Param(value = "maxAge") Integer maxAge, @Param(value = "minAge") Integer minAge);
}
