package com.lazyproductions.appserver.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBase {

	private final String url = "jdbc:postgresql://localhost:5432/User";
	private final String user = "postgres";
	private final String password = "password";

	public Connection connect() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			System.out.println("Connected to the PostgreSQL server successfully.");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return conn;
	}

	public boolean doesUserExist(String username) {

		int totalFound = 0;
		String SQL = "SELECT COUNT(*) AS total FROM Users WHERE UserName=?";
		try (Connection conn = connect(); PreparedStatement statement = conn.prepareStatement(SQL)) {
			statement.setString(1, username);
			ResultSet rs = statement.executeQuery();
			rs.next();
			totalFound = rs.getInt("total");
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		return totalFound > 0;

	}

	public boolean validateUser(String username, String password) {
		String SQL = "SELECT Count(*) AS total FROM users WHERE username =? AND password=?";
		try (Connection conn = connect(); PreparedStatement statement = conn.prepareStatement(SQL);) {

			statement.setString(1, username);
			statement.setString(2, password);

			ResultSet result = statement.executeQuery();
			result.next();
			return result.getInt("total") == 1;
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}

	public void registerUser(String username, String password) {
		String SQL = "INSERT INTO users(username,password) " + "VALUES(?,?)";
		try (Connection conn = connect(); PreparedStatement statement = conn.prepareStatement(SQL);) {

			statement.setString(1, username);
			statement.setString(2, password);

			statement.execute();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());

		}
	}
}
