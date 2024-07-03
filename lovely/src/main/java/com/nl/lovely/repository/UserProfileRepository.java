package com.nl.lovely.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nl.lovely.entity.User;
import com.nl.lovely.entity.UserProfile;
import com.nl.lovely.enums.ActionType;

import jakarta.transaction.Transactional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile,Long>{
	@Query("SELECT u.profile FROM User u WHERE u.username = :username")
    UserProfile findByUsername(@Param("username") String username);
	
	Optional<UserProfile> findByUserId(Long userId);
	
	// Método para actualizar sin imagen 
	@Modifying()
	@Transactional
	@Query("update UserProfile set location=:location, gender=:gender,  age=:age,  likeGender=:likeGender ,  maxAge=:maxAge ,  minAge=:minAge  where id = :id")
    void updateProfileData(@Param(value = "id") Long id,  @Param(value = "location") String location, @Param(value = "gender") String gender, @Param(value = "age") String age, @Param(value = "likeGender") String likeGender, @Param(value = "maxAge") Integer maxAge, @Param(value = "minAge") Integer minAge);

	
	// Método para actualizar con imagen
	@Modifying
	@Transactional
	@Query("update UserProfile set photo=:photo, photoFileName=:photoFileName, location=:location, gender=:gender,  age=:age,  likeGender=:likeGender ,  maxAge=:maxAge ,  minAge=:minAge where  id = :id")
    void updatePhotoAndProfile(@Param(value = "id") Long id, @Param("photo") byte[] photo, @Param("photoFileName") String photoFileName,  @Param(value = "location") String location, @Param(value = "gender") String gender, @Param(value = "age") String age, @Param(value = "likeGender") String likeGender, @Param(value = "maxAge") Integer maxAge, @Param(value = "minAge") Integer minAge);  


	// Para actualizar la imagen del perfil
	//@Modifying
	//@Query("UPDATE UserProfile SET photo=:photo, photoFileName=:photoFileName WHERE id = :id")
	//void updateProfileImage(@Param("id") Long id, @Param("photo") byte[] photo, @Param("photoFileName") String photoFileName);
	
	// Método para buscar perfiles de usuario que cumplan con las preferencias del usuario actual
	@Query("SELECT up FROM UserProfile up WHERE up.gender = :likeGender AND up.age BETWEEN :minAge AND :maxAge")
    List<UserProfile> findFilteredUserProfiless(@Param("likeGender") String likeGender, @Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);
	
	// Método filtrar los perfiles por preferencia
	@Query("SELECT up FROM UserProfile up WHERE up.gender = :likeGender " +
	           "AND up.age BETWEEN :minAge AND :maxAge " +
	           "AND NOT EXISTS (SELECT ua FROM UserAction ua WHERE ua.liker = :currentUser " +
	           "AND ua.target = up AND ua.actionType IN (:excludedActions))")
	List<UserProfile> findFilteredUserProfiles(@Param("likeGender") String likeGender,
	                                               @Param("minAge") Integer minAge,
	                                               @Param("maxAge") Integer maxAge,
	                                               @Param("currentUser") UserProfile currentUser,
	                                               @Param("excludedActions") List<ActionType> excludedActions);
	
	// Método para buscar los match de un respectivo usuario con su id
	@Query("SELECT DISTINCT up.user FROM UserProfile up " +
	           "JOIN Match m ON up.id = m.profile1.id OR up.id = m.profile2.id " +
	           "WHERE up.id != :userId AND (m.profile1.id = :userId OR m.profile2.id = :userId)")
	List<User> findUsersByMatch(@Param("userId") Long userId);
	
	
}
