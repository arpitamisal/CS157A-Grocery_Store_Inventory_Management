package application;

import java.sql.Connection;
import java.sql.DriverManager;


public class TestMySQLConnection {
	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/cs157_hw3";
		String username = "root";
		String password = "Beach321";
		
		try(Connection connection = DriverManager.getConnection(url, username, password)) {
			System.out.println("Connected to the database successfully!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
