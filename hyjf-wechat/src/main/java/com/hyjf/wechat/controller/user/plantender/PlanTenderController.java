package com.hyjf.wechat.controller.user.plantender;

import java.math.BigDecimal;

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
import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanDetailCustomize;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.wechat.annotation.SignValidate;
import com.hyjf.wechat.base.BaseController;
import com.hyjf.wechat.base.BaseResultBean;
import com.hyjf.wechat.service.plantender.PlanTenderService;

/**
 * 
 * 计划类出借相关
 * @author sunss
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年2月1日
 * @see 上午10:20:44
 */
@Controller(PlanTenderDefine.CONTROLLER_NAME)
@RequestMapping(value = PlanTenderDefine.REQUEST_MAPPING)
public class PlanTenderController extends BaseController {

    @Autowired
    private PlanTenderService planService;
    
    Logger _log = LoggerFactory.getLogger(PlanTenderController.class);

    /**
     * 获取计划类出借信息、计算预期收益
     * @author sunss
     * @param userName
     * @param password
     * @return
     */
    @SignValidate
    @ResponseBody
    @RequestMapping(value = PlanTenderDefine.DOLOGIN_MAPPING, method = RequestMethod.POST)
    public BaseResultBean getInvestInfo(HttpServletRequest request, HttpServletResponse response) {
    	String borrowNid = request.getParameter("borrowNid");
    	String money = request.getParameter("money");
		String couponId = request.getParameter("couponId");
		Integer userId = requestUtil.getRequestUserId(request);
		String platform = "1"; // 微官网值是 1
		
		_log.info("================HJH borrowNid is " + borrowNid);
		
		InvestInfoResultVo resultVo = new InvestInfoResultVo();
		
		resultVo.setProspectiveEarnings("");
		resultVo.setInterest("");
		resultVo.setStandardValues("0");
		
		_log.info("money is: {}", money);
		if (money == null || "".equals(money) || (new BigDecimal(money).compareTo(BigDecimal.ZERO) == 0)) {
			money = "0";
			resultVo.setRealAmount("");
			resultVo.setButtonWord("确认");
		} else {
			resultVo.setRealAmount("");
			resultVo.setButtonWord("确认出借"+CommonUtils.formatAmount(money)+"元");
		}
		
		if (StringUtils.isNotBlank(money) && new BigDecimal(money).compareTo(BigDecimal.ZERO) > 0) {
			resultVo.setButtonWord("确认加入" + CommonUtils.formatAmount(money) + "元");
		}
        
		BigDecimal couponInterest = BigDecimal.ZERO;
		BigDecimal borrowInterest = new BigDecimal(0);

        // 查询计划信息  传入borrowNid
		resultVo.setStandardValues(CustomConstants.TENDER_THRESHOLD);
        // 根据项目标号获取相应的计划信息
        HjhPlan plan = planService.getPlanByNid(borrowNid);
        resultVo.setBorrowApr(plan.getExpectApr()+"%");
        DebtPlanDetailCustomize planDetail = planService.selectDebtPlanDetail(borrowNid);
        resultVo.setPaymentOfInterest("0" + "元");
        // 获取用户最优优惠券 
        UserCouponConfigCustomize couponConfig = null;
        if (null != planDetail) {
            resultVo.setBorrowNid(borrowNid);
            // -设置  开放额度剩余金额
            String borrowAccountWait = "0";
            if (planDetail.getAvailableInvestAccount() != null) {
                borrowAccountWait =  CommonUtils.formatAmount(planDetail.getAvailableInvestAccount());
            }
            borrowAccountWait = borrowAccountWait.replaceAll(",", "");
            resultVo.setBorrowAccountWait(borrowAccountWait); // 开放额度剩余金额
            String initMoney = "0";
            // -设置 最小出借金额(起投金额)-->计算最后一笔出借
            if (planDetail.getDebtMinInvestment() != null) {
                initMoney = new BigDecimal(planDetail.getDebtMinInvestment()).intValue()+"";
            }
            resultVo.setInitMoney(initMoney);
            // -设置优惠券
            _log.info("HJH couponId is:{}, borrowNid is :{}", couponId, borrowNid);
            if (couponId == null || "".equals(couponId) || couponId.length() == 0) {
                // 不用获取最优优惠券了
                //couponConfig = planService.getUserOptimalCoupon(couponId, borrowNid, userId, money, platform);
            } else {
                // 如果已经有了优惠券  判断优惠券是否可用
				JSONObject userCoupon = planService.getHJHProjectUserCoupon(borrowNid, userId, money,
						platform);
				if (planService.isHjhCouponAvailable(couponId, userCoupon)) {
					couponConfig = planService.getCouponById(couponId);
				}
            }
            _log.info("优惠券信息couponConfig: {}", JSONObject.toJSONString(couponConfig));
            if("-1".equals(couponId)){
                couponConfig = null;
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
            if ((!StringUtils.isBlank(money) && Double.parseDouble(money) >= 0)) {
            	// 这里有个坑，如果计划剩余可投小于用户出借金额，那么计算收益需要用计划剩余可投计算，不能使用用户出借金额计算收益
				_log.info("计划剩余可投: {}", borrowAccountWait);
				_log.info("用户出借金额: {}", money);
//				if (new BigDecimal(borrowAccountWait).compareTo(new BigDecimal(money)) < 0) {
//					_log.info("计划剩余可投小于用户出借金额,收益按照计划剩余可投计算...");
//					earnings = planService.setProspectiveEarnings(resultVo,couponConfig, borrowNid,userId,platform,borrowAccountWait);
//				} else {
					_log.info("计划剩余可投大于用户出借金额,收益按照用户出借金额计算...");
					earnings = planService.setProspectiveEarnings(resultVo,couponConfig, borrowNid,userId,platform,money);
//				}
            }
            _log.info("本金出借计算出的收益是: {}", earnings);
            
            // 设置优惠券
            resultVo.setCapitalInterest("");
            resultVo.setConfirmCouponDescribe("未使用优惠券");
            resultVo.setRealAmount("");
            resultVo.setCouponType("");
            JSONObject counts =  CommonSoaUtils.getUserCouponAvailableCount(borrowNid, userId, money, platform);
            String couponAvailableCount = "0";
            if(Validator.isNotNull(counts) && counts.containsKey("availableCouponListCount")){
                couponAvailableCount = counts.getString("availableCouponListCount");
            }
            if (couponConfig != null) {
                if (couponConfig != null && couponConfig.getId() > 0 && couponConfig.getCouponType() == 1) {
                    resultVo.setCouponDescribe("体验金: " + couponConfig.getCouponQuota() + "元");
                    resultVo.setConfirmCouponDescribe("体验金: " + couponConfig.getCouponQuota() + "元");
                    resultVo.setCouponType("体验金");
                }
                if (couponConfig != null && couponConfig.getId() > 0 && couponConfig.getCouponType() == 2) {
                    resultVo.setCouponDescribe("加息券: " + couponConfig.getCouponQuota() + "%");
                    resultVo.setConfirmCouponDescribe("加息券: " + couponConfig.getCouponQuota() + "%");
                    resultVo.setCouponType("加息券");

                }
                if (couponConfig != null && couponConfig.getId() > 0 && couponConfig.getCouponType() == 3) {
                    resultVo.setCouponDescribe("代金券: " + couponConfig.getCouponQuota() + "元");
                    resultVo.setConfirmCouponDescribe("代金券: " + couponConfig.getCouponQuota() + "元");
                    resultVo.setCouponType("代金券");
                    resultVo.setRealAmount("实际出借 " + CommonUtils.formatAmount(new BigDecimal(money).add(couponConfig.getCouponQuota())) + "元");

                }
                resultVo.setCouponName(couponConfig.getCouponName());
                resultVo.setCouponQuota(couponConfig.getCouponQuota().toString());
                resultVo.setEndTime(couponConfig.getCouponAddTime() + "-" + couponConfig.getEndTime());
                resultVo.setIsThereCoupon("1");
                resultVo.setCouponId(couponConfig.getUserCouponId());
                resultVo.setIsUsedCoupon("1");

				_log.info("开始计算优惠券收益....");
				String calculateIncomeCapital = "";
				if (new BigDecimal(borrowAccountWait).compareTo(new BigDecimal(money)) < 0) {
					_log.info("同样，计划剩余可投小于用户出借金额,收益按照计划剩余可投计算...");
					calculateIncomeCapital = borrowAccountWait;
				} else {
					_log.info("同样，计划剩余可投大于用户出借金额,收益按照用户出借金额计算...");
					calculateIncomeCapital = money;
				}
				_log.info("优惠券金额按照{}计算....", calculateIncomeCapital);
				JSONObject couResult = CommonSoaUtils.getCouponInterest(couponConfig.getUserCouponId() + "",
						borrowNid, calculateIncomeCapital);
                _log.info("优惠券预期收益计算结果couResult: {}", couResult);

                resultVo.setCapitalInterest(couResult.getString("couponInterest") + "元");
                // 优惠券预期收益
                couponInterest = new BigDecimal(couResult.getString("couponInterest"));

                // 预期收益
				borrowInterest = earnings.add(couponInterest);

              	//备注
				resultVo.setDesc("预期年化利率:  "+plan.getExpectApr()+"%      预期收益:  " + borrowInterest+"元");
                resultVo.setProspectiveEarnings(CommonUtils.formatAmount(borrowInterest) + "元");
                resultVo.setInterest(CommonUtils.formatAmount(borrowInterest));
            }else{
                // 没有可用优惠券
                resultVo.setIsThereCoupon("0");
                resultVo.setCouponDescribe("暂无可用");
                resultVo.setCouponName("");
                resultVo.setCouponQuota("");
                resultVo.setEndTime("");
                resultVo.setCouponId("-1");
              
                JSONObject userCoupon = planService.getHJHProjectUserCoupon(borrowNid, userId, money, platform);
				if(!"0".equals(userCoupon.getString("availableCouponListCount"))){
					resultVo.setIsThereCoupon("1");
					resultVo.setCouponDescribe("请选择");
				}else if ("0".equals(userCoupon.getString("availableCouponListCount")) && !"0".equals(userCoupon.getString("notAvailableCouponListCount"))) {
					resultVo.setIsThereCoupon("1");
					resultVo.setCouponDescribe("暂无可用");
				}else {
					resultVo.setIsThereCoupon("0");
					resultVo.setCouponDescribe("无可用");
				}

                resultVo.setDesc("预期年化利率: "+plan.getExpectApr()+"%      预期收益: " + earnings +"元");
              	resultVo.setProspectiveEarnings(earnings + "元");
            }
            // 可用优惠券数量
            resultVo.setCouponAvailableCount(couponAvailableCount);
            resultVo.setConfirmRealAmount("出借金额: " + CommonUtils.formatAmount(money) + "元");
            // -设置 用户余额
            Account account = planService.getAccount(userId);
            BigDecimal balance = account.getBankBalance();
            resultVo.setBalance(CommonUtils.formatAmount(balance));
            // 起投金额
            resultVo.setInitMoney(plan.getMinInvestment().intValue()+"");
            // 递增金额
            resultVo.setIncreaseMoney(plan.getInvestmentIncrement().intValue()+"");
            resultVo.setInvestmentDescription(resultVo.getInitMoney() + "元起投," + resultVo.getIncreaseMoney() + "元递增");
            resultVo.setBorrowAccountWait1(borrowAccountWait);//没有格式化的开放额度剩余金额
            resultVo.setBorrowAccountWait(CommonUtils.formatAmount(borrowAccountWait)); // 开放额度剩余金额
            BigDecimal tmpmoney = balance.subtract(plan.getMinInvestment()).divide(plan.getInvestmentIncrement(), 0, BigDecimal.ROUND_DOWN)
                    .multiply(plan.getInvestmentIncrement()).add(plan.getMinInvestment());
            if (balance.subtract(plan.getMinInvestment()).compareTo(new BigDecimal("0")) < 0) {
                // 可用余额<起投金额 时 investAllMoney 传 -1
                // 全投金额
                resultVo.setInvestAllMoney("-1");
            } else {
                String borrowAccountWaitStr = resultVo.getBorrowAccountWait().replace(",", "");
                if (plan.getMaxInvestment().compareTo(new BigDecimal(borrowAccountWaitStr)) < 0) {
                    if(balance.compareTo(plan.getMaxInvestment())<0){
                        resultVo.setInvestAllMoney(balance + "");
                    }else{
                        resultVo.setInvestAllMoney(plan.getMaxInvestment() + "");
                    }
                } else if (tmpmoney.compareTo(new BigDecimal(borrowAccountWaitStr)) < 0) {
                    // 全投金额
                    if(balance.compareTo(tmpmoney)<0){
                        resultVo.setInvestAllMoney(balance + "");
                    }else{
                        resultVo.setInvestAllMoney(tmpmoney + "");
                    }
                    resultVo.setInvestAllMoney(tmpmoney + "");
                } else {
                    // 全投金额
                    resultVo.setInvestAllMoney(resultVo.getBorrowAccountWait() + "");
                }
            }
            resultVo.setAnnotation("");
            // add by liuyang 神策数据统计 20180823 start
            resultVo.setProjectTag("计划");
            resultVo.setProjectId(borrowNid);
            // 根据计划编号查询计划信息
            DebtPlanDetailCustomize hjhPlan = this.planService.selectDebtPlanDetail(borrowNid);
            // 项目名称
            resultVo.setProjectName(hjhPlan.getPlanName());
            // 项目期限
            resultVo.setProjectDuration(Integer.parseInt(hjhPlan.getPlanPeriod()));
            if ("1".equals(hjhPlan.getIsMonth())){
                resultVo.setDurationUnit("月");
            }else{
                resultVo.setDurationUnit("天");
            }
            // 历史年回报率
            resultVo.setProjectApr(new BigDecimal(hjhPlan.getPlanApr()));
            resultVo.setDiscountApr(BigDecimal.ZERO);
            resultVo.setProjectRepaymentType(hjhPlan.getBorrowStyleName());
            // add by liuyang 神策数据统计 20180823 end
        } else {
			_log.info("=================HJH borrow is null! =============");
            resultVo.setStatusDesc(CustomConstants.APP_STATUS_DESC_FAIL);
        }
    	
        return resultVo;
    }
    
	
    
}
