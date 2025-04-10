package com.example.demo.export;

import java.awt.Font;
import java.io.IOException;
import java.net.http.HttpClient;
import java.text.NumberFormat.Style;
import java.util.List;

import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.example.demo.model.Proposer;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class ExcelExport {

	private List<Proposer> praposers;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	
	public ExcelExport(List<Proposer> praposers) {
		super();
		this.praposers = praposers;
		workbook = new XSSFWorkbook();
	}
	
	public void writeHeaderRow() {
		sheet = workbook.createSheet("Proposer");
		Row row = sheet.createRow(0);

		CellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		cellStyle.setFont(font);

		row.createCell(0).setCellValue("ID");
		row.createCell(1).setCellValue("Name");
		row.createCell(2).setCellValue("Email");
		row.createCell(3).setCellValue("City");

		for (Cell cell : row) {
			cell.setCellStyle(cellStyle);
		}
	}
	 public void writeDataRow() {
		 
		 int rowCount = 1;
		 
		 for (Proposer p : praposers) {
	            Row row = sheet.createRow(rowCount++);

	            row.createCell(0).setCellValue(p.getId());
	            row.createCell(1).setCellValue(p.getFullName());
	            row.createCell(2).setCellValue(p.getEmail());
	            row.createCell(3).setCellValue(p.getCity());
	        }
	 }
	
	public void export(HttpServletResponse response) throws IOException {
		writeHeaderRow();
		writeDataRow();
		
		ServletOutputStream outputStream = response.getOutputStream();
		
		workbook.write(outputStream);
		
		workbook.close();
		
		outputStream.close();
		}
}
