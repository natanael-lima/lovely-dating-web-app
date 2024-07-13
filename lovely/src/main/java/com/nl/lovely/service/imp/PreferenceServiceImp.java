package com.nl.lovely.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nl.lovely.dto.PreferenceDTO;
import com.nl.lovely.entity.Preference;
import com.nl.lovely.entity.ProfileDetail;
import com.nl.lovely.entity.User;
import com.nl.lovely.repository.PreferenceRepository;
import com.nl.lovely.repository.UserRepository;
import com.nl.lovely.response.ApiResponse;
import com.nl.lovely.service.PreferenceService;
import com.nl.lovely.service.UserService;

@Service
public class PreferenceServiceImp implements PreferenceService{

	@Autowired
	private UserRepository userRepository;
	@Autowired
    private PreferenceRepository preferenceRepository;
	
	
	@Override
	public ApiResponse updatePreferenceData(PreferenceDTO request) throws Exception {
		User user = userRepository.findByPreferenceId(request.getId())
                .orElseThrow(() -> new Exception("Usuario no encontrado para Preference  con ID: " + request.getId()));

		Preference preference = user.getPreference();
        if (preference == null) {
            throw new Exception("Preference no encontrado para el usuario con ID: " + user.getId());
        }

	    // Actualización de campos del ProfileDetail
        preference.setMaxAge(request.getMaxAge());
        preference.setMinAge(request.getMinAge());
        preference.setLikeGender(request.getLikeGender());
	    preference.setLocation(request.getLocation());
	    preference.setDistance(request.getDistance());
	    preference.setInterests(request.getInterests());
	    
	    System.out.print("lista: "+preference.getInterests());

	    // Llamar al método de actualización sin foto
	    preferenceRepository.updatePreference(
                user.getId(),
                preference.getMaxAge(),
                preference.getMinAge(),
                preference.getLikeGender(),
                preference.getLocation(),
                preference.getDistance()
               
        );
	 	return new ApiResponse("La preferencia se actualizo satisfactoriamente");
	}

}
