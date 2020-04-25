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
package com.hyjf.web.user.invest;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import redis.clients.jedis.JedisPool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.AverageCapitalPlusInterestUtils;
import com.hyjf.common.calculate.AverageCapitalUtils;
import com.hyjf.common.calculate.BeforeInterestAfterPrincipalUtils;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.chinapnr.MerPriv;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.web.BaseController;
import com.hyjf.web.chinapnr.ChinapnrService;

/**
 * @package com.hyjf.web.borrow.invest
 * @author 郭勇
 * @date 2015/12/04 14:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = InvestDefine.WECHAT_REQUEST_MAPPING)
public class WeChatInvestController extends BaseController {

	@Autowired
	private InvestService investService;
	@Autowired
	private ChinapnrService chinapnrService;
	public static JedisPool pool = RedisUtils.getConnection();

	@RequestMapping(value = "/test")
	@ResponseBody
	public String test(HttpServletRequest request, HttpServletResponse response) {
		String data = request.getParameter("backinfo");
		System.out.println("data===========" + data);
		return data;
	}

	/**
	 * 获取相应的待投金额
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getBorrowAccountWait")
	public String getBorrowAccountWait(HttpServletRequest request, HttpServletResponse response) {
		String borrowNid = request.getParameter("borrowNid");
		String wait = null;
		try {
			wait = RedisUtils.get(borrowNid);
		} catch (Exception e) {
			Borrow borrow = investService.getBorrowByNid(borrowNid);
			if (borrow != null) {
				wait = borrow.getBorrowAccountWait().toString();
			}
		}
		if (StringUtils.isBlank(wait)) {
			Borrow borrow = investService.getBorrowByNid(borrowNid);
			if (borrow != null) {
				wait = borrow.getBorrowAccountWait().toString();
			}
		}
		if (StringUtils.isBlank(wait)) {
			wait = "0.00";
		}
		return wait;
	}

	/**
	 * 
	 * 根据出借项目id获取出借信息
	 * 
	 * @author 王坤
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = InvestDefine.GET_INVEST_INFO_MAPPING)
	public String getInvestInfo(HttpServletRequest request, HttpServletResponse response) {

		String borrowNid = request.getParameter("borrowNid");
		String money = request.getParameter("money");
		BigDecimal earnings = new BigDecimal("0");
		// 查询项目信息
		Borrow borrow = investService.getBorrowByNid(borrowNid);
		if (null != borrow) {
			String balanceWait = borrow.getBorrowAccountWait() + "";
			if (balanceWait == null || balanceWait.equals("")) {
				balanceWait = "0";
			}
			// 刚加载页面并且可投小于起投
			if ((StringUtils.isBlank(money) || money.equals("0"))
					&& new BigDecimal(balanceWait).compareTo(new BigDecimal(borrow.getTenderAccountMin())) < 1) {
				money = new BigDecimal(balanceWait).intValue() + "";
			}
			if (!StringUtils.isBlank(money) && Long.parseLong(money) > 0) {// 如果出借金额不为空
				String borrowStyle = borrow.getBorrowStyle();
				// 收益率
				BigDecimal borrowApr = borrow.getBorrowApr();
				if (borrow.getProjectType() == 13 && borrow.getBorrowExtraYield() != null
						&& borrow.getBorrowExtraYield().compareTo(BigDecimal.ZERO) > 0) {
					borrowApr = borrowApr.add(borrow.getBorrowExtraYield());
				}
				// 周期
				Integer borrowPeriod = borrow.getBorrowPeriod();
				switch (borrowStyle) {
				case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“：历史回报=出借金额*年化收益÷12*月数；
					// 计算历史回报
					earnings = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(money),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
					break;
				case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“：历史回报=出借金额*年化收益÷360*天数；
					earnings = DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(money),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
					break;
				case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：历史回报=出借金额*年化收益÷12*月数；
					earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(new BigDecimal(money),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod, borrowPeriod).setScale(2,
							BigDecimal.ROUND_DOWN);
					break;
				case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：历史回报=出借金额*年化收益÷12*月数；
					earnings = AverageCapitalPlusInterestUtils.getInterestCount(new BigDecimal(money),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
					break;
				case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
					earnings = AverageCapitalUtils.getInterestCount(new BigDecimal(money),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
					break;
				default:
					break;
				}
			}
		}
		return earnings + "";
	}

	/**
	 * 微信出借验证
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = InvestDefine.INVEST_CHECK_ACTION, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String tenderCheck(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(InvestController.class.toString(), InvestDefine.INVEST_CHECK_ACTION);
		String borrowNid = request.getParameter("nid");// 借款borrowNid,如HBD120700101
		String account = request.getParameter("num");// 出借金额
		String couponGrantId = request.getParameter("couponGrantId");
		if (account == null || "".equals(account)) {
			account = "0";
		}
		String userId = StringUtils.isBlank(request.getParameter("userId")) ? "0" : request.getParameter("userId");// 用户id
		JSONObject result = this.investService.checkParam(borrowNid, account, Integer.parseInt(userId), "1",
				couponGrantId);
		// 参数验证成功，则返回url，否则不返回
		Map<String, String> map = new HashMap<String, String>();
		if (result == null) {
			map.put("error", "1");
			map.put("message", "出借失败");
		} else if (result.get("error") != null && result.get("error").equals("0")) {
			if (StringUtils.isNotEmpty(couponGrantId) && !couponGrantId.equals("-1")) {
				// 调用 优惠券出借校验接口
				JSONObject resultcoupon = CommonSoaUtils.CheckCoupon(userId, borrowNid, account, "1", couponGrantId);
				LogUtil.infoLog(InvestServiceImpl.class.toString(), "updateCouponTender", "优惠券出借校验结果：" + resultcoupon);
				System.out.println("updateCouponTender" + "优惠券出借校验结果：" + resultcoupon);
				if (MapUtils.isNotEmpty(resultcoupon)) {
					if (resultcoupon.get("status") != null && (resultcoupon.get("status") + "").equals("0")) {
						map.put("error", "0");
						map.put("message", "操作成功!");
						String returl = PropUtils.getSystem("hyjf.web.host").trim() + request.getContextPath()
								+ "/weChatInvest/tender";
						map.put("url", returl);
					} else {
						map.put("error", "1");
						map.put("message", resultcoupon.get("statusDesc") + "");
					}

				} else {
					map.put("error", "1");
					map.put("message", "验证失败!");
				}
			} else {
				map.put("error", "0");
				map.put("message", "操作成功");
				String returl = PropUtils.getSystem("hyjf.web.host").trim() + request.getContextPath()
						+ "/weChatInvest/tender";
				map.put("url", returl);
			}

		} else {
			map.put("error", "1");
			map.put("message", result.getString("data"));
		}
		String data = JSON.toJSONString(map);
		LogUtil.endLog(InvestController.class.toString(), InvestDefine.INVEST_CHECK_ACTION);
		return data;
	}

	/**
	 * 微信出借
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = InvestDefine.INVEST_ACTION)
	public ModelAndView tender(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(WeChatInvestController.class.toString(), InvestDefine.INVEST_ACTION);

		ModelAndView modelAndView = new ModelAndView();
		String borrowNid = request.getParameter("nid");// 借款borrowNid,如HBD120700101
		String account = request.getParameter("num");// 出借金额
		String userId = StringUtils.isBlank(request.getParameter("userId")) ? "0" : request.getParameter("userId");// 用户id
		String callback = request.getParameter("callback");// 返回地址
		// 出借金额
		if (account == null || "".equals(account)) {
			account = "0";
		}
		// add by zhangjp 优惠券出借 start
		// 优惠券用户id （coupon_user的id）
		String couponGrantId = request.getParameter("couponGrantId");
		System.err.println("couponGrantId:" + couponGrantId);
		// 排他校验时间
		int couponOldTime = Integer.MIN_VALUE;
		CouponConfigCustomizeV2 cuc = null;
		// -1:有可用的优惠券，但是出借时不选择优惠券 空或null：用户没有可用优惠券
		if (StringUtils.isNotEmpty(couponGrantId) && !StringUtils.equals("-1", couponGrantId)) {
			cuc = investService.getCouponUser(couponGrantId, Integer.parseInt(userId));
			// 排他check用
			couponOldTime = cuc.getUserUpdateTime();
		}
		// add by zhangjp 优惠券出借 end
		BigDecimal decimalAccount = StringUtils.isNotEmpty(account) ? new BigDecimal(account) : BigDecimal.ZERO;
		if (decimalAccount.compareTo(BigDecimal.ZERO) != 1 && cuc != null
				&& (cuc.getCouponType() == 3 || cuc.getCouponType() == 1)) {
			// 优惠券出借调用
			JSONObject resultcoupon = CommonSoaUtils.CouponInvestForPC(userId, borrowNid, account, "1", couponGrantId, "",
					GetCilentIP.getIpAddr(request), couponOldTime + "");
			if (MapUtils.isNotEmpty(resultcoupon)) {
				if ((resultcoupon.get("status") + "").equals("0")) {
					Borrow borrow = investService.getBorrowByNid(borrowNid);
					// 取得项目类别
					BorrowProjectType borrowProjectType = investService.getBorrowProjectType(borrow.getProjectType()
							.toString());
					/** 修改出借成功页面显示修改开始 */
					String projectTypeName = investService.getProjectTypeName(borrowProjectType);
					// 跳转到成功画面
					Map<String, String> map = new HashMap<String, String>();
					map.put("error", "0");
					map.put("message", "优惠券出借成功");
					map.put("borrowNid", borrowNid);
					map.put("projectTypeName", projectTypeName);
					// 优惠券收益
					map.put("couponInterest", resultcoupon.get("couponInterest") + "");
					// 优惠券类别
					map.put("couponType", resultcoupon.get("couponType") + "");
					// 优惠券额度
					map.put("couponQuota", resultcoupon.get("couponQuota") + "");
					String url = JSON.toJSONString(map);

					try {
						url = URLEncoder.encode(url, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					return new ModelAndView("redirect:" + callback + "backinfo/" + url);
				} else {
					Map<String, String> map = new HashMap<String, String>();
					map.put("error", "1");
					map.put("message", resultcoupon.get(CustomConstants.APP_STATUS_DESC) + "");
					String url = JSON.toJSONString(map);
					try {
						url = URLEncoder.encode(url, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					return new ModelAndView("redirect:" + callback + "backinfo/" + url);
				}

			} else {
				Map<String, String> map = new HashMap<String, String>();
				map.put("error", "1");
				map.put("message", "优惠券出借失败！");
				String url = JSON.toJSONString(map);
				try {
					url = URLEncoder.encode(url, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return new ModelAndView("redirect:" + callback + "backinfo/" + url);
			}
		}
		JSONObject result = this.investService.checkParam(borrowNid, account, Integer.parseInt(userId), "1",
				couponGrantId);
		// 参数验证成功，则返回url，否则不返回
		Map<String, String> map = new HashMap<String, String>();
		if (result == null) {
			map.put("error", "1");
			map.put("message", "出借失败");
			String url = JSON.toJSONString(map);
			try {
				url = URLEncoder.encode(url, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return new ModelAndView("redirect:" + "backinfo/" + callback + url);
		}
		if (result.get("error") != null && result.get("error").equals("1")) {
			String url = JSON.toJSONString(result);
			try {
				url = URLEncoder.encode(url, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return new ModelAndView("redirect:" + callback + "backinfo/" + url);
		}
		String tenderUsrcustid = result.getString("tenderUsrcustid");
		String borrowerUsrcustid = result.getString("borrowerUsrcustid");
		// 生成订单
		String orderId = GetOrderIdUtils.getOrderId2(Integer.valueOf(userId));
		// 写日志
		Boolean flag = investService.updateBeforeChinaPnR(borrowNid, orderId, Integer.valueOf(userId), account,
				GetCilentIP.getIpAddr(request));
		// 成功后调用出借接口
		if (flag) {
			/** userId和callback不能替换,如果需要添加参数，请添加于callback前，王坤 20160812 耗费时间8H */
			// 回调路径
			String returl = PropUtils.getSystem("hyjf.web.host").trim() + InvestDefine.WECHAT_REQUEST_MAPPING
					+ InvestDefine.RETURL_SYN_ACTION + ".do?userId=" + userId + "&couponGrantId=" + couponGrantId
					+ "&couponOldTime=" + couponOldTime + "&callback=" + callback;
			// 异步回调路径
			String bgRetUrl = PropUtils.getSystem("http.hyjf.web.host").trim() + InvestDefine.WECHAT_REQUEST_MAPPING
					+ InvestDefine.RETURL_ASY_ACTION + ".do?userId=" + userId + "&couponGrantId=" + couponGrantId
					+ "&couponOldTime=" + couponOldTime + "&callback=" + callback;
			ChinapnrBean chinapnrBean = new ChinapnrBean();
			// 接口版本号
			chinapnrBean.setVersion(ChinaPnrConstant.VERSION_20);// 2.0
			// 消息类型(主动投标)
			chinapnrBean.setCmdId(ChinaPnrConstant.CMDID_INITIATIVE_TENDER);
			// 用户客户号
			chinapnrBean.setUsrCustId(tenderUsrcustid);
			// 订单号(必须)
			chinapnrBean.setOrdId(orderId);
			// 订单时间(必须)格式为yyyyMMdd，例如：20130307
			chinapnrBean.setOrdDate(GetDate.getServerDateTime(1, new Date()));
			// 交易金额(必须)
			chinapnrBean.setTransAmt(CustomUtil.formatAmount(account));
			// 手续费率最高0.1
			chinapnrBean.setMaxTenderRate("0.10");
			chinapnrBean.setBorrowerCustId(borrowerUsrcustid);
			chinapnrBean.setBorrowerAmt(CustomUtil.formatAmount(account));
			chinapnrBean.setBorrowerRate("0.30");
			MerPriv merPriv = new MerPriv();
			merPriv.setBorrowNid(borrowNid);
			chinapnrBean.setMerPrivPo(merPriv);
			chinapnrBean.setLogUserId(Integer.valueOf(userId));
			Map<String, String> map1 = new HashMap<String, String>();
			map1.put("BorrowerCustId", borrowerUsrcustid);
			map1.put("BorrowerAmt", CustomUtil.formatAmount(account));
			map1.put("BorrowerRate", "0.30");// 云龙提示
			if (result.getString("bankInputFlag") != null && result.getString("bankInputFlag").equals("1")) {
				map1.put("ProId", borrowNid);// 2.0新增字段
				chinapnrBean.setProId(borrowNid);// 2.0新增字段
			}
			JSONArray array = new JSONArray();
			array.add(map1);
			String BorrowerDetails = JSON.toJSONString(array);
			System.out.println("微信用户出借回调returl:" + returl);
			chinapnrBean.setBorrowerDetails(BorrowerDetails);
			chinapnrBean.setRetUrl(returl); // 页面返回
			chinapnrBean.setBgRetUrl(bgRetUrl); // 商户后台应答地址(必须)
			chinapnrBean.setIsFreeze("Y"); // 2.0冻结类型
			chinapnrBean.setFreezeOrdId(orderId); // 2.0冻结订单号
			// 日志相关参数
			// 日志类型
			chinapnrBean.setType("user_tender"); // 日志类型
			// 平台
			chinapnrBean.setLogClient("1");
			try {
				modelAndView = ChinapnrUtil.callApi(chinapnrBean);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("error", "1");
				map.put("message", "出借失败");
				String url = JSON.toJSONString(map);
				try {
					url = URLEncoder.encode(url, "UTF-8");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				return new ModelAndView("redirect:" + callback + "backinfo/" + url);
			}
		}
		LogUtil.endLog(WeChatInvestController.class.toString(), InvestDefine.INVEST_ACTION);
		return modelAndView;
	}

	/**
	 * 同步回调结果
	 * 
	 * @param request
	 * @param response
	 * @param bean
	 * @return
	 */
	@RequestMapping(InvestDefine.RETURL_SYN_ACTION)
	public String tenderRetUrl(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute ChinapnrBean bean) {

		System.out.println("微信出借同步开始！");
		String error = "1";
		String message = "出借失败";
		String callback = request.getParameter("callback");
		String userIdStr = request.getParameter("userId");
		Integer userId = 0;
		if (StringUtils.isNotBlank(userIdStr)) {
			userId = Integer.valueOf(userIdStr);
		} else {
			return returnUrl(error, message, callback, "", "", "", 0);
		}
		bean.convert();
		// 获取项目编号
		MerPriv merPriv = bean.getMerPrivPo();
		String borrowNid = merPriv.getBorrowNid();
		if (StringUtils.isBlank(borrowNid)) {
			message = "回调时,borrowNid为空!";
			return returnUrl(error, message, callback, "", "", "", 0);
		}
		Borrow borrow = investService.getBorrowByNid(borrowNid);
		// 冻结标示
		String freezeTrxId = bean.getFreezeTrxId();
		Integer borrowUserId = borrow != null ? borrow.getUserId() : 0;
		String account = bean.getTransAmt();
		String ordId = bean.getOrdId();
		String ordDate = bean.getOrdDate();
		String uuid = request.getParameter("uuid");
		String respCode = bean.getRespCode();
		// add by zxs 优惠券出借 start
		// 优惠券用户id （coupon_user的id）
		String couponGrantId = request.getParameter("couponGrantId");
		// 是否用优惠券 0没用 1 用了
		String couponFlag = "0";
		System.out.println(couponGrantId);
		String tempTime = request.getParameter("couponOldTime");
		int couponOldTime = StringUtils.isNotEmpty(tempTime) ? Integer.valueOf(tempTime) : 0;
		// add by zxs 优惠券出借 end

		// 操作记录更新标识
		boolean updateFlag = investService.countBorrowTenderNum(userId, borrowNid, ordId) <= 0 ? true : false;
		// 出借成功
		System.out.println("同步微信用户:" + userId + "***********************************出借接口结果码：" + respCode);
		// 发送状态
		String status = ChinaPnrConstant.STATUS_FAIL;
		if (updateFlag) {
			if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))
					|| ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)) {
				// 冻结成功，写各个表
				String ip = GetCilentIP.getIpAddr(request);
				// 传递参数
				bean.setLogUserId(userId);
				bean.setLogClient("1");
				bean.setLogIp(ip);
				JSONObject info = investService.userTender(borrow, bean);
				if (info.getString("status").equals(ChinaPnrConstant.STATUS_VERTIFY_OK)) {
					error = "0";
					message = "恭喜您出借成功!";
					status = ChinaPnrConstant.STATUS_SUCCESS;
					System.out.println("微信用户:" + userId + "***********************************出借成功：" + account);

					if (info.getString("isExcute") != null && info.getString("isExcute").equals("1")) {
						// add by zxs 优惠券出借 start
						if (StringUtils.isNotEmpty(couponGrantId)) {
							// 进行出借
							// 优惠券出借调用
							CommonSoaUtils.CouponInvestForPC(userId + "", borrowNid, account, "1", couponGrantId, ordId,
									ip, couponOldTime + "");
							couponFlag = "1";
						}
						// add by zxs 优惠券出借 end
						// 如果是融通宝项目并且费率不为null和0
						if (borrow.getProjectType() == 13 && borrow.getBorrowExtraYield() != null
								&& borrow.getBorrowExtraYield().compareTo(new BigDecimal("0")) > 0) {
							// 额外收益出借开始zxs
							// TODO
							investService.extraUeldInvest(borrow, bean);
							// 额外收益出借结束zxs
						}

					}
					//如果是融通宝项目并且费率不为null和0
				} else {
					System.out.println("微信用户:" + userId + "***********************************冻结成功后处理失败：" + account);
					message = info.getString("message");
					boolean tenderFlag = investService.updateBorrowTenderTmp(userId, borrowNid, ordId) > 0 ? true
							: false;
					if (tenderFlag) {
						status = ChinaPnrConstant.STATUS_FAIL;
						// 冻结失败 ，解冻
						try {
							boolean flag = investService.unFreezeOrder(borrowUserId, userId, ordId, freezeTrxId,
									ordDate);
							if (!flag) {
								message = "出借解冻失败,请联系客服人员!";
							}
						} catch (Exception e1) {
							e1.printStackTrace();
							message = "出借解冻失败,请联系客服人员!";
						}
					} else {
						error = "0";
						message = "恭喜您出借成功!";
						status = ChinaPnrConstant.STATUS_SUCCESS;
						System.out.println("PC用户:" + userId + "**相应订单的出借记录存在,出借成功,订单号：" + ordId + "**调用汇付接口返回错误码"
								+ respCode);
					}
				}
			} else {
				// 364返回码提示余额不足，不结冻
				if (ChinaPnrConstant.RESPCODE_YUE2_FAIL.equals(respCode)) {
					System.out.println("微信用户:" + userId + "**出借接口调用失败，余额不足，错误码：" + respCode);
					message = "出借失败，可用余额不足！请联系客服！";
					status = ChinaPnrConstant.STATUS_FAIL;
				} else {
					boolean tenderFlag = investService.countBorrowTenderNum(userId, borrowNid, ordId) <= 0 ? true
							: false;
					if (tenderFlag) {
						System.out.println("微信用户:" + userId + "**出借接口调用失败,订单号：" + ordId + "**调用解冻接口**汇付接口返回错误码"
								+ respCode);
						status = ChinaPnrConstant.STATUS_FAIL;
						// 冻结失败 ，解冻
						try {
							boolean flag = investService.unFreezeOrder(borrowUserId, userId, ordId, freezeTrxId,
									ordDate);
							if (flag) {
								message = "出借失败！";
							} else {
								message = "出借解冻失败,请联系客服人员！";
							}
						} catch (Exception e1) {
							e1.printStackTrace();
							message = "出借解冻失败,请联系客服人员！";
						}
					} else {
						error = "0";
						message = "恭喜您出借成功！";
						status = ChinaPnrConstant.STATUS_SUCCESS;
						System.out.println("微信用户:" + userId + "**相应订单的出借记录存在,出借else成功,订单号：" + ordId + "**调用汇付接口返回错误码"
								+ respCode);
					}
				}
			}
		} else {
			if (StringUtils.isNotEmpty(couponGrantId)) {
				couponFlag = "1";
			}
			error = "0";
			message = "恭喜您出借成功!";
			status = ChinaPnrConstant.STATUS_SUCCESS;
		}
		// 更新状态记录
		if (updateFlag && Validator.isNotNull(uuid)) {
			this.chinapnrService.updateChinapnrExclusiveLogStatus(Long.parseLong(uuid), status);
		}
		if (status.equals(ChinaPnrConstant.STATUS_FAIL)) {
			return returnUrl(error, message, callback, "", "", "", 0);
		} else {
			String result = "";
			BigDecimal earnings = new BigDecimal("0");
			DecimalFormat df = CustomConstants.DF_FOR_VIEW;
			// 如果出借金额不为空
			if (!StringUtils.isBlank(account) && Double.parseDouble(account) > 0) {
				String borrowStyle = borrow.getBorrowStyle();
				// 收益率
				BigDecimal borrowApr = borrow.getBorrowApr();
				// 融通宝收益叠加
				if (borrow.getProjectType() == 13 && borrow.getBorrowExtraYield() != null
						&& borrow.getBorrowExtraYield().compareTo(BigDecimal.ZERO) > 0) {
					borrowApr = borrowApr.add(borrow.getBorrowExtraYield());
				}
				// 周期
				Integer borrowPeriod = borrow.getBorrowPeriod();
				switch (borrowStyle) {
				case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“：历史回报=出借金额*年化收益÷12*月数；
					// 计算历史回报
					earnings = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(account),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
					break;
				case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“：历史回报=出借金额*年化收益÷360*天数；
					earnings = DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(account),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
					break;
				case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：历史回报=出借金额*年化收益÷12*月数；
					earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(new BigDecimal(account),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod, borrowPeriod).setScale(2,
							BigDecimal.ROUND_DOWN);
					break;
				case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：历史回报=出借金额*年化收益÷12*月数；
					earnings = AverageCapitalPlusInterestUtils.getInterestCount(new BigDecimal(account),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
					break;
				case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
					earnings = AverageCapitalUtils.getInterestCount(new BigDecimal(account),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
					break;
				default:
					break;
				}
				result = df.format(earnings);
			}
			return returnUrl(error, message, callback, result, account, couponFlag, borrow.getProjectType());
		}
	}

	/**
	 * 出借异步回调结果
	 * 
	 * @param request
	 * @param response
	 * @param bean
	 * @return
	 */
	@RequestMapping(InvestDefine.RETURL_ASY_ACTION)
	public String tenderBgtUrl(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute ChinapnrBean bean) {

		System.out.println("微信出借异步开始！");
		String error = "1";
		String message = "出借失败!";
		String callback = request.getParameter("callback");
		String userIdStr = request.getParameter("userId");
		Integer userId = 0;
		if (StringUtils.isNotBlank(userIdStr)) {
			userId = Integer.valueOf(userIdStr);
		} else {
			return returnUrl(error, message, callback, "", "", "", 0);
		}
		bean.convert();
		// 获取项目编号
		MerPriv merPriv = bean.getMerPrivPo();
		String borrowNid = merPriv.getBorrowNid();
		if (StringUtils.isBlank(borrowNid)) {
			message = "回调时,borrowNid为空";
			return returnUrl(error, message, callback, "", "", "", 0);
		}
		Borrow borrow = investService.getBorrowByNid(borrowNid);
		// 冻结标示
		String freezeTrxId = bean.getFreezeTrxId();
		Integer borrowUserId = borrow != null ? borrow.getUserId() : 0;
		String account = bean.getTransAmt();
		String ordId = bean.getOrdId();
		String ordDate = bean.getOrdDate();
		String uuid = request.getParameter("uuid");
		String respCode = bean.getRespCode();
		// add by zxs 优惠券出借 start
		// 优惠券用户id （coupon_user的id）
		String couponGrantId = request.getParameter("couponGrantId");
		System.out.println(couponGrantId);
		String tempTime = request.getParameter("couponOldTime");
		int couponOldTime = StringUtils.isNotEmpty(tempTime) ? Integer.valueOf(tempTime) : 0;
		// add by zxs 优惠券出借 end
		// 操作记录更新标识
		boolean updateFlag = investService.countBorrowTenderNum(userId, borrowNid, ordId) <= 0 ? true : false;
		// 出借成功
		System.out.println("异步微信用户:" + userId + "***********************************出借接口结果码：" + respCode);
		// 发送状态
		String status = ChinaPnrConstant.STATUS_FAIL;
		if (updateFlag) {
			if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))
					|| ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)) {
				// 冻结成功，写各个表
				String ip = GetCilentIP.getIpAddr(request);
				// 传递参数
				bean.setLogUserId(userId);
				bean.setLogClient("1");
				bean.setLogIp(ip);
				JSONObject info = investService.userTender(borrow, bean);
				if (info.getString("status").equals(ChinaPnrConstant.STATUS_VERTIFY_OK)) {
					error = "0";
					message = "恭喜您出借成功!";
					status = ChinaPnrConstant.STATUS_SUCCESS;
					System.out.println("微信用户:" + userId + "***********************************出借成功：" + account);

					if (info.getString("isExcute") != null && info.getString("isExcute").equals("1")) {
						// add by zxs 优惠券出借 start
						if (StringUtils.isNotEmpty(couponGrantId)) {
							// 进行出借
							// 优惠券出借调用
							CommonSoaUtils.CouponInvestForPC(userId + "", borrowNid, account, "1", couponGrantId, ordId,
									ip, couponOldTime + "");
						}
						// 如果是融通宝项目并且费率不为null和0
						if (borrow.getProjectType() == 13 && borrow.getBorrowExtraYield() != null
								&& borrow.getBorrowExtraYield().compareTo(new BigDecimal("0")) > 0) {
							// 额外收益出借开始zxs
							// TODO
							investService.extraUeldInvest(borrow, bean);
							// 额外收益出借结束zxs
						}
					}
				} else {
					System.out.println("微信用户:" + userId + "***********************************冻结成功后处理失败：" + account);
					message = info.getString("message");
					boolean tenderFlag = investService.updateBorrowTenderTmp(userId, borrowNid, ordId) > 0 ? true
							: false;
					if (tenderFlag) {
						status = ChinaPnrConstant.STATUS_FAIL;
						// 冻结失败 ，解冻
						try {
							boolean flag = investService.unFreezeOrder(borrowUserId, userId, ordId, freezeTrxId,
									ordDate);
							if (!flag) {
								message = "出借解冻失败,请联系客服人员!";
							}
						} catch (Exception e1) {
							e1.printStackTrace();
							message = "出借解冻失败,请联系客服人员!";
						}
					} else {
						error = "0";
						message = "恭喜您出借成功!";
						status = ChinaPnrConstant.STATUS_SUCCESS;
						System.out.println("微信用户:" + userId + "**相应订单的出借记录存在,出借成功,订单号：" + ordId + "**调用汇付接口返回错误码"
								+ respCode);
					}
				}
			} else {
				// 364返回码提示余额不足，不结冻
				if (ChinaPnrConstant.RESPCODE_YUE2_FAIL.equals(respCode)) {
					System.out.println("微信用户:" + userId + "**出借接口调用失败，余额不足，错误码：" + respCode);
					message = "出借失败，可用余额不足！请联系客服.";
					status = ChinaPnrConstant.STATUS_FAIL;
				} else {
					boolean tenderFlag = investService.countBorrowTenderNum(userId, borrowNid, ordId) <= 0 ? true
							: false;
					if (tenderFlag) {
						System.out.println("微信用户:" + userId + "**出借接口调用失败,订单号：" + ordId + "**调用解冻接口**汇付接口返回错误码"
								+ respCode);
						status = ChinaPnrConstant.STATUS_FAIL;
						// 冻结失败 ，解冻
						try {
							boolean flag = investService.unFreezeOrder(borrowUserId, userId, ordId, freezeTrxId,
									ordDate);
							if (flag) {
								message = "出借失败！";
							} else {
								message = "出借解冻失败,请联系客服人员！";
							}
						} catch (Exception e1) {
							e1.printStackTrace();
							message = "出借解冻失败,请联系客服人员！";
						}
					} else {
						error = "0";
						message = "恭喜您出借成功!";
						status = ChinaPnrConstant.STATUS_SUCCESS;
						System.out.println("微信用户:" + userId + "**相应订单的出借记录存在,出借else成功,订单号：" + ordId + "**调用汇付接口返回错误码"
								+ respCode);
					}
				}
			}
		} else {
			error = "0";
			message = "恭喜您出借成功!";
			status = ChinaPnrConstant.STATUS_SUCCESS;
		}
		// 更新状态记录
		if (updateFlag && Validator.isNotNull(uuid)) {
			this.chinapnrService.updateChinapnrExclusiveLogStatus(Long.parseLong(uuid), status);
		}
		if (ChinaPnrConstant.STATUS_FAIL.equals(status)) {
			return returnUrl(error, message, callback, "", "", "", 0);
		} else {
			String result = "";
			BigDecimal earnings = new BigDecimal("0");
			DecimalFormat df = CustomConstants.DF_FOR_VIEW;
			// 如果出借金额不为空
			if (!StringUtils.isBlank(account) && Double.parseDouble(account) > 0) {
				String borrowStyle = borrow.getBorrowStyle();
				// 收益率
				BigDecimal borrowApr = borrow.getBorrowApr();
				// 周期
				Integer borrowPeriod = borrow.getBorrowPeriod();
				switch (borrowStyle) {
				case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“：历史回报=出借金额*年化收益÷12*月数；
					// 计算历史回报
					earnings = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(account),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
					break;
				case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“：历史回报=出借金额*年化收益÷360*天数；
					earnings = DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(account),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
					break;
				case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：历史回报=出借金额*年化收益÷12*月数；
					earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(new BigDecimal(account),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod, borrowPeriod).setScale(2,
							BigDecimal.ROUND_DOWN);
					break;
				case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：历史回报=出借金额*年化收益÷12*月数；
					earnings = AverageCapitalPlusInterestUtils.getInterestCount(new BigDecimal(account),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
					break;
				case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
					earnings = AverageCapitalUtils.getInterestCount(new BigDecimal(account),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
					break;
				default:
					break;
				}
				result = df.format(earnings);
			}
			return returnUrl(error, message, callback, result, account, "", borrow.getProjectType());
		}

	}

	/**
	 * 拼接返回结果
	 * 
	 * @param error
	 * @param data
	 * @param callback
	 * @param result
	 * @param account
	 * @param integer
	 * @return
	 */
	public String returnUrl(String error, String data, String callback, String result, String account,
			String couponFlag, Integer projectType) {
		// RespCode:000 成功；否则，失败
		Map<String, String> map = new HashMap<String, String>();
		map.put("error", error);
		map.put("message", data);
		if (error.equals("0")) {
			map.put("investMoney", account);
			map.put("interest", result);
			map.put("couponFlag", couponFlag);
			map.put("projectType", projectType + "");
		}
		data = JSON.toJSONString(map);
		try {
			data = URLEncoder.encode(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LogUtil.errorLog(WeChatInvestController.class.getName(), "tenderRetUrl", e);
			e.printStackTrace();
		}
		String url = callback + "backinfo/" + data;
		return "redirect:" + url;

	}

}
