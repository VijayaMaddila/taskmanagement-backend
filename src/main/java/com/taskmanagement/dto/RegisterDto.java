package com.taskmanagement.dto;

import com.taskmanagement.role.Role;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class RegisterDto {
	
    
	@NotBlank(message = "Username cannot be blank")
	private String username;

	@NotBlank(message = "Email cannot be blank")
	@Email(message = "Email should be valid")
	private String email;

	@NotBlank(message = "Password cannot be blank")
	@Size(min = 6, message = "Password should contain at least 6 characters")
	private String password;

	@NotNull(message = "Role must be provided")
	@Enumerated(EnumType.STRING)
	private Role role;

    public RegisterDto()
    {
    	
    }

	public RegisterDto(@NotBlank(message = "Username cannot be blank") String username,
			@NotBlank(message = "Email cannot be blank") @Email(message = "Email should be valid") String email,
			@NotBlank(message = "Password cannot be blank") @Size(min = 6, message = "Password should contain at least 6 characters") String password,
			@NotNull(message = "Role must be provided") Role role) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
    

	
}