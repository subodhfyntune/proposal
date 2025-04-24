package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.QueueTable;

public interface QueueRepository extends JpaRepository<QueueTable, Long> {

}
