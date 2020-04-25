package com.hyjf.admin.exception.autotenderexception;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.borrow.borrowinvest.BorrowInvestService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.customize.BorrowInvestCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminPlanAccedeListCustomize;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 汇计划出借异常处理Controller
 * 
 * @ClassName AccedeListController
 * @author LIBIN
 * @date 2017年8月16日 上午9:45:20
 */
@Controller
@RequestMapping(value = AutoTenderExceptionDefine.REQUEST_MAPPING)
public class AutoTenderExceptionController extends BaseController {
	
	@Autowired
	private AutoTenderExceptionService accedeListService;
	@Autowired
	private BorrowInvestService borrowInvestService;
	//类名 
	private static final String THIS_CLASS = AutoTenderExceptionController.class.toString();
	
	Logger _log = LoggerFactory.getLogger(AutoTenderExceptionController.class);


	/**
	 * 画面初期化
	 * 
	 * @Title init
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AutoTenderExceptionDefine.INIT)
	@RequiresPermissions(AutoTenderExceptionDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, AutoTenderExceptionBean form) {
		LogUtil.startLog(THIS_CLASS, AutoTenderExceptionDefine.INIT);
		ModelAndView modeAndView = new ModelAndView(AutoTenderExceptionDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(THIS_CLASS, AutoTenderExceptionDefine.INIT);
		return modeAndView;
	}
	
	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, AutoTenderExceptionBean form) {
		int count = accedeListService.countAccedeRecord(form);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			form.setLimitStart(paginator.getOffset());
			form.setLimitEnd(paginator.getLimit());
			// 汇计划加入明细列表
			List<AdminPlanAccedeListCustomize> recordList = this.accedeListService.selectAccedeRecordList(form);
			form.setPaginator(paginator);
			form.setRecordList(recordList);
		}
		modelAndView.addObject(AutoTenderExceptionDefine.JOINDETAIL_FORM, form);
	}
	
	/**
	 * 检索Action
	 * 
	 * @Title searchAction
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AutoTenderExceptionDefine.SEARCH_ACTION)
	@RequiresPermissions(AutoTenderExceptionDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request, AutoTenderExceptionBean form) {
		LogUtil.startLog(THIS_CLASS, AutoTenderExceptionDefine.SEARCH_ACTION);
		ModelAndView modeAndView = new ModelAndView(AutoTenderExceptionDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(THIS_CLASS, AutoTenderExceptionDefine.SEARCH_ACTION);
		return modeAndView;
	}
	
	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(AutoTenderExceptionDefine.EXPORTEXECL)
	@RequiresPermissions(AutoTenderExceptionDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, AutoTenderExceptionBean form) throws Exception {
		LogUtil.startLog(THIS_CLASS, AutoTenderExceptionDefine.EXPORTEXECL);
		// 表格sheet名称
		String sheetName = "加入明细";
		List<AdminPlanAccedeListCustomize> resultList = this.accedeListService.selectAccedeRecordList(form);
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
		String[] titles = new String[] { "序号","智投订单号", "智投编号","服务回报期限", "用户名", "授权服务金额", "已出借金额(元)","待还总额(元) ","待还本金(元) ","待还利息(元) ","操作平台","订单状态",  "开始计息时间", "授权服务时间" };
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
					AdminPlanAccedeListCustomize planAccedeDetail = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 智投订单号
					else if (celLength == 1) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getPlanOrderId()) ? StringUtils.EMPTY : planAccedeDetail.getPlanOrderId());
					}
					// 智投编号
					else if (celLength == 2) {
						cell.setCellValue(planAccedeDetail.getDebtPlanNid());
					}
					// 服务回报期限
					else if (celLength == 3) {
						String dateUnit="";
						if (planAccedeDetail.getIsMonth()==0){
							dateUnit="天";
						}else if(planAccedeDetail.getIsMonth()==1){
							dateUnit="个月";
						}
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getDebtLockPeriod()) ? StringUtils.EMPTY : planAccedeDetail.getDebtLockPeriod() + dateUnit);

					}
					// 用户名
					else if (celLength == 4) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getUserName()) ? StringUtils.EMPTY : planAccedeDetail.getUserName());
					}
					// 授权服务金额
					else if (celLength == 5) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getAccedeAccount()) ? StringUtils.EMPTY : planAccedeDetail.getAccedeAccount());
					}
					// 已出借金额
					else if (celLength == 6) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getAlreadyInvest()) ? StringUtils.EMPTY : planAccedeDetail.getAlreadyInvest());
					}
					// 待收总额
					else if (celLength == 7) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getWaitTotal()) ? StringUtils.EMPTY : planAccedeDetail.getWaitTotal());
					}
					// 待收本金
					else if (celLength == 8) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getWaitCaptical()) ? StringUtils.EMPTY : planAccedeDetail.getWaitCaptical());
					}
					// 待还利息
					else if (celLength == 9) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getWaitInterest()) ? StringUtils.EMPTY : planAccedeDetail.getWaitInterest());
					}

					// 操作平台
					else if (celLength == 10) {
						if ("0".equals(planAccedeDetail.getPlatform())) {
							cell.setCellValue("PC");
						} else if ("1".equals(planAccedeDetail.getPlatform())) {
							cell.setCellValue("微官网");
						} else if ("2".equals(planAccedeDetail.getPlatform())) {
							cell.setCellValue("android");
						} else if ("3".equals(planAccedeDetail.getPlatform())) {
							cell.setCellValue("ios");
						}
					}
					// 订单状态
					else if (celLength == 11) {
						if (0 == Integer.parseInt(planAccedeDetail.getOrderStatus())) {
							cell.setCellValue("自动投标中");
						} else if (2 == Integer.parseInt(planAccedeDetail.getOrderStatus())) {
							cell.setCellValue("自动投标成功");
						} else if (3 == Integer.parseInt(planAccedeDetail.getOrderStatus())) {
							cell.setCellValue("锁定中");
						} else if (5 == Integer.parseInt(planAccedeDetail.getOrderStatus())) {
							cell.setCellValue("退出中");
						} else if (7 == Integer.parseInt(planAccedeDetail.getOrderStatus())) {
							cell.setCellValue("已退出");
						} else if (99 == Integer.parseInt(planAccedeDetail.getOrderStatus())) {
							cell.setCellValue("自动出借异常");
						} else{
                            cell.setCellValue(planAccedeDetail.getOrderStatus());
                        }
					}
					// 开始计息时间
					else if (celLength == 12) {
						if (StringUtils.isNotEmpty(planAccedeDetail.getCountInterestTime())) {
							cell.setCellValue(planAccedeDetail.getCountInterestTime());
						}
					}
					// 授权服务时间
					else if (celLength == 13) {
						if (StringUtils.isNotEmpty(planAccedeDetail.getCreateTime())) {
							cell.setCellValue(planAccedeDetail.getCreateTime());
						}
					}
					// 预期年化
					else if (celLength == 14) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getExpectApr()) ? StringUtils.EMPTY : planAccedeDetail.getExpectApr() + "%");
					}
					// 用户属性（当前）
					else if (celLength == 15) {
						if ("0".equals(planAccedeDetail.getUserAttribute())) {
							cell.setCellValue("无主单");
						} else if ("1".equals(planAccedeDetail.getUserAttribute())) {
							cell.setCellValue("有主单");
						} else if ("2".equals(planAccedeDetail.getUserAttribute())) {
							cell.setCellValue("线下员工");
						} else if ("3".equals(planAccedeDetail.getUserAttribute())) {
							cell.setCellValue("线上员工");
						}
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(THIS_CLASS, AutoTenderExceptionDefine.EXPORTEXECL);
	}
	
	/**
	 * 跳转到协议发送入力页面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = AutoTenderExceptionDefine.TOEXPORT_AGREEMENT_ACTION)
	@RequiresPermissions(AutoTenderExceptionDefine.PERMISSIONS_EXPORT)
	public ModelAndView toExportAgreementAction(HttpServletRequest request, AutoTenderExceptionBean form) {
		LogUtil.startLog(THIS_CLASS, AutoTenderExceptionDefine.TOEXPORT_AGREEMENT_ACTION);
		ModelAndView modelAndView = new ModelAndView(AutoTenderExceptionDefine.EXPORT_AGREEMENT_PATH);//输入Email的画面
		String userid = request.getParameter("userid");
		String planOrderId = request.getParameter("planOrderId");
		String debtPlanNid = request.getParameter("debtPlanNid");
		modelAndView.addObject("userid", userid);
		modelAndView.addObject("planOrderId", planOrderId);
		modelAndView.addObject("debtPlanNid", debtPlanNid);
		LogUtil.endLog(THIS_CLASS, AutoTenderExceptionDefine.TOEXPORT_AGREEMENT_ACTION);
		return modelAndView;
	}
	
	/**
	 * 异常处理 请求
	 * 
	 * @param request
	 * @return
	 */
	@RequiresPermissions(AutoTenderExceptionDefine.PERMISSIONS_AUTOTENDEREXCEPTION)
	@ResponseBody
	@RequestMapping(value = AutoTenderExceptionDefine.TENDER_EXCEPTION_ACTION, produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	public String tenderExceptionAction(HttpServletRequest request) {
		
		JSONObject ret = new JSONObject();
		String userid = request.getParameter("userid");
		String planOrderId = request.getParameter("planOrderId");
		String debtPlanNid = request.getParameter("debtPlanNid");
		
		if (StringUtils.isEmpty(planOrderId) || StringUtils.isEmpty(debtPlanNid)) {
			ret.put(AutoTenderExceptionDefine.JSON_STATUS_KEY, AutoTenderExceptionDefine.JSON_STATUS_NG);
			ret.put(AutoTenderExceptionDefine.JSON_RESULT_KEY, "传递参数不正确");
			return ret.toString();
		}
		String msg = accedeListService.tenderExceptionAction(userid, planOrderId, debtPlanNid);
		if (msg == null) {
			ret.put(AutoTenderExceptionDefine.JSON_RESULT_KEY, "操作完成");
			ret.put(AutoTenderExceptionDefine.JSON_STATUS_KEY, AutoTenderExceptionDefine.JSON_STATUS_OK);
		} else {
			ret.put(AutoTenderExceptionDefine.JSON_STATUS_KEY, AutoTenderExceptionDefine.JSON_STATUS_NG);
			ret.put(AutoTenderExceptionDefine.JSON_RESULT_KEY, msg);
		}
		
		return ret.toString();
	}
	
	/**
	 * 出借明细画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AutoTenderExceptionDefine.TENDER_INFO)
	@RequiresPermissions(AutoTenderExceptionDefine.PERMISSIONS_VIEW)
	public ModelAndView tenderInit(HttpServletRequest request, AutoTenderExceptionBean form) {
		LogUtil.startLog(THIS_CLASS, AutoTenderExceptionDefine.TENDER_INFO);
		ModelAndView modelAndView = new ModelAndView(AutoTenderExceptionDefine.TENDER_INFO_LIST_PATH);
		// 创建分页
		this.createTenderPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, AutoTenderExceptionDefine.TENDER_INFO);
		return modelAndView;
	}
	
	/**
	 * 出借明细创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createTenderPage(HttpServletRequest request, ModelAndView modelAndView, AutoTenderExceptionBean form) {
		//出借共通实体
		BorrowInvestCustomize borrowInvestCustomize = new BorrowInvestCustomize();
		// 传 汇计划加入订单号
		borrowInvestCustomize.setAccedeOrderIdSrch(form.getAccedeOrderIdSrch());
		// 传 计划编号
		borrowInvestCustomize.setPlanNidSrch(form.getDebtPlanNidSrch());
		// 调用出借查询
		Long count = this.borrowInvestService.countBorrowInvest(borrowInvestCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			borrowInvestCustomize.setLimitStart(paginator.getOffset());
			borrowInvestCustomize.setLimitEnd(paginator.getLimit());
			List<BorrowInvestCustomize> recordList = this.borrowInvestService.selectBorrowInvestList(borrowInvestCustomize);
			form.setPaginator(paginator);
			form.setRecordLists(recordList);
			modelAndView.addObject("recordList",recordList);
		}
		modelAndView.addObject(AutoTenderExceptionDefine.TENDER_INFO_FORM, form);
	}
			
}
