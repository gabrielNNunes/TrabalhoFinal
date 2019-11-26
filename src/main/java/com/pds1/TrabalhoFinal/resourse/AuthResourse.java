package com.pds1.TrabalhoFinal.resourse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pds1.TrabalhoFinal.dto.CredentialsDTO;
import com.pds1.TrabalhoFinal.dto.TokenDTO;
import com.pds1.TrabalhoFinal.services.AuthService;

@RestController
@RequestMapping(value = "/auth")
public class AuthResourse {
	
	@Autowired
	private AuthService service;
	
	@PostMapping(value = "/login")
	public ResponseEntity<TokenDTO> login(@RequestBody CredentialsDTO dto){
		TokenDTO tokenDTO = service.authenticate(dto);
		return ResponseEntity.ok().body(tokenDTO);
	}
	
		
	
}
