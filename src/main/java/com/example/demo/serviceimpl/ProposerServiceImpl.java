package com.example.demo.serviceimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.springframework.http.HttpHeaders;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.persistence.criteria.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.ProposerDto;
import com.example.demo.entity.GenderType;
import com.example.demo.entity.Product;
import com.example.demo.entity.Proposer;
import com.example.demo.entity.QueueTable;
import com.example.demo.entity.ResponceExcel;
import com.example.demo.enums.Area;
import com.example.demo.enums.Gender;
import com.example.demo.enums.Title;
import com.example.demo.enums.Town;
import com.example.demo.exception.ProposerDeletedAlready;
import com.example.demo.pagination.ProposerPage;
import com.example.demo.pagination.SearchFilter;
import com.example.demo.repository.*;
import com.example.demo.response.ProposerResponse;
import com.example.demo.response.ResponseHandler;
import com.example.demo.service.ProposerService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;


@Service
public class ProposerServiceImpl implements ProposerService {
	@Autowired
    private  UserRepository userRepository;

//	private final WebClient.Builder webClientBuilder;
	@Autowired
	private ProposerRepository proposerRepository;

	@Autowired
	private GenderRepository genderRepository;

	@Autowired
	private ResponceExcelRepository responceExcelRepository;
	@Autowired
	private QueueRepository queueRepository;
	Integer totalRecord = 0;
	Integer count;
	Integer totalEntry = 0;
	Integer falseCount;


   

	

//	    private final String PRODUCT_API_URL_ = "https://fakestoreapi.com/products";
//

	@Override
	public List<Proposer> getAllProposer() {
		List<Proposer> proposers = proposerRepository.findByStatus('Y');
		return proposers;
	}

	@Override
	public Proposer deleteProposer(Long id) {

		Optional<Proposer> deletedProposer = proposerRepository.findById(id);

		if (!deletedProposer.isPresent()) {
			throw new IllegalArgumentException("Id not found");
		}

		Proposer updatedeletedStatus = deletedProposer.get();
		if (updatedeletedStatus.getStatus() == 'N') {
			throw new ProposerDeletedAlready("Proposer is already deleted");
		}
		updatedeletedStatus.setStatus('N');
		Proposer changeProposer = proposerRepository.save(updatedeletedStatus);
		return changeProposer;
	}
	@Override
	public ProposerResponse getProposerById(Long id) {
		
		Optional<Proposer> getProposer = proposerRepository.findByIdAndStatus(id, 'Y');

		if (!getProposer.isPresent()) {
			throw new IllegalArgumentException("Proposer not found with id " + id);
		}

		Proposer proposer = getProposer.get();
		
		ProposerResponse response = new ProposerResponse();
		response.setId(proposer.getId());
		response.setTitle(proposer.getTitle());
		response.setFullName(proposer.getFullName());
		response.setGender(proposer.getGender());
		response.setDateOfBirth(proposer.getDateOfBirth());
		response.setAnnualIncome(proposer.getAnnualIncome());
		response.setPanNumber(proposer.getPanNumber());
		response.setAadharNumber(proposer.getAadharNumber());
		response.setMaritalStatus(proposer.getMaritalStatus());
		response.setGenderId(proposer.getGenderId());
		response.setEmail(proposer.getEmail());
		response.setMobileNumber(proposer.getMobileNumber());
		response.setAlternateMobileNumber(proposer.getAlternateMobileNumber());
		response.setAddressLine1(proposer.getAddressLine1());
		response.setAddressLine2(proposer.getAddressLine2());
		response.setAddressLine3(proposer.getAddressLine3());
		response.setPincode(proposer.getPincode());
		response.setArea(proposer.getArea());
		response.setTown(proposer.getTown());
		response.setCity(proposer.getCity());
		response.setState(proposer.getState());
		response.setStatus(proposer.getStatus());
		response.setCreateAt(proposer.getCreateAt());
		response.setUpdatedAt(proposer.getUpdatedAt());
		

		
		return response;
		
	}


	@Override
	public Proposer registerProposer(ProposerDto proposerDto) {
		// TODO Auto-generated method stub
		if (proposerDto.getFullName() == null || proposerDto.getFullName().trim().isEmpty()) {
			throw new IllegalArgumentException("Enter the Full Name");
		}

		if (proposerDto.getEmail() == null
				|| proposerDto.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$\r\n" + "")) {
			throw new IllegalArgumentException("Enter a valid Email");
		}

		if (proposerDto.getMobileNumber() == null || proposerDto.getMobileNumber().length() != 10
				|| !proposerDto.getMobileNumber().matches("^[6-9]\\d{9}$")) {
			throw new IllegalArgumentException("Enter valid mobile no.");
		}
		boolean pan = proposerRepository.existsByPanNumber(proposerDto.getPanNumber());
		if (proposerDto.getPanNumber() == null || proposerDto.getPanNumber().length() != 10
				|| !proposerDto.getPanNumber().matches("^[A-Z]{5}[0-9]{4}[A-Z]{1}$") || pan) {
			throw new IllegalArgumentException("Enter a valid PAN number");
		}
		boolean aadhar = proposerRepository.existsByAadharNumber(proposerDto.getAadharNumber());
		if (proposerDto.getAadharNumber() == null || proposerDto.getAadharNumber().length() != 12
				|| !proposerDto.getAadharNumber().matches("^[2-9]{1}[0-9]{11}$") || aadhar) {
			throw new IllegalArgumentException("Enter a valid Aadhar Number");
		}

		if (proposerDto.getAlternateMobileNumber() == null || proposerDto.getAlternateMobileNumber().length() != 10
				|| !proposerDto.getAlternateMobileNumber().matches("^[6-9]\\d{9}$")) {
			throw new IllegalArgumentException("Enter a valid Alternate Mobile Number");
		}

		if (proposerDto.getPincode() == null || proposerDto.getPincode().length() != 6
				|| !proposerDto.getPincode().matches("\\d+")) {
			throw new IllegalArgumentException("Enter a valid Pincode");
		}

		if (proposerDto.getAnnualIncome() == null || proposerDto.getAnnualIncome().isEmpty()
				|| !proposerDto.getAnnualIncome().matches("\\d+")) {
			throw new IllegalArgumentException("Enter a valid Annual Income");
		}

		if (proposerDto.getAddressLine1() == null || proposerDto.getAddressLine1().trim().isEmpty()) {
			throw new IllegalArgumentException("Enter Address Line 1");
		}

		if (proposerDto.getAddressLine2() == null || proposerDto.getAddressLine2().trim().isEmpty()) {
			throw new IllegalArgumentException("Enter Address Line 2");
		}

		if (proposerDto.getAddressLine3() == null || proposerDto.getAddressLine3().trim().isEmpty()) {
			throw new IllegalArgumentException("Enter Address Line 3");
		}

		Proposer proposer = new Proposer();
		proposer.setStatus('Y');
		proposer.setTitle(proposerDto.getTitle());
		proposer.setFullName(proposerDto.getFullName());

		String gender = proposerDto.getGender();
		if (gender != null && !gender.isEmpty()) {
			if (gender.equalsIgnoreCase(Gender.MALE.toString())) {
				proposer.setGender(Gender.MALE);
			} else if (gender.equalsIgnoreCase(Gender.FEMALE.toString())) {
				proposer.setGender(Gender.FEMALE);
			} else if (gender.equalsIgnoreCase(Gender.OTHER.toString())) {
				proposer.setGender(Gender.OTHER);
			} else {
				throw new IllegalArgumentException("enter corrrect gender");
			}
		} else {
			throw new IllegalArgumentException("gender can not be null");
		}

		if (gender != null && !gender.isEmpty()) {
			Optional<GenderType> genderType = genderRepository.findByType(gender);
			if (genderType.isPresent()) {
				proposer.setGenderId(genderType.get().getGenderId());
			} else {
				throw new IllegalArgumentException("enter corrrect gender");
			}
		} else {
			throw new IllegalArgumentException("enter can not be null");
		}

		proposer.setDateOfBirth(proposerDto.getDateOfBirth());
		proposer.setAnnualIncome(proposerDto.getAnnualIncome());
		proposer.setPanNumber(proposerDto.getPanNumber());
		proposer.setAadharNumber(proposerDto.getAadharNumber());
		proposer.setMaritalStatus(proposerDto.getMaritalStatus());
		proposer.setEmail(proposerDto.getEmail());
		proposer.setMobileNumber(proposerDto.getMobileNumber());
		proposer.setAlternateMobileNumber(proposerDto.getAlternateMobileNumber());
		proposer.setAddressLine1(proposerDto.getAddressLine1());
		proposer.setAddressLine2(proposerDto.getAddressLine2());
		proposer.setAddressLine3(proposerDto.getAddressLine3());
		proposer.setPincode(proposerDto.getPincode());
		proposer.setArea(proposerDto.getArea());
		proposer.setTown(proposerDto.getTown());
		proposer.setCity(proposerDto.getCity());
		proposer.setState(proposerDto.getState());
		proposerRepository.save(proposer);

		return proposer;
	}


	@Override
	public Proposer updateProposerUsingDto(Long id, ProposerDto proposerDto) {
		Optional<Proposer> getProposer = proposerRepository.findByIdAndStatus(id, 'Y');

		if (!getProposer.isPresent()) {
			throw new IllegalArgumentException("Proposer not found with id " + id);
		}

		Proposer proposer = getProposer.get();

		if (proposerDto.getFullName() != null && !proposerDto.getFullName().trim().isEmpty()) {
			proposer.setFullName(proposerDto.getFullName());
		} else if (proposerDto.getFullName() == null || proposerDto.getFullName().trim().isEmpty()) {
			throw new IllegalArgumentException("Enter the Full Name");
		}

		if (proposerDto.getEmail() != null) {
			if (!proposerDto.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
				throw new IllegalArgumentException("Enter a valid Email");
			}
			proposer.setEmail(proposerDto.getEmail());
		}

		if (proposerDto.getMobileNumber() != null) {
			if (proposerDto.getMobileNumber().length() != 10 || !proposerDto.getMobileNumber().matches("^[6-9]\\d{9}$")) {
				throw new IllegalArgumentException("Enter valid mobile no.");
			}
			proposer.setMobileNumber(proposerDto.getMobileNumber());
		}

		if (proposerDto.getPanNumber() != null) {

			if (proposerDto.getPanNumber().length() != 10
					|| !proposerDto.getPanNumber().matches("^[A-Z]{5}[0-9]{4}[A-Z]{1}$")) {
				throw new IllegalArgumentException("Enter a valid PAN number");
			}
			proposer.setPanNumber(proposerDto.getPanNumber());
		}

		if (proposerDto.getAadharNumber() != null) {

			if (proposerDto.getAadharNumber().length() != 12 || !proposerDto.getAadharNumber().matches("^[2-9]{1}[0-9]{11}$")) {
				throw new IllegalArgumentException("Enter a valid Aadhar Number");
			}
			proposer.setAadharNumber(proposerDto.getAadharNumber());
		}

		if (proposerDto.getAlternateMobileNumber() != null) {
			if (proposerDto.getAlternateMobileNumber().length() != 10
					|| !proposerDto.getAlternateMobileNumber().matches("^[6-9]\\d{9}$")) {
				throw new IllegalArgumentException("Enter a valid Alternate Mobile Number");
			}
			proposer.setAlternateMobileNumber(proposerDto.getAlternateMobileNumber());
		}

		if (proposerDto.getPincode() != null) {
			if (proposerDto.getPincode().length() != 6 || !proposerDto.getPincode().matches("\\d+")) {
				throw new IllegalArgumentException("Enter a valid Pincode");
			}
			proposer.setPincode(proposerDto.getPincode());
		}

		if (proposerDto.getAnnualIncome() != null && !proposerDto.getAnnualIncome().isEmpty()
				&& proposerDto.getAnnualIncome().matches("\\d+")) {
			proposer.setAnnualIncome(proposerDto.getAnnualIncome());
		} else if (proposerDto.getAnnualIncome() != null) {
			throw new IllegalArgumentException("Enter a valid Annual Income");
		}

		if (proposerDto.getAddressLine1() != null && !proposerDto.getAddressLine1().trim().isEmpty()) {
			proposer.setAddressLine1(proposerDto.getAddressLine1());
		} else if (proposerDto.getAddressLine1() == null || proposerDto.getAddressLine1().trim().isEmpty()) {
			throw new IllegalArgumentException("Enter Address Line 1");
		}

		if (proposerDto.getAddressLine2() != null && !proposerDto.getAddressLine2().trim().isEmpty()) {
			proposer.setAddressLine2(proposerDto.getAddressLine2());
		} else if (proposerDto.getAddressLine2() == null || proposerDto.getAddressLine2().trim().isEmpty()) {
			throw new IllegalArgumentException("Enter Address Line 2");
		}

		if (proposerDto.getAddressLine3() != null && !proposerDto.getAddressLine3().trim().isEmpty()) {
			proposer.setAddressLine3(proposerDto.getAddressLine3());
		} else if (proposerDto.getAddressLine3() == null || proposerDto.getAddressLine3().trim().isEmpty()) {
			throw new IllegalArgumentException("Enter Address Line 3");
		}

		if (proposerDto.getTitle() != null)
			proposer.setTitle(proposerDto.getTitle());

		String gender = proposerDto.getGender();
		if (gender != null && !gender.isEmpty()) {
			if (gender.equalsIgnoreCase(Gender.MALE.toString())) {
				proposer.setGender(Gender.MALE);
			} else if (gender.equalsIgnoreCase(Gender.FEMALE.toString())) {
				proposer.setGender(Gender.FEMALE);
			} else if (gender.equalsIgnoreCase(Gender.OTHER.toString())) {
				proposer.setGender(Gender.OTHER);
			} else {
				throw new IllegalArgumentException("enter corrrect gender");
			}
		} else {
			throw new IllegalArgumentException("gender can not be null");
		}

		if (proposerDto.getDateOfBirth() != null)
			proposer.setDateOfBirth(proposerDto.getDateOfBirth());
		if (proposerDto.getMaritalStatus() != null)
			proposer.setMaritalStatus(proposerDto.getMaritalStatus());
		if (proposerDto.getArea() != null)
			proposer.setArea(proposerDto.getArea());
		if (proposerDto.getTown() != null)
			proposer.setTown(proposerDto.getTown());
		if (proposerDto.getCity() != null)
			proposer.setCity(proposerDto.getCity());
		if (proposerDto.getState() != null)
			proposer.setState(proposerDto.getState());

		return proposerRepository.save(proposer);
	}

	@Autowired
	private EntityManager entityManager;


	// working
	@Override
	public List<Proposer> getAllProposersByPagingAndSorting(ProposerPage proposerPage) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Proposer> criteriaQuery = criteriaBuilder.createQuery(Proposer.class);
		Root<Proposer> root = criteriaQuery.from(Proposer.class);
		if (proposerPage.getPageNumber() >= 0 && proposerPage.getPageSize() >= 0) {
			if (proposerPage.getSortBy() == null || proposerPage.getSortOrder().isEmpty()) {
				proposerPage.setSortBy("id");
				proposerPage.setSortOrder("DESC");
			}

		} else {
			throw new IllegalArgumentException("Error occured");
		}
		if (proposerPage.getSortBy() != null && proposerPage.getSortOrder() != null) {
			String sortBy = proposerPage.getSortBy();
			if ("ASC".equalsIgnoreCase(proposerPage.getSortOrder())) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get(sortBy)));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get(sortBy)));
			}
		}

		if (proposerPage.getPageNumber() <= 0 && proposerPage.getPageSize() <= 0) {
			return entityManager.createQuery(criteriaQuery).getResultList();
		} else {

			Integer size = proposerPage.getPageSize();
			Integer page = proposerPage.getPageNumber();

			TypedQuery<Proposer> typedQuery = entityManager.createQuery(criteriaQuery);
			typedQuery.setFirstResult((page - 1) * size);
			typedQuery.setMaxResults(size);
			return typedQuery.getResultList();
		}
	}

	@Override
	public List<Proposer> getAllProposersByPagingAndSortingAndfiltering(ProposerPage proposerPage,
			ResponseHandler<List<Proposer>> responseHandler) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Proposer> criteriaQuery = criteriaBuilder.createQuery(Proposer.class);
		Root<Proposer> root = criteriaQuery.from(Proposer.class);

		List<Predicate> predicates = new ArrayList<>();
		List<SearchFilter> searchFilters = proposerPage.getSearchFilters();

	    predicates.add(criteriaBuilder.equal(root.get("status"), 'Y'));
		List<SearchFilter> searchFilter = proposerPage.getSearchFilters();

		if (searchFilters != null) {
			for (SearchFilter filter : searchFilters) {
				if (filter.getFullName() != null && !filter.getFullName().trim().isEmpty()) {
					predicates.add(criteriaBuilder.like(root.get("fullName"), "%" + filter.getFullName().trim() + "%"));
				}
				if (filter.getEmail() != null && !filter.getEmail().trim().isEmpty()) {
					predicates.add(criteriaBuilder.like(root.get("email"), "%" + filter.getEmail().trim() + "%"));
				}
				if (filter.getCity() != null && !filter.getCity().trim().isEmpty()) {
					predicates.add(criteriaBuilder.like(root.get("city"), "%" + filter.getCity().trim() + "%"));
				}
				if (filter.getStatus() != null && filter.getStatus() == 'Y') {
					predicates.add(criteriaBuilder.equal(root.get("status"), 'Y'));
				}
				if (filter.getStatus() != null && filter.getStatus() == 'N') {
					predicates.add(criteriaBuilder.equal(root.get("status"), 'N'));
				}
			}
		}

		if (!predicates.isEmpty()) {
			criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
		}

		String sortBy = proposerPage.getSortBy();
		if (sortBy == null || sortBy.trim().isEmpty()) {
			sortBy = "id";
		}

		String sortOrder = proposerPage.getSortOrder();
		if (sortOrder == null || sortOrder.trim().isEmpty() || sortOrder.equalsIgnoreCase("desc")) {
			criteriaQuery.orderBy(criteriaBuilder.desc(root.get(sortBy)));
		} else {
			criteriaQuery.orderBy(criteriaBuilder.asc(root.get(sortBy)));
		}

		TypedQuery<Proposer> typedQuery = entityManager.createQuery(criteriaQuery);

		List<Proposer> resultList = typedQuery.getResultList();
		totalRecord = resultList.size();
		System.err.println("totalRecord<<<<" + totalRecord);

		if (proposerPage.getPageNumber() > 0 && proposerPage.getPageSize() > 0) {
			int firstResult = (proposerPage.getPageNumber() - 1) * proposerPage.getPageSize();
			typedQuery.setFirstResult(firstResult);
			typedQuery.setMaxResults(proposerPage.getPageSize());
		}

		return typedQuery.getResultList();
	}

	@Override
	public Integer getTotalRecord() {
		// TODO Auto-generated method stub
		return totalRecord;
	}

	@Override

	public void generateExcel(HttpServletResponse httpServletResponse) throws Exception {
		// TODO Auto-generated method stub
		List<Proposer> praposers = proposerRepository.findAll();
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Proposer");
		List<String> headers = Arrays.asList("ID", "Title", "Full Name", "Gender", "Date of Birth", "Annual Income",
				"PAN Number", "Aadhar Number", "Marital Status", "Email", "Mobile Number", "Address Line 1", "Area",
				"Town", "City", "State");

		Row headerRow = sheet.createRow(0);
		for (int i = 0; i < headers.size(); i++) {
			headerRow.createCell(i).setCellValue(headers.get(i));
		}

		int rowCount = 1;

		for (Proposer p : praposers) {
			Row rowNew = sheet.createRow(rowCount++);

			rowNew.createCell(0).setCellValue(p.getId());
			rowNew.createCell(1).setCellValue(p.getTitle() != null ? p.getTitle().toString() : "");
			rowNew.createCell(2).setCellValue(p.getFullName());
			rowNew.createCell(3).setCellValue(p.getGenderId() != null ? p.getGender().toString() : "");

			rowNew.createCell(4).setCellValue(p.getDateOfBirth());
			rowNew.createCell(5).setCellValue(p.getAnnualIncome());
			rowNew.createCell(6).setCellValue(p.getPanNumber());

			rowNew.createCell(7).setCellValue(p.getAadharNumber());
			rowNew.createCell(8).setCellValue(p.getMaritalStatus());

			rowNew.createCell(9).setCellValue(p.getEmail());
			rowNew.createCell(10).setCellValue(p.getMobileNumber());
			rowNew.createCell(11).setCellValue(p.getAddressLine1());

			rowNew.createCell(12).setCellValue(p.getArea() != null ? p.getArea().toString() : "");
			rowNew.createCell(13).setCellValue(p.getTown() != null ? p.getTown().toString() : "");
			rowNew.createCell(14).setCellValue(p.getCity());
			rowNew.createCell(15).setCellValue(p.getState());

		}
		String filePathString = "C:\\subodh\\";
		String uuid = UUID.randomUUID().toString();
		String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
		String fileName = "sample_" + uuid + "_" + currentDateTime + ".xlsx";

		String fullFilePath = filePathString + fileName;
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(fullFilePath);
			workbook.write(fileOutputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("generated");

		workbook.close();
	}

//	@Scheduled(fixedRate = 5000)
	public void generateExcel2() throws Exception {
		// TODO Auto-generated method stub
		List<Proposer> praposers = proposerRepository.findAll();
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Proposer");
		List<String> headers = Arrays.asList("ID", "Title", "Full Name", "Gender", "Date of Birth", "Annual Income",
				"PAN Number", "Aadhar Number", "Marital Status", "Email", "Mobile Number", "Address Line 1", "Area",
				"Town", "City", "State");

		Row headerRow = sheet.createRow(0);
		for (int i = 0; i < headers.size(); i++) {
			headerRow.createCell(i).setCellValue(headers.get(i));
		}

		int rowCount = 1;

		for (Proposer p : praposers) {
			Row rowNew = sheet.createRow(rowCount++);

			rowNew.createCell(0).setCellValue(p.getId());
			rowNew.createCell(1).setCellValue(p.getTitle() != null ? p.getTitle().toString() : "");
			rowNew.createCell(2).setCellValue(p.getFullName());
			rowNew.createCell(3).setCellValue(p.getGenderId() != null ? p.getGender().toString() : "");

			rowNew.createCell(4).setCellValue(p.getDateOfBirth());
			rowNew.createCell(5).setCellValue(p.getAnnualIncome());
			rowNew.createCell(6).setCellValue(p.getPanNumber());

			rowNew.createCell(7).setCellValue(p.getAadharNumber());
			rowNew.createCell(8).setCellValue(p.getMaritalStatus());

			rowNew.createCell(9).setCellValue(p.getEmail());
			rowNew.createCell(10).setCellValue(p.getMobileNumber());
			rowNew.createCell(11).setCellValue(p.getAddressLine1());

			rowNew.createCell(12).setCellValue(p.getArea() != null ? p.getArea().toString() : "");
			rowNew.createCell(13).setCellValue(p.getTown() != null ? p.getTown().toString() : "");
			rowNew.createCell(14).setCellValue(p.getCity());
			rowNew.createCell(15).setCellValue(p.getState());

		}
		String filePathString = "C:\\subodh\\";
		String uuid = UUID.randomUUID().toString();
		String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
		String fileName = "sample_" + uuid + "_" + currentDateTime + ".xlsx";

		String fullFilePath = filePathString + fileName;
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(fullFilePath);
			workbook.write(fileOutputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("generated");

		workbook.close();

	}

	public String generateSampleExcel() throws IOException {
//		String filePathString = "C:/subodh/";
		String filePathString = "C:\\subodh\\";

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Proposer");

		List<String> headers = Arrays.asList("ID", "Title", "Full Name", "Gender", "Date of Birth", "Annual Income",
				"PAN Number", "Aadhar Number", "Marital Status", "Gender ID", "Email", "Mobile Number",
				"Alternate Mobile Number", "Address Line 1", "Address Line 2", "Address Line 3", "Pincode", "Area",
				"Town", "City", "State", "Status", "Created At", "Updated At");

		Row headerRow = sheet.createRow(0);
		for (int i = 0; i < headers.size(); i++) {
			headerRow.createCell(i).setCellValue(headers.get(i));
		}
		String uuid = UUID.randomUUID().toString();
		String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
		String fileName = "sample_" + uuid + "_" + currentDateTime + ".xlsx";

		String fullFilePath = filePathString + fileName;

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(fullFilePath);
			workbook.write(fileOutputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}

		workbook.close();
		return fullFilePath;
	}

	private String getCellValueAsString(Cell cell) {
		if (cell == null)
			return "";

		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue().trim();
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				return simpleDateFormat.format(cell.getDateCellValue());
			} else {
				return String.valueOf((long) cell.getNumericCellValue());
			}
		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		case FORMULA:
			return cell.getCellFormula();
		case BLANK:
			return "";
		default:
			return "";
		}
	}

	private String check(Row row, int index) {
		Cell cell = row.getCell(index);
		if (cell == null)
			return "";
		return getCellValueAsString(cell).trim();
	}

	public List<Proposer> saveProposersFromExcel(MultipartFile file) throws IOException {
		List<Proposer> excelList = new ArrayList<>();
		try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
			XSSFSheet sheet = workbook.getSheetAt(0);

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				ResponceExcel responceExcel = new ResponceExcel();

				Row row = sheet.getRow(i);
				if (row == null)
					continue;
				String title = check(row, 0);
				String fullName = check(row, 1);
				String genderString = check(row, 2);
				String dob = check(row, 3);
				String income = check(row, 4);
				String pan = check(row, 5);
				String aadhar = check(row, 6);
				String maritalStatus = check(row, 7);
				String email = check(row, 8);
				String mobile = check(row, 9);
				String altMobile = check(row, 10);
				String address1 = check(row, 11);
				String address2 = check(row, 12);
				String address3 = check(row, 13);
				String pincode = check(row, 14);
				String area = check(row, 15);
				String town = check(row, 16);
				String city = check(row, 17);
				String state = check(row, 18);


				Proposer proposer = new Proposer();
				proposer.setTitle(Title.valueOf(getCellValueAsString(row.getCell(0)).toUpperCase()));
				if (fullName == null || fullName.isEmpty()) {

					responceExcel.setStatus("failed");
					responceExcel.setErrorField("full Name");
					responceExcel.setReason("error in full Name");
					responceExcelRepository.save(responceExcel);
					continue;

				} else {
					proposer.setFullName(getCellValueAsString(row.getCell(1)));

				}
				;
				if (genderString.isEmpty() || genderString == null) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("gender");
					responceExcel.setReason("error in gender");
					responceExcelRepository.save(responceExcel);
					continue;

				} else {
					proposer.setGender(Gender.valueOf(getCellValueAsString(row.getCell(2)).toUpperCase()));
				}

				if (dob.isEmpty() || dob == null) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("dob");
					responceExcel.setReason("error in dob");
					responceExcelRepository.save(responceExcel);
					continue;

				} else {
					proposer.setDateOfBirth(getCellValueAsString(row.getCell(3)));
				}

				if (pan.length() != 10 || !pan.matches("^[A-Z]{5}[0-9]{4}[A-Z]{1}$") || pan == null || pan.isEmpty()) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("pancard");
					responceExcel.setReason("error in pancard");
					responceExcelRepository.save(responceExcel);
					continue;

				} else {
					proposer.setPanNumber(getCellValueAsString(row.getCell(5)));
				}

				proposer.setAnnualIncome(getCellValueAsString(row.getCell(4)));
				if (aadhar.length() != 12 || !aadhar.matches("\\d{12}") || aadhar == null || aadhar.isEmpty()) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("aadhar card");
					responceExcel.setReason("error in aadhar card");
					responceExcelRepository.save(responceExcel);
					continue;

				} else {
					proposer.setAadharNumber(getCellValueAsString(row.getCell(6)));
				}

				if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$") || email.isEmpty()
						|| email == null) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("email");
					responceExcel.setReason("error in email");
					responceExcelRepository.save(responceExcel);
					continue;

				} else {
					proposer.setEmail(getCellValueAsString(row.getCell(8)));
				}

				if (!mobile.matches("\\d{10}") || mobile.isEmpty() || mobile == null) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("mobile number");
					responceExcel.setReason("error in mobile number");
					responceExcelRepository.save(responceExcel);
					continue;

				} else {
					proposer.setMobileNumber(getCellValueAsString(row.getCell(9)));
				}

				boolean isValidArea = false;
				for (Area areaEnum : Area.values()) {

					if (areaEnum.name().equalsIgnoreCase(area.trim())) {
						isValidArea = true;
						break;
					}
				}
				if (area.isEmpty() || area == null || isValidArea == false) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("area");
					responceExcel.setReason("error in area");
					responceExcelRepository.save(responceExcel);
					continue;

				} else {
					proposer.setArea(Area.valueOf(getCellValueAsString(row.getCell(15)).toUpperCase()));
				}

				if (pincode.length() != 6 || pincode == null) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("pin code");
					responceExcel.setReason("error in pin code");
					responceExcelRepository.save(responceExcel);
					continue;

				} else {
					proposer.setPincode(getCellValueAsString(row.getCell(14)));
				}
				boolean isValidTown = false;
				for (Town townEnum : Town.values()) {

					if (townEnum.name().equalsIgnoreCase(town.trim())) {
						isValidTown = true;
						break;
					}
				}
				if (isValidTown == false || town.isEmpty() || town == null) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("town");
					responceExcel.setReason("error in town");
					responceExcelRepository.save(responceExcel);
					continue;

				} else {
					proposer.setTown(Town.valueOf(getCellValueAsString(row.getCell(16)).toUpperCase()));
				}

				if (city.isEmpty() || city == null) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("city");
					responceExcel.setReason("error in city");
					responceExcelRepository.save(responceExcel);
					continue;

				} else {
					proposer.setCity(getCellValueAsString(row.getCell(17)));
				}

				if (state.isEmpty() || state == null) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("state");
					responceExcel.setReason("error in state");
					responceExcelRepository.save(responceExcel);
					continue;

				} else {
					proposer.setState(getCellValueAsString(row.getCell(18)));
				}

				proposer.setMaritalStatus(getCellValueAsString(row.getCell(7)));


				proposer.setAlternateMobileNumber(getCellValueAsString(row.getCell(10)));
				proposer.setAddressLine1(getCellValueAsString(row.getCell(11)));
				proposer.setAddressLine2(getCellValueAsString(row.getCell(12)));
				proposer.setAddressLine3(getCellValueAsString(row.getCell(13)));

				proposer.setStatus('Y');
				String gender = proposer.getGender().toString();
				if (gender != null && !gender.isEmpty()) {
					Optional<GenderType> genderType = genderRepository.findByType(gender);
					if (genderType.isPresent()) {
						proposer.setGenderId(genderType.get().getGenderId());
					} else {
						throw new IllegalArgumentException("enter corrrect gender");
					}
				} else {
					throw new IllegalArgumentException("enter can not be null");
				}
				Proposer savedProposer = proposerRepository.save(proposer);
				excelList.add(savedProposer);
				Long id = savedProposer.getId();
				System.out.println(id);
				responceExcel.setStatus("sucess");
				responceExcel.setErrorField(String.valueOf(id));
				responceExcel.setReason("sucessfully added");
				responceExcelRepository.save(responceExcel);
			}
		}
		return excelList;
	}

	// do not use
	@Override
	public List<Proposer> saveProposersFromExcelUsingDto(MultipartFile file) throws IOException {
		List<Proposer> excelList = new ArrayList<>();

		try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
			XSSFSheet sheet = workbook.getSheetAt(0);

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null)
					continue;

				ProposerDto proposerdto = new ProposerDto();
				proposerdto.setTitle(safeEnumValueOf(Title.class, getCellValueAsString(row.getCell(0))));
				proposerdto.setFullName(getCellValueAsString(row.getCell(1)));
				proposerdto.setGender(getCellValueAsString(row.getCell(2)));
				proposerdto.setDateOfBirth(getCellValueAsString(row.getCell(3)));
				proposerdto.setAnnualIncome(getCellValueAsString(row.getCell(4)));
				proposerdto.setPanNumber(getCellValueAsString(row.getCell(5)));
				proposerdto.setAadharNumber(getCellValueAsString(row.getCell(6)));
				proposerdto.setMaritalStatus(getCellValueAsString(row.getCell(7)));
				proposerdto.setEmail(getCellValueAsString(row.getCell(8)));
				proposerdto.setMobileNumber(getCellValueAsString(row.getCell(9)));
				proposerdto.setAlternateMobileNumber(getCellValueAsString(row.getCell(10)));
				proposerdto.setAddressLine1(getCellValueAsString(row.getCell(11)));
				proposerdto.setAddressLine2(getCellValueAsString(row.getCell(12)));
				proposerdto.setAddressLine3(getCellValueAsString(row.getCell(13)));
				proposerdto.setPincode(getCellValueAsString(row.getCell(14)));
				proposerdto.setState(getCellValueAsString(row.getCell(15)));
				proposerdto.setArea(safeEnumValueOf(Area.class, getCellValueAsString(row.getCell(16))));
				proposerdto.setTown(safeEnumValueOf(Town.class, getCellValueAsString(row.getCell(17))));
				proposerdto.setCity(getCellValueAsString(row.getCell(18)));

				Proposer savedProposer = registerProposer(proposerdto);
				excelList.add(savedProposer);
			}
		}

		return excelList;
	}

	public static <T extends Enum<T>> T safeEnumValueOf(Class<T> enumClass, String value) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}
		try {
			return Enum.valueOf(enumClass, value.trim().toUpperCase());
		} catch (IllegalArgumentException e) {
			System.err.println("Invalid enum value '" + value + "' for enum " + enumClass.getSimpleName());
			return null;
		}
	}

	@Override
	public Proposer registerProposerExcel(Proposer proposer) {
		if (proposer.getFullName() == null || proposer.getFullName().trim().isEmpty()) {
			throw new IllegalArgumentException("Enter the Full Name");
		}

		if (proposer.getEmail() == null
				|| proposer.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$\r\n" + "")) {
			throw new IllegalArgumentException("Enter a valid Email");
		}

		if (proposer.getMobileNumber() == null || proposer.getMobileNumber().length() != 10
				|| !proposer.getMobileNumber().matches("\\d+")) {
			throw new IllegalArgumentException("Enter valid mobile no.");
		}
		boolean pan = proposerRepository.existsByPanNumber(proposer.getPanNumber());
		if (proposer.getPanNumber() == null || proposer.getPanNumber().length() != 10
				|| !proposer.getPanNumber().matches("^[A-Z]{5}[0-9]{4}[A-Z]{1}$") || pan) {
			throw new IllegalArgumentException("Enter a valid PAN number");
		}
		boolean aadhar = proposerRepository.existsByAadharNumber(proposer.getAadharNumber());
		if (proposer.getAadharNumber() == null || proposer.getAadharNumber().length() != 12
				|| !proposer.getAadharNumber().matches("\\d+") || aadhar) {
			throw new IllegalArgumentException("Enter a valid Aadhar Number");
		}

		if (proposer.getAlternateMobileNumber() == null || proposer.getAlternateMobileNumber().length() != 10
				|| !proposer.getAlternateMobileNumber().matches("\\d+")) {
			throw new IllegalArgumentException("Enter a valid Alternate Mobile Number");
		}

		if (proposer.getPincode() == null || proposer.getPincode().length() != 6
				|| !proposer.getPincode().matches("\\d+")) {
			throw new IllegalArgumentException("Enter a valid Pincode");
		}

		if (proposer.getAnnualIncome() == null || proposer.getAnnualIncome().isEmpty()
				|| !proposer.getAnnualIncome().matches("\\d+")) {
			throw new IllegalArgumentException("Enter a valid Annual Income");
		}

		if (proposer.getAddressLine1() == null || proposer.getAddressLine1().trim().isEmpty()) {
			throw new IllegalArgumentException("Enter Address Line 1");
		}

		if (proposer.getAddressLine2() == null || proposer.getAddressLine2().trim().isEmpty()) {
			throw new IllegalArgumentException("Enter Address Line 2");
		}

		if (proposer.getAddressLine3() == null || proposer.getAddressLine3().trim().isEmpty()) {
			throw new IllegalArgumentException("Enter Address Line 3");
		}

		Proposer newProposer = new Proposer();
		newProposer.setStatus('Y');
		newProposer.setTitle(proposer.getTitle());
		newProposer.setFullName(proposer.getFullName());

		String gender = proposer.getGender().toString();
		if (gender != null && !gender.isEmpty()) {
			if (gender.equalsIgnoreCase(Gender.MALE.toString())) {
				newProposer.setGender(Gender.MALE);
			} else if (gender.equalsIgnoreCase(Gender.FEMALE.toString())) {
				newProposer.setGender(Gender.FEMALE);
			} else if (gender.equalsIgnoreCase(Gender.OTHER.toString())) {
				newProposer.setGender(Gender.OTHER);
			} else {
				throw new IllegalArgumentException("enter corrrect gender");
			}
		} else {
			throw new IllegalArgumentException("gender can not be null");
		}

		if (gender != null && !gender.isEmpty()) {
			Optional<GenderType> genderType = genderRepository.findByType(gender);
			if (genderType.isPresent()) {
				newProposer.setGenderId(genderType.get().getGenderId());
			} else {
				throw new IllegalArgumentException("enter corrrect gender");
			}
		} else {
			throw new IllegalArgumentException("enter can not be null");
		}

		newProposer.setDateOfBirth(proposer.getDateOfBirth());
		newProposer.setAnnualIncome(proposer.getAnnualIncome());
		newProposer.setPanNumber(proposer.getPanNumber());
		newProposer.setAadharNumber(proposer.getAadharNumber());
		newProposer.setMaritalStatus(proposer.getMaritalStatus());
		newProposer.setEmail(proposer.getEmail());
		newProposer.setMobileNumber(proposer.getMobileNumber());
		newProposer.setAlternateMobileNumber(proposer.getAlternateMobileNumber());
		newProposer.setAddressLine1(proposer.getAddressLine1());
		newProposer.setAddressLine2(proposer.getAddressLine2());
		newProposer.setAddressLine3(proposer.getAddressLine3());
		newProposer.setPincode(proposer.getPincode());
		newProposer.setArea(proposer.getArea());
		newProposer.setTown(proposer.getTown());
		newProposer.setCity(proposer.getCity());
		newProposer.setState(proposer.getState());
		proposerRepository.save(newProposer);

		return newProposer;
	}

	@Override
	public String generateSampleExcelMandatory() throws IOException {

		String filePathString = "C:\\subodh\\";

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Proposer");

		List<String> headers = Arrays.asList("Title", "Full Name*", "Gender*", "Date of Birth*", "Annual Income",
				"PAN Number*", "Aadhar Number*", "Marital Status", "Email*", "Mobile Number*",
				"Alternate Mobile Number", "Address Line 1", "Address Line 2", "Address Line 3", "Pincode*", "Area",
				"Town", "City*", "State*");

		Row headerRow = sheet.createRow(0);
		for (int i = 0; i < headers.size(); i++) {
			headerRow.createCell(i).setCellValue(headers.get(i));
		}
		String uuid = UUID.randomUUID().toString();
		String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
		String fileName = "sample_" + uuid + "_" + currentDateTime + ".xlsx";

		String fullFilePath = filePathString + fileName;

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(fullFilePath);
			workbook.write(fileOutputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}

		workbook.close();
		return fullFilePath;
	}

	@Override
	public List<Proposer> saveProposersFromExcelMandatory(MultipartFile file) throws IOException {
		List<Proposer> excelList = new ArrayList<>();
		try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
			XSSFSheet sheet = workbook.getSheetAt(0);
			count = 0;
			falseCount = 0;
			totalEntry = sheet.getLastRowNum();
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				ResponceExcel responceExcel = new ResponceExcel();

				Row row = sheet.getRow(i);
				if (row == null)
					continue;
				String title = check(row, 0);
				String fullName = check(row, 1);
				String genderString = check(row, 2);
				String dob = check(row, 3);
				String income = check(row, 4);
				String pan = check(row, 5);
				String aadhar = check(row, 6);
				String maritalStatus = check(row, 7);
				String email = check(row, 8);
				String mobile = check(row, 9);
				String altMobile = check(row, 10);
				String address1 = check(row, 11);
				String address2 = check(row, 12);
				String address3 = check(row, 13);
				String pincode = check(row, 14);
				String area = check(row, 15);
				String town = check(row, 16);
				String city = check(row, 17);
				String state = check(row, 18);

				Proposer proposer = new Proposer();
				System.err.println(title);

				boolean isValidTitle = false;
				for (Title titleEnum : Title.values()) {

					if (titleEnum.name().equalsIgnoreCase(title.trim())) {
						isValidTitle = true;
						break;
					}
				}
				if (isValidTitle == true) {
					proposer.setTitle(Title.valueOf(getCellValueAsString(row.getCell(0)).toUpperCase()));

				}
				if (fullName == null || fullName.isEmpty() || !fullName.matches("[A-Za-z\\s]+")) {
//					System.out.println(fullName + "error");
//					System.err.println("errror ocured");
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("full Name");
					responceExcel.setReason("error in full Name");
					responceExcelRepository.save(responceExcel);
					falseCount++;
					continue;

				} else {
					proposer.setFullName(getCellValueAsString(row.getCell(1)));

				}

				if (genderString.isEmpty() || genderString == null) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("gender");
					responceExcel.setReason("error in gender");
					responceExcelRepository.save(responceExcel);
					falseCount++;
					continue;

				} else {
					proposer.setGender(Gender.valueOf(getCellValueAsString(row.getCell(2)).toUpperCase()));
				}

				if (dob.isEmpty() || dob == null) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("dob");
					responceExcel.setReason("error in dob");
					responceExcelRepository.save(responceExcel);
					falseCount++;
					continue;

				} else {
					proposer.setDateOfBirth(getCellValueAsString(row.getCell(3)));
				}

				if (pan.length() != 10 || !pan.matches("^[A-Z]{5}[0-9]{4}[A-Z]{1}$") || pan == null || pan.isEmpty()) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("pancard");
					responceExcel.setReason("error in pancard");
					responceExcelRepository.save(responceExcel);
					falseCount++;
					continue;

				} else {
					proposer.setPanNumber(getCellValueAsString(row.getCell(5)));
				}
				if (!income.matches("\\d+")) {
					proposer.setAnnualIncome(null);
				} else {
					proposer.setAnnualIncome(getCellValueAsString(row.getCell(4)));
				}

				if (aadhar.length() != 12 || !aadhar.matches("\\d{12}") || aadhar == null || aadhar.isEmpty()) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("aadhar card");
					responceExcel.setReason("error in aadhar card");
					responceExcelRepository.save(responceExcel);
					falseCount++;
					continue;

				} else {
					proposer.setAadharNumber(getCellValueAsString(row.getCell(6)));
				}

				if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$") || email.isEmpty()
						|| email == null) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("email");
					responceExcel.setReason("error in email");
					responceExcelRepository.save(responceExcel);
					falseCount++;
					continue;

				} else {
					proposer.setEmail(getCellValueAsString(row.getCell(8)));
				}

				if (!mobile.matches("\\d{10}") || mobile.isEmpty() || mobile == null
						|| !mobile.matches("^[6-9]\\d{9}$")) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("mobile number");
					responceExcel.setReason("error in mobile number");
					responceExcelRepository.save(responceExcel);
					falseCount++;
					continue;

				} else {
					proposer.setMobileNumber(getCellValueAsString(row.getCell(9)));
				}

				boolean isValidArea = false;
				for (Area areaEnum : Area.values()) {

					if (areaEnum.name().equalsIgnoreCase(area.trim())) {
						isValidArea = true;
						break;
					}
				}
				if (area.isEmpty() || area == null || isValidArea == false) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("area");
					responceExcel.setReason("error in area");
					responceExcelRepository.save(responceExcel);
					falseCount++;
					continue;

				} else {
					proposer.setArea(Area.valueOf(getCellValueAsString(row.getCell(15)).toUpperCase()));
				}

				if (pincode.length() != 6 || pincode == null) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("pin code");
					responceExcel.setReason("error in pin code");
					responceExcelRepository.save(responceExcel);
					falseCount++;
					continue;

				} else {
					proposer.setPincode(getCellValueAsString(row.getCell(14)));
				}
				
				if (city.isEmpty() || city == null) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("city");
					responceExcel.setReason("error in city");
					responceExcelRepository.save(responceExcel);
					falseCount++;
					continue;

				} else {
					proposer.setCity(getCellValueAsString(row.getCell(17)));
				}

				if (state.isEmpty() || state == null) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("state");
					responceExcel.setReason("error in state");
					responceExcelRepository.save(responceExcel);
					falseCount++;
					continue;

				} else {
					proposer.setState(getCellValueAsString(row.getCell(18)));
				}
				if (maritalStatus.isEmpty() || maritalStatus == null) {
					proposer.setMaritalStatus("SINGLE");
				} else {
					proposer.setMaritalStatus(getCellValueAsString(row.getCell(7)));
				}
				if (town.isEmpty() || town == null) {
					proposer.setTown(Town.PANVEL);

				} else {
					proposer.setTown(Town.valueOf(getCellValueAsString(row.getCell(16)).toUpperCase()));
				}

				
				if (altMobile.matches("\\d{10}") && altMobile.matches("^[6-9]\\d{9}$")) {
					proposer.setAlternateMobileNumber(getCellValueAsString(row.getCell(10)));
				}
				if (!address1.matches("^[A-Za-z0-9\s,/-]+$")) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("address1");
					responceExcel.setReason("error in address1");
					responceExcelRepository.save(responceExcel);
					falseCount++;
					continue;
				} else {
					proposer.setAddressLine1(getCellValueAsString(row.getCell(11)));
				}
				if (address2.matches("^[A-Za-z0-9\s,/-]+$")) {
					proposer.setAddressLine2(getCellValueAsString(row.getCell(12)));
				}

				if (address3.matches("^[A-Za-z0-9\s,/-]+$")) {
					proposer.setAddressLine3(getCellValueAsString(row.getCell(13)));
				}

				proposer.setStatus('Y');
				String gender = proposer.getGender().toString();
				if (gender != null && !gender.isEmpty()) {
					Optional<GenderType> genderType = genderRepository.findByType(gender);
					if (genderType.isPresent()) {
						proposer.setGenderId(genderType.get().getGenderId());
					} else {
						throw new IllegalArgumentException("enter corrrect gender");
					}
				} else {
					throw new IllegalArgumentException("enter can not be null");
				}

				Proposer savedProposer = proposerRepository.save(proposer);
				excelList.add(savedProposer);
				Long id = savedProposer.getId();
				System.out.println(id);
				responceExcel.setStatus("sucess");
				responceExcel.setErrorField(String.valueOf(id));
				responceExcel.setReason("sucessfully added");
				responceExcelRepository.save(responceExcel);
				count++;

			}
		}
		return excelList;
	}

	@Override
	public Integer getTotalCountSucess() {
		// TODO Auto-generated method stub
		return count;
	}

	@Override
	public Integer totalEntry() {
		// TODO Auto-generated method stub
		return totalEntry;
	}

	@Override
	public Integer totalFalseEntry() {
		// TODO Auto-generated method stub
		return falseCount;
	}

	@Override
	public Map<String, Object> saveProposersFromExcelMandatory2(MultipartFile file) throws IOException {

		List<Proposer> excelList = new ArrayList<>();
		Map<String, Object> resultMap = new HashMap<>();
		int successCount = 0;
		int failedCount = 0;
		int totalCount = 0;

		try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
			XSSFSheet sheet = workbook.getSheetAt(0);
			totalCount = sheet.getLastRowNum();
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				ResponceExcel responceExcel = new ResponceExcel();

				Row row = sheet.getRow(i);
				if (row == null)
					continue;
				String title = check(row, 0);
				String fullName = check(row, 1);
				String genderString = check(row, 2);
				String dob = check(row, 3);
				String income = check(row, 4);
				String pan = check(row, 5);
				String aadhar = check(row, 6);
				String maritalStatus = check(row, 7);
				String email = check(row, 8);
				String mobile = check(row, 9);
				String altMobile = check(row, 10);
				String address1 = check(row, 11);
				String address2 = check(row, 12);
				String address3 = check(row, 13);
				String pincode = check(row, 14);
				String area = check(row, 15);
				String town = check(row, 16);
				String city = check(row, 17);
				String state = check(row, 18);

				Proposer proposer = new Proposer();

				boolean isValidTitle = false;
				for (Title titleEnum : Title.values()) {

					if (titleEnum.name().equalsIgnoreCase(title.trim())) {
						isValidTitle = true;
						break;
					}
				}
				if (isValidTitle == true) {
					proposer.setTitle(Title.valueOf(getCellValueAsString(row.getCell(0)).toUpperCase()));

				}
				if (fullName == null || fullName.isEmpty() || !fullName.matches("[A-Za-z\\s]+")) {

					responceExcel.setStatus("failed");
					responceExcel.setErrorField("full Name");
					responceExcel.setReason("error in full Name");
					responceExcelRepository.save(responceExcel);
					failedCount++;
					continue;

				} else {
					proposer.setFullName(getCellValueAsString(row.getCell(1)));

				}

				if (genderString.isEmpty() || genderString == null) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("gender");
					responceExcel.setReason("error in gender");
					responceExcelRepository.save(responceExcel);
					failedCount++;
					continue;

				} else {
					proposer.setGender(Gender.valueOf(getCellValueAsString(row.getCell(2)).toUpperCase()));
				}

				if (dob.isEmpty() || dob == null) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("dob");
					responceExcel.setReason("error in dob");
					responceExcelRepository.save(responceExcel);
					failedCount++;
					continue;

				} else {
					proposer.setDateOfBirth(getCellValueAsString(row.getCell(3)));
				}

				if (pan.length() != 10 || !pan.matches("^[A-Z]{5}[0-9]{4}[A-Z]{1}$") || pan == null || pan.isEmpty()) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("pancard");
					responceExcel.setReason("error in pancard");
					responceExcelRepository.save(responceExcel);
					failedCount++;
					continue;

				} else {
					proposer.setPanNumber(getCellValueAsString(row.getCell(5)));
				}
				if (!income.matches("\\d+")) {
					proposer.setAnnualIncome(null);
				} else {
					proposer.setAnnualIncome(getCellValueAsString(row.getCell(4)));
				}

				if (aadhar.length() != 12 || !aadhar.matches("\\d{12}") || aadhar == null || aadhar.isEmpty()) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("aadhar card");
					responceExcel.setReason("error in aadhar card");
					responceExcelRepository.save(responceExcel);
					failedCount++;
					continue;

				} else {
					proposer.setAadharNumber(getCellValueAsString(row.getCell(6)));
				}

				if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$") || email.isEmpty()
						|| email == null) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("email");
					responceExcel.setReason("error in email");
					responceExcelRepository.save(responceExcel);
					failedCount++;
					continue;

				} else {
					proposer.setEmail(getCellValueAsString(row.getCell(8)));
				}

				if (!mobile.matches("\\d{10}") || mobile.isEmpty() || mobile == null
						|| !mobile.matches("^[6-9]\\d{9}$")) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("mobile number");
					responceExcel.setReason("error in mobile number");
					responceExcelRepository.save(responceExcel);
					failedCount++;
					continue;

				} else {
					proposer.setMobileNumber(getCellValueAsString(row.getCell(9)));
				}

				boolean isValidArea = false;
				for (Area areaEnum : Area.values()) {

					if (areaEnum.name().equalsIgnoreCase(area.trim())) {
						isValidArea = true;
						break;
					}
				}
				if (area.isEmpty() || area == null || isValidArea == false) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("area");
					responceExcel.setReason("error in area");
					responceExcelRepository.save(responceExcel);
					failedCount++;
					continue;

				} else {
					proposer.setArea(Area.valueOf(getCellValueAsString(row.getCell(15)).toUpperCase()));
				}

				if (pincode.length() != 6 || pincode == null) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("pin code");
					responceExcel.setReason("error in pin code");
					responceExcelRepository.save(responceExcel);
					failedCount++;
					continue;

				} else {
					proposer.setPincode(getCellValueAsString(row.getCell(14)));
				}

				if (city.isEmpty() || city == null) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("city");
					responceExcel.setReason("error in city");
					responceExcelRepository.save(responceExcel);
					failedCount++;
					continue;

				} else {
					proposer.setCity(getCellValueAsString(row.getCell(17)));
				}

				if (state.isEmpty() || state == null) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("state");
					responceExcel.setReason("error in state");
					responceExcelRepository.save(responceExcel);
					failedCount++;
					continue;

				} else {
					proposer.setState(getCellValueAsString(row.getCell(18)));
				}
				if (maritalStatus.isEmpty() || maritalStatus == null) {
					proposer.setMaritalStatus("SINGLE");
				} else {
					proposer.setMaritalStatus(getCellValueAsString(row.getCell(7)));
				}
				if (town.isEmpty() || town == null) {
					proposer.setTown(Town.PANVEL);

				} else {
					proposer.setTown(Town.valueOf(getCellValueAsString(row.getCell(16)).toUpperCase()));
				}

				if (altMobile.matches("\\d{10}") && altMobile.matches("^[6-9]\\d{9}$")) {
					proposer.setAlternateMobileNumber(getCellValueAsString(row.getCell(10)));
				}
				if (!address1.matches("^[A-Za-z0-9\s,/-]+$")) {
					responceExcel.setStatus("failed");
					responceExcel.setErrorField("address1");
					responceExcel.setReason("error in address1");
					responceExcelRepository.save(responceExcel);
					failedCount++;
					continue;
				} else {
					proposer.setAddressLine1(getCellValueAsString(row.getCell(11)));
				}
				if (address2.matches("^[A-Za-z0-9\s,/-]+$")) {
					proposer.setAddressLine2(getCellValueAsString(row.getCell(12)));
				}

				if (address3.matches("^[A-Za-z0-9\s,/-]+$")) {
					proposer.setAddressLine3(getCellValueAsString(row.getCell(13)));
				}

				proposer.setStatus('Y');
				String gender = proposer.getGender().toString();
				if (gender != null && !gender.isEmpty()) {
					Optional<GenderType> genderType = genderRepository.findByType(gender);
					if (genderType.isPresent()) {
						proposer.setGenderId(genderType.get().getGenderId());
					} else {
						throw new IllegalArgumentException("enter corrrect gender");
					}
				} else {
					throw new IllegalArgumentException("enter can not be null");
				}
				Proposer savedProposer = proposerRepository.save(proposer);
				excelList.add(savedProposer);
				responceExcel.setStatus("success");
				responceExcel.setErrorField(String.valueOf(savedProposer.getId()));
				responceExcel.setReason("successfully added");
				responceExcelRepository.save(responceExcel);
				successCount++;
			}
		}

		resultMap.put("totalCount", totalCount);
		resultMap.put("failedCount", failedCount);
		resultMap.put("successCount", successCount);
		resultMap.put("addedProposers", excelList);

		return resultMap;

	}

	@Override
	public List<Map<String, Object>> getAllProposersByPagingAndSortingAndfilteringUsingMap(ProposerPage proposerPage,
			ResponseHandler<List<Map<String, Object>>> responseHandler) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Proposer> criteriaQuery = criteriaBuilder.createQuery(Proposer.class);
		Root<Proposer> root = criteriaQuery.from(Proposer.class);

		List<Predicate> predicates = new ArrayList<>();
		List<SearchFilter> searchFilters = proposerPage.getSearchFilters();
		
		if (searchFilters != null) {
			for (SearchFilter filter : searchFilters) {
				if (filter.getFullName() != null && !filter.getFullName().trim().isEmpty()) {
					predicates.add(criteriaBuilder.like(root.get("fullName"), "%" + filter.getFullName().trim() + "%"));
				}
				if (filter.getEmail() != null && !filter.getEmail().trim().isEmpty()) {
					predicates.add(criteriaBuilder.like(root.get("email"), "%" + filter.getEmail().trim() + "%"));
				}
				if (filter.getCity() != null && !filter.getCity().trim().isEmpty()) {
					predicates.add(criteriaBuilder.like(root.get("city"), "%" + filter.getCity().trim() + "%"));
				}
				if (filter.getStatus() != null && filter.getStatus() == 'Y') {
					predicates.add(criteriaBuilder.equal(root.get("status"), 'Y'));
				}
				if (filter.getStatus() != null && filter.getStatus() == 'N') {
					predicates.add(criteriaBuilder.equal(root.get("status"), 'N'));
				}
			}
		}

		if (!predicates.isEmpty()) {
			criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
		}

		String sortBy = proposerPage.getSortBy();
		if (sortBy == null || sortBy.trim().isEmpty()) {
			sortBy = "id";
		}

		String sortOrder = proposerPage.getSortOrder();
		if (sortOrder == null || sortOrder.trim().isEmpty() || sortOrder.equalsIgnoreCase("desc")) {
			criteriaQuery.orderBy(criteriaBuilder.desc(root.get(sortBy)));
		} else {
			criteriaQuery.orderBy(criteriaBuilder.asc(root.get(sortBy)));
		}

		TypedQuery<Proposer> typedQuery = entityManager.createQuery(criteriaQuery);

		List<Proposer> fullList = typedQuery.getResultList();
		totalRecord = fullList.size();

		if (proposerPage.getPageNumber() > 0 && proposerPage.getPageSize() > 0) {
			int firstResult = (proposerPage.getPageNumber() - 1) * proposerPage.getPageSize();
			typedQuery.setFirstResult(firstResult);
			typedQuery.setMaxResults(proposerPage.getPageSize());
		}

		List<Proposer> pagedList = typedQuery.getResultList();

		List<Map<String, Object>> resultList = new ArrayList<>();
		for (Proposer proposer : pagedList) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", proposer.getId());
			map.put("fullName", proposer.getFullName());
			map.put("email", proposer.getEmail());
			map.put("city", proposer.getCity());

			resultList.add(map);
		}

		return resultList;
	}

	@Autowired
	private RestTemplate restTemplate;

	private static final String PRODUCT_API_URL = "https://fakestoreapi.com/products";

	@Override
	public List<Map<String, Object>> getAllProducts() {
		// TODO Auto-generated method stub
		return restTemplate.getForObject(PRODUCT_API_URL, List.class);
	}

	@Override
	public List<Map<String, Object>> getFilteredProducts(String category, Double minPrice, Double maxPrice,
			String sortBy, Boolean groupByCategory, Integer topN) {
		List<Map<String, Object>> products = restTemplate.getForObject(PRODUCT_API_URL, List.class);

		if (category != null) {
			products = products.stream().filter(p -> category.equalsIgnoreCase((String) p.get("category")))
					.collect(Collectors.toList());
		}

		if (minPrice != null) {
			products = products.stream().filter(p -> ((Number) p.get("price")).doubleValue() >= minPrice)
					.collect(Collectors.toList());
		}

		if (maxPrice != null) {
			products = products.stream().filter(p -> ((Number) p.get("price")).doubleValue() <= maxPrice)
					.collect(Collectors.toList());
		}

		if ("price".equalsIgnoreCase(sortBy)) {
			products.sort(Comparator.comparing(p -> ((Number) p.get("price")).doubleValue()));
		} else if ("rating".equalsIgnoreCase(sortBy)) {
			products.sort((p1, p2) -> {
				double r1 = 0.0, r2 = 0.0;

				Object rating1 = p1.get("rating");
				if (rating1 instanceof Map<?, ?> ratingMap1) {
					Object rate1 = ratingMap1.get("rate");
					if (rate1 instanceof Number num1) {
						r1 = num1.doubleValue();
					}
				}

				Object rating2 = p2.get("rating");
				if (rating2 instanceof Map<?, ?> ratingMap2) {
					Object rate2 = ratingMap2.get("rate");
					if (rate2 instanceof Number num2) {
						r2 = num2.doubleValue();
					}
				}

				return Double.compare(r2, r1);
			});
		}
		if (topN != null && topN > 0) {
			products = products.stream().limit(topN).collect(Collectors.toList());
		}

		if (groupByCategory != null && groupByCategory) {
			Map<String, List<Map<String, Object>>> grouped = products.stream()
					.collect(Collectors.groupingBy(p -> (String) p.get("category")));

			List<Map<String, Object>> groupedList = new ArrayList<>();
			for (Map.Entry<String, List<Map<String, Object>>> entry : grouped.entrySet()) {
				Map<String, Object> group = new HashMap<>();
				group.put("category", entry.getKey());
				group.put("products", entry.getValue());
				groupedList.add(group);
			}
			return groupedList;
		}

		return products;
	}

	public List<Map<String, Object>> getSelectedUserInfo() {
		String url = "https://jsonplaceholder.typicode.com/users";
		List<Map<String, Object>> users = restTemplate.getForObject(url, List.class);

		return users.stream().map(user -> {
			Map<String, Object> simplifiedUser = new HashMap<>();

			simplifiedUser.put("name", user.get("name"));

			Map<String, Object> address = (Map<String, Object>) user.get("address");
			Map<String, Object> simplifiedAddress = new HashMap<>();
			simplifiedAddress.put("street", address.get("street"));
			simplifiedAddress.put("suite", address.get("suite"));
			simplifiedAddress.put("city", address.get("city"));
			simplifiedAddress.put("zipcode", address.get("zipcode"));
			simplifiedUser.put("address", simplifiedAddress);
			Map<String, Object> geo = (Map<String, Object>) address.get("geo");
			Map<String, Object> simplifiedGeo = new HashMap<>();
			simplifiedGeo.put("latitude", geo.get("lat"));
			simplifiedUser.put("geo", simplifiedGeo);
			Map<String, Object> company = (Map<String, Object>) user.get("company");
			simplifiedUser.put("companyName", company.get("name"));

			return simplifiedUser;
		}).collect(Collectors.toList());
	}

//	@Override
//	public List<Map<String, Object>> getFilteredProductsUsingWebClient(String category, Double minPrice,
//			Double maxPrice, String sortBy, Boolean groupByCategory, Integer topN) {
//
//		WebClient webClient = WebClient.builder().baseUrl("https://fakestoreapi.com").build();
//		Mono<List<Map<String, Object>>> responseMono = webClient.get().uri("/products").retrieve()
//				.bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {
//				});
//
//		List<Map<String, Object>> products = responseMono.block(); // Blocking for now
//
//		if (products == null)
//			return List.of();
//
//		// Filter by category
//		if (category != null) {
//			products = products.stream().filter(p -> category.equalsIgnoreCase((String) p.get("category")))
//					.collect(Collectors.toList());
//		}
//
//		// Filter by min price
//		if (minPrice != null) {
//			products = products.stream().filter(p -> ((Number) p.get("price")).doubleValue() >= minPrice)
//					.collect(Collectors.toList());
//		}
//
//		// Filter by max price
//		if (maxPrice != null) {
//			products = products.stream().filter(p -> ((Number) p.get("price")).doubleValue() <= maxPrice)
//					.collect(Collectors.toList());
//		}
//
//		// Sorting
//		if ("price".equalsIgnoreCase(sortBy)) {
//			products.sort(Comparator.comparing(p -> ((Number) p.get("price")).doubleValue()));
//		} else if ("rating".equalsIgnoreCase(sortBy)) {
//			products.sort((p1, p2) -> {
//				double r1 = 0.0, r2 = 0.0;
//
//				Object rating1 = p1.get("rating");
//				if (rating1 instanceof Map<?, ?> map1) {
//					Object rate1 = map1.get("rate");
//					if (rate1 instanceof Number num1) {
//						r1 = num1.doubleValue();
//					}
//				}
//
//				Object rating2 = p2.get("rating");
//				if (rating2 instanceof Map<?, ?> map2) {
//					Object rate2 = map2.get("rate");
//					if (rate2 instanceof Number num2) {
//						r2 = num2.doubleValue();
//					}
//				}
//
//				return Double.compare(r2, r1); // Descending
//			});
//		}
//
//		// Top N
//		if (topN != null && topN > 0) {
//			products = products.stream().limit(topN).collect(Collectors.toList());
//		}
//
//		// Group by category
//		if (Boolean.TRUE.equals(groupByCategory)) {
//			Map<String, List<Map<String, Object>>> grouped = products.stream()
//					.collect(Collectors.groupingBy(p -> (String) p.get("category")));
//
//			List<Map<String, Object>> groupedList = new ArrayList<>();
//			for (Map.Entry<String, List<Map<String, Object>>> entry : grouped.entrySet()) {
//				Map<String, Object> group = new HashMap<>();
//				group.put("category", entry.getKey());
//				group.put("products", entry.getValue());
//				groupedList.add(group);
//			}
//			return groupedList;
//		}
//
//		return products;
//	}

	public List<Product> getAllProduct() {
		RestTemplate restTemplate = new RestTemplate();
		String BASE_URL = "https://fakestoreapi.com/products";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<Product[]> response = restTemplate.exchange(BASE_URL, HttpMethod.GET, entity, Product[].class);

		return Arrays.asList(response.getBody());
	}

	public Product getProductById(int id) {
		String BASE_URL = "https://fakestoreapi.com/products";
		String url = BASE_URL + "/" + id;

		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(url, Product.class);
	}

	@Override
	public Map<String, Object> saveProposersFromExcelMandatoryUsingScheduler(MultipartFile file) throws IOException {

		String uploadDir = "C:\\subodh\\";

		File dir = new File(uploadDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
		String filePath = uploadDir + fileName;
		XSSFWorkbook newworkbook = new XSSFWorkbook(file.getInputStream());
		XSSFSheet newsheet = newworkbook.getSheetAt(0);

		if (newsheet.getLastRowNum() > 10) {
			QueueTable queue = new QueueTable();
			queue.setIsProcessed('N');
			queue.setStatus('Y');
			queue.setRowCount(newsheet.getLastRowNum());
			queue.setRowRead(0);
			queue.setFilePath(filePath);
			file.transferTo(new File(filePath));
			queueRepository.save(queue);
			Map<String, Object> scheduledResponse = new HashMap<>();
			scheduledResponse.put("message", "File has been queued for processing in batches.");
			scheduledResponse.put("rowCount", newsheet.getLastRowNum());
			scheduledResponse.put("filePath", filePath);
			scheduledResponse.put("queueId", queue.getId());

			newworkbook.close();
			return scheduledResponse;
		}
		List<Proposer> excelList = new ArrayList<>();
		Map<String, Object> resultMap = new HashMap<>();
		int successCount = 0;
		int failedCount = 0;
		int totalCount = 0;

		try {

			totalCount = newsheet.getLastRowNum();
			for (int i = 1; i <= newsheet.getLastRowNum(); i++) {

				Row row = newsheet.getRow(i);
				if (row == null)
					continue;
				List<String> errors = new ArrayList<>();
				String title = check(row, 0);
				String fullName = check(row, 1);
				String genderString = check(row, 2);
				String dob = check(row, 3);
				String income = check(row, 4);
				String pan = check(row, 5);
				String aadhar = check(row, 6);
				String maritalStatus = check(row, 7);
				String email = check(row, 8);
				String mobile = check(row, 9);
				String altMobile = check(row, 10);
				String address1 = check(row, 11);
				String address2 = check(row, 12);
				String address3 = check(row, 13);
				String pincode = check(row, 14);
				String area = check(row, 15);
				String town = check(row, 16);
				String city = check(row, 17);
				String state = check(row, 18);

				Proposer proposer = new Proposer();

				boolean isValidTitle = false;
				for (Title titleEnum : Title.values()) {

					if (titleEnum.name().equalsIgnoreCase(title.trim())) {
						isValidTitle = true;
						break;
					}
				}
				if (isValidTitle == true) {
					proposer.setTitle(Title.valueOf(getCellValueAsString(row.getCell(0)).toUpperCase()));

				}
				if (fullName == null || fullName.isEmpty() || !fullName.matches("[A-Za-z\\s]+")) {

					errors.add("FullName");

				} else {
					proposer.setFullName(getCellValueAsString(row.getCell(1)));

				}

				if (genderString.isEmpty() || genderString == null) {

					errors.add("gender");

				} else {
					proposer.setGender(Gender.valueOf(getCellValueAsString(row.getCell(2)).toUpperCase()));
				}

				if (dob.isEmpty() || dob == null) {

					errors.add("Date of Birth");

				} else {
					proposer.setDateOfBirth(getCellValueAsString(row.getCell(3)));
				}

				if (pan.length() != 10 || !pan.matches("^[A-Z]{5}[0-9]{4}[A-Z]{1}$") || pan == null || pan.isEmpty()) {

					errors.add("PanCard");

				} else {
					proposer.setPanNumber(getCellValueAsString(row.getCell(5)));
				}
				if (!income.matches("\\d+")) {
					proposer.setAnnualIncome(null);
				} else {
					proposer.setAnnualIncome(getCellValueAsString(row.getCell(4)));
				}

				if (aadhar.length() != 12 || !aadhar.matches("\\d{12}") || aadhar == null || aadhar.isEmpty()) {

					errors.add("AadharCard");

				} else {
					proposer.setAadharNumber(getCellValueAsString(row.getCell(6)));
				}

				if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$") || email.isEmpty()
						|| email == null) {

					errors.add("Email");

				} else {
					proposer.setEmail(getCellValueAsString(row.getCell(8)));
				}

				if (!mobile.matches("\\d{10}") || mobile.isEmpty() || mobile == null
						|| !mobile.matches("^[6-9]\\d{9}$")) {

					errors.add("MobileNumber");

				} else {
					proposer.setMobileNumber(getCellValueAsString(row.getCell(9)));
				}

				boolean isValidArea = false;
				for (Area areaEnum : Area.values()) {

					if (areaEnum.name().equalsIgnoreCase(area.trim())) {
						isValidArea = true;
						break;
					}
				}
				if (area.isEmpty() || area == null || isValidArea == false) {

					errors.add("Area");

				} else {
					proposer.setArea(Area.valueOf(getCellValueAsString(row.getCell(15)).toUpperCase()));
				}

				if (pincode.length() != 6 || pincode == null) {

					errors.add("Pincode");

				} else {
					proposer.setPincode(getCellValueAsString(row.getCell(14)));
				}

				if (city.isEmpty() || city == null) {

					errors.add("City");

				} else {
					proposer.setCity(getCellValueAsString(row.getCell(17)));
				}

				if (state.isEmpty() || state == null) {

					errors.add("State");

				} else {
					proposer.setState(getCellValueAsString(row.getCell(18)));
				}
				if (maritalStatus.isEmpty() || maritalStatus == null) {
					proposer.setMaritalStatus("SINGLE");
				} else {
					proposer.setMaritalStatus(getCellValueAsString(row.getCell(7)));
				}
				if (town.isEmpty() || town == null) {
					proposer.setTown(Town.PANVEL);

				} else {
					proposer.setTown(Town.valueOf(getCellValueAsString(row.getCell(16)).toUpperCase()));
				}

				if (altMobile.matches("\\d{10}") && altMobile.matches("^[6-9]\\d{9}$")) {
					proposer.setAlternateMobileNumber(getCellValueAsString(row.getCell(10)));
				}
				if (!address1.matches("^[A-Za-z0-9\s,/-]+$")) {

					errors.add("Address1");

				} else {
					proposer.setAddressLine1(getCellValueAsString(row.getCell(11)));
				}
				if (address2.matches("^[A-Za-z0-9\s,/-]+$")) {
					proposer.setAddressLine2(getCellValueAsString(row.getCell(12)));
				}

				if (address3.matches("^[A-Za-z0-9\s,/-]+$")) {
					proposer.setAddressLine3(getCellValueAsString(row.getCell(13)));
				}

				proposer.setStatus('Y');
				if (proposer.getGender() != null) {
					String gender = proposer.getGender().toString();

					if (gender != null && !gender.isEmpty()) {
						Optional<GenderType> genderType = genderRepository.findByType(gender);
						if (genderType.isPresent()) {
							proposer.setGenderId(genderType.get().getGenderId());
						} else {
							throw new IllegalArgumentException("enter corrrect gender");
						}
					} else {
						throw new IllegalArgumentException("enter can not be null");
					}
				}

				if (!errors.isEmpty()) {

					for (String error : errors) {
						ResponceExcel responceExcel = new ResponceExcel();
						responceExcel.setStatus("failed");
						responceExcel.setErrorField(error);
						responceExcel.setReason(error + " error in field");
						
						responceExcelRepository.save(responceExcel);
					}
					failedCount++;

				} else {
					ResponceExcel responceExcel2 = new ResponceExcel();
					Proposer savedProposer = proposerRepository.save(proposer);
					responceExcel2.setStatus("success");
					responceExcel2.setErrorField(String.valueOf(savedProposer.getId()));
					responceExcel2.setReason("successfully added");
					successCount++;
					responceExcelRepository.save(responceExcel2);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		resultMap.put("totalCount", totalCount);
		resultMap.put("failedCount", failedCount);
		resultMap.put("successCount", successCount);
		resultMap.put("addedProposers", excelList);

		return resultMap;

	}

	@Override
	@Scheduled(fixedDelay = 60000)
	public void scheduleQueueProcessing() {

	List<QueueTable> queueTables = queueRepository.findByIsProcessed('N');
		for (QueueTable queue : queueTables) {
		    int lastProcessedRow = queue.getLastProcessedRow() != null ? queue.getLastProcessedRow() : 0;
		    String filePath = queue.getFilePath();

			File file = new File(filePath);
			if (file.exists()) {

				try {
					FileInputStream fileInputStream = new FileInputStream(file);
					XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
					XSSFSheet sheet = workbook.getSheetAt(0);
					int totalRows = sheet.getLastRowNum();
					int rowsToProcess = 5;
					int startRow = lastProcessedRow + 1;
					int endRow = Math.min(startRow + rowsToProcess - 1, totalRows);
					int batchSize = 10;
					int processedCount = 0;
					int batchNumber = (startRow - 1) / batchSize + 1;

					System.out.println(" Starting Batch " + batchNumber);
					System.out.println(" Processing rows from " + startRow + " to " + endRow);

					for (int i = startRow; i <= endRow; i++) {
						
						Row row = sheet.getRow(i);
						if (row == null)
							continue;
						Boolean hasError = false;
						List<String> errors = new ArrayList<>();

						String title = check(row, 0);
						String fullName = check(row, 1);
						String genderString = check(row, 2);
						String dob = check(row, 3);
						String income = check(row, 4);
						String pan = check(row, 5);
						String aadhar = check(row, 6);
						String maritalStatus = check(row, 7);
						String email = check(row, 8);
						String mobile = check(row, 9);
						String altMobile = check(row, 10);
						String address1 = check(row, 11);
						String address2 = check(row, 12);
						String address3 = check(row, 13);
						String pincode = check(row, 14);
						String area = check(row, 15);
						String town = check(row, 16);
						String city = check(row, 17);
						String state = check(row, 18);
						Proposer proposer = new Proposer();

						boolean isValidTitle = false;
						for (Title titleEnum : Title.values()) {

							if (titleEnum.name().equalsIgnoreCase(title.trim())) {
								isValidTitle = true;
								break;
							}
						}
						if (isValidTitle == true) {
							proposer.setTitle(Title.valueOf(getCellValueAsString(row.getCell(0)).toUpperCase()));

						}
						if (fullName == null || fullName.isEmpty() || !fullName.matches("[A-Za-z\\s]+")) {

							hasError = true;

							errors.add("fullName");
							System.err.println(errors + proposer.getFullName());

						} else {
							proposer.setFullName(getCellValueAsString(row.getCell(1)));

						}

						if (genderString == null || genderString.isEmpty()) {

							hasError = true;

							errors.add("Gender");
							System.err.println(errors);

						} else {
							proposer.setGender(Gender.valueOf(getCellValueAsString(row.getCell(2)).toUpperCase()));

						}

						if (dob == null || dob.isEmpty()) {

							hasError = true;

							errors.add("Date of Birth");

						} else {
							proposer.setDateOfBirth(getCellValueAsString(row.getCell(3)));
						}

						if (pan == null || pan.isEmpty() || pan.length() != 10
								|| !pan.matches("^[A-Z]{5}[0-9]{4}[A-Z]{1}$")) {

							hasError = true;

							errors.add("Pancard");
							System.err.println(errors);

						} else {
							proposer.setPanNumber(getCellValueAsString(row.getCell(5)));
						}
						if (!income.matches("\\d+")) {
							proposer.setAnnualIncome(null);
						} else {
							proposer.setAnnualIncome(getCellValueAsString(row.getCell(4)));
						}

						if (aadhar == null || aadhar.isEmpty() || aadhar.length() != 12 || !aadhar.matches("\\d{12}")) {
							
							hasError = true;
							    
							errors.add("AadharCard");
							System.err.println(errors);

						} else {
							proposer.setAadharNumber(getCellValueAsString(row.getCell(6)));
						}

						if (email == null || email.isEmpty()
								|| !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
							
							hasError = true;
						    
							errors.add("Email");
							System.err.println(errors);

						} else {
							proposer.setEmail(getCellValueAsString(row.getCell(8)));
						}

						if (mobile == null || mobile.isEmpty() || !mobile.matches("\\d{10}")
								|| !mobile.matches("^[6-9]\\d{9}$")) {							
							hasError = true;
							    
							errors.add("Mobile");
							System.err.println(errors);

						} else {
							proposer.setMobileNumber(getCellValueAsString(row.getCell(9)));
						}

						boolean isValidArea = false;
						for (Area areaEnum : Area.values()) {

							if (areaEnum.name().equalsIgnoreCase(area.trim())) {
								isValidArea = true;
								break;
							}
						}
						if (area == null || area.isEmpty() || isValidArea == false) {

							hasError = true;

							errors.add("Area");

						} else {
							proposer.setArea(Area.valueOf(getCellValueAsString(row.getCell(15)).toUpperCase()));
						}

						if (pincode == null || pincode.length() != 6) {

							errors.add("Pincode");

						} else {
							proposer.setPincode(getCellValueAsString(row.getCell(14)));
						}

						if (city == null || city.isEmpty()) {

							hasError = true;

							errors.add("City");

						} else {
							proposer.setCity(getCellValueAsString(row.getCell(17)));
						}

						if (state == null || state.isEmpty()) {

							hasError = true;

							errors.add("State");

						} else {
							proposer.setState(getCellValueAsString(row.getCell(18)));
						}
						if (maritalStatus.isEmpty() || maritalStatus == null) {
							proposer.setMaritalStatus("SINGLE");
						} else {
							proposer.setMaritalStatus(getCellValueAsString(row.getCell(7)));
						}
						if (town.isEmpty() || town == null) {
							proposer.setTown(Town.PANVEL);

						} else {
							proposer.setTown(Town.valueOf(getCellValueAsString(row.getCell(16)).toUpperCase()));
						}

						if (altMobile.matches("\\d{10}") && altMobile.matches("^[6-9]\\d{9}$")) {
							proposer.setAlternateMobileNumber(getCellValueAsString(row.getCell(10)));
						}
						if (address1 == null || !address1.matches("^[A-Za-z0-9\s,/-]+$")) {

							hasError = true;

							errors.add("Address1");

						} else {
							proposer.setAddressLine1(getCellValueAsString(row.getCell(11)));
						}
						if (address2.matches("^[A-Za-z0-9\s,/-]+$")) {
							proposer.setAddressLine2(getCellValueAsString(row.getCell(12)));
						}

						if (address3.matches("^[A-Za-z0-9\s,/-]+$")) {
							proposer.setAddressLine3(getCellValueAsString(row.getCell(13)));
						}

						proposer.setStatus('Y');
						if (proposer.getGender() != null) {
							String gender = proposer.getGender().toString();

							if (gender != null && !gender.isEmpty()) {
								Optional<GenderType> genderType = genderRepository.findByType(gender);
								if (genderType.isPresent()) {
									proposer.setGenderId(genderType.get().getGenderId());
								} else {
									throw new IllegalArgumentException("enter corrrect gender");
								}
							} else {
								throw new IllegalArgumentException("enter can not be null");
							}
						}


						if (!errors.isEmpty()) {
							Long queueId  =  queue.getId();
							for( String error :errors) {
								ResponceExcel responceExcel = new ResponceExcel();
							responceExcel.setStatus("failed");
							responceExcel.setErrorField(error);
							responceExcel.setReason( error + " error in field"  );
							responceExcel.setQueueId(queueId);
							responceExcelRepository.save(responceExcel);
							
							}

						} else {
							ResponceExcel responceExcel2 = new ResponceExcel();
							Proposer savedProposer = proposerRepository.save(proposer);
							responceExcel2.setStatus("success");
							responceExcel2.setErrorField(String.valueOf(savedProposer.getId()));
							responceExcel2.setReason("successfully added");
							
							responceExcelRepository.save(responceExcel2);
						}

						processedCount++;
						queue.setRowRead(i);
						queue.setLastProcessedRow(i);
						if (queue.getLastProcessedRow() >= totalRows) {
							queue.setIsProcessed('Y');
						}
						queueRepository.save(queue);
					}

					System.out.println("scheduled");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}


}
