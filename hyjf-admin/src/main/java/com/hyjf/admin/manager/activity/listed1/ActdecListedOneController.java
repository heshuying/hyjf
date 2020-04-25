package com.hyjf.admin.manager.activity.listed1;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.borrow.borrow.BorrowController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.customize.ActdecListedOneCustomize;

/**
 * @package com.hyjf.admin.manager.activity.shangshi1
 * @author liushouyi
 * @date 2018/01/31 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = ActdecListedOneDefine.REQUEST_MAPPING)
public class ActdecListedOneController extends BaseController {

	@Autowired
	private ActdecListedOneService actdecListedOneService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ActdecListedOneDefine.INIT)
	@RequiresPermissions(ActdecListedOneDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("actdecListedOneForm") ActdecListedOneBean form) {
		LogUtil.startLog(ActdecListedOneController.class.toString(), ActdecListedOneDefine.INIT);
		ModelAndView modeAndView = new ModelAndView(ActdecListedOneDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(ActdecListedOneController.class.toString(), ActdecListedOneDefine.INIT);
		return modeAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ActdecListedOneDefine.SEARCH_ACTION)
	@RequiresPermissions(ActdecListedOneDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, HttpServletResponse response, ActdecListedOneBean form) {
		LogUtil.startLog(ActdecListedOneController.class.toString(), ActdecListedOneDefine.SEARCH_ACTION);
		ModelAndView modeAndView = new ModelAndView(ActdecListedOneDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(ActdecListedOneController.class.toString(), ActdecListedOneDefine.SEARCH_ACTION);
		return modeAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modeAndView, ActdecListedOneBean actdecListedOneBean) {

		ActdecListedOneCustomize actdecListedOneCustomize = new ActdecListedOneCustomize();
		// 用户名
		actdecListedOneCustomize.setUserNameSrch(actdecListedOneBean.getUserNameSrch());
		// 姓名
		actdecListedOneCustomize.setUserTureNameSrch(actdecListedOneBean.getUserTureNameSrch());
		// 手机号
		actdecListedOneCustomize.setUserMobileSrch(actdecListedOneBean.getUserMobileSrch());
		// 标的编号
		actdecListedOneCustomize.setNumberSrch(actdecListedOneBean.getNumberSrch());
		// 订单号
		actdecListedOneCustomize.setOrderNumberSrch(actdecListedOneBean.getOrderNumberSrch());
		// 获得奖励
		actdecListedOneCustomize.setRewardSrch(actdecListedOneBean.getRewardSrch());
		// 是否领取
		actdecListedOneCustomize.setWhetherSrch(actdecListedOneBean.getWhetherSrch());
		// 领取时间
		actdecListedOneCustomize.setCreateTimeStartSrch(actdecListedOneBean.getCreateTimeStartSrch());
		actdecListedOneCustomize.setCreateTimeEndSrch(actdecListedOneBean.getCreateTimeEndSrch());

		Integer count = this.actdecListedOneService.countActdecListedOne(actdecListedOneCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(actdecListedOneBean.getPaginatorPage(), count);
			actdecListedOneCustomize.setLimitStart(paginator.getOffset());
			actdecListedOneCustomize.setLimitEnd(paginator.getLimit());
			List<ActdecListedOneCustomize> recordList = this.actdecListedOneService.selectActdecListedOneList(actdecListedOneCustomize);
			actdecListedOneBean.setPaginator(paginator);
			modeAndView.addObject("recordList", recordList);
		}
		modeAndView.addObject(ActdecListedOneDefine.ACTDEC_LISTED_ONE_FORM, actdecListedOneBean);
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(ActdecListedOneDefine.EXPORT_ACTION)
	@RequiresPermissions(ActdecListedOneDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, ActdecListedOneBean actdecListedOneBean)
			throws Exception {
		LogUtil.startLog(BorrowController.class.toString(), ActdecListedOneDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "上市活动一列表";

		ActdecListedOneCustomize actdecListedOneCustomize = new ActdecListedOneCustomize();
		// 用户名
		actdecListedOneCustomize.setUserNameSrch(actdecListedOneBean.getUserNameSrch());
		// 姓名
		actdecListedOneCustomize.setUserTureNameSrch(actdecListedOneBean.getUserTureNameSrch());
		// 手机号
		actdecListedOneCustomize.setUserMobileSrch(actdecListedOneBean.getUserMobileSrch());
		// 标的编号
		actdecListedOneCustomize.setNumberSrch(actdecListedOneBean.getNumberSrch());
		// 订单号
		actdecListedOneCustomize.setOrderNumberSrch(actdecListedOneBean.getOrderNumberSrch());
		// 获得奖励
		actdecListedOneCustomize.setRewardSrch(actdecListedOneBean.getRewardSrch());
		// 是否领取
		actdecListedOneCustomize.setWhetherSrch(actdecListedOneBean.getWhetherSrch());
		// 领取时间
		actdecListedOneCustomize.setCreateTimeStartSrch(actdecListedOneBean.getCreateTimeStartSrch());
		actdecListedOneCustomize.setCreateTimeEndSrch(actdecListedOneBean.getCreateTimeEndSrch());

		List<ActdecListedOneCustomize> resultList = this.actdecListedOneService.exportActdecListedOneList(actdecListedOneCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "用户id", "用户手机号", "用户名", "真实姓名", "出借金额", "年化金额", "标的编号" , "订单编号", "首尾笔订单", "获得奖励", 
				"是否领取", "创建人", "创建时间", "修改人", "修改时间", "是否删除"};
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
					sheet = ExportExcel
							.createHSSFWorkbookTitle(workbook, titles, (sheetName + "_第" + sheetCount + "页"));
					rowNum = 1;
				}

				// 新建一行
				Row row = sheet.createRow(rowNum);
				// 循环数据
				for (int celLength = 0; celLength < titles.length; celLength++) {
					ActdecListedOneCustomize actdecListedOneCustomizeResult = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 用户id
					else if (celLength == 1) {
						cell.setCellValue(actdecListedOneCustomizeResult.getUserId() + "");
					}
					// 用户手机号
					else if (celLength == 2) {
						cell.setCellValue(actdecListedOneCustomizeResult.getUserMobile() + "");
					}
					// 用户名
					else if (celLength == 3) {
						cell.setCellValue(actdecListedOneCustomizeResult.getUserName() + "");
					}
					// 真实姓名
					else if (celLength == 4) {
						cell.setCellValue(actdecListedOneCustomizeResult.getUserTureName() + "");
					}
					// 出借金额
					else if (celLength == 5) {
						if (null != actdecListedOneCustomizeResult.getInvestment()) {
							cell.setCellValue(actdecListedOneCustomizeResult.getInvestment() + "");
						} else {
							cell.setCellValue("");
						}
					}
					// 年化金额
					else if (celLength == 6) {
						if (null != actdecListedOneCustomizeResult.getAnnual()) {
							cell.setCellValue(actdecListedOneCustomizeResult.getAnnual() + "");
						} else {
							cell.setCellValue("");
						}
					}
					// 标记编号
					else if (celLength == 7) {
						cell.setCellValue(actdecListedOneCustomizeResult.getNumber() + "");
					}
					// 订单编号
					else if (celLength == 8) {
						cell.setCellValue(actdecListedOneCustomizeResult.getOrderNumber() + "");
					}
					// 首尾笔订单
					else if (celLength == 9) {
						if (actdecListedOneCustomizeResult.getType().equals(0)) {
							cell.setCellValue("首");
						} else if (actdecListedOneCustomizeResult.getType().equals(1)) {
							cell.setCellValue("尾");
						} else if (actdecListedOneCustomizeResult.getType().equals(2)) {
							cell.setCellValue("首尾");
						}
					}
					// 获得奖励
					else if (celLength == 10) {
						if (null != actdecListedOneCustomizeResult.getReward()) {
							cell.setCellValue(actdecListedOneCustomizeResult.getReward());
						} else {
							cell.setCellValue("");
						}
					}
					// 是否领取 0未领取 1 已领取
					else if (celLength == 11) {
						if (null != actdecListedOneCustomizeResult.getWhether()) {
							if (actdecListedOneCustomizeResult.getWhether().equals(0)) {
								cell.setCellValue("未领取 ");
							} else if (actdecListedOneCustomizeResult.getWhether().equals(1)) {
								cell.setCellValue("已领取 ");
							}
						} else {
							cell.setCellValue("未领取 ");
						}
					}
					// 创建人
					else if (celLength == 12) {
						cell.setCellValue(StringUtils.isBlank(actdecListedOneCustomizeResult.getCreateUser()) ? "" : actdecListedOneCustomizeResult.getCreateUser());
					}
					// 创建时间
					else if (celLength == 13) {
						if (("0").equals(actdecListedOneCustomizeResult.getCreateTime() + "") || null == actdecListedOneCustomizeResult.getCreateTime()) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(actdecListedOneCustomizeResult.getCreateTime()));
						}
					}
					// 修改人
					else if (celLength == 14) {
						cell.setCellValue(StringUtils.isBlank(actdecListedOneCustomizeResult.getUpdateUser()) ? "" : actdecListedOneCustomizeResult.getUpdateUser());
					}
					// 修改时间
					else if (celLength == 15) {
						if (("0").equals(actdecListedOneCustomizeResult.getUpdateTime() + "") || null == actdecListedOneCustomizeResult.getUpdateTime()) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(actdecListedOneCustomizeResult.getUpdateTime()));
						}
					}
					// 是否删除
					else if (celLength == 16) {
						if (null != actdecListedOneCustomizeResult.getDelFlg()) {
							cell.setCellValue(actdecListedOneCustomizeResult.getDelFlg());
						} else {
							cell.setCellValue("");
						}
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(BorrowController.class.toString(), ActdecListedOneDefine.EXPORT_ACTION);
	}
}
