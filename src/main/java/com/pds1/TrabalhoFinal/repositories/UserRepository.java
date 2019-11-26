package com.pds1.TrabalhoFinal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pds1.TrabalhoFinal.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	User findByEmail(String email);
}
