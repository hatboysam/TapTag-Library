package com.taptag.beta.location;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TagAddress implements Serializable {
	
	private String address1;
	private String address2;
	private String city;
	private String state;
	private Integer zip;
	private LatLong coordinates;
	
	public TagAddress() {
		this.address1 = "";
		this.address2 = "";
		this.city = "";
		this.state = "";
		this.zip = 00000;
		this.coordinates = null;
	}
	
	public TagAddress(String streetAddress1, String streetAddress2, String city, String state, String zip, String country) {
		this.address1 = streetAddress1;
		this.address2 = streetAddress2;
		this.city = city;
		this.state = state;
		this.zip = Integer.parseInt(zip);
		this.coordinates = null;
	}
	
	public TagAddress(String streetAddress1, String city, String state, String zip) {
		this.address1 = streetAddress1;
		this.address2 = "";
		this.city = city;
		this.state = state;
		this.zip = Integer.parseInt(zip);
		this.coordinates = null;
	}
	
	
	public String toString() {
		String result = "";
		result += address1;
		if (address2 != null && !address2.equals("")) {
			result += (", " + address2);
		}
		result += (", " + city + " " + state + ", " + Integer.toString(zip));
		return result;
	}
	
	/**
	 * Get the latitude and longitude from the TagAddress, uses the internet
	 * @param context
	 * @return
	 */
	public Double[] getLatLong(Context context) {
		if (coordinates != null) {
			return coordinates.toArray();
		}
		Geocoder code = new Geocoder(context);
		List<Address> addresses;
		try {
			addresses = code.getFromLocationName(this.toString(), 2);

			if(addresses.size() > 0) {
				Address bestGuess = addresses.get(0);
				coordinates = new LatLong(bestGuess.getLatitude(), bestGuess.getLongitude());
			} else {
				coordinates = new LatLong();
			}
		} catch (IOException e) {
			coordinates = new LatLong();
		}
		return coordinates.toArray();
	}
	
	/**
	 * Get the distance between two {lat, long} points
	 * @param thisLatLong
	 * @param thatLatLong
	 * @return
	 */
	public static Double getDistance(Double[] thisLatLong, Double[] thatLatLong) {
		if (thisLatLong == null || thatLatLong == null || thisLatLong.length < 2 || thatLatLong.length < 2) {
			return 0.0;
		}
		float[] result = new float[5];
		Location.distanceBetween (thisLatLong[0], thisLatLong[1], thatLatLong[0], thatLatLong[1], result);
		return ((double) result[0]);
	}
	
	//============================================================
	//====================GETTERS AND SETTERS=====================
	//============================================================
	
	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getZip() {
		return zip;
	}

	public void setZip(Integer zip) {
		this.zip = zip;
	}

	public LatLong getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(LatLong coordinates) {
		this.coordinates = coordinates;
	}


}
