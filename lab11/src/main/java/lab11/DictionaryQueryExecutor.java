package main.java.lab11;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DictionaryQueryExecutor {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/dictionary";
    private static final String DB_USER = "username";
    private static final String DB_PASSWORD = "password";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public String getTranslation(String word, String language) {
        String translation = null;
        String query = "SELECT translation FROM words WHERE word = ? AND language = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, word);
            statement.setString(2, language);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    translation = resultSet.getString("translation");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return translation;
    }

    public void addWord(String word, String translation, String language) {
        String query = "INSERT INTO words (word, translation, language) VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, word);
            statement.setString(2, translation);
            statement.setString(3, language);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}