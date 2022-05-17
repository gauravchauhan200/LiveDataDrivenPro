package com.w2a.utilities;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Hashtable;

import org.apache.maven.shared.utils.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.DataProvider;

import com.w2a.base.TestBase;

public class TestUtils extends TestBase
{
	public static String screenShotPath;
	public static String screenshotName;
	
	public static void 	captureScreenshot() throws IOException
	{
		Date d = new Date();
		screenshotName=d.toString().replace(":", "_").replace(" ", "_")+".jpg";
		
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);	
		File destFile = new  File(System.getProperty("user.dir")+"\\target\\surefire-reports\\html\\"+screenshotName+"");
		FileUtils.copyFile(scrFile, destFile);
	}
	
	@DataProvider(name="dp")				//Common data Provider
	public Object[][] getData(Method m)		//will get method name
	{
		String sheetName= m.getName();		//will provide the name of method which is same as sheet name
		int rows=excel.getRowCount(sheetName);
		int cols=excel.getColumnCount(sheetName);
		
		Object data[][] = new Object[rows-1][1];
		
		Hashtable<String,String> table = null;     // added because hashtable added to testcases
		
		
		for(int rowNum=2; rowNum<=rows; rowNum++)
		{
			table=new Hashtable<String,String>();
			
			for(int colNum = 0;colNum < cols;colNum++)
			{				
									//first parameter get filled like name,alert,postcode// 1 is for keyvalues row
									//second parameter will increament the row only one by one
									//data[0][0]
									//data[rowNum-2][colNum] = excel.getCellData(sheetName,colNum,rowNum);
				
				
				table.put(excel.getCellData(sheetName,colNum, 1), excel.getCellData(sheetName, colNum, rowNum));
				data[rowNum-2][0]=table;			//method accepts only 1 column so Zero
			}
		}
		return data;
	}
	



	public static boolean isTestRunnable(String testName, ExcelReader excel)
	{
		String sheetName="test_suite";
		int rows = excel.getRowCount("test_suite");
	
		for(int rNum=2;rNum<=rows;rNum++)
		{
			String testCase= excel.getCellData(sheetName,"TCID",rNum);
			
			if(testCase.equalsIgnoreCase(testName))
			{
				String runmode = excel.getCellData(sheetName, "Runmode", rNum);
				if(runmode.equalsIgnoreCase("Y"))
				
					return true;
					else
					return false;	
			}
		}
		return false;
	}

}
