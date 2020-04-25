package com.hyjf.admin.manager.config.pushmoney;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.PushMoney;
import com.hyjf.mybatis.model.auto.PushMoneyExample;

@Service
public class PushMoneyServiceImpl extends BaseServiceImpl implements PushMoneyService {

    /**
     * 获取取惠天利配置列表
     * 
     * @return
     */
    public List<PushMoney> getRecordList(PushMoney borrowFinserCharge, int limitStart, int limitEnd) {
        PushMoneyExample example = new PushMoneyExample();
        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        return pushMoneyMapper.selectByExampleWithBLOBs(example);
    }

    /**
     * 获取单个取惠天利配置维护
     * 
     * @return
     */
    public PushMoney getRecord(Integer record) {
        PushMoney pushMoney = pushMoneyMapper.selectByPrimaryKey(record);
        return pushMoney;
    }

    /**
     * 根据主键判断取惠天利配置中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(PushMoney record) {
        PushMoneyExample example = new PushMoneyExample();
        PushMoneyExample.Criteria cra = example.createCriteria();
        cra.andIdEqualTo(record.getId());
        List<PushMoney> PushMoneyList = pushMoneyMapper.selectByExample(example);
        if (PushMoneyList != null && PushMoneyList.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 根据主键判断取惠天利配置中数据是否存在
     * 
     * @return
     */
    public boolean isExistsPermission(PushMoney record) {
        PushMoneyExample example = new PushMoneyExample();
        PushMoneyExample.Criteria cra = example.createCriteria();
        cra.andIdEqualTo(record.getId());
        if (record.getId() != null) {
            cra.andIdEqualTo(record.getId());
        }
        List<PushMoney> PushMoneyList = pushMoneyMapper.selectByExample(example);
        if (PushMoneyList != null && PushMoneyList.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 取惠天利配置插入
     * 
     * @param record
     */
    public void insertRecord(PushMoney record) {
        record.setCreateTime(GetDate.getDate());
        record.setUpdateTime(GetDate.getDate());
        pushMoneyMapper.insertSelective(record);
    }

    /**
     * 取惠天利配置更新
     * 
     * @param record
     */
    public void updateRecord(PushMoney record) {
        record.setUpdateTime(GetDate.getDate());
        pushMoneyMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 取惠天利配置维护删除
     * 
     * @param record
     */
    public void deleteRecord(List<Integer> recordList) {
        for (Integer id : recordList) {
            pushMoneyMapper.deleteByPrimaryKey(id);
        }
    }

}
