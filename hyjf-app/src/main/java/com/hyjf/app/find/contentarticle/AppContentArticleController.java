package com.hyjf.app.find.contentarticle;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.AppPushManage;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.BaseResultBeanFrontEnd;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.ContentArticle;
import com.hyjf.mybatis.model.customize.app.ContentArticleCustomize;

@Controller
@RequestMapping(value=AppContentArticleDefine.REQUEST_MAPPING)
public class AppContentArticleController  extends BaseController{
	/** 类名 */
	public static final String THIS_CLASS = AppContentArticleController.class.getName();
    @Autowired
    private AppContentArticleService appContentArticleService;
	
	/**
	 * 根据文章类型获取文章列表
	 * @param request
	 * @param response
	 * @return 
	 */
    @ResponseBody
    @RequestMapping(value = AppContentArticleDefine.GET_CONTENT_ARTICLE_LIST_BY_TYPE_ACTION) 
    public JSONObject getContentArticleListByType(HttpServletRequest request, HttpServletResponse response,@ModelAttribute() AppContentArticleBean form) {
    	LogUtil.startLog(THIS_CLASS, AppContentArticleDefine.GET_CONTENT_ARTICLE_LIST_BY_TYPE_ACTION);
    	JSONObject ret = new JSONObject();
    	ret.put("status", "0");
        ret.put("statusDesc", "请求成功");
    	ret.put("request", AppContentArticleDefine.REQUEST_HOME+AppContentArticleDefine.REQUEST_MAPPING+AppContentArticleDefine.GET_CONTENT_ARTICLE_LIST_BY_TYPE_ACTION);
    	try {

            // 检查参数正确性
            if (Validator.isNull(form.getVersion()) || Validator.isNull(form.getPlatform())){    
                ret.put("status", "1");
                ret.put("statusDesc", "请求参数非法");
                return ret;
            }
            // 检查参数正确性
            if (form.getSize()<0||form.getPage()<0){    
                ret.put("status", "1");
                ret.put("statusDesc", "分页参数非法");
                return ret;
            }
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("type", form.getType());
            params.put("limitStart", -1);
            params.put("limitEnd", -1);
            // 查询总数
            Integer count = appContentArticleService.countContentArticleByType(params);
            
            if (count != null && count > 0) {
                // 构造分页
                params.put("limitStart", form.getSize() * (form.getPage() - 1));
                params.put("limitEnd", form.getSize());
                List<ContentArticleCustomize> list=appContentArticleService.getContentArticleListByType(params,request);
    
                if (!CollectionUtils.isEmpty(list)) {
                    ret.put("messageCount", count);
                    ret.put("messageList", list);
                } else {
                    ret.put("messageCount", "0");
                    ret.put("messageList", new ArrayList<ContentArticleCustomize>());
                }
            } else {
                ret.put("messageCount", "0");
                ret.put("messageList", new ArrayList<ContentArticleCustomize>());
            }
    	} catch (Exception e) {
    	    ret.put("status", "1");
            ret.put("statusDesc", "系统异常请稍后再试");
            ret.put("messageCount", "0");
            ret.put("messageList", new ArrayList<ContentArticleCustomize>());
            return ret;
        }
    	LogUtil.endLog(THIS_CLASS, AppContentArticleDefine.GET_CONTENT_ARTICLE_LIST_BY_TYPE_ACTION);
    	return ret;
    	
    }
    
    
    /**
     * 返回文章翻页信息
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AppContentArticleDefine.GET_CONTENT_ARTICLE_FLIP_ACTION) 
    public JSONObject getContentArticleFlip(HttpServletRequest request, HttpServletResponse response,@ModelAttribute() AppContentArticleBean form) {
        LogUtil.startLog(THIS_CLASS, AppContentArticleDefine.GET_CONTENT_ARTICLE_FLIP_ACTION);
        JSONObject ret = new JSONObject();
        ret.put("status", "0");
        ret.put("statusDesc", "请求成功");
        ret.put("request", AppContentArticleDefine.REQUEST_HOME+AppContentArticleDefine.REQUEST_MAPPING+AppContentArticleDefine.GET_CONTENT_ARTICLE_FLIP_ACTION);
        try {

            // 检查参数正确性
            if (Validator.isNull(form.getOffset()) || Validator.isNull(form.getMessageId())){    
                ret.put("status", "1");
                ret.put("statusDesc", "请求参数非法");
                return ret;
            }
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("messageId", form.getMessageId());
            params.put("type", form.getType());
            // 查询总数
            ContentArticleCustomize contentArticleCustomize = appContentArticleService.getContentArticleFlip(params,form.getOffset());
            
            if(contentArticleCustomize!=null){
                ret.put("messageId", contentArticleCustomize.getMessageId());
                ret.put("messageUrl", contentArticleCustomize.getMessageUrl());
                ret.put("shareTitle", contentArticleCustomize.getTitle());
                ret.put("shareContent", contentArticleCustomize.getShareContent());
                ret.put("sharePicUrl", contentArticleCustomize.getSharePicUrl());
                ret.put("shareUrl", contentArticleCustomize.getShareUrl());
            }else{
                ret.put("messageId", "");
                ret.put("messageUrl", "");
                ret.put("shareTitle", "");
                ret.put("shareContent", "");
                ret.put("sharePicUrl", "");
                ret.put("shareUrl", "");
            }
        } catch (Exception e) {
            ret.put("status", "1");
            ret.put("statusDesc", "系统异常请稍后再试");
            ret.put("messageCount", "0");
            ret.put("messageList", new ArrayList<ContentArticleCustomize>());
            return ret;
        }
        LogUtil.endLog(THIS_CLASS, AppContentArticleDefine.GET_CONTENT_ARTICLE_FLIP_ACTION);
        return ret;
        
    }
    
    
	
    /**
     * 根据id返回文章详情
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AppContentArticleDefine.GET_CONTENT_ARTICLE_ID_ACTION) 
    public BaseResultBeanFrontEnd getContentArticleById(@PathVariable("contentArticleId") Integer contentArticleId,@PathVariable("type") Integer type) {

        AppContentArticleResultBean result = new AppContentArticleResultBean();
        result.setStatus(AppContentArticleResultBean.SUCCESS);
        result.setStatusDesc(AppContentArticleResultBean.SUCCESS_MSG);
        
        result.setTopTitle(getTopTitle(type));
        try {
            if(type != null && 4 == type){//3.0.9 新增推送公告详情
                AppPushManage manageInfo = appContentArticleService.getAppPushManagerContentByID(contentArticleId);
                if(manageInfo != null){
                    result.setTopTitle(manageInfo.getTitle());
                    JSONObject details = new JSONObject();
                    details.put("title",manageInfo.getTitle());
                    details.put("content",manageInfo.getContent());
                    details.put("date", GetDate.times10toStrYYYYMMDD(manageInfo.getCreateTime()));
                    result.setDetails(details);
                }
            }else{
                // 根据id返回文章详情
                ContentArticle contentArticle = appContentArticleService.getContentArticleById(contentArticleId);
                if(contentArticle!=null){
                    JSONObject details = new JSONObject();
                    details.put("title",contentArticle.getTitle());
                    details.put("content",contentArticle.getContent());
                    details.put("date",new SimpleDateFormat("yyyy-MM-dd").format(contentArticle.getCreateTime()));
                    result.setDetails(details);
                }else{
                    result.setStatus(AppContentArticleResultBean.FAIL);
                    result.setStatusDesc(AppContentArticleResultBean.FAIL_MSG);
                }
            }

        } catch (Exception e) {
            result.setStatus(AppContentArticleResultBean.FAIL);
            result.setStatusDesc(AppContentArticleResultBean.FAIL_MSG);
        }
        
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




