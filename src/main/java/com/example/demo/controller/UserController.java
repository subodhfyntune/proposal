package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Users;
import com.example.demo.response.ResponseHandler;
import com.example.demo.service.UserService;
import com.example.demo.serviceimpl.UserServiceImpl;



@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
     private UserService userService;

   
	@PostMapping("/register")
	public ResponseHandler<String> register(@RequestBody Users user) {
	    ResponseHandler<String> response = new ResponseHandler<>();
	    try {
	        String result = userService.registerUser(user);
	        response.setStatus("success");
	        response.setMessage(result); 
	        response.setData(result);
	    } catch (IllegalArgumentException e) {
	        response.setStatus("error");
	        response.setMessage(e.getMessage()); 
	        response.setData(null);
	    } catch (Exception e) {
	        response.setStatus("error");
	        response.setMessage("Something went wrong during registration");
	        response.setData(null);
	    }
	    return response;
	}


	@PostMapping("/login")
	public ResponseHandler<String> login(@RequestParam String username, @RequestParam String password) {
	    ResponseHandler<String> response = new ResponseHandler<>();
	    try {
	        String session = userService.loginUser(username, password);
	        response.setStatus("success");
	        response.setMessage("Login successful");
	        response.setData(session);
	    } catch (IllegalArgumentException e) {
	        response.setStatus("error");
	        response.setMessage(e.getMessage());
	        response.setData(null);
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.setStatus("error");
	        response.setMessage("Something went wrong during login");
	        response.setData(null);
	    }
	    return response;
	}


}
