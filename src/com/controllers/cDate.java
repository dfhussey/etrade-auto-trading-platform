package com.controllers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class cDate {
	
	public static void pauseOneMinute(Integer threadNo){
		try {
			System.out.println("Thread sleeping..."+threadNo);
			Thread.sleep(60000);
			System.out.println("Thread waking..."+threadNo);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void pauseTwoMinutes(Integer threadNo){
		try {
			System.out.println("Thread sleeping..."+threadNo);
			Thread.sleep(120000);
			System.out.println("Thread waking..."+threadNo);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void pause30seconds(){
		try {
			System.out.println("Thread sleeping...");
			Thread.sleep(30000);
			System.out.println("Thread waking...");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void pauseTenMinutes(Integer threadNo){
		try {
			System.out.println("Thread sleeping..."+threadNo);
			Thread.sleep(600000);
			System.out.println("Thread waking..."+threadNo);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static boolean dateAndTimeAreValid() {
		boolean valid = true;
	    Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        
        if(dayOfWeek == Calendar.SATURDAY 
        		|| dayOfWeek == Calendar.SUNDAY){
        	valid = false;
        } 
        
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	sdf.setTimeZone(TimeZone.getTimeZone("EST5EDT"));
    	
    	Calendar currentTime = Calendar.getInstance();
    	Calendar endTime = fetchEndTime();
    	Calendar startTime = fetchStartTime();

    	if(!(currentTime.getTime().after(startTime.getTime()) 
    			&& currentTime.getTime().before(endTime.getTime()))) {
        	valid = false;
    	}
		return valid;
	}
	
	private static Calendar fetchStartTime() {
    	Calendar startTime = Calendar.getInstance();
    	startTime.set(Calendar.HOUR_OF_DAY,7);
    	startTime.set(Calendar.MINUTE,00);
    	startTime.set(Calendar.SECOND,0);
    	startTime.set(Calendar.MILLISECOND,0);
    	return startTime;
	}
	
	private static Calendar fetchEndTime() {
    	Calendar endTime = Calendar.getInstance();
    	endTime.set(Calendar.HOUR_OF_DAY,15);
    	endTime.set(Calendar.MINUTE,00);
    	endTime.set(Calendar.SECOND,0);
    	endTime.set(Calendar.MILLISECOND,0);
    	return endTime;
	}
	
	public static boolean checkIfApplicationIsWithinHours(){
		do {
			cDate.pause30seconds();
		} while(!cDate.dateAndTimeAreValid());
		return true;
	}
}
