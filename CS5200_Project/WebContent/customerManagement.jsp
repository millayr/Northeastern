<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Customer Management</title>
<link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
	<div>
		<a href="/CS5200_Project">Home</a>
		<h1 style="text-align:center">Customer Management</h1>
		<h3>Operations</h3>
		<div class="tile">
			<ul>
				<li>
					<form action="/CS5200_Project/QueryCustomersServlet" method="POST">
						<p><u>List All Customers in the Database:</u></p>
						<input type="hidden" name="type" value="show" />
						<input type="submit" value="SHOW ALL CUSTOMERS!" />
					</form>
				</li>
				<li>
					<form action="/CS5200_Project/QueryCustomersServlet" method="POST">
						<p><u>Search for a Customer in the Database:</u></p>
						<table class="input">
							<tr>
								<td>Customer Name:</td>
								<td><input type="text" name="customer" /></td>
							</tr>
						</table>
						<input type="hidden" name="type" value="search" />
						<input type="submit" value="SEARCH!" />
					</form>
				</li>
				<li>
					<form action="/CS5200_Project/NewCustomerServlet" method="POST">
						<p><u>Add a New Customer to the Database:</u></p>
						<table class="input">
							<tr>
								<td>Customer Name*:</td>
								<td><input type="text" name="newCustomer" /></td>
							</tr>
							<tr>
								<td>Technical Account Manager:</td>
								<td><input type="text" name="tam" /></td>
							</tr>
							<tr>
								<td>Cluster Name:</td>
								<td><input type="text" name="cluster" /></td>
							</tr>
							<tr>
								<td>Cluster Size:</td>
								<td><input type="text" name="size" /></td>
							</tr>
							<tr>
								<td>Data Center:</td>
								<td>
									<select name="datacenter">
										<option value="SoftLayer">SoftLayer</option>
										<option value="AWS">AWS</option>
										<option value="Rackspace">Rackspace</option>
										<option value="Azure">Azure</option>
									</select>
								</td>
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
						<input type="hidden" name="type" value="search" />
						<input type="submit" value="ADD!" />	
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