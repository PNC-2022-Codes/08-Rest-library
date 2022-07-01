package com.douglashdezt.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.douglashdezt.library.models.dtos.MessageDTO;
import com.douglashdezt.library.models.entities.User;
import com.douglashdezt.library.services.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@GetMapping("/whoami")
	public ResponseEntity<?> whoami () {
		try {
			User loggedUser = userService.getUserAuthenticated();
			
			return new ResponseEntity<User>(
				loggedUser,
				HttpStatus.OK
			);
		} catch (Exception e) {
			return new ResponseEntity<MessageDTO>(
				new MessageDTO("Error de servidor"),
				HttpStatus.INTERNAL_SERVER_ERROR
			);
		}
	}
	
}
