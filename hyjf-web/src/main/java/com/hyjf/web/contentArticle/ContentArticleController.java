package com.hyjf.web.contentArticle;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.BanksConfig;
import com.hyjf.mybatis.model.auto.ContentArticle;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.web.BaseController;
import com.hyjf.web.home.HomePageDefine;

/**
 * 
 * 活动列表
 * 
 * @author hyjf
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年4月19日
 * @see 上午9:58:53
 * 
 */
@Controller
@RequestMapping(value = ContentArticleDefine.REQUEST_MAPPING)
public class ContentArticleController extends BaseController{
	
	private Logger log= LoggerFactory.getLogger(ContentArticleController.class);
	
	@Autowired
	private ContentArticleService contentArticleService;
	
	private static String HOST = PropUtils.getSystem("hyjf.web.host").trim();
	
	
	/**
	 * 
     * 网站公告页面
     * @author Chenyanwei
     * @return
     * 
     * */
     @RequestMapping(value = ContentArticleDefine.SITE_NOTICE_ACTION)
     public ModelAndView siteNotices(HttpServletRequest request, HttpServletResponse response ,@ModelAttribute(ContentArticleDefine.NOTICE_LIST_FORM) ContentArticleBean form){
     	 LogUtil.startLog(ContentArticleController.class.toString(), ContentArticleDefine.SITE_NOTICE_ACTION);
     	 
    	 ModelAndView modelAndView = new ModelAndView(ContentArticleDefine.SITE_NOTICE_PATH);
    	 
    	 LogUtil.endLog(ContentArticleController.class.toString(), ContentArticleDefine.SITE_NOTICE_ACTION);
         return modelAndView;
     }
     
     /**
      * 
      * 获取网站公告列表
      * @author Chenyanwei
      * @param noticeType
      * @return
      * 
      */
     @ResponseBody  
     @RequestMapping(value = ContentArticleDefine.GET_NOTICE_LIST_PAGE_ACTION)
     public ContentArticleResult getNoticeListPage(ContentArticleBean form) {
    	 LogUtil.startLog(ContentArticleController.class.toString(), ContentArticleDefine.GET_NOTICE_LIST_PAGE_ACTION);
    	 ContentArticleResult result = new ContentArticleResult();
    	 String noticeType = "2";
    	 int totalPage = contentArticleService.getNoticeListCount(noticeType);
    	 if (totalPage > 0) {
    		 Paginator paginator = new Paginator(form.getPaginatorPage(), totalPage, form.getPageSize());//(当前，总条数，每页10条公告)
    		 // 检索活动列表数据
    		 List<ContentArticle> recordList =
                 contentArticleService.searchNoticeList(noticeType, paginator.getOffset(), paginator.getLimit());   
    		 if (recordList != null && recordList.size() != 0) {
				for (int i = 0; i < recordList.size(); i++) {
					recordList.get(i).setContent(
							(recordList.get(i).getContent().replaceAll("src=\"//", "src=\"" + HOST + "//")));	
				}
				result.setContentArticleList(recordList);
				result.setPaginator(paginator);		         
		        result.setHost(ContentArticleDefine.HOST);
    		 }
    	 }
    	 LogUtil.endLog(ContentArticleController.class.toString(), ContentArticleDefine.GET_NOTICE_LIST_PAGE_ACTION);
    	 result.success();
         return result;
     }
     
     
     /**
      * 
      * 获取网站公告详情
      * @author chenyanwei
      * @param id
      * @return
      */
     @RequestMapping(value = ContentArticleDefine.SITE_NOTICE_DETAIL_ACTION)
     public ModelAndView getCompanyNoticeDetail(Integer id) {
         
         ModelAndView modelAndView = new ModelAndView(ContentArticleDefine.NOTICE_DYNAMICS_DETAIL_PATH);
         ContentArticle companyNoticeDetial = contentArticleService.getNoticeInfo(id);
         if (companyNoticeDetial != null) {
             if (companyNoticeDetial.getContent().contains("../../../..")) {
            	 companyNoticeDetial.setContent(companyNoticeDetial.getContent().replaceAll("../../../..", HomePageDefine.HOST));
             } else if (companyNoticeDetial.getContent().contains("src=\"/")) {
            	 companyNoticeDetial.setContent(
            			 companyNoticeDetial.getContent().replaceAll("src=\"/", "src=\"" + HomePageDefine.HOST) + "//");
             }
         }
         modelAndView.addObject("companyNotice", companyNoticeDetial);
         return modelAndView;
     }
     
    
     /**
      * 
      * 公司动态页面
      * @author chenyanwei
      * @param form
      * @return
      */
     @RequestMapping(value = ContentArticleDefine.COMPANY_DYNAMICS_LIST_ACTION)
     public ModelAndView getCompanyDynamicsList(HttpServletRequest request, HttpServletResponse response) {
    	 LogUtil.startLog(ContentArticleController.class.toString(), ContentArticleDefine.COMPANY_DYNAMICS_LIST_PATH);
    	 
         ModelAndView modelAndView = new ModelAndView(ContentArticleDefine.COMPANY_DYNAMICS_LIST_PATH);
         
         LogUtil.endLog(ContentArticleController.class.toString(), ContentArticleDefine.COMPANY_DYNAMICS_LIST_PATH);
         return modelAndView;
     }
       
    
    /**
     * 
     * ajax查询公司动态列表
     * @author renxingchen
     * @param form
     * @return
     * 
     */
    @ResponseBody
    @RequestMapping(value = ContentArticleDefine.COMPANY_DYNAMICS_LISTPAGE_ACTION)
    public ContentArticleResult getCompanyDynamicsListPage(ContentArticleBean form) {
        // 查询公司动态数据
        int totalPage = contentArticleService.getNoticeListCount(ContentArticleDefine.NOTICE_TYPE_COMPANY_DYNAMICS);
        Paginator paginator = new Paginator(form.getPaginatorPage(), totalPage, form.getPageSize());
        List<ContentArticle> companyDynamicsList = contentArticleService.searchNoticeList(
                ContentArticleDefine.NOTICE_TYPE_COMPANY_DYNAMICS, paginator.getOffset(), paginator.getLimit());
        for (ContentArticle companyDynamics : companyDynamicsList) {
            if (companyDynamics.getContent().contains("../../../..")) {
                companyDynamics.setContent(companyDynamics.getContent().replaceAll("../../../..", ContentArticleDefine.HOST));
            } else if (companyDynamics.getContent().contains("src=\"/")) {
                companyDynamics.setContent(companyDynamics.getContent().replaceAll("src=\"/",
                        "src=\"" + ContentArticleDefine.HOST)
                        + "//");
            }
        }
        ContentArticleResult result = new ContentArticleResult();
        result.success();
        result.setPaginator(paginator);
        result.setContentArticleList(companyDynamicsList);
        result.setHost(ContentArticleDefine.HOST);
        return result;
    }
    
    
    /**
     * 
     * 获取公司动态详情
     * @author renxingchen
     * @param id
     * @return
     * 
     */
    @RequestMapping(value = ContentArticleDefine.COMPANY_DYNAMICS_DETAIL_ACTION)
    public ModelAndView getCompanyDynamicsDetail(Integer id) {
        ModelAndView modelAndView = new ModelAndView(ContentArticleDefine.COMPANY_DYNAMICS_DETAIL_PATH);
        ContentArticle companyDynamics = contentArticleService.getNoticeInfo(id);
        if (companyDynamics == null || companyDynamics.getId() == null) {
            modelAndView = new ModelAndView("/error/404");
            return modelAndView;
        }
        if (companyDynamics != null) {
            if (companyDynamics.getContent().contains("../../../..")) {
                companyDynamics.setContent(companyDynamics.getContent().replaceAll("../../../..", HomePageDefine.HOST));
            } else if (companyDynamics.getContent().contains("src=\"/")) {
                companyDynamics.setContent(companyDynamics.getContent().replaceAll("src=\"/","src=\"" + HomePageDefine.HOST) + "//");
            }
            // 避免页面上出现未转换的HTML特殊字符
            companyDynamics.setContent(StringEscapeUtils.unescapeHtml4(companyDynamics.getContent()));
        }
        modelAndView.addObject("companyDynamics", companyDynamics);
        return modelAndView;
    }
    
    
    /**
     * 
     * 页脚页面
     * @author michael
     * @param form
     * @return
     * 
     */
    @RequestMapping(value = ContentArticleDefine.SECURITY_ACTION)
    public ModelAndView getSecurityPage(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView=null;
        String pageType = request.getParameter("pageType");
        if(StringUtils.isBlank(pageType)){
        	 modelAndView = new ModelAndView("/contentarticle/bank-page");
        }else{
        	if(null != pageType) {
        		pageType = pageType.replace(".", "");
        	}
            modelAndView = new ModelAndView("/contentarticle/" + pageType);
            JSONArray data = CommonSoaUtils.getBanksList().getJSONArray("data");
            String str = data.toJSONString();
            List<BanksConfig> list = JSONObject.parseArray(str, BanksConfig.class);
            for (BanksConfig banksConfig : list) {
                BigDecimal monthCardQuota = banksConfig.getMonthCardQuota();
                BigDecimal singleQuota = banksConfig.getSingleQuota();
                BigDecimal singleCardQuota = banksConfig.getSingleCardQuota();
                banksConfig.setSingleQuota(new BigDecimal(CommonUtils.formatBigDecimal(singleQuota.divide(new BigDecimal(10000)))));
                banksConfig.setSingleCardQuota(new BigDecimal(CommonUtils.formatBigDecimal(singleCardQuota.divide(new BigDecimal(10000)))));
                banksConfig.setMonthCardQuota(new BigDecimal(CommonUtils.formatBigDecimal(monthCardQuota.divide(new BigDecimal(10000)))));
            }
            modelAndView.addObject("date", list);
        }
        return modelAndView;
    }

    
    /**
     * 获取银行存管限额配置数据 add by jijun 20180509
     */
    @ResponseBody
	@RequestMapping(ContentArticleDefine.RECHARGE_RULE_ACTION)
	public RechargeDescResultBean rechargeRule() {
		log.info("获取充值限额说明....");
		JSONArray jsonArray = CommonSoaUtils.getBanksList().getJSONArray("data");

		RechargeDescResultBean result = new RechargeDescResultBean();
		if (jsonArray != null) {
			log.info("jsonArray....{}", jsonArray.toJSONString());
			this.conventBanksConfigToResult(result, jsonArray.toJSONString());
		} else {
			log.info("jsonArray....{}", "未获取到返回数据");
		}
		return result;
	}
	
	
	private void conventBanksConfigToResult(RechargeDescResultBean result, String jsonArrayStr) {
		List<BanksConfig> configs = JSON.parseArray(jsonArrayStr, BanksConfig.class);
		if (!CollectionUtils.isEmpty(configs)) {
			List<RechargeDescResultBean.RechargeLimitAmountDesc> list = result.getList();
			RechargeDescResultBean.RechargeLimitAmountDesc bean = null;
			for (BanksConfig config : configs) {
				bean = new RechargeDescResultBean.RechargeLimitAmountDesc();
				bean.setBankName(config.getBankName());
				//单卡单日限额
				if(config.getSingleCardQuota().compareTo(BigDecimal.ZERO)==0){
					bean.setDay("不限");
				}else{
					bean.setDay(String.valueOf(config.getSingleCardQuota().divide(new BigDecimal(10000)))+"万");
				}
				//单笔限额
				if(config.getSingleQuota().compareTo(BigDecimal.ZERO)==0){
					bean.setOnce("不限");
				}else{
					bean.setOnce(String.valueOf(config.getSingleQuota().divide(new BigDecimal(10000)))+"万");
				}
				//单月单卡限额
				if(config.getMonthCardQuota().compareTo(BigDecimal.ZERO)==0){
					bean.setMonth("不限");
				}else{
					bean.setMonth(String.valueOf(config.getMonthCardQuota().divide(new BigDecimal(10000)))+"万");
				}
				
				list.add(bean);
			}
		}
	}
    
}
