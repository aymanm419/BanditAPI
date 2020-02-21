package com.api.Response;

import com.api.Repository.PostsLoader;
import com.google.gson.JsonElement;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

import static com.api.Main.gson;

public class PostsResponse {
    public static String getHotPosts(Request request, Response response) {
        response.type("application/json");
        try {
            int startPoint = Integer.parseInt(request.queryParamOrDefault("limitstart", "1"));
            int endPoint = Integer.parseInt(request.queryParamOrDefault("limitend", "10"));
            JsonElement postsArrayJson = gson.toJsonTree(PostsLoader.loadPosts(startPoint, endPoint));
            return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, postsArrayJson));
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException | NumberFormatException e) {
            System.out.println(e.getMessage());
            return gson.toJson(new StandardResponse(StatusResponse.ERROR, e.getMessage()));
        }
    }
}
