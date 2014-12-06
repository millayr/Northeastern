package com.nu.tools;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.nu.containers.Customer;

public class CustomerOps {	
	// grab the db connection instance
	private DbConnection db = DbConnection.getInstance();
	
	public String showCustomers() throws Exception {
		// Initialize the output string
		String output = "";
		
		// build the sql query
		String sql = "select c.id, c.name, c.since, cl.name, cl.datacenter, cl.clusterSize, e.name "
				+ "from Customer c "
				+ "left join Cluster cl on cl.ownedBy = c.id "
				+ "left join TechnicalAccountManager t on c.consultsWith = t.id "
				+ "left join Employee e on t.id = e.id";
		
		// create a prepared statment with the query
		PreparedStatement query = db.getConnection().prepareStatement(sql);
		
		// execute the query
		ResultSet results = query.executeQuery();
		
		// start building the output table
		output = "<table><tr><th>ID</th><th>Company Name</th><th>Customer Since</th>"
				+ "<th>Cluster Name</th><th>Data Center</th><th>Cluster Size</th><th>Technical Account Manager</th></tr>";
		while(results.next()) {
			int id = results.getInt(1);
			String customerName = results.getString(2);
			String date = results.getString(3);
			String cluster = results.getString(4);
			String datacenter = results.getString(5);
			String size = results.getString(6);
			String tam = results.getString(7);
			output += "<tr><td>" + id + "</td><td>" + customerName + "</td><td>" + date + "</td>"
					+ "<td>" + cluster + "</td><td>" + datacenter + "</td><td>" + size + "</td>"
					+ "<td>" + tam + "</td></tr>";
		}
		output += "</table>";
		query.close();
		
		// return the table
		return output;
	}
	
	public String queryCustomers(String customerName) throws Exception {
		// Initialize the output string
		String output = "";
		
		// build the sql query
		String sql = "select c.id, c.name, c.since, cl.name, cl.datacenter, cl.clusterSize, e.name "
				+ "from Customer c "
				+ "left join Cluster cl on cl.ownedBy = c.id "
				+ "left join TechnicalAccountManager t on c.consultsWith = t.id "
				+ "left join Employee e on t.id = e.id "
				+ "where c.name like ?";
		
		// create a prepared statement with the query and insert the customer
		PreparedStatement query = db.getConnection().prepareStatement(sql);
		query.setString(1, customerName);
		
		// execute the query
		ResultSet results = query.executeQuery();
		
		// start building the output table
		output = "<table><tr><th>ID</th><th>Company Name</th><th>Customer Since</th>"
				+ "<th>Cluster Name</th><th>Data Center</th><th>Cluster Size</th><th>Technical Account Manager</th></tr>";
		while(results.next()) {
			int id = results.getInt(1);
			String customer = results.getString(2);
			String date = results.getString(3);
			String cluster = results.getString(4);
			String datacenter = results.getString(5);
			String size = results.getString(6);
			String tam = results.getString(7);
			output += "<tr><td>" + id + "</td><td>" + customer + "</td><td>" + date + "</td>"
					+ "<td>" + cluster + "</td><td>" + datacenter + "</td><td>" + size + "</td>"
					+ "<td>" + tam + "</td></tr>";
		}
		output += "</table>";
		query.close();
		
		//return the table
		return output;
	}
	
	
	public int addNewCustomer(Customer c) throws Exception {
		try {
			// make use of transactions
			db.getConnection().setAutoCommit(false);
			
			String sql = null;
			// having a tam isn't required
			PreparedStatement insert;
			if(c.tam != null) {
				sql = "insert into Customer(name, since, consultsWith) values (?, ?, ?)";
				insert = db.getConnection().prepareStatement(sql);
				insert.setString(1, c.name);
				insert.setString(2, c.since);
				insert.setInt(3, c.tam.id);
			} else {
				sql = "insert into Customer(name, since) values (?, ?)";
				insert = db.getConnection().prepareStatement(sql);
				insert.setString(1, c.name);
				insert.setString(2, c.since);
			}
			insert.executeUpdate();
			insert.close();
			
			// get the auto_incremented Id
			PreparedStatement query = db.getConnection().prepareStatement("select last_insert_id() from Customer");
			ResultSet results = query.executeQuery();
			db.getConnection().commit();
			results.next();
			int id = results.getInt(1);
			query.close();

			return id;
		} catch (Exception e) {
			throw e;
		} finally {
			db.getConnection().setAutoCommit(true);
		}
	}
}
