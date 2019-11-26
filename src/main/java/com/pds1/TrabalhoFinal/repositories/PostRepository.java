package com.pds1.TrabalhoFinal.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.pds1.TrabalhoFinal.entities.Post;
import com.pds1.TrabalhoFinal.entities.User;

public interface PostRepository extends JpaRepository<Post, Long>{
	
	@Transactional(readOnly = true) 
	@Query("SELECT obj FROM Post obj WHERE LOWER(obj.title) LIKE LOWER(CONCAT('%',:text,'%')) OR LOWER(obj.body) LIKE LOWER(CONCAT('%',:text,'%'))")
	Page<Post> findByTitleOrBodyContainingIgnoreCase(@Param("text") String text, Pageable pageable);
	
	List<Post> findByAuthor(User author);
}
