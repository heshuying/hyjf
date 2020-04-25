package com.hyjf.admin.manager.config.banksetting;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.BanksConfig;
import com.hyjf.mybatis.model.auto.BanksConfigExample;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankSettingServiceImpl extends BaseServiceImpl implements BankSettingService {
   

    /**
     * 获取列表列表
     * 
     * @return
     */
    public List<BanksConfig> getRecordList(BanksConfig BanksConfig, int limitStart, int limitEnd) {
        BanksConfigExample example = new BanksConfigExample();

        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        example.setOrderByClause("sort_id ASC");
        BanksConfigExample.Criteria criteria = example.createCriteria();
        criteria.andDelFlgEqualTo(0);
        
        // 条件查询
        if (BanksConfig.getBankName() != null) {
            if(!BanksConfig.getBankName().isEmpty()){
                criteria.andBankNameEqualTo(BanksConfig.getBankName()); 
            }
                
        }
        if (BanksConfig.getPayAllianceCode() != null) {
            if(!BanksConfig.getPayAllianceCode().isEmpty()){
                criteria.andPayAllianceCodeEqualTo(BanksConfig.getPayAllianceCode());
            }
            
        }
        return banksConfigMapper.selectByExample(example);
    }

    /**
     * 获取单个维护
     * 
     * @return
     */
    public BanksConfig getRecord(Integer record) {
        BanksConfig FeeConfig = banksConfigMapper.selectByPrimaryKey(record);
        return FeeConfig;
    }

    /**
     * 根据主键判断维护中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(BanksConfig record) {
        if (record.getId() == null) {
            return false;
        }
        BanksConfigExample example = new BanksConfigExample();
        BanksConfigExample.Criteria cra = example.createCriteria();
        cra.andIdEqualTo(record.getId());
        List<BanksConfig> bankConfigs = banksConfigMapper.selectByExample(example);
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
    public void insertRecord(BanksConfig record) {
        record.setCreateTime(GetDate.getNowTime10());
        record.setUpdateTime(GetDate.getNowTime10());
//        record.setLogo("1");
        banksConfigMapper.insertSelective(record);
    }

    /**
     * 维护更新
     * 
     * @param record
     */
    public void updateRecord(BanksConfig record) {
        record.setUpdateTime(GetDate.getNowTime10());
//        record.setLogo("1");
        banksConfigMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据主键删除
     * 
     * @param recordList
     */
    @Override
    public void deleteRecord(List<Integer> recordList) {
        for (Integer id : recordList) {
        	BanksConfig record = new BanksConfig();
        	record.setId(id);
        	record.setDelFlg(1);
            banksConfigMapper.updateByPrimaryKeySelective(record);
        }
    }

	/**
	 * 获取银行列表(快捷支付卡)
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<BanksConfig> getBankRecordList() {
	    BanksConfigExample example = new BanksConfigExample();
	    BanksConfigExample.Criteria cra = example.createCriteria();
        cra.andQuickPaymentEqualTo(1);//支持快捷支付
        example.setOrderByClause(" id");
        return banksConfigMapper.selectByExample(example);
	}

    @Override
    public List<BanksConfig> exportRecordList(BanksConfig record) {
        BanksConfigExample example = new BanksConfigExample();
        // 条件查询
        example.setOrderByClause("sort_id ASC");
        return banksConfigMapper.selectByExample(example);
    }

}
