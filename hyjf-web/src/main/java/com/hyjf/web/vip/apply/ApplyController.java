package com.hyjf.web.vip.apply;

import java.math.BigDecimal;
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
import com.hyjf.common.chinapnr.MerPriv;
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
import com.hyjf.web.WebBaseAjaxResultBean;
import com.hyjf.web.BaseController;
import com.hyjf.web.chinapnr.ChinapnrService;
import com.hyjf.web.util.WebUtils;

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
public class ApplyController extends BaseController {

	Logger _log = LoggerFactory.getLogger(ApplyController.class);

	private static String HOST = PropUtils.getSystem("hyjf.web.host").trim();
	private static String HOST_ASYN = PropUtils.getSystem("http.hyjf.web.host").trim();
	@Autowired
	private ChinapnrService chinapnrService;
	@Autowired
	private ApplyService applyService;

	/**
	 * 初期化,跳转到vip申请页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = ApplyDefine.INIT, method = RequestMethod.GET)
	public ModelAndView init(HttpServletRequest request) throws Exception {
		LogUtil.startLog(ApplyController.class.getName(), ApplyDefine.INIT);
		ModelAndView modeAndView = new ModelAndView(ApplyDefine.APPLY);
		Integer userId = WebUtils.getUserId(request);
		UsersInfo usersInfo = applyService.getUsersInfoByUserId(userId);
		// 是否vip
		boolean vipFlg = usersInfo != null && usersInfo.getVipId() != null ? true : false;
		modeAndView.addObject("vipFlg", vipFlg);
		// 获取用户的汇付信息
		LogUtil.endLog(ApplyController.class.getName(), ApplyDefine.INIT);
		return modeAndView;
	}

	/**
	 * 购买vip前校验
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = ApplyDefine.APPLY_CHECK, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public WebBaseAjaxResultBean applyCheck(HttpServletRequest request) throws Exception {
		LogUtil.startLog(ApplyController.class.getName(), ApplyDefine.INIT);
		Properties properties = PropUtils.getSystemResourcesProperties();
		String account = properties.getProperty(CustomConstants.HYJF_VIP_MONEY).trim();
		Integer userId = WebUtils.getUserId(request);
		// 参数验证成功，则返回url，否则不返回
		WebBaseAjaxResultBean result = new WebBaseAjaxResultBean();
		JSONObject checkResult = null;
		try {
			checkResult = applyService.checkParam(account, userId);
		} catch (Exception e) {
			LogUtil.errorLog(ApplyController.class.getName(), ApplyDefine.INIT, "系统异常", e);
			throw e;
		}
		String errorCode = checkResult.getString("error");
		if (errorCode != "0") {
			result.setStatus(false);
			result.setMessage(checkResult.getString("data"));
			result.setErrorCode(errorCode);
			if (errorCode == "1") {
				// 登录请求URL
				result.setHost(ApplyDefine.LOGIN_REQUEST_MAPPING);
			} else if (errorCode == "2") {
				// 用户开户URL
				result.setHost(ApplyDefine.OPEN_ACCOUNT_REQUEST_MAPPING);
			} else if (errorCode == "3") {
				// 用户开户URL
				result.setHost(ApplyDefine.RECHARGE_REQUEST_MAPPING);
			}

		} else {
			// 校验通过
			result.setStatus(true);
			// vip申请URL
			result.setHost(ApplyDefine.VIP_APPLY_ACTION_MAPPING);
		}
		// 获取用户的汇付信息
		LogUtil.endLog(ApplyController.class.getName(), ApplyDefine.INIT);
		return result;
	}

	/**
	 * vip申请，跳转到汇付画面
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = ApplyDefine.VIP_APPLY_ACTION)
	public ModelAndView vipApply(HttpServletRequest request) throws Exception {
		LogUtil.startLog(ApplyController.class.getName(), ApplyDefine.VIP_APPLY_ACTION);
		Integer userId = WebUtils.getUserId(request);
		Properties properties = PropUtils.getSystemResourcesProperties();
		String account = properties.getProperty(CustomConstants.HYJF_VIP_MONEY).trim();
		ModelAndView modelAndView = new ModelAndView();
		AccountChinapnr accountChinapnrBorrower = applyService.getAccountChinapnr(userId);
		Long usrcustid = accountChinapnrBorrower.getChinapnrUsrcustid();
		// 生成订单
		String orderId = GetOrderIdUtils.getOrderId2(Integer.valueOf(userId));
		// 调用用户账户支付接口
		// 回调路径
		String returl = HOST + ApplyDefine.REQUEST_MAPPING + ApplyDefine.VIP_APPLY_RETURN_ACTION + ".do";
		// 商户后台应答地址(必须)
		String bgRetUrl = HOST_ASYN + ApplyDefine.REQUEST_MAPPING + ApplyDefine.VIP_APPLY_RETURN_ASYN_ACTION + "1.do";
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
		//
		LogUtil.endLog(ApplyController.class.getName(), ApplyDefine.VIP_APPLY_ACTION);
		return modelAndView;
	}

	/**
	 * 用户账户支付回调(同步)
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = ApplyDefine.VIP_APPLY_RETURN_ACTION)
	public ModelAndView vipApplyReturn(HttpServletRequest request, HttpServletResponse response, @ModelAttribute ChinapnrBean bean) {
		LogUtil.startLog(ApplyController.class.getName(), ApplyDefine.VIP_APPLY_RETURN_ACTION);
		ModelAndView modelAndView = new ModelAndView();
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
		_log.info("用户账户支付回调,用户编号："+userId);
		UsersInfo userInfo = applyService.getUsersInfoByUserId(userId);
		if(userInfo.getVipId() != null){
			modelAndView.setViewName(ApplyDefine.APPLY_SUCCESS);
			LogUtil.infoLog(ApplyController.class.getName(),
					ApplyDefine.VIP_APPLY_RETURN_ACTION, "用户:" + userId
							+ ",会员已异步购买，购买成功!");
			_log.info("用户:" + userId+ ",会员异步购买，购买成功!");
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
				_log.info("vipApplyReturn购买返回结果："+record.toString());
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
					modelAndView.setViewName(ApplyDefine.APPLY_SUCCESS);
					LogUtil.infoLog(ApplyController.class.getName(),
							ApplyDefine.VIP_APPLY_RETURN_ACTION, "用户:" + userId
									+ ",成功购买会员!");
					_log.info("用户:" + userId + ",购买会员成功!(异步购买)");
					return modelAndView;
				} else if(ChinaPnrConstant.STATUS_RUNNING.equals(record
						.getStatus())&& StringUtils.equals(respCode, "000")){
					// 如果其他线程正在处理，则当前线程休眠1s再继续执行，避免抢夺资源
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						_log.info("线程异常");
					}
					updateFlag = true;
				} else {
					modelAndView.setViewName(ApplyDefine.APPLY_FAIL);
					LogUtil.infoLog(ApplyController.class.getName(), ApplyDefine.VIP_APPLY_RETURN_ACTION, "用户:" + userId + ",购买会员失败!");
					_log.info("用户:" + userId + ",购买会员失败!");
					
				}
			}
		}
		// 验签通过且汇付返回状态成功
		String status = StringUtils.EMPTY;
		if (updateFlag) {
			JSONObject result = null;
			try {
				BigDecimal transAmtBigDecimal = new BigDecimal(transAmt);
				_log.info("用户:" + userId + ",更新vip相关数据表");
				result = applyService.updateUserVip(userId, transAmtBigDecimal, request, ordId, usrCustId, merCustId);
				if (result != null && result.getInteger("upgradeId") != -1) {
					CommonParamBean paramBean = new CommonParamBean();
					paramBean.setUserId(String.valueOf(userId));
					// 2:vip礼包
					paramBean.setSendFlg(2);
					// vip编号
					paramBean.setVipId(result.getInteger("vipId"));
					// vip升级成长编号
					int upgradeId = result.getInteger("upgradeId");
					_log.info("用户:" + userId + ",购买会员成功（同步），发放礼包。");
					// 调用发放优惠券接口
					String jsonStr = CommonSoaUtils.sendUserCoupon(paramBean);
					JSONObject sendResult = JSONObject.parseObject(jsonStr);
					// 发放是否成功状态
					int sendStatus = sendResult.getIntValue("status");
					// 发放优惠券的数量
					int sendCount = sendResult.getIntValue("couponCount");
					if (sendStatus == 0 && sendCount > 0) {
						modelAndView.addObject("sendCount", sendCount);
						// 更新礼包发放状态
						applyService.updateGiftStatus(userId, upgradeId);
					}
					_log.info("用户:" + userId + "，礼包发放完毕。");
					modelAndView.setViewName(ApplyDefine.APPLY_SUCCESS);
					LogUtil.infoLog(ApplyController.class.getName(), ApplyDefine.VIP_APPLY_RETURN_ACTION, "用户:" + userId + ",成功购买会员!");
					status = ChinaPnrConstant.STATUS_SUCCESS;
				}else if(result != null && result.getInteger("upgradeId") == -1){
					// 排他校验 用户已经购买过会员
					modelAndView.setViewName(ApplyDefine.APPLY_SUCCESS);
					LogUtil.infoLog(ApplyController.class.getName(),
							ApplyDefine.VIP_APPLY_RETURN_ACTION, "用户:" + userId
									+ ",成功购买会员!");
					_log.info("用户:" + userId + ",购买会员成功!(异步购买)");
					return modelAndView;
				} else {
					modelAndView.setViewName(ApplyDefine.APPLY_FAIL);
					LogUtil.infoLog(ApplyController.class.getName(), ApplyDefine.VIP_APPLY_RETURN_ACTION, "用户:" + userId + ",购买会员失败!");
					status = ChinaPnrConstant.STATUS_FAIL;
				}
			} catch (Exception e) {
				if (result != null) {
					// 购买成功（无论是否发券）
					modelAndView.setViewName(ApplyDefine.APPLY_SUCCESS);
				} else {
					// 购买不成功
					modelAndView.setViewName(ApplyDefine.APPLY_FAIL);
				}
				LogUtil.infoLog(ApplyController.class.getName(), ApplyDefine.VIP_APPLY_RETURN_ACTION, "系统异常，用户:" + userId + ",购买VIP失败!");
				status = ChinaPnrConstant.STATUS_FAIL;
			}

		}else{
			// 验签失败
			modelAndView.setViewName(ApplyDefine.APPLY_FAIL);
			status = ChinaPnrConstant.STATUS_FAIL;
		}
		// 更新状态记录
		this.chinapnrService.updateChinapnrExclusiveLogStatus(Long.parseLong(uuid), status);
		// 获取用户的汇付信息
		LogUtil.endLog(ApplyController.class.getName(), ApplyDefine.VIP_APPLY_RETURN_ACTION);
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
	public ModelAndView vipApplyReturnAsyn(HttpServletRequest request,
			HttpServletResponse response, @ModelAttribute ChinapnrBean bean) {
		LogUtil.startLog(ApplyController.class.getName(),
				ApplyDefine.VIP_APPLY_RETURN_ASYN_ACTION);
		_log.info("会员购买支付异步回调开始---->ordId:"+bean.getOrdId());
		ModelAndView modelAndView = new ModelAndView();
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
		try {
			// 线程等待3s
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			_log.info("线程异常！");
		}
		_log.info("用户账户支付异步回调,用户编号："+userId);
		UsersInfo userInfo = applyService.getUsersInfoByUserId(userId);
		if (userInfo.getVipId() != null) {
			// modelAndView.setViewName(ApplyDefine.APPLY_FAIL);
			LogUtil.infoLog(ApplyController.class.getName(),
					ApplyDefine.VIP_APPLY_RETURN_ASYN_ACTION, "用户:" + userId
							+ ",会员已同步购买，购买成功!");
			_log.info("用户:" + userId+ ",会员已同步购买，购买成功!");
			return null;
		}
		// 检证日志编号
		String uuid = request.getParameter("uuid");
		// 更新状态，0：检证失败，1：已经更新，2：需要更新
		boolean updateFlag = false;
		if (StringUtils.isNotEmpty(uuid)) {
			// 取得检证数据
			ChinapnrExclusiveLogWithBLOBs record = chinapnrService
					.selectChinapnrExclusiveLog(Long.parseLong(uuid));
			// 如果检证数据状态为未发送
			if (record != null) {
				// 汇付返回状态码
				String respCode = record.getRespcode();
				_log.info("vipApplyReturnAsyn购买返回结果："+record.toString());
				// 检证通过时
				if (ChinaPnrConstant.STATUS_VERTIFY_OK.equals(record
						.getStatus()) && StringUtils.equals(respCode, "000")) {
					// 将状态更新成[2:处理中]
					record.setId(Long.parseLong(uuid));
					record.setResult(bean.getAllParams().toString());
					record.setStatus(ChinaPnrConstant.STATUS_RUNNING);
					int cnt = this.chinapnrService
							.updateChinapnrExclusiveLog(record);
					if (cnt > 0) {
						updateFlag = true;
					}
				} else if(ChinaPnrConstant.STATUS_SUCCESS.equals(record
						.getStatus()) && StringUtils.equals(respCode, "000")){
					// 已经执行过更新，不再需要执行vip的相关更新
					_log.info("用户:" + userId+ ",会员已同步购买，购买成功!");
					return null;
				} else if(ChinaPnrConstant.STATUS_RUNNING.equals(record
						.getStatus())&& StringUtils.equals(respCode, "000")){
					// 如果其他线程正在处理，则当前线程休眠1s再继续执行，避免抢夺资源
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						_log.info("线程异常");
					}
					updateFlag = true;
				} else {
					// 检证失败
					modelAndView.setViewName(ApplyDefine.APPLY_FAIL);
					LogUtil.infoLog(ApplyController.class.getName(),
							ApplyDefine.VIP_APPLY_RETURN_ASYN_ACTION, "用户:" + userId
									+ ",购买会员失败!");
					_log.info("用户:" + userId + ",购买会员失败!");
					
				}
			}
		}
		// 验签通过且汇付返回状态成功
		String status = StringUtils.EMPTY;
		if (updateFlag) {
			JSONObject result = null;
			try {
				BigDecimal transAmtBigDecimal = new BigDecimal(transAmt);
				_log.info("用户:" + userId + ",更新vip相关数据表");
				result = applyService.updateUserVip(userId, transAmtBigDecimal,
						request, ordId, usrCustId, merCustId);
				if (result != null && result.getInteger("upgradeId") != -1) {
					CommonParamBean paramBean = new CommonParamBean();
					paramBean.setUserId(String.valueOf(userId));
					// 2:vip礼包
					paramBean.setSendFlg(2);
					// vip编号
					paramBean.setVipId(result.getInteger("vipId"));
					// vip升级成长编号
					int upgradeId = result.getInteger("upgradeId");
					// 调用发放优惠券接口
					_log.info("用户:" + userId + ",购买会员成功（异步），发放礼包。");
					String jsonStr = CommonSoaUtils.sendUserCoupon(paramBean);
					JSONObject sendResult = JSONObject.parseObject(jsonStr);
					// 发放是否成功状态
					int sendStatus = sendResult.getIntValue("status");
					// 发放优惠券的数量
					int sendCount = sendResult.getIntValue("couponCount");
					if (sendStatus == 0 && sendCount > 0) {
						modelAndView.addObject("sendCount", sendCount);
						// 更新礼包发放状态
						applyService.updateGiftStatus(userId, upgradeId);
					}
					_log.info("用户:" + userId + "，礼包发放完毕。");
					modelAndView.setViewName(ApplyDefine.APPLY_SUCCESS);
					LogUtil.infoLog(ApplyController.class.getName(),
							ApplyDefine.VIP_APPLY_RETURN_ASYN_ACTION, "用户:" + userId
									+ ",成功购买会员!");
					status = ChinaPnrConstant.STATUS_SUCCESS;
				}else if(result != null && result.getInteger("upgradeId") == -1){
					// 排他校验 用户已经购买过会员
					_log.info("用户:" + userId + ",购买会员成功!(同步购买)");
					return null;
				} else {
					modelAndView.setViewName(ApplyDefine.APPLY_FAIL);
					LogUtil.infoLog(ApplyController.class.getName(),
							ApplyDefine.VIP_APPLY_RETURN_ASYN_ACTION, "用户:" + userId
									+ ",购买会员失败!");
					status = ChinaPnrConstant.STATUS_FAIL;
				}
			} catch (Exception e) {
				if (result != null) {
					// 购买成功（无论是否发券）
					modelAndView.setViewName(ApplyDefine.APPLY_SUCCESS);
				} else {
					// 购买不成功
					modelAndView.setViewName(ApplyDefine.APPLY_FAIL);
				}
				LogUtil.infoLog(ApplyController.class.getName(),
						ApplyDefine.VIP_APPLY_RETURN_ASYN_ACTION, "系统异常，用户:"
								+ userId + ",购买VIP失败!");
				status = ChinaPnrConstant.STATUS_FAIL;
			}

		}else{
			// 验签失败
			modelAndView.setViewName(ApplyDefine.APPLY_FAIL);
			status = ChinaPnrConstant.STATUS_FAIL;
		}
		// 更新状态记录
		this.chinapnrService.updateChinapnrExclusiveLogStatus(Long.parseLong(uuid), status);
		// 获取用户的汇付信息
		LogUtil.endLog(ApplyController.class.getName(),
				ApplyDefine.VIP_APPLY_RETURN_ASYN_ACTION);
		return modelAndView;
	}
}
