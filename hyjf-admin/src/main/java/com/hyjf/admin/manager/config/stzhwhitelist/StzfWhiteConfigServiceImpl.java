package com.hyjf.admin.manager.config.stzhwhitelist;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RedisConstants;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.auto.HjhInstConfigExample;
import com.hyjf.mybatis.model.customize.STZHWhiteListCustomize;
import com.hyjf.mybatis.model.customize.STZHWhiteListCustomizeExample;

@Service
public class StzfWhiteConfigServiceImpl extends BaseServiceImpl implements StzfWhiteConfigService {

    /**
     * 获取受托支付白名单列表
     * 
     * @return
     */
    public List<STZHWhiteListCustomize> getRecordList(int limitStart, int limitEnd) {
    	STZHWhiteListCustomizeExample example = new STZHWhiteListCustomizeExample();
        example.createCriteria().andDelFlgEqualTo(0);
        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        return stzhWhiteListCustomizeMapper.selectByExample(example);
    }

    /**
     * 获取单个受托支付白名单
     * 
     * @return
     */
    public STZHWhiteListCustomize getRecord(Integer record) {
    	STZHWhiteListCustomize pushMoney = stzhWhiteListCustomizeMapper.selectByPrimaryKey(record);
        return pushMoney;
    }

    /**
     * 根据主键判断配置中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(STZHWhiteListCustomize record) {
    	STZHWhiteListCustomizeExample example = new STZHWhiteListCustomizeExample();
    	STZHWhiteListCustomizeExample.Criteria cra = example.createCriteria();
        cra.andIdEqualTo(record.getId());
        List<STZHWhiteListCustomize> STZHWhiteList = stzhWhiteListCustomizeMapper.selectByExample(example);
        if (STZHWhiteList != null && STZHWhiteList.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 根据主键判断数据是否存在
     * 
     * @return
     */
    public boolean isExistsPermission(STZHWhiteListCustomize record) {
    	STZHWhiteListCustomizeExample example = new STZHWhiteListCustomizeExample();
    	STZHWhiteListCustomizeExample.Criteria cra = example.createCriteria();
        cra.andIdEqualTo(record.getId());
        if (record.getId() != null) {
            cra.andIdEqualTo(record.getId());
        }
        List<STZHWhiteListCustomize> HjhInstConfigList = stzhWhiteListCustomizeMapper.selectByExample(example);
        if (HjhInstConfigList != null && HjhInstConfigList.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 受托支付插入
     * 
     * @param record
     */
    public int insertRecord(STZHWhiteListCustomize record) {
        record.setCreatetime(String.valueOf(GetDate.getNowTime10()));
        record.setUpdatetime(String.valueOf(GetDate.getNowTime10()));
        record.setApprovalTime(String.valueOf(GetDate.strYYYYMMDD2Timestamp2(record.getApprovalTime())));
        record.setDelFlg(0);
        return stzhWhiteListCustomizeMapper.insertSelective(record);
    }

    /**
     * 受托支付配置更新
     * 
     * @param record
     */
    public int updateRecord(STZHWhiteListCustomize record) {
        record.setUpdatetime(String.valueOf(GetDate.getNowTime10()));
        record.setApprovalTime(String.valueOf(GetDate.strYYYYMMDD2Timestamp2(record.getApprovalTime())));
        return stzhWhiteListCustomizeMapper.updateByPrimaryKeySelective(record);
    }
    /**
     * 根据名称获取单个取提成配置
     * 
     * @return
     */
    public STZHWhiteListCustomize selectByObject(STZHWhiteListCustomize record){
    	STZHWhiteListCustomize pushMoney = stzhWhiteListCustomizeMapper.selectByObject(record);
        return pushMoney;
    }
    /**
     * 获取机构列表
     * 
     * @return
     */
    public List<HjhInstConfig> getRegionList(){
    	return hjhInstConfigMapper.selectByExample(new HjhInstConfigExample());
    }
    /**
     * 按照机构编号获取机构名称
     * 
     * @return
     */
    public List<HjhInstConfig> getRegionName(HjhInstConfigExample hjhInstConfigExample){
    	return hjhInstConfigMapper.selectByExample(hjhInstConfigExample);
    	
    }
    



}
