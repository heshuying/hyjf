package com.hyjf.mqreceiver.crm.utils;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hyjf.common.util.RSAHelper;
import com.hyjf.common.util.RSAKeyUtil;

/**
 * Created by cui on 2018/4/8.
 */
public class CheckSignUtil {
    private static Logger logger = LoggerFactory.getLogger(CheckSignUtil.class);

    // 汇盈金服私钥文件地址(请求)
    public static final String HYJF_REQ_PRI_KEY_PATH = PropUtils.getSystem("hyjf.req.pri.key");

    //汇盈金服(请求)密码
    public static final String HYJF_REQ_KEY_PASS = PropUtils.getSystem("hyjf.req.password");


    /**
     * 请求数据加签
     *
     * @param mapText
     */
    public static String encryptByRSA(Map<String, Object> mapText, String instCode) {
        try {
            String signText = getSignText(mapText);
            logger.info("待加签数据【" + signText + "】");

            RSAKeyUtil rsaKey = new RSAKeyUtil(new File(HYJF_REQ_PRI_KEY_PATH + instCode + ".p12"), HYJF_REQ_KEY_PASS);
            RSAHelper signer = new RSAHelper(rsaKey.getPrivateKey());
            String sign = signer.sign(signText);
            return sign;
        } catch (Exception e) {
            logger.error("加签失败！" + e.getMessage(), e);
        }
        throw new IllegalArgumentException("加签失败！");

    }

    private static String getSignText(Map<String, Object> generalSignInfo) throws Exception {
        TreeMap<String, Object> treeMap = new TreeMap<>(generalSignInfo);

        StringBuffer buff = new StringBuffer();
        Iterator<Map.Entry<String, Object>> iter = treeMap.entrySet().iterator();
        Map.Entry<String, Object> entry;
        while (iter.hasNext()) {
            entry = iter.next();
            if (entry.getValue() == null) {
                entry.setValue("");
                buff.append("");
            } else {
                buff.append(String.valueOf(entry.getValue()));
            }
        }
        String requestMerged = buff.toString();
        return requestMerged.replaceAll("[\\t\\n\\r]", "");
    }
}
