package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "responce_excel")
public class ResponceExcel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "status")
	private String status;
	
	@Column(name = "field")
	private String errorField;
	@Column(name = "reason")
	private String reason;
	@Column(name = "queue_id")
	private Long queueId;
	public ResponceExcel() {
		super();
	}
	public ResponceExcel(Integer id, String status, String errorField, String reason ,Long queueId) {
		super();
		this.id = id;
		this.status = status;
		this.errorField = errorField;
		this.reason = reason;
		this.queueId = queueId;
	}
	public Long getQueueId() {
		return queueId;
	}
	public void setQueueId(Long queueId) {
		this.queueId = queueId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrorField() {
		return errorField;
	}
	public void setErrorField(String errorField) {
		this.errorField = errorField;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
	
	
	
	

}
