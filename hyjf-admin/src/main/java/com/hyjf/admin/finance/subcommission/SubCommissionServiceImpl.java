package com.hyjf.admin.finance.subcommission;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.security.utils.MD5Utils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.DecimalFormatUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountWebList;
import com.hyjf.mybatis.model.auto.AccountWebListExample;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.SubCommission;
import com.hyjf.mybatis.model.auto.SubCommissionExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.mybatis.model.customize.SubCommissionListConfigCustomize;
import com.hyjf.mybatis.model.customize.SubCommissionListConfigCustomizeExample;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

/**
 * 商户分佣Service实现类
 *
 * @author liuyang
 */
@Service
public class SubCommissionServiceImpl extends BaseServiceImpl implements SubCommissionService {
	Logger _log = LoggerFactory.getLogger(SubCommissionServiceImpl.class);

	/**
	 * 检索分账记录件数
	 *
	 * @param form
	 * @return
	 */
	@Override
	public Integer countSubCommissionList(SubCommissionBean form) {
		SubCommissionExample example = new SubCommissionExample();
		SubCommissionExample.Criteria cra = example.createCriteria();
		// 转入用户名检索用
		if (StringUtils.isNotEmpty(form.getReceiveUserNameSrch())) {
			cra.andReceiveUserNameLike(form.getReceiveUserNameSrch());
		}
		// 订单号检索用
		if (StringUtils.isNotEmpty(form.getOrderIdSrch())) {
			cra.andOrderIdEqualTo(form.getOrderIdSrch());
		}
		// 转账状态
		if (StringUtils.isNotEmpty(form.getTradeStatusSrch())) {
			cra.andTradeStatusEqualTo(Integer.parseInt(form.getTradeStatusSrch()));
		}
		// 添加时间开始
		if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
			cra.andCreateTimeGreaterThanOrEqualTo(GetDate.getDayStart10(form.getTimeStartSrch()));
		}
		// 添加时间结束
		if (StringUtils.isNotEmpty(form.getTimeEndSrch())) {
			cra.andCreateTimeLessThanOrEqualTo(GetDate.getDayEnd10(form.getTimeEndSrch()));
		}
		return this.subCommissionMapper.countByExample(example);
	}

	/**
	 * 检索分账记录列表
	 *
	 * @param form
	 * @param offset
	 * @param limit
	 * @return
	 */
	@Override
	public List<SubCommission> searchSubCommissionList(SubCommissionBean form, int offset, int limit) {
		SubCommissionExample example = new SubCommissionExample();
		SubCommissionExample.Criteria cra = example.createCriteria();
		// 转入用户名检索用
		if (StringUtils.isNotEmpty(form.getReceiveUserNameSrch())) {
			cra.andReceiveUserNameLike(form.getReceiveUserNameSrch());
		}
		// 订单号检索用
		if (StringUtils.isNotEmpty(form.getOrderIdSrch())) {
			cra.andOrderIdEqualTo(form.getOrderIdSrch());
		}
		// 转账状态
		if (StringUtils.isNotEmpty(form.getTradeStatusSrch())) {
			cra.andTradeStatusEqualTo(Integer.parseInt(form.getTradeStatusSrch()));
		}
		// 添加时间开始
		if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
			cra.andCreateTimeGreaterThanOrEqualTo(GetDate.getDayStart10(form.getTimeStartSrch()));
		}
		// 添加时间结束
		if (StringUtils.isNotEmpty(form.getTimeEndSrch())) {
			cra.andCreateTimeLessThanOrEqualTo(GetDate.getDayEnd10(form.getTimeEndSrch()));
		}
		if (offset > -1) {
			example.setLimitStart(offset);
			example.setLimitEnd(limit);
		}
		example.setOrderByClause("create_time desc");
		return this.subCommissionMapper.selectByExample(example);
	}

	/**
	 * 请求前插入分佣记录表
	 *
	 * @param bean
	 * @return
	 */
	@Override
	public boolean insetSubCommissionLog(BankCallBean bean, SubCommissionBean form) {
		// 当前时间
		Integer nowTime = GetDate.getNowTime10();
		SubCommission subCommission = new SubCommission();
		subCommission.setOrderId(bean.getLogOrderId());
		subCommission.setAccountId(bean.getAccountId());
		subCommission.setReceiveUserId(form.getReceiveUserId());
		subCommission.setReceiveUserName(form.getReceiveUserName());
		//新增转入姓名
		subCommission.setTruename(form.getTruename());
		subCommission.setReceiveAccountId(bean.getForAccountId());
		subCommission.setAccount(new BigDecimal(bean.getTxAmount()));
		subCommission.setTxDate(Integer.parseInt(bean.getTxDate()));
		subCommission.setTxTime(Integer.parseInt(bean.getTxTime()));
		subCommission.setSeqNo(bean.getSeqNo());
		subCommission.setTradeStatus(0);
		subCommission.setRemark(form.getRemark());
		subCommission.setCreateUserId(Integer.parseInt(ShiroUtil.getLoginUserId()));
		subCommission.setCreateUserName(ShiroUtil.getLoginUsername());
		subCommission.setCreateTime(nowTime);
		return this.subCommissionMapper.insertSelective(subCommission) > 0 ? true : false;
	}

	/**
	 * 更新失败订单状态
	 *
	 * @param bean
	 */
	@Override
	public void updateSubCommission(BankCallBean bean) {
		Integer nowTime = GetDate.getNowTime10();
		SubCommission subCommission = this.selectSubCommissionByOrderId(bean.getLogOrderId());
		if (subCommission != null) {
			// 银行返回错误信息
			String errorMsg = this.getBankRetMsg(bean.getRetCode() == null ? "" : bean.getRetCode());
			subCommission.setTradeStatus(2);// 失败
			subCommission.setErrorMsg(errorMsg);
			subCommission.setUpdateTime(nowTime);
			subCommission.setUpdateUserId(Integer.parseInt(ShiroUtil.getLoginUserId()));
			subCommission.setUpdateUserName(ShiroUtil.getLoginUsername());
			this.subCommissionMapper.updateByPrimaryKeySelective(subCommission);
		}
	}

	/**
	 * 分账成功后,后续账户信息操作
	 *
	 * @param resultBean
	 * @param form
	 * @return
	 */
	@Override
	public boolean updateSubCommissionSuccess(BankCallBean resultBean, SubCommissionBean form) {
		Integer nowTime = GetDate.getNowTime10();
		// 转账订单号
		String orderId = resultBean.getLogOrderId();
		// 转入用户ID
		Integer receiveUserId = form.getReceiveUserId();
		// 交易金额
		String txAmount = resultBean.getTxAmount();
		// 交易日期
		String txDate = resultBean.getTxDate();
		// 交易时间
		String txTime = resultBean.getTxTime();
		// 交易流水号
		String seqNo = resultBean.getSeqNo();
		// 更新订单状态
		SubCommission subCommission = this.selectSubCommissionByOrderId(orderId);
		subCommission.setTradeStatus(1);// 成功
		subCommission.setUpdateTime(nowTime);
		subCommission.setUpdateUserId(Integer.parseInt(ShiroUtil.getLoginUserId()));
		subCommission.setUpdateUserName(ShiroUtil.getLoginUsername());
		
		if (form.getReceiveAccountId()!=null) {
			subCommission.setReceiveAccountId(form.getReceiveAccountId());
		}
		
		boolean updateFlag = this.subCommissionMapper.updateByPrimaryKeySelective(subCommission) > 0 ? true : false;
		if (!updateFlag) {
			_log.info("更新分账记录表失败,订单号:[" + resultBean.getLogOrderId() + "].");
			throw new RuntimeException("更新分账记录表失败,订单号:[" + resultBean.getLogOrderId() + "].");
		}
		// 查询交易记录
		boolean isExistFlag = this.accountListByOrderId(orderId);
		if (isExistFlag) {
			_log.info("重复转账:转账订单号:[" + orderId + "].");
			throw new RuntimeException("重复转账:转账订单号:[" + orderId + "].");
		}

		// 更新转入用户账户信息
		Account receiveUserAccount = new Account();
		receiveUserAccount.setUserId(receiveUserId);
		receiveUserAccount.setBankTotal(new BigDecimal(txAmount));
		receiveUserAccount.setBankBalance(new BigDecimal(txAmount));
		boolean isUpdateFlag = this.adminAccountCustomizeMapper.updateOfSubCommissionTransferIn(receiveUserAccount) > 0 ? true : false;
		if (!isUpdateFlag) {
			_log.info("更新转入用户的账户信息失败,用户ID:[" + receiveUserId + "].订单号:[" + orderId + "].");
			throw new RuntimeException("更新转入用户的账户信息失败,用户ID:[" + receiveUserId + "].订单号:[" + orderId + "].");
		}
		// 插入交易明细
		receiveUserAccount = this.getAccountByUserId(receiveUserId);

		AccountList receiveUserList = new AccountList();
		receiveUserList.setNid(orderId); // 订单号
		receiveUserList.setUserId(receiveUserId); // 转入人用户ID
		receiveUserList.setAmount(new BigDecimal(txAmount)); // 操作金额
		/** 银行相关 */
		receiveUserList.setAccountId(resultBean.getForAccountId());
		receiveUserList.setBankAwait(receiveUserAccount.getBankAwait());
		receiveUserList.setBankAwaitCapital(receiveUserAccount.getBankAwaitCapital());
		receiveUserList.setBankAwaitInterest(receiveUserAccount.getBankAwaitInterest());
		receiveUserList.setBankBalance(receiveUserAccount.getBankBalance());
		receiveUserList.setBankFrost(receiveUserAccount.getBankFrost());
		receiveUserList.setBankInterestSum(receiveUserAccount.getBankInterestSum());
		receiveUserList.setBankInvestSum(receiveUserAccount.getBankInvestSum());
		receiveUserList.setBankTotal(receiveUserAccount.getBankTotal());
		receiveUserList.setBankWaitCapital(receiveUserAccount.getBankWaitCapital());
		receiveUserList.setBankWaitInterest(receiveUserAccount.getBankWaitInterest());
		receiveUserList.setBankWaitRepay(receiveUserAccount.getBankWaitRepay());
		receiveUserList.setCheckStatus(0);
		receiveUserList.setTradeStatus(1);// 交易状态 0:失败 1:成功
		receiveUserList.setIsBank(1);
		receiveUserList.setTxDate(Integer.parseInt(txDate));
		receiveUserList.setTxTime(Integer.parseInt(txTime));
		receiveUserList.setSeqNo(seqNo);
		receiveUserList.setBankSeqNo(txDate + txTime + seqNo);
		/** 非银行相关 */
		receiveUserList.setType(1); // 1收入
		receiveUserList.setTrade("fee_share_in"); // 手续费分账转入
		receiveUserList.setTradeCode("balance"); // 余额操作
		receiveUserList.setTotal(receiveUserAccount.getTotal()); // 出借人资金总额
		receiveUserList.setBalance(receiveUserAccount.getBalance()); // 出借人可用金额
		receiveUserList.setPlanFrost(receiveUserAccount.getPlanFrost());// 汇添金冻结金额
		receiveUserList.setPlanBalance(receiveUserAccount.getPlanBalance());// 汇添金可用金额
		receiveUserList.setFrost(receiveUserAccount.getFrost()); // 出借人冻结金额
		receiveUserList.setAwait(receiveUserAccount.getAwait()); // 出借人待收金额
		receiveUserList.setCreateTime(nowTime); // 创建时间
		receiveUserList.setBaseUpdate(nowTime); // 更新时间
		receiveUserList.setOperator(CustomConstants.OPERATOR_AUTO_REPAY); // 操作者
		receiveUserList.setRemark("账户分佣");
		receiveUserList.setIp(""); // 操作IP
		receiveUserList.setIsUpdate(0);
		receiveUserList.setBaseUpdate(0);
		receiveUserList.setInterest(BigDecimal.ZERO); // 利息
		receiveUserList.setWeb(0); // PC
		boolean receiveUserListFlag = this.accountListMapper.insertSelective(receiveUserList) > 0 ? true : false;
		if (!receiveUserListFlag) {
			_log.info("插入转入用户交易记录失败,用户ID:[" + receiveUserId + "],订单号:[" + orderId + "].");
			throw new RuntimeException("插入转出用户交易记录失败,用户ID:[" + receiveUserId + "],订单号:[" + orderId + "].");
		}
		// 插入网站收支明细记录
		AccountWebList accountWebList = new AccountWebList();
		accountWebList.setOrdid(orderId);// 订单号
		accountWebList.setBorrowNid(""); // 出借编号
		accountWebList.setUserId(receiveUserId); //
		accountWebList.setAmount(new BigDecimal(txAmount)); // 管理费
		accountWebList.setType(CustomConstants.TYPE_OUT); // 类型1收入,2支出
		accountWebList.setTrade("fee_share_out"); // 管理费 
		accountWebList.setTradeType("手续费分佣"); // 还款服务费
		accountWebList.setRemark(form.getRemark()); // 备注
		accountWebList.setCreateTime(nowTime);
		accountWebList.setOperator(ShiroUtil.getLoginUsername());
		AccountWebListExample example = new AccountWebListExample();
		example.createCriteria().andOrdidEqualTo(accountWebList.getOrdid()).andTradeEqualTo("fee_share_out");// TODO
		int webListCount = this.accountWebListMapper.countByExample(example);
		if (webListCount == 0) {
			UsersInfo usersInfo = getUsersInfoByUserId(receiveUserId);
			if (usersInfo != null) {
				Integer attribute = usersInfo.getAttribute();
				if (attribute != null) {
					// 查找用户的的推荐人
					Users users = getUsersByUserId(receiveUserId);
					Integer refUserId = users.getReferrer();
					SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
					SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
					spreadsUsersExampleCriteria.andUserIdEqualTo(receiveUserId);
					List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
					if (sList != null && !sList.isEmpty()) {
						refUserId = sList.get(0).getSpreadsUserid();
					}
					// 如果是线上员工或线下员工，推荐人的userId和username不插
					if (users != null && (attribute == 2 || attribute == 3)) {
						// 查找用户信息
						EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(receiveUserId);
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
			boolean accountWebListFlag = this.accountWebListMapper.insertSelective(accountWebList) > 0 ? true : false;
			if (!accountWebListFlag) {
				throw new RuntimeException("网站收支记录(huiyingdai_account_web_list)更新失败！" + "[订单号：" + orderId + "]");
			}
		} else {
			throw new RuntimeException("网站收支记录(huiyingdai_account_web_list)已存在!" + "[出借订单号：" + orderId + "]");
		}
		return true;
	}

	/**
	 * 根据订单号,用户ID查询交易明细是否存在
	 *
	 * @param orderId
	 * @param userId
	 * @return
	 */
	@Override
	public boolean accountListByOrderId(String orderId) {
		AccountWebListExample example = new AccountWebListExample();
		AccountWebListExample.Criteria cra = example.createCriteria();
		cra.andOrdidEqualTo(orderId);
		cra.andTradeEqualTo("fee_share_out");
		return this.accountWebListMapper.countByExample(example) > 0 ? true : false;
	}

	/**
	 * 根据订单号查询分账记录
	 *
	 * @param orderId
	 * @return
	 */
	private SubCommission selectSubCommissionByOrderId(String orderId) {
		SubCommissionExample example = new SubCommissionExample();
		SubCommissionExample.Criteria cra = example.createCriteria();
		cra.andOrderIdEqualTo(orderId);
		List<SubCommission> list = this.subCommissionMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 根据用户ID查询用户账户信息
	 *
	 * @param userId
	 * @return
	 */
	private Account getAccountByUserId(Integer userId) {
		AccountExample accountExample = new AccountExample();
		accountExample.createCriteria().andUserIdEqualTo(userId);
		List<Account> listAccount = this.accountMapper.selectByExample(accountExample);
		if (listAccount != null && listAccount.size() > 0) {
			return listAccount.get(0);
		}
		return null;
	}

	/**
	 * 检验参数
	 * 
	 * @param modelAndView
	 * @param form
	 */
	@Override
	public void checkTransferParam(ModelAndView modelAndView, SubCommissionBean form) {
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "accountId", form.getAccountId());
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "receiveUserName", String.valueOf(form.getReceiveUserId()));
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "receiveUserName", form.getReceiveUserName());
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "password", form.getPassword());
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "txAmount", form.getTxAmount());
		if (new BigDecimal(form.getBalance()).compareTo(new BigDecimal(form.getTxAmount())) < 0) {
			ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "txAmount", "feeshare.transfer.txamount.error", "账户余额不足");
		}
		String password = CustomConstants.SUB_COMMISSION_PASSWORD;
		if (!password.equals(MD5Utils.MD5(form.getPassword()))) {
			_log.info("交易密码错误");
			ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "password", "feeshare.transfer.password.error", "交易密码错误");
		}
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "remark", form.getRemark(), 50, true);
	}
	@Override
	public List<SubCommissionListConfigCustomize> users() {
		SubCommissionListConfigCustomizeExample subCommissionListConfigExample = new SubCommissionListConfigCustomizeExample();
		SubCommissionListConfigCustomizeExample.Criteria userCrt = subCommissionListConfigExample.createCriteria();
		List<SubCommissionListConfigCustomize> users = this.subCommissionListConfigCustomizeMapper.selectByNameExample(subCommissionListConfigExample);
		return users;
	}

}
