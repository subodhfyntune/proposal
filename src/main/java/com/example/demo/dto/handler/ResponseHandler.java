package com.example.demo.dto.handler;

public class ResponseHandler<T> {

	private String status;
	private Object data;
	private String message;
	private Long totalCount;
	
	
	public ResponseHandler(String status, Object data, String message, Long totalCount) {
		super();
		this.status = status;
		this.data = data;
		this.message = message;
		this.totalCount = totalCount;
	}
	public ResponseHandler() {
		// TODO Auto-generated constructor stub
	}
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
	public Long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
	
}
