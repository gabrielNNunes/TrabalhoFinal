package com.pds1.TrabalhoFinal.config;

import java.time.Instant;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.pds1.TrabalhoFinal.entities.Comment;
import com.pds1.TrabalhoFinal.entities.Post;
import com.pds1.TrabalhoFinal.entities.Role;
import com.pds1.TrabalhoFinal.entities.User;
import com.pds1.TrabalhoFinal.repositories.CommentRepository;
import com.pds1.TrabalhoFinal.repositories.PostRepository;
import com.pds1.TrabalhoFinal.repositories.RoleRepository;
import com.pds1.TrabalhoFinal.repositories.UserRepository;

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
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Override
	public void run(String... args) throws Exception {	
		
		User u1 = new User(null, "Maria Brown", "maria@gmail.com",passwordEncode.encode("123456"));
		User u2 = new User(null, "Alex Green", "alex@gmail.com",passwordEncode.encode("123456"));
		
		userRepository.saveAll(Arrays.asList(u1, u2));
		
		Post p1 = new Post(null, Instant.now(), "Título do post numero 01", "Corpo do post numero 01, ihhi hih ih hi hi hih h ih ih Maria",u1);
		Post p2 = new Post(null, Instant.now(), "Título do post numero 02", "Corpo do post numero 02, ihhi hih ih hi hi hih h ih ih Maria",u1);
		Post p3 = new Post(null, Instant.now(), "Título do post numero 03", "Corpo do post numero 03, ihhi hih ih hi hi hih h ih ih Alex",u2);
		
		p1.setAuthor(u1);
		p2.setAuthor(u1);
		p3.setAuthor(u2);				
		
		postRepository.saveAll(Arrays.asList(p1,p2,p3));
		
		u1.addPosts(p1);
		u1.addPosts(p2);
		u2.addPosts(p3);
		
		userRepository.saveAll(Arrays.asList(u1, u2));
		
		Comment c1 = new Comment(null, "Comentario do Post 01 Alex", Instant.now(),p1, u2);
		Comment c2 = new Comment(null, "Comentario do Post 02 Alex", Instant.now(),p2, u2);
		Comment c3 = new Comment(null, "Comentario do Post 03 Maria", Instant.now(),p3, u1);
		
		c1.setPost(p1);
		c2.setPost(p2);
		c3.setPost(p3);
		
		c1.setAuthor(u2);
		c2.setAuthor(u2);
		c3.setAuthor(u1);
		
		commentRepository.saveAll(Arrays.asList(c1,c2,c3));
		
		p1.addComments(c1);
		p2.addComments(c2);
		p3.addComments(c3);
		
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
