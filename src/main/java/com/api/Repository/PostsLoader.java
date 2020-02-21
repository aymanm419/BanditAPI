package com.api.Repository;

import com.api.Database.DatabaseConnection;
import com.api.Model.Post;
import com.api.Model.Song;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PostsLoader {
    public static ArrayList<Post> loadPosts(int postStart, int postsEnd) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        Connection connection = databaseConnection.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from posts ORDER BY (post_upvote-post_downvote) DESC LIMIT " +
                (postsEnd - postStart + 1) + " OFFSET " + (postStart - 1));
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
}
