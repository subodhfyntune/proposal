package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.ProposerDto;
import com.example.demo.model.Proposer;

public interface ProposerService {

//	public Proposer registerProposer(Proposer proposer);
	public List<Proposer> getAllProposer();
	public Proposer updateProposer(Long id,Proposer proposer);
	public Proposer deleteProposer(Long id);
	
	public Proposer registerProposer(ProposerDto proposerDto);
	public Proposer updateProposerUsingDto(Long id  ,ProposerDto proposerDto);
	
	
}
