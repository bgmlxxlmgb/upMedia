package servlet;


import pojo.Token;
import upLoad.MediaUpload;
import util.CommonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by bgm on 2015/11/26.
 */
@WebServlet(name = "ReciveServlet",urlPatterns = {"/receive"},loadOnStartup = 1)
@MultipartConfig(fileSizeThreshold = 5242880,maxFileSize = 20971520L,maxRequestSize = 41943040L)

public class reciveServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Part part = request.getPart("part");
        String appId = "wxddf3a1aaa9b9a02d";
        String appSecret = "d4624c36b6795d1d99dcf0547af5443d";
        Token token = CommonUtil.getToken(appId, appSecret);
        String accessToken = token.getAccessToken();
        System.out.println("11111111111111111111111");
        String type = request.getParameter("type");
        String contentType = part.getContentType();
        String s = part.getHeader("Content-Type");
        InputStream inputStream = part.getInputStream();
        MediaUpload.uploadMedia(accessToken, type, contentType, inputStream);
        inputStream.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
