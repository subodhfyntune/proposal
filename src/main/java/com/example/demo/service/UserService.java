package com.example.demo.service;

import com.example.demo.model.Users;

public interface UserService {

	public String registerUser(Users user);
	
	public String loginUser(String username, String password);
}
