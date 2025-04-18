package com.example.demo.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.ProposerDto;
import com.example.demo.dto.handler.ResponseHandler;
import com.example.demo.model.Proposer;
import com.example.demo.pagination.ProposerPage;

import jakarta.servlet.http.HttpServletResponse;

public interface ProposerService {

//	public Proposer registerProposer(Proposer proposer);
	public List<Proposer> getAllProposer();
//	public Proposer updateProposer(Long id,Proposer proposer);
	public Proposer deleteProposer(Long id);
	
	public Proposer registerProposer(ProposerDto proposerDto);
	
	public Proposer registerProposerExcel(Proposer proposer);
	public Proposer updateProposerUsingDto(Long id  ,ProposerDto proposerDto);
	
	public List<Proposer> getAllProposersByPagingAndSorting(ProposerPage proposerPage);
	public List<Proposer> getAllProposersByPagingAndSortingAndfiltering(ProposerPage proposerPage, ResponseHandler<List<Proposer>> responseHandler);

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

}
