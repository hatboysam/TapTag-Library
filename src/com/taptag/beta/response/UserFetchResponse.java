package com.taptag.beta.response;

public class UserFetchResponse extends APIResponse {
	
	private Integer id;
	private String status;
	
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
}
