package com.hyjf.admin.manager.activity.listed2;

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
import com.hyjf.mybatis.model.customize.ActdecListedTwoCustomize;

/**
 * @package com.hyjf.admin.manager.activity.shangshi2
 * @author liushouyi
 * @date 2018/01/31 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = ActdecListedTwoDefine.REQUEST_MAPPING)
public class ActdecListedTwoController extends BaseController {

	@Autowired
	private ActdecListedTwoService actdecListedTwoService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ActdecListedTwoDefine.INIT)
	@RequiresPermissions(ActdecListedTwoDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("actdecListedTwoForm") ActdecListedTwoBean form) {
		LogUtil.startLog(ActdecListedTwoController.class.toString(), ActdecListedTwoDefine.INIT);
		ModelAndView modeAndView = new ModelAndView(ActdecListedTwoDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(ActdecListedTwoController.class.toString(), ActdecListedTwoDefine.INIT);
		return modeAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ActdecListedTwoDefine.SEARCH_ACTION)
	@RequiresPermissions(ActdecListedTwoDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, HttpServletResponse response, ActdecListedTwoBean form) {
		LogUtil.startLog(ActdecListedTwoController.class.toString(), ActdecListedTwoDefine.SEARCH_ACTION);
		ModelAndView modeAndView = new ModelAndView(ActdecListedTwoDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(ActdecListedTwoController.class.toString(), ActdecListedTwoDefine.SEARCH_ACTION);
		return modeAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modeAndView, ActdecListedTwoBean actdecListedTwoBean) {

		ActdecListedTwoCustomize actdecListedTwoCustomize = new ActdecListedTwoCustomize();
		// 用户名
		actdecListedTwoCustomize.setUserNameSrch(actdecListedTwoBean.getUserNameSrch());
		// 姓名
		actdecListedTwoCustomize.setTrueNameSrch(actdecListedTwoBean.getTrueNameSrch());
		// 手机号
		actdecListedTwoCustomize.setMobileSrch(actdecListedTwoBean.getMobileSrch());
		// 操作类型
		actdecListedTwoCustomize.setTradeSrch(actdecListedTwoBean.getTradeSrch());
		// 领取奖励
		actdecListedTwoCustomize.setAcceptPrizeSrch(actdecListedTwoBean.getAcceptPrizeSrch());
		// 领取时间
		actdecListedTwoCustomize.setAcceptTimeStartSrch(actdecListedTwoBean.getAcceptTimeStartSrch());
		actdecListedTwoCustomize.setAcceptTimeEndSrch(actdecListedTwoBean.getAcceptTimeEndSrch());

		Integer count = this.actdecListedTwoService.countActdecListedTwo(actdecListedTwoCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(actdecListedTwoBean.getPaginatorPage(), count);
			actdecListedTwoCustomize.setLimitStart(paginator.getOffset());
			actdecListedTwoCustomize.setLimitEnd(paginator.getLimit());
			List<ActdecListedTwoCustomize> recordList = this.actdecListedTwoService.selectActdecListedTwoList(actdecListedTwoCustomize);
			actdecListedTwoBean.setPaginator(paginator);
			modeAndView.addObject("recordList", recordList);
		}
		modeAndView.addObject(ActdecListedTwoDefine.ACTDEC_LISTED_TWO_FORM, actdecListedTwoBean);
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(ActdecListedTwoDefine.EXPORT_ACTION)
	@RequiresPermissions(ActdecListedTwoDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, ActdecListedTwoBean actdecListedTwoBean)
			throws Exception {
		LogUtil.startLog(BorrowController.class.toString(), ActdecListedTwoDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "上市活动二列表";

		ActdecListedTwoCustomize actdecListedTwoCustomize = new ActdecListedTwoCustomize();
		// 用户名
		actdecListedTwoCustomize.setUserNameSrch(actdecListedTwoBean.getUserNameSrch());
		// 姓名
		actdecListedTwoCustomize.setTrueNameSrch(actdecListedTwoBean.getTrueNameSrch());
		// 手机号
		actdecListedTwoCustomize.setMobileSrch(actdecListedTwoBean.getMobileSrch());
		// 领取奖励
		actdecListedTwoCustomize.setAcceptPrizeSrch(actdecListedTwoBean.getAcceptPrizeSrch());
		// 领取时间
		actdecListedTwoCustomize.setAcceptTimeStartSrch(actdecListedTwoBean.getAcceptTimeStartSrch());
		actdecListedTwoCustomize.setAcceptTimeEndSrch(actdecListedTwoBean.getAcceptTimeEndSrch());

		List<ActdecListedTwoCustomize> resultList = this.actdecListedTwoService.exportActdecListedTwoList(actdecListedTwoCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "用户名", "姓名", "手机号", "操作", "领奖时增投金额", "领奖金额", "领取时间" , "操作金额", "当前新增充值金额", "当前新增冲投金额", 
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
					ActdecListedTwoCustomize actdecListedTwoCustomizeResult = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 用户名
					else if (celLength == 1) {
						cell.setCellValue(actdecListedTwoCustomizeResult.getUserName());
					}
					// 姓名
					else if (celLength == 2) {
						cell.setCellValue(actdecListedTwoCustomizeResult.getTrueName());
					}
					// 手机号
					else if (celLength == 3) {
						cell.setCellValue(actdecListedTwoCustomizeResult.getMobile());
					}
					// 操作
					else if (celLength == 4) {
						if ("0".equals(actdecListedTwoCustomizeResult.getTrade())) {
							cell.setCellValue("领奖");
						} else if("1".equals(actdecListedTwoCustomizeResult.getTrade())) {
							cell.setCellValue("充值");
						} else if("2".equals(actdecListedTwoCustomizeResult.getTrade())) {
							cell.setCellValue("出借");
						} else if("3".equals(actdecListedTwoCustomizeResult.getTrade())) {
							cell.setCellValue("提现");
						} else {
							cell.setCellValue("");
						}
					}
					// 领奖时增投金额
					else if (celLength == 5) {
						cell.setCellValue(actdecListedTwoCustomizeResult.getInvestedAmount());
					}
					// 领奖金额
					else if (celLength == 6) {
						if (StringUtils.isBlank(actdecListedTwoCustomizeResult.getAcceptPrize())) {
							cell.setCellValue("");
						} else if (("0").equals(actdecListedTwoCustomizeResult.getAcceptPrize())){
							cell.setCellValue("");
						} else if (("20000").equals(actdecListedTwoCustomizeResult.getAcceptPrize())){
							cell.setCellValue("1%加息券*1张 ");
						} else if (("50000").equals(actdecListedTwoCustomizeResult.getAcceptPrize())){
							cell.setCellValue("2%加息券*1张 ");
						} else if (("100000").equals(actdecListedTwoCustomizeResult.getAcceptPrize())){
							cell.setCellValue("3%加息券*1张 ");
						} else if (("200000").equals(actdecListedTwoCustomizeResult.getAcceptPrize())){
							cell.setCellValue("3%加息券*2张 ");
						}
					}
					// 领取时间
					else if (celLength == 7) {
						if (0 == actdecListedTwoCustomizeResult.getAcceptTime()) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(actdecListedTwoCustomizeResult.getAcceptTime()));
						}
					}
					// 操作金额
					else if (celLength == 8) {
						cell.setCellValue(actdecListedTwoCustomizeResult.getAmount());
					}
					// 当前新增充值金额
					else if (celLength == 9) {
						cell.setCellValue(actdecListedTwoCustomizeResult.getCumulativeCharge());
					}
					// 当前新增冲投金额
					else if (celLength == 10) {
						cell.setCellValue(actdecListedTwoCustomizeResult.getCumulativeInvest());
					}
					// 创建人
					else if (celLength == 11) {
						cell.setCellValue(actdecListedTwoCustomizeResult.getCreateUser());
					}
					// 创建时间
					else if (celLength == 12) {
						if (0 == actdecListedTwoCustomizeResult.getCreateTime()) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(actdecListedTwoCustomizeResult.getCreateTime()));
						}
					}
					// 修改人
					else if (celLength == 13) {
						cell.setCellValue(actdecListedTwoCustomizeResult.getUpdateUser());
					}
					// 修改时间
					else if (celLength == 14) {
						if (0 == actdecListedTwoCustomizeResult.getUpdateTime()) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(actdecListedTwoCustomizeResult.getUpdateTime()));
						}
					}
					// 是否删除
					else if (celLength == 15) {
						cell.setCellValue(actdecListedTwoCustomizeResult.getDelFlg());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(BorrowController.class.toString(), ActdecListedTwoDefine.EXPORT_ACTION);
	}
}
