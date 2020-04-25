/**
 * Description:用户充值
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: GOGTZ-T
 * @version: 1.0
 *           Created at: 2015年12月4日 下午1:45:13
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.web.user.recharge;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

import com.hyjf.mybatis.model.auto.AccountBank;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.auto.BankRechargeLimitConfig;
import com.hyjf.mybatis.model.auto.FeeConfig;
import com.hyjf.mybatis.model.customize.QpCardInfo;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.web.BaseService;

public interface UserRechargeService extends BaseService {

    /**
     * 更新充值状态
     *
     * @param bean
     * @return
     */
    public int updateAccountRecharge(ChinapnrBean bean, Integer status);

    /**
     * 获取用户的身份证号
     *
     * @param userId
     * @return 用户的身份证号
     */
    public String getUserIdcard(Integer userId);

    /**
     * 获取用户的银行卡信息
     *
     * @param userId
     * @return 用户的银行卡信息
     */
    public AccountBank getBankInfo(Integer userId, String bankCode);

    /**
     * 判断是否充值成功
     *
     * @param ordId
     * @return
     */
    public int getAccountRechargeCnt(String ordId);

    /**
     * 获取充值手续费
     * @param bankCode
     * @param money
     * @return
     */
    public BigDecimal getFee(String bankCode, BigDecimal money, String gateBusiId);

    /**
     * 
     * 插入充值记录
     * @author renxingchen
     * @param bean
     * @param params
     * @return
     */
    public int insertRechargeInfo(ChinapnrBean bean, Map<String, String> params);

    /**
     * 
     * 处理充值信息并返回响应页面
     * @author renxingchen
     * @param ip
     * @param feeFrom
     * @param userId
     * @param bean
     * @param modelAndView
     * @return
     */
    public ModelAndView handleRechargeInfo(String ip, String feeFrom, Integer userId, ChinapnrBean bean,
        ModelAndView modelAndView);

    /**
     * 
     * 查询银行的网关充值和快捷充值费率
     * @author renxingchen
     * @param bankCode
     * @return
     */
    public FeeConfig getCardCharges(String bankCode);

    /**
     * 
     * 查询用户的快捷充值卡
     * @author renxingchen
     * @param userId
     * @return
     */
    public QpCardInfo getUserQpCard(Integer userId);

    /**
     * 
     * 获取可以展示在页面的银行卡列表
     * @author renxingchen
     * @return
     */
    public List<BankConfig> getPersonBankInfos();

    /**
     * 
     * 获取可以展示在页面的银行卡列表
     * @author renxingchen
     * @return
     */
    public List<BankConfig> getCorpBankInfos();

    /**
     * 获取银行列表(快捷支付卡)
     * @return
     * @author Michael
     */
    public List<BankConfig> getBankQuickList();

    /**
     * 获取手续费列表
     * 
     * @return
     */
    public List<BankRechargeLimitConfig> getRechargeLimitList();

}
