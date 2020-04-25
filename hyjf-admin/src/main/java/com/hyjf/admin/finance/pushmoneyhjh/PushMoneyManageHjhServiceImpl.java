package com.hyjf.admin.finance.pushmoneyhjh;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.manager.hjhplan.accedelist.AccedeListBean;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetterUtil;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.util.StringUtil;
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
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.BorrowTenderExample;
import com.hyjf.mybatis.model.auto.HjhAccede;
import com.hyjf.mybatis.model.auto.HjhAccedeExample;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.auto.HjhPlanExample;
import com.hyjf.mybatis.model.auto.PushMoney;
import com.hyjf.mybatis.model.auto.PushMoneyExample;
import com.hyjf.mybatis.model.auto.SpreadsLog;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.TenderCommission;
import com.hyjf.mybatis.model.auto.TenderCommissionExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.mybatis.model.customize.PushMoneyCustomize;
import com.hyjf.mybatis.model.customize.UserInfoCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;

@Service
public class PushMoneyManageHjhServiceImpl extends BaseServiceImpl implements PushMoneyManageHjhService {

	
	@Autowired
    @Qualifier("smsProcesser")
    private MessageProcesser smsProcesser;  
	
	@Autowired
	@Qualifier("appMsProcesser")
	private MessageProcesser appMsProcesser;

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
	 * 根据主键查找待提成数据
	 */
	@Override
	public TenderCommission queryTenderCommissionByPrimaryKey(Integer id) {
		TenderCommission tenderCommission = this.tenderCommissionMapper.selectByPrimaryKey(id);
		return tenderCommission;
	}



	/**
	 * 取得提成配置
	 *
	 * @param type
	 * @return
	 */
	private PushMoney getPushMoney(String type) {
		PushMoneyExample example = new PushMoneyExample();
		example.createCriteria().andTypeEqualTo(type);
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
	public int updateTenderCommissionRecord(TenderCommission commission, BankCallBean bankBean, ChinapnrBean chinapnrBean) {
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
		spreadsLog.setNid(commission.getOrdid());
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
		BigDecimal money = new BigDecimal(bankBean == null ? chinapnrBean.getTransAmt() : bankBean.getTxAmount());// 提成
		BankOpenAccount bankOpenAccountInfo = null;
		if(bankBean != null){
		    bankOpenAccountInfo = getBankOpenAccount(userId);
		    account.setBankBalance(account.getBankBalance().add(money));
		    account.setBankTotal(account.getBankTotal().add(money)); // 累加到账户总资产
		    account.setBankBalanceCash(bankBalanceCash.add(money));
		}else if(chinapnrBean != null){
		    account.setTotal(account.getTotal().add(money)); // 累加到账户总资产
		    account.setBalance(account.getBalance().add(money)); // 累加可用余额
		    account.setIncome(account.getIncome().add(money));// 累加到总收入
		}
		ret += this.accountMapper.updateByExampleSelective(account, accountExample);

		// 写入收支明细
		AccountList accountList = new AccountList();
		accountList.setNid(commission.getOrdid());
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
        accountList.setPlanBalance(account.getPlanBalance());
        accountList.setPlanFrost(account.getPlanFrost());
		accountList.setTotal(account.getTotal());
		accountList.setBalance(account.getBalance());
		accountList.setFrost(account.getFrost());
		accountList.setAwait(account.getAwait());
		accountList.setRepay(account.getRepay());
		accountList.setRemark(commission.getBorrowNid());
		accountList.setCreateTime(time);
		accountList.setOperator(operator);
		accountList.setIp(bankBean == null ? chinapnrBean.getLogIp() : bankBean.getLogIp());
		accountList.setIsUpdate(0);
		accountList.setBaseUpdate(0);
		accountList.setInterest(null);
		accountList.setWeb(2);
		accountList.setIsBank(bankBean == null ? 0 : 1);
		ret += this.accountListMapper.insertSelective(accountList);

		// 插入网站收支明细记录
		AccountWebList accountWebList = new AccountWebList();
		accountWebList.setOrdid(accountList.getNid());// 订单号
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
		        bankMerchantAccountList.setOrderId(commission.getOrdid());
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
		// 部门
		if (Validator.isNotNull(pushMoneyCustomize.getCombotreeSrch())) {
			if (pushMoneyCustomize.getCombotreeSrch().contains(StringPool.COMMA)) {
				String[] list = pushMoneyCustomize.getCombotreeSrch().split(StringPool.COMMA);
				pushMoneyCustomize.setCombotreeListSrch(list);
			} else {
				pushMoneyCustomize.setCombotreeListSrch(new String[] { pushMoneyCustomize.getCombotreeSrch() });
			}
		}
		return this.pushMoneyCustomizeMapper.queryPushMoneyTotle(pushMoneyCustomize);
		 
	}

	@Override
	public PushMoneyManageHjhBean queryAccoundInfo(String planOrderId) {
		PushMoneyManageHjhBean hjhBean = new PushMoneyManageHjhBean();
		Integer userId = 0;//提成人id
		HjhAccedeExample example = new HjhAccedeExample();
		example.createCriteria().andAccedeOrderIdEqualTo(planOrderId);
		List<HjhAccede> list = this.hjhAccedeMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			hjhBean.setAccedeOrderIdSearch (list.get(0).getAccedeOrderId());//计划订单编号
			hjhBean.setPlanNid(list.get(0).getPlanNid());//计划标号
			hjhBean.setLockPeriod(StringUtil.valueOf((list.get(0).getLockPeriod())));//锁定期
		}
//		HjhPlanExample hjhExample = new HjhPlanExample();
//		hjhExample.createCriteria().andPlanNidEqualTo(hjhBean.getPlanNid());
//		List<HjhPlan> list2 = this.hjhPlanMapper.selectByExample(hjhExample);
//		if (list2 != null && list2.size() > 0) {
//			hjhBean.setExpectApr(list2.get(0).getExpectApr());//年化利率
//			hjhBean.setBorrowStyle(StringUtil.valueOf(list2.get(0).getIsMonth()));//'默认0 天标，1 月标',
//		}
//		TenderCommissionExample tendExample = new TenderCommissionExample();
//		tendExample.createCriteria().andOrdidEqualTo(planOrderId);
//		List<TenderCommission> list3 = this.tenderCommissionMapper.selectByExample(tendExample);
//		if (list3!= null && list3.size() > 0) {
//			hjhBean.setExpectApr(list3.get(0).getCommission());//提成金额			
//			userId = list3.get(0).getTenderUserId();
//		}
//		
//		UsersExample usersExample = new UsersExample();
//		usersExample.createCriteria().andUserIdEqualTo(userId);
//		List<Users> list4 = this.usersMapper.selectByExample(usersExample);
//		if (list4!= null && list4.size() > 0) {
//			hjhBean.setReferernameSearch(list4.get(0).getUsername());//提成人
//		}
		return hjhBean;
	}
}
