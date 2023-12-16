package main.java.lab11;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "dictionary";
    private static final String DB_USER = "vshevtsov";
    private static final String DB_PASSWORD = "vshevtsov";

    public void createDatabase() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {
            String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            statement.executeUpdate(createDatabaseQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTables() {
        try (Connection connection = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS words (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "word VARCHAR(255)," +
                    "translation VARCHAR(255)," +
                    "language VARCHAR(50)" +
                    ")";
            statement.executeUpdate(createTableQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}