package com.api.Repository.User;

import com.api.Database.DatabaseConnection;
import com.api.Model.Post;
import com.api.Model.Song;

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

    private static List<Integer> getLikesID(int userID) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
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

    private static List<Integer> getDislikesID(int userID) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
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

    private static List<Integer> getFavouritesID(int userID) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
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

    public static List<Post> getPostsByIDs(List<Integer> postsIDs) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        if (postsIDs.isEmpty())
            return new ArrayList<>();
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        Connection connection = databaseConnection.getConnection();
        Statement statement = connection.createStatement();
        StringBuilder query = new StringBuilder("select * from posts WHERE ");
        for (int i = 0; i < postsIDs.size() - 1; i++) {
            query.append("post_id = ").append(postsIDs.get(i)).append(" OR ");
        }
        query.append("post_id = ").append(postsIDs.get(postsIDs.size() - 1));
        ResultSet resultSet = statement.executeQuery(query.toString());
        ArrayList<Post> postsLoaded = new ArrayList<>();
        while (resultSet.next()) {
            Song song = new Song(resultSet.getString("post_song_name"),
                    resultSet.getString("post_album_name"),
                    resultSet.getString("post_song_dir"));
            Post post = new Post(resultSet.getInt("post_id"), song, resultSet.getString("post_picture_dir"));
            postsLoaded.add(post);
        }
        resultSet.close();
        databaseConnection.releaseConnection(connection);
        return postsLoaded;
    }

    public static List<Post> getFavourites(int userID) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        return getPostsByIDs(getFavouritesID(userID));
    }

    public static List<Post> getLikes(int userID) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        return getPostsByIDs(getLikesID(userID));
    }

    public static List<Post> getDislikes(int userID) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        return getPostsByIDs(getDislikesID(userID));
    }
}
