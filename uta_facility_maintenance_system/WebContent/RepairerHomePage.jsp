<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Repairer Home Page</title>
</head>
<body>
<div class="logo">
      	<h1>
      		<a href="<c:url value='/' />">
      			UTA Facility Maintenance System
      		</a>
      	</h1>
      </div>
	 <div class="menu_nav">
        <ul>
          <li><a href="/uta_facility_maintenance_system/MarReportController?action=listViewMyRepairs"  target="_top"><span>View My Reserved Repairs</span></a></li>
          <li><a href="RequestReservation.jsp"  target="_top"><span>Request Reservation</span></a></li>
           <li><a href="/uta_facility_maintenance_system/UserDetailController?action=viewProfile"  target="_top"><span>View Profile</span></a></li>
          <li><a href="/uta_facility_maintenance_system/UserDetailController?action=logout"  target="_top"><span>LogOut</span></a></li>
        </ul>
      </div>
</body>
</html>