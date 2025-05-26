package com.example.demo.serviceimpl;

import com.example.demo.config.JwtUtil;
import com.example.demo.entity.UserToken;
import com.example.demo.entity.Users;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserTokenRepository;
import com.example.demo.service.UserService;

import org.apache.naming.java.javaURLContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private UserTokenRepository userTokenRepository;

    public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil ) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    @Override
    public String registerUser(Users user) {
    	
    	if(user.getUsername() == null || user.getUsername().isEmpty() ||
    			user.getPassword() == null || user.getPassword().isEmpty() ||
    			user.getEmail() == null || user.getEmail().isEmpty() ||
    			user.getRole() == null || user.getRole().isEmpty() ) {
    		return "All fieids are manadatory";
    	}
    			
    	
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "Username already exists";
        }
        String hashedPassword =  passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
        return "User registered successfully";
    }
    @Override
    public String loginUser(String username, String password) {
        Optional<Users> existingUser = userRepository.findByUsername(username);

        if (existingUser.isPresent()) {
            Users userlogin = existingUser.get();
            if (passwordEncoder.matches(password, userlogin.getPassword())) {

				Optional<UserToken> byUsername = userTokenRepository.findByUsername(username);
				if (byUsername.isPresent()) {
					UserToken userToken = byUsername.get();
					if (userToken.getExpiryDate().after(new java.util.Date())) {
						return userToken.getToken();
					}else {
						return jwtUtil.generateToken(userlogin.getUsername(), userlogin, null);
					}
				} else {
					return jwtUtil.generateToken(userlogin.getUsername(), userlogin, null);
				}
            	
            	 
                
            }
        }
        return "Invalid username or password";
    }



}
