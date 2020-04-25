package com.hyjf.admin.manager.content.qualify;

import java.util.List;

import com.hyjf.mybatis.model.auto.ContentQualify;

public interface ContentQualifyService {

    /**
     * 获取配置列表
     * 
     * @return
     */
    public List<ContentQualify> getRecordList(ContentQualify contentQualify, int limitStart, int limitEnd);

    /**
     * 获取单个配置维护
     * 
     * @return
     */
    public ContentQualify getRecord(Integer record);

    /**
     * 根据主键判断配置中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(ContentQualify record);

    /**
     * 配置插入
     * 
     * @param record
     */
    public void insertRecord(ContentQualify record);

    /**
     * 配置更新
     * 
     * @param record
     */
    public void updateRecord(ContentQualify record);

    /**
     * 配置删除
     * 
     * @param record
     */
    public void deleteRecord(List<Integer> recordList);

    /**
     * 根据条件查询数据
     * 
     * @param ContentArticle
     * @param i
     * @param j
     * @return
     */
    public List<ContentQualify> selectRecordList(ContentQualifyBean form, int limitStart, int limitEnd);

}