<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Database Management</title>
<link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
	<div>
		<a href="/CS5200_Project">Home</a>
		<h1 style="text-align:center">Database Management</h1>
		<h3>Operations</h3>
		<div class="tile">
			<ul>
				<li>
					<form action="/CS5200_Project/QueryDatabasesServlet" method="POST">
						<p><u>List All Databases kept in the MDM:</u></p>
						<input type="hidden" name="type" value="showAll" />
						<input type="submit" value="SHOW ALL DATABASES!" />
					</form>
				</li>
				<li>
					<form action="/CS5200_Project/QueryDatabasesServlet" method="POST">
						<p><u>Show Shards for a Specific Cluster:</u></p>
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
					<form action="/CS5200_Project/QueryDatabasesServlet" method="POST">
						<p><u>Update the Default Shard Count for a Database:</u></p>
						<table class="input">
							<tr>
								<td>Database Name:</td>
								<td><input type="text" name="databaseName" /></td>
							</tr>
							<tr>
								<td>Cluster Name:</td>
								<td><input type="text" name="clusterName" /></td>
							</tr>
							<tr>
								<td>New Shard Count:</td>
								<td><input type="text" name="shardCount" /></td>
							</tr>
						</table>
						<input type="hidden" name="type" value="updateShardCount" />
						<input type="submit" value="UPDATE!" />
					</form>
				</li>
				<li>
					<form action="/CS5200_Project/QueryDatabasesServlet" method="POST">
						<p><u>List Indexes for a Database:</u></p>
						<table class="input">
							<tr>
								<td>Database Name:</td>
								<td><input type="text" name="databaseName" /></td>
							</tr>
							<tr>
								<td>Cluster Name:</td>
								<td><input type="text" name="clusterName" /></td>
							</tr>
						</table>
						<input type="hidden" name="type" value="viewIndexes" />
						<input type="submit" value="LIST INDEXES!" />
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