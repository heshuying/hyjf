package com.hyjf.app.find;

import java.util.List;


import org.springframework.stereotype.Service;

import com.hyjf.mybatis.model.auto.ContentArticle;
import com.hyjf.mybatis.model.auto.ContentArticleExample;
import com.hyjf.app.BaseServiceImpl;

/**
 * 从web 拷贝的代码，没有做任何修改
 * 此处为类说明
 * @author hyjf
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年12月8日
 * @see 下午5:45:52
 */
@Service
public class ContentArticleServiceImpl extends BaseServiceImpl implements ContentArticleService {

	
	@Override
	public ContentArticle getNoticeInfo(Integer id) {
		ContentArticle article = contentArticleCustomizeMapper.selectByPrimaryKey(id);
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
