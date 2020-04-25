
package com.hyjf.web.aboutus;

import java.math.BigDecimal;
import java.util.List;

import com.hyjf.mybatis.mapper.auto.EventsMapper;
import org.springframework.stereotype.Service;

import com.hyjf.mybatis.model.auto.CalculateInvestInterest;
import com.hyjf.mybatis.model.auto.CalculateInvestInterestExample;
import com.hyjf.mybatis.model.auto.ContentArticle;
import com.hyjf.mybatis.model.auto.ContentArticleExample;
import com.hyjf.mybatis.model.auto.Events;
import com.hyjf.mybatis.model.auto.EventsExample;
import com.hyjf.mybatis.model.auto.Jobs;
import com.hyjf.mybatis.model.auto.JobsExample;
import com.hyjf.mybatis.model.auto.Links;
import com.hyjf.mybatis.model.auto.LinksExample;
import com.hyjf.mybatis.model.auto.Team;
import com.hyjf.mybatis.model.auto.TeamExample;
import com.hyjf.mybatis.model.auto.LinksExample.Criteria;
import com.hyjf.web.BaseServiceImpl;


@Service
public class AboutUsServiceImpl extends BaseServiceImpl implements AboutUsService {

	/**
	 * 根据年份获取公司记事
	 * 
	 * @param year
	 * @return
	 */
	@Override
	public List<Events> getEventRecordList(int year) {
		EventsExample example = new EventsExample();
		EventsExample.Criteria cra = example.createCriteria();
		cra.andEventYearEqualTo(year);
		example.setOrderByClause(" act_time desc ");
		return eventsMapper.selectByExample(example);
	}

	/**
	 *  根据ID 获取公司纪事详情
	 * @param id
	 * @Author : huanghui
	 * @return
	 */
	@Override
	public Events getEventRecordDetail(Integer id) {
		Events eventsInfo = eventsMapper.selectByPrimaryKey(id);
		return eventsInfo;
	}

	/**
	 * 获取所有公司记事记录
	 * 
	 * @return
	 */

	@Override
	public List<Events> getEventsList() {
		EventsExample example = new EventsExample();
		EventsExample.Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(1);
		example.setOrderByClause("act_time desc, create_time DESC");
		return eventsMapper.selectByExample(example);
	}

	/**
	 * 获取关于我们
	 * 
	 * @return
	 * @author Michael
	 */
	@Override
	public ContentArticle getAboutUs() {
		ContentArticleExample example = new ContentArticleExample();
		ContentArticleExample.Criteria cra = example.createCriteria();
		cra.andTypeEqualTo("5");// 关于我们
		cra.andStatusEqualTo(1);// 启用状态
		List<ContentArticle> conlist = contentArticleMapper.selectByExampleWithBLOBs(example);
		if (conlist != null && conlist.size() > 0) {
			return conlist.get(0);
		}
		return new ContentArticle();
	}

	/**
	 * 获取联系我们
	 * 
	 * @return
	 * @author Michael
	 */
	@Override
	public ContentArticle getContactUs() {
		ContentArticleExample example = new ContentArticleExample();
		ContentArticleExample.Criteria cra = example.createCriteria();
		cra.andTypeEqualTo("8");// 联系我们
		cra.andStatusEqualTo(1);// 启用状态
		List<ContentArticle> conlist = contentArticleMapper.selectByExampleWithBLOBs(example);
		if (conlist != null && conlist.size() > 0) {
			return conlist.get(0);
		}
		return new ContentArticle();
	}

	/**
	 * 
	 * 获取创始人信息 （最新数据）
	 * 
	 * @return
	 * @author Michael
	 */
	public Team getFounder() {
		TeamExample example = new TeamExample();
		TeamExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(1);// 开启状态
		example.setOrderByClause("`order` asc");
		List<Team> teamList = teamMapper.selectByExampleWithBLOBs(example);
		return teamList.size() == 0 ? new Team() : teamList.get(0);
	}

	/**
	 * 获取合作伙伴列表
	 * 
	 * @return
	 */
	@Override
	public List<Links> getPartnersList(Integer partnerType) {
		LinksExample example = new LinksExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo((short) 1);// 启用状态
		criteria.andTypeEqualTo(2);// 合作伙伴
		if (partnerType != null) {
			criteria.andPartnerTypeEqualTo(partnerType);
		}
		example.setOrderByClause("`partner_type` ASC,`order` Asc,`create_time` Desc");
		return linksMapper.selectByExample(example);
	}
	
	/**
	 * 获取招贤纳士列表
	 * 
	 * @return List
	 */
	@Override
	public List<Jobs> getJobsList() {
		JobsExample example = new JobsExample();
		com.hyjf.mybatis.model.auto.JobsExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(1);//开启状态
        example.setOrderByClause("`order` Asc,`create_time` Desc");
		return jobsMapper.selectByExampleWithBLOBs(example);
	}
	
	/**
	 * 获取风险教育总数
	 * 
	 * @return List
	 */
	@Override
	public int countHomeNoticeList(String noticeType) {
		ContentArticleExample example = new ContentArticleExample();
		ContentArticleExample.Criteria crt = example.createCriteria();
		crt.andTypeEqualTo(noticeType);
		crt.andStatusEqualTo(1);
		return contentArticleMapper.countByExample(example);
	}
	
	@Override
	public List<ContentArticle> searchHomeNoticeList(String noticeType, int offset, int limit) {
		ContentArticleExample example = new ContentArticleExample();
		if (offset != -1) {
			example.setLimitStart(offset);
			example.setLimitEnd(limit);
		}
		ContentArticleExample.Criteria crt = example.createCriteria();
		crt.andTypeEqualTo(noticeType);
		crt.andStatusEqualTo(1);
		example.setOrderByClause("create_time Desc");
		List<ContentArticle> contentArticles = contentArticleMapper.selectByExampleWithBLOBs(example);
		return contentArticles;
	}
	
	@Override
	public ContentArticle getHomeNoticeInfo(Integer id) {
		ContentArticle article = contentArticleMapper.selectByPrimaryKey(id);
		return article;
	}

	/**
	 * 获取出借总额
	 * @return
	 * @author Michael
	 */
	@Override
	public BigDecimal getTenderSum() {
		List<CalculateInvestInterest> list = this.calculateInvestInterestMapper.selectByExample(new CalculateInvestInterestExample());
		if(list != null && list.size() > 0){
			return list.get(0).getTenderSum();
		}
		return null;	
	}
	
	/**
	 * 获取出借总额
	 * @return
	 * @author Michael
	 */
	@Override
	public BigDecimal getProfitSum() {
		List<CalculateInvestInterest> list = this.calculateInvestInterestMapper.selectByExample(new CalculateInvestInterestExample());
		if(list != null && list.size() > 0){
			return list.get(0).getInterestSum();
		}
		return null;
	}

	//获取累计出借金额
	@Override
	public String getTotalInvestmentAmount() {
		return String.valueOf(calculateInvestInterestMapper.selectByPrimaryKey(1).getTenderSum().divide(new BigDecimal("100000000")));
	}
}