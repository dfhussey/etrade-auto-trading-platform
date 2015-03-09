package com.objects;

import java.sql.Timestamp;
import java.util.ArrayList;

public class oWarehouse {
		
	private ArrayList<Timestamp> time;
	private ArrayList<Double> price;
	private ArrayList<String> company;
	
	
	public ArrayList<Timestamp> getTime() {
		return time;
	}
	public void setTime(ArrayList<Timestamp> time) {
		this.time = time;
	}
	public ArrayList<Double> getPrice() {
		return price;
	}
	public void setPrice(ArrayList<Double> price) {
		this.price = price;
	}
	public ArrayList<String> getCompany() {
		return company;
	}
	public void setCompany(ArrayList<String> company) {
		this.company = company;
	}
	
}
