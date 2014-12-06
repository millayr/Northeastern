package com.nu.tools;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TicketOps {
	// grab the db connection instance
	private DbConnection db = DbConnection.getInstance();
	
	public String showTickets() throws Exception {
		String output = "";
		
		// build the sql query
		String sql = "select t.id, t.title, p.name, s.name, t.opened, t.message, e.name, cl.name, c.name " 
				+ "from SupportTicket t, Priorities p, Status s, Employee e, Cluster cl, Customer c "
				+ "where t.priority = p.id and t.status = s.id and t.assignedTo = e.id and t.pertainsTo = cl.id and t.openedBy = c.id";
		PreparedStatement query = db.getConnection().prepareStatement(sql);
		ResultSet results = query.executeQuery();
		
		output += "<table class=\"tickets\"><tr><th>Ticket Id</th><th>Title</th><th>Priority</th><th>Status</th><th>Opened</th>"
				+ "<th>Contents</th><th>Assigned To</th><th>Cluster</th><th>Customer</th></tr>";
		
		// iterate over the results and build the table
		while(results.next()){
			output += "<tr>";
			output += "<td>" + results.getInt(1) + "</td>";
			output += "<td>" + results.getString(2) + "</td>";
			output += "<td>" + results.getString(3) + "</td>";
			output += "<td>" + results.getString(4) + "</td>";
			output += "<td>" + results.getString(5) + "</td>";
			output += "<td>" + results.getString(6) + "</td>";
			output += "<td>" + results.getString(7) + "</td>";
			output += "<td>" + results.getString(8) + "</td>";
			output += "<td>" + results.getString(9) + "</td>";
			output += "</tr>";
 		}
		query.close();
		
		output += "</table>";
		
		// return the table
		return output;
	}
	
	
	public String queryTicketsByCustomer(String customerName) throws Exception {
		String output = "";
		
		// build the sql query
		String sql = "select t.id, t.title, p.name, s.name, t.opened, t.message, e.name, cl.name, c.name " 
				+ "from SupportTicket t, Priorities p, Status s, Employee e, Cluster cl, Customer c "
				+ "where t.priority = p.id and t.status = s.id and t.assignedTo = e.id and t.pertainsTo = cl.id and t.openedBy = c.id "
				+ "and c.name = ?";
		PreparedStatement query = db.getConnection().prepareStatement(sql);
		query.setString(1, customerName);
		ResultSet results = query.executeQuery();
		
		output += "<table class=\"tickets\"><tr><th>Ticket Id</th><th>Title</th><th>Priority</th><th>Status</th><th>Opened</th>"
				+ "<th>Contents</th><th>Assigned To</th><th>Cluster</th><th>Customer</th></tr>";
		
		// iterate over the results and build the table
		while(results.next()){
			output += "<tr>";
			output += "<td>" + results.getInt(1) + "</td>";
			output += "<td>" + results.getString(2) + "</td>";
			output += "<td>" + results.getString(3) + "</td>";
			output += "<td>" + results.getString(4) + "</td>";
			output += "<td>" + results.getString(5) + "</td>";
			output += "<td>" + results.getString(6) + "</td>";
			output += "<td>" + results.getString(7) + "</td>";
			output += "<td>" + results.getString(8) + "</td>";
			output += "<td>" + results.getString(9) + "</td>";
			output += "</tr>";
 		}
		query.close();
		
		output += "</table>";
		
		// return the table
		return output;
	}
	
	
	public String queryTicketsById(String ticketId) throws Exception {
		String output = "";
		
		// convert the id to an int
		int ticketIdNum = Integer.parseInt(ticketId);
		
		// build the sql query
		String sql = "select t.id, t.title, p.name, s.name, t.opened, t.message, e.name, cl.name, c.name " 
				+ "from SupportTicket t, Priorities p, Status s, Employee e, Cluster cl, Customer c "
				+ "where t.priority = p.id and t.status = s.id and t.assignedTo = e.id and t.pertainsTo = cl.id and t.openedBy = c.id "
				+ "and t.id = ?";
		PreparedStatement query = db.getConnection().prepareStatement(sql);
		query.setInt(1, ticketIdNum);
		ResultSet results = query.executeQuery();
		
		output += "<table class=\"tickets\"><tr><th>Ticket Id</th><th>Title</th><th>Priority</th><th>Status</th><th>Opened</th>"
				+ "<th>Contents</th><th>Assigned To</th><th>Cluster</th><th>Customer</th></tr>";
		
		// iterate over the results and build the table
		while(results.next()){
			output += "<tr>";
			output += "<td>" + results.getInt(1) + "</td>";
			output += "<td>" + results.getString(2) + "</td>";
			output += "<td>" + results.getString(3) + "</td>";
			output += "<td>" + results.getString(4) + "</td>";
			output += "<td>" + results.getString(5) + "</td>";
			output += "<td>" + results.getString(6) + "</td>";
			output += "<td>" + results.getString(7) + "</td>";
			output += "<td>" + results.getString(8) + "</td>";
			output += "<td>" + results.getString(9) + "</td>";
			output += "</tr>";
 		}
		query.close();
		
		output += "</table>";
		
		// return the table
		return output;
	}
	
	
	public void resolveTicket(String ticketId) throws Exception {
		// convert the id to an int
		int ticketIdNum = Integer.parseInt(ticketId);
		
		String sql = "update SupportTicket set status = 2 where id = ?";
		PreparedStatement update = db.getConnection().prepareStatement(sql);
		update.setInt(1, ticketIdNum);
		update.executeUpdate();
		update.close();
	}
}
