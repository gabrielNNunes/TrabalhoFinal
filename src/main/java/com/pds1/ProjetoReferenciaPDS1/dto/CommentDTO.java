package com.pds1.ProjetoReferenciaPDS1.dto;

import java.io.Serializable;
import java.time.Instant;

import com.pds1.ProjetoReferenciaPDS1.entities.Comment;

public class CommentDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;	
	private String text;
	private Instant moment;
	
	public CommentDTO(){		
	}

	public CommentDTO(Long id, String text, Instant moment) {
		super();
		this.id = id;
		this.text = text;
		this.moment = moment;
	}
	
	public CommentDTO(Comment entity) {		
		this.id = entity.getId();
		this.text = entity.getText();
		this.moment = entity.getMoment();
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
	
	public Comment toEntity(){
		return new Comment(id,text,moment);
	}
	
}
