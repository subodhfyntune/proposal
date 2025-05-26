package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
@Entity
public class SessionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String sessionId;
	
	private Integer userId;
	
	private LocalDateTime createdAt;

	private LocalDateTime expiredAt; 

	public SessionEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SessionEntity(Integer id, String sessionId, Integer userId, LocalDateTime createdAt,
			LocalDateTime expiredAt) {
		super();
		this.id = id;
		this.sessionId = sessionId;
		this.userId = userId;
		this.createdAt = createdAt;
		this.expiredAt = expiredAt;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getExpiredAt() {
		return expiredAt;
	}

	public void setExpiredAt(LocalDateTime expiredAt) {
		this.expiredAt = expiredAt;
	}
	
	
}
