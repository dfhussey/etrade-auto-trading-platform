package com.etrade;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import com.etrade.etws.account.Account;
import com.etrade.etws.account.AccountBalanceResponse;
import com.etrade.etws.account.AccountListResponse;
import com.etrade.etws.market.DetailFlag;
import com.etrade.etws.market.QuoteResponse;
import com.etrade.etws.oauth.sdk.client.IOAuthClient;
import com.etrade.etws.oauth.sdk.client.OAuthClientImpl;
import com.etrade.etws.oauth.sdk.common.Token;
import com.etrade.etws.order.EquityOrderAction;
import com.etrade.etws.order.EquityOrderRequest;
import com.etrade.etws.order.EquityOrderRoutingDestination;
import com.etrade.etws.order.EquityOrderTerm;
import com.etrade.etws.order.EquityPriceType;
import com.etrade.etws.order.MarketSession;
import com.etrade.etws.order.PlaceEquityOrder;
import com.etrade.etws.order.PlaceEquityOrderResponse;
import com.etrade.etws.sdk.client.AccountsClient;
import com.etrade.etws.sdk.client.ClientRequest;
import com.etrade.etws.sdk.client.Environment;
import com.etrade.etws.sdk.client.MarketClient;
import com.etrade.etws.sdk.client.OrderClient;
import com.objects.oAnalysis;
import com.objects.oSell;
import com.repositories.rMySql;

public class eRequest {

	//SANDBOX
	public static Environment environment = Environment.SANDBOX;
	public static String oauth_consumer_key = "b5feceaf63044571622f4469bdb90db7";
	public static String oauth_consumer_secret = "a760838266843cc0cd36bf658bfe76ab";
	
	//PRODUCTION
	//public static Environment environment = Environment.LIVE;
	//public static String oauth_consumer_key = "dcbebe71d8ad0bac6af3c9f536d38c62";
	//public static String oauth_consumer_secret = "c4c26b1eed2bb8e583f4617b2e6ed0ca";
	
	public static IOAuthClient client = null;
	public static ClientRequest request = null;
	public static Token token = null;
	public static String oauth_request_token = null;
	public static String oauth_request_token_secret = null;
	public static String oauth_access_token = null;
	public static String oauth_access_token_secret = null;
	public static String oauth_verify_code = "";

	public static ClientRequest authorizeApp() {
		ClientRequest request = setRequest();
		AuthorizeApplication(request);
		fetchAccessCode();
		return request;
	}

	public static ClientRequest setRequest() {
		try {
			client = OAuthClientImpl.getInstance();
			request = new ClientRequest();
			request.setEnv(Environment.SANDBOX);
			request.setConsumerKey(oauth_consumer_key);
			request.setConsumerSecret(oauth_consumer_secret);
			token = client.getRequestToken(request);
			request.setToken(token.getToken());
			oauth_request_token = token.getToken();
			oauth_request_token_secret = token.getSecret();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return request;
	}

	public static void AuthorizeApplication(ClientRequest request) {
		try {
			String authorizeURL = null;
			authorizeURL = client.getAuthorizeUrl(request);
			System.out.println(authorizeURL);
			JOptionPane.showMessageDialog(null, authorizeURL);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void fetchAccessCode() {
		// code is good until midnight EST
		try {
			request = new ClientRequest();
			request.setEnv(Environment.SANDBOX);
			request.setConsumerKey(oauth_consumer_key);
			request.setConsumerSecret(oauth_consumer_secret);
			request.setToken(oauth_request_token);
			request.setTokenSecret(oauth_request_token_secret);
			String verifyCode = JOptionPane.showInputDialog("Verification");
			oauth_verify_code = verifyCode;
			request.setVerifierCode(oauth_verify_code);
			token = client.getAccessToken(request); 
			oauth_access_token = token.getToken();
			oauth_access_token_secret = token.getSecret();
			System.out.println("Access token: " + token.getToken());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static QuoteResponse fetchPrices(ClientRequest request, ArrayList<String> list) {
		QuoteResponse response = null;
		try {
			request.setToken(oauth_access_token);
			request.setTokenSecret(oauth_access_token_secret);
			request.setEnv(Environment.SANDBOX);			
			MarketClient client = new MarketClient(request);
			response = client.getQuote(list, true, DetailFlag.FUNDAMENTAL);
		} catch (Exception ex){
			ex.printStackTrace();
		}
		return response;
	}
	
	public static AccountBalanceResponse getAccount(ClientRequest request){
		AccountBalanceResponse balance = null;
		Account a = null;
		try {
			AccountsClient client = new AccountsClient(request);
			AccountListResponse response = client.getAccountList();
			List<Account> alist = response.getResponse();
			Iterator<Account> al = alist.iterator();
			while (al.hasNext()) {
			     a = al.next();
			     System.out.println("Account: " + a.getAccountId());
			     balance = client.getAccountBalance(a.getAccountId());
			     System.out.println("Balance: "+balance.getAccountBalance().getNetAccountValue());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return balance;
	}
	
	public static void placeEquityOrder(oAnalysis aBean, ClientRequest request){
		OrderClient client = null;
		try {
			client = new OrderClient(request);			
			PlaceEquityOrder orderRequest = new PlaceEquityOrder(); 
			EquityOrderRequest eor = new EquityOrderRequest(); 
			eor.setAccountId(aBean.getAccountId()); 
			eor.setSymbol(aBean.getPatterns().get(0).getSymbol());
			eor.setAllOrNone("FALSE"); 
			eor.setClientOrderId(rMySql.setNextOrderId(aBean)); 
			eor.setOrderTerm(EquityOrderTerm.GOOD_FOR_DAY); 
			eor.setOrderAction(EquityOrderAction.BUY); 
			eor.setMarketSession(MarketSession.REGULAR); 
			eor.setPriceType(EquityPriceType.MARKET);
			eor.setQuantity(new BigInteger(aBean.getPatterns().get(0).getQuantity()));
			eor.setRoutingDestination(EquityOrderRoutingDestination.AUTO.value()); 
			eor.setReserveOrder("FALSE"); 
			orderRequest.setEquityOrderRequest(eor); 
			PlaceEquityOrderResponse response = client.placeEquityOrder(orderRequest);
			
			System.out.println("AMT: "+response.getEquityOrderResponse().getEstimatedTotalAmount());
			rMySql.insertNewTransaction(aBean);
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void sellEquityOrder(oSell sBean, ClientRequest request){
		OrderClient client = null;

		AccountBalanceResponse balance = getAccount(request);
		try {
			client = new OrderClient(request);			
			PlaceEquityOrder orderRequest = new PlaceEquityOrder(); 
			EquityOrderRequest eor = new EquityOrderRequest(); 
			eor.setAccountId(balance.getAccountId()); 
			eor.setSymbol(sBean.getSymbol());
			eor.setAllOrNone("FALSE"); 
			eor.setClientOrderId(sBean.getOrderId()); 
			eor.setOrderTerm(EquityOrderTerm.GOOD_FOR_DAY); 
			eor.setOrderAction(EquityOrderAction.SELL); 
			eor.setMarketSession(MarketSession.REGULAR); 
			eor.setPriceType(EquityPriceType.MARKET);
			eor.setQuantity(new BigInteger(sBean.getQuantity()));
			eor.setRoutingDestination(EquityOrderRoutingDestination.AUTO.value()); 
			eor.setReserveOrder("FALSE"); 
			orderRequest.setEquityOrderRequest(eor); 
			PlaceEquityOrderResponse response = client.placeEquityOrder(orderRequest);
			rMySql.updateSoldEquity(sBean);
			System.out.println("AMT: "+response.getEquityOrderResponse().getEstimatedTotalAmount());
			
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}

}
