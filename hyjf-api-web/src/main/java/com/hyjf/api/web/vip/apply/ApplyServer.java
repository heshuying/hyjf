package com.hyjf.api.web.vip.apply;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.chinapnr.ChinapnrService;
import com.hyjf.common.chinapnr.MerPriv;
import com.hyjf.common.http.URLCodec;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.ChinapnrExclusiveLogWithBLOBs;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import com.hyjf.soa.apiweb.CommonParamBean;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.vip.apply.ApplyBean;
import com.hyjf.vip.apply.ApplyDefine;
import com.hyjf.vip.apply.ApplyResultBean;
import com.hyjf.vip.apply.ApplyService;

/**
 * <p>
 * vip购买
 * </p>
 * 
 * @author zhangjp
 * @version 1.0.0
 */
@Controller
// delete by zhangjp 会员停售  关闭会员购买接口 start
// @RequestMapping(ApplyDefine.REQUEST_MAPPING)
// delete by zhangjp 会员停售  关闭会员购买接口 end
public class ApplyServer extends BaseController {

	Logger _log = LoggerFactory.getLogger(ApplyServer.class);
	@Autowired
	private ApplyService applyService;
	
	@Autowired
	private ChinapnrService chinapnrService;
	
	/**
	 * 购买vip前校验
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = ApplyDefine.APPLY_CHECK, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public ApplyResultBean applyCheck(HttpServletRequest request,ApplyBean paramBean) throws Exception {
		LogUtil.startLog(ApplyServer.class.getName(), "/applyCheck");
		// 参数验证成功，则返回url，否则不返回
				ApplyResultBean result = new ApplyResultBean();
		if(!this.checkSign(paramBean, ApplyDefine.METHOD_APPLY_CHECK)){
			result.setStatus(BaseResultBean.STATUS_FAIL);
			result.setStatusDesc("验签失败！");
			_log.info("验签失败！---->"+JSONObject.toJSONString(paramBean));
			return result;
		}
		Properties properties = PropUtils.getSystemResourcesProperties();
		String account = properties.getProperty(CustomConstants.HYJF_VIP_MONEY).trim();
		Integer userId = paramBean.getUserId();
		
		
		JSONObject checkResult = null;
		try {
			checkResult = applyService.checkParam(account, userId);
		} catch (Exception e) {
			LogUtil.errorLog(ApplyServer.class.getName(), ApplyDefine.INIT, "系统异常", e);
			_log.info("系统异常！---->"+JSONObject.toJSONString(paramBean));
			throw e;
		}
		String errorCode = checkResult.getString("error");
		if (errorCode != "0") {
			result.setStatus(BaseResultBean.STATUS_FAIL);
			result.setStatusDesc(checkResult.getString("data"));
			result.setErrorCode(errorCode);
			_log.info("参数校验失败！---->userId:"+paramBean.getUserId()+"--->errorCode："+errorCode);
		} else {
			// 校验通过
			result.setStatus(BaseResultBean.STATUS_SUCCESS);
		}
		// 获取用户的汇付信息
		LogUtil.endLog(ApplyServer.class.getName(), "applyCheck");
		return result;
	}
	
	/**
	 * vip申请，跳转到汇付画面
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = ApplyDefine.VIP_APPLY_ACTION)
	public ModelAndView vipApply(HttpServletRequest request,ApplyBean paramBean) throws Exception {
		LogUtil.startLog(ApplyServer.class.getName(), ApplyDefine.VIP_APPLY_ACTION);
		ModelAndView modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_VIEW);
		if(!this.checkSign(paramBean, ApplyDefine.METHOD_VIP_APPLY)){
			_log.info("验签失败！---->"+JSONObject.toJSONString(paramBean));
			return null;
		}
		/*ApplyResultBean applycheck = this.applyCheck(request, paramBean);
		if(applycheck.getStatus() != ApplyResultBean.STATUS_SUCCESS){
			_log.info("参数校验失败！---->"+JSONObject.toJSONString(paramBean));
			return null;
		}*/
		Integer userId = paramBean.getUserId();
		String platform = paramBean.getPlatform();
		Properties properties = PropUtils.getSystemResourcesProperties();
		String account = properties.getProperty(CustomConstants.HYJF_VIP_MONEY).trim();
		AccountChinapnr accountChinapnrBorrower = applyService.getAccountChinapnr(userId);
		Long usrcustid = accountChinapnrBorrower.getChinapnrUsrcustid();
		// 生成订单
		String orderId = GetOrderIdUtils.getOrderId2(Integer.valueOf(userId));
		// 调用用户账户支付接口
		// 同步回调路径
		String returl = ApplyDefine.HOST + ApplyDefine.REQUEST_MAPPING + ApplyDefine.VIP_APPLY_RETURN_ACTION + ".do?platform=" + platform+"&callBackUrl="+paramBean.getCallBackUrl();
		// 异步回调路径
		String bgRetUrl = ApplyDefine.HOST_ASYN + ApplyDefine.REQUEST_MAPPING + ApplyDefine.VIP_APPLY_RETURN_ASYN_ACTION + ".do?platform=" + platform+"&callBackUrl="+paramBean.getCallBackUrl();
		ChinapnrBean chinapnrBean = new ChinapnrBean();
		// 接口版本号
		chinapnrBean.setVersion(ChinaPnrConstant.VERSION_10);
		// 消息类型
		chinapnrBean.setCmdId(ChinaPnrConstant.CMDID_USR_ACCT_PAY);
		// 订单号
		chinapnrBean.setOrdId(orderId);
		// 用户客户号
		chinapnrBean.setUsrCustId(String.valueOf(usrcustid));
		// 商户客户号
		chinapnrBean.setMerCustId(PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID));
		MerPriv merPriv = new MerPriv();
		merPriv.setUserId(userId);
		chinapnrBean.setMerPrivPo(merPriv);
		// 金额
		chinapnrBean.setTransAmt(CustomUtil.formatAmount(account));
		// 入账子账户
		chinapnrBean.setInAcctId(PropUtils.getSystem(ChinaPnrConstant.PROP_MERACCT10));
		// 入账子账户类别
		chinapnrBean.setInAcctType("MERDT");
		// 页面返回
		chinapnrBean.setRetUrl(returl);
		// 商户后台应答地址(必须)
		chinapnrBean.setBgRetUrl(bgRetUrl);
		try {
			modelAndView = ChinapnrUtil.callApi(chinapnrBean);
		} catch (Exception e) {
			e.printStackTrace();
			modelAndView = new ModelAndView(ApplyDefine.APPLY_FAIL);
			modelAndView.addObject("investDesc", "支付失败！");
		}
		_log.info("会员购买跳转汇付---->userId:"+paramBean.getUserId());
		LogUtil.endLog(ApplyServer.class.getName(), ApplyDefine.VIP_APPLY_ACTION);
		return modelAndView;
	}
	
	/**
	 * 用户账户支付回调（同步）
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = ApplyDefine.VIP_APPLY_RETURN_ACTION)
	public ModelAndView vipApplyReturn(HttpServletRequest request, HttpServletResponse response, @ModelAttribute ChinapnrBean bean) {
		LogUtil.startLog(ApplyServer.class.getName(), ApplyDefine.VIP_APPLY_RETURN_ACTION);
		_log.info("会员购买支付回调开始---->ordId:"+bean.getOrdId());
		ModelAndView modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_VIEW);
		bean.convert();
		// 支付金额
		String transAmt = bean.getTransAmt();
		// 订单号
		String ordId = bean.getOrdId();
		// 用户客户号
		String usrCustId = bean.getUsrCustId();
		// 商户客户号
		String merCustId = bean.getMerCustId();
		// 用户编号
		MerPriv merPriv = bean.getMerPrivPo();
		Integer userId = merPriv.getUserId();
		String platform = request.getParameter("platform");
		String callBackUrl = request.getParameter("callBackUrl");
		ApplyResultBean applyResult = new ApplyResultBean();
		// 调用方提供的回调url
		applyResult.setCallBackAction(URLCodec.decodeURL(callBackUrl));
		_log.info("会员购买支付回调,用户编号："+userId);
		UsersInfo userInfo = applyService.getUsersInfoByUserId(userId);
		if(userInfo.getVipId() != null){
			applyResult.setStatus(BaseResultBean.STATUS_SUCCESS);
			applyResult.setStatusDesc("会员已异步购买，购买成功!");
			LogUtil.infoLog(ApplyServer.class.getName(), ApplyDefine.VIP_APPLY_RETURN_ACTION, "用户:" + userId + ",会员已异步购买，购买成功!");
			_log.info("用户:" + userId+ ",会员已异步购买，购买成功!");
			modelAndView.addObject("callBackForm",applyResult);
			return modelAndView;
		}
		// 检证日志编号
		String uuid = request.getParameter("uuid");
		boolean updateFlag = false;
		if (StringUtils.isNotEmpty(uuid)) {
			// 取得检证数据
			ChinapnrExclusiveLogWithBLOBs record = chinapnrService.selectChinapnrExclusiveLog(Long.parseLong(uuid));
			// 如果检证数据状态为未发送
			if (record != null) {
				// 汇付返回状态码
				String respCode = record.getRespcode();
				_log.info("vipApplyReturn购买返回状态："+respCode);
				_log.info("vipApplyReturn检证状态："+record.getStatus());
				//_log.info("vipApplyReturn购买返回结果："+record.toString());
				// 检证通过时
				if (ChinaPnrConstant.STATUS_VERTIFY_OK.equals(record.getStatus()) && StringUtils.equals(respCode, "000")) {
					// 将状态更新成[2:处理中]
					record.setId(Long.parseLong(uuid));
					record.setResult(bean.getAllParams().toString());
					record.setStatus(ChinaPnrConstant.STATUS_RUNNING);
					int cnt = this.chinapnrService.updateChinapnrExclusiveLog(record);
					if (cnt > 0) {
						updateFlag = true;
					}
				} else if(ChinaPnrConstant.STATUS_SUCCESS.equals(record
						.getStatus()) && StringUtils.equals(respCode, "000")){
					_log.info("用户:" + userId + ",购买会员成功!(异步购买1)");
					applyResult.setStatus(BaseResultBean.STATUS_SUCCESS);
					applyResult.setStatusDesc("购买会员成功!(异步购买)!");
					modelAndView.addObject("callBackForm",applyResult);
					return modelAndView;
				}else if(ChinaPnrConstant.STATUS_RUNNING.equals(record
						.getStatus())&& StringUtils.equals(respCode, "000")){
					// 如果其他线程正在处理，则当前线程休眠1s再继续执行，避免抢夺资源
					_log.info("vipApplyReturn检证状态："+record.getStatus());
					_log.info("vipApplyReturn休眠1s");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						_log.info("线程异常");
					}
					updateFlag = true;
				} else {
					applyResult.setStatus(BaseResultBean.STATUS_FAIL);
					applyResult.setStatusDesc("检证失败!");
					LogUtil.infoLog(ApplyServer.class.getName(), ApplyDefine.VIP_APPLY_RETURN_ACTION, "用户:" + userId + ",购买会员失败!");
					_log.info("用户:" + userId + ",购买会员失败!1");
				}
			}
		}
		// 验签通过且汇付返回状态成功
		String status = StringUtils.EMPTY;
		if (updateFlag) {
			JSONObject result = null;
			try {
				BigDecimal transAmtBigDecimal = new BigDecimal(transAmt);
				_log.info("用户:" + userId + ",同步更新vip相关数据表");
				result = applyService.updateUserVip(userId, transAmtBigDecimal, request, ordId, usrCustId, merCustId,platform);
				if (result != null && result.getInteger("upgradeId") != -1) {
					CommonParamBean paramBean = new CommonParamBean();
					paramBean.setUserId(String.valueOf(userId));
					// 2:vip礼包
					paramBean.setSendFlg(2);
					// vip编号
					paramBean.setVipId(result.getInteger("vipId"));
					// vip升级成长编号
					int upgradeId = result.getInteger("upgradeId");
					_log.info("用户:" + userId + ",购买会员成功(同步)，发放礼包。");
					// 调用发放优惠券接口
					String jsonStr = CommonSoaUtils.sendUserCoupon(paramBean);
					JSONObject sendResult = JSONObject.parseObject(jsonStr);
					// 发放是否成功状态
					int sendStatus = sendResult.getIntValue("status");
					// 发放优惠券的数量
					int sendCount = sendResult.getIntValue("couponCount");
					if (sendStatus == 0 && sendCount > 0) {
						applyResult.setCouponCount(sendCount);
						// 更新礼包发放状态
						applyService.updateGiftStatus(userId, upgradeId);
					}
					_log.info("用户:" + userId + "，礼包发放完毕。");
					LogUtil.infoLog(ApplyServer.class.getName(), ApplyDefine.VIP_APPLY_RETURN_ACTION, "用户:" + userId + ",成功购买会员!");
					applyResult.setStatus(BaseResultBean.STATUS_SUCCESS);
					applyResult.setStatusDesc("会员购买成功!");
					status = ChinaPnrConstant.STATUS_SUCCESS;
				} else if(result != null && result.getInteger("upgradeId") == -1){
					// 排他校验 用户已经购买过会员
					_log.info("用户:" + userId + ",购买会员成功!(异步购买)2");
					applyResult.setStatus(BaseResultBean.STATUS_SUCCESS);
					applyResult.setStatusDesc("购买会员成功!(异步购买)!");
					modelAndView.addObject("callBackForm",applyResult);
					return modelAndView;
				} else {
					status = ChinaPnrConstant.STATUS_FAIL;
					applyResult.setStatus(BaseResultBean.STATUS_FAIL);
					applyResult.setStatusDesc("购买会员失败!2");
					LogUtil.infoLog(ApplyServer.class.getName(), ApplyDefine.VIP_APPLY_RETURN_ACTION, "用户:" + userId + ",购买会员失败!");
				}
			} catch (Exception e) {
				if (result != null) {
					// 购买成功（无论是否发券）
					applyResult.setStatus(BaseResultBean.STATUS_SUCCESS);
					applyResult.setStatusDesc("会员购买成功，未发放优惠券！");
				} else {
					// 购买不成功
					applyResult.setStatus(BaseResultBean.STATUS_FAIL);
					applyResult.setStatusDesc("系统异常，会员购买失败");
				}
				LogUtil.infoLog(ApplyServer.class.getName(), ApplyDefine.VIP_APPLY_RETURN_ACTION, "系统异常，用户:" + userId + ",购买VIP失败!");
				status = ChinaPnrConstant.STATUS_FAIL;
			}

		}else{
			// 购买失败
			status = ChinaPnrConstant.STATUS_FAIL;
			applyResult.setStatus(BaseResultBean.STATUS_FAIL);
			applyResult.setStatusDesc("会员购买失败！");
			LogUtil.infoLog(ApplyServer.class.getName(), ApplyDefine.VIP_APPLY_RETURN_ACTION, "验签失败！");
		}
		// 更新状态记录
		this.chinapnrService.updateChinapnrExclusiveLogStatus(Long.parseLong(uuid), status);
		// 获取用户的汇付信息
		LogUtil.endLog(ApplyServer.class.getName(), ApplyDefine.VIP_APPLY_RETURN_ACTION);
		modelAndView.addObject("callBackForm",applyResult);
		Map<String,String> postMap = new HashMap<String,String>();
		postMap.put("status", applyResult.getStatus());
		postMap.put("statusDesc", applyResult.getStatusDesc());
		
		//HttpPool.post(applyResult.getCallBackAction(), postMap);
		//HttpDeal.post(applyResult.getCallBackAction(), postMap);
		return modelAndView;
	}
	
	/**
	 * 用户账户支付回调（异步）
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = ApplyDefine.VIP_APPLY_RETURN_ASYN_ACTION)
	public ModelAndView vipApplyReturnAsyn(HttpServletRequest request, HttpServletResponse response, @ModelAttribute ChinapnrBean bean) {
		LogUtil.startLog(ApplyServer.class.getName(), ApplyDefine.VIP_APPLY_RETURN_ASYN_ACTION);
		_log.info("会员购买支付异步回调开始---->ordId:"+bean.getOrdId());
		ModelAndView modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_VIEW);
		bean.convert();
		// 支付金额
		String transAmt = bean.getTransAmt();
		// 订单号
		String ordId = bean.getOrdId();
		// 用户客户号
		String usrCustId = bean.getUsrCustId();
		// 商户客户号
		String merCustId = bean.getMerCustId();
		// 用户编号
		MerPriv merPriv = bean.getMerPrivPo();
		Integer userId = merPriv.getUserId();
		String platform = request.getParameter("platform");
		String callBackUrl = request.getParameter("callBackUrl");
		ApplyResultBean applyResult = new ApplyResultBean();
		// 调用方提供的回调url
		applyResult.setCallBackAction(URLCodec.decodeURL(callBackUrl));

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			_log.info("线程异常");
		}
		_log.info("会员购买支付异步回调,用户编号："+userId);
		UsersInfo userInfo = applyService.getUsersInfoByUserId(userId);
		if(userInfo.getVipId() != null){
			_log.info("用户:" + userId+ ",会员已同步购买，购买成功!");
			return null;
		}
		// 检证日志编号
		String uuid = request.getParameter("uuid");
		// 更新状态，0：检证失败，1：已经更新，2：需要更新
		boolean updateFlag = false;
		if (StringUtils.isNotEmpty(uuid)) {
			// 取得检证数据
			ChinapnrExclusiveLogWithBLOBs record = chinapnrService.selectChinapnrExclusiveLog(Long.parseLong(uuid));
			// 如果检证数据状态为未发送
			if (record != null) {
				// 汇付返回状态码
				String respCode = record.getRespcode();
				_log.info("vipApplyReturnAsyn购买返回状态："+respCode);
				_log.info("vipApplyReturnAsyn检证状态："+record.getStatus());
				//_log.info("vipApplyAsynReturn购买返回结果："+record.toString());
				// 检证通过时
				if (ChinaPnrConstant.STATUS_VERTIFY_OK.equals(record.getStatus()) && StringUtils.equals(respCode, "000")) {
					// 将状态更新成[2:处理中]
					record.setId(Long.parseLong(uuid));
					record.setResult(bean.getAllParams().toString());
					record.setStatus(ChinaPnrConstant.STATUS_RUNNING);
					int cnt = this.chinapnrService.updateChinapnrExclusiveLog(record);
					if (cnt > 0) {
						updateFlag = true;
					}
				} else if(ChinaPnrConstant.STATUS_SUCCESS.equals(record
						.getStatus()) && StringUtils.equals(respCode, "000")){
					_log.info("用户:" + userId + ",购买会员成功!(同步购买)1");
					//return null;
				} else if(ChinaPnrConstant.STATUS_RUNNING.equals(record
						.getStatus())&& StringUtils.equals(respCode, "000")){
					// 如果其他线程正在处理，则当前线程休眠1s再继续执行，避免抢夺资源
					_log.info("vipApplyReturnAsyn检证状态："+record.getStatus());
					_log.info("vipApplyReturnAsyn休眠1s");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						_log.info("线程异常");
					}
					updateFlag = true;
				} else {
					applyResult.setStatus(BaseResultBean.STATUS_FAIL);
					applyResult.setStatusDesc("检证失败!");
					LogUtil.infoLog(ApplyServer.class.getName(), ApplyDefine.VIP_APPLY_RETURN_ASYN_ACTION, "用户:" + userId + ",购买会员失败!");
					_log.info("用户:" + userId + ",购买会员失败!1");
					modelAndView.addObject("callBackForm",applyResult);
					return modelAndView;
				}
			}
		}
		// 验签通过且汇付返回状态成功
		String status = StringUtils.EMPTY;
		if (updateFlag) {
			JSONObject result = null;
			try {
				BigDecimal transAmtBigDecimal = new BigDecimal(transAmt);
				_log.info("用户:" + userId + ",异步更新vip相关数据表");
				result = applyService.updateUserVip(userId, transAmtBigDecimal, request, ordId, usrCustId, merCustId,platform);
				if (result != null && result.getInteger("upgradeId") != -1) {
					CommonParamBean paramBean = new CommonParamBean();
					paramBean.setUserId(String.valueOf(userId));
					// 2:vip礼包
					paramBean.setSendFlg(2);
					// vip编号
					paramBean.setVipId(result.getInteger("vipId"));
					// vip升级成长编号
					int upgradeId = result.getInteger("upgradeId");
					_log.info("用户:" + userId + ",购买会员成功(异步)，发放礼包。");
					// 调用发放优惠券接口
					String jsonStr = CommonSoaUtils.sendUserCoupon(paramBean);
					JSONObject sendResult = JSONObject.parseObject(jsonStr);
					// 发放是否成功状态
					int sendStatus = sendResult.getIntValue("status");
					// 发放优惠券的数量
					int sendCount = sendResult.getIntValue("couponCount");
					if (sendStatus == 0 && sendCount > 0) {
						applyResult.setCouponCount(sendCount);
						// 更新礼包发放状态
						applyService.updateGiftStatus(userId, upgradeId);
					}
					_log.info("用户:" + userId + "，礼包发放完毕。");
					LogUtil.infoLog(ApplyServer.class.getName(), ApplyDefine.VIP_APPLY_RETURN_ASYN_ACTION, "用户:" + userId + ",成功购买会员!");
					applyResult.setStatus(BaseResultBean.STATUS_SUCCESS);
					applyResult.setStatusDesc("会员购买成功!");
					status = ChinaPnrConstant.STATUS_SUCCESS;
				} else if(result != null && result.getInteger("upgradeId") == -1){
					// 排他校验 用户已经购买过会员
					_log.info("用户:" + userId + ",购买会员成功!(同步购买)2");
					return null;
				} else {
					status = ChinaPnrConstant.STATUS_FAIL;
					applyResult.setStatus(BaseResultBean.STATUS_FAIL);
					applyResult.setStatusDesc("购买会员失败!");
					LogUtil.infoLog(ApplyServer.class.getName(), ApplyDefine.VIP_APPLY_RETURN_ASYN_ACTION, "用户:" + userId + ",购买会员失败!");
				}
			} catch (Exception e) {
				if (result != null) {
					// 购买成功（无论是否发券）
					applyResult.setStatus(BaseResultBean.STATUS_SUCCESS);
					applyResult.setStatusDesc("会员购买成功，未发放优惠券！");
				} else {
					// 购买不成功
					applyResult.setStatus(BaseResultBean.STATUS_FAIL);
					applyResult.setStatusDesc("系统异常，会员购买失败");
				}
				LogUtil.infoLog(ApplyServer.class.getName(), ApplyDefine.VIP_APPLY_RETURN_ASYN_ACTION, "系统异常，用户:" + userId + ",购买VIP失败!");
				status = ChinaPnrConstant.STATUS_FAIL;
			}

		}else{
			// 购买失败
			status = ChinaPnrConstant.STATUS_FAIL;
			applyResult.setStatus(BaseResultBean.STATUS_FAIL);
			applyResult.setStatusDesc("汇付验签失败，会员购买失败！");
			LogUtil.infoLog(ApplyServer.class.getName(), ApplyDefine.VIP_APPLY_RETURN_ASYN_ACTION, "验签失败！");
		}
		// 更新状态记录
		this.chinapnrService.updateChinapnrExclusiveLogStatus(Long.parseLong(uuid), status);
		// 获取用户的汇付信息
		LogUtil.endLog(ApplyServer.class.getName(), ApplyDefine.VIP_APPLY_RETURN_ASYN_ACTION);
		modelAndView.addObject("callBackForm",applyResult);
		Map<String,String> postMap = new HashMap<String,String>();
		postMap.put("status", applyResult.getStatus());
		postMap.put("statusDesc", applyResult.getStatusDesc());
		
		//HttpPool.post(applyResult.getCallBackAction(), postMap);
		//HttpDeal.post(applyResult.getCallBackAction(), postMap);
		return modelAndView;
	}
	
}
