<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Ticket Management</title>
<link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
	<div>
		<a href="/CS5200_Project">Home</a>
		<h1 style="text-align:center">Ticket Management</h1>
		<h3>Operations</h3>
		<div class="tile">
			<ul>
				<li>
					<form action="/CS5200_Project/QueryTicketsServlet" method="POST">
						<p><u>List All Tickets in the Database:</u></p>
						<input type="hidden" name="type" value="showAll" />
						<input type="submit" value="SHOW ALL TICKETS!" />
					</form>
				</li>
				<li>
					<form action="/CS5200_Project/QueryTicketsServlet" method="POST">
						<p><u>Show Tickets for a Specific Customer:</u></p>
						<table class="input">
							<tr>
								<td>Customer Name:</td>
								<td><input type="text" name="customerName" /></td>
							</tr>
						</table>
						<input type="hidden" name="type" value="byCustomer" />
						<input type="submit" value="SEARCH!" />
					</form>
				</li>
				<li>
					<form action="/CS5200_Project/QueryTicketsServlet" method="POST">
						<p><u>Find a Specific Ticket by Id:</u></p>
						<table class="input">
							<tr>
								<td>Ticket Id:</td>
								<td><input type="text" name="ticketId" /></td>
							</tr>
						</table>
						<input type="hidden" name="type" value="byId" />
						<input type="submit" value="SEARCH!" />	
					</form>
				</li>
				<li>
					<form action="/CS5200_Project/ResolveTicketServlet" method="POST">
						<p><u>Resolve a Ticket by Id:</u></p>
						<table class="input">
							<tr>
								<td>Ticket Id:</td>
								<td><input type="text" name="ticketId" /></td>
							</tr>
						</table>
						<input type="submit" value="RESOLVE!" />	
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