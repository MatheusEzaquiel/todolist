package br.com.mbe.todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.mbe.todolist.domain.user.IUserRepository;
import br.com.mbe.todolist.domain.user.User;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	IUserRepository userRepository;
	
	@GetMapping
	public String list() {
		return "hello world";
	}
	
	@PostMapping
	public ResponseEntity create(@RequestBody User user) {
		
		User userFind = userRepository.findByUserName(user.getUserName());
		
		if(userFind != null) {
			System.out.println("Usuário com este username já existe");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("UserName já existe");
		}
		
		String passwordHashred = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
		user.setPassword(passwordHashred);
		
		User userCreated = userRepository.save(user);
	
		return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
		
	}

}
