package com.nl.lovely.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.nl.lovely.entity.User;
import com.nl.lovely.enums.ActionType;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
	//Buscar el usuario por id informacion completa
	//@Query("SELECT p FROM User p LEFT JOIN FETCH p.preference LEFT JOIN FETCH p.profileDetail WHERE p.id = :id")
	//Optional<User> findByIdWithPreferenceAndProfileDetail(@Param("id") Long id);
	
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
	
	public boolean existsByUsername(String username); // Devuelve un bool por la existencia de un username
	
	Optional<User> findByProfileDetailId(Long id); // Buscar usuario by id de profile
	
	Optional<User> findByPreferenceId(Long id); // Buscar usuario by de preference
	
	Optional<User> findByUsername(String username); // Buscar by username un User

	Optional<User> findById(Long id); // Buscar by ID un User
	
	@Query("SELECT u FROM User u WHERE u.username = :username")
    User findByUsername2(@Param("username") String username);
	//@Query("SELECT u.profile FROM User u WHERE u.username = :username")
    //User findByUsername2(@Param("username") String username);
	
	//Actualizar User datos basicos
	@Modifying()
	@Transactional
	@Query("update User set lastname=:lastname, name=:name where id = :id")
    void updateUserQuery(@Param(value = "id") Long id,   @Param(value = "lastname") String lastname, @Param(value = "name") String name);

	// Método para buscar perfiles de usuario que cumplan con las preferencias del usuario actual
	@Query("SELECT u FROM User u " +
		       "JOIN u.profileDetail pd " +
		       "WHERE (u.preference.likeGender = :currentUserGender AND pd.gender = :likeGender)" +  // Filtrar por género del perfil que coincide con el género que le gusta al usuario
		       "AND (YEAR(CURRENT_DATE) - YEAR(pd.birthDate)) BETWEEN :minAge AND :maxAge " +
		       "AND NOT EXISTS (SELECT ua FROM UserAction ua WHERE ua.liker = :currentUser " +
		       "AND ua.target = u AND ua.actionType IN (:excludedActions))")
		List<User> findFilteredUsers(
		        @Param("likeGender") String likeGender,
		        @Param("minAge") Integer minAge,
		        @Param("maxAge") Integer maxAge,
		        @Param("currentUser") User currentUser,
		        @Param("currentUserGender") String currentUserGender,
		        @Param("excludedActions") List<ActionType> excludedActions
		);

	
	//Método para buscar los match de un respectivo usuario con su id
	@Query("SELECT DISTINCT u FROM User u " +
		       "JOIN Match m ON u.id = m.profile1.id OR u.id = m.profile2.id " +
		       "WHERE u.id != :id AND (m.profile1.id = :id OR m.profile2.id = :id)")
		List<User> findUsersByMatch(@Param("id") Long id);
	
	// Método filtrar los perfiles por preferencia
	/*@Query("SELECT up FROM UserProfile up WHERE up.gender = :likeGender " +
	           "AND up.age BETWEEN :minAge AND :maxAge " +
	           "AND NOT EXISTS (SELECT ua FROM UserAction ua WHERE ua.liker = :currentUser " +
	           "AND ua.target = up AND ua.actionType IN (:excludedActions))")
	List<UserProfile> findFilteredUserProfiles(@Param("likeGender") String likeGender,
	                                               @Param("minAge") Integer minAge,
	                                               @Param("maxAge") Integer maxAge,
	                                               @Param("currentUser") UserProfile currentUser,
	                                               @Param("excludedActions") List<ActionType> excludedActions);*/
	
	
}
