package com.hyjf.admin.manager.config.account.accountbalance;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.MerchantAccount;
import com.hyjf.mybatis.model.auto.MerchantAccountExample;

/**
 * 
 * 余额监控Service
 * @author liuyang
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月6日
 * @see 下午5:39:36
 */
@Service
public class AccountBalanceMonitoringServiceImpl extends BaseServiceImpl implements AccountBalanceMonitoringService {

    @Override
    public int countSubAccountList(AccountBalanceMonitoringBean form) {
        MerchantAccountExample example = new MerchantAccountExample();
        MerchantAccountExample.Criteria cra = example.createCriteria();
        // 子账户名称
        if (StringUtils.isNotEmpty(form.getSubAccountNameSear())) {
            cra.andSubAccountNameLike("%" + form.getSubAccountNameSear() + "%");
        }
        // 子账户类型
        if (StringUtils.isNotEmpty(form.getSubAccountTypeSear())) {
            cra.andSubAccountTypeEqualTo(form.getSubAccountTypeSear());
        }
        return merchantAccountMapper.countByExample(example);
    }

    @Override
    public List<MerchantAccount> searchSubAccountList(AccountBalanceMonitoringBean form, int limitStart, int limitEnd) {
        MerchantAccountExample example = new MerchantAccountExample();
        MerchantAccountExample.Criteria cra = example.createCriteria();
        // 子账户名称
        if (StringUtils.isNotEmpty(form.getSubAccountNameSear())) {
            cra.andSubAccountNameLike("%" + form.getSubAccountNameSear() + "%");
        }
        // 子账户类型
        if (StringUtils.isNotEmpty(form.getSubAccountTypeSear())) {
            cra.andSubAccountTypeEqualTo(form.getSubAccountTypeSear());
        }

        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }

        example.setOrderByClause("`update_time` DESC, `id` ASC");

        return merchantAccountMapper.selectByExample(example);
    }

    @Override
    public int updateSubAccountList(List<SubAccountListResult> updateList) {
        int ret = 0;
        int nowTime = GetDate.getNowTime10();
        if (updateList != null && updateList.size() > 0) {
            for (int i = 0; i < updateList.size(); i++) {
                SubAccountListResult record = updateList.get(i);
                MerchantAccount merchantAccount = new MerchantAccount();
                // 如果数据有更新
                if (record.isUpdateFlg()) {
                    BeanUtils.copyProperties(record, merchantAccount);
                    merchantAccount.setUpdateTime(nowTime);
                    merchantAccount.setBalanceLowerLimit(merchantAccount.getBalanceLowerLimit() == null ? 0
                            : merchantAccount.getBalanceLowerLimit());
                    merchantAccount.setTransferIntoRatio(merchantAccount.getTransferIntoRatio() == null ? 0
                            : merchantAccount.getTransferIntoRatio());
                    ret += this.merchantAccountMapper.updateByPrimaryKeySelective(merchantAccount);
                }
            }
        }
        return ret;
    }

    @Override
    public List<MerchantAccount> searchAllSubAccountList(AccountBalanceMonitoringBean form, int limitStart, int limitEnd) {
        MerchantAccountExample example = new MerchantAccountExample();
        example.setOrderByClause("`update_time` DESC , `id` ASC");
        return merchantAccountMapper.selectByExample(example);
    }

}
