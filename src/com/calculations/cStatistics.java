package com.calculations;

import java.util.ArrayList;
import java.util.Collections;

import com.controllers.cGeneral;

public class cStatistics {
	
	public static double calculateSimpleMovingAverage(ArrayList<Double> tickers){
		double ticker = 0.00;
		for(int i = 0; i < tickers.size(); i++){
			ticker = ticker + tickers.get(i);
		}
		double avg = cGeneral.round(ticker/tickers.size());
		return avg;
	}
	
	public static double calculateSmoothedAverage(ArrayList<Double> tickers){
		double current = 0.00;
		double smoothAvg = 0.00;
		double totalMultipliers = 0.00;
		for(int i = 0; i < tickers.size(); i++){
			current = tickers.get(i) * (tickers.size() - i);
			smoothAvg = smoothAvg + current;
			totalMultipliers = totalMultipliers + (tickers.size() -  i);
		}
		double avg = cGeneral.round(smoothAvg/totalMultipliers);
		return avg;
	}
	
	public static double calculateSmoothedStandardDeviation(ArrayList<Double> tickers){
		Collections.reverse(tickers);		
		double smoothAvg = calculateSmoothedAverage(tickers);
		double deviationSquareSum = 0.00;
		for(int i = 0; i < tickers.size(); i++){
			double deviationSquared 
				= (tickers.get(i) - smoothAvg) * (tickers.get(i) - smoothAvg);
			deviationSquareSum = deviationSquareSum + deviationSquared;
		}
		double standardDev = deviationSquareSum / (tickers.size() - 1);
		standardDev = cGeneral.round(Math.sqrt(standardDev));
		return standardDev;		
	}
	
	public static double calculateZScore(double standardDeviation, double sma, double latestTrade){
		double zscore = (latestTrade - sma) / standardDeviation;
		zscore = cGeneral.round(zscore);
		return zscore;
	}
}
