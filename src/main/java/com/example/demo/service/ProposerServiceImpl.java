package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.HealthApplication;
import com.example.demo.dto.ProposerDto;
import com.example.demo.exception.ProposerDeletedAlready;
import com.example.demo.model.Proposer;
import com.example.demo.repository.ProposerRepository;

@Service
public class ProposerServiceImpl implements ProposerService
{
	@Autowired
	private ProposerRepository proposerRepository;
	

//	@Override
//	public Proposer registerProposer(Proposer proposer) {
//		        proposer.setStatus('Y');
//		Proposer registerProposer =  proposerRepository.save(proposer);
//		return registerProposer;
//	}

	@Override
	public List<Proposer> getAllProposer() {
		List<Proposer> proposers = proposerRepository.findByStatus('Y');
		return proposers;
	}

	@Override
	public Proposer updateProposer(Long id,Proposer proposer) {

		Optional<Proposer> getProposer = proposerRepository.findById(id);
		
			 Proposer oldProposer = getProposer.get() ;
			 oldProposer.setTitle(proposer.getTitle());
			 oldProposer.setFullName(proposer.getFullName());
			 oldProposer.setGender(proposer.getGender());
			 oldProposer.setDateOfBirth(proposer.getDateOfBirth());
			 oldProposer.setAnnualIncome(proposer.getAnnualIncome());
			 oldProposer.setPanNumber(proposer.getPanNumber());
			 oldProposer.setAadharNumber(proposer.getAadharNumber());
			 oldProposer.setMaritalStatus(proposer.getMaritalStatus());
			 oldProposer.setEmail(proposer.getEmail());
			 oldProposer.setMobileNumber(proposer.getMobileNumber());
			 oldProposer.setAlternateMobileNumber(proposer.getAlternateMobileNumber());
			 oldProposer.setAddressLine1(proposer.getAddressLine1());
			 oldProposer.setAddressLine2(proposer.getAddressLine2());
			 oldProposer.setAddressLine3(proposer.getAddressLine3());
			 oldProposer.setPincode(proposer.getPincode());
			 oldProposer.setArea(proposer.getArea());
			 oldProposer.setTown(proposer.getTown());
			 oldProposer.setCity(proposer.getCity());
			 oldProposer.setState(proposer.getState());
			 
			 Proposer updatedProposer = proposerRepository.save(oldProposer);
			 return updatedProposer;
		
		
		
	}

	@Override
	public Proposer deleteProposer(Long id) {
		
		Optional<Proposer> deletedProposer = proposerRepository.findById(id);
		Proposer updatedeletedStatus  = deletedProposer.get();
		if(updatedeletedStatus.getStatus() == 'N'){
		 throw new ProposerDeletedAlready("Proposer is already deleted");
		}
		updatedeletedStatus.setStatus('N');
		Proposer changeProposer =proposerRepository.save(updatedeletedStatus);
		return changeProposer;
	}

	@Override
	public Proposer registerProposer(ProposerDto proposerDto) {
		// TODO Auto-generated method stub
		if (proposerDto.getFullName() == null || proposerDto.getFullName().trim().isEmpty()) {
		    throw new IllegalArgumentException("Enter the Full Name");
		}

		if (proposerDto.getEmail() == null || proposerDto.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$\r\n"
				+ "") ) {
		    throw new IllegalArgumentException("Enter a valid Email");
		}

		if (proposerDto.getMobileNumber() == null || proposerDto.getMobileNumber().length() != 10 || !proposerDto.getMobileNumber().matches("\\d+") ) {
		    throw new IllegalArgumentException("Enter valid mobile no.");
		}
		boolean pan = proposerRepository.existsByPanNumber(proposerDto.getPanNumber());
		if (proposerDto.getPanNumber() == null || proposerDto.getPanNumber().length() != 10 || !proposerDto.getPanNumber().matches("^[A-Z]{5}[0-9]{4}[A-Z]{1}$") || pan) {
		    throw new IllegalArgumentException("Enter a valid PAN number");
		}
		boolean aadhar =proposerRepository.existsByAadharNumber(proposerDto.getAadharNumber());
		if (proposerDto.getAadharNumber() == null || proposerDto.getAadharNumber().length() != 12 || !proposerDto.getAadharNumber().matches("\\d+") || aadhar ) {
		    throw new IllegalArgumentException("Enter a valid Aadhar Number");
		}

		if (proposerDto.getAlternateMobileNumber() == null || proposerDto.getAlternateMobileNumber().length() != 10 || !proposerDto.getAlternateMobileNumber().matches("\\d+")) {
		    throw new IllegalArgumentException("Enter a valid Alternate Mobile Number");
		}

		if (proposerDto.getPincode() == null || proposerDto.getPincode().length() != 6 || !proposerDto.getPincode().matches("\\d+")) {
		    throw new IllegalArgumentException("Enter a valid Pincode");
		}

		if (proposerDto.getAnnualIncome() == null || proposerDto.getAnnualIncome().isEmpty() || !proposerDto.getAnnualIncome().matches("\\d+")) {
		    throw new IllegalArgumentException("Enter a valid Annual Income");
		}

		if (proposerDto.getAddressLine1() == null || proposerDto.getAddressLine1().trim().isEmpty()) {
		    throw new IllegalArgumentException("Enter Address Line 1");
		}

		if (proposerDto.getAddressLine2() == null || proposerDto.getAddressLine2().trim().isEmpty()) {
		    throw new IllegalArgumentException("Enter Address Line 2");
		}

		if (proposerDto.getAddressLine3() == null || proposerDto.getAddressLine3().trim().isEmpty()) {
		    throw new IllegalArgumentException("Enter Address Line 3");
		}
		
		

		Proposer proposer = new Proposer();
		proposer.setStatus('Y');
		proposer.setTitle(proposerDto.getTitle());
		proposer.setFullName(proposerDto.getFullName());
		proposer.setGender(proposerDto.getGender());
		proposer.setDateOfBirth(proposerDto.getDateOfBirth());
		proposer.setAnnualIncome(proposerDto.getAnnualIncome());
		proposer.setPanNumber(proposerDto.getPanNumber());
		proposer.setAadharNumber(proposerDto.getAadharNumber());
		proposer.setMaritalStatus(proposerDto.getMaritalStatus());
		proposer.setEmail(proposerDto.getEmail());
		proposer.setMobileNumber(proposerDto.getMobileNumber());
		proposer.setAlternateMobileNumber(proposerDto.getAlternateMobileNumber());
		proposer.setAddressLine1(proposerDto.getAddressLine1());
		proposer.setAddressLine2(proposerDto.getAddressLine2());
		proposer.setAddressLine3(proposerDto.getAddressLine3());
		proposer.setPincode(proposerDto.getPincode());
		proposer.setArea(proposerDto.getArea());
		proposer.setTown(proposerDto.getTown());
		proposer.setCity(proposerDto.getCity());
		proposer.setState(proposerDto.getState());
		proposerRepository.save(proposer);
		
		return proposer;
	}

	@Override
	public Proposer updateProposerUsingDto(Long id, ProposerDto proposerDto) {
		// TODO Auto-generated method stub
		Optional<Proposer> getProposer = proposerRepository.findByIdAndStatus(id,'Y');
		
		if (proposerDto.getFullName() == null || proposerDto.getFullName().trim().isEmpty()) {
		    throw new IllegalArgumentException("Enter the Full Name");
		}

		if (proposerDto.getEmail() == null || proposerDto.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$\r\n"
				+ "") ) {
		    throw new IllegalArgumentException("Enter a valid Email");
		}

		if (proposerDto.getMobileNumber() == null || proposerDto.getMobileNumber().length() != 10 || !proposerDto.getMobileNumber().matches("\\d+") ) {
		    throw new IllegalArgumentException("Enter valid mobile no.");
		}
		boolean pan =proposerRepository.existsByPanNumber(proposerDto.getPanNumber());
		if (proposerDto.getPanNumber() == null || proposerDto.getPanNumber().length() != 10 || !proposerDto.getPanNumber().matches("^[A-Z]{5}[0-9]{4}[A-Z]{1}$") || pan  ) {
		    throw new IllegalArgumentException("Enter a valid PAN number");
		}
		boolean aadhar = proposerRepository.existsByAadharNumber(proposerDto.getAadharNumber());
		if (proposerDto.getAadharNumber() == null || proposerDto.getAadharNumber().length() != 12 || !proposerDto.getAadharNumber().matches("\\d+") || aadhar  ) {
		    throw new IllegalArgumentException("Enter a valid Aadhar Number");
		}

		if (proposerDto.getAlternateMobileNumber() == null || proposerDto.getAlternateMobileNumber().length() != 10 || !proposerDto.getAlternateMobileNumber().matches("\\d+")) {
		    throw new IllegalArgumentException("Enter a valid Alternate Mobile Number");
		}

		if (proposerDto.getPincode() == null || proposerDto.getPincode().length() != 6 || !proposerDto.getPincode().matches("\\d+")) {
		    throw new IllegalArgumentException("Enter a valid Pincode");
		}

		if (proposerDto.getAnnualIncome() == null || proposerDto.getAnnualIncome().isEmpty() || !proposerDto.getAnnualIncome().matches("\\d+")) {
		    throw new IllegalArgumentException("Enter a valid Annual Income");
		}

		if (proposerDto.getAddressLine1() == null || proposerDto.getAddressLine1().trim().isEmpty()) {
		    throw new IllegalArgumentException("Enter Address Line 1");
		}

		if (proposerDto.getAddressLine2() == null || proposerDto.getAddressLine2().trim().isEmpty()) {
		    throw new IllegalArgumentException("Enter Address Line 2");
		}

		if (proposerDto.getAddressLine3() == null || proposerDto.getAddressLine3().trim().isEmpty()) {
		    throw new IllegalArgumentException("Enter Address Line 3");
		}
		 Proposer proposer = getProposer.get() ;
	
			proposer.setTitle(proposerDto.getTitle());
			proposer.setFullName(proposerDto.getFullName());
			proposer.setGender(proposerDto.getGender());
			proposer.setDateOfBirth(proposerDto.getDateOfBirth());
			proposer.setAnnualIncome(proposerDto.getAnnualIncome());
			proposer.setPanNumber(proposerDto.getPanNumber());
			proposer.setAadharNumber(proposerDto.getAadharNumber());
			proposer.setMaritalStatus(proposerDto.getMaritalStatus());
			proposer.setEmail(proposerDto.getEmail());
			proposer.setMobileNumber(proposerDto.getMobileNumber());
			proposer.setAlternateMobileNumber(proposerDto.getAlternateMobileNumber());
			proposer.setAddressLine1(proposerDto.getAddressLine1());
			proposer.setAddressLine2(proposerDto.getAddressLine2());
			proposer.setAddressLine3(proposerDto.getAddressLine3());
			proposer.setPincode(proposerDto.getPincode());
			proposer.setArea(proposerDto.getArea());
			proposer.setTown(proposerDto.getTown());
			proposer.setCity(proposerDto.getCity());
			proposer.setState(proposerDto.getState());
			
			Proposer updatedProposer = proposerRepository.save(proposer);
			 return updatedProposer;
	}

}
