package com.hyjf.admin.manager.content.team;

import java.util.List;

import com.hyjf.mybatis.model.auto.Team;

public interface ContentTeamService {

    /**
     * 获取文章列表列表
     * 
     * @return
     */
    public List<Team> getRecordList(Team team, int limitStart, int limitEnd);

    /**
     * 获取单个文章列表维护
     * 
     * @return
     */
    public Team getRecord(Integer record);

    /**
     * 根据主键判断文章列表中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(Team record);

    /**
     * 文章列表插入
     * 
     * @param record
     */
    public void insertRecord(Team record);

    /**
     * 文章列表更新
     * 
     * @param record
     */
    public void updateRecord(Team record);
    
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
    public List<Team> selectRecordList(ContentTeamBean form, int limitStart, int limitEnd);

}