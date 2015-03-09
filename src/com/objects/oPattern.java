package com.objects;


public class oPattern {
		
	private String symbol;
	private double probability;
	private double stopLoss;
	private double stopGain;
	private String pattern;
	private String quantity;

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
	public double getProbability() {
		return probability;
	}
	public void setProbability(double probability) {
		this.probability = probability;
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
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}	

}
