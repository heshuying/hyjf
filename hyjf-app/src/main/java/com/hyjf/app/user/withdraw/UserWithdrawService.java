/**
 * Description:用户充值
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: GOGTZ-T
 * @version: 1.0
 * Created at: 2015年12月4日 下午1:45:13
 * Modification History:
 * Modified by :
 */
package com.hyjf.app.user.withdraw;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.auto.AccountBank;
import com.hyjf.mybatis.model.auto.Accountwithdraw;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;

public interface UserWithdrawService extends BaseService {

    /**
     * 根据用户ID取得该用户的提现卡
     *
     * @param userId
     * @return
     */
    public List<AccountBank> getBankCardByUserId(Integer userId);

    /**
     * 根据code取得银行信息
     *
     * @param code
     * @return
     */
    public BankConfig getBankInfo(String code);

    /**
     * 用户充值前处理
     *
     * @param bean
     * @return
     */
    public boolean updateBeforeCash(ChinapnrBean bean, Map<String, String> params);

    /**
     * 用户充值后处理
     *
     * @param bean
     * @return
     */
    public boolean handlerAfterCash(ChinapnrBean bean, Map<String, String> params);

    /**
     * 获取用户的身份证号
     *
     * @param userId
     * @return 用户的身份证号
     */
    public String getUserIdcard(Integer userId);

    /**
     * 判断是否提现成功
     *
     * @param ordId
     * @return
     */
    public Accountwithdraw getAccountWithdrawByOrdId(String ordId);

    /**
     * 更新提现表
     *
     * @param ordId
     * @return
     */
    public int updateAccountWithdrawByOrdId(String ordId, String reason);

    /**
     * 获取用户的银行卡信息
     *
     * @param userId
     * @return 用户的银行卡信息
     */
    public AccountBank getBankInfo(Integer userId, String bankId);

    /**
     * 获取用户的银行卡费率
     *
     * @param userId
     * @return 用户的银行卡费率
     */
    public String getWithdrawFee(Integer userId, String bankId, BigDecimal amount, String type);

    /**
     * 判断是否提现成功
     *
     * @param ordId
     * @return S:成功 F:失败
     */
    public String checkCashResult(String ordId);

    /**
     * 取得成功信息
     *
     * @param ordId
     * @return
     */
    public JSONObject getMsgData(String ordId);
}
