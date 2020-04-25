package com.hyjf.admin.manager.user.bankcardlog;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.BankAccountLog;
import com.hyjf.mybatis.model.auto.BankAccountLogExample;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.auto.BankConfigExample;

@Service
public class BankAccountLogServiceImpl extends BaseServiceImpl implements BankAccountLogService {

    /**
     * 获取列表
     * 
     * @return
     */
    @Override
	public List<BankAccountLog> getRecordList(BankAccountLogBean bean, int limitStart, int limitEnd) {
        BankAccountLogExample example = new BankAccountLogExample();
        BankAccountLogExample.Criteria criteria = example.createCriteria();
        // 条件查询
        if (StringUtils.isNotEmpty(bean.getBankIdSrch())) {
            criteria.andBankCodeEqualTo(bean.getBankIdSrch());
        }
        if (StringUtils.isNotEmpty(bean.getUserNameSrch())) {
            criteria.andUserNameLike("%"+bean.getUserNameSrch()+"%");
        }
        if(StringUtils.isNotEmpty(bean.getStartTime())){
        	criteria.andCreateTimeGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(bean.getStartTime())));
        }
        if(StringUtils.isNotEmpty(bean.getEndTime())){
        	criteria.andCreateTimeLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayEnd(bean.getEndTime())));
        }
        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        example.setOrderByClause("create_time Desc");
        return this.bankAccountLogMapper.selectByExample(example);
    }

	/**
	 * 获取列表记录数
	 * @param form
	 * @return
	 * @author Michael
	 */
		
	@Override
	public Integer getRecordCount(BankAccountLogBean bean) {
        BankAccountLogExample example = new BankAccountLogExample();
        BankAccountLogExample.Criteria criteria = example.createCriteria();
        // 条件查询
        if (StringUtils.isNotEmpty(bean.getBankIdSrch())) {
            criteria.andBankCodeEqualTo(bean.getBankIdSrch());
        }
        if (StringUtils.isNotEmpty(bean.getUserNameSrch())) {
            criteria.andUserNameLike("%"+bean.getUserNameSrch()+"%");
        }
        if(StringUtils.isNotEmpty(bean.getStartTime())){
        	criteria.andCreateTimeGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(bean.getStartTime())));
        }
        if(StringUtils.isNotEmpty(bean.getEndTime())){
        	criteria.andCreateTimeLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayEnd(bean.getEndTime())));
        }
        return this.bankAccountLogMapper.countByExample(example);
	}
    
	

	/**
	 * 获取银行列表
	 * 
	 * @param string
	 * @return
	 * @author Administrator
	 */
	@Override
	public List<BankConfig> getBankcardList() {
		List<BankConfig> banks = bankConfigMapper.selectByExample(new BankConfigExample());
		return banks;
	}
}
