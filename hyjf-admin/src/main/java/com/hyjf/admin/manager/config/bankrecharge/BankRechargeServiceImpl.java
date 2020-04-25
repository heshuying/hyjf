package com.hyjf.admin.manager.config.bankrecharge;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.auto.BankConfigExample;
import com.hyjf.mybatis.model.auto.BankRechargeLimitConfig;
import com.hyjf.mybatis.model.auto.BankRechargeLimitConfigExample;

@Service
public class BankRechargeServiceImpl extends BaseServiceImpl implements BankRechargeService {
   

    /**
     * 获取列表列表
     * 
     * @return
     */
    public List<BankRechargeLimitConfig> getRecordList(BankRechargeLimitConfig bankRechargeLimitConfig, int limitStart, int limitEnd) {
    	BankRechargeLimitConfigExample example = new BankRechargeLimitConfigExample();

        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
//        BankRechargeLimitConfigExample.Criteria criteria = example.createCriteria();
        // 条件查询
        example.setOrderByClause("create_time");
        return bankRechargeLimitConfigMapper.selectByExample(example);
    }

    /**
     * 获取单个维护
     * 
     * @return
     */
    public BankRechargeLimitConfig getRecord(Integer record) {
    	BankRechargeLimitConfig FeeConfig = bankRechargeLimitConfigMapper.selectByPrimaryKey(record);
        return FeeConfig;
    }

    /**
     * 根据主键判断维护中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(BankRechargeLimitConfig record) {
        if (record.getId() == null) {
            return false;
        }
        BankRechargeLimitConfigExample example = new BankRechargeLimitConfigExample();
        BankRechargeLimitConfigExample.Criteria cra = example.createCriteria();
        cra.andIdEqualTo(record.getId());
        List<BankRechargeLimitConfig> BankRechargeLimitConfigs = bankRechargeLimitConfigMapper.selectByExample(example);
        if (BankRechargeLimitConfigs != null && BankRechargeLimitConfigs.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 维护插入
     * 
     * @param record
     */
    public void insertRecord(BankRechargeLimitConfig record) {
        record.setCreateTime(GetDate.getDate());
        record.setUpdateTime(GetDate.getDate());
//        record.setLogo("1");
        bankRechargeLimitConfigMapper.insertSelective(record);
    }

    /**
     * 维护更新
     * 
     * @param record
     */
    public void updateRecord(BankRechargeLimitConfig record) {
        record.setUpdateTime(GetDate.getDate());
//        record.setLogo("1");
        bankRechargeLimitConfigMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据主键删除
     * 
     * @param recordList
     */
    @Override
    public void deleteRecord(List<Integer> recordList) {
        for (Integer id : recordList) {
        	bankRechargeLimitConfigMapper.deleteByPrimaryKey(id);
        }
    }

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param bankRechargeLimitConfig
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<BankRechargeLimitConfig> exportRecordList(BankRechargeLimitConfig bankRechargeLimitConfig) {
	 	BankRechargeLimitConfigExample example = new BankRechargeLimitConfigExample();
        // 条件查询
	 	example.setOrderByClause("create_time");
        return bankRechargeLimitConfigMapper.selectByExample(example);
			
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param bankId
	 * @param recordId
	 * @return
	 * @author Michael
	 */
		
	@Override
	public int bankIsExists(Integer bankId, Integer recordId) {
		BankRechargeLimitConfigExample example = new BankRechargeLimitConfigExample();
		BankRechargeLimitConfigExample.Criteria cra = example.createCriteria();
		cra.andBankIdEqualTo(bankId);
		if (recordId != null && recordId != 0) {
			cra.andIdNotEqualTo(recordId);
		}
		List<BankRechargeLimitConfig> list = this.bankRechargeLimitConfigMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return 1;
		}
		return 0;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param bankCode
	 * @param singleTransQuota
	 * @param cardDailyTransQuota
	 * @author Michael
	 */
		
	@Override
	public void updateBankRechargeConfig(String bankCode, String singleTransQuota, String cardDailyTransQuota) {
		if(StringUtils.isEmpty(bankCode)){
			return;
		}
		//获取银行卡信息
		BankConfig bank = this.getBankConfigByCode(bankCode);
		if(bank == null){
			return;
		}
		//获取快捷卡配置信息
		BankRechargeLimitConfigExample example = new BankRechargeLimitConfigExample();
		BankRechargeLimitConfigExample.Criteria cra = example.createCriteria();
		cra.andBankIdEqualTo(bank.getId());
		List<BankRechargeLimitConfig> list = this.bankRechargeLimitConfigMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			for(int i = 0;i < list.size();i++){
				BankRechargeLimitConfig config = list.get(i);
				if(StringUtils.isEmpty(singleTransQuota)){
					config.setSingleQuota(null);
				}else{
					config.setSingleQuota(new BigDecimal(singleTransQuota.replaceAll(",", "")));
				}
				if(StringUtils.isEmpty(cardDailyTransQuota)){
					config.setSingleCardQuota(null);
				}else{
					config.setSingleCardQuota(new BigDecimal(cardDailyTransQuota.replaceAll(",", "")));
				}
				//更新数据
				this.bankRechargeLimitConfigMapper.updateByPrimaryKeySelective(config);
			}
		}
	}
	/**
	 * 根据银行编码获取银行信息
	 * @param code
	 * @return
	 */
	public BankConfig getBankConfigByCode(String code){
		//获取银行卡信息
		BankConfigExample example = new BankConfigExample();
        BankConfigExample.Criteria cra = example.createCriteria();
        cra.andQuickPaymentEqualTo(1);//支持快捷支付
        cra.andCodeEqualTo(code);
		List<BankConfig> bankList = this.bankConfigMapper.selectByExample(example);
		if(bankList != null && bankList.size() > 0){
			return bankList.get(0);
		}
		return null;
	}
}
