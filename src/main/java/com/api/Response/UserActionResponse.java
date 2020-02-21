package com.api.Response;

import com.api.Database.DatabaseConnection;
import com.api.Request.RequestValidation;
import com.api.User.UserData;
import spark.Request;
import spark.Response;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

import static com.api.Main.gson;

public class UserActionResponse {
    public static final RequestValidation requestValidation = new RequestValidation();

    public static String changePostLike(Request request, Response response) {
        Set<String> queryParams = request.queryParams();
        if (requestValidation.isParametersMissing(queryParams, "post", "username", "change"))
            return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Invalid Request. Missing parameters"));
        String postID = request.params("post");
        String userName = request.params("username");
        int change = Integer.parseInt(request.params("change"));
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        try {
            Connection connection = databaseConnection.getConnection();
            Statement statement = connection.createStatement();
            statement.execute("UPDATE posts SET " + "post_upvote" + " = " + "post_upvote" + " + " + change + " WHERE post_id = " + postID + ";");
            if (change == 1)
                statement.execute("INSERT INTO " + "likes" + " VALUES (" + UserData.getID(userName) + "," + postID + ");");
            else
                statement.execute("DELETE FROM " + "likes" + " WHERE ID = " + UserData.getID(userName) + " AND postID = " + postID + ";");
            DatabaseConnection.getInstance().releaseConnection(connection);
            return gson.toJson(new StandardResponse(StatusResponse.SUCCESS));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
            e.printStackTrace();
            return gson.toJson(new StandardResponse(StatusResponse.ERROR, e.getMessage()));
        }
    }

    public static String changePostDislike(Request request, Response response) {
        Set<String> queryParams = request.queryParams();
        if (requestValidation.isParametersMissing(queryParams, "post", "username", "change"))
            return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Invalid Request. Missing parameters"));
        String postID = request.params("post");
        String userName = request.params("username");
        int change = Integer.parseInt(request.params("change"));
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        try {
            Connection connection = databaseConnection.getConnection();
            Statement statement = connection.createStatement();
            statement.execute("UPDATE posts SET " + "post_downvote" + " = " + "post_downvote" + " + " + change + " WHERE post_id = " + postID + ";");
            if (change == 1)
                statement.execute("INSERT INTO " + "dislikes" + " VALUES (" + UserData.getID(userName) + "," + postID + ");");
            else
                statement.execute("DELETE FROM " + "dislikes" + " WHERE ID = " + UserData.getID(userName) + " AND postID = " + postID + ";");
            DatabaseConnection.getInstance().releaseConnection(connection);
            return gson.toJson(new StandardResponse(StatusResponse.SUCCESS));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
            e.printStackTrace();
            return gson.toJson(new StandardResponse(StatusResponse.ERROR, e.getMessage()));
        }
    }

    public static String changePostFavourite(Request request, Response response) {
        Set<String> queryParams = request.queryParams();
        if (requestValidation.isParametersMissing(queryParams, "post", "username", "change"))
            return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Invalid Request. Missing parameters"));
        String postID = request.params("post");
        String userName = request.params("username");
        int change = Integer.parseInt(request.params("change"));
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        try {
            Connection connection = databaseConnection.getConnection();
            Statement statement = connection.createStatement();
            if (change == 1)
                statement.execute("INSERT INTO favourites VALUES(" + UserData.getID(userName) + " ," + postID + ")");
            else
                statement.execute("DELETE FROM favourites WHERE ID = " + UserData.getID(userName) + " AND postID = " + postID);
            DatabaseConnection.getInstance().releaseConnection(connection);
            return gson.toJson(new StandardResponse(StatusResponse.SUCCESS));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
            e.printStackTrace();
            return gson.toJson(new StandardResponse(StatusResponse.ERROR, e.getMessage()));
        }
    }
}
