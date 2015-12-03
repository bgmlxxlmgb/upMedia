package upLoad;

import net.sf.json.JSONObject;
import util.CommonUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by bgm on 2015/11/26.
 */
public class MediaUpload {
    public static WeixinMedia uploadMedia(String accessToken, String type,String contentType,InputStream inputStream_File) {
        WeixinMedia weixinMedia = null;
        // 拼装请求地址
        //https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=ACCESS_TOKEN
        //https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=ACCESS_TOKEN
        //String uploadMediaUrl = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
        String uploadMediaUrl = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=ACCESS_TOKEN";
        uploadMediaUrl = uploadMediaUrl.replace("ACCESS_TOKEN", accessToken);
        System.out.println(uploadMediaUrl);
        uploadMediaUrl = uploadMediaUrl.replace("TYPE", type);
        System.out.println("type:"+type);

        // 定义数据分隔符
        String boundary = "------------7da2e536604c8";
        try {
            URL uploadUrl = new URL(uploadMediaUrl);
            HttpURLConnection uploadConn = (HttpURLConnection) uploadUrl.openConnection();
            uploadConn.setDoOutput(true);
            uploadConn.setDoInput(true);
            uploadConn.setRequestMethod("POST");
            // 设置请求头Content-Type
            uploadConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            // 获取媒体文件上传的输出流（往微信服务器写数据）
            OutputStream outputStream = uploadConn.getOutputStream();
            // 根据内容类型判断文件扩展名
            System.out.println("contentType:"+contentType);
            String fileExt = CommonUtil.getFileExt(contentType);
            System.out.println(fileExt+"#####################################");
            // 请求体开始
            outputStream.write(("--" + boundary + "\r\n").getBytes());
            outputStream.write(String.format("Content-Disposition: form-data; name=\"media\"; filename=\"file1%s\"\r\n", fileExt).getBytes());
            outputStream.write(String.format("Content-Type: %s\r\n\r\n", contentType).getBytes());

            // 获取媒体文件的输入流（读取文件）
            BufferedInputStream bis = new BufferedInputStream(inputStream_File);
            byte[] buf = new byte[8096];
            int size = 0;
            while ((size = bis.read(buf)) != -1) {
                // 将媒体文件写到输出流（往微信服务器写数据）
                outputStream.write(buf, 0, size);
            }
            // 请求体结束
            outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
            outputStream.close();
            bis.close();
            //meidaConn.disconnect();

            // 获取媒体文件上传的输入流（从微信服务器读数据）
            InputStream inputStream = uploadConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer buffer = new StringBuffer();
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            uploadConn.disconnect();

            // 使用JSON-lib解析返回结果
            System.out.println("qqqqqqqqqqqqqqqqqqq");
            JSONObject jsonObject = JSONObject.fromObject(buffer.toString());
            weixinMedia = new WeixinMedia();
            weixinMedia.setType(jsonObject.getString("type"));
            System.out.println(jsonObject.getString("type")+"\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
            // type等于thumb时的返回结果和其它类型不一样
            if ("thumb".equals(type))
            {
                weixinMedia.setMediaId(jsonObject.getString("thumb_media_id"));
                System.out.println(jsonObject.getString("thumb_media_id")+"=====================================");
            }
            else
            {
                weixinMedia.setMediaId(jsonObject.getString("media_id"));
                System.out.println("media_id:"+jsonObject.getString("media_id"));
            }
            weixinMedia.setCreatedAt(jsonObject.getInt("created_at"));
        } catch (Exception e) {
            weixinMedia = null;
            System.out.println("上传媒体文件失败：{}"+ e);
        }
        return weixinMedia;
    }
    public static String getMedia(String accessToken, String mediaId, String savePath) {
        String filePath = null;
        // 拼接请求地址
        String requestUrl = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("MEDIA_ID", mediaId);
        System.out.println(requestUrl);
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            if (!savePath.endsWith("/")) {
                savePath += "/";
            }
            // 根据内容类型获取扩展名
            System.out.println(conn.getHeaderField("Content-Type"));
            String fileExt = CommonUtil.getFileExt(conn.getHeaderField("Content-Type"));
            System.out.println(fileExt);
            // 将mediaId作为文件名
            filePath = savePath + mediaId + fileExt;
            System.out.println(filePath);

            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            FileOutputStream fos = new FileOutputStream(new File(filePath));
            byte[] buf = new byte[8096];
            int size = 0;
            while ((size = bis.read(buf)) != -1)
                fos.write(buf, 0, size);
            fos.close();
            bis.close();

            conn.disconnect();
            System.out.println("下载媒体文件成功，filePath=" + filePath);
        } catch (Exception e) {
            filePath = null;
            System.out.println("下载媒体文件失败："+ e);
        }
        return filePath;
    }
}

