package com.objects;


public class oSell {
		
	private String symbol;
	private String accountId;
	private String orderId;
	private double stopLoss;
	private double stopGain;
	private double actualBuy;
	private String quantity;
	private double latestTrade;
	
	
	public double getLatestTrade() {
		return latestTrade;
	}
	public void setLatestTrade(double latestTrade) {
		this.latestTrade = latestTrade;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
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
	public double getStopLoss() {
		return stopLoss;
	}
	public void setStopLoss(double stopLoss) {
		this.stopLoss = stopLoss;
	}
	public double getStopGain() {
		return stopGain;
	}
	public void setStopGain(double stopGain) {
		this.stopGain = stopGain;
	}
	public double getActualBuy() {
		return actualBuy;
	}
	public void setActualBuy(double actualBuy) {
		this.actualBuy = actualBuy;
	}
	

}
