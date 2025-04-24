package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "Queue")
public class QueueTable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 
	@Column(name = "row_read")
	private Integer rowRead;
	@Column(name = "row_count")
	private Integer rowCount;
	@Column(name = "is_processed")
	private Character isProcessed;
	@Column(name = "status")
	private Character status;
	@Column(name = "file_path")
	private String filePath;
	public QueueTable() {
		super();
		// TODO Auto-generated constructor stub
	}
	public QueueTable(Long id, Integer rowRead, Integer rowCount, Character isProcessed, Character status, String filePath) {
		super();
		this.id = id;
		this.rowRead = rowRead;
		this.rowCount = rowCount;
		this.isProcessed = isProcessed;
		this.status = status;
		this.filePath = filePath;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getRowRead() {
		return rowRead;
	}
	public void setRowRead(Integer rowRead) {
		this.rowRead = rowRead;
	}
	public Integer getRowCount() {
		return rowCount;
	}
	public void setRowCount( Integer rowCount) {
		this.rowCount = rowCount;
	}
	public Character getIsProcessed() {
		return isProcessed;
	}
	public void setIsProcessed(Character isProcessed) {
		this.isProcessed = isProcessed;
	}
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	

}
