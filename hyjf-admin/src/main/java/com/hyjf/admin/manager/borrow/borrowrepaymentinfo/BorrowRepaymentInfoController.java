package com.hyjf.admin.manager.borrow.borrowrepaymentinfo;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.borrow.borrowrepaymentinfo.infolist.BorrowRepaymentInfoListDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.*;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.customize.BorrowRepaymentInfoCustomize;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = BorrowRepaymentInfoDefine.REQUEST_MAPPING)
public class BorrowRepaymentInfoController extends BaseController {

	@Autowired
	private BorrowRepaymentInfoService borrowRepaymentInfoService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowRepaymentInfoDefine.INIT)
	@RequiresPermissions(BorrowRepaymentInfoDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, BorrowRepaymentInfoBean form) {
		LogUtil.startLog(BorrowRepaymentInfoController.class.toString(), BorrowRepaymentInfoDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BorrowRepaymentInfoDefine.LIST_PATH);
		// 默认当天
		Date endDate = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		form.setYesTimeStartSrch(simpleDateFormat.format(DateUtils.addDays(endDate, 0)));
		form.setYesTimeEndSrch(simpleDateFormat.format(DateUtils.addDays(endDate, 0)));
		//modify by cwyang 搜索条件存在标的号时，不加时间限制  20180510
		if(StringUtils.isNotBlank(form.getBorrowNid())){
			form.setYesTimeStartSrch(null);
			form.setYesTimeEndSrch(null);
		}
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BorrowRepaymentInfoController.class.toString(), BorrowRepaymentInfoDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowRepaymentInfoDefine.SEARCH_ACTION)
	@RequiresPermissions(BorrowRepaymentInfoDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, BorrowRepaymentInfoBean form) {
		LogUtil.startLog(BorrowRepaymentInfoController.class.toString(), BorrowRepaymentInfoDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowRepaymentInfoDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BorrowRepaymentInfoController.class.toString(), BorrowRepaymentInfoDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BorrowRepaymentInfoBean form) {

		BorrowRepaymentInfoCustomize borrowRepaymentInfoCustomize = new BorrowRepaymentInfoCustomize();
		BeanUtils.copyProperties(form, borrowRepaymentInfoCustomize);
		/*--------------add by LSY START-------------------*/
		// 资金来源
		List<HjhInstConfig> hjhInstConfigList = this.borrowRepaymentInfoService.hjhInstConfigList("");
		modelAndView.addObject("hjhInstConfigList", hjhInstConfigList);
		/*--------------add by LSY END-------------------*/
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

		if(borrowRepaymentInfoCustomize.getYesTimeStartSrch() != null&&!"".equals(borrowRepaymentInfoCustomize.getYesTimeStartSrch())) {
	        Date date;
			try {
				date = simpleDateFormat.parse(borrowRepaymentInfoCustomize.getYesTimeStartSrch());
				
				borrowRepaymentInfoCustomize.setYesTimeStartSrch(String.valueOf(date.getTime()/1000));
			} catch (ParseException e) {
				e.printStackTrace();
			}
	        
		}
		if(borrowRepaymentInfoCustomize.getYesTimeEndSrch() != null&&!"".equals(borrowRepaymentInfoCustomize.getYesTimeStartSrch())) {
			Date date;
			try {
				date = simpleDateFormat.parse(borrowRepaymentInfoCustomize.getYesTimeEndSrch());
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				cal.add(Calendar.DATE, 1);
				
				borrowRepaymentInfoCustomize.setYesTimeEndSrch(String.valueOf(cal.getTime().getTime()/1000));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
		borrowRepaymentInfoCustomize.setSerchFlag(form.getSerchFlag());
		Long count = this.borrowRepaymentInfoService.countBorrowRepaymentInfo(borrowRepaymentInfoCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			borrowRepaymentInfoCustomize.setLimitStart(paginator.getOffset());
			borrowRepaymentInfoCustomize.setLimitEnd(paginator.getLimit());
			// 将列表查询与导出查询独立区分
			List<BorrowRepaymentInfoCustomize> recordList = this.borrowRepaymentInfoService.selectBorrowRepaymentInfoListForView(borrowRepaymentInfoCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
			BorrowRepaymentInfoCustomize sumObject = this.borrowRepaymentInfoService.sumBorrowRepaymentInfo(borrowRepaymentInfoCustomize);
			modelAndView.addObject("sumObject", sumObject);
		}
		modelAndView.addObject(BorrowRepaymentInfoDefine.REPAYMENTINFO_FORM, form);
	}

	/**
	 * 跳转到详细列表
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowRepaymentInfoDefine.TO_XIANGQING_ACTION)
	@RequiresPermissions(BorrowRepaymentInfoDefine.PERMISSIONS_INFO)
	public ModelAndView toSearchInfoListAction(HttpServletRequest request, BorrowRepaymentInfoBean form, RedirectAttributes attr) {
		LogUtil.startLog(BorrowRepaymentInfoController.class.toString(), BorrowRepaymentInfoDefine.TO_XIANGQING_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowRepaymentInfoListDefine.RE_LIST_PATH);
		attr.addAttribute("borrowNid", form.getBorrowNid());
		attr.addAttribute("nid", form.getNid());
		attr.addAttribute(BorrowRepaymentInfoListDefine.ACTFROM, BorrowRepaymentInfoListDefine.ACTFROMINFO);

		LogUtil.endLog(BorrowRepaymentInfoController.class.toString(), BorrowRepaymentInfoDefine.TO_XIANGQING_ACTION);
		return modelAndView;
	}

	/**
	 * 数据导出
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowRepaymentInfoDefine.ENHANCE_EXPORT_ACTION)
	@RequiresPermissions(value = {BorrowRepaymentInfoDefine.PERMISSIONS_EXPORT, BorrowRepaymentInfoDefine.ENHANCE_PERMISSIONS_EXPORT}, logical = Logical.AND)
	public void enhanceExportAction(HttpServletRequest request, HttpServletResponse response, BorrowRepaymentInfoBean form) {
		LogUtil.startLog(BorrowRepaymentInfoController.class.toString(), BorrowRepaymentInfoDefine.ENHANCE_EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "还款明细导出数据";

		BorrowRepaymentInfoCustomize borrowRepaymentInfoCustomize = new BorrowRepaymentInfoCustomize();
		BeanUtils.copyProperties(form, borrowRepaymentInfoCustomize);
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		if(borrowRepaymentInfoCustomize.getYesTimeStartSrch() != null&&!"".equals(borrowRepaymentInfoCustomize.getYesTimeStartSrch())) {
	        Date date;
			try {
				date = simpleDateFormat.parse(borrowRepaymentInfoCustomize.getYesTimeStartSrch());
				
				borrowRepaymentInfoCustomize.setYesTimeStartSrch(String.valueOf(date.getTime()/1000));
			} catch (ParseException e) {
				e.printStackTrace();
			}
	        
		}
		if(borrowRepaymentInfoCustomize.getYesTimeEndSrch() != null&&!"".equals(borrowRepaymentInfoCustomize.getYesTimeEndSrch())) {
			Date date;
			try {
				date = simpleDateFormat.parse(borrowRepaymentInfoCustomize.getYesTimeEndSrch());
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				cal.add(Calendar.DATE, 1);
				
				borrowRepaymentInfoCustomize.setYesTimeEndSrch(String.valueOf(cal.getTime().getTime()/1000));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}

		List<BorrowRepaymentInfoCustomize> recordList;
        if ((StringUtils.isBlank(borrowRepaymentInfoCustomize.getYesTimeStartSrch()) && StringUtils.isBlank(borrowRepaymentInfoCustomize.getYesTimeEndSrch())) &&
                (StringUtils.isBlank(borrowRepaymentInfoCustomize.getRecoverTimeStartSrch()) && StringUtils.isBlank(borrowRepaymentInfoCustomize.getRecoverTimeEndSrch()))) {
			recordList = new ArrayList<BorrowRepaymentInfoCustomize>();
		}else{
			recordList = this.borrowRepaymentInfoService.selectBorrowRepaymentInfoList(borrowRepaymentInfoCustomize);
		}

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		/*-----------upd by LSY START---------------------*/
        //String[] titles = new String[] { "项目编号", "计划编号", "借款人ID", "借款人用户名", "项目名称", "项目类型", "项目期限", "出借利率", "借款金额", "借到金额", "还款方式", "出借人用户名", "出借人ID", "出借人用户属性（当前）", "出借人所属一级分部（当前）", "出借人所属二级分部（当前）", "出借人所属团队（当前）", "推荐人用户名（当前）", "推荐人姓名（当前）", "推荐人所属一级分部（当前）", "推荐人所属二级分部（当前）", "推荐人所属团队（当前）", "出借金额", "应回本金",
        //"应回利息", "应还本息", "管理费", "已回本金", "已回利息", "已回本息", "剩余待回本金", "剩余待回利息", "剩余待回本息", "回款状态", "到期日","实际回款时间"};
        String[] titles = new String[] { "项目编号", "资产来源", "智投编号", "借款人ID", "借款人用户名", "项目名称", "项目类型", "项目期限", "出借利率", "借款金额", "借到金额", "还款方式", "出借人用户名", "出借人ID", "出借人用户属性（当前）", "出借人所属一级分部（当前）", "出借人所属二级分部（当前）", "出借人所属团队（当前）", "推荐人用户名（当前）", "推荐人姓名（当前）", "推荐人所属一级分部（当前）", "推荐人所属二级分部（当前）", "推荐人所属团队（当前）", "出借金额", "应回本金",
                "应回利息", "应回本息", "还款服务费", "已回本金", "已回利息", "已回本息", "剩余待回本金", "剩余待回利息", "剩余待回本息", "回款状态", "到期日","实际回款时间","还款冻结订单号"};
		/*-----------upd by LSY END---------------------*/
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");

		if (recordList != null && recordList.size() > 0) {

			int sheetCount = 1;
			int rowNum = 0;

			for (int i = 0; i < recordList.size(); i++) {
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
					BorrowRepaymentInfoCustomize record = recordList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					// 项目编号计划编号
					if (celLength == 0) {
						cell.setCellValue(record.getBorrowNid());
					}
					/*----------add by LSY START---------------------*/
					// 资产来源
					else if (celLength == 1) {
						cell.setCellValue(record.getInstName());
					}
					/*----------add by LSY END---------------------*/
					// 计划编号
					else if (celLength == 2) {
						cell.setCellValue(record.getPlanNid());
					}
					// 借款人ID
					else if (celLength == 3) {
						cell.setCellValue(record.getUserId());
					}
					// 借款人用户名
					else if (celLength == 4) {
						cell.setCellValue(record.getBorrowUserName());
					}
					// 项目名称
					else if (celLength == 5) {
						cell.setCellValue(record.getBorrowName());
					}
					// 项目类型
					else if (celLength == 6) {
						cell.setCellValue(record.getProjectTypeName());
					}
					// 借款期限
					else if (celLength == 7) {
						if("endday".equals(record.getBorrowStyle())) {
							cell.setCellValue(record.getBorrowPeriod() + "天");
						}else{
							cell.setCellValue(record.getBorrowPeriod() + "个月");
						}
					}
					// 出借利率
					else if (celLength == 8) {
						cell.setCellValue(record.getBorrowApr() + "%");
					}
					// 借款金额
					else if (celLength == 9) {
						cell.setCellValue(
								record.getBorrowAccount().equals("") ? 0 : Double.valueOf(record.getBorrowAccount()));
					}
					// 借到金额
					else if (celLength == 10) {
						cell.setCellValue(record.getBorrowAccountYes().equals("") ? 0
								: Double.valueOf(record.getBorrowAccountYes()));
					}
					// 还款方式
					else if (celLength == 11) {
						cell.setCellValue(record.getRepayType());
					}
					// 出借人用户名
					else if (celLength == 12) {
						cell.setCellValue(record.getRecoverUserName());
					}
					// 出借人ID
					else if (celLength == 13) {
						cell.setCellValue(record.getRecoverUserId());
					}
					// 出借人用户属性（当前）
					else if (celLength == 14) {
						if ("0".equals(record.getRecoverUserAttribute())) {
							cell.setCellValue("无主单");
						} else if ("1".equals(record.getRecoverUserAttribute())) {
							cell.setCellValue("有主单");
						} else if ("2".equals(record.getRecoverUserAttribute())) {
							cell.setCellValue("线下员工");
						} else if ("3".equals(record.getRecoverUserAttribute())) {
							cell.setCellValue("线上员工");
						}
					}
					// 出借人所属一级分部（当前）
					else if (celLength == 15) {
						cell.setCellValue(record.getRecoverRegionName());
					}
					// 出借人所属二级分部（当前）
					else if (celLength == 16) {
						cell.setCellValue(record.getRecoverBranchName());
					}
					// 出借人所属团队（当前）
					else if (celLength == 17) {
						cell.setCellValue(record.getRecoverDepartmentName());
					}
					// 推荐人用户名（当前）
					else if (celLength == 18) {
						cell.setCellValue(record.getReferrerName());
					}
					// 推荐人姓名（当前）
					else if (celLength == 19) {
						cell.setCellValue(record.getReferrerTrueName());
					}
					// 推荐人所属一级分部（当前）
					else if (celLength == 20) {
						cell.setCellValue(record.getReferrerRegionName());
					}
					// 推荐人所属二级分部（当前）
					else if (celLength == 21) {
						cell.setCellValue(record.getReferrerBranchName());
					}
					// 推荐人所属团队（当前）
					else if (celLength == 22) {
						cell.setCellValue(record.getReferrerDepartmentName());
					}
					// 出借金额
					else if (celLength == 23) {
						cell.setCellValue(
								record.getRecoverTotal().equals("") ? 0 : Double.valueOf(record.getRecoverTotal()));
					}
					// 应还本金
					else if (celLength == 24) {
						cell.setCellValue(
								record.getRecoverCapital().equals("") ? 0 : Double.valueOf(record.getRecoverCapital()));
					}
					// 应还利息
					else if (celLength == 25) {
						cell.setCellValue(record.getRecoverInterest().equals("") ? 0
								: Double.valueOf(record.getRecoverInterest()));
					}
					// 应还本息
					else if (celLength == 26) {
						cell.setCellValue(
								record.getRecoverAccount().equals("") ? 0 : Double.valueOf(record.getRecoverAccount()));
					}
					// 管理费
					else if (celLength == 27) {
						cell.setCellValue(
								record.getRecoverFee().equals("") ? 0 : Double.valueOf(record.getRecoverFee()));
					}
					// 已还本金
					else if (celLength == 28) {
						cell.setCellValue(record.getRecoverCapitalYes().equals("") ? 0
								: Double.valueOf(record.getRecoverCapitalYes()));
					}
					// 已还利息
					else if (celLength == 29) {
						cell.setCellValue(record.getRecoverInterestYes().equals("") ? 0
								: Double.valueOf(record.getRecoverInterestYes()));
					}
					// 已还本息
					else if (celLength == 30) {
						cell.setCellValue(record.getRecoverAccountYes().equals("") ? 0
								: Double.valueOf(record.getRecoverAccountYes()));
					}
					// 未还本金
					else if (celLength == 31) {
						cell.setCellValue(record.getRecoverCapitalWait().equals("") ? 0
								: Double.valueOf(record.getRecoverCapitalWait()));
					}
					// 未还利息
					else if (celLength == 32) {
						cell.setCellValue(record.getRecoverInterestWait().equals("") ? 0
								: Double.valueOf(record.getRecoverInterestWait()));
					}
					// 未还本息
					else if (celLength == 33) {
						cell.setCellValue(record.getRecoverAccountWait().equals("") ? 0
								: Double.valueOf(record.getRecoverAccountWait()));
					}
					// 还款状态
					else if (celLength == 34) {
						if (StringUtils.isNotEmpty(record.getStatus())) {
							cell.setCellValue(record.getStatus().equals("0") ? "还款中" : "已还款");
						}
					}
					// 最后还款日
					else if (celLength == 35) {
						cell.setCellValue(record.getRecoverLastTime());
					}else if (celLength == 36) {
						cell.setCellValue(record.getRepayActionTime());
					}else if (celLength == 37) {
						cell.setCellValue(StringUtils.isBlank(record.getFreezeOrderId())?"":record.getFreezeOrderId());
					}

				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(BorrowRepaymentInfoController.class.toString(), BorrowRepaymentInfoDefine.ENHANCE_EXPORT_ACTION);
	}

	@RequestMapping(BorrowRepaymentInfoDefine.EXPORT_ACTION)
	@RequiresPermissions(BorrowRepaymentInfoDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, BorrowRepaymentInfoBean form) {
		LogUtil.startLog(BorrowRepaymentInfoController.class.toString(), BorrowRepaymentInfoDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "还款明细导出数据";

		BorrowRepaymentInfoCustomize borrowRepaymentInfoCustomize = new BorrowRepaymentInfoCustomize();
		BeanUtils.copyProperties(form, borrowRepaymentInfoCustomize);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

		if(borrowRepaymentInfoCustomize.getYesTimeStartSrch() != null&&!"".equals(borrowRepaymentInfoCustomize.getYesTimeStartSrch())) {
			Date date;
			try {
				date = simpleDateFormat.parse(borrowRepaymentInfoCustomize.getYesTimeStartSrch());

				borrowRepaymentInfoCustomize.setYesTimeStartSrch(String.valueOf(date.getTime()/1000));
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
		if(borrowRepaymentInfoCustomize.getYesTimeEndSrch() != null&&!"".equals(borrowRepaymentInfoCustomize.getYesTimeEndSrch())) {
			Date date;
			try {
				date = simpleDateFormat.parse(borrowRepaymentInfoCustomize.getYesTimeEndSrch());
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				cal.add(Calendar.DATE, 1);

				borrowRepaymentInfoCustomize.setYesTimeEndSrch(String.valueOf(cal.getTime().getTime()/1000));
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}


		List<BorrowRepaymentInfoCustomize> recordList;
		if ((StringUtils.isBlank(borrowRepaymentInfoCustomize.getYesTimeStartSrch()) && StringUtils.isBlank(borrowRepaymentInfoCustomize.getYesTimeEndSrch())) &&
				(StringUtils.isBlank(borrowRepaymentInfoCustomize.getRecoverTimeStartSrch()) && StringUtils.isBlank(borrowRepaymentInfoCustomize.getRecoverTimeEndSrch()))) {
			recordList = new ArrayList<BorrowRepaymentInfoCustomize>();
		}else{
			recordList = this.borrowRepaymentInfoService.selectBorrowRepaymentInfoList(borrowRepaymentInfoCustomize);
		}

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		/*-----------upd by LSY START---------------------*/
        String[] titles = new String[] { "项目编号", "资产来源", "智投编号", "借款人ID", "借款人用户名", "项目名称", "项目类型", "项目期限", "出借利率", "借款金额", "借到金额", "还款方式", "出借人用户名", "出借人ID", "出借人用户属性（当前）", "推荐人用户名（当前）", "推荐人姓名（当前）", "出借金额", "应回本金",
                "应回利息", "应回本息", "还款服务费", "已回本金", "已回利息", "已回本息", "剩余待回本金", "剩余待回利息", "剩余待回本息", "回款状态", "到期日","实际回款时间", "还款冻结订单号"};
		/*-----------upd by LSY END---------------------*/
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");

		if (recordList != null && recordList.size() > 0) {

			int sheetCount = 1;
			int rowNum = 0;

			for (int i = 0; i < recordList.size(); i++) {
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
					BorrowRepaymentInfoCustomize record = recordList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					// 项目编号计划编号
					if (celLength == 0) {
						cell.setCellValue(record.getBorrowNid());
					}
					/*----------add by LSY START---------------------*/
					// 资产来源
					else if (celLength == 1) {
						cell.setCellValue(record.getInstName());
					}
					/*----------add by LSY END---------------------*/
					// 智投编号
					else if (celLength == 2) {
						cell.setCellValue(record.getPlanNid());
					}
					// 借款人ID
					else if (celLength == 3) {
						cell.setCellValue(record.getUserId());
					}
					// 借款人用户名
					else if (celLength == 4) {
						cell.setCellValue(record.getBorrowUserName());
					}
					// 项目名称
					else if (celLength == 5) {
						cell.setCellValue(record.getBorrowName());
					}
					// 项目类型
					else if (celLength == 6) {
						cell.setCellValue(record.getProjectTypeName());
					}
					// 借款期限
					else if (celLength == 7) {
						if("endday".equals(record.getBorrowStyle())) {
							cell.setCellValue(record.getBorrowPeriod() + "天");
						}else{
							cell.setCellValue(record.getBorrowPeriod() + "个月");
						}
					}
					// 出借利率
					else if (celLength == 8) {
						cell.setCellValue(record.getBorrowApr() + "%");
					}
					// 借款金额
					else if (celLength == 9) {
						cell.setCellValue(
								record.getBorrowAccount().equals("") ? 0 : Double.valueOf(record.getBorrowAccount()));
					}
					// 借到金额
					else if (celLength == 10) {
						cell.setCellValue(record.getBorrowAccountYes().equals("") ? 0
								: Double.valueOf(record.getBorrowAccountYes()));
					}
					// 还款方式
					else if (celLength == 11) {
						cell.setCellValue(record.getRepayType());
					}
					// 出借人用户名
					else if (celLength == 12) {
						cell.setCellValue(record.getRecoverUserName());
					}
					// 出借人ID
					else if (celLength == 13) {
						cell.setCellValue(record.getRecoverUserId());
					}
					// 出借人用户属性（当前）
					else if (celLength == 14) {
						if ("0".equals(record.getRecoverUserAttribute())) {
							cell.setCellValue("无主单");
						} else if ("1".equals(record.getRecoverUserAttribute())) {
							cell.setCellValue("有主单");
						} else if ("2".equals(record.getRecoverUserAttribute())) {
							cell.setCellValue("线下员工");
						} else if ("3".equals(record.getRecoverUserAttribute())) {
							cell.setCellValue("线上员工");
						}
					}
					// 推荐人用户名（当前）
					else if (celLength == 15) {
						cell.setCellValue(record.getReferrerName());
					}
					// 推荐人姓名（当前）
					else if (celLength == 16) {
						cell.setCellValue(record.getReferrerTrueName());
					}
					// 授权服务金额
					else if (celLength == 17) {
						cell.setCellValue(
								record.getRecoverTotal().equals("") ? 0 : Double.valueOf(record.getRecoverTotal()));
					}
					// 应还本金
					else if (celLength == 18) {
						cell.setCellValue(
								record.getRecoverCapital().equals("") ? 0 : Double.valueOf(record.getRecoverCapital()));
					}
					// 应还利息
					else if (celLength == 19) {
						cell.setCellValue(record.getRecoverInterest().equals("") ? 0
								: Double.valueOf(record.getRecoverInterest()));
					}
					// 应还本息
					else if (celLength == 20) {
						cell.setCellValue(
								record.getRecoverAccount().equals("") ? 0 : Double.valueOf(record.getRecoverAccount()));
					}
					// 管理费
					else if (celLength == 21) {
						cell.setCellValue(
								record.getRecoverFee().equals("") ? 0 : Double.valueOf(record.getRecoverFee()));
					}
					// 已还本金
					else if (celLength == 22) {
						cell.setCellValue(record.getRecoverCapitalYes().equals("") ? 0
								: Double.valueOf(record.getRecoverCapitalYes()));
					}
					// 已还利息
					else if (celLength == 23) {
						cell.setCellValue(record.getRecoverInterestYes().equals("") ? 0
								: Double.valueOf(record.getRecoverInterestYes()));
					}
					// 已还本息
					else if (celLength == 24) {
						cell.setCellValue(record.getRecoverAccountYes().equals("") ? 0
								: Double.valueOf(record.getRecoverAccountYes()));
					}
					// 未还本金
					else if (celLength == 25) {
						cell.setCellValue(record.getRecoverCapitalWait().equals("") ? 0
								: Double.valueOf(record.getRecoverCapitalWait()));
					}
					// 未还利息
					else if (celLength == 26) {
						cell.setCellValue(record.getRecoverInterestWait().equals("") ? 0
								: Double.valueOf(record.getRecoverInterestWait()));
					}
					// 未还本息
					else if (celLength == 27) {
						cell.setCellValue(record.getRecoverAccountWait().equals("") ? 0
								: Double.valueOf(record.getRecoverAccountWait()));
					}
					// 还款状态
					else if (celLength == 28) {
						if (StringUtils.isNotEmpty(record.getStatus())) {
							cell.setCellValue(record.getStatus().equals("0") ? "还款中" : "已还款");
						}
					}
					// 最后还款日
					else if (celLength == 29) {
						cell.setCellValue(record.getRecoverLastTime());
					}else if (celLength == 30) {
						cell.setCellValue(record.getRepayActionTime());
					}
					// 还款冻结订单号
					else if (celLength == 31){
						cell.setCellValue(StringUtils.isBlank(record.getFreezeOrderId())?"":record.getFreezeOrderId());
					}

				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(BorrowRepaymentInfoController.class.toString(), BorrowRepaymentInfoDefine.EXPORT_ACTION);
	}
}
