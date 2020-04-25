package com.hyjf.admin.manager.user.preregistcea;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.user.regist.RegistDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.customize.admin.AdminPreRegistChannelExclusiveActivityCustomize;

/**
 * 预注册渠道专属活动-控制层
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2016/06/29 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = PreRegistChannelExclusiveActivityDefine.REQUEST_MAPPING)
public class PreRegistChannelExclusiveActivityController extends BaseController {

	@Autowired
	private PreRegistChannelExclusiveActivityService preRegistChannelExclusiveActivityService;

	/**
	 * 预注册渠道专属活动画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PreRegistChannelExclusiveActivityDefine.REGIST_LIST_ACTION)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute PreRegistChannelExclusiveActivityCustomizeBean form) {
		LogUtil.startLog(PreRegistChannelExclusiveActivityController.class.toString(), PreRegistChannelExclusiveActivityDefine.REGIST_LIST_ACTION);
		ModelAndView modelAndView = new ModelAndView(PreRegistChannelExclusiveActivityDefine.REGIST_LIST_PATH);
		Map<String, Object> map = new HashMap<String, Object>();
		List<AdminPreRegistChannelExclusiveActivityCustomize> iniReRegistChannelExclusiveActivity = null;
		//初始查询条件时初始化预注册渠道专属活动
		if(StringUtils.isEmpty(form.getUserName())&&StringUtils.isEmpty(form.getMobile())&&StringUtils.isEmpty(form.getUtmSource())&&form.getPaginatorPage()==1){
		    if(StringUtils.isNotEmpty(request.getParameter("startTime"))&&StringUtils.isNotEmpty(request.getParameter("endTime"))){
		        map.put("preRegChannelExclusiveActivityTimeStart", GetDate.strYYYYMMDDHHMMSS2Timestamp2(request.getParameter("startTime")));
		        map.put("preRegChannelExclusiveActivityTimeEnd", GetDate.strYYYYMMDDHHMMSS2Timestamp2(request.getParameter("endTime")));
		        iniReRegistChannelExclusiveActivity = preRegistChannelExclusiveActivityService.iniPreRegistChannelExclusiveActivity(map);
		    }
		    if(iniReRegistChannelExclusiveActivity!=null && iniReRegistChannelExclusiveActivity.size()>0){
		        preRegistChannelExclusiveActivityService.iniUpdatePreRegistChannelExclusiveActivity(iniReRegistChannelExclusiveActivity);
		    }
		}
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(PreRegistChannelExclusiveActivityController.class.toString(), PreRegistChannelExclusiveActivityDefine.REGIST_LIST_ACTION);
		return modelAndView;
	}

	/**
	 * 创建权限维护分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, PreRegistChannelExclusiveActivityCustomizeBean form) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userName", form.getUserName());
		map.put("mobile", form.getMobile());
		map.put("referrerUserName", form.getReferrerUserName());
		map.put("utmSource", form.getUtmSource());
		map.put("reward", form.getReward());
		int recordTotal = this.preRegistChannelExclusiveActivityService.countRecordTotal(map);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			List<AdminPreRegistChannelExclusiveActivityCustomize> recordList = this.preRegistChannelExclusiveActivityService.getRecordList(map, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject("preRegistChannelExclusiveActivityListForm", form);
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
    @RequestMapping(PreRegistChannelExclusiveActivityDefine.EXPORT_REGIST_ACTION)
    public void exportExcel(@ModelAttribute PreRegistChannelExclusiveActivityCustomizeBean form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        LogUtil.startLog(RegistDefine.THIS_CLASS, RegistDefine.EXPORT_REGIST_ACTION);
        // 表格sheet名称
        String sheetName = "预注册渠道专属活动";
        // 文件名称
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        // 需要输出的结果列表
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userName", form.getUserName());
        map.put("mobile", form.getMobile());
        map.put("referrerUserName", form.getReferrerUserName());
        map.put("utmSource", form.getUtmSource());
        map.put("reward", form.getReward());
        List<AdminPreRegistChannelExclusiveActivityCustomize> recordList = this.preRegistChannelExclusiveActivityService.getRecordList(map, -1, -1);
        String[] titles = new String[] { "序号", "用户名", "手机号", "推荐人", "注册时间", "渠道", "活动期内累计出借额", "活动期内最高单笔出借额", "活动奖励"};
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
                    AdminPreRegistChannelExclusiveActivityCustomize preRegist = recordList.get(i);
                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    if (celLength == 0) {// 序号
                        cell.setCellValue(i + 1);
                    } else if (celLength == 1) {//用户名
                        cell.setCellValue(preRegist.getUserName());
                    } else if (celLength == 2) {//手机号
                        cell.setCellValue(preRegist.getMobile());
                    } else if (celLength == 3) {//推荐人
                        cell.setCellValue(preRegist.getReferrerUserName());
                    } else if (celLength == 4) {//注册时间
                        cell.setCellValue(preRegist.getRegistTime());
                    } else if (celLength == 5) {//渠道
                        cell.setCellValue(preRegist.getUtmSource());
                    } else if (celLength == 6) {//活动期内累计出借额
                        cell.setCellValue(preRegist.getTenderTotal());
                    } else if (celLength == 7) {//活动期内最高单笔出借额
                        cell.setCellValue(preRegist.getTenderSingle());
                    } else if (celLength == 8) {//活动奖励
                        cell.setCellValue(preRegist.getReward());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
        LogUtil.endLog(RegistDefine.THIS_CLASS, RegistDefine.EXPORT_REGIST_ACTION);
    }
}
