package com.taptag.beta.tap;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tap implements Serializable {

	@JsonProperty("vendor_id")
	private Integer vendorID;
	@JsonProperty("company_id")
	private Integer companyID;
	@JsonProperty("user_id")
	private Integer userID;
	@JsonProperty("tapped_time")
	private Date tappedTime;
	
	public Tap() {
		vendorID = 0;
		companyID = 0;
		userID = 0;
		tappedTime = new Date();
	}
	
	//============================================================
	//====================GETTERS AND SETTERS=====================
	//============================================================

	public Integer getVendorID() {
		return vendorID;
	}
	public void setVendorID(Integer vendorID) {
		this.vendorID = vendorID;
	}

	public Integer getCompanyID() {
		return companyID;
	}
	public void setCompanyID(Integer companyID) {
		this.companyID = companyID;
	}

	public Integer getUserID() {
		return userID;
	}
	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	public Date getTappedTime() {
		return tappedTime;
	}
	public void setTappedTime(Date tappedTime) {
		this.tappedTime = tappedTime;
	}
	
}
