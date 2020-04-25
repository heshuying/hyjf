package com.hyjf.admin.manager.activity.nami;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.activity.activitylist.ActivityListService;
import com.hyjf.admin.manager.user.manageruser.ManageUsersService;
import com.hyjf.admin.manager.user.manageruser.UserListCustomizeBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.customize.admin.NaMiActivityUserListCustomize;
/**
 * 
 * 此处为类说明
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月12日
 * @see 下午4:58:59
 */
@Controller
@RequestMapping(value = NaMiActivityDefine.REQUEST_MAPPING)
public class NaMiActivityController extends BaseController {

	@Autowired
	private NaMiActivityService naMiActivityService;

	@Autowired
    private ActivityListService activityListService;
	@Resource
    private ManageUsersService usersService;
	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(NaMiActivityDefine.INIT)
	@RequiresPermissions(NaMiActivityDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, NaMiActivityBean form) {
		LogUtil.startLog(NaMiActivityController.class.toString(), NaMiActivityDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(NaMiActivityDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(NaMiActivityController.class.toString(), NaMiActivityDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(NaMiActivityDefine.SEARCH_ACTION)
	@RequiresPermissions(NaMiActivityDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, NaMiActivityBean form) {
		LogUtil.startLog(NaMiActivityController.class.toString(), NaMiActivityDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(NaMiActivityDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(NaMiActivityController.class.toString(), NaMiActivityDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, NaMiActivityBean form) {

	    Map<String, Object> paraMap = new HashMap<String, Object>();
	 // 部门
        String[] list = new String[] {};
        if (Validator.isNotNull(form.getCombotreeSrch())) {
            if (form.getCombotreeSrch().contains(StringPool.COMMA)) {
                list = form.getCombotreeSrch().split(StringPool.COMMA);
                form.setCombotreeListSrch(list);
            } else {
                list = new String[] { form.getCombotreeSrch() };
                form.setCombotreeListSrch(list);
            }
        }
        String userName = StringUtils.isNotEmpty(form.getUserName()) ? form.getUserName() : null;
        String mobile = StringUtils.isNotEmpty(form.getMobile()) ? form.getMobile() : null;
        String[] combotreeListSrchStr = form.getCombotreeListSrch();
        paraMap.put("userName", userName);
        paraMap.put("mobile", mobile);
        paraMap.put("combotreeListSrch", combotreeListSrchStr);
	    ActivityList activityList=activityListService.getRecord(form.getActivityId());
	    if(activityList!=null){
	        Integer timeStart=activityList.getTimeStart();
	        Integer timeEnd=activityList.getTimeEnd();
	        paraMap.put("timeStart", timeStart);
	        paraMap.put("timeEnd", timeEnd);
	    }
		Integer count = this.naMiActivityService.selectRecordCount(paraMap);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			paraMap.put("limitStart", paginator.getOffset());
			paraMap.put("limitEnd", paginator.getLimit());
			List<NaMiActivityUserListCustomize> recordList = this.naMiActivityService.selectRecordList(paraMap);
			form.setPaginator(paginator);
			for (NaMiActivityUserListCustomize naMiActivityUserListCustomize : recordList) {
			    naMiActivityUserListCustomize.setFoldRatio(naMiActivityService.getFoldRatio(naMiActivityUserListCustomize.getUserId()));
            }
			
			modelAndView.addObject("recordList", recordList);
		}

		modelAndView.addObject(NaMiActivityDefine.PRIZECODE_FORM, form);
	}
	

	
	/**
     * 数据导出
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(NaMiActivityDefine.EXPORT_ACTION)
    @RequiresPermissions(NaMiActivityDefine.PERMISSIONS_EXPORT)
    public void exportAction(HttpServletRequest request, HttpServletResponse response, NaMiActivityBean form) throws Exception {
        LogUtil.startLog(NaMiActivityController.class.toString(), NaMiActivityDefine.EXPORT_ACTION);
        // 表格sheet名称
        String sheetName = "纳米活动用户出借统计";

        Map<String, Object> paraMap = new HashMap<String, Object>();
        String[] list = new String[] {};
        if (Validator.isNotNull(form.getCombotreeSrch())) {
            if (form.getCombotreeSrch().contains(StringPool.COMMA)) {
                list = form.getCombotreeSrch().split(StringPool.COMMA);
                form.setCombotreeListSrch(list);
            } else {
                list = new String[] { form.getCombotreeSrch() };
                form.setCombotreeListSrch(list);
            }
        }
        String userName = StringUtils.isNotEmpty(form.getUserName()) ? form.getUserName() : null;
        String mobile = StringUtils.isNotEmpty(form.getMobile()) ? form.getMobile() : null;
        String[] combotreeListSrchStr = form.getCombotreeListSrch();
        paraMap.put("userName", userName);
        paraMap.put("mobile", mobile);
        paraMap.put("combotreeListSrch", combotreeListSrchStr);
        ActivityList activityList=activityListService.getRecord(form.getActivityId());
        if(activityList!=null){
            Integer timeStart=activityList.getTimeStart();
            Integer timeEnd=activityList.getTimeEnd();
            paraMap.put("timeStart", timeStart);
            paraMap.put("timeEnd", timeEnd);
        }
        List<NaMiActivityUserListCustomize> resultList = this.naMiActivityService.selectRecordList(paraMap);

        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        String[] titles = new String[] { "序号", "分公司", "分部", "团队", "用户名", "手机号", "推荐人", "折合总量"};
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
                    NaMiActivityUserListCustomize record = resultList.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);

                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    else if (celLength == 1) {
                        cell.setCellValue(record.getRegionName());
                    }
                    else if (celLength == 2) {
                        cell.setCellValue(record.getBranchName());
                    }
                    else if (celLength == 3) {
                        cell.setCellValue(record.getDepartmentName());
                    }
                    else if (celLength == 4) {
                        cell.setCellValue(record.getUserName());
                    }
                    else if (celLength == 5) {
                        cell.setCellValue(record.getMobile());
                    }
                    else if (celLength == 6) {
                        cell.setCellValue(record.getRecommendName());
                    }
                    else if (celLength == 7) {
                        cell.setCellValue(naMiActivityService.getFoldRatio(record.getUserId()));
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
        LogUtil.endLog(NaMiActivityController.class.toString(), NaMiActivityDefine.EXPORT_ACTION);
    }
    
    /**
     * 取得部门信息
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping("getCrmDepartmentList")
    @ResponseBody
    public String getCrmDepartmentListAction(@RequestBody UserListCustomizeBean form) {
        // 部门
        String[] list = new String[] {};
        if (Validator.isNotNull(form.getIds())) {
            if (form.getIds().contains(StringPool.COMMA)) {
                list = form.getIds().split(StringPool.COMMA);
            } else {
                list = new String[] { form.getIds() };
            }
        }

        JSONArray ja = this.usersService.getCrmDepartmentList(list);
        if (ja != null) {
            return ja.toString();
        }

        return StringUtils.EMPTY;
    }
}
