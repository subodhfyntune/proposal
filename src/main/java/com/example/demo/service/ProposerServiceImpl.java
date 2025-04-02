package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.HealthApplication;
import com.example.demo.exception.ProposerDeletedAlready;
import com.example.demo.model.Proposer;
import com.example.demo.repository.ProposerRepository;

@Service
public class ProposerServiceImpl implements ProposerService
{

    

	private ProposerRepository proposerRepository;
	@Autowired
	public ProposerServiceImpl(ProposerRepository proposerRepository, HealthApplication healthApplication) {
		super();
		this.proposerRepository = proposerRepository;
		
	}

	@Override
	public Proposer registerProposer(Proposer proposer) {
		        proposer.setStatus('Y');
		Proposer registerProposer =  proposerRepository.save(proposer);
		return registerProposer;
	}

	@Override
	public List<Proposer> getAllProposer() {
		List<Proposer> proposers = proposerRepository.findAll();
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

}
