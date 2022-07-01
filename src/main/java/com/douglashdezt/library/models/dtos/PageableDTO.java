package com.douglashdezt.library.models.dtos;

import javax.validation.constraints.Min;

public class PageableDTO {
	@Min(0)
	private int page;
	
	@Min(0)
	private int limit;
	
	public PageableDTO() {
		super();
		
		this.page = 0;
		this.limit = 10;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	@Override
	public String toString() {
		return "PageableDTO [page=" + page + ", limit=" + limit + "]";
	}
}
