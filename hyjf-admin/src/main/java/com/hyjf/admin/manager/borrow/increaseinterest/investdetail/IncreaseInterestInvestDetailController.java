package com.hyjf.admin.manager.borrow.increaseinterest.investdetail;

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

import cn.jpush.api.utils.StringUtils;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.IncreaseInterestInvest;

/**
 * 融通宝加息出借明细Controller
 *
 * @ClassName InvestDetailController
 * @author liuyang
 * @date 2016年12月28日 上午11:43:21
 */
@Controller
@RequestMapping(value = IncreaseInterestInvestDetailDefine.REQUEST_MAPPING)
public class IncreaseInterestInvestDetailController extends BaseController {

	@Autowired
	private IncreaseInterestInvestDetailService investDetailService;

	/**
	 * 画面初始化
	 *
	 * @Title init
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(IncreaseInterestInvestDetailDefine.INIT)
	@RequiresPermissions(IncreaseInterestInvestDetailDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, IncreaseInterestInvestDetailBean form) {
		LogUtil.startLog(IncreaseInterestInvestDetailDefine.THIS_CLASS, IncreaseInterestInvestDetailDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(IncreaseInterestInvestDetailDefine.LIST_PATH);
		// 分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(IncreaseInterestInvestDetailDefine.THIS_CLASS, IncreaseInterestInvestDetailDefine.INIT);
		return modelAndView;
	}

	/**
	 * 出借明细分页
	 *
	 * @Title createPage
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	public void createPage(HttpServletRequest request, ModelAndView modelAndView, IncreaseInterestInvestDetailBean form) {
		int total = this.investDetailService.countRecordList(form);
		if (total > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), total);
			List<IncreaseInterestInvest> recordList = this.investDetailService.selectRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setRecordList(recordList);
			form.setPaginator(paginator);
			/*--------add by LSY START-----------*/
			String sumAccount = this.investDetailService.sumAccount(form);
			modelAndView.addObject("sumAccount", sumAccount);
			/*--------add by LSY END-----------*/
		}
		modelAndView.addObject(IncreaseInterestInvestDetailDefine.INVEST_DETAIL_FORM, form);
	}

	/**
	 *
	 * 列表检索Action
	 *
	 * @author liuyang
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(IncreaseInterestInvestDetailDefine.SEARCH_ACTION)
	@RequiresPermissions(IncreaseInterestInvestDetailDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, IncreaseInterestInvestDetailBean form) {
		LogUtil.startLog(IncreaseInterestInvestDetailDefine.THIS_CLASS, IncreaseInterestInvestDetailDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(IncreaseInterestInvestDetailDefine.LIST_PATH);
		// 分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(IncreaseInterestInvestDetailDefine.THIS_CLASS, IncreaseInterestInvestDetailDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 导出功能
	 *
	 * @param request
	 * @param form
	 */
	@RequestMapping(IncreaseInterestInvestDetailDefine.EXPORT_ACTION)
	@RequiresPermissions(IncreaseInterestInvestDetailDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, IncreaseInterestInvestDetailBean form) throws Exception {
		LogUtil.startLog(IncreaseInterestInvestDetailDefine.THIS_CLASS, IncreaseInterestInvestDetailDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "加息出借明细";

		List<IncreaseInterestInvest> resultList = investDetailService.selectRecordList(form, -1, -1);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
		//20180730增加导出字段：推荐人/项目期限/还款方式/出借订单号/加息收益/回款时间
		String[] titles = new String[] { "序号", "出借人","推荐人", "项目编号", "出借利率", "加息收益率", "项目期限","还款方式","出借订单号","出借金额","加息收益", "操作平台", "出借时间" ,"回款时间" };
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
					IncreaseInterestInvest investDetail = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 用户名
					else if (celLength == 1) {
						cell.setCellValue(StringUtils.isEmpty(investDetail.getInvestUserName()) ? "" : investDetail.getInvestUserName());
					}
					//推荐人(出借)++++++++++
					else if (celLength == 2) {
						cell.setCellValue(StringUtils.isEmpty(investDetail.getInviteUserName()) ? "" : investDetail.getInviteUserName());
					}
					// 项目编号
					else if (celLength == 3) {
						cell.setCellValue(StringUtils.isEmpty(investDetail.getBorrowNid()) ? "" : investDetail.getBorrowNid());
					}
					// 出借利率
					else if (celLength == 4) {
						cell.setCellValue(investDetail.getBorrowApr() + "%");
					}
					// 产品加息收益率
					else if (celLength == 5) {
						cell.setCellValue(investDetail.getBorrowExtraYield() + "%");
					}
					//项目期限+++++++++
					else if (celLength == 6) {
						//cell.setCellValue(StringUtils.isEmpty(investDetail.getBorrowPeriod().toString()) ? "" : investDetail.getBorrowPeriod().toString());
						if ("endday".equals(investDetail.getBorrowStyle())) {
							cell.setCellValue(investDetail.getBorrowPeriod() + "天");
						} else {
							cell.setCellValue(investDetail.getBorrowPeriod() + "个月");
						}
					}
					// 还款方式++++++++
					else if (celLength == 7) {
						cell.setCellValue(StringUtils.isEmpty(investDetail.getBorrowStyleName()) ? "" : investDetail.getBorrowStyleName());
					}
					// 出借订单号++++++++
					else if (celLength == 8) {
						cell.setCellValue(StringUtils.isEmpty(investDetail.getOrderId()) ? "" : investDetail.getOrderId());
					}
					// 出借金额
					else if (celLength == 9) {
						cell.setCellValue(investDetail.getAccount().toString());
					}
					// 加息收益++++++++
					else if (celLength == 10) {
						cell.setCellValue(investDetail.getRepayInterest()==null ? "" : investDetail.getRepayInterest().toString());
					}
					// 操作平台
					else if (celLength == 11) {
						// 客户端,0PC,1微官网,2Android,3iOS,4其他
						if (investDetail.getClient() == 0) {
							cell.setCellValue("PC");
						} else if (investDetail.getClient() == 1) {
							cell.setCellValue("微官网");
						} else if (investDetail.getClient() == 2) {
							cell.setCellValue("Android");
						} else if (investDetail.getClient() == 3) {
							cell.setCellValue("iOS");
						} else {
							cell.setCellValue("其他");
						}
					}
					// 出借时间
					else if (celLength == 12) {
						cell.setCellValue(investDetail.getCreateTime()==null ? "" :GetDate.getDateTimeMyTime(investDetail.getCreateTime()));
					}
					// 回款时间++++++++++
					else if (celLength == 13) {
						cell.setCellValue((investDetail.getRepayTime()==null ? 0 : investDetail.getRepayTime()) == 0 ? "" :  GetDate.getDateMyTimeInMillis(investDetail.getRepayTime()));
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(IncreaseInterestInvestDetailDefine.THIS_CLASS, IncreaseInterestInvestDetailDefine.EXPORT_ACTION);
	}
}
