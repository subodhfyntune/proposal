package com.example.demo.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputFilter.Status;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.criteria.*;

import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.HealthApplication;
import com.example.demo.dto.ProposerDto;
import com.example.demo.dto.handler.ResponseHandler;
import com.example.demo.exception.ProposerDeletedAlready;
import com.example.demo.model.Area;
import com.example.demo.model.Gender;
import com.example.demo.model.GenderType;
import com.example.demo.model.Proposer;
import com.example.demo.model.Title;
import com.example.demo.model.Town;
import com.example.demo.pagination.ProposerPage;
import com.example.demo.pagination.SearchFilter;
import com.example.demo.repository.GenderRepository;
import com.example.demo.repository.ProposerRepository;

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
	private ProposerRepository proposerRepository;

	@Autowired
	private GenderRepository genderRepository;
	Integer totalRecord = 0;

//	@Override
//	public Proposer registerProposer(Proposer proposer) {
//		        proposer.setStatus('Y');
//		Proposer registerProposer =  proposerRepository.save(proposer);
//		return registerProposer;
//	}

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
				|| !proposerDto.getMobileNumber().matches("\\d+")) {
			throw new IllegalArgumentException("Enter valid mobile no.");
		}
		boolean pan = proposerRepository.existsByPanNumber(proposerDto.getPanNumber());
		if (proposerDto.getPanNumber() == null || proposerDto.getPanNumber().length() != 10
				|| !proposerDto.getPanNumber().matches("^[A-Z]{5}[0-9]{4}[A-Z]{1}$") || pan) {
			throw new IllegalArgumentException("Enter a valid PAN number");
		}
		boolean aadhar = proposerRepository.existsByAadharNumber(proposerDto.getAadharNumber());
		if (proposerDto.getAadharNumber() == null || proposerDto.getAadharNumber().length() != 12
				|| !proposerDto.getAadharNumber().matches("\\d+") || aadhar) {
			throw new IllegalArgumentException("Enter a valid Aadhar Number");
		}

		if (proposerDto.getAlternateMobileNumber() == null || proposerDto.getAlternateMobileNumber().length() != 10
				|| !proposerDto.getAlternateMobileNumber().matches("\\d+")) {
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

//	@Override
//	public Proposer updateProposerUsingDto(Long id, ProposerDto proposerDto) {
//		Optional<Proposer> getProposer = proposerRepository.findByIdAndStatus(id, 'Y');
//
//		if (!getProposer.isPresent()) {
//			throw new IllegalArgumentException("Proposer not found with id " + id);
//		}
//		if (proposerDto.getFullName() == null || proposerDto.getFullName().trim().isEmpty()) {
//		    throw new IllegalArgumentException("Enter the Full Name");
//		}
//
//		if (proposerDto.getEmail() == null || proposerDto.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$\r\n"
//				+ "") ) {
//		    throw new IllegalArgumentException("Enter a valid Email");
//		}
//
//		if (proposerDto.getMobileNumber() == null || proposerDto.getMobileNumber().length() != 10 || !proposerDto.getMobileNumber().matches("\\d+") ) {
//		    throw new IllegalArgumentException("Enter valid mobile no.");
//		}
//		boolean pan = proposerRepository.existsByPanNumber(proposerDto.getPanNumber());
//		if (proposerDto.getPanNumber() == null || proposerDto.getPanNumber().length() != 10 || !proposerDto.getPanNumber().matches("^[A-Z]{5}[0-9]{4}[A-Z]{1}$") || pan) {
//		    throw new IllegalArgumentException("Enter a valid PAN number");
//		}
//		boolean aadhar =proposerRepository.existsByAadharNumber(proposerDto.getAadharNumber());
//		if (proposerDto.getAadharNumber() == null || proposerDto.getAadharNumber().length() != 12 || !proposerDto.getAadharNumber().matches("\\d+") || aadhar ) {
//		    throw new IllegalArgumentException("Enter a valid Aadhar Number");
//		}
//
//		if (proposerDto.getAlternateMobileNumber() == null || proposerDto.getAlternateMobileNumber().length() != 10 || !proposerDto.getAlternateMobileNumber().matches("\\d+")) {
//		    throw new IllegalArgumentException("Enter a valid Alternate Mobile Number");
//		}
//
//		if (proposerDto.getPincode() == null || proposerDto.getPincode().length() != 6 || !proposerDto.getPincode().matches("\\d+")) {
//		    throw new IllegalArgumentException("Enter a valid Pincode");
//		}
//
//		if (proposerDto.getAnnualIncome() == null || proposerDto.getAnnualIncome().isEmpty() || !proposerDto.getAnnualIncome().matches("\\d+")) {
//		    throw new IllegalArgumentException("Enter a valid Annual Income");
//		}
//
//		if (proposerDto.getAddressLine1() == null || proposerDto.getAddressLine1().trim().isEmpty()) {
//		    throw new IllegalArgumentException("Enter Address Line 1");
//		}
//
//		if (proposerDto.getAddressLine2() == null || proposerDto.getAddressLine2().trim().isEmpty()) {
//		    throw new IllegalArgumentException("Enter Address Line 2");
//		}
//
//		if (proposerDto.getAddressLine3() == null || proposerDto.getAddressLine3().trim().isEmpty()) {
//		    throw new IllegalArgumentException("Enter Address Line 3");
//		}
//		Proposer proposer = getProposer.get();
//		
//		proposer.setTitle(proposerDto.getTitle());
//		proposer.setFullName(proposerDto.getFullName());
//		proposer.setGender(proposerDto.getGender());
//		proposer.setDateOfBirth(proposerDto.getDateOfBirth());
//		proposer.setAnnualIncome(proposerDto.getAnnualIncome());
//		proposer.setPanNumber(proposerDto.getPanNumber());
//		proposer.setAadharNumber(proposerDto.getAadharNumber());
//		proposer.setMaritalStatus(proposerDto.getMaritalStatus());
//		proposer.setEmail(proposerDto.getEmail());
//		proposer.setMobileNumber(proposerDto.getMobileNumber());
//		proposer.setAlternateMobileNumber(proposerDto.getAlternateMobileNumber());
//		proposer.setAddressLine1(proposerDto.getAddressLine1());
//		proposer.setAddressLine2(proposerDto.getAddressLine2());
//		proposer.setAddressLine3(proposerDto.getAddressLine3());
//		proposer.setPincode(proposerDto.getPincode());
//		proposer.setArea(proposerDto.getArea());
//		proposer.setTown(proposerDto.getTown());
//		proposer.setCity(proposerDto.getCity());
//		proposer.setState(proposerDto.getState());
//		
//
//		Proposer updatedProposer = proposerRepository.save(proposer);
//		return updatedProposer;
//	}
//
//
//}

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
			if (proposerDto.getMobileNumber().length() != 10 || !proposerDto.getMobileNumber().matches("\\d+")) {
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

			if (proposerDto.getAadharNumber().length() != 12 || !proposerDto.getAadharNumber().matches("\\d+")) {
				throw new IllegalArgumentException("Enter a valid Aadhar Number");
			}
			proposer.setAadharNumber(proposerDto.getAadharNumber());
		}

		if (proposerDto.getAlternateMobileNumber() != null) {
			if (proposerDto.getAlternateMobileNumber().length() != 10
					|| !proposerDto.getAlternateMobileNumber().matches("\\d+")) {
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
//	    if (proposerDto.getGender() != null) proposer.setGender(proposerDto.getGender());

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

//	@Override
//	public List<Proposer> getAllProposersByPagingAndSorting(ProposerPage proposerPage) {
//		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//		CriteriaQuery<Proposer> criteriaQuery = criteriaBuilder.createQuery(Proposer.class);
//		Root<Proposer> root = criteriaQuery.from(Proposer.class);
//		if(proposerPage.getPageNumber() >= 0 && proposerPage.getPageSize() >= 0) {
//			if(proposerPage.getSortBy() == null || proposerPage.getSortOrder().isEmpty()) {
//				proposerPage.setSortBy("id");
//				proposerPage.setSortOrder("DESC");
//			}
//			
//		}
//		else {
//			throw new IllegalArgumentException("Error occured");
//		}
//		if (proposerPage.getPageNumber() <= 0 && proposerPage.getPageSize() <= 0) {
//			criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));
//		} else if (proposerPage.getSortBy() != null && proposerPage.getSortOrder() != null) {
//			String sortBy = proposerPage.getSortBy();
//			if ("ASC".equalsIgnoreCase(proposerPage.getSortOrder())) {
//				criteriaQuery.orderBy(criteriaBuilder.asc(root.get(sortBy)));
//			} else {
//				criteriaQuery.orderBy(criteriaBuilder.desc(root.get(sortBy)));
//			}
//		}
//
//		if (proposerPage.getPageNumber() <= 0 && proposerPage.getPageSize() <= 0) {
//			return entityManager.createQuery(criteriaQuery).getResultList();
//		} else {
//
//			Integer size = proposerPage.getPageSize();
//			Integer page = proposerPage.getPageNumber();
//
//			TypedQuery<Proposer> typedQuery = entityManager.createQuery(criteriaQuery);
//			typedQuery.setFirstResult((page - 1) * size);
//			typedQuery.setMaxResults(size);
//			return typedQuery.getResultList();
//		}
//	}
//	

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

//	    predicates.add(criteriaBuilder.equal(root.get("status"), 'Y'));
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
		Row row = sheet.createRow(0);

		row.createCell(0).setCellValue("ID");
		row.createCell(1).setCellValue("Name");
		row.createCell(2).setCellValue("Email");
		row.createCell(3).setCellValue("City");

		int rowCount = 1;

		for (Proposer p : praposers) {

			row.createCell(0).setCellValue(p.getId());
			row.createCell(1).setCellValue(p.getFullName());
			row.createCell(2).setCellValue(p.getEmail());
			row.createCell(3).setCellValue(p.getCity());
			rowCount++;
		}
		ServletOutputStream outputStream = httpServletResponse.getOutputStream();
		workbook.write(outputStream);

		workbook.close();

		outputStream.close();
	}

	public String generateSampleExcel() throws IOException {
//		String filePathString = "C:/subodh/";
		String filePathString = "C:\\subodh\\";

		XSSFWorkbook workbook = new XSSFWorkbook();
		var sheet = workbook.createSheet("Proposer");

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
			FileOutputStream fileOutputStream= new FileOutputStream(fullFilePath);
			workbook.write(fileOutputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//		httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + fileName);

//		workbook.write(httpServletResponse.getOutputStream());
		workbook.close();
		return fullFilePath;
	}

	private String getCellValueAsString(Cell cell) {
	    if (cell == null) return "";

	    switch (cell.getCellType()) {
	        case STRING:
	            return cell.getStringCellValue().trim();
	        case NUMERIC:
	            if (DateUtil.isCellDateFormatted(cell)) {
	                return cell.getDateCellValue().toString(); 
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

	public void saveProposersFromExcel(MultipartFile file) throws IOException {
	    try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
	        XSSFSheet sheet = workbook.getSheetAt(0);

	        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
	            Row row = sheet.getRow(i);
	            if (row == null) continue;

	            Proposer proposer = new Proposer();

	            proposer.setTitle(Title.valueOf(getCellValueAsString(row.getCell(0)).toUpperCase()));
	            proposer.setFullName(getCellValueAsString(row.getCell(1)));
	            proposer.setGender(Gender.valueOf(getCellValueAsString(row.getCell(2)).toUpperCase()));
	            proposer.setDateOfBirth(getCellValueAsString(row.getCell(3)));
	            proposer.setAnnualIncome(getCellValueAsString(row.getCell(4)));
	            proposer.setPanNumber(getCellValueAsString(row.getCell(5)));
	            proposer.setAadharNumber(getCellValueAsString(row.getCell(6)));
	            proposer.setMaritalStatus(getCellValueAsString(row.getCell(7)));
//	            proposer.setGenderId((int) row.getCell(8).getNumericCellValue());
	            proposer.setEmail(getCellValueAsString(row.getCell(8)));
	            proposer.setMobileNumber(getCellValueAsString(row.getCell(9)));
	            proposer.setAlternateMobileNumber(getCellValueAsString(row.getCell(10)));
	            proposer.setAddressLine1(getCellValueAsString(row.getCell(11)));
	            proposer.setAddressLine2(getCellValueAsString(row.getCell(12)));
	            proposer.setAddressLine3(getCellValueAsString(row.getCell(13)));
	            proposer.setPincode(getCellValueAsString(row.getCell(14)));
	            proposer.setState(getCellValueAsString(row.getCell(15)));
//	            proposer.setStatus(getCellValueAsString(row.getCell(17)).charAt(0));
	            proposer.setArea(Area.valueOf(getCellValueAsString(row.getCell(16)).toUpperCase()));
	            proposer.setTown(Town.valueOf(getCellValueAsString(row.getCell(17)).toUpperCase()));
	            proposer.setCity(getCellValueAsString(row.getCell(18)));

	            proposerRepository.save(proposer);
	        }
	    }
	}

    

}
