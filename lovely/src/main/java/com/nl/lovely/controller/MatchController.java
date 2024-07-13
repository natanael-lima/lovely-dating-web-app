package com.nl.lovely.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.nl.lovely.dto.ChatDTO;
import com.nl.lovely.dto.MatchDTO;
import com.nl.lovely.entity.User;
import com.nl.lovely.enums.ActionType;
import com.nl.lovely.repository.UserRepository;
import com.nl.lovely.response.ApiResponse;
import com.nl.lovely.service.ChatService;
import com.nl.lovely.service.MatchService;
import com.nl.lovely.service.UserService;


@CrossOrigin(origins = "http://localhost:4200") // Reemplaza esto con el dominio de tu frontend
@RestController
@RequestMapping("/api/matches")
public class MatchController {
	@Autowired
    private MatchService matchService;
	@Autowired
    private ChatService chatService;
    
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    // API que registra un like.
    @PostMapping("/like/{targetId}")
    public ResponseEntity<Map<String, String>> ActionLike(@PathVariable Long targetId, Authentication authentication) {
    
    	UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername(); // Si el username se utiliza para buscar el UserProfile
        User autor = userService.findByUsername2(username); // Método para buscar el UserProfile por nombre de usuario
        User target = userService.findUserById(targetId);
        matchService.processAction(autor, target,ActionType.LIKE);
        Map<String, String> response = new HashMap<>();
        response.put("message", "El like se registro correctamente");
        return ResponseEntity.ok(response);
    }
    // API que registra un dislike.
    @PostMapping("/dislike/{targetId}")
    public ResponseEntity<Map<String, String>> ActionDislike(@PathVariable Long targetId, Authentication authentication) {
    
    	UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername(); // Si el username se utiliza para buscar el UserProfile
        User autor = userService.findByUsername2(username); // Método para buscar el UserProfile por nombre de usuario
        User target = userService.findUserById(targetId);
        matchService.processAction(autor, target,ActionType.DISLIKE);
        Map<String, String> response = new HashMap<>();
        response.put("message", "El dislike se registro correctamente");
        return ResponseEntity.ok(response);
    }
    // API para confirmar match devolviendo un bool
    @DeleteMapping("/delete-match/{id}")
    public ResponseEntity<ApiResponse> deleteMatch(@PathVariable Long id) {
    	try {
            matchService.deleteMatch(id);
            return ResponseEntity.ok(new ApiResponse("Se eliminó correctamente el match con el chat"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Match not found with id " + id));
        }
    }
    
    // API para confirmar match devolviendo un bool
    @GetMapping("/check-match")
    public ResponseEntity<Boolean> checkMatch(@RequestParam Long profileId1, @RequestParam Long profileId2) {
        User profile1 = userRepository.findById(profileId1)
            .orElseThrow();
        User profile2 = userRepository.findById(profileId2)
            .orElseThrow();

        boolean isMatch = matchService.confirmMatch(profile1, profile2);
        return ResponseEntity.ok(isMatch);
    }
    
    
    // API para obtener una lista de los match del usuario logueado.
    @GetMapping("/my-matches/{id}")
    public ResponseEntity<List<MatchDTO>> findAll(@PathVariable Long id) {
    	 //Long idUser = getCurrentUserId();
    	 List<MatchDTO> matches = matchService.findAllMatchByUserProfile(id);

    	    if (matches.isEmpty()) {
    	        return ResponseEntity.noContent().build(); // Retorna un código 204 si la lista está vacía
    	    }

    	    return ResponseEntity.ok(matches); // Retorna la lista de Match encontrados
    }
    
    // API para obtener el chat de un usuario especifico con matchid.
    @GetMapping("/chat/{matchId}")
    public ResponseEntity<ChatDTO> findChatByMatchId(@PathVariable Long matchId) {
        try {
            ChatDTO chatDto = chatService.findChatByMatchId(matchId);
            return ResponseEntity.ok(chatDto);
        } catch (RuntimeException e) {
            return ResponseEntity.noContent().build(); // Retorna un código 204 si el chat no está encontrado
        }
    }
    
    // API para obtener el match por 2 UserId
    @GetMapping("/byusers/{userId1}/{userId2}")
    public ResponseEntity<MatchDTO> findMatchByUsersIds(@PathVariable Long userId1, @PathVariable Long userId2) {
    	try {
            MatchDTO matchDto = matchService.findMatchByProfileIds(userId1, userId2);
            return ResponseEntity.ok(matchDto);
        } catch (RuntimeException e) {
            return ResponseEntity.noContent().build(); // Retorna un código 204 si el match no está encontrado
        }
    }
    
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findByUsername(username).orElse(null);
            if (user != null) {
            	 System.out.println("id de user: "+user.getId());
                return user.getId();
               
            }
        }
        return null;
    }
    
    
}
