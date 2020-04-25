package com.hyjf.admin.manager.content.jobs;

import java.util.List;

import com.hyjf.mybatis.model.auto.Jobs;

public interface ContentJobsService {

    /**
     * 获取文章列表列表
     * 
     * @return
     */
    public List<Jobs> getRecordList(Jobs jobs, int limitStart, int limitEnd);

    /**
     * 获取单个文章列表维护
     * 
     * @return
     */
    public Jobs getRecord(Integer record);

    /**
     * 根据主键判断文章列表中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(Jobs record);

    /**
     * 文章列表插入
     * 
     * @param record
     */
    public void insertRecord(Jobs record);

    /**
     * 文章列表更新
     * 
     * @param record
     */
    public void updateRecord(Jobs record);
    
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
    public List<Jobs> selectRecordList(ContentJobsBean form, int limitStart, int limitEnd);

}