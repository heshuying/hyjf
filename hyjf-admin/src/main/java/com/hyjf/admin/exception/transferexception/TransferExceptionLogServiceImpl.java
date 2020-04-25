package com.hyjf.admin.exception.transferexception;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountListExample;
import com.hyjf.mybatis.model.auto.AccountWebList;
import com.hyjf.mybatis.model.auto.AccountWebListExample;
import com.hyjf.mybatis.model.auto.BankMerchantAccount;
import com.hyjf.mybatis.model.auto.BankMerchantAccountExample;
import com.hyjf.mybatis.model.auto.BankMerchantAccountList;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowTenderCpn;
import com.hyjf.mybatis.model.auto.BorrowTenderCpnExample;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.CouponRecover;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.TransferExceptionLog;
import com.hyjf.mybatis.model.auto.TransferExceptionLogWithBLOBs;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.mybatis.model.customize.UserInfoCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminTransferExceptionLogCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponRecoverCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

/**
 * service接口实现类
 */
@Service
public class TransferExceptionLogServiceImpl extends BaseServiceImpl implements TransferExceptionLogService {
    /** 用户ID */
    private static final String USERID = "userId";
    /** 还款金额 */
    private static final String VAL_BALANCE = "val_balance";
    @Autowired
    @Qualifier("smsProcesser")
    private MessageProcesser smsProcesser; 
/** 还款金额(优惠券用) */
    private static final String VAL_AMOUNT = "val_amount";
    
    /**
     * 获取用户的优惠券列表
     */
    @Override
    public List<AdminTransferExceptionLogCustomize> getRecordList(Map<String, Object> paraMap) {
        return transferExceptionLogCustomizeMapper.selectTransferExceptionList(paraMap);
    }

    /**
     * 获得记录数
     */
    @Override
    public Integer countRecord(Map<String, Object> paraMap) {
        return transferExceptionLogCustomizeMapper.countTransferException(paraMap);
    }
    
    /**
     * 根据uuid更新一条记录
     */
    @Override
    public int updateRecordByUUID(AdminTransferExceptionLogCustomize AdminTransferExceptionLogCustomize){
        TransferExceptionLogWithBLOBs target = new TransferExceptionLogWithBLOBs();
        BeanUtils.copyProperties(AdminTransferExceptionLogCustomize, target);
        return transferExceptionLogMapper.updateByPrimaryKeySelective(target);
    }
    
    /**
     * 根据uuid更新一条记录
     */
    @Override
    public int updateRecordByUUID(TransferExceptionLog transferExceptionLog){
        return transferExceptionLogMapper.updateByPrimaryKey(transferExceptionLog);
    }
    
    /**
     * 根据uuid查询一条记录
     */
    @Override
    public TransferExceptionLog getRecordByUUID(String uuid){
        return transferExceptionLogMapper.selectByPrimaryKey(uuid);
    }
    
    @Override
    public int countAccountListByNidCoupon(String nid) {
        AccountListExample accountListExample = new AccountListExample();
        accountListExample.or(accountListExample.createCriteria().andNidEqualTo(nid).andTradeEqualTo("increase_interest_profit"));
        accountListExample.or(accountListExample.createCriteria().andNidEqualTo(nid).andTradeEqualTo("experience_profit"));
        return this.accountListMapper.countByExample(accountListExample);
    }
    
    @Override
    public Account getAccountByUserId(Integer userId) {
        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria().andUserIdEqualTo(userId);
        List<Account> listAccount = this.accountMapper.selectByExample(accountExample);
        if (listAccount != null && listAccount.size() > 0) {
            return listAccount.get(0);
        }
        return null;
    }
    
    @Override
    public int updateOfRepayTender(Account account) {
        return this.adminAccountCustomizeMapper.updateOfRepayTender(account);
    }

    /**
     * 写入收支明细
     *
     * @param accountList
     * @return
     */
    @Override
    public int insertAccountList(AccountList accountList) {
        // 写入收支明细
        return this.accountListMapper.insertSelective(accountList);
    }
    
    /**
     * 取得优惠券出借信息
     * @param couponTenderNid
     * @return
     */
    @Override
    public BorrowTenderCpn getCouponTenderInfo(String couponTenderNid){
        BorrowTenderCpnExample example = new BorrowTenderCpnExample();
        example.createCriteria().andNidEqualTo(couponTenderNid);
        List<BorrowTenderCpn> btList = this.borrowTenderCpnMapper.selectByExample(example);
        if(btList!=null&&btList.size()>0){
            return btList.get(0);
        }
        return null;
    }
    
    /**
     * 获取CouponTender
     * @author hsy
     * @param tenderNid
     * @return
     */
    public String getCouponTender(String tenderNid) {
        Map<String, Object> paraMap = new HashMap<String,Object>();
        paraMap.put("tenderNid", tenderNid);
        String couponUserCode = transferExceptionLogCustomizeMapper.getCouponUserCode(paraMap);
        
        return couponUserCode;
    }
    
    /**
     * 转账成功后续处理
     * @author hsy
     * @param transfer
     * @return
     * @throws Exception
     * @see com.hyjf.admin.exception.transferexception.TransferExceptionLogService#transferAfter(com.hyjf.mybatis.model.auto.TransferExceptionLog)
     */
    public boolean transferAfter(TransferExceptionLog transfer, BankCallBean resultBean) throws Exception{
        //取的优惠券还款记录
        CouponRecover recover = couponRecoverMapper.selectByPrimaryKey(transfer.getRecoverId());
        if(recover == null){
            throw new Exception("未查询到对应的优惠券还款记录，[recoverid：" + transfer.getRecoverId() + "]");
        }
        
        String couponUserCode = getCouponTender(recover.getTenderId());
        if(couponUserCode == null){
            throw new Exception("未查询到对应的CouponTender记录，[tender_nid：" + recover.getTenderId() + "]");
        }
        
        // 取得优惠券出借信息
        BorrowTenderCpn borrowTender = this.getCouponTenderInfo(recover.getTenderId());
        if(borrowTender == null) {
            throw new Exception("未查询到对应的优惠券出借记录，[tender_nid：" + recover.getTenderId() + "]");
        }
        
        boolean isMonth = false;
        // 当前期数
        Integer periodNow = recover.getRecoverPeriod();
        // 剩余还款期数
    	Integer periodNext = null;
        if(borrowTender.getTenderType() != 3){
        	// 取得借款详情
        	BorrowWithBLOBs borrow = getBorrow(borrowTender.getBorrowNid());
        	if(borrow == null) {
        		throw new Exception("未查询到对应的标的记录，[borrow_nid：" + borrowTender.getBorrowNid() + "]");
        	}
        	
        	// 还款期数
        	Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
        	// 剩余还款期数
        	periodNext = borrowPeriod - periodNow;
        	String borrowStyle = borrow.getBorrowStyle();
        	
        	// 是否分期(true:月标, false:天标)
        	isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
        	
        }
        
        
        // 当前还款
        CouponRecoverCustomize currentRecover = null;
        currentRecover = this.getCurrentCouponRecover(recover.getTenderId(),periodNow);
        if (currentRecover == null) {
            throw new Exception("优惠券还款数据不存在。[项目编号：" + borrowTender.getBorrowNid() + "]，" + "[优惠券出借编号：" + recover.getTenderId() + "]");
        }
        
		// 应还利息
		String recoverInterestStr = StringUtils.isEmpty(currentRecover.getRecoverInterest()) ? "0.00" : currentRecover.getRecoverInterest();
		// 应还本息
		String recoverAccountStr = StringUtils.isEmpty(currentRecover.getRecoverAccount()) ? "0.00" : currentRecover.getRecoverAccount();
		// 应还本金
		String recoverCapitalStr = StringUtils.isEmpty(currentRecover.getRecoverCapital()) ? "0.00" : currentRecover.getRecoverCapital();
		BigDecimal recoverInterest = new BigDecimal(recoverInterestStr);
		BigDecimal recoverAccount = new BigDecimal(recoverAccountStr);
		BigDecimal recoverCapital = new BigDecimal(recoverCapitalStr);

        // 判断该收支明细是否存在时,跳出本次循环
        if (countAccountListByNidCoupon(transfer.getOrderId()) == 0) {

        	// 更新账户信息(出借人)
			Account account = new Account();
			
			int accountCnt;
			// 如果是计划类出借
			if(borrowTender.getTenderType() == 3 ){
				// 更新账户信息(出借人)
				account.setUserId(transfer.getUserId());
				
	            account.setBankBalance(recoverAccount); // 账户余额
	            account.setBankInterestSum(recoverAccount);
	            
				// 计划已还总利息
				account.setPlanRepayInterest(recoverAccount);
				// 计划待收收益
				account.setPlanInterestWait(recoverInterest);
				// 计划待收总额
				account.setPlanAccountWait(recoverAccount);
				
				// 更新用户计划账户
				accountCnt = this.adminAccountCustomizeMapper.updateOfRepayCouponHjh(account);
			}else{
				// 直投类
				account.setUserId(transfer.getUserId());
	            account.setBankTotal(BigDecimal.ZERO);// 出借人资金总额 +利息
	            account.setBankFrost(BigDecimal.ZERO);// 出借人冻结金额+出借金额(等额本金时出借金额可能会大于计算出的本金之和)
	            account.setBankBalance(recoverAccount); // 账户余额
	            account.setBankAwait(recoverAccount);// 出借人待收金额+利息+ 本金
	            account.setBankAwaitCapital(BigDecimal.ZERO);// 出借人待收本金
	            account.setBankAwaitInterest(recoverAccount);// 出借人待收利息
	            account.setBankInterestSum(recoverAccount);
	            account.setBankInvestSum(BigDecimal.ZERO);// 出借人累计出借
	            account.setBankFrostCash(BigDecimal.ZERO);// 江西银行冻结金额
	            
				accountCnt = this.adminAccountCustomizeMapper.updateOfRepayTender(account);
			}

            if (accountCnt == 0) {
                throw new Exception("出借人资金记录(huiyingdai_account)更新失败！" + "[优惠券出借编号：" + recover.getTenderId() + "]");
            }
            // 取得账户信息(出借人)
            account  = getAccountByUserId(transfer.getUserId());
            if (account == null) {
                throw new Exception("出借人账户信息不存在。[出借人ID：" + transfer.getUserId() + "]，" + "[优惠券出借编号：" + recover.getTenderId() + "]");
            }

            // 写入收支明细
            AccountList accountList = new AccountList();
            // 转账订单编号
            accountList.setNid(transfer.getOrderId());
            // 出借人
            accountList.setUserId(transfer.getUserId());
            accountList.setBankAwait(account.getBankAwait());
            accountList.setBankAwaitCapital(account.getBankAwaitCapital());
            accountList.setBankAwaitInterest(account.getBankAwaitInterest());
            accountList.setBankBalance(account.getBankBalance());
            accountList.setBankFrost(account.getBankFrost());
            accountList.setBankInterestSum(account.getBankInterestSum());
            accountList.setBankTotal(account.getBankTotal());
            accountList.setBankWaitCapital(account.getBankWaitCapital());
            accountList.setBankWaitInterest(account.getBankWaitInterest());
            accountList.setBankWaitRepay(account.getBankWaitRepay());
            accountList.setPlanBalance(account.getPlanBalance());//汇计划账户可用余额
			accountList.setPlanFrost(account.getPlanFrost());
            accountList.setAccountId(transfer.getAccountId());
            accountList.setTxDate(Integer.parseInt(GetOrderIdUtils.getTxDate()));
            accountList.setTxTime(Integer.parseInt(GetOrderIdUtils.getTxTime()));
            accountList.setSeqNo(transfer.getSeqNo());
            accountList.setBankSeqNo(transfer.getSeqNo());
            accountList.setCheckStatus(0);
            accountList.setTradeStatus(1);
            accountList.setIsBank(1);
            // 余额操作
            accountList.setTradeCode("balance"); 
            // 创建时间
            accountList.setCreateTime(GetDate.getNowTime10()); 
            // 操作者
            accountList.setOperator(CustomConstants.OPERATOR_AUTO_REPAY); 
            
            // 出借收入
            accountList.setAmount(transfer.getTransAmt());
            // 1收入
            accountList.setType(1);

            if(currentRecover.getCouponType()==1){
                //体验金
                accountList.setRemark("体验金："+ couponUserCode);
                // 还款成功
                accountList.setTrade("experience_profit");
            }else if(currentRecover.getCouponType()==2){
                //加息券
                accountList.setRemark("加息券："+ couponUserCode);
                // 还款成功
                accountList.setTrade("increase_interest_profit");
            }else if(currentRecover.getCouponType()==3){
                //加息券
                accountList.setRemark("代金券："+ couponUserCode);
                // 还款成功
                accountList.setTrade("cash_coupon_profit");
            }
            
            accountList.setIp(""); // 操作IP
            accountList.setIsUpdate(0);
            accountList.setBaseUpdate(0);
//            accountList.setInterest(transfer.getTransAmt()); // 利息
            accountList.setWeb(0); // PC
            int accountListCnt = insertAccountList(accountList);
            if (accountListCnt == 0) {
                throw new Exception("收支明细(huiyingdai_account_list)写入失败！" + "[优惠券出借编号：" + recover.getTenderId() + "]");
            }
        }
        

		// 已收总额
		borrowTender.setRecoverAccountYes(borrowTender.getRecoverAccountYes().add(recoverAccount));
		// 已收本金
		borrowTender.setRecoverAccountCapitalYes(borrowTender.getRecoverAccountCapitalYes().add(recoverCapital));
		// 已收利息
		borrowTender.setRecoverAccountInterestYes(borrowTender.getRecoverAccountInterestYes().add(recoverInterest));
		// 待收总额
		borrowTender.setRecoverAccountWait(borrowTender.getRecoverAccountWait().subtract(recoverAccount));
		// 待收本金
		borrowTender.setRecoverAccountCapitalWait(borrowTender.getRecoverAccountCapitalWait().subtract(recoverCapital));
		// 待收利息
		borrowTender.setRecoverAccountInterestWait(borrowTender.getRecoverAccountInterestWait().subtract(recoverInterest));
        int borrowTenderCnt = borrowTenderCpnMapper.updateByPrimaryKeySelective(borrowTender);
        if (borrowTenderCnt == 0) {
            throw new Exception("出借表(huiyingdai_borrow_tender)更新失败！" + "[优惠券出借编号：" + recover.getTenderId() + "]");
        }
        CouponRecover crTemp = this.couponRecoverMapper.selectByPrimaryKey(recover.getId());
        // 更新优惠券出借还款表
        CouponRecover cr = new CouponRecover(); 
        cr.setId(recover.getId());
        // 转账订单编号
        cr.setTransferId(transfer.getOrderId());
        // 已还款
        cr.setRecoverStatus(1);
        // 收益领取状态(已领取)
        cr.setReceivedFlg(5);
        // 转账时间
        cr.setTransferTime(GetDate.getNowTime10());
        // 已经还款时间
        cr.setRecoverYestime(GetDate.getNowTime10());
        // 已还本息
        cr.setRecoverAccountYes(transfer.getTransAmt());
        // 已还利息
        cr.setRecoverInterestYes(crTemp.getRecoverInterest());
        if(currentRecover.getCouponType() == 3){
        	// 代金券
        	// 已还本金
            cr.setRecoverCapitalYes(crTemp.getRecoverCapital());
        }else{
        	// 体验金和加息券
        	// 已还本金
        	cr.setRecoverCapitalYes(BigDecimal.ZERO);
        }
        // 更新时间
        cr.setUpdateTime(GetDate.getNowTime10());
        // 更新用户
        cr.setUpdateUser(CustomConstants.OPERATOR_AUTO_REPAY);
        // 通知用户
        cr.setNoticeFlg(1);
        
        this.couponRecoverMapper.updateByPrimaryKeySelective(cr);
        
        // 插入网站收支明细记录
        AccountWebList accountWebList = new AccountWebList();
        if(!isMonth){
            // 未分期
            accountWebList.setOrdid(borrowTender.getNid());// 订单号
        }else{
            // 分期
            accountWebList.setOrdid(borrowTender.getNid() + "_" + periodNow);// 订单号
            if(periodNext !=null &&  periodNext > 0){
                // 更新还款期
                crRecoverPeriod(recover.getTenderId(),periodNow+1);
            }
        }
        
        accountWebList.setBorrowNid(borrowTender.getBorrowNid()); // 出借编号
        accountWebList.setUserId(borrowTender.getUserId()); // 出借者
        accountWebList.setAmount(transfer.getTransAmt()); // 优惠券出借受益
        accountWebList.setType(CustomConstants.TYPE_OUT); // 类型1收入,2支出
        String remark = "";
        if(currentRecover.getCouponType()==1){
        	// 体验金
        	accountWebList.setTrade(CustomConstants.TRADE_COUPON_TYJ); 
            //体验金收益
            accountWebList.setTradeType(CustomConstants.TRADE_COUPON_SY); 
        }else if(currentRecover.getCouponType()==2){
        	// 加息券
        	accountWebList.setTrade(CustomConstants.TRADE_COUPON_JXQ); 
            //加息券回款
            accountWebList.setTradeType(CustomConstants.TRADE_COUPON_HK); 
        }else if(currentRecover.getCouponType()==3){
        	// 代金券
        	accountWebList.setTrade(CustomConstants.TRADE_COUPON_DJQ); 
            // 代金券回款
            accountWebList.setTradeType(CustomConstants.TRADE_COUPON_DJ); 
            
        }
        remark = "项目编号："+borrowTender.getBorrowNid()+"<br />优惠券:"+couponUserCode;
        accountWebList.setRemark(remark); // 出借编号
        accountWebList.setCreateTime(GetDate.getNowTime10());
        int accountWebListCnt = insertAccountWebList(accountWebList);
        if (accountWebListCnt == 0) {
            throw new Exception("网站收支记录(huiyingdai_account_web_list)更新失败！" + "[出借订单号：" + borrowTender.getNid() + "]");
        }
        
        // 添加红包账户明细
        BankMerchantAccount nowBankMerchantAccount = this.getBankMerchantAccount(resultBean.getAccountId());
        nowBankMerchantAccount.setAvailableBalance(nowBankMerchantAccount.getAvailableBalance().subtract(transfer.getTransAmt()));
        nowBankMerchantAccount.setAccountBalance(nowBankMerchantAccount.getAccountBalance().subtract(transfer.getTransAmt()));
        nowBankMerchantAccount.setUpdateTime(GetDate.getNowTime10());
        
        // 更新红包账户信息
        int updateCount = this.updateBankMerchantAccount(nowBankMerchantAccount);
        if(updateCount > 0){
            UserInfoCustomize userInfoCustomize = this.queryUserInfoByUserId(transfer.getUserId());
            
            // 添加红包明细
            BankMerchantAccountList bankMerchantAccountList = new BankMerchantAccountList();
            bankMerchantAccountList.setOrderId(transfer.getOrderId());
            bankMerchantAccountList.setUserId(transfer.getUserId());
            bankMerchantAccountList.setAccountId(transfer.getAccountId());
            bankMerchantAccountList.setAmount(transfer.getTransAmt());
            bankMerchantAccountList.setBankAccountCode(resultBean.getAccountId());
            bankMerchantAccountList.setBankAccountBalance(nowBankMerchantAccount.getAccountBalance());
            bankMerchantAccountList.setBankAccountFrost(nowBankMerchantAccount.getFrost());
            bankMerchantAccountList.setTransType(CustomConstants.BANK_MER_TRANS_TYPE_AUTOMATIC);
            bankMerchantAccountList.setType(CustomConstants.BANK_MER_TYPE_EXPENDITURE);
            bankMerchantAccountList.setStatus(CustomConstants.BANK_MER_TRANS_STATUS_SUCCESS);
            bankMerchantAccountList.setTxDate(Integer.parseInt(resultBean.getTxDate()));
            bankMerchantAccountList.setTxTime(Integer.parseInt(resultBean.getTxTime()));
            bankMerchantAccountList.setSeqNo(resultBean.getSeqNo());
            bankMerchantAccountList.setCreateTime(new Date());
            bankMerchantAccountList.setUpdateTime(new Date());
            bankMerchantAccountList.setCreateUserId(transfer.getUserId());
            bankMerchantAccountList.setUpdateUserId(transfer.getUserId());
            bankMerchantAccountList.setRegionName(userInfoCustomize.getRegionName());
            bankMerchantAccountList.setBranchName(userInfoCustomize.getBranchName());
            bankMerchantAccountList.setDepartmentName(userInfoCustomize.getDepartmentName());
            bankMerchantAccountList.setCreateUserName(userInfoCustomize.getUserName());
            bankMerchantAccountList.setUpdateUserName(userInfoCustomize.getUserName());
            bankMerchantAccountList.setRemark("优惠券还款异常修复");
            
            this.bankMerchantAccountListMapper.insertSelective(bankMerchantAccountList);
        }

        
        LogUtil.infoLog(TransferExceptionLogServiceImpl.class.toString(), "transferAfter", "-----------重新执行还款结束---"+ borrowTender.getBorrowNid() +"---------"+recover.getTransferId()+"---------------");

        List<Map<String, String>> retMsgList = new ArrayList<Map<String, String>>();
        Map<String, String> msg = new HashMap<String, String>();
        retMsgList.add(msg);
        msg.put(USERID, String.valueOf(borrowTender.getUserId()));
        msg.put(VAL_AMOUNT, transfer.getTransAmt() == null ? "0.00" : transfer.getTransAmt().toString());
        // 发送短信
        this.sendSmsCoupon(retMsgList);
        return true;
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
     * 发送短信(还款成功)
     *
     * @param userId
     */
    public void sendSms(List<Map<String, String>> msgList) {
        if (msgList != null && msgList.size() > 0) {
            for (Map<String, String> msg : msgList) {
                if (Validator.isNotNull(msg.get(USERID)) && NumberUtils.isNumber(msg.get(USERID)) && Validator.isNotNull(msg.get(VAL_BALANCE))) {
                    Users users = getUsersByUserId(Integer.valueOf(msg.get(USERID)));
                    if (users == null || Validator.isNull(users.getMobile()) || (users.getRecieveSms() != null && users.getRecieveSms() == 1)) {
                        return;
                    }
     			   SmsMessage smsMessage =
                           new SmsMessage(Integer.valueOf(msg.get(USERID)), msg, null, null, MessageDefine.SMSSENDFORUSER, null,
                           		CustomConstants.PARAM_TPL_SHOUDAOHUANKUAN, CustomConstants.CHANNEL_TYPE_NORMAL);
                  smsProcesser.gather(smsMessage);
                }
            }
        }
    }
    
    /**
     * 发送短信(优惠券还款成功)
     *
     * @param userId
     */
	public void sendSmsCoupon(List<Map<String, String>> msgList) {
        if (msgList != null && msgList.size() > 0) {
            for (Map<String, String> msg : msgList) {
                if (Validator.isNotNull(msg.get(USERID)) && NumberUtils.isNumber(msg.get(USERID)) && Validator.isNotNull(msg.get(VAL_AMOUNT))) {
                    Users users = getUsersByUserId(Integer.valueOf(msg.get(USERID)));
                    if (users == null || Validator.isNull(users.getMobile()) || (users.getRecieveSms() != null && users.getRecieveSms() == 1)) {
                        return;
                    }
                    SmsMessage smsMessage = new SmsMessage(Integer.valueOf(msg.get(USERID)), msg, null, null, MessageDefine.SMSSENDFORUSER, null,
                     		CustomConstants.PARAM_TPL_COUPON_PROFIT, CustomConstants.CHANNEL_TYPE_NORMAL);
					smsProcesser.gather(smsMessage);
                }
            }
        }
    }

    /**
     * 取得标的详情
     *
     * @return
     */
    public BorrowWithBLOBs getBorrow(String borrowNid) {
        BorrowExample example = new BorrowExample();
        BorrowExample.Criteria criteria = example.createCriteria();
        criteria.andBorrowNidEqualTo(borrowNid);
        example.setOrderByClause(" id asc ");
        List<BorrowWithBLOBs> list = this.borrowMapper.selectByExampleWithBLOBs(example);

        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 更新还款期
     * @param tenderNid 出借订单编号
     * @param resetRecoverFlg 0:非还款期，1;还款期
     * @param currentRecoverFlg 0:非还款期，1;还款期
     * @param period 期数
     */
    private void crRecoverPeriod(String tenderNid,int period){
        Map<String,Object> paramMapAll = new HashMap<String,Object>();
        paramMapAll.put("currentRecoverFlg", 0);
        paramMapAll.put("tenderNid", tenderNid);
        this.couponRecoverCustomizeMapper.crRecoverPeriod(paramMapAll);
        
        Map<String,Object> paramMapCurrent = new HashMap<String,Object>();
        paramMapCurrent.put("currentRecoverFlg", 1);
        paramMapCurrent.put("tenderNid", tenderNid);
        paramMapCurrent.put("period", period);
        this.couponRecoverCustomizeMapper.crRecoverPeriod(paramMapCurrent);
        
    }

    /**
     * 根据订单编号取得该订单的还款列表
     * @param realTenderId
     * @return
     */
    private CouponRecoverCustomize getCurrentCouponRecover(String couponTenderNid,int periodNow){
        
        Map<String,Object> paramMap = new HashMap<String ,Object>();
        paramMap.put("tenderNid", couponTenderNid);
        paramMap.put("periodNow", periodNow);
        return this.couponRecoverCustomizeMapper.selectCurrentCouponRecoverFailed(paramMap);
        
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
}
