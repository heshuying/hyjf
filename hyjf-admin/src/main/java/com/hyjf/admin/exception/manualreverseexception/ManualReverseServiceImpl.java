package com.hyjf.admin.exception.manualreverseexception;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.maintenance.admin.AdminDefine;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.customize.ManualReverseCustomize;

/**
 * 手动冲正Service实现类
 * 
 * @author PC-LIUSHOUYI
 */
@Service
public class ManualReverseServiceImpl extends BaseServiceImpl implements ManualReverseService{

	/**
	 * 检索手动冲正数量
	 * @param form
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public int countManualReverse(ManualReverseBean form) {
		Map<String, Object> param = new HashMap<String, Object>();
		// 原交易流水号
		if (StringUtils.isNotEmpty(form.getSeqNoSrch())) {
			param.put("seqNoSrch", form.getSeqNoSrch());
		}
		// 用户名
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			param.put("userNameSrch", form.getUserNameSrch());
		}
		// 电子账号
		if (StringUtils.isNotEmpty(form.getAccountIdSrch())) {
			param.put("accountIdSrch", form.getAccountIdSrch());
		}
		// 交易时间开始
		if (StringUtils.isNotEmpty(form.getTxTimeStartSrch())) {
			param.put("txTimeStartSrch", form.getTxTimeStartSrch());
		}
		// 交易时间结束
		if (StringUtils.isNotEmpty(form.getTxTimeEndSrch())) {
			param.put("txTimeEndSrch", form.getTxTimeEndSrch());
		}
		int count = this.manualReverseCustomizeMapper.countManualReverse(param);
		return count;
	}

	/**
	 * 获取手动冲正数据
	 * @param form
	 * @param offset
	 * @param limit
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public List<ManualReverseCustomize> selectManualReverseList(ManualReverseBean form,  int limitStart, int limitEnd) {

		Map<String, Object> param = new HashMap<String, Object>();
		// 原交易流水号
		if (StringUtils.isNotEmpty(form.getSeqNoSrch())) {
			param.put("seqNoSrch", form.getSeqNoSrch());
		}
		// 用户名
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			param.put("userNameSrch", form.getUserNameSrch());
		}
		// 电子账号
		if (StringUtils.isNotEmpty(form.getAccountIdSrch())) {
			param.put("accountIdSrch", form.getAccountIdSrch());
		}
		// 交易时间开始
		if (StringUtils.isNotEmpty(form.getTxTimeStartSrch())) {
			param.put("txTimeStartSrch", form.getTxTimeStartSrch());
		}
		// 交易时间结束
		if (StringUtils.isNotEmpty(form.getTxTimeEndSrch())) {
			param.put("txTimeEndSrch", form.getTxTimeEndSrch());
		}

		if (limitStart != -1) {
			param.put("limitStart", limitStart);
			param.put("limitEnd", limitEnd);
		}
		
		List<ManualReverseCustomize> records = this.manualReverseCustomizeMapper.selectManualReverseList(param);
			
		return records;
	}

	/**
	 * 资金明细插入数据
	 * @param form
	 * @return
	 * @author PC-LIUSHOUYI
	 */
	@Override
	public boolean insertAccountList(ManualReverseBean form, HttpServletRequest request) {
		
		//操作管理员与ip获取
        //String createUserId = request.getParameter("userId");
        //String createIp = request.getParameter("ip");
        
		//设置日期格式
		//SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		Integer nowTime = GetDate.getNowTime10();
		//冲正金额
		BigDecimal total = new BigDecimal(form.getAmount().trim());
		
		//获取用户userId
		Users user = this.getUserByUserName(form.getUserName());
		// 重新获取用户信息
		Account account = this.getAccount(user.getUserId());
		// 写入收支明细
		AccountList accountList = new AccountList();
		// 账户信息
		// 订单号：空
		//accountList.setNid(GetOrderIdUtils.getOrderId2(user.getUserId()));
		accountList.setUserId(user.getUserId());
		accountList.setAmount(total);
		//accountList.setType(2);
		//手动冲正表->收支类型:0收入 1支出
		//list表->1收入2支出
		if ("0".equals(form.getType())) {
			accountList.setType(1);
			//收入
			accountList.setTrade("account_adjustment_up");
		} else if ("1".equals(form.getType())) {
			accountList.setType(2);
			//支出
			accountList.setTrade("account_adjustment_down");
			total = total.negate();
		}
		//操作识别码 balance余额操作 frost冻结操作 await待收操作
		accountList.setTradeCode("balance");
		accountList.setTotal(account.getTotal());
		accountList.setBalance(account.getBalance());
		accountList.setFrost(account.getFrost());
		accountList.setAwait(account.getAwait());
		accountList.setRepay(account.getRepay());
		accountList.setBankTotal(account.getBankTotal().add(total)); // 银行总资产
		accountList.setBankBalance(account.getBankBalance().add(total)); // 银行可用余额
		accountList.setBankFrost(account.getBankFrost());// 银行冻结金额
		accountList.setBankWaitCapital(account.getBankWaitCapital());// 银行待还本金
		accountList.setBankWaitInterest(account.getBankWaitInterest());// 银行待还利息
		accountList.setBankAwaitCapital(account.getBankAwaitCapital());// 银行待收本金
		accountList.setBankAwaitInterest(account.getBankAwaitInterest());// 银行待收利息
		accountList.setBankAwait(account.getBankAwait());// 银行待收总额
		accountList.setBankInterestSum(account.getBankInterestSum()); // 银行累计收益
		accountList.setBankInvestSum(account.getBankInvestSum());// 银行累计出借
		accountList.setBankWaitRepay(account.getBankWaitRepay());// 银行待还金额
		accountList.setPlanBalance(account.getPlanBalance());//汇计划账户可用余额
		accountList.setPlanFrost(account.getPlanFrost());
		accountList.setSeqNo(form.getSeqNo());
		accountList.setTxDate(nowTime);
		accountList.setTxTime(nowTime);
		accountList.setBankSeqNo(form.getBankSeqNo());
		accountList.setAccountId(form.getAccountId());
		accountList.setRemark("原交易订单号 " + form.getBankSeqNo());
		accountList.setCreateTime(nowTime);
		accountList.setBaseUpdate(nowTime);
		//操作员
		//accountList.setOperator(createUserId);
		//操作IP
		//accountList.setIp(createIp);
		accountList.setIsUpdate(0);
		accountList.setBaseUpdate(0);
		accountList.setInterest(null);
		accountList.setIsBank(1);
		accountList.setWeb(0);
		accountList.setCheckStatus(1);// 对账状态0：未对账 1：已对账
		accountList.setTradeStatus(1);// 0失败1成功2失败
		//插入资产明细
		boolean isAccountListFlag = this.accountListMapper.insertSelective(accountList) > 0 ? true : false;
		if (!isAccountListFlag) {
			return false;
		}
		//用户相应余额增加
		Account newAccount = new Account();
		
		newAccount.setUserId(user.getUserId());// 用户Id
		newAccount.setBankTotal(total); // 累加到账户总资产
		newAccount.setBankBalance(total); // 累加可用余额
		newAccount.setBankBalanceCash(total);// 江西银行可用余额
		//余额恢复
		boolean isAccountUpdateFlag = this.adminAccountCustomizeMapper.updateManualReverseSuccess(newAccount) > 0 ? true : false;
		if (!isAccountUpdateFlag) {
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * 添加手动冲正数据
	 * @param form
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public void insertManualReverse(ManualReverseBean form, boolean result) {
		ManualReverseCustomize manualReverseCustomize = new ManualReverseCustomize();
		//原交易系统跟踪号
		manualReverseCustomize.setSeqNo(form.getSeqNo());
		//原交易订单号
		manualReverseCustomize.setBankSeqNo(form.getBankSeqNo());
		//设置日期格式
		//SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		//交易时间
		manualReverseCustomize.setTxTime(new Date());
		//用户名
		manualReverseCustomize.setUserName(form.getUserName());
//		// 重新获取用户银行卡号
//		if (StringUtils.isEmpty(form.getAccountId())) {
//			form.setAccountId(getAccountId(form.getUserName()));
//		}
		//电子账号
		manualReverseCustomize.setAccountId(form.getAccountId());
		//资金托管平台：1江西银行
		manualReverseCustomize.setIsBank("1");
		//收支类型:0收入 1支出
		manualReverseCustomize.setType(form.getType());
		//交易类型
		manualReverseCustomize.setTransType("充值");
		//操作金额
		manualReverseCustomize.setAmount(form.getAmount());
		//操作结果 操作状态 0 成功 1失败
		if (result) {
			manualReverseCustomize.setStatus("0");
		} else {
			manualReverseCustomize.setStatus("1");
		}
		//用户id
		manualReverseCustomize.setCreateUserId(Integer.valueOf(ShiroUtil.getLoginUserId()));
		//插入时间
		manualReverseCustomize.setCreateTime(GetDate.getNowTime10());
		
		this.manualReverseCustomizeMapper.insertManualReverse(manualReverseCustomize);
	}
	
	/**
	 * 借款用户必须是已开户的用户名
	 * 
	 * @param userId
	 * @return
	 * @author Administrator
	 */
	@Override
	public String isExistsUser(HttpServletRequest request) {
		JSONObject ret = new JSONObject();
		String message = ValidatorFieldCheckUtil.getErrorMessage("required", "");
		message = message.replace("{label}", "借款金额");

		String param = request.getParameter("param");
		if (StringUtils.isEmpty(param)) {
			//ret.put(AdminDefine.JSON_VALID_INFO_KEY, message);
			//return ret.toString();
			//ret.put(AdminDefine.JSON_VALID_STATUS_KEY, AdminDefine.JSON_VALID_STATUS_OK);
			return ret.toJSONString();
		}

		int usersFlag = this.isExistsUser(param);
		if (usersFlag == 1) {
			message = ValidatorFieldCheckUtil.getErrorMessage("username.not.exists", "");
			ret.put(AdminDefine.JSON_VALID_INFO_KEY, message);
			return ret.toString();
		} else if (usersFlag == 2) {
			message = ValidatorFieldCheckUtil.getErrorMessage("username.not.account");
			ret.put(AdminDefine.JSON_VALID_INFO_KEY, message);
			return ret.toString();
		} else if (usersFlag == 3) {
			message = ValidatorFieldCheckUtil.getErrorMessage("username.not.use");
			ret.put(AdminDefine.JSON_VALID_INFO_KEY, message);
			return ret.toString();
		}

		ret.put(AdminDefine.JSON_VALID_STATUS_KEY, AdminDefine.JSON_VALID_STATUS_OK);

		return ret.toJSONString();
	}
	

	/**
	 * 借款用户必须是已开户的用户名
	 * 
	 * @param userId
	 * @return
	 * @author Administrator
	 */
	@Override
	public int isExistsUser(String userId) {
		if (StringUtils.isNotEmpty(userId)) {
			UsersExample example = new UsersExample();
			UsersExample.Criteria cra = example.createCriteria();
			cra.andUsernameEqualTo(userId);
			List<Users> userList = this.usersMapper.selectByExample(example);
			if (userList == null || userList.size() == 0) {
				// 借款人用户名不存在。
				return 1;
			}

			Users users = userList.get(0);
			BankOpenAccount openAccount = this.getBankOpenAccount(users.getUserId());
			if (Validator.isNull(openAccount)) {
				// 借款人用户名必须已在银行开户
				return 2;
			}

			if (users.getStatus() != 0) {
				// 借款人用户名已经被禁用
				return 3;
			}
			return 0;
		}
		return 1;
	}

	/**
	 * 通过用户信息获取账户信息
	 * 
	 * @param userName
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public String getAccountId(String userName) {
		if (StringUtils.isNotEmpty(userName)) {
			UsersExample example1 = new UsersExample();
			UsersExample.Criteria cra1 = example1.createCriteria();
			cra1.andUsernameEqualTo(userName);
			List<Users> userList = this.usersMapper.selectByExample(example1);
			
			if (null == userList|| 0 == userList.size()) {
				// 用户不存在
				return null;
			}
			Users users = userList.get(0);
			BankOpenAccount openAccount = this.getBankOpenAccount(users.getUserId());

			if (Validator.isNull(openAccount)) {
				// 借款人用户名必须已在银行开户
				return null;
			}
			return openAccount.getAccount();
		}	
		return null;
	}
	
	/**
	 * 通过账户信息获取用户信息
	 * @param userName
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public String getUser(String accountId) {
		Map<String, Object> param = new HashMap<String, Object>();
		// 原交易流水号
		param.put("accountId", accountId);
		String userName = this.manualReverseCustomizeMapper.selectUserNamebyAccountId(param);
		return userName;
	}
	
	/**
	 * 校验订单号是否存在
	 * @param form
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public boolean isExistsOrderId(ManualReverseBean form) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userName", form.getUserName());
		param.put("seqNo", form.getSeqNo());
		param.put("bankSeqNo", form.getBankSeqNo());
		if (this.manualReverseCustomizeMapper.countAccountList(param)>0) {
			return true;
		}
		return false;
	}
}

	