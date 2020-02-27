package com.api.Response;

import com.api.Repository.User.UserValidation;
import com.google.gson.JsonPrimitive;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

import static com.api.Main.gson;

public class UserValidationResponse {
    public static String authenticateUser(Request request, Response response) {
        String username = request.queryParams("username");
        String password = request.queryParams("password");
        try {
            if (UserValidation.validateAccountCredentials(username, password))
                return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, new JsonPrimitive("User Authenticated successfully!")));
            return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Authentication Failed For user: " + username));
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Connection to database could not be Initiated"));
        }
    }

    public static String createAccount(Request request, Response response) {
        String username = request.queryParams("username");
        String password = request.queryParams("password");
        try {
            if (UserValidation.userExists(username))
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Username already exists"));
            UserValidation.createAccount(username, password);
            return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, new JsonPrimitive("Username was not found")));
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Connection to database could not be Initiated"));
        }
    }

    public static String findUser(Request request, Response response) {
        try {
            if (UserValidation.userExists(request.queryParams("username")))
                return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, new JsonPrimitive("Username was found")));
            return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Could not find user with username: " + request.queryParams("username")));
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Connection to database could not be Initiated"));
        }
    }
}
