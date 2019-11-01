package com.pds1.ProjetoReferenciaPDS1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pds1.ProjetoReferenciaPDS1.dto.CredentialsDTO;
import com.pds1.ProjetoReferenciaPDS1.dto.TokenDTO;
import com.pds1.ProjetoReferenciaPDS1.entities.User;
import com.pds1.ProjetoReferenciaPDS1.repositories.UserRepository;
import com.pds1.ProjetoReferenciaPDS1.security.JWTUtil;

import services.exceptions.JWTAuthenticationException;
import services.exceptions.JWTAuthorizationException;

@Service
public class AuthService {
	
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;	
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Transactional(readOnly = true)
	public TokenDTO authenticate(CredentialsDTO dto){
		try {
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(dto.getEmail(),dto.getPassword());
			authenticationManager.authenticate(authToken);
			String token = jwtUtil.generetionToken(dto.getEmail());
			return new TokenDTO(dto.getEmail(),token);
		}catch(AuthenticationException e) {
			throw new JWTAuthenticationException("Bad credentials");
		}
	}
	public User authenticated(){
		try {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userRepository.findByEmail(userDetails.getUsername());
		}catch(Exception e){
			throw new JWTAuthenticationException("Access denied");
		}
	}
	
	public void validateSelfOrAdmin(Long userId) {
		User user = authenticated();
		if(user == null || (!user.getId().equals(userId)) && !user.hasRole("ROLE_ADMIN")){
			throw new JWTAuthorizationException("Access denied");
		}
	}
	
	
}
