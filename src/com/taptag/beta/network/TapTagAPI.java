package com.taptag.beta.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.annotate.JsonRootName;

import com.taptag.beta.facebook.FacebookUserInfo;
import com.taptag.beta.location.LatLong;
import com.taptag.beta.redemption.Redemption;
import com.taptag.beta.response.RedemptionResponse;
import com.taptag.beta.response.TapSubmitResponse;
import com.taptag.beta.response.UserFetchResponse;
import com.taptag.beta.reward.Reward;
import com.taptag.beta.tap.Tap;
import com.taptag.beta.vendor.Vendor;

public class TapTagAPI {

	public static HttpClient client = new DefaultHttpClient();
	public static SimpleDateFormat rewardDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public static String ROOT = "http://taptag.herokuapp.com";
	public static String HTTPS_ROOT = "https://taptag.herokuapp.com";
	public static String USERS = "/users";
	public static String TAPS = "/taps";
	public static String VENDORS = "/vendors";
	public static String REDEMPTIONS = "/redemptions";
	public static String VISITED = "/visited.json";
	public static String PROGRESS = "/progress.json";
	public static String COMPLETED = "/completed.json";
	public static String REDEEMED = "/redeemed.json";
	public static String FETCH = "/users/fetch.json";
	public static String NEAR = "/vendors/near.json";
	public static String JSON = "application/json";
	public static String JSON_END = ".json";

	public static TapSubmitResponse submitTap(Integer userId, Vendor vendor) {
		Tap tap = new Tap();
		tap.setCompanyID(vendor.getCompanyId());
		tap.setVendorID(vendor.getId());
		tap.setUserID(userId);
		//Create POST
		URI tapsPath = combinePath(HTTPS_ROOT, TAPS);
		byte[] tapBytes = writeObjectToByteArray(new TapWrapper(tap));
		ByteArrayEntity bae = new ByteArrayEntity(tapBytes);
		HttpPost jsonPost = jsonPost(tapsPath);
		jsonPost.setEntity(bae);
		//Submit POST
		try {
			ObjectMapper om = new ObjectMapper();
			InputStream responseStream = streamFrom(jsonPost);
			TapSubmitResponse tsr = om.readValue(responseStream, TapSubmitResponse.class);
			return tsr;
		} catch (Exception e) {
			e.printStackTrace();
			return new TapSubmitResponse();
		}
	}
	
	public static Vendor[] vendorsVisitedBy(Integer userID) {
		URI visitedPath = pathWithID(ROOT + USERS, userID, VISITED);
		InputStream stream = streamFrom(jsonGet(visitedPath));
		ObjectMapper om = new ObjectMapper();
		try {
			VendorsListWrapper vlw = om.readValue(stream, VendorsListWrapper.class);
			return vlw.getVendorArray();
		} catch (Exception e) {
			e.printStackTrace();
			return (new Vendor[0]);
		}
	}
	
	public static Vendor[] allVendors() {
		URI vendorsPath = combinePath(ROOT, VENDORS + ".json");
		InputStream stream = streamFrom(jsonGet(vendorsPath));
		ObjectMapper om = new ObjectMapper();
		try {
			VendorsListWrapper vlw = om.readValue(stream, VendorsListWrapper.class);
			return vlw.getVendorArray();
		} catch (Exception e) {
			e.printStackTrace();
			return (new Vendor[0]);
		}
	}
	
	public static Vendor[] vendorsNear(LatLong location, Integer radius) {
		URI nearPath = pathWithParams(HTTPS_ROOT, NEAR, nearParameters(location, radius));
		InputStream stream = streamFrom(jsonGet(nearPath));
		ObjectMapper om = new ObjectMapper();
		try {
			VendorsListWrapper vlw = om.readValue(stream, VendorsListWrapper.class);
			return vlw.getVendorArray();
		} catch (Exception e) {
			e.printStackTrace();
			return (new Vendor[0]);
		}
	}

	public static Reward[] progressByUserAndCompany(Integer userID, Integer companyID) {
		String withParams = PROGRESS + "?company="+ Integer.toString(companyID);
		URI progressPath = pathWithID(ROOT + USERS, userID, withParams);
		return progressFromURI(progressPath);
	}
	
	public static Reward[] progressByUser(Integer userID) {
		URI progressPath = pathWithID(ROOT + USERS, userID, PROGRESS);
		return progressFromURI(progressPath);
	}
	
	public static Reward[] completedByUser(Integer userID) {
		URI completedPath = pathWithID(ROOT + USERS, userID, COMPLETED);
		InputStream stream = streamFrom(jsonGet(completedPath));
		ObjectMapper om = new ObjectMapper();
		om.setDateFormat(rewardDateFormat);
		try {
			CompletedWrapper cw = om.readValue(stream, CompletedWrapper.class);
			return cw.getCompleted();
		} catch (Exception e) {
			e.printStackTrace();
			return new Reward[0];
		}
	}
	
	public static Reward[] redeemedByUser(Integer userID) {
		URI completedPath = pathWithID(ROOT + USERS, userID, REDEEMED);
		InputStream stream = streamFrom(jsonGet(completedPath));
		ObjectMapper om = new ObjectMapper();
		om.setDateFormat(rewardDateFormat);
		try {
			RedeemedWrapper cw = om.readValue(stream, RedeemedWrapper.class);
			return cw.getRedeemed();
		} catch (Exception e) {
			e.printStackTrace();
			return new Reward[0];
		}
	}
	
	private static Reward[] progressFromURI(URI progressPath) {
		InputStream stream = streamFrom(jsonGet(progressPath));
		ObjectMapper om = new ObjectMapper();
		om.setDateFormat(rewardDateFormat);
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
			facebookUserInfo = om.readValue(userInfoResponse, FacebookUserInfo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return facebookUserInfo;
	}
	
	public static UserFetchResponse fetchUser(FacebookUserInfo facebookUserInfo) {
		URI fetchPath = combinePath(HTTPS_ROOT, FETCH);
		HttpPost httpPost = jsonPost(fetchPath);
		ObjectMapper om  = new ObjectMapper();
		
		FacebookUserInfoWrapper facebookUserInfoWrapper = new FacebookUserInfoWrapper();
		facebookUserInfoWrapper.setUser(facebookUserInfo);
		
		byte[] array = writeObjectToByteArray(facebookUserInfoWrapper);
		ByteArrayEntity bae = new ByteArrayEntity(array);
		httpPost.setEntity(bae);
		
		InputStream responseStream = streamFrom(httpPost);
		try {
			UserFetchResponse response = om.readValue(responseStream, UserFetchResponse.class);
			return response;
		} catch (Exception e) {
			return (new UserFetchResponse());
		}
		
	}
	
	public static RedemptionResponse redeemReward(Redemption toSubmit) {
		URI redeemPath = combinePath(HTTPS_ROOT, REDEMPTIONS);
		HttpPost httpPost = jsonPost(redeemPath);
		ObjectMapper om = new ObjectMapper();
		
		byte[] array = writeObjectToByteArray(toSubmit);
		ByteArrayEntity bae = new ByteArrayEntity(array);
		httpPost.setEntity(bae);
		
		InputStream responseStream = streamFrom(httpPost);
		try {
			RedemptionResponse response = om.readValue(responseStream, RedemptionResponse.class);
			return response;
		} catch (Exception e) {
			return (new RedemptionResponse());
		}
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
	
	public static URI combinePath(String prefix, String suffix) {
		String fullPath = prefix + suffix;
		try {
			URI result = new URI(fullPath);
			return result;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static URI pathWithParams(String root, String path, String params) {
		String fullPath = root + path + params;
		try {
			URI result = new URI(fullPath);
			return result;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String nearParameters(LatLong location, Integer radius) {
		Double lat = location.getLat();
		Double lng = location.getLng();
		return "?lat=" + lat.toString() + "&lng=" + lng.toString() + "&radius=" + radius.toString();
	}

	public static HttpGet jsonGet(URI path) {
		HttpGet result = new HttpGet(path);
		result.setHeader("Content-Type", JSON);
		result.setHeader("Accept", JSON);
		return result;
	}
	
	public static HttpPost jsonPost(URI path) {
		HttpPost result = new HttpPost(path);
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
	
	public static InputStream streamFrom(HttpPost httpPost) {
		InputStream result = null;
		try {
			HttpResponse response = client.execute(httpPost);
			result = response.getEntity().getContent();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
	}
	
	public static byte[] writeObjectToByteArray(Serializable object) {
		ObjectMapper om = new ObjectMapper();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			om.writeValue(baos, object);
			return baos.toByteArray();
		} catch (Exception e) {
			return new byte[0];
		}
	}

	
	// ===========================================================
	// =====================RESPONSE HANDLERS=====================
	// ===========================================================
	
	/**
	 * Class to handle JSON responses and return an InputStream
	 * @author samstern
	 */
	public static class StreamResponseHandler implements ResponseHandler<InputStream> {
		@Override
		public InputStream handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
			HttpEntity entity = response.getEntity();
			return entity.getContent();
		}	
	}
	
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
	
	public static class CompletedWrapper {
		private Reward[] completed;
		
		public CompletedWrapper() {
			completed = new Reward[0];
		}
		public Reward[] getCompleted() {
			return completed;
		}
		public void setCompleted(Reward[] completed) {
			this.completed = completed;
		}
	}
	
	public static class RedeemedWrapper {
		private Reward[] redeemed;
		
		public RedeemedWrapper() {
			redeemed = new Reward[0];
		}
		public Reward[] getRedeemed() {
			return redeemed;
		}
		public void setRedeemed(Reward[] redeemed) {
			this.redeemed = redeemed;
		}
	}
	
	@SuppressWarnings("serial")
	public static class TapWrapper implements Serializable {
		private Tap tap;
		
		public TapWrapper() {
		}
		public TapWrapper(Tap tap) {
			this.tap = tap;
		}	
		public Tap getTap() {
			return tap;
		}
		public void setTap(Tap tap) {
			this.tap = tap;
		}
	}
	
	@SuppressWarnings("serial")
	public static class RedemptionWrapper implements Serializable {
		private Redemption redemption;
		
		public RedemptionWrapper(){
		}
		public RedemptionWrapper(Redemption redemption){
			this.redemption = redemption;
		}
		public Redemption getRedemption() {
			return redemption;
		}
		public void setRedemption(Redemption redemption){
			this.redemption = redemption;
		}
	}

}
