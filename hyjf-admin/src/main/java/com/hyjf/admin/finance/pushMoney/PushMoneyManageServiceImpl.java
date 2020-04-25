package com.hyjf.admin.finance.pushMoney;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.calculate.AverageCapitalPlusInterestUtils;
import com.hyjf.common.http.HtmlUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetterUtil;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.AppMsMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountWebList;
import com.hyjf.mybatis.model.auto.AccountWebListExample;
import com.hyjf.mybatis.model.auto.BankMerchantAccount;
import com.hyjf.mybatis.model.auto.BankMerchantAccountExample;
import com.hyjf.mybatis.model.auto.BankMerchantAccountList;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.BorrowApicronExample;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.BorrowTenderExample;
import com.hyjf.mybatis.model.auto.PushMoney;
import com.hyjf.mybatis.model.auto.PushMoneyExample;
import com.hyjf.mybatis.model.auto.SpreadsLog;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.TenderCommission;
import com.hyjf.mybatis.model.auto.TenderCommissionExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.mybatis.model.customize.PushMoneyCustomize;
import com.hyjf.mybatis.model.customize.UserInfoCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

@Service
public class PushMoneyManageServiceImpl extends BaseServiceImpl implements PushMoneyManageService {

	
	@Autowired
    @Qualifier("smsProcesser")
    private MessageProcesser smsProcesser;  
	
	@Autowired
	@Qualifier("appMsProcesser")
	private MessageProcesser appMsProcesser;

	/**
	 * 查询符合条件的提成数量
	 *
	 * @param pushMoneyCustomize
	 * @return
	 * @author HBZ
	 */
	@Override
	public Integer queryPushMoneyCount(PushMoneyCustomize pushMoneyCustomize) {
		Integer accountCount = this.pushMoneyCustomizeMapper.queryPushMoneyCount(pushMoneyCustomize);
		return accountCount;
	}

	/**
	 * 提成管理列表查询
	 *
	 * @param pushMoneyCustomize
	 * @return
	 * @author HBZ
	 */
	@Override
	public List<PushMoneyCustomize> queryPushMoneyList(PushMoneyCustomize pushMoneyCustomize) {
		List<PushMoneyCustomize> accountInfos = this.pushMoneyCustomizeMapper.queryPushMoneyList(pushMoneyCustomize);
		return accountInfos;
	}

	/**
	 * 查询符合条件的提成明细数量
	 *
	 * @param pushMoneyCustomize
	 * @return
	 * @author HBZ
	 */
	@Override
	public Integer queryPushMoneyDetailCount(PushMoneyCustomize pushMoneyCustomize) {
		// 部门
		if (Validator.isNotNull(pushMoneyCustomize.getCombotreeSrch())) {
			if (pushMoneyCustomize.getCombotreeSrch().contains(StringPool.COMMA)) {
				String[] list = pushMoneyCustomize.getCombotreeSrch().split(StringPool.COMMA);
				pushMoneyCustomize.setCombotreeListSrch(list);
			} else {
				pushMoneyCustomize.setCombotreeListSrch(new String[] { pushMoneyCustomize.getCombotreeSrch() });
			}
		}
		Integer accountCount = this.pushMoneyCustomizeMapper.queryPushMoneyDetailCount(pushMoneyCustomize);
		return accountCount;
	}

	/**
	 * 提成明细列表查询
	 *
	 * @param pushMoneyCustomize
	 * @return
	 * @author HBZ
	 */
	@Override
	public List<PushMoneyCustomize> queryPushMoneyDetail(PushMoneyCustomize pushMoneyCustomize) {
		// 部门
		if (Validator.isNotNull(pushMoneyCustomize.getCombotreeSrch())) {
			if (pushMoneyCustomize.getCombotreeSrch().contains(StringPool.COMMA)) {
				String[] list = pushMoneyCustomize.getCombotreeSrch().split(StringPool.COMMA);
				pushMoneyCustomize.setCombotreeListSrch(list);
			} else {
				pushMoneyCustomize.setCombotreeListSrch(new String[] { pushMoneyCustomize.getCombotreeSrch() });
			}
		}
		List<PushMoneyCustomize> accountInfos = this.pushMoneyCustomizeMapper.queryPushMoneyDetail(pushMoneyCustomize);
		return accountInfos;
	}

	/**
	 * 取得借款API表
	 *
	 * @param borrowNid
	 * @return
	 */
	public BorrowApicron getBorrowApicronBorrowNid(String borrowNid) {
		if (borrowNid != null) {
			BorrowApicronExample example = new BorrowApicronExample();
			example.createCriteria().andBorrowNidEqualTo(borrowNid).andApiTypeEqualTo(0);
			List<BorrowApicron> list = this.borrowApicronMapper.selectByExample(example);
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		return null;
	}

	/**
	 * 根据主键查找待提成数据
	 */
	@Override
	public TenderCommission queryTenderCommissionByPrimaryKey(Integer id) {
		TenderCommission tenderCommission = this.tenderCommissionMapper.selectByPrimaryKey(id);
		return tenderCommission;
	}

	/**
	 * 发提成处理- 计算提成
	 *
	 * @param form
	 * @return
	 */
	public int insertTenderCommissionRecord(Integer apicornId, PushMoneyManageBean form) {
		int ret = -1;
		// 项目编号
		String borrowNid = form.getBorrowNid();

		// 根据项目编号取得borrow表
		BorrowExample borrowExample = new BorrowExample();
		borrowExample.createCriteria().andBorrowNidEqualTo(borrowNid);
		List<Borrow> borrowList = this.borrowMapper.selectByExample(borrowExample);
		if (borrowList == null || borrowList.size() == 0) {
			return ret;
		}
		Borrow borrow = borrowList.get(0);

		// 根据项目编号取得borrowTender表
		BorrowTenderExample borrowTenderExample = new BorrowTenderExample();
		borrowTenderExample.createCriteria().andBorrowNidEqualTo(borrowNid);
		List<BorrowTender> borrowTenderList = this.borrowTenderMapper.selectByExample(borrowTenderExample);
		if (borrowTenderList == null || borrowTenderList.size() == 0) {
			return ret;
		}

		// 循环BorrowTender表,计算提成
		for (BorrowTender borrowTender : borrowTenderList) {
			boolean is51 = false;
			// 插入提成数据
			TenderCommission tenderCommission = new TenderCommission();
			// 1 直投类提成
			tenderCommission.setTenderType(1);
			// 出借人
			tenderCommission.setTenderUserId(borrowTender.getUserId());
			// 出借金额
			tenderCommission.setAccountTender(borrowTender.getAccount());
			// 项目编号
			tenderCommission.setBorrowNid(borrow.getBorrowNid());
			// 出借ID
			tenderCommission.setTenderId(borrowTender.getId());
			// 出借时间
			tenderCommission.setTenderTime(borrowTender.getAddtime());
			// 状态 0：未发放；1：已发放
			tenderCommission.setStatus(0);
			// 备注
			tenderCommission.setRemark("3");
			// 计算时间
			tenderCommission.setComputeTime(GetDate.getMyTimeInMillis());
			// 订单号
			tenderCommission.setOrdid(borrowTender.getNid());

			// 获得提成的地区名
			tenderCommission.setRegionName(borrowTender.getInviteRegionName());
			tenderCommission.setRegionId(borrowTender.getInviteRegionId());
			// 获得提成的分公司
			tenderCommission.setBranchName(borrowTender.getInviteBranchName());
			tenderCommission.setBranchId(borrowTender.getInviteBranchId());
			// 获得提成的部门
			tenderCommission.setDepartmentName(borrowTender.getInviteDepartmentName());
			tenderCommission.setDepartmentId(borrowTender.getInviteDepartmentId());

			UsersInfo tenderUsersInfo = getUsersInfoByUserId(borrowTender.getUserId());
			int tenderIs51 = 0; // 1 是
			if (tenderUsersInfo != null && tenderUsersInfo.getIs51()!=null) {
				tenderIs51 = tenderUsersInfo.getIs51();
			}
			// 提成人id
			if (borrowTender.getTenderUserAttribute() == 3) {
				// 出借时出借人是线上员工时，提成人是自己
				tenderCommission.setUserId(borrowTender.getUserId());
				if (tenderIs51 == 1) {
					is51 = true;
				}
			} else {
				UsersInfo refererUsersInfo = getUsersInfoByUserId(borrowTender.getInviteUserId());
				if (borrowTender.getInviteUserAttribute() != null && borrowTender.getInviteUserAttribute() == 3) {
					// 出借时推荐人的用户属性是线上员工，提成人是推荐人
					tenderCommission.setUserId(borrowTender.getInviteUserId());
					if (refererUsersInfo != null && refererUsersInfo.getIs51()!=null && refererUsersInfo.getIs51() == 1) {
						is51 = true;
					}
				} else if (borrowTender.getInviteUserAttribute() != null && borrowTender.getInviteUserAttribute() < 2) {
					// 出借时推荐人不是员工，且推荐人是51老用户，提成人是推荐人
					if (refererUsersInfo != null && refererUsersInfo.getIs51()!=null && refererUsersInfo.getIs51() == 1) {
						tenderCommission.setUserId(borrowTender.getInviteUserId());
						is51 = true;
					}
				}
			}
			if(tenderCommission.getUserId()==null ||tenderCommission.getUserId()==0){
				//如果没有提成人，返回
				continue;
			}
			
			BankOpenAccount bankOpenAccountInfo = this.getBankOpenAccount(tenderCommission.getUserId());
			if(bankOpenAccountInfo != null){
			    tenderCommission.setAccountId(bankOpenAccountInfo.getAccount());
			}

			// 计算提成(提成金额,提成人,提成人部门ID,出借人部门ID)
			calculateCommission(tenderCommission, borrowTender.getTenderUserAttribute(), // 出借时出借人的用户属性
					borrowTender.getInviteUserAttribute(), // 出借时推荐人的用户属性
					borrow.getBorrowStyle(), // 还款方式（endday表示天，其它表示月）
					borrow.getBorrowPeriod(), // 借款期限（几个月/天）
					borrow.getProjectType(), // 0汇保贷 1汇典贷 2汇小贷 3汇车贷 4新手标
					borrow.getBorrowApr(), // 借款利率
					is51);

			if (tenderCommission.getCommission()!=null && tenderCommission.getCommission().compareTo(BigDecimal.ZERO) > 0) {
				TenderCommissionExample tenderCommissionExample = new TenderCommissionExample();
				tenderCommissionExample.createCriteria().andTenderTypeEqualTo(1).andBorrowNidEqualTo(borrowNid)
						.andTenderIdEqualTo(borrowTender.getId());
				if (this.tenderCommissionMapper.countByExample(tenderCommissionExample) == 0) {
					// 执行插入
					ret += this.tenderCommissionMapper.insertSelective(tenderCommission);
				} else {
					ret++;
				}
			}
		}

		// 更新借款API表
		BorrowApicron borrowApicron = new BorrowApicron();
		borrowApicron.setId(apicornId);
		borrowApicron.setWebStatus(1);
		ret += borrowApicronMapper.updateByPrimaryKeySelective(borrowApicron);

		return ret;
	}

	/**
	 * 计算提成
	 *
	 * @param tenderCommission
	 * @param tenderAttr
	 * @param refererAttr
	 * @param borrowStyle
	 * @param borrowPerios
	 * @param projectType
	 * @param borrowApr
	 */
	private void calculateCommission(TenderCommission tenderCommission, Integer tenderAttr, Integer refererAttr,
			String borrowStyle, Integer borrowPerios, Integer projectType, BigDecimal borrowApr, Boolean is51) {
		// 提成人
		Integer commissionUserId = tenderCommission.getUserId();
		// 提成金额
		BigDecimal commission = BigDecimal.ZERO;
		// 提成利率(天标)
		BigDecimal rateDay = BigDecimal.ZERO;
		// 提成利率(月标)
		BigDecimal rateMonth = BigDecimal.ZERO;
		// 出借金额
		BigDecimal accountTender = tenderCommission.getAccountTender();
		// 年利率
		borrowApr = borrowApr.divide(new BigDecimal(100), 6, BigDecimal.ROUND_HALF_UP);

		// 判断提成人是否开户  (提成人未开户的不计算提成)
		BankOpenAccount bankOpenAccountInfo = this.getBankOpenAccount(commissionUserId);
		if (bankOpenAccountInfo == null || Validator.isNull(bankOpenAccountInfo.getAccount())) {
			LogUtil.errorLog(this.getClass().getName(), "calculateCommission", "计算提成失败，因为用户没有开户", null);
		    return;
		}

		// 取得提成利率
//		PushMoney pushMoney = getPushMoney(!is51 ? "51老用户" : "线上员工");//如果提成人即是51老用户，又是线上员工，那么安装线上员工标准来计算。
		String tcrAttr="";
		if( (tenderAttr!=null && tenderAttr==3) || (refererAttr!=null && refererAttr==3) ){
			tcrAttr="线上员工";
		}else{
			tcrAttr="51老用户";
		}
		
		// 51老用户不发提成
		if(tcrAttr.equals("51老用户")){
			return;
		}
		
		PushMoney pushMoney = getPushMoney(tcrAttr);
		if(pushMoney ==null || pushMoney.getRewardSend() != 1){
			return;
		}
		
		if (pushMoney != null && NumberUtils.isNumber(pushMoney.getDayTender())) {
			rateDay = new BigDecimal(pushMoney.getDayTender());// 提成利率(天标)
			rateMonth = new BigDecimal(pushMoney.getMonthTender());// 提成利率(月标)
		}

		// 汇消费以外
		if (projectType != 8) {
			// 按天计息,到期还本还息
			if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)) {
				// 线上员工
				if ( (tenderAttr!=null && tenderAttr==3) || (refererAttr!=null && refererAttr==3) ) {
					// 每笔提成= 出借金额*提成比例（天-线上员工）*融资天数
					commission = accountTender.multiply(rateDay).multiply(new BigDecimal(borrowPerios));
				} 
				//51老用户（非员工）
				else {
					if (borrowPerios >= 50) {
						// 借款期限≥50天时，每笔提成=出借金额*提成比例（月-51老用户）
						commission = accountTender.multiply(rateMonth);
					} else {
						// 借款期限＜50天时，每笔提成=出借金额*提成比例（天-51老用户）*天数
						commission = accountTender.multiply(rateDay).multiply(new BigDecimal(borrowPerios));
					}
				}
			}
			// 其他还款方式 (月标)
			else {
				// 线上员工
				if ( (tenderAttr!=null && tenderAttr==3) || (refererAttr!=null && refererAttr==3) ) {
					// 每笔提成=出借金额*提成比例（月-线上员工）*融资月数
					commission = accountTender.multiply(rateMonth).multiply(new BigDecimal(borrowPerios));
				} 
				//51老用户（非员工）
				else {
					// 每笔提成= 出借金额*提成比例（月-51老用户）
					commission = accountTender.multiply(rateMonth);
				}
			}
		}
		// 汇消费(等额本息)
		else {
			// 线上员工
			if ( (tenderAttr!=null && tenderAttr==3) || (refererAttr!=null && refererAttr==3) ) {
				// 等额本息还贷第n个月还贷本金之和
				Map<Integer, BigDecimal> monthPrincipal = AverageCapitalPlusInterestUtils
						.getPerMonthPrincipal(accountTender, borrowApr, borrowPerios);
				if (monthPrincipal != null && monthPrincipal.size() > 0) {
					for (Entry<Integer, BigDecimal> entry : monthPrincipal.entrySet()) {
						commission = commission
								.add(entry.getValue().multiply(rateMonth).multiply(new BigDecimal(entry.getKey())));
					}
				}
			} 
			//51老用户（非员工）
			else {
				// 出借金额*提成比例
				commission = accountTender.multiply(rateMonth);
			}
		}

		// 提成金额
		tenderCommission.setCommission(commission == null ? BigDecimal.ZERO : commission);

		// 根据用户ID查询部门信息
		List<UserInfoCustomize> userInfoCustomizes = this.userInfoCustomizeMapper
				.queryDepartmentInfoByUserId(commissionUserId);
		UserInfoCustomize userInfoCustomize;
		if (userInfoCustomizes != null && userInfoCustomizes.size() > 0) {
			userInfoCustomize = userInfoCustomizes.get(0);
		} else {
			userInfoCustomize = null;
			// System.out.println("提成管理 -根据用户ID查询部门信息失败!");
		}

		if (userInfoCustomize != null) {
			tenderCommission.setRegionId(userInfoCustomize.getRegionId());
			tenderCommission.setRegionName(userInfoCustomize.getRegionName());
			tenderCommission.setBranchId(userInfoCustomize.getBranchId());
			tenderCommission.setBranchName(userInfoCustomize.getBranchName());
			tenderCommission.setDepartmentId(userInfoCustomize.getDepartmentId());
			tenderCommission.setDepartmentName(HtmlUtil.unescape(userInfoCustomize.getDepartmentName()));
		}
	}

	/**
	 * 取得提成配置
	 *
	 * @param type
	 * @return
	 */
	private PushMoney getPushMoney(String type) {
		PushMoneyExample example = new PushMoneyExample();
		example.createCriteria().andProjectTypeEqualTo(1).andTypeEqualTo(type);
		List<PushMoney> list = this.pushMoneyMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 发提成处理
	 *
	 * @param form
	 * @return
	 */
	public int updateTenderCommissionRecord(TenderCommission commission, BankCallBean bankBean) {
		int ret = 0;

		// 增加时间
		Integer time = GetDate.getMyTimeInMillis();
		// 发放人ID
		Integer userId = commission.getUserId();
		// 出借人ID
		Integer tenderUserId = commission.getTenderUserId();
		// 操作者用户名
		String operator = ShiroUtil.getLoginUsername();

		// 更新发放状态
		commission.setStatus(1);// 已发放
		commission.setUpdateTime(time);
		commission.setSendTime(time);
		ret += this.tenderCommissionMapper.updateByPrimaryKeySelective(commission);

		// 写入发放记录表
		SpreadsLog spreadsLog = new SpreadsLog();
		spreadsLog.setUserId(tenderUserId);
		spreadsLog.setSpreadsUserid(userId);
		spreadsLog.setNid(bankBean.getLogOrderId());
		spreadsLog.setType("full");
		spreadsLog.setSpreadsType("tender");
		spreadsLog.setAccountType("capital");
		spreadsLog.setScales(getScales(commission.getBorrowNid(), userId).toString());
		spreadsLog.setBorrowNid(commission.getBorrowNid());
		spreadsLog.setTenderId(commission.getTenderId());
		spreadsLog.setRepayId(0);
		spreadsLog.setAccountAll(BigDecimal.ZERO);
		spreadsLog.setAccountCapital(commission.getAccountTender());
		spreadsLog.setAccountInterest(BigDecimal.ZERO);
		spreadsLog.setAccount(commission.getCommission());
		spreadsLog.setRemark("");
		spreadsLog.setAddtime(String.valueOf(time));
		spreadsLog.setAddip("");
		spreadsLog.setPayStatus(1);
		spreadsLog.setIsValid(1);
		ret += this.spreadsLogMapper.insertSelective(spreadsLog);

		// 更新账户信息
		AccountExample accountExample = new AccountExample();
		AccountExample.Criteria accountCriteria = accountExample.createCriteria();
		accountCriteria.andUserIdEqualTo(userId);
		Account account = accountMapper.selectByExample(accountExample).get(0);
		BigDecimal bankBalanceCash = account.getBankBalanceCash() == null ? BigDecimal.ZERO : account.getBankBalanceCash();
		BigDecimal money = new BigDecimal(bankBean.getTxAmount());// 提成
		BankOpenAccount bankOpenAccountInfo = null;
		if(bankBean != null){
		    bankOpenAccountInfo = getBankOpenAccount(userId);
		    account.setBankBalance(account.getBankBalance().add(money));
		    account.setBankTotal(account.getBankTotal().add(money)); // 累加到账户总资产
		    account.setBankBalanceCash(bankBalanceCash.add(money));
		}
		ret += this.accountMapper.updateByExampleSelective(account, accountExample);

		// 写入收支明细
		AccountList accountList = new AccountList();
		accountList.setNid(bankBean.getLogOrderId());
		accountList.setSeqNo(bankBean.getSeqNo());
		accountList.setTxDate(Integer.parseInt(bankBean.getTxDate()));
        accountList.setTxTime(Integer.parseInt(bankBean.getTxTime()));
        accountList.setBankSeqNo(bankBean.getTxDate() + bankBean.getTxTime() + bankBean.getSeqNo());
        accountList.setCheckStatus(0);
        accountList.setTradeStatus(1);
		accountList.setUserId(userId);
		accountList.setAccountId(bankOpenAccountInfo.getAccount());
		accountList.setAmount(money);
		accountList.setType(1);// 1收入2支出3冻结
		accountList.setTrade("borrow_spreads_tender");
		accountList.setTradeCode("balance");
		accountList.setBankTotal(account.getBankTotal()); // 银行总资产
        accountList.setBankBalance(account.getBankBalance()); // 银行可用余额
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
		accountList.setTotal(account.getTotal());
		accountList.setBalance(account.getBalance());
		accountList.setFrost(account.getFrost());
		accountList.setAwait(account.getAwait());
		accountList.setRepay(account.getRepay());
		accountList.setRemark(commission.getBorrowNid());
		accountList.setCreateTime(time);
		accountList.setOperator(operator);
		accountList.setIp(bankBean.getLogIp());
		accountList.setIsUpdate(0);
		accountList.setBaseUpdate(0);
		accountList.setInterest(null);
		accountList.setWeb(2);
		accountList.setIsBank(bankBean == null ? 0 : 1);
		ret += this.accountListMapper.insertSelective(accountList);

		// 插入网站收支明细记录
		AccountWebList accountWebList = new AccountWebList();
		accountWebList.setOrdid(bankBean.getLogOrderId());// 订单号
		accountWebList.setUserId(accountList.getUserId()); // 出借者
		accountWebList.setAmount(accountList.getAmount()); // 管理费
		accountWebList.setType(CustomConstants.TYPE_OUT); // 类型1收入 2支出
		accountWebList.setTrade(CustomConstants.TRADE_TGTC); // 提成
		accountWebList.setTradeType(CustomConstants.TRADE_TGTC_NM); // 出借推广提成
		accountWebList.setRemark(getBorrowNidByOrdId(accountList.getNid())); // 出借推广提成
		accountWebList.setCreateTime(GetterUtil.getInteger(accountList.getCreateTime()));
		ret += insertAccountWebList(accountWebList);

		if(bankBean !=null){
		    BankMerchantAccount nowBankMerchantAccount = this.getBankMerchantAccount(bankBean.getAccountId());
		    nowBankMerchantAccount.setAvailableBalance(nowBankMerchantAccount.getAvailableBalance().subtract(money));
		    nowBankMerchantAccount.setAccountBalance(nowBankMerchantAccount.getAccountBalance().subtract(money));
		    nowBankMerchantAccount.setUpdateTime(GetDate.getNowTime10());
		    
		    // 更新红包账户信息
		    int updateCount = this.updateBankMerchantAccount(nowBankMerchantAccount);
		    if(updateCount > 0){
		        UserInfoCustomize userInfoCustomize = this.queryUserInfoByUserId(userId);
		        
		        // 添加红包明细
		        BankMerchantAccountList bankMerchantAccountList = new BankMerchantAccountList();
		        bankMerchantAccountList.setOrderId(bankBean.getLogOrderId());
		        bankMerchantAccountList.setBorrowNid(commission.getBorrowNid());
		        bankMerchantAccountList.setUserId(commission.getUserId());
		        bankMerchantAccountList.setAccountId(bankOpenAccountInfo.getAccount());
		        bankMerchantAccountList.setAmount(money);
		        bankMerchantAccountList.setBankAccountCode(bankBean.getAccountId());
		        bankMerchantAccountList.setBankAccountBalance(nowBankMerchantAccount.getAccountBalance());
		        bankMerchantAccountList.setBankAccountFrost(nowBankMerchantAccount.getFrost());
		        bankMerchantAccountList.setTransType(CustomConstants.BANK_MER_TRANS_TYPE_AUTOMATIC);
		        bankMerchantAccountList.setType(CustomConstants.BANK_MER_TYPE_EXPENDITURE);
		        bankMerchantAccountList.setStatus(CustomConstants.BANK_MER_TRANS_STATUS_SUCCESS);
		        bankMerchantAccountList.setTxDate(Integer.parseInt(bankBean.getTxDate()));
		        bankMerchantAccountList.setTxTime(Integer.parseInt(bankBean.getTxTime()));
		        bankMerchantAccountList.setSeqNo(bankBean.getSeqNo());
		        bankMerchantAccountList.setCreateTime(new Date());
		        bankMerchantAccountList.setUpdateTime(new Date());
		        bankMerchantAccountList.setUpdateTime(new Date());
		        bankMerchantAccountList.setRegionName(userInfoCustomize.getRegionName());
		        bankMerchantAccountList.setBranchName(userInfoCustomize.getBranchName());
		        bankMerchantAccountList.setDepartmentName(userInfoCustomize.getDepartmentName());
		        bankMerchantAccountList.setCreateUserId(userId);
		        bankMerchantAccountList.setUpdateUserId(userId);
		        bankMerchantAccountList.setCreateUserName(userInfoCustomize.getUserName());
		        bankMerchantAccountList.setUpdateUserName(userInfoCustomize.getUserName());
		        bankMerchantAccountList.setRemark("出借推广提成");
		        
		        this.bankMerchantAccountListMapper.insertSelective(bankMerchantAccountList);
		    }
		    
		}

		// 纯发短信接口
		Map<String, String> replaceMap = new HashMap<String, String>();
		replaceMap.put("val_amount", commission.getCommission().toString());
		   SmsMessage smsMessage =
                   new SmsMessage(userId, replaceMap, null, null, MessageDefine.SMSSENDFORUSER, null,
                   		CustomConstants.PARAM_TPL_SDTGTC, CustomConstants.CHANNEL_TYPE_NORMAL);
          smsProcesser.gather(smsMessage);
          //
          UserInfoCustomize userInfo =
                  this.userInfoCustomizeMapper.selectUserInfoByUserId(userId);
          if (userInfo != null) {
              Map<String, String> param = new HashMap<String, String>();
              if (userInfo.getTrueName() != null && userInfo.getTrueName().length() > 1) {
                  param.put("val_name", userInfo.getTrueName().substring(0, 1));
              } else {
                  param.put("val_name", userInfo.getTrueName());
              }
              if ("1".equals(userInfo.getSex())) {
                  param.put("val_sex", "先生");
              } else if ("2".equals(userInfo.getSex())) {
                  param.put("val_sex", "女士");
              } else {
                  param.put("val_sex", "");
              }
              param.put("val_amount", commission.getCommission().toString());
              AppMsMessage appMsMessage = new AppMsMessage(null, param, userInfo.getMobile(), MessageDefine.APPMSSENDFORMOBILE,
                      CustomConstants.JYTZ_TPL_SDTGTC);
              appMsProcesser.gather(appMsMessage);
          }
          
		return ret;
	}
	
   /**
    * 
    * 加载红包账户
    * @param accountCode
    * @return
    */
   public BankMerchantAccount getBankMerchantAccount(String accountCode) {
        BankMerchantAccountExample bankMerchantAccountExample = new BankMerchantAccountExample();
        bankMerchantAccountExample.createCriteria().andAccountCodeEqualTo(accountCode);
        List<BankMerchantAccount> bankMerchantAccounts = bankMerchantAccountMapper.selectByExample(bankMerchantAccountExample);
        if (bankMerchantAccounts != null && bankMerchantAccounts.size() != 0) {
            return bankMerchantAccounts.get(0);
        } else {
            return null;
        }
    }
   
   /**
    * 
    * 更新红包账户
    * @param account
    * @return
    */
   public int updateBankMerchantAccount(BankMerchantAccount account){
       return bankMerchantAccountMapper.updateByPrimaryKeySelective(account);
   }
   
   public UserInfoCustomize queryUserInfoByUserId(Integer userId) {
       return userInfoCustomizeMapper.queryUserInfoByUserId(userId);
   }

	/**
	 * 取得提成利率
	 *
	 * @param borrowNid
	 * @param userId
	 */
	private BigDecimal getScales(String borrowNid, Integer userId) {
		BigDecimal rate = BigDecimal.ZERO;

		if (Validator.isNotNull(borrowNid) && Validator.isNotNull(userId)) {
			// 取得借款数据
			String borrowStyle = null;
			BorrowExample example = new BorrowExample();
			example.createCriteria().andBorrowNidEqualTo(borrowNid);
			List<Borrow> borrowList = this.borrowMapper.selectByExample(example);
			if (borrowList != null && borrowList.size() > 0) {
				borrowStyle = borrowList.get(0).getBorrowStyle();
			}

			UsersInfo usersInfo = super.getUsersInfoByUserId(userId);
			if (usersInfo != null) {
				String type = "";
				// 提成发放人时线上用户或51老用户
				if (usersInfo.getAttribute() == 3) {
					type = "线上用户";
				} else if (usersInfo.getIs51() == 1) {
					type = "51老用户";
				}

				// 取得提成利率
				PushMoney pushMoney = getPushMoney(type);
				if (pushMoney != null && NumberUtils.isNumber(pushMoney.getDayTender())) {
					if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)) {
						rate = new BigDecimal(pushMoney.getDayTender());
					} else {
						rate = new BigDecimal(pushMoney.getMonthTender());
					}
				}
			}
		}
		return rate;
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
	 * 插入网站收支记录
	 *
	 * @param nid
	 * @return
	 */
	private int insertAccountWebList(AccountWebList accountWebList) {
		if (countAccountWebList(accountWebList.getOrdid(), accountWebList.getTrade()) == 0) {
			// 设置部门名称
			setDepartments(accountWebList);
			// 插入
			return this.accountWebListMapper.insertSelective(accountWebList);
		}
		return 0;
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
					Users users = getUsersByUserId(userId);

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
	 * 根据出借订单号取出借编号
	 *
	 * @param ordId
	 * @return
	 */
	private String getBorrowNidByOrdId(String ordId) {
		BorrowTenderExample example = new BorrowTenderExample();
		example.createCriteria().andNidEqualTo(ordId);
		List<BorrowTender> list = this.borrowTenderMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0).getBorrowNid();
		}
		return null;
	}
	
    /**
     * 根据用户id查询其在crm中的员工属性
     * @param id
     * @return
     */
    public Integer queryCrmCuttype(Integer userid){
    	
    	Integer cuttype= this.employeeCustomizeMapper.queryCuttype(userid);
    	return cuttype;
    }

    

    /**
     * 查询金额总计 
     * @param id
     * @return
     */
	@Override
	public Map<String, Object> queryPushMoneyTotle(PushMoneyCustomize pushMoneyCustomize) {
		return this.pushMoneyCustomizeMapper.queryPushMoneyTotle(pushMoneyCustomize);
		 
	}
    
}
