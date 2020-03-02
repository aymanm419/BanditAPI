package com.api.Response;

import com.google.gson.JsonPrimitive;
import spark.Request;
import spark.Response;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
        try {
            File mp3 = new File("C:\\FTPServer\\" + request.queryParams("dir"));
            String range = request.headers("Range");
            if (range == null) {
                response.status(200);
                byte[] bytes = Files.readAllBytes(java.nio.file.Paths.get("C:\\FTPServer\\" + request.queryParams("dir")));
                response.header("Content-Type", "audio/mpeg");
                response.header("Content-Length", String.valueOf(bytes.length));
                HttpServletResponse raw = response.raw();
                raw.getOutputStream().write(bytes);
                raw.getOutputStream().flush();
                raw.getOutputStream().close();
                return raw;
            }
            int[] fromTo = fromTo(mp3, range);
            int length = fromTo[1] - fromTo[0] + 1;
            OutputStream os = response.raw().getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);
            response.status(206);
            response.raw().setContentType("audio/mpeg");
            response.header("Accept-Ranges", "bytes");
            response.header("Content-Range", contentRangeByteString(fromTo));
            response.header("Content-Length", String.valueOf(length));
            final RandomAccessFile raf = new RandomAccessFile(mp3, "r");
            raf.seek(fromTo[0]);
            writeAudioToOS(length, raf, bos);
            raf.close();
            bos.flush();
            bos.close();
            return response.raw();
        } catch (IOException e) {
            e.printStackTrace();
            response.header("Content-Type", "application/json");
            return gson.toJson(new StandardResponse(StatusResponse.ERROR, e.toString()));
        }
    }

    public static int[] fromTo(File mp3, String range) {
        int[] ret = new int[3];
        String[] ranges = range.split("=")[1].split("-");
        Integer chunkSize = 8192;
        Integer from = Integer.parseInt(ranges[0]);
        int to = chunkSize + from;
        if (to >= mp3.length()) {
            to = (int) (mp3.length() - 1);
        }
        if (ranges.length == 2) {
            to = Integer.parseInt(ranges[1]);
        }
        ret[0] = from;
        ret[1] = to;
        ret[2] = (int) mp3.length();
        return ret;

    }

    public static String contentRangeByteString(int[] fromTo) {
        return "bytes " + fromTo[0] + "-" + fromTo[1] + "/" + fromTo[2];
    }

    public static void writeAudioToOS(Integer length, RandomAccessFile raf, BufferedOutputStream os) throws IOException {
        byte[] buf = new byte[256];
        while (length != 0) {
            int read = raf.read(buf, 0, buf.length > length ? length : buf.length);
            os.write(buf, 0, read);
            length -= read;
        }
    }
}
