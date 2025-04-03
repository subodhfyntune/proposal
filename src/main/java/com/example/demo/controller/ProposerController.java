package com.example.demo.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ProposerDto;

import com.example.demo.dto.handler.ResponseHandler;
import com.example.demo.model.Area;
import com.example.demo.model.Gender;
import com.example.demo.model.MaritalStatus;
import com.example.demo.model.Nationality;
import com.example.demo.model.Occupation;
import com.example.demo.model.Proposer;
import com.example.demo.model.Title;
import com.example.demo.model.Town;
import com.example.demo.repository.ProposerRepository;
import com.example.demo.service.ProposerService;

import jakarta.persistence.Id;

@RestController
@RequestMapping("/api")
public class ProposerController {
	@Autowired
    private  ProposerRepository proposerRepository;

	
	private ProposerService proposerService;
	
	public ProposerController(ProposerService proposerService) {
		super();
		
		this.proposerService = proposerService;
	}
//	@PostMapping("/registerProposer")
//	public ResponseEntity<Proposer>  registerProposer(@RequestBody Proposer proposer){
//		Proposer registeredPraposer =proposerService.registerProposer(proposer);
//		return new ResponseEntity<Proposer>(registeredPraposer,HttpStatus.OK);
//	}
	
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
	public ResponseEntity<?> deleteProposer(@PathVariable Long id) {
	    proposerService.deleteProposer(id);
	    
	    return ResponseEntity.ok("Proposer with ID " + id + " has been successfully deleted.");
	}

	
//	@PostMapping("/registerwithDto")
//	public ResponseEntity<Proposer> registerUsingDTO(@RequestBody ProposerDto proposerDto){
//		Proposer registeredPraposer = proposerService.registerProposer(proposerDto);
//		return new  ResponseEntity<>(registeredPraposer,HttpStatus.CREATED);
//	}
//	@PostMapping("/registerwithDto")
//	public ResponseEntity<ResponseHandler<Proposer>> registerUsingDTO(@RequestBody ProposerDto proposerDto) {
//	    
//	    Proposer registeredProposer = proposerService.registerProposer(proposerDto);
//	    
//	    ResponseHandler<Proposer> responseHandler = new ResponseHandler<>("sucess", registeredProposer, "registred");
//		return new ResponseEntity<>(responseHandler,HttpStatus.OK);
//	    
//	}
	
	@PostMapping("/registerwithDto")
	public ResponseHandler<Proposer> registerUsingDTO(@RequestBody ProposerDto proposerDto) {
	    ResponseHandler<Proposer> responseHandler = new ResponseHandler<>();
	    
	    try {
	        Proposer registeredProposer = proposerService.registerProposer(proposerDto);
	        
	        // Set fields in the response handler for success case
	        responseHandler.setStatus("success");
	        responseHandler.setData(registeredProposer);
	        responseHandler.setMessage("Registered");
	        
	    } catch (Exception e) {
	        // Set fields in the response handler for error case
	    	e.printStackTrace();
	        responseHandler.setStatus("Registration Failed");
	        responseHandler.setData(null);
	        responseHandler.setMessage(e.getMessage());
	    }
	    
	    return responseHandler;
	}



//	@PutMapping("/updatewithDto/{id}")
//	public ResponseEntity<Proposer> updateProposer(@PathVariable Long id,@RequestBody ProposerDto updateProposer){
//		
//		Proposer updatedProposer =  proposerService.updateProposerUsingDto(id, updateProposer);
//		return new ResponseEntity<Proposer>(updatedProposer,HttpStatus.OK);
//	}
	
	@PutMapping("/updatewithDto/{id}")
	public ResponseHandler<Proposer> updateProposer(@PathVariable Long id, @RequestBody ProposerDto updateProposer) {
	    ResponseHandler<Proposer> responseHandler = new ResponseHandler<>();
	    
	    try {
	        Proposer updatedProposer = proposerService.updateProposerUsingDto(id, updateProposer);
	        
	        // Set fields in the response handler for success case
	        responseHandler.setStatus("success");
	        responseHandler.setData(updatedProposer);  // Set the updated proposer object
	        responseHandler.setMessage("Proposer updated successfully");
	        
	    } catch (Exception e) {
	    	e.printStackTrace();
	        // Set fields in the response handler for error case
	        responseHandler.setStatus("failed to update proposal");
	        responseHandler.setData(new ArrayList<>());  // Set an empty ArrayList on error
	        responseHandler.setMessage(  e.getMessage());
	    }
	    
	    return responseHandler;
	}

	
	@GetMapping("/getOccupation")
	public List<Occupation> getAllOcupations(){
		return
				Arrays.asList(Occupation.values());
	}
	
	@GetMapping("/getGender")
	public List<Gender> getAllGender(){
		return
				Arrays.asList(Gender.values());
	}
	
	@GetMapping("/getMaritalStatus")
	public List<MaritalStatus> getAllMaritalStatus(){
		return
				Arrays.asList(MaritalStatus.values());
	}
	@GetMapping("/getNationality")
	public List<Nationality> getAllNationality(){
		return Arrays.asList(Nationality.values());
	}
	
	@GetMapping("/getTitle")
	public List<Title> getAllTitle(){
		return Arrays.asList(Title.values());
	}
	@GetMapping("/getTown")
	public List<Town> getAllTown(){
		return Arrays.asList(Town.values());
	}
	@GetMapping("/getArea")
	public List<Area> getAllArea(){
		return Arrays.asList(Area.values());
	}
	
	@GetMapping("/pagination/{name}")
	public Page<Proposer> getProposerByName(@PathVariable String name,
			@RequestParam(defaultValue = "0")int page,
			@RequestParam(defaultValue = "5")int size){
		return proposerRepository.findByFullName(name,PageRequest.of(page, size));
	}
	
	
	
	
}
