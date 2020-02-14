package com.api;

import com.api.Database.DatabaseConnection;
import com.api.Model.Post;
import com.api.Model.Song;
import com.api.Repository.FilesHandler;
import com.api.Repository.PostsLoader;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static spark.Spark.*;

public class Main {
    public static Gson gson = new Gson();

    public static void main(String[] args) {
        port(6969);
        get("/posts/hot", (request, response) -> {
            response.type("application/json");
            try {
                int startPoint = Integer.parseInt(request.queryParamOrDefault("limitstart","1"));
                int endPoint = Integer.parseInt(request.queryParamOrDefault("limitend","10"));
                JsonElement postsArrayJson = gson.toJsonTree(PostsLoader.loadPosts(startPoint, endPoint));
                return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, postsArrayJson));
            } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException | NumberFormatException e) {
                System.out.println(e.getMessage());
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, e.getMessage()));
            }
        });
        get("/posts/file",(request, response) ->
        {
            response.header("Content-Type", "application/download");
            response.header("Content-Disposition", "attachment; filename=" + request.queryParams("dir"));
            byte[] bytes = Files.readAllBytes(Paths.get("C:\\FTPServer\\" + request.queryParams("dir")));
            response.header("Content-Length", String.valueOf(bytes.length));
            HttpServletResponse raw = response.raw();
            raw.getOutputStream().write(bytes);
            raw.getOutputStream().flush();
            raw.getOutputStream().close();
            return response.raw();
        });
        get("/users/find","application/json", (request, response) ->
        {
            try {
                if (UserHandler.userExists(request.queryParams("username")))
                    return gson.toJson(new StandardResponse(StatusResponse.SUCCESS));
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Could not find user with username: " + request.queryParams("username")));
            } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Connection to database could not be Initiated"));
            }
        });
        post("/users/create", "application/json", (request, response) ->
        {
            String username = request.queryParams("username");
            String password = request.queryParams("password");
            try
            {
                if(UserHandler.userExists(username))
                    return gson.toJson(new StandardResponse(StatusResponse.ERROR,"Username already exists"));
                UserHandler.createAccount(username,password);
                return gson.toJson(new StandardResponse(StatusResponse.SUCCESS,"Successfully made account"));
            } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Connection to database could not be Initiated"));
            }
        });
        get("/users/login","application/json",(request, response) ->
        {
            String username = request.queryParams("username");
            String password = request.queryParams("password");
            try
            {
                if(UserHandler.validateAccountCredentials(username,password))
                    return gson.toJson(new StandardResponse(StatusResponse.SUCCESS,"User Authenticated successfully!"));
                return gson.toJson(new StandardResponse(StatusResponse.ERROR,"Authentication Failed For user: " + username));
            } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Connection to database could not be Initiated"));
            }
        });
    }
}
