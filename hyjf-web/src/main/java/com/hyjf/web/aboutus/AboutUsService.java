
package com.hyjf.web.aboutus;

import java.math.BigDecimal;
import java.util.List;

import com.hyjf.mybatis.model.auto.ContentArticle;
import com.hyjf.mybatis.model.auto.Events;
import com.hyjf.mybatis.model.auto.Jobs;
import com.hyjf.mybatis.model.auto.Links;
import com.hyjf.mybatis.model.auto.Team;
import com.hyjf.web.BaseService;


public interface AboutUsService extends BaseService {
  
	/**
	 * 根据年份获取公司记事
	 * @param year
	 * @return
	 */
	List<Events> getEventRecordList(int year);

	/**
	 * 根据ID 获取公司纪事详情
	 * @param id
	 * @Author : huanghui
	 * @return
	 */
	public Events getEventRecordDetail(Integer id);

	/**
	 * 获取所有公司记事记录
	 * @return
	 */
	List<Events> getEventsList();

    /**
     * 获取关于我们
     * @return
     */
    public ContentArticle getAboutUs();
    /**
     * 获取联系我们
     * @return
     */
    public ContentArticle getContactUs();
    
    /**
     * 获取创始人信息
     * 
     * @return
     */
    public Team getFounder();
    
    /**
     * 获取合作伙伴列表
     * 
     * @return
     */
    public List<Links> getPartnersList(Integer partnerType);
    
    /**
     * 获取招贤纳士列表
     * 
     * @return
     */
    public List<Jobs> getJobsList();
    
	/**
	 * 取首页公告(风险教育..)数量
	 * @return
	 */
    int countHomeNoticeList(String noticeType);
	/**
	 * 取首页公告(风险教育..)列表
	 * @return
	 */
	List<ContentArticle> searchHomeNoticeList(String noticeType, int offset, int limit);
	
	/**
	 * 根据主键ID获取Aricle
	 * 
	 * @param id
	 * @return
	 */
	public ContentArticle getHomeNoticeInfo(Integer id);
	
	/**
	 * 获取累计出借
	 * 
	 * @param id
	 * @return
	 */
	BigDecimal getTenderSum();
	
	/**
	 * 获取累计收益
	 * 
	 * @param id
	 * @return
	 */
	BigDecimal getProfitSum();

	String getTotalInvestmentAmount();
}
