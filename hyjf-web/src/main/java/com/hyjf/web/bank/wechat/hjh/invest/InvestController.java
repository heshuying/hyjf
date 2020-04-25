package com.hyjf.web.bank.wechat.hjh.invest;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanDetailCustomize;
import com.hyjf.web.BaseController;

@Controller("hjhInvestController")
@RequestMapping(value = InvestDefine.REQUEST_MAPPING)
public class InvestController extends BaseController {

	@Autowired
	private InvestService investService;


	@Autowired
    private PlanService planService;
	private static String HOST = PropUtils.getSystem("hyjf.web.host").trim();

	/**
	 * 
	 * 根据出借项目id获取出借信息
	 * 
	 * @author sss
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = InvestDefine.GET_INVEST_INFO_MAPPING)
	public Object getInvestInfo(HttpServletRequest request, HttpServletResponse response) {

		String borrowNid = request.getParameter("borrowNid");
		String sign = request.getParameter("sign");
		String money = request.getParameter("money");
		String version = request.getParameter("version");
		String platform = request.getParameter("platform");
		String couponId = request.getParameter("couponId");
		Integer userId = Integer.parseInt(request.getParameter("userId"));
		// liuyang 汇转让追加 start
		// 出借类型
		String investType = borrowNid.substring(0, 3);
		// 债转编号
		String creditNid = borrowNid.substring(3);
		// liuyang 汇转让 追加 end

		DecimalFormat df = null;
		if (StringUtils.contains(version, CustomConstants.APP_VERSION_NUM)) {
			df = CustomConstants.DF_FOR_VIEW_V1;
		} else {
			df = CustomConstants.DF_FOR_VIEW;
		}
		InvestInfoResultVo resultVo = new InvestInfoResultVo();
		resultVo.setProspectiveEarnings("");
		resultVo.setInterest("");
		if (money == null || "".equals(money)) {
			money = "0";
			resultVo.setRealAmount("");
		} else {
			resultVo.setRealAmount("");
		}
		System.out.println("=============cwyang 跳转出借详情 money is " + money);
		System.out.println("=============cwyang 跳转出借详情 creditNid is " + creditNid);
		if (!money.matches("^[-+]?(([0-9]+)(([0-9]+))?|(([0-9]+))?)$") || !Validator.isNumber(creditNid)) {
			resultVo.setStatusDesc(CustomConstants.APP_STATUS_DESC_FAIL);
		} else {
			// 查询用户可用余额
			BigDecimal couponInterest = BigDecimal.ZERO;
			BigDecimal borrowInterest = new BigDecimal(0);
			// 汇计划 新增 by sunss    borrowAccountWait：可投金额或开放额度        balance：可用余额或可用金额
            if ("HJH".equals(investType)) {

                System.out.println("================HJH borrowNid is " + borrowNid);
                // 查询计划信息  传入borrowNid
                // 根据项目标号获取相应的计划信息
                HjhPlan plan = planService.getPlanByNid(borrowNid);
                DebtPlanDetailCustomize planDetail = planService.selectDebtPlanDetail(borrowNid);
                
                // 获取用户最优优惠券 
                UserCouponConfigCustomize couponConfig = null;
                if (null != planDetail) {
                    resultVo.setBorrowNid(borrowNid);
                    // -设置  开放额度剩余金额
                    String borrowAccountWait = "0";
                    if (planDetail.getAvailableInvestAccount() != null) {
                        borrowAccountWait =  df.format(new BigDecimal(planDetail.getAvailableInvestAccount()));
                    }
                    resultVo.setBorrowAccountWait(borrowAccountWait); // 开放额度剩余金额
                    borrowAccountWait = borrowAccountWait.replaceAll(",", "");
                    String initMoney = "0";
                    // -设置 最小出借金额(起投金额)-->计算最后一笔出借
                    if (planDetail.getDebtMinInvestment() != null) {
                        initMoney = new BigDecimal(planDetail.getDebtMinInvestment()).intValue()+"";
                    }
                    resultVo.setInitMoney(initMoney);
                    // -设置优惠券
                    couponConfig = new UserCouponConfigCustomize();
                    if (couponId == null || "".equals(couponId) || couponId.length() == 0) {
                        // 获取用户最优优惠券 ???
                        // 最后一个参数 平台  优惠券的使用平台0:全部，1：PC，2：微官网，3：Android，4：IOS
                        couponConfig = planService.getUserOptimalCoupon(couponId, borrowNid, userId, null, platform);
                    } else {
                        // 如果已经有了优惠券  就查询现有的
                        couponConfig = getBestCouponById(couponId);
                    }
                    
                    // 刚加载页面并且可投小于起投
                    if ((StringUtils.isBlank(money) || money.equals("0")) && new BigDecimal(borrowAccountWait).compareTo(new BigDecimal(planDetail.getDebtMinInvestment())) < 1) {
                        money = new BigDecimal(borrowAccountWait).intValue() + "";
                    }
                    if (money.contains(",")) {
                        money = money.replace(",", "");
                    }
                    
                    BigDecimal earnings = new BigDecimal("0");
                    // 计算收益
                    if ((!StringUtils.isBlank(money) && Double.parseDouble(money) >= 0) || (couponConfig != null && (couponConfig.getCouponType() == 1 || couponConfig.getCouponType() == 3))) {
                        planService.setProspectiveEarnings(resultVo,couponConfig, borrowNid,userId,platform,money);
                    }
                    
                    // 设置优惠券
                    resultVo.setCapitalInterest("历史回报：0元");
                    resultVo.setConfirmCouponDescribe("加息券： 无");
                    resultVo.setRealAmount("");
                    resultVo.setCouponType("");
                    if (couponConfig != null) {
                        if (couponConfig != null && couponConfig.getId() > 0 && couponConfig.getCouponType() == 1) {
                            resultVo.setCouponDescribe("体验金： " + couponConfig.getCouponQuota() + "元");
                            resultVo.setConfirmCouponDescribe("体验金： " + couponConfig.getCouponQuota() + "元");
                            resultVo.setCouponType("体验金");
                        }
                        if (couponConfig != null && couponConfig.getId() > 0 && couponConfig.getCouponType() == 2) {
                            resultVo.setCouponDescribe("加息券： " + couponConfig.getCouponQuota() + "%");
                            resultVo.setConfirmCouponDescribe("加息券： " + couponConfig.getCouponQuota() + "%");
                            resultVo.setCouponType("加息券");

                        }
                        if (couponConfig != null && couponConfig.getId() > 0 && couponConfig.getCouponType() == 3) {
                            resultVo.setCouponDescribe("代金券： " + couponConfig.getCouponQuota() + "元");
                            resultVo.setConfirmCouponDescribe("代金券： " + couponConfig.getCouponQuota() + "元");
                            resultVo.setCouponType("代金券");
                            resultVo.setRealAmount("实际出借 " + df.format(new BigDecimal(money).add(couponConfig.getCouponQuota())) + "元");

                        }
                        resultVo.setCouponName(couponConfig.getCouponName());
                        resultVo.setCouponQuota(couponConfig.getCouponQuota().toString());
                        resultVo.setEndTime(couponConfig.getCouponAddTime() + "-" + couponConfig.getEndTime());
                        resultVo.setIsThereCoupon("1");
                        resultVo.setCouponId(couponConfig.getUserCouponId());

                        resultVo.setIsUsedCoupon("1");
                        resultVo.setCapitalInterest("历史回报：" + df.format(couponInterest) + "元");
                    }else{
                        // 没有可用优惠券
                        resultVo.setIsThereCoupon("0");
                        resultVo.setCouponDescribe("暂无可用");
                        resultVo.setCouponName("");
                        resultVo.setCouponQuota("");
                        resultVo.setEndTime("");
                        resultVo.setCouponId(couponId == null ? "" : couponId);
                        resultVo.setIsUsedCoupon(couponId == null ? "" : "1");
                    }
                    
                    resultVo.setConfirmRealAmount("出借金额：" + df.format(new BigDecimal(money)) + "元");
                    resultVo.setBorrowInterest("历史回报：" + df.format(borrowInterest) + "元");
                    
                    // -设置 用户余额
                    Account account = investService.getAccount(userId);
                    BigDecimal balance = account.getBankBalance();
                    resultVo.setBalance(df.format(balance));
                    resultVo.setStatus(CustomConstants.APP_STATUS_SUCCESS);
                    resultVo.setStatusDesc(CustomConstants.APP_STATUS_DESC_SUCCESS);
                    // 起投金额
                    resultVo.setInitMoney(df.format(plan.getMinInvestment()));
                    // 递增金额
                    resultVo.setIncreaseMoney(df.format(plan.getInvestmentIncrement()));
                    resultVo.setInvestmentDescription(resultVo.getInitMoney() + "元起投," + resultVo.getIncreaseMoney() + "元递增");
                   
                    BigDecimal tmpmoney = balance.subtract(plan.getMinInvestment()).divide(plan.getInvestmentIncrement(), 0, BigDecimal.ROUND_DOWN)
                            .multiply(plan.getInvestmentIncrement()).add(plan.getMinInvestment());
                    
                    if (balance.subtract(plan.getMinInvestment()).compareTo(new BigDecimal("0")) < 0) {
                        // 可用余额<起投金额 时 investAllMoney 传 -1
                        // 全投金额
                        resultVo.setInvestAllMoney("-1");
                    } else {
                        String borrowAccountWaitStr = resultVo.getBorrowAccountWait().replace(",", "");
                        if (plan.getMaxInvestment().compareTo(new BigDecimal(borrowAccountWaitStr)) < 0) {
                            resultVo.setInvestAllMoney(plan.getMaxInvestment() + "");
                        } else if (tmpmoney.compareTo(new BigDecimal(borrowAccountWaitStr)) < 0) {
                            // 全投金额
                            resultVo.setInvestAllMoney(tmpmoney + "");
                        } else {
                            // 全投金额
                            resultVo.setInvestAllMoney(resultVo.getBorrowAccountWait() + "");
                        }
                    }
                } else {
                    System.out.println("=================HJH borrow is null! =============");
                    resultVo.setStatusDesc(CustomConstants.APP_STATUS_DESC_FAIL);
                }
            }// if end
		}
		return resultVo;
	}

	/**
	 * 
	 * 获取出借url
	 * 
	 * @author 王坤
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = InvestDefine.TENDER_URL_ACTION)
	public JSONObject getTenderUrl(@ModelAttribute AppTenderVo vo, HttpServletRequest request, HttpServletResponse response) {
		JSONObject info = new JSONObject();
		// add by zhangjp 优惠券出借 start
		// 优惠券用户id （coupon_user的id）
		String couponGrantId = request.getParameter("couponGrantId");
		// add by zhangjp 优惠券出借 end
		// add by liuyang 债转出借 start
		String borrowNid = request.getParameter("borrowNid");
		Integer userId = Integer.parseInt(request.getParameter("userId"));
		String investType = borrowNid.substring(0, 3);
		// add by liuyang 债转出借 end
		if ((Validator.isNull(vo.getAccount()) && StringUtils.isEmpty(couponGrantId)) || Validator.isNull(vo.getBorrowNid())) {
			info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
			info.put(CustomConstants.APP_STATUS_DESC, "请求参数非法");
		} else {// 拼接充值地址并返回
			// 唯一标识
			String sign = request.getParameter("sign");
			// 出借平台
			String platform = request.getParameter("platform");
			// 用户id
			// 校验相应的出借参数
			// modify by zhangjp 优惠券相关 start
			JSONObject result = null;
			int couponOldTime = Integer.MIN_VALUE;
			CouponConfigCustomizeV2 cuc = null;
			if (StringUtils.isNotEmpty(couponGrantId) && new Integer(couponGrantId) > 0) {
				cuc = investService.getCouponUser(couponGrantId, userId);
				// 排他check用
				couponOldTime = cuc.getUserUpdateTime();
			}
			else if("HJH".equals(investType)){
			    // 2017-11-08 by sunss  汇计划验证判断
			    // 先进行check  然后 进行出借
			    // check start
			    result  = planService.checkHJHParam(vo.getBorrowNid(), vo.getAccount(), String.valueOf(userId), platform, cuc);
			    return result;
			}
		}
		System.out.println("==============cwyang getTenderUREL 返回结果 " + info);
		return info;
	}

	/**
	 * 获取最优优惠券
	 * 
	 * @param couponId
	 * @return
	 */
	private UserCouponConfigCustomize getBestCouponById(String couponId) {
		return investService.getBestCouponById(couponId);
	}
	
	/**
	 * 
	 * 立即出借
	 * @author sss
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = InvestDefine.INVEST_ACTION)
    public ModelAndView tender(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(InvestController.class.toString(), InvestDefine.INVEST_ACTION);
        System.out.println("开始调用出借");
        ModelAndView modelAndView = new ModelAndView();
        // 唯一标识
        String sign = request.getParameter("sign");
        
        String token = request.getParameter("token");
        // 项目id
        String borrowNid = request.getParameter("borrowNid");
        System.out.println(borrowNid);
        // 出借金额
        String account = request.getParameter("account");
        // 出借平台
        String platform = request.getParameter("platform");
        // 用户id
        Integer userId = Integer.parseInt(request.getParameter("userId"));
        // add by liuyang start
        // 出借类型
        String investType = request.getParameter("investType");
        // add by zhangjp 优惠券出借 start
        String couponGrantId = request.getParameter("couponGrantId");
        // 汇计划出借
        if ("HJH".equals(investType)) {
         // check 通过了  就 出借
            // check 通过了  就 出借
            JSONObject result = new JSONObject();
            CouponConfigCustomizeV2 cuc = null;
            if (StringUtils.isNotEmpty(couponGrantId) && new Integer(couponGrantId) > 0) {
                cuc = investService.getCouponUser(couponGrantId, userId);
            }
            result = planService.investInfo(modelAndView,borrowNid, account, String.valueOf(userId), platform,cuc,GetCilentIP.getIpAddr(request),couponGrantId);
            System.out.println("请求出借返回结果："+result);
            if(result!=null&&result.get(CustomConstants.APP_STATUS).equals(CustomConstants.APP_STATUS_SUCCESS)){
                System.out.println("出借成功页面----2");
                // 成功
                modelAndView.setViewName("/hjh/hjhInvestSuccess");
            }else{
                modelAndView = new ModelAndView("/hjh/hjhInvestError");
                modelAndView.addObject(CustomConstants.APP_STATUS_DESC, result.get(CustomConstants.APP_STATUS_DESC));
                return modelAndView;
            }
        }
        return modelAndView;
    }

}
