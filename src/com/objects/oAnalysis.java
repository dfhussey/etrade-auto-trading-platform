package com.objects;

import java.util.ArrayList;


public class oAnalysis {
		
	private Integer accountValue;
	private Integer minimumValue;
	private Integer cashAvailable;
	private Integer amtAvailPerTrade;
	private String accountId;
	private String orderId;
	private double actualBuy;
	private ArrayList<oPattern> patterns;

	public ArrayList<oPattern> getPatterns() {
		return patterns;
	}
	public void setPatterns(ArrayList<oPattern> patterns) {
		this.patterns = patterns;
	}
	public Integer getAccountValue() {
		return accountValue;
	}
	public void setAccountValue(Integer accountValue) {
		this.accountValue = accountValue;
	}
	public Integer getMinimumValue() {
		return minimumValue;
	}
	public void setMinimumValue(Integer minimumValue) {
		this.minimumValue = minimumValue;
	}
	public Integer getCashAvailable() {
		return cashAvailable;
	}
	public void setCashAvailable(Integer cashAvailable) {
		this.cashAvailable = cashAvailable;
	}
	public Integer getAmtAvailPerTrade() {
		return amtAvailPerTrade;
	}
	public void setAmtAvailPerTrade(Integer amtAvailPerTrade) {
		this.amtAvailPerTrade = amtAvailPerTrade;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public double getActualBuy() {
		return actualBuy;
	}
	public void setActualBuy(double actualBuy) {
		this.actualBuy = actualBuy;
	}
	
}
