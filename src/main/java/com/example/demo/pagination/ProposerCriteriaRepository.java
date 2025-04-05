package com.example.demo.pagination;



import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Proposer;
import com.example.demo.repository.ProposerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Repository
public class ProposerCriteriaRepository {

//    private final ProposerRepository proposerRepository;
//	@Autowired
//	private EntityManager entityManager;
//	@Autowired
//	private  CriteriaBuilder criteriaBuilder;
//
//    ProposerCriteriaRepository(ProposerRepository proposerRepository) {
//        this.proposerRepository = proposerRepository;
//    }
//	
//	public Pageable findAllWithFilters(ProposerPage proposerPage, ProposerSearchCriteria proposerSearchCriteria){
//		
//		CriteriaQuery<Proposer> criteriaQuery = criteriaBuilder.createQuery(Proposer.class);
//		Root <Proposer> proposerRoot = criteriaQuery.from(Proposer.class);
//		Predicate predicate = getPredicate( proposerSearchCriteria,proposerRoot);
//		return null;
//		
//	}
//
//	private Predicate getPredicate(ProposerSearchCriteria proposerSearchCriteria, Root<Proposer> proposerRoot) {
//		// TODO Auto-generated method stub
//		List<Predicate> predicates = new ArrayList<>();
//		if(Objects.nonNull(proposerSearchCriteria.getFullName())) {
//			predicates.add(criteriaBuilder.like("fullName", "%" +  proposerSearchCriteria.getFullName() + "%"));
//		}
//		return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
//		
//	}
}
