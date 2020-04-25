package com.hyjf.admin.manager.user.preregist;

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
import com.hyjf.admin.manager.user.regist.RegistDefine;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.admin.AdminPreRegistListCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = PreRegistDefine.REQUEST_MAPPING)
public class PreRegistController extends BaseController {

	@Autowired
	private PreRegistService preRegistService;

	/**
	 * 权限维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PreRegistDefine.REGIST_LIST_ACTION)
	@RequiresPermissions(PreRegistDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute PreRegistListCustomizeBean form) {
		LogUtil.startLog(PreRegistController.class.toString(), PreRegistDefine.REGIST_LIST_ACTION);
		ModelAndView modelAndView = new ModelAndView(PreRegistDefine.REGIST_LIST_PATH);
		try {
			String userId = ShiroUtil.getLoginUserId();
			modelAndView.addObject("userId", userId);
		} catch (Exception e) {
			modelAndView.addObject("userId", "0");
		}
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(PreRegistController.class.toString(), PreRegistDefine.REGIST_LIST_ACTION);
		return modelAndView;
	}

	/**
	 * 创建权限维护分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, PreRegistListCustomizeBean form) {

		// 注册平台
		List<ParamName> registPlat = this.preRegistService.getParamNameList("CLIENT");
		modelAndView.addObject("registPlat", registPlat);
		Map<String, Object> registUser = new HashMap<String, Object>();
		registUser.put("mobile", form.getMobile());
		registUser.put("referrer", form.getReferrer());
		registUser.put("source", form.getSource());
		registUser.put("preRegistTime", form.getPreRegistTime());
		registUser.put("registFlag", form.getRegistFlag());
		registUser.put("registTime", form.getRegistTime());
		registUser.put("platformId", form.getPlatformId());
		registUser.put("platformName", form.getPlatformName());
		registUser.put("remark", form.getRemark());
		registUser.put("createTime", form.getCreateTime());
		registUser.put("updateTime", form.getUpdateTime());
		registUser.put("updateBy", form.getUpdateBy());
		registUser.put("preRegTimeStart", StringUtils.isNotBlank(form.getPreRegTimeStart())?form.getPreRegTimeStart():null);
		registUser.put("preRegTimeEnd", StringUtils.isNotBlank(form.getPreRegTimeEnd())?form.getPreRegTimeEnd():null);

		int recordTotal = this.preRegistService.countRecordTotal(registUser);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			List<AdminPreRegistListCustomize> recordList = this.preRegistService.getRecordList(registUser, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject("preregistListForm", form);
		}
	}

	/**
	 * 预注册用户编辑页面跳转
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PreRegistDefine.REGIST_UPDATE_ACTION)
	public ModelAndView updatePreRegistList(HttpServletRequest request, PreRegistListCustomizeBean form) {
		LogUtil.startLog(PreRegistController.class.toString(), PreRegistDefine.REGIST_UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(PreRegistDefine.REGIST_UPDATE_PATH);
		if (!StringUtils.isEmpty(form.getId())) {
			AdminPreRegistListCustomize preRegist = this.preRegistService.getPreRegist(Integer.parseInt(form.getId()));
			modelAndView.addObject("preRegist", preRegist);
		} else {
			modelAndView.addObject("preRegist", null);
		}
		LogUtil.endLog(PreRegistController.class.toString(), PreRegistDefine.REGIST_UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 预注册用户编辑保存
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PreRegistDefine.REGIST_SAVE_ACTION)
	public ModelAndView savePreRegistList(HttpServletRequest request, @ModelAttribute PreRegistListCustomizeBean form) {

		LogUtil.startLog(PreRegistController.class.toString(), PreRegistDefine.REGIST_SAVE_ACTION);
		ModelAndView modelAndView = new ModelAndView(PreRegistDefine.REGIST_UPDATE_PATH);
		try {
			if (!StringUtils.isEmpty(form.getId())) {
				Map<String, Object> resultMap = this.preRegistService.savePreRegist(form);
				if ((Boolean) resultMap.get("success")) {
					modelAndView.addObject("success", "success");
					modelAndView.addObject("preRegist", resultMap.get("preRegist"));
				} else {
					modelAndView.addObject("success", "failed");
					modelAndView.addObject("preRegist", form);
					modelAndView.addObject("message", resultMap.get("msg"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		LogUtil.endLog(PreRegistController.class.toString(), PreRegistDefine.REGIST_SAVE_ACTION);
		return modelAndView;
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
	@RequestMapping(PreRegistDefine.EXPORT_REGIST_ACTION)
	@RequiresPermissions(PreRegistDefine.PERMISSIONS_EXPORT)
	public void exportExcel(@ModelAttribute PreRegistListCustomizeBean form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LogUtil.startLog(RegistDefine.THIS_CLASS, RegistDefine.EXPORT_REGIST_ACTION);
		// 表格sheet名称
		String sheetName = "预注册用户";
		// 文件名称
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		// 需要输出的结果列表
		Map<String, Object> registUser = new HashMap<String, Object>();
		registUser.put("mobile", form.getMobile());
		registUser.put("referrer", form.getReferrer());
		registUser.put("source", form.getSource());
		registUser.put("preRegistTime", form.getPreRegistTime());
		registUser.put("registFlag", form.getRegistFlag());
		registUser.put("registTime", form.getRegistTime());
		registUser.put("platformId", form.getPlatformId());
		registUser.put("platformName", form.getPlatformName());
		registUser.put("remark", form.getRemark());
		registUser.put("createTime", form.getCreateTime());
		registUser.put("updateTime", form.getUpdateTime());
		registUser.put("updateBy", form.getUpdateBy());
		registUser.put("preRegTimeStart", form.getPreRegTimeStart());
		registUser.put("preRegTimeEnd", form.getPreRegTimeEnd());
		List<AdminPreRegistListCustomize> recordList = this.preRegistService.getRecordList(registUser, -1, -1);
		String[] titles = new String[] { "序号", "手机号", "推荐人", "渠道", "预注册时间", "是否已经注册", "操作终端", "备注" };
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
					AdminPreRegistListCustomize preRegist = recordList.get(i);
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {// 序号
						cell.setCellValue(i + 1);
					} else if (celLength == 1) {// 手机号
						cell.setCellValue(preRegist.getMobile());
					} else if (celLength == 2) {// 推荐人
						cell.setCellValue(preRegist.getReferrer());
					} else if (celLength == 3) {// 渠道
						cell.setCellValue(preRegist.getSource());
					} else if (celLength == 4) {// 预注册时间
						cell.setCellValue(preRegist.getPreRegistTime());
					} else if (celLength == 5) {// 是否已经注册
						if (preRegist.getRegistFlag().equals("1")) {
							cell.setCellValue("是");
						} else {
							cell.setCellValue("否");
						}
					} else if (celLength == 6) {// 操作终端
						cell.setCellValue(preRegist.getPlatformName());
					} else if (celLength == 7) {// 备注
						cell.setCellValue(preRegist.getRemark());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(RegistDefine.THIS_CLASS, RegistDefine.EXPORT_REGIST_ACTION);
	}
}
