package com.example.demo.pagination;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jakarta.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Proposer;
import com.example.demo.repository.ProposerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;

import jakarta.persistence.criteria.Root;

@Repository
public class ProposerCriteriaRepository {

	private final ProposerRepository proposerRepository;
	private final EntityManager entityManager;
	private final CriteriaBuilder criteriaBuilder;

	@Autowired
	public ProposerCriteriaRepository(ProposerRepository proposerRepository, EntityManager entityManager) {
		this.proposerRepository = proposerRepository;
		this.entityManager = entityManager;
		this.criteriaBuilder = entityManager.getCriteriaBuilder();
	}

	private Predicate getPredicate(ProposerSearchCriteria proposerSearchCriteria, Root<Proposer> proposerRoot) {
		List<Predicate> predicates = new ArrayList<>();
		if (Objects.nonNull(proposerSearchCriteria.getFullName())) {
			predicates.add(criteriaBuilder.like(proposerRoot.get("fullName"),
					"%" + proposerSearchCriteria.getFullName() + "%"));
		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

	}
}
