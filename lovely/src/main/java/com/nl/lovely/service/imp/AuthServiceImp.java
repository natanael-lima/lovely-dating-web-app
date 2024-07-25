package com.nl.lovely.service.imp;

import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.nl.lovely.dto.ProfileRegistrationDTO;
import com.nl.lovely.entity.Preference;
import com.nl.lovely.entity.ProfileDetail;
import com.nl.lovely.entity.User;
import com.nl.lovely.enums.RoleType;
import com.nl.lovely.enums.UserStatus;
import com.nl.lovely.repository.PreferenceRepository;
import com.nl.lovely.repository.ProfileDetailRepository;
import com.nl.lovely.repository.UserRepository;
import com.nl.lovely.request.LoginRequest;
import com.nl.lovely.response.AuthResponse;
import com.nl.lovely.service.AuthService;
import com.nl.lovely.service.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {

	@Autowired
	private final PreferenceRepository preferenceRepository;
	@Autowired
	private final ProfileDetailRepository profileDetailRepository;
	@Autowired
	private final UserRepository userRepository;
	@Autowired
	private final JwtService jwtService;
	
	private final PasswordEncoder passwordEncoder;
	
	private final AuthenticationManager authenticationManager;
	
	
	@Override
	public AuthResponse registerProfile(ProfileRegistrationDTO request, MultipartFile file) {
		// TODO Auto-generated method stub
		byte[] photoBytes = null;
        String photoFileName = null;

        if (!file.isEmpty()) {
            // Obtener los bytes del archivo
            try {
				photoBytes = file.getBytes();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            // Establecer el nombre del archivo
            photoFileName = file.getOriginalFilename();
        }

        String capitalizedDescription = capitalizeFirstLetter(request.getProfileDetail().getDescription());
        String capitalizedWork = capitalizeFirstLetter(request.getProfileDetail().getWork());
		ProfileDetail profileDetailData = ProfileDetail.builder()
	            .phone(request.getProfileDetail().getPhone())
	            .gender(request.getProfileDetail().getGender())
	            .birthDate(request.getProfileDetail().getBirthDate())
	            .description(capitalizedDescription)
	            .work(capitalizedWork)
	            .photo(photoBytes)
	            .photoFileName(photoFileName)
	            .timestamp(LocalDateTime.now())
	            .build();

	    // Guardar el perfil de usuario en la base de datos
		profileDetailRepository.save(profileDetailData);
	    
	    // Construir el objeto UserProfile con los datos del registro
	    Preference preferenceData = Preference.builder()
	    		.maxAge(request.getPreference().getMaxAge())
	            .minAge(request.getPreference().getMinAge())
	            .likeGender(request.getPreference().getLikeGender())
	            .location(request.getPreference().getLocation())
	            .interests(request.getPreference().getInterests())
	            .build();

	    // Guardar el perfil de usuario en la base de datos
	    preferenceRepository.save(preferenceData);
	    
        // Crear un objeto UserProfile con los datos del registro y la ruta/nombre de la imagen guardada
	    // Capitalizar la primera letra del nombre y apellido
        String capitalizedName = capitalizeFirstLetter(request.getName());
        String capitalizedLastname = capitalizeFirstLetter(request.getLastname());
	    User profileData = User.builder()
        		.username(request.getUsername())
		        .password(passwordEncoder.encode(request.getPassword()))
		        .lastname(capitalizedLastname)
		        .name(capitalizedName)
		        .role(RoleType.USER)
		        .state(UserStatus.ACTIVO)
		        .isVisible(false)
                .preference(preferenceData)
                .profileDetail(profileDetailData)
	            .build();
		
		// Guardar el usuario en la base de datos
        userRepository.save(profileData);
		
        
		// Crear y devolver la respuesta de autenticación
		return AuthResponse.builder()
				        .token(jwtService.getToken(profileData))
				        .build();
	}
	
	@Override
	public AuthResponse login(LoginRequest request) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user=userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token=jwtService.getToken(user);
        return AuthResponse.builder()
            .token(token)
            .build();

	}

	public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

	

}
