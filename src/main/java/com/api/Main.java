package com.api;

import com.api.Repository.PostsLoader;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.Files;
import java.sql.SQLException;

import static spark.Spark.*;

public class Main {
    public static Gson gson = new Gson();

    public static void main(String[] args) {
        port(6969);
        get(Paths.POSTS_HOT, (request, response) -> {
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
        });
        get(Paths.POSTS_FILE, (request, response) ->
        {
            response.header("Content-Type", "application/download");
            response.header("Content-Disposition", "attachment; filename=" + request.queryParams("dir"));
            byte[] bytes = Files.readAllBytes(java.nio.file.Paths.get("C:\\FTPServer\\" + request.queryParams("dir")));
            response.header("Content-Length", String.valueOf(bytes.length));
            HttpServletResponse raw = response.raw();
            raw.getOutputStream().write(bytes);
            raw.getOutputStream().flush();
            raw.getOutputStream().close();
            return response.raw();
        });
        get(Paths.USER_FIND, "application/json", (request, response) ->
        {
            try {
                if (UserValidationHandler.userExists(request.queryParams("username")))
                    return gson.toJson(new StandardResponse(StatusResponse.SUCCESS));
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Could not find user with username: " + request.queryParams("username")));
            } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Connection to database could not be Initiated"));
            }
        });
        post(Paths.USER_CREATE, "application/json", (request, response) ->
        {
            String username = request.queryParams("username");
            String password = request.queryParams("password");
            try {
                if (UserValidationHandler.userExists(username))
                    return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Username already exists"));
                UserValidationHandler.createAccount(username, password);
                return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, "Successfully made account"));
            } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Connection to database could not be Initiated"));
            }
        });
        get(Paths.USER_LOGIN, "application/json", (request, response) ->
        {
            String username = request.queryParams("username");
            String password = request.queryParams("password");
            try {
                if (UserValidationHandler.validateAccountCredentials(username, password))
                    return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, "User Authenticated successfully!"));
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Authentication Failed For user: " + username));
            } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Connection to database could not be Initiated"));
            }
        });
        get(Paths.USER_DATA, (request, response) -> {
            String parameter = request.params(":data");
            String username = request.params(":username");
            if (!UserValidationHandler.userExists(username))
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Username already exists"));
            switch (parameter) {
                case "id":
                    return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, gson.toJson(UserDataHandler.getID(username))));
                case "likes":
                    return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, gson.toJson(UserDataHandler.getLikes(username))));
                case "dislikes":
                    return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, gson.toJson(UserDataHandler.getDislikes(username))));
                case "favourites":
                    return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, gson.toJson(UserDataHandler.getFavourites(username))));
                default:
                    return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Client asked for invalid path route"));
            }
        });
        get(Paths.USER_ACTION, (request, response) -> {
            String parameter = request.params(":action");
            String username = request.params(":username");
            if (!UserValidationHandler.userExists(username))
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Username already exists"));
            int postID = Integer.parseInt(request.queryParams("postid"));
            int userID = UserDataHandler.getID(username);
            switch (parameter) {

                case "favourite":
                    return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, gson.toJson(UserDataHandler.getID(username))));
                case "like":
                    return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, gson.toJson(UserDataHandler.getLikes(username))));
                case "dislike":
                    return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, gson.toJson(UserDataHandler.getDislikes(username))));
                case "favourites":
                    return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, gson.toJson(UserDataHandler.getFavourites(username))));
                default:
                    return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Client asked for invalid path route"));
            }
        });
    }
}
