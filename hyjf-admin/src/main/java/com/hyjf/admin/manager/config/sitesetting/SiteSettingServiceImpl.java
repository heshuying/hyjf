package com.hyjf.admin.manager.config.sitesetting;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.SiteSetting;
import com.hyjf.mybatis.model.auto.SiteSettingExample;

@Service
public class SiteSettingServiceImpl extends BaseServiceImpl implements SiteSettingService {
  
	/**
	 * 获取网站邮件设定配置列表
	 * 
	 * @return
	 */
	public List<SiteSetting> getRecordList(SiteSetting borrowFinserCharge, int limitStart, int limitEnd) {
		SiteSettingExample example = new SiteSettingExample();
		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		return siteSettingMapper.selectByExampleWithBLOBs(example);
	}

	/**
	 * 获取单个网站邮件设定配置维护
	 * 
	 * @return
	 */
	public SiteSetting getRecord(SiteSetting record) {
		SiteSettingExample example = new SiteSettingExample();
		SiteSettingExample.Criteria cra = example.createCriteria();
		cra.andIdEqualTo(record.getId());
		List<SiteSetting> SiteSettingList = siteSettingMapper.selectByExample(example);
		if (SiteSettingList != null && SiteSettingList.size() > 0) {
			return SiteSettingList.get(0);
		}
		return new SiteSetting();
	}

	/**
	 * 根据主键判断网站邮件设定配置中数据是否存在
	 * 
	 * @return
	 */
	public boolean isExistsRecord(SiteSetting record) {
		SiteSettingExample example = new SiteSettingExample();
		SiteSettingExample.Criteria cra = example.createCriteria();
		cra.andIdEqualTo(record.getId());
		List<SiteSetting> SiteSettingList = siteSettingMapper.selectByExample(example);
		if (SiteSettingList != null && SiteSettingList.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 根据主键判断网站邮件设定配置中数据是否存在
	 * 
	 * @return
	 */
	public boolean isExistsPermission(SiteSetting record) {
		SiteSettingExample example = new SiteSettingExample();
		SiteSettingExample.Criteria cra = example.createCriteria();
		cra.andIdEqualTo(record.getId());
		if (record.getId()!=null) {
			cra.andIdEqualTo(record.getId());
		}
		List<SiteSetting> SiteSettingList = siteSettingMapper.selectByExample(example);
		if (SiteSettingList != null && SiteSettingList.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 网站邮件设定配置插入
	 * 
	 * @param record
	 */
	public void insertRecord(SiteSetting record) {
		siteSettingMapper.insertSelective(record);
	}

	/**
	 * 网站邮件设定配置更新
	 * 
	 * @param record
	 */
	public void updateRecord(SiteSetting record) {
		siteSettingMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 网站邮件设定配置维护删除
	 * 
	 * @param record
	 */
	public void deleteRecord(List<Integer> recordList) {
		for (Integer id : recordList) {
			siteSettingMapper.deleteByPrimaryKey(id);
		}
	}


}
