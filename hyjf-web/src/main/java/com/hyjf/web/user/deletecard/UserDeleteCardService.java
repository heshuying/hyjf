/**
 * Description:用户删除卡: 
 */
package com.hyjf.web.user.deletecard;

import com.hyjf.mybatis.model.auto.AccountBank;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecord;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.web.BaseService;

public interface UserDeleteCardService extends BaseService {

    /**
     * 用户删除卡后处理
     * 
     * @param bean
     * @return
     */
    public String updateAfterDeleteCard(ChinapnrBean bean, Integer userId);
    
    /**
     * 获取用户的身份证号
     * 
     * @param userId
     * @return 用户的身份证号
     */
    public String getUserIdcard(Integer userId);

    /**
     * 获取用户的银行卡号
     * @param cardId
     * @return
     */
	public AccountBank getUserBankCard(int cardId);

    /**
     * 根据电子账号查询用户在江西银行的信息
     * @param userId
     * @param accountId
     * @return
     */
    public BankCallBean getBankBalanceCall(Integer userId, String accountId);
}
