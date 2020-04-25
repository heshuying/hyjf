package com.hyjf.admin.manager.activity.listed3;

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
import com.hyjf.mybatis.model.customize.ActdecListedThreeCustomize;

/**
 * @package com.hyjf.admin.manager.activity.shangshi1
 * @author liushouyi
 * @date 2018/01/31 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = ActdecListedThreeDefine.REQUEST_MAPPING)
public class ActdecListedThreeController extends BaseController {

	@Autowired
	private ActdecListedThreeService actdecListedThreeService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ActdecListedThreeDefine.INIT)
	@RequiresPermissions(ActdecListedThreeDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("actdecListedThreeForm") ActdecListedThreeBean form) {
		LogUtil.startLog(ActdecListedThreeController.class.toString(), ActdecListedThreeDefine.INIT);
		ModelAndView modeAndView = new ModelAndView(ActdecListedThreeDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(ActdecListedThreeController.class.toString(), ActdecListedThreeDefine.INIT);
		return modeAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ActdecListedThreeDefine.SEARCH_ACTION)
	@RequiresPermissions(ActdecListedThreeDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, HttpServletResponse response, ActdecListedThreeBean form) {
		LogUtil.startLog(ActdecListedThreeController.class.toString(), ActdecListedThreeDefine.SEARCH_ACTION);
		ModelAndView modeAndView = new ModelAndView(ActdecListedThreeDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(ActdecListedThreeController.class.toString(), ActdecListedThreeDefine.SEARCH_ACTION);
		return modeAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modeAndView, ActdecListedThreeBean actdecListedThreeBean) {

		ActdecListedThreeCustomize actdecListedThreeCustomize = new ActdecListedThreeCustomize();
		// 用户名
		actdecListedThreeCustomize.setUserNameSrch(actdecListedThreeBean.getUserNameSrch());
		// 姓名
		actdecListedThreeCustomize.setUserTureNameSrch(actdecListedThreeBean.getUserTureNameSrch());
		// 手机号
		actdecListedThreeCustomize.setUserMobileSrch(actdecListedThreeBean.getUserMobileSrch());

		Integer count = this.actdecListedThreeService.countActdecListedThree(actdecListedThreeCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(actdecListedThreeBean.getPaginatorPage(), count);
			actdecListedThreeCustomize.setLimitStart(paginator.getOffset());
			actdecListedThreeCustomize.setLimitEnd(paginator.getLimit());
			List<ActdecListedThreeCustomize> recordList = this.actdecListedThreeService.selectActdecListedThreeList(actdecListedThreeCustomize);
			actdecListedThreeBean.setPaginator(paginator);
			modeAndView.addObject("recordList", recordList);
		}
		modeAndView.addObject(ActdecListedThreeDefine.ACTDEC_LISTED_THREE_FORM, actdecListedThreeBean);
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(ActdecListedThreeDefine.EXPORT_ACTION)
	@RequiresPermissions(ActdecListedThreeDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, ActdecListedThreeBean actdecListedThreeBean)
			throws Exception {
		LogUtil.startLog(BorrowController.class.toString(), ActdecListedThreeDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "上市活动三1列表";

		ActdecListedThreeCustomize actdecListedThreeCustomize = new ActdecListedThreeCustomize();
		// 用户名
		actdecListedThreeCustomize.setUserNameSrch(actdecListedThreeBean.getUserNameSrch());
		// 姓名
		actdecListedThreeCustomize.setUserTureNameSrch(actdecListedThreeBean.getUserTureNameSrch());
		// 手机号
		actdecListedThreeCustomize.setUserMobileSrch(actdecListedThreeBean.getUserMobileSrch());

		List<ActdecListedThreeCustomize> resultList = this.actdecListedThreeService.exportActdecListedThreeList(actdecListedThreeCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "用户id", "用户手机号", "用户名", "真实姓名", "单笔", "累计", "注册时间" ,
				"创建人", "创建时间", "修改人", "修改时间", "是否删除"};
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
					ActdecListedThreeCustomize actdecListedThreeCustomizeResult = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 用户id
					else if (celLength == 1) {
						cell.setCellValue(actdecListedThreeCustomizeResult.getUserId() + "");
					}
					// 用户手机号
					else if (celLength == 2) {
						cell.setCellValue(actdecListedThreeCustomizeResult.getUserMobile() + "");
					}
					// 用户名
					else if (celLength == 3) {
						cell.setCellValue(actdecListedThreeCustomizeResult.getUserName() + "");
					}
					// 真实姓名
					else if (celLength == 4) {
						cell.setCellValue(actdecListedThreeCustomizeResult.getUserTureName() + "");
					}
					// 单笔
					else if (celLength == 5) {
						if (null != actdecListedThreeCustomizeResult.getSingle()) {
							cell.setCellValue(actdecListedThreeCustomizeResult.getSingle() + "");
						} else {
							cell.setCellValue("");
						}
					}
					// 累计
					else if (celLength == 6) {
						if (null != actdecListedThreeCustomizeResult.getCumulative()) {
							cell.setCellValue(actdecListedThreeCustomizeResult.getCumulative() + "");
						} else {
							cell.setCellValue("");
						}
					}
					// 注册时间
					else if (celLength == 7) {
						if (("0").equals(actdecListedThreeCustomizeResult.getRegistrationTime() + "") || null == actdecListedThreeCustomizeResult.getRegistrationTime()) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(actdecListedThreeCustomizeResult.getRegistrationTime()));
						}
					}
					// 创建人
					else if (celLength == 8) {
						cell.setCellValue(StringUtils.isBlank(actdecListedThreeCustomizeResult.getCreateUser()) ? "" : actdecListedThreeCustomizeResult.getCreateUser());
					}
					// 创建时间
					else if (celLength == 9) {
						if (("0").equals(actdecListedThreeCustomizeResult.getCreateTime() + "") || null == actdecListedThreeCustomizeResult.getCreateTime()) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(actdecListedThreeCustomizeResult.getCreateTime()));
						}
					}
					// 修改人
					else if (celLength == 10) {
						cell.setCellValue(StringUtils.isBlank(actdecListedThreeCustomizeResult.getUpdateUser()) ? "" : actdecListedThreeCustomizeResult.getUpdateUser());
					}
					// 修改时间
					else if (celLength == 11) {
						if (("0").equals(actdecListedThreeCustomizeResult.getUpdateTime() + "" ) || null == actdecListedThreeCustomizeResult.getUpdateTime()) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(actdecListedThreeCustomizeResult.getUpdateTime()));
						}
					}
					// 是否删除
					else if (celLength == 12) {
						if (null != actdecListedThreeCustomizeResult.getDelFlg()) {
							cell.setCellValue(actdecListedThreeCustomizeResult.getDelFlg());
						} else {
							cell.setCellValue("");
						}
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(BorrowController.class.toString(), ActdecListedThreeDefine.EXPORT_ACTION);
	}
}
