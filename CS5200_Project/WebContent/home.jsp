<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<link rel="stylesheet" type="text/css" href="css/style.css">
<title>DBaaS MDM</title>
</head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<body>
	<div>
		Ryan Millay (millay.r@husky.neu.edu)
		<h1 style="text-align:center">DBaaS Master Data Manager</h1>
	  	<table>
	  		<tr>
	  			<td><a class="white" href="/CS5200_Project/alarmManagement.jsp">Alarm Management</a></td>
	  			<td>Enable/Disable Alarms, Create New Alarms, Search Alarms</td>
	  		</tr>
	  		<tr>
	  			<td><a class="white" href="/CS5200_Project/ticketManagement.jsp">Support Ticket Management</a></td>
	  			<td>Query or Resolve Support Tickets</td>
	  		</tr>
	  		<tr>
	  			<td><a class="white" href="/CS5200_Project/clusterManagement.jsp">Cluster Management</a></td>
	  			<td>Add/Remove Node to Cluster, Query Cluster Statistics</td>
	  		</tr>
	  		<tr>
	  			<td><a class="white" href="/CS5200_Project/dbManagement.jsp">Database Management</a></td>
	  			<td>Update Database Shards, Query Database Indexes</td>
	  		</tr>
	  		<tr>
	  			<td><a class="white" href="/CS5200_Project/customerManagement.jsp">Customer Management</a></td>
	  			<td>Add New Customers, Query Customer Information, Find a Customer's TAM</td>
	  		</tr>
	  		<tr>
	  			<td><a class="white" href="/CS5200_Project/logManagement.jsp">Log Management</a></td>
	  			<td>Query Log Messages for a Cluster</td>
	  		</tr>
	  	</table>
	</div>
</body>
</html>
