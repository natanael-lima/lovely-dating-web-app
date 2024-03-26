package com.nl.lovely.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nl.lovely.entity.User;
import com.nl.lovely.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	@Autowired
    private UserService userService;

	
	@PostMapping("/demo")
    public String welcome() {
        // Lógica para verificar las credenciales y establecer el estado de inicio de sesión
        //user.setLoggedIn(true);
        // Devuelve una respuesta adecuada para Angular
        return "Welcome user";
    }
    
  	//============================ Metodo para mostrar verficar credenciales al cerrar sesion ============================
    @GetMapping("/logout")
    public ResponseEntity<String> logoutAccount() {
        // Lógica para cerrar sesión y cambiar el estado de loggedIn
        // En lugar de usar un objeto User, puedes manejar la sesión en el frontend de Angular
        // Por lo tanto, aquí podrías simplemente invalidar la sesión en el backend si fuera necesario
        return ResponseEntity.ok("redirect:/login-user");
    }
	
    @GetMapping("/random-user")
    public ResponseEntity<User> getRandomUser() {
        User randomUser = userService.getRandomProfile();
        return ResponseEntity.ok(randomUser);
    }
    
    @GetMapping("/random")
    public List<User> getRandomUsers() {
        return userService.getRandomProfiles(3); // Por ejemplo, obtenemos 10 perfiles aleatorios
    }
    
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        return userService.updateUser(user);
    }
}
