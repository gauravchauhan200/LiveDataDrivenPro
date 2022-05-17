package com.w2a.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.w2a.utilities.ExcelReader;
import com.w2a.utilities.ExtentManager;
import com.w2a.utilities.TestLogs;
import com.w2a.utilities.TestUtils;


public class TestBase 
{
/*
 * WebDriver
 * Properties
 * Logs
 * ExtentReports
 * DB
 * Excel
 * Mail
 */
	
	public static WebDriverWait wait;
	public static WebDriver driver;
	public static Properties config = new Properties();
	public static Properties OR = new Properties();
	public static FileInputStream fis;
	public static ExcelReader excel = new ExcelReader(System.getProperty("user.dir")+"\\src\\test\\resources\\excel\\testdata.xlsx");
	public static Logger log = Logger.getLogger(TestLogs.class.getName());
	public ExtentReports rep =  ExtentManager.getInstance();
	public static ExtentTest test;
	public static String browser;
	
	
	
	
	@BeforeSuite
	public void setUp() throws IOException
	{
		Date d = new Date();
		System.out.println(d.toString().replace(":", "_").replace(" ", "_"));
		System.setProperty("current.date", d.toString().replace(":", "_").replace(" ", "_"));
		PropertyConfigurator.configure("./src/test/resources/properties/log4j.properties");
		
		if(driver==null)
		{
		fis= new FileInputStream(System.getProperty("user.dir")+"\\src\\test\\resources\\properties\\Config.properties");
		config.load(fis);
				
		fis= new FileInputStream(System.getProperty("user.dir")+"\\src\\test\\resources\\properties\\OR.properties");
		OR.load(fis);
		
			//code for jenkins browser choice
			
			if(System.getenv("browser")!=null && !System.getenv("browser").isEmpty())
			{
				browser= System.getenv("browser");
			}
			else
			{
				browser = config.getProperty("browser"); 
			}
			config.setProperty("browser",browser);
			
			//till here
		
		
	
		
			if(config.getProperty("browser").equals("firefox"))
			{
			System.setProperty("webdriver.gecko.driver",System.getProperty("user.dir")+"\\src\\test\\resources\\executables\\geckodriver.exe");	
			driver = new FirefoxDriver();
			}
			else if(config.getProperty("browser").equals("chrome"))
			{
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\src\\test\\resources\\executables\\chromedriver.exe");
			driver = new ChromeDriver();
			}
		driver.get(config.getProperty("testsiteurl"));
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Integer.parseInt(config.getProperty("implicit.wait")), TimeUnit.SECONDS);
		wait = new WebDriverWait(driver,5);
		}	
	}
	
	public static void verifyEquals(String expected,String actual) throws IOException
	{
		try {
			Assert.assertEquals(actual,expected);
		}catch(Throwable t)
		{
			TestUtils.captureScreenshot();
			// For reportNG
			Reporter.log("<br>"+"Verification failure : "+t.getMessage()+"<br>");
			Reporter.log("<a target=\"_blank\" href="+TestUtils.screenshotName+"><img src="+TestUtils.screenshotName+" height=200 width=200></img></a>");
		
			// For ExtentReport
			test.log(LogStatus.FAIL, "Verification failed with exception : "+t.getMessage());	
			test.log(LogStatus.FAIL, test.addScreenCapture(TestUtils.screenshotName));		
		
		
		}
	}
	
	
	public void click(String locator)
	{
		if(locator.endsWith("_CSS"))
		{driver.findElement(By.cssSelector(OR.getProperty(locator))).click();}
		
		else if(locator.endsWith("_XPATH"))
		{driver.findElement(By.xpath(OR.getProperty(locator))).click();}
		
		else if(locator.endsWith("_ID"))
		{driver.findElement(By.id(OR.getProperty(locator))).click();}
		
		test.log(LogStatus.INFO,"Clicking on : " +locator);			
	}
	
	
	
	public void type(String locator, String value)
	{
		if(locator.endsWith("_CSS"))
		{driver.findElement(By.cssSelector(OR.getProperty(locator))).sendKeys(value);}
	
		else if(locator.endsWith("_XPATH"))
		{driver.findElement(By.xpath(OR.getProperty(locator))).sendKeys(value);}
	
		else if(locator.endsWith("_ID"))
		{driver.findElement(By.id(OR.getProperty(locator))).sendKeys(value);}

		
		test.log(LogStatus.INFO, "Typing in : "+locator+" entered values : "+value);
	}
	
	
	
	static WebElement dropdown;
	
	public void select(String locator, String value)
	{
	
		if(locator.endsWith("_CSS"))
		{dropdown=driver.findElement(By.cssSelector(OR.getProperty(locator)));}
	
		else if(locator.endsWith("_XPATH"))
		{dropdown=driver.findElement(By.xpath(OR.getProperty(locator)));}
	
		else if(locator.endsWith("_ID"))
		{dropdown=driver.findElement(By.id(OR.getProperty(locator)));}
	
		Select select =new Select(dropdown);
		select.selectByVisibleText(value);
		
		test.log(LogStatus.INFO,"Selecting from dropdown : "+ locator +"value as"+ value);
		
		
		
	}
	
	
	
	public boolean isElementPresent(By by)
	{
		try {
			driver.findElement(by);
			return true;} 
		catch (NoSuchElementException e) 
			{return false;}
	}
	
	
	
	@AfterSuite()
	public void tearDown()
	{
		if(driver!=null)
		{
			driver.quit();
		}
	}
	
}
