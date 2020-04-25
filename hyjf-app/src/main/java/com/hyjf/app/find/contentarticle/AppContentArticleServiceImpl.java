package com.hyjf.app.find.contentarticle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hyjf.mybatis.model.auto.AppPushManage;
import org.springframework.stereotype.Service;

import com.hyjf.app.BaseServiceImpl;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.ContentArticle;
import com.hyjf.mybatis.model.customize.app.ContentArticleCustomize;
@Service
public class AppContentArticleServiceImpl extends BaseServiceImpl implements AppContentArticleService {
    /**
     * 
     * 根据文章类型获取记录数
     * @author pcc
     * @return
     * @see
     */
    @Override
    public Integer countContentArticleByType(Map<String, Object> params) {
        return contentArticleCustomizeMapper.countNetLoanKnowledge(params);
    }
    /**
     * 
     * 根据文章类型获取文章列表
     * @author pcc
     * @param params
     * @return
     */
    @Override
    public List<ContentArticleCustomize> getContentArticleListByType(Map<String, Object> params, HttpServletRequest request) {
        List<ContentArticle> list=contentArticleCustomizeMapper.getNetLoanKnowledgeList(params);
        List<ContentArticleCustomize> knowledgeCustomizes=new ArrayList<ContentArticleCustomize>();
        for (ContentArticle contentArticle : list) {
            ContentArticleCustomize customize=new ContentArticleCustomize();
            customize.setTitle(contentArticle.getTitle());
            customize.setTime(new SimpleDateFormat("yyyy-MM-dd").format(contentArticle.getCreateTime()));
            customize.setMessageId(contentArticle.getId()+"");
            customize.setMessageUrl(PropUtils.getSystem("hyjf.web.host")+AppContentArticleDefine.REQUEST_MAPPING+
                    AppContentArticleDefine.GET_CONTENT_ARTICLE_ID_ACTION.replace("{contentArticleId}", contentArticle.getId()+"").replace("{type}", (String)params.get("type")));
            customize.setShareTitle(contentArticle.getTitle());
            customize.setShareContent(contentArticle.getSummary());
            customize.setSharePicUrl("https://www.hyjf.com/data/upfiles/image/20140617/1402991818340.png");
            customize.setShareUrl(PropUtils.getSystem("hyjf.web.host")+AppContentArticleDefine.REQUEST_MAPPING+
                    AppContentArticleDefine.GET_CONTENT_ARTICLE_ID_ACTION.replace("{contentArticleId}", contentArticle.getId()+"").replace("{type}", (String)params.get("type")));
            
            knowledgeCustomizes.add(customize);
        }
        return knowledgeCustomizes;
    }
    @Override
    public ContentArticle getContentArticleById(Integer knowledgeId) {
        ContentArticle contentArticle=contentArticleMapper.selectByPrimaryKey(knowledgeId);
        return contentArticle;
    }
    @Override
    public ContentArticleCustomize getContentArticleFlip(Map<String, Object> params,String offset) {
        ContentArticle contentArticle=null;
        if("0".equals(offset)){
            contentArticle=contentArticleCustomizeMapper.getContentArticleUp(params);  
        }else{
            contentArticle=contentArticleCustomizeMapper.getContentArticleDown(params);
        }
        
        
        if(contentArticle!=null){
            ContentArticleCustomize customize=new ContentArticleCustomize();
            customize.setTitle(contentArticle.getTitle());
            customize.setTime(new SimpleDateFormat("yyyy-MM-dd").format(contentArticle.getCreateTime()));
            customize.setMessageId(contentArticle.getId()+"");
            customize.setMessageUrl(PropUtils.getSystem("hyjf.web.host")+AppContentArticleDefine.REQUEST_MAPPING+
                    AppContentArticleDefine.GET_CONTENT_ARTICLE_ID_ACTION.replace("{contentArticleId}", contentArticle.getId()+"").replace("{type}", (String)params.get("type")));
            customize.setShareTitle(contentArticle.getTitle());
            customize.setShareContent(contentArticle.getSummary());
            customize.setSharePicUrl("https://www.hyjf.com/data/upfiles/image/20140617/1402991818340.png");
            customize.setShareUrl(PropUtils.getSystem("hyjf.web.host")+AppContentArticleDefine.REQUEST_MAPPING+
                    AppContentArticleDefine.GET_CONTENT_ARTICLE_ID_ACTION.replace("{contentArticleId}", contentArticle.getId()+"").replace("{type}", (String)params.get("type")));
            return customize;
        }
        return null;
    }

    /**
     * 根据ID获得推送内容详情
     * @param contentArticleId
     * @return
     */
    @Override
    public AppPushManage getAppPushManagerContentByID(Integer contentArticleId) {

        AppPushManage appPushManage = this.appPushManageMapper.selectByPrimaryKey(contentArticleId);
        if(appPushManage != null){
            return appPushManage;
        }
        return null;
    }

}




