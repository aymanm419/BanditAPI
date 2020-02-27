package com.api.Response;

import com.api.Repository.User.UserAction;
import com.api.Request.RequestValidation;
import com.google.gson.JsonPrimitive;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.Set;

import static com.api.Main.gson;

public class UserActionResponse {
    public static final RequestValidation requestValidation = new RequestValidation();

    public static String changePostLike(Request request, Response response) {
        Set<String> queryParams = request.queryParams();
        if (requestValidation.isParametersMissing(queryParams, "postID", "userID", "change"))
            return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Invalid Request. Missing parameters"));
        int postID = Integer.parseInt(request.queryParams("postID"));
        int userID = Integer.parseInt(request.queryParams("userID"));
        int change = Integer.parseInt(request.queryParams("change"));
        try {
            UserAction.changePostLike(userID, postID, change);
            return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, new JsonPrimitive("Action done successfully!")));
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, e.getMessage()));
        }
    }

    public static String changePostDislike(Request request, Response response) {
        Set<String> queryParams = request.queryParams();
        if (requestValidation.isParametersMissing(queryParams, "postID", "userID", "change"))
            return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Invalid Request. Missing parameters"));
        int postID = Integer.parseInt(request.queryParams("postID"));
        int userID = Integer.parseInt(request.queryParams("userID"));
        int change = Integer.parseInt(request.queryParams("change"));
        try {
            UserAction.changePostDislike(userID, postID, change);
            return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, new JsonPrimitive("Action done successfully!")));
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, e.getMessage()));
        }
    }

    public static String changePostFavourite(Request request, Response response) {
        Set<String> queryParams = request.queryParams();
        if (requestValidation.isParametersMissing(queryParams, "postID", "userID", "change"))
            return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Invalid Request. Missing parameters"));
        int postID = Integer.parseInt(request.queryParams("postID"));
        int userID = Integer.parseInt(request.queryParams("userID"));
        int change = Integer.parseInt(request.queryParams("change"));
        try {
            UserAction.changePostFavourite(userID, postID, change);
            return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, new JsonPrimitive("Action done successfully!")));
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, e.getMessage()));
        }
    }
}
