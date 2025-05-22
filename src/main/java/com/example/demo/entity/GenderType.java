package com.example.demo.entity;




import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "gender_type")
public class GenderType {
	
	
	@Id
	@Column(name = "gender_id")
	private Integer genderId;
	
	
	@Column(name = "gender_type")
	private String type;
	
	public GenderType() {
		super();
	}

	public GenderType(Integer genderId, String type) {
		super();
		this.genderId = genderId;
		this.type = type;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
