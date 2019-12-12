package tests.selenium;

import java.util.regex.Pattern;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;
import functions.UFMSFunctions;

import uta_facility_maintenance_system.model.UserDetail;
import uta_facility_maintenance_system.util.SQLConnection;


@RunWith(JUnitParamsRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SeleniumTC03 extends UFMSFunctions {

	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	public String sAppURL, sSharedUIMapPath, testDelay, username, password;
	static SQLConnection DBMgr = SQLConnection.getInstance();

	
	
	
	@Before
	public void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver", "c:/ChromeDriver/chromedriver.exe");
		driver = new ChromeDriver();
		baseUrl = "http://localhost:8080/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		prop = new Properties();
		prop.load(new FileInputStream("./Configuration/UFMS_Configuration.properties"));
		prop.load(new FileInputStream("src/Login/Login.properties"));
		username = prop.getProperty("usernameAdmin");
		password = prop.getProperty("passwordAdmin");
		sAppURL = prop.getProperty("sAppURL");
		sSharedUIMapPath = prop.getProperty("sSharedUIMapPath");
		testDelay = prop.getProperty("testDelay");
		prop.load(new FileInputStream(sSharedUIMapPath));
	}

	
	@Test
	public void verifyAllLinks() throws Exception {
		driver.get(sAppURL);

		UFMS_LoginUser(driver, username, password);
		driver.findElement(By.xpath(prop.getProperty("AdminHomePage_ViewProfile_Lnk"))).click();
		assertEquals("Modify User Details", driver.getTitle());
		driver.findElement(By.xpath(prop.getProperty("ViewProfile_HomePage_Lnk"))).click();
		Thread.sleep(2_000);
		driver.findElement(By.xpath(prop.getProperty("AdminHomePage_LogOutInAdminHomePage_Lnk"))).click();
		Thread.sleep(2_000);
		assertEquals("UTA MAC Facility Maintenance", driver.getTitle());
	}

	@Test
	@FileParameters("src/Excel/TC03a_test_cases.csv")
	public void TC03a(String uname, String errMsg) throws Exception {
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		driver.get(sAppURL);
		UFMS_UserRegistraton(driver, "john123", "John", "Robb", "John@123", "John@123","Admin", "1001999999", "6825641234", "john.robb@uta.edu", "Greek Row, Apt 201", "Arlington", "Texas", "76019" );
		assertEquals("UTA MAC Facility Maintenance", driver.getTitle());
		UFMS_LoginUser(driver, username, password);
		assertEquals("Admin", driver.getTitle());
		MainApp_function(driver, FunctionEnum.adminSearchesUser); // select Search For User from homepage
		// Check with incorrect username
		Thread.sleep(2_000);
		searchUser_function(driver, uname, methodName + " searchUserFunction test case ");
		verifySearchUserErrorMessages(driver, errMsg, methodName + " verifySearchUserErrorMessages");
		Thread.sleep(2_000);
		driver.findElement(By.xpath(prop.getProperty("SearchForUser_HomePage_Lnk"))).click();
		assertEquals("Admin", driver.getTitle()); // go back to homepage
	}
	
	
	@Test
	@FileParameters("src/Excel/TC03b_test_cases.csv")
	public void TC03b(String uname, String ConfirmationMsg) throws Exception {
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		driver.get(sAppURL);
		UFMS_LoginUser(driver, username, password);
		assertEquals("Admin", driver.getTitle());
		MainApp_function(driver, FunctionEnum.adminSearchesUser); // select Search For User from homepage
		// Modify user_role of selected user
		
		searchUser_function(driver, uname, methodName + " userDetailsBeforeChangingUserRole");
		takeScreenshot(driver, methodName + " userDetailsBeforeChangingUserRole");
		Thread.sleep(2_000);
		driver.findElement(By.xpath(prop.getProperty("SearchForUser_ViewBtnToModifyUserDetails_Lnk"))).click();
		assertEquals("Modify Selected User", driver.getTitle());
		
		new Select(driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_UserRoleDropdownOption"))))
				.selectByVisibleText("User");
		
		
		Thread.sleep(2_000);
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_ModifyUser_Btn"))).click();
		assertEquals("Modify Selected User", driver.getTitle());
		verifyUserProfileEdited_ConfirmationMsg(driver, ConfirmationMsg,
				methodName + " verifyChangedUserRoleConfirmationMsg");
		
		Thread.sleep(2_000);
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_HomePage_Lnk"))).click();
		assertEquals("Admin", driver.getTitle());
		
		MainApp_function(driver, FunctionEnum.adminSearchesUser);
		searchUser_function(driver, uname, methodName + " userDetailsAfterChangingUserRole");
		takeScreenshot(driver, methodName + " userDetailsAfterChangingUserRole");
	
		
		Thread.sleep(2_000);
		driver.findElement(By.xpath(prop.getProperty("SearchForUser_LogOut_Lnk"))).click();
		assertEquals("UTA MAC Facility Maintenance", driver.getTitle());
		
		

	}
	
	
	@Test
	@FileParameters("src/Excel/TC03d_test_cases.csv")
	public void TC03d(int testCaseNumber, String uname, String lastName, String utaId, String phone, String email, String address, 
			String city, String zipCode, String errMsg, String emailErrorMsg, String zipCodeErrMsg) throws Exception {
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		driver.get(sAppURL);
		UFMS_LoginUser(driver, username, password);
		assertEquals("Admin", driver.getTitle());
		MainApp_function(driver, FunctionEnum.adminSearchesUser); // select Search For User from homepage
		
		// Check validations on edit User Profile page
		searchUser_function(driver, uname, methodName + " userDetailsBeforeEditingProfile");
		Thread.sleep(2_000);
		driver.findElement(By.xpath(prop.getProperty("SearchForUser_ViewBtnToModifyUserDetails_Lnk"))).click();
		assertEquals("Modify Selected User", driver.getTitle());
		
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_LastName_Box"))).clear();
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_LastName_Box"))).sendKeys(lastName);
		
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_UtaID_Box"))).clear();
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_UtaID_Box"))).sendKeys(utaId);
		
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_Phone_Box"))).clear();
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_Phone_Box"))).sendKeys(phone);
		
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_Email_Box"))).clear();
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_Email_Box"))).sendKeys(email);
		
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_Address_Box"))).clear();
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_Address_Box"))).sendKeys(address);
		
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_City_Box"))).clear();
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_City_Box"))).sendKeys(city);
		
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_ZipCode_Box"))).clear();
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_ZipCode_Box"))).sendKeys(zipCode);
		
		Thread.sleep(2_000);
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_ModifyUser_Btn"))).click();
		assertEquals("Modify Selected User", driver.getTitle());
		takeScreenshot(driver, methodName + " EditUserProfile " + testCaseNumber);
		verifyEditUserProfileErrorMessages(driver, errMsg, emailErrorMsg, zipCodeErrMsg);
		Thread.sleep(2_000);
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_HomePage_Lnk"))).click(); 
		assertEquals("Admin", driver.getTitle());// go back to homepage
		
		
	}
	
	@Test
	@FileParameters("src/Excel/TC03e_test_cases.csv")
	public void TC03e(int testCaseNumber, String uname, String lastName, String utaId, String phone, String email, String address, 
			String city, String zipCode, String ConfirmationMsg) throws Exception {
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		driver.get(sAppURL);
		UFMS_LoginUser(driver, username, password);
		MainApp_function(driver, FunctionEnum.adminSearchesUser); // select Search For User from homepage
		
		// Edit User Profile
		searchUser_function(driver, uname, methodName + " userDetailsBeforeEditingProfile");
		takeScreenshot(driver, methodName + " userDetailsBeforeEditingProfile");
		Thread.sleep(2_000);
		driver.findElement(By.xpath(prop.getProperty("SearchForUser_ViewBtnToModifyUserDetails_Lnk"))).click();
		assertEquals("Modify Selected User", driver.getTitle());
		
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_LastName_Box"))).clear();
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_LastName_Box"))).sendKeys(lastName);
		
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_UtaID_Box"))).clear();
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_UtaID_Box"))).sendKeys(utaId);
		
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_Phone_Box"))).clear();
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_Phone_Box"))).sendKeys(phone);
		
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_Email_Box"))).clear();
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_Email_Box"))).sendKeys(email);
		
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_Address_Box"))).clear();
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_Address_Box"))).sendKeys(address);
		
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_City_Box"))).clear();
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_City_Box"))).sendKeys(city);
		
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_ZipCode_Box"))).clear();
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_ZipCode_Box"))).sendKeys(zipCode);
		
		Thread.sleep(2_000);
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_ModifyUser_Btn"))).click();
		assertEquals("Modify Selected User", driver.getTitle());
		
		verifyUserProfileEdited_ConfirmationMsg(driver, ConfirmationMsg, methodName + " verifyChangedUserRoleConfirmationMsg");
		
		Thread.sleep(2_000);
		driver.findElement(By.xpath(prop.getProperty("ModifyUserDetails_HomePage_Lnk"))).click();
		assertEquals("Admin", driver.getTitle());
		
		MainApp_function(driver, FunctionEnum.adminSearchesUser);
		searchUser_function(driver, uname, methodName + " userDetailsAfterEditingProfile");
		takeScreenshot(driver, methodName + " userDetailsAfterEditingProfile");
		
		Thread.sleep(2_000);
		driver.findElement(By.xpath(prop.getProperty("SearchForUser_LogOut_Lnk"))).click();
		assertEquals("UTA MAC Facility Maintenance", driver.getTitle());
		
		
	}
	
	

	

	@Test
	@FileParameters("src/Excel/TC03c_test_cases.csv")
	public void TC03c(String col1, String col2, String col3, String col4, String col5, String col6, String col7)
			throws Exception {
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		driver.get(sAppURL);
		UFMS_LoginUser(driver, username, password);
		assertEquals("Admin", driver.getTitle());
		Thread.sleep(2_000);
		MainApp_function(driver, FunctionEnum.adminSearchesUser); // select Search For User from homepage

		// check user detail listing headers
		verifyHeaders_User(driver, "SearchForUser_col1InUserListTableHeader", col1,
				"SearchForUser_col2InUserListTableHeader", col2, "SearchForUser_col3InUserListTableHeader", col3,
				"SearchForUser_col4InUserListTableHeader", col4, "SearchForUser_col5InUserListTableHeader", col5,
				"SearchForUser_col6InUserListTableHeader", col6, "SearchForUser_col7InUserListTableHeader", col7,
				methodName + " verifyHeaders test case ");		 
		Thread.sleep(2_000);
		driver.findElement(By.xpath(prop.getProperty("SearchForUser_HomePage_Lnk"))).click();
		assertEquals("Admin", driver.getTitle());// go back to homepage
	}
	

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

}
