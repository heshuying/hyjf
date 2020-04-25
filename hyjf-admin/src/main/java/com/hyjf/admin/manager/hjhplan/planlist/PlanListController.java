package com.hyjf.admin.manager.hjhplan.planlist;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.bank.service.mq.MqService;
import com.hyjf.common.util.*;
import com.hyjf.mybatis.model.auto.ParamName;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.hyjf.admin.BaseController;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.customize.PlanListCommonCustomize;
import com.mysql.fabric.xmlrpc.base.Param;

/**
 * 
 * @author: LIBIN
 * @description:汇计划所用控制器
 * @version: 1
 * @date: 2017年8月11日
 */
@Controller
@RequestMapping(value = PlanListDefine.REQUEST_MAPPING)
public class PlanListController extends BaseController {
	Logger _log = LoggerFactory.getLogger(PlanListController.class);

	@Autowired
	private MqService mqService;

	@Autowired
	private PlanListService planListService;
	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanListDefine.INIT)
	@RequiresPermissions(PlanListDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute("PlanListBean") PlanListBean form) {//原为"PlanBean"
		LogUtil.startLog(PlanListController.class.toString(), PlanListDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(PlanListDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(PlanListController.class.toString(), PlanListDefine.INIT);
		return modelAndView;
	}
	
	/**
	 * 画面检索面板-检索按键画面请求
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanListDefine.SEARCH_ACTION)
	@RequiresPermissions(PlanListDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, PlanListBean form) {//原为"PlanBean"
		LogUtil.startLog(PlanListController.class.toString(), PlanListDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanListDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(PlanListController.class.toString(), PlanListDefine.SEARCH_ACTION);
		return modelAndView;
	}
	
	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, PlanListBean form) {
	
		PlanListCommonCustomize planListCommonCustomize = new PlanListCommonCustomize();
		// 取查询计划编码
		planListCommonCustomize.setPlanNidSrch(form.getPlanNidSrch());
		// 取查询计划名称
		planListCommonCustomize.setPlanNameSrch(form.getPlanNameSrch());
		//取锁定期
		planListCommonCustomize.setLockPeriodSrch(form.getLockPeriodSrch());
		// 取出借状态 ： 0 全部；1 启用；2 关闭；
		planListCommonCustomize.setPlanStatusSrch(form.getPlanStatusSrch());
		// 取显示状态 ： 0 全部；1 显示；2 隐藏；
		planListCommonCustomize.setPlanDisplayStatusSrch(form.getPlanDisplayStatusSrch());
		//取添加时间的开始时间
		planListCommonCustomize.setAddTimeStart(StringUtils.isNotBlank(form.getAddTimeStart())?form.getAddTimeStart():null);
		//取添加时间的结束时间
		planListCommonCustomize.setAddTimeEnd(StringUtils.isNotBlank(form.getAddTimeEnd())?form.getAddTimeEnd():null);
		// 取排序
		planListCommonCustomize.setSort(form.getSort());
		// 取排序列
		planListCommonCustomize.setCol(form.getCol());
		
		// 还款方式 汇计划三期新增
		planListCommonCustomize.setBorrowStyleSrch(form.getBorrowStyleSrch());
		
		// 查询总条数
		int count = planListService.countPlan(planListCommonCustomize);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			planListCommonCustomize.setLimitStart(paginator.getOffset());
			planListCommonCustomize.setLimitEnd(paginator.getLimit());
			List<HjhPlan> recordList = planListService.selectPlanList(planListCommonCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
			
			/*----待还总额累计 add by LSY START--------*/
			String sumHjhPlan = planListService.sumHjhPlan(planListCommonCustomize);
			modelAndView.addObject("sumHjhPlan", sumHjhPlan);
			/*----add by LSY END--------*/
			
			
			/*----开放额度累计 add by LIBIN START--------*/
			String sumOpenAccount = planListService.sumOpenAccount(planListCommonCustomize);
			modelAndView.addObject("sumOpenAccount", sumOpenAccount);
			/*----add by LIBIN END--------*/
			
			/*----累计加入金额累计 add by LIBIN START--------*/
			String sumJoinTotal = planListService.sumJoinTotal(planListCommonCustomize);
			modelAndView.addObject("sumJoinTotal", sumJoinTotal);
			/*----add by LIBIN END--------*/
			
		}
		modelAndView.addObject(PlanListDefine.PLAN_LIST_FORM, form);
	}
	
	/**
	 * 点击添加迁按钮-移到汇计划详情画面
	 * 
	 * 
	 * 
	 * @Title moveToInfoAction
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = PlanListDefine.ADD_PLAN_ACTION)
	@RequiresPermissions(value = PlanListDefine.PERMISSIONS_ADD)
	public ModelAndView moveToInfoAction(HttpServletRequest request, PlanListBean form) {
		LogUtil.startLog(PlanListController.class.toString(), PlanListDefine.ADD_PLAN_ACTION);
		//展示出的添加详情画面路径
		ModelAndView modelAndView = new ModelAndView(PlanListDefine.INFO_PATH);
		//获取还款方式
		List<BorrowStyle> borrowStyleList = this.planListService.getBorrowStyleList();
		modelAndView.addObject("borrowStyleList", borrowStyleList);

		// 风险测评投资等级
		List<ParamName> investLevelList = this.planListService.getParamNameList(CustomConstants.INVEST_LEVEL);
		modelAndView.addObject("investLevelList",investLevelList);
		form.setInvestLevel("稳健型");
		//初始化 webhost 用来在详情画面上传图片
		String webhost = UploadFileUtils.getDoPath(PropUtils.getSystem("hyjf.web.host"));
		webhost = webhost.substring(0, webhost.length() - 1);
		modelAndView.addObject("webhost", webhost);
		// (6)计划编号(注意：当发起 添加 请求时，计划编号为空，当发起 修改 请求时，计划编号需要判空)
		String debtPlanNid = form.getDebtPlanNid();
		// 发起修改请求时才经过以下判断
		if (StringUtils.isNotEmpty(debtPlanNid)) {
			// 计划编号是否存在
			boolean isExistRecord = this.planListService.isExistsRecord(debtPlanNid);
			if (isExistRecord) {
				// 获取计划数据(修改请求时的数据源)
				this.planListService.getPlanInfo(form);
			}
		}


		if (StringUtils.isNotBlank(debtPlanNid) && debtPlanNid.length() < 3
				&& !"HJH".equals(debtPlanNid.substring(0, 3))) {
			modelAndView.addObject("error", "计划编号必须以HJH开头");
		}
		modelAndView.addObject(PlanListDefine.PLAN_INFO_FORM, form);//原planForm
		LogUtil.endLog(PlanListController.class.toString(), PlanListDefine.ADD_PLAN_ACTION);
		return modelAndView;
	}
	
	/**
	 * 计划名称是否已经存在
	 * 
	 * @Title isExistsApplicant
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = PlanListDefine.ISDEBTPLANNAMEEXIST_ACTION, method = RequestMethod.POST)
	public String isDebtPlanNameExist(HttpServletRequest request) {
		LogUtil.startLog(PlanListController.class.toString(), PlanListDefine.ISDEBTPLANNAMEEXIST_ACTION);
		String message = this.planListService.isDebtPlanNameExist(request);
		LogUtil.endLog(PlanListController.class.toString(), PlanListDefine.ISDEBTPLANNAMEEXIST_ACTION);
		return message;
	}
	
	/**
	 * 汇计划添加信息(添加更改共用)
	 * 
	 * @Title insertAction
	 * @param request
	 * @param attr
	 * @param form
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(PlanListDefine.INSERT_ACTION)
	@RequiresPermissions(PlanListDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, RedirectAttributes attr, PlanListBean form) throws Exception {
		// 若报错回显到详情画面
		ModelAndView modelAndView = new ModelAndView(PlanListDefine.INFO_PATH);
		// 计划编码
		String debtPlanNid = form.getDebtPlanNid();
		// 可用券配置(如果前台配置为空，优惠券设置为0)
		if(StringUtils.isEmpty(form.getCouponConfig())){
		    form.setCouponConfig("0");
		}
		// 计划编号是否存在( true：存在, false：不存在)
		boolean isExistRecord = StringUtils.isNotEmpty(debtPlanNid) && this.planListService.isExistsRecord(debtPlanNid);
		// 画面校验
		this.planListService.validatorFieldCheck(modelAndView, form, isExistRecord);
		// 如果画面校验出错
		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			// 还款方式
			List<BorrowStyle> borrowStyleList = this.planListService.getBorrowStyleList();
			this.createPage(request, modelAndView, form);
			// 风险测评投资等级
			List<ParamName> investLevelList = this.planListService.getParamNameList(CustomConstants.INVEST_LEVEL);
			modelAndView.addObject("investLevelList",investLevelList);
			modelAndView.addObject("borrowStyleList", borrowStyleList);
			modelAndView.addObject(PlanListDefine.PLAN_INFO_FORM, form);
			return modelAndView;
		}
		//汇计划二期迭代最高可投金额为空设置默认值为1250000
		if(StringUtils.isEmpty(form.getDebtMaxInvestment())){
			form.setDebtMaxInvestment("1250000");
		}
		if (isExistRecord) {
			// (修改请求)更新操作
			this.planListService.updateRecord(form);
		} else {
			// (新增请求)插入操作
			this.planListService.insertRecord(form);

			// add 合规数据上报 埋点 liubin 20181122 start
			// 推送数据到MQ 新增智投完成后
			Map<String, String> params = new HashMap<String, String>();
			params.put("planNid", form.getDebtPlanNid());
			params.put("borrowNid", null);
			this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.HJHPLAN_ADD_DELAY_KEY, JSONObject.toJSONString(params));
			// add 合规数据上报 埋点 liubin 20181122 end

		}
		modelAndView = new ModelAndView("redirect:/manager/hjhplan/planlist/init");
		return modelAndView;
	}
	
	/**
	 * 点击启用/关闭按键
	 * 
	 * @Title moveToInfoAction
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = PlanListDefine.SWIITCH_ACTION)
	@RequiresPermissions(value = PlanListDefine.PERMISSIONS_MODIFY)
	public  ModelAndView switchAction(HttpServletRequest request, RedirectAttributes attr, PlanListBean form) throws Exception {
		ModelAndView modelAndView = new ModelAndView(PlanListDefine.LIST_INFO_PATH);
		// 计划编码
		String debtPlanNid = form.getDebtPlanNid();
		// 修改状态
		if (StringUtils.isNotEmpty(debtPlanNid)) {
			this.planListService.updatePlanRecord(form);
		}
		return modelAndView;
	}
	/**
	 * 点击显示/隐藏按键
	 * 
	 * @Title moveToInfoAction
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = PlanListDefine.DISPLAY_ACTION)
	@ResponseBody
	public JSONObject displayAction(HttpServletRequest request, PlanListBean form) throws Exception {
		JSONObject jst = new JSONObject();
		// 计划编码
		String parameter = request.getParameter("debtPlanNid");
		// 修改状态
		if (StringUtils.isNotEmpty(parameter)) {
			this.planListService.updatePlanDisplayRecord(form);
		}
		jst.put("status",1);
		return jst;
	}
	/**
	 * 选择状态
	 *
	 * @Title moveToInfoAction
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = PlanListDefine.CHECK_STATUS)
	@ResponseBody
	public JSONObject checkStatus(HttpServletRequest request, PlanListBean form) throws Exception {
		JSONObject ret = new JSONObject();
		String debtPlanNid = form.getDebtPlanNid();
		String debtPlanStatus = form.getDebtPlanStatus();
		String addTime = form.getAddTime();
		if (StringUtils.isNotEmpty(debtPlanNid)) {
			PlanListBean planInfo = this.planListService.getPlanInfo(form);
			String debtPlanStatus2 = planInfo.getDebtPlanStatus();
			if(debtPlanStatus.equals(debtPlanStatus2)) {
				ret.put("status", 1);
			}else {
				ret.put("status", 0);
			}
		}
		return ret;
	}

	/**
	 * 校验智投显示隐藏状态
	 * @param request
	 * @param form
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = PlanListDefine.CHECK_DISPLAY_STATUS)
	@ResponseBody
	public JSONObject checkDisplayStatus(HttpServletRequest request, PlanListBean form) throws Exception {
		JSONObject ret = new JSONObject();
		String debtPlanNid = form.getDebtPlanNid();
		String planDisplayStatus = form.getPlanDisplayStatusSrch();
		if (StringUtils.isNotEmpty(debtPlanNid)) {
			PlanListBean planInfo = this.planListService.getPlanInfo(form);
			String planDisplayStatus2 = planInfo.getPlanDisplayStatusSrch();
			if(planDisplayStatus.equals(planDisplayStatus2)) {
				ret.put("status", 1);
			}else {
				ret.put("status", 0);
			}
		}
		return ret;
	}


	/**
	 * 点击显示/隐藏按键跳转到详情页面
	 * 
	 * @Title moveToInfoAction
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = PlanListDefine.DISPLAY_SHOW_ACTION)
	@RequiresPermissions(value = PlanListDefine.PERMISSIONS_MODIFY)
	public ModelAndView displayShowAction(HttpServletRequest request, PlanListBean form) throws Exception {
		ModelAndView modelAndView = new ModelAndView(PlanListDefine.INFO_SHOW_PATH);
		String debtPlanNid = request.getParameter("debtPlanNid");
		form.setDebtPlanNid(debtPlanNid);
		String addTime = form.getAddTime();
		String dateStr = "";
		//时间显示转成去掉秒的
		if (StringUtils.isNoneEmpty(addTime)) {	
			long time= Long.parseLong(addTime);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date date = format.parse(format.format(time*1000));
			dateStr = format2.format(date);
		}
		// 修改状态
		if (StringUtils.isNotEmpty(debtPlanNid)) {
			PlanListBean planInfo = this.planListService.getPlanInfo(form);
			planInfo.setAddTime(dateStr);
			modelAndView.addObject("planInfo",planInfo);
		}
		modelAndView.addObject("enableOrDisplayFlag",form.getEnableOrDisplayFlag());

		return modelAndView;
	}
	/**
	 * 计划编号是否已经存在
	 * 
	 * @Title isExists
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = PlanListDefine.ISDEBTPLANNIDEXIST_ACTION, method = RequestMethod.POST)
	public String isDebtPlanNidExist(HttpServletRequest request) {
		LogUtil.startLog(PlanListController.class.toString(), PlanListDefine.ISDEBTPLANNIDEXIST_ACTION);
		String message = this.planListService.isDebtPlanNidExist(request);
		LogUtil.endLog(PlanListController.class.toString(), PlanListDefine.ISDEBTPLANNIDEXIST_ACTION);
		return message;
	}

	/**
	 * 导出功能
	 * @param request
	 * @param response
	 * @param form
	 * @throws Exception
	 */
	@RequestMapping(PlanListDefine.EXPORTEXECL)
	@RequiresPermissions(PlanListDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, PlanListBean form) throws Exception {
		LogUtil.startLog(PlanListController.class.toString(), PlanListDefine.PERMISSIONS_EXPORT);
		// 表格sheet名称
		String sheetName = "智投管理";
		
		PlanListCommonCustomize planListCommonCustomize = new PlanListCommonCustomize();
		// 取查询计划编码
		planListCommonCustomize.setPlanNidSrch(form.getPlanNidSrch());
		// 取查询计划名称
		planListCommonCustomize.setPlanNameSrch(form.getPlanNameSrch());
		//取锁定期
		planListCommonCustomize.setLockPeriodSrch(form.getLockPeriodSrch());
		// 取出借状态 ： 0 全部；1 启用；2 关闭；
		planListCommonCustomize.setPlanStatusSrch(form.getPlanStatusSrch());
		// 取显示状态 ： 0 全部；1 显示；2 隐藏；
		planListCommonCustomize.setPlanDisplayStatusSrch(form.getPlanDisplayStatusSrch());
		//取添加时间的开始时间
		planListCommonCustomize.setAddTimeStart(StringUtils.isNotBlank(form.getAddTimeStart())?form.getAddTimeStart():null);
		//取添加时间的结束时间
		planListCommonCustomize.setAddTimeEnd(StringUtils.isNotBlank(form.getAddTimeEnd())?form.getAddTimeEnd():null);
		// 取排序
		planListCommonCustomize.setSort(form.getSort());
		// 取排序列
		planListCommonCustomize.setCol(form.getCol());
		
		// 还款方式 汇计划三期新增
		planListCommonCustomize.setBorrowStyleSrch(form.getBorrowStyleSrch());
		List<HjhPlan> resultList = planListService.selectPlanList(planListCommonCustomize);
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
		String[] titles = new String[] { "序号","智投编号","智投名称", "还款方式","服务回报期限", "参考年回报率","最低授权服务金额（元）","最高授权服务金额（元）","递增金额（元）", "最小投标笔数", "开放额度（元）", "累计授权服务金额（元）","待还总额（元）","智投状态","添加时间" };
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");

		if (resultList != null && resultList.size() > 0) {
			int sheetCount = 1;
			int rowNum = 0;
			for (int i = 0; i < resultList.size(); i++) {
				rowNum++;
				if (i != 0 && i % 60000 == 0) {
					sheetCount++;
					sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, (sheetName + "_第" + sheetCount + "页"));
					rowNum = 1;
				}
				// 新建一行
				Row row = sheet.createRow(rowNum);
				// 循环数据
				for (int celLength = 0; celLength < titles.length; celLength++) {
					HjhPlan hjhPlan = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 计划编号
					else if (celLength == 1) {
						cell.setCellValue(StringUtils.isEmpty(hjhPlan.getPlanNid()) ? StringUtils.EMPTY : hjhPlan.getPlanNid());
					}
					// 计划名称
					else if (celLength == 2) {
						cell.setCellValue(StringUtils.isEmpty(hjhPlan.getPlanName()) ? StringUtils.EMPTY : hjhPlan.getPlanName());
					}
					// 还款方式
					else if (celLength == 3) {
						if ("endday".equals(hjhPlan.getBorrowStyle())) {
							cell.setCellValue("按天计息，到期还本还息");
						} else if ("end".equals(hjhPlan.getBorrowStyle())) {
							cell.setCellValue("按月计息，到期还本还息");
						} else {
							cell.setCellValue(hjhPlan.getBorrowStyle());
						}
					}
					// 锁定期
					else if (celLength == 4) {
						if (hjhPlan.getIsMonth() == 0) {
							cell.setCellValue(hjhPlan.getLockPeriod()+ "天");
						} else if (hjhPlan.getIsMonth() == 1) {
							cell.setCellValue(hjhPlan.getLockPeriod()+ "个月");
						}
					}
					// 预期出借利率率
					else if (celLength == 5) {
						cell.setCellValue( hjhPlan.getExpectApr() + "%");
					}
					
					// 最低加入金额（元）
					else if (celLength == 6) {
						if(hjhPlan.getMinInvestment() != null){
							cell.setCellValue(hjhPlan.getMinInvestment().toString());
						}else{
							cell.setCellValue("0.00");
						}
					}
					// 最高加入金额（元）
					else if (celLength == 7) {
						if(hjhPlan.getMaxInvestment() != null){
							cell.setCellValue(hjhPlan.getMaxInvestment().toString());
						}else{
							cell.setCellValue("0.00");
						}
					}
					// 出借增量（元）
					else if (celLength == 8) {
						if(hjhPlan.getInvestmentIncrement() != null){
							cell.setCellValue(hjhPlan.getInvestmentIncrement().toString());
						}else{
							cell.setCellValue("0.00");
						}
					}
					// 最小出借笔数
					else if (celLength == 9) {
						if(hjhPlan.getMinInvestCounts() != null){
							cell.setCellValue(hjhPlan.getMinInvestCounts().toString());
						}else{
							cell.setCellValue("0.00");
						}
					}
					// 开放额度（元）
					else if (celLength == 10) {
						if(hjhPlan.getAvailableInvestAccount() != null){
							cell.setCellValue(hjhPlan.getAvailableInvestAccount().toString());
						}else{
							cell.setCellValue("0.00");
						}
					}
					// 累计加入金额（元）
					else if (celLength == 11) {
						if(hjhPlan.getJoinTotal() != null){
							cell.setCellValue(hjhPlan.getJoinTotal().toString());
						}else{
							cell.setCellValue("0.00");
						}
					}
					// 待还总额（元）
					else if (celLength == 12) {
						if(hjhPlan.getRepayWaitAll() != null){
							cell.setCellValue(hjhPlan.getRepayWaitAll().toString());
						}else{
							cell.setCellValue("0.00");
						}
					}
					// 计划状态
					else if (celLength == 13) {
						if (hjhPlan.getPlanInvestStatus() == 1) {
							cell.setCellValue("启用");
						} else if (hjhPlan.getPlanInvestStatus() == 2) {
							cell.setCellValue("关闭");
						}
					}
					// 添加时间
					else if (celLength == 14) {
						if(hjhPlan.getAddTime()!= null){                        
							cell.setCellValue(GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(hjhPlan.getAddTime()));
						}
					}	
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(PlanListController.class.toString(), PlanListDefine.PERMISSIONS_EXPORT);
	}
	
	/**
	 * 运营记录画面
	 * @Title optRecordAction
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = PlanListDefine.OPT_RECORD_ACTION)
	@RequiresPermissions(value = PlanListDefine.PERMISSIONS_VIEW)
	public ModelAndView optRecordAction(HttpServletRequest request, PlanListBean form) {
		LogUtil.startLog(PlanListController.class.toString(), PlanListDefine.OPT_RECORD_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanListDefine.OPT_LOAD_LIST_PATH);
		// 前画面传递计划编号
		String PlanNid = form.getDebtPlanNid();
		/*this.createPage(request, modelAndView, form);  在此方法继续实现运营记录 TODO*/
		LogUtil.endLog(PlanListController.class.toString(), PlanListDefine.SEARCH_ACTION);
		return modelAndView;
	}
}
