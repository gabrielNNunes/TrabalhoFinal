package com.pds1.TrabalhoFinal.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.pds1.TrabalhoFinal.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	
	@Transactional(readOnly = true)
	@Query("SELECT obj FROM Comment obj WHERE LOWER(obj.text) LIKE LOWER(CONCAT('%',:text,'%'))")
	Page<Comment> findByTextContainingIgnoreCase(@Param("text") String text, Pageable pageable);
	
}
