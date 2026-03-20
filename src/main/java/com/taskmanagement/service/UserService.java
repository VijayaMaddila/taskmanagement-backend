package com.taskmanagement.service;


import org.springframework.data.domain.Pageable;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.taskmanagement.dto.LoginDto;
import com.taskmanagement.dto.RegisterDto;
import com.taskmanagement.model.User;
import com.taskmanagement.repository.UserRepository;
import com.taskmanagement.util.JwtUtil;

import java.util.Map;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtUtil  jwtUtil;
	
	public User register(RegisterDto register) {
		if(userRepository.existsByEmail(register.getEmail())) {
			throw new RuntimeException("Email already exists");
		}
		User user=new User();
		user.setEmail(register.getEmail());
		user.setUsername(register.getUsername());
		user.setRole(register.getRole());
		user.setPassword(passwordEncoder.encode(register.getPassword()));
		
		return userRepository.save(user);
		
		
	}
	public Map<String, Object> loginAndGetToken(LoginDto login) {
	    User user = userRepository.findByEmail(login.getEmail());

	    if (user == null) {
	        throw new RuntimeException("User not found");
	    }

	    if (!passwordEncoder.matches(login.getPassword(), user.getPassword())) {
	        throw new RuntimeException("Invalid credentials");
	    }

	    String token = jwtUtil.generateToken(user);
	    return Map.of(
	        "token", token,
	        "id", user.getId(),
	        "username", user.getUsername(),
	        "email", user.getEmail(),
	        "role", user.getRole() != null ? user.getRole().name() : ""
	    );
	}
	public Page<User> getAllUsers(int page, int size) {

	    Pageable pageable = PageRequest.of(page, size);

	    return userRepository.findAll(pageable);
	}
	public User getUserById(Long id) {
		User user=userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found: "+id));
		
		return user;
	}
	public User updateUser(User user, Long id) {
		User newUser=userRepository.findById(id)
				.orElseThrow(()->new RuntimeException("User not found: "+id));
		newUser.setEmail(user.getEmail());
		newUser.setUsername(user.getUsername());
		newUser.setRole(user.getRole());
		
		return userRepository.save(newUser);
		
	}
	public void deleteUser(Long id) {
		User user=userRepository.findById(id)
				.orElseThrow(()->new RuntimeException("User not found: "+id));
		user.setIs_deleted(true);
		userRepository.save(user);
	}

	

}
