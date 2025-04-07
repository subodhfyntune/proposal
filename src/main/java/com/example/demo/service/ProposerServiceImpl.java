package com.example.demo.service;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.HealthApplication;
import com.example.demo.dto.ProposerDto;
import com.example.demo.exception.ProposerDeletedAlready;
import com.example.demo.model.Gender;
import com.example.demo.model.Proposer;
import com.example.demo.pagination.ProposerPage;
import com.example.demo.repository.ProposerRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

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
		
		if(!deletedProposer.isPresent()) {
			throw new IllegalArgumentException("Id not found");
		}
		
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
		
		
		 String gender =  proposerDto.getGender();
		    if(gender != null && !gender.isEmpty()) {
		    	if(gender.equalsIgnoreCase(Gender.MALE.toString())) {
		    		proposer.setGender(Gender.MALE);
		    	}else if( gender.equalsIgnoreCase(Gender.FEMALE.toString())) {
		    		proposer.setGender(Gender.FEMALE);
		    	}else if( gender.equalsIgnoreCase(Gender.OTHER.toString())) {
		    		proposer.setGender(Gender.OTHER);
		    	}else {
		    		throw new IllegalArgumentException("enter corrrect gender");
		    	}
		    }else {
	    		throw new IllegalArgumentException("gender can not be null");
	    	}
		
		
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

//	@Override
//	public Proposer updateProposerUsingDto(Long id, ProposerDto proposerDto) {
//		Optional<Proposer> getProposer = proposerRepository.findByIdAndStatus(id, 'Y');
//
//		if (!getProposer.isPresent()) {
//			throw new IllegalArgumentException("Proposer not found with id " + id);
//		}
//		if (proposerDto.getFullName() == null || proposerDto.getFullName().trim().isEmpty()) {
//		    throw new IllegalArgumentException("Enter the Full Name");
//		}
//
//		if (proposerDto.getEmail() == null || proposerDto.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$\r\n"
//				+ "") ) {
//		    throw new IllegalArgumentException("Enter a valid Email");
//		}
//
//		if (proposerDto.getMobileNumber() == null || proposerDto.getMobileNumber().length() != 10 || !proposerDto.getMobileNumber().matches("\\d+") ) {
//		    throw new IllegalArgumentException("Enter valid mobile no.");
//		}
//		boolean pan = proposerRepository.existsByPanNumber(proposerDto.getPanNumber());
//		if (proposerDto.getPanNumber() == null || proposerDto.getPanNumber().length() != 10 || !proposerDto.getPanNumber().matches("^[A-Z]{5}[0-9]{4}[A-Z]{1}$") || pan) {
//		    throw new IllegalArgumentException("Enter a valid PAN number");
//		}
//		boolean aadhar =proposerRepository.existsByAadharNumber(proposerDto.getAadharNumber());
//		if (proposerDto.getAadharNumber() == null || proposerDto.getAadharNumber().length() != 12 || !proposerDto.getAadharNumber().matches("\\d+") || aadhar ) {
//		    throw new IllegalArgumentException("Enter a valid Aadhar Number");
//		}
//
//		if (proposerDto.getAlternateMobileNumber() == null || proposerDto.getAlternateMobileNumber().length() != 10 || !proposerDto.getAlternateMobileNumber().matches("\\d+")) {
//		    throw new IllegalArgumentException("Enter a valid Alternate Mobile Number");
//		}
//
//		if (proposerDto.getPincode() == null || proposerDto.getPincode().length() != 6 || !proposerDto.getPincode().matches("\\d+")) {
//		    throw new IllegalArgumentException("Enter a valid Pincode");
//		}
//
//		if (proposerDto.getAnnualIncome() == null || proposerDto.getAnnualIncome().isEmpty() || !proposerDto.getAnnualIncome().matches("\\d+")) {
//		    throw new IllegalArgumentException("Enter a valid Annual Income");
//		}
//
//		if (proposerDto.getAddressLine1() == null || proposerDto.getAddressLine1().trim().isEmpty()) {
//		    throw new IllegalArgumentException("Enter Address Line 1");
//		}
//
//		if (proposerDto.getAddressLine2() == null || proposerDto.getAddressLine2().trim().isEmpty()) {
//		    throw new IllegalArgumentException("Enter Address Line 2");
//		}
//
//		if (proposerDto.getAddressLine3() == null || proposerDto.getAddressLine3().trim().isEmpty()) {
//		    throw new IllegalArgumentException("Enter Address Line 3");
//		}
//		Proposer proposer = getProposer.get();
//		
//		proposer.setTitle(proposerDto.getTitle());
//		proposer.setFullName(proposerDto.getFullName());
//		proposer.setGender(proposerDto.getGender());
//		proposer.setDateOfBirth(proposerDto.getDateOfBirth());
//		proposer.setAnnualIncome(proposerDto.getAnnualIncome());
//		proposer.setPanNumber(proposerDto.getPanNumber());
//		proposer.setAadharNumber(proposerDto.getAadharNumber());
//		proposer.setMaritalStatus(proposerDto.getMaritalStatus());
//		proposer.setEmail(proposerDto.getEmail());
//		proposer.setMobileNumber(proposerDto.getMobileNumber());
//		proposer.setAlternateMobileNumber(proposerDto.getAlternateMobileNumber());
//		proposer.setAddressLine1(proposerDto.getAddressLine1());
//		proposer.setAddressLine2(proposerDto.getAddressLine2());
//		proposer.setAddressLine3(proposerDto.getAddressLine3());
//		proposer.setPincode(proposerDto.getPincode());
//		proposer.setArea(proposerDto.getArea());
//		proposer.setTown(proposerDto.getTown());
//		proposer.setCity(proposerDto.getCity());
//		proposer.setState(proposerDto.getState());
//		
//
//		Proposer updatedProposer = proposerRepository.save(proposer);
//		return updatedProposer;
//	}
//
//
//}
	
	@Override
	public Proposer updateProposerUsingDto(Long id, ProposerDto proposerDto) {
	    Optional<Proposer> getProposer = proposerRepository.findByIdAndStatus(id, 'Y');

	    if (!getProposer.isPresent()) {
	        throw new IllegalArgumentException("Proposer not found with id " + id);
	    }

	    Proposer proposer = getProposer.get();
	    
	   
	    if (proposerDto.getFullName() != null && !proposerDto.getFullName().trim().isEmpty()) {
	        proposer.setFullName(proposerDto.getFullName());
	    } else if (proposerDto.getFullName() == null || proposerDto.getFullName().trim().isEmpty()) {
	        throw new IllegalArgumentException("Enter the Full Name");
	    }

	   
	    if (proposerDto.getEmail() != null) {
	        if (!proposerDto.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
	            throw new IllegalArgumentException("Enter a valid Email");
	        }
	        proposer.setEmail(proposerDto.getEmail());
	    }

	    
	    if (proposerDto.getMobileNumber() != null) {
	        if (proposerDto.getMobileNumber().length() != 10 || !proposerDto.getMobileNumber().matches("\\d+")) {
	            throw new IllegalArgumentException("Enter valid mobile no.");
	        }
	        proposer.setMobileNumber(proposerDto.getMobileNumber());
	    }

	   
	    if (proposerDto.getPanNumber() != null) {
	       
	        if (proposerDto.getPanNumber().length() != 10 || !proposerDto.getPanNumber().matches("^[A-Z]{5}[0-9]{4}[A-Z]{1}$") ) {
	            throw new IllegalArgumentException("Enter a valid PAN number");
	        }
	        proposer.setPanNumber(proposerDto.getPanNumber());
	    }

	    
	    if (proposerDto.getAadharNumber() != null) {
	        
	        if (proposerDto.getAadharNumber().length() != 12 || !proposerDto.getAadharNumber().matches("\\d+") ) {
	            throw new IllegalArgumentException("Enter a valid Aadhar Number");
	        }
	        proposer.setAadharNumber(proposerDto.getAadharNumber());
	    }

	    
	    if (proposerDto.getAlternateMobileNumber() != null) {
	        if (proposerDto.getAlternateMobileNumber().length() != 10 || !proposerDto.getAlternateMobileNumber().matches("\\d+")) {
	            throw new IllegalArgumentException("Enter a valid Alternate Mobile Number");
	        }
	        proposer.setAlternateMobileNumber(proposerDto.getAlternateMobileNumber());
	    }

	   
	    if (proposerDto.getPincode() != null) {
	        if (proposerDto.getPincode().length() != 6 || !proposerDto.getPincode().matches("\\d+")) {
	            throw new IllegalArgumentException("Enter a valid Pincode");
	        }
	        proposer.setPincode(proposerDto.getPincode());
	    }

	    
	    if (proposerDto.getAnnualIncome() != null && !proposerDto.getAnnualIncome().isEmpty() && proposerDto.getAnnualIncome().matches("\\d+")) {
	        proposer.setAnnualIncome(proposerDto.getAnnualIncome());
	    } else if (proposerDto.getAnnualIncome() != null) {
	        throw new IllegalArgumentException("Enter a valid Annual Income");
	    }

	   
	    if (proposerDto.getAddressLine1() != null && !proposerDto.getAddressLine1().trim().isEmpty()) {
	        proposer.setAddressLine1(proposerDto.getAddressLine1());
	    } else if (proposerDto.getAddressLine1() == null || proposerDto.getAddressLine1().trim().isEmpty()) {
	        throw new IllegalArgumentException("Enter Address Line 1");
	    }

	    if (proposerDto.getAddressLine2() != null && !proposerDto.getAddressLine2().trim().isEmpty()) {
	        proposer.setAddressLine2(proposerDto.getAddressLine2());
	    } else if (proposerDto.getAddressLine2() == null || proposerDto.getAddressLine2().trim().isEmpty()) {
	        throw new IllegalArgumentException("Enter Address Line 2");
	    }

	    if (proposerDto.getAddressLine3() != null && !proposerDto.getAddressLine3().trim().isEmpty()) {
	        proposer.setAddressLine3(proposerDto.getAddressLine3());
	    } else if (proposerDto.getAddressLine3() == null || proposerDto.getAddressLine3().trim().isEmpty()) {
	        throw new IllegalArgumentException("Enter Address Line 3");
	    }

	    
	    if (proposerDto.getTitle() != null) proposer.setTitle(proposerDto.getTitle());
//	    if (proposerDto.getGender() != null) proposer.setGender(proposerDto.getGender());
	    
	    String gender =  proposerDto.getGender();
	    if(gender != null && !gender.isEmpty()) {
	    	if(gender.equalsIgnoreCase(Gender.MALE.toString())) {
	    		proposer.setGender(Gender.MALE);
	    	}else if( gender.equalsIgnoreCase(Gender.FEMALE.toString())) {
	    		proposer.setGender(Gender.FEMALE);
	    	}else if( gender.equalsIgnoreCase(Gender.OTHER.toString())) {
	    		proposer.setGender(Gender.OTHER);
	    	}else {
	    		throw new IllegalArgumentException("enter corrrect gender");
	    	}
	    }else {
    		throw new IllegalArgumentException("gender can not be null");
    	}
	    
	    if (proposerDto.getDateOfBirth() != null) proposer.setDateOfBirth(proposerDto.getDateOfBirth());
	    if (proposerDto.getMaritalStatus() != null) proposer.setMaritalStatus(proposerDto.getMaritalStatus());
	    if (proposerDto.getArea() != null) proposer.setArea(proposerDto.getArea());
	    if (proposerDto.getTown() != null) proposer.setTown(proposerDto.getTown());
	    if (proposerDto.getCity() != null) proposer.setCity(proposerDto.getCity());
	    if (proposerDto.getState() != null) proposer.setState(proposerDto.getState());

	   
	    return proposerRepository.save(proposer);
	}

	@Autowired
	private EntityManager entityManager;

	@Override
	public List<Proposer> getAllProposersByPagingAndSorting(ProposerPage proposerPage) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Proposer> criteriaQuery = criteriaBuilder.createQuery(Proposer.class);
		Root<Proposer> root = criteriaQuery.from(Proposer.class);

		if (proposerPage.getSortBy() != null && proposerPage.getSortOrder() != null) {
			String sortBy = proposerPage.getSortBy();
			if ("ASC".equalsIgnoreCase(proposerPage.getSortBy())) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get(sortBy)));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get(sortBy)));
			}
		}

		if (proposerPage.getPageNumber() <= 0 && proposerPage.getPageSize() <= 0) {
			return entityManager.createQuery(criteriaQuery).getResultList();
		} else {
			Integer size = proposerPage.getPageSize();
			Integer page = proposerPage.getPageNumber();

			TypedQuery<Proposer> typedQuery = entityManager.createQuery(criteriaQuery);
			typedQuery.setFirstResult((page - 1) * size);
			typedQuery.setMaxResults(size);
			return typedQuery.getResultList();

		}
	}


}

