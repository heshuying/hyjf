/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: Michael
 * @version: 1.0
 * Created at: 2015年12月2日 下午4:31:47
 * Modification History:
 * Modified by : 
 */

package com.hyjf.admin.htl.dafei;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.PropUtils;

/**
 * @author Michael
 */

public class DaFeiHttpRequest {
	// 请求地址
	public static String FIN_URL = PropUtils.getSystem("hyjf.dafei.request");
	// 拒绝地址
	public static String REFUSE_URL = PropUtils.getSystem("hyjf.dafei.refuse");
	//校验key
	public static String HYJF_KEY = PropUtils.getSystem("hyjf.dafei.key");

	public static String CREDITMODEL = "P2P_HYJF";
	// 现金贷合同查询
	public static String OPERATIONTYPE_GET = "GetCashCredit";
	// 拒绝合同
	public static String OPERATIONTYPE_REF = "RefuseContract";


	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) throws Exception {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
			throw new Exception("发送POST请求出现异常！");
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 获得传输的参数
	 * 
	 * @param creditModel
	 * @param operateionType
	 * @param requestParm
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getRequestParm(String creditModel, String operateionType, Object requestParm)
			throws UnsupportedEncodingException {
		String msg = "";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("CreditModel", creditModel);
		map.put("OperationType", operateionType);
		if (requestParm.getClass().equals(String.class)) {
			map.put("RequestParm", requestParm);
		} else if (requestParm.getClass().equals(ArrayList.class)) {
			map.put("DataContent", requestParm);
		}
		String signData;
		signData = RSASignature.sign(creditModel, HYJF_KEY);
		signData = URLEncoder.encode(signData, "UTF-8");
		map.put("SignContent", signData);
		msg = JSONArray.toJSONString(map);
		return msg;
	}

	/**
	 * json 转对象 ResultBean
	 */
	public static ResultBean jsonToObject(String message) {
		ResultBean con = (ResultBean) JSONObject.toJavaObject(JSONArray.parseObject(message), ResultBean.class);
		return con;
	}

	/**
	 * json 转对象数组 List<ConsumeInfo>
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public static List<ConsumeInfo> jsonToArray(String message) throws UnsupportedEncodingException {
		String mes;
		List<ConsumeInfo> consumeList = new ArrayList<ConsumeInfo>();
		mes = URLDecoder.decode(message, "UTF-8");
		if(StringUtils.isNotEmpty(mes)){
			consumeList = JSONArray.parseArray(mes, ConsumeInfo.class);
		}
		return consumeList;
	}

	/*
	 * 获取现金贷合同接口 searchDate yyyy-MM-dd
	 */
	public static List<ConsumeInfo> getConsumeList(String searchDate) throws UnsupportedEncodingException, Exception {
		List<ConsumeInfo> consumeList = new ArrayList<ConsumeInfo>();
		String result = sendPost(FIN_URL, getRequestParm(CREDITMODEL, OPERATIONTYPE_GET, searchDate));
		ResultBean con = jsonToObject(result);
		String mes = URLDecoder.decode(con.getContent(), "UTF-8");
		System.out.println(mes);
		consumeList = jsonToArray(mes);
		return consumeList;
	}

	/**
	 * 拒绝合同接口
	 * 
	 * @param refuseList
	 * @return 返回成功失败信息
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	public static String refuseContract(List<RefuseInfo> refuseList) throws UnsupportedEncodingException, Exception {
		String res = "";
		String result = sendPost(REFUSE_URL, getRequestParm(CREDITMODEL, OPERATIONTYPE_REF, refuseList));
		System.out.println(getRequestParm(CREDITMODEL, OPERATIONTYPE_REF, refuseList));
		ResultBean con = jsonToObject(result);
		// String mes = URLDecoder.decode(con.getContent(), "UTF-8");
		System.out.println(result);
		res = con.getReturnCode();
		return res;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		// 发送 POST 请求 查询接口
		 String message = DaFeiHttpRequest.getRequestParm("P2P_HYJF",
		 "GetCashCredit", "2015-11-03");
		 System.out.println(message);
		 String result="";
		try {
			result = DaFeiHttpRequest.sendPost(FIN_URL, message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
				
		}
		 ResultBean con = jsonToObject(result);
		 String mes = URLDecoder.decode(con.getContent(), "UTF-8");
		 List<ConsumeInfo> consumeList = jsonToArray(mes);
		 System.out.println(consumeList.get(0).getLoan_Date());
		

		// 拒绝接口
//		RefuseInfo info1 = new RefuseInfo();
//		RefuseInfo info2 = new RefuseInfo();
//		info1.setContractNo("10362223002");
//		info1.setContractDate("2015-12-14");
//		info1.setCreditAmount("5000.00");
//		info1.setCustomerName("张三");
//		info1.setReason("测试");
//		info1.setCreditModel(CREDITMODEL);
//		info2.setContractNo("10379353002");
//		info2.setContractDate("2015-12-14");
//		info2.setCreditAmount("5000.00");
//		info2.setCustomerName("李四");
//		info2.setReason("测试");
//		info2.setCreditModel(CREDITMODEL);
//		List<RefuseInfo> li = new ArrayList<RefuseInfo>();
//		li.add(info1);
//		li.add(info2);
//		try {
//			refuseContract(li);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//
//		}
	}

}