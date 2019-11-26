package com.pds1.TrabalhoFinal.resourse;

import java.net.URI;

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
import com.pds1.TrabalhoFinal.services.CommentService;

@RestController
@RequestMapping(value = "/coments")
public class CommentResourse {
	
	@Autowired
	private CommentService service;
	
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
	public ResponseEntity<CommentDTO> insert(@Valid @RequestBody CommentDTO dto){
		CommentDTO newDTO = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newDTO.getId()).toUri();
		return ResponseEntity.created(uri).body(newDTO);
	}
	
	@PutMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
	public ResponseEntity<CommentDTO> update(@PathVariable Long id, @Valid @RequestBody CommentDTO dto){
		dto = service.update(id, dto);
		return ResponseEntity.ok().body(dto);
	}
	
	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping(value="/search")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Page<CommentDTO>> findAllPaged(
			@RequestParam(value = "text",defaultValue = "") String text,			
			@RequestParam(value = "page",defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage",defaultValue = "12") Integer linesPerPage,
			@RequestParam(value = "orderBy",defaultValue = "text") String orderBy,
			@RequestParam(value = "direction",defaultValue = "ASC") String direction){
		
		PageRequest pageRequest = PageRequest.of(page,linesPerPage,Direction.valueOf(direction),orderBy);
		Page<CommentDTO> list = service.findByNamePaged(text, pageRequest);				
		return ResponseEntity.ok().body(list);
	}
	
	
	
	
	
	
	
	
	/*@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
	public ResponseEntity<List<CommentDTO>> findAll(){
		List<CommentDTO> list = service.findAll();				
		return ResponseEntity.ok().body(list);
	}
	@GetMapping(value = "/{id}")
	public ResponseEntity<CommentDTO> findById(@PathVariable Long id){
		CommentDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}*/
	
	
	
	
	
	
	
}
