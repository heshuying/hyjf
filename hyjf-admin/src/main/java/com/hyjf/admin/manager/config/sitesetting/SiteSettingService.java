package com.hyjf.admin.manager.config.sitesetting;

import java.util.List;

import com.hyjf.mybatis.model.auto.SiteSetting;

public interface SiteSettingService {

    /**
     * 获取网站邮件设置列表
     * 
     * @return
     */
    public List<SiteSetting> getRecordList(SiteSetting siteSetting, int limitStart, int limitEnd);

    /**
     * 获取单个取网站邮件设置
     * 
     * @return
     */
    public SiteSetting getRecord(SiteSetting record);

    /**
     * 根据主键判断取网站邮件设置中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(SiteSetting record);

    /**
     * 取网站邮件设置插入
     * 
     * @param record
     */
    public void insertRecord(SiteSetting record);

    /**
     * 取网站邮件设置更新
     * 
     * @param record
     */
    public void updateRecord(SiteSetting record);

    /**
     * 取网站邮件设置删除
     * 
     * @param record
     */
    public void deleteRecord(List<Integer> recordList);
}
