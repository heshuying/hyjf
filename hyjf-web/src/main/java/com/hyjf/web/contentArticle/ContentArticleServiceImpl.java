package com.hyjf.web.contentArticle;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.auto.ContentArticle;
import com.hyjf.mybatis.model.auto.ContentArticleExample;
import com.hyjf.web.BaseServiceImpl;

@Service
public class ContentArticleServiceImpl extends BaseServiceImpl implements ContentArticleService {


	
	@Override
	public ContentArticle getNoticeInfo(Integer id) {
		ContentArticle article = contentArticleMapper.selectByPrimaryKey(id);
		return article;
	}
	
	/**
     * 
     * 获取公司公告件数
     * @author liuyang
     * @param noticeType
     * @return
     */
    @Override
    public int getNoticeListCount(String noticeType) {
        ContentArticleExample example = new ContentArticleExample();
        ContentArticleExample.Criteria crt = example.createCriteria();
        crt.andTypeEqualTo(noticeType);
        crt.andStatusEqualTo(1);
        return contentArticleMapper.countByExample(example);
    }
    
    /**
     * 
     * 获取公司公告列表
     * @author liuyang
     * @param noticeType
     * @param offset
     * @param limit
     * @return
     */
    @Override
    @Cached(name="webHomeNoticeCache-", expire = CustomConstants.HOME_CACHE_LIVE_TIME, cacheType = CacheType.BOTH)
    @CacheRefresh(refresh = 60, stopRefreshAfterLastAccess = 60, timeUnit = TimeUnit.SECONDS)
    public List<ContentArticle> searchNoticeList(String noticeType, int offset, int limit) {
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



}
