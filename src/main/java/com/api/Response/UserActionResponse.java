package com.api.Response;

import com.api.User.UserData;
import com.api.User.UserValidation;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

import static com.api.Main.gson;

public class UserActionResponse {

    public static String doData(Request request, Response response) {
        String parameter = request.params(":action");
        String username = request.params(":username");
        try {
            if (!UserValidation.userExists(username))
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Username already exists"));
            int postID = Integer.parseInt(request.queryParams("postid"));
            int userID = UserData.getID(username);
            int change = Integer.parseInt(request.queryParams("change"));
            switch (parameter) {
                case "favourite":
                    return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, gson.toJson(UserData.getID(username))));
                case "like":
                    return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, gson.toJson(UserData.getLikes(username))));
                case "dislike":
                    return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, gson.toJson(UserData.getDislikes(username))));
                case "favourites":
                    return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, gson.toJson(UserData.getFavourites(username))));
                default:
                    return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Client asked for invalid path route"));
            }
        } catch (InstantiationException | SQLException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            return gson.toJson(new StandardResponse(StatusResponse.ERROR, e.toString()));
        }
    }
}
