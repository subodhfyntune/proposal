package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.QueueTable;

import java.util.List;


public interface QueueRepository extends JpaRepository<QueueTable, Long> {

	Optional<QueueTable> findFirstByIsProcessed(Character isProcessed);
	
	List<QueueTable> findByIsProcessed(Character isProcessed);

}
