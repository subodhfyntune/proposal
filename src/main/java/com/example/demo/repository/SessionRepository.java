package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.SessionEntity;

public interface SessionRepository extends JpaRepository<SessionEntity, Integer>{
	
	Optional<SessionEntity> findBySessionId(String sessionId);

}
