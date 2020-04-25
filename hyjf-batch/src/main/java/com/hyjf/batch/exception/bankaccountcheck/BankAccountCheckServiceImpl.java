package com.hyjf.batch.exception.bankaccountcheck;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountListExample;
import com.hyjf.mybatis.model.auto.AccountRecharge;
import com.hyjf.mybatis.model.auto.AccountTrade;
import com.hyjf.mybatis.model.auto.AccountTradeExample;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankCardExample;
import com.hyjf.mybatis.model.customize.admin.AdminBankAccountCheckCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

@Service
public class BankAccountCheckServiceImpl extends BaseServiceImpl implements BankAccountCheckService{

	@Override
	public void updateAccountCheck() {
		//准备参数
		String accountId = null;
		Integer userId = null;
		String userName = null;
		try {
			//获得所有用户开户信息,并遍历循环,对每个用户进行对账处理
			List<AdminBankAccountCheckCustomize> allBankOpenAccount = this.adminBankAccountCheckCustomizeMapper.queryAllBankOpenAccount(null);
			if (allBankOpenAccount!=null&&allBankOpenAccount.size()>0) {
				for (int i = 0; i < allBankOpenAccount.size(); i++) {
					accountId = allBankOpenAccount.get(i).getAccountId();
					userId = allBankOpenAccount.get(i).getUserId();
					userName = allBankOpenAccount.get(i).getUserName();
					//查询交易明细接口获得返回列表
					System.out.println("=============cwyang  Start queryAllAccountDetails ,The username is " + userName);
					int size = 0;
					try {
						List<ResultBean> resultList = queryAllAccountDetails(userId,accountId);
						if (resultList!=null) {
							size = resultList.size();
							//遍历循环返回列表进行入账处理
							updateBankAccountCheck(resultList,userId,userName);
						}
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("========The " + userName +" queryAccountDetail is Error !");
					}
					System.out.println("=============cwyang queryAllAccountDetails is END, The listSize is " + size);
					
				}
			}else{
				System.out.println("==================cwyang The bankOpenAccountList is empty! ======");
			}
		} catch (Exception e) {
			System.out.println("===========The bankAccountCheck is error!");
			e.printStackTrace();
			
		}
		
	}
	/**
	 * 开始对单个用户进行入账处理
	 * @param resultList
	 */
	public void updateBankAccountCheck(List<ResultBean> resultList,Integer userId,String userName) {
		// TODO 开始对单个用户进行入账处理
		System.out.println("==============cwyang Start bankAccountCheck!=======");
		if (resultList.size()>0) {
			for (int i = 0; i < resultList.size(); i++) {
				try {

					ResultBean bean = resultList.get(i);
					String orFlage = bean.getOrFlag();
					String bankSeqNo = bean.getInpDate()+bean.getInpTime()+bean.getTraceNo()+"";
					String describe = bean.getDescribe();
					System.out.println("============cwyang The bankseqNo is " + bankSeqNo);
					System.out.println("============cwyang The describe is " + describe);
					AdminBankAccountCheckCustomize customize = null;
					if (StringUtils.isNotBlank(bankSeqNo)||StringUtils.isNotBlank(describe)) {
						//交易描述字段,用来红包发放对账
						if (StringUtils.isNotBlank(describe)) {
							AccountListExample example = new AccountListExample();
							example.createCriteria().andNidEqualTo(describe);
							List<AccountList> accountList = this.accountListMapper.selectByExample(example);
							if (accountList!=null&&accountList.size()==1) {
								customize = new AdminBankAccountCheckCustomize();
								customize.setId(accountList.get(0).getId());
								customize.setAmount(accountList.get(0).getAmount());
								customize.setCheckStatus(accountList.get(0).getCheckStatus()+"");
								AccountTradeExample example2 = new AccountTradeExample();
								example2.createCriteria().andValueEqualTo(accountList.get(0).getTrade());
								List<AccountTrade> accountTrade = this.accountTradeMapper.selectByExample(example2 );
								if (accountTrade!=null) {
									customize.setTypeId(accountTrade.get(0).getTypeId());
								}
							}else{
								System.out.println("=================cwyang 红包发放对账异常!异常订单号：" + describe);
							}
							
						}
						if (customize==null&&StringUtils.isNotBlank(bankSeqNo)) {
							//根据接口返回的流水号对应库表中是否存在匹配的交易明细(充值对账)
							customize = this.adminBankAccountCheckCustomizeMapper.queryAccountDeatilByBankSeqNo(bankSeqNo);
						}
						//根据接口返回的流水号对应库表中是否存在匹配的交易明细
						customize = this.adminBankAccountCheckCustomizeMapper.queryAccountDeatilByBankSeqNo(bankSeqNo);
						if (orFlage!=null && "R".equals(orFlage)) {//交易为冲正交易
							if (customize!=null&&customize.getId()>0&&customize.getAmount()==bean.getTxAmount()) {//存在需要冲正的交易明细并且金额正确
								//获得冲正前的资金流向 对应huiyingdai_account_trade 的type_id 1:支出  3:收入
								int typeId = customize.getTypeId();
								System.out.println("==========The typeID is " + typeId);
								//开始冲正回滚金额
								startorFlage(customize.getId(),typeId,userId,bean);
							}else{//银行明细记录无法匹配本地库表记录,需要录入到银行入账异常记录表
								System.out.println("=============The bankAccontDetailInfo is error info,Start insert to huiyingdai_account_bank_check_exception");
								insertAccountCheckExceptionInfo(userId,bean,BankAccountCheckConstants.TRADE_STATUS_RETURN,bankSeqNo,"冲正异常");
							}
						}else{//交易为原始交易
							//获得银行返回交易的交易类型
							String transType = bean.getTranType();
							if (BankCallConstant.TRANS_TYPE_7820.equals(transType)||BankCallConstant.TRANS_TYPE_7617.equals(transType)) {//交易为线下交易
								if (customize!=null) {
									//该线下交易已对账,不再进行处理
									System.out.println("The trans_type_7820 is exsit!");
								}else{
									//开始处理线下交易,将线下交易插入对应库表
									startOfflineTranscation(bean,userId,userName,bankSeqNo);
								}
							}else {//非线下交易,并且是未入账交易,需要入账处理
								if (BankAccountCheckConstants.TRANS_TYPE_5500.equals(transType)) {//交易为活期收益
									if (customize!=null) {
										//该活期收益已对账,不再进行处理
										System.out.println("该笔活期收益已对账,不用再次处理!");
									}else{
										//开始处理活期收益,将活期收益插入对应库表
										
									}
								}
								
								if (customize!=null&&BankAccountCheckConstants.CHECK_STATUS_FAIL==Integer.valueOf(customize.getCheckStatus())) {
									//存在匹配的明细记录
									//进行入账处理
									if (bean.getTxAmount().equals(customize.getAmount())) {
										AccountList record = new AccountList();
										record = accountListMapper.selectByPrimaryKey(customize.getId());
										record.setCheckStatus(BankAccountCheckConstants.CHECK_STATUS_SUCCESS);
										record.setId(customize.getId());
										record.setCheckDate(GetDate.getNowTime10());
										record.setCheckBalance(bean.getTxAmount());
										record.setAccountDate(Integer.parseInt(GetDate.get10Time(bean.getAccDate())));
										this.accountListMapper.updateByPrimaryKeySelective(record);
									}else{
										System.out.println("===============The accountDetailAcoumt is error !===========");
										//银行返回的交易不是线下充值交易并且无法和本地库表对应,需要放入异常表中
										insertAccountCheckExceptionInfo(userId, bean, BankAccountCheckConstants.TRADE_STATUS_SUCCESS, bankSeqNo, "交易金额无法匹配!");
									}
								}else{
									//无法匹配的明细记录,不做处理
									System.out.println("===============The accountDetail is not exceit !===========");
									insertAccountCheckExceptionInfo(userId, bean, BankAccountCheckConstants.TRADE_STATUS_SUCCESS, bankSeqNo, "交易记录无法匹配!");
								}
							}
						}
					}else{
						System.out.println("The bankSeqNo is null !");
					}
				} catch (Exception e) {
					System.out.println("===============本次交易明细对比异常=========");
				}
			}
		}else{
			System.out.println("=========The resultSize is empty!=====");
		}
		System.out.println("==============cwyang End bankAccountCheck!=======");
	}
	/**
	 * 开始线下处理
	 * @param bean
	 * @param userName
	 */
	private void startOfflineTranscation(ResultBean bean,int userId, String userName,String bankSeqNo) {
		// TODO 开始线下处理
		Account account = new Account();
		account = this.adminBankAccountCheckCustomizeMapper.queryAccountIdByUserId(userId);
		account.setTotal(account.getTotal().add(bean.getTxAmount())); // 累加到账户总资产
		account.setBankBalance(account.getBankBalance().add(bean.getTxAmount()));// 累加可用余额(江西账户)
		account.setIncome(account.getIncome().add(bean.getTxAmount())); // 累加到总收入
		// 更新账户信息
		this.accountMapper.updateByPrimaryKeySelective(account);
		// 生成交易明细
		AccountList accountList = new AccountList();
		/* accountList.setNid(orderId); */
		accountList.setUserId(account.getUserId());
		accountList.setAmount(bean.getTxAmount());
		accountList.setType(CustomConstants.TYPE_IN);// 收入
		accountList.setTrade("recharge_offline");
		accountList.setTradeCode("balance");
		accountList.setTotal(account.getTotal());
		accountList.setBalance(account.getBalance());
		accountList.setFrost(account.getFrost());
		accountList.setAwait(account.getAwait());
		accountList.setRepay(account.getRepay());
		accountList.setRemark("线下充值");
		accountList.setCreateTime(GetDate.getNowTime10());
		accountList.setBaseUpdate(1);
		accountList.setOperator(account.getUserId() + "");
		accountList.setIp("");
		accountList.setIsUpdate(0);
		accountList.setBaseUpdate(0);
		accountList.setInterest(null);
		accountList.setWeb(0);
		accountList.setTxDate(Integer.parseInt(bean.getInpDate()));
		accountList.setTxTime(Integer.parseInt(bean.getInpTime()));
		accountList.setSeqNo(bean.getTraceNo()+"");
		accountList.setBankSeqNo(bankSeqNo);
		accountList.setCheckDate(GetDate.getNowTime10());
		accountList.setCheckBalance(bean.getTxAmount());
		this.accountListMapper.insertSelective(accountList);// 插入交易明细

		// 插入充值信息
		BankCardExample example = new BankCardExample();
		example.createCriteria().andUserIdEqualTo(account.getUserId());
		List<BankCard> bankCardList = bankCardMapper.selectByExample(example);
		BankCard bankCard = new BankCard();
		if (bankCardList != null && bankCardList.size() > 0) {
			bankCard = bankCardList.get(0);
		}
		Integer nowTime = getOrderTime(bean.getInpDate(), bean.getInpTime());
		AccountRecharge record = new AccountRecharge();
		// record.setNid(orderId); // 订单号
		record.setUserId(account.getUserId()); // 用户ID
		record.setUsername(userName);// 用户 名
		record.setStatus(BankAccountCheckConstants.RECHARGE_STATUS_SUCCESS); // 状态 0：失败；1：成功 2:充值中
		record.setMoney(bean.getTxAmount()); // 金额
		record.setCardid(bankCard.getCardNo());// 银行卡号
		record.setFeeFrom(null);// 手续费扣除方式
		record.setFee(BigDecimal.ZERO); // 费用
		record.setDianfuFee(BigDecimal.ZERO);// 垫付费用
		record.setBalance(bean.getTxAmount()); // 实际到账余额
		record.setPayment(""); // 所属银行
		record.setGateType("B2C"); // 网关类型：QP快捷充值 B2C个人网银充值 B2B企业网银充值
		record.setType(0); // 类型.1网上充值.0线下充值
		record.setRemark("线下充值");// 备注
		record.setCreateTime(nowTime);
		record.setOperator(account.getUserId() + "");
		record.setAddtime(String.valueOf(nowTime));
		record.setClient(4); // 0pc 1WX 2AND 3IOS 4other
		record.setIsBank(1);// 资金托管平台 0:汇付,1:江西银行
		record.setTxDate(Integer.parseInt(bean.getInpDate()));
		record.setTxTime(Integer.parseInt(bean.getInpTime()));
		record.setSeqNo(bean.getTraceNo());
		record.setBankSeqNo(bankSeqNo);
		// 插入用户充值记录表
		this.accountRechargeMapper.insertSelective(record);
		
	}
	/**
	 * 银行入账异常处理信息记录
	 * @param userId
	 * @param bean
	 * @param tradeStatus
	 * @param bankSeqNo
	 * @param remark
	 */
	private void insertAccountCheckExceptionInfo(int userId, ResultBean bean,int tradeStatus,String bankSeqNo,String remark) {
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
		info.setSeqNo(bean.getTraceNo()+"");
		info.setBankSeqNo(bankSeqNo);
		info.setCheckStatus(BankAccountCheckConstants.CHECK_STATUS_FAIL);
		info.setCheckDate(GetDate.getNowTime10());
		info.setCheckBalance(bean.getTxAmount());
		info.setTradeStatus(tradeStatus);
		info.setCheckDate(GetDate.getNowTime10());
		
		this.adminBankAccountCheckCustomizeMapper.insert(info);
	}
	private Integer getOrderTime(String txData, String txTime) {
		String stringDate = txData + txTime;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");// 24小时制
		Date date = null;

		try {
			date = simpleDateFormat.parse(stringDate);
		} catch (Exception e) {
			// e.printStackTrace();
			return (int) (new Date().getTime() / 1000);
		}
		return (int) date.getTime() / 1000;
	}
	
	/**
	 * 开始冲正交易
	 * @param id
	 * @param typeId
	 * @param userId
	 * @param bean 
	 */
	private void startorFlage(int id, int typeId, int userId, ResultBean bean) {
		// TODO 开始冲正交易
		//需要回滚的金额是 用户总账户金额的总可用余额   直投余额和冻结金额的直投类可用余额  用户资产金额的累积收益
		//获得需要更新的库表
		Account account = this.adminBankAccountCheckCustomizeMapper.queryAccountIdByUserId(userId);
		if (account!=null&&account.getId()>0) {
			//判断资金走向
			if (BankAccountCheckConstants.TYPE_ID_INCOME == typeId) {//冲正前为收入
				//冲正操作需要减少相应金额
				account.setBalance(account.getBalance().subtract(bean.getTxAmount()));//用户总可用余额
				account.setBankBalance(account.getBankBalance().subtract(bean.getTxAmount()));//直投类可用余额
				account.setBankInterestSum(account.getBankInterestSum().subtract(bean.getTxAmount()));//用户资产累计收益
			}else if(BankAccountCheckConstants.TYPE_ID_PAY == typeId){//冲正前为支出
				//冲正操作需要增加相应金额
				account.setBalance(account.getBalance().add(bean.getTxAmount()));//用户总可用余额
				account.setBankBalance(account.getBankBalance().add(bean.getTxAmount()));//直投类可用余额
				account.setBankInterestSum(account.getBankInterestSum().add(bean.getTxAmount()));//用户资产累计收益
			}
			this.accountMapper.updateByPrimaryKeySelective(account);
			//更新交易明细
			AccountList record = accountListMapper.selectByPrimaryKey(id);
			record.setCheckStatus(BankAccountCheckConstants.CHECK_STATUS_SUCCESS);
			record.setCheckDate(GetDate.getNowTime10());
			record.setCheckBalance(bean.getTxAmount());
			this.accountListMapper.updateByPrimaryKeySelective(record);
		}else{
			System.out.println("=======The accountInfo is NULL!");
		}
		
	}
	/**
	 * 获得接口返回单个用户的所有交易明细
	 * @return
	 */
	public List<ResultBean> queryAllAccountDetails(Integer userId,String accountId) {
		// TODO 获得接口返回的所有交易明细
		//整合数据
		StringBuilder msg = new StringBuilder();
		//分页数据
		int pageNum = 1;
		int pageSize = 20;
		int totalItems = 20;
		//查询日期准备,暂定查询周期为一周,用户可以在页面手动对账,所以查询周期使用一周的周期,如有遗漏使用手动对账
		Date checkEndDate = new Date();
		checkEndDate = GetDate.countDate(5, -1);//获得一天前日期
//				GetDate.getNoticeDate(checkEndDate, 0, -1);
		Date checkStartDate = GetDate.countDate(5, -7);//获得一周前日期
		
		String startDate = GetDate.formatDate(checkStartDate,"yyyyMMdd");
		String endDate = GetDate.formatDate(checkEndDate,"yyyyMMdd");
		
		if(totalItems >= pageNum * pageSize){
			for(int j = 0; j < totalItems/pageSize; j++){
				
				//调用查询明细接口 查所有交易明细
				BankCallBean bean = this.queryAccountDetails(userId,accountId, 
						startDate, endDate, "0", null, String.valueOf(pageNum), String.valueOf(pageSize));
				if(bean == null){	
					continue;
				}
				msg.append(bean.getSubPacks());
				pageNum ++;
				if (bean.getTotalItems()!=null&&!"".equals(bean.getTotalItems())) {
					totalItems =Integer.parseInt(bean.getTotalItems());
				}
			}
		}
		
		//转换结果
		List<ResultBean> recordList = new ArrayList<ResultBean>();
		if (!"".equals(msg.toString())) {
			recordList = JSONArray.parseArray(msg.toString(), ResultBean.class);
		}
		return recordList;
	}
	/**
	 * 调用交易明细查询接口获得交易明细
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
	public BankCallBean queryAccountDetails(Integer userId,String accountId, String startDate, String endDate,
			String type, String transType, String pageNum, String pageSize) {
		// 参数不正确
		if (StringUtils.isEmpty(accountId) || StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)
				|| StringUtils.isEmpty(type)) {
			return null;
		}
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallConstant.TXCODE_ACCOUNT_DETAILS_QUERY);// 消息类型
																		// 修改手机号增强
																		// accountDetailsQuery
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
		if (StringUtils.isNotEmpty(pageNum)) {
			bean.setPageNum(pageNum);
		} else {
			bean.setPageNum("1");
		}
		if (StringUtils.isNotEmpty(pageSize)) {
			bean.setPageSize(pageSize);
		} else {
			bean.setPageSize("10");
		}
		// 操作者ID
		bean.setLogUserId(String.valueOf(userId));
		bean.setLogOrderId(GetOrderIdUtils.getUsrId(userId));
		// 调用接口
		return BankCallUtils.callApiBg(bean);
	}

}
