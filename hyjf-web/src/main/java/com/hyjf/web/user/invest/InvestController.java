/**
 * Description:出借控制器
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 *           Created at: 2015年12月4日 下午2:32:36
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.web.user.invest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.web.BaseController;
import com.hyjf.web.WebBaseAjaxResultBean;
import com.hyjf.web.chinapnr.ChinapnrService;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.coupon.CouponService;
import com.hyjf.web.project.ProjectService;
import com.hyjf.web.util.WebUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

/**
 * @package com.hyjf.web.borrow.invest
 * @author 郭勇
 * @date 2015/12/04 14:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = InvestDefine.REQUEST_MAPPING)
public class InvestController extends BaseController {

	@Autowired
	private InvestService investService;

	@Autowired
	private ChinapnrService chinapnrService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private CouponService couponService;

	public static JedisPool pool = RedisUtils.getPool();

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
	@RequestMapping(value = InvestDefine.INVEST_INFO_ACTION, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public InvestInfoAjaxBean getInvestInfo(HttpServletRequest request, HttpServletResponse response) {

		String borrowNid = request.getParameter("nid");
		String accountStr = request.getParameter("money");
		String couponId = request.getParameter("couponGrantId");
		String appoint = request.getParameter("appoint");
		InvestInfoAjaxBean investInfo = new InvestInfoAjaxBean();
		DecimalFormat df = CustomConstants.DF_FOR_VIEW;
		df.setRoundingMode(RoundingMode.FLOOR);
		// 查询项目信息
		String money = String.valueOf(Integer.parseInt(accountStr));
		Borrow borrow = investService.getBorrowByNid(borrowNid);
		if (null != borrow) {
			/** 计算最优优惠券开始 pccvip */
			WebViewUser loginUser = WebUtils.getUser(request);
			UserCouponConfigCustomize couponConfig = null;
			couponConfig = new UserCouponConfigCustomize();
			if (couponId == null || "".equals(couponId) || couponId.length() == 0) {
				// 获取用户最优优惠券
				couponConfig = getBestCoupon(borrowNid, loginUser.getUserId(), accountStr, "0");
			} else {
				couponConfig = getBestCouponById(couponId);
			}
			if (couponConfig != null) {
				investInfo.setIsThereCoupon(1);

			} else {
				investInfo.setIsThereCoupon(0);
			}

			/** 可用优惠券张数开始 pccvip */
			String couponAvailableCount = couponService.getUserCouponAvailableCount(borrowNid, loginUser.getUserId(),
					accountStr, "0");
			investInfo.setCouponAvailableCount(new Integer(couponAvailableCount));
			/** 可用优惠券张数结束 pccvip */

			/** 获取用户是否是vip 开始 pccvip */
			UsersInfo usersInfo = projectService.getUsersInfoByUserId(loginUser.getUserId());
			if (usersInfo.getVipId() != null && usersInfo.getVipId() != 0) {
				investInfo.setIfVip(1);
			} else {
				investInfo.setIfVip(0);
			}
			/** 获取用户是否是vip 结束 pccvip */

			/** 获取用户优惠券总张数开始 pccvip */
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("usedFlag", 0);
			paraMap.put("userId", loginUser.getUserId());
			Integer recordTotal = couponService.countCouponUsers(paraMap);
			investInfo.setRecordTotal(recordTotal);
			/** 获取用户优惠券总张数结束 pccvip */

			// 如果出借金额不为空
			if ((!StringUtils.isBlank(money) && Long.parseLong(money) > 0)
					|| (couponConfig != null && (couponConfig.getCouponType() == 3 || couponConfig.getCouponType() == 1))) {
				String borrowStyle = borrow.getBorrowStyle();
				// 收益率
				BigDecimal borrowApr = borrow.getBorrowApr();
				if (borrow.getProjectType() == 13 && borrow.getBorrowExtraYield() != null
						&& borrow.getBorrowExtraYield().compareTo(BigDecimal.ZERO) > 0) {
					borrowApr = borrowApr.add(borrow.getBorrowExtraYield());
				}
				BigDecimal couponInterest = BigDecimal.ZERO;
				/** 叠加收益率开始 pccvip */
				if (couponConfig != null && appoint == null) {
					if (couponConfig.getCouponType() == 1) {
						couponInterest = getInterestDj(couponConfig.getCouponQuota(),
								couponConfig.getCouponProfitTime(), borrowApr);
					} else {
						couponInterest = getInterest(borrowStyle, couponConfig.getCouponType(), borrowApr,
								couponConfig.getCouponQuota(), money, borrow.getBorrowPeriod());
					}

					couponConfig.setCouponInterest(df.format(couponInterest));
					if (couponConfig.getCouponType() == 2) {
						borrowApr = borrowApr.add(couponConfig.getCouponQuota());
					}
					if (couponConfig.getCouponType() == 3) {
						money = new BigDecimal(money).add(couponConfig.getCouponQuota()).toString();
					}
				}
				/** 叠加收益率结束 */

				// 周期
				Integer borrowPeriod = borrow.getBorrowPeriod();
				BigDecimal earnings = new BigDecimal("0");
				switch (borrowStyle) {
				case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“：历史回报=出借金额*年化收益÷12*月数；
					// 计算历史回报
					earnings = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(money),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod).divide(new BigDecimal("1"), 2,
							BigDecimal.ROUND_DOWN);
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
				investInfo.setEarnings(df.format(earnings));
				if (couponConfig != null && couponConfig.getCouponType() == 3) {
					investInfo.setCapitalInterest(df.format(earnings.add(couponConfig.getCouponQuota()).subtract(
							couponInterest)));
				} else if (couponConfig != null && couponConfig.getCouponType() == 1) {
					investInfo.setEarnings(df.format(earnings.add(couponInterest)));
					investInfo.setCapitalInterest(df.format(earnings));
				} else {
					investInfo.setCapitalInterest(df.format(earnings.subtract(couponInterest)));
				}
				investInfo.setCouponConfig(couponConfig);
				investInfo.setStatus(true);
				investInfo.setMessage("历史回报计算完成");
			} else {
				investInfo.setStatus(false);
				investInfo.setMessage("请填写正确的出借金额");
			}
		} else {
			investInfo.setStatus(false);
			investInfo.setMessage("请填写正确的金额");
		}
		return investInfo;
	}

	private BigDecimal getInterest(String borrowStyle, Integer couponType, BigDecimal borrowApr,
			BigDecimal couponQuota, String money, Integer borrowPeriod) {
		BigDecimal earnings = new BigDecimal("0");
		// 出借金额
		BigDecimal accountDecimal = null;
		if (couponType == 1) {
			// 体验金 出借资金=体验金面值
			accountDecimal = couponQuota;
		} else if (couponType == 2) {
			// 加息券 出借资金=真实出借资金
			accountDecimal = new BigDecimal(money);
			borrowApr = couponQuota;
		} else if (couponType == 3) {
			// 代金券 出借资金=体验金面值
			accountDecimal = couponQuota;
		}
		switch (borrowStyle) {
		case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“：历史回报=出借金额*年化收益÷12*月数；
			// 计算历史回报
			earnings = DuePrincipalAndInterestUtils.getMonthInterest(accountDecimal,
					borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
			break;
		case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“：历史回报=出借金额*年化收益÷360*天数；
			// 计算历史回报
			earnings = DuePrincipalAndInterestUtils.getDayInterest(accountDecimal,
					borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
			break;
		case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：历史回报=出借金额*年化收益÷12*月数；
			// 计算历史回报
			earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(accountDecimal,
					borrowApr.divide(new BigDecimal("100")), borrowPeriod, borrowPeriod).setScale(2,
					BigDecimal.ROUND_DOWN);
			break;
		case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：历史回报=出借金额*年化收益÷12*月数；
			// 计算历史回报
			earnings = AverageCapitalPlusInterestUtils.getInterestCount(accountDecimal,
					borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
			break;
		default:
			break;
		}
		if (couponType == 3) {
			earnings = earnings.add(couponQuota);
		}
		return earnings;
	}

	private BigDecimal getInterestDj(BigDecimal couponQuota, Integer couponProfitTime, BigDecimal borrowApr) {
		BigDecimal earnings = new BigDecimal("0");

		earnings = couponQuota.multiply(borrowApr.divide(new BigDecimal(100), 6, BigDecimal.ROUND_HALF_UP))
				.divide(new BigDecimal(365), 6, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(couponProfitTime))
				.setScale(2, BigDecimal.ROUND_DOWN);

		return earnings;

	}

	/**
	 * pc预约验证
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = InvestDefine.APPOINT_CHECK_ACTION, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public WebBaseAjaxResultBean appointCheck(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(InvestController.class.toString(), InvestDefine.INVEST_ACTION);
		// 借款borrowNid,如HBD120700101
		String borrowNid = request.getParameter("nid");
		// 出借金额
		String money = request.getParameter("money");
		String appointCheck = request.getParameter("termcheck");
		Integer userId = WebUtils.getUserId(request);
		System.out.println("tenderCheck ShiroUtil.getLoginUserId--------------------" + userId);
		JSONObject info = investService.checkParamAppointment(borrowNid, money, userId, "0", appointCheck);

		// 参数验证成功，则返回url，否则不返回
		WebBaseAjaxResultBean result = new WebBaseAjaxResultBean();
		if (info == null) {
			result.setStatus(false);
			result.setMessage("出借失败");
		} else if (info.get("error").equals("0")) {
			result.setStatus(true);
			result.setMessage("操作成功");
		} else {
			result.setStatus(false);
			result.setMessage(info.getString("data"));
		}
		LogUtil.endLog(InvestController.class.toString(), InvestDefine.INVEST_ACTION);
		return result;
	}

	/**
	 * pc出借验证
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = InvestDefine.INVEST_CHECK_ACTION, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public WebBaseAjaxResultBean tenderCheck(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(InvestController.class.toString(), InvestDefine.INVEST_ACTION);
		String borrowNid = request.getParameter("nid");
		String couponGrantId = request.getParameter("couponGrantId");
		// 出借金额
		String money = request.getParameter("money");
		if (money == null || "".equals(money)) {
			money = "0";
		}
		Integer userId = WebUtils.getUserId(request);
		System.out.println("tenderCheck ShiroUtil.getLoginUserId--------------------" + userId);
		JSONObject info = investService.checkParam(borrowNid, money, userId, "0", couponGrantId);
		// 参数验证成功，则返回url，否则不返回
		WebBaseAjaxResultBean result = new WebBaseAjaxResultBean();
		if (info == null) {
			result.setStatus(false);
			result.setMessage("出借失败");
		} else if (info.get("error").equals("0")) {
			if (StringUtils.isNotEmpty(couponGrantId)) {
				/*
				 * CouponConfigCustomizeV2 cuc =
				 * investService.getCouponUser(couponGrantId, userId);
				 */
				// 借款项目
				Borrow borrow = investService.getBorrowByNid(borrowNid);
				Map<String, String> validateMap = this.validateCoupon(userId, money, couponGrantId,
						borrow.getBorrowNid(), CustomConstants.CLIENT_PC);
				LogUtil.infoLog(InvestServiceImpl.class.toString(), "updateCouponTender",
						"优惠券出借校验结果：" + validateMap.get("statusDesc"));
				System.out.println("updateCouponTender" + "优惠券出借校验结果：" + validateMap.get("statusDesc"));
				if (MapUtils.isEmpty(validateMap)) {
					result.setStatus(true);
					result.setMessage("操作成功");
				} else {
					result.setStatus(false);
					result.setMessage(validateMap.get("statusDesc"));
				}
			} else {
				result.setStatus(true);
				result.setMessage("操作成功");
			}
		} else {
			result.setStatus(false);
			result.setMessage(info.getString("data"));
		}
		LogUtil.endLog(InvestController.class.toString(), InvestDefine.INVEST_ACTION);
		return result;
	}

	/**
	 * 优惠券出借校验
	 * 
	 * @param userId
	 * 
	 * @param account
	 * @param couponType
	 * @param borrowNid
	 * @return
	 */
	private Map<String, String> validateCoupon(Integer userId, String account, String couponGrantId, String borrowNid,
			String platform) {

		JSONObject jsonObject = CommonSoaUtils.CheckCoupon(userId + "", borrowNid, account, platform, couponGrantId);
		int status = jsonObject.getIntValue("status");
		String statusDesc = jsonObject.getString("statusDesc");
		Map<String, String> paramMap = new HashMap<String, String>();
		if (status == 1) {
			paramMap.put(CustomConstants.APP_STATUS_DESC, statusDesc);
		}

		return paramMap;
	}

	/**
	 * 出借
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(InvestDefine.INVEST_ACTION)
	public ModelAndView tender(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(InvestController.class.toString(), InvestDefine.INVEST_URL_ACTION);
		// add by zhangjp 优惠券出借 start
		// 优惠券用户id （coupon_user的id）
		String couponGrantId = request.getParameter("couponGrantId");
		Integer userId = WebUtils.getUserId(request);
		// 排他校验时间
		int couponOldTime = Integer.MIN_VALUE;
		CouponConfigCustomizeV2 cuc = null;
		// -1:有可用的优惠券，但是出借时不选择优惠券 空或null：用户没有可用优惠券
		if (StringUtils.isNotEmpty(couponGrantId) && !StringUtils.equals("-1", couponGrantId)) {
			cuc = investService.getCouponUser(couponGrantId, userId);
			// 排他check用
			couponOldTime = cuc.getUserUpdateTime();
		}
		// add by zhangjp 优惠券出借 end

		ModelAndView modelAndView = new ModelAndView();
		// 借款borrowNid,如HBD120700101
		String borrowNid = request.getParameter("nid");
		// 出借金额
		String accountStr = request.getParameter("money");
		if (accountStr == null || "".equals(accountStr)) {
			accountStr = "0";
		}
		System.out.println("tender ShiroUtil.getLoginUserId--------------------" + userId);// 如果没有本金出借且有优惠券出借
		BigDecimal decimalAccount = StringUtils.isNotEmpty(accountStr) ? new BigDecimal(accountStr) : BigDecimal.ZERO;
		if (decimalAccount.compareTo(BigDecimal.ZERO) != 1 && cuc != null
				&& (cuc.getCouponType() == 3 || cuc.getCouponType() == 1)) {
			System.out.println("cuc.getCouponType():" + cuc.getCouponType());
			this.couponTender(modelAndView, request, cuc, userId, couponOldTime);
			return modelAndView;
		}
		JSONObject result = investService.checkParam(borrowNid, accountStr, userId, "0", couponGrantId);
		if (result == null) {
			modelAndView = new ModelAndView(InvestDefine.INVEST_ERROR_PATH);
			modelAndView.addObject("investDesc", "出借失败！");
			return modelAndView;
		} else if (result.get("error") != null && result.get("error").equals("1")) {
			System.out.println(result.toJSONString());
			modelAndView = new ModelAndView(InvestDefine.INVEST_ERROR_PATH);
			modelAndView.addObject("investDesc", result.get("data") + "");
			return modelAndView;
		}
		String account = String.valueOf(Integer.parseInt(accountStr));
		String tenderUsrcustid = result.getString("tenderUsrcustid");
		String borrowerUsrcustid = result.getString("borrowerUsrcustid");
		// 生成订单
		String orderId = GetOrderIdUtils.getOrderId2(Integer.valueOf(userId));
		// 写日志
		Boolean flag = investService.updateBeforeChinaPnR(borrowNid, orderId, userId, account,
				GetCilentIP.getIpAddr(request));
		// 成功后调用出借接口
		if (flag) {
			// 回调路径
			String returl = PropUtils.getSystem("hyjf.web.host").trim() + InvestDefine.REQUEST_MAPPING
					+ InvestDefine.RETURL_SYN_ACTION + ".do?couponGrantId=" + couponGrantId + "&couponOldTime="
					+ couponOldTime;
			// 商户后台应答地址(必须)
			String bgRetUrl = PropUtils.getSystem("http.hyjf.web.host").trim() + InvestDefine.REQUEST_MAPPING
					+ InvestDefine.RETURL_ASY_ACTION + ".do?couponGrantId=" + couponGrantId + "&couponOldTime="
					+ couponOldTime;
			System.out.println(returl);
			System.out.println(bgRetUrl);
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
			// 借款人客户号
			chinapnrBean.setBorrowerCustId(borrowerUsrcustid);
			// 借款金额
			chinapnrBean.setBorrowerAmt(CustomUtil.formatAmount(account));
			// 最高还款金额
			chinapnrBean.setBorrowerRate("0.30");
			// 商户私有域
			MerPriv merPriv = new MerPriv();
			merPriv.setBorrowNid(borrowNid);
			chinapnrBean.setMerPrivPo(merPriv);
			// 借款详情
			Map<String, String> map = new HashMap<String, String>();
			map.put("BorrowerCustId", borrowerUsrcustid);
			map.put("BorrowerAmt", CustomUtil.formatAmount(account));
			map.put("BorrowerRate", "0.30");// 云龙提示
			if (result.getString("bankInputFlag") != null && result.getString("bankInputFlag").equals("1")) {
				map.put("ProId", borrowNid);// 2.0新增字段
				chinapnrBean.setProId(borrowNid);// 2.0新增字段
			}
			JSONArray array = new JSONArray();
			array.add(map);
			String BorrowerDetails = JSON.toJSONString(array);
			chinapnrBean.setBorrowerDetails(BorrowerDetails);
			// 同步地址
			chinapnrBean.setRetUrl(returl);
			// 异步地址
			chinapnrBean.setBgRetUrl(bgRetUrl);
			// 2.0冻结类型
			chinapnrBean.setIsFreeze("Y");
			// 2.0冻结订单号
			chinapnrBean.setFreezeOrdId(orderId);
			// 日志相关参数
			// 日志类型
			chinapnrBean.setType("user_tender"); // 日志类型
			// 平台
			chinapnrBean.setLogClient("0");
			// 出借用户
			chinapnrBean.setLogUserId(Integer.valueOf(userId));
			try {
				modelAndView = ChinapnrUtil.callApi(chinapnrBean);
			} catch (Exception e) {
				e.printStackTrace();
				modelAndView = new ModelAndView(InvestDefine.INVEST_ERROR_PATH);
				modelAndView.addObject("investDesc", "出借失败！");
			}
		} else {
			modelAndView = new ModelAndView(InvestDefine.INVEST_ERROR_PATH);
			modelAndView.addObject("investDesc", "出借失败！");
		}
		LogUtil.endLog(InvestController.class.toString(), InvestDefine.INVEST_URL_ACTION);
		return modelAndView;
	}

	// 出借同步回调
	@RequestMapping(InvestDefine.RETURL_SYN_ACTION)
	public ModelAndView tenderRetUrl(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute ChinapnrBean bean) {

		ModelAndView modelAndView = new ModelAndView();
		System.out.println("开始调用出借同步方法");
		// 错误信息
		String data = "您来晚了，下次再来抢吧";
		bean.convert();
		// 获取项目编号
		MerPriv merPriv = bean.getMerPrivPo();
		String borrowNid = merPriv.getBorrowNid();
		// 记录uuid
		String uuid = request.getParameter("uuid");
		System.out.println("uuid:" + request.getParameter("uuid") + "同步回调" + bean.getAllParams().toString());
		System.out.println("标号:" + borrowNid);
		if (StringUtils.isBlank(borrowNid)) {
			modelAndView = new ModelAndView(InvestDefine.INVEST_ERROR_PATH);
			data = "回调时,borrowNid为空";
			modelAndView.addObject("investDesc", data);
			return modelAndView;
		}
		// 借款项目
		Borrow borrow = investService.getBorrowByNid(borrowNid);
		// 借款人用户id
		Integer borrowUserId = borrow.getUserId();
		// 获取出借用户的出借客户号
		String tenderUsrcustid = bean.getUsrCustId();
		// 出借人id
		Integer userId = investService.getUserIdByUsrcustId(tenderUsrcustid); // 冻结标示
		String freezeTrxId = bean.getFreezeTrxId();
		// 借款金额
		String account = bean.getTransAmt();
		// 订单id
		String ordId = bean.getOrdId();
		// 订单日期
		String ordDate = bean.getOrdDate();
		// 出借结果返回码
		String respCode = bean.getRespCode();
		// add by zhangjp 优惠券出借 start
		// 优惠券出借状态
		boolean couponSuccess = false;
		// 优惠券用户id （coupon_user的id）
		String couponGrantId = request.getParameter("couponGrantId");
		System.out.println(couponGrantId);
		String tempTime = request.getParameter("couponOldTime");
		int couponOldTime = StringUtils.isNotEmpty(tempTime) ? Integer.valueOf(tempTime) : 0;
		BigDecimal couponAccount = null;
		BigDecimal couponQuota = null;
		BigDecimal couponRate = null;
		int couponType = Integer.MIN_VALUE;
		// add by zhangjp 优惠券出借 end

		// 操作记录更新标识
		boolean updateFlag = investService.countBorrowTenderNum(userId, borrowNid, ordId) <= 0 ? true : false;
		// 出借成功
		System.out.println("PC用户:" + userId + "***出借接口结果码：" + respCode);
		// 未调用过异步请求
		// 发送状态
		String status = ChinaPnrConstant.STATUS_FAIL;
		if (updateFlag) {
			if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))
					|| ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)) {
				// 冻结成功，写各个表
				String ip = GetCilentIP.getIpAddr(request);
				// 传递参数
				bean.setLogUserId(userId);
				bean.setLogClient("0");
				bean.setLogIp(ip);
				JSONObject info = investService.userTender(borrow, bean);
				if (info.getString("status").equals(ChinaPnrConstant.STATUS_VERTIFY_OK)) {
					System.out.println("PC用户:" + userId + "***出借成功：" + account);
					data = "恭喜您出借成功!";
					status = ChinaPnrConstant.STATUS_SUCCESS;
					if (info.getString("isExcute") != null && info.getString("isExcute").equals("1")) {
						// add by zhangjp 优惠券出借 start
						Map<String, Object> retMap = new HashMap<String, Object>();
						if (StringUtils.isNotEmpty(couponGrantId)) {
							// 优惠券出借校验
							try {
								/*
								 * CouponConfigCustomizeV2 cuc =
								 * investService.getCouponUser(couponGrantId,
								 * userId);
								 */
								Map<String, String> validateMap = this.validateCoupon(userId, account, couponGrantId,
										borrow.getBorrowNid(), CustomConstants.CLIENT_PC);
								LogUtil.infoLog(InvestServiceImpl.class.toString(), "updateCouponTender", "优惠券出借校验结果："
										+ validateMap.get("statusDesc"));
								System.out.println("updateCouponTender" + "优惠券出借校验结果：" + validateMap.get("statusDesc"));
								if (MapUtils.isEmpty(validateMap)) {
									// 校验通过 进行出借
									couponSuccess = investService.updateCouponTender(couponGrantId, borrowNid, ordDate,
											userId, account, GetCilentIP.getIpAddr(request), couponOldTime, ordId,
											retMap);
								}
							} catch (Exception e) {
								LogUtil.infoLog(InvestController.class.getName(), "tenderRetUrl", "优惠券出借失败");
							}
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
					System.out.println("PC用户:" + userId + "**出借成功后，后续处理失败" + ",订单号：" + ordId + "解冻：" + account);
					data = info.getString("message");
					boolean tenderFlag = investService.updateBorrowTenderTmp(userId, borrowNid, ordId) > 0 ? true
							: false;
					if (tenderFlag) {
						status = ChinaPnrConstant.STATUS_FAIL;
						// 冻结失败 ，解冻
						try {
							boolean flag = investService.unFreezeOrder(borrowUserId, userId, ordId, freezeTrxId,
									ordDate);
							if (!flag) {
								data = "出借解冻失败,请联系客服人员!";
							}
						} catch (Exception e1) {
							e1.printStackTrace();
							data = "出借解冻失败,请联系客服人员!";
						}
					} else {
						data = "恭喜您出借成功!";
						status = ChinaPnrConstant.STATUS_SUCCESS;
						System.out.println("PC用户:" + userId + "**相应订单的出借记录存在,出借成功,订单号：" + ordId + "**调用汇付接口返回错误码"
								+ respCode);
					}
				}
			} else {
				// 364返回码提示余额不足，不结冻
				if (ChinaPnrConstant.RESPCODE_YUE2_FAIL.equals(respCode)) {
					System.out.println("PC用户:" + userId + "**出借接口调用失败，余额不足，错误码：" + respCode);
					data = "出借失败，可用余额不足！请联系客服.";
					status = ChinaPnrConstant.STATUS_FAIL;
				} else {
					boolean tenderFlag = investService.countBorrowTenderNum(userId, borrowNid, ordId) <= 0 ? true
							: false;
					if (tenderFlag) {
						System.out.println("PC用户:" + userId + "**出借接口调用失败,订单号：" + ordId + "**调用解冻接口**汇付接口返回错误码"
								+ respCode);
						status = ChinaPnrConstant.STATUS_FAIL;
						// 冻结失败 ，解冻
						try {
							boolean flag = investService.unFreezeOrder(borrowUserId, userId, ordId, freezeTrxId,
									ordDate);
							if (flag) {
								data = "出借失败!";
							} else {
								data = "出借解冻失败,请联系客服人员!";
							}
						} catch (Exception e1) {
							e1.printStackTrace();
							data = "出借解冻失败,请联系客服人员!";
						}
					} else {
						data = "恭喜您出借成功!";
						status = ChinaPnrConstant.STATUS_SUCCESS;
						System.out.println("PC用户:" + userId + "**相应订单的出借记录存在,出借成功,订单号：" + ordId + "**调用汇付接口返回错误码"
								+ respCode);
					}
				}
			}
		} else {
			data = "恭喜您出借成功!";
			status = ChinaPnrConstant.STATUS_SUCCESS;
		}
		// 更新状态记录
		if (updateFlag && Validator.isNotNull(uuid)) {
			this.chinapnrService.updateChinapnrExclusiveLogStatus(Long.parseLong(uuid), status);
		}
		if (status.equals(ChinaPnrConstant.STATUS_FAIL)) {
			modelAndView = new ModelAndView(InvestDefine.INVEST_ERROR_PATH);
			modelAndView.addObject("investDesc", data);
			modelAndView.addObject("projectType", borrow.getProjectType());
		} else {
			modelAndView = new ModelAndView(InvestDefine.INVEST_SUCCESS_PATH);
			LogUtil.endLog(InvestDefine.class.toString(), InvestDefine.RETURL_SYN_ACTION, "[交易完成后,回调结束]");
			DecimalFormat df = CustomConstants.DF_FOR_VIEW;
			// 取得项目类别
			BorrowProjectType borrowProjectType = investService
					.getBorrowProjectType(borrow.getProjectType().toString());
			// String projectTypeName = borrowProjectType.getBorrowName();
			/** 修改出借成功页面显示修改开始 */
			String projectTypeName = getProjectTypeName(borrowProjectType);
			if (StringUtils.isNotBlank(couponGrantId)) {
				couponSuccess = this.investService.updateCouponTenderStatus(ordId, couponGrantId, userId);
			}
			/** 修改出借成功页面显示修改结束 */
			Map<String, Object> retMap = this.investService.queryCouponData(couponGrantId, borrow, userId, account,
					couponOldTime);
			if (couponSuccess && Validator.isNotNull(retMap)) {
				// 优惠券出借额度
				couponAccount = (BigDecimal) retMap.get("couponAccount");
				// 优惠券类别
				couponType = (int) retMap.get("couponType");
				// 优惠券面值
				couponQuota = new BigDecimal(retMap.get("couponQuota").toString());
				// 如果是加息券则按照加息券的利率计算收益，反之如果是体验金则按照标的的年收益率计算收益
				couponRate = new BigDecimal(retMap.get("couponRate").toString());
			}
			String interest = null;
			String couponInterest = StringUtils.EMPTY;
			if (StringUtils.isBlank(interest)) {
				// 根据项目编号获取相应的项目
				String borrowStyle = borrow.getBorrowStyle();
				BigDecimal borrowApr = borrow.getBorrowApr();
				// 融通宝收益叠加
				if (borrow.getProjectType() == 13 && borrow.getBorrowExtraYield() != null
						&& borrow.getBorrowExtraYield().compareTo(BigDecimal.ZERO) > 0) {
					borrowApr = borrowApr.add(borrow.getBorrowExtraYield());
				}
				// 如果是加息券则按照加息券的利率计算收益，反之如果是体验金则按照标的的年收益率计算收益
				couponRate = couponType == 2 ? couponQuota : borrowApr;
				// 周期
				Integer borrowPeriod = borrow.getBorrowPeriod();
				BigDecimal earnings = new BigDecimal("0");
				df.setRoundingMode(RoundingMode.FLOOR);
				CouponConfigCustomizeV2 cuc = investService.getCouponUser(couponGrantId, userId);
				switch (borrowStyle) {
				case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“：历史回报=出借金额*年化收益÷12*月数；
					// 计算历史回报
					earnings = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(account),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
					interest = df.format(earnings);
					if (couponSuccess && cuc != null && Validator.isNotNull(couponRate)
							&& Validator.isNotNull(couponAccount)) {
						if (couponType == 1) {
							earnings = getInterestDj(cuc.getCouponQuota(), cuc.getCouponProfitTime(), borrowApr);
						} else {
							earnings = DuePrincipalAndInterestUtils.getMonthInterest(couponAccount,
									couponRate.divide(new BigDecimal("100")), borrowPeriod).setScale(2,
									BigDecimal.ROUND_DOWN);
						}
						couponInterest = df.format(earnings);
					}
					break;
				case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“：历史回报=出借金额*年化收益÷360*天数；
					earnings = DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(account),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
					interest = df.format(earnings);

					if (couponSuccess && cuc != null && Validator.isNotNull(couponRate)
							&& Validator.isNotNull(couponAccount)) {
						if (couponType == 1) {
							earnings = getInterestDj(cuc.getCouponQuota(), cuc.getCouponProfitTime(), borrowApr);
						} else {
							earnings = DuePrincipalAndInterestUtils.getDayInterest(couponAccount,
									couponRate.divide(new BigDecimal("100")), borrowPeriod).setScale(2,
									BigDecimal.ROUND_DOWN);
						}
						couponInterest = df.format(earnings);
					}
					break;
				case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：历史回报=出借金额*年化收益÷12*月数；
					earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(new BigDecimal(account),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod, borrowPeriod).setScale(2,
							BigDecimal.ROUND_DOWN);
					interest = df.format(earnings);
					if (couponSuccess && cuc != null && Validator.isNotNull(couponRate)
							&& Validator.isNotNull(couponAccount)) {
						if (couponType == 1) {
							earnings = getInterestDj(cuc.getCouponQuota(), cuc.getCouponProfitTime(), borrowApr);
						} else {
							earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(couponAccount,
									couponRate.divide(new BigDecimal("100")), borrowPeriod, borrowPeriod).setScale(2,
									BigDecimal.ROUND_DOWN);
						}
						couponInterest = df.format(earnings);
					}
					break;
				case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：历史回报=出借金额*年化收益÷12*月数；
					earnings = AverageCapitalPlusInterestUtils.getInterestCount(new BigDecimal(account),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
					interest = df.format(earnings);
					if (couponSuccess && cuc != null && Validator.isNotNull(couponRate)
							&& Validator.isNotNull(couponAccount)) {
						if (couponType == 1) {
							earnings = getInterestDj(cuc.getCouponQuota(), cuc.getCouponProfitTime(), borrowApr);
						} else {
							earnings = AverageCapitalPlusInterestUtils.getInterestCount(couponAccount,
									couponRate.divide(new BigDecimal("100")), borrowPeriod).setScale(2,
									BigDecimal.ROUND_DOWN);
						}
						couponInterest = df.format(earnings);
					}
					break;
				case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
					earnings = AverageCapitalUtils.getInterestCount(new BigDecimal(account),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
					interest = df.format(earnings);
					if (couponSuccess && cuc != null && Validator.isNotNull(couponRate)
							&& Validator.isNotNull(couponAccount)) {
						if (couponType == 1) {
							earnings = getInterestDj(cuc.getCouponQuota(), cuc.getCouponProfitTime(), borrowApr);
						} else {
							earnings = AverageCapitalUtils.getInterestCount(couponAccount,
									couponRate.divide(new BigDecimal("100")), borrowPeriod).setScale(2,
									BigDecimal.ROUND_DOWN);
						}
						couponInterest = df.format(earnings);
					}
					break;
				default:
					break;
				}
			}
			if (StringUtils.isNotBlank(interest)) {
				modelAndView.addObject("interest", interest);
			}

			// add by zhangjp 优惠券相关 start
			if (StringUtils.isNotEmpty(couponInterest)) {
				// 历史回报
				modelAndView.addObject("couponInterest", couponInterest);
			}
			if (couponQuota != null) {
				// 优惠券面值
				modelAndView.addObject("couponQuota", couponQuota);
			}
			if (couponType != Integer.MIN_VALUE) {
				// 优惠券类别
				modelAndView.addObject("couponType", couponType);
			}
			modelAndView.addObject("borrowNid", borrow.getBorrowNid());
			modelAndView.addObject("projectTypeName", projectTypeName);
			modelAndView.addObject("account", df.format(new BigDecimal(account)));
			modelAndView.addObject("investDesc", data);
			modelAndView.addObject("projectType", borrow.getProjectType());
			if (StringUtils.isNotEmpty(borrow.getBorrowAssetNumber())) {
				modelAndView.addObject("borrowAssetNumber", borrow.getBorrowAssetNumber());
			} else {
				modelAndView.addObject("borrowAssetNumber", "");
			}
		}
		return modelAndView;
	}

	// 出借异步回调
	@RequestMapping(InvestDefine.RETURL_ASY_ACTION)
	public ModelAndView tenderBigRetUrl(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute ChinapnrBean bean) {

		ModelAndView modelAndView = new ModelAndView();
		System.out.println("开始调用出借异步方法");
		// 错误信息
		String data = "您来晚了，下次再来抢吧";
		bean.convert();
		// 获取项目编号
		MerPriv merPriv = bean.getMerPrivPo();
		String borrowNid = merPriv.getBorrowNid();
		// 记录uuid
		String uuid = request.getParameter("uuid");
		System.out.println("uuid:" + request.getParameter("uuid") + "异步回调" + bean.getAllParams().toString());
		System.out.println("标号:" + borrowNid);
		if (StringUtils.isBlank(borrowNid)) {
			modelAndView = new ModelAndView(InvestDefine.INVEST_ERROR_PATH);
			data = "回调时,borrowNid为空";
			modelAndView.addObject("investDesc", data);
			return modelAndView;
		}
		// 借款项目
		Borrow borrow = investService.getBorrowByNid(borrowNid);
		// 借款人用户id
		Integer borrowUserId = borrow.getUserId();
		// 获取出借用户的出借客户号
		String tenderUsrcustid = bean.getUsrCustId();
		// 出借人id
		Integer userId = investService.getUserIdByUsrcustId(tenderUsrcustid);
		// 冻结标示
		String freezeTrxId = bean.getFreezeTrxId();
		// 借款金额
		String account = bean.getTransAmt();
		// 订单id
		String ordId = bean.getOrdId();
		// 订单日期
		String ordDate = bean.getOrdDate();
		// 出借结果返回码
		String respCode = bean.getRespCode();
		// add by zhangjp 优惠券出借 start
		// 优惠券出借状态
		boolean couponSuccess = false;
		// 优惠券用户id （coupon_user的id）
		String couponGrantId = request.getParameter("couponGrantId");
		System.out.println(couponGrantId);
		String tempTime = request.getParameter("couponOldTime");
		int couponOldTime = StringUtils.isNotEmpty(tempTime) ? Integer.valueOf(tempTime) : 0;
		BigDecimal couponAccount = null;
		BigDecimal couponQuota = null;
		BigDecimal couponRate = null;
		int couponType = Integer.MIN_VALUE;
		// add by zhangjp 优惠券出借 end

		// 操作记录更新标识
		boolean updateFlag = investService.countBorrowTenderNum(userId, borrowNid, ordId) <= 0 ? true : false;
		// 出借成功
		System.out.println("PC用户:" + userId + "***出借接口结果码：" + respCode);
		// 未调用过异步请求
		// 发送状态
		String status = ChinaPnrConstant.STATUS_FAIL;
		if (updateFlag) {
			if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))
					|| ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)) {
				// 冻结成功，写各个表
				String ip = GetCilentIP.getIpAddr(request);
				// 传递参数
				bean.setLogUserId(userId);
				bean.setLogClient("0");
				bean.setLogIp(ip);
				JSONObject info = investService.userTender(borrow, bean);
				if (info.getString("status").equals(ChinaPnrConstant.STATUS_VERTIFY_OK)) {
					System.out.println("PC用户:" + userId + "***出借成功：" + account);
					data = "恭喜您出借成功!";
					status = ChinaPnrConstant.STATUS_SUCCESS;
					if (info.getString("isExcute") != null && info.getString("isExcute").equals("1")) {
						// add by zhangjp 优惠券出借 start
						Map<String, Object> retMap = new HashMap<String, Object>();
						if (StringUtils.isNotEmpty(couponGrantId)) {
							// 优惠券出借校验
							try {
								/*
								 * CouponConfigCustomizeV2 cuc =
								 * investService.getCouponUser(couponGrantId,
								 * userId);
								 */
								Map<String, String> validateMap = this.validateCoupon(userId, account, couponGrantId,
										borrow.getBorrowNid(), CustomConstants.CLIENT_PC);
								LogUtil.infoLog(InvestServiceImpl.class.toString(), "updateCouponTender", "优惠券出借校验结果："
										+ validateMap.get("statusDesc"));
								System.out.println("updateCouponTender" + "优惠券出借校验结果：" + validateMap.get("statusDesc"));
								if (MapUtils.isEmpty(validateMap)) {
									// 校验通过 进行出借
									couponSuccess = investService.updateCouponTender(couponGrantId, borrowNid, ordDate,
											userId, account, GetCilentIP.getIpAddr(request), couponOldTime, ordId,
											retMap);
								}
							} catch (Exception e) {
								LogUtil.infoLog(InvestController.class.getName(), "tenderRetUrl", "优惠券出借失败");
							}
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
					System.out.println("PC用户:" + userId + "**出借成功后，后续处理失败" + ",订单号：" + ordId + "解冻：" + account);
					data = info.getString("message");
					boolean tenderFlag = investService.updateBorrowTenderTmp(userId, borrowNid, ordId) > 0 ? true
							: false;
					if (tenderFlag) {
						status = ChinaPnrConstant.STATUS_FAIL;
						// 冻结失败 ，解冻
						try {
							boolean flag = investService.unFreezeOrder(borrowUserId, userId, ordId, freezeTrxId,
									ordDate);
							if (!flag) {
								data = "出借解冻失败,请联系客服人员!";
							}
						} catch (Exception e1) {
							e1.printStackTrace();
							data = "出借解冻失败,请联系客服人员!";
						}
					} else {
						data = "恭喜您出借成功!";
						status = ChinaPnrConstant.STATUS_SUCCESS;
						System.out.println("PC用户:" + userId + "**相应订单的出借记录存在,出借成功,订单号：" + ordId + "**调用汇付接口返回错误码"
								+ respCode);
					}
				}
			} else {
				// 364返回码提示余额不足，不结冻
				if (ChinaPnrConstant.RESPCODE_YUE2_FAIL.equals(respCode)) {
					System.out.println("PC用户:" + userId + "**出借接口调用失败，余额不足，错误码：" + respCode);
					data = "出借失败，可用余额不足！请联系客服.";
					status = ChinaPnrConstant.STATUS_FAIL;
				} else {
					boolean tenderFlag = investService.countBorrowTenderNum(userId, borrowNid, ordId) <= 0 ? true
							: false;
					if (tenderFlag) {
						System.out.println("PC用户:" + userId + "**出借接口调用失败,订单号：" + ordId + "**调用解冻接口**汇付接口返回错误码"
								+ respCode);
						status = ChinaPnrConstant.STATUS_FAIL;
						// 冻结失败 ，解冻
						try {
							boolean flag = investService.unFreezeOrder(borrowUserId, userId, ordId, freezeTrxId,
									ordDate);
							if (flag) {
								data = "出借失败!";
							} else {
								data = "出借解冻失败,请联系客服人员!";
							}
						} catch (Exception e1) {
							e1.printStackTrace();
							data = "出借解冻失败,请联系客服人员!";
						}
					} else {
						data = "恭喜您出借成功!";
						status = ChinaPnrConstant.STATUS_SUCCESS;
						System.out.println("PC用户:" + userId + "**相应订单的出借记录存在,出借成功,订单号：" + ordId + "**调用汇付接口返回错误码"
								+ respCode);
					}
				}
			}
		} else {
			data = "恭喜您出借成功!";
			status = ChinaPnrConstant.STATUS_SUCCESS;
		}
		// 更新状态记录
		if (updateFlag && Validator.isNotNull(uuid)) {
			this.chinapnrService.updateChinapnrExclusiveLogStatus(Long.parseLong(uuid), status);
		}
		if (status.equals(ChinaPnrConstant.STATUS_FAIL)) {
			modelAndView = new ModelAndView(InvestDefine.INVEST_ERROR_PATH);
			modelAndView.addObject("investDesc", data);
			modelAndView.addObject("projectType", borrow.getProjectType());
		} else {
			modelAndView = new ModelAndView(InvestDefine.INVEST_SUCCESS_PATH);
			LogUtil.endLog(InvestDefine.class.toString(), InvestDefine.RETURL_SYN_ACTION, "[交易完成后,回调结束]");
			DecimalFormat df = CustomConstants.DF_FOR_VIEW;
			// 取得项目类别
			BorrowProjectType borrowProjectType = investService
					.getBorrowProjectType(borrow.getProjectType().toString());
			// String projectTypeName = borrowProjectType.getBorrowName();
			/** 修改出借成功页面显示修改开始 */
			String projectTypeName = getProjectTypeName(borrowProjectType);
			/** 修改出借成功页面显示修改结束 */
			if (StringUtils.isNotBlank(couponGrantId)) {
				couponSuccess = this.investService.updateCouponTenderStatus(ordId, couponGrantId, userId);
			}
			Map<String, Object> retMap = this.investService.queryCouponData(couponGrantId, borrow, userId, account,
					couponOldTime);
			if (couponSuccess && Validator.isNotNull(retMap)) {
				// 优惠券出借额度
				couponAccount = (BigDecimal) retMap.get("couponAccount");
				// 优惠券类别
				couponType = (int) retMap.get("couponType");
				// 优惠券面值
				couponQuota = new BigDecimal(retMap.get("couponQuota").toString());
				// 如果是加息券则按照加息券的利率计算收益，反之如果是体验金则按照标的的年收益率计算收益
				couponRate = new BigDecimal(retMap.get("couponRate").toString());
			}
			String interest = null;
			String couponInterest = StringUtils.EMPTY;
			if (StringUtils.isBlank(interest)) {
				// 根据项目编号获取相应的项目
				String borrowStyle = borrow.getBorrowStyle();
				BigDecimal borrowApr = borrow.getBorrowApr();
				// 如果是加息券则按照加息券的利率计算收益，反之如果是体验金则按照标的的年收益率计算收益
				couponRate = couponType == 2 ? couponQuota : borrowApr;
				// 周期
				Integer borrowPeriod = borrow.getBorrowPeriod();
				BigDecimal earnings = new BigDecimal("0");
				df.setRoundingMode(RoundingMode.FLOOR);
				CouponConfigCustomizeV2 cuc = investService.getCouponUser(couponGrantId, userId);
				switch (borrowStyle) {
				case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“：历史回报=出借金额*年化收益÷12*月数；
					// 计算历史回报
					earnings = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(account),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
					interest = df.format(earnings);
					if (couponSuccess && cuc != null && Validator.isNotNull(couponRate)
							&& Validator.isNotNull(couponAccount)) {
						if (couponType == 1) {
							earnings = getInterestDj(cuc.getCouponQuota(), cuc.getCouponProfitTime(), borrowApr);
						} else {
							earnings = DuePrincipalAndInterestUtils.getMonthInterest(couponAccount,
									couponRate.divide(new BigDecimal("100")), borrowPeriod).setScale(2,
									BigDecimal.ROUND_DOWN);
						}
						couponInterest = df.format(earnings);
					}
					break;
				case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“：历史回报=出借金额*年化收益÷360*天数；
					earnings = DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(account),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
					interest = df.format(earnings);
					if (couponSuccess && cuc != null && Validator.isNotNull(couponRate)
							&& Validator.isNotNull(couponAccount)) {
						if (couponType == 1) {
							earnings = getInterestDj(cuc.getCouponQuota(), cuc.getCouponProfitTime(), borrowApr);
						} else {
							earnings = DuePrincipalAndInterestUtils.getDayInterest(couponAccount,
									couponRate.divide(new BigDecimal("100")), borrowPeriod).setScale(2,
									BigDecimal.ROUND_DOWN);
						}
						couponInterest = df.format(earnings);
					}
					break;
				case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：历史回报=出借金额*年化收益÷12*月数；
					earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(new BigDecimal(account),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod, borrowPeriod).setScale(2,
							BigDecimal.ROUND_DOWN);
					interest = df.format(earnings);

					if (couponSuccess && cuc != null && Validator.isNotNull(couponRate)
							&& Validator.isNotNull(couponAccount)) {
						if (couponType == 1) {
							earnings = getInterestDj(cuc.getCouponQuota(), cuc.getCouponProfitTime(), borrowApr);
						} else {
							earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(couponAccount,
									couponRate.divide(new BigDecimal("100")), borrowPeriod, borrowPeriod).setScale(2,
									BigDecimal.ROUND_DOWN);
						}
						couponInterest = df.format(earnings);
					}
					break;
				case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：历史回报=出借金额*年化收益÷12*月数；
					earnings = AverageCapitalPlusInterestUtils.getInterestCount(new BigDecimal(account),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
					interest = df.format(earnings);
					if (couponSuccess && cuc != null && Validator.isNotNull(couponRate)
							&& Validator.isNotNull(couponAccount)) {
						if (couponType == 1) {
							earnings = getInterestDj(cuc.getCouponQuota(), cuc.getCouponProfitTime(), borrowApr);
						} else {
							earnings = AverageCapitalPlusInterestUtils.getInterestCount(couponAccount,
									couponRate.divide(new BigDecimal("100")), borrowPeriod).setScale(2,
									BigDecimal.ROUND_DOWN);
						}
						couponInterest = df.format(earnings);
					}
					break;
				case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
					earnings = AverageCapitalUtils.getInterestCount(new BigDecimal(account),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
					interest = df.format(earnings);
					if (couponSuccess && cuc != null && Validator.isNotNull(couponRate)
							&& Validator.isNotNull(couponAccount)) {
						if (couponType == 1) {
							earnings = getInterestDj(cuc.getCouponQuota(), cuc.getCouponProfitTime(), borrowApr);
						} else {
							earnings = AverageCapitalUtils.getInterestCount(couponAccount,
									couponRate.divide(new BigDecimal("100")), borrowPeriod).setScale(2,
									BigDecimal.ROUND_DOWN);
						}
						couponInterest = df.format(earnings);
					}
					break;
				default:
					break;
				}
			}
			if (StringUtils.isNotBlank(interest)) {
				modelAndView.addObject("interest", interest);
			}
			// add by zhangjp 优惠券相关 start
			if (StringUtils.isNotEmpty(couponInterest)) {
				// 历史回报
				modelAndView.addObject("couponInterest", couponInterest);
			}
			if (couponQuota != null) {
				// 优惠券面值
				modelAndView.addObject("couponQuota", couponQuota);
			}
			if (couponType != Integer.MIN_VALUE) {
				// 优惠券类别
				modelAndView.addObject("couponType", couponType);
			}
			modelAndView.addObject("borrowNid", borrow.getBorrowNid());
			modelAndView.addObject("projectTypeName", projectTypeName);
			modelAndView.addObject("account", df.format(new BigDecimal(account)));
			modelAndView.addObject("investDesc", data);
			modelAndView.addObject("projectType", borrow.getProjectType());
		}
		return modelAndView;
	}

	/**
	 * 获取相应的项目名称
	 * 
	 * @param borrowProjectType
	 * @return
	 */
	private String getProjectTypeName(BorrowProjectType borrowProjectType) {
		// 项目类型
		return investService.getProjectTypeName(borrowProjectType);
	}

	/**
	 * 获取最优优惠券
	 * 
	 * @param borrowNid
	 * @param userId
	 * @param money
	 * @param platform
	 * @return
	 */
	private UserCouponConfigCustomize getBestCoupon(String borrowNid, Integer userId, String money, String platform) {
		if (money == null || "".equals(money) || money.length() == 0) {
			money = "0";
		}
		return projectService.getBestCoupon(borrowNid, userId, money, platform);
	}

	/**
	 * 获取最优的优惠券
	 * 
	 * @param couponId
	 * @return
	 */
	private UserCouponConfigCustomize getBestCouponById(String couponId) {
		return projectService.getBestCouponById(couponId);
	}

	/**
	 * 体验金出借
	 * 
	 * @param modelAndView
	 * @param request
	 * @param cuc
	 * @param userId
	 * @return
	 */
	public ModelAndView couponTender(ModelAndView modelAndView, HttpServletRequest request,
			CouponConfigCustomizeV2 cuc, int userId, int couponOldTime) {

		// 金额
		String account = request.getParameter("money");
		String ip = GetCilentIP.getIpAddr(request);
		String couponGrantId = request.getParameter("couponGrantId");
		String borrowNid = request.getParameter("nid");
		if (account == null || "".equals(account)) {
			account = "0";
		}
		// 根据项目编号获取相应的项目
		Borrow borrow = investService.getBorrowByNid(borrowNid);

		// 优惠券出借校验
		Map<String, String> validateMap = this.validateCoupon(userId, account, couponGrantId, borrow.getBorrowNid(),
				CustomConstants.CLIENT_PC);
		if (validateMap.containsKey(CustomConstants.APP_STATUS_DESC)) {
			modelAndView.setViewName(InvestDefine.INVEST_ERROR_PATH);
			modelAndView.addObject("investDesc", validateMap.get(CustomConstants.APP_STATUS_DESC));
			return modelAndView;
		}
		JSONObject jsonObject = CommonSoaUtils.CouponInvest(userId + "", borrowNid, account, CustomConstants.CLIENT_PC,
				couponGrantId, "", ip, couponOldTime + "");

		if (jsonObject.getIntValue("status") == 0) {
			// 取得项目类别
			BorrowProjectType borrowProjectType = investService
					.getBorrowProjectType(borrow.getProjectType().toString());
			/** 修改出借成功页面显示修改开始 */
			String projectTypeName = getProjectTypeName(borrowProjectType);
			modelAndView.addObject("borrowNid", borrowNid);
			modelAndView.addObject("projectTypeName", projectTypeName);
			// 优惠券收益
			modelAndView.addObject("couponInterest", jsonObject.getString("couponInterest"));
			// 优惠券类别
			modelAndView.addObject("couponType", jsonObject.getString("couponTypeInt"));
			// 优惠券额度
			modelAndView.addObject("couponQuota", jsonObject.getString("couponQuota"));
			modelAndView.addObject("investDesc", "出借成功！");
			// 跳转到成功画面
			modelAndView.setViewName(InvestDefine.INVEST_SUCCESS_PATH);
			return modelAndView;
		} else {
			LogUtil.infoLog(InvestServiceImpl.class.toString(), "updateCouponTender", "优惠券出借结束。。。。。。");
			modelAndView.setViewName(InvestDefine.INVEST_ERROR_PATH);
			modelAndView.addObject("investDesc", jsonObject.getString("statusDesc"));
			modelAndView.addObject("projectType", borrow.getProjectType());
			return modelAndView;
		}

	}

	/**
	 * 预约需知页面
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(InvestDefine.APPOINT_CONTRACT_ACTION)
	public ModelAndView contract(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("/user/invest/contract");
	}

	/**
	 * 预约需知页面pdf
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(InvestDefine.APPOINT_CONTRACTPDF_ACTION)
	public Object contractPdf(HttpServletRequest request, HttpServletResponse response) {
		String filename = PropUtils.getSystem(CustomConstants.CONTRACT_FTL_PATH) + "/contractPdf.pdf";
		File file = new File(filename);
		byte[] buf = new byte[1024 * 1024 * 10];
		int len = 0;
		ServletOutputStream ut = null;
		BufferedInputStream br = null;
		response.reset();
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "must-revalidate, no-transform");
		response.setDateHeader("Expires", 0L);

		String userAgent = request.getHeader("User-Agent");
		boolean isIE = (userAgent != null) && (userAgent.toLowerCase().indexOf("msie") != -1);

		String displayFilename = "预约投标服务需知.pdf";

		response.setContentType("application/x-download");
		if (isIE) {
			try {
				displayFilename = URLEncoder.encode(displayFilename, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			response.setHeader("Content-Disposition", "attachment;filename=\"" + displayFilename + "\"");
		} else {
			try {
				displayFilename = new String(displayFilename.getBytes("UTF-8"), "ISO8859-1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			response.setHeader("Content-Disposition", "attachment;filename=" + displayFilename);
		}
		try {
			br = new BufferedInputStream(new FileInputStream(file));
			ut = response.getOutputStream();
			while ((len = br.read(buf)) != -1)
				ut.write(buf, 0, len);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				ut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;

	}

	/**
	 * 预约
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(InvestDefine.APPOINTMENT_ACTION)
	public ModelAndView appointment(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(InvestController.class.toString(), InvestDefine.APPOINTMENT_ACTION);
		Integer userId = WebUtils.getUserId(request);
		ModelAndView modelAndView = new ModelAndView();
		// 借款borrowNid,如HBD120700101
		String borrowNid = request.getParameter("nid");
		String appointCheck = request.getParameter("termcheck");
		// 出借金额
		String accountStr = request.getParameter("money");
		System.out.println("tender ShiroUtil.getLoginUserId--------------------" + userId);
		JSONObject result = investService.checkParamAppointment(borrowNid, accountStr, userId, "0", appointCheck);
		modelAndView.addObject("projectType", 4);
		if (result == null) {
			modelAndView = new ModelAndView(InvestDefine.INVEST_ERROR_PATH);
			modelAndView.addObject("isAppoint", 1);
			modelAndView.addObject("borrowNid", borrowNid);
			modelAndView.addObject("investDesc", "预约失败！");
			return modelAndView;
		} else if (result.get("error") != null && result.get("error").equals("1")) {
			System.out.println(result.toJSONString());
			modelAndView = new ModelAndView(InvestDefine.INVEST_ERROR_PATH);
			modelAndView.addObject("isAppoint", 1);
			modelAndView.addObject("borrowNid", borrowNid);
			modelAndView.addObject("investDesc", result.get("data") + "");
			return modelAndView;
		}
		String account = String.valueOf(Integer.parseInt(accountStr));
		String tenderUsrcustid = result.getString("tenderUsrcustid");
		String borrowerUsrcustid = result.getString("borrowerUsrcustid");
		// 生成订单号
		String orderId = GetOrderIdUtils.getOrderId2(Integer.valueOf(userId));
		// 预约
		BigDecimal accountBigDecimal = new BigDecimal(account);
		Jedis jedis = pool.getResource();
		String balance = RedisUtils.get(borrowNid + CustomConstants.APPOINT);
		if (StringUtils.isNotBlank(balance)) {
			// 操作redis
			while ("OK".equals(jedis.watch(borrowNid + CustomConstants.APPOINT))) {
				balance = RedisUtils.get(borrowNid + CustomConstants.APPOINT);
				if (StringUtils.isNotBlank(balance)) {
					System.out.println("PC用户:" + userId + "***********************************冻结前可投金额：" + balance);
					if (new BigDecimal(balance).compareTo(BigDecimal.ZERO) == 0) {
						modelAndView = new ModelAndView(InvestDefine.INVEST_ERROR_PATH);
						modelAndView.addObject("isAppoint", 1);
						modelAndView.addObject("borrowNid", borrowNid);
						modelAndView.addObject("investDesc", "您来晚了，下次再来抢吧");
						return modelAndView;
					} else {
						if (new BigDecimal(balance).compareTo(accountBigDecimal) < 0) {
							modelAndView = new ModelAndView(InvestDefine.INVEST_ERROR_PATH);
							modelAndView.addObject("isAppoint", 1);
							modelAndView.addObject("borrowNid", borrowNid);
							modelAndView.addObject("investDesc", "可预约剩余金额为" + balance + "元");
							return modelAndView;
						} else {
							Transaction tx = jedis.multi();
							BigDecimal lastAccount = new BigDecimal(balance).subtract(accountBigDecimal);
							tx.set(borrowNid + CustomConstants.APPOINT, lastAccount + "");
							List<Object> result1 = tx.exec();
							if (result1 == null || result1.isEmpty()) {
								jedis.unwatch();
								modelAndView = new ModelAndView(InvestDefine.INVEST_ERROR_PATH);
								modelAndView.addObject("isAppoint", 1);
								modelAndView.addObject("borrowNid", borrowNid);
								modelAndView.addObject("investDesc", "可预约剩余金额为" + balance + "元");
								return modelAndView;
							} else {
								System.out.println("PC用户:" + userId + "***********************************预约前减redis："
										+ accountBigDecimal);
								// 写队列
								break;
							}
						}
					}
				} else {
					modelAndView = new ModelAndView(InvestDefine.INVEST_ERROR_PATH);
					modelAndView.addObject("isAppoint", 1);
					modelAndView.addObject("borrowNid", borrowNid);
					modelAndView.addObject("investDesc", "您来晚了，下次再来抢吧");
					return modelAndView;
				}
			}
		} else {
			modelAndView = new ModelAndView(InvestDefine.INVEST_ERROR_PATH);
			modelAndView.addObject("isAppoint", 1);
			modelAndView.addObject("borrowNid", borrowNid);
			modelAndView.addObject("investDesc", "您来晚了，下次再来抢吧");
			return modelAndView;
		}
		// 调用冻结接口开始冻结
		FreezeDefine freezeDefine = investService.freeze(userId, account, tenderUsrcustid, borrowerUsrcustid, orderId);
		// 冻结标识
		boolean freezeFlag = false;
		String freezeTrxId = null;
		if (freezeDefine != null) {
			freezeTrxId = freezeDefine.getFreezeTrxId();
			System.out.println("PC用户:" + userId + "***********************************冻结订单号：" + freezeTrxId);
			if (freezeDefine.isFreezeFlag()) {
				System.out.println("PC用户:" + userId + "***********************************冻结成功额度：" + account);
				freezeFlag = freezeDefine.isFreezeFlag();
			}
		}
		if (!freezeFlag) {
			System.out.println("PC用户预约:" + userId + "***********************************冻结失败额度：" + account);
			// 恢复redis
			investService.recoverRedis(borrowNid + CustomConstants.APPOINT, userId, account);
			LogUtil.errorLog(InvestController.class.getName(), "appointment", "freeze error freezeDefine is null", null);
			modelAndView = new ModelAndView(InvestDefine.INVEST_ERROR_PATH);
			modelAndView.addObject("isAppoint", 1);
			modelAndView.addObject("borrowNid", borrowNid);
			modelAndView.addObject("investDesc", "预约冻结失败！请联系客服。");
			return modelAndView;
		}
		// 借款项目
		Borrow borrow = investService.getBorrowByNid(borrowNid);
		boolean afterDealFlag = false;
		// 写入预约表
		try {
			afterDealFlag = investService.updateAfterAppointRedis(borrow.getId(), borrowNid, borrow.getProjectType(),
					borrow.getBorrowStyle(), borrow.getBorrowApr(), borrow.getBorrowPeriod(),
					borrow.getBorrowAccountWaitAppoint(), orderId, userId, account, tenderUsrcustid,
					borrow.getAccount(), 0, GetCilentIP.getIpAddr(request), freezeTrxId);

			if (afterDealFlag) {
				modelAndView = new ModelAndView(InvestDefine.INVEST_SUCCESS_PATH);
				modelAndView.addObject("isAppoint", 1);
				modelAndView.addObject("borrowNid", borrowNid);
				LogUtil.endLog(InvestDefine.class.toString(), InvestDefine.RETURL_SYN_ACTION, "[交易完成后,回调结束]");
				DecimalFormat df = CustomConstants.DF_FOR_VIEW;
				// 取得项目类别
				BorrowProjectType borrowProjectType = investService.getBorrowProjectType(borrow.getProjectType()
						.toString());
				// String projectTypeName = borrowProjectType.getBorrowName();
				/** 修改出借成功页面显示修改开始 */
				String projectTypeName = getProjectTypeName(borrowProjectType);
				/** 修改出借成功页面显示修改结束 */
				String interest = null;
				if (StringUtils.isBlank(interest)) {
					// 根据项目编号获取相应的项目
					String borrowStyle = borrow.getBorrowStyle();
					BigDecimal borrowApr = borrow.getBorrowApr();
					// 周期
					Integer borrowPeriod = borrow.getBorrowPeriod();
					BigDecimal earnings = new BigDecimal("0");
					df.setRoundingMode(RoundingMode.FLOOR);
					switch (borrowStyle) {
					case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“：历史回报=出借金额*年化收益÷12*月数；
						// 计算历史回报
						earnings = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(account),
								borrowApr.divide(new BigDecimal("100")), borrowPeriod);
						interest = df.format(earnings);
						break;
					case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“：历史回报=出借金额*年化收益÷360*天数；
						earnings = DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(account),
								borrowApr.divide(new BigDecimal("100")), borrowPeriod);
						interest = df.format(earnings);
						break;
					case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：历史回报=出借金额*年化收益÷12*月数；
						earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(new BigDecimal(account),
								borrowApr.divide(new BigDecimal("100")), borrowPeriod, borrowPeriod);
						interest = df.format(earnings);
						break;
					case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：历史回报=出借金额*年化收益÷12*月数；
						earnings = AverageCapitalPlusInterestUtils.getInterestCount(new BigDecimal(account),
								borrowApr.divide(new BigDecimal("100")), borrowPeriod);
						interest = df.format(earnings);
						break;
					case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
						earnings = AverageCapitalUtils.getInterestCount(new BigDecimal(account),
								borrowApr.divide(new BigDecimal("100")), borrowPeriod);
						interest = df.format(earnings);
						break;
					default:
						break;
					}
				}
				if (StringUtils.isNotBlank(interest)) {
					modelAndView.addObject("interest", interest);
				}
				modelAndView.addObject("borrowNid", borrow.getBorrowNid());
				modelAndView.addObject("projectTypeName", projectTypeName);
				modelAndView.addObject("account", df.format(new BigDecimal(account)));
				modelAndView.addObject("investDesc", "恭喜您预约成功!");
				modelAndView.addObject("projectType", borrow.getProjectType());
				System.out.println("PC用户:" + userId + "***********************************预约成功：" + account);
				LogUtil.endLog(InvestController.class.toString(), InvestDefine.APPOINTMENT_ACTION);
				return modelAndView;
			} else {
				System.out.println("PC用户:" + userId + "***********************************预约成功后处理失败：" + account);
				// 恢复redis
				investService.recoverRedis(borrowNid + CustomConstants.APPOINT, userId, account);
				modelAndView = new ModelAndView(InvestDefine.INVEST_ERROR_PATH);
				modelAndView.addObject("isAppoint", 1);
				modelAndView.addObject("borrowNid", borrowNid);
				modelAndView.addObject("investDesc", "系统异常");
				return modelAndView;
			}
		} catch (Exception e) {
			// 恢复redis
			investService.recoverRedis(borrowNid + CustomConstants.APPOINT, userId, account);
			modelAndView = new ModelAndView(InvestDefine.INVEST_ERROR_PATH);
			modelAndView.addObject("isAppoint", 1);
			modelAndView.addObject("borrowNid", borrowNid);
			modelAndView.addObject("investDesc", "系统异常");
			System.out.println("PC用户:" + userId + "***********************************预约成功后处理失败：" + account);
			LogUtil.errorLog(InvestController.class.getName(), "tenderRetUrl", e);
			return modelAndView;

		}
	}

}
