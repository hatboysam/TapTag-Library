package com.taptag.beta.response;

public class TapSubmitResponse extends APIResponse {
	
	private String status;
	
	public TapSubmitResponse() {
		this.status = NONE;
	}

	@Override
	public boolean hasError() {
		return (NONE.equals(status) || ERROR.equals(status));
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

}
