package com.hyjf.api.surong.user.recharge;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.auto.BankRechargeLimitConfig;
import com.hyjf.mybatis.model.auto.FeeConfig;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.QpCardInfo;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;

public interface RdfRechargeService extends BaseService{
	
	Users findUserByMobile(String mobile);
	
	QpCardInfo getUserQpCard(Integer userId);

    FeeConfig getQpCardFee(String code);

    String getUserIdcard(Integer userId);

    int updateBeforeRecharge(ChinapnrBean bean, Map<String, String> params);

    int getAccountRechargeCnt(String ordId);

    int updateAccountRecharge(ChinapnrBean bean, Integer status);

    /**
     * 获取充值手续费
     * 
     * @param bankCode
     * @param money
     * @return
     */
    public BigDecimal getFee(String bankCode, BigDecimal money, String gateBusiId);

    /**
     * 
     * 处理充值响应
     * @author renxingchen
     * @param uuid
     * @param ip
     * @param bean
     * @param modelAndView
     * @return
     */
    ModelAndView handleRechargeInfo(String ip, String feeFrom,Integer userId, ChinapnrBean bean,
        ModelAndView modelAndView);

    List<AccountChinapnr> getUserChinapnr(String usrCustId);

    
    
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
