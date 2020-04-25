package com.hyjf.admin.manager.config.hzrconfig;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.HzrConfig;
import com.hyjf.mybatis.model.auto.HzrConfigExample;

@Service
public class HzrConfigServiceImpl extends BaseServiceImpl implements HzrConfigService {

	/**
	 * 获取汇转让配置列表
	 * 
	 * @return
	 */
	public List<HzrConfig> getRecordList(HzrConfigBean borrowFinserCharge, int limitStart, int limitEnd) {
		HzrConfigExample example = new HzrConfigExample();
		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		return hzrConfigMapper.selectByExampleWithBLOBs(example);
	}

	/**
	 * 获取单个取汇转让配置维护
	 * 
	 * @return
	 */
	public HzrConfig getRecord(Integer record) {
		HzrConfig list = hzrConfigMapper.selectByPrimaryKey(record);
		return list;
	}

	/**
	 * 根据主键判断取汇转让配置中数据是否存在
	 * 
	 * @return
	 */
	public boolean isExistsRecord(HzrConfigBean record) {
		HzrConfigExample example = new HzrConfigExample();
		HzrConfigExample.Criteria cra = example.createCriteria();
		cra.andIdEqualTo(Integer.valueOf(record.getId()));
		List<HzrConfig> HzrConfigList = hzrConfigMapper.selectByExample(example);
		if (HzrConfigList != null && HzrConfigList.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 取汇转让配置插入
	 * 
	 * @param record
	 */
	public void insertRecord(HzrConfigBean record) {
		HzrConfig hzrConfig = new HzrConfig();
		hzrConfig.setCode(record.getCode());
		hzrConfig.setName(record.getName());
		hzrConfig.setUnit(record.getUnit());
		hzrConfig.setValue(record.getValue());
		hzrConfig.setRemark(record.getRemark());
		hzrConfig.setCreateTime(GetDate.getDate());
		hzrConfig.setUpdateTime(GetDate.getDate());
		hzrConfigMapper.insertSelective(hzrConfig);
	}

	/**
	 * 取汇转让配置更新
	 * 
	 * @param record
	 */
	public void updateRecord(HzrConfigBean record) {
		HzrConfig hzrConfig = new HzrConfig();
		hzrConfig.setId(Integer.valueOf(record.getId()));
		hzrConfig.setCode(record.getCode());
		hzrConfig.setName(record.getName());
		hzrConfig.setUnit(record.getUnit());
		hzrConfig.setValue(record.getValue());
		if (StringUtils.isNotEmpty(record.getRemark())) {
			hzrConfig.setRemark(record.getRemark());
		} else {
			hzrConfig.setRemark(StringUtils.EMPTY);
		}
		hzrConfig.setUpdateTime(GetDate.getDate());
		hzrConfigMapper.updateByPrimaryKeySelective(hzrConfig);
	}

	/**
	 * 汇转让配置维护删除
	 * 
	 * @param record
	 */
	public void deleteRecord(List<Integer> recordList) {
		for (Integer id : recordList) {
			hzrConfigMapper.deleteByPrimaryKey(id);
		}
	}

	/**
	 * 编号是否已经存在（存在：true）
	 * 
	 * @param record
	 * @return
	 */
	public boolean isExistsCode(HzrConfigBean record) {
		HzrConfigExample example = new HzrConfigExample();
		HzrConfigExample.Criteria cra = example.createCriteria();
		cra.andCodeEqualTo(record.getCode());
		if (StringUtils.isNotEmpty(record.getId())) {
			cra.andIdNotEqualTo(Integer.valueOf(record.getId()));
		}

		List<HzrConfig> list = this.hzrConfigMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

}
