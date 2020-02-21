package com.api.Response;

import spark.Request;
import spark.Response;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;

import static com.api.Main.gson;

public class FilesResponse {
    public static String transferFile(Request request, Response response) {

        try {
            byte[] bytes = Files.readAllBytes(java.nio.file.Paths.get("C:\\FTPServer\\" + request.queryParams("dir")));
            response.header("Content-Type", "application/download");
            response.header("Content-Disposition", "attachment;filename = " + request.queryParams("dir"));
            response.header("Content-Length", String.valueOf(bytes.length));
            HttpServletResponse raw = response.raw();
            raw.getOutputStream().write(bytes);
            raw.getOutputStream().flush();
            raw.getOutputStream().close();
            return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, gson.toJson(response.raw())));
        } catch (IOException e) {
            e.printStackTrace();
            return gson.toJson(new StandardResponse(StatusResponse.ERROR, e.toString()));
        }
    }
}
