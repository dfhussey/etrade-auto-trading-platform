package com.test;

import java.util.Random;

public class TestData {

	public static double fetchRandomPrice(){
		Random ran = new Random();
		double x = ran.nextDouble() + 25.00;
		return x;
	}
}
