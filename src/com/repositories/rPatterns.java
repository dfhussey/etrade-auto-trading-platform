package com.repositories;

import java.sql.ResultSet;

import com.mysql.jdbc.Statement;

public class rPatterns {
	
	public static ResultSet fetchAllTodaysResults (String symbol) {
		Statement stmt = rConnection.fetchStatement();
		ResultSet rs = null;
	    try {
	        String query = "SELECT * FROM db2."+symbol+" order by time desc "; //where time > CURDATE()
	        try {
	            rs = stmt.executeQuery(query);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return rs;
	}
	
}
