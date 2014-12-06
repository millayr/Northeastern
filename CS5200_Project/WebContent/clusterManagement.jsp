<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Cluster Management</title>
<link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
	<div>
		<a href="/CS5200_Project">Home</a>
		<h1 style="text-align:center">Cluster Management</h1>
		<h3>Operations</h3>
		<div class="tile">
			<ul>
				<li>
					<form action="/CS5200_Project/QueryClustersServlet" method="POST">
						<p><u>List All Clusters in the Database:</u></p>
						<input type="hidden" name="type" value="showAll" />
						<input type="submit" value="SHOW ALL CLUSTERS!" />
					</form>
				</li>
				<li>
					<form action="/CS5200_Project/QueryClustersServlet" method="POST">
						<p><u>Show Statistics for a Specific Cluster:</u></p>
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
					<form action="/CS5200_Project/ModifyClusterServlet" method="POST">
						<p><u>Add a New Node to a Cluster:</u></p>
						<table class="input">
							<tr>
								<td>Cluster Name:</td>
								<td><input type="text" name="clusterName" /></td>
							</tr>
							<tr>
								<td>Node Number Prefix:</td>
								<td><input type="text" name="nodeName" /></td>
							</tr>
							<tr>
								<td>Node Vendor:</td>
								<td>
									<select name="vendor">
										<option value="DELL">DELL</option>
										<option value="IBM">IBM</option>
										<option value="Cisco">Cisco</option>
									</select>
								</td>
							</tr>
						</table>
						<input type="hidden" name="type" value="addNode" />
						<input type="submit" value="ADD!" />	
					</form>
				</li>
				<li>
					<form action="/CS5200_Project/ModifyClusterServlet" method="POST">
						<p><u>Delete a Node from a Cluster:</u></p>
						<table class="input">
							<tr>
								<td>Cluster Name:</td>
								<td><input type="text" name="clusterName" /></td>
							</tr>
							<tr>
								<td>Node Name:</td>
								<td><input type="text" name="nodeName" /></td>
							</tr>
						</table>
						<input type="hidden" name="type" value="deleteNode" />
						<input type="submit" value="DELETE!" />	
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