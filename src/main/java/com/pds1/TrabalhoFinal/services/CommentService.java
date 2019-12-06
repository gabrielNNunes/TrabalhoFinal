package com.pds1.TrabalhoFinal.services;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pds1.TrabalhoFinal.dto.CommentDTO;
import com.pds1.TrabalhoFinal.entities.Comment;
import com.pds1.TrabalhoFinal.entities.User;
import com.pds1.TrabalhoFinal.repositories.CommentRepository;

import services.exceptions.DatabaseException;
import services.exceptions.ResourceNotFoundException;

@Service
public class CommentService{
	
	@Autowired
	private CommentRepository repository;
	
	@Autowired
	private AuthService authService;
	
	public List<CommentDTO> findAll(){
		List<Comment> list = repository.findAll();
		return list.stream().map(e -> new CommentDTO(e)).collect(Collectors.toList());
	}
	
	public CommentDTO findById(Long id) {
		authService.validateSelfOrAdmin(id);
		Optional<Comment> obj = repository.findById(id);
		Comment entity = obj.orElseThrow(() -> new ResourceNotFoundException(id));
		return new CommentDTO(entity);
	}
	
	public CommentDTO insert(CommentDTO dto) {
		Comment entity = dto.toEntity();					
		User author = authService.authenticated();
		entity.setAuthor(author);
		entity = repository.save(entity);
		return new CommentDTO(entity);
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
	public CommentDTO update(Long id,CommentDTO dto) {
		Comment entity = repository.getOne(id);
		authService.validateSelfOrAdmin(entity.getAuthor().getId());
		try {
		
		updateData(entity,dto);
		entity = repository.save(entity);
		return new CommentDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}
	private void updateData(Comment entity, CommentDTO dto) {		
		entity.setText(dto.getText());
		entity.setMoment(Instant.now());
		
	}
	
	public Page<CommentDTO> findByNamePaged(String text, Pageable pageable){
		Page<Comment> list;
		
			list = repository.findByTextContainingIgnoreCase(text, pageable);
		
		return list.map(e -> new CommentDTO(e));
	}
	
	
}
