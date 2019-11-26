package com.pds1.TrabalhoFinal.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import com.pds1.TrabalhoFinal.dto.PostDTO;
import com.pds1.TrabalhoFinal.entities.Comment;
import com.pds1.TrabalhoFinal.entities.Post;
import com.pds1.TrabalhoFinal.entities.User;
import com.pds1.TrabalhoFinal.repositories.PostRepository;

import services.exceptions.DatabaseException;
import services.exceptions.ResourceNotFoundException;

@Service
public class PostService{
	
	@Autowired
	private PostRepository repository;
	
	@Autowired
	private AuthService authService;
	
	@Transactional(readOnly = true)
	public List<CommentDTO> findComments(Long id) {
		Post post = repository.getOne(id);
		Set<Comment> set = post.getComments();
		return set.stream().map(e -> new CommentDTO(e)).collect(Collectors.toList());
	}
	
	public List<PostDTO> findByAuthor(){
		User author = authService.authenticated();
		List<Post> list = repository.findByAuthor(author);
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
	
	public Page<PostDTO> findByNamePaged(String text, Pageable pageable){
		Page<Post> list;
		
			list = repository.findByTitleOrBodyContainingIgnoreCase(text, pageable);
		
		return list.map(e -> new PostDTO(e));
	}
	
	
	
}
