package com.w2a.testcases;

import java.util.Hashtable;

import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.w2a.base.TestBase;
import com.w2a.utilities.TestUtils;

public class AddCustomerTest extends TestBase
{
	@Test(dataProviderClass=TestUtils.class,dataProvider="dp")
	public void addCustomer(Hashtable <String,String> data) throws InterruptedException
												//	public void addCustomer(String firstName, String lastName,String postCode,String alertText,String runmode) throws InterruptedException
	{
		if(!data.get("runmode").equals("Y"))      		//if(!runmode.equals("Y"))	
		{				
			throw new SkipException("Skipping the test case as the run mode for data is set to NO");
		}
		
		click("addCustBtn_CSS");
		type("firstName_CSS",data.get("firstName"));	//type("firstName_CSS",firstName);
		type("lastName_XPATH",data.get("lastName"));
		type("postCode_CSS",data.get("postCode"));	
		click("submit_CSS");
	
		Alert alert =  wait.until(ExpectedConditions.alertIsPresent());
		Assert.assertTrue(alert.getText().contains(data.get("alertText")));
		alert.accept();
		
		
	}
}
