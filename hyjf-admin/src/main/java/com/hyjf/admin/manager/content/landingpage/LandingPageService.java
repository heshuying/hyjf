package com.hyjf.admin.manager.content.landingpage;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.mybatis.model.auto.LandingPage;

public interface LandingPageService {

    /**
     * 获取着落页列表
     * 
     * @return
     */
    public List<LandingPage> getRecordList(LandingPageBean bean, int limitStart, int limitEnd);

    /**
     * 获取着落页列表记录数
     * 
     * @return
     */
    public Integer getRecordCount(LandingPageBean form);

    
    /**
     * 获取单个着落页信息
     * 
     * @return
     */
    public LandingPage getRecord(Integer record);

    /**
     * 根据主键判断列表中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(LandingPage record);

    /**
     * 插入
     * 
     * @param record
     */
    public void insertRecord(LandingPage record);

    /**
     * 更新
     * 
     * @param record
     */
    public void updateRecord(LandingPage record);

    /**
     * 删除
     * 
     * @param record
     */
    public void deleteRecord(List<Integer> recordList);

    /**
     * 检查着落页名称唯一性
     * 
     * @param id
     * @param pagename
     */
    public int countByPageName(Integer id, String pageName);
    
	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception;
    
    
}