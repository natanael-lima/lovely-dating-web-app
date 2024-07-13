package com.nl.lovely.service;

import com.nl.lovely.dto.PreferenceDTO;
import com.nl.lovely.response.ApiResponse;

public interface PreferenceService {

	public ApiResponse updatePreferenceData(PreferenceDTO request) throws Exception;
	//public PreferenceDTO getPreferenceById(Long id);
}
