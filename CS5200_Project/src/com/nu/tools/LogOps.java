package com.nu.tools;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LogOps {
	// grab the db connection instance
	private DbConnection db = DbConnection.getInstance();
	
	public String showLogs(String clusterName) throws Exception {
		String output = "";
		
		// create the sql query
		String sql = "select c.name, n.name, l.message, l.created "
				+ "from logstatement l, cluster c, node n "
				+ "where l.createdBy = n.id and n.belongsTo = c.id and c.name = ?";
		PreparedStatement query = db.getConnection().prepareStatement(sql);
		query.setString(1, clusterName);
		ResultSet results = query.executeQuery();
		
		// iterate over all of the results returned
		output = "<table><tr><th>Cluster Name</th><th>Node Name</th><th>Message</th><th>Date Created</th></tr>";
		while(results.next()) {
			output += "<tr>";
			output += "<td>" + results.getString(1) + "</td>";
			output += "<td>" + results.getString(2) + "</td>";
			output += "<td>" + results.getString(3) + "</td>";
			output += "<td>" + results.getString(4) + "</td>";
			output += "</tr>";
		}
		query.close();
		
		// return the html table
		return output;
	}
}
