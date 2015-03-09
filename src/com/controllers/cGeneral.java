package com.controllers;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


import com.analysis.aOne;
import com.etrade.eRequest;
import com.etrade.etws.account.AccountBalanceResponse;
import com.etrade.etws.market.QuoteResponse;
import com.etrade.etws.sdk.client.ClientRequest;
import com.etrade.etws.sdk.common.ETWSException;
import com.objects.oAnalysis;
import com.objects.oPattern;
import com.objects.oSell;
import com.objects.oWarehouse;
import com.test.TestData;

public class cGeneral {

	public static void runPatterns(oAnalysis aBean, String symbol) {
		aBean.setPatterns(new ArrayList<oPattern>());
		aOne.runPattern(aBean, symbol);
		filterToHighestProbabilityPattern(aBean);
	}
	
	public static oAnalysis setAccountValues(ClientRequest request){
		AccountBalanceResponse balance = eRequest.getAccount(request);
		oAnalysis aBean = new oAnalysis();
		aBean.setAccountValue(balance.getAccountBalance().getNetAccountValue().intValue());
		aBean.setMinimumValue(4000);
		aBean.setCashAvailable(balance.getAccountBalance().getCashAvailableForWithdrawal().intValue());
		aBean.setAmtAvailPerTrade(aBean.getAccountValue() / 10);
		aBean.setAccountId(balance.getAccountId());
		System.out.println("account value: "+aBean.getAccountValue());
		return aBean;
	}
	
	public static void fetchPricing(oWarehouse bean, ClientRequest request) throws IOException, ETWSException {
		ArrayList<Timestamp> time = new ArrayList<Timestamp>();
		ArrayList<Double> price = new ArrayList<Double>();
		
		QuoteResponse response = eRequest.fetchPrices(request, bean.getCompany());
		
		for(int i = 0; i < bean.getCompany().size(); i++) {
			//price.add(response.getQuoteData().get(i).getFundamental().getLastTrade());
			price.add(TestData.fetchRandomPrice());
			Calendar cal = GregorianCalendar.getInstance();
			time.add(new Timestamp(cal.getTimeInMillis()));
		}
		bean.setTime(time);
		bean.setPrice(price);
	}
	
	public static void filterToHighestProbabilityPattern(oAnalysis aBean){
		Integer highestPattern = 0;
		for(int i = 0; i < aBean.getPatterns().size(); i++){
			double probability = aBean.getPatterns().get(i).getProbability();
			if(probability >= aBean.getPatterns().get(highestPattern).getProbability()) {
				highestPattern = i;
			} else {
				aBean.getPatterns().remove(i);
			}
		}
	}
	
	public static void addPatternBasedOnProbability(oAnalysis aBean, oPattern pBean){
		if(pBean.getProbability() > 50.00){
			aBean.getPatterns().add(pBean);
		}
	}
	
	public static boolean accountHasValidFunds(oAnalysis aBean) {
		if((aBean.getAccountValue() > aBean.getMinimumValue()) 
				&& aBean.getCashAvailable() > aBean.getAmtAvailPerTrade()){
			return true;
		} 
		return false;
	}
	
	public static boolean patternShowsBuySignal(ArrayList<oPattern> pBean){
		if(pBean.size() > 0){
			return true;
		}
		return false;
	}
	
	public static double addLastTrade(ClientRequest request, String symbol){
		ArrayList<String> list = new ArrayList<String>();
		list.add(symbol);
		QuoteResponse response = eRequest.fetchPrices(request, list);
		return response.getQuoteData().get(0).getFundamental().getLastTrade();		
	}
	
	public static boolean sellSignal(oSell sBean){
		System.out.println("checking sell signal");
		if((sBean.getLatestTrade() >= sBean.getStopGain()) 
				|| (sBean.getLatestTrade() <= sBean.getStopLoss())){
			return true;
		}
		return false;
	}
	
	public static double round(double value) {
		DecimalFormat df = new DecimalFormat("#.##");
		try {
			String shortenedDouble = String.valueOf(value);
			if(shortenedDouble.length() > 5)
				shortenedDouble = shortenedDouble.substring(0,5);
			value = Double.valueOf(shortenedDouble);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Double.valueOf(df.format(value));
	}
	
}
