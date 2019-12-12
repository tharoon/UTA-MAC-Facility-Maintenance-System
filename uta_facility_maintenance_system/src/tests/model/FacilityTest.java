package tests.model;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;
import uta_facility_maintenance_system.model.Facility;
import uta_facility_maintenance_system.model.FacilityErrorMsgs;

@RunWith(JUnitParamsRunner.class)
public class FacilityTest {

	Facility facility;
	FacilityErrorMsgs facilityErrorMsgs;
	
	@Before
	public void setUp() throws Exception {
		facility = new Facility();
		facilityErrorMsgs = new FacilityErrorMsgs();
	}
	
	@Test
	@FileParameters("src/Excel/Facility_Test_Cases.csv")
	public void test(String TestCaseNo, String action, String facilityType, String facilityName, String interval, String duration, String venue,
			String bookedDuration, String bookingStatus, String bookedBy,String errorMsg, String facilityTypeError, String facilityNameError, 
			String intervalError, String durationError, String venueError,String bookedDurationError, String bookedStatusError, String bookedByError,
			String greetingText) {
		
		facility.setFacilityDetails(facilityType, facilityName, interval, duration, venue, " ");
		@SuppressWarnings("unused")
		String dummy = facility.getBookedBy();
		facility.setBookedBy(bookedBy);
		facility.getBookingStatus();
		facility.setBookingStatus(bookingStatus);
		facility.getGreetingText();
		facility.getBookedDuration();
		facility.setBookedDuration(bookedDuration);
		facility.setGreetingText(greetingText);
		if(action.equals("reqReservation")) {
			facility.validateRequestReservationDetails(action, facility, facilityErrorMsgs);
			assertTrue(errorMsg.equals(facilityErrorMsgs.getErrorMsg()));
			assertTrue(bookedDurationError.equals(facilityErrorMsgs.getBookedDurationError()));
			assertTrue(facilityNameError.equals(facilityErrorMsgs.getFacilityNameError()));
		} else {
			facility.validateInputFacilityDetails(action, facility, facilityErrorMsgs);
			assertTrue(errorMsg.equals(facilityErrorMsgs.getErrorMsg()));
			assertTrue(facilityTypeError.equals(facilityErrorMsgs.getFacilityTypeError()));
			assertTrue(facilityNameError.equals(facilityErrorMsgs.getFacilityNameError()));
			assertTrue(intervalError.equals(facilityErrorMsgs.getIntervalError()));
			assertTrue(durationError.equals(facilityErrorMsgs.getDurationError()));
			assertTrue(venueError.equals(facilityErrorMsgs.getVenueError()));
		}
		 
		
	}

}
