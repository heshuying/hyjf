package com.hyjf.admin.promotion.reconciliation;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.AdminUtmReadPermissions;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.customize.admin.PcChannelReconciliationCustomize;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * app渠道对账
 * 
 * @author Michael
 */
@Controller
@RequestMapping(value = ChannelReconciliationDefine.REQUEST_MAPPING)
public class ChannelReconciliationController extends BaseController {

	@Autowired
	private ChannelReconciliationService channelReconciliationService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ChannelReconciliationDefine.INIT)
	@RequiresPermissions(ChannelReconciliationDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute(ChannelReconciliationDefine.FORM) ChannelReconciliationBean form) {
		LogUtil.startLog(ChannelReconciliationController.class.toString(), ChannelReconciliationDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ChannelReconciliationDefine.LIST_PATH);

		// 获取登录用户的userId
		Integer userId = Integer.parseInt(ShiroUtil.getLoginUserId());
		// 根据用户Id查询渠道账号管理
		AdminUtmReadPermissions adminUtmReadPermissions = this.channelReconciliationService.selectAdminUtmReadPermissions(userId);
		if (adminUtmReadPermissions != null) {
			form.setUtmIds(adminUtmReadPermissions.getUtmIds());// 封装到页面
		}

		// 默认前后一个月查询时间
		form.setTimeStartSrch(GetDate.date2Str(GetDate.getTodayBeforeOrAfter(-30), new SimpleDateFormat("yyyy-MM-dd")));
		form.setTimeEndSrch(GetDate.date2Str(new Date(), new SimpleDateFormat("yyyy-MM-dd")));

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(ChannelReconciliationController.class.toString(), ChannelReconciliationDefine.INIT);
		return modelAndView;
	}
	
	/**
     * 画面初始化_计划
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ChannelReconciliationDefine.HJH_INIT)
    @RequiresPermissions(ChannelReconciliationDefine.PERMISSIONS_VIEW)
    public ModelAndView hjh_init(HttpServletRequest request, @ModelAttribute(ChannelReconciliationDefine.FORM) ChannelReconciliationBean form) {
        LogUtil.startLog(ChannelReconciliationController.class.toString(), ChannelReconciliationDefine.HJH_INIT);
        ModelAndView modelAndView = new ModelAndView(ChannelReconciliationDefine.HJH_LIST_PATH);

        // 获取登录用户的userId
        Integer userId = Integer.parseInt(ShiroUtil.getLoginUserId());
        // 根据用户Id查询渠道账号管理
        AdminUtmReadPermissions adminUtmReadPermissions = this.channelReconciliationService.selectAdminUtmReadPermissions(userId);
        if (adminUtmReadPermissions != null) {
            form.setUtmIds(adminUtmReadPermissions.getUtmIds());// 封装到页面
        }
		// 默认前后一个月查询时间
		form.setTimeStartSrch(GetDate.date2Str(GetDate.getTodayBeforeOrAfter(-30), new SimpleDateFormat("yyyy-MM-dd")));
		form.setTimeEndSrch(GetDate.date2Str(new Date(), new SimpleDateFormat("yyyy-MM-dd")));

        // 创建分页
        this.createPageHjh(request, modelAndView, form);
        LogUtil.endLog(ChannelReconciliationController.class.toString(), ChannelReconciliationDefine.HJH_INIT);
        return modelAndView;
    }

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ChannelReconciliationDefine.SEARCH_ACTION)
	@RequiresPermissions(ChannelReconciliationDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, ChannelReconciliationBean form) {
		LogUtil.startLog(ChannelReconciliationController.class.toString(), ChannelReconciliationDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(ChannelReconciliationDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(ChannelReconciliationController.class.toString(), ChannelReconciliationDefine.SEARCH_ACTION);
		return modelAndView;
	}


	/**
	 * 画面初始化
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ChannelReconciliationDefine.HJH_SEARCH_ACTION)
	@RequiresPermissions(ChannelReconciliationDefine.PERMISSIONS_SEARCH)
	public ModelAndView search_hjh(HttpServletRequest request, ChannelReconciliationBean form) {
		LogUtil.startLog(ChannelReconciliationController.class.toString(), ChannelReconciliationDefine.HJH_SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(ChannelReconciliationDefine.HJH_LIST_PATH);

		// 创建分页
		this.createPageHjh(request, modelAndView, form);
		LogUtil.endLog(ChannelReconciliationController.class.toString(), ChannelReconciliationDefine.HJH_SEARCH_ACTION);
		return modelAndView;
	}
	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, ChannelReconciliationBean form) {

		PcChannelReconciliationCustomize pcChannelReconciliationCustomize = new PcChannelReconciliationCustomize();
		if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
			pcChannelReconciliationCustomize.setTimeStartSrch(GetDate.getDayStart(form.getTimeStartSrch()));
		}
		if (StringUtils.isNotEmpty(form.getUtmIds())) {
			pcChannelReconciliationCustomize.setUtmIds(form.getUtmIds());;
		}
		if (StringUtils.isNotEmpty(form.getTimeEndSrch())) {
			pcChannelReconciliationCustomize.setTimeEndSrch(GetDate.getDayEnd(form.getTimeEndSrch()));
		}
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			pcChannelReconciliationCustomize.setUserName(form.getUserNameSrch());
		}
		if (StringUtils.isNotEmpty(form.getOrderCodeSrch())) {
			pcChannelReconciliationCustomize.setOrderCode(form.getOrderCodeSrch());
		}
		if (StringUtils.isNotEmpty(form.getBorrowNidSrch())) {
			pcChannelReconciliationCustomize.setBorrowNid(form.getBorrowNidSrch());
		}
		if (form.getIsFirst() != null) {
			pcChannelReconciliationCustomize.setIsFirst(form.getIsFirst());
		}
		if (StringUtils.isNotEmpty(form.getRegStartTime())) {
			pcChannelReconciliationCustomize.setRegStartTime(GetDate.getDayStart(form.getRegStartTime()));
		}
		if (StringUtils.isNotEmpty(form.getRegEndTime())) {
			pcChannelReconciliationCustomize.setRegEndTime(GetDate.getDayEnd(form.getRegEndTime()));
		}
		// 渠道
		String[] utmIds = new String[] {};
		if (Validator.isNotNull(form.getUtmIds())) {
			if (form.getUtmIds().contains(StringPool.COMMA)) {
				utmIds = form.getUtmIds().split(StringPool.COMMA);
				pcChannelReconciliationCustomize.setUtmIdsSrch(utmIds);
			} else {
				utmIds = new String[] { form.getUtmIds() };
				pcChannelReconciliationCustomize.setUtmIdsSrch(utmIds);
			}
		}
		//获取pc渠道
		UtmPlat utmPlat=new UtmPlat();
		utmPlat.setSourceType(0);
		List<UtmPlat> utmtTypeList=this.channelReconciliationService.utmPlatListPcGet(utmPlat);
		form.setUtmtTypeList(utmtTypeList);
		modelAndView.addObject("utmtTypeList", utmtTypeList);

		Integer count = this.channelReconciliationService.countPcChannelReconciliationRecord(pcChannelReconciliationCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			pcChannelReconciliationCustomize.setLimitStart(paginator.getOffset());
			pcChannelReconciliationCustomize.setLimitEnd(paginator.getLimit());
			List<PcChannelReconciliationCustomize> recordList = this.channelReconciliationService.selectPcChannelReconciliationRecord(pcChannelReconciliationCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(ChannelReconciliationDefine.FORM, form);
	}
	
	/**
     * 创建分页机能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPageHjh(HttpServletRequest request, ModelAndView modelAndView, ChannelReconciliationBean form) {

        PcChannelReconciliationCustomize pcChannelReconciliationCustomize = new PcChannelReconciliationCustomize();
		if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
			pcChannelReconciliationCustomize.setTimeStartSrch(GetDate.getDayStart(form.getTimeStartSrch()));
		}
		if (StringUtils.isNotEmpty(form.getUtmIds())) {
			pcChannelReconciliationCustomize.setUtmIds(form.getUtmIds());;
		}
		if (StringUtils.isNotEmpty(form.getTimeEndSrch())) {
			pcChannelReconciliationCustomize.setTimeEndSrch(GetDate.getDayEnd(form.getTimeEndSrch()));
		}
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			pcChannelReconciliationCustomize.setUserName(form.getUserNameSrch());
		}
		if (StringUtils.isNotEmpty(form.getOrderCodeSrch())) {
			pcChannelReconciliationCustomize.setOrderCode(form.getOrderCodeSrch());
		}
		if (StringUtils.isNotEmpty(form.getBorrowNidSrch())) {
			pcChannelReconciliationCustomize.setBorrowNid(form.getBorrowNidSrch());
		}
		if (form.getIsFirst() != null) {
			pcChannelReconciliationCustomize.setIsFirst(form.getIsFirst());
		}
		if (StringUtils.isNotEmpty(form.getRegStartTime())) {
			pcChannelReconciliationCustomize.setRegStartTime(GetDate.getDayStart(form.getRegStartTime()));
		}
		if (StringUtils.isNotEmpty(form.getRegEndTime())) {
			pcChannelReconciliationCustomize.setRegEndTime(GetDate.getDayEnd(form.getRegEndTime()));
		}
        // 渠道
        String[] utmIds = new String[] {};
        if (Validator.isNotNull(form.getUtmIds())) {
            if (form.getUtmIds().contains(StringPool.COMMA)) {
                utmIds = form.getUtmIds().split(StringPool.COMMA);
                pcChannelReconciliationCustomize.setUtmIdsSrch(utmIds);
            } else {
                utmIds = new String[] { form.getUtmIds() };
                pcChannelReconciliationCustomize.setUtmIdsSrch(utmIds);
            }
        }
        //获取pc渠道
        UtmPlat utmPlat=new UtmPlat();
        utmPlat.setSourceType(0);
        List<UtmPlat> utmtTypeList=this.channelReconciliationService.utmPlatListPcGet(utmPlat);
        form.setUtmtTypeList(utmtTypeList);
        modelAndView.addObject("utmtTypeList", utmtTypeList);

        Integer count = this.channelReconciliationService.countPcChannelReconciliationRecordHjh(pcChannelReconciliationCustomize);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            pcChannelReconciliationCustomize.setLimitStart(paginator.getOffset());
            pcChannelReconciliationCustomize.setLimitEnd(paginator.getLimit());
            List<PcChannelReconciliationCustomize> recordList = this.channelReconciliationService.selectPcChannelReconciliationRecordHjh(pcChannelReconciliationCustomize);
            form.setPaginator(paginator);
            modelAndView.addObject("recordList", recordList);
        }
        modelAndView.addObject(ChannelReconciliationDefine.HJH_FORM, form);
    }

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param response
	 * @param form
	 */
	@RequestMapping(ChannelReconciliationDefine.EXPORT_ACTION)
	@RequiresPermissions(ChannelReconciliationDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, ChannelReconciliationBean form) throws Exception {
		LogUtil.startLog(ChannelReconciliationController.class.toString(), ChannelReconciliationDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "PC渠道对账-散标";

		PcChannelReconciliationCustomize pcChannelReconciliationCustomize = new PcChannelReconciliationCustomize();

		if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
			pcChannelReconciliationCustomize.setTimeStartSrch(GetDate.getDayStart(form.getTimeStartSrch()));
		}
		if (StringUtils.isNotEmpty(form.getTimeEndSrch())) {
			pcChannelReconciliationCustomize.setTimeEndSrch(GetDate.getDayEnd(form.getTimeEndSrch()));
		}
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			pcChannelReconciliationCustomize.setUserName(form.getUserNameSrch());
		}
		if (StringUtils.isNotEmpty(form.getOrderCodeSrch())) {
			pcChannelReconciliationCustomize.setOrderCode(form.getOrderCodeSrch());
		}
		if (StringUtils.isNotEmpty(form.getBorrowNidSrch())) {
			pcChannelReconciliationCustomize.setBorrowNid(form.getBorrowNidSrch());
		}
		if (form.getIsFirst() != null) {
			pcChannelReconciliationCustomize.setIsFirst(form.getIsFirst());
		}
		if (StringUtils.isNotEmpty(form.getRegStartTime())) {
			pcChannelReconciliationCustomize.setRegStartTime(GetDate.getDayStart(form.getRegStartTime()));
		}
		if (StringUtils.isNotEmpty(form.getRegEndTime())) {
			pcChannelReconciliationCustomize.setRegEndTime(GetDate.getDayEnd(form.getRegEndTime()));
		}
		if (StringUtils.isNotEmpty(form.getUtmIds())) {
			String[] utmIds = new String[] {};
			if (form.getUtmIds().contains(StringPool.COMMA)) {
				utmIds = form.getUtmIds().split(StringPool.COMMA);
				pcChannelReconciliationCustomize.setUtmIdsSrch(utmIds);
			} else {
				utmIds = new String[] { form.getUtmIds() };
				pcChannelReconciliationCustomize.setUtmIdsSrch(utmIds);
			}
		}
		List<PcChannelReconciliationCustomize> recordList = this.channelReconciliationService.selectPcChannelReconciliationRecord(pcChannelReconciliationCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "用户名", "渠道","注册时间","出借订单", "项目编号", "标的期限", "授权服务金额","是否首投", "出借时间" };
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
					PcChannelReconciliationCustomize record = recordList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 用户名
					else if (celLength == 1) {
						cell.setCellValue(record.getUserName()==null?"":record.getUserName());
					}
					// 渠道
					else if (celLength == 2) {
						cell.setCellValue(record.getUtmName()==null?"":record.getUtmName());
					}
					// 注册时间
					else if (celLength == 3) {
						cell.setCellValue(record.getRegTime()==null?"":GetDate.timestamptoStrYYYYMMDDHHMM(record.getRegTime()));
					}
					// 出借订单
					else if (celLength == 4) {
						cell.setCellValue(record.getOrderCode()==null?"":record.getOrderCode());
					}
					// 项目编号
					else if (celLength == 5) {
						cell.setCellValue(record.getBorrowNid()==null?"":record.getBorrowNid());
					}
					// 标的期限
					else if (celLength == 6) {
						cell.setCellValue(record.getBorrowPeriod()==null?"":record.getBorrowPeriod());
					}
					// 出借金额
					else if (celLength == 7) {
						cell.setCellValue(record.getInvestAmount()==null?"":record.getInvestAmount());
					}
					// 是否首投
					else if (celLength == 8) {
						if(record.getIsFirst() != null&&record.getIsFirst().intValue()==1){
							cell.setCellValue("是");
						}else{
							cell.setCellValue("否");
						}
					}
					// 出借时间
					else if (celLength == 9) {
						cell.setCellValue(record.getInvestTime()==null?"":GetDate.timestamptoStrYYYYMMDDHHMM(String.valueOf(record.getInvestTime())));
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(ChannelReconciliationController.class.toString(), ChannelReconciliationDefine.EXPORT_ACTION);
	}




	/**
	 * 导出功能
	 *
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(ChannelReconciliationDefine.HJH_EXPORT_ACTION)
	@RequiresPermissions(ChannelReconciliationDefine.PERMISSIONS_EXPORT)
	public void exportHjhAction(HttpServletRequest request, HttpServletResponse response, ChannelReconciliationBean form) throws Exception {
		LogUtil.startLog(ChannelReconciliationController.class.toString(), ChannelReconciliationDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "PC渠道对账-智投服务";

		PcChannelReconciliationCustomize pcChannelReconciliationCustomize = new PcChannelReconciliationCustomize();

		if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
			pcChannelReconciliationCustomize.setTimeStartSrch(GetDate.getDayStart(form.getTimeStartSrch()));
		}
		if (StringUtils.isNotEmpty(form.getTimeEndSrch())) {
			pcChannelReconciliationCustomize.setTimeEndSrch(GetDate.getDayEnd(form.getTimeEndSrch()));
		}
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			pcChannelReconciliationCustomize.setUserName(form.getUserNameSrch());
		}
		if (StringUtils.isNotEmpty(form.getOrderCodeSrch())) {
			pcChannelReconciliationCustomize.setOrderCode(form.getOrderCodeSrch());
		}
		if (StringUtils.isNotEmpty(form.getBorrowNidSrch())) {
			pcChannelReconciliationCustomize.setBorrowNid(form.getBorrowNidSrch());
		}
		if (form.getIsFirst() != null) {
			pcChannelReconciliationCustomize.setIsFirst(form.getIsFirst());
		}
		if (StringUtils.isNotEmpty(form.getRegStartTime())) {
			pcChannelReconciliationCustomize.setRegStartTime(GetDate.getDayStart(form.getRegStartTime()));
		}
		if (StringUtils.isNotEmpty(form.getRegEndTime())) {
			pcChannelReconciliationCustomize.setRegEndTime(GetDate.getDayEnd(form.getRegEndTime()));
		}
		if (StringUtils.isNotEmpty(form.getUtmIds())) {
			String[] utmIds = new String[] {};
			if (form.getUtmIds().contains(StringPool.COMMA)) {
				utmIds = form.getUtmIds().split(StringPool.COMMA);
				pcChannelReconciliationCustomize.setUtmIdsSrch(utmIds);
			} else {
				utmIds = new String[] { form.getUtmIds() };
				pcChannelReconciliationCustomize.setUtmIdsSrch(utmIds);
			}
		}
		List<PcChannelReconciliationCustomize> recordList = this.channelReconciliationService.selectPcChannelReconciliationRecordHjh(pcChannelReconciliationCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "用户名", "渠道","注册时间", "智投订单号", "智投编号", "服务回报期限", "授权服务金额","是否首投", "出借时间" };
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
					PcChannelReconciliationCustomize record = recordList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 用户名
					else if (celLength == 1) {
						cell.setCellValue(record.getUserName()==null?"":record.getUserName());
					}
					// 渠道
					else if (celLength == 2) {
						cell.setCellValue(record.getUtmName()==null?"":record.getUtmName());
					}
					// 注册时间
					else if (celLength == 3) {
						cell.setCellValue(record.getRegTime()==null?"":GetDate.timestamptoStrYYYYMMDDHHMM(record.getRegTime()));
					}
					// 出借订单
					else if (celLength == 4) {
						cell.setCellValue(record.getOrderCode()==null?"":record.getOrderCode());
					}
					// 项目编号
					else if (celLength == 5) {
						cell.setCellValue(record.getBorrowNid()==null?"":record.getBorrowNid());
					}
					// 标的期限
					else if (celLength == 6) {
						cell.setCellValue(record.getBorrowPeriod()==null?"":record.getBorrowPeriod());
					}
					// 出借金额
					else if (celLength == 7) {
						cell.setCellValue(record.getInvestAmount()==null?"":record.getInvestAmount());
					}
					// 是否首投
					else if (celLength == 8) {
						if(record.getIsFirst() != null&&record.getIsFirst().intValue()==1){
							cell.setCellValue("是");
						}else{
							cell.setCellValue("否");
						}
					}
					// 出借时间
					else if (celLength == 9) {
						cell.setCellValue(record.getInvestTime()==null?"":GetDate.timestamptoStrYYYYMMDDHHMM(String.valueOf(record.getInvestTime())));
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(ChannelReconciliationController.class.toString(), ChannelReconciliationDefine.EXPORT_ACTION);
	}
}
