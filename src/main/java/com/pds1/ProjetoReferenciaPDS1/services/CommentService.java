package com.pds1.ProjetoReferenciaPDS1.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pds1.ProjetoReferenciaPDS1.dto.CommentDTO;
import com.pds1.ProjetoReferenciaPDS1.entities.Comment;
import com.pds1.ProjetoReferenciaPDS1.repositories.CommentRepository;

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
		authService.validateSelfOrAdmin(id);
		try {
		Comment entity = repository.getOne(id);
		updateData(entity,dto);
		entity = repository.save(entity);
		return new CommentDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}
	private void updateData(Comment entity, CommentDTO dto) {
		entity.setId(dto.getId());
		entity.setText(dto.getText());
		entity.setMoment(dto.getMoment());
		
	}
	
	
	
}
