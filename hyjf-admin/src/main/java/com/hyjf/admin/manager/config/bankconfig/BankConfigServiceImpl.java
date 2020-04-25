package com.hyjf.admin.manager.config.bankconfig;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.auto.BankConfigExample;

@Service
public class BankConfigServiceImpl extends BaseServiceImpl implements BankConfigService {
   

    /**
     * 获取列表列表
     * 
     * @return
     */
    public List<BankConfig> getRecordList(BankConfig bankConfig, int limitStart, int limitEnd) {
    	BankConfigExample example = new BankConfigExample();

        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        com.hyjf.mybatis.model.auto.BankConfigExample.Criteria criteria = example.createCriteria();
        // 条件查询
        if (bankConfig.getName() != null) {
            criteria.andNameEqualTo(bankConfig.getName());
        }
        if (bankConfig.getCode() != null) {
            criteria.andCodeEqualTo(bankConfig.getCode());
        }
        return bankConfigMapper.selectByExample(example);
    }

    /**
     * 获取单个维护
     * 
     * @return
     */
    public BankConfig getRecord(Integer record) {
    	BankConfig FeeConfig = bankConfigMapper.selectByPrimaryKey(record);
        return FeeConfig;
    }

    /**
     * 根据主键判断维护中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(BankConfig record) {
        if (record.getId() == null) {
            return false;
        }
        BankConfigExample example = new BankConfigExample();
        BankConfigExample.Criteria cra = example.createCriteria();
        cra.andIdEqualTo(record.getId());
        List<BankConfig> bankConfigs = bankConfigMapper.selectByExample(example);
        if (bankConfigs != null && bankConfigs.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 维护插入
     * 
     * @param record
     */
    public void insertRecord(BankConfig record) {
        record.setCreateTime(GetDate.getDate());
        record.setUpdateTime(GetDate.getDate());
//        record.setLogo("1");
        bankConfigMapper.insertSelective(record);
    }

    /**
     * 维护更新
     * 
     * @param record
     */
    public void updateRecord(BankConfig record) {
        record.setUpdateTime(GetDate.getDate());
//        record.setLogo("1");
        bankConfigMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据主键删除
     * 
     * @param recordList
     */
    @Override
    public void deleteRecord(List<Integer> recordList) {
        for (Integer id : recordList) {
        	bankConfigMapper.deleteByPrimaryKey(id);
        }
    }

	/**
	 * 获取银行列表(快捷支付卡)
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<BankConfig> getBankRecordList() {
		BankConfigExample example = new BankConfigExample();
        BankConfigExample.Criteria cra = example.createCriteria();
        cra.andQuickPaymentEqualTo(1);//支持快捷支付
        example.setOrderByClause(" id");
        return bankConfigMapper.selectByExample(example);
	}

}
