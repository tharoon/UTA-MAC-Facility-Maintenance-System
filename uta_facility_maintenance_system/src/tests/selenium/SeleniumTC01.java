package tests.selenium;

//import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import java.util.Properties;
import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
//import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;

import java.util.Properties;
import java.io.FileInputStream;
import functions.UFMSFunctions;
import functions.UFMSFunctions.FunctionEnum;

import org.junit.runners.MethodSorters;

@RunWith(JUnitParamsRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SeleniumTC01 extends UFMSFunctions {
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	public static String sAppURL, sSharedUIMapPath, username, password;

	@Before
	public void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver", "c:/ChromeDriver/chromedriver.exe");
		driver = new ChromeDriver();
		// baseUrl = "http://localhost:8080/";
		prop = new Properties();
		prop.load(new FileInputStream("./Configuration/UFMS_Configuration.properties"));
		sAppURL = prop.getProperty("sAppURL");
		sSharedUIMapPath = prop.getProperty("sSharedUIMapPath");
		prop.load(new FileInputStream(sSharedUIMapPath));
		prop.load(new FileInputStream("src/Login/Login.properties"));
		username = prop.getProperty("usernameUser");
		password = prop.getProperty("passwordUser");
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void verifyAllLinks() throws Exception {
		driver.get(sAppURL);
		UFMS_LoginUser(driver, username, password);
		Thread.sleep(2_000);
		driver.findElement(By.xpath(prop.getProperty("UserHomePage_viewProfileLink"))).click();
		assertEquals("Modify User Details", driver.getTitle());
		driver.findElement(By.xpath(prop.getProperty("UserHomePage_viewProfile_homeBtn"))).click();
		Thread.sleep(2_000);
		driver.findElement(By.xpath(prop.getProperty("UserHomePage_searchFacilities"))).click();
		assertEquals("Search Facilities", driver.getTitle());
		driver.findElement(By.xpath(prop.getProperty("UserHomePage_searchFacilities_homeBtn"))).click();
		Thread.sleep(2_000);
		driver.findElement(By.xpath(prop.getProperty("UserHomePage_createMar"))).click();
		assertEquals("Create MAR", driver.getTitle());
		driver.findElement(By.xpath(prop.getProperty("UserHomePage_createMar_homeBtn"))).click();
		Thread.sleep(2_000);
		driver.findElement(By.xpath(prop.getProperty("UserHomePage_searchMar"))).click();
		assertEquals("MAR List", driver.getTitle());
		Thread.sleep(2_000);
		driver.findElement(By.xpath(prop.getProperty("UserHomePage_searchMar_homeBtn"))).click();
	}

	@Test
	@FileParameters("src/Excel/UserDetailTestCases.csv")
	public void TC01(int testCaseNumber, String userName, String firstName, String lastName, String password,
			String confirmPassword, String userRole, String utaID, String phone, String email, String address,
			String city, String state, String zipCode, String errorMsg, String userNameErrorMessage,
			String firstNameErrorMessage, String lastNameErrorMessage, String passwordErrorMessage,
			String confirmPasswordErrorMessage, String userRoleErrorMessage, String utaIDErrorMessage,
			String phoneErrorMessage, String emailErrorMessage, String addressErrorMessage, String cityErrorMessage,
			String StateErrorMessage, String zipCodeErrorMessage) throws Exception {

		driver.get(sAppURL);

		UFMS_UserRegistraton(driver, userName, firstName, lastName, password, confirmPassword, userRole, utaID, phone,
				email, address, city, state, zipCode);
		takeScreenshot(driver, "Registration " + testCaseNumber);
		verifyRegistrationErrorMessages(driver, errorMsg, userNameErrorMessage, firstNameErrorMessage,
				lastNameErrorMessage, passwordErrorMessage, confirmPasswordErrorMessage, userRoleErrorMessage,
				utaIDErrorMessage, phoneErrorMessage, emailErrorMessage, addressErrorMessage, cityErrorMessage,
				StateErrorMessage, zipCodeErrorMessage);

	}

	@Test
	@FileParameters("src/Excel/LoginTestCases.csv")
	public void TC02(int testCaseNumber, String userName, String password, String errorMsg, String userNameErrorMessage,
			String passwordErrorMessage, String invalidGreetingMessage) {
		driver.get(sAppURL);
		UFMS_LoginUser(driver, userName, password);
		takeScreenshot(driver, "Login " + testCaseNumber);
		verifyLoginErrorMessages(driver, errorMsg, userNameErrorMessage, passwordErrorMessage, invalidGreetingMessage);

	}

	@Test
	@FileParameters("src/Excel/CreateMarTestCases.csv")
	public void TC03(int testCaseNumber, String userName, String password, String faciltyType, String faciltyName,
			String urgency, String description, String errorMsg, String descriptionErrorMessage) throws Exception {
		driver.get(sAppURL);
		UFMS_LoginUser(driver, userName, password);
		MainApp_function(driver, FunctionEnum.createMAR);
		UFMS_CreateMAR(driver, faciltyType, faciltyName, urgency, description);
		takeScreenshot(driver, "CreateMAR " + testCaseNumber);
		verifyCreateMARErrorMessages(driver, errorMsg, descriptionErrorMessage);
		Thread.sleep(2_000);
		driver.findElement(By.xpath(prop.getProperty("CreateMar_Logout_Btn"))).click();

	}

	@Test
	public void TC04() throws Exception {
		driver.get(sAppURL);
		UFMS_UserRegistraton(driver, "skinny", "Skinny", "Pete", "Skinny@123", "Skinny@123", "User", "1001704601",
				"6822406433", "skinny@gmail.com", "1006 Greek Row Dr", "Arlington", "Texas", "76013");

		// Registering new user

		UFMS_LoginUser(driver, "skinny", "Skinny@123");
		MainApp_function(driver, FunctionEnum.createMAR);
		UFMS_CreateMAR(driver, "Badminton courts", "BMC 2", "Unusable",
				"Courts are very Dirty and very difficult to play. Need Immediate action ASAP");
		driver.findElement(By.xpath(prop.getProperty("CreateMar_Logout_Btn"))).click();
	}

	@Test
	@FileParameters("src/Excel/UserUpdateProfileTestCases.csv")
	public void TC05(int testCaseNo, String firstName, String lastName, String UTAid, String phone, String email,
			String streetAddress, String city, String state, String zipCode, String errorMesg, String firstNameError,
			String lastNameError, String UTAidError, String phoneError, String emailError, String streetAddressError,
			String cityError, String zipCodeError, String successGreetingMessage) throws Exception {
		driver.get(sAppURL);
		UFMS_LoginUser(driver, "skinny", "Skinny@123");
		MainApp_function(driver, FunctionEnum.viewUserProfile);

		// Modifying the user details which we created TC04
		UFMS_UpdateUserProfile(driver, firstName, lastName, UTAid, phone, email, streetAddress, city, state, zipCode);
		takeScreenshot(driver, "UserUpdateProfile " + testCaseNo);
		verifyModifyUserDetailsErrorMessages(driver, errorMesg, firstNameError, lastNameError, UTAidError, phoneError,
				emailError, streetAddressError, cityError, zipCodeError, successGreetingMessage);
		Thread.sleep(2_000);
		driver.findElement(By.xpath(prop.getProperty("ViewProfile_Logout_Btn"))).click();
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
