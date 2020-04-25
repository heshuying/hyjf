package com.hyjf.wechat.controller.find.contentarticle;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.ContentArticle;
import com.hyjf.mybatis.model.customize.app.ContentArticleCustomize;
import com.hyjf.wechat.base.BaseController;
import com.hyjf.wechat.base.BaseResultBean;
import com.hyjf.wechat.service.find.contentarticle.WechatContentArticleService;
import com.hyjf.wechat.util.ResultEnum;

@Controller
@RequestMapping(value=WechatContentArticleDefine.REQUEST_MAPPING)
public class WechatContentArticleController  extends BaseController{
	/** 类名 */
	public static final String THIS_CLASS = WechatContentArticleController.class.getName();
    @Autowired
    private WechatContentArticleService appContentArticleService;
	
	/**
	 * 根据文章类型获取文章列表
	 * @param request
	 * @param response
	 * @return 
	 */
    @ResponseBody
    @RequestMapping(value = WechatContentArticleDefine.GET_CONTENT_ARTICLE_LIST_BY_TYPE_ACTION) 
    public BaseResultBean getContentArticleListByType(HttpServletRequest request, HttpServletResponse response,@ModelAttribute() WechatContentArticleBean form) {
    	LogUtil.startLog(THIS_CLASS, WechatContentArticleDefine.GET_CONTENT_ARTICLE_LIST_BY_TYPE_ACTION);
    	WechatContentArticleResultVo result = new WechatContentArticleResultVo();
        result.setEnum(ResultEnum.SUCCESS);
    	//返回当前页
    	result.setCurrentPage(form.getCurrentPage());
    	try {
            // 检查参数正确性
            if (form.getPageSize()<0||form.getCurrentPage()<0){    
            	result.setEnum(ResultEnum.PARAM);
                return result;
            }
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("type", form.getType());
            params.put("limitStart", -1);
            params.put("limitEnd", -1);
            // 查询总数
            Integer count = appContentArticleService.countContentArticleByType(params);
            
            if (count != null && count > 0) {
                // 构造分页
            	if(form.getCurrentPage()<=0){
            		form.setCurrentPage(1);
        		}
        		if(form.getPageSize()<=0){
        			form.setPageSize(10);
        		}
                params.put("limitStart", form.getPageSize() * (form.getCurrentPage() - 1));
                params.put("limitEnd", form.getPageSize()+1);
                List<ContentArticleCustomize> list=appContentArticleService.getContentArticleListByType(params,request);
                
                if(!CollectionUtils.isEmpty(list)){
                	if(list.size()==(form.getPageSize()+1)) {
                		//不是最后一页
                    	result.setEndPage(0);
                	}else {
                		//是最后一页
                		result.setEndPage(1);
                	}
                	list.remove(list.size()-1);
                }
                
                
                if (!CollectionUtils.isEmpty(list)) {
                    result.setMessageCount(count);
                    result.setMessageList(list);
                } else {
                    result.setMessageCount(0);
                    result.setMessageList(new ArrayList<ContentArticleCustomize>());
                }
            } else {
            	result.setMessageCount(0);
                result.setMessageList(new ArrayList<ContentArticleCustomize>());
            }
    	} catch (Exception e) {
            result.setEnum(ResultEnum.FAIL);
            result.setMessageCount(0);
            result.setMessageList(new ArrayList<ContentArticleCustomize>());
            return result;
        }
    	LogUtil.endLog(THIS_CLASS, WechatContentArticleDefine.GET_CONTENT_ARTICLE_LIST_BY_TYPE_ACTION);
    	return result;
    	
    }
    
    
    /**
     * 返回文章翻页信息
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = WechatContentArticleDefine.GET_CONTENT_ARTICLE_FLIP_ACTION) 
    public BaseResultBean getContentArticleFlip(HttpServletRequest request, HttpServletResponse response,@ModelAttribute() WechatContentArticleBean form) {
        LogUtil.startLog(THIS_CLASS, WechatContentArticleDefine.GET_CONTENT_ARTICLE_FLIP_ACTION);
        WechatContentArticleResultVo result = new WechatContentArticleResultVo();
        result.setEnum(ResultEnum.SUCCESS);
        try {

            // 检查参数正确性
            if (Validator.isNull(form.getOffset()) || Validator.isNull(form.getMessageId())){    
            	result.setEnum(ResultEnum.PARAM);
                return result;
            }
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("messageId", form.getMessageId());
            params.put("type", form.getType());
            // 查询总数
            ContentArticleCustomize contentArticleCustomize = appContentArticleService.getContentArticleFlip(params,form.getOffset());
            
            if(contentArticleCustomize!=null){
            	result.setMessageId(contentArticleCustomize.getMessageId());
            	result.setMessageUrl(contentArticleCustomize.getMessageUrl());
            	result.setShareTitle(contentArticleCustomize.getTitle());
            	result.setShareContent(contentArticleCustomize.getShareContent());
            	result.setSharePicUrl(contentArticleCustomize.getSharePicUrl());
            	result.setShareUrl(contentArticleCustomize.getShareUrl());
            }else{
                result.setMessageId("");
            	result.setMessageUrl("");
            	result.setShareTitle("");
            	result.setShareContent("");
            	result.setSharePicUrl("");
            	result.setShareUrl("");
            }
        } catch (Exception e) {
        	result.setEnum(ResultEnum.FAIL);
            return result;
        }
        LogUtil.endLog(THIS_CLASS, WechatContentArticleDefine.GET_CONTENT_ARTICLE_FLIP_ACTION);
        return result;
        
    }
    
    
	
    /**
     * 根据id返回文章详情
     * @return
     */
    @ResponseBody
    @RequestMapping(value = WechatContentArticleDefine.GET_CONTENT_ARTICLE_ID_ACTION) 
    public BaseResultBean getContentArticleById(@PathVariable("contentArticleId") Integer contentArticleId,@PathVariable("type") Integer type) {
        LogUtil.startLog(THIS_CLASS, WechatContentArticleDefine.GET_CONTENT_ARTICLE_ID_ACTION);

        WechatContentArticleResultVo result = new WechatContentArticleResultVo();
        result.setEnum(ResultEnum.SUCCESS);
        result.setTopTitle(getTopTitle(type));
        try {
         // 根据id返回文章详情
            ContentArticle contentArticle = appContentArticleService.getContentArticleById(contentArticleId);
            if(contentArticle!=null){
                JSONObject details = new JSONObject();
                details.put("title",contentArticle.getTitle());
                details.put("content",contentArticle.getContent());
                details.put("date",new SimpleDateFormat("yyyy-MM-dd").format(contentArticle.getCreateTime()));
                result.setDetails(details);
            }else{
                result.setEnum(ResultEnum.ERROR_037);
            }
        } catch (Exception e) {
            result.setEnum(ResultEnum.ERROR_037);
        }
        
        LogUtil.endLog(THIS_CLASS, WechatContentArticleDefine.GET_CONTENT_ARTICLE_ID_ACTION);
        return result;
        
    }


    private String getTopTitle(Integer type) {
        switch (type) {
        case 2:
            return "网站公告";
        case 3:
            return "网贷知识";
        case 5:
            return "关于我们";
        case 101:
            return "风险教育";
        case 8:
            return "联系我们";
        case 20:
            return "公司动态";
        default:
            return "";
        }        
    }
}




