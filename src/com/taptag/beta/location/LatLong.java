package com.taptag.beta.location;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LatLong implements Serializable {

	private Double lat;
	private Double lng;
	
	public LatLong() {
		lat = 0.0;
		lng = 0.0;
	}
	
	public LatLong(Double lat, Double lng) {
		this.lat = lat;
		this.lng = lng;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}
	
	public Double[] toArray() {
		Double[] result = new Double[2];
		result[0] = lat;
		result [1] = lng;
		return result;
	}
	
}
