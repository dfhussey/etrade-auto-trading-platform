package com.repositories;

import java.sql.Connection;
import java.sql.DriverManager;

import com.mysql.jdbc.Statement;

public class rConnection {

	public static Statement fetchStatement() {
		Statement stmt = null;
	    Connection con = null;
	    try {
	        Class.forName("com.mysql.jdbc.Driver").newInstance();
	        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "102189");
	        
	        try {
	            stmt = (Statement) con.createStatement();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		return stmt;
	}
	
}
