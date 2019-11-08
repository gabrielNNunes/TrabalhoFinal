package com.pds1.ProjetoReferenciaPDS1.config;

import java.time.Instant;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.pds1.ProjetoReferenciaPDS1.entities.Post;
import com.pds1.ProjetoReferenciaPDS1.entities.Role;
import com.pds1.ProjetoReferenciaPDS1.entities.User;
import com.pds1.ProjetoReferenciaPDS1.repositories.PostRepository;
import com.pds1.ProjetoReferenciaPDS1.repositories.RoleRepository;
import com.pds1.ProjetoReferenciaPDS1.repositories.UserRepository;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncode;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Override
	public void run(String... args) throws Exception {	
		
		User u1 = new User(null, "Maria Brown", "maria@gmail.com",passwordEncode.encode("123456"));
		User u2 = new User(null, "Alex Green", "alex@gmail.com",passwordEncode.encode("123456"));
		
		userRepository.saveAll(Arrays.asList(u1, u2));
		
		Post p1 = new Post(null, Instant.now(), "Título do post numero 01", "Corpo do post numero 01, ihhi hih ih hi hi hih h ih ih Maria");
		Post p2 = new Post(null, Instant.now(), "Título do post numero 02", "Corpo do post numero 02, ihhi hih ih hi hi hih h ih ih Maria");
		Post p3 = new Post(null, Instant.now(), "Título do post numero 03", "Corpo do post numero 03, ihhi hih ih hi hi hih h ih ih Alex");
		
		p1.setUser(u1);
		p2.setUser(u1);
		p3.setUser(u2);
		
		postRepository.saveAll(Arrays.asList(p1,p2,p3));
		
		
		
		
		
		
		u1.addPosts(p1);
		u1.addPosts(p2);
		u2.addPosts(p3);
		
		userRepository.saveAll(Arrays.asList(u1, u2));
		
		Role r1 = new Role(null,"ROLE_MEMBER");
		Role r2 = new Role(null,"ROLE_ADMIN");
		
		roleRepository.saveAll(Arrays.asList(r1,r2));
		
		u1.getRoles().add(r1);
		u2.getRoles().add(r1);
		u2.getRoles().add(r2);
		
		userRepository.saveAll(Arrays.asList(u1, u2));		
		
	}
	
	
}
