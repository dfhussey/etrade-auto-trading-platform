package com.analysis;

import java.sql.ResultSet;
import java.util.ArrayList;

import com.calculations.cStatistics;
import com.config.cLiterals;
import com.controllers.cGeneral;
import com.objects.oAnalysis;
import com.objects.oPattern;
import com.repositories.rMySql;
import com.repositories.rPatterns;

public class aOne {

	private static ResultSet rs = null;
	private static oPattern pBean;
	private static double latestTrade;;
	private static double simpleMovingAvg;
	private static double smoothedMovingAvg;
	private static double standardDeviation;
	private static double zscore;
	private static double pscore;
	private static ArrayList<Double> tickers;
	
	public static void runPattern(oAnalysis aBean, String symbol){
		
		try {
			rs = rPatterns.fetchAllTodaysResults(symbol);
			tickers = new ArrayList<Double>();
			
			while (rs != null && rs.next()){
				double ticker = rs.getDouble("price");
				if(tickers.isEmpty())
					latestTrade = ticker;
				tickers.add(ticker);
			}
			if(tickers.size() > 0){ 		
				simpleMovingAvg = cStatistics.calculateSimpleMovingAverage(tickers);
				smoothedMovingAvg = cStatistics.calculateSmoothedAverage(tickers);
				standardDeviation = cStatistics.calculateSmoothedStandardDeviation(tickers);
				zscore = cStatistics.calculateZScore(standardDeviation, smoothedMovingAvg, latestTrade);
				pscore = rMySql.fetchPScore(zscore);
			
				System.out.println("sma: "+simpleMovingAvg+" smoothed avg: "+smoothedMovingAvg+" SD: "+standardDeviation
						+" pscore: "+pscore+" zscore: "+zscore);
	
				if(latestTrade < smoothedMovingAvg){
					pBean = new oPattern();
					fetchSellGainAmt(pBean);
					fetchSellLossAmt(pBean);
					pBean.setPattern(cLiterals.exponentiallysmoothedMovingAverage_OneDay);
					pBean.setSymbol(symbol);
					checkBuyPossibility(pBean, aBean);
					cGeneral.addPatternBasedOnProbability(aBean, pBean);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	protected static void fetchSellGainAmt(oPattern pBean){
		pBean.setStopGain(smoothedMovingAvg);
		pBean.setProbability(pscore);
		System.out.println("Chosen Stop Gain: "+pBean.getStopGain());
	}
	
	protected static void fetchSellLossAmt(oPattern pBean){
		double testAmt = latestTrade;	
		boolean stopLossFound = false;
		do {
			testAmt = cGeneral.round(testAmt - .01);	
			double testZscore = cStatistics.calculateZScore(standardDeviation, smoothedMovingAvg, testAmt);
			double testPscore = rMySql.fetchPScore(testZscore);
			if(testPscore < .20){
				pBean.setStopLoss(testAmt);
				stopLossFound = true;
			}
		} while (!stopLossFound);
		System.out.println("Chosen Stop Loss: "+pBean.getStopLoss());
	}
	
	protected static void checkBuyPossibility(oPattern pBean, oAnalysis aBean){
		
		pBean.setQuantity("2");
	}
	
}
