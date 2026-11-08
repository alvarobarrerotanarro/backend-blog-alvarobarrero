package com.alvarobarrero.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	private static Connection conn;

	static {
		String sqliteFile = System.getenv("SQLITE_FILE");
		if (sqliteFile == null) {
			sqliteFile = "/data/data.db";
		}
		String connURL = "jdbc:sqlite:" + sqliteFile;
		try {
			System.out.println("Connecting to database: " + connURL);
			conn = DriverManager.getConnection(connURL);
			System.out.println("Connected to database.");
		} catch (SQLException e) {
			System.err.println("Could not open connection to database: " + e.getMessage());
			System.exit(1);
		}
	}

	public static Connection get() {
		return conn;
	}
}
