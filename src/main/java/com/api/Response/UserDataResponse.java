package com.api.Response;

import com.api.User.UserData;
import com.api.User.UserValidation;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

import static com.api.Main.gson;

public class UserDataResponse {
    public static String getUserID(Request request, Response response) {
        String username = request.queryParams("username");
        try {
            if (!UserValidation.userExists(username))
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Username does not exist."));
            return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, gson.toJson(UserData.getID(username))));
        } catch (InstantiationException | SQLException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            return gson.toJson(new StandardResponse(StatusResponse.ERROR, e.getMessage()));
        }
    }

    public static String getUserLikes(Request request, Response response) {
        String username = request.queryParams("username");
        try {
            if (!UserValidation.userExists(username))
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Username does not exist."));
            return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, gson.toJson(UserData.getLikes(username))));
        } catch (InstantiationException | SQLException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            return gson.toJson(new StandardResponse(StatusResponse.ERROR, e.getMessage()));
        }
    }

    public static String getUserDislikes(Request request, Response response) {
        String username = request.queryParams("username");
        try {
            if (!UserValidation.userExists(username))
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Username does not exist."));
            return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, gson.toJson(UserData.getDislikes(username))));
        } catch (InstantiationException | SQLException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            return gson.toJson(new StandardResponse(StatusResponse.ERROR, e.getMessage()));
        }
    }

    public static String getUserFavourites(Request request, Response response) {
        String username = request.queryParams("username");
        try {
            if (!UserValidation.userExists(username))
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Username does not exist."));
            return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, gson.toJson(UserData.getFavourites(username))));
        } catch (InstantiationException | SQLException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            return gson.toJson(new StandardResponse(StatusResponse.ERROR, e.getMessage()));
        }
    }
}
