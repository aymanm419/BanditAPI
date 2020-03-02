package com.api;

import com.api.Response.*;
import com.google.gson.Gson;

import static spark.Spark.*;

public class Main {
    public static Gson gson = new Gson();

    public static void main(String[] args) {
        port(6969);
        get(Paths.POSTS_IMAGE, FilesResponse::postImageResponse);
        get(Paths.POSTS_SONG, FilesResponse::postAudioResponse);
        get(Paths.POSTS_HOT, "application/json", PostsResponse::getHotPosts);
        get(Paths.USER_FIND, "application/json", UserValidationResponse::findUser);
        post(Paths.USER_CREATE, "application/json", UserValidationResponse::createAccount);
        get(Paths.USER_LOGIN, "application/json", UserValidationResponse::authenticateUser);
        get(Paths.USERS_INFO_ID, "application/json", UserDataResponse::getUserID);
        get(Paths.USERS_INFO_LIKES, "application/json", UserDataResponse::getUserLikes);
        get(Paths.USERS_INFO_DISLIKES, "application/json", UserDataResponse::getUserDislikes);
        get(Paths.USERS_INFO_FAVOURITES, "application/json", UserDataResponse::getUserFavourites);
        post(Paths.USER_ACTION_LIKE, "application/json", UserActionResponse::changePostLike);
        post(Paths.USER_ACTION_DISLIKE, "application/json", UserActionResponse::changePostDislike);
        post(Paths.USER_ACTION_FAVOURITE, "application/json", UserActionResponse::changePostFavourite);

    }
}
