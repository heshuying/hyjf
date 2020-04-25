package com.hyjf.api.server.test;

import com.hyjf.common.util.ApiSignUtil;

public class TestRsaCorrect {


	
	public static void main(String[] args) {

		
		String needEnStr = "verygoodsadfsafaffffffffffffffffff    fhtp://www.baidu.com \r\n ffffffffffffffffffffffffffff";
		
		String instCode = "10000013";
		
		String chkValue = ApiSignUtil.encryptByRSAForRequest(instCode,needEnStr);
		System.out.println(chkValue);
//		chkValue = chkValue.replaceAll("\\+", " ");
		boolean isOk = ApiSignUtil.verifyByRSA(instCode,chkValue, needEnStr);
		System.out.println("请求  " + isOk);
//		
		String reschkValue = ApiSignUtil.encryptByRSA(needEnStr);
		boolean resisOk = ApiSignUtil.verifyByRSAForRequest(reschkValue, needEnStr);
		
		System.out.println("返回 " + resisOk);
		
		
	}

}
