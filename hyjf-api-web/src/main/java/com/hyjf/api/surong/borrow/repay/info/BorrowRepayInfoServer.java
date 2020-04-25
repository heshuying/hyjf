package com.hyjf.api.surong.borrow.repay.info;

import java.math.BigDecimal;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

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
import com.hyjf.bank.service.user.repay.RepayBean;
import com.hyjf.bank.service.user.repay.RepayService;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

/**
 * 融东风-借款人还款确认及还款相关
 * 
 * @author zhangjinpeng
 *
 */
@Controller
@RequestMapping(BorrowRepayInfoDefine.REQUEST_MAPPING)
public class BorrowRepayInfoServer extends BaseController {

	Logger _log = LoggerFactory.getLogger(BorrowRepayInfoServer.class);

	@Autowired
	BorrowRepayInfoService borrowRepayInfoService;
	
	@Autowired
	RepayService repayService;

	/**
	 * 确认还款信息
	 * 
	 * @param request
	 * @param paramBean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = BorrowRepayInfoDefine.BORROW_REPAY_CONFIRM_MAPPING, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public BorrowRepayInfoResultBean borrowRepayConfirm(HttpServletRequest request,
			BorrowRepayInfoParamBean paramBean) {
		_log.info("---取得标的还款计划开始。。。" + "标的编号：" + paramBean.getBorrowNid());
		RepayBean repayBean = new RepayBean();
		BorrowRepayInfoResultBean resultBean = new BorrowRepayInfoResultBean();
		// 验签
		if (!this.checkSign(paramBean, BaseDefine.METHOD_BORROW_REPAY_CONFIRM)) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("验签失败！");
			return resultBean;
		}
		if (StringUtils.isEmpty(paramBean.getBorrowNid()) || null == paramBean.getUsername()
				|| null == paramBean.getMobile()) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("参数非法！");
			return resultBean;
		}
		Borrow borrow = this.borrowRepayInfoService.getBorrow(paramBean.getBorrowNid());
		if (null == borrow) {
			_log.info("标的不存在！标的编号：" + paramBean.getBorrowNid());
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("标的不存在！");
			return resultBean;
		}

		try {
			Users user = this.borrowRepayInfoService.getUsersByExample(paramBean.getUsername(), paramBean.getMobile());
			Account account = this.borrowRepayInfoService.getUserAccountByExample(user.getUserId());
			resultBean.setBankBalance(account.getBankBalance());
			if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrow.getBorrowStyle())
					|| CustomConstants.BORROW_STYLE_END.equals(borrow.getBorrowStyle())) {
				repayBean = this.repayService.calculateRepay(user.getUserId(), borrow);
			} else {
				repayBean = this.repayService.calculateRepayByTerm(user.getUserId(), borrow);
			}
			// 一次性还款,不分期的标的
			// 按天计息，到期还本还息
			// 按月计息，到期还本还息
			if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrow.getBorrowStyle())
					|| CustomConstants.BORROW_STYLE_END.equals(borrow.getBorrowStyle())) {
				// 总的还款期数
				resultBean.setBorrowPeriod(1);
			}else{
				// 分期的标的
				// 总的还款期数
				resultBean.setBorrowPeriod(Integer.valueOf(borrow.getBorrowPeriod()));
			}
			// 提前还款天数
			resultBean.setChargeDays(repayBean.getChargeDays());
			// 提前还款减息
			resultBean.setChargeInterest(repayBean.getChargeInterest());
			// 逾期天数
			resultBean.setLateDays(repayBean.getLateDays());
			// 逾期还款加息
			resultBean.setLateInterest(repayBean.getLateInterest());
			// 延期还款天数
			resultBean.setDelayDays(repayBean.getDelayDays());
			// 延期还款利息
			resultBean.setDelayInterest(repayBean.getDelayInterest());
			// 还款总额
			//BigDecimal repayAccountAll = repayBean.getRepayAccountAll().add(repayBean.getRepayFee());
			/*if(repayBean.getChargeDays() > 0){
				// 提前还款减息
				repayAccountAll = repayAccountAll.add(repayBean.getChargeInterest());
			}
			if(repayBean.getLateDays() > 0){
				// 逾期还款加息
				repayAccountAll = repayAccountAll.add(repayBean.getLateInterest());
			}
			if(repayBean.getDelayDays() > 0){
				// 延期还款加息
				repayAccountAll = repayAccountAll.add(repayBean.getDelayInterest());
			}*/
			resultBean.setRepayAccountAll(repayBean.getRepayAccountAll());
			// 还款本金
			resultBean.setRepayCapital(repayBean.getRepayCapital());
			// 管理费
			resultBean.setRepayFee(repayBean.getRepayFee());
			// 应还款利息
			resultBean.setRepayInterest(repayBean.getRepayInterest());
			// 应还款时间
			resultBean.setRepayTime(Integer.valueOf(repayBean.getRepayTime()));
			// 当前还款期数 = 总的还款期数 - 剩余还款期数 + 1
			_log.info("剩余还款期数："+repayBean.getRepayPeriod());
			Integer nowPeriod = resultBean.getBorrowPeriod() - repayBean.getRepayPeriod() + 1;
			_log.info("当前还款期数："+nowPeriod);
			// 当前还款期数
			resultBean.setRepayPeriod(nowPeriod);
			// 状态
			resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);

		} catch (Exception e) {
			_log.info("还款信息计算失败，标的编号：" + paramBean.getBorrowNid());
		}
		_log.info("---取得标的还款计划结束。。。" + "标的编号：" + paramBean.getBorrowNid());
		return resultBean;
	}

	/**
	 * 还款是否完成的查询
	 * 
	 * @param request
	 * @param paramBean
	 */
	@ResponseBody
	@RequestMapping(value = BorrowRepayInfoDefine.BORROW_REPAY_SEARCH_MAPPING, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public BorrowApicronResultBean borrowRepaySearch(HttpServletRequest request, BorrowRepayInfoParamBean paramBean) {

		BorrowApicronResultBean resultBean = new BorrowApicronResultBean();

		// 验签
		if (!this.checkSign(paramBean, BaseDefine.METHOD_BORROW_REPAY_SEARCH)) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("验签失败！");
			return resultBean;
		}
		if (StringUtils.isEmpty(paramBean.getBorrowNid())) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("参数非法！");
			return resultBean;
		}
		BorrowApicron apicronBean = this.borrowRepayInfoService.getBorrowApicron(paramBean);
		if (null == apicronBean) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("还款任务数据不存在！");
			return resultBean;
		}
		if (apicronBean.getStatus() == 6) {
			
			BorrowRepay borrowRepay = this.borrowRepayInfoService.getBorrowRepayTimeByStatus(paramBean.getBorrowNid(), 1);
			// 标的最后实际还款时间
			if(null != borrowRepay){
				resultBean.setBorrowRepayYestime(borrowRepay.getRepayYestime());
			}
			resultBean.setRepayStatus(6);
			resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
			resultBean.setStatusDesc("还款成功！");
			_log.info("速融还款成功，还款编号：" + paramBean.getBorrowNid() + "期数：" + paramBean.getRepayPeriod());
		} else {
			// 还款中
			resultBean.setRepayStatus(3);
			resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
			resultBean.setStatusDesc("还款中！");
			_log.info("速融还款中，还款编号：" + paramBean.getBorrowNid() + "期数：" + paramBean.getRepayPeriod());
		}

		return resultBean;
	}

	/**
	 * 用户发起还款
	 * 
	 * @param request
	 * @param paramBean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = BorrowRepayInfoDefine.BORROW_REPAY_MAPPING, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public BorrowRepayInfoResultBean borrowRepay(HttpServletRequest request, BorrowRepayInfoParamBean paramBean) {
		BorrowRepayInfoResultBean resultBean = new BorrowRepayInfoResultBean();
		// 验签
		if (!this.checkSign(paramBean, BaseDefine.METHOD_BORROW_REPAY)) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("验签失败！");
			return resultBean;
		}
		if (StringUtils.isEmpty(paramBean.getBorrowNid()) || null == paramBean.getUsername()
				|| null == paramBean.getMobile()) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("参数非法！");
			return resultBean;
		}
		Users user = this.borrowRepayInfoService.getUsersByExample(paramBean.getUsername(), paramBean.getMobile());
		Integer userId = user.getUserId();
		// 用户角色1出借人2借款人3垫付机构 伟创速融只考虑借款人的情况
		String roleId = "2";
		String username = user.getUsername();
		String borrowNid = paramBean.getBorrowNid();
		Borrow borrow = this.repayService.searchRepayProject(userId, username, roleId, borrowNid);
		// 校验用户/垫付机构的还款
		RepayBean repay = null;

		// 校验借款用户还款
		try {
			JSONObject valid = new JSONObject();
			repay = this.validatorFieldCheckRepay(valid, user, borrow);
			if (StringUtils.equals(BaseResultBean.STATUS_FAIL, valid.getString("validatorStatus"))) {
				resultBean.setStatus(BaseResultBean.STATUS_FAIL);
				resultBean.setStatusDesc(valid.getString("validatorStatusDesc"));
				return resultBean;
			}
			/** redis 锁 */
//			if (StringUtils.isNotEmpty(RedisUtils.get("repay_borrow_nid" + borrowNid))) {
//				resultBean.setStatus(BaseResultBean.STATUS_FAIL);
//				resultBean.setStatusDesc("标的正在还款中");
//				return resultBean;
//			} else {
//				RedisUtils.set("repay_borrow_nid" + borrowNid, borrowNid, 90);
//			}
			
			boolean reslut = RedisUtils.tranactionSet("repay_borrow_nid" + borrowNid, 90);
			// 如果没有设置成功，说明有请求来设置过
			if(!reslut){
				resultBean.setStatus(BaseResultBean.STATUS_FAIL);
				resultBean.setStatusDesc("标的正在还款中");
				return resultBean;
			}
			
		} catch (Exception e1) {
			// 还款异常
			_log.info("用户还款异常！用户名：" + paramBean.getUsername());
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("标的还款系统异常");
		}
		// 获取IP地址
		String ip = GetCilentIP.getIpAddr(request);
		repay.setIp(ip);
		BankCallBean callBackBean = null;
		boolean flag = false;
		try {
			// 冻结账户资金
			callBackBean = this.freezeMoney(user,repay.getRepayAccountAll(),ip,borrowNid);
			String respCode = callBackBean == null ? "" : callBackBean.getRetCode();
			// 申请冻结资金失败
			if (!BankCallConstant.RESPCODE_SUCCESS.equals(respCode)) {
				System.out.println("调用还款申请冻结资金接口失败:" + callBackBean.getRetMsg() + "订单号:" + callBackBean.getOrderId());
			}else{
				// 用户还款
				flag = this.repayService.updateRepayMoney(repay,callBackBean, Integer.valueOf(roleId), userId,user.getUsername(), false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (flag) {
			_log.info("还款成功！用户名：" + paramBean.getUsername());
			resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
			resultBean.setStatusDesc("还款成功");
			// 还款总额
			//BigDecimal repayAccountAll = repay.getRepayAccountAll().add(repay.getRepayFee());
			/*if(repay.getChargeDays() > 0){
				// 提前还款减息(负数)
				repayAccountAll = repayAccountAll.add(repay.getChargeInterest());
			}
			if(repay.getLateDays() > 0){
				// 逾期还款加息
				repayAccountAll = repayAccountAll.add(repay.getLateInterest());
			}
			if(repay.getDelayDays() > 0){
				// 延期还款加息
				repayAccountAll = repayAccountAll.add(repay.getDelayInterest());
			}*/
			// 实际还款总额(包含提前、逾期、延期的情况)
			resultBean.setRepayAccountAll(repay.getRepayAccountAll());
			// 一次性还款,不分期的标的
			// 按天计息，到期还本还息
			// 按月计息，到期还本还息
			if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrow.getBorrowStyle())
					|| CustomConstants.BORROW_STYLE_END.equals(borrow.getBorrowStyle())) {
				// 总的还款期数
				resultBean.setBorrowPeriod(1);
			}else{
				// 分期的标的
				// 总的还款期数
				resultBean.setBorrowPeriod(Integer.valueOf(borrow.getBorrowPeriod()));
			}
			// 当前还款期数
			resultBean.setRepayPeriod(resultBean.getBorrowPeriod() - repay.getRepayPeriod() + 1);
			// 提前还款天数
			resultBean.setChargeDays(repay.getChargeDays());
			// 提前还款减息
			resultBean.setChargeInterest(repay.getChargeInterest());
			// 逾期天数
			resultBean.setLateDays(repay.getLateDays());
			// 逾期还款加息
			resultBean.setLateInterest(repay.getLateInterest());
			// 延期还款天数
			resultBean.setDelayDays(repay.getDelayDays());
			// 延期还款利息
			resultBean.setDelayInterest(repay.getDelayInterest());
			return resultBean;
		}

		_log.info("还款失败！用户名：" + paramBean.getUsername());
		resultBean.setStatus(BaseResultBean.STATUS_FAIL);
		resultBean.setStatusDesc("标的还款系统异常");
		return resultBean;
	}
	
	/**
	 * 用户发起还款，冻结用户相应还款额度的资金
	 * @param user
	 * @param repayAccountAll
	 * @param ip
	 * @param borrowNid 
	 * @param callBackBean
	 * @return
	 * @throws Exception
	 */
	private BankCallBean freezeMoney(Users user,BigDecimal repayAccountAll,String ip, String borrowNid) throws Exception{
		BankOpenAccount bankOpenAccount = this.borrowRepayInfoService.getBankOpenAccount(user.getUserId());
		// 申请还款冻结资金
		// 调用江西银行还款申请冻结资金
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 版本号
		bean.setTxCode(BankCallMethodConstant.TXCODE_BALANCE_FREEZE);// 交易代码
		bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
		bean.setChannel(BankCallConstant.CHANNEL_APP); // 交易渠道
		bean.setAccountId(bankOpenAccount.getAccount());// 电子账号
		bean.setOrderId(GetOrderIdUtils.getOrderId2(user.getUserId())); // 订单号
		bean.setTxAmount(String.valueOf(repayAccountAll));// 交易金额
		bean.setProductId(borrowNid);
        bean.setFrzType("0");
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(user.getUserId()));// 订单号
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		bean.setLogUserId(String.valueOf(user.getUserId()));
		bean.setLogUserName(user.getUsername());
		bean.setLogClient(0);
		bean.setLogIp(ip);
		BankCallBean callBackBean = BankCallUtils.callApiBg(bean);
		
		return callBackBean;
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
	private RepayBean validatorFieldCheckRepay(JSONObject info, Users user, Borrow borrow) throws Exception {
		RepayBean repayByTerm = new RepayBean();
		// 检查用户是否存在
		if (user == null) {
			_log.info("还款用户不存在！");
			info.put("validatorStatus", BaseResultBean.STATUS_FAIL);
			info.put("validatorStatusDesc", "还款用户不存在！");
			return null;
		}

		// 获取用户的账户余额信息
		Account account = this.repayService.searchRepayUserAccount(user.getUserId());
		// 查询用户的账户余额信息
		if (account == null) {
			_log.info("用户账户异常！");
			info.put("validatorStatus", BaseResultBean.STATUS_FAIL);
			info.put("validatorStatusDesc", "用户账户异常！");
			return null;
		}

		// 获取用户的余额
		BigDecimal balance = account.getBankBalance();
		// 获取当前的用户还款的项目
		borrow = this.repayService.searchRepayProject(user.getUserId(), user.getUsername(), "2",
				borrow.getBorrowNid());
		// 判断用户当前还款的项目是否存在
		if (borrow == null) {
			_log.info("还款标的不存在！");
			info.put("validatorStatus", BaseResultBean.STATUS_FAIL);
			info.put("validatorStatusDesc", "还款标的不存在！");
			return null;
		}

		// 获取项目还款方式
		String borrowStyle = StringUtils.isNotEmpty(borrow.getBorrowStyle()) ? borrow.getBorrowStyle() : null;
		// 获取年化利率
		BigDecimal borrowApr = borrow.getBorrowApr();
		// 判断项目的还款方式是否为空
		if (StringUtils.isEmpty(borrowStyle)) {
			_log.info("标的还款方式不能为空！");
			info.put("validatorStatus", BaseResultBean.STATUS_FAIL);
			info.put("validatorStatusDesc", "标的还款方式不能为空！");
			return null;

		}

		// 一次性还款
		if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)
				|| CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
			BigDecimal repayTotal = this.repayService.searchRepayTotal(user.getUserId(), borrow);
			if (repayTotal.compareTo(balance) == 1) {
				_log.info("用户汇盈平台余额不足！");
				info.put("validatorStatus", BaseResultBean.STATUS_FAIL);
				info.put("validatorStatusDesc", "用户汇盈平台余额不足！");
				return null;
			}
			// 用户符合还款条件，可以还款 
			// 为了应对测试 暂时关闭该接口
			BankOpenAccount bankOpenAccount = this.borrowRepayInfoService.getBankOpenAccount(user.getUserId());
			BigDecimal userBalance = this.repayService.getBankBalance(user.getUserId(),bankOpenAccount.getAccount());
			if (repayTotal.compareTo(userBalance) != 1) {
				// 用户符合还款条件，可以还款
				repayByTerm = this.repayService.calculateRepay(user.getUserId(), borrow);
				return repayByTerm;
			} else {
				_log.info("用户在江西银行余额不足！");
				info.put("validatorStatus", BaseResultBean.STATUS_FAIL);
				info.put("validatorStatusDesc", "用户在江西银行余额不足！");
				return null;
			}

		} // 分期还款
		else {
			BigDecimal repayTotal = this.repayService.searchRepayByTermTotal(user.getUserId(), borrow,
					borrowApr, borrowStyle, borrow.getBorrowPeriod());
			if (repayTotal.compareTo(balance) == 1) {
				_log.info("用户汇盈平台余额不足！");
				info.put("validatorStatus", BaseResultBean.STATUS_FAIL);
				info.put("validatorStatusDesc", "用户汇盈平台余额不足！");
				return null;
			}
			// 为了应对测试 暂时关闭该接口
			BankOpenAccount bankOpenAccount = this.borrowRepayInfoService.getBankOpenAccount(user.getUserId());
			BigDecimal userBalance = this.repayService.getBankBalance(user.getUserId(),bankOpenAccount.getAccount());
			if (repayTotal.compareTo(userBalance) == 1) {
				
				_log.info("用户在江西银行余额不足！");
				info.put("validatorStatus", BaseResultBean.STATUS_FAIL);
				info.put("validatorStatusDesc", "用户在江西银行余额不足！");
				return null;
			} else {
				// 用户符合还款条件，可以还款
				repayByTerm = this.repayService.calculateRepayByTerm(user.getUserId(), borrow);
				return repayByTerm;
			}

		}

	}
}
