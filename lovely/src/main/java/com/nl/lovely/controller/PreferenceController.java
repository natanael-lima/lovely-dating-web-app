package com.nl.lovely.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nl.lovely.dto.PreferenceDTO;
import com.nl.lovely.response.ApiResponse;
import com.nl.lovely.service.PreferenceService;
import com.nl.lovely.service.UserService;

@CrossOrigin(origins = "http://localhost:4200") // Reemplaza esto con el dominio de tu frontend
@RestController
@RequestMapping("/api/preference")
public class PreferenceController {
	
	@Autowired
    private PreferenceService preferenceService;
	
	//************************** API para actualizar la foto de perfil del usuario + profile. **************************
    @PutMapping(value="/update-preference/{id}")
    public ResponseEntity<ApiResponse> updatePreference(@PathVariable Long id, @RequestBody PreferenceDTO req) throws Exception {

    	 if (req == null && id == null) {
    		 return ResponseEntity.badRequest().body(new ApiResponse("Error: la solicitud están vacíos"));
    	 }
    	 try {
    		 
    		 req.setId(id);  
    		 preferenceService.updatePreferenceData(req); 
             return ResponseEntity.ok(new ApiResponse("Preferencias actualizadas con éxito"));
         } catch (RuntimeException e) {
             return ResponseEntity.noContent().build();
         }
     }

}
