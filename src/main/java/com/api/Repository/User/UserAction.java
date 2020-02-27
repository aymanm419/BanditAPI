package com.api.Repository.User;

import com.api.Database.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class UserAction {
    public static boolean changePostLike(int userID, int postID, int change) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        Connection connection = databaseConnection.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("UPDATE posts SET " + "post_upvote" + " = " + "post_upvote" + " + " + change + " WHERE post_id = " + postID + ";");
        if (change == 1)
            statement.execute("INSERT INTO " + "likes" + " VALUES (" + userID + "," + postID + ");");
        else
            statement.execute("DELETE FROM " + "likes" + " WHERE userID = " + userID + " AND postID = " + postID + ";");
        DatabaseConnection.getInstance().releaseConnection(connection);
        return true;
    }

    public static boolean changePostDislike(int userID, int postID, int change) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        Connection connection = databaseConnection.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("UPDATE posts SET " + "post_downvote" + " = " + "post_downvote" + " + " + change + " WHERE post_id = " + postID + ";");
        if (change == 1)
            statement.execute("INSERT INTO " + "dislikes" + " VALUES (" + userID + "," + postID + ");");
        else
            statement.execute("DELETE FROM " + "dislikes" + " WHERE userID = " + userID + " AND postID = " + postID + ";");
        DatabaseConnection.getInstance().releaseConnection(connection);
        return true;
    }

    public static boolean changePostFavourite(int userID, int postID, int change) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        Connection connection = databaseConnection.getConnection();
        Statement statement = connection.createStatement();
        if (change == 1)
            statement.execute("INSERT INTO favourites VALUES(" + userID + " ," + postID + ")");
        else
            statement.execute("DELETE FROM favourites WHERE userID = " + userID + " AND postID = " + postID);
        DatabaseConnection.getInstance().releaseConnection(connection);
        return true;
    }
}
