package com.example.demo.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.ProposerDto;
import com.example.demo.entity.Product;
import com.example.demo.entity.Proposer;
import com.example.demo.pagination.ProposerPage;
import com.example.demo.response.ProposerResponse;
import com.example.demo.response.ResponseHandler;

import jakarta.servlet.http.HttpServletResponse;

public interface ProposerService {


	public List<Proposer> getAllProposer();

	public ProposerResponse getProposerById(Long id);

	public Proposer deleteProposer(Long id);

	public Proposer registerProposer(ProposerDto proposerDto);

	public Proposer registerProposerExcel(Proposer proposer);

	public Proposer updateProposerUsingDto(Long id, ProposerDto proposerDto);

	public List<Proposer> getAllProposersByPagingAndSorting(ProposerPage proposerPage);

	public List<Proposer> getAllProposersByPagingAndSortingAndfiltering(ProposerPage proposerPage,
			ResponseHandler<List<Proposer>> responseHandler);

	public Integer getTotalRecord();

	public void generateExcel(HttpServletResponse httpServletResponse) throws Exception;

	public void generateExcel2() throws Exception;

	public String generateSampleExcel() throws IOException;

	public String generateSampleExcelMandatory() throws IOException;

	public List<Proposer> saveProposersFromExcel(MultipartFile file) throws IOException;

	public List<Proposer> saveProposersFromExcelMandatory(MultipartFile file) throws IOException;

	public List<Proposer> saveProposersFromExcelUsingDto(MultipartFile file) throws IOException;

	public Integer getTotalCountSucess();

	public Integer totalEntry();

	public Integer totalFalseEntry();

	public Map<String, Object> saveProposersFromExcelMandatory2(MultipartFile file) throws IOException;

	public List<Map<String, Object>> getAllProposersByPagingAndSortingAndfilteringUsingMap(ProposerPage proposerPage,
			ResponseHandler<List<Map<String, Object>>> responseHandler);
	
	public List<Map<String,Object>> getAllProducts();
	
	public List<Map<String, Object>> getFilteredProducts(String category, Double minPrice, Double maxPrice, String sortBy, Boolean groupByCategory, Integer topN);
	public List<Map<String, Object>> getSelectedUserInfo();
	
//	public List<Map<String, Object>> getFilteredProductsUsingWebClient(String category, Double minPrice, Double maxPrice, String sortBy, Boolean groupByCategory, Integer topN);
	public List<Product> getAllProduct();
	public Product getProductById(int id);
	public Map<String, Object> saveProposersFromExcelMandatoryUsingScheduler(MultipartFile file) throws IOException;
	public void scheduleQueueProcessing();
//	public Map<String, Object>  processExcelAndSaveData(MultipartFile file);
}
