package com.pds1.TrabalhoFinal.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pds1.TrabalhoFinal.dto.UserDTO;
import com.pds1.TrabalhoFinal.dto.UserInsertDTO;
import com.pds1.TrabalhoFinal.entities.Role;
import com.pds1.TrabalhoFinal.entities.User;
import com.pds1.TrabalhoFinal.repositories.RoleRepository;
import com.pds1.TrabalhoFinal.repositories.UserRepository;

import services.exceptions.DatabaseException;
import services.exceptions.ResourceNotFoundException;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private AuthService authService;
	
	public List<UserDTO> findAll(){
		List<User> list = repository.findAll();
		return list.stream().map(e -> new UserDTO(e)).collect(Collectors.toList());
	}
	public UserDTO findById(Long id) {
		Optional<User> obj = repository.findById(id);
		User entity = obj.orElseThrow(() -> new ResourceNotFoundException(id));
		return new UserDTO(entity);
	}
	
	public UserDTO insert(UserInsertDTO dto) {
		User entity = dto.toEntity();
		entity.setPassword(passwordEncoder.encode(dto.getPassword()));
		entity = repository.save(entity);
		Role role = roleRepository.findByAuthority("ROLE_MEMBER");
		entity.getRoles().add(role);
		entity = repository.save(entity);
		return new UserDTO(entity);
	}
	public void delete(Long id){
		try {
			repository.deleteById(id);
		} catch(EmptyResultDataAccessException e){
			throw new ResourceNotFoundException(id);
		}catch(DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	@Transactional 
	public UserDTO update(Long id,UserDTO dto) {
		authService.validateSelfOrAdmin(id);
		try {
		User entity = repository.getOne(id);
		updateData(entity,dto);
		entity = repository.save(entity);
		return new UserDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}
	private void updateData(User entity, UserDTO dto) {
		entity.setName(dto.getName());
		entity.setEmail(dto.getEmail());	
		
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user= repository.findByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException(username);
		}
		return user;
	}
	
}
