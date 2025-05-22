package com.example.demo.repository;




import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Proposer;
@Repository
public interface ProposerRepository extends JpaRepository<Proposer, Long> {
	

	Page<Proposer> findByFullName(String fullName,Pageable pageable);
	
	 boolean existsByPanNumber(String panNumber);
	 
	 boolean existsByAadharNumber(String aadharNumber);
	 
	 boolean existsByMobileNumber(String mobileNumber);
	 
	 boolean existsByAlternateMobileNumber(String alternateMobileNumber);
	 
	 List<Proposer> findByStatus(char status);
	 
	 Optional<Proposer> findByIdAndStatus(Long id, char status);
	 
	 
	 
	 
}
