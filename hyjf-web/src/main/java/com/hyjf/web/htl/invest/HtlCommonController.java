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

import java.math.BigDecimal;
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
import com.hyjf.web.htl.statistics.HtlStatisticsService;
import com.hyjf.web.tender.paginator.WebPaginator;
import com.hyjf.web.util.WebUtils;

/**
 * 汇天利资金中心接口
 * @author Michael
 */
@Controller
@RequestMapping(value = HtlCommonDefine.REQUEST_MAPPING)
public class HtlCommonController extends BaseController {
    /** THIS_CLASS */
    private static final String THIS_CLASS = ChinapnrUtil.class.getName();

	@Autowired
	private HtlCommonService htlCommonService;
	
	@Autowired
	private HtlStatisticsService htlStatisticsService;

	@Autowired
	private ChinapnrService chinapnrService;
	
    /** 购买url */
    private static String BUYCALLURL = "/hyjf-web/htl/buyProduct.do";
    
    /** 赎回url */
    private static String REDEEMCALLURL = "/hyjf-web/htl/redeemProduct.do";
	
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
        // 交易金额
        String transAmt = request.getParameter("amount");
        // 购买为1  赎回为2
        int flag = Integer.parseInt(request.getParameter("flag")); 
        // 用户ID
        Integer userId = WebUtils.getUserId(request); 
        // 检查参数
        Map<String,String> result = this.htlCommonService.check(userId, transAmt,flag);
        
        if (result != null && result.size() > 0) {
            return JSON.toJSONString(result);
        }
        if(flag == 1){
        	result.put("url",BUYCALLURL+"?amount="+transAmt);
        }else if (flag ==2){
        	result.put("url",REDEEMCALLURL+"?amount="+transAmt+"&userId="+userId);
        }else{
        	result.put("error","1");
        	result.put("data","请求参数不合法");
        }
		String res = JSON.toJSONString(result);
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
        Integer userId = WebUtils.getUserId(request); // 用户ID
		String amount = request.getParameter("amount");// 出借金额
		if(userId == null || StringUtils.isEmpty(amount)){
			modelAndView = new ModelAndView(HtlCommonDefine.ERROR_PAGE);
			return modelAndView;
		}
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
		String retUrl = host + request.getContextPath() + HtlCommonDefine.REQUEST_MAPPING+HtlCommonDefine.BUY_RETURN_MAPPING; //返回路径
	//	String retBgUrl = host + request.getContextPath() + HtlCommonDefine.REQUEST_MAPPING+HtlCommonDefine.BUY_RETURN_MAPPING; //返回路径
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
			productList.setClient(0);
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
            e.printStackTrace();
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
	@RequestMapping(value = HtlCommonDefine.BUY_RETURN_MAPPING)
	public ModelAndView buyProductReturn(HttpServletRequest request, @ModelAttribute ChinapnrBean bean) {
		LogUtil.startLog(THIS_CLASS, HtlCommonDefine.BUY_RETURN_MAPPING,"[交易完成后,回调开始]");
		ModelAndView modelAndView =null;
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
    		modelAndView = new ModelAndView(HtlCommonDefine.INVEST_ERROR_PAGE);
    		modelAndView.addObject("message", "购买失败,其他程序正在处理中");
    		return modelAndView;
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
                //失败返回
        		modelAndView = new ModelAndView(HtlCommonDefine.INVEST_ERROR_PAGE);
        		modelAndView.addObject("message",  "出借失败,"+bean.get(ChinaPnrConstant.PARAM_RESPDESC));
        		return modelAndView;
            }
            status = ChinaPnrConstant.STATUS_SUCCESS;
            LogUtil.debugLog(THIS_CLASS, HtlCommonDefine.BUY_RETURN_MAPPING, "成功");
        } else {
            // 执行结果(失败)
            status = ChinaPnrConstant.STATUS_FAIL;
            LogUtil.debugLog(THIS_CLASS, HtlCommonDefine.BUY_RETURN_MAPPING, "失败");
            //失败返回
    		modelAndView = new ModelAndView(HtlCommonDefine.INVEST_ERROR_PAGE);
    		modelAndView.addObject("message",  "出借失败,"+bean.get(ChinaPnrConstant.PARAM_RESPDESC));
    		return modelAndView;
        }
        // 更新状态记录
        if (updateFlag && Validator.isNotNull(uuid)) {
            this.chinapnrService.updateChinapnrExclusiveLogStatus(Long.parseLong(uuid), status);
        }
        //成功
        modelAndView = new ModelAndView(HtlCommonDefine.INVEST_SUCCESS_PAGE);
        modelAndView.addObject("amount", bean.get(ChinaPnrConstant.PARAM_TRANSAMT));
        modelAndView.addObject("interestDate", GetDate.getCountDate(5, 1));
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
		ModelAndView modelAndView = null;
		ProductRedeem productRedeem = new ProductRedeem();
        Integer userId =  WebUtils.getUserId(request); // 用户ID
		String amount = request.getParameter("amount");// 出借金额
        // 取得用户在汇付天下的客户号
        AccountChinapnr accountChinapnrReceiver = this.htlCommonService.getAccountChinapnr(userId);
        // 取得用户在汇付天下的客户号(白海燕)
        AccountChinapnr accountChinapnrTender = this.htlCommonService.getAccountChinapnr(ProductConstants.BAI_USERID);//CustomConstants.BAI_USERID
        // 取得用户在汇付天下的客户号(对公)
        AccountChinapnr accountChinapnrTenderPub = this.htlCommonService.getAccountChinapnr(ProductConstants.PUB_USERID);//CustomConstants.BAI_USERID
       // map = this.htlCommonService.check(userId, amount, 2);
        String status = "";
        /***********调用接口***************/
        try {
        	productRedeem.setAmount(new BigDecimal(amount));
        	productRedeem.setUserId(userId);
        	productRedeem.setClient(0);
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
            productErrorLog.setRemark("汇天利赎回"+e.getCause());
            productErrorLog.setIsSms(0);
            this.htlCommonService.insertProductErrorLog(productErrorLog);
            e.printStackTrace();
		}
        /***********end***************/
        //调用成功
        if(status.equals("成功")){
    		modelAndView = new ModelAndView(HtlCommonDefine.REDEEM_SUCCESS_PAGE);
        }else{
    		modelAndView = new ModelAndView(HtlCommonDefine.REDEEM_ERROR_PAGE);
    		modelAndView.addObject("message", status);
        }
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
	public String getProductListRecords(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(THIS_CLASS, HtlCommonDefine.PRODUCT_LIST_RECORD_MAPPING);
		ProductSearchForPage productList = new ProductSearchForPage();
		Map<String, Object> map = new HashMap<String, Object>();
		Integer userId =  WebUtils.getUserId(request); // 用户ID
		if (userId == null) {
			map.put("error","用户未登陆");
			return JSON.toJSONString(map);
		}
		//时间检索
		String dateStartStr = request.getParameter("dateStartStr");
		String dateEndStr = request.getParameter("dateEndStr");
		if(StringUtils.isNotEmpty(dateStartStr)){
			productList.setDateStartStr(GetDate.get10Time(dateStartStr));
		}
		if(StringUtils.isNotEmpty(dateEndStr)){
			productList.setDateEndStr(GetDate.get10Time(dateEndStr));
		}
		//分页信息
		String pageStr = request.getParameter("pageIndex");
		int page = 1;
		int pageSize = 10;
		if (StringUtils.isNotEmpty(pageStr)) {
			page = Integer.parseInt(pageStr);
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
		WebPaginator paginator = new WebPaginator(page, totalSize,pageSize, "getProductListRecords", null);
    	map.put("total", totalSize);
		map.put("lists",records);
		map.put("webPaginator", paginator);
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
	public String getProductRedeemRecords(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(THIS_CLASS, HtlCommonDefine.PRODUCT_REDEEM_RECORD_MAPPING);
		ProductSearchForPage productRedeem = new ProductSearchForPage();
		Map<String, Object> map = new HashMap<String, Object>();
		Integer userId =  WebUtils.getUserId(request); // 用户ID
		if (userId == null) {
			map.put("error","用户未登陆");
			return JSON.toJSONString(map);
		}
		//时间检索
		String dateStartStr = request.getParameter("dateStartStr");
		String dateEndStr = request.getParameter("dateEndStr");
		if(StringUtils.isNotEmpty(dateStartStr)){
			productRedeem.setDateStartStr(GetDate.get10Time(dateStartStr));
		}
		if(StringUtils.isNotEmpty(dateEndStr)){
			productRedeem.setDateEndStr(GetDate.get10Time(dateEndStr));
		}
		//分页信息
		String pageStr = request.getParameter("pageIndex");
		int page = 1;
		int pageSize = 10;
		if (StringUtils.isNotEmpty(pageStr)) {
			page = Integer.parseInt(pageStr);
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
		WebPaginator paginator = new WebPaginator(page, totalSize,pageSize, "getProductRedeemRecords", null);
    	map.put("total", totalSize);
		map.put("lists",records);
		map.put("webPaginator", paginator);
		String rep = JSON.toJSONString(map);
		LogUtil.endLog(THIS_CLASS, HtlCommonDefine.PRODUCT_REDEEM_RECORD_MAPPING);
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
	public String getProductInterestRecords(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(THIS_CLASS, HtlCommonDefine.PRODUCT_INTEREST_RECORD_MAPPING);
		ProductSearchForPage productInterest = new ProductSearchForPage();
		Map<String, Object> map = new HashMap<String, Object>();

		Integer userId =  WebUtils.getUserId(request); // 用户ID
		if (userId == null) {
			map.put("error","用户未登陆");
			return JSON.toJSONString(map);
		}
		//时间检索
		String dateStartStr = request.getParameter("dateStartStr");
		String dateEndStr = request.getParameter("dateStartStr");
		if(StringUtils.isNotEmpty(dateStartStr)){
			productInterest.setDateStartStr(GetDate.get10Time(dateStartStr));
		}
		if(StringUtils.isNotEmpty(dateEndStr)){
			productInterest.setDateEndStr(GetDate.get10Time(dateEndStr));
		}
		//分页信息
		String pageStr = request.getParameter("pageIndex");
		int page = 1;
		int pageSize = 10;
		if (StringUtils.isNotEmpty(pageStr)) {
			page = Integer.parseInt(pageStr);
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
		WebPaginator paginator = new WebPaginator(page, totalSize,pageSize, "getProductInterestRecords", null);
    	map.put("total", totalSize);
		map.put("lists",records);
		map.put("webPaginator", paginator);
		String rep = JSON.toJSONString(map);
		LogUtil.endLog(THIS_CLASS, HtlCommonDefine.PRODUCT_INTEREST_RECORD_MAPPING);
		return rep;
	}

	/**
	 * 汇天利用户信息
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = HtlCommonDefine.PRODUCT_USER_INFO_MAPPING)
	public ModelAndView getUserProdcutInfo(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(THIS_CLASS, HtlCommonDefine.PRODUCT_USER_INFO_MAPPING);
		ModelAndView modelAndView = null;
		HtlCommonBean form = new  HtlCommonBean();
		Integer userId =  WebUtils.getUserId(request);// 用户ID
		if (userId == null) {
			modelAndView = new ModelAndView(HtlCommonDefine.LOGIN_PAGE);
			return modelAndView;
		}
		//获取用户本金
		ProductSearchForPage productSearchForPage = new ProductSearchForPage();
		productSearchForPage.setUserId(userId);
		productSearchForPage = htlCommonService.selectUserPrincipal(productSearchForPage);
		BigDecimal principal = BigDecimal.ZERO;
		if(productSearchForPage != null){
		    principal = productSearchForPage.getPrincipal();
			if(principal == null){
				form.setUserPrincipal("0.00");
			}else{
				form.setUserPrincipal(CustomConstants.DF_FOR_VIEW.format(principal.divide(BigDecimal.ONE,2,BigDecimal.ROUND_DOWN)));
			}
		}else{
			form.setUserPrincipal("0.00");
			productSearchForPage = new ProductSearchForPage();
		}
		//获取已提取收益
		productSearchForPage.setUserId(userId);
		productSearchForPage = htlCommonService.selectUserAlreadyInterest(productSearchForPage);
		BigDecimal interest = BigDecimal.ZERO;
		if(productSearchForPage != null){
			interest = productSearchForPage.getAlreadyInterest();
				if(interest == null){
					form.setExtractInterest("0.00");
				}else{
					form.setExtractInterest(CustomConstants.DF_FOR_VIEW.format(productSearchForPage.getAlreadyInterest().divide(BigDecimal.ONE,2,BigDecimal.ROUND_DOWN)));
				}
		}else{
			form.setExtractInterest("0.00");
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
    	form.setNotExtractInterest(CustomConstants.DF_FOR_VIEW.format(restAmountTemp.divide(BigDecimal.ONE,2,BigDecimal.ROUND_DOWN)));
    	//利率
    	form.setHtlRate(CustomConstants.DF_FOR_VIEW.format(ProductConstants.INTEREST_RATE.divide(new BigDecimal("0.01"),2,BigDecimal.ROUND_DOWN)));
    	modelAndView = new ModelAndView(HtlCommonDefine.HTL_USER_DETAIL_PAGE);
    	modelAndView.addObject(HtlCommonDefine.FORM,form);
		LogUtil.endLog(THIS_CLASS, HtlCommonDefine.PRODUCT_USER_INFO_MAPPING);
		return modelAndView;
	}

	
	/**
	 * 用户赎回时 实时可得收益
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = HtlCommonDefine.PRODUCT_USER_REDEEMINTEREST_MAPPING, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String getUserRedeemInterest(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(THIS_CLASS, HtlCommonDefine.PRODUCT_USER_REDEEMINTEREST_MAPPING);
		Map<String, Object> map = new HashMap<String, Object>();
		Integer userId =  WebUtils.getUserId(request); // 用户ID
		String amountStr = request.getParameter("amount");
		String principalStr = request.getParameter("principal");
		BigDecimal amount = BigDecimal.ZERO;
		BigDecimal principal = BigDecimal.ZERO;
		if (userId == null) {
			map.put("error","用户未登陆");
			return JSON.toJSONString(map);
		}
		if(StringUtils.isNotEmpty(amountStr)){
			amount = new BigDecimal(amountStr);
		}else{
			map.put("error","赎回金额不可为空");
			return JSON.toJSONString(map);
		}
		
		if(StringUtils.isNotEmpty(principalStr)){
			principal = new BigDecimal(principalStr);
		}else{
			//获取用户本金
			ProductSearchForPage productSearchForPage = new ProductSearchForPage();
			productSearchForPage.setUserId(userId);
			productSearchForPage = htlCommonService.selectUserPrincipal(productSearchForPage);
			if(productSearchForPage != null){
			    principal = productSearchForPage.getPrincipal();
			}
		}
		if(principal.compareTo(amount) < 0){
			map.put("error","赎回金额大于可用本金");
			return JSON.toJSONString(map);
		}
		//获取收益
		BigDecimal interest = htlCommonService.getRedeemInterest(userId, amount);
		if(interest == null){
			map.put("RedeemInterest", "0.00");
		}else{
			map.put("RedeemInterest",CustomConstants.DF_FOR_VIEW.format(interest.divide(BigDecimal.ONE,2,BigDecimal.ROUND_DOWN)));
		}
		LogUtil.endLog(THIS_CLASS, HtlCommonDefine.PRODUCT_USER_REDEEMINTEREST_MAPPING);
		String rep = JSON.toJSONString(map);
		return rep;
	}
	
/**----------------------新版新增michael--------------------------------*/	
	
	/**
	 * 汇天利信息
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = HtlCommonDefine.HTLINFO_MAPPING)
	@ResponseBody
	public ModelAndView getHtlInfo(HttpServletRequest request, HtlCommonBean form) {
		LogUtil.startLog(THIS_CLASS, HtlCommonDefine.HTLINFO_MAPPING);
		ModelAndView modelAndView = new ModelAndView(HtlCommonDefine.HTLINFO_PAGE);
//        Product product = htlCommonService.getProduct();
//		//利率
//		form.setHtlRate(ProductConstants.INTEREST_RATE.divide(new BigDecimal("0.01"),2,BigDecimal.ROUND_DOWN).toString());
//    	//汇天利可申购金额
//		if((product.getAllpupper().subtract(product.getTotal())).compareTo(BigDecimal.ZERO) > 0){
//	     	 form.setAvaPurchase(CustomConstants.DF_FOR_VIEW.format(product.getAllpupper().subtract(product.getTotal()))); 
//		}else{
//			 form.setAvaPurchase("0.00");
//		}
//        //汇天利单户上限
//        form.setUserPupper(CustomConstants.DF_FOR_VIEW.format(product.getPupper()));
//        //汇天利累计交易额
//        form.setHtlTotalSum(CustomConstants.DF_FOR_VIEW.format(new BigDecimal(htlStatisticsService.searchTotalStatistics().getTotalSum())));
//        modelAndView.addObject(HtlCommonDefine.FORM,form);
        LogUtil.endLog(THIS_CLASS, HtlCommonDefine.HTLINFO_MAPPING);
		return modelAndView;
	}

	/**
	 * 汇天利出借页
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = HtlCommonDefine.HTLINVEST_MAPPING)
	@ResponseBody
	public ModelAndView moveToInvestPage(HttpServletRequest request, HtlCommonBean form) {
		LogUtil.startLog(THIS_CLASS, HtlCommonDefine.HTLINVEST_MAPPING);
		ModelAndView modelAndView = null;
		Integer userid = WebUtils.getUserId(request);
		//未登陆 跳转登陆页面
		if(userid == null){
			modelAndView = new ModelAndView(HtlCommonDefine.LOGIN_PAGE);
			return modelAndView;
		}else{
			modelAndView = new ModelAndView(HtlCommonDefine.HTLINVEST_PAGE);
		}
		
        Product product = htlCommonService.getProduct();
        
      //获取用户本金
		ProductSearchForPage productSearchForPage = new ProductSearchForPage();
		productSearchForPage.setUserId(userid);
		productSearchForPage = htlCommonService.selectUserPrincipal(productSearchForPage);
		BigDecimal principal = BigDecimal.ZERO;
		if(productSearchForPage != null){
		    principal = productSearchForPage.getPrincipal();
		}
		form.setUserPrincipal(CustomConstants.DF_FOR_VIEW.format(principal));
		//利率
		form.setHtlRate(ProductConstants.INTEREST_RATE.divide(new BigDecimal("0.01"),2,BigDecimal.ROUND_DOWN).toString());
    	//汇天利可申购金额
		if((product.getAllpupper().subtract(product.getTotal())).compareTo(BigDecimal.ZERO) > 0){
	     	 form.setAvaPurchase(CustomConstants.DF_FOR_VIEW.format(product.getAllpupper().subtract(product.getTotal()))); 
		}else{
			 form.setAvaPurchase("0.00");
		}
        //汇天利单户上限
        form.setUserPupper(CustomConstants.DF_FOR_VIEW.format(product.getPupper()));
        //汇天利累计交易额
        form.setHtlTotalSum(CustomConstants.DF_FOR_VIEW.format(new BigDecimal(htlStatisticsService.searchTotalStatistics().getTotalSum())));
        //用户当前余额
        Account account = this.htlCommonService.getAccount(userid);
        form.setUserBalance(CustomConstants.DF_FOR_VIEW.format(account.getBalance().divide(BigDecimal.ONE,2,BigDecimal.ROUND_DOWN)));
        //用户可申购金额
        form.setUserAvaPurchase(CustomConstants.DF_FOR_VIEW.format(product.getPupper().subtract(principal)));
        //计息日期
        form.setInterestDate(GetDate.getCountDate(5, 1));
        
        /**-------------前端校验用---------------**/
        //汇天利可申购金额(数字金额)
        form.setAvaPurchaseNumber(product.getAllpupper().subtract(product.getTotal()));
        //用户可申购金额(数字金额)
        form.setUserAvaPurchaseNumber(product.getPupper().subtract(principal));
        //用户当前余额(数字金额)
        form.setUserBalanceNumber(account.getBalance().divide(BigDecimal.ONE,2,BigDecimal.ROUND_DOWN));
        
        modelAndView.addObject(HtlCommonDefine.FORM,form);
        LogUtil.endLog(THIS_CLASS, HtlCommonDefine.HTLINVEST_MAPPING);
		return modelAndView;
	}

	/**
	 * 汇天利赎回页
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = HtlCommonDefine.HTLREDEEM_MAPPING)
	@ResponseBody
	public ModelAndView moveToRedeemPage(HttpServletRequest request, HtlCommonBean form) {
		LogUtil.startLog(THIS_CLASS, HtlCommonDefine.HTLREDEEM_MAPPING);
		ModelAndView modelAndView = null;
		Integer userid = WebUtils.getUserId(request);
		//未登陆 跳转登陆页面
		if(userid == null){
			modelAndView = new ModelAndView(HtlCommonDefine.LOGIN_PAGE);
			return modelAndView;
		}else{
			modelAndView = new ModelAndView(HtlCommonDefine.HTLREDEEM_PAGE);
		}
		
        Product product = htlCommonService.getProduct();
        
        //获取用户本金
		ProductSearchForPage productSearchForPage = new ProductSearchForPage();
		productSearchForPage.setUserId(userid);
		productSearchForPage = htlCommonService.selectUserPrincipal(productSearchForPage);
		BigDecimal principal = BigDecimal.ZERO;
		if(productSearchForPage != null){
		    principal = productSearchForPage.getPrincipal();
		}
		form.setUserPrincipal(CustomConstants.DF_FOR_VIEW.format(principal));
		//利率
		form.setHtlRate(ProductConstants.INTEREST_RATE.divide(new BigDecimal("0.01"),2,BigDecimal.ROUND_DOWN).toString());
        //汇天利单户上限
        form.setUserPupper(CustomConstants.DF_FOR_VIEW.format(product.getPupper()));
        //汇天利本金（数字，前端校验）
        form.setUserPrincipalNumber(principal);
        modelAndView.addObject(HtlCommonDefine.FORM,form);
        LogUtil.endLog(THIS_CLASS, HtlCommonDefine.HTLREDEEM_MAPPING);
		return modelAndView;
	}

	

}
