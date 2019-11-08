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

import com.pds1.ProjetoReferenciaPDS1.dto.PostDTO;
import com.pds1.ProjetoReferenciaPDS1.entities.Post;
import com.pds1.ProjetoReferenciaPDS1.repositories.PostRepository;

import services.exceptions.DatabaseException;
import services.exceptions.ResourceNotFoundException;

@Service
public class PostService{
	
	@Autowired
	private PostRepository repository;
	
	@Autowired
	private AuthService authService;
	
	public List<PostDTO> findAll(){
		List<Post> list = repository.findAll();
		return list.stream().map(e -> new PostDTO(e)).collect(Collectors.toList());
	}
	
	public PostDTO findById(Long id) {
		authService.validateSelfOrAdmin(id);
		Optional<Post> obj = repository.findById(id);
		Post entity = obj.orElseThrow(() -> new ResourceNotFoundException(id));
		return new PostDTO(entity);
	}
	
	public PostDTO insert(PostDTO dto) {
		Post entity = dto.toEntity();		
		entity = repository.save(entity);
		return new PostDTO(entity);
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
	public PostDTO update(Long id,PostDTO dto) {
		authService.validateSelfOrAdmin(id);
		try {
		Post entity = repository.getOne(id);
		updateData(entity,dto);
		entity = repository.save(entity);
		return new PostDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}
	private void updateData(Post entity, PostDTO dto) {
		entity.setId(dto.getId());
		entity.setMoment(dto.getMoment());
		entity.setTitle(dto.getTitle());
		entity.setBody(dto.getBody());
	}
	
	
	
}
