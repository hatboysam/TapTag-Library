package com.taptag.beta.redemption;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

import com.taptag.beta.reward.Reward;

@SuppressWarnings("serial")
public class Redemption implements Serializable {

	@JsonProperty("user_id")
	private int userId;
	@JsonProperty("company_id")
	private int companyId;
	@JsonProperty("vendor_id")
	private int vendorId;
	@JsonProperty("reward_id")
	private int rewardId;
	@JsonProperty("redeemed_date")
	private Date redeemedDate;
	private int taps;
	
	public Redemption() {
	}
	
	public Redemption (int userId, int vendorId, Reward reward) {
		this.userId = userId;
		this.companyId = reward.getCompany_id();
		this.vendorId = vendorId;
		this.rewardId = reward.getId();
		this.taps = reward.getTotal();
		this.redeemedDate = new Date();
	}

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public int getVendorId() {
		return vendorId;
	}
	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}

	public int getRewardId() {
		return rewardId;
	}
	public void setRewardId(int rewardId) {
		this.rewardId = rewardId;
	}

	public Date getRedeemedDate() {
		return redeemedDate;
	}
	public void setRedeemedDate(Date redeemedDate) {
		this.redeemedDate = redeemedDate;
	}

	public int getTaps() {
		return taps;
	}
	public void setTaps(int taps) {
		this.taps = taps;
	}
	
}
