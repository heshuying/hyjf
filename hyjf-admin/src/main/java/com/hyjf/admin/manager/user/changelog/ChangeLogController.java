package com.hyjf.admin.manager.user.changelog;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.hyjf.admin.manager.user.manageruser.ManageUsersService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.AsteriskProcessUtil;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.ChangeLogCustomize;

/**
 * 用户信息修改Controller
 * @author
 */
@Controller
@RequestMapping(value = ChangeLogDefine.REQUEST_MAPPING)
public class ChangeLogController extends BaseController {

	@Autowired
	private ChangeLogService changeLogService;
    @Resource
    private ManageUsersService usersService;
	/**
	 * 列表维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ChangeLogDefine.INIT)
	@RequiresPermissions(ChangeLogDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute(ChangeLogDefine.CHANGELOG_FORM) ChangeLogBean form) {
		LogUtil.startLog(ChangeLogController.class.toString(), ChangeLogDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ChangeLogDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(ChangeLogController.class.toString(), ChangeLogDefine.INIT);
		return modelAndView;
	}

	/**
	 * 文章列表维护分页机能 页面初始化
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, ChangeLogBean form) {
	 // 用户属性
        List<ParamName> userPropertys = this.usersService.getParamNameList("USER_PROPERTY");
        modelAndView.addObject("userPropertys", userPropertys);
        // 51老用户
        List<ParamName> is51 = this.usersService.getParamNameList("IS_51");
        modelAndView.addObject("is51", is51);
		int count = this.changeLogService.countRecordTotal(form);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			form.setLimitStart(paginator.getOffset());
			form.setLimitEnd(paginator.getLimit());
			// 合规四期 手机号加密显示 mod by nxl
//			List<ChangeLogCustomize> recordList = this.changeLogService.getChangeLogList(form);
            List<ChangeLogCustomize> recordList = this.changeLogService.selectChangeLogList(form);
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(ChangeLogDefine.CHANGELOG_FORM, form);
		}
	}

	 /**
     * 导出用户信息修改列表EXCEL
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(ChangeLogDefine.EXPORT_CHANGELOG_ACTION)
    @RequiresPermissions(ChangeLogDefine.PERMISSIONS_CHANGELOG_EXPORT)
    public void exportAccountsExcel(HttpServletRequest request, HttpServletResponse response, ChangeLogBean form)
            throws Exception {
        // 表格sheet名称
        String sheetName = "操作日志";
//        List<ChangeLogCustomize> recordList = this.changeLogService.getChangeLogList(form);
        // 合规四期 手机号加密显示 mod by nxl
        List<ChangeLogCustomize> recordList = this.changeLogService.selectChangeLogList(form);


        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + ".xls";

        String[] titles = new String[] { "序号", "用户名", "姓名", "手机号", "用户角色", "用户属性", "推荐人", "51老用户", "用户状态", "修改人", "修改时间", "说明","邮箱"};
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
                    sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles,
                            (sheetName + "_第" + sheetCount + "页"));
                    rowNum = 1;
                }

                // 新建一行
                Row row = sheet.createRow(rowNum);
                // 循环数据
                for (int celLength = 0; celLength < titles.length; celLength++) {
                    ChangeLogCustomize changeLog = recordList.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);

                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    
                    // 用户名
                    else if (celLength == 1) {
                        cell.setCellValue(changeLog.getUsername());
                    }
                    // 真实姓名
                    else if (celLength == 2) {
                        cell.setCellValue(changeLog.getRealName());
                    }
                    // 手机号
                    else if (celLength == 3) {
                        cell.setCellValue(AsteriskProcessUtil.getAsteriskedValue(changeLog.getMobile(), ChangeLogDefine.PERMISSION_HIDE_SHOW));
                    }
                    // 用户角色
                    else if (celLength == 4) {
                        cell.setCellValue(changeLog.getRole()==null?"":changeLog.getRole()==1?"出借人":"借款人");
                    }
                    // 用户属性
                    else if (celLength == 5) {
                        cell.setCellValue(changeLog.getAttribute()==null?"":changeLog.getAttribute().equals("0")?"无主单":changeLog.getAttribute().equals("1")?"有主单":changeLog.getAttribute().equals("2")?"线下员工":"线上员工");
                    }
                    // 推荐人
                    else if (celLength == 6) {
                        cell.setCellValue(changeLog.getRecommendUser());
                    }
                    // 是否51老用户
                    else if (celLength == 7) {
                        cell.setCellValue(changeLog.getIs51()==null?"":changeLog.getIs51()==1?"是" : "否");
                    }
                    // 用户状态
                    else if (celLength == 8) {
                        cell.setCellValue(changeLog.getStatus()==null?"":changeLog.getStatus()==1?"启用" : "禁用");
                    }
                    // 修改人
                    else if (celLength == 9) {
                        cell.setCellValue(changeLog.getChangeUser());
                    }
                    // 修改时间
                    else if (celLength == 10) {
                        cell.setCellValue(changeLog.getChangeTime());
                    }
                    // 修改说明
                    else if (celLength == 11) { 
                        cell.setCellValue(changeLog.getRemark());
                    }
                    // 合规四期,添加导出邮箱列表, add by nxl
                    //邮箱
                    else if (celLength == 12) {
                        cell.setCellValue(changeLog.getEmail());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);

    }


}
