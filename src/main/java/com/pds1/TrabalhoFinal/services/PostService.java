package com.pds1.TrabalhoFinal.services;

import java.time.Instant;
import java.util.List;
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
import com.pds1.TrabalhoFinal.repositories.UserRepository;

import services.exceptions.DatabaseException;
import services.exceptions.ResourceNotFoundException;

@Service
public class PostService{
	
	@Autowired
	private PostRepository repository;
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private UserRepository userRepository;
	
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
	
	public List<PostDTO> findById(Long userId) {
		User author = userRepository.getOne(userId);
		List<Post> list = repository.findByAuthor(author);
		return list.stream().map(e -> new PostDTO(e)).collect(Collectors.toList());
	}
	
	public PostDTO insert(PostDTO dto) {		
		Post entity = dto.toEntity();		
		User author = authService.authenticated();
		entity.setAuthor(author);		
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
		Post entity = repository.getOne(id);
		authService.validateSelfOrAdmin(entity.getAuthor().getId());
		try {		
		updateData(entity,dto);
		entity = repository.save(entity);
		return new PostDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}
	private void updateData(Post entity, PostDTO dto) {		
		entity.setMoment(Instant.now());
		entity.setTitle(dto.getTitle());
		entity.setBody(dto.getBody());
	}
	
	public Page<PostDTO> findByNamePaged(String text, Pageable pageable){
		Page<Post> list;
		
			list = repository.findByTitleOrBodyContainingIgnoreCase(text, pageable);
		
		return list.map(e -> new PostDTO(e));
	}
	
	
	
}
