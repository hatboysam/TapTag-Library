package com.taptag.beta.facebook;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;


@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookUserInfo implements Comparable<FacebookUserInfo>, Serializable {
	
	private String first_name;
	private String last_name;
	private String email;
	private int id;
	
	public FacebookUserInfo(){	
	}
	
	public FacebookUserInfo(String first_name, String last_name, String email) {
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.id = 999999;
	}

	public int compareTo(FacebookUserInfo arg0) {
		String thisEmail = this.getEmail();
		String otherEmail = arg0.getEmail();
		return thisEmail.compareTo(otherEmail);
	}
	
	@JsonIgnore
	public String getFirst_name() {
		return first_name;
	}
	@JsonProperty("first")
	public String getFirst() {
		return first_name;
	}
	@JsonProperty("first_name")
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	
	@JsonIgnore
	public String getLast_name() {
		return last_name;
	}
	@JsonProperty("last")
	public String getLast() {
		return last_name;
	}
	@JsonProperty("last_name")
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@JsonIgnore
	public int getId() {
		return id;
	}
	@JsonProperty("facebook")
	public String getFacebook() {
		return Integer.toString(id);
	}
	@JsonProperty("id")
	public void setId(int id) {
		this.id = id;
	}
}
