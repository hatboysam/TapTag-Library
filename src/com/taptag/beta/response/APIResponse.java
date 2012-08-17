package com.taptag.beta.response;

public abstract class APIResponse {
	
	public static String ERROR = "ERROR";
	public static String CREATED = "CREATED";
	public static String FOUND = "FOUND";
	public static String NONE = "NONE";
	
	public abstract boolean hasError();
	
	public boolean success() {
		return !hasError();
	}

}
