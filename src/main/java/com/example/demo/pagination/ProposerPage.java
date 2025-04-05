package com.example.demo.pagination;

import java.util.SortedMap;

import org.hibernate.query.SortDirection;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class ProposerPage {

	private Long pageNumber;
	private Long pageSizeLong;
	private Sort.Direction sortDirection = Sort.Direction.ASC;
	private String sortBy ;
	
	public ProposerPage() {
		super();
	}
	public ProposerPage(Long pageNumber, Long pageSizeLong, Direction sortDirection, String sortBy) {
		super();
		this.pageNumber = pageNumber;
		this.pageSizeLong = pageSizeLong;
		this.sortDirection = sortDirection;
		this.sortBy = sortBy;
	}
	public Long getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(Long pageNumber) {
		this.pageNumber = pageNumber;
	}
	public Long getPageSizeLong() {
		return pageSizeLong;
	}
	public void setPageSizeLong(Long pageSizeLong) {
		this.pageSizeLong = pageSizeLong;
	}
	public Sort.Direction getSortDirection() {
		return sortDirection;
	}
	public void setSortDirection(Sort.Direction sortDirection) {
		this.sortDirection = sortDirection;
	}
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	
}
