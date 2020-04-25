package com.hyjf.admin.maintenance.config;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.Config;
import com.hyjf.mybatis.model.auto.ConfigExample;

@Service
public class ConfigServiceImpl extends BaseServiceImpl implements ConfigService {

	/**
	 * 获取配置列表
	 * 
	 * @return
	 */
	public List<Config> getRecordList(Config config, int limitStart, int limitEnd) {
		ConfigExample example = new ConfigExample();
		ConfigExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(CustomConstants.FLAG_STATUS_ENABLE);

		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		return configMapper.selectByExample(example);
	}

	/**
	 * 获取单个权限维护
	 * 
	 * @return
	 */
	public Config getRecord(Config record) {
		ConfigExample example = new ConfigExample();
		ConfigExample.Criteria cra = example.createCriteria();
		cra.andIdEqualTo(record.getId());
		List<Config> ConfigList = configMapper.selectByExample(example);
		if (ConfigList != null && ConfigList.size() > 0) {
			return ConfigList.get(0);
		}
		return new Config();
	}

	/**
	 * 根据主键判断权限维护中数据是否存在
	 * 
	 * @return
	 */
	public boolean isExistsRecord(Config record) {
		ConfigExample example = new ConfigExample();
		ConfigExample.Criteria cra = example.createCriteria();
		cra.andIdEqualTo(record.getId());
		List<Config> ConfigList = configMapper.selectByExample(example);
		if (ConfigList != null && ConfigList.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 权限维护插入
	 * 
	 * @param record
	 */
	public void insertRecord(Config record) {
		int nowTime = GetDate.getNowTime10();
		record.setStatus(CustomConstants.FLAG_STATUS_ENABLE);
		record.setCreateTime(nowTime);
		record.setUpdateTime(nowTime);
		configMapper.insertSelective(record);
	}

	/**
	 * 权限维护更新
	 * 
	 * @param record
	 */
	public void updateRecord(Config record) {
		int nowTime = GetDate.getNowTime10();
		record.setUpdateTime(nowTime);
		configMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 权限维护删除
	 * 
	 * @param record
	 */
	public void deleteRecord(List<String> recordList) {
		for (String permissionUuid : recordList) {
			Config record = new Config();
			record.setId(Integer.parseInt(permissionUuid));
			record.setStatus(CustomConstants.FLAG_STATUS_ENABLE);
			record.setUpdateTime(GetDate.getNowTime10());
			configMapper.updateByPrimaryKeySelective(record);
		}
	}
}
