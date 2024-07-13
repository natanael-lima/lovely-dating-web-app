package com.nl.lovely.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.nl.lovely.dto.ProfileDetailDTO;
import com.nl.lovely.response.ApiResponse;
import com.nl.lovely.service.UserService;

@CrossOrigin(origins = "http://localhost:4200") // Reemplaza esto con el dominio de tu frontend
@RestController
@RequestMapping("/api/profile")
public class ProfileDetailController {
	
	@Autowired
    private UserService userService;
	
	//************************** API para actualizar la foto de perfil del usuario + profile. **************************
    @PutMapping(value="/update-profile-detail/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> updateProfileAndPhoto(@PathVariable Long id,@RequestPart(value = "file", required = false) MultipartFile file, @RequestPart(value = "req", required = true) ProfileDetailDTO req, @RequestParam(value = "keepCurrentImage", required = false) Boolean keepCurrentImage) throws Exception {
        // Tu lógica existente para cargar el archivo y actualizar el perfil
    	 if (req == null && file == null ) {
    		 return ResponseEntity.badRequest().body(new ApiResponse("Error: el archivo de imagen o la solicitud están vacíos"));
    	 }
    	 try {
             if (Boolean.TRUE.equals(keepCurrentImage)) {
                 // Mantener la imagen actual
                 System.out.println("Mantener la imagen actual"+ req);
                 req.setId(id); 
                 userService.updateProfileWithPhoto(req, null); // No se pasa file porque se mantiene la imagen actual
             } else {
                 // Se proporciona una nueva imagen, actualizar con la nueva imagen
                 if (file == null) {
                     return ResponseEntity.badRequest().body(new ApiResponse("Error: se esperaba una nueva imagen pero no se proporcionó"));
                 }
                 System.out.println("Actualizar con nueva imagen");
                 req.setId(id); 
                 userService.updateProfileWithPhoto(req, file);
             }

             return ResponseEntity.ok(new ApiResponse("Profile Detail actualizado con éxito"));
         } catch (RuntimeException e) {
             return ResponseEntity.noContent().build();
         }
     }
    
    
    
    

}
