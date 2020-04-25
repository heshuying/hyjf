package com.hyjf.web.aboutus;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.http.HttpClientUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.mapper.customize.OperationReportCustomizeMapper;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.web.home.HomePageDefine;
import com.hyjf.web.platdatastatistics.PlatDataStatisticsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 关于我们~页面~~
 */
@Controller
@RequestMapping(AboutUsDefine.REQUEST_MAPPING)
public class AboutUsController {
	@Autowired
	private AboutUsService aboutUsService;

	@Autowired
	private PlatDataStatisticsService statisticsService;

	private static String HOST = PropUtils.getSystem("hyjf.web.host").trim();

	private static DecimalFormat DF_FOR_VIEW = new DecimalFormat("#,##0.00");

	/**
	 * 关于我们
	 * 
	 * @author Libin
	 * @return
	 */
	@RequestMapping(value = AboutUsDefine.ABOUTUS_ACTION)
	public ModelAndView aboutus(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(AboutUsController.class.toString(), AboutUsDefine.ABOUTUS_ACTION);
		ModelAndView modelAndView = new ModelAndView(AboutUsDefine.ABOUTUS_PATH);
		// 1.公司简介
		ContentArticle aboutus = this.aboutUsService.getAboutUs();
		String contents = aboutus.getContent();
		//获取累计出借金额
		String totalInvestmentAmount = aboutUsService.getTotalInvestmentAmount();
		totalInvestmentAmount =  StringUtils.isBlank(totalInvestmentAmount)?"0.00":DF_FOR_VIEW.format(new BigDecimal(totalInvestmentAmount));
		contents = contents.replaceAll("money",totalInvestmentAmount.substring(0,totalInvestmentAmount.indexOf(".")));
        aboutus.setContent(contents);
		modelAndView.addObject("aboutus", aboutus);
		// 2.获取创始人信息
		Team team = aboutUsService.getFounder();
		modelAndView.addObject("founder", team);
		LogUtil.endLog(AboutUsController.class.toString(), AboutUsDefine.ABOUTUS_ACTION);
		return modelAndView;
	}

	/**
	 * 合作伙伴
	 * 
	 * @author Libin
	 * @return
	 */
	@RequestMapping(value = AboutUsDefine.PARTNERS_ACTION)
	public ModelAndView partners(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(AboutUsController.class.toString(), AboutUsDefine.PARTNERS_ACTION);
		ModelAndView modelAndView = new ModelAndView(AboutUsDefine.PARTNERS_PATH);

		List<Links> serviceSupportList = aboutUsService.getPartnersList(10);// 服务支持
		List<Links> lawSupportList = aboutUsService.getPartnersList(11);// 法律支持
		List<Links> financeOrgList = aboutUsService.getPartnersList(7);// 金融机构
		List<Links> otherOrgList = aboutUsService.getPartnersList(12);// 其他机构

		modelAndView.addObject("serviceSupportList", serviceSupportList);
		modelAndView.addObject("lawSupportList", lawSupportList);
		modelAndView.addObject("financeOrgList", financeOrgList);
		modelAndView.addObject("otherOrgList", otherOrgList);

		LogUtil.endLog(AboutUsController.class.toString(), AboutUsDefine.PARTNERS_ACTION);
		return modelAndView;
	}

	/**
	 * 联系我们
	 * 
	 * @author Libin
	 * @return
	 */
	@RequestMapping(value = AboutUsDefine.CONTACTUS_ACTION)
	public ModelAndView contactus(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView(AboutUsDefine.CONTACTUS_PATH);
		ContentArticle contactUs = this.aboutUsService.getContactUs();
		modelAndView.addObject("contactUs", contactUs);
		return modelAndView;
	}

	/**
	 * 招贤纳士
	 * 
	 * @author Libin
	 * @return
	 */
	@RequestMapping(value = AboutUsDefine.JOBS_ACTION)
	public ModelAndView jobs(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(AboutUsController.class.toString(), AboutUsDefine.JOBS_ACTION);
		ModelAndView modelAndView = new ModelAndView(AboutUsDefine.JOBS_PATH);
		List<Jobs> jobList = this.aboutUsService.getJobsList();
		modelAndView.addObject("jobList", jobList);
		LogUtil.endLog(AboutUsController.class.toString(), AboutUsDefine.JOBS_ACTION);
		return modelAndView;
	}

	/**
	 * 公司纪事
	 * 
	 * @author Libin
	 * @return
	 */
	@RequestMapping(value = AboutUsDefine.EVENTS_ACTION)
	public ModelAndView events(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(AboutUsController.class.toString(), AboutUsDefine.EVENTS_ACTION);
		ModelAndView modelAndView = new ModelAndView(AboutUsDefine.EVENTS_PATH);
		// 取所有年份和所有event
		List<Events> eventsList = this.aboutUsService.getEventsList();
		modelAndView.addObject("eventsList", eventsList);
		LogUtil.endLog(AboutUsController.class.toString(), AboutUsDefine.EVENTS_ACTION);
		return modelAndView;
	}

	/**
	 * 根据ID获取公司纪事详情
	 * @param id
	 * @Author : huanghui
	 * @return
	 */
	@RequestMapping(value = AboutUsDefine.EVENTS_DETAIL_ACTION)
	public ModelAndView getEventDetail(Integer id){
		LogUtil.startLog(AboutUsController.class.toString(), AboutUsDefine.EVENTS_DETAIL_ACTION);
		ModelAndView modelAndView = new ModelAndView(AboutUsDefine.EVENTS_DETAIL_PAGE);

		Events eventsContent = aboutUsService.getEventRecordDetail(id);

		modelAndView.addObject("eventsContent", eventsContent);
		LogUtil.endLog(AboutUsController.class.toString(), AboutUsDefine.EVENTS_DETAIL_ACTION);
		return modelAndView;
	}

	/**
	 * 网贷知识
	 * 
	 * @author Libin
	 * @return
	 */
	@RequestMapping(value = AboutUsDefine.KNOW_REPORT_LIST_INIT_ACTION)
	public ModelAndView getLoanKnowList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView(AboutUsDefine.KNOW_REPORT_LIST_PATH);
		return modelAndView;
	}

	/**
	 * ajax查询 网贷知识 动态列表
	 * 
	 * @author Libin
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AboutUsDefine.KNOW_REPORT_LIST_ACTION)
	public CompanyDynamicsListResult getKnowReportList(AboutUsPageBean form) {
		LogUtil.startLog(AboutUsController.class.toString(), AboutUsDefine.KNOW_REPORT_LIST_ACTION);
		CompanyDynamicsListResult result = new CompanyDynamicsListResult();
		String noticeType = "3";
		int totalPage = aboutUsService.countHomeNoticeList(noticeType);
		if (totalPage > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), totalPage, form.getPageSize());
			List<ContentArticle> recordList = aboutUsService.searchHomeNoticeList(noticeType, paginator.getOffset(), paginator.getLimit());
			if (recordList != null && recordList.size() != 0) {
				for (int i = 0; i < recordList.size(); i++) {
					recordList.get(i).setContent((recordList.get(i).getContent().replaceAll("src=\"//", "src=\"" + HOST + "//")));
				}
				result.setMediaReportList(recordList);
				result.setPaginator(paginator);
				result.setHost(AboutUsDefine.HOST);
			}
		}
		LogUtil.endLog(AboutUsController.class.toString(), AboutUsDefine.KNOW_REPORT_LIST_ACTION);
		result.success();
		return result;
	}

	/**
	 * 
	 * 风险教育
	 * 
	 * @author Liushouyi
	 * @param form
	 * @return
	 */
	@RequestMapping(value = AboutUsDefine.FX_RISK_ACTION)
	public ModelAndView fxRisk(AboutUsPageBean form) {
		ModelAndView modelAndView = new ModelAndView(AboutUsDefine.FX_RISK_PATH);
		return modelAndView;
	}
	
	/**
	 * 
	 * 法律法规
	 * 
	 * @author Liushouyi
	 * @param form
	 * @return
	 */
	@RequestMapping(value = AboutUsDefine.FX_RISK_LAW_ACTION)
	public ModelAndView fxRiskLaw(AboutUsPageBean form) {
		ModelAndView modelAndView = new ModelAndView(AboutUsDefine.FX_RISK_LAW_PATH);
		return modelAndView;
	}
	
	/**
	 * 
	 * 实时政策
	 * 
	 * @author Libin
	 * @param form
	 * @return
	 */
	@RequestMapping(value = AboutUsDefine.FX_REPORT_LIST_INIT_ACTION)
	public ModelAndView searchFXReportList(AboutUsPageBean form) {
		ModelAndView modelAndView = new ModelAndView(AboutUsDefine.FX_REPORT_LIST_PATH);
		return modelAndView;
	}

	/**
	 * ajax查询 风险教育 动态列表
	 * 
	 * @author Libin
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AboutUsDefine.FX_REPORT_LIST_ACTION)
	public CompanyDynamicsListResult getFXReportList(AboutUsPageBean form) {
		LogUtil.startLog(AboutUsController.class.toString(), AboutUsDefine.FX_REPORT_LIST_ACTION);
		CompanyDynamicsListResult result = new CompanyDynamicsListResult();
		String noticeType = "101";	
		int totalPage = aboutUsService.countHomeNoticeList(noticeType);
		if (totalPage > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), totalPage, form.getPageSize());
			List<ContentArticle> recordList = aboutUsService.searchHomeNoticeList(noticeType, paginator.getOffset(), paginator.getLimit());
			if (recordList != null && recordList.size() != 0) {
				for (int i = 0; i < recordList.size(); i++) {
					recordList.get(i).setContent((recordList.get(i).getContent().replaceAll("src=\"//", "src=\"" + HOST + "//")));
				}
				result.setMediaReportList(recordList);
				result.setPaginator(paginator);
				result.setHost(AboutUsDefine.HOST);
			}
		}
		LogUtil.endLog(AboutUsController.class.toString(), AboutUsDefine.FX_REPORT_LIST_ACTION);
		result.success();
		return result;
	}

	/**
	 * 
	 * 获取媒体报道（风险教育 +网贷知识）详情
	 * 
	 * @author Libin
	 * @param id
	 * @return
	 */
	@RequestMapping(value = AboutUsDefine.MEDIA_REPORT_DETAIL_ACTION)
	public ModelAndView getMediaReportInfo(Integer id) {

		ModelAndView modelAndView = null;

		// 根据type查询 风险教育 或 媒体报道 或 网贷知识
		ContentArticle mediaReport = aboutUsService.getHomeNoticeInfo(id);
		if(!"".equals(mediaReport.getType()) && mediaReport.getType().equals("101")){
			// 风险教育
			modelAndView = new ModelAndView(AboutUsDefine.FX_REPORT_INFO_PATH);
		} else if(!"".equals(mediaReport.getType()) && mediaReport.getType().equals("3")){
			// 网贷知识
			modelAndView = new ModelAndView(AboutUsDefine.MEDIA_REPORT_INFO_PATH);
		}
		if (mediaReport != null) {
			if (mediaReport.getContent().contains("../../../..")) {
				mediaReport.setContent(mediaReport.getContent().replaceAll("../../../..", HomePageDefine.HOST));
			} else if (mediaReport.getContent().contains("src=\"/")) {
				mediaReport.setContent(mediaReport.getContent().replaceAll("src=\"/", "src=\"" + HomePageDefine.HOST) + "//");
			}
		}
		modelAndView.addObject("mediaReport", mediaReport);
		return modelAndView;
	}
	
    /**
     * 
     * 运营报告
     * @author Libin
     * @return
     */
    @RequestMapping(value = AboutUsDefine.REPORT_INIT_ACTION)
    public ModelAndView getOperationReport(HttpServletRequest request, HttpServletResponse response) {
    	LogUtil.startLog(AboutUsController.class.toString(), AboutUsDefine.REPORT_INIT_ACTION);
    	DecimalFormat df = CustomConstants.DF_FOR_VIEW;
		df.setRoundingMode(RoundingMode.FLOOR);
		
    	ModelAndView modelAndView = new ModelAndView(AboutUsDefine.REPORT_PATH);
        //ModelAndView modelAndView = new ModelAndView("/report/report-list");
//    	//累积出借数额
//    	BigDecimal tenderSum = this.aboutUsService.getTenderSum();
//    	String sumTender = df.format(tenderSum);
//    	//累积收益数额(不加本金)
//    	BigDecimal profitSum = this.aboutUsService.getProfitSum();
//    	String sumProfit = df.format(profitSum);
    	modelAndView.addObject("sumTender", statisticsService.selectTotalInvest().setScale(2, BigDecimal.ROUND_DOWN));
    	modelAndView.addObject("sumProfit", statisticsService.selectTotalInterest().setScale(2, BigDecimal.ROUND_DOWN));
    	
    	LogUtil.endLog(AboutUsController.class.toString(), AboutUsDefine.REPORT_INIT_ACTION);
        return modelAndView;
    }
    
    /**
     * 
     * 运营报告详情
     * @author Libin
     * @return
     */
    @RequestMapping(value = AboutUsDefine.REPORT_DETAIL_ACTION)
    public ModelAndView reportDetail(HttpServletRequest request, HttpServletResponse response) {
    	LogUtil.startLog(AboutUsController.class.toString(), AboutUsDefine.REPORT_DETAIL_ACTION);
    	ModelAndView modelAndView = new ModelAndView(AboutUsDefine.REPORT_PATH);
    	String reportPage = request.getParameter("report");
    	if(null != reportPage) {
    		reportPage = reportPage.replace(".", "");
    	}
    	if(StringUtils.isNotBlank(reportPage)){
    		modelAndView = new ModelAndView("report/"+reportPage);
    	}
    	LogUtil.endLog(AboutUsController.class.toString(), AboutUsDefine.REPORT_DETAIL_ACTION);
        return modelAndView;
    }
    
    /**
     * 
     * 手机端下载
     * @author Libin
     * @return
     */
    @RequestMapping(value = AboutUsDefine.MOBILE_DOWNLOAD_ACTION)
    public ModelAndView mobileDownload(HttpServletRequest request, HttpServletResponse response) {
    	LogUtil.startLog(AboutUsController.class.toString(), AboutUsDefine.MOBILE_DOWNLOAD_ACTION);
    	ModelAndView modelAndView = new ModelAndView(AboutUsDefine.MOBILE_DOWNLOAD_PATH);
    	LogUtil.endLog(AboutUsController.class.toString(), AboutUsDefine.MOBILE_DOWNLOAD_ACTION);
        return modelAndView;
    }
    /**
     * 信息披露
     * 
     * @author pcc
     * @return
     */
    @RequestMapping(value = AboutUsDefine.INFORMATION_ACTION)
    public ModelAndView getInformation(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView(AboutUsDefine.INFORMATION_PATH);
        return modelAndView;
    }
    /**
     *  从业机构基本信息
     * 
     * @author pcc
     * @return
     */
    @RequestMapping(value = AboutUsDefine.BASIC_ACTION)
    public ModelAndView getBasic(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView(AboutUsDefine.BASIC_PATH);
        return modelAndView;
    }
    /**
     *  从业机构治理信息
     * 
     * @author pcc
     * @return
     */
    @RequestMapping(value = AboutUsDefine.GOVERNMENT_ACTION)
    public ModelAndView getGovernment(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView(AboutUsDefine.GOVERNMENT_PATH);
        return modelAndView;
    }
    /**
     *  从业机构基本信息
     * 
     * @author pcc
     * @return
     */
    @RequestMapping(value = AboutUsDefine.EVENT_ACTION)
    public ModelAndView getEvent(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView(AboutUsDefine.EVENT_PATH);
        return modelAndView;
    }

    /**
     *  备案信息
     * 
     * @author Liushouyi
     * @return
     */
    @RequestMapping(value = AboutUsDefine.RECORD_ACTION)
    public ModelAndView getRecord(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView(AboutUsDefine.RECORD_PATH);
        return modelAndView;
    }

	/**
	 *  信披声明
	 *
	 * @author Wangtianyang
	 * @return
	 */
	@RequestMapping(value = AboutUsDefine.COMMITMENT_ACTION)
	public ModelAndView getCommitment(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView(AboutUsDefine.COMMITMENT_PATH);
		return modelAndView;
	}

	/**
     * 信息披露（关于我们）
     */
    @RequestMapping(value = AboutUsDefine.AUDIT_INFO_PAGE)
    public ModelAndView auditInfo(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView(AboutUsDefine.AUDIT_INFO_PATH);
        return modelAndView;
    }    
    
    public static void main(String[] args) {
        Map<String, String> params = new HashMap<String, String>();
        final String SOA_INTERFACE_KEY="c430totb012s";
        String userId="3742";
        String borrowNid="HBD17120271";
        String money="500";
        String platform="0";
        String couponGrantId="10851";
        String ordId="15145310229064237544";
        String ip="10.10.3.80";
        String couponOldTime="1514201989";
        // 用户编号
        params.put("userId", userId);
        // 项目编号
        params.put("borrowNid", borrowNid);
        // 出借金额
        params.put("money", money);
        // 平台标识
        params.put("platform", platform);
        // 优惠券id
        params.put("couponGrantId", couponGrantId);
        // 订单号
        params.put("ordId", ordId);
        // ip
        params.put("ip", ip);
        // 排他check
        params.put("couponOldTime", couponOldTime);
        String timestamp = GetDate.getNowTime10() + "";
        // 时间戳
        params.put("timestamp", timestamp);
        // 发放优惠券url
        String requestUrl = "http://10.10.3.80:8080/hyjf-api-web/invest/couponTender.json";
        String sign = StringUtils.lowerCase(MD5.toMD5Code(SOA_INTERFACE_KEY + userId + borrowNid + money + platform
                + couponGrantId + ip + ordId + couponOldTime + timestamp + SOA_INTERFACE_KEY));
        params.put("chkValue", sign);

        String result = HttpClientUtils.post(requestUrl, params);
        JSONObject status = JSONObject.parseObject(result);
        System.out.println(status.toJSONString());
    }
}
