package com.taptag.beta.network;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.info.FacebookUserInfo;
import com.taptag.beta.R;
import com.taptag.beta.reward.Reward;
import com.taptag.beta.vendor.Vendor;

public class TapTagAPI {

	public static HttpClient client = new DefaultHttpClient();

	public static String ROOT = "http://taptag.herokuapp.com";
	public static String USERS = "/users";
	public static String VENDORS = "/vendors";
	public static String VISITED = "/visited.json";
	public static String PROGRESS = "/progress.json";
	public static String JSON = "application/json";
	public static String JSON_END = ".json";
	public static String JSON_FETCH = "http://taptag.herokuapp.com/users/fetch";

	public static Vendor[] vendorsVisitedBy(Integer userID) {
		URI visitedPath = pathWithID(ROOT + USERS, userID, VISITED);
		InputStream stream = streamFrom(jsonGet(visitedPath));
		ObjectMapper om = new ObjectMapper();
		try {
			VendorsListWrapper vlw = om.readValue(stream,
					VendorsListWrapper.class);
			return vlw.getVendorArray();
		} catch (Exception e) {
			e.printStackTrace();
			return (new Vendor[0]);
		}
	}

	public static Reward[] progressByUserAndCompany(Integer userID,
			Integer companyID) {
		String withParams = PROGRESS + "?company="
				+ Integer.toString(companyID);
		URI progressPath = pathWithID(ROOT + USERS, userID, withParams);
		InputStream stream = streamFrom(jsonGet(progressPath));
		ObjectMapper om = new ObjectMapper();
		try {
			ProgressWrapper pw = om.readValue(stream, ProgressWrapper.class);
			return pw.getProgress();
		} catch (Exception e) {
			e.printStackTrace();
			return (new Reward[0]);
		}
	}

	public static Vendor vendorById(Integer vendorID) {
		URI vendorPath = pathWithID(ROOT + VENDORS, vendorID, JSON_END);
		InputStream stream = streamFrom(jsonGet(vendorPath));
		ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, true);
		try {
			Vendor vendor = om.readValue(stream, Vendor.class);
			return vendor;
		} catch (Exception e) {
			e.printStackTrace();
			return (new Vendor());
		}
	}

	public static FacebookUserInfo userInfoFromFacebook(String userInfoResponse) {
		FacebookUserInfo facebookUserInfo = new FacebookUserInfo();
		ObjectMapper om = new ObjectMapper();
		try {
			facebookUserInfo = om.readValue(userInfoResponse,
					FacebookUserInfo.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return facebookUserInfo;
	}

	public static URI pathWithID(String prefix, Integer id, String suffix) {
		String idString = "/" + Integer.toString(id);
		String fullPath = prefix + idString + suffix;
		try {
			URI result = new URI(fullPath);
			return result;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static HttpGet jsonGet(URI path) {
		HttpGet result = new HttpGet(path);
		result.setHeader("Content-Type", JSON);
		result.setHeader("Accept", JSON);
		return result;
	}

	public static InputStream streamFrom(HttpGet httpGet) {
		InputStream result = null;
		try {
			HttpResponse response = client.execute(httpGet);
			result = response.getEntity().getContent();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
	}

	/**
	public static String jsonPostFacebookUserInfo(FacebookUserInfo facebookUserInfo) throws Exception {
		
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(JSON_FETCH);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectMapper om  = new ObjectMapper();
		
		FacebookUserInfoWrapper facebookUserInfoWrapper = new FacebookUserInfoWrapper();
		om.writeValue(byteArrayOutputStream ,facebookUserInfoWrapper);
		
		byte[] array = byteArrayOutputStream.toByteArray();
		
		ByteArrayEntity se = new ByteArrayEntity(array);
		httpost.setEntity(se);
		httpost.setHeader("Accept", "application/json");
		httpost.setHeader("Content-type", "application/json");

		ResponseHandler responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httpost, responseHandler);
		return response;
	}
	 */
	// ===========================================================
	// =====================WRAPPER CLASSES=======================
	// ===========================================================

	@JsonRootName(value = "vendors")
	public static class VendorsListWrapper {

		private VendorWrapper[] vendors;

		public VendorsListWrapper() {
			vendors = new VendorWrapper[0];
		}

		public VendorWrapper[] getVendors() {
			return vendors;
		}

		public void setVendor(VendorWrapper[] vendors) {
			this.vendors = vendors;
		}

		public Vendor[] getVendorArray() {
			Vendor[] result = new Vendor[vendors.length];
			for (int i = 0; i < result.length; i++) {
				VendorWrapper vw = vendors[i];
				result[i] = vw.getVendor();
			}
			return result;
		}
	}
	
	@SuppressWarnings("serial")
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class FacebookUserInfoWrapper implements  Serializable {
		private FacebookUserInfo user;

		public FacebookUserInfo getUser() {
			return user;
		}

		public void setUser(FacebookUserInfo user) {
			this.user = user;
		}
	}

	public static class VendorWrapper {

		private Vendor vendor;

		public VendorWrapper() {
			vendor = null;
		}

		public Vendor getVendor() {
			return vendor;
		}

		public void setVendor(Vendor vendor) {
			this.vendor = vendor;
		}
	}

	public static class ProgressWrapper {
		private Reward[] progress;

		public ProgressWrapper() {
			progress = new Reward[0];
		}

		public Reward[] getProgress() {
			return progress;
		}

		public void setProgress(Reward[] progress) {
			this.progress = progress;
		}
	}

}
