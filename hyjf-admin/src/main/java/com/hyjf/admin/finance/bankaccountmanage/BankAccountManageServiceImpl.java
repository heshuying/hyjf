package com.hyjf.admin.finance.bankaccountmanage;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.finance.bank.merchant.SynBalanceBean;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.BankAccountManageCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminBankAccountCheckCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.soa.apiweb.CommonSoaUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BankAccountManageServiceImpl extends BaseServiceImpl implements BankAccountManageService {

	/**
	 * 查询符合条件的用户数量
	 * 
	 * @param accountInfoCustomize
	 * @return
	 * @author HBZ
	 */
	@Override
	public Integer queryAccountCount(BankAccountManageCustomize accountInfoCustomize) {
		// 部门
		if (Validator.isNotNull(accountInfoCustomize.getCombotreeSrch())) {
			if (accountInfoCustomize.getCombotreeSrch().contains(StringPool.COMMA)) {
				String[] list = accountInfoCustomize.getCombotreeSrch().split(StringPool.COMMA);
				accountInfoCustomize.setCombotreeListSrch(list);
			} else {
				accountInfoCustomize.setCombotreeListSrch(new String[] { accountInfoCustomize.getCombotreeSrch() });
			}
		}
		Integer accountCount = null;
		
		// 为了优化检索查询，判断参数是否全为空，为空不进行带join count
		if(checkFormAllBlank(accountInfoCustomize)){
			accountCount = this.bankAccountManageCustomizeMapper.queryAccountCountAll(accountInfoCustomize);
		}else{
			accountCount = this.bankAccountManageCustomizeMapper.queryAccountCount(accountInfoCustomize);
		}
		return accountCount;

	}

	/**
	 * 账户管理列表查询
	 * 
	 * @param accountInfoCustomize
	 * @return
	 * @author HBZ
	 */
	@Override
	public List<BankAccountManageCustomize> queryAccountInfos(BankAccountManageCustomize accountInfoCustomize) {
		// 为了优化检索查询，判断参数是否全为空，为空不进行带join count
		if(checkFormAllBlank(accountInfoCustomize)){
			accountInfoCustomize.setInitQuery(1);
	    }
		List<BankAccountManageCustomize> accountInfos = this.bankAccountManageCustomizeMapper.queryAccountInfos(accountInfoCustomize);
		return accountInfos;

	}

	private boolean checkFormAllBlank(BankAccountManageCustomize accountInfoCustomize) {
		if (StringUtils.isBlank(accountInfoCustomize.getUsername()) 
				&& StringUtils.isBlank(accountInfoCustomize.getCombotreeSrch())
				&& StringUtils.isBlank(accountInfoCustomize.getAccountSrch())
						&& StringUtils.isBlank(accountInfoCustomize.getVipSrch())) {
			return true;
		}
		return false;
	}

	/**
	 * 更新账户可用余额和冻结金额
	 *
	 * @return
	 */
	public int updateAccount(Integer userId, BigDecimal balance, BigDecimal frost) {
		Account account = new Account();
		// 可提现
		if (Validator.isNotNull(balance)) {
			account.setBankBalanceCash(balance);
		}
		// 不可提现
		if (Validator.isNotNull(frost)) {
			account.setBankFrostCash(frost);
		}
		AccountExample example = new AccountExample();
		example.createCriteria().andUserIdEqualTo(userId);
		// 更新账户表
		return this.accountMapper.updateByExampleSelective(account, example);
	}

	/**
	 * 更新账户信息
	 * 
	 * @param account
	 * @return
	 */
	public int updateAccountSelective(Account account) {

		int result = this.accountMapper.updateByPrimaryKeySelective(account);
		return result;
	}

	/**
	 * insert account_list
	 * 
	 * @param list
	 * @return
	 */
	public int insertAccountList(AccountList accountList) {
		int ret = this.accountListMapper.insertSelective(accountList);
		return ret;
	}

	/**
	 * 根据userId获取账户信息
	 * 
	 * @param userId
	 * @return
	 */
	public Account queryAccountInfoByUserId(Integer userId) {
		AccountExample accountExample = new AccountExample();
		AccountExample.Criteria aCriteria = accountExample.createCriteria();
		aCriteria.andUserIdEqualTo(userId);

		List<Account> accounts = this.accountMapper.selectByExample(accountExample);
		if (accounts != null && accounts.size() == 1) {
			return accounts.get(0);
		}

		return null;
	}

	/**
	 * 手动银行对账 add by yangchangwei 2017/4/18
	 */
	@Override
	public String updateAccountCheck(Integer userId, String startTime, String endTime) {
		// 手动银行对账
		String msg = "success";
		String accountId = "";
		String userName = "";
		try {
			AdminBankAccountCheckCustomize customize = new AdminBankAccountCheckCustomize();
			customize.setUserId(userId);
			List<AdminBankAccountCheckCustomize> bankOpenAccountList = this.adminBankAccountCheckCustomizeMapper.queryAllBankOpenAccount(customize);
			/** redis 锁 */
//			if (StringUtils.isNotEmpty(RedisUtils.get("synBalance:" + userId))) {
//				msg = "不能重复对账";
//				return msg;
//			} else {
//				RedisUtils.set("synBalance:" + userId, String.valueOf(userId), 30);
//			}
			
			boolean reslut = RedisUtils.tranactionSet("synBalance:" + userId, 30);
			// 如果没有设置成功，说明有请求来设置过
			if(!reslut){
				msg = "不能重复对账";
				return msg;
			}
			if (bankOpenAccountList != null && bankOpenAccountList.size() > 0) {
				for (int i = 0; i < 1; i++) {
					accountId = bankOpenAccountList.get(0).getAccountId();
					userName = bankOpenAccountList.get(0).getUserName();
				}
				List<ResultBean> resultList = queryAllAccountDetails(userId, accountId, startTime, endTime);
				if (resultList != null && resultList.size() > 0) {
					// 遍历循环返回列表进行入账处理
					updateBankAccountCheck(resultList, userId, userName,accountId);
				} else {
					msg = "该时间段没有需要对账的交易!";
				}
			} else {
				msg = "该用户未开户!";
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("========The " + userId + " queryAccountDetail is Error !");
			msg = userName + "对账异常!";
		}
		return msg;
	}

	/**
	 * 获得接口返回单个用户的所有线下充值明细
	 * 
	 * @param endTime
	 * @param startTime
	 * @return
	 */
	public List<ResultBean> queryAllAccountDetails(Integer userId, String accountId, String startTime, String endTime) {

		// 获得接口返回的所有交易明细
		// 分页数据
		int pageNum = 1;
		int pageSize = 10;

		Date checkEndDate = BankAccountCheckUtil.getDateByString(endTime);
		Date checkStartDate = BankAccountCheckUtil.getDateByString(startTime);

		String startDate = BankAccountCheckUtil.getDateString(checkStartDate, BankAccountCheckUtil.DATEFORMAT_TYPE2);
		String endDate = BankAccountCheckUtil.getDateString(checkEndDate, BankAccountCheckUtil.DATEFORMAT_TYPE2);
		
		List<ResultBean> recordList = new ArrayList<ResultBean>();
		List<ResultBean> list = new ArrayList<ResultBean>();
		// 调用查询明细接口 查所有交易明细
		String inpDate = "";
		String inpTime = "";
		String relDate = "";
		String traceNo = "";
		do {
		    BankCallBean bean = this.queryAccountDetails(userId, accountId, startDate, endDate, "1", "", String.valueOf(pageNum), String.valueOf(pageSize),
		            inpDate,inpTime,relDate,traceNo);
            if(bean==null){
                LogUtil.endLog(this.getClass().getName(), "同步余额失败");
                return null;
            }
            //返回失败
            if(!BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())){
                LogUtil.endLog(this.getClass().getName(), "-------------------调用查询接口失败，失败原因：" + this.getBankRetMsg(bean.getRetCode())+"--------------------");
                return null;
            }
            //解析返回数据(记录为空)
            String content = bean.getSubPacks();
            if(StringUtils.isEmpty(content)){
                return recordList;
            }
            list = JSONArray.parseArray(bean.getSubPacks(), ResultBean.class);
            recordList.addAll(list);
            // 获得最后一条交易记录 并准备下一次查询用的参数
            if(list!=null&&list.size()>0){
                ResultBean lastResult = list.get(list.size()-1);
                inpDate = lastResult.getInpDate();
                inpTime = lastResult.getInpTime();
                relDate = lastResult.getRelDate();
                traceNo = String.valueOf(lastResult.getTraceNo());
            }
            pageNum++;
        } while (list.size()==pageSize);
		LogUtil.infoLog(this.getClass().getName(),"-------------------"+recordList.size()+"同步余额总条数--------------------");
		LogUtil.infoLog(this.getClass().getName(),"-------------------"+pageNum+"同步余额请求次数--------------------");
		return recordList;
	}

	/**
	 * 调用交易明细查询接口获得交易明细 已改
	 * 
	 * @param userId
	 * @param accountId
	 * @param startDate
	 * @param endDate
	 * @param type
	 * @param transType
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public BankCallBean queryAccountDetails(Integer userId, String accountId, String startDate, String endDate, String type, String transType, 
	    String pageNum, String pageSize,String inpDate,String inpTime,String relDate,String traceNo) {
		// 参数不正确
		if (StringUtils.isEmpty(accountId) || StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate) || StringUtils.isEmpty(type)) {
			return null;
		}
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallConstant.TXCODE_ACCOUNT_DETAILS_QUERY);// 消息类型
																		// 修改手机号增强
																		// accountDetailsQuery2
		bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
		bean.setTxDate(GetOrderIdUtils.getTxDate());
		bean.setTxTime(GetOrderIdUtils.getTxTime());
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
		bean.setChannel(BankCallConstant.CHANNEL_PC);
		bean.setAccountId(accountId);// 电子账号
		bean.setStartDate(startDate);// 起始日期
		bean.setEndDate(endDate);// 结束日期
		bean.setType(type);// 交易种类 0-所有交易 1-转入交易 2-转出交易 9-指定交易类型
		if ("9".equals(type)) {
			bean.setTranType(transType);// 交易类型
		}
		 // 翻页标识  空：首次查询；1：翻页查询；
        if (StringUtils.isNotEmpty(pageNum)&&!"1".equals(pageNum)) {
            bean.setRtnInd("1");
        } else {
            bean.setRtnInd("");
        }
        bean.setInpDate(inpDate);
        bean.setInpTime(inpTime);
        bean.setRelDate(relDate);
        bean.setTraceNo(traceNo);
		// 操作者ID
		bean.setLogUserId(String.valueOf(userId));
		bean.setLogOrderId(GetOrderIdUtils.getUsrId(userId));
		// 调用接口
		return BankCallUtils.callApiBg(bean);
	}

	/**
	 * 开始对单个用户进行入账处理
	 * 
	 * @param resultList
	 */
	public void updateBankAccountCheck(List<ResultBean> resultList, Integer userId, String userName,String accountId) {
		// 开始对单个用户进行入账处理
		System.out.println("==============cwyang Start bankAccountCheck!=======");
		if (resultList.size() > 0) {
			for (int i = 0; i < resultList.size(); i++) {
			    
			        
				String bankSeqNo = null;
				try {
					ResultBean bean = resultList.get(i);
					 // 如果江西银行不返回电子账户号  就设置本地的电子帐户号
                    if(bean.getAccountId()==null){
                        bean.setAccountId(accountId);
                    }
					String orFlage = bean.getOrFlag();
					bankSeqNo = bean.getInpDate() + bean.getInpTime() + bean.getTraceNo() + "";
					String describe = bean.getDescribe();
					System.out.println("============cwyang The bankseqNo is " + bankSeqNo);
					System.out.println("============cwyang The describe is " + describe);
					AdminBankAccountCheckCustomize customize = null;
					if (StringUtils.isNotBlank(bankSeqNo) && ("O".equals(orFlage) || "0".equals(orFlage))) {
					    //判断是否是线下充值
						boolean isType = isRechargeTransType(bean.getTranType());
					    if(isType){
    						customize = this.adminBankAccountCheckCustomizeMapper.queryAccountDeatilByBankSeqNo(bankSeqNo);
    						if (customize != null) {
    							// 该线下交易已对账,不再进行处理
    							System.out.println("该笔线下充值已对账,不予处理!bankseqNo is " + bankSeqNo);
    						} else {
    							// 开始处理线下交易,将线下交易插入对应库表
    							updateOfflineTranscation(bean, userId, userName, bankSeqNo);
    						}
					    }
					} else {
						System.out.println("The bankSeqNo is " + bankSeqNo);
						System.out.println("The orFlage is " + orFlage);
						System.out.println("=========线下充值对账异常==========");
					}
				} catch (Exception e) {
					System.out.println("该笔对账异常! bankseqno is " + bankSeqNo);
				}
			}
		} else {
			System.out.println("=========The resultSize is empty!=====");
		}
		System.out.println("==============cwyang End bankAccountCheck!=======");
	}

	/**
	 * 判断是否属于线下充值类型.
	 * 	优先从Redis中取数据,当Redis中的数据为空时,从数据表中读取数据
	 * @param tranType
	 * @return
	 * @Author : huanghui
	 */
	private boolean isRechargeTransType(String tranType) {

		//从Redis获取线下充值类型List
		String codeStringList = RedisUtils.get(RedisConstants.UNDER_LINE_RECHARGE_TYPE);
        JSONArray redisCodeList = JSONArray.parseArray(codeStringList);

        if (StringUtils.isBlank(codeStringList) || redisCodeList.size() <= 0){
			LogUtil.infoLog(this.getClass().getName(), "---------------------------线下充值类型Redis为空!-------------------------");

			//Redis中数据为空时,直接查询数据库
			UnderLineRechargeExample underLineRechargeExample = new UnderLineRechargeExample();

			underLineRechargeExample.setOrderByClause("`add_time` DESC");

			List<UnderLineRecharge> codeList =  underLineRechargeMapper.selectByExample(underLineRechargeExample);
			if (codeList.isEmpty()){
				LogUtil.infoLog(this.getClass().getName(), "---------------------------线下充值类型数据库未配置!-------------------------");
				return false;
			}else {
				for (UnderLineRecharge code : codeList){
					if (code.getCode().equals(tranType)){
						return true;
					}else {
						continue;
					}
				}
			}
		}else {

			for(Object code : redisCodeList) {
				if (code.equals(tranType)){
					return true;
				}else {
					continue;
				}
			}
		}
		return false;
	}


	/**
	 * 开始线下处理
	 * 
	 * @param bean
	 * @param userName
	 */
	private void updateOfflineTranscation(ResultBean bean, int userId, String userName, String bankSeqNo) throws Exception {
		// 当前时间
		Integer nowTime = GetDate.getNowTime10();
		Account account = this.getAccount(userId);
		System.out.println("========TraceNo:" + bean.getTraceNo());
		System.out.println("========InpDate:" + bean.getInpDate());
		System.out.println("========InpDate:" + bean.getInpDate());
		System.out.println("========TxAmount:" + bean.getTxAmount());

		if (account == null || account.getUserId() != userId) {
			return;
		}
		// 添加重复校验
		SynBalanceBean synBalanceBean = new SynBalanceBean();
		BeanUtils.copyProperties(bean, synBalanceBean);
		// 校验交易明细是否已经插入当前笔充值
		AccountListExample accountListExample = new AccountListExample();
		accountListExample.createCriteria().andTxDateEqualTo(Integer.parseInt(synBalanceBean.getInpDate())).andTxTimeEqualTo(Integer.parseInt(synBalanceBean.getInpTime()))
				.andSeqNoEqualTo(synBalanceBean.getTraceNo() + "").andTypeEqualTo(CustomConstants.TYPE_IN)
				.andBankSeqNoEqualTo(synBalanceBean.getInpDate() + synBalanceBean.getInpTime() + synBalanceBean.getTraceNo());
		List<AccountList> accountLists = accountListMapper.selectByExample(accountListExample);
		if (accountLists != null && accountLists.size() != 0) {
			return;
		}
		// 校验充值信息是否已经插入当前笔充值
		AccountRechargeExample accountRechargeExample = new AccountRechargeExample();
		accountRechargeExample.createCriteria().andTxDateEqualTo(Integer.parseInt(synBalanceBean.getInpDate())).andTxTimeEqualTo(Integer.parseInt(synBalanceBean.getInpTime()))
				.andSeqNoEqualTo(synBalanceBean.getTraceNo()).andBankSeqNoEqualTo(synBalanceBean.getInpDate() + synBalanceBean.getInpTime() + synBalanceBean.getTraceNo());
		List<AccountRecharge> accountRecharges = accountRechargeMapper.selectByExample(accountRechargeExample);
		if (accountRecharges != null && accountRecharges.size() != 0) {
			return;
		}
		// 更新用户账户信息
		Account updateAccount = new Account();
		updateAccount.setUserId(account.getUserId());
		updateAccount.setBankTotal(synBalanceBean.getTxAmount()); // 累加到账户总资产
		updateAccount.setBankBalance(synBalanceBean.getTxAmount());// 累加可用余额(江西账户)
		boolean isAccountUpdateFlag = adminAccountCustomizeMapper.updateOfSynBalance(updateAccount) > 0 ? true : false;
		if (!isAccountUpdateFlag) {
			new RuntimeException("同步线下充值,更新用户账户信息失败~~~~,用户ID:" + account.getUserId());
		}
		CommonSoaUtils.listedTwoRecharge(userId, synBalanceBean.getTxAmount());
		// 重新获取用户账户信息
		account = this.getAccount(userId);
		// 生成交易明细
		AccountList accountList = new AccountList();
		accountList.setNid(GetOrderIdUtils.getOrderId2(account.getUserId()));
		accountList.setUserId(account.getUserId());
		accountList.setAmount(synBalanceBean.getTxAmount());
		accountList.setType(CustomConstants.TYPE_IN);// 收入
		accountList.setTrade("recharge_offline");
		accountList.setTradeCode("balance");
		accountList.setAccountId(synBalanceBean.getAccountId());
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
		accountList.setRemark("线下充值");
		accountList.setCreateTime(GetDate.getNowTime10());
		accountList.setBaseUpdate(GetDate.getNowTime10());
		accountList.setOperator(account.getUserId() + "");
		accountList.setIsUpdate(0);
		accountList.setBaseUpdate(0);
		accountList.setInterest(null);
		accountList.setWeb(0);
		accountList.setTxDate(Integer.parseInt(synBalanceBean.getInpDate()));
		accountList.setTxTime(Integer.parseInt(synBalanceBean.getInpTime()));
		accountList.setSeqNo(synBalanceBean.getTraceNo() + "");
		accountList.setBankSeqNo(synBalanceBean.getInpDate() + synBalanceBean.getInpTime() + synBalanceBean.getTraceNo());
		accountList.setIsBank(1);// 资金托管平台 0:汇付,1:江西银行
		accountList.setTradeStatus(1);
		// 插入交易明细
		boolean accountListUpdateFlag = this.accountListMapper.insertSelective(accountList) > 0 ? true : false;
		if (!accountListUpdateFlag) {
			throw new Exception("线下充值后,同步用户余额,插入交易明细失败~~,用户ID:" + userId);
		}
		// 根据用户ID查询用户银行卡
		BankCard bankCard = this.getBankCardByUserId(userId);

		AccountRecharge record = new AccountRecharge();
		record.setNid(GetOrderIdUtils.getOrderId2(account.getUserId())); // 订单号
		record.setUserId(account.getUserId()); // 用户ID
		record.setUsername(userName);// 用户 名
		record.setStatus(2); // 状态 0：失败；1：成功 2:充值中
		record.setMoney(synBalanceBean.getTxAmount()); // 金额
		record.setCardid(bankCard == null ? "" : bankCard.getCardNo());// 银行卡号
		record.setFeeFrom(null);// 手续费扣除方式
		record.setFee(BigDecimal.ZERO); // 费用
		record.setDianfuFee(BigDecimal.ZERO);// 垫付费用
		record.setBalance(synBalanceBean.getTxAmount()); // 实际到账余额
		record.setPayment(bankCard == null ? "" : bankCard.getBank()); // 所属银行
		record.setGateType("OFFLINE"); // 网关类型：QP快捷充值 B2C个人网银充值 B2B企业网银充值
		record.setType(0); // 类型.1网上充值.0线下充值
		record.setRemark("线下充值");// 备注
		record.setCreateTime(nowTime);
		record.setOperator(account.getUserId() + "");
		record.setAddtime(String.valueOf(nowTime));
		record.setClient(0); // 0pc 1WX 2AND 3IOS 4other
		record.setIsBank(1);// 资金托管平台 0:汇付,1:江西银行
		record.setTxDate(Integer.parseInt(synBalanceBean.getInpDate()));
		record.setTxTime(Integer.parseInt(synBalanceBean.getInpTime()));
		record.setSeqNo(synBalanceBean.getTraceNo());
		record.setBankSeqNo(synBalanceBean.getInpDate() + synBalanceBean.getInpTime() + synBalanceBean.getTraceNo());
		// 插入用户充值记录表
		boolean isRechargeFlag = this.accountRechargeMapper.insertSelective(record) > 0 ? true : false;
		if (!isRechargeFlag) {
			throw new Exception("线下充值后,插入充值记录失败~~!用户ID:" + userId);
		}
	}

	public Account getAccount(Integer userId) {
		AccountExample example = new AccountExample();
		AccountExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		List<Account> listAccount = accountMapper.selectByExample(example);
		if (listAccount != null && listAccount.size() > 0) {
			return listAccount.get(0);
		}
		return null;
	}

	/**
	 * 银行入账异常处理信息记录
	 * 
	 * @param userId
	 * @param bean
	 * @param tradeStatus
	 * @param bankSeqNo
	 * @param remark
	 */
	@SuppressWarnings("unused")
	private void insertAccountCheckExceptionInfo(int userId, ResultBean bean, int tradeStatus, String bankSeqNo, String remark) {
		// TODO 插入异常表信息
		AccountList info = new AccountList();
		info.setUserId(userId);
		info.setAmount(bean.getTxAmount());
		info.setType(Integer.valueOf(bean.getTranType()));
		info.setRemark(remark);
		info.setCreateTime(GetDate.getNowTime10());
		info.setBaseUpdate(0);
		info.setOperator(userId + "");
		info.setIsUpdate(0);
		info.setAccountId(bean.getAccountId());
		info.setTxDate(Integer.parseInt(bean.getInpDate()));
		info.setTxTime(Integer.parseInt(bean.getInpTime()));
		info.setSeqNo(bean.getTraceNo() + "");
		info.setBankSeqNo(bankSeqNo);
		info.setCheckStatus(BankAccountCheckConstants.CHECK_STATUS_FAIL);
		info.setCheckDate(GetDate.getNowTime10());
		info.setCheckBalance(bean.getTxAmount());
		info.setTradeStatus(tradeStatus);
		info.setCheckDate(GetDate.getNowTime10());

		this.adminBankAccountCheckCustomizeMapper.insert(info);
	}

	/**
	 * 开始冲正交易
	 * 
	 * @param id
	 * @param typeId
	 * @param userId
	 * @param bean
	 */
	@SuppressWarnings("unused")
	private void updateorFlage(int id, int typeId, int userId, ResultBean bean) {
		// 需要回滚的金额是 用户总账户金额的总可用余额 直投余额和冻结金额的直投类可用余额 用户资产金额的累积收益
		// 获得需要更新的库表
		Account account = this.adminBankAccountCheckCustomizeMapper.queryAccountIdByUserId(userId);
		if (account != null && account.getId() > 0) {
			// 判断资金走向
			if (BankAccountCheckConstants.TYPE_ID_INCOME == typeId) {// 冲正前为收入
				// 冲正操作需要减少相应金额
				account.setBalance(account.getBalance().subtract(bean.getTxAmount()));// 用户总可用余额
				account.setBankBalance(account.getBankBalance().subtract(bean.getTxAmount()));// 直投类可用余额
				account.setBankInterestSum(account.getBankInterestSum().subtract(bean.getTxAmount()));// 用户资产累计收益
			} else if (BankAccountCheckConstants.TYPE_ID_PAY == typeId) {// 冲正前为支出
				// 冲正操作需要增加相应金额
				account.setBalance(account.getBalance().add(bean.getTxAmount()));// 用户总可用余额
				account.setBankBalance(account.getBankBalance().add(bean.getTxAmount()));// 直投类可用余额
				account.setBankInterestSum(account.getBankInterestSum().add(bean.getTxAmount()));// 用户资产累计收益
			}
			this.accountMapper.updateByPrimaryKeySelective(account);
			// 更新交易明细
			AccountList record = new AccountList();
			record.setCheckStatus(BankAccountCheckConstants.CHECK_STATUS_SUCCESS);
			record.setCheckDate(GetDate.getNowTime10());
			record.setCheckBalance(bean.getTxAmount());
			record.setId(id);
			this.accountListMapper.updateByPrimaryKeySelective(record);
		} else {
			System.out.println("=======The accountInfo is NULL!");
		}
	}

	private BankCard getBankCardByUserId(Integer userId) {
		BankCardExample example = new BankCardExample();
		BankCardExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(userId);
		List<BankCard> list = this.bankCardMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
}
