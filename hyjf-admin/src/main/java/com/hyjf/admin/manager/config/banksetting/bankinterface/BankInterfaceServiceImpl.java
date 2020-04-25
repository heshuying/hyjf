package com.hyjf.admin.manager.config.banksetting.bankinterface;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.calculate.DateUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.BankInterface;
import com.hyjf.mybatis.model.auto.BanksConfig;
import com.hyjf.mybatis.model.auto.BanksConfigExample;
import com.hyjf.mybatis.model.customize.admin.AdminBankInterfaceCustomize;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BankInterfaceServiceImpl extends BaseServiceImpl implements BankInterfaceService {

    /**
     * 获取列表列表
     * 
     * @return
     */
    public List<AdminBankInterfaceCustomize> selectRecordList(Map<String, Object> paraMap) {
        return this.adminBankInterfaceCustomizeMapper.selectBankInterfaceList(paraMap);
    }

    /**
     * 获取接口配置表数据数量
     *
     * @return
     */
    public Integer getRecordCount(Map<String, Object> paraMap) {
        return this.adminBankInterfaceCustomizeMapper.countRecordTotal(paraMap);
    }

    /**
     * 获取单个维护
     *
     * @return
     */
    public BankInterface getRecord(Integer id) {
        BankInterface bankInterface = bankInterfaceMapper.selectByPrimaryKey(id);
        return bankInterface;
    }

    /**
     * 接口配置删除
     *
     * @param record
     */
    public void deleteRecord(BankInterface record) {
        this.bankInterfaceMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 禁用/启用
     *
     * @param record
     */
    public void updateRecord(BankInterface record) {
        this.bankInterfaceMapper.updateByPrimaryKeySelective(record);
    }
}
