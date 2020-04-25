package com.hyjf.batch.couponinterest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.mybatis.customize.CustomizeMapper;
import com.hyjf.mybatis.model.auto.Account;

/**
 * 加息还款统计累计收益
 * 
 * @author liuyang
 *
 */
@Service
public class CouponInterestServiceImpl extends CustomizeMapper implements CouponInterestService {

	/**
	 * 检索待更新用户数据
	 * 
	 * @return
	 */
	@Override
    public List<Map<String,Object>> getDataForUpdate() {
		return couponAccountUpdateCustomizeMapper.selectDataForAccountUpdate();
	}

	/**
	 * 循环更新出借人账户信息
	 * 
	 * @param increaseInterestLoan
	 * @throws Exception
	 */
	@Override
    public void updateCouponUserAccount(List<Map<String, Object>> dataList) {
	    
	    if (dataList != null && dataList.size() > 0) {
            for (Map<String, Object> data : dataList) {
                Object userId = data.get("user_id");
                Object recoverAccount = data.get("recover_account");
                if (userId == null || recoverAccount == null) {
                    continue;
                }
                BigDecimal recoverAccountDecimal = (BigDecimal)recoverAccount;
                
                // 取得出借用户ID
                Account newAccount = new Account();
                newAccount.setUserId((Integer)userId);
                newAccount.setBankTotal(recoverAccountDecimal);// 账户总资产
                newAccount.setBankAwait(recoverAccountDecimal);// 待收总额
                newAccount.setBankAwaitInterest(recoverAccountDecimal);// 待收收益
                // 更新出借人加息还款的银行总资产,待收总额,待收收益
                boolean isUpdateFlag = this.adminAccountCustomizeMapper.updateTenderUserAccount(newAccount) > 0 ? true : false;
                if (!isUpdateFlag) {
                    System.err.println("债权迁移后,更新出借人优惠券的银行总资产,待收总额,待收收益失败,出借用户ID:" + userId + " 金额：" + recoverAccountDecimal);
                }else {
                    System.out.println("债权迁移后,更新出借人优惠券的银行总资产,待收总额,待收收益成功,出借用户ID:" + userId + " 金额：" + recoverAccountDecimal);
                }
                
            }
        }
	    
	}
}
