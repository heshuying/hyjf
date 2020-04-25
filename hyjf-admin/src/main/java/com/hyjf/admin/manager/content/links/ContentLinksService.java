package com.hyjf.admin.manager.content.links;

import java.util.List;

import com.hyjf.mybatis.model.auto.Links;

public interface ContentLinksService {

    /**
     * 获取文章列表列表
     * 
     * @return
     */
    public List<Links> getRecordList(ContentLinksBean bean, int limitStart, int limitEnd);

    /**
     * 获取单个文章列表维护
     * 
     * @return
     */
    public Links getRecord(Short record);

    /**
     * 根据主键判断文章列表中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(Links record);

    /**
     * 文章列表插入
     * 
     * @param record
     */
    public void insertRecord(Links record);

    /**
     * 文章列表更新
     * 
     * @param record
     */
    public void updateRecord(Links record);
    
    /**
     * 配置删除
     * 
     * @param record
     */
    public void deleteRecord(List<Integer> recordList);

    /**
     * 根据条件查询数据
     * 
     * @param ContentLinks
     * @param i
     * @param j
     * @return
     */
    public List<Links> selectRecordList(ContentLinksBean form, int limitStart, int limitEnd);

}