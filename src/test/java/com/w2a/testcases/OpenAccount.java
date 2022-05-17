package com.w2a.testcases;

import java.util.Hashtable;

import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.w2a.base.TestBase;
import com.w2a.utilities.TestUtils;

public class OpenAccount extends TestBase
{
	@Test(dataProviderClass=TestUtils.class,dataProvider="dp")
	
	public void openAccount(Hashtable<String,String> data) throws InterruptedException
	
	//	public void openAccount(String customer, String currency) throws InterruptedException
	{
		//runmode=Y
		
		if(!(TestUtils.isTestRunnable("openAccount",excel)))
		{
			throw new SkipException("Skipping the test : "+"openAccount" +" as the Runmode is NO");
		}
		
		click("openaccount_XPATH");
		select("customer_XPATH",data.get("customer"));		//select("customer_XPATH",customer);
		select("currency_XPATH",data.get("currency"));
		click("process_XPATH");
		
		
		Alert alert =  wait.until(ExpectedConditions.alertIsPresent());
		alert.accept();
	
	}
}
