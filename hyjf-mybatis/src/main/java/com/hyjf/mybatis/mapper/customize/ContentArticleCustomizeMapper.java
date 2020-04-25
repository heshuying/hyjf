package com.hyjf.mybatis.mapper.customize;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.auto.ContentArticle;
import com.hyjf.mybatis.model.customize.ContentArticleCustomize;

public interface ContentArticleCustomizeMapper {

	/**
	 * 根据条件查询文章列表
	 * @param borrowCommonCustomize
	 * @return
	 */
	List<ContentArticle> selectContentArticle(ContentArticleCustomize contentArticleCustomize);
	/**
	 * 根据公告id，查询指定的公告
	 * @param id
	 * @return
	 */
	ContentArticle selectByPrimaryKey(int id);
	/**
	 * 返回指定的列
	 * @param contentArticleCustomize
	 * @return
	 */
	List<ContentArticle> selectContentArticleCustomize(ContentArticleCustomize contentArticleCustomize);
	/**
	 * 
	 * 查询网贷知识记录条数
	 * @author pcc
	 * @param params
	 * @return
	 */
    Integer countNetLoanKnowledge(Map<String, Object> map);
    /**
     * 
     * 获取网贷知识列表
     * @author pcc
     * @param params
     * @return
     */
    List<ContentArticle> getNetLoanKnowledgeList(Map<String, Object> params);
    /**
     * 
     * 上一页
     * @author pcc
     * @param params
     * @return
     */
    ContentArticle getContentArticleUp(Map<String, Object> params);
    /**
     * 
     * 下一页
     * @author pcc
     * @param params
     * @return
     */
    ContentArticle getContentArticleDown(Map<String, Object> params);


}