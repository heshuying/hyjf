package com.hyjf.admin.manager.config.applicant;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.ConfigApplicant;
import com.hyjf.mybatis.model.auto.ConfigApplicantExample;

@Service
public class ApplicantConfigServiceImpl extends BaseServiceImpl implements ApplicantConfigService {

    /**
     * 获取记录数
     */
	public Integer countRecord(ApplicantConfigBean configApplicant) {
    	ConfigApplicantExample example = new ConfigApplicantExample();
    	ConfigApplicantExample.Criteria cra= example.createCriteria();
    	if(configApplicant!=null && configApplicant.getApplicant()!=null){
    		cra.andApplicantLike("%" +configApplicant.getApplicant() + "%");
    	}
		return configApplicantMapper.countByExample(example);
	}
    /**
     * 获取列表
     *
     * @return
     */
    public List<ConfigApplicant> getRecordList(ApplicantConfigBean configApplicant, int limitStart, int limitEnd) {
    	ConfigApplicantExample example = new ConfigApplicantExample();
    	ConfigApplicantExample.Criteria cra= example.createCriteria();
    	if(configApplicant!=null && configApplicant.getApplicant()!=null){
    		cra.andApplicantLike("%" +configApplicant.getApplicant() + "%");
    	}
    	
		example.setOrderByClause("id desc");
		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		return configApplicantMapper.selectByExample(example);
    }

    /**
     * 获取单个信息
     *
     * @return
     */
    public ConfigApplicant getRecord(Integer id) {
    	ConfigApplicantExample example = new ConfigApplicantExample();
    	ConfigApplicantExample.Criteria cra = example.createCriteria();
    	cra.andIdEqualTo(id);
        List<ConfigApplicant> list = configApplicantMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return new ConfigApplicant();
    }

    /**
     * 根据主键判断账户中数据是否存在
     *
     * @return
     */
    public boolean isExistsRecord(Integer id) {
        if (Validator.isNull(id)) {
            return false;
        }
        ConfigApplicantExample example = new ConfigApplicantExample();
        ConfigApplicantExample.Criteria cra = example.createCriteria();
        cra.andIdEqualTo(id);
        int cnt = configApplicantMapper.countByExample(example);
        return cnt > 0;
    }
	/**
	 * 插入记录
	 */
	public void insertRecord(ApplicantConfigBean form) {
        ConfigApplicant record = new ConfigApplicant();
        BeanUtils.copyProperties(form, record);
        String username = ShiroUtil.getLoginUsername();
        record.setAddOperator(username);
        record.setAddTime(new Date());
        
        configApplicantMapper.insertSelective(record);
	}
	/**
	 * 更新记录
	 */
	public void updateRecord(ApplicantConfigBean form) {
        ConfigApplicant record = new ConfigApplicant();
        BeanUtils.copyProperties(form, record);
        String username = ShiroUtil.getLoginUsername();
        record.setUpdateOperator(username);
        record.setUpdateTime(new Date());
        configApplicantMapper.updateByPrimaryKeySelective(record);
	}
	/**
	 * 批量删除
	 */
	public void deleteRecord(List<Integer> ids) {
		ConfigApplicantExample example = new ConfigApplicantExample();
		ConfigApplicantExample.Criteria cra = example.createCriteria();
		cra.andIdIn(ids);
		configApplicantMapper.deleteByExample(example);
	}




}
