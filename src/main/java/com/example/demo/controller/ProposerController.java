package com.example.demo.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
import com.example.demo.pagination.ProposerPage;
import com.example.demo.repository.ProposerRepository;
import com.example.demo.service.ProposerService;



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
	public ResponseHandler<List<Proposer>> getProposer(){
		ResponseHandler<List<Proposer>> responseHandler = new ResponseHandler<>();
		try {
			List<Proposer>allProposers =proposerService.getAllProposer();
			 responseHandler.setStatus("success");
		        responseHandler.setData(allProposers);
		        responseHandler.setMessage("get all Proposer data");
		        
		} catch (Exception e) {
			e.printStackTrace();
	        responseHandler.setStatus("Registration Failed");
	        responseHandler.setData(new ArrayList<>());
	        responseHandler.setMessage(e.getMessage());
		}
		return responseHandler;	
		
	}
	
	@PutMapping("/update/{id}")
	public ResponseHandler<Proposer> updateProposer(@PathVariable Long id,@RequestBody Proposer updateProposer){
		ResponseHandler<Proposer> responseHandler = new ResponseHandler<>();
		try {
			Proposer updatedProposer =  proposerService.updateProposer(id, updateProposer);
			 responseHandler.setStatus("success");
	        responseHandler.setData(updatedProposer);
	        responseHandler.setMessage("Updated Successfull");
		} catch (Exception e) {
			e.printStackTrace();
	        responseHandler.setStatus("Update Failed");
	        responseHandler.setData(new ArrayList<>());
	        responseHandler.setMessage(e.getMessage());
		}
		
		return responseHandler;
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseHandler<?> deleteProposer(@PathVariable Long id) {
		ResponseHandler<Proposer> responseHandler = new ResponseHandler<>();
		try {
			 proposerService.deleteProposer(id);
			 responseHandler.setStatus("success");
		        responseHandler.setData(new ArrayList<>());
		        responseHandler.setMessage("Proposer with ID " + id + " has been successfully deleted.");
		} catch (Exception e) {
			e.printStackTrace();
	        responseHandler.setStatus("delete Failed");
	        responseHandler.setData(new ArrayList<>());
	        responseHandler.setMessage(e.getMessage());
		}
		return responseHandler;
	    
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
	        
	        
	        responseHandler.setStatus("success");
	        responseHandler.setData(registeredProposer);
	        responseHandler.setMessage("Registered");
	        
	    }catch (IllegalArgumentException e) {
	        
	    	e.printStackTrace();
	        responseHandler.setStatus("Registration Failed");
	        responseHandler.setData(new ArrayList<>());
	        responseHandler.setMessage(e.getMessage());
	    } catch (Exception e) {
	        
	    	e.printStackTrace();
	        responseHandler.setStatus("Registration Failed");
	        responseHandler.setData(new ArrayList<>());
	        responseHandler.setMessage("failed");
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
	        
	        responseHandler.setStatus("success");
	        responseHandler.setData(updatedProposer);  
	        responseHandler.setMessage("Proposer updated successfully");
	        
	    }catch (IllegalArgumentException e) {
	        
	    	e.printStackTrace();
	        responseHandler.setStatus("update Failed");
	        responseHandler.setData(new ArrayList<>());
	        responseHandler.setMessage(e.getMessage());
	    } catch (Exception e) {
	    	e.printStackTrace();
	        
	        responseHandler.setStatus("failed to update proposal");
	        responseHandler.setData(new ArrayList<>());  
	        responseHandler.setMessage("failed to update");
	    }
	    
	    return responseHandler;
	}

	
	@GetMapping("/getOccupation")
	public ResponseHandler<List<Occupation>> getAllOccupations() {
	    ResponseHandler<List<Occupation>> responseHandler = new ResponseHandler<>();
	    try {
	        
	        responseHandler.setStatus("success");
	        responseHandler.setData(Arrays.asList(Occupation.values()));
	        return responseHandler;
	    }catch (IllegalArgumentException e) {
	        
	    	e.printStackTrace();
	        responseHandler.setStatus("occupation Failed");
	        responseHandler.setData(new ArrayList<>());
	        responseHandler.setMessage(e.getMessage());
	    } catch (Exception e) {
	        e.printStackTrace();
	        responseHandler.setStatus("failed to get occupation");
	        responseHandler.setData(new ArrayList<>());  
	        responseHandler.setMessage("occupation failed");
	        return responseHandler;
	    }
		return responseHandler;
	}

	
	@GetMapping("/getGender")
	public ResponseHandler<List<Gender>> getAllGender() {
	    ResponseHandler<List<Gender>> responseHandler = new ResponseHandler<>();
	    try {
	        
	        responseHandler.setStatus("success");
	        responseHandler.setData(Arrays.asList(Gender.values()));
	        return responseHandler;
	    }catch (IllegalArgumentException e) {
	        
	    	e.printStackTrace();
	        responseHandler.setStatus("failed to get Gender");
	        responseHandler.setData(new ArrayList<>());
	        responseHandler.setMessage(e.getMessage());
	    } catch (Exception e) {
	        e.printStackTrace();
	        responseHandler.setStatus("failed to get Gender");
	        responseHandler.setData(new ArrayList<>());  
	        responseHandler.setMessage("failed to get Gender");
	        return responseHandler;
	    }
		return responseHandler;
	}
	
	@GetMapping("/getMaritalStatus")
	public ResponseHandler<List<MaritalStatus>> getAllMaritalStatus(){
		ResponseHandler<List<MaritalStatus>> responseHandler = new ResponseHandler<>();
		try {
	        
	        responseHandler.setStatus("success");
	        responseHandler.setData(Arrays.asList(MaritalStatus.values()));
	        return responseHandler;
	    } catch (Exception e) {
	        e.printStackTrace();
	        responseHandler.setStatus("failed to get Marital Status");
	        responseHandler.setData(new ArrayList<>());  
	        responseHandler.setMessage(e.getMessage());
	        return responseHandler;
	    }
	}
	
	@GetMapping("/getNationality")
	public ResponseHandler<List<Nationality>>  getAllNationality(){
		ResponseHandler<List<Nationality>> responseHandler = new ResponseHandler<>();
		
		try {
			 	responseHandler.setStatus("success");
		        responseHandler.setData(Arrays.asList(Nationality.values()));
		        return responseHandler;
		} catch (Exception e) {
			e.printStackTrace();
	        responseHandler.setStatus("failed to get Nationality");
	        responseHandler.setData(new ArrayList<>());  
	        responseHandler.setMessage(e.getMessage());
	        return responseHandler;
		}
		
	}
	
	@GetMapping("/getTitle")
	public ResponseHandler<List<Title>>  getAllTitle(){
		ResponseHandler<List<Title>> responseHandler = new ResponseHandler<>();
		
		try {
			 	responseHandler.setStatus("success");
		        responseHandler.setData(Arrays.asList(Title.values()));
		        return responseHandler;
		} catch (Exception e) {
			e.printStackTrace();
	        responseHandler.setStatus("failed to get Title");
	        responseHandler.setData(new ArrayList<>());  
	        responseHandler.setMessage(e.getMessage());
	        return responseHandler;
		}
	}

	@GetMapping("/getTown")
	public ResponseHandler<List<Town>> getAllTown() {
		ResponseHandler<List<Town>> responseHandler = new ResponseHandler<>();
		try {
			responseHandler.setStatus("success");
			responseHandler.setData(Arrays.asList(Town.values()));
			return responseHandler;

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			responseHandler.setStatus("failed to get town");
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage(e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			responseHandler.setStatus("failed to get town");
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage(e.getMessage());

		}
		return responseHandler;
	}
	@GetMapping("/getArea")
	public ResponseHandler<List<Area>> getAllArea(){
		ResponseHandler<List<Area>> responseHandler = new ResponseHandler<>();
		try {
			responseHandler.setStatus("success");
			responseHandler.setData(Arrays.asList(Area.values()));
			return responseHandler;
		} catch (Exception e) {
			e.printStackTrace();
			responseHandler.setStatus("failed to get status");
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage(e.getMessage());
			return responseHandler;
		}
		
	}
	
	@GetMapping("/pagination/{name}")
	public Page<Proposer> getProposerByName(@PathVariable String name,
			@RequestParam(defaultValue = "0")int page,
			@RequestParam(defaultValue = "5")int size){
		return proposerRepository.findByFullName(name,PageRequest.of(page, size));
	}
	
	@PostMapping("/getbyPageAndSize")
	public ResponseHandler<List<Proposer>> getAllBysortingAndPagination(@RequestBody ProposerPage proposerPage){
	    ResponseHandler<List<Proposer>> responseHandler = new ResponseHandler<>();
	    try {
	        // Fetch proposers using pagination and sorting
	        List<Proposer> proposers = proposerService.getAllProposersByPagingAndSorting(proposerPage);

	        if (proposers.isEmpty()) {
	            responseHandler.setStatus("no records found");
	            responseHandler.setData(new ArrayList<>());
	            responseHandler.setMessage("No proposers found matching the criteria");
	        } else {
	            responseHandler.setStatus("success");
	            responseHandler.setData(proposers);
	            responseHandler.setMessage("Proposers fetched successfully");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        responseHandler.setStatus("error");
	        responseHandler.setData(new ArrayList<>());
	        responseHandler.setMessage("An error occurred while fetching details");
	    }
	    return responseHandler;
	}

		

	
}
