/**
 * Description:汇直投列表查询控制器
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.wecat;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.calculate.*;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.customize.web.*;
import com.hyjf.mybatis.model.customize.wecat.WecatProjectListCustomize;
import com.hyjf.web.BaseController;
import com.hyjf.web.project.ProjectFileBean;
import com.hyjf.web.project.ProjectService;
import com.hyjf.web.project.RepayPlanBean;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("wecatProjectListController")
@RequestMapping(value = ProjectListDefine.REQUEST_MAPPING)
public class ProjectListController extends BaseController {

	@Autowired
	private ProjectListService projectListService;

    @Autowired
    private ProjectService projectService;
    /** 发布地址 */
    private static String HOST_URL = PropUtils.getSystem("hyjf.web.host");

	/**
	 * 获取汇直投列表
	 * 
	 * @param hzt
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = ProjectListDefine.PROJECT_LIST_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String searchHztList(@ModelAttribute ProjectListBean hzt, HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(ProjectListDefine.THIS_CLASS, ProjectListDefine.PROJECT_LIST_ACTION);
		JSONObject ret = new JSONObject();
		JSONObject info = new JSONObject();
		this.createProjectListPage(request, info, hzt);
		ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
		ret.put(CustomConstants.DATA, info);
		ret.put(CustomConstants.MSG, "");
		LogUtil.endLog(ProjectListDefine.THIS_CLASS, ProjectListDefine.PROJECT_LIST_ACTION);
		return JSONObject.toJSONString(ret, true);
	}

	/**
	 * 创建相应的汇直投相应的列表分页
	 * 
	 * @param request
	 * @param info
	 * @param form
	 */
	private void createProjectListPage(HttpServletRequest request, JSONObject info, ProjectListBean form) {

	    String status=request.getParameter("status");
		// 统计相应的汇直投的数据记录数
		int recordTotal = this.projectListService.countProjectListRecordTotal(form,status);
		// 李深强修改 by明举 所有产品列表只查2页之内的数据
		int pageNum = 2;
		if(recordTotal > form.getPageSize() * pageNum){
			recordTotal = form.getPageSize() * pageNum;
		}
	    info.put("recordTotal", recordTotal);
        info.put("paginatorPage", form.getPaginatorPage());
        info.put("pageSize", form.getPageSize());
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal, form.getPageSize());
			// 查询相应的汇直投列表数据
			List<WecatProjectListCustomize> recordList = projectListService.searchProjectList(form, paginator.getOffset(), paginator.getLimit(),status);
			info.put("projectlist", recordList);
		} else {
			info.put("projectlist", "");
		}
	}
	/**
     * 微信查询相应的项目详情
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = ProjectListDefine.PROJECT_DETAIL_ACTION)
    @ResponseBody
    public Object searchProjectDetail(HttpServletRequest request, HttpServletResponse response,String borrowNid) {
        Map<String, Object> modelmap=new HashMap<String , Object>();
        LogUtil.startLog(ProjectListDefine.THIS_CLASS, ProjectListDefine.PROJECT_DETAIL_ACTION);
        // 2.根据项目标号获取相应的项目信息
        WebProjectDetailCustomize borrow = this.projectService.selectProjectDetail(borrowNid);
        if (borrow == null) {
            modelmap.put("error", "1");
            modelmap.put("message", "根据id查询借款失败");
            return modelmap;
        } else {
            //去最大出借金额和可投的最小值
        /*    if (borrow.getTenderAccountMax()!=null&&borrow.getInvestAccount()!=null) {
                borrow.setInvestAccount((
                        new BigDecimal(borrow.getTenderAccountMax()).intValue()<
                        new BigDecimal(borrow.getInvestAccount()).intValue()?
                                new BigDecimal(borrow.getTenderAccountMax()).intValue():
                                    new BigDecimal(borrow.getInvestAccount()).intValue())+"");
            }*/
            String nowTime = String.valueOf(GetDate.getNowTime10());
            modelmap.put("nowTime", nowTime);
            String borrowStyle = borrow.getBorrowStyle();
            BigDecimal borrowInterest = new BigDecimal(0);
            switch (borrowStyle) {
            case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“
                // 计算历史回报
                borrowInterest = DuePrincipalAndInterestUtils
                        .getMonthInterest(new BigDecimal(borrow.getBorrowAccount()),
                                new BigDecimal(borrow.getBorrowApr()).divide(new BigDecimal("100")),
                                Integer.parseInt(borrow.getBorrowPeriod()))
                        .divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
                break;
            case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“
                borrowInterest = DuePrincipalAndInterestUtils
                        .getDayInterest(new BigDecimal(borrow.getBorrowAccount()),
                                new BigDecimal(borrow.getBorrowApr()).divide(new BigDecimal("100")),
                                Integer.parseInt(borrow.getBorrowPeriod()))
                        .divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
                break;
            case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：；
                borrowInterest = BeforeInterestAfterPrincipalUtils
                        .getInterestCount(new BigDecimal(borrow.getBorrowAccount()),
                                new BigDecimal(borrow.getBorrowApr()).divide(new BigDecimal("100")),
                                Integer.parseInt(borrow.getBorrowPeriod()), Integer.parseInt(borrow.getBorrowPeriod()))
                        .divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
                break;
            case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：；
                borrowInterest = AverageCapitalPlusInterestUtils
                        .getInterestCount(new BigDecimal(borrow.getBorrowAccount()),
                                new BigDecimal(borrow.getBorrowApr()).divide(new BigDecimal("100")),
                                Integer.parseInt(borrow.getBorrowPeriod()))
                        .divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
                break;
            case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
                borrowInterest = AverageCapitalUtils
                        .getInterestCount(new BigDecimal(borrow.getBorrowAccount()),
                                new BigDecimal(borrow.getBorrowApr()).divide(new BigDecimal("100")),
                                Integer.parseInt(borrow.getBorrowPeriod()))
                        .divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
                break;
            default:
                break;
            }
            borrow.setBorrowInterest(borrowInterest.toString());
            if (borrow.getType().equals("9")) {// 如果项目为汇资产项目
                // 添加相应的项目详情信息
                modelmap.put("projectDeatil", borrow);
                // 4查询相应的汇资产的首页信息
                WebHzcProjectDetailCustomize borrowInfo = this.projectService.searchHzcProjectDetail(borrowNid);
                modelmap.put("borrowInfo", borrowInfo);
                // 处置预案
                WebHzcDisposalPlanCustomize disposalPlan = this.projectService.searchDisposalPlan(borrowNid);
                modelmap.put("disposalPlan", disposalPlan);
                // 5查询相应的还款计划
                List<RepayPlanBean> repayPlanList = this.projectService.getRepayPlan(borrowNid);
                modelmap.put("repayPlanList", repayPlanList);
                // 相关文件
                List<ProjectFileBean> files = this.projectService.searchProjectFiles(borrowNid, HOST_URL);
                modelmap.put("fileList", files);
            } else {// 项目为非汇资产项目
                // 添加相应的项目详情信息
                modelmap.put("projectDeatil", borrow);
                // 4查询非汇资产项目的项目信息
                if (StringUtils.isNotEmpty(borrow.getComOrPer()) && borrow.getComOrPer().equals("1")) {
                    // 查询相应的企业项目详情
                    WebProjectCompanyDetailCustomize borrowInfo = projectService.searchProjectCompanyDetail(borrowNid);
                    modelmap.put("borrowInfo", borrowInfo);
                } else if (StringUtils.isNotEmpty(borrow.getComOrPer()) && borrow.getComOrPer().equals("2")) {
                    // 查询相应的汇直投个人项目详情
                    WebProjectPersonDetailCustomize borrowInfo = projectService.searchProjectPersonDetail(borrowNid);
                    modelmap.put("borrowInfo", borrowInfo);
                }
              
                // 风控信息
                WebRiskControlCustomize riskControl = this.projectService.selectRiskControl(borrowNid);
    			riskControl.setControlMeasures(riskControl.getControlMeasures()==null?"":riskControl.getControlMeasures().replace("\r\n", ""));
    			riskControl.setControlMort(riskControl.getControlMort()==null?"":riskControl.getControlMort().replace("\r\n", ""));
                // 添加风控信息
                modelmap.put("riskControl", riskControl);
                List<WebMortgageCustomize> mortgageList = this.projectService.selectMortgageList(borrowNid);
                // 添加相应的房产信息
                modelmap.put("mortgageList", mortgageList);
                List<WebVehiclePledgeCustomize> vehiclePledgeList = this.projectService
                        .selectVehiclePledgeList(borrowNid);
                // 添加相应的汽车抵押信息
                modelmap.put("vehiclePledgeList", vehiclePledgeList);
                // 5查询相应的认证信息
                List<WebProjectAuthenInfoCustomize> authenList = projectService.searchProjectAuthenInfos(borrowNid);
                modelmap.put("authenList", authenList);
                // 6查询相应的还款计划
                List<RepayPlanBean> repayPlanList = this.projectService.getRepayPlan(borrowNid);
                modelmap.put("repayPlanList", repayPlanList);
                // 7 相关文件
                List<ProjectFileBean> files = this.projectService.searchProjectFiles(borrowNid, HOST_URL);
                modelmap.put("fileList", files);
            }
        

            LogUtil.endLog(ProjectListDefine.THIS_CLASS, ProjectListDefine.PROJECT_DETAIL_ACTION);
            return modelmap;
        }

    }
}
