package com.example.demo.dto.handler;

public class ResponseHandler<T> {

	private String status;
	private Object data;
	private String message;
	private Integer totalRecord;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(Integer totalRecord) {
		this.totalRecord = totalRecord;
	}
	public ResponseHandler(String status, Object data, String message, Integer totalRecord) {
		super();
		this.status = status;
		this.data = data;
		this.message = message;
		this.totalRecord = totalRecord;
	}
	public ResponseHandler() {
		super();
	}
	
	
	
	
}
