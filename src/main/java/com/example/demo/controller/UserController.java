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
            if ("Username already exists".equals(result) || "All fieids are manadatory".equals(result)) {
                response.setStatus("fail");
                response.setMessage(result);
                
            } else {
                response.setStatus("success");
                response.setMessage("User registered successfully");
                response.setData(result);
            }
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Something went wrong during registration");
           
        }
        return response;
    }

    @PostMapping("/login")
    public ResponseHandler<String> login(@RequestParam String username, @RequestParam String password) {
        ResponseHandler<String> response = new ResponseHandler<>();
        try {
            String token = userService.loginUser(username, password);
            if ("Invalid username or password".equals(token)) {
                response.setStatus("fail");
                response.setMessage(token);
            } else {
                response.setStatus("success");
                response.setMessage("Login successful");
                response.setData(token);
            }
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Something went wrong during login");
            response.setData(null);
        }
        return response;
    }

}
