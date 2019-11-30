package com.pds1.TrabalhoFinal.dto;

import java.io.Serializable;
import java.time.Instant;

import com.pds1.TrabalhoFinal.entities.Comment;
import com.pds1.TrabalhoFinal.entities.Post;
import com.pds1.TrabalhoFinal.entities.User;

public class CommentDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;	
	private String text;
	private Instant moment;
	private Long post_id;
	private Long author_id;
	
	public CommentDTO(){		
	}

	public CommentDTO(Long id, String text, Instant moment,Long post_id,Long author_id) {
		super();
		this.id = id;
		this.text = text;
		this.moment = moment;
		this.post_id = post_id;
		this.author_id = author_id;
	}
	
	public CommentDTO(Comment entity) {		
		this.id = entity.getId();
		this.text = entity.getText();
		this.moment = entity.getMoment();
		this.post_id = entity.getPost().getId();
		this.author_id = entity.getAuthor().getId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Instant getMoment() {
		return moment;
	}

	public void setMoment(Instant moment) {
		this.moment = moment;
	}	
	
	public Long getPost_id() {
		return post_id;
	}

	public void setPost_id(Long post_id) {
		this.post_id = post_id;
	}

	public Long getAuthor_id() {
		return author_id;
	}

	public void setAuthor_id(Long author_id) {
		this.author_id = author_id;
	}

	public Comment toEntity(){
		Post post = new Post(post_id, null, null, null, null);
		User author = new User(author_id, null, null, null);
		return new Comment(id,text,moment,post,author);
	}
	
}
