package com.nu.tools;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.nu.containers.Employee;

public class EmployeeOps {
	// grab the db connection instance
	private DbConnection db = DbConnection.getInstance();
	
	public Employee getTAMByName(String name) throws Exception {
		String sql = "select t.id, e.name, e.location from Employee e, TechnicalAccountManager t "
				+ "where e.id = t.id and e.name = ?";
		
		PreparedStatement query = db.getConnection().prepareStatement(sql);
		query.setString(1, name);
		
		ResultSet result = query.executeQuery();
		Employee employee = null;
		
		if(result.next()) {
			employee = new Employee();
			employee.id = result.getInt(1);
			employee.name = result.getString(2);
			employee.location = result.getString(3);
		} else {
			throw new Exception("No Technical Account Manager found with name " + name);
		}
		query.close();
		
		return employee;
	}
}
