package com.hyjf.admin.finance.bank.merchant;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankMerchantAccount;
import com.hyjf.mybatis.model.auto.BankMerchantAccountInfo;
import com.hyjf.mybatis.model.auto.BankMerchantAccountList;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

public interface BankMerchantAccountService extends BaseService {

	/**
	 * 获取商户子账户列表
	 * 
	 * @param BankMerchantAccountListBean
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */

	public List<BankMerchantAccount> selectRecordList(BankMerchantAccountListBean form, int limitStart,int limitEnd);

	/**
	 * 获取商户子账户总数
	 * 
	 * @param BankMerchantAccountListBean
	 * @return
	 */

	public int queryRecordTotal(BankMerchantAccountListBean form);

	/**
	 * 更新商户子账户金额
	 * @return 
	 * 
	 */
		
	public List<BankMerchantAccount> updateBankMerchantAccount(List<BankMerchantAccount> recordList);
	
	
	
	/**
     * 查询用户银行交易明细
     * @param accountId 电子账号
     * @param startDate 起始日期 （YYYYMMDD）
     * @param endDate   结束日期（YYYYMMDD）
     * @param type  0-所有交易 1-转入交易 2-转出交易 9-指定交易类型
     * @param transType  type=9必填，后台交易类型
     * @param pageNum  
     * @param pageSize
     * @return
     */
    public BankCallBean queryAccountDetails(Integer userId,String accountId,String startDate,String endDate,String type,String transType,String pageNum,String pageSize,
        String inpDate,String inpTime,String relDate,String traceNo);
    
    /**
     * 处理线下充值
     * @param bankMerchantAccount
     * @param transAmount
     * @param string 
     * @param user 
     * @return
     */
    public boolean insertAccountDetails(BankMerchantAccount bankMerchantAccount,SynBalanceBean synBalanceBean, String username, String ip);
    /**
     * 
     * 根据电子账户查询电子账户信息
     * @author pcc
     * @param accountCode
     * @return
     */
    public BankOpenAccount getBankOpenAccountByCode(String accountCode);
    /**
     * 
     * 获取平台子账户信息
     * @author pcc
     * @param accountCode
     * @return
     */
    public BankMerchantAccount getBankMerchantAccount(String accountCode);

    public String getWithdrawFee(Integer userId, int integer, BigDecimal bigDecimal);

    public String check(String param, String accountCode);

    public BankCard getBankCardByAccountCode(String accountCode);

    public int updateBeforeCash(BankCallBean bean, Map<String, String> params, BankMerchantAccount bankMerchantAccount);

    public BankMerchantAccountList getAccountWithdrawByOrdId(String logOrderId);

    public String checkCashResult(BankCallBean bean);

    public JSONObject getMsgData(String logOrderId);

    public int updateAccountWithdrawByOrdId(String logOrderId, String reason);

    public JSONObject handlerAfterCash(BankCallBean bean, Map<String, String> params) throws Exception;

    public int updateBankMerchantAccount(String accountCode, BigDecimal currBalance, BigDecimal balance,
        BigDecimal frost);

    public void updateBankMerchantAccountIsSetPassword(String accountId, int flag);

    public BankMerchantAccountInfo getBankMerchantAccountInfoByCode(String accountCode);

    /**
     * 
     * 用户充值前处理
     * @author sunss
     * @param bean
     * @param params
     * @param bankMerchantAccount
     * @return
     */
    public int updateBeforeRecharge(BankCallBean bean, Map<String, String> params,
        BankMerchantAccount bankMerchantAccount);

    /**
     * 
     * 充值后处理
     * 
     * @author sunss
     * @param bean
     * @param params
     */
    public JSONObject handlerAfterRecharge(BankCallBean bean, Map<String, String> params);


}
