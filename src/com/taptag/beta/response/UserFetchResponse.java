package com.taptag.beta.response;

public class UserFetchResponse extends APIResponse {
	
	private Integer id;
	private String status;
	private String first;
	private String last;
	
	public UserFetchResponse() {
		id = -1;
		status = NONE;
	}
	
	public boolean hasError() {
		return (NONE.equals(status) || ERROR.equals(status));
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getFirst() {
		return first;
	}
	public void setFirst(String first) {
		this.first = first;
	}

	public String getLast() {
		return last;
	}
	public void setLast(String last) {
		this.last = last;
	}
}
