package com.pds1.ProjetoReferenciaPDS1.resourse;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.pds1.ProjetoReferenciaPDS1.dto.PostDTO;
import com.pds1.ProjetoReferenciaPDS1.services.PostService;

@RestController
@RequestMapping(value = "/posts")
public class PostResourse {
	
	@Autowired
	private PostService service;
	
	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
	public ResponseEntity<List<PostDTO>> findAll(){
		List<PostDTO> list = service.findAll();				
		return ResponseEntity.ok().body(list);
	}
	@GetMapping(value = "/{id}")
	public ResponseEntity<PostDTO> findById(@PathVariable Long id){
		PostDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}
	
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
	public ResponseEntity<PostDTO> insert(@Valid @RequestBody PostDTO dto){
		PostDTO newDTO = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newDTO.getId()).toUri();
		return ResponseEntity.created(uri).body(newDTO);
	}
	
	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
	public ResponseEntity<PostDTO> update(@PathVariable Long id, @Valid @RequestBody PostDTO dto){
		dto = service.update(id, dto);
		return ResponseEntity.ok().body(dto);
	}
	
}
