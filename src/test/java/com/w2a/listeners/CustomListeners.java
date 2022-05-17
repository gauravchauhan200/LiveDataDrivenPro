package com.w2a.listeners;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.relevantcodes.extentreports.LogStatus;
import com.w2a.base.TestBase;
import com.w2a.utilities.MonitoringMail;
import com.w2a.utilities.TestConfig;
import com.w2a.utilities.TestUtils;

public class CustomListeners extends TestBase implements ITestListener,ISuiteListener
{
	String messageBody;

	public void onTestStart(ITestResult result) 
	{
		test = rep.startTest(result.getName().toUpperCase());			//.getName used to get method name	
	
	}

	public void onTestSuccess(ITestResult result) 
	{
			test.log(LogStatus.PASS, result.getName().toUpperCase()+"PASS");	//ExtentReporting
			rep.endTest(test);
			rep.flush();      													 //flushing  to generate Extentreport
	}

	public void onTestFailure(ITestResult result) 
	{
			System.setProperty("org.uncommons.reportng.escape-output","false");
			try {
			TestUtils.captureScreenshot();}
			catch (IOException e) {
			e.printStackTrace();}
		
			test.log(LogStatus.FAIL, result.getName().toUpperCase()+"FAILED WITH EXCEPTION : "+result.getThrowable()); //Extent reporting	
			test.log(LogStatus.FAIL, test.addScreenCapture(TestUtils.screenshotName));		//extent
		
			Reporter.log("<a href="+TestUtils.screenshotName+">ScreenShot</a>");	
			Reporter.log("<br>"); 													//-space-new tab - image icon added
			Reporter.log("<a target=\"_blank\" href="+TestUtils.screenshotName+"><img src="+TestUtils.screenshotName+" height=200 width=200></img></a>");
	
			rep.endTest(test);
			rep.flush();
	}

	public void onTestSkipped(ITestResult result) 
	{
		
		test.log(LogStatus.SKIP, result.getName().toUpperCase()+" Skipped the test as the run mode is set to NO");
		rep.endTest(test);
		rep.flush();

	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		
	
	}

	public void onTestFailedWithTimeout(ITestResult result) {
		
		
	}

	public void onStart(ITestContext context) {
		
		
	}

	public void onFinish(ITestContext context) 
	{
		MonitoringMail mail = new MonitoringMail();	
		
		try {
			messageBody = "http://"+ InetAddress.getLocalHost().getHostAddress()+":8080/job/DataDrivenLiveProject/Extent_20Report/";
			
			} catch (UnknownHostException e) {e.printStackTrace();}
	
		
		try {
			mail.sendMail(TestConfig.server, TestConfig.from, TestConfig.to, TestConfig.subject, messageBody);
			} catch (AddressException e) {e.printStackTrace();} 
		
		
		catch (MessagingException e) {e.printStackTrace();}
	
		
	}

}
