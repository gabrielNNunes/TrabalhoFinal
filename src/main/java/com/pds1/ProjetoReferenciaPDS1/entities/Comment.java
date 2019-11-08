package com.pds1.ProjetoReferenciaPDS1.entities;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cascade;

public class Comment implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;	
	private String text;
	private Instant moment;
	
	public Comment(){		
	}

	public Comment(Long id, String text, Instant moment) {
		super();
		this.id = id;
		this.text = text;
		this.moment = moment;
	}

	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;
	
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

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
	@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comment other = (Comment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
	
	
}
