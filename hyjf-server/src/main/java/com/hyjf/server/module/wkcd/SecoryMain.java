package com.hyjf.server.module.wkcd;

import com.hyjf.common.security.utils.DES;
import com.hyjf.common.security.utils.MD5Utils;
import com.hyjf.server.util.SecretUtil;

public class SecoryMain {

    public static void main(String[] args) {
        //请求资源路径
        String appKey = "A9A9BA4E";//APPKEY明文
        String secretKey = "xtd9j7te";//secretKey明文
        String timeStamp = "20161108134719609";//timeStamp明文
        
        String secretKeyMiWen = DES.encryptDES_ECB(secretKey, appKey);
        String timeStampMiWen = DES.encryptDES_ECB(timeStamp, appKey);
        String uniqueSign = MD5Utils.MD5(SecretUtil.sortByAscii(secretKey+timeStamp));
        System.out.println("secretKey("+secretKey+")密文------>"+secretKeyMiWen);
        System.out.println("timeStamp("+timeStamp+")密文------>"+timeStampMiWen);
        System.out.println("uniqueSign密文------>"+uniqueSign);
    }

}
