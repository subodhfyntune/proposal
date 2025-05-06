package com.example.demo.service;

import com.example.demo.config.JwtUtil;
import com.example.demo.model.Users;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public String registerUser(Users user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "Username already exists";
        }
        userRepository.save(user);
        return "User registered successfully";
    }

    public String loginUser(Users user) {
        Optional<Users> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent() && existingUser.get().getPassword().equals(user.getPassword())) {
            return jwtUtil.generateToken(user.getUsername(), existingUser.get().getUserId());
        }
        return "Invalid username or password";
    }

}
