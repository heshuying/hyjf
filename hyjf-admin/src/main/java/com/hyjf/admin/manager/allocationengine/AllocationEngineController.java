package com.hyjf.admin.manager.allocationengine;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.HjhAllocationEngine;
import com.hyjf.mybatis.model.auto.HjhLabel;
import com.hyjf.mybatis.model.auto.HjhRegion;

import net.sf.ehcache.pool.impl.FromLargestCacheOnDiskPoolEvictor;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 标的分配规则引擎控制器  
 * 
 * @author 
 *
 */
@Controller
@RequestMapping(value = AllocationEngineDefine.REQUEST_MAPPING)
public class AllocationEngineController extends BaseController {
	
	@Autowired
	private AllocationEngineService allocationEngineService;
	
	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AllocationEngineDefine.INIT)
	@RequiresPermissions(AllocationEngineDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request,@ModelAttribute(AllocationEngineDefine.ALLOCATIONENGINE_FORM) AllocationEngineBean form) {
		LogUtil.startLog(AllocationEngineController.class.toString(), AllocationEngineDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(AllocationEngineDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(AllocationEngineController.class.toString(), AllocationEngineDefine.INIT);
		return modelAndView;
	}
	
	/**
	 * 根据条件查询所需要数据
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AllocationEngineDefine.SEARCH_ACTION)
	@RequiresPermissions(AllocationEngineDefine.PERMISSIONS_SEARCH)
	public ModelAndView selectContentLinks(HttpServletRequest request,@ModelAttribute(AllocationEngineDefine.ALLOCATIONENGINE_FORM) AllocationEngineBean form) {
		LogUtil.startLog(AllocationEngineController.class.toString(), AllocationEngineDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(AllocationEngineDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(AllocationEngineController.class.toString(), AllocationEngineDefine.SEARCH_ACTION);
		return modelAndView;
	}
	
	/**
	 * 分页
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, AllocationEngineBean form) {
		List<HjhRegion> recordList = this.allocationEngineService.getRecordList(form, -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = allocationEngineService.getRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(AllocationEngineDefine.ALLOCATIONENGINE_FORM, form);
		}
	}
	
	
	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AllocationEngineDefine.INFO_ACTION)
	@RequiresPermissions(value = { AllocationEngineDefine.PERMISSIONS_INFO, AllocationEngineDefine.PERMISSIONS_ADD,AllocationEngineDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr,@ModelAttribute(AllocationEngineDefine.ALLOCATIONENGINE_FORM) AllocationEngineBean form) {
		LogUtil.startLog(AllocationEngineController.class.toString(), AllocationEngineDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(AllocationEngineDefine.INFO_PATH);
		if (StringUtils.isNotEmpty(form.getId())) {
			Integer id = Integer.valueOf(form.getId());
			HjhRegion record = this.allocationEngineService.getRecord(id);
			modelAndView.addObject(AllocationEngineDefine.ALLOCATIONENGINE_FORM, record);
		}
		LogUtil.endLog(AllocationEngineController.class.toString(), AllocationEngineDefine.INIT);
		return modelAndView;
	}
	
	/**
	 * 添加计划配置
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = AllocationEngineDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(AllocationEngineDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, AllocationEngineBean form) {
		LogUtil.startLog(AllocationEngineController.class.toString(), AllocationEngineDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(AllocationEngineDefine.INFO_PATH);
		HjhRegion newForm = new HjhRegion();
		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) { 
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		//完成校验后插表
		//1.计划编号
		newForm.setPlanNid(form.getPlanNidSrch());
		//2.计划名称
		String planName = this.allocationEngineService.getPlanName(form.getPlanNidSrch());//form.getPlanNidSrch()已经校验非空
		newForm.setPlanName(planName);
		//3.添加时间
		newForm.setConfigAddTime(GetDate.getNowTime10());
		//4.状态
		newForm.setConfigStatus(Integer.valueOf(form.getConfigStatus()));
		// 数据插入
		this.allocationEngineService.insertRecord(newForm);
		// 跳转页面用（info里面有）
		modelAndView.addObject(AllocationEngineDefine.SUCCESS, AllocationEngineDefine.SUCCESS);
		LogUtil.endLog(AllocationEngineController.class.toString(), AllocationEngineDefine.INSERT_ACTION);
		return modelAndView;
	}
	
	/**
	 * 调用校验表单方法
	 * 
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView validatorFieldCheck(ModelAndView modelAndView, AllocationEngineBean form) {
		// 字段校验
		// 计划编号非空判断
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "planNidSrch", form.getPlanNidSrch())) {
			return modelAndView;
		}
		return null;
	}
	
	/**
	 * 调用校验表单方法
	 * 
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView validatorCheck(ModelAndView modelAndView, AllocationEngineBean form) {
		// 字段校验
		// 计划编号非空判断
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "labelName", form.getLabelName())) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "labelSort", form.getLabelSort())) {
			return modelAndView;
		}
		
		return null;
	}
	
	
    /**
     * 校验入力的计划编号相关
     *
     * @param planNid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AllocationEngineDefine.CHECKINPUTPLANNIDSRCH, method = RequestMethod.POST)
    public String checkInputPlanNidSrch(HttpServletRequest request) {
        LogUtil.startLog(AllocationEngineController.class.toString(), AllocationEngineDefine.CHECKINPUTPLANNIDSRCH);
        String message = this.allocationEngineService.checkInputPlanNidSrch(request);
        LogUtil.endLog(AllocationEngineController.class.toString(), AllocationEngineDefine.CHECKINPUTPLANNIDSRCH);
        return message;
    }
    
	/**
	 * 修改状态
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AllocationEngineDefine.STATUS_ACTION)
	@RequiresPermissions(AllocationEngineDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateStatus(HttpServletRequest request, RedirectAttributes attr,AllocationEngineBean form) {
		LogUtil.startLog(AllocationEngineController.class.toString(), AllocationEngineDefine.STATUS_ACTION);

		ModelAndView modelAndView = new ModelAndView(AllocationEngineDefine.RE_LIST_PATH);
		// 修改状态
		if (StringUtils.isNotEmpty(form.getId())) {
			
			//1.更改计划专区的计划的状态
			Integer id = Integer.valueOf(form.getId());
			//取更新之前的
			HjhRegion record = this.allocationEngineService.getRecord(id);
			if (record.getConfigStatus() == 1) {
				record.setConfigStatus(0);
			} else {
				record.setConfigStatus(1);
			}
			//更新计划专区表
			this.allocationEngineService.updateRecord(record);
			//2.如果某个计划的状态被更改，那这个计划下的所有标签的状态也要同时修改
			//取更新之后的(ConfigStatus已修改)
			HjhRegion record1 = this.allocationEngineService.getRecord(id);
			this.allocationEngineService.updateAllocationEngineRecord(record1.getPlanNid(),record1.getConfigStatus());//通过record的planNid去更新引擎表的 ---计划状态和标签状态
			
		}
		attr.addFlashAttribute(AllocationEngineDefine.ALLOCATIONENGINE_FORM, form);
		LogUtil.endLog(AllocationEngineController.class.toString(), AllocationEngineDefine.STATUS_ACTION);
		return modelAndView;
	}
	
	

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(AllocationEngineDefine.EXPORTEXECL)
	@RequiresPermissions(AllocationEngineDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, AllocationEngineBean form) throws Exception {
		LogUtil.startLog(AllocationEngineController.class.toString(), AllocationEngineDefine.EXPORTEXECL);
		// 表格sheet名称
		String sheetName = "智投专区";
		List<HjhRegion> resultList = this.allocationEngineService.getRecordList(form, -1, -1);
/*		Paginator paginator = new Paginator(form.getPaginatorPage(), resultList.size());
		resultList = allocationEngineService.getRecordList(form, paginator.getOffset(), paginator.getLimit());*/
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
		String[] titles = new String[] { "序号","智投编号", "智投名称","添加时间", "状态" };
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
					HjhRegion hjhRegion = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 计划编号
					else if (celLength == 1) {
						cell.setCellValue(StringUtils.isEmpty(hjhRegion.getPlanNid()) ? StringUtils.EMPTY : hjhRegion.getPlanNid());
					}
					// 计划名称
					else if (celLength == 2) {
						cell.setCellValue(StringUtils.isEmpty(hjhRegion.getPlanName()) ? StringUtils.EMPTY : hjhRegion.getPlanName());
					}
					// 添加时间
					else if (celLength == 3) {
						if (hjhRegion.getConfigAddTime() != null) {//数据库默认为空
							String configAddTime = GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(hjhRegion.getConfigAddTime());
							cell.setCellValue(configAddTime);
						} else {
							cell.setCellValue(0);
						}
					}
					// 状态
					else if (celLength == 4) {
						if (0 == hjhRegion.getConfigStatus()) {//数据库默认为0
							cell.setCellValue("停用");
						} else {
							cell.setCellValue("启用");
						} 
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(AllocationEngineController.class.toString(), AllocationEngineDefine.EXPORTEXECL);
	}
	
	
	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AllocationEngineDefine.PLANCONFIG)
	@RequiresPermissions(AllocationEngineDefine.PERMISSIONS_VIEW)
	public ModelAndView planConfig(HttpServletRequest request, AllocationEngineBean form) {
		LogUtil.startLog(AllocationEngineController.class.toString(),AllocationEngineDefine.PLANCONFIG);
		ModelAndView modelAndView = new ModelAndView(AllocationEngineDefine.PLANCONFIG_LIST_PATH);
		// 创建分页
		this.createPlanConfigPage(request, modelAndView, form);
		LogUtil.endLog(AllocationEngineController.class.toString(), AllocationEngineDefine.PLANCONFIG);
		return modelAndView;
	}
	
	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPlanConfigPage(HttpServletRequest request, ModelAndView modelAndView, AllocationEngineBean form) {
		
		String planName = this.allocationEngineService.getPlanName(form.getPlanNidSrch());//form.getPlanNidSrch()已经校验非空
		
		List<HjhAllocationEngine> recordList = this.allocationEngineService.getAllocationEngineRecordList(form, -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = allocationEngineService.getAllocationEngineRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordAllocationEngineList(recordList);
			modelAndView.addObject("planName", planName);//相同的form
			modelAndView.addObject(AllocationEngineDefine.ALLOCATIONENGINE_FORM, form);//相同的form
		}
	}
	
	
	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(AllocationEngineDefine.EXPORTEXECLPLANCONFIG)
	@RequiresPermissions(AllocationEngineDefine.PERMISSIONS_EXPORT)
	public void exportPlanConfigAction(HttpServletRequest request, HttpServletResponse response, AllocationEngineBean form) throws Exception {
		LogUtil.startLog(AllocationEngineController.class.toString(), AllocationEngineDefine.EXPORTEXECLPLANCONFIG);
		// 表格sheet名称
		String sheetName = "智投配置";
		List<HjhAllocationEngine> resultList = this.allocationEngineService.getAllocationEngineRecordList(form, -1, -1);
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
		String[] titles = new String[] { "序号","标签编号", "标签名称","添加时间", "状态","排序" };
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
					HjhAllocationEngine hjhAllocationEngine = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 标签编号
					else if (celLength == 1) {
						cell.setCellValue(hjhAllocationEngine.getLabelId());//DB标签id默认为0
					}
					// 标签名称
					else if (celLength == 2) {
						cell.setCellValue(StringUtils.isEmpty(hjhAllocationEngine.getLabelName()) ? StringUtils.EMPTY : hjhAllocationEngine.getLabelName());
					}
					// 添加时间
					else if (celLength == 3) {
						if (hjhAllocationEngine.getAddTime() != null) {//数据库默认为0
							String addTime = GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(hjhAllocationEngine.getAddTime());
							cell.setCellValue(addTime);
						} else {
							cell.setCellValue(0);
						}
					}
					// 状态
					else if (celLength == 4) {
						if (0 == hjhAllocationEngine.getLabelStatus()) {//数据库默认为0
							cell.setCellValue("停用");
						} else {
							cell.setCellValue("启用");
						} 
					}
					// 排序
					else if (celLength == 5) {
						cell.setCellValue(hjhAllocationEngine.getLabelSort());//DB标签默认为0
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(AllocationEngineController.class.toString(), AllocationEngineDefine.EXPORTEXECLPLANCONFIG);
	}
	
	
	/**
	 * 修改状态
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AllocationEngineDefine.LABEL_STATUS_ACTION)
	@RequiresPermissions(AllocationEngineDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateLabelStatus(HttpServletRequest request, RedirectAttributes attr,AllocationEngineBean form) {
		LogUtil.startLog(AllocationEngineController.class.toString(), AllocationEngineDefine.LABEL_STATUS_ACTION);
		ModelAndView modelAndView = new ModelAndView(AllocationEngineDefine.PLANCONFIG_LIST_PATH);//回计划配置画面
		// 修改状态
		if (StringUtils.isNotEmpty(form.getId())) {
			Integer id = Integer.valueOf(form.getId());
			HjhAllocationEngine record = this.allocationEngineService.getPlanConfigRecord(id);
			if (record.getLabelStatus() == 1) {//标签状态
				record.setLabelStatus(0);
			} else {
				record.setLabelStatus(1);
			}
			this.allocationEngineService.updatePlanConfigRecord(record);
		}
		//数据库已经改完状态
		HjhAllocationEngine record = this.allocationEngineService.getPlanConfigRecord(Integer.valueOf(form.getId()));
		String planName = record.getPlanName();
		String planNid = record.getPlanNid();
		form.setPlanNidSrch(planNid);
		List<HjhAllocationEngine> recordList = this.allocationEngineService.getAllocationEngineRecordList(form, -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = allocationEngineService.getAllocationEngineRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordAllocationEngineList(recordList);
			modelAndView.addObject("planName", planName);//相同的form
			modelAndView.addObject("planNid", planNid);//相同的form
			modelAndView.addObject(AllocationEngineDefine.ALLOCATIONENGINE_FORM, form);//相同的form
		}
		/*attr.addFlashAttribute(AllocationEngineDefine.ALLOCATIONENGINE_FORM, form);*/
		LogUtil.endLog(AllocationEngineController.class.toString(), AllocationEngineDefine.LABEL_STATUS_ACTION);
		return modelAndView;
	}
	
	/**
	 * 计划配置画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AllocationEngineDefine.INFO_CONFIG_ACTION)
	@RequiresPermissions(value = { AllocationEngineDefine.PERMISSIONS_INFO, AllocationEngineDefine.PERMISSIONS_ADD,AllocationEngineDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView infoConfig(HttpServletRequest request, RedirectAttributes attr,@ModelAttribute(AllocationEngineDefine.ALLOCATIONENGINE_FORM) AllocationEngineBean form) {
		LogUtil.startLog(AllocationEngineController.class.toString(), AllocationEngineDefine.INFO_CONFIG_ACTION);
		//新增
		ModelAndView modelAndView = new ModelAndView(AllocationEngineDefine.INFO_CONFIG_PATH);
		if(form.getPlanNidSrch()!= null){
			modelAndView.addObject("planNid", form.getPlanNidSrch());
		}
		//修改
		if (StringUtils.isNotEmpty(form.getId())) {
			Integer id = Integer.valueOf(form.getId());
			HjhRegion record = this.allocationEngineService.getRecord(id);
			modelAndView.addObject(AllocationEngineDefine.ALLOCATIONENGINE_FORM, record);
		}
		LogUtil.endLog(AllocationEngineController.class.toString(), AllocationEngineDefine.INFO_CONFIG_ACTION);
		return modelAndView;
	}
	
	/**
	 * 校验入力的标签名称相关
	 *
	 * @param labelName
	 * @return 
	 */
	@RequestMapping(AllocationEngineDefine.CHECKINPUTLABELNAME)
	@RequiresPermissions(AllocationEngineDefine.PERMISSIONS_VIEW)
	@ResponseBody
	public Map<String, Object> checkInputlabelname(HttpServletRequest request, RedirectAttributes attr,AllocationEngineBean form) {
		
		Map<String, Object> labelNameMap = new HashMap();
		String labelName = form.getLabelName();
		String planNid = form.getPlanNid();
		if(StringUtils.isEmpty(labelName)){
			labelNameMap.put("info", "请输入标签名称");
			labelNameMap.put("status", "n");
			return labelNameMap;
		}
		if(StringUtils.isEmpty(planNid)){
			labelNameMap.put("info", "该标签所属计划编号不存在，请查询计划专区");
			labelNameMap.put("status", "n");
			return labelNameMap;
		}
		// 校验一个计划下不能用重复的标签名称
		boolean existflg = this.allocationEngineService.checkRepeat(labelName,planNid);
		if(!existflg){
			labelNameMap.put("info", "该标签已经被使用，无法再次添加");
			labelNameMap.put("status", "n");
			return labelNameMap;
		}
		//1.校验该标签名称是否存在于标签表
		HjhLabel hjhLabel = this.allocationEngineService.getHjhLabelRecord(labelName);
		if(hjhLabel == null){
			labelNameMap.put("info", "标签数据不存在，请先查看标签列表是否已经添加");
			labelNameMap.put("status", "n");
			return labelNameMap;
		} else {
			//校验
			if(hjhLabel.getLabelState() == 0){
				labelNameMap.put("info", "标签已停用，请先启用");
				labelNameMap.put("status", "n");
				return labelNameMap;
			}
			if(hjhLabel.getDelFlg() == 1){
				labelNameMap.put("info", "标签已删除");
				labelNameMap.put("status", "n");
				return labelNameMap;
			}
			//标签存在的情况下：
			//先查询汇计划表获取该计划的还款方式
			String planBorrowStyle = this.allocationEngineService.getPlanBorrowStyle(planNid);//planNid已经校验非空
			if(StringUtils.isEmpty(hjhLabel.getBorrowStyle())){
				labelNameMap.put("info", "该标签的还款方式为空,请查询标签列表");
				labelNameMap.put("status", "n");
				return labelNameMap;
			}
			if(StringUtils.isEmpty(planBorrowStyle)){
				labelNameMap.put("info", "该计划的还款方式为空，请查询计划列表");
				labelNameMap.put("status", "n");
				return labelNameMap;
			}
			//二期迭代取消标签还款方式校验
			/*if(!planBorrowStyle.trim().equals(hjhLabel.getBorrowStyle().trim())){
				labelNameMap.put("info", "标签还款方式与计划还款方式不一致，无法添加");
				labelNameMap.put("status", "n");
				return labelNameMap;
			}*/
			labelNameMap.put("status", "y");
		}
		return labelNameMap;
			
	}
	

	/**
	 * 校验入力的标签排序相关
	 *
	 * @param labelSort
	 * @return 
	 */
	@RequestMapping(AllocationEngineDefine.CHECKINPUTLABELSORT)
	@RequiresPermissions(AllocationEngineDefine.PERMISSIONS_VIEW)
	@ResponseBody
	public Map<String, Object> checkInputlabelSort(HttpServletRequest request, RedirectAttributes attr,AllocationEngineBean form) {
		
		Map<String, Object> labelSortMap = new HashMap();
		
		String labelSort = form.getLabelSort();
		String planNid = form.getPlanNid();
		
		if(StringUtils.isEmpty(labelSort)){
			labelSortMap.put("info", "请输入标签排序");
			labelSortMap.put("status", "n");
			return labelSortMap;
		}
		if(StringUtils.isEmpty(planNid)){
			labelSortMap.put("info", "该标签所属计划编号不存在，请查询计划专区");
			labelSortMap.put("status", "n");
			return labelSortMap;
		}
		//一个计划下有多个标签，每次新建一个标签时，都会在某一个计划下插入一条记录，也就是通过planNid会在引擎表查出多条记录
		List<HjhAllocationEngine> hjhAllocationEngineList = this.allocationEngineService.getHjhAllocationEngineListByPlan(planNid);
		if (hjhAllocationEngineList != null) {
			for(HjhAllocationEngine object : hjhAllocationEngineList){
				//取自DB的LabelSort
				Integer planLabelSort = object.getLabelSort();
				//如果 取自DB的LabelSort 等同于 画面传入的 labelSort,那说明重复，则不能插入
				if(planLabelSort !=null && planLabelSort == Integer.valueOf(labelSort)){
					labelSortMap.put("info", "该计划已有标签使用此排序,请重新输入排序");
					labelSortMap.put("status", "n");
					return labelSortMap;
				}
			}
		} else{
			labelSortMap.put("info", "该标签所属计划编号不存在，请查询计划专区");
			labelSortMap.put("status", "n");
			return labelSortMap;
		}
		labelSortMap.put("status", "y");
		
		return labelSortMap;
	}
	
	/**
	 * 添加计划标签
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = AllocationEngineDefine.INSERT_CONFIG_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(AllocationEngineDefine.PERMISSIONS_ADD)
	public ModelAndView insertConfigAction(HttpServletRequest request, AllocationEngineBean form) {
		LogUtil.startLog(AllocationEngineController.class.toString(), AllocationEngineDefine.INSERT_CONFIG_ACTION);
		
		ModelAndView modelAndView = new ModelAndView(AllocationEngineDefine.INFO_CONFIG_PATH);
		// 开始插表
		HjhAllocationEngine newForm = new HjhAllocationEngine();
		//如果PlanNid不为空，则用PlanNid查询计划专区表获取必要信息,PlanNid是唯一索引
		HjhRegion record = this.allocationEngineService.getHjhRegionRecord(form.getPlanNid());
		//1.
		newForm.setPlanNid(record.getPlanNid());
		//2.
		newForm.setPlanName(record.getPlanName());
		//3.
		newForm.setConfigAddTime(record.getConfigAddTime());
		//4.
		newForm.setConfigStatus(record.getConfigStatus());
		
		HjhLabel hjhLabel = this.allocationEngineService.getHjhLabelRecord(form.getLabelName());//已经校验过必须入力的LabelName
		//5.
		newForm.setLabelId(hjhLabel.getId());
		//6.
		newForm.setLabelName(hjhLabel.getLabelName());
		//7.
		newForm.setAddTime(GetDate.getNowTime10());
		//8.
		newForm.setLabelSort(Integer.valueOf(form.getLabelSort()));//已经校验过必须入力的LabelSort
		//9.
		if(StringUtils.isNotEmpty(form.getTransferTimeSort())){//表单入力时
			newForm.setTransferTimeSort(Integer.valueOf(form.getTransferTimeSort()));
		}
		//10.
		if(StringUtils.isNotEmpty(form.getTransferTimeSortPriority())){//表单入力时
			newForm.setTransferTimeSortPriority(Integer.valueOf(form.getTransferTimeSortPriority()));
		}
		//11.
		if(StringUtils.isNotEmpty(form.getAprSort())){//表单入力时
			newForm.setAprSort(Integer.valueOf(form.getAprSort()));
		}
		//12.
		if(StringUtils.isNotEmpty(form.getAprSortPriority())){//表单入力时
			newForm.setAprSortPriority(Integer.valueOf(form.getAprSortPriority()));
		}
		//13.
		if(StringUtils.isNotEmpty(form.getActulPaySort())){//表单入力时
			newForm.setActulPaySort(Integer.valueOf(form.getActulPaySort()));
		}
		//14.
		if(StringUtils.isNotEmpty(form.getActulPaySortPriority())){//表单入力时
			newForm.setActulPaySortPriority(Integer.valueOf(form.getActulPaySortPriority()));
		}
		//15.
		if(StringUtils.isNotEmpty(form.getInvestProgressSort())){//表单入力时
			newForm.setInvestProgressSort(Integer.valueOf(form.getInvestProgressSort()));
		}
		//16.
		if(StringUtils.isNotEmpty(form.getInvestProgressSortPriority())){//表单入力时
			newForm.setInvestProgressSortPriority(Integer.valueOf(form.getInvestProgressSortPriority()));
		}
		//17.
		newForm.setLabelStatus(Integer.valueOf(form.getLabelStatus()));//必须入力
		
		this.allocationEngineService.insertHjhAllocationEngineRecord(newForm);
		// 跳转页面用（info里面有）
		modelAndView.addObject(AllocationEngineDefine.SUCCESS, AllocationEngineDefine.SUCCESS);

		LogUtil.endLog(AllocationEngineController.class.toString(), AllocationEngineDefine.INSERT_CONFIG_ACTION);
		return modelAndView;
	}
	
	/**
	 * 点击添加迁按钮-移到汇计划详情画面
	 * 
	 * @Title moveToInfoAction
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = AllocationEngineDefine.MODIFY_ACTION)
	@RequiresPermissions(value = AllocationEngineDefine.PERMISSIONS_MODIFY)
	public ModelAndView modifyAction(HttpServletRequest request, AllocationEngineBean form) {
		//展示出的添加详情画面路径
		ModelAndView modelAndView = new ModelAndView(AllocationEngineDefine.INFO_CONFIG_PATH);
		String planNid = form.getPlanNidSrch();
		String labelId = form.getLabelId();
		if(form.getPlanNidSrch()!= null){
			modelAndView.addObject("planNid", form.getPlanNidSrch());
		}
		if (StringUtils.isNotEmpty(labelId)) {
			HjhAllocationEngine record = this.allocationEngineService.getRecordBylabelId(planNid,labelId);
			modelAndView.addObject(AllocationEngineDefine.ALLOCATIONENGINE_FORM, record);
		}	
		return modelAndView;
	}
	/**
	 * 点击换绑迁按钮-移到换绑详情画面
	 * 
	 * @Title moveToInfoAction
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = AllocationEngineDefine.CHANGE_ACTION)
	@RequiresPermissions(value = AllocationEngineDefine.PERMISSIONS_MODIFY)
	public ModelAndView changeAction(HttpServletRequest request, AllocationEngineBean form) {
		//展示出的换绑详情画面路径
		ModelAndView modelAndView = new ModelAndView(AllocationEngineDefine.CHANGE_CONFIG_PATH);
		if(form.getPlanNidSrch()!= null){
			modelAndView.addObject("planNid", form.getPlanNidSrch());
			
			HjhAllocationEngine hjhallo = allocationEngineService.selectByPrimaryKey(Integer.parseInt(form.getId()));
			modelAndView.addObject("labelName", hjhallo.getLabelName());
		}
		if (StringUtils.isNotEmpty(form.getLabelId())) {
			HjhAllocationEngine record = this.allocationEngineService.getRecordBylabelId(form.getPlanNidSrch(),form.getLabelId());
			modelAndView.addObject(AllocationEngineDefine.GINE_FORM, record);
		}	
		return modelAndView;
	}
	
	
	/**
	 * 点击换绑后的保存
	 * 根据获取到的leableName到allocation-engine查询出来相对应的数据,执行删除操作
	 * 根据planNid去新加一条数据,
	 * @Title moveToInfoAction
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = AllocationEngineDefine.UPDATE_CONFIG_ACTION_INFO )
	@RequiresPermissions(value = AllocationEngineDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateConfigActionInfo(HttpServletRequest request, AllocationEngineBean form){
		ModelAndView modelAndView = new ModelAndView(AllocationEngineDefine.INFO_CONFIG_PATH);
		//获取原计划的计划编号
		String planNidSrch = form.getPlanNidSrch();
		//获取到当前输入的订单号
		String planNid = form.getPlanNid();
		//获取到当前输入的标签名
		String labelName = form.getLabelName();
		String newLabelStatus = form.getLabelStatus();
		String id = form.getId();
		form.setPlanNid(planNidSrch);
		form.setLabelStatus("0");
		//根据LabelName进行更改状态
		HjhAllocationEngine hjhAllocationEngine = new HjhAllocationEngine();
		BeanUtils.copyProperties(form,hjhAllocationEngine);		
		hjhAllocationEngine.setLabelStatus(0);
		hjhAllocationEngine.setId(Integer.parseInt(id));
		this.allocationEngineService.updateHjhAllocationEngineRecord(hjhAllocationEngine);
		hjhAllocationEngine.setPlanNid(planNid);
		String planNameM = this.allocationEngineService.getPlanName(planNid);
		hjhAllocationEngine.setPlanName(planNameM);
		hjhAllocationEngine.setLabelStatus(Integer.parseInt(newLabelStatus));
		hjhAllocationEngine.setUpdateTime(GetDate.getNowTime10());
		hjhAllocationEngine.setLabelSort(Integer.parseInt(form.getLabelSort()));
		hjhAllocationEngine.setCreateTime(GetDate.getNowTime10());
		this.allocationEngineService.updateHjhAllocationEngineRecord(hjhAllocationEngine);
		//根据LabelName执行删除方法进行删除操作
		return modelAndView;
	}
	
	
	
	
	/**
	 * 修改畫面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AllocationEngineDefine.UPDATE_CONFIG_ACTION)
	@RequiresPermissions(AllocationEngineDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateConfigAction(HttpServletRequest request, RedirectAttributes attr,AllocationEngineBean form) {
		ModelAndView modelAndView = new ModelAndView(AllocationEngineDefine.INFO_CONFIG_PATH);//回计划配置画面
		// 开始插表
		HjhAllocationEngine record = new HjhAllocationEngine();
		if (StringUtils.isNotEmpty(form.getPlanNid()) && StringUtils.isNotEmpty(form.getLabelName())) {
			record = this.allocationEngineService.getRecordBylabelName(form.getPlanNid(),form.getLabelName());
			//1.
			record.setLabelName(form.getLabelName());
			//2.
			if(StringUtils.isNotEmpty(form.getTransferTimeSort())){
				record.setTransferTimeSort(Integer.valueOf(form.getTransferTimeSort()));
			} 
			//3.
			if(StringUtils.isNotEmpty(form.getTransferTimeSortPriority())){
				record.setTransferTimeSortPriority(Integer.valueOf(form.getTransferTimeSortPriority()));
			} 
			//4.
			if(StringUtils.isNotEmpty(form.getAprSort())){
				record.setAprSort(Integer.valueOf(form.getAprSort()));
			}
			//5.
			if(StringUtils.isNotEmpty(form.getAprSortPriority())){
				record.setAprSortPriority(Integer.valueOf(form.getAprSortPriority()));
			}
			//6.
			if(StringUtils.isNotEmpty(form.getActulPaySort())){
				record.setActulPaySort(Integer.valueOf(form.getActulPaySort()));
			}
			//7.
			if(StringUtils.isNotEmpty(form.getActulPaySortPriority())){
				record.setActulPaySortPriority(Integer.valueOf(form.getActulPaySortPriority()));
			}
			//8.
			if(StringUtils.isNotEmpty(form.getInvestProgressSort())){
				record.setInvestProgressSort(Integer.valueOf(form.getInvestProgressSort()));
			}
			//9.
			if(StringUtils.isNotEmpty(form.getInvestProgressSortPriority())){
				record.setInvestProgressSortPriority(Integer.valueOf(form.getInvestProgressSortPriority()));
			}
			//9.x
			if(StringUtils.isNotEmpty(form.getPlanNid())){
				record.setPlanNid(form.getPlanNid());
			}
			if(StringUtils.isNotEmpty(form.getConfigStatus())){
				record.setConfigStatus(Integer.valueOf(form.getConfigStatus()));
			}
			if(StringUtils.isNotEmpty(form.getLabelSort())){
				record.setLabelSort(Integer.valueOf(form.getLabelSort()));
			}
			
			//10.
			record.setLabelSort(Integer.valueOf(form.getLabelSort()));
			//11.
			record.setLabelStatus(Integer.valueOf(form.getLabelStatus()));
		
			this.allocationEngineService.updateHjhAllocationEngineRecord(record);
		}
		// 跳转页面用（info里面有）
		modelAndView.addObject(AllocationEngineDefine.SUCCESS, AllocationEngineDefine.SUCCESS);
		return modelAndView;
		
	}
	
	
	/**
	 * 修改前报消息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AllocationEngineDefine.REPORT_ACTION)
	@RequiresPermissions(AllocationEngineDefine.PERMISSIONS_MODIFY)
	@ResponseBody
	public Map<String, Object> reportAction(HttpServletRequest request, RedirectAttributes attr,AllocationEngineBean form) {
		
		LogUtil.startLog(AllocationEngineController.class.toString(), AllocationEngineDefine.REPORT_ACTION);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> reportMap = new HashMap();
		
		//原始画面已经存在，不需要校验为空
		String labelName = form.getLabelName();
		String planName = form.getPlanName();
		// (计划专区)原始专区画面已经有此计划了，不需要为空校验
		HjhRegion hjhRegion  = this.allocationEngineService.getHjhRegionRecordByName(planName);
		// (标签列表)原始画面已经有此标签了
		HjhLabel  hjhLabel = this.allocationEngineService.getHjhLabelRecord(labelName);
		// (引擎)
		HjhAllocationEngine hjhAllocationEngine = this.allocationEngineService.getRecordBy(planName,labelName);
		
		if(hjhRegion.getConfigStatus() == 0 && hjhLabel.getLabelState()  == 1 && hjhAllocationEngine.getLabelStatus() == 0){
			reportMap.put("info", "计划已被停用需重新启用");
			reportMap.put("status", "0");
			return reportMap;
		}
		if(hjhRegion.getConfigStatus() == 0 && hjhLabel.getLabelState()  == 0 && hjhAllocationEngine.getLabelStatus() == 0){
			reportMap.put("info", "计划和标签都已被停用需重新启用");
			reportMap.put("status", "1");
			return reportMap;
		}
		if(hjhRegion.getConfigStatus() == 1 && hjhLabel.getLabelState()  == 0 && hjhAllocationEngine.getLabelStatus() == 0){
			reportMap.put("info", "标签已被停用需重新启用");
			reportMap.put("status", "2");
			return reportMap;
		}
		if(hjhRegion.getConfigStatus() == 1 && hjhLabel.getLabelState()  == 1 && hjhAllocationEngine.getLabelStatus() == 0){
			reportMap.put("info", "确定要执行本次操作吗！");
			reportMap.put("status", "3");
			return reportMap;
		}
		return reportMap;
	}
	
    /**
     * 校验计划编号是否存在
     *
     * @param request
     * @return
     */
    @ResponseBody 
    @RequestMapping(value = AllocationEngineDefine.ISEXISTSPLANNUMBER_ACTION, method = RequestMethod.POST)
    public String isExistsUser(HttpServletRequest request) {
        LogUtil.startLog(AllocationEngineController.class.toString(), AllocationEngineDefine.ISEXISTSPLANNUMBER_ACTION);
        String message = this.allocationEngineService.isExistsPlanNumber(request);
        LogUtil.endLog(AllocationEngineController.class.toString(), AllocationEngineDefine.ISEXISTSPLANNUMBER_ACTION);
        return message;
    }

	
	
		
}
