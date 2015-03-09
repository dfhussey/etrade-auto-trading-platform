package com.repositories;

import java.sql.ResultSet;
import java.util.ArrayList;

import com.controllers.cGeneral;
import com.mysql.jdbc.Statement;
import com.objects.oAnalysis;
import com.objects.oPattern;
import com.objects.oSell;
import com.objects.oWarehouse;

public class rMySql {
	
	public static ArrayList<String> fetchSymbols() {
		ArrayList<String> list = new ArrayList<String>();
		Statement stmt = rConnection.fetchStatement();
		
	    try {
	        String query = "SELECT DISTINCT symbolscol FROM db2.symbols ";
	        try {
	            ResultSet rs = stmt.executeQuery(query);
	            while (rs.next()) {
	            	list.add(rs.getString("symbolscol"));
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return list;
	}
	
	public static void storePricing(oWarehouse bean) {
		Statement stmt = rConnection.fetchStatement();

		for(int i = 0; i < bean.getCompany().size(); i++){
		    try {
		        String query = "INSERT INTO db2."+bean.getCompany().get(i)+" (PRICE, TIME) " +
		        		"VALUES ("+bean.getPrice().get(i)+",'"+bean.getTime().get(i)+"')";
		        try {
		        	System.out.println(query);
		            stmt.executeUpdate(query);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
	}
	
	public static String setNextOrderId(oAnalysis aBean) {
		Statement stmt = rConnection.fetchStatement();
		
	    try {
	        String query = "SELECT orderId FROM db2.transactions order by orderId DESC limit 1 ";
	        try {
	            ResultSet rs = stmt.executeQuery(query);
	            while (rs.next()) {
	            	Integer currentOrderId = Integer.valueOf(rs.getString("orderId"));
	            	Integer newOrderId = currentOrderId + 1;
	            	aBean.setOrderId(newOrderId.toString());
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    if(aBean.getOrderId() == null){
	    	aBean.setOrderId("1");
	    }
	    return aBean.getOrderId();
	}
	
	public static void insertNewTransaction(oAnalysis aBean) {
		Statement stmt = rConnection.fetchStatement();
		oPattern pBean = new oPattern();
		pBean = aBean.getPatterns().get(0);
		
	    try {
	        String query = "INSERT INTO db2.transactions (symbol, transTime, probability," +
	        		"active, pattern, orderId, stopLoss, stopGain, actualBuy, quantity) VALUES ('"+
	        		pBean.getSymbol()+"',CURRENT_TIMESTAMP,"+pBean.getProbability()+","+
	        		"1,'"+pBean.getPattern()+"','"+aBean.getOrderId()+"',"+pBean.getStopLoss()+","
	        		+ pBean.getStopGain()+","+aBean.getActualBuy()+","+pBean.getQuantity()+")";
	        try {
	        	System.out.println(query);
	            stmt.executeUpdate(query);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public static ArrayList<oSell> fetchAllCurrentOrders() {
		Statement stmt = rConnection.fetchStatement();
		ArrayList<oSell> beanList = new ArrayList<oSell>();
		
	    try {
	        String query = "SELECT symbol, stopLoss, stopGain, orderId, quantity " +
	        		" FROM db2.transactions where active = 1 ";
	        try {
	        	System.out.println(query);
	            ResultSet rs = stmt.executeQuery(query);
	            while (rs.next()) {
	        		oSell bean = new oSell();
	            	bean.setSymbol(rs.getString("symbol"));
	            	bean.setStopLoss(rs.getDouble("stopLoss"));
	            	bean.setStopGain(rs.getDouble("stopGain"));
	            	bean.setOrderId(rs.getString("orderId"));
	            	bean.setQuantity(rs.getString("quantity"));
	            	beanList.add(bean);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        } 
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return beanList;
	}
	
	public static void updateSoldEquity(oSell sBean) {
		Statement stmt = rConnection.fetchStatement();
	    try {
	        String query = "UPDATE DB2.TRANSACTIONS set active = 0, " +
	        		"actualSell = "+sBean.getLatestTrade()+", sellTime = Current_timestamp "+
	        		"where orderId = '"+sBean.getOrderId()+"' ";
	        try {
	        	System.out.println(query);
	            stmt.executeUpdate(query);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public static double fetchPScore(double zscore) {
		Statement stmt = rConnection.fetchStatement();
		double pvalue = 0.00;
		
	    try {
	        String query = "SELECT pvalue FROM db2.ptable where zscore = "+zscore;
	        try {
	            ResultSet rs = stmt.executeQuery(query);
	            while (rs.next()) {
	            	pvalue = rs.getDouble("pvalue");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    pvalue = cGeneral.round(pvalue);
	    return pvalue;
	}
	
}
