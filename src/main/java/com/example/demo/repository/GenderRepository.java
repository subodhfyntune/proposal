package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.GenderType;
@Repository
public interface GenderRepository extends JpaRepository<GenderType, Integer> {

	Optional<GenderType> findByType(String type);
}
