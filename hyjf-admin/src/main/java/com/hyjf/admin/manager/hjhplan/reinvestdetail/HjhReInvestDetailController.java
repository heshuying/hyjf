package com.hyjf.admin.manager.hjhplan.reinvestdetail;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.customize.web.hjh.HjhReInvestDetailCustomize;

/**
 * 复投详情
 * 
 * @author HJH
 */
@Controller
@RequestMapping(value = HjhReInvestDetailDefine.REQUEST_MAPPING)
public class HjhReInvestDetailController extends BaseController {

	@Autowired
	private HjhReInvestDetailService hjhReInvestDetailService;

	/**
	 * 分页
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modeAndView, HjhReInvestDetailBean form) {

		HjhReInvestDetailCustomize hjhReInvestDetailCustomize = new HjhReInvestDetailCustomize();
		hjhReInvestDetailCustomize.setAccedeOrderIdSrch(form.getAccedeOrderIdSrch());
		hjhReInvestDetailCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
		hjhReInvestDetailCustomize.setBorrowStyleSrch(form.getBorrowStyleSrch());
		hjhReInvestDetailCustomize.setInvestTypeSrch(form.getInvestTypeSrch());
		hjhReInvestDetailCustomize.setLockPeriodSrch(form.getLockPeriodSrch());
		hjhReInvestDetailCustomize.setUserNameSrch(form.getUserNameSrch());
		hjhReInvestDetailCustomize.setPlanNid(form.getPlanNid());
		hjhReInvestDetailCustomize.setDate(form.getDate());

		Integer count = this.hjhReInvestDetailService.queryHjhReInvestDetailCount(hjhReInvestDetailCustomize);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			hjhReInvestDetailCustomize.setLimitStart(paginator.getOffset());
			hjhReInvestDetailCustomize.setLimitEnd(paginator.getLimit());

			List<HjhReInvestDetailCustomize> accountDetails = this.hjhReInvestDetailService.queryHjhReInvestDetails(hjhReInvestDetailCustomize);
			form.setPaginator(paginator);
			modeAndView.addObject("recordList",accountDetails);
			modeAndView.addObject(HjhReInvestDetailDefine.REINVESTDETAIL_FORM, form);
			//求合计值
			String total = this.hjhReInvestDetailService.queryReInvestDetailTotal(hjhReInvestDetailCustomize);
			modeAndView.addObject("total", total);
		} else {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			hjhReInvestDetailCustomize.setLimitStart(paginator.getOffset());
			hjhReInvestDetailCustomize.setLimitEnd(paginator.getLimit());
			form.setPaginator(paginator);
			modeAndView.addObject(HjhReInvestDetailDefine.REINVESTDETAIL_FORM, form);
		}
	}

	/**
	 * 资金明细 列表
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HjhReInvestDetailDefine.INIT)
	@RequiresPermissions(HjhReInvestDetailDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, HjhReInvestDetailBean form) {
		LogUtil.startLog(HjhReInvestDetailController.class.toString(), HjhReInvestDetailDefine.INIT);
		ModelAndView modeAndView = new ModelAndView(HjhReInvestDetailDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(HjhReInvestDetailController.class.toString(), HjhReInvestDetailDefine.INIT);
		return modeAndView;
	}
	
	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HjhReInvestDetailDefine.SEARCH_ACTION)
	@RequiresPermissions(HjhReInvestDetailDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request, HttpServletResponse response, HjhReInvestDetailBean form) {
		LogUtil.startLog(HjhReInvestDetailDefine.class.toString(), HjhReInvestDetailDefine.SEARCH_ACTION);
		ModelAndView modeAndView = new ModelAndView(HjhReInvestDetailDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(HjhReInvestDetailDefine.class.toString(), HjhReInvestDetailDefine.SEARCH_ACTION);
		return modeAndView;
	}

	/**
	 * 导出资金明细列表
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(HjhReInvestDetailDefine.EXPORT_ACTION)
	@RequiresPermissions(HjhReInvestDetailDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, HjhReInvestDetailBean form) throws Exception {
		// 表格sheet名称
		String sheetName = "资金明细";

		HjhReInvestDetailCustomize hjhReInvestDetailCustomize = new HjhReInvestDetailCustomize();
		hjhReInvestDetailCustomize.setAccedeOrderIdSrch(form.getAccedeOrderIdSrch());
		hjhReInvestDetailCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
		hjhReInvestDetailCustomize.setBorrowStyleSrch(form.getBorrowStyleSrch());
		hjhReInvestDetailCustomize.setInvestTypeSrch(form.getInvestTypeSrch());
		hjhReInvestDetailCustomize.setLockPeriodSrch(form.getLockPeriodSrch());
		hjhReInvestDetailCustomize.setUserNameSrch(form.getUserNameSrch());
		hjhReInvestDetailCustomize.setBorrowStyleSrch(form.getBorrowStyleSrch());
		hjhReInvestDetailCustomize.setInvestTypeSrch(form.getInvestTypeSrch());
		hjhReInvestDetailCustomize.setPlanNid(form.getPlanNid());
		hjhReInvestDetailCustomize.setDate(form.getDate());
		// 取得数据
		List<HjhReInvestDetailCustomize> recordList = this.hjhReInvestDetailService.exportReInvestDetails(hjhReInvestDetailCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + ".xls";

		String[] titles = new String[] { "序号","智投订单号","智投编号","用户名","推荐人","用户属性","项目编号","出借利率","借款期限","出借金额（元）","还款方式","投标方式","开始计息时间","出借时间" };
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
					HjhReInvestDetailCustomize hjhReInvestDetail = recordList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 计划订单号
					else if (celLength == 1) {
						cell.setCellValue(hjhReInvestDetail.getAccedeOrderId());
					}
					// 计划编号
					else if (celLength == 2) {
						cell.setCellValue(hjhReInvestDetail.getPlanNid());
					}
					// 用户名
					else if (celLength == 3) {
						cell.setCellValue(hjhReInvestDetail.getUserName());
					}
					// 推荐人
					else if (celLength == 4) {
						cell.setCellValue(hjhReInvestDetail.getInviteUserName());
					}
					// 用户属性
					else if (celLength == 5) {
						cell.setCellValue(hjhReInvestDetail.getUserAttribute());
					}
					// 项目编号
					else if (celLength == 6) {
						cell.setCellValue(hjhReInvestDetail.getBorrowNid());
					}
					// 年化利率
					else if (celLength == 7) {
						cell.setCellValue(hjhReInvestDetail.getExpectApr());
					}
					// 借款期限
					else if (celLength == 8) {
						cell.setCellValue(hjhReInvestDetail.getBorrowPeriod() + hjhReInvestDetail.getIsMonth());
					}
					// 出借金额（元）
					else if (celLength == 9) {
						cell.setCellValue(hjhReInvestDetail.getAccedeAccount());
					}
					// 还款方式
					else if (celLength == 10) {
						cell.setCellValue(hjhReInvestDetail.getBorrowStyle());
					}
					// 投标方式
					else if (celLength == 11) {
						cell.setCellValue(hjhReInvestDetail.getInvestType());
					}
					// 计息时间
					else if (celLength == 12) {
						cell.setCellValue(hjhReInvestDetail.getCountInterestTime());
					}
					// 出借时间
					else if (celLength == 13) {
						cell.setCellValue(hjhReInvestDetail.getAddTime());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

	}
}
