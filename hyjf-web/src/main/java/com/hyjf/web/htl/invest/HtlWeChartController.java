/**
 * Description:出借控制器
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 * Created at: 2015年12月4日 下午2:32:36
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.htl.invest;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.ChinapnrExclusiveLogWithBLOBs;
import com.hyjf.mybatis.model.auto.Product;
import com.hyjf.mybatis.model.auto.ProductErrorLog;
import com.hyjf.mybatis.model.auto.ProductList;
import com.hyjf.mybatis.model.auto.ProductRedeem;
import com.hyjf.mybatis.model.customize.ProductSearchForPage;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import com.hyjf.web.BaseController;
import com.hyjf.web.chinapnr.ChinapnrService;
import com.hyjf.web.htl.listener.ProductConstants;
import com.hyjf.web.htl.listener.ProductUtils;

/**
 * 汇天利资金中心接口(微信)
 * @author Michael
 */
@Controller
@RequestMapping(value = HtlCommonDefine.REQUEST_WECHART_MAPPING)
public class HtlWeChartController extends BaseController {
    /** THIS_CLASS */
    private static final String THIS_CLASS = HtlWeChartController.class.getName();

	@Autowired
	private HtlCommonService htlCommonService;

	@Autowired
	private ChinapnrService chinapnrService;

    /** 购买url */
    private static String BUYCALLURL = "/hyjf-web/htl/wechart/buyProduct";
    
    /** 赎回url */
    private static String REDEEMCALLURL = "/hyjf-web/htl/wechart/redeemProduct";
    
	/** 返回给PHP画面的URL */
	private static String RETURN_URL = PropUtils.getSystem("hyjf.web.htl.wechat.callbackurl");
	
    /**
     * 检查参数
     * 
     * @param request
     * @param form
     * @return
     */
    @ResponseBody
	@RequestMapping(value = HtlCommonDefine.CHECK_MAPPING, produces = "application/json; charset=UTF-8")
    public String check(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(THIS_CLASS, HtlCommonDefine.CHECK_MAPPING);
        String res = "";
        try {
	        int userId = Integer.parseInt(request.getParameter("uid"));//用户id
	        String transAmt = request.getParameter("amount");// 交易金额
	        int flag = Integer.parseInt(request.getParameter("flag")); // 购买为1  赎回为2  
	        // 检查参数
	        Map<String,String> result = this.htlCommonService.check(userId, transAmt,flag);
	        if (result != null && result.size() > 0) {
	            return JSON.toJSONString(result);
	        }
	        if(flag == 1){
	        	result.put("url",BUYCALLURL+"?amount="+transAmt+"&uid="+userId);
	        }else if (flag ==2){
	        	result.put("url",REDEEMCALLURL+"?amount="+transAmt+"&uid="+userId);
	        }else{
	        	result.put("error","1");
	        	result.put("data","请求参数不合法");
	        }
			res = JSON.toJSONString(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
        LogUtil.endLog(THIS_CLASS, HtlCommonDefine.CHECK_MAPPING);
        return res;
    }
	
	/**
	 * 汇天利购买
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = HtlCommonDefine.BUY_PRODUCT_MAPPING)
	public ModelAndView buyProduct(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(THIS_CLASS, HtlCommonDefine.BUY_PRODUCT_MAPPING);
		ModelAndView modelAndView = new ModelAndView(HtlCommonDefine.JSP_CHINAPNR_SEND);
		ProductList productList = new ProductList();
		int userId = Integer.parseInt(request.getParameter("uid"));//用户id
		String amount = request.getParameter("amount");// 出借金额
        // 取得用户当前本金
    	ProductSearchForPage productSearchForPage = new ProductSearchForPage();
    	productSearchForPage.setUserId(userId);
    	productSearchForPage = htlCommonService.selectUserPrincipal(productSearchForPage);
        // 取得用户在汇付天下的客户号
        AccountChinapnr accountChinapnrTender = this.htlCommonService.getAccountChinapnr(userId);
        // 取得用户在汇付天下的客户号(白海燕)
        AccountChinapnr accountChinapnrReceiver = this.htlCommonService.getAccountChinapnr(ProductConstants.BAI_USERID);//白海燕账号

        // 调用汇付接口(用户账户支付)
        Properties properties = PropUtils.getSystemResourcesProperties();
		String host = properties.getProperty("hyjf.web.host").trim();
		String retUrl = host + request.getContextPath() + HtlCommonDefine.REQUEST_WECHART_MAPPING+HtlCommonDefine.BUY_RETURN_MAPPING; //返回路径
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion(ChinaPnrConstant.VERSION_10);// 接口版本号
		bean.setCmdId(ChinaPnrConstant.CMDID_INITIATIVE_TENDER); // 消息类型(主动投标)
		bean.setOrdId(GetOrderIdUtils.getHtlOrderId()); // 订单号(必须)
		bean.setOrdDate(GetDate.getServerDateTime(1, new Date()));// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		bean.setTransAmt(CustomUtil.formatAmount(amount));// 交易金额(必须)
		bean.setUsrCustId(String.valueOf(accountChinapnrTender.getChinapnrUsrcustid()));// 用户客户号
		bean.setMaxTenderRate(CustomConstants.HTL_MAXTENDERRATE);// 云龙提示0.00
		bean.setBorrowerCustId(String.valueOf(accountChinapnrReceiver.getChinapnrUsrcustid()));
		bean.setBorrowerAmt(CustomUtil.formatAmount(amount));
		bean.setBorrowerRate(CustomConstants.HTL_BORROWERRATE);
		//bean.setIsFreeze("N");//是否冻结
		Map<String, String> map = new HashMap<String, String>();
		map.put("BorrowerCustId", String.valueOf(accountChinapnrReceiver.getChinapnrUsrcustid()));
		map.put("BorrowerAmt", CustomUtil.formatAmount(amount));
		map.put("BorrowerRate", CustomConstants.HTL_BORROWERRATE);// 小栋提示
		JSONArray array = new JSONArray();
		array.add(map);
		String BorrowerDetails = JSON.toJSONString(array);
		bean.setBorrowerDetails(BorrowerDetails);
		bean.setRetUrl(retUrl); // 页面返回
		bean.setBgRetUrl(ChinapnrUtil.getBgRetUrl()); // 商户后台应答地址(必须)
		//调用接口前操作
        try {
			productList.setAmount(new BigDecimal(amount));
			productList.setUserId(userId);
			if(productSearchForPage != null){
				productList.setBalance(productSearchForPage.getPrincipal());
			}else{
				productList.setBalance(BigDecimal.ZERO);
			}
			productList.setClient(1);//微信
			productList.setOrderId(bean.getOrdId());//订单id保持一致（汇付天下与汇盈）
			int pid = this.htlCommonService.insertBuyProduct(bean,productList);
			bean.set(ChinaPnrConstant.PARAM_MERPRIV, String.valueOf(pid)); //商户私有域  ，查看返回时是否有值
            // 跳转到汇付天下画面
            modelAndView = ChinapnrUtil.callApi(bean);
        } catch (Exception e) {
            //错误日志
            ProductErrorLog productErrorLog = new ProductErrorLog();
            productErrorLog.setAmount(new BigDecimal(amount));
            productErrorLog.setUserId(userId);
            productErrorLog.setDate(GetDate.getDate("yyyy-MM-dd HH:mm:ss"));
            productErrorLog.setInvestTime(GetDate.getNowTime10());
            productErrorLog.setOrderId(productList.getOrderId());
            productErrorLog.setRemark("汇天利购买前"+e.getCause());
            productErrorLog.setIsSms(0);
            this.htlCommonService.insertProductErrorLog(productErrorLog);
        }
        LogUtil.endLog(THIS_CLASS, HtlCommonDefine.BUY_PRODUCT_MAPPING);
        return modelAndView;
	}
	
	/**
	 * 汇天利购买 成功或失败
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = HtlCommonDefine.BUY_RETURN_MAPPING, produces = "application/json; charset=UTF-8")
	public ModelAndView buyProductReturn(HttpServletRequest request, @ModelAttribute ChinapnrBean bean) {
		LogUtil.startLog(THIS_CLASS, HtlCommonDefine.BUY_RETURN_MAPPING,"[交易完成后,回调开始]");
        //返回结果
		Map<String, String> map = new HashMap<String, String>();
        //参数转换
		bean.convert();
		String uuid = request.getParameter("uuid");
		boolean updateFlag = false;
        if (Validator.isNotNull(uuid)) {
            // 取得检证数据
            ChinapnrExclusiveLogWithBLOBs record = chinapnrService.selectChinapnrExclusiveLog(Long.parseLong(uuid));
            // 如果检证数据状态为未发送
            if (record != null && ChinaPnrConstant.STATUS_VERTIFY_OK.equals(record.getStatus())) {
                // 将状态更新成[2:处理中]
                record.setId(Long.parseLong(uuid));
                record.setStatus(ChinaPnrConstant.STATUS_RUNNING);
                this.chinapnrService.updateChinapnrExclusiveLog(record);
                updateFlag = true;
            }
        } else {
            updateFlag = true;
        }
        // 其他程序正在处理中,或者返回值错误
        if (!updateFlag) {
            // 执行结果(失败)
        	map.put("error", "1");
    		map.put("message", "购买失败,其他程序正在处理中");
        }
        // 发送状态
        String status = ChinaPnrConstant.STATUS_VERTIFY_OK;
        // 成功
        if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
            try {
                // 取得更新用id
                String pid = bean.get(ChinaPnrConstant.PARAM_MERPRIV);
                if(StringUtils.isNotEmpty(pid) ){
                	String ip = CustomUtil.getIpAddr(request);
                    // 调用成功后处理
                	int productListId = Integer.parseInt(pid);
                    this.htlCommonService.insertBuyProductReturn(bean,productListId,ip);
                }
            } catch (Exception e) {
                // 执行结果(失败)
            	map.put("error", "1");
        		map.put("message", "购买失败,"+bean.get(ChinaPnrConstant.PARAM_RESPDESC));
                status = ChinaPnrConstant.STATUS_FAIL;
                LogUtil.errorLog(THIS_CLASS, HtlCommonDefine.BUY_RETURN_MAPPING, e);
                //错误日志
                ProductErrorLog productErrorLog = new ProductErrorLog();
                productErrorLog.setAmount(new BigDecimal(bean.get(ChinaPnrConstant.PARAM_TRANSAMT)));
                Integer userId = chinapnrService.selectUserIdByUsrcustid(Long.valueOf(bean.getUsrCustId()));
                productErrorLog.setUserId(userId);
                productErrorLog.setDate(GetDate.getDate("yyyy-MM-dd HH:mm:ss"));
                productErrorLog.setInvestTime(GetDate.getNowTime10());
                productErrorLog.setOrderId(bean.get(ChinaPnrConstant.PARAM_ORDID));
                productErrorLog.setRemark("汇天利购买回调"+e.getCause());
                productErrorLog.setIsSms(0);
                this.htlCommonService.insertProductErrorLog(productErrorLog);
            }
            // 执行结果(成功)
        	map.put("error", "0");
    		map.put("message", "成功");

            status = ChinaPnrConstant.STATUS_SUCCESS;
            LogUtil.debugLog(THIS_CLASS, HtlCommonDefine.BUY_RETURN_MAPPING, "成功");
        } else {
            // 执行结果(失败)
        	map.put("error", "1");
    		map.put("message", "购买失败,"+bean.get(ChinaPnrConstant.PARAM_RESPDESC));
            status = ChinaPnrConstant.STATUS_FAIL;
            LogUtil.debugLog(THIS_CLASS, HtlCommonDefine.BUY_RETURN_MAPPING, "失败");
        }

        // 更新状态记录
        if (updateFlag && Validator.isNotNull(uuid)) {
            this.chinapnrService.updateChinapnrExclusiveLogStatus(Long.parseLong(uuid), status);
        }
        
        //返回页面
		String data = JSON.toJSONString(map);
		try {
			data = URLEncoder.encode(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LogUtil.errorLog(THIS_CLASS, HtlCommonDefine.BUY_RETURN_MAPPING, e);
		}
        String url = RETURN_URL + "backinfo/" + data ;
        ModelAndView modelAndView = new ModelAndView("redirect:" + url);
        LogUtil.endLog(THIS_CLASS, HtlCommonDefine.BUY_RETURN_MAPPING, "[交易完成后,回调结束]");
        return modelAndView;
	}

	/**
	 * 汇天利赎回
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = HtlCommonDefine.REDEEM_PRODUCT_MAPPING)
	public ModelAndView redeemProduct(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(THIS_CLASS, HtlCommonDefine.REDEEM_PRODUCT_MAPPING);
		ProductRedeem productRedeem = new ProductRedeem();
		Map<String, String> map = new HashMap<String, String>();
		String userStr = request.getParameter("uid");
		if (StringUtils.isEmpty(userStr)) {
			map.put("error", "1");
    		map.put("message", "用户未登陆");
		}else{
			int userId = Integer.parseInt(request.getParameter("uid"));
			String amount = request.getParameter("amount");// 出借金额
	        // 取得用户在汇付天下的客户号
	        AccountChinapnr accountChinapnrReceiver = this.htlCommonService.getAccountChinapnr(userId);
	        // 取得用户在汇付天下的客户号(白海燕)
	        AccountChinapnr accountChinapnrTender = this.htlCommonService.getAccountChinapnr(ProductConstants.BAI_USERID);//CustomConstants.BAI_USERID
	        // 取得用户在汇付天下的客户号(对公)
	        AccountChinapnr accountChinapnrTenderPub = this.htlCommonService.getAccountChinapnr(ProductConstants.PUB_USERID);//CustomConstants.BAI_USERID
	        
	        String status = "";
	        /***********调用接口***************/
	        try {
	        	productRedeem.setAmount(new BigDecimal(amount));
	        	productRedeem.setUserId(userId);
	        	productRedeem.setClient(1);
	        	String ip = CustomUtil.getIpAddr(request);
	        	status = this.htlCommonService.insertProductRedeem(accountChinapnrReceiver, accountChinapnrTender,accountChinapnrTenderPub, productRedeem,ip);
			} catch (Exception e) {
	            //错误日志
	            ProductErrorLog productErrorLog = new ProductErrorLog();
	            productErrorLog.setAmount(new BigDecimal(amount));
	            productErrorLog.setUserId(userId);
	            productErrorLog.setDate(GetDate.getDate("yyyy-MM-dd HH:mm:ss"));
	            productErrorLog.setInvestTime(GetDate.getNowTime10());
	            productErrorLog.setOrderId(productRedeem.getOrderId());
	            productErrorLog.setRemark("汇天利赎回异常"+e.getCause());
	            productErrorLog.setIsSms(0);
	            this.htlCommonService.insertProductErrorLog(productErrorLog);
			}
	        /***********end***************/
	        //调用成功
	        if(status.equals("成功")){
	        	map.put("error", "0");
	    		map.put("message", "赎回成功");
	        }else{
	        	map.put("error", "1");
	    		map.put("message", status);
	        }
		}
		String data = JSON.toJSONString(map);
		try {
			data = URLEncoder.encode(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LogUtil.errorLog(THIS_CLASS, HtlCommonDefine.BUY_RETURN_MAPPING, e);
		}

        String url = RETURN_URL + "backinfo/" + data;
        ModelAndView modelAndView = new ModelAndView("redirect:" + url);
        LogUtil.endLog(THIS_CLASS, HtlCommonDefine.REDEEM_PRODUCT_MAPPING);
        return modelAndView;
	}	

	/**
	 * 汇天利购买记录
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = HtlCommonDefine.PRODUCT_LIST_RECORD_MAPPING, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String getPrdocutListRecords(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(THIS_CLASS, HtlCommonDefine.PRODUCT_LIST_RECORD_MAPPING);
		ProductSearchForPage productList = new ProductSearchForPage();
		Map<String, Object> map = new HashMap<String, Object>();
		String userStr = request.getParameter("uid");
		if (StringUtils.isEmpty(userStr)) {
			map.put("error","用户未登陆");
			return JSON.toJSONString(map);
		}
		int userId = Integer.parseInt(userStr);
		//分页信息
		String pageStr = request.getParameter("pageIndex");
		String pageSizeStr = request.getParameter("pageSize");
		int page = 1;
		int pageSize = 10;
		if (StringUtils.isNotEmpty(pageStr)) {
			page = Integer.parseInt(pageStr);
		}
		if(StringUtils.isNotEmpty(pageSizeStr)){
			pageSize = Integer.parseInt(pageSizeStr);
		}
		int totalSize = 0;
		List<ProductSearchForPage> records;
		try {
			productList.setUserId(userId);
			productList.setLimitStart(pageSize * (page - 1));
			productList.setLimitEnd(pageSize);
			totalSize = this.htlCommonService.countBuyRecordPage(productList);
		    records =  this.htlCommonService.getProductList(productList);
		} catch (Exception e) {
	    	map.put("total", "0");
			map.put("lists","获取列表失败");
			return JSON.toJSONString(map);
		}
    	map.put("total", totalSize);
		map.put("lists",records);
		String rep = JSON.toJSONString(map);
		LogUtil.endLog(THIS_CLASS, HtlCommonDefine.PRODUCT_LIST_RECORD_MAPPING);
		return rep;
	}
	/**
	 * 汇天利赎回记录
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = HtlCommonDefine.PRODUCT_REDEEM_RECORD_MAPPING, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String getPrdocutRedeemRecords(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(THIS_CLASS, HtlCommonDefine.PRODUCT_REDEEM_RECORD_MAPPING);
		ProductSearchForPage productRedeem = new ProductSearchForPage();
		Map<String, Object> map = new HashMap<String, Object>();
		String userStr = request.getParameter("uid");
		if (StringUtils.isEmpty(userStr)) {
			map.put("error","用户未登陆");
			return JSON.toJSONString(map);
		}
		int userId = Integer.parseInt(userStr);
		//分页信息
		//分页信息
		String pageStr = request.getParameter("pageIndex");
		String pageSizeStr = request.getParameter("pageSize");
		int page = 1;
		int pageSize = 10;
		if (StringUtils.isNotEmpty(pageStr)) {
			page = Integer.parseInt(pageStr);
		}
		if(StringUtils.isNotEmpty(pageSizeStr)){
			pageSize = Integer.parseInt(pageSizeStr);
		}
		int totalSize = 0;
		List<ProductSearchForPage> records;
		try {
			productRedeem.setUserId(userId);
			productRedeem.setLimitStart(pageSize * (page - 1));
			productRedeem.setLimitEnd(pageSize);
			totalSize = this.htlCommonService.countRedeemRecordPage(productRedeem);
		    records =  this.htlCommonService.getProductRedeem(productRedeem);
		} catch (Exception e) {
	    	map.put("total","0");
			map.put("lists","获取列表失败");
			return JSON.toJSONString(map);
		}
		LogUtil.endLog(THIS_CLASS, HtlCommonDefine.PRODUCT_REDEEM_RECORD_MAPPING);
    	map.put("total", totalSize);
		map.put("lists",records);
		String rep = JSON.toJSONString(map);
		return rep;
	}

	/**
	 * 汇天利收益记录
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = HtlCommonDefine.PRODUCT_INTEREST_RECORD_MAPPING, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String getPrdocutInterestRecords(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(THIS_CLASS, HtlCommonDefine.PRODUCT_INTEREST_RECORD_MAPPING);
		ProductSearchForPage productInterest = new ProductSearchForPage();
		Map<String, Object> map = new HashMap<String, Object>();
		String userStr = request.getParameter("uid");
		if (StringUtils.isEmpty(userStr)) {
			map.put("error","用户未登陆");
			return JSON.toJSONString(map);
		}
		int userId = Integer.parseInt(userStr);
		//分页信息
		String pageStr = request.getParameter("pageIndex");
		String pageSizeStr = request.getParameter("pageSize");
		int page = 1;
		int pageSize = 10;
		if (StringUtils.isNotEmpty(pageStr)) {
			page = Integer.parseInt(pageStr);
		}
		if(StringUtils.isNotEmpty(pageSizeStr)){
			pageSize = Integer.parseInt(pageSizeStr);
		}
		int totalSize = 0;
		List<ProductSearchForPage> records;
		try {
			productInterest.setUserId(userId);
			productInterest.setLimitStart(pageSize * (page - 1));
			productInterest.setLimitEnd(pageSize);
			totalSize = this.htlCommonService.countInterestRecordPage(productInterest);
		    records =  this.htlCommonService.getProductInterestRecords(productInterest);
		} catch (Exception e) {
	    	map.put("total", "0");
			map.put("lists","获取列表失败");
			return JSON.toJSONString(map);
		}
		LogUtil.endLog(THIS_CLASS, HtlCommonDefine.PRODUCT_INTEREST_RECORD_MAPPING);
    	map.put("total", totalSize);
		map.put("lists",records);
		String rep = JSON.toJSONString(map);
		return rep;
	}

	/**
	 * 汇天利收益记录
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = HtlCommonDefine.PRODUCT_USER_INFO_MAPPING, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String getUserPrdocutInfo(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(THIS_CLASS, HtlCommonDefine.PRODUCT_USER_INFO_MAPPING);
		ProductSearchForPage productSearchForPage = new ProductSearchForPage();
		Map<String, Object> map = new HashMap<String, Object>();
		String userStr = request.getParameter("uid");
		if (StringUtils.isEmpty(userStr)) {
			map.put("error","用户未登陆");
			return JSON.toJSONString(map);
		}
		int userId = Integer.parseInt(userStr);
		//获取用户本金
		productSearchForPage.setUserId(userId);
		productSearchForPage = htlCommonService.selectUserPrincipal(productSearchForPage);
		BigDecimal principal = BigDecimal.ZERO;
		if(productSearchForPage != null){
		    principal = productSearchForPage.getPrincipal();
			if(principal == null){
				map.put("Principal", new BigDecimal("0.00"));
			}else{
				map.put("Principal", principal.divide(BigDecimal.ONE,2,BigDecimal.ROUND_DOWN));
			}
		}else{
			map.put("Principal", new BigDecimal("0.00"));
			productSearchForPage = new ProductSearchForPage();
		}
		//获取已提取收益
		productSearchForPage.setUserId(userId);
		productSearchForPage = htlCommonService.selectUserAlreadyInterest(productSearchForPage);
		BigDecimal interest = BigDecimal.ZERO;
		if(productSearchForPage != null){
			interest = productSearchForPage.getAlreadyInterest();
				if(interest == null){
					map.put("Interest", new BigDecimal("0.00") );
				}else{
					map.put("Interest", productSearchForPage.getAlreadyInterest().divide(BigDecimal.ONE,2,BigDecimal.ROUND_DOWN));
				}
		}else{
			map.put("Interest", new BigDecimal("0.00") );
			productSearchForPage = new ProductSearchForPage();
		}
		BigDecimal restAmountTemp = BigDecimal.ZERO;
		productSearchForPage.setUserId(userId);
		List<ProductSearchForPage> productSearchForPageList = this.htlCommonService.selectUserNotRedeemRecord(productSearchForPage);
		if(productSearchForPageList != null && productSearchForPageList.size() >0 ){
			for(int i = 0;i < productSearchForPageList.size(); i++){
				productSearchForPage = productSearchForPageList.get(i);
				restAmountTemp = restAmountTemp.add(ProductUtils.getInterest(productSearchForPage.getAmount(),productSearchForPage.getTime()));
			}
		}
		//未提取收益
    	map.put("restInterest",restAmountTemp.divide(BigDecimal.ONE,2,BigDecimal.ROUND_DOWN));
    	//利率
    	map.put("InterestRate",ProductConstants.INTEREST_RATE.divide(new BigDecimal("0.01"),2,BigDecimal.ROUND_DOWN));
        // 取得用户当前余额
        Account account = this.htlCommonService.getAccount(userId);
        map.put("userBalance",account.getBalance().divide(BigDecimal.ONE,2,BigDecimal.ROUND_DOWN));
        //汇天利可投金额
        Product product = htlCommonService.getProduct();
        if((product.getAllpupper().subtract(product.getTotal())).compareTo(BigDecimal.ZERO) > 0){
        	  map.put("userPupper",product.getAllpupper().subtract(product.getTotal()));
        }else{
        	map.put("userPupper",new BigDecimal("0.00"));
        }
        //用户可申购金额
        map.put("avaPurchase",product.getPupper().subtract(principal));
		LogUtil.endLog(THIS_CLASS, HtlCommonDefine.PRODUCT_USER_INFO_MAPPING);
		String rep = JSON.toJSONString(map);
		return rep;
	}
	

}
