package util;

import pojo.Token;
import upLoad.MediaUpload;

/**
 * Created by bgm on 2015/11/26.
 */
public class test {
    public static void main(String a[]){
        String appId = "wxddf3a1aaa9b9a02d";
        String appSecret = "d4624c36b6795d1d99dcf0547af5443d";
        Token token = CommonUtil.getToken(appId, appSecret);
        String accessToken = token.getAccessToken();
        MediaUpload.getMedia(accessToken, "EzQqfSqnLu_7cltiAboRnuk5RVOu6wznuwlZlbfMdNU-XkOSzAMMhuY8oeMPtw0w", "D:\\test");
    }
}
