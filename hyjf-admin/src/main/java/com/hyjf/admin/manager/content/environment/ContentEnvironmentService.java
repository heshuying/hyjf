package com.hyjf.admin.manager.content.environment;

import java.util.List;

import com.hyjf.mybatis.model.auto.ContentEnvironment;

public interface ContentEnvironmentService {

    /**
     * 获取活动列表列表
     * 
     * @return
     */
    public List<ContentEnvironment> getRecordList(ContentEnvironmentBean bean,
            int limitStart, int limitEnd);

    /**
     * 获取单个活动列表维护
     * 
     * @return
     */
    public ContentEnvironment getRecord(Integer record);

    /**
     * 根据主键判断活动列表中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(ContentEnvironment record);

    /**
     * 活动列表插入
     * 
     * @param record
     */
    public void insertRecord(ContentEnvironment record);

    /**
     * 活动列表更新
     * 
     * @param record
     */
    public void updateRecord(ContentEnvironment record);

    /**
     * 活动列表删除
     * 
     * @param record
     */
    public void deleteRecord(List<Integer> recordList);

}