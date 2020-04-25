package com.hyjf.api.server.test.util;
import java.io.File;

import org.apache.commons.lang3.StringUtils;

import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.RSAHelper;
import com.hyjf.common.util.RSAKeyUtil;
public class RSAUtil {
    // 私钥
    private final static String HYJF_RES_PRI_KEY_PATH = PropUtils.getSystem("hyjf.req.pri.key");//"D:\\HYJFCER\\hyjf_key_test\\hyjfApiReq.p12";
    private final static String HYJF_RES_KEY_PASS = PropUtils.getSystem("hyjf.req.password");
    
    public static String encryptByRSA(String... encPramas) {
        
        // 生成待签名字符串
        StringBuffer buff = new StringBuffer();
        for (String param : encPramas) {
            buff.append(StringUtils.trimToEmpty(param));
        }
        String signStr = buff.toString();
        
        // 生成签名
        String sign = null;
        RSAHelper signer = null;
        try {
            RSAKeyUtil rsaKey = new RSAKeyUtil(new File(HYJF_RES_PRI_KEY_PATH), HYJF_RES_KEY_PASS);
            signer = new RSAHelper(rsaKey.getPrivateKey());
            sign = signer.sign(signStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return sign;
    }

}
