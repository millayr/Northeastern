package com.nu.tools;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection {
	private static String driver = "com.mysql.jdbc.Driver";
	private static String uname = "root";
	private static String pwd = "not4long";
	private static String db = "jdbc:mysql://localhost/cs5200_project";
	private Connection conn;
	private static DbConnection instance = null;
	
	protected DbConnection() {
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(db, uname, pwd);
		} catch (Exception e) {
			System.out.println("Database connection failed to initialize: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static DbConnection getInstance() {
		if(instance == null) {
			instance = new DbConnection();
		}
		return instance;
	}
	
	public Connection getConnection() {
		return conn;
	}
}
