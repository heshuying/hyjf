package com.hyjf.admin.manager.user.mspconfigure;

import java.math.BigDecimal;
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
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.exception.openaccountenquiryexception.OpenAccountEnquiryDefine;
import com.hyjf.admin.manager.user.msp.MspApplyService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.customize.MspConfigure;
import com.hyjf.mybatis.model.customize.MspConfigureExample;

/**安融反欺诈查询配置表
 * 
 * @author
 *
 */
@Controller
@RequestMapping(value = MspDefine.REQUEST_MAPPING)
public class MspController extends BaseController {

	@Autowired
	private MspService mspService;
	@Autowired
	private MspApplyService mspApplyService;

	/**
	 * 列表维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MspDefine.INIT)
	@RequiresPermissions(MspDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(MspDefine.FORM) MspBean form) {
		LogUtil.startLog(MspController.class.toString(), MspDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(MspDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(MspController.class.toString(), MspDefine.INIT);
		return modelAndView;
	}

	/**
	 * 列表维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MspDefine.SEARCH_ACTION)
	@RequiresPermissions(MspDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(MspDefine.FORM) MspBean form) {
		LogUtil.startLog(MspController.class.toString(), MspDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(MspDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(MspController.class.toString(), MspDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 文章列表维护分页机能 页面初始化
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, MspBean form) {
		// 封装查询条件
		Map<String, Object> conditionMap = setCondition(form);
		Integer count = this.mspService.getRecordCount(conditionMap);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			List<MspConfigure> recordList = this.mspService.getRecordList(conditionMap, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(MspDefine.FORM, form);
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MspDefine.INFO_ACTION)
	@RequiresPermissions(value = { MspDefine.PERMISSIONS_ADD, MspDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(MspDefine.FORM) MspBean form) {
		LogUtil.startLog(MspController.class.toString(), MspDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(MspDefine.INFO_PATH);
		if (form.getId()!=null) {
			MspConfigure record = mspService.getRecord(form.getId().toString());
			record.setRegionList(this.mspApplyService.getRegionList());
			modelAndView.addObject(MspDefine.FORM, record);
			return modelAndView;
		}
		form.setUnredeemedMoney(new BigDecimal(0));
		form.setLoanTimeLimit(1); 
		form.setRegionList(this.mspApplyService.getRegionList());
		LogUtil.endLog(MspController.class.toString(), MspDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 添加信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = MspDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(MspDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, MspBean form) {
		LogUtil.startLog(MspController.class.toString(), MspDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(MspDefine.INFO_PATH);

		if (form.getId()!=null) {
			MspConfigure record = mspService.getRecord(form.getId().toString());
			if (record != null) {
				ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "id", "repeat");
			}
		}
		System.out.println(form.getConfigureName());
		MspConfigure mspConfigure =new MspConfigure();
		mspConfigure.setConfigureName(form.getConfigureName());
		//验证标的名称是否重复
		int record = mspService.sourceNameIsExists(mspConfigure);
		if (record == 1) {
			ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "sourceName", "repeat");
		}
		
//		// 画面验证
//		this.validatorFieldCheck(modelAndView, form);
//
//		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
//			modelAndView.addObject(MspDefine.FORM, form);
//			return modelAndView;
//		}

		// 数据插入
		this.mspService.insertRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(MspDefine.SUCCESS, MspDefine.SUCCESS);
		LogUtil.endLog(MspController.class.toString(), MspDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = MspDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(MspDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, MspBean form,MspConfigure mspConfigure) {
		LogUtil.startLog(MspController.class.toString(), MspDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(MspDefine.INFO_PATH);

		// 画面验证
		this.validatorFieldCheck(modelAndView, form);

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			form.setRegionList(this.mspApplyService.getRegionList());
			modelAndView.addObject(MspDefine.FORM, form);
			return modelAndView;
		}

		// 更新
		this.mspService.updateRecord(mspConfigure);
		// 跳转页面用（info里面有）
		modelAndView.addObject(MspDefine.SUCCESS, MspDefine.SUCCESS);
		LogUtil.endLog(MspController.class.toString(), MspDefine.UPDATE_ACTION);
		return modelAndView;
	}
	/**
     * ajax用户按照手机号和身份证号查询开户掉单校验
     * 
     * @param request
     * 
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(MspDefine.ERROR)
    public String configureNameError(HttpServletRequest request, HttpServletResponse response) {
    	LogUtil.startLog(OpenAccountEnquiryDefine.THIS_CLASS, MspDefine.ERROR);
    	JSONObject jsonString = new JSONObject();
    	String configureNam = request.getParameter("param").trim(); 
    	
    	MspConfigure mspConfigure =new MspConfigure();
		mspConfigure.setConfigureName(configureNam);
    	int record = mspService.sourceNameIsExists(mspConfigure);
    	if (record>0) {
    		jsonString.put("info", "标的名不允许重复！");
       	 LogUtil.errorLog(OpenAccountEnquiryDefine.THIS_CLASS, OpenAccountEnquiryDefine.UPDATE_PATH, "标的名不允许重复", null);
       	 return jsonString.toJSONString();
        }else{
        	 jsonString.put("status", "y");
             LogUtil.errorLog(OpenAccountEnquiryDefine.THIS_CLASS, OpenAccountEnquiryDefine.UPDATE_PATH, "验证通过", null);
             return jsonString.toJSONString(); 
        }
    }
    	
	/**
	 * 画面校验
	 * 
	 * @param modelAndView
	 * @param form
	 */
	private ModelAndView validatorFieldCheck(ModelAndView modelAndView, MspBean form) {
		// 字段校验(非空判断和长度判断)
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "configureName", form.getConfigureName())) {
			return modelAndView;
		}
		//申请借款地点
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "creditAddress", form.getCreditAddress(), 150, true)) {
			return modelAndView;
		}
		// 期数
		if(!ValidatorFieldCheckUtil.validateSignlessNum(modelAndView, "loanTimeLimit", form.getLoanTimeLimit().toString(), 4, true)){
			return modelAndView;
		}
//		// 判未偿还本金
//		if(!ValidatorFieldCheckUtil.validateNum(modelAndView, "unredeemedMoney", form.getUnredeemedMoney().toString(), true)){
//			return modelAndView;
//		}
			// 合同金额
//			if(!bigDecimalMix(form.getLoanMoney())){
		if(!ValidatorFieldCheckUtil.validateNumMainGreaterSub(modelAndView, "loanMoney", form.getLoanMoney().intValue(), 100, true)){
			return modelAndView;
		}
		return modelAndView;
		
		// 名称
//		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "sourceName", form.getSourceName(), 150, true);
		// 发标时间
//		ValidatorFieldCheckUtil.validateRequired(modelAndView, "delFlag", form.getDelFlag());
		// 备注说明
//		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "remark", form.getRemark(), 225, false);
	}
	//判断合同金额是否大于100
	public boolean bigDecimalMix(BigDecimal bigDecimal){
		BigDecimal b = new BigDecimal(100);
		if (bigDecimal.compareTo(b)==-1) {
			return false;
		}
		return true;
		
	}

	/**
	 * 删除安融查询数据
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MspDefine.DELETE_ACTION)
	@RequiresPermissions(MspDefine.PERMISSIONS_DELETE)
	public ModelAndView deleteRecordAction(HttpServletRequest request, MspBean form) {
		LogUtil.startLog(MspController.class.toString(), MspDefine.DELETE_ACTION);
		ModelAndView modelAndView = new ModelAndView(MspDefine.DELETE_AFTER_PATH);
		this.mspService.deleteRecord(form.getId().toString());
		LogUtil.endLog(MspController.class.toString(), MspDefine.DELETE_ACTION);
		return modelAndView;
//		return "redirect:" + MspDefine.REQUEST_MAPPING + "/" + MspDefine.INIT;
	}
	/**
	 * 数据导出
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MspDefine.EXPORT_ACTION)
	@RequiresPermissions(MspDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, MspBean form) throws Exception {
		LogUtil.startLog(MspController.class.toString(), MspDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "安融反欺诈查询配置表";
		
		// 封装查询条件
		Map<String, Object> conditionMap = setCondition(form);

		List<MspConfigure> resultList = null;
		
		resultList = this.mspService.getRecordList(conditionMap,-1,-1);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "标的名称", "业务类型", "借款类型(借款用途)", "审批结果 ", "借款金额（合同金额）（元）", "借款/还款期数（月）", "借款城市(借款地点)", "担保类型", "未偿还本金", "当前还款状态"};
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
					MspConfigure record = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 标的名称
					else if (celLength == 1) {
						cell.setCellValue(record.getConfigureName());
					}
					// 业务类型
					else if (celLength == 2) {
						cell.setCellValue(record.getServiceTypeName());
					}
					// 借款类型
					else if (celLength == 3) {
						cell.setCellValue(record.getLoanTypeName());
					}
					// 审批结果
					else if (celLength == 4) {
						cell.setCellValue(record.getApprovalResultName());
					}
					// 借款金额
					else if (celLength == 5) {
						cell.setCellValue(record.getLoanMoney().toString());
					}
					// 期数
					else if (celLength == 6) {
						cell.setCellValue(record.getLoanTimeLimit());
					}
					// 借款地点
					else if (celLength == 7) {
						cell.setCellValue(record.getCreditAddress());
					}
					// 担保类型
					else if (celLength == 8) {
						cell.setCellValue(record.getGuaranteeTypeName());
					}

					// 未偿还本金
					else if (celLength == 9) {
						cell.setCellValue(record.getUnredeemedMoney().toString());
					}
					// 当前还款状态
					else if (celLength == 10) {
						cell.setCellValue(record.getRepaymentStatusName());
					}
					
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(MspController.class.toString(), MspDefine.EXPORT_ACTION);
	}
	/**
	 * 封装查询条件
	 * 
	 * @param form
	 * @return
	 */
	private Map<String, Object> setCondition(MspBean form) {
		//封装查询条件
		MspConfigureExample example = new MspConfigureExample();
		String serviceTypeSrch = StringUtils.isNotEmpty(form.getServiceTypeSrch()) ? form.getServiceTypeSrch() : null;
		String sourceTypeSrch = StringUtils.isNotEmpty(form.getSourceTypeSrch()) ? form.getSourceTypeSrch() : null;
		String loanTypeSrch = StringUtils.isNotEmpty(form.getLoanTypeSrch()) ? form.getLoanTypeSrch() : null;
		String loanMoney = StringUtils.isNotEmpty(form.getLoanMoneys()) ? form.getLoanMoneys() : null;
		String creditAddress = StringUtils.isNotEmpty(form.getCreditAddress()) ? form.getCreditAddress() : null;
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("serviceTypeSrch", serviceTypeSrch);
		conditionMap.put("sourceTypeSrch", sourceTypeSrch);
		conditionMap.put("loanTypeSrch", loanTypeSrch);
		conditionMap.put("loanMoney", loanMoney);
		conditionMap.put("creditAddress", creditAddress);
		return conditionMap;
	}
	/**
	 * 检查编号唯一性
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = MspDefine.CHECK_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(value = { MspDefine.PERMISSIONS_ADD, MspDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public String checkAction(HttpServletRequest request) {
		LogUtil.startLog(MspController.class.toString(), MspDefine.CHECK_ACTION);

		String param = request.getParameter("param");

		JSONObject ret = new JSONObject();
		MspBean form = new MspBean();
		form.setSourceId(param);
		int record = mspService.sourceIdIsExists(form.getSourceId());
		if (record == 1) {
			String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
			message = message.replace("{label}", "渠道编号");
			ret.put(MspDefine.JSON_VALID_INFO_KEY, message);
		}
		// 没有错误时,返回y
		if (!ret.containsKey(MspDefine.JSON_VALID_INFO_KEY)) {
			ret.put(MspDefine.JSON_VALID_STATUS_KEY, MspDefine.JSON_VALID_STATUS_OK);
		}
		LogUtil.endLog(MspController.class.toString(), MspDefine.CHECK_ACTION);
		return ret.toString();
	}

}
