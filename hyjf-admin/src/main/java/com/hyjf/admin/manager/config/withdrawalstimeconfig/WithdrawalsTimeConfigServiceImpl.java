package com.hyjf.admin.manager.config.withdrawalstimeconfig;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.WithdrawalsTimeConfig;
import com.hyjf.mybatis.model.auto.WithdrawalsTimeConfigExample;

@Service
public class WithdrawalsTimeConfigServiceImpl extends BaseServiceImpl implements WithdrawalsTimeConfigService {
   

    /**
     * 获取列表列表
     * 
     * @return
     */
    public List<WithdrawalsTimeConfig> getRecordList(WithdrawalsTimeConfig withdrawalsTimeConfig, int limitStart, int limitEnd) {
        WithdrawalsTimeConfigExample example = new WithdrawalsTimeConfigExample();

        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        example.createCriteria();
        return withdrawalsTimeConfigMapper.selectByExample(example);
    }

    /**
     * 获取单个维护
     * 
     * @return
     */
    public WithdrawalsTimeConfig getRecord(Integer record) {
        WithdrawalsTimeConfig withdrawalsTimeConfig = withdrawalsTimeConfigMapper.selectByPrimaryKey(record);
        return withdrawalsTimeConfig;
    }



    /**
     * 维护插入
     * 
     * @param record
     */
    public void insertRecord(WithdrawalsTimeConfigBean record) {
        record.setCreatetime(GetDate.getDate());
        record.setUpdatetime(GetDate.getDate());
        withdrawalsTimeConfigMapper.insertSelective(record);
    }

    /**
     * 维护更新
     * 
     * @param record
     */
    public void updateRecord(WithdrawalsTimeConfigBean record) {
        record.setUpdatetime(GetDate.getDate());
        withdrawalsTimeConfigMapper.updateByPrimaryKeySelective(record);
    }



}
