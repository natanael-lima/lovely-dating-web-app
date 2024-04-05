package com.nl.lovely.service.imp;

import java.util.List;
import java.util.Optional;
import java.util.Random;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nl.lovely.dto.UserDTO;
import com.nl.lovely.entity.User;
import com.nl.lovely.enums.RoleType;
import com.nl.lovely.exception.NotFoundException;
import com.nl.lovely.repository.UserRepository;
import com.nl.lovely.request.UserRequest;
import com.nl.lovely.response.UserResponse;
import com.nl.lovely.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService{
	@Autowired
	private UserRepository userRepository;

	@Override
	public Optional<User> findByUsername(String username) {
		// TODO Auto-generated method stub
		Optional<User> result= userRepository.findByUsername(username);
		return result;
	}
	
	@Transactional
	public UserResponse updateUser(UserRequest userRequest) {
		// TODO Auto-generated method stub
		User user = User.builder()
		        .id(userRequest.getId())
		        .lastname(userRequest.getLastname())
		        .name(userRequest.getName())
		        .role(RoleType.USER)
		        .build();
		        
		        userRepository.updateUser(user.getId(), user.getLastname(), user.getName());

		        return new UserResponse("El usuario se actualizo satisfactoriamente");
	}
	
	@Override
	public UserDTO getUser(Long id) {
	        
		User user= userRepository.findById(id).orElse(null);
	       
	        if (user!=null)
	        {
	            UserDTO userDTO = UserDTO.builder()
	            .id(user.getId())
	            .username(user.getUsername())
	            .lastname(user.getLastname())
	            .name(user.getName())
	            .build();
	            return userDTO;
	        }
			return null;      
	}
	
	@Override
	public void deleteUser(Long id) {
		// TODO Auto-generated method stub
		userRepository.deleteById(id);
	}


	@Override
	public List<User> getRandomProfiles(int count) {
		List<User> allUsers = userRepository.findAll();
        if (allUsers.size() <= count) {
            return allUsers; // Si hay menos perfiles que el número deseado, devolvemos todos los perfiles
        } else {
            // Usamos una muestra aleatoria de la lista de perfiles
            Random random = new Random();
            for (int i = allUsers.size() - 1; i > 0; i--) {
                int j = random.nextInt(i + 1);
                User temp = allUsers.get(i);
                allUsers.set(i, allUsers.get(j));
                allUsers.set(j, temp);
            }
            return allUsers.subList(0, count); // Devolvemos una sublista con los perfiles aleatorios
        }
	}

	@Override
	public User getRandomProfile() {
		// Obtener la lista de todos los usuarios
        List<User> allUsers = userRepository.findAll();

        // Verificar si hay usuarios en la lista
        if (allUsers.isEmpty()) {
            return null; // Devolver null si no hay usuarios
        }

        // Obtener un índice aleatorio dentro del rango de la lista de usuarios
        Random random = new Random();
        int randomIndex = random.nextInt(allUsers.size());

        // Obtener el usuario aleatorio en función del índice generado
        return allUsers.get(randomIndex);
	}

	@Override
	public Optional<User> findUserById(Long id) {
		// TODO Auto-generated method stub
		return userRepository.findById(id);
	}

   

}
