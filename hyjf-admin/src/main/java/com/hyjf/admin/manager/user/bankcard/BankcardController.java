package com.hyjf.admin.manager.user.bankcard;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.AsteriskProcessUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.admin.AdminBankcardListCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = BankcardDefine.REQUEST_MAPPING)
public class BankcardController extends BaseController {

	@Autowired
	private BankcardService bankcardService;

	/**
	 * 权限维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BankcardDefine.BANKCARD_LIST_ACTION)
	@RequiresPermissions(BankcardDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(BankcardDefine.BANKCARD_LIST_FORM) BankcardListCustomizeBean form) {
		LogUtil.startLog(BankcardDefine.THIS_CLASS, BankcardDefine.BANKCARD_LIST_ACTION);
		ModelAndView modelAndView = new ModelAndView(BankcardDefine.BANK_CARD_LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BankcardDefine.THIS_CLASS, BankcardDefine.BANKCARD_LIST_ACTION);
		return modelAndView;
	}

	/**
	 * 创建权限维护分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BankcardListCustomizeBean form) {
		// 所属银行
		List<BankConfig> banks = this.bankcardService.getBankcardList();
		modelAndView.addObject("banks", banks);
		// 银行卡属性
		List<ParamName> bankcardProperty = this.bankcardService.getParamNameList("BANKCARD_PROPERTY");
		modelAndView.addObject("bankcardProperty", bankcardProperty);

		// 是否默认
		List<ParamName> bankcardType = this.bankcardService.getParamNameList("BANKCARD_TYPE");
		modelAndView.addObject("bankcardType", bankcardType);
		// 封装查询条件
		Map<String, Object> bankCardUser = new HashMap<String, Object>();
		bankCardUser.put("userName", form.getUserName());
		bankCardUser.put("bank", form.getBank());
		bankCardUser.put("account", form.getAccount());
		bankCardUser.put("cardProperty", form.getCardProperty());
		bankCardUser.put("cardType", form.getCardType());
		bankCardUser.put("addTimeStart", StringUtils.isNotBlank(form.getAddTimeStart())?form.getAddTimeStart():null);
		bankCardUser.put("addTimeEnd", StringUtils.isNotBlank(form.getAddTimeEnd())?form.getAddTimeEnd():null);
		int recordTotal = this.bankcardService.countRecordTotal(bankCardUser);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			List<AdminBankcardListCustomize> recordList = this.bankcardService.getRecordList(bankCardUser, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(BankcardDefine.BANKCARD_LIST_FORM, form);
		}
	}

	/**
	 * 根据业务需求导出相应的表格 此处暂时为可用情况 缺陷： 1.无法指定相应的列的顺序， 2.无法配置，excel文件名，excel sheet名称
	 * 3.目前只能导出一个sheet 4.列的宽度的自适应，中文存在一定问题
	 * 5.根据导出的业务需求最好可以在导出的时候输入起止页码，因为在大数据量的情况下容易造成卡顿
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(BankcardDefine.EXPORT_BANKCARD_ACTION)
	@RequiresPermissions(BankcardDefine.PERMISSIONS_EXPORT)
	public void exportExcel(@ModelAttribute BankcardListCustomizeBean form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LogUtil.startLog(BankcardDefine.THIS_CLASS, BankcardDefine.EXPORT_BANKCARD_ACTION);
		// 表格sheet名称
		String sheetName = "银行卡管理";
		// 文件名称
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
		// 需要输出的结果列表

		// 封装查询条件
		Map<String, Object> bankCardUser = new HashMap<String, Object>();
		bankCardUser.put("userName", form.getUserName());
		bankCardUser.put("bank", form.getBank());
		bankCardUser.put("account", form.getAccount());
		bankCardUser.put("cardProperty", form.getCardProperty());
		bankCardUser.put("cardType", form.getCardType());
		bankCardUser.put("addTimeStart", form.getAddTimeStart());
		bankCardUser.put("addTimeEnd", form.getAddTimeEnd());

		List<AdminBankcardListCustomize> recordList = this.bankcardService.getRecordList(bankCardUser, -1, -1);
		String[] titles = new String[] { "序号", "用户名", "银行账号", "所属银行", "是否默认", "银行卡属性", "添加时间" };
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
					AdminBankcardListCustomize user = recordList.get(i);
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {// 序号
						cell.setCellValue(i + 1);
					} else if (celLength == 1) {// 用户名
						cell.setCellValue(user.getUserName());
					} else if (celLength == 2) {// 银行账号
						cell.setCellValue(AsteriskProcessUtil.getAsteriskedValue(user.getAccount(), BankcardDefine.PERMISSION_HIDE_SHOW));
					} else if (celLength == 3) {// 所属银行
						cell.setCellValue(user.getBank());
					} else if (celLength == 4) {// 是否默认
						cell.setCellValue(user.getCardType());
					} else if (celLength == 5) {// 银行卡属性
						cell.setCellValue(user.getCardProperty());
					} else if (celLength == 6) {// 添加时间
						cell.setCellValue(user.getAddTime());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(BankcardDefine.THIS_CLASS, BankcardDefine.EXPORT_BANKCARD_ACTION);
	}
	
}
