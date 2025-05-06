package com.example.demo.service;

import com.example.demo.config.JwtUtil;
import com.example.demo.model.Users;
import com.example.demo.repository.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil ) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String registerUser(Users user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "Username already exists";
        }
        String hashedPassword =  passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
        return "User registered successfully";
    }

    public String loginUser(Users user) {
        Optional<Users> existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser.isPresent()) {
            Users userlogin = existingUser.get();
         
            if (passwordEncoder.matches(user.getPassword(), userlogin.getPassword())) {
                return jwtUtil.generateToken(
                    userlogin.getUsername(),
                    userlogin.getUserId(),
                    userlogin.getEmail(),
                    userlogin.getRole()
                );
            }
        }
        return "Invalid username or password";
    }


}
