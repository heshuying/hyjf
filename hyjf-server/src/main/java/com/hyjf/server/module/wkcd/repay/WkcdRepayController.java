package com.hyjf.server.module.wkcd.repay;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.validator.ValidatorCheckUtil;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.server.BaseController;

@Controller
@RequestMapping(value = WkcdRepayDefine.REQUEST_MAPPING)
public class WkcdRepayController extends BaseController {
	@Autowired
	private WkcdRepayService repayService;
    
	/**
	 * 用户还款
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value=WkcdRepayDefine.USER_REPAY_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public JSONObject repayAction(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject ret = new JSONObject();
		//获取明文数据
		String requestObjectMingwen = request.getAttribute("requestObject").toString();
		if(StringUtils.isEmpty(requestObjectMingwen)){
			ret.put("status", "1");
	        ret.put("statusDesc", "接口调用失败,参数为空");
	        return ret;
		}
        Map<String, String> map = parseRequestJson(requestObjectMingwen);
        String userid= map.get("userId");
        String borrowNid= map.get("borrowNid");
        if (StringUtils.isBlank(userid) || StringUtils.isBlank(borrowNid)) {
        	ret.put("status", "1");
	        ret.put("statusDesc", "接口调用失败,参数缺失");
	        return ret;
        }
     // 校验用户的还款
        Integer userId= Integer.valueOf(userid);
        JSONObject info = new JSONObject();
        RepayByTermBean repay = this.validatorFieldCheckRepay(info, userId, borrowNid);
        if (!ValidatorCheckUtil.hasValidateError(info) && repay.getObjectFlag() != null && repay.getObjectFlag()) {
        	// 生成订单
            String ip = GetCilentIP.getIpAddr(request);
            // 用户还款
            repay.setIp(ip);
            try {
                Map<String, Object> flag = this.repayService.updateRepayMoney(repay);
                if (flag.get("success")!=null && (Boolean)flag.get("success")) {
                    Borrow borrow = this.repayService.searchRepayProject(userId, borrowNid);
                    ret.put("borrowName", borrow.getName());
                    ret.put("status", "0");
                    ret.put("statusDesc","接口调用成功，还款成功");
                    return ret;
                }else{
                    Borrow borrow = this.repayService.searchRepayProject(userId, borrowNid);
                    ret.put("borrowName", borrow.getName());
                    ret.put("status", "0");
                    ret.put("statusDesc",flag.get("msg"));
                    return ret;
                }
            } catch (Exception e) {
                ret.put("status", "1");
                ret.put("statusDesc", "还款失败，请稍后再试！");
            }
        }else{
            ret.put("status", "1");
            ret.put("statusDesc", "还款失败,"+repay.getObjectResult());
        }
        LogUtil.endLog(WkcdRepayDefine.THIS_CLASS, WkcdRepayDefine.USER_REPAY_ACTION);
        return ret;
	} 
	
    /**
     * 校验用户还款信息(计算结果形成中间结果值)
     * 
     * @param info
     * @param userId
     * @param password
     * @param borrowNid
     * @throws ParseException
     */
    private RepayByTermBean validatorFieldCheckRepay(JSONObject info, int userId, String borrowNid)
        throws ParseException {
        RepayByTermBean repayByTerm = new RepayByTermBean();
        // 获取当前用户
        Users user = this.repayService.getUsers(userId);
        // 检查用户是否存在
        if (user != null) {
        	 // 获取用户的账户余额信息
            Account account = this.repayService.getAccount(userId);
            // 查询用户的账户余额信息
            if (account != null) {
                // 获取用户的余额
                BigDecimal balance = account.getBalance();
                // 获取当前的用户还款的项目
                Borrow borrow = this.repayService.searchRepayProject(userId, borrowNid);
                // 判断用户当前还款的项目是否存在
                if (borrow != null) {
                	 // 获取项目还款方式
                    String borrowStyle =
                            StringUtils.isNotEmpty(borrow.getBorrowStyle()) ? borrow.getBorrowStyle() : null;
                    // 获取年化利率
                    BigDecimal borrowApr = borrow.getBorrowApr();
                    // 判断项目的还款方式是否为空
                    if (StringUtils.isNotEmpty(borrowStyle)) {
                        if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)
                                || CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
                            BigDecimal repayTotal =
                                    this.repayService.searchRepayTotal(userId, borrowNid, borrowApr, borrowStyle,
                                            borrow.getBorrowPeriod());
                            if (repayTotal.compareTo(balance) == 0 || repayTotal.compareTo(balance) == -1) {
                                // ** 用户符合还款条件，可以还款 *//*
                                AccountChinapnr accountChinapnr = this.repayService.getChinapnrUserInfo(userId);
                                BigDecimal userBalance =
                                        this.repayService.getUserBalance(accountChinapnr.getChinapnrUsrcustid());
                                if (repayTotal.compareTo(userBalance) == 0
                                        || repayTotal.compareTo(userBalance) == -1) {
                                    // ** 用户符合还款条件，可以还款 *//*
                                    repayByTerm =
                                            this.repayService.calculateRepay(userId, borrowNid, borrowApr,
                                                    borrowStyle, borrow.getBorrowPeriod());
                                    repayByTerm.setObjectFlag(true);
                                    return repayByTerm;
                                } else {
                                    repayByTerm.setObjectFlag(false);
                                    repayByTerm.setObjectResult("用户余额不符合还款条件");
                                    return repayByTerm;
                                }
                            } else {
                                repayByTerm.setObjectFlag(false);
                                repayByTerm.setObjectResult("用户余额不符合还款条件");
                                return repayByTerm;
                            }
                        } else {
                            BigDecimal repayTotal =
                                    this.repayService.searchRepayByTermTotal(userId, borrowNid, borrowApr,
                                            borrowStyle, borrow.getBorrowPeriod());
                            if (repayTotal.compareTo(balance) == 0 || repayTotal.compareTo(balance) == -1) {
                                AccountChinapnr accountChinapnr = this.repayService.getChinapnrUserInfo(userId);
                                BigDecimal userBalance =
                                        this.repayService.getUserBalance(accountChinapnr.getChinapnrUsrcustid());
                                if (repayTotal.compareTo(userBalance) == 0
                                        || repayTotal.compareTo(userBalance) == -1) {
                                    // ** 用户符合还款条件，可以还款 *//*
                                    repayByTerm =
                                            this.repayService.calculateRepayByTerm(userId, borrowNid, borrowApr,
                                                    borrowStyle, borrow.getBorrowPeriod());
                                    repayByTerm.setObjectFlag(true);
                                    return repayByTerm;
                                } else {
                                    repayByTerm.setObjectFlag(false);
                                    repayByTerm.setObjectResult("用户余额不符合还款条件");
                                    return repayByTerm;
                                }
                            } else {
                                repayByTerm.setObjectFlag(false);
                                repayByTerm.setObjectResult("用户余额不符合还款条件");
                                return repayByTerm;
                            }
                        }
                    }else{
                        repayByTerm.setObjectFlag(false);
                        repayByTerm.setObjectResult("还款方式不能为空");
                        return repayByTerm;
                    }
                }else{
                    repayByTerm.setObjectFlag(false);
                    repayByTerm.setObjectResult("获取不到标的信息");
                    return repayByTerm;
                }
            }else{
                repayByTerm.setObjectFlag(false);
                repayByTerm.setObjectResult("获取不到账户信息");
                return repayByTerm;
            }
        }else{
            repayByTerm.setObjectFlag(false);
            repayByTerm.setObjectResult("获取不到用户信息");
            return repayByTerm;
        }
    } 
}





