package com.example.demo.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


@Entity
@Table(name = "proposer")
public class Proposer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Enumerated(EnumType.STRING)
	@Column(name = "title")
	private Title title;
	
	@Column(name = "full_Name")
	private String fullName;
	@Enumerated(EnumType.STRING)
	@Column(name = "gender")
	private Gender gender;
	
	@Column(name = "date_Of_Birth")
	private String dateOfBirth;
	
	@Column(name = "annual_Income")
	private String annualIncome;
	
	@Column(name = "panNumber")
	private String panNumber;
	
	@Column(name = "aadhar_Number")
	private String aadharNumber;
	@Column(name = "marital_status")
	private String maritalStatus;
	
	@Column(name = "email")
	private String email;
	@Column(name = "mobile_Number")
	private String mobileNumber;
	@Column(name = "alternate_Mobile_Number")
	private String alternateMobileNumber;
	@Column(name = "address_Line_1")
	private String addressLine1;
	@Column(name = "address_Line_2")
	private String addressLine2;
	@Column(name = "address_Line_3")
	private String addressLine3;
	@Column(name = "pincode")
	private String pincode;
	@Enumerated(EnumType.STRING)
	@Column(name = "area")
	private Area area;
	@Enumerated(EnumType.STRING)
	@Column(name = "town" )
	private Town town;
	@Column(name = "city")
	private String city;
	@Column(name = "state")
	private String state;
	private char status ;
	public Proposer() {
		
	}
	
	
	public Proposer(Long id, Title title, String fullName, Gender gender, String dateOfBirth, String annualIncome,
			String panNumber, String aadharNumber, String maritalStatus,
			 String email, String mobileNumber,
			String alternateMobileNumber, String addressLine1, String addressLine2, String addressLine3, String pincode,
			Area area, Town town, String city, String state, char status) {
		super();
		this.id = id;
		this.title = title;
		this.fullName = fullName;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.annualIncome = annualIncome;
		this.panNumber = panNumber;
		this.aadharNumber = aadharNumber;
		this.maritalStatus = maritalStatus;
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.alternateMobileNumber = alternateMobileNumber;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.addressLine3 = addressLine3;
		this.pincode = pincode;
		this.area = area;
		this.town = town;
		this.city = city;
		this.state = state;
		this.status = status;
	}


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Title getTitle() {
		return title;
	}
	public void setTitle(Title title) {
		this.title = title;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender string) {
		this.gender = string;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getAnnualIncome() {
		return annualIncome;
	}
	public void setAnnualIncome(String annualIncome) {
		this.annualIncome = annualIncome;
	}
	public String getPanNumber() {
		return panNumber;
	}
	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}
	public String getAadharNumber() {
		return aadharNumber;
	}
	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getAlternateMobileNumber() {
		return alternateMobileNumber;
	}
	public void setAlternateMobileNumber(String alternateMobileNumber) {
		this.alternateMobileNumber = alternateMobileNumber;
	}
	public String getAddressLine1() {
		return addressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	public String getAddressLine2() {
		return addressLine2;
	}
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	public String getAddressLine3() {
		return addressLine3;
	}
	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	public Town getTown() {
		return town;
	}
	public void setTown(Town town) {
		this.town = town;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	
}