package com.facebook.info;

import java.io.Serializable;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;


@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookUserInfo implements Comparable<FacebookUserInfo>, Serializable {
	
	private String first_name;
	private String last_name;
	private String email;
	private int id;
	
	public FacebookUserInfo(){
		
	}
	
	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int compareTo(FacebookUserInfo arg0) {
		String thisEmail = this.getEmail();
		String otherEmail = arg0.getEmail();
		return thisEmail.compareTo(otherEmail);
	}
}
