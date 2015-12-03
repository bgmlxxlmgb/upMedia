package util;


import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import pojo.Token;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;

/**
 * Created by bgm on 2015/11/26.
 */
public class CommonUtil {
    //private static Logger log = LoggerFactory.getLogger(CommonUtil.class);
    public final static String token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";


    public static JSONObject httpsRequest(String requestUrl , String requestMethoad , String outputStr){
        JSONObject jsonObject = null;
        try{
            TrustManager[] tm = { new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL","SunJSSE");
            sslContext.init(null , tm , new java.security.SecureRandom());
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod(requestMethoad);
            if(null != outputStr){
                OutputStream outputStream= conn.getOutputStream();
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while((str = bufferedReader.readLine()) != null){
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            System.out.println("222222222222222222222"+"eeeeeeeeeeeeeeeeeeeeeeeee");
            jsonObject = JSONObject.fromObject(buffer.toString());
            System.out.println("/////////////////////////");

        }catch(ConnectException ce){
            ce.printStackTrace();
            System.out.println("连接超时"+ce);
            // log.error("连接超时:{}", ce);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("https请求异常:"+e);
            // log.error("https请求异常:{}",e);
        }
        return jsonObject;
    }

    public static Token getToken(String appid , String appsecret){
        Token token = null;
        String requestUrl = token_url.replace("APPID",appid).replace("APPSECRET",appsecret);
        System.out.println("requestUrl:"+requestUrl);
        JSONObject jsonObject = httpsRequest(requestUrl , "GET" , null);
        System.out.println("requestUrl:"+requestUrl);
        if(null != jsonObject){
            try{
                token = new Token();
                token.setAccessToken(jsonObject.getString("access_token"));
                token.setExpiresIn(jsonObject.getInt("expires_in"));
            }catch(JSONException e){
                token = null;
                System.out.println("获取token失败 errcode:"+ jsonObject.getInt("errcode")+jsonObject.getString("errmsg"));
                //log.error("获取token失败 errcode:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return token;
    }
    public static String getFileExt(String contentType) {
        String fileExt = "";
        if ("image/jpeg".equals(contentType))
            fileExt = ".jpg";
        else if ("audio/mp3".equals(contentType))
            fileExt = ".mp3";
        else if ("audio/amr".equals(contentType))
            fileExt = ".amr";
        else if ("video/mp4".equals(contentType))
            fileExt = ".mp4";
        else if ("video/mpeg4".equals(contentType))
            fileExt = ".mp4";
        return fileExt;
    }
}
