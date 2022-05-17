package com.w2a.testcases;

import java.io.IOException;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.w2a.base.TestBase;


public class BankManagerLoginTest extends TestBase
{
	@Test
	public void loginAsBankManager() throws InterruptedException, IOException
	{
		verifyEquals("abc","abc");
		
		click("bmlBtn_CSS");
		Assert.assertTrue(isElementPresent(By.cssSelector(OR.getProperty("addCustBtn_CSS"))));
		
		//Assert.fail("HardAssertion Failure manually");
	}	
}
