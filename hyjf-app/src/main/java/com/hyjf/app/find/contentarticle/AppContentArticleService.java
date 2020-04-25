package com.hyjf.app.find.contentarticle;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.auto.AppPushManage;
import com.hyjf.mybatis.model.auto.ContentArticle;
import com.hyjf.mybatis.model.customize.app.ContentArticleCustomize;

public interface AppContentArticleService extends BaseService {

    /**
     * 
     * 获取网贷知识列表总条数
     * @author pcc
     * @param params
     * @return
     */
    public Integer countContentArticleByType(Map<String, Object> params);
    /**
     * 
     * 获取网贷知识列表
     * @author pcc
     * @param params
     * @param request 
     * @return
     */
    public List<ContentArticleCustomize> getContentArticleListByType(Map<String, Object> params, HttpServletRequest request);
    /**
     * 
     * 根据id获取网贷知识
     * @author pcc
     * @param type 
     * @param knowledgeId
     * @return
     */
    public ContentArticle getContentArticleById(Integer contentArticleId);
    /**
     * 
     * 返回网贷知识翻页信息
     * @author pcc
     * @param params
     * @param offset 
     * @return
     */
    public ContentArticleCustomize getContentArticleFlip(Map<String, Object> params, String offset);

    /**
     * 根据ID返回推送公告内容
     * @param contentArticleId
     * @return
     */
    public AppPushManage getAppPushManagerContentByID(Integer contentArticleId);
}
