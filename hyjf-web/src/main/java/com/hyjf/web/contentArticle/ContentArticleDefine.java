package com.hyjf.web.contentArticle;

import com.hyjf.common.util.PropUtils;

public class ContentArticleDefine {

	/** 文章内容 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/contentarticle";

    /** 安全保障页面 @RequestMapping值 */
    public static final String SECURITY_ACTION = "/getSecurityPage";

	/** 获取公司动态数据 @RequestMapping值 */
	public static final String COMPANY_DYNAMICS_LIST_ACTION = "/getCompanyDynamicsList";
	
	/** 获取公司动态数据 @RequestMapping值 */
    public static final String COMPANY_DYNAMICS_LIST_PATH = "contentarticle/company_dynamics_list";
    
    /** 获取公司动态数据 @RequestMapping值 */
    public static final String COMPANY_DYNAMICS_DETAIL_PATH = "contentarticle/company_dynamics_detail";

	/** 获取公司动态数据详情 @RequestMapping值 */
	public static final String COMPANY_DYNAMICS_DETAIL_ACTION = "/getCompanyDynamicsDetail";
	
	/** 获取网站公告数据详情 @RequestMapping值 */
	public static final String COMPANY_NOTICE_DETAIL_ACTION = "/getCompanyNoticeDetail";

	/** 获取公司动态数据列表 @RequestMapping值 */
	public static final String COMPANY_DYNAMICS_LISTPAGE_ACTION = "/getCompanyDynamicsListPage";

	/** 获取公司动态数据列表Alone @RequestMapping值 */
	public static final String COMPANY_DYNAMICS_LISTPAGEALONE_ACTION = "/getCompanyDynamicsListPageAlone";
	
	/** hyjf.web.host */
	public static String HOST = PropUtils.getSystem("hyjf.web.host").trim();
	
	/** 消息类型-公司动态 */
	public static final String NOTICE_TYPE_COMPANY_DYNAMICS = "20";
		
	/** 列表画面-网站公告 @RequestMapping值 */
	public static final String SITE_NOTICE_ACTION = "/siteNotices";
	
	/** 活动列表-网站公告 @RequestMapping值 */
	public static final String GET_NOTICE_LIST_PAGE_ACTION = "/getNoticeListPage";
	
	/** 网站公告画面 路径 */
	public static final String SITE_NOTICE_PATH = "contentarticle/newannounce-list";

	/** 网站公告详情@RequestMapping值 */
    public static final String SITE_NOTICE_DETAIL_ACTION = "/getNoticeInfo";
    
    public static final String NOTICE_DYNAMICS_DETAIL_PATH = "contentarticle/notice_dynamics_detail";

    /** 网站公告详情 	路径@RequestMapping值 */
    public static final String SITE_NOTICE_DETAIL_PATH = "contentarticle/newannounce-detail";
    
    /** FROM */
    public static final String NOTICE_LIST_FORM = "noticelistForm";

    /** 获取银行存管限额配置数据 */
    public static final String RECHARGE_RULE_ACTION = "/rechargeRule";
}
