package com.main;

import java.io.IOException;
import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.controllers.cDate;
import com.controllers.cGeneral;
import com.etrade.eRequest;
import com.etrade.etws.sdk.client.ClientRequest;
import com.etrade.etws.sdk.common.ETWSException;
import com.objects.oAnalysis;
import com.objects.oSell;
import com.objects.oWarehouse;
import com.repositories.rMySql;

public class mThread {

	public static Test runDataWarehouseThread(ClientRequest request) throws IOException, ETWSException{
		TestSuite suite = new TestSuite();
	  	Integer thread = 1;
		oWarehouse bean = new oWarehouse();		
		bean.setCompany(rMySql.fetchSymbols());
		do {
			cGeneral.fetchPricing(bean, request);
			rMySql.storePricing(bean);	
			cDate.pauseOneMinute(thread);
		} while (cDate.dateAndTimeAreValid());		
		return suite;
	}
	
	public static Test runDataAnalysisThread(ClientRequest request){
		TestSuite suite = new TestSuite();
		oAnalysis aBean = new oAnalysis();
	  	Integer thread = 2;
	  	
		ArrayList<String> symbols = rMySql.fetchSymbols();
		
		do {
			for(int i= 0; i < symbols.size(); i++) {
				aBean = cGeneral.setAccountValues(request);
				if(cGeneral.accountHasValidFunds(aBean)) {				
					cGeneral.runPatterns(aBean, symbols.get(i));
					if(cGeneral.patternShowsBuySignal(aBean.getPatterns())) {
						aBean.setActualBuy(
								cGeneral.addLastTrade(request, symbols.get(i)));
						eRequest.placeEquityOrder(aBean, request);
					}
				}
			}
			cDate.pauseTenMinutes(thread);				
		} while (cDate.dateAndTimeAreValid());		
		return suite;
	}
	
	public static Test runSellEquityThread(ClientRequest request){
		TestSuite suite = new TestSuite();
	  	Integer thread = 3;
	  			
		do {
			ArrayList<oSell> beanList = new ArrayList<oSell>();
		  	beanList = rMySql.fetchAllCurrentOrders();
			for(int i= 0; i < beanList.size(); i++) {
				beanList.get(i).setLatestTrade(
						cGeneral.addLastTrade(request, beanList.get(i).getSymbol()));
				if(cGeneral.sellSignal(beanList.get(i))){
					eRequest.sellEquityOrder(beanList.get(i), request);
				}
			}
			cDate.pauseOneMinute(thread);				
		} while (cDate.dateAndTimeAreValid());		
		return suite;
	}
}
