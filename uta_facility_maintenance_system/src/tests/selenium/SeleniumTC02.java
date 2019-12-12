package tests.selenium;

import java.util.regex.Pattern;
import java.io.FileInputStream;
import java.util.Properties;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import java.util.List;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;
import functions.UFMSFunctions;
import uta_facility_maintenance_system.model.Facility;
import uta_facility_maintenance_system.model.MarReport;
import uta_facility_maintenance_system.util.SQLConnection;
import java.util.ArrayList;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;


@RunWith(JUnitParamsRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SeleniumTC02 extends UFMSFunctions {
  private WebDriver driver;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  public static String sAppURL, sSharedUIMapPath, username, password;
  static SQLConnection DBMgr = SQLConnection.getInstance();

  @Before
  public void setUp() throws Exception {
	  System.setProperty("webdriver.chrome.driver", "c:/ChromeDriver/chromedriver.exe");
		driver = new ChromeDriver();
      prop = new Properties();
      prop.load(new FileInputStream("./Configuration/UFMS_Configuration.properties"));
      sAppURL = prop.getProperty("sAppURL");
      prop.load(new FileInputStream(prop.getProperty("sSharedUIMapPath")));
      prop.load(new FileInputStream("src/Login/Login.properties"));
	  username = prop.getProperty("usernameFacilityManager");
	  password = prop.getProperty("passwordFacilityManager");
      driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
      
  }
  private static ArrayList<MarReport> returnMatchingMARList(String queryString) {
	ArrayList<MarReport> listInDB = new ArrayList<MarReport>();

		Statement stmt = null;
		Connection conn = SQLConnection.getDBConnection();
		try {
			stmt = conn.createStatement();
			ResultSet userList = stmt.executeQuery(queryString);
			while (userList.next()) {
				MarReport mcfm = new MarReport();
				mcfm.setMarNumber(userList.getString("mar_number"));				
				mcfm.setEstimateOfRepair(userList.getString("estimate_of_repair"));
				mcfm.setAssignTo(userList.getString("assigned_to"));
				mcfm.setUrgency(userList.getString("urgency"));
				mcfm.setFacilityType(userList.getString("facility_type"));
				mcfm.setFacilityName(userList.getString("facility_name"));
				mcfm.setDescription(userList.getString("description"));
				mcfm.setReportedBy(userList.getString("reported_by"));
				mcfm.setCreatedDate(userList.getString("created_date"));
				listInDB.add(mcfm);
			}
		} catch (SQLException e) {
		}
		return listInDB;
	}
  
  private static ArrayList<MarReport> returnMatchingMarReportList(String queryString) {
		ArrayList<MarReport> marReportListInDB = new ArrayList<MarReport>();

		Statement stmt = null;
		Connection conn = SQLConnection.getDBConnection();

		try {
			stmt = conn.createStatement();
			ResultSet marReportList = stmt.executeQuery(queryString);

			while (marReportList.next()) {
				MarReport marReport = new MarReport();
				marReport.setMarNumber(marReportList.getString("mar_number"));
				marReport.setFacilityName(marReportList.getString("facility_name"));
				marReport.setFacilityType(marReportList.getString("facility_type"));
				marReport.setUrgency(marReportList.getString("urgency"));
				marReport.setDescription(marReportList.getString("description"));
				marReport.setAssignTo(marReportList.getString("assigned_to"));
				marReport.setAssignDate(marReportList.getString("assigned_date"));
				marReport.setEstimateOfRepair(marReportList.getString("estimate_of_repair"));
				marReportListInDB.add(marReport);
			}
		} catch (SQLException e) {
		}
		return marReportListInDB;
	}
  
  private static ArrayList<Facility> returnMatchingFacilitiesList (String queryString) {
		ArrayList<Facility> facilityListInDB = new ArrayList<Facility>();
		
		Statement stmt = null;
		Connection conn = SQLConnection.getDBConnection();  
		try {
			stmt = conn.createStatement();
			ResultSet facilityList = stmt.executeQuery(queryString);
			while (facilityList.next()) {
				Facility facility = new Facility(); 
				facility.setFacilityType(facilityList.getString("facility_type"));
				facility.setFacilityName(facilityList.getString("facility_name"));
				facility.setInterval(facilityList.getString("interval"));
				facility.setDuration(facilityList.getString("duration"));  
				facility.setVenue(facilityList.getString("venue"));
				facility.setBookingStatus(facilityList.getString("booking_status"));
				facilityListInDB.add(facility);	
			}
		} catch (SQLException e) {}
		return facilityListInDB;
	}
  
  public static ArrayList<MarReport> listUnassignedMAR() {
	return returnMatchingMARList("SELECT * from mar_report WHERE assigned_to is null ORDER BY mar_number");
  }

  public static ArrayList<MarReport> assignedMARByDate(String date) {
		return returnMatchingMarReportList(
				" SELECT * FROM mar_report WHERE assigned_date = '" + date + "'");
	}
  
  public static ArrayList<Facility>  listFacilities() {  
		return returnMatchingFacilitiesList(" SELECT * from FACILITIES WHERE booking_status = 'Available' ORDER BY facility_name");
  }
  
  public static ArrayList<Facility>   searchFacilityByType(String facilitytype)  {  
		return returnMatchingFacilitiesList(" SELECT * from FACILITIES WHERE facility_type = '"+facilitytype+"' and booking_status = 'Available' ORDER BY facility_name");
}
  private String [][] getSearchFAcilityListFromDB(int listSize, String facilityType) throws SQLException { // this method gets the list assign repairs table contents from the DB
	    ArrayList<Facility> fromDB = searchFacilityByType(facilityType);
	    String [][] arrayDB = new String [listSize-1][6];
	    int i = 0;
	    for (Facility p:fromDB) {
	    	arrayDB[i][0] = p.getFacilityType();
	    	arrayDB[i][1] = p.getFacilityName();
	    	arrayDB[i][2] = p.getInterval();
	    	arrayDB[i][3] = p.getDuration();
	    	arrayDB[i][4] = p.getVenue();
	    	arrayDB[i][5] = p.getBookingStatus();
	 		i++;
	    }
	    return arrayDB;
}
  
  private String [][] getFacilityListFromDB(int listSize) throws SQLException { // this method gets the list assign repairs table contents from the DB
	    ArrayList<Facility> fromDB = listFacilities();
	    String [][] arrayDB = new String [listSize-1][6];
	    int i = 0;
	    for (Facility p:fromDB) {
	    	arrayDB[i][0] = p.getFacilityType();
	    	arrayDB[i][1] = p.getFacilityName();
	    	arrayDB[i][2] = p.getInterval();
	    	arrayDB[i][3] = p.getDuration();
	    	arrayDB[i][4] = p.getVenue();
	    	arrayDB[i][5] = p.getBookingStatus();
	 		i++;
	    }
	    return arrayDB;
}
  
  private String [][] getUnassignedMARListFromDB(int listSize) throws SQLException { // this method gets the list assign repairs table contents from the DB
	    ArrayList<MarReport> fromDB = listUnassignedMAR();
	    String [][] arrayDB = new String [listSize-1][7];
	    int i = 0;
	    for (MarReport p:fromDB) {
	    	arrayDB[i][0] = p.getFacilityType();
	    	arrayDB[i][1] = p.getFacilityName();
	    	arrayDB[i][2] = p.getUrgency();
	    	arrayDB[i][3] = p.getDescription();
	    	arrayDB[i][4] = p.getReportedBy();
	    	arrayDB[i][5] = p.getCreatedDate();
	    	arrayDB[i][6] = p.getMarNumber();
	 		i++;
	    }
	    return arrayDB;
}
  
  private String [][] getAssignedMARListFromDB(int listSize, String date) throws SQLException, ParseException { // this method gets the list assign repairs table contents from the DB
  	System.out.println("print date" + date); 
  	 String startDateString = date;
     SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
     SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
     String mardate= sdf2.format(sdf.parse(startDateString));
	  ArrayList<MarReport> fromDB = assignedMARByDate(mardate);
	    String [][] arrayDB = new String [listSize-1][8];
	    int i = 0;
	    for (MarReport p:fromDB) {
	    	arrayDB[i][0] = p.getMarNumber();	    			
	    	arrayDB[i][1] = p.getAssignTo();
	    	arrayDB[i][2] = p.getAssignDate();
	    	arrayDB[i][3] = p.getFacilityName();	    			
	    	arrayDB[i][4] = p.getFacilityType();	    			
	    	arrayDB[i][5] = p.getUrgency();	    			
	    	arrayDB[i][6] = p.getDescription();
	    	arrayDB[i][7] = p.getEstimateOfRepair();
	 		i++;
	    }
	    return arrayDB;
}
  private String [][] getUnassignedMARListContentFromTable(int rows, List<WebElement> rowValues, int columns) throws Exception { 
	
	  String [][] UnassignMARArray = new String[rows-1][columns];	  
	  for(int i=1; i < rows; i++) {
		  List<WebElement> colVals = rowValues.get(i).findElements(By.tagName("td"));
		  for(int j=0; j < columns; j++) {
			  UnassignMARArray[i-1][j] = colVals.get(j).getText();
		  }
	  }
	  return UnassignMARArray;
  }
  
  private String [][] getAssignedMARListContentFromTable(int rows, List<WebElement> rowValues, int columns) throws Exception { 
		
	  String [][] assignMARArray = new String[rows-1][columns];	  
	  for(int i=1; i < rows; i++) {
		  List<WebElement> colVals = rowValues.get(i).findElements(By.tagName("td"));
		  for(int j=0; j < columns; j++) {
			  assignMARArray[i-1][j] = colVals.get(j).getText();
		  }
	  }
	  return assignMARArray;
  }
  
  private String [][] getFacilityListContentFromTable(int rows, List<WebElement> rowValues, int columns) throws Exception { 
		
	  String [][] facilityListArray = new String[rows-1][columns];	  
	  for(int i=1; i < rows; i++) {
		  List<WebElement> colVals = rowValues.get(i).findElements(By.tagName("td"));
		  for(int j=0; j < columns; j++) {
			  facilityListArray[i-1][j] = colVals.get(j).getText();
		  }
	  }
	  return facilityListArray;
  }
  
  private Boolean arraysDiff2 (String [][] array1, String [][] array2) { 
	  Boolean diff = false || (array1.length != array2.length);
	  for (int i = 0; i < array1.length && !diff; i++) {
		 diff  = !array1[i][0].equals(array2[i][0]) || !array1[i][1].equals(array2[i][1]) || 
				 !array1[i][2].equals(array2[i][2]) || !array1[i][3].equals(array2[i][3]) ||
				 !array1[i][4].equals(array2[i][4]) || !array1[i][5].equals(array2[i][5]);
	  }
	  return diff;
  }
  
  private Boolean arraysDiff (String [][] array1, String [][] array2) { 
	  Boolean diff = false || (array1.length != array2.length);
	  for (int i = 0; i < array1.length && !diff; i++) {
		 diff  = !array1[i][0].equals(array2[i][0]) || !array1[i][1].equals(array2[i][1]) || 
				 !array1[i][2].equals(array2[i][2]) || !array1[i][3].equals(array2[i][3]) ||
				 !array1[i][4].equals(array2[i][4]) || !array1[i][5].equals(array2[i][5]) ||
				 !array1[i][6].equals(array2[i][6]);
	  }
	  return diff;
  }
  private Boolean arraysDiff1 (String [][] array1, String [][] array2) { 
	  Boolean diff = false || (array1.length != array2.length);
	  for (int i = 0; i < array1.length && !diff; i++) {
		 diff  = !array1[i][0].equals(array2[i][0]) || !array1[i][1].equals(array2[i][1]) || 
				 !array1[i][2].equals(array2[i][2]) || !array1[i][3].equals(array2[i][3]) ||
				 !array1[i][4].equals(array2[i][4]) || !array1[i][5].equals(array2[i][5]) ||
				 !array1[i][6].equals(array2[i][6]) || !array1[i][7].equals(array2[i][7]);
	  }
	  return diff;
  }
  
  @Test
  public void verifyAllLinks() throws Exception {
	  driver.get(sAppURL);
	  UFMS_LoginUser(driver,username, password);
	  
	  // Check Create MAR Title
	  driver.findElement(By.xpath(prop.getProperty("FacilityManager_CreateMARNumber"))).click();
	  assertEquals("Create MAR", driver.getTitle());
	  Thread.sleep(2_000);
	  driver.findElement(By.xpath(prop.getProperty("CreateMARNumber_HomeLink"))).click();
	  
	  //Check Add new Facility Title
	  driver.findElement(By.xpath(prop.getProperty("FacilityManager_AddNewFacility"))).click();
	  assertEquals("ADD NEW FACILITY", driver.getTitle());
	  Thread.sleep(2_000);
	  driver.findElement(By.xpath(prop.getProperty("AddNewFacility_HomeLink"))).click();
	  
	  //Check Search Repairer By Date Title
	  driver.findElement(By.xpath(prop.getProperty("FacilityManager_SearchRepairerByDate"))).click();
	  assertEquals("Search Repairer Schedule", driver.getTitle());
	  Thread.sleep(2_000);
	  driver.findElement(By.xpath(prop.getProperty("SearchRepairerByDate_HomeLink"))).click();

	  
	  // Check Assigned MARS Title
	  driver.findElement(By.xpath(prop.getProperty("FacilityManager_SearchAssignedMARSByDate"))).click();
	  assertEquals("Search Assigned MARs", driver.getTitle());
	  Thread.sleep(2_000);
	  driver.findElement(By.xpath(prop.getProperty("SearchAssignedMARSByDate_HomeLink"))).click();
	  
	  //Check View all MARS Title
	  driver.findElement(By.xpath(prop.getProperty("FacilityManager_ViewAllMARS"))).click();
	  assertEquals("MAR List", driver.getTitle());
	  Thread.sleep(2_000);
	  driver.findElement(By.xpath(prop.getProperty("ViewAllMARS_HomeLink"))).click();
	  
	  // Check View Available Facilities Title
	  driver.findElement(By.xpath(prop.getProperty("FacilityManager_ViewAvailableFacilities"))).click();
	  assertEquals("Facilities List", driver.getTitle());
	  Thread.sleep(2_000);
	  driver.findElement(By.xpath(prop.getProperty("ViewAvailableFacilities_HomeLink"))).click();
	  
	  // Check Search Facilities Title
	  driver.findElement(By.xpath(prop.getProperty("FacilityManager_SearchFacilities"))).click();
	  assertEquals("Search Facilities", driver.getTitle());
	  Thread.sleep(2_000);
	  driver.findElement(By.xpath(prop.getProperty("SearchFacilities_HomeLink"))).click();
	  
	  
	  //Check View Specific MAR Title
	  driver.findElement(By.xpath(prop.getProperty("FacilityManager_ViewSpecificMAR"))).click();
	  assertEquals("View Specific MAR", driver.getTitle());
	  Thread.sleep(2_000);
	  driver.findElement(By.xpath(prop.getProperty("ViewSpecificMAR_HomeLink"))).click();
	  
	  driver.findElement(By.xpath(prop.getProperty("FacilityManager_Logout"))).click();
  }
  
  @Test
  public void testSeleniumTC02a() throws Exception {
    driver.get(sAppURL);
    String methodName= new Throwable().getStackTrace()[0].getMethodName();  
    //USer Registration
    UFMS_UserRegistraton(driver, "pavi0324", "Pavithra", "Rathinasabapathy", "Pavi@123", "Pavi@123", "Facility Manager", 
	    				  "1001698735", "1234567891", "pavi.rathina@gmail.com","1006 greek row", "Arlington", "Texas", "76013");
    //Login
    UFMS_LoginUser(driver,username, password);
    MainApp_function(driver,FunctionEnum.searchUnassignedMARS); 
	Thread.sleep(2_000);	
	verifyHeaders_UnassignedMARS(driver, "SearchUnassignedMAR_FacilityType", "FacilityType", "SearchUnassignedMAR_FacilityName", "FacilityName", "SearchUnassignedMAR_Urgency", "Urgency", "SearchUnassignedMAR_Description",
								"Description", "SearchUnassignedMAR_ReportedBy","Reported by","SearchUnassignedMAR_ReportedDate", "Reported date", 
								"SearchUnassignedMAR_MARNumber", "Marnumber", methodName + "_verifyHeaders_test_case ");
	WebElement unAssigned_MAR_Table = driver.findElement(By.xpath(prop.getProperty("SearchUnassignedMAR_MARTable")));
	int rows = unAssigned_MAR_Table.findElements(By.tagName("tr")).size();
	int columns = unAssigned_MAR_Table.findElements(By.xpath("//*[@id='myMarTable']/tbody/tr[1]/th")).size();
	System.out.println(columns);
	List<WebElement> rowValues = unAssigned_MAR_Table.findElements(By.tagName("tr"));
	assertFalse(arraysDiff(getUnassignedMARListFromDB(rows), getUnassignedMARListContentFromTable(rows, rowValues, columns)));	
	driver.findElement(By.xpath(prop.getProperty("SearchUnassignedMAR_HomeLink"))).click();
  }
 
  
  @Test
  @FileParameters("src/Excel/TC02_test_cases.csv")
  public void testSeleniumTC02b(String testCaseNumber, String MARNumberFromCSV, String path, String estimate_of_Repair, String repairer, String date, String GreetingText) throws Exception {  
	String methodName= new Throwable().getStackTrace()[0].getMethodName();
	driver.get(sAppURL);
	UFMS_LoginUser(driver,username, password);
	MainApp_function(driver,FunctionEnum.searchUnassignedMARS);
	Thread.sleep(2_000);
	assertEquals("MAR List", driver.getTitle());
	String MARNo = driver.findElement(By.xpath(prop.getProperty("SearchUnassignedMAR_MARNumberValue"))).getText();
	driver.findElement(By.xpath(path)).click();	
	Thread.sleep(2_000);
	assertEquals("MAR Form", driver.getTitle());
    UFMS_AssignMAR(driver, estimate_of_Repair, repairer, MARNo,GreetingText, methodName +"_test_case_" + testCaseNumber  );
    MainApp_function(driver,FunctionEnum.searchAssignedMARs);
    UFMS_SearchAssignedMARSbyDate(driver, date, methodName + "search_Assigned_MARS_Date_" + testCaseNumber);
    assertEquals("Assigned Repair Details", driver.getTitle());
    VerifyHeaders_AssignedMARs(driver, "AssignedMARsHeaders_MARNumber", "Mar number", "AssignedMARsHeaders_AssignedTo", "assigned to", "AssignedMARsHeaders_AssignedDate", "assigned Date", "AssignedMARsHeaders_FacilityName", "Facility name", "AssignedMARsHeaders_FacilityType", "Facility type", 
    								   "AssignedMARsHeaders_Urgency", "Urgency", "AssignedMARsHeaders_Description", "Description", "AssignedMARsHeaders_EstimateOfRepair", "Estimate_Of_repair", methodName + "_header_" + testCaseNumber);
    WebElement assigned_MAR_Table = driver.findElement(By.xpath(prop.getProperty("AssignedMARsTable")));
	int rows = assigned_MAR_Table.findElements(By.tagName("tr")).size();
	int columns = assigned_MAR_Table.findElements(By.xpath("//*[@id='assignMARTable']/tbody/tr[1]/th")).size();
	List<WebElement> rowValues = assigned_MAR_Table.findElements(By.tagName("tr"));	
	assertFalse(arraysDiff1(getAssignedMARListFromDB(rows, date), getAssignedMARListContentFromTable(rows, rowValues, columns)));
	driver.findElement(By.xpath(prop.getProperty("AssignedMARsResultHomeLink"))).click();
    UFMS_Logout(driver);   
  }

  @Test
  @FileParameters("src/Excel/TC02_test_cases2a.csv")
  public void testSeleniumTC02c(String testCaseNumber, String path, String facility, String date, String time ) throws Exception {
	  String methodName= new Throwable().getStackTrace()[0].getMethodName();
	  driver.get(sAppURL); 
	  UFMS_LoginUser(driver,username, password);
	  MainApp_function(driver,FunctionEnum.viewAvailableFacilities);
	  Thread.sleep(2_000); 
	  verifyHeaders_FacilityTable(driver, "ViewAvailableFacilities_FacilityType", "Facility Type", "ViewAvailableFacilities_FacilityName", "Facility Name", "ViewAvailableFacilities_FacilityInterval", "Interval", "ViewAvailableFacilities_FacilityDuration", "Duration", "ViewAvailableFacilities_FacilityVenue", "Venue",
		  "ViewAvailableFacilities_FacilityStatus", "Status", methodName + "_ViewAvailableFacilities_" + testCaseNumber);
	  String facilityTypeValue = driver.findElement(By.xpath(prop.getProperty("ViewAvailableFacilities_FacilityTypeContent"))).getText();
	  String facilityNameValue = driver.findElement(By.xpath(prop.getProperty("ViewAvailableFacilities_FacilityNameContent"))).getText();
	  String intervalValue = driver.findElement(By.xpath(prop.getProperty("ViewAvailableFacilities_FacilityIntervalContent"))).getText();
	  String durationValue = driver.findElement(By.xpath(prop.getProperty("ViewAvailableFacilities_FacilityDurationContent"))).getText();
	  String venueValue = driver.findElement(By.xpath(prop.getProperty("ViewAvailableFacilities_FacilityVenueContent"))).getText();
	  String statusValue = driver.findElement(By.xpath(prop.getProperty("ViewAvailableFacilities_FacilityStatusContent"))).getText();	  
	  WebElement facilityListTable = driver.findElement(By.xpath(prop.getProperty("ViewAvailableFacilities_FacilityTable")));
	  int rows = facilityListTable.findElements(By.tagName("tr")).size();
	  int columns = facilityListTable.findElements(By.xpath("//*[@id=\"facilitiesList\"]/tbody/tr[1]/th")).size();
	  List<WebElement> rowValues = facilityListTable.findElements(By.tagName("tr"));	
	  assertFalse(arraysDiff2(getFacilityListFromDB(rows), getFacilityListContentFromTable(rows, rowValues, columns)));
	  driver.findElement(By.xpath(prop.getProperty("ViewAvailableFacilities_Home"))).click();
	  MainApp_function(driver,FunctionEnum.viewAvailableFacilities);
	  Thread.sleep(2_000);
	  driver.findElement(By.xpath(path)).click();
	  Thread.sleep(2_000);
	  assertEquals("Facility Details", driver.getTitle());
	  verifyHeaders_FacilityTable(driver, "ViewSpecificFacility_FacilityType", "Facility Type:", "ViewSpecificFacility_FacilityName", "Facility Name:", "ViewSpecificFacility_FacilityInterval", "Interval:", "ViewSpecificFacility_FacilityDuration", "Duration:", "ViewSpecificFacility_FacilityVenue", "Venue:",
			    "ViewSpecificFacility_FacilityStatus", "Status:", methodName + "_ViewSpecificFacility_" + testCaseNumber);
	  verifyContent_viewSpcificFacility(driver, "ViewSpecificFacilityContent_FacilityType", facilityTypeValue , "ViewSpecificFacilityContent_FacilityName", facilityNameValue, "ViewSpecificFacilityContent_FacilityInterval", intervalValue, "ViewSpecificFacilityContent_FacilityDuration", durationValue, "ViewSpecificFacilityContent_FacilityVenue", venueValue,
			    "ViewSpecificFacilityContent_FacilityStatus", statusValue, methodName + "_ViewSpecificFacilityContent_" + testCaseNumber);
	  driver.findElement(By.xpath(prop.getProperty("ViewSpecificFacility_Home"))).click();	 
	  MainApp_function(driver,FunctionEnum.searchFacilities);
	  UFMS_searchFacility(driver, facility, date, time, methodName + "_searchFacility_" + testCaseNumber);
	  assertEquals("Facilities List", driver.getTitle());
	  verifyHeaders_FacilityTable(driver, "searchFacility_typeHeader", "Facility Type", "searchFacility_nameHeader", "Facility Name", "searchFacility_intervalHeader", "Interval", "searchFacility_durationHeader", "Duration", "searchFacility_venueHeader", "Venue",
			    "searchFacility_statusHeader", "Status", methodName + "_ViewSpecificFacility_" + testCaseNumber);
	  WebElement searchFacilityTable = driver.findElement(By.xpath(prop.getProperty("searchFacilityResultTable")));
	  int rows1 = searchFacilityTable.findElements(By.tagName("tr")).size();
	  int columns1 = searchFacilityTable.findElements(By.xpath("//*[@id=\"searchFac\"]/tbody/tr[1]/th")).size();
	  List<WebElement> rowValues1 = searchFacilityTable.findElements(By.tagName("tr"));	
	  assertFalse(arraysDiff2(getSearchFAcilityListFromDB(rows1, facility), getFacilityListContentFromTable(rows1, rowValues1, columns1)));
	  driver.findElement(By.xpath(prop.getProperty("searchFacilityResult_homelink"))).click();
	  
  }
  
  @Test
  @FileParameters("src/Excel/TC02_test_cases2b.csv")
  public void testSeleniumTC02d(String testCaseNumber, String factype, String facname, String interval, String duration, String venue, String errorMsg, String typeError, String nameError,
		                        String intervalError, String durationError, String venueError, String greetingText) throws Exception {
	  String methodName= new Throwable().getStackTrace()[0].getMethodName();
	  driver.get(sAppURL); 
	  UFMS_LoginUser(driver,username, password);
	  MainApp_function(driver,FunctionEnum.addNewFacility);
	  assertEquals("ADD NEW FACILITY", driver.getTitle());
	  UFMS_AddFacility(driver, factype, facname, interval, duration, venue, greetingText, methodName + "_addNewFacility" + testCaseNumber);
	  verify_ErrorsAddingFacility(driver, errorMsg, typeError, nameError, intervalError, durationError, venueError, greetingText);
	  driver.findElement(By.xpath(prop.getProperty("AddNewFacility_homelink"))).click();
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
