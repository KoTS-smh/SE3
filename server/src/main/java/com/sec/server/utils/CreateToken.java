package com.sec.server.utils;

import com.qiniu.api.rs.*;
import com.qiniu.api.config.Config;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.net.CallRet;

public class CreateToken {
    public static String uploadToken(String bucket, String accessKey, String secretKey) {
        PutPolicy upPolicy = new PutPolicy(bucket);
        //upPolicy.endUser = user;
        //upPolicy.callbackUrl = callbackUrl;
        upPolicy.callbackBody = "key=$(key)&hash=$(etag)&width=$(imageInfo.width)&height=$(imageInfo.height)&user=$(endUser)&size=$(fsize)&mime=$(mimeType)";
        String token = null;
        Mac mac = new Mac(accessKey, secretKey);
        try{
            token = upPolicy.token(mac);
        }catch (Exception e){
            e.printStackTrace();
        }
        return token;
    }

    public static void main(String args[]){
        String bucket = "mrgs-bucket";
        String accessKey = "j0dwMMGFcKPhncC7vb_PWXshbpiSMEWB69NiKhn4";
        String secretKey = "2vWVIw3WJfk314YN3e24ZnixdJMbyoZ14FXiqF--";

        String token = CreateToken.uploadToken(bucket, accessKey, secretKey);
        System.out.println(token);

    }
}
