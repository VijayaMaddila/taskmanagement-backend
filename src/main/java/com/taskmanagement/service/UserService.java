package com.taskmanagement.service;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.taskmanagement.dto.ChangePasswordDto;
import com.taskmanagement.dto.LoginDto;
import com.taskmanagement.dto.ProfileDto;
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

	    if (user == null || user.isIs_deleted()) {
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
	    return userRepository.findActiveUsers(PageRequest.of(page, size));
	}
	public User getUserById(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found: " + id));
		if (user.isIs_deleted()) {
			throw new RuntimeException("User not found: " + id);
		}
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

	public User getProfile(String email) {
		User user = userRepository.findByEmail(email);
		if (user == null || user.isIs_deleted()) {
			throw new RuntimeException("User not found");
		}
		return user;
	}

	public User updateProfile(String email, ProfileDto dto) {
		User user = userRepository.findByEmail(email);
		if (user == null || user.isIs_deleted()) {
			throw new RuntimeException("User not found");
		}
		if (!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
			throw new RuntimeException("Email already in use");
		}
		user.setUsername(dto.getUsername());
		user.setEmail(dto.getEmail());
		return userRepository.save(user);
	}

	public void changePassword(String email, ChangePasswordDto dto) {
		User user = userRepository.findByEmail(email);
		if (user == null || user.isIs_deleted()) {
			throw new RuntimeException("User not found");
		}
		if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
			throw new RuntimeException("Current password is incorrect");
		}
		user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
		userRepository.save(user);
	}

}
