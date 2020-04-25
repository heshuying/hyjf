/**
 * Description:用户出借实现类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 郭勇
 * @version: 1.0
 *           Created at: 2015年12月4日 下午1:50:02
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.web.user.rechargefee;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountWebList;
import com.hyjf.mybatis.model.auto.AccountWebListExample;
import com.hyjf.mybatis.model.auto.RechargeFeeReconciliation;
import com.hyjf.mybatis.model.auto.RechargeFeeReconciliationExample;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.UserTransfer;
import com.hyjf.mybatis.model.auto.UserTransferExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.mybatis.model.customize.RechargeCustomize;
import com.hyjf.mybatis.util.mail.MailUtil;
import com.hyjf.web.BaseServiceImpl;

@Service
public class RechargeFeeServiceImpl extends BaseServiceImpl implements RechargeFeeService {

	
	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	private TransactionDefinition transactionDefinition;
	
	private String emails[] = {"gaohonggang@hyjf.com"};
	
	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param rechargeFee
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<RechargeFeeReconciliation> queryRechargeFeeList(RechargeFeeBean form, int limitStart,int limitEnd) {
		RechargeFeeReconciliationExample example = new RechargeFeeReconciliationExample();
		RechargeFeeReconciliationExample.Criteria cra = example.createCriteria();
		if(StringUtils.isNotEmpty(form.getStatusSrch())){
			cra.andStatusEqualTo(Integer.valueOf(form.getStatusSrch()));
		}
		if(StringUtils.isNotEmpty(form.getStartTimeSrch())){
			cra.andStartTimeGreaterThanOrEqualTo(Integer.valueOf(GetDate.getSearchStartTime(form.getStartTimeSrch())));
		}
		if(StringUtils.isNotEmpty(form.getEndTimeSrch())){
			cra.andEndTimeLessThanOrEqualTo(Integer.valueOf(GetDate.getSearchEndTime(form.getEndTimeSrch())));
		}
		cra.andUserIdEqualTo(form.getUserId());
		example.setOrderByClause("add_time desc");
		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		return this.rechargeFeeReconciliationMapper.selectByExample(example);
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param form
	 * @return
	 * @author Michael
	 */
		
	@Override
	public int countFeeRecordTotal(RechargeFeeBean form) {
		RechargeFeeReconciliationExample example = new RechargeFeeReconciliationExample();
		RechargeFeeReconciliationExample.Criteria cra = example.createCriteria();
		if(StringUtils.isNotEmpty(form.getStatusSrch())){
			cra.andStatusEqualTo(Integer.valueOf(form.getStatusSrch()));
		}
		if(StringUtils.isNotEmpty(form.getStartTimeSrch())){
			cra.andStartTimeGreaterThanOrEqualTo(Integer.valueOf(GetDate.getSearchStartTime(form.getStartTimeSrch())));
		}
		if(StringUtils.isNotEmpty(form.getEndTimeSrch())){
			cra.andEndTimeLessThanOrEqualTo(Integer.valueOf(GetDate.getSearchEndTime(form.getEndTimeSrch())));
		}
		cra.andUserIdEqualTo(form.getUserId());
		int count = this.rechargeFeeReconciliationMapper.countByExample(example);
		if(count > 0 ){
			return count;
		}
		return 0;
	}

	/**
	 * 校验参数
	 * @param recordId
	 * @param ret
	 * @return
	 * @author Michael
	 */
		
	@Override
	public RechargeFeeReconciliation checkParam(String recordId, JSONObject ret) {
		RechargeFeeReconciliation rechargeFee = null;
		//参数是否为空
		if(StringUtils.isNotEmpty(recordId)){
			rechargeFee = this.rechargeFeeReconciliationMapper.selectByPrimaryKey(Integer.valueOf(recordId));
		}else{
			ret.put("error", "1");
			ret.put("data", "参数传输失败，请刷新页面重新操作");
			return rechargeFee;
		}
		//记录是否为空
		if(rechargeFee == null){
			ret.put("error", "1");
			ret.put("data", "当前记录不存在，请刷新页面");
			return rechargeFee;
		}
		//已付款记录不操作
		if(rechargeFee.getStatus() == 1){
			ret.put("error", "1");
			ret.put("data", "该笔账单已付款，请刷新页面");
			return rechargeFee;
		}
		int outUserId = rechargeFee.getUserId();
		Users user = this.getUsers(outUserId);
		if (user == null) {
			ret.put("error", "1");
			ret.put("data", "未查询到相应的用户信息");
			return rechargeFee;
		}
		AccountChinapnr chinapnr = this.getAccountChinapnr(outUserId);
		if(chinapnr == null){
			ret.put("error", "1");
			ret.put("data", "用户未开户,请先开户");
			return rechargeFee;
		}
		Account account = this.getAccount(outUserId);
		if(account == null){
			ret.put("error", "1");
			ret.put("data", "用户余额异常，请确认");
			return rechargeFee;
		}
		if(account.getBalance().compareTo(rechargeFee.getRechargeFee()) < 0){
			ret.put("error", "1");
			ret.put("data", "用户余额不足");
			return rechargeFee;
		}
		return rechargeFee;
			
	}

	/**
	 * 调用接口，更新记录
	 * @param rechargeFeeReconciliation
	 * @return
	 * @author Michael
	 */
		
	@Override
	public boolean updateRechargeFee(RechargeFeeReconciliation rechargeFeeReconciliation,String orderId) {
		boolean flag = false;
		try {
			//用户转账记录添加
			UserTransfer userTransfer = new UserTransfer();
			userTransfer.setOrderId(orderId);
			userTransfer.setOutUserId(rechargeFeeReconciliation.getUserId());
			userTransfer.setOutUserName(rechargeFeeReconciliation.getUserName());
			userTransfer.setInUserId(null);
			userTransfer.setInUserName(null);
			userTransfer.setTransferAmount(rechargeFeeReconciliation.getRechargeFee());
			userTransfer.setTransferUrl("");
			userTransfer.setTransferTime(new Date());
			userTransfer.setStatus(1);//转账中
			userTransfer.setTransferType(1);//充值手续费转账
			userTransfer.setRemark("充值手续费转账");
			userTransfer.setCreateUserId(rechargeFeeReconciliation.getUserId());
			userTransfer.setCreateUserName(rechargeFeeReconciliation.getUserName());
			userTransfer.setCreateTime(new Date());
			userTransfer.setUpdateUserId(null);
			userTransfer.setUpdateTime(null);
			userTransfer.setReconciliationId(rechargeFeeReconciliation.getRechargeNid());//对账标识
			this.userTransferMapper.insertSelective(userTransfer);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	/**
	 * 校验记录是否已付款
	 * @param recordId
	 * @param orderId
	 * @param ret
	 * @return
	 * @author Michael
	 */
	@Override
	public boolean checkRecordIsPay(String recordId, String orderId, JSONObject ret) {
		boolean flag = false;
		RechargeFeeReconciliation rechargeFee = null;
		if(StringUtils.isNotEmpty(recordId)){
			rechargeFee = this.rechargeFeeReconciliationMapper.selectByPrimaryKey(Integer.valueOf(recordId));
		}else{
			ret.put("error", "1");
			ret.put("data", "付款失败，请刷新页面重新操作");
			return flag;
		}
		if(rechargeFee.getStatus() == 1){
			ret.put("error", "1");
			ret.put("data", "该笔账单已付款完成，请刷新页面");
			return flag;
		}
		flag = true;
		return flag;
	}

	/**
	 * 回调成功后更新
	 * @param recordId
	 * @param orderId
	 * @return
	 * @author Michael
	 */
	@Override
	public boolean updateRecordSuccess(Integer recordId, String orderId,String ip) {
		RechargeFeeReconciliation rechargeFee = null;
		UserTransfer userTransfer = null;
		if(recordId == null || StringUtils.isEmpty(orderId)){
			return false;
		}
		//开始事务
		TransactionStatus txStatus = null;
		try {
			txStatus = this.transactionManager.getTransaction(transactionDefinition);
			rechargeFee = this.rechargeFeeReconciliationMapper.selectByPrimaryKey(Integer.valueOf(recordId));
			userTransfer = getUserTransferByOrderId(orderId);
			if(rechargeFee == null){
				return false;
			}
			//重复付款
			if(rechargeFee.getStatus() == 1){
				//发送邮件 
				Map<String, String> replaceMap = new HashMap<String, String>();
				replaceMap.put("val_name", rechargeFee.getUserName());
				replaceMap.put("val_order", rechargeFee.getRechargeNid());
				replaceMap.put("val_time", GetDate.timestamptoStrYYYYMMDD(rechargeFee.getStartTime())+"-"+ GetDate.timestamptoStrYYYYMMDD(rechargeFee.getEndTime()));
				replaceMap.put("val_amount", String.valueOf(rechargeFee.getRechargeFee()));
				try {
					MailUtil.sendMail(emails, "借款人充值手续费重复付款", CustomConstants.PARAM_RECHARGE_FEE_REPEAT, replaceMap, null) ;
				} catch (Exception e) {
					System.out.println("发送邮件异常！！");
				}
			}
			// 转账金额
			BigDecimal amout = rechargeFee.getRechargeFee();
			// 系统时间
			int nowTime = GetDate.getNowTime10();
			
			rechargeFee.setStatus(1); //已付款
			rechargeFee.setOrderId(orderId);//转账订单号
			this.rechargeFeeReconciliationMapper.updateByPrimaryKeySelective(rechargeFee);
			if(userTransfer == null){
				return false;
			}
			userTransfer.setStatus(2);//转账成功
			this.userTransferMapper.updateByPrimaryKeySelective(userTransfer);
			/**
			 * 更新账户信息
			 * 可用金额减少
			 * 总资产减少
			 */
			Account account = this.getAccount(rechargeFee.getUserId());
			if(account == null ){
				return false;
			}
			account.setBalance(account.getBalance().subtract(amout));
			account.setTotal(account.getTotal().subtract(amout));
			this.accountMapper.updateByPrimaryKeySelective(account);
			//资金明细添加一条收支记录
			AccountList accountListNew = new AccountList();
			accountListNew.setAmount(amout);
			accountListNew.setAwait(new BigDecimal(0));
			accountListNew.setBalance(account.getBalance());
			accountListNew.setBaseUpdate(0);
			accountListNew.setCreateTime(nowTime);
			accountListNew.setFrost(account.getFrost());
			accountListNew.setInterest(new BigDecimal(0));
			accountListNew.setIp(ip);
			accountListNew.setIsUpdate(0);
			accountListNew.setNid(orderId);
			accountListNew.setOperator(String.valueOf(rechargeFee.getUserId()));
			accountListNew.setRemark(rechargeFee.getRechargeNid());
			accountListNew.setRepay(new BigDecimal(0));
			accountListNew.setTotal(account.getTotal());
			//充值手续费转账
			accountListNew.setTrade("recharge_fee_transfer");
			accountListNew.setTradeCode("balance");
			// 收支类型1收入2支出3冻结
			accountListNew.setType(2);
			accountListNew.setUserId(rechargeFee.getUserId());
			accountListNew.setWeb(0);
		    accountListMapper.insertSelective(accountListNew);
			
			// 4网站收支增加一条收支记录，收入
			AccountWebList accountWebList = new AccountWebList();
			accountWebList.setOrdid(orderId);// 订单号
			accountWebList.setUserId(rechargeFee.getUserId());// 转账人
			accountWebList.setAmount(amout);// 转账金额
			accountWebList.setType(CustomConstants.TYPE_IN);// 类型1收入,2支出
			accountWebList.setTrade(CustomConstants.TRADE_RECHARGE_FEE_TRANSFER);// 充值手续费转账
			accountWebList.setTradeType(CustomConstants.TRADE_RECHARGE_FEE_TRANSFER_NM);// 充值手续费转账
			accountWebList.setRemark(rechargeFee.getRechargeNid());// 转账订单号
			accountWebList.setCreateTime(nowTime);// 系统时间
			insertAccountWebList(accountWebList);
			// 提交事务
			this.transactionManager.commit(txStatus);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			transactionManager.rollback(txStatus);
		}
		return false;
	}

	/**
	 * 回调失败后更新
	 * @param recordId
	 * @param orderId
	 * @return
	 * @author Michael
	 */
	@Override
	public boolean updateRecordFail(Integer recordId, String orderId) {
		UserTransfer userTransfer = null;
		if(StringUtils.isEmpty(orderId)){
			return false;
		}
		userTransfer = getUserTransferByOrderId(orderId);
		if(userTransfer == null){
			return false;
		}
		userTransfer.setStatus(3);//转账失败
		this.userTransferMapper.updateByPrimaryKeySelective(userTransfer);
		return true;
	}
 
	/**
	 * 通过orderid 获取用户转账记录
	 * @param orderId
	 * @return
	 */
	public UserTransfer getUserTransferByOrderId(String orderId){
		UserTransferExample example = new UserTransferExample();
		UserTransferExample.Criteria cra = example.createCriteria();
		cra.andOrderIdEqualTo(orderId);
		List<UserTransfer> list = this.userTransferMapper.selectByExample(example);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 插入网站收支记录
	 *
	 * @param nid
	 * @return
	 */
	private int insertAccountWebList(AccountWebList accountWebList) {
		if (countAccountWebList(accountWebList.getOrdid(), accountWebList.getTrade()) == 0) {
			// 设置部门信息
			setDepartments(accountWebList);
			// 插入
			return this.accountWebListMapper.insertSelective(accountWebList);
		}
		return 0;
	}

	/**
	 * 判断网站收支是否存在
	 *
	 * @param nid
	 * @return
	 */
	private int countAccountWebList(String nid, String trade) {
		AccountWebListExample example = new AccountWebListExample();
		example.createCriteria().andOrdidEqualTo(nid).andTradeEqualTo(trade);
		return this.accountWebListMapper.countByExample(example);
	}

	/**
	 * 设置部门名称
	 *
	 * @param accountWebList
	 */
	private void setDepartments(AccountWebList accountWebList) {
		if (accountWebList != null) {
			Integer userId = accountWebList.getUserId();
			UsersInfo usersInfo = getUsersInfoByUserId(userId);

			if (usersInfo != null) {

				Integer attribute = usersInfo.getAttribute();

				if (attribute != null) {
					// 查找用户的的推荐人
					Users users = this.getUsers(userId);

					Integer refUserId = users.getReferrer();
					SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
					SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
					spreadsUsersExampleCriteria.andUserIdEqualTo(userId);
					List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
					if (sList != null && !sList.isEmpty()) {
						refUserId = sList.get(0).getSpreadsUserid();
					}

					// 如果是线上员工或线下员工，推荐人的userId和username不插
					if (users != null && (attribute == 2 || attribute == 3)) {
						// 查找用户信息
						EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(userId);
						if (employeeCustomize != null) {
							accountWebList.setRegionName(employeeCustomize.getRegionName());
							accountWebList.setBranchName(employeeCustomize.getBranchName());
							accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
						}
					}
					// 如果是无主单，全插
					else if (users != null && (attribute == 1)) {
						// 查找用户推荐人
						EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
						if (employeeCustomize != null) {
							accountWebList.setRegionName(employeeCustomize.getRegionName());
							accountWebList.setBranchName(employeeCustomize.getBranchName());
							accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
						}
					}
					// 如果是有主单
					else if (users != null && (attribute == 0)) {
						// 查找用户推荐人
						EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
						if (employeeCustomize != null) {
							accountWebList.setRegionName(employeeCustomize.getRegionName());
							accountWebList.setBranchName(employeeCustomize.getBranchName());
							accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
						}
					}
				}
				accountWebList.setTruename(usersInfo.getTruename());
				accountWebList.setFlag(1);
			}
		}

	}

	/**
	 * 充值管理列表查询
	 * @param rechargeCustomize
	 * @return
	 * @author HBZ
	 */
	@Override
	public List<RechargeCustomize> queryRechargeList(RechargeCustomize rechargeCustomize) {
		List<RechargeCustomize> accountInfos= this.rechargeCustomizeMapper.queryRechargeList(rechargeCustomize);           
		return accountInfos;
			
	}

	
}
