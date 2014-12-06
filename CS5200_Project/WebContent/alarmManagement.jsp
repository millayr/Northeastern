<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Alarm/Alert Management</title>
<link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
	<div>
		<a href="/CS5200_Project">Home</a>
		<h1 style="text-align:center">Alarm/Alert Management</h1>
		<h3>Operations</h3>
		<div class="tile">
			<ul>
				<li>
					<form action="/CS5200_Project/AlertServlet" method="POST">
						<p><u>List All Alarms/Alerts in the Database:</u></p>
						<input type="hidden" name="type" value="showAll" />
						<input type="submit" value="SHOW ALL ALERTS!" />
					</form>
				</li>
				<li>
					<form action="/CS5200_Project/AlertServlet" method="POST">
						<p><u>Show Alarms/Alerts for a Specific Cluster:</u></p>
						<table class="input">
							<tr>
								<td>Cluster Name:</td>
								<td><input type="text" name="clusterName" /></td>
							</tr>
						</table>
						<input type="hidden" name="type" value="byCluster" />
						<input type="submit" value="SEARCH!" />
					</form>
				</li>
				<li>
					<form action="/CS5200_Project/AlertServlet" method="POST">
						<p><u>Enable an Alarm/Alert for a Specific Cluster:</u></p>
						<table class="input">
							<tr>
								<td>Cluster Name:</td>
								<td><input type="text" name="clusterName" /></td>
							</tr>
							<tr>
								<td>Alarm/Alert Id:</td>
								<td><input type="text" name="alertName" /></td>
							</tr>
						</table>
						<input type="hidden" name="type" value="enableAlert" />
						<input type="submit" value="ENABLE!" />	
					</form>
				</li>
				<li>
					<form action="/CS5200_Project/AlertServlet" method="POST">
						<p><u>Disable an Alarm/Alert for a Specific Cluster:</u></p>
						<table class="input">
							<tr>
								<td>Cluster Name:</td>
								<td><input type="text" name="clusterName" /></td>
							</tr>
							<tr>
								<td>Alarm/Alert Id:</td>
								<td><input type="text" name="alertName" /></td>
							</tr>
						</table>
						<input type="hidden" name="type" value="disableAlert" />
						<input type="submit" value="DISABLE!" />	
					</form>
				</li>
				<li>
					<form action="/CS5200_Project/AlertServlet" method="POST">
						<p><u>Create New Alarm/Alert:</u></p>
						<table class="input">
							<tr>
								<td>Alarm/Alert Name:</td>
								<td><input type="text" name="alertName" /></td>
							</tr>
							<tr>
								<td>Threshold:</td>
								<td><input type="text" name="threshold" /></td>
							</tr>
							<tr>
								<td>Apply to All Clusters?</td>
								<td>
									<input type="radio" name="applyToAllClusters" value="Yes" checked />Yes
									<input type="radio" name="applyToAllClusters" value="No" />No
								</td>
						</table>
						<input type="hidden" name="type" value="newAlert" />
						<input type="submit" value="CREATE!" />	
					</form>
				</li>
			</ul>
		</div>
		<% if(request.getAttribute("output") != null) { %>
			<div class="tile noMargin"><%= request.getAttribute("output") %></div>
		<% } %>
	</div>
</body>
</html>