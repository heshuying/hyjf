package com.hyjf.api.server.user.repay;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyjf.api.server.util.ErrorCodeConstant;
import com.hyjf.api.web.BaseController;
import com.hyjf.bank.service.user.repay.ProjectBean;
import com.hyjf.bank.service.user.repay.ProjectRepayBean;
import com.hyjf.bank.service.user.repay.RepayProjectListBean;
import com.hyjf.bank.service.user.repay.RepayService;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.customize.admin.BatchCenterCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserRepayProjectListCustomize;
import com.hyjf.plan.repay.BatchHjhBorrowRepayApiService;


/**
 * 用户还款
 * @author cwyang
 *
 */
@Controller
@RequestMapping(value = RepayDefine.REQUEST_MAPPING)
public class RepayServer extends BaseController{
	
	Logger _log = LoggerFactory.getLogger(RepayServer.class);
	
	@Autowired
	private RepayService repayService;
	
	@Autowired
	private BatchHjhBorrowRepayApiService  batchBorrowRepayService;
	
	/**
	 * 获得标的还款计划
	 * @param repaybean
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = RepayDefine.REPAY_INFO_ACTION)
	public BaseResultBean getRepayPlanInfo(@RequestBody RepayParamBean repaybean, HttpServletRequest request, HttpServletResponse response){
		UserRepayProjectBean resultBean = new UserRepayProjectBean();
		RepayProjectListBean userRepay = new RepayProjectListBean();
		BankOpenAccount  openAccount = null;
		try {
			openAccount = checkRepayPlanInfo(repaybean);
		} catch (Exception e) {
			resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000002);
			resultBean.setStatusDesc(e.getMessage());
			return resultBean;
		}
		
		//判断查询类型 0:待还款 1:已还款    
		String repayType = repaybean.getRepayType();
		// 用户ID
		if (openAccount != null) {
			String productId = repaybean.getProductId();
			String instCode = repaybean.getInstCode();
			String borrowNid = batchBorrowRepayService.getborrowIdByProductId(productId,instCode);
			if (StringUtils.isBlank(borrowNid)) {
				resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_HK000001);
				resultBean.setStatusDesc("没有查询到对应借款编号!");
				return resultBean;
			}
			userRepay.setUserId(openAccount.getUserId() + "");
			userRepay.setRoleId("0");// 角色为借款人
			userRepay.setStatus(repayType);
			userRepay.setBorrowNid(borrowNid);
			//查询还款列表
			List<WebUserRepayProjectListCustomize> recordList = repayService.searchUserRepayList(userRepay, 0, 0);
			if (recordList != null && recordList.size() > 0) {
				WebUserRepayProjectListCustomize repayInfo = recordList.get(0);
				resultBean.setBorrowNid(repayInfo.getBorrowNid());
				resultBean.setBorrowInterest(repayInfo.getBorrowInterest());
				resultBean.setBorrowAccount(repayInfo.getBorrowAccount());
				resultBean.setYesAccount(repayInfo.getYesAccount());
				resultBean.setYesAccountTime(repayInfo.getYesAccountTime());
				resultBean.setBorrowTotal(repayInfo.getBorrowTotal());
				if ("0".equals(repayType)) {//待还款
					//获取还款计划
					ProjectBean form = new ProjectBean();
					form.setUserId(openAccount.getUserId() + "");
					form.setUsername(openAccount.getUserName());
					form.setRoleId("0");
					form.setBorrowNid(borrowNid);
					// 查询用户的还款详情
					try {
						ProjectBean repayProject = repayService.searchRepayProjectDetail(form,false);
						List<ProjectRepayListBean> list = getRepayDetailList(repayProject.getUserRepayList());
						resultBean.setDetailList(list);
						resultBean.setStatusForResponse(ErrorCodeConstant.SUCCESS);
						resultBean.setStatusDesc("查询成功!");
						return resultBean;
					} catch (Exception e) {
						resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
						resultBean.setStatusDesc("查询对应还款计划异常!");
						return resultBean;
					}
				}else{
					resultBean.setRepayYesTime(repayInfo.getRepayYesTime());
					resultBean.setRepayTotal(repayInfo.getRepayTotal());
					return resultBean;
				}
			}else{
				resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
				resultBean.setStatusDesc("没有查询到对应还款计划!");
				return resultBean;
			}
			
		}
		resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000006);
		resultBean.setStatusDesc("没有用户开户信息!");
		return resultBean;
	}
	

	/**
	 * 获取待还款详细还款信息
	 * @param userRepayList
	 * @return
	 */
	private List<ProjectRepayListBean> getRepayDetailList(List<ProjectRepayBean> userRepayList) {
		List<ProjectRepayListBean> list = new ArrayList<>();
		if (userRepayList != null) {
			for (int i = 0; i < userRepayList.size(); i++) {
				BigDecimal planRepay = new BigDecimal(0);
				ProjectRepayBean resultInfo = userRepayList.get(i);
				BigDecimal capital = new BigDecimal(resultInfo.getRepayCapital());
				BigDecimal interest = new BigDecimal(resultInfo.getRepayInterest());
				BigDecimal detailRepayAccount = capital.add(interest);
				ProjectRepayListBean bean = new ProjectRepayListBean();
				bean.setRepayPeriod(resultInfo.getRepayPeriod());
				bean.setRepayTime(resultInfo.getRepayTime());
				bean.setRepayCapital(resultInfo.getRepayCapital());
				bean.setRepayInterest(resultInfo.getRepayInterest());
				bean.setManageFee(resultInfo.getManageFee());
				bean.setRepayAccount(detailRepayAccount.toString());
				bean.setRepayStatus(resultInfo.getStatus());
				BigDecimal repayAccount = new BigDecimal(bean.getRepayAccount());
				BigDecimal repayManageFee = new BigDecimal(bean.getManageFee());
				planRepay = repayAccount.add(repayManageFee);
				bean.setPlanRepayTotal(planRepay.toString());
				list.add(bean);
			}
		}
		return list;
	}


	/**
	 * 获取还款计划验签
	 * @param repaybean
	 * @return
	 */
	private BankOpenAccount checkRepayPlanInfo(RepayParamBean info) {
		if (StringUtils.isBlank(info.getAccountId()) || StringUtils.isBlank(info.getProductId()) || StringUtils.isBlank(info.getInstCode())) {
			throw new RuntimeException("参数非法,ProductId或accountId或instCode不得为空!");
		}
		//验签
		if(!this.verifyRequestSign(info, BaseDefine.METHOD_REPAY_INFO)){
			throw new RuntimeException("验签失败!");
		}
		BankOpenAccount bankOpenAccount = repayService.getBankOpenAccount(info.getAccountId());
		if (bankOpenAccount == null) {
			throw new RuntimeException("该用户没有在平台开户!");
		}
		return bankOpenAccount;
	}


	/**
	 * 获得还款批次处理结果
	 * @param repaybean
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = RepayDefine.REPAY_RESULT_ACTION)
	public BaseResultBean getRepayResult(@RequestBody RepayParamBean repaybean, HttpServletRequest request, HttpServletResponse response){
		RepayResultBean resultBean = new RepayResultBean();
		try {
			checkRepayResult(repaybean);
		} catch (Exception e) {
			resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000002);
			resultBean.setStatusDesc(e.getMessage());
			return resultBean;
		}
		BatchCenterCustomize batchCenterCustomize = new BatchCenterCustomize();
		// 借款编号 检索条件
	    batchCenterCustomize.setBorrowNid(repaybean.getBorrowNid()); 
		// 出借人 检索条件
	    batchCenterCustomize.setApiType(1);
		Long count = this.batchBorrowRepayService.countBatchCenter(batchCenterCustomize);
		if (count != null && count > 0) {
			List<BatchCenterCustomize> recordList = this.batchBorrowRepayService.selectBatchCenterList(batchCenterCustomize);
			BatchCenterCustomize info = recordList.get(0); 
			resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
			resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
			resultBean.setBorrowNid(info.getBorrowNid());
			resultBean.setBatchNo(info.getBatchNo());
			resultBean.setRole(info.getIsRepayOrgFlag());
			resultBean.setUserName(info.getUserName());
			resultBean.setPeriodNow(info.getPeriodNow());
			resultBean.setBorrowPeriod(info.getBorrowPeriod());
			resultBean.setBorrowAccount(info.getBorrowAccount());
			resultBean.setBatchAmount(info.getBatchAmount());
			resultBean.setBatchServiceFee(info.getBatchServiceFee());
			resultBean.setSucAmount(info.getSucAmount());
			resultBean.setFailAmount(info.getFailAmount());
			resultBean.setBatchStatus(info.getStatus());
			return resultBean;
		}
		resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_HK000002);
		resultBean.setStatusDesc("借款标的没有还款记录!");
		return resultBean;
	}
	
	private BankOpenAccount checkRepayResult(RepayParamBean info) {
		if (StringUtils.isBlank(info.getAccountId()) || StringUtils.isBlank(info.getBorrowNid()) || StringUtils.isBlank(info.getInstCode())) {
			throw new RuntimeException("参数非法,BorrowNid或accountId或instcode不得为空!");
		}
		//验签
		if(!this.verifyRequestSign(info, BaseDefine.METHOD_REPAY_RESULT)){
			throw new RuntimeException("验签失败!");
		}
		BankOpenAccount bankOpenAccount = repayService.getBankOpenAccount(info.getAccountId());
		if (bankOpenAccount == null) {
			throw new RuntimeException("该用户没有在平台开户!");
		}
		return bankOpenAccount;
	}


	/**
	 * 还款申请冻结接口 暂不开放,使用平台的批量还款功能进行还款,如开放,请参照汇计划的还款冻结逻辑修改
	 * @param repaybean
	 * @param request
	 * @param response
	 * @return
	 */
	/*@ResponseBody
	@RequestMapping(value = RepayDefine.REPAY_ACTION)
	public BaseResultBean repay(@RequestBody RepayParamBean repaybean, HttpServletRequest request, HttpServletResponse response){
		BaseResultBean resultBean = new BaseResultBean();
		String accountId = repaybean.getAccountId();
		String borrowNid = repaybean.getBorrowNid();
		_log.info("===================用户开始还款.用户电子账号:" + accountId + ",还款标的:" + borrowNid);
		try {
			//参数校验
			BankOpenAccount  openAccount = checkRepayInfo(repaybean);
			Integer userId = openAccount.getUserId();
			UsersInfo usersInfo = repayService.getUsersInfoByUserId(userId);
			String roleId = usersInfo.getRoleId() + "";
			String userName = openAccount.getUserName();
			Borrow borrow = this.repayService.searchRepayProject(userId, userName, roleId, borrowNid);
			// 校验用户/垫付机构的还款
			RepayBean repay = null;
			if (StringUtils.isNotEmpty(roleId) && "3".equals(roleId)) {// 垫付机构还款校验
				Integer repayUserId = borrow.getUserId();
				repay = this.validatorFieldCheckRepay_org(userId, borrow, repayUserId);
			} else { // 借款人还款校验
				repay = this.validatorFieldCheckRepay(userId, borrow);
			}
			if (repay != null) {
				BigDecimal repayTotal = repay.getRepayAccountAll();
				// 用户还款
				String orderId = GetOrderIdUtils.getOrderId2(userId);
				//插入冻结信息日志表 add by cwyang 2017-07-08
				repayService.insertRepayFreezeLof(userId,orderId,accountId,borrowNid,repayTotal,userName);
				// 申请还款冻结资金
				// 调用江西银行还款申请冻结资金
				BankCallBean bean = new BankCallBean();
				bean.setVersion(BankCallConstant.VERSION_10);// 版本号
				bean.setTxCode(BankCallMethodConstant.TXCODE_BALANCE_FREEZE);// 交易代码
				bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
				bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
				bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
				bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
				bean.setAccountId(accountId);// 电子账号
				bean.setOrderId(orderId); // 订单号
				bean.setTxAmount(String.valueOf(repayTotal));// 交易金额
				bean.setLogOrderId(orderId);// 订单号
				bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
				bean.setLogUserId(String.valueOf(userId));
				bean.setLogUserName(userName);
				bean.setLogClient(0);
				BankCallBean callBackBean = BankCallUtils.callApiBg(bean);
				String respCode = callBackBean == null ? "" : callBackBean.getRetCode();
				// 申请冻结资金失败
				if (!BankCallConstant.RESPCODE_SUCCESS.equals(respCode)) {
					if (!"".equals(respCode)) {
						this.repayService.deleteFreezeTempLogs(orderId);
					}
					_log.error("调用还款申请冻结资金接口失败:" + callBackBean.getRetMsg() + "订单号:" + callBackBean.getOrderId());
					throw new RuntimeException("调用还款申请冻结资金接口失败!");
				}
				boolean flag = this.repayService.updateRepayMoney(repay, callBackBean, Integer.valueOf(roleId), userId, userName);
				if (flag) {
					resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
			        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
			        return resultBean;
				} else {
					throw new RuntimeException("还款失败，请稍后再试！");
				}
			} else {
				throw new RuntimeException("没有对应的还款信息!");
			}
		} catch (Exception e) {
			_log.error("==============" + accountId + "还款失败!失败原因:" + e.getMessage());
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc(e.getMessage());
            return resultBean;
		}
	}*/

	/**
	 * 校验参数
	 * @param accountId
	 * @param borrowNid
	 * @return
	 */
	/*private BankOpenAccount checkRepayInfo(RepayParamBean info) {

		if (StringUtils.isBlank(info.getAccountId()) || StringUtils.isBlank(info.getBorrowNid())) {
			throw new RuntimeException("参数非法,borrowNid或accountId不得为空!");
		}
		//验签
//		if(!this.checkSign(info, BaseDefine.METHOD_REPAY)){
//			throw new RuntimeException("验签失败!");
//		}
		BankOpenAccount bankOpenAccount = repayService.getBankOpenAccount(info.getAccountId());
		if (bankOpenAccount == null) {
			throw new RuntimeException("该用户没有在平台开户!");
		}
		return bankOpenAccount;
	}
	
	*//**
	 * 校验用户还款信息(计算结果形成中间结果值)
	 * 
	 * @param info
	 * @param userId
	 * @param password
	 * @param borrowNid
	 * @throws ParseException
	 *//*
	private RepayBean validatorFieldCheckRepay(int userId, Borrow borrow) throws ParseException {
		// 获取当前用户
		Users user = this.repayService.searchRepayUser(userId);
		// 检查用户是否存在
		if (user == null) {
			throw new RuntimeException("用户不存在!");
		}
		// 获取用户的账户余额信息
		Account account = this.repayService.searchRepayUserAccount(userId);
		// 查询用户的账户余额信息
		if (account == null) {
			throw new RuntimeException("用户账户信息不存在!");
		}
		// 获取用户的余额
		BigDecimal balance = account.getBankBalance();
		// 获取当前的用户还款的项目
		borrow = this.repayService.searchRepayProject(userId, user.getUsername(), "2", borrow.getBorrowNid());
		// 判断用户当前还款的项目是否存在
		if (borrow == null) {
			throw new RuntimeException("用户还款项目不存在!");
		}
		// 获取项目还款方式
		String borrowStyle = StringUtils.isNotEmpty(borrow.getBorrowStyle()) ? borrow.getBorrowStyle() : null;
		// 获取年化利率
		BigDecimal borrowApr = borrow.getBorrowApr();
		// 判断项目的还款方式是否为空
		if (StringUtils.isEmpty(borrowStyle)) {
			throw new RuntimeException("用户还款方式为空!");
		}
		// 一次性还款
		if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle) || CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
			BigDecimal repayTotal = this.repayService.searchRepayTotal(userId, borrow);
			if (repayTotal.compareTo(balance) == 0 || repayTotal.compareTo(balance) == -1) {
				// ** 用户符合还款条件，可以还款 *//*
				// 查询用户在银行电子账户的余额
				BankOpenAccount accountChinapnr = this.repayService.getBankOpenAccount(userId);
				BigDecimal userBankBalance = this.repayService.getBankBalance(userId, accountChinapnr.getAccount());
				if (repayTotal.compareTo(userBankBalance) == 0 || repayTotal.compareTo(userBankBalance) == -1) {
					// ** 用户符合还款条件，可以还款 *//*
					RepayBean repayByTerm = this.repayService.calculateRepay(userId, borrow);
					return repayByTerm;
				} else {
					throw new RuntimeException("用户余额不足!");
				}
			} else {
				throw new RuntimeException("用户余额不足!");
			}
		} // 分期还款
		else {
			BigDecimal repayTotal = this.repayService.searchRepayByTermTotal(userId, borrow, borrowApr, borrowStyle, borrow.getBorrowPeriod());
			if (repayTotal.compareTo(balance) == 0 || repayTotal.compareTo(balance) == -1) {
				BankOpenAccount accountChinapnr = this.repayService.getBankOpenAccount(userId);
				// 查询用户在银行电子账户的可用余额
				BigDecimal userBalance = this.repayService.getBankBalance(userId, accountChinapnr.getAccount());
				if (repayTotal.compareTo(userBalance) == 0 || repayTotal.compareTo(userBalance) == -1) {
					// ** 用户符合还款条件，可以还款 *//*
					RepayBean repayByTerm = this.repayService.calculateRepayByTerm(userId, borrow);
					return repayByTerm;
				} else {
					throw new RuntimeException("用户余额不足!");
				}
			} else {
				throw new RuntimeException("用户余额不足!");
			}
		}
	}

	*//**
	 * 校验垫付机构还款信息(计算结果形成中间结果值)
	 * 
	 * @param info
	 * @param userId
	 *            垫付机构id
	 * @param password
	 * @param borrowNid
	 * @param repayUserId
	 *            借款人id
	 * @throws ParseException
	 *//*
	private RepayBean validatorFieldCheckRepay_org(int userId, Borrow borrow, Integer repayUserId) throws ParseException {
		// 获取当前垫付机构
		Users user = this.repayService.searchRepayUser(userId);
		// 检查垫付机构是否存在
		if (user == null) {
			throw new RuntimeException("用户不存在!");
		}
		// 获取垫付机构的账户余额信息
		Account account = this.repayService.searchRepayUserAccount(userId);
		// 查询垫付机构的账户余额信息
		if (account == null) {
			throw new RuntimeException("用户账户信息不存在!");
		}
		// 获取用户在平台的账户余额
		BigDecimal balance = account.getBankBalance();
		// 获取当前垫付机构要还款的项目
		borrow = this.repayService.searchRepayProject(repayUserId, null, null, borrow.getBorrowNid());
		// 判断用户当前还款的项目是否存在
		if (borrow == null) {
			throw new RuntimeException("用户还款项目不存在!");
		}
		// 获取项目还款方式
		String borrowStyle = StringUtils.isNotEmpty(borrow.getBorrowStyle()) ? borrow.getBorrowStyle() : null;
		// 获取年化利率
		BigDecimal borrowApr = borrow.getBorrowApr();
		// 判断项目的还款方式是否为空
		if (StringUtils.isEmpty(borrowStyle)) {
			throw new RuntimeException("用户还款方式为空!");
		}
		// 一次性还款
		if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle) || CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
			BigDecimal repayTotal = this.repayService.searchRepayTotal(repayUserId, borrow);
			if (repayTotal.compareTo(balance) == 0 || repayTotal.compareTo(balance) == -1) {
				// ** 垫付机构符合还款条件，可以还款 *//*
				// 查询用户在银行的电子账户
				BankOpenAccount bankOpenAccount = this.repayService.getBankOpenAccount(userId);
				// 获取用户在银行的电子账户余额
				BigDecimal userBankBalance = this.repayService.getBankBalance(userId, bankOpenAccount.getAccount());
				if (repayTotal.compareTo(userBankBalance) == 0 || repayTotal.compareTo(userBankBalance) == -1) {
					// ** 垫付机构符合还款条件，可以还款 *//*
					RepayBean repayByTerm = this.repayService.calculateRepay(repayUserId, borrow);
					repayByTerm.setRepayUserId(userId);// 垫付机构id
					return repayByTerm;
				} else {
					throw new RuntimeException("用户余额不足!");
				}
			} else {
				throw new RuntimeException("用户余额不足!");
			}
		} // 分期还款
		else {
			BigDecimal repayTotal = this.repayService.searchRepayByTermTotal(repayUserId, borrow, borrowApr, borrowStyle, borrow.getBorrowPeriod());
			if (repayTotal.compareTo(balance) == 0 || repayTotal.compareTo(balance) == -1) {
				// ** 垫付机构符合还款条件，可以还款 *//*
				// 查询用户在银行的电子账户
				BankOpenAccount bankOpenAccount = this.repayService.getBankOpenAccount(userId);
				// 获取用户在银行的电子账户余额
				BigDecimal userBankBalance = this.repayService.getBankBalance(userId, bankOpenAccount.getAccount());
				if (repayTotal.compareTo(userBankBalance) == 0 || repayTotal.compareTo(userBankBalance) == -1) {
					// ** 用户符合还款条件，可以还款 *//*
					RepayBean repayByTerm = this.repayService.calculateRepayByTerm(repayUserId, borrow);
					repayByTerm.setRepayUserId(userId);// 垫付机构id
					return repayByTerm;
				} else {
					throw new RuntimeException("用户余额不足!");
				}
			} else {
				throw new RuntimeException("用户余额不足!");
			}
		}
	}*/
}
