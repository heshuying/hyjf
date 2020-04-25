package com.hyjf.admin.manager.activity.listed4;

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
import com.hyjf.mybatis.model.customize.ActdecListedFourCustomize;

/**
 * @package com.hyjf.admin.manager.activity.shangshi1
 * @author liushouyi
 * @date 2018/01/31 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = ActdecListedFourDefine.REQUEST_MAPPING)
public class ActdecListedFourController extends BaseController {

	@Autowired
	private ActdecListedFourService actdecListedFourService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ActdecListedFourDefine.INIT)
	@RequiresPermissions(ActdecListedFourDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("actdecListedFourForm") ActdecListedFourBean form) {
		LogUtil.startLog(ActdecListedFourController.class.toString(), ActdecListedFourDefine.INIT);
		ModelAndView modeAndView = new ModelAndView(ActdecListedFourDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(ActdecListedFourController.class.toString(), ActdecListedFourDefine.INIT);
		return modeAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ActdecListedFourDefine.SEARCH_ACTION)
	@RequiresPermissions(ActdecListedFourDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, HttpServletResponse response, ActdecListedFourBean form) {
		LogUtil.startLog(ActdecListedFourController.class.toString(), ActdecListedFourDefine.SEARCH_ACTION);
		ModelAndView modeAndView = new ModelAndView(ActdecListedFourDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(ActdecListedFourController.class.toString(), ActdecListedFourDefine.SEARCH_ACTION);
		return modeAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modeAndView, ActdecListedFourBean actdecListedFourBean) {

		ActdecListedFourCustomize actdecListedFourCustomize = new ActdecListedFourCustomize();
		// 用户名
		actdecListedFourCustomize.setUserNameSrch(actdecListedFourBean.getUserNameSrch());
		// 姓名
		actdecListedFourCustomize.setUserTureNameSrch(actdecListedFourBean.getUserTureNameSrch());
		// 手机号
		actdecListedFourCustomize.setUserMobileSrch(actdecListedFourBean.getUserMobileSrch());

		Integer count = this.actdecListedFourService.countActdecListedFour(actdecListedFourCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(actdecListedFourBean.getPaginatorPage(), count);
			actdecListedFourCustomize.setLimitStart(paginator.getOffset());
			actdecListedFourCustomize.setLimitEnd(paginator.getLimit());
			List<ActdecListedFourCustomize> recordList = this.actdecListedFourService.selectActdecListedFourList(actdecListedFourCustomize);
			actdecListedFourBean.setPaginator(paginator);
			modeAndView.addObject("recordList", recordList);
		}
		modeAndView.addObject(ActdecListedFourDefine.ACTDEC_LISTED_FOUR_FORM, actdecListedFourBean);
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(ActdecListedFourDefine.EXPORT_ACTION)
	@RequiresPermissions(ActdecListedFourDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, ActdecListedFourBean actdecListedFourBean)
			throws Exception {
		LogUtil.startLog(BorrowController.class.toString(), ActdecListedFourDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "上市活动三2列表";

		ActdecListedFourCustomize actdecListedFourCustomize = new ActdecListedFourCustomize();
		// 用户名
		actdecListedFourCustomize.setUserNameSrch(actdecListedFourBean.getUserNameSrch());
		// 姓名
		actdecListedFourCustomize.setUserTureNameSrch(actdecListedFourBean.getUserTureNameSrch());
		// 手机号
		actdecListedFourCustomize.setUserMobileSrch(actdecListedFourBean.getUserMobileSrch());

		List<ActdecListedFourCustomize> resultList = this.actdecListedFourService.exportActdecListedFourList(actdecListedFourCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "用户id", "用户手机号", "用户名", "真实姓名", "邀请人id", "邀请人手机号", "邀请人用户名" , "邀请人姓名", "累计", "是否已开户",
				"注册时间", "开户时间", "创建人", "创建时间", "修改人", "修改时间", "是否删除"};
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
					ActdecListedFourCustomize actdecListedFourCustomizeResult = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 用户id
					else if (celLength == 1) {
						cell.setCellValue(actdecListedFourCustomizeResult.getUserId() + "");
					}
					// 手机号
					else if (celLength == 2) {
						cell.setCellValue(actdecListedFourCustomizeResult.getUserMobile() + "");
					}
					// 用户名
					else if (celLength == 3) {
						cell.setCellValue(actdecListedFourCustomizeResult.getUserName() + "");
					}
					// 真实姓名
					else if (celLength == 4) {
						cell.setCellValue(actdecListedFourCustomizeResult.getUserTureName() + "");
					}
					// 邀请人id
					else if (celLength == 5) {
						if (null != actdecListedFourCustomizeResult.getCoverUserId()) {
							cell.setCellValue(actdecListedFourCustomizeResult.getCoverUserId() + "");
						} else {
							cell.setCellValue("");
						}
					}
					// 邀请人手机号
					else if (celLength == 6) {
						if (StringUtils.isNotBlank(actdecListedFourCustomizeResult.getCoverUserMobile())) {
							cell.setCellValue(actdecListedFourCustomizeResult.getCoverUserMobile() + "");
						} else {
							cell.setCellValue("");
						}
					}
					// 邀请人用户名
					else if (celLength == 7) {
						if (null != actdecListedFourCustomizeResult.getCoverUserId()) {
							cell.setCellValue(actdecListedFourCustomizeResult.getCoverUserId() + "");
						} else {
							cell.setCellValue("");
						}
					}
					// 邀请人姓名
					else if (celLength == 8) {
						if (StringUtils.isNotBlank(actdecListedFourCustomizeResult.getCoverUserTureName())) {
							cell.setCellValue(actdecListedFourCustomizeResult.getCoverUserTureName() + "");
						} else {
							cell.setCellValue("");
						}
					}
					// 累计
					else if (celLength == 9) {
						if (null != actdecListedFourCustomizeResult.getCumulative()) {
							cell.setCellValue(actdecListedFourCustomizeResult.getCumulative() + "");
						} else {
							cell.setCellValue("");
						}
					}
					// 是否卡开户
					else if (celLength == 10) {
						if (null != actdecListedFourCustomizeResult.getWhether()) {
							if (actdecListedFourCustomizeResult.getWhether().equals(0)) {
								cell.setCellValue("未开户 ");
							} else if (actdecListedFourCustomizeResult.getWhether().equals(1)) {
								cell.setCellValue("已开户 ");
							}
						} else {
							cell.setCellValue("未开户 ");
						}
					}
					// 注册时间
					else if (celLength == 11) {
						if (("0").equals(actdecListedFourCustomizeResult.getRegistrationTime()  + "") || null == actdecListedFourCustomizeResult.getRegistrationTime()) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(actdecListedFourCustomizeResult.getRegistrationTime()));
						}
					}
					// 开户时间
					else if (celLength == 12) {
						if (("0").equals(actdecListedFourCustomizeResult.getOpenTime() + "") || null == actdecListedFourCustomizeResult.getOpenTime()) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(actdecListedFourCustomizeResult.getOpenTime()));
						}
					}
					// 创建人
					else if (celLength == 13) {
						cell.setCellValue(StringUtils.isBlank(actdecListedFourCustomizeResult.getCreateUser()) ? "" : actdecListedFourCustomizeResult.getCreateUser());
					}
					// 创建时间
					else if (celLength == 14) {
						if (("0").equals(actdecListedFourCustomizeResult.getCreateTime() + "") || null == actdecListedFourCustomizeResult.getCreateTime()) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(actdecListedFourCustomizeResult.getCreateTime()));
						}
					}
					// 修改人
					else if (celLength == 15) {
						cell.setCellValue(StringUtils.isBlank(actdecListedFourCustomizeResult.getUpdateUser()) ? "" : actdecListedFourCustomizeResult.getUpdateUser());
					}
					// 修改时间
					else if (celLength == 16) {
						if (("0").equals(actdecListedFourCustomizeResult.getUpdateTime() + "") || null == actdecListedFourCustomizeResult.getUpdateTime()) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(actdecListedFourCustomizeResult.getUpdateTime()));
						}
					}
					// 是否删除
					else if (celLength == 17) {
						if (null != actdecListedFourCustomizeResult.getDelFlg()) {
							cell.setCellValue(actdecListedFourCustomizeResult.getDelFlg());
						} else {
							cell.setCellValue("");
						}
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(BorrowController.class.toString(), ActdecListedFourDefine.EXPORT_ACTION);
	}
}
