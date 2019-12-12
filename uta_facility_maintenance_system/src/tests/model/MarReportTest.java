package tests.model;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;  
import java.sql.Date; 

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;
import uta_facility_maintenance_system.model.CreateMARErrorMsgs;
import uta_facility_maintenance_system.model.MarReport;

@RunWith(JUnitParamsRunner.class)
public class MarReportTest {
	
	MarReport mar;
	CreateMARErrorMsgs marErrorMsgs;
	
	@Before
	public void setUp() throws Exception {
		mar = new MarReport();
		marErrorMsgs = new CreateMARErrorMsgs();
	}

	@Test
	@FileParameters("src/Excel/MAR_Report_Test_cases.csv")
	public void test(String TestCaseNo, String action,String facilityType, String facilityName, String urgency, String description,   
		    String estimateofrepair, String marNumber, String assignTo, 
			String reportedBy, String createdDate, String assignDate, String assignTime, 
			String count, String resultSetMAR, String userName, String searchDate, String greetingText, String errorMsg, String descriptionError, String timeError) {
		
		if(action.equals("modifytime")) {
			mar.setAssignTime(assignTime);
			mar.validateTime(mar, marErrorMsgs);
			assertTrue(errorMsg.equals(marErrorMsgs.getErrorMsg()));
			assertTrue(timeError.equals(marErrorMsgs.getTimeError()));
		}
		else {
			mar.setMarDetails(facilityType, facilityName, urgency, description);
			mar.validateMARReport(action, mar, marErrorMsgs);
			mar.setMAC(estimateofrepair, marNumber, assignTo);
			mar.validateCount(assignTo);
			mar.validateRules(assignTo);
			mar.setMarReport(marNumber, facilityName, facilityType, urgency, descriptionError, reportedBy, createdDate, assignTo, assignDate, assignTime, estimateofrepair);
			assertTrue(errorMsg.equals(marErrorMsgs.getErrorMsg()));
			assertTrue(descriptionError.equals(marErrorMsgs.getDescriptionError()));
		}
		
		
		@SuppressWarnings("unused")
		String adate= mar.getAssignDate();
		mar.setAssignDate(assignDate);
		@SuppressWarnings("unused")
		String atime= mar.getAssignTime();
		mar.setAssignTime(assignTime);
		@SuppressWarnings("unused")
		String aResult= mar.getResultSetMAR();
		mar.setResultSetMAR(resultSetMAR);
		@SuppressWarnings("unused")
		String assignTorepairer = mar.getAssignTo();
		@SuppressWarnings("unused")
		String agreet= mar.getGreetingText();
		mar.setGreetingText(greetingText);
		@SuppressWarnings("unused")
		String aname= mar.getUserName();
		mar.setUserName(userName);
		@SuppressWarnings("unused")
		String acreate= mar.getCreatedDate();
		mar.setCreatedDate(createdDate);
		@SuppressWarnings("unused")
		String afacilityType= mar.getFacilityType();
		mar.setFacilityType(facilityType);
		@SuppressWarnings("unused")
		String afacilityName= mar.getFacilityName();
		mar.setFacilityName(facilityName);
		@SuppressWarnings("unused")
		String aurgency= mar.getUrgency();
		mar.setUrgency(urgency);
		@SuppressWarnings("unused")
		String adescription= mar.getDescription();
		mar.setDescription(description);
		@SuppressWarnings("unused")
		String arebortedBy= mar.getReportedBy();
		mar.setReportedBy(reportedBy);
		@SuppressWarnings("unused")
		String amarnumber= mar.getMarNumber();
		mar.setMarNumber(marNumber);
		@SuppressWarnings("unused")
		String aestimate= mar.getEstimateOfRepair();
		mar.setEstimateOfRepair(estimateofrepair);
		@SuppressWarnings("unused")
		String searchdate= mar.getSearchDate();
		mar.setSearchDate(searchDate);		
	}


}
