package com.hyjf.web.home;

import com.hyjf.web.BaseDefine;

public class HomePageDefine extends BaseDefine {

	/** 汇天利数据统计 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/homepage";

	/** 首页面跳转 @RequestMapping值 */
	public static final String HOME_ACTION = "/home";

	/** 计算器页面跳转 @RequestMapping值 */
	public static final String CALC_ACTION = "/calc";

	/** 五周年庆宣传页页面跳转 @RequestMapping值 */
	public static final String FIVEFESTIVAL_ACTION = "/fivefestival";

	/** 统计 @RequestMapping值 */
	public static final String INVEST_STATISTICS_ACTION = "investstatistics";

	/** 统计 @RequestMapping值 */
	public static final String HZT_LIST_ACTION = "hztlist";

	/** 项目详情企业或者个人 @RequestMapping值 */
	public static final String HZT_DETAIL_ACTION = "hztdetail";

	/** 项目详情企业 @RequestMapping值 */
	public static final String HZT_INVEST_LIST_ACTION = "hztinvestlist";

	/** 统计 @RequestMapping值 */
	public static final String HXF_LIST_ACTION = "hxflist";

	/** 项目详情企业或者个人 @RequestMapping值 */
	public static final String HXF_DETAIL_ACTION = "hxfdetail";

	/** 项目详情企业 @RequestMapping值 */
	public static final String HXF_INVEST_LIST_ACTION = "hxfinvestlist";

	/** 项目详情企业 @RequestMapping值 */
	public static final String HXF_CONSUME_LIST_ACTION = "hxfconsumelist";

	/** 文章 @RequestMapping值 */
	public static final String CONTENT_LIST_ACTION = "noticelist";

	/** 文章详情 @RequestMapping值 */
	public static final String CONTENT_INFO_ACTION = "noticeInfo";

	/** 合作伙伴 @RequestMapping值 */
	public static final String PARTNER_LIST_ACTION = "linkslist";

	/** 首页BANNER广告 @RequestMapping值 */
	public static final String ADS_LIST_ACTION = "adslist";

	/** 首页banner@RequestMapping */
	public static final String ADS_LISTPAGE_ACTION = "getAdsList";

	/** 媒体报道@RequestMapping */
	public static final String MEDIA_REPORT_LIST_INIT_ACTION = "searchMediaReportList";
	/** 风险教育@RequestMapping */
	public static final String FX_REPORT_LIST_INIT_ACTION = "searchFXReportList";

	/** ajax查询请求媒体报道@RequestMapping */
	public static final String MEDIA_REPORT_LIST_ACTION = "getMediaReportList";
	/** ajax查询请求风险教育@RequestMapping */
	public static final String FX_REPORT_LIST_ACTION = "getFXReportList";

	/** 媒体报道详情@RequestMapping *//*
	public static final String MEDIA_REPORT_DETAIL_ACTION = "getMediaReportInfo";*/

	/** 关于我们左侧菜单 @RequestMapping值 */
	public static final String ABOUT_MENU_ACTION = "aboutLeftMenu";

	/** 招贤纳士 @RequestMapping值 */
	public static final String JOBS_LIST_ACTION = "jobslist";

	/** 公司纪事 @RequestMapping值 */
	public static final String EVENTS_LIST_ACTION = "eventslist";

	/** 公司纪事 @RequestMapping值 */
	public static final String GET_CHUANGSHIREN_ACTION = "getChuangshiren";
	
	
	/** 信息系统安全等级保护备案证明 @RequestMapping值 */
    public static final String SYSTEM_SAFETY_LEVEL_PROTECTION_RECORD_INIT = "systemSafetyLevelProtectionRecordInit";
	

	/** 统计类名 */
	public static final String THIS_CLASS = HomePageController.class.getName();

	/** 消息类型-公司动态 */
	public static final String NOTICE_TYPE_COMPANY_DYNAMICS = "20";

	/** 媒体报道列表 */
	public static final String MEDIA_REPORT_LIST_PATH = "aboutus/media";
	/** 风险教育列表 */
	public static final String FX_REPORT_LIST_PATH = "aboutus/fengxian";

	/** 媒体报道详情 */
	public static final String MEDIA_REPORT_INFO_PATH = "aboutus/media-detail";
	/** 风险详情 */
	public static final String FX_REPORT_INFO_PATH = "aboutus/fengxian-detail";
	/** 信息系统安全等级保护备案证明列表 */
    public static final String SYSTEM_SAFETY_LEVEL_PROTECTION_RECORD_PATH = "aboutus/record-filing";
    
	/** 查询最新更新地址 @RequestMapping值 */
	public static final String APP_DL_LASTEST_URL = "lastesturl";
}
