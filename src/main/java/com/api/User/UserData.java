package com.api.User;

import com.api.Database.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserData {
    public static int getID(String userName) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        Connection connection = databaseConnection.getConnection();
        if (connection == null)
            throw new SQLException("Could not make connection to DB");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from accounts where username='" + userName + "'");
        DatabaseConnection.getInstance().releaseConnection(connection);
        if (resultSet.next())
            return resultSet.getInt("ID");
        throw new RuntimeException("User ID was not found");
    }

    public static List<Integer> getLikes(int userID) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        Connection connection = databaseConnection.getConnection();
        if (connection == null)
            throw new SQLException("Could not make connection to DB");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from likes where userID = " + "'" + userID + "'");
        List<Integer> likes = new ArrayList<>();
        while (resultSet.next())
            likes.add(resultSet.getInt("postID"));
        return likes;

    }

    public static List<Integer> getDislikes(int userID) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        Connection connection = databaseConnection.getConnection();
        if (connection == null)
            throw new SQLException("Could not make connection to DB");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from dislikes where userID = " + userID);
        List<Integer> dislikes = new ArrayList<>();
        while (resultSet.next())
            dislikes.add(resultSet.getInt("postID"));
        return dislikes;
    }

    public static List<Integer> getFavourites(int userID) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        Connection connection = databaseConnection.getConnection();
        if (connection == null)
            throw new SQLException("Could not make connection to DB");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from favourites where userID = " + userID);
        List<Integer> favourites = new ArrayList<>();
        while (resultSet.next())
            favourites.add(resultSet.getInt("postID"));
        return favourites;
    }
}
