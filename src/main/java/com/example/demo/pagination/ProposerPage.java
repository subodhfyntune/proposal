package com.example.demo.pagination;

import java.util.List;

public class ProposerPage {

	private Integer pageNumber;
	private Integer pageSize;
	private String sortOrder; //asc
	private String sortBy ; //fullname
	private List<SearchFilter> searchFilters;

	
	

	public List<SearchFilter> getSearchFilters() {
		return searchFilters;
	}

	public void setSearchFilters(List<SearchFilter> searchFilters) {
		this.searchFilters = searchFilters;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	
}
