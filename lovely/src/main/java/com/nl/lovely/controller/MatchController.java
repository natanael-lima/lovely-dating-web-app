package com.nl.lovely.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nl.lovely.dto.ChatDTO;
import com.nl.lovely.dto.MatchDTO;
import com.nl.lovely.dto.MessageDTO;
import com.nl.lovely.entity.Match;
import com.nl.lovely.entity.User;
import com.nl.lovely.entity.UserProfile;
import com.nl.lovely.enums.ActionType;
import com.nl.lovely.exception.NotFoundException;
<<<<<<< HEAD
import com.nl.lovely.repository.UserProfileRepository;
=======
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
import com.nl.lovely.service.ChatService;
import com.nl.lovely.service.MatchService;
import com.nl.lovely.service.UserProfileService;
import com.nl.lovely.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:4200") // Reemplaza esto con el dominio de tu frontend
@RestController
@RequestMapping("/api/matches")
public class MatchController {
	@Autowired
    private MatchService matchService;
	@Autowired
    private ChatService chatService;
    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private UserProfileRepository userProfileRepository;

    // API que registra un like.
    @PostMapping("/like/{targetId}")
    public ResponseEntity<Map<String, String>> ActionLike(@PathVariable Long targetId, Authentication authentication) {
    
<<<<<<< HEAD
=======
    @PostMapping("/like/{targetId}")
    public ResponseEntity<Map<String, String>> ActionLike(@PathVariable Long targetId, Authentication authentication) {
    
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
    	UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername(); // Si el username se utiliza para buscar el UserProfile
        UserProfile actor = userProfileService.findByUsername(username); // Método para buscar el UserProfile por nombre de usuario
        UserProfile target = userProfileService.findUserById(targetId);
        matchService.processAction(actor, target,ActionType.LIKE);
        Map<String, String> response = new HashMap<>();
        response.put("message", "El like se registro correctamente");
        return ResponseEntity.ok(response);
    }
<<<<<<< HEAD
    // API que registra un dislike.
=======
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
    @PostMapping("/dislike/{targetId}")
    public ResponseEntity<Map<String, String>> ActionDislike(@PathVariable Long targetId, Authentication authentication) {
    
    	UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername(); // Si el username se utiliza para buscar el UserProfile
        UserProfile actor = userProfileService.findByUsername(username); // Método para buscar el UserProfile por nombre de usuario
        UserProfile target = userProfileService.findUserById(targetId);
        matchService.processAction(actor, target,ActionType.DISLIKE);
        Map<String, String> response = new HashMap<>();
        response.put("message", "El dislike se registro correctamente");
        return ResponseEntity.ok(response);
    }
    
    
<<<<<<< HEAD
    // API para obtener una lista de los match del usuario logueado.
=======
    //Metodo para obtener una lista de los match del usuario logueado
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
    @GetMapping("/{userId}")
    public ResponseEntity<List<MatchDTO>> findAll(@PathVariable Long userId) {
    
    	 List<MatchDTO> matches = matchService.findAllMatchByUserProfile(userId);

    	    if (matches.isEmpty()) {
    	        return ResponseEntity.noContent().build(); // Retorna un código 204 si la lista está vacía
    	    }

    	    return ResponseEntity.ok(matches); // Retorna la lista de Match encontrados
    }
    
<<<<<<< HEAD
    // API para obtener el chat de un usuario especifico con matchid.
=======
    //Metodo para obtener el chat de un usuario especifico
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
    @GetMapping("/chat/{matchId}")
    public ResponseEntity<ChatDTO> findChatByMatchId(@PathVariable Long matchId) {
        try {
            ChatDTO chatDto = chatService.findChatByMatchId(matchId);
            return ResponseEntity.ok(chatDto);
        } catch (RuntimeException e) {
            return ResponseEntity.noContent().build(); // Retorna un código 204 si el chat no está encontrado
        }
    }
    
<<<<<<< HEAD
    // API para obtener el match por 2 UserId
=======
    //Metodo para obtener el match by dos user id
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
    @GetMapping("/byusers/{userId1}/{userId2}")
    public ResponseEntity<MatchDTO> findMatchByUsersIds(@PathVariable Long userId1, @PathVariable Long userId2) {
    	try {
            MatchDTO matchDto = matchService.findMatchByProfileIds(userId1, userId2);
            return ResponseEntity.ok(matchDto);
        } catch (RuntimeException e) {
            return ResponseEntity.noContent().build(); // Retorna un código 204 si el match no está encontrado
        }
    }
    
<<<<<<< HEAD
    // API para confirmar match
    @GetMapping("/check-match")
    public ResponseEntity<Boolean> checkMatch(@RequestParam Long profileId1, @RequestParam Long profileId2) {
        UserProfile profile1 = userProfileRepository.findById(profileId1)
            .orElseThrow();
        UserProfile profile2 = userProfileRepository.findById(profileId2)
            .orElseThrow();

        boolean isMatch = matchService.confirmMatch(profile1, profile2);
        return ResponseEntity.ok(isMatch);
    }
=======
>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074
    
    /*@PostMapping("/like")
    public ResponseEntity<String> likeUser(@RequestParam Long likerId, @RequestParam Long targetId) {
    	try {
            UserProfile liker = userProfileService.findUserById(likerId);
            UserProfile target = userProfileService.findUserById(targetId);

            matchService.handleLike(liker, target);
            
            return ResponseEntity.ok("Liked user " + targetId);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
    
    @PostMapping("/dislike")
    public ResponseEntity<String> dislikeUser(@RequestParam Long likerId, @RequestParam Long targetId) {
    	try {
    		UserProfile liker = userProfileService.findUserById(likerId);
            UserProfile target = userProfileService.findUserById(targetId);

            //matchService.handleDislike(liker, target);
            
            return ResponseEntity.ok("Disliked user " + targetId);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }*/
    
}
