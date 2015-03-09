package com.main;

import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.controllers.cDate;
import com.etrade.eRequest;
import com.etrade.etws.sdk.client.ClientRequest;
import com.etrade.etws.sdk.common.ETWSException;

public class mMain {

	public static ClientRequest request = null;

	public static void main(String[]args) throws IOException, ETWSException{
		junit.textui.TestRunner.run(suite());
	}
	
	public static Test suite() throws IOException, ETWSException{
		TestSuite suite = new TestSuite();
		cDate.checkIfApplicationIsWithinHours();
		request = eRequest.authorizeApp();

		Thread thread1 = new Thread () {
			  public void run () {
					System.out.println("starting thread 1");
					try {
						mThread.runDataWarehouseThread(request);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ETWSException e) {
						e.printStackTrace();
					}
			  }
		};
		Thread thread2 = new Thread () {
			  public void run ()  {
				  	Integer thread = 2;
					System.out.println("starting thread 2");
					cDate.pauseOneMinute(thread);
					mThread.runDataAnalysisThread(request);
			  }
		};
		Thread thread3 = new Thread () {
			  public void run ()  {
				  	Integer thread = 3;
					System.out.println("starting thread 3");
					cDate.pauseTwoMinutes(thread);
					mThread.runSellEquityThread(request);
			  }
		};
		thread1.start();
		thread2.start();
		thread3.start();
		return suite;
	}
	
	
}
