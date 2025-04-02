package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Proposer;
import com.example.demo.service.ProposerService;

import jakarta.persistence.Id;

@RestController
@RequestMapping("/api")
public class ProposerController {

	private ProposerService proposerService;
	@Autowired
	public ProposerController(ProposerService proposerService) {
		super();
		this.proposerService = proposerService;
	}
	@PostMapping("/registerProposer")
	public ResponseEntity<Proposer>  registerProposer(@RequestBody Proposer proposer){
		Proposer registeredPraposer =proposerService.registerProposer(proposer);
		return new ResponseEntity<Proposer>(registeredPraposer,HttpStatus.OK);
	}
	
	@GetMapping("/get")
	public ResponseEntity<List<Proposer>> getProposer(){
		List<Proposer>allProposers =proposerService.getAllProposer();
		return new ResponseEntity<List<Proposer>>(allProposers,HttpStatus.OK);
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<Proposer> updateProposer(@PathVariable Long id,@RequestBody Proposer updateProposer){
		
		Proposer updatedProposer =  proposerService.updateProposer(id, updateProposer);
		return new ResponseEntity<Proposer>(updatedProposer,HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deletePraposer(@PathVariable Long id) {
		  proposerService.deleteProposer(id);
		  return ResponseEntity.ok().build();
	}
	
	
}
