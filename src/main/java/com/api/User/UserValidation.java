package com.api.User;

import com.api.Database.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserValidation {
    public static boolean userExists(String userName) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        if (connection == null)
            throw new SQLException("Could not connect to database");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from accounts where username='" + userName + "'");
        boolean success = resultSet.next();
        resultSet.close();
        DatabaseConnection.getInstance().releaseConnection(connection);
        return success;
    }
    public static boolean validateAccountCredentials(String username,String password) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        Connection connection = databaseConnection.getConnection();
        if (connection == null)
            throw new SQLException("Could not make connection to DB");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from accounts where username='" + username + "'");
        boolean success = false;
        while (resultSet.next()) {
            success = resultSet.getString("password").equals(password);
            if(success)
                break;
        }
        resultSet.close();
        databaseConnection.releaseConnection(connection);
        return success;
    }
    public static void createAccount(String username,String password) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO accounts(username, password) VALUES ('" + username + "','" + password + "')");
        DatabaseConnection.getInstance().releaseConnection(connection);
    }
}
