package com.alvarobarrero;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	private static Connection conn;

	static {
		String connURL = "jdbc:sqlite:/data/data.db";
		try {
			conn = DriverManager.getConnection(connURL);
		} catch (SQLException e) {
			System.err.println("Could not open connection to database: " + e.getMessage());
			System.exit(1);
		}
	}

	public static Connection get() {
		return conn;
	}
}
