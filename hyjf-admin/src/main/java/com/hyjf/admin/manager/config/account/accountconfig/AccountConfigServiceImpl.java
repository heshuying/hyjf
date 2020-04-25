package com.hyjf.admin.manager.config.account.accountconfig;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.MerchantAccount;
import com.hyjf.mybatis.model.auto.MerchantAccountExample;

@Service
public class AccountConfigServiceImpl extends BaseServiceImpl implements AccountConfigService {

    @Override
    public int countSubAccountList(AccountConfigBean form) {
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
    public List<MerchantAccount> searchSubAccountList(AccountConfigBean form, int limitStart, int limitEnd) {
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

        example.setOrderByClause("`update_time` DESC , `id` ASC ");

        return merchantAccountMapper.selectByExample(example);
    }

    @Override
    public MerchantAccount getAccountListInfo(String ids) {
        return merchantAccountMapper.selectByPrimaryKey(Integer.parseInt(ids));
    }

    @Override
    public int insertAccountListInfo(AccountConfigBean form) {
        MerchantAccount record = new MerchantAccount();
        BeanUtils.copyProperties(form, record);
        int nowTime = GetDate.getNowTime10();
        record.setCreateTime(nowTime);
        record.setUpdateTime(nowTime);
        return merchantAccountMapper.insertSelective(record);
    }

    @Override
    public int updateAccountListInfo(AccountConfigBean form) {
        MerchantAccount record = new MerchantAccount();
        BeanUtils.copyProperties(form, record);
        int nowTime = GetDate.getNowTime10();
        record.setUpdateTime(nowTime);
        if (StringUtils.isNotEmpty(form.getIds())) {
            record.setId(Integer.parseInt(form.getIds()));
        }
        return merchantAccountMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int countAccountListInfoBySubAccountName(String ids, String subAccountName) {
        MerchantAccountExample example = new MerchantAccountExample();
        MerchantAccountExample.Criteria cra = example.createCriteria();
        cra.andSubAccountNameEqualTo(subAccountName);
        if (StringUtils.isNotEmpty(ids)) {
            cra.andIdNotEqualTo(Integer.parseInt(ids));
        }
        return merchantAccountMapper.countByExample(example);
    }

    @Override
    public int countAccountListInfoBySubAccountCode(String ids, String subAccountCode) {
        MerchantAccountExample example = new MerchantAccountExample();
        MerchantAccountExample.Criteria cra = example.createCriteria();
        cra.andSubAccountCodeEqualTo(subAccountCode);
        if (StringUtils.isNotEmpty(ids)) {
            cra.andIdNotEqualTo(Integer.parseInt(ids));
        }
        return merchantAccountMapper.countByExample(example);
    }

}
