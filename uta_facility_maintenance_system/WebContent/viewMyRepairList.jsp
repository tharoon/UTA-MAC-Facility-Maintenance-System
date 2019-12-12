<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Repair List</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" >
<link href="style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<h1>Repairs List</h1>
	<table border="1" class="myTable">
		<tr class="myTableRow">
			<th class="myTableHead" style="padding-right: 20px; text-align: left">Mar
				number</th>
			<th class="myTableHead" style="padding-right: 35px; text-align: left">assigned
				to</th>
			<th class="myTableHead" style="padding-right: 20px; text-align: left">Facility
				name</th>
			<th class="myTableHead" style="padding-right: 30px; text-align: left">Facility
				type</th>
			<th class="myTableHead" style="padding-right: 30px; text-align: left">Urgency</th>
			<th class="myTableHead" style="padding-right: 30px; text-align: left">Description</th>
			<th class="myTableHead" style="padding-right: 30px; text-align: left">Estimate_Of_repair</th>
		</tr>

		<c:forEach items="${MyRepairList}" var="item" varStatus="status">
			<tr class="myTableRow">
				<td class="myTableCell" style="padding-right: 20px;"><c:out
						value="${item.marNumber}" /></td>
				<td class="myTableCell" style="padding-right: 35px;"><c:out
						value="${item.assignTo}" /></td>
				<td class="myTableCell" style="padding-right: 20px;"><c:out
						value="${item.facilityName}" /></td>
				<td class="myTableCell" style="padding-right: 30px;"><c:out
						value="${item.facilityType}" /></td>
				<td class="myTableCell" style="padding-right: 35px;"><c:out
						value="${item.urgency}" /></td>
				<td class="myTableCell" style="padding-right: 20px;"><c:out
						value="${item.description}" /></td>
				<td class="myTableCell" style="padding-right: 30px;"><c:out
						value="${item.estimateOfRepair}" /></td>

				<td><a
					href="/uta_facility_maintenance_system/MarReportController?action=ViewSpecificRepair&id=${item.marNumber}">View</a>
				</td>
			</tr>
		</c:forEach>
		
	</table>
	<a href="UserDetailController?action=homepage"  target="_top"><span>Home</span></a><br>
	<a href="/uta_facility_maintenance_system/UserDetailController?action=logout"  target="_top"><span>LogOut</span></a>
</body>
</html>