package com.pds1.TrabalhoFinal.resourse;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.pds1.TrabalhoFinal.dto.CommentDTO;
import com.pds1.TrabalhoFinal.dto.PostDTO;
import com.pds1.TrabalhoFinal.services.PostService;

@RestController
@RequestMapping(value = "/posts")
public class PostResourse {
	
	@Autowired
	private PostService service;
	
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
	public ResponseEntity<PostDTO> insert(@Valid @RequestBody PostDTO dto){
		PostDTO newDTO = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newDTO.getId()).toUri();
		return ResponseEntity.created(uri).body(newDTO);
	}
	
	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
	public ResponseEntity<List<PostDTO>> findByAuthor(){		
		List<PostDTO> list = service.findByAuthor();				
		return ResponseEntity.ok().body(list);
	}
		
	@GetMapping(value = "/user/{userId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
	public ResponseEntity<List<PostDTO>> findById(@PathVariable Long userId){
		List<PostDTO> listDTO = service.findById(userId);
		return ResponseEntity.ok().body(listDTO);
	}
	
	@GetMapping(value = "/{id}/coments")
	@PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
	public ResponseEntity<List<CommentDTO>> findComments(@PathVariable Long id){
		List<CommentDTO> list = service.findComments(id);
		return ResponseEntity.ok().body(list);
	}
	
	
	
	
	@GetMapping(value="/search")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Page<PostDTO>> findAllPaged(
			@RequestParam(value = "text",defaultValue = "") String text,			
			@RequestParam(value = "page",defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage",defaultValue = "12") Integer linesPerPage,
			@RequestParam(value = "orderBy",defaultValue = "moment") String orderBy,
			@RequestParam(value = "direction",defaultValue = "ASC") String direction){
		
		PageRequest pageRequest = PageRequest.of(page,linesPerPage,Direction.valueOf(direction),orderBy);
		Page<PostDTO> list = service.findByNamePaged(text, pageRequest);			
		return ResponseEntity.ok().body(list);
	}
	
	
	@PutMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
	public ResponseEntity<PostDTO> update(@PathVariable Long id, @Valid @RequestBody PostDTO dto){
		dto = service.update(id, dto);
		return ResponseEntity.ok().body(dto);
	}
	
	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
