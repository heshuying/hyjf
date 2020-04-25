package com.hyjf.app.user.plan;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.BaseResultBeanFrontEnd;
import com.hyjf.app.user.plan.MyPlanListResultBean.ProjectList;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractService;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.HjhRepay;
import com.hyjf.mybatis.model.auto.TenderAgreement;
import com.hyjf.mybatis.model.customize.app.AppCouponCustomize;
import com.hyjf.mybatis.model.customize.app.AppMyHjhDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppMyPlanCustomize;
import com.hyjf.mybatis.model.customize.web.CurrentHoldRepayMentPlanListCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.UserHjhInvistListCustomize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author xiasq
 * @version MyPlanController, v0.1 2017/11/9 11:07 app 我的账户-我的计划
 */

@RestController
@RequestMapping(MyPlanDefine.REQUEST_MAPPING)
public class MyPlanController extends BaseController {
	private Logger logger = LoggerFactory.getLogger(MyPlanController.class);

	private final String ILLEGAL_PARAMETER_STATUS_DESC = "请求参数非法";
	private final String TOKEN_ISINVALID_STATUS = "Token失效，请重新登录";
	/** 计息时间 */
    //	private final String PLAN_ON_ACCRUAL = "计划进入锁定期后开始计息";
	// mod by nxl 智投服务修改计息时间
	private final String PLAN_ON_ACCRUAL = "从授权出借本金全部放款成功之日开始，至服务回报期限届满。";
	private static DecimalFormat DF_FOR_VIEW = new DecimalFormat("#,##0.00");

	@Autowired
	private MyPlanService myPlanService;
	//法大大
    @Autowired
    private FddGenerateContractService fddGenerateContractService;

	/**
	 * 获取我的计划列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(MyPlanDefine.MYPLAN_LIST_ACTION)
	public MyPlanListResultBean getMyPlanList(HttpServletRequest request) {

		MyPlanListResultBean result = new MyPlanListResultBean(MyPlanDefine.MYPLAN_LIST_REQUEST);
		result.setStatus(CustomConstants.APP_STATUS_SUCCESS);
		result.setStatusDesc(CustomConstants.APP_STATUS_DESC_SUCCESS);

		// 计划的状态：1为持有中，2为已退出，3为退出中
		String type = request.getParameter("type");
		String sign = request.getParameter("sign");

		// 检查参数正确性
		if (Validator.isNull(sign) || Validator.isNull(type) || !Arrays.asList("1", "2" , "3").contains(type)) {
			result.setStatus(CustomConstants.APP_STATUS_FAIL);
			result.setStatusDesc(ILLEGAL_PARAMETER_STATUS_DESC);
			return result;
		}
		Integer userId = null;
		try {
			userId = SecretUtil.getUserId(sign);
		} catch (Exception e) { // token失效
			result.setStatus(CustomConstants.APP_STATUS_FAIL);
			result.setStatusDesc(TOKEN_ISINVALID_STATUS);
			return result;
		}
		if (userId == null) {
			result.setStatus(CustomConstants.APP_STATUS_FAIL);
			result.setStatusDesc(TOKEN_ISINVALID_STATUS);
			return result;
		}
		logger.info("request params: type is: {}, userId is: {}", type, userId);

		// 构建查询条件
		Map<String, Object> params = buildQueryParameter(request);
		params.put("userId", userId);
		params.put("type", Integer.parseInt(type));
		// 待收金额
		result.setMoney(DF_FOR_VIEW.format(new BigDecimal(myPlanService.getMyPlanWaitAmountTotal(userId))));
		
		//add by pcc app3.1.1追加 20180823 start
		//返回退出中测试造假数据
		if("3".equals(type)){
			Integer count = 3;
			result.setProjectTotal(count);
			List<ProjectList> projectList = createExitLabelShowFlag();
			result.setProjectList(projectList);
		}else{
			// 查询我的汇计划总数
			Integer count = myPlanService.countAppMyPlan(params);
			if (count != null && count > 0) {
				result.setProjectTotal(count);
				List<AppMyPlanCustomize> projectList = myPlanService.selectAppMyPlanList(params);
				result.setProjectList(convertAppMyPlanToReturnBean(projectList, request));

			} else {
				result.setProjectTotal(0);
				result.setProjectList(new ArrayList<MyPlanListResultBean.ProjectList>());
			}
		}
		//add by pcc app3.1.1追加 20180823 end
		result.setType(type);
		return result;
	}
	//临时构建测试参数使用
	private List<ProjectList> createExitLabelShowFlag() {
		//JSON数组
		String jsonStr="[{\"borrowName\" : \"月月盈M / 3\",\"borrowTheThirdDesc\" : \"加入时间\",\"label\" : \"\","+
				"\"borrowUrl\" : \"https: //testapp3.hyjf.com/user/plan/25349235013991517856?type=3&couponType=0\","+
				"\"borrowTheSecond\" : \"3 个月\",\"type\" : \"1\",\"borrowTheSecondDesc\" : \"锁定期限\","+
				"\"borrowTheFirstDesc\" : \"加入金额\",\"borrowTheFirst\" : \"200.00\",\"couponType\" : \"2\","+
				"\"borrowNid\" : \"HJH20180802\",\"borrowTheThird\" : \"2018-08-22\"},"+
				
				"{\"borrowName\" : \"月月盈M / 3\",\"borrowTheThirdDesc\" : \"加入时间\",\"label\" : \"\","+
				"\"borrowUrl\" : \"https: //testapp3.hyjf.com/user/plan/25349235013991517856?type=3&couponType=0\","+
				"\"borrowTheSecond\" : \"3 个月\",\"type\" : \"1\",\"borrowTheSecondDesc\" : \"锁定期限\","+
				"\"borrowTheFirstDesc\" : \"加入金额\",\"borrowTheFirst\" : \"200.00\",\"couponType\" : \"0\","+
				"\"borrowNid\" : \"HJH20180802\",\"borrowTheThird\" : \"2018-08-22\"},"+
				
				"{\"borrowName\" : \"月月盈M / 3\",\"borrowTheThirdDesc\" : \"加入时间\",\"label\" : \"\","+
				"\"borrowUrl\" : \"https: //testapp3.hyjf.com/user/plan/25349235013991517856?type=3&couponType=0\","+
				"\"borrowTheSecond\" : \"3 个月\",\"type\" : \"1\",\"borrowTheSecondDesc\" : \"锁定期限\","+
				"\"borrowTheFirstDesc\" : \"加入金额\",\"borrowTheFirst\" : \"200.00\",\"couponType\" : \"0\","+
				"\"borrowNid\" : \"HJH20180802\",\"borrowTheThird\" : \"2018-08-22\"}]";
		JSONArray projectListJson = JSONObject.parseArray(jsonStr);
		//将JSON数组转换成数组对象
		List<ProjectList> projectList = JSONObject.toJavaObject(projectListJson, List.class);
		return projectList;
	}

	/**
	 * 计划详细页面 H5
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(MyPlanDefine.MYPLAN_DETAIL_ACTION)
	public MyPlanDetailResultBean getMyPlanDetail(@RequestParam Integer couponType, @RequestParam String type,
			@PathVariable String orderId, HttpServletRequest request) {
		MyPlanDetailResultBean result = new MyPlanDetailResultBean();

		logger.info("request params: orderId is: {}, couponType is: {}", orderId, couponType);
		result.setAccedeOrderId(orderId);
		String sign = request.getParameter("sign");
		// 检查参数正确性
		if (Validator.isNull(sign) || Validator.isNull(couponType) || Validator.isNull(type)
				|| Validator.isNull(orderId)) {
			result.setStatus(BaseResultBeanFrontEnd.FAIL);
			result.setStatusDesc(ILLEGAL_PARAMETER_STATUS_DESC);
			return result;
		}
		Integer userId = null;
		try {
			userId = SecretUtil.getUserId(sign);
		} catch (Exception e) { // token失效
			result.setStatus(BaseResultBeanFrontEnd.FAIL);
			result.setStatusDesc(TOKEN_ISINVALID_STATUS);
			return result;
		}
		if (userId == null) {
			result.setStatus(BaseResultBeanFrontEnd.FAIL);
			result.setStatusDesc(TOKEN_ISINVALID_STATUS);
			return result;
		}

		// 构建查询条件
		Map<String, Object> params = new HashMap<>();
		params.put("accedeOrderId", orderId);
		params.put("userId", userId);
		List<TenderAgreement> tenderAgreementsNid= fddGenerateContractService.selectByExample(orderId+"");//居间协议
        if(tenderAgreementsNid!=null && tenderAgreementsNid.size()>0){
            TenderAgreement tenderAgreement = tenderAgreementsNid.get(0);
            Integer fddStatus = tenderAgreement.getStatus();
            //法大大协议生成状态：0:初始,1:成功,2:失败，3下载成功
            //System.out.println("构建查询条件******************1法大大协议状态："+tenderAgreement.getStatus());
            if(fddStatus.equals(3)){
                result.setFddStatus("1");
            }else {
                //隐藏下载按钮
                //System.out.println("构建查询条件******************2法大大协议状态：0");
                result.setFddStatus("0");
            }
        }else {
            //下载老版本协议
            //System.out.println("构建查询条件******************3法大大协议状态：2");
            result.setFddStatus("1");
        }
		// 真实资金出借
		if (couponType == 0) {
			AppMyHjhDetailCustomize customize = myPlanService.selectUserHjhInvistDetail(params);
			if (customize == null) {
				result.setStatus(BaseResultBeanFrontEnd.FAIL);
				result.setStatusDesc("加入订单号不正确......");
				return result;
			}
			// 1. 计划信息
			this.copyPlanBaseInfoToResult(result, customize, type);

			// 2.加入信息
			this.copyPlanCapitalInfoToResult(result, customize,type);
			// 3. 真实资金出借优惠券信息是空
			result.setCouponIntr(null);

			// 4.真实出借还款计划
			HjhRepay hjhRepay = myPlanService.getPlanRepayment(orderId);
			this.copyPlanRepaymentToResult(result, hjhRepay, customize);

			// 计划处于出借中状态
			List<String> statusList = Arrays.asList("0", "99");
			// 出借中状态不显示持有列表
			if (customize != null && !statusList.contains(customize.getOrderStatus())) {
				// 5. 持有项目列表
				this.copyPlanHoldInvestToResult(result, myPlanService.selectUserHjhInvistBorrowList(params));
			}

		} else { // 优惠券出借
			AppCouponCustomize appCouponCustomize = myPlanService.selectAppMyPlanCouponInfo(params);
			if (appCouponCustomize == null) {
				result.setStatus(BaseResultBeanFrontEnd.FAIL);
				result.setStatusDesc("优惠券加入订单号不正确......");
				return result;
			}
			// 1. 计划信息
			this.copyCouponPlanBaseToResult(result, appCouponCustomize, type);
			// 有本金出借才显示加入信息
			if (!StringUtils.isEmpty(appCouponCustomize.getRealTenderId())) {
				// 2.加入信息
				this.copyCouponPlanCapitalToResult(result, appCouponCustomize, type);
			} else {
				result.setInvestIntr(null);
			}
			// 3.优惠券信息
			this.copyPlanCouponInfoToResult(result, appCouponCustomize);

			// 4.优惠券出借还款计划
			List<CurrentHoldRepayMentPlanListCustomize> repaymentPlanList = myPlanService
					.getPlanCouponRepayment(orderId);
			this.copyPlanCouponRepaymentToResult(result, repaymentPlanList);

			// 5.优惠券出借不显示持有项目
		}
		return result;
	}

	/**
	 * 计划列表响应
	 *
	 * @param customizeProjectList
	 * @return
	 */
	private List<MyPlanListResultBean.ProjectList> convertAppMyPlanToReturnBean(
			List<AppMyPlanCustomize> customizeProjectList, HttpServletRequest request) {
		List<MyPlanListResultBean.ProjectList> projectList = new ArrayList<>();
		MyPlanListResultBean.ProjectList project;

		if (CollectionUtils.isEmpty(customizeProjectList)){
			return projectList;
		}

		for (AppMyPlanCustomize entity : customizeProjectList) {
			project = new MyPlanListResultBean.ProjectList();
			BeanUtils.copyProperties(entity, project);
			project.setBorrowTheFirst(DF_FOR_VIEW.format(new BigDecimal(entity.getAccedeAmount())));
			// mod by nxl 智投服务 修改加入金额->授权金额,锁定期限->回报期限 Start
//			project.setBorrowTheFirstDesc("加入金额");
			project.setBorrowTheFirstDesc("授权金额");
			project.setBorrowTheSecond(entity.getLockPeriod());
//			project.setBorrowTheSecondDesc("锁定期限");
			project.setBorrowTheSecondDesc("回报期限");
			// mod by nxl 智投服务 修改加入金额->授权金额,锁定期限->回报期限 End
			String label = entity.getLabel();
			// 优惠券样式用的
			project.setCouponType(label);
			switch (label) {
			case "1":
				project.setLabel("体验金");
				break;
			case "2":
				project.setLabel("加息券");
				break;
			case "3":
				project.setLabel("代金券");
				break;
			default:
				project.setLabel("");
			}

			if ("1".equals(project.getType())) {
				// mod by nxl 智投服务 修改加入时间-> 授权时间
//				project.setBorrowTheThirdDesc("加入时间");
				project.setBorrowTheThirdDesc("授权时间");
				project.setBorrowTheThird(entity.getCreateTime());
			} else {
		        // mod 汇计划二期前端优化 已退出的计划将回款时间改为退出时间  nxl 20180426 start
//				project.setBorrowTheThirdDesc("退出时间");
				// mod 汇计划二期前端优化 已退出的计划将回款时间改为退出时间  nxl 20180426 end
				// mod 智投服务 退出时间->回款时间  nxl  start
				project.setBorrowTheThirdDesc("回款时间");
				// mod 智投服务 退出时间->回款时间  nxl  end
				project.setBorrowTheThird(entity.getRecoverTime());
			}

			// 项目详情url
			String hostUrl = MyPlanDefine.MYPLAN_DETAIL_REQUEST + "/" + entity.getOrderId() + "?type="
					+ entity.getType() + "&couponType=" + label;
			// project.setBorrowUrl(CommonUtils.concatReturnUrl(request,
			// hostUrl));
			project.setBorrowUrl(hostUrl);
			projectList.add(project);
		}
		return projectList;
	}

	/**
	 * 构建查询参数map
	 * 
	 * @param request
	 * @return
	 */
	private Map<String, Object> buildQueryParameter(HttpServletRequest request) {
		Map<String, Object> params = new HashMap<>();
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
		params.put("limitStart", (page - 1) * pageSize);
		params.put("limitEnd", pageSize);
		return params;
	}

	/**
	 * 计划的持有项目列表
	 * 
	 * @param result
	 * @param userHjhInvistBorrowList
	 */
	private void copyPlanHoldInvestToResult(MyPlanDetailResultBean result,
			List<UserHjhInvistListCustomize> userHjhInvistBorrowList) {
		if (!CollectionUtils.isEmpty(userHjhInvistBorrowList)) {
			List<MyPlanDetailResultBean.BorrowComposition> projectIntrs = result.getBorrowComposition();
			MyPlanDetailResultBean.BorrowComposition borrow;
			for (UserHjhInvistListCustomize entity : userHjhInvistBorrowList) {
				borrow = new MyPlanDetailResultBean.BorrowComposition();
				borrow.setAccount(entity.getAccount());
				borrow.setBorrowNid(entity.getBorrowNid());
				borrow.setTenderTime(entity.getAddTime());
				borrow.setType(entity.getType());
				borrow.setNid(entity.getNid());
				projectIntrs.add(borrow);

			}
		}
	}

	/**
	 * 计划信息
	 * 
	 * @param result
	 * @param customize
	 * @param type
	 */
	private void copyPlanBaseInfoToResult(MyPlanDetailResultBean result, AppMyHjhDetailCustomize customize,
			String type) {
		MyPlanDetailResultBean.ProjectIntr projectIntr = result.getProjectIntr();
		
		// 计划处于出借中状态(orderStatus1锁定中 2退出中 3已退出)
        String orderStatus = customize.getOrderStatus();
        if(org.apache.commons.lang3.StringUtils.isNotBlank(orderStatus)){
			//非锁定中,app端隐藏智投服务协议按钮
			projectIntr.setType(orderStatus);
        }else{
            projectIntr.setType("0");
        }
        List<String> statusList = Arrays.asList("0", "2", "99", "9");
        // 出借中状态不显示持有列表
        if (customize != null && statusList.contains(customize.getOrderStatus())) {
            projectIntr.setStatus("出借中");
        } else if("7".equals(customize.getOrderStatus())){
            projectIntr.setStatus("已退出");
        } else{
            projectIntr.setStatus("未回款");
        }
        // mod by nxl 智投服务 修改锁定期显示 start 20180905
        // add 汇计划二期前端优化 修改锁定期的显示方式  nxl 20180426 start
       /* SimpleDateFormat smp = new SimpleDateFormat("yyyy-MM-dd");
		Date datePeriod = null;
		if (customize.getCountInterestTime().equals("--")) {
			customize.setPlanPeriod("— —");
		}else {
			try {
				Date dateAddTime = smp.parse(customize.getCountInterestTime());
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(dateAddTime);
				if (customize.getPlanPeriod().contains("天")) {
					String days = customize.getPlanPeriod().split("天")[0];
					int intD = Integer.parseInt(days);
					calendar.add(Calendar.DAY_OF_MONTH, +intD);
					datePeriod = calendar.getTime();
				}
				if (customize.getPlanPeriod().contains("个月")) {
					String days = customize.getPlanPeriod().split("个月")[0];
					int intD = Integer.parseInt(days);
					calendar.add(Calendar.MONTH, +intD);
					datePeriod = calendar.getTime();
				}
				if (datePeriod != null) {
					String endStrDate = smp.format(datePeriod);
					String startStrDate = customize.getAddTime().substring(0, 10);
					customize.setPlanPeriod(startStrDate + "~" + endStrDate);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
        customize.getPlanPeriod();
        projectIntr.setBorrowPeriod(customize.getPlanPeriod());
        */
        // add 汇计划二期前端优化 修改锁定期的显示方式  nxl 20180426 end
		// mod by nxl 智投服务 修改锁定期显示 end 20180905
		projectIntr.setBorrowApr(customize.getPlanApr());
		projectIntr.setBorrowPeriod(customize.getPlanPeriod());
		projectIntr.setBorrowPeriodUnit(CommonUtils.getPeriodUnitByRepayStyle(customize.getRepayStyle()));
		projectIntr.setRepayStyle(customize.getRepayMethod());
		projectIntr.setOnAccrual(PLAN_ON_ACCRUAL);
		projectIntr.setPlanName(customize.getPlanName());
	}

	/**
	 * 加入信息
	 * 
	 * @param result
	 * @param customize
	 * @param type 
	 */
	private void copyPlanCapitalInfoToResult(MyPlanDetailResultBean result, AppMyHjhDetailCustomize customize, String type) {
		MyPlanDetailResultBean.InvestIntr investIntr = result.getInvestIntr();
		investIntr.setAddDate(customize.getAddTime());
		investIntr.setCapital(DF_FOR_VIEW.format(new BigDecimal(customize.getAccedeAccount())));
		investIntr.setCapitalInterest(customize.getReceivedTotal());
		
		// 计划处于出借中状态
        List<String> statusList = Arrays.asList("0", "2", "99");
        // 出借中状态不显示持有列表
        if (customize != null && statusList.contains(customize.getOrderStatus())) {
            investIntr.setCapitalOnCall("--");
            investIntr.setInterestOnCall("--");
        }else{
            investIntr.setCapitalOnCall(DF_FOR_VIEW.format(new BigDecimal(customize.getWaitCaptical())));
            investIntr.setInterestOnCall(customize.getWaitInterest());
        }
	}

	/**
	 * 优惠券计划信息
	 * 
	 * @param result
	 * @param appCouponCustomize
	 * @param type
	 */
	private void copyCouponPlanBaseToResult(MyPlanDetailResultBean result, AppCouponCustomize appCouponCustomize,
			String type) {
		MyPlanDetailResultBean.ProjectIntr projectIntr = result.getProjectIntr();
		//projectIntr.setStatus(type);
		// 计划处于出借中状态
		List<String> statusList = Arrays.asList("0", "2", "99", "9");
		// 出借中状态不显示持有列表
		if (appCouponCustomize != null && statusList.contains(appCouponCustomize.getOrderStatus())) {
			projectIntr.setStatus("出借中");
		} else if("7".equals(appCouponCustomize.getOrderStatus())){
			projectIntr.setStatus("已退出");
		} else{
			projectIntr.setStatus("未回款");
		}
		projectIntr.setPlanName(appCouponCustomize.getPlanName());
		projectIntr.setBorrowApr(appCouponCustomize.getPlanApr());
		// add 汇计划二期前端优化 持有中计划详情修改锁定期 nxl 20180420 start
		// mod by nxl 智投服务 修改优惠券锁定期显示 start 20180906
		/*SimpleDateFormat smp = new SimpleDateFormat("yyyy-MM-dd");
		Date datePeriod = null;
		if (appCouponCustomize.getCountInterestTime().equals("--")) {
			appCouponCustomize.setPlanPeriod("— —");
		}else {
			Date dateAddTime;
			try {
				dateAddTime = smp.parse(appCouponCustomize.getCountInterestTime());
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(dateAddTime);
				if (appCouponCustomize.getPlanPeriod().contains("天")) {
					String days = appCouponCustomize.getPlanPeriod().split("天")[0];
					int intD = Integer.parseInt(days);
					calendar.add(Calendar.DAY_OF_MONTH, +intD);
					datePeriod = calendar.getTime();
				}
				if (appCouponCustomize.getPlanPeriod().contains("个月")) {
					String days = appCouponCustomize.getPlanPeriod().split("个月")[0];
					int intD = Integer.parseInt(days);
					calendar.add(Calendar.MONTH, +intD);
					datePeriod = calendar.getTime();
				}
				if (datePeriod != null) {
					String endStrDate = smp.format(datePeriod);
					String startStrDate = appCouponCustomize.getAddTime().substring(0, 10);
					appCouponCustomize.setPlanPeriod(startStrDate + "~" + endStrDate);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}*/
		// mod by nxl 智投服务 修改优惠券锁定期显示 end 20180906
		// add 汇计划二期前端优化 持有中计划详情修改锁定期 nxl 20180420 end
		projectIntr.setBorrowPeriod(appCouponCustomize.getPlanPeriod());
		projectIntr.setBorrowPeriodUnit(CommonUtils.getPeriodUnitByRepayStyle(appCouponCustomize.getRepayStyle()));
		projectIntr.setRepayStyle(appCouponCustomize.getRepayMethod());
		projectIntr.setOnAccrual(PLAN_ON_ACCRUAL);
	}

	/**
	 * 优惠券加入信息
	 * 
	 * @param result
	 * @param appCouponCustomize
	 * @param type 
	 */
	private void copyCouponPlanCapitalToResult(MyPlanDetailResultBean result, AppCouponCustomize appCouponCustomize, String type) {
		MyPlanDetailResultBean.InvestIntr investIntr = result.getInvestIntr();
		investIntr.setAddDate(appCouponCustomize.getAddTime());
		investIntr.setCapital(appCouponCustomize.getAccedeAccount());
		investIntr.setCapitalInterest(appCouponCustomize.getReceivedTotal());
		investIntr.setCapitalOnCall(appCouponCustomize.getWaitCaptical());
		investIntr.setInterestOnCall(appCouponCustomize.getWaitInterest());
	}

	/**
	 * 优惠券信息
	 * 
	 * @param result
	 * @param appCouponCustomize
	 */
	private void copyPlanCouponInfoToResult(MyPlanDetailResultBean result, AppCouponCustomize appCouponCustomize) {
		MyPlanDetailResultBean.CouponIntr couponIntr = result.getCouponIntr();
		if (Arrays.asList("1","2","3").contains(appCouponCustomize.getCouponType())){

			switch (appCouponCustomize.getCouponType()) {
				case "1":
					couponIntr.setCouponTypeCode(1);
					couponIntr.setCouponType("体验金");
					break;
				case "2":
					couponIntr.setCouponTypeCode(2);
					couponIntr.setCouponType("加息券");
					break;
				case "3":
					couponIntr.setCouponTypeCode(3);
					couponIntr.setCouponType("代金券");
					break;
				default:
					logger.error("coupon type is error");
					break;
			}

		}else if (Validator.isNumber(appCouponCustomize.getCouponType())
				&& !Arrays.asList("0","1","2","3").contains(appCouponCustomize.getCouponType())){
			couponIntr.setCouponTypeCode(Integer.parseInt(appCouponCustomize.getCouponType()));
			couponIntr.setCouponType("体验金");
		}


		couponIntr.setCouponAmount(appCouponCustomize.getCouponAmount());
		couponIntr.setInterestOnCall(appCouponCustomize.getRecoverAccountInterestWait());
		couponIntr.setCapitalOnCall(appCouponCustomize.getRecoverAccountCapitalWait());
		couponIntr.setCapitalInterestOnCall(appCouponCustomize.getRecoverAccountWait());
	}

	/**
	 * 本金出借还款计划 目前只有一条 repay表存的是已回款金额，回款总额应从accede中取
	 * 
	 * @param result
	 * @param hjhRepay
	 */
	private void copyPlanRepaymentToResult(MyPlanDetailResultBean result, HjhRepay hjhRepay, AppMyHjhDetailCustomize customize) {
		if (hjhRepay != null) {
			List<MyPlanDetailResultBean.RepayPlan> repayPlans = result.getRepayPlan();
			MyPlanDetailResultBean.RepayPlan repayPlan = new MyPlanDetailResultBean.RepayPlan();
			repayPlan.setTime(customize.getLastPaymentTime());

			repayPlan.setAccount(DF_FOR_VIEW.format(new BigDecimal(customize.getWaitTotal()).add(new BigDecimal(customize.getReceivedTotal()))));
			// 目前只有一期
			repayPlan.setNumber("1");
			// 0 -未还款 1- 部分还款 2- 已还款
			if (hjhRepay.getRepayStatus() == 2) {
				repayPlan.setStatus("已回款");
			} else {
				repayPlan.setStatus("未回款");
			}

			repayPlans.add(repayPlan);
		}
	}

	/**
	 * 优惠券还款计划
	 * 
	 * @param result
	 * @param repaymentPlanList
	 */
	private void copyPlanCouponRepaymentToResult(MyPlanDetailResultBean result,
			List<CurrentHoldRepayMentPlanListCustomize> repaymentPlanList) {
		if (!CollectionUtils.isEmpty(repaymentPlanList)) {
			List<MyPlanDetailResultBean.RepayPlan> repayPlans = result.getRepayPlan();
			MyPlanDetailResultBean.RepayPlan repayPlan;
			for (CurrentHoldRepayMentPlanListCustomize entity : repaymentPlanList) {
				repayPlan = new MyPlanDetailResultBean.RepayPlan();
				repayPlan.setTime(entity.getRecoverTime());
				repayPlan.setAccount(entity.getRecoverAccountWait());
				repayPlan.setNumber(entity.getRecoverPeriod());
				repayPlan.setStatus(entity.getRecoveStatus());
				repayPlans.add(repayPlan);
			}
		}
	}
}
