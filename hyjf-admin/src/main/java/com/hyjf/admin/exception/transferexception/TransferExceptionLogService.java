package com.hyjf.admin.exception.transferexception;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.BorrowTenderCpn;
import com.hyjf.mybatis.model.auto.TransferExceptionLog;
import com.hyjf.mybatis.model.customize.admin.AdminTransferExceptionLogCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

/**
 * service接口定义
 */
public interface TransferExceptionLogService extends BaseService {

    /**
     * 
     * 获取转账异常列表
     * @author hsy
     * @param transferExceptionLogCustomize
     * @return
     */
	public List<AdminTransferExceptionLogCustomize> getRecordList(Map<String, Object> paraMap);

	/**
	 * 获取转账异常记录数
	 * @author hsy
	 * @return
	 */
	public Integer countRecord(Map<String, Object> paraMap);

	/**
	 * 根据UUID更新一条记录
	 * @author hsy
	 * @param transferExceptionLogCustomize
	 * @return
	 */
    int updateRecordByUUID(AdminTransferExceptionLogCustomize transferExceptionLogCustomize);

    /**
     * 根据uuid查询一条记录
     */
    public TransferExceptionLog getRecordByUUID(String uuid);

    public abstract int countAccountListByNidCoupon(String nid);

    public Account getAccountByUserId(Integer userId);

    public int updateOfRepayTender(Account account);

    public abstract int insertAccountList(AccountList accountList);

    public BorrowTenderCpn getCouponTenderInfo(String couponTenderNid);
    
    public boolean transferAfter(TransferExceptionLog transfer, BankCallBean resultBean) throws Exception;

    public int updateRecordByUUID(TransferExceptionLog transferExceptionLog);
	

}