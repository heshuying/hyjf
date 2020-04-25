package com.hyjf.api.web.invest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.web.BaseController;
import com.hyjf.api.web.vip.apply.ApplyServer;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.calculate.AverageCapitalPlusInterestUtils;
import com.hyjf.common.calculate.AverageCapitalUtils;
import com.hyjf.common.calculate.BeforeInterestAfterPrincipalUtils;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.invest.AvailableCouponResultBean;
import com.hyjf.invest.CouponInvestResultBean;
import com.hyjf.invest.InvestBean;
import com.hyjf.invest.InvestDefine;
import com.hyjf.invest.InvestResultBean;
import com.hyjf.invest.InvestService;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;
import com.hyjf.vip.apply.ApplyDefine;

@Controller
@RequestMapping(value = InvestDefine.REQUEST_MAPPING)
public class InvestServer extends BaseController {
	Logger _log = LoggerFactory.getLogger(ApplyServer.class);
	@Autowired
	private InvestService investService;

	/** 发布地址 */
	// private static String HOST_URL = PropUtils.getSystem("hyjf.api.web.url");

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
	public InvestResultBean getInvestInfo(HttpServletRequest request, HttpServletResponse response,
			InvestBean investBean) {

		String borrowNid = investBean.getBorrowNid();
		String money = investBean.getMoney();
		String platform = investBean.getPlatform();
		String couponGrantId = investBean.getCouponGrantId();
		Integer userId = investBean.getUserId();
		DecimalFormat df = null;
		df = CustomConstants.DF_FOR_VIEW;

		InvestResultBean resultVo = new InvestResultBean();
		// resultVo.setProspectiveEarnings("");

		if (!this.checkSign(investBean, InvestDefine.METHOD_GET_INVEST_INFO_MAPPING)) {
			resultVo.setStatus(BaseResultBean.STATUS_FAIL);
			resultVo.setStatusDesc("验签失败！");
			return resultVo;
		}

		resultVo.setInterest("");
		if (money == null || "".equals(money)) {
			money = "0";
			resultVo.setRealAmount("");
		} else {
			resultVo.setRealAmount("");
		}

		// 查询项目信息
		Borrow borrow = investService.getBorrowByNid(borrowNid);
		// BigDecimal borrowInterest = new BigDecimal(0);
		BigDecimal earnings = new BigDecimal("0");
		BigDecimal couponInterest = BigDecimal.ZERO;
		// 获取用户最优优惠券
		UserCouponConfigCustomize couponConfig = null;
		if (null != borrow) {
			// Integer projectType=borrow.getProjectType();
			// if(projectType!=4){
			couponConfig = new UserCouponConfigCustomize();
			if (couponGrantId == null || "".equals(couponGrantId) || couponGrantId.length() == 0) {
				// 获取用户最优优惠券
				couponConfig = getBestCoupon(borrowNid, userId, money, platform);
			} else {
				couponConfig = getBestCouponById(couponGrantId);
			}

			String balanceWait = borrow.getBorrowAccountWait() + "";
			if (balanceWait == null || balanceWait.equals("")) {
				balanceWait = "0";
			}

			// 刚加载页面并且可投小于起投
			if ((StringUtils.isBlank(money) || money.equals("0"))
					&& new BigDecimal(balanceWait).compareTo(new BigDecimal(borrow.getTenderAccountMin())) < 1) {
				money = new BigDecimal(balanceWait).intValue() + "";
			}
			if (money.contains(",")) {
				money = money.replace(",", "");
			}
			if ((!StringUtils.isBlank(money) && Double.parseDouble(money) > 0)
					|| (couponConfig != null && (couponConfig.getCouponType() == 1 || couponConfig.getCouponType() == 3))) {
				String borrowStyle = borrow.getBorrowStyle();
				// String prospectiveEarnings = "";
				// 收益率
				BigDecimal borrowApr = borrow.getBorrowApr();
				if (couponConfig != null && couponConfig.getId() > 0) {
					/*
					 * if (couponConfig.getCouponType() == 1) { BigDecimal
					 * tempMoney = StringUtils.isEmpty(money) ? BigDecimal.ZERO
					 * : new BigDecimal(money); money =
					 * tempMoney.add(couponConfig.getCouponQuota()).toString();
					 * }
					 */
					if (couponConfig.getCouponType() == 2) {
						borrowApr = borrowApr.add(couponConfig.getCouponQuota());
					}
					if (couponConfig.getCouponType() == 3) {
						BigDecimal tempMoney = StringUtils.isEmpty(money) ? BigDecimal.ZERO : new BigDecimal(money);
						money = tempMoney.add(couponConfig.getCouponQuota()).toString();
					}

				}
				if (borrow.getProjectType() == 13 && borrow.getBorrowExtraYield() != null
						&& borrow.getBorrowExtraYield().compareTo(BigDecimal.ZERO) > 0) {
					borrowApr = borrowApr.add(borrow.getBorrowExtraYield());
				}
				// 周期
				Integer borrowPeriod = borrow.getBorrowPeriod();

				df.setRoundingMode(RoundingMode.FLOOR);
				switch (borrowStyle) {
				case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“：预期收益=出借金额*年化收益÷12*月数；
					// 计算预期收益
					earnings = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(money),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
					resultVo.setInterest(df.format(earnings));
					break;
				case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“：预期收益=出借金额*年化收益÷360*天数；
					earnings = DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(money),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
					resultVo.setInterest(df.format(earnings));
					break;
				case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：预期收益=出借金额*年化收益÷12*月数；
					earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(new BigDecimal(money),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod, borrowPeriod).setScale(2,
							BigDecimal.ROUND_DOWN);
					resultVo.setInterest(df.format(earnings));
					break;
				case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：预期收益=出借金额*年化收益÷12*月数；
					earnings = AverageCapitalPlusInterestUtils.getInterestCount(new BigDecimal(money),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
					resultVo.setInterest(df.format(earnings));
					break;
				case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
					earnings = AverageCapitalUtils.getInterestCount(new BigDecimal(money),
							borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
					resultVo.setInterest(df.format(earnings));
					break;
				default:
					resultVo.setInterest("");
					break;
				}

				borrowPeriod = borrow.getBorrowPeriod();

				money = investBean.getMoney();
				if (money == null || "".equals(money)) {
					money = "0";
				}
				if (money.contains(",")) {
					money = money.replace(",", "");
				}
				if (couponConfig != null && couponConfig.getId() > 0) {
					borrowApr = borrow.getBorrowApr();
					borrowStyle = borrow.getBorrowStyle();
					if (couponConfig.getCouponType() == 1) {
						couponInterest = getInterestDj(couponConfig.getCouponQuota(),
								couponConfig.getCouponProfitTime(), borrowApr);
					} else {
						couponInterest = getInterest(borrowStyle, couponConfig.getCouponType(), borrowApr,
								couponConfig.getCouponQuota(), money, borrow.getBorrowPeriod());
					}

					couponConfig.setCouponInterest(df.format(couponInterest));
				}

			}

			// 查询用户可用余额

			if (couponConfig != null && couponConfig.getId() > 0) {
				if (couponConfig.getCouponType() == 1) {
					resultVo.setCouponDescribe("体验金:" + couponConfig.getCouponQuota() + "元");
					resultVo.setCouponType("体验金");
					resultVo.setInterest(df.format(earnings.add(couponInterest)));
				}
				if (couponConfig.getCouponType() == 2) {
					resultVo.setCouponDescribe("加息券:" + couponConfig.getCouponQuota() + "%");
					resultVo.setCouponType("加息券");
					resultVo.setRealAmount("");

				}
				if (couponConfig.getCouponType() == 3) {
					resultVo.setCouponDescribe("代金券:" + couponConfig.getCouponQuota() + "元");
					resultVo.setCouponType("代金券");
					resultVo.setRealAmount("实际出借 " + df.format(new BigDecimal(money)) + "元");

				}
				resultVo.setCouponTypeStr(couponConfig.getCouponType() + "");
				resultVo.setCouponName(couponConfig.getCouponName());
				resultVo.setCouponQuota(couponConfig.getCouponQuota().toString());
				resultVo.setEndTime(couponConfig.getCouponAddTime() + "-" + couponConfig.getEndTime());
				resultVo.setIsThereCoupon("1");
				resultVo.setCouponId(couponConfig.getUserCouponId());

				resultVo.setIsUsedCoupon("1");
				resultVo.setCapitalInterest(df.format(couponInterest));
			} else {
				resultVo.setIsThereCoupon("0");
				resultVo.setCouponDescribe("暂无可用");
				resultVo.setCouponName("");
				resultVo.setCouponQuota("");
				resultVo.setEndTime("");
				resultVo.setCouponId("");
				String couponAvailableCount = investService.getUserCouponAvailableCount(borrowNid, userId,
						request.getParameter("money"), platform);
				if (!"0".equals(couponAvailableCount)) {
					resultVo.setIsThereCoupon("1");
					resultVo.setCouponDescribe(couponAvailableCount + "张可用");
				}

				resultVo.setIsUsedCoupon("0");
				resultVo.setCapitalInterest("");
			}

			if (couponConfig != null && couponConfig.getCouponType() == 3) {
				resultVo.setBorrowInterest(df.format(earnings.add(couponConfig.getCouponQuota()).subtract(
						couponInterest)));
			} else if (couponConfig != null && couponConfig.getCouponType() == 1) {
				resultVo.setBorrowInterest(df.format(earnings));
			} else {
				resultVo.setBorrowInterest(df.format(earnings.subtract(couponInterest)));
			}

			String couponAvailableCount = investService.getUserCouponAvailableCount(borrowNid, userId,
					request.getParameter("money"), platform);
			resultVo.setCouponAvailableCount(couponAvailableCount);

			resultVo.setStatus(CustomConstants.APP_STATUS_SUCCESS);
			resultVo.setStatusDesc(CustomConstants.APP_STATUS_DESC_SUCCESS);

		} else {
			resultVo.setStatusDesc(CustomConstants.APP_STATUS_DESC_FAIL);
		}

		return resultVo;
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
		case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“：预期收益=出借金额*年化收益÷12*月数；
			// 计算预期收益
			earnings = DuePrincipalAndInterestUtils.getMonthInterest(accountDecimal,
					borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
			break;
		case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“：预期收益=出借金额*年化收益÷360*天数；
			// 计算预期收益
			earnings = DuePrincipalAndInterestUtils.getDayInterest(accountDecimal,
					borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
			break;
		case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：预期收益=出借金额*年化收益÷12*月数；
			// 计算预期收益
			earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(accountDecimal,
					borrowApr.divide(new BigDecimal("100")), borrowPeriod, borrowPeriod).setScale(2,
					BigDecimal.ROUND_DOWN);
			break;
		case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：预期收益=出借金额*年化收益÷12*月数；
			// 计算预期收益
			earnings = AverageCapitalPlusInterestUtils.getInterestCount(accountDecimal,
					borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
			break;
		case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
			earnings = AverageCapitalUtils.getInterestCount(accountDecimal, borrowApr.divide(new BigDecimal("100")),
					borrowPeriod).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
			break;
		default:
			break;
		}
		if (couponType == 3) {
			earnings = earnings.add(couponQuota);
		}
		return earnings;
	}

	private UserCouponConfigCustomize getBestCoupon(String borrowNid, Integer userId, String money, String platform) {
		// Integer userId = 20000125;
		if (money == null || "".equals(money) || money.length() == 0) {
			money = "0";
		}
		return investService.getBestCoupon(borrowNid, userId, money, platform);
	}

	private UserCouponConfigCustomize getBestCouponById(String couponId) {
		return investService.getBestCouponById(couponId);
	}

	/**
	 * 
	 * 根据borrowNid和用户id获取用户可用优惠券和不可用优惠券列表
	 * 
	 * @author pcc
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = InvestDefine.GET_PROJECT_AVAILABLE_USER_COUPON_ACTION, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public BaseResultBean getProjectAvailableUserCoupon(HttpServletRequest request, HttpServletResponse response,
			InvestBean paramBean) {

		AvailableCouponResultBean result = new AvailableCouponResultBean();
		// 检查参数正确性
		if (Validator.isNull(paramBean.getBorrowNid()) || Validator.isNull(paramBean.getUserId())) {
			result.setStatus(AvailableCouponResultBean.STATUS_FAIL);
			result.setStatusDesc("请求参数非法");
			return result;
		}

		if (!this.checkSign(paramBean, InvestDefine.METHOD_GET_PROJECT_AVAILABLE_USER_COUPON_ACTION)) {
			result.setStatus(BaseResultBean.STATUS_FAIL);
			result.setStatusDesc("验签失败！");
			return result;
		}
		if (paramBean.getMoney() == null || "".equals(paramBean.getMoney()) || paramBean.getMoney().length() == 0) {
			paramBean.setMoney("0");
		}
		result = investService.getProjectAvailableUserCoupon(paramBean);

		return result;
	}

	/**
	 * 出借校验
	 * 
	 * @param account
	 * @param userId
	 * @param couponType
	 * @param borrowNid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = InvestDefine.CHECK_PARAM_ACTION, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public BaseResultBean checkParam(HttpServletRequest request, HttpServletResponse response, InvestBean investBean) {
		BaseResultBean baseResultBean = new BaseResultBean();
		String account = investBean.getMoney();
		String couponGrantId = investBean.getCouponGrantId();
		String borrowNid = investBean.getBorrowNid();
		int userId = investBean.getUserId();
		// 出借平台
		String platform = investBean.getPlatform();

		if (!this.checkSign(investBean, InvestDefine.METHOD_CHECK_PARAM_ACTION)) {
			baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
			baseResultBean.setStatusDesc("验签失败！");
			return baseResultBean;
		}

		CouponConfigCustomizeV2 couponConfig = null;
		if (StringUtils.isNotEmpty(couponGrantId)) {
			couponConfig = investService.getCouponUser(couponGrantId, userId);
		}
		JSONObject result = investService.checkParam(borrowNid, account, userId + "", platform, couponConfig);
		if (result == null || result.get("error").equals("1")) {
			// 回调url（h5错误页面）
			baseResultBean.setStatus(BaseResultBean.STATUS_DESC_FAIL);
			baseResultBean.setStatusDesc((String) result.get(CustomConstants.APP_STATUS));
		} else {
			baseResultBean.setStatus(BaseResultBean.STATUS_DESC_SUCCESS);
			baseResultBean.setStatusDesc("");
		}
		return baseResultBean;
	}

	/**
	 * 优惠券出借校验
	 * 
	 * @param account
	 * @param userId
	 * @param couponType
	 * @param borrowNid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = InvestDefine.VALIDATE_COUPON_ACTION, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public BaseResultBean validateCoupon(HttpServletRequest request, HttpServletResponse response, InvestBean investBean) {
		BaseResultBean baseResultBean = new BaseResultBean();
		// 金额
		String account = investBean.getMoney();
		String couponGrantId = investBean.getCouponGrantId();
		String borrowNid = investBean.getBorrowNid();
		int userId = investBean.getUserId();
		System.out.println("优惠劵校验:" + couponGrantId );

		// 根据项目编号获取相应的项目
		Borrow borrow = investService.getBorrowByNid(borrowNid);
		// 出借平台
		String platform = investBean.getPlatform();

		if (!this.checkSign(investBean, InvestDefine.METHOD_VALIDATE_COUPON_ACTION)) {
			baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
			baseResultBean.setStatusDesc("验签失败！");
			return baseResultBean;
		}
		 // 检查用户角色是否能出借  合规接口改造之后需要判断
        UsersInfo userInfo = investService.getUsersInfoByUserId(userId);
        if (null != userInfo) {
			String roleIsOpen = PropUtils.getSystem(CustomConstants.HYJF_ROLE_ISOPEN);
			if(StringUtils.isNotBlank(roleIsOpen) && roleIsOpen.equals("true")){
				if (userInfo.getRoleId() != 1) {// 非出借用户
					baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
					baseResultBean.setStatusDesc("仅限出借人进行出借");
					return baseResultBean;
				}
			}


        } else {
            baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
            baseResultBean.setStatusDesc("账户信息异常");
            return baseResultBean;
        }

		CouponConfigCustomizeV2 couponConfig = null;
		if (StringUtils.isNotEmpty(couponGrantId)) {
			couponConfig = investService.getCouponUser(couponGrantId, userId);
		}

		if (StringUtils.isNotEmpty(couponGrantId)) {
			couponConfig = investService.getCouponUser(couponGrantId, userId);
			if (couponConfig == null || couponConfig.getUsedFlag() != 0) {
				baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
				baseResultBean.setStatusDesc("当前优惠券不存在或已使用");
				return baseResultBean;
			}
		} else {
			baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
			baseResultBean.setStatusDesc("优惠券id为空");
			return baseResultBean;
		}

		// 加息券标识（0：禁用，1：可用）
		int interestCoupon = borrow.getBorrowInterestCoupon();
		// 体验金标识（0：禁用，1：可用）
		int moneyCoupon = borrow.getBorrowTasteMoney();
		// CouponType(1：体验金，2：加息券)
		if (couponConfig.getCouponType() != 3&&((interestCoupon == 0 && moneyCoupon == 0)
				|| (interestCoupon == 1 && moneyCoupon != 1 && couponConfig.getCouponType() == 1)
				|| (interestCoupon != 1 && moneyCoupon == 1 && couponConfig.getCouponType() == 2))) {
			baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
			baseResultBean.setStatusDesc("您选择的优惠券不满足使用条件，请核对后重新选择！");
			return baseResultBean;
		}
		// 取得优惠券配置
		if (couponConfig.getUsedFlag() != 0) {
			baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
			baseResultBean.setStatusDesc("您选择的券已经使用，请重新选择优惠券！");
			return baseResultBean;
		}
		// 加息券不能单独出借
		if ((StringUtils.isEmpty(account) || StringUtils.equals(account, "0")) && couponConfig.getCouponType() == 2) {
			baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
			baseResultBean.setStatusDesc("加息券出借，真实出借金额不能为空！");
			return baseResultBean;
		}

		// 体验金不能单独出借
		/*
		 * if ((StringUtils.isEmpty(account) || StringUtils.equals(account,
		 * "0")) && couponConfig.getCouponType() ==
		 * 1&&couponConfig.getAddFlg()==0) {
		 * baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
		 * baseResultBean.setStatusDesc("该体验金出借，真实出借金额不能为空！"); return
		 * baseResultBean; }
		 */
		// 操作系统
		if (!StringUtils.contains(couponConfig.getCouponSystem(), platform)) {
			baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
			baseResultBean.setStatusDesc("对不起，当前平台不能使用此优惠券！");
			return baseResultBean;
		}
		// 项目类型 pcc20160715
		/*
		 * String[] couponProjectTypeList =
		 * StringUtils.split(couponConfig.getProjectType(),
		 * InvestDefine.DOU_HAO);
		 */
		String projectType = String.valueOf(borrow.getProjectType());

		String msg = investService.validateCouponProjectType(couponConfig.getProjectType(), projectType);

		if (msg != null && msg.length() != 0) {
			baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
			baseResultBean.setStatusDesc(msg);
			return baseResultBean;
		}
		// 项目金额
		// 金额类别
		int tenderQuotaType = couponConfig.getTenderQuotaType();
		// 出借金额
		BigDecimal tenderAccount = new BigDecimal(account);
		// 金额范围
		if (tenderQuotaType == 1) {
			// 出借金额最小额度
			BigDecimal tenderQuotaMin = new BigDecimal(couponConfig.getTenderQuotaMin());
			// 出借金额最大额度
			BigDecimal tenderQuotaMax = new BigDecimal(couponConfig.getTenderQuotaMax());
			// 比较出借金额（-1表示小于,0是等于,1是大于）
			int minCheck = tenderAccount.compareTo(tenderQuotaMin);
			int maxCheck = tenderAccount.compareTo(tenderQuotaMax);
			if (minCheck == -1 || maxCheck == 1) {
				baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
				baseResultBean.setStatusDesc("您选择的优惠券不满足使用条件，请核对后重新选择！");
				return baseResultBean;
			}
		} else if (tenderQuotaType == 2) {
			// 大于等于
			BigDecimal tenderQuota = new BigDecimal(couponConfig.getTenderQuota());
			// 比较出借金额（-1表示小于,0是等于,1是大于）
			int chkFlg = tenderAccount.compareTo(tenderQuota);
			if (chkFlg == -1) {
				baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
				baseResultBean.setStatusDesc("您选择的优惠券不满足使用条件，请核对后重新选择！");
				return baseResultBean;
			}

		}
		// 项目期限
		int borrowPeriod = borrow.getBorrowPeriod();
		int couponExType = couponConfig.getProjectExpirationType();
		int expirationLength = couponConfig.getProjectExpirationLength() == null ? 0 : couponConfig
				.getProjectExpirationLength();
		int expirationMin = couponConfig.getProjectExpirationLengthMin() == null ? 0 : couponConfig
				.getProjectExpirationLengthMin();
		int expirationMax = couponConfig.getProjectExpirationLengthMax() == null ? 0 : couponConfig
				.getProjectExpirationLengthMax();
		if (StringUtils.equals(borrow.getBorrowStyle(), CustomConstants.BORROW_STYLE_ENDDAY)) {
			expirationLength = expirationLength * 30;
			expirationMin = expirationMin * 30;
			expirationMax = expirationMax * 30;
		}
		// 等于
		if (couponExType == 1) {
			if (expirationLength != borrowPeriod) {
				baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
				baseResultBean.setStatusDesc("您选择的优惠券不满足使用条件，请核对后重新选择！");
				return baseResultBean;
			}
		} else if (couponExType == 2) {
			// 期限范围
			if (borrowPeriod < expirationMin || borrowPeriod > expirationMax) {
				baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
				baseResultBean.setStatusDesc("您选择的优惠券不满足使用条件，请核对后重新选择！");
				return baseResultBean;
			}
		} else if (couponExType == 3) {
			// 大于等于
			if (borrowPeriod < expirationLength) {
				baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
				baseResultBean.setStatusDesc("您选择的优惠券不满足使用条件，请核对后重新选择！");
				return baseResultBean;
			}
		} else if (couponExType == 4) {
			// 小于等于
			if (borrowPeriod > expirationLength) {
				baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
				baseResultBean.setStatusDesc("您选择的优惠券不满足使用条件，请核对后重新选择！");
				return baseResultBean;
			}
		}
		// 截止时间
		// yyyy-MM-dd 的时间戳
		int nowTime = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf));
		if (couponConfig.getEndTime() < nowTime) {
			baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
			baseResultBean.setStatusDesc("当前优惠券无法使用，优惠券已过期");
			return baseResultBean;
		}
		baseResultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
		baseResultBean.setStatusDesc("");
		return baseResultBean;
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
	@ResponseBody
	@RequestMapping(value = InvestDefine.COUPON_TENDER_ACTION, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public CouponInvestResultBean couponTender(HttpServletRequest request, HttpServletResponse response,
			InvestBean investBean) {
		CouponInvestResultBean couponInvestResultBean = new CouponInvestResultBean();
		// 下单时间
		String ordDate = GetDate.getServerDateTime(1, new Date());
		// 金额
		String account = investBean.getMoney();
		String ip = investBean.getIp();
		String couponGrantId = investBean.getCouponGrantId();
		String borrowNid = investBean.getBorrowNid();
		String ordId = investBean.getOrdId();
		int userId = investBean.getUserId();
		int couponOldTime = investBean.getCouponOldTime();
		System.out.println("=================cwyang 开始优惠券出借=================================" + couponGrantId);
		if (!this.checkSign(investBean, InvestDefine.METHOD_COUPON_TENDER_ACTION)) {
			couponInvestResultBean.setStatus(BaseResultBean.STATUS_FAIL);
			couponInvestResultBean.setStatusDesc("验签失败！");
			return couponInvestResultBean;
		}

		// 根据项目编号获取相应的项目
		Borrow borrow = investService.getBorrowByNid(borrowNid);
		// 出借平台
		String platform = investBean.getPlatform();
		CouponConfigCustomizeV2 cuc = null;
//		BigDecimal couponQuota = null;
		if (StringUtils.isNotEmpty(couponGrantId)) {
			cuc = investService.getCouponUser(couponGrantId, userId);
			if (cuc == null || cuc.getUsedFlag() != 0) {
				couponInvestResultBean.setStatus(BaseResultBean.STATUS_FAIL);
				couponInvestResultBean.setStatusDesc("当前优惠券不存在或已使用");
				return couponInvestResultBean;
			}
//			couponQuota = cuc.getCouponQuota();
			// 排他check用
			couponOldTime = cuc.getUserUpdateTime();
		} else {
			couponInvestResultBean.setStatus(BaseResultBean.STATUS_FAIL);
			couponInvestResultBean.setStatusDesc("优惠券id为空");
			return couponInvestResultBean;
		}
		int couponType = cuc.getCouponType();
		int client = StringUtils.isNotEmpty(platform) ? Integer.valueOf(platform) : 0;
		// 用户优惠券更新时间 排他校验用
		// int orderTime = cuc.getUpdateTime();
		// 优惠券出借校验
		InvestBean bean = createValidateCouponCheckSign(investBean);
		BaseResultBean baseResultBean = this.validateCoupon(request, response, bean);
		if (BaseResultBean.STATUS_FAIL.equals(baseResultBean.getStatus())) {
			couponInvestResultBean.setStatus(BaseResultBean.STATUS_FAIL);
			couponInvestResultBean.setStatusDesc(baseResultBean.getStatusDesc());
			return couponInvestResultBean;
		}

		Map<String, Object> retMap = new HashMap<String, Object>();
		// 优惠券出借
		boolean couponSuccess;
		try {
			couponSuccess = investService.updateCouponTender(couponGrantId, borrowNid, ordDate, userId, account, ip,
					client, couponOldTime, ordId, retMap);
			if (!couponSuccess) {
				_log.info("选择的优惠券异常，请刷新后重试！");
				couponInvestResultBean.setStatus(BaseResultBean.STATUS_FAIL);
				couponInvestResultBean.setStatusDesc("选择的优惠券异常，请刷新后重试！");
				return couponInvestResultBean;
			}
		} catch (Exception e) {
			_log.info("选择的优惠券异常，请刷新后重试！");
			couponInvestResultBean.setStatus(BaseResultBean.STATUS_FAIL);
			couponInvestResultBean.setStatusDesc("选择的优惠券异常，请刷新后重试！");
			return couponInvestResultBean;
		}

		// 优惠券出借额度
		BigDecimal couponAccount = (BigDecimal) retMap.get("couponAccount");
		// 优惠券类别
		int couponTypeInt = Integer.valueOf(couponType);
		// 优惠券面值
		BigDecimal couponQuota = new BigDecimal(retMap.get("couponQuota").toString());
		// 优惠券收益
		String couponInterest = null;
		BigDecimal earnings = null;
		BigDecimal borrowApr = new BigDecimal(0);
		if (couponTypeInt == 2) {//add by cywang 加息券时,计算利率根据优惠券面值
			borrowApr = cuc.getCouponQuota();//borrow.getBorrowApr(); modify by cwyang 2017-5-10 计算优惠券的收益的时候应该使用优惠券的面值进行计算
		}else{
			System.out.println("================cwyang 体验金和代金券的利率有标的决定========");
			borrowApr = borrow.getBorrowApr();
		}
		BigDecimal djBorrowApr = borrow.getBorrowApr();
		System.out.println("==============cweyang 优惠券出借利率是 " + borrowApr);
		Integer borrowPeriod = borrow.getBorrowPeriod();
		DecimalFormat df = CustomConstants.DF_FOR_VIEW;
		if (couponTypeInt == 1) {//体验金的预期收益
			couponInterest = df.format(getInterestDj(cuc.getCouponQuota(), cuc.getCouponProfitTime(), djBorrowApr));
		} else {
			System.out.println("============cwyang borrow.getBorrowStyle() is " + borrow.getBorrowStyle());
			switch (borrow.getBorrowStyle()) {
			case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“：预期收益=出借金额*年化收益÷12*月数；
				// 计算预期收益
				earnings = DuePrincipalAndInterestUtils.getMonthInterest(couponAccount,
						borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
				couponInterest = df.format(earnings);
				break;
			case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“：预期收益=出借金额*年化收益÷360*天数；
				earnings = DuePrincipalAndInterestUtils.getDayInterest(couponAccount,
						borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
				couponInterest = df.format(earnings);
				break;
			case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：预期收益=出借金额*年化收益÷12*月数；
				earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(couponAccount,
						borrowApr.divide(new BigDecimal("100")), borrowPeriod, borrowPeriod).setScale(2,
						BigDecimal.ROUND_DOWN);
				couponInterest = df.format(earnings);
				break;
			case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：预期收益=出借金额*年化收益÷12*月数；
				earnings = AverageCapitalPlusInterestUtils.getInterestCount(couponAccount,
						borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
				couponInterest = df.format(earnings);
				break;
			case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
				earnings = AverageCapitalUtils.getInterestCount(couponAccount, borrowApr.divide(new BigDecimal("100")),
						borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
				couponInterest = df.format(earnings);
				break;
			default:
				break;
			}
		}
		couponInvestResultBean.setAccountDecimal(couponAccount.toString()); 
		// 优惠券收益
		couponInvestResultBean.setCouponInterest(couponInterest);
		System.out.println("===================cwyang 优惠券预期收益为: " + couponInterest);
		// 优惠券类别
		String couponTypeString = "";
		if (couponTypeInt == 1) {
			couponTypeString = "体验金";
		} else if (couponTypeInt == 2) {
			couponTypeString = "加息券";
		} else if (couponTypeInt == 3) {
			couponTypeString = "代金券";
		}
		couponInvestResultBean.setCouponTypeInt(couponTypeInt + "");
		couponInvestResultBean.setCouponType(couponTypeString);
		// 优惠券额度
		couponInvestResultBean.setCouponQuota(couponQuota.toString());
		;
		// 跳转到成功画面
		// modelAndView.setViewName(InvestDefine.INVEST_SUCCESS_PATH);
		couponInvestResultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
		couponInvestResultBean.setStatusDesc("");
		return couponInvestResultBean;
	}

	private BigDecimal getInterestDj(BigDecimal couponQuota, Integer couponProfitTime, BigDecimal borrowApr) {
		BigDecimal earnings = new BigDecimal("0");

		earnings = couponQuota.multiply(borrowApr.divide(new BigDecimal(100), 6, BigDecimal.ROUND_HALF_UP))
				.divide(new BigDecimal(365), 6, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(couponProfitTime))
				.setScale(2, BigDecimal.ROUND_DOWN);

		return earnings;

	}

	private InvestBean createValidateCouponCheckSign(InvestBean investBean) {
		String accessKey = PropUtils.getSystem(ApplyDefine.AOP_INTERFACE_ACCESSKEY);
		InvestBean bean = new InvestBean();
		Integer userId = investBean.getUserId();
		Long timestamp = investBean.getTimestamp();
		String borrowNid = investBean.getBorrowNid();
		String platform = investBean.getPlatform();
		String couponGrantId = investBean.getCouponGrantId() == null ? "" : investBean.getCouponGrantId();
		String money = investBean.getMoney() == null ? "" : investBean.getMoney();
		String sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + borrowNid + money + platform
				+ couponGrantId + timestamp + accessKey));
		bean.setUserId(userId);
		bean.setTimestamp(timestamp);
		bean.setBorrowNid(borrowNid);
		bean.setPlatform(platform);
		bean.setCouponGrantId(couponGrantId);
		bean.setMoney(money);
		bean.setChkValue(sign);
		return bean;
	}
}
