package com.itbulls.learnit.onlinestore.persistence.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.itbulls.learnit.onlinestore.persistence.utils.connectionpools.DbcpConnectionPool;

public class DBUtils {
	
	private DBUtils() {
	}
	
	public static Connection getConnection() {
		try {
			return DbcpConnectionPool.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
