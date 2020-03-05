package com.api.Response;

import com.google.gson.JsonPrimitive;
import spark.Request;
import spark.Response;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import static com.api.Main.gson;

public class FilesResponse {
    public static String postImageResponse(Request request, Response response) {
        try {
            byte[] bytes = Files.readAllBytes(java.nio.file.Paths.get("C:\\FTPServer\\" + request.queryParams("dir")));
            response.raw().setContentType("image/jpeg");
            response.raw().setContentLength(bytes.length);
            HttpServletResponse raw = response.raw();
            raw.getOutputStream().write(bytes);
            raw.getOutputStream().flush();
            raw.getOutputStream().close();
            return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, new JsonPrimitive("File sent successfully")));
        } catch (IOException e) {
            e.printStackTrace();
            response.header("Content-Type", "application/download");
            return gson.toJson(new StandardResponse(StatusResponse.ERROR, e.toString()));
        }
    }

    public static Object postAudioResponse(Request request, Response response) {
        try (OutputStream os = response.raw().getOutputStream(); BufferedOutputStream bos = new BufferedOutputStream(os)) {
            File mp3 = new File("C:\\FTPServer\\" + request.queryParams("dir"));
            String range = request.headers("Range");
            byte[] bytes = Files.readAllBytes(java.nio.file.Paths.get("C:\\FTPServer\\" + request.queryParams("dir")));
            if (range == null) {
                response.status(200);
                response.header("Content-Type", "audio/mpeg");
                response.header("Content-Length", String.valueOf(bytes.length));
                HttpServletResponse raw = response.raw();
                raw.getOutputStream().write(bytes);
                raw.getOutputStream().flush();
                raw.getOutputStream().close();
                return gson.toJson(new StandardResponse(StatusResponse.SUCCESS));
            }
            int[] fromTo = fromTo(mp3, range);
            int length = fromTo[1] - fromTo[0];
            response.header("Content-Range", contentRangeByteString(fromTo));
            response.header("Content-Length", String.valueOf(length));
            response.header("Content-Type", "audio/mpeg"); // change content type if necessary
            response.header("Accept-Ranges", "bytes");
            response.status(206);
            HttpServletResponse raw = response.raw();
            raw.getOutputStream().write(bytes, fromTo[0], length);
            raw.getOutputStream().flush();
            raw.getOutputStream().close();
            return gson.toJson(new StandardResponse(StatusResponse.SUCCESS));
        } catch (IOException e) {
            e.printStackTrace();
            response.header("Content-Type", "application/json");
            return gson.toJson(new StandardResponse(StatusResponse.ERROR, e.toString()));
        }
    }

    public static int[] fromTo(File mp3, String range) {
        int[] ret = new int[3];
        String[] ranges = range.split("=")[1].split("-");
        int from = Integer.parseInt(ranges[0]);
        int to = (int) mp3.length();
        ret[0] = from;
        ret[1] = to;
        ret[2] = (int) mp3.length();
        return ret;
    }
    public static String contentRangeByteString(int[] fromTo) {
        return "bytes " + fromTo[0] + "-" + fromTo[1] + "/" + fromTo[2];
    }
}
