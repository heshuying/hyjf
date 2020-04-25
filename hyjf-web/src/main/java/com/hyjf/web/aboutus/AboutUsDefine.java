/**
 * Description:我要融资常量定义
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.aboutus;

import com.hyjf.web.BaseDefine;

public class AboutUsDefine extends BaseDefine {

	
	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/aboutus";

	/** 关于我们 @RequestMapping值 */
	public static final String ABOUTUS_ACTION = "aboutus";
	/** 关于我们画面 路径 */
	public static final String ABOUTUS_PATH = "aboutus/aboutus";

	/** 创始人 @RequestMapping值 */
	public static final String LEADERS_ACTION = "leaders";
	/** 创始人画面 路径 */
	public static final String LEADERS_PATH = "aboutus/leaders";

	/** 合作伙伴 @RequestMapping值 */
	public static final String PARTNERS_ACTION = "partners";
	/** 合作伙伴画面 路径 */
	public static final String PARTNERS_PATH = "aboutus/partners";

	/** 联系我们 @RequestMapping值 */
	public static final String CONTACTUS_ACTION = "contactus";
	/** 联系我们画面 路径 */
	public static final String CONTACTUS_PATH = "aboutus/contactus";

	/** 招贤纳士 @RequestMapping值 */
	public static final String JOBS_ACTION = "jobs";
	/** 招贤纳士画面 路径 */
	public static final String JOBS_PATH = "aboutus/jobs";
	
	/** 公司纪事 @RequestMapping值 */
	public static final String EVENTS_ACTION = "events";
	
	/** 根据年份获取公司纪事 @RequestMapping值 */
	public static final String EVENTS_YEAR_ACTION = "getEventsYear";
	/** 公司纪事画面 路径 */
	public static final String EVENTS_PATH = "aboutus/events";

	/** 获取纪事详情 */
	public static final String EVENTS_DETAIL_ACTION = "getEventDetail";
	/** 纪事详情 画面路径 **/
	public static final String EVENTS_DETAIL_PAGE = "aboutus/events_detail";

	/** 媒体报道 @RequestMapping值 */
	public static final String MEDIA_ACTION = "media";
	/** 媒体报道画面 路径 */
	public static final String MEDIA_PATH = "aboutus/media";
	
	/** 当前controller名称 */
	public static final String THIS_CLASS = AboutUsController.class.getName();

    /** 安全保障页面 @RequestMapping值 */
    public static final String SECURITY_ACTION = "/getSecurityPage";
    
    /*网贷知识*/
	/** 网贷知识列表 @RequestMapping值 */
	public static final String KNOW_REPORT_LIST_INIT_ACTION = "searchKnowReportList";
	/** 网贷知识列表 */
	public static final String KNOW_REPORT_LIST_PATH = "aboutus/know"; 
	/** ajax查询请求网贷知识@RequestMapping */
	public static final String KNOW_REPORT_LIST_ACTION = "getKnowReportList"; 
	/*信息披露*/
    /** 信息披露 @RequestMapping值 */
    public static final String INFORMATION_ACTION = "getInformation";
    /** 信息披露页面 */
    public static final String INFORMATION_PATH = "aboutus/about-information";
    
    /*从业机构基本信息*/
    /** 从业机构基本信息 @RequestMapping值 */
    public static final String BASIC_ACTION = "getBasic";
    /** 从业机构基本信息页面 */
    public static final String BASIC_PATH = "aboutus/about-basic";
    /*从业机构治理信息*/
    /** 从业机构治理信息@RequestMapping值 */
    public static final String GOVERNMENT_ACTION = "getGovernment";
    /** 从业机构治理信息页面 */
    public static final String GOVERNMENT_PATH = "aboutus/about-government";
    /*从业机构治理信息*/
    /** 从业机构治理信息@RequestMapping值 */
    
    public static final String EVENT_ACTION = "getEvent";
    /** 从业机构治理信息页面 */
    public static final String EVENT_PATH = "aboutus/about-event";
/*	*//** 网贷知识列表 @RequestMapping值 *//*
	public static final String RISK_LIST_ACTION = "riskList";
	*//** 网贷知识列表画面 路径 *//*
	public static final String RISK_LIST_PATH = "aboutus/riskList";*/
	/** 备案信息 */
    public static final String RECORD_ACTION = "getRecord";
    public static final String RECORD_PATH = "aboutus/about-record";

	/** 信披声明 */
	public static final String COMMITMENT_ACTION = "getCommitment";
	public static final String COMMITMENT_PATH = "aboutus/about-commitment";

    /** 信息披露  审核信息页面*/
    public static final String AUDIT_INFO_PAGE = "auditInfo";
    public static final String AUDIT_INFO_PATH = "aboutus/about-auditing";
    
	/*风险教育*/
	/** 实时政策@RequestMapping值 */
	public static final String FX_REPORT_LIST_INIT_ACTION = "searchFXReportList";
	/** 风险教育列表 */
	public static final String FX_REPORT_LIST_PATH = "aboutus/fengxian";
	/** ajax查询请求风险教育@RequestMapping */
	public static final String FX_REPORT_LIST_ACTION = "getFXReportList";
	/** 新风险教育 */
	public static final String FX_RISK_ACTION = "fxrisk";
	public static final String FX_RISK_PATH = "aboutus/about-risk";
	/** 法律法规  */
	public static final String FX_RISK_LAW_ACTION = "fxrisklaw";
	public static final String FX_RISK_LAW_PATH = "aboutus/about-risk-law";
	
	/** 媒体报道（风险教育）详情@RequestMapping */
	public static final String MEDIA_REPORT_DETAIL_ACTION = "getMediaReportInfo";
	/** 风险教育详情 */
	public static final String FX_REPORT_INFO_PATH = "aboutus/fengxian-detail";
	/** 网贷知识详情 */
	public static final String MEDIA_REPORT_INFO_PATH = "aboutus/know-detail";
	
	/*运营报告*/
	/** 运营报告 @RequestMapping值 */
	public static final String REPORT_INIT_ACTION = "report";
	/** 运营报告 @RequestMapping值 */
	public static final String REPORT_DETAIL_ACTION = "reportDetail";
	/** 合作伙伴画面 路径 */
	public static final String REPORT_PATH = "aboutus/report-list";
	/** 手机端下载请求 */
	public static final String MOBILE_DOWNLOAD_ACTION = "mobileDownload";
	/** 合作伙伴画面 路径 */
	public static final String MOBILE_DOWNLOAD_PATH = "aboutus/app-download";
	
	

}
