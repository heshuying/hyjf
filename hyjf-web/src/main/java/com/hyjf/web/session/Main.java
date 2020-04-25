/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2016
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 * Created at: 2016年1月16日 上午11:38:55
 * Modification History:
 * Modified by : 
 */

package com.hyjf.web.session;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.hyjf.common.security.utils.ThreeDESUtils;
import com.hyjf.common.util.PropUtils;

/**
 * @author 郭勇
 */

public class Main {
	private static String KEY = PropUtils.getSystem("hyjf.3des.key").trim();

	public static void main(String[] args) throws Exception {
		String jsessionid = "zfgMgMWj4bO5qLpckKmj0I%25252FHvpdeEN6BNfYhksj9JWtkC8G60ZA2%25252BNqqjpd0E4py6%25252B3KumH9CtjM2aR%25252BjZWnhA%25253D%25253D";
		String jtimestamp = "1453183543";

		try {
			jsessionid = URLDecoder.decode(jsessionid, "UTF-8");
			System.out.println("jsessionid---decode---" + jsessionid);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		while (jsessionid.contains("%")) {
			try {
				jsessionid = URLDecoder.decode(jsessionid, "UTF-8");
				System.out.println("jsessionid:" + jsessionid);
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}

		// try {
		// jsessionid = URLEncoder.encode(jsessionid, "UTF-8");
		// System.out.println("jsessionid---encode---" + jsessionid);
		// System.out.println("jsessionid---encode---" +
		// getEncoding(jsessionid));
		// } catch (UnsupportedEncodingException e1) {
		// e1.printStackTrace();
		// }
		String kkey = KEY + jtimestamp;
		String result = ThreeDESUtils.Decrypt3DES(kkey, jsessionid);
		System.out.println("preHandle:---request result--------------" + result);
	}

	public static String getEncoding(String str) {
		String encode = "GB2312";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s = encode;
				return s;
			}
		} catch (Exception exception) {
		}
		encode = "ISO-8859-1";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s1 = encode;
				return s1;
			}
		} catch (Exception exception1) {
		}
		encode = "UTF-8";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s2 = encode;
				return s2;
			}
		} catch (Exception exception2) {
		}
		encode = "GBK";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s3 = encode;
				return s3;
			}
		} catch (Exception exception3) {
		}
		return "";
	}

}
