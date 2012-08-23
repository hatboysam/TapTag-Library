package com.taptag.beta.response;

public class RedemptionResponse extends APIResponse {

	private String status;
	
	public RedemptionResponse(){
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public boolean hasError() {
		return ERROR.equals(status);
	}

}
