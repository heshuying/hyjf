package com.hyjf.admin.manager.config.feeconfig;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.FeeConfig;
import com.hyjf.mybatis.model.auto.FeeConfigExample;

@Service
public class FeeConfigServiceImpl extends BaseServiceImpl implements FeeConfigService {
   

    /**
     * 获取列表列表
     * 
     * @return
     */
    public List<FeeConfig> getRecordList(FeeConfig feeConfig, int limitStart, int limitEnd) {
    	FeeConfigExample example = new FeeConfigExample();

        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        com.hyjf.mybatis.model.auto.FeeConfigExample.Criteria criteria = example.createCriteria();
        // 条件查询
        if (feeConfig.getName() != null) {
            criteria.andNameEqualTo(feeConfig.getName());
        }
        return feeConfigMapper.selectByExample(example);
    }

    /**
     * 获取单个维护
     * 
     * @return
     */
    public FeeConfig getRecord(Integer record) {
    	FeeConfig FeeConfig = feeConfigMapper.selectByPrimaryKey(record);
        return FeeConfig;
    }

    /**
     * 根据主键判断维护中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(FeeConfig record) {
        if (record.getId() == null) {
            return false;
        }
        FeeConfigExample example = new FeeConfigExample();
        FeeConfigExample.Criteria cra = example.createCriteria();
        cra.andIdEqualTo(record.getId());
        List<FeeConfig> FeeConfig = feeConfigMapper.selectByExample(example);
        if (FeeConfig != null && FeeConfig.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 维护插入
     * 
     * @param record
     */
    public void insertRecord(FeeConfigBean record) {
        record.setCreateTime(GetDate.getDate());
        record.setUpdateTime(GetDate.getDate());
        feeConfigMapper.insertSelective(record);
    }

    /**
     * 维护更新
     * 
     * @param record
     */
    public void updateRecord(FeeConfigBean record) {
        record.setUpdateTime(GetDate.getDate());
        feeConfigMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据主键删除
     * 
     * @param recordList
     */
    @Override
    public void deleteRecord(List<Integer> recordList) {
        for (Integer id : recordList) {
        	feeConfigMapper.deleteByPrimaryKey(id);
        }
    }

}
