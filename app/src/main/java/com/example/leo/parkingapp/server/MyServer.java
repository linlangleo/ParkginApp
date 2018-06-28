package com.example.leo.parkingapp.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;

/**
 * 项目名：    ParkingApp
 * 包名： com.example.leo.parkingapp.service
 * 文件名：    MyServer
 * 创建者：    leo
 * 创建时间：   2018/3/5 19:44
 * 描述： TODO
 */
public class MyServer extends NanoHTTPD {

    public MyServer(int port) {
        super(port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        StringBuilder builder = new StringBuilder();
        builder.append("<html>\n" +
                "<head>\n" +
                "  <script>\n" +
                "    function init() {\n" +
                "      enableVideoClicks();\n" +
                "    }\n" +
                "\n" +
                "    function enableVideoClicks() {\n" +
                "      var videos = document.getElementsByTagName('video') || [];\n" +
                "      for (var i = 0; i < videos.length; i++) {\n" +
                "        // TODO: use attachEvent in IE\n" +
                "        videos[i].addEventListener('click', function(videoNode) {\n" +
                "          return function() {\n" +
                "            videoNode.play();\n" +
                "          };\n" +
                "        }(videos[i]));\n" +
                "      }\n" +
                "    }\n" +
                "  </script>\n" +
                "</head>\n" +
                "<body onload=\"init()\">\n" +
                "\n" +
                "  <video width=\"400\" height=\"300\" controls>" +
                        "<source src=\"/storage/emulated/0/ssp.mp4\" type=\"video/mp4\" codecs=\"avc1.42E01E, mp4a.40.2\"></source>" +
                        "</video>\n" +
                "\n" +
                "  ...\n" +
                "\n" +
                "</body>\n" +
                "</html>");

        return  responseVideoStream(session, "/storage/emulated/0/ssp.mp4");

//        return new Response(builder.toString());
    }

    public Response responseVideoStream(IHTTPSession session,String videopath) {
        try {
            FileInputStream fis = new FileInputStream(videopath);
            return  new resp(Status.OK, "video/mp4", fis, 1000000);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
            return  newFixedLengthResponse("Error");
        }
    }

    class resp extends NanoHTTPD.Response{
        public resp(NanoHTTPD.Response.IStatus status, String mimeType, InputStream data, long totalBytes){
            super(status, mimeType, data, totalBytes);
        }
    }

}
