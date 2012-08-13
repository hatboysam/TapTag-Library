package com.taptag.beta.response;

public class UserFetchResponse implements APIResponse {
	
	private Integer id;
	private String status;
	
	public UserFetchResponse() {
		id = -1;
		status = "NONE";
	}
	
	public boolean hasError() {
		return "ERROR".equals(status) || (id == -1);
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
}
