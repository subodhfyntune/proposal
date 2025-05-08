package com.example.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.example.demo.config.JwtUtil;
import com.example.demo.dto.ProposerDto;
import com.example.demo.enums.Area;
import com.example.demo.enums.Gender;
import com.example.demo.enums.MaritalStatus;
import com.example.demo.enums.Nationality;
import com.example.demo.enums.Occupation;
import com.example.demo.enums.Title;
import com.example.demo.enums.Town;
import com.example.demo.model.Product;
import com.example.demo.model.Proposer;
import com.example.demo.pagination.ProposerPage;
import com.example.demo.repository.ProposerRepository;
import com.example.demo.response.ResponseHandler;
import com.example.demo.service.ProposerService;
import com.example.demo.serviceimpl.ProposerServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class ProposerController {

	
	@Autowired
	private ProposerRepository proposerRepository;

	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private ProposerService proposerService;

	

//	@PostMapping("/registerProposer")
//	public ResponseEntity<Proposer>  registerProposer(@RequestBody Proposer proposer){
//		Proposer registeredPraposer =proposerService.registerProposer(proposer);
//		return new ResponseEntity<Proposer>(registeredPraposer,HttpStatus.OK);
//	}

	@GetMapping("/get")
	public ResponseHandler<List<Proposer>> getProposer( HttpServletRequest request) {
		
		String header = request.getHeader("Authorization");
		System.err.println("header >>>>>"+header);
		String token = null;
		if(header!=null && header.startsWith("Bearer ")) {
			token = header.substring(7);
		}
		String username = jwtUtil.extractUsername(token);
		Long UserId = jwtUtil.extractUserId(token);

		System.err.println(username);
		System.err.println(UserId);

		
		ResponseHandler<List<Proposer>> responseHandler = new ResponseHandler<>();
		try {
			List<Proposer> allProposers = proposerService.getAllProposer();
			responseHandler.setStatus("success");
			responseHandler.setData(allProposers);
			responseHandler.setMessage("get all Proposer data "+UserId);

		} catch (Exception e) { 
			e.printStackTrace();
			responseHandler.setStatus("Registration Failed");
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage(e.getMessage());
		}
		return responseHandler;

	}

//	@PutMapping("/update/{id}")
//	public ResponseHandler<Proposer> updateProposer(@PathVariable Long id,@RequestBody Proposer updateProposer){
//		ResponseHandler<Proposer> responseHandler = new ResponseHandler<>();
//		try {
//			Proposer updatedProposer =  proposerService.updateProposer(id, updateProposer);
//			 responseHandler.setStatus("success");
//	        responseHandler.setData(updatedProposer);
//	        responseHandler.setMessage("Updated Successfull");
//		} catch (Exception e) {
//			e.printStackTrace();
//	        responseHandler.setStatus("Update Failed");
//	        responseHandler.setData(new ArrayList<>());
//	        responseHandler.setMessage(e.getMessage());
//		}
//		
//		return responseHandler;
//	}

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

	@PostMapping("/register_with_dto")
	public ResponseHandler<Proposer> registerUsingDTO(@RequestBody ProposerDto proposerDto) {
		ResponseHandler<Proposer> responseHandler = new ResponseHandler<>();

		try {
			Proposer registeredProposer = proposerService.registerProposer(proposerDto);

			responseHandler.setStatus("success");
			responseHandler.setData(registeredProposer);
			responseHandler.setMessage("Registered");

		} catch (IllegalArgumentException e) {

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

	@PutMapping("/update_with_dto/{id}")
	public ResponseHandler<Proposer> updateProposer(@PathVariable Long id, @RequestBody ProposerDto updateProposer) {
		ResponseHandler<Proposer> responseHandler = new ResponseHandler<>();

		try {
			Proposer updatedProposer = proposerService.updateProposerUsingDto(id, updateProposer);

			responseHandler.setStatus("success");
			responseHandler.setData(updatedProposer);
			responseHandler.setMessage("Proposer updated successfully");

		} catch (IllegalArgumentException e) {

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

	@GetMapping("/get_occupation")
	public ResponseHandler<List<Occupation>> getAllOccupations() {
		ResponseHandler<List<Occupation>> responseHandler = new ResponseHandler<>();
		try {

			responseHandler.setStatus("success");
			responseHandler.setData(Arrays.asList(Occupation.values()));
			return responseHandler;
		} catch (IllegalArgumentException e) {

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

	@GetMapping("/get_gender")
	public ResponseHandler<List<Gender>> getAllGender() {
		ResponseHandler<List<Gender>> responseHandler = new ResponseHandler<>();
		try {

			responseHandler.setStatus("success");
			responseHandler.setData(Arrays.asList(Gender.values()));
			return responseHandler;
		} catch (IllegalArgumentException e) {

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

	@GetMapping("/get_marital_status")
	public ResponseHandler<List<MaritalStatus>> getAllMaritalStatus() {
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

	@GetMapping("/get_nationality")
	public ResponseHandler<List<Nationality>> getAllNationality() {
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

	@GetMapping("/get_title")
	public ResponseHandler<List<Title>> getAllTitle() {
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

	@GetMapping("/get_town")
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

	@GetMapping("/get_area")
	public ResponseHandler<List<Area>> getAllArea() {
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
	public Page<Proposer> getProposerByName(@PathVariable String name, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {
		return proposerRepository.findByFullName(name, PageRequest.of(page, size));
	}

	@PostMapping("/getby_page_and_size")
	public ResponseHandler<List<Proposer>> getAllBysortingAndPagination(@RequestBody ProposerPage proposerPage) {
		ResponseHandler<List<Proposer>> responseHandler = new ResponseHandler<>();
		try {

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
		} catch (IllegalArgumentException e) {

			e.printStackTrace();
			responseHandler.setStatus("Failed");
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			responseHandler.setStatus("error");
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage("An error occurred while fetching details");
		}
		return responseHandler;
	}

	@PostMapping("/get_by_page_and_size_and_filter")
	public ResponseHandler<List<Proposer>> getAllBysortingAndPaginationAndFiltering(
			@RequestBody ProposerPage proposerPage) {
		ResponseHandler<List<Proposer>> responseHandler = new ResponseHandler<>();
		try {
			List<Proposer> proposers = proposerService.getAllProposersByPagingAndSortingAndfiltering(proposerPage,
					responseHandler);
//	        Integer totalRecord  =  proposerService.getTotalRecord();
			Integer total = (int) proposerRepository.count();
			List<Proposer> getProposers = proposerRepository.findByStatus('Y');
			if (proposers.isEmpty()) {
				responseHandler.setStatus("no records found");
				responseHandler.setData(new ArrayList<>());
				responseHandler.setMessage("No proposers found matching the criteria");
			} else {
				responseHandler.setStatus("success");
				responseHandler.setData(proposers);
				responseHandler.setMessage("Proposers fetched successfully");
//	            responseHandler.setTotalRecord(proposerService.getTotalRecord());
//	            responseHandler.setTotalRecord(total);
				responseHandler.setTotalRecord(proposerService.getTotalRecord());
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			responseHandler.setStatus("failed");
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage(e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			responseHandler.setStatus("error");
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage("An error occurred while fetching details");

		}
		return responseHandler;
	}

//	@GetMapping("export_excelfile")
//	public void exportToExcel(HttpServletResponse response) throws IOException {
//		 response.setContentType("application/octet-stream");
//	        String headerKey = "Content-Disposition";
//	        String headerValue = "attachment; filename=praposers.xlsx";
//	        response.setHeader(headerKey, headerValue);
//
//	        List<Proposer> list = proposerRepository.findAll();
//
//	        ExcelExport exporter = new ExcelExport(list);
//	        exporter.export(response);
//	}

	@GetMapping("/export_excel_file")
	public void exportToExcel(HttpServletResponse response) throws Exception {
		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=proposers.xlsx";
		response.setHeader(headerKey, headerValue);
		proposerService.generateExcel(response);
		response.flushBuffer();

	}

	@GetMapping("/export_excel_file_using_scheduler")
	public void exportToExcel2() throws Exception {

		proposerService.generateExcel2();

	}

	@GetMapping("/generate_sample_excel")
	public ResponseHandler generateSampleExcel() throws IOException {

		ResponseHandler responseHandler = new ResponseHandler<>();
		try {
			String downloadLink = proposerService.generateSampleExcel();
			responseHandler.setStatus("downloaded");
			responseHandler.setData(downloadLink);
			responseHandler.setMessage("download successfully");
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
			responseHandler.setStatus("error");
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage("An error occurred while downloading");
		}

		return responseHandler;
	}

	@GetMapping("/generate_sample_excel_mandatory")
	public ResponseHandler generateSampleExcelMandatory() throws IOException {

		ResponseHandler responseHandler = new ResponseHandler<>();
		try {
			String downloadLink = proposerService.generateSampleExcelMandatory();
			responseHandler.setStatus("downloaded");
			responseHandler.setData(downloadLink);
			responseHandler.setMessage("download successfully");
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
			responseHandler.setStatus("error");
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage("An error occurred while downloading");
		}

		return responseHandler;
	}

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseHandler<String> uploadExcel(@RequestParam("file") MultipartFile file) {
		ResponseHandler<String> responseHandler = new ResponseHandler<>();
		try {
			List<Proposer> savedProposers = proposerService.saveProposersFromExcel(file);
			responseHandler.setStatus("success");
			responseHandler.setData(savedProposers);
			responseHandler.setMessage("Upload completed.");
		} catch (Exception e) {
			e.printStackTrace();
			responseHandler.setStatus("error");
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage("Failed to process Excel file.");
		}
		return responseHandler;
	}

	@PostMapping(value = "/upload_mandatory", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseHandler<String> uploadExcelMandatory(@RequestParam("file") MultipartFile file) {
		ResponseHandler<String> responseHandler = new ResponseHandler<>();
		try {
			List<Proposer> savedProposers = proposerService.saveProposersFromExcelMandatory(file);

			Integer total = proposerService.totalEntry();
			Integer sucess = proposerService.getTotalCountSucess();
			Integer falseEntry = proposerService.totalFalseEntry();
			responseHandler.setStatus("success");
			responseHandler.setData(savedProposers);
			responseHandler.setMessage(
					"Upload completed " + "total-> " + total + " " + "sucess->  " + sucess + " failed-> " + falseEntry);

			responseHandler.setTotalRecord(total);
		} catch (Exception e) {
			e.printStackTrace();
			responseHandler.setStatus("error");
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage("Failed to process Excel file.");
		}
		return responseHandler;
	}

	@PostMapping(value = "/upload_using_dto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseHandler uploadExcelUsingDto(@RequestParam("file") MultipartFile file) {
		ResponseHandler<String> responseHandler = new ResponseHandler<>();
		try {
			List<Proposer> savedProposers = proposerService.saveProposersFromExcelUsingDto(file);
			responseHandler.setStatus("success");
			responseHandler.setData(savedProposers);
			responseHandler.setMessage("Upload completed.");
		} catch (Exception e) {
			e.printStackTrace();
			responseHandler.setStatus("error");
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage("Failed to process Excel file.");
		}
		return responseHandler;
	}
//	"/upload_mandatory_using_map"
	@PostMapping(value = "/upload_excel_map", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseHandler<List<Proposer>> uploadExcelMandatory2(@RequestParam("file") MultipartFile file) {
		ResponseHandler<List<Proposer>> responseHandler = new ResponseHandler<>();

		try {
			Map<String, Object> resultMap = proposerService.saveProposersFromExcelMandatory2(file);

			Integer total = (Integer) resultMap.get("totalCount");
			Integer success = (Integer) resultMap.get("successCount");
			Integer failed = (Integer) resultMap.get("failedCount");

			List<Proposer> savedProposers = (List<Proposer>) resultMap.get("addedProposers");

			responseHandler.setStatus("success");
			responseHandler.setData(savedProposers);
			responseHandler.setMessage(
					"Upload completed | total: " + total + " | success: " + success + " | failed: " + failed);
			responseHandler.setTotalRecord(total);

		} catch (Exception e) {
			e.printStackTrace();
			responseHandler.setStatus("error");
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage("Failed to process Excel file.");
		}

		return responseHandler;
	}
//	"/get_by_page_and_size_and_filter_using_map"
	@PostMapping("/listing")
	public ResponseHandler<List<Map<String, Object>>> getAllBysortingAndPaginationAndFilteringUsingMap(
			@RequestBody ProposerPage proposerPage) {
		ResponseHandler<List<Map<String, Object>>> responseHandler = new ResponseHandler<>();
		try {
			List<Map<String, Object>> proposers = proposerService
					.getAllProposersByPagingAndSortingAndfilteringUsingMap(proposerPage, responseHandler);

			if (proposers.isEmpty()) {
				responseHandler.setStatus("no records found");
				responseHandler.setData(new ArrayList<>());
				responseHandler.setMessage("No proposers found matching the criteria");
			} else {
				responseHandler.setStatus("success");
				responseHandler.setData(proposers);
				responseHandler.setMessage("Proposers fetched successfully");
				responseHandler.setTotalRecord(proposerService.getTotalRecord());
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			responseHandler.setStatus("failed");
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			responseHandler.setStatus("error");
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage("An error occurred while fetching details");
		}
		return responseHandler;
	}

	@GetMapping("/products")
	public List<Map<String, Object>> getProducts() {
		return proposerService.getAllProducts();
	}

	@GetMapping("/product")
	public List<Map<String, Object>> getFilteredProducts(@RequestParam(required = false) String category,
			@RequestParam(required = false) Double minPrice, @RequestParam(required = false) Double maxPrice,
			@RequestParam(required = false) String sortBy, @RequestParam(required = false) Boolean groupByCategory,
			@RequestParam(required = false) Integer topN) {
		return proposerService.getFilteredProducts(category, minPrice, maxPrice, sortBy, groupByCategory, topN);
	}

//    @GetMapping("/filtered-users")
//    public ResponseEntity<List<Map<String, Object>>> getFilteredUsers() {
//        return ResponseEntity.ok(userService.getSelectedUserInfo());
//    }
	@GetMapping("/filtered_users")
	public ResponseEntity<ResponseHandler<List<Map<String, Object>>>> getFilteredUsers() {
		ResponseHandler<List<Map<String, Object>>> response = new ResponseHandler<>();

		try {
			List<Map<String, Object>> data = proposerService.getSelectedUserInfo();
			response.setStatus("success");
			response.setMessage("User data fetched successfully");
			response.setData(data);
			response.setTotalRecord(data.size());

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.setStatus("error");
			response.setMessage("Failed to fetch data");
			response.setData(new ArrayList<>());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

//	@GetMapping("/filter_using_web_client")
//	public List<Map<String, Object>> filterProductsUsingWebClient(@RequestParam(required = false) String category,
//			@RequestParam(required = false) Double minPrice, @RequestParam(required = false) Double maxPrice,
//			@RequestParam(required = false) String sortBy, @RequestParam(required = false) Boolean groupByCategory,
//			@RequestParam(required = false) Integer topN) {
//		return proposerService.getFilteredProductsUsingWebClient(category, minPrice, maxPrice, sortBy, groupByCategory,
//				topN);
//	}

	@GetMapping("/product_web_client")
	public ResponseHandler<List<Product>> getAllProducts() {
		ResponseHandler<List<Product>> response = new ResponseHandler<>();
		try {
			List<Product> data = proposerService.getAllProduct();
			response.setStatus("success");
			response.setMessage("User data fetched successfully");
			response.setData(data);
			response.setTotalRecord(data.size());

			return response;
		} catch (Exception e) {
			response.setStatus("error");
			response.setMessage("Failed to fetch data");
			response.setData(new ArrayList<>());
			return response;
		}
	}

	@GetMapping("/{id}")
	public ResponseHandler<Product> getProduct(@PathVariable int id) {
		ResponseHandler<Product> response = new ResponseHandler<>();
		try {
			Product data = proposerService.getProductById(id);

			response.setStatus("success");
			response.setMessage("User data fetched successfully");
			response.setData(data);
			response.setTotalRecord(1);

			return response;
		} catch (Exception e) {
			response.setStatus("error");
			response.setMessage("Failed to fetch data");
			response.setData(new ArrayList<>());
			return response;
		}
	}
//	upload_mandatory_using_scheduler
	@PostMapping(value = "/upload_excel_scheduler", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseHandler<String> uploadExcelMandatoryUsingScheduler(@RequestParam("file") MultipartFile file) {
		ResponseHandler<String> responseHandler = new ResponseHandler<>();
		try {
			Map<String, Object> resultMap = proposerService.saveProposersFromExcelMandatoryUsingScheduler(file);
			if (resultMap.containsKey("queueId")) {
				responseHandler.setStatus("queued");
				responseHandler.setData(resultMap);
				responseHandler.setMessage((String) resultMap.get("message"));
				responseHandler.setTotalRecord((Integer) resultMap.get("rowCount"));
				return responseHandler;
			}
			Integer total = (Integer) resultMap.get("totalCount");
			Integer success = (Integer) resultMap.get("successCount");
			Integer failed = (Integer) resultMap.get("failedCount");

			List<Proposer> savedProposers = (List<Proposer>) resultMap.get("addedProposers");

			responseHandler.setStatus("success");
			responseHandler.setData(savedProposers);
			responseHandler.setMessage(
					"Upload completed | total: " + total + " | success: " + success + " | failed: " + failed);
			responseHandler.setTotalRecord(total);

		} catch (Exception e) {
			e.printStackTrace();
			responseHandler.setStatus("error");
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage("Failed to process Excel file.");
		}

		return responseHandler;
	}

}
