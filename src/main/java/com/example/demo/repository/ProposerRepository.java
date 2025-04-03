package com.example.demo.repository;




import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import com.example.demo.model.Proposer;

public interface ProposerRepository extends JpaRepository<Proposer, Long> {

	Page<Proposer> findByFullName(String fullName,Pageable pageable);
	
	 boolean existsByPanNumber(String panNumber);
	 
	 boolean existsByAadharNumber(String aadharNumber);
	 
	 boolean existsByMobileNumber(String mobileNumber);
	 
	 boolean existsByAlternateMobileNumber(String alternateMobileNumber);
}
