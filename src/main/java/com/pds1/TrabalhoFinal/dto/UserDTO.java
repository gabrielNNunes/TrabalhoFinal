package com.pds1.TrabalhoFinal.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.pds1.TrabalhoFinal.entities.User;
import com.pds1.TrabalhoFinal.services.validation.UserUpdateValid;

@UserUpdateValid
public class UserDTO implements Serializable{	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@NotEmpty(message = "can't be empty")
	@Length(min = 5,max = 80,message ="lentgh must be between 5 and 80")
	private String name;
	
	@NotEmpty(message = "can't be empty")
	@Email(message = "invalid email")
	private String email;
	
	
	public UserDTO(){		
	}

	public UserDTO(Long id, String name, String email) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		
	}
	
	public UserDTO(User entity){
		this.id = entity.getId();
		this.name = entity.getName();
		this.email = entity.getEmail();
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}	
	
	public User toEntity(){
		return new User(id, name, email, null);
	}
	
}
