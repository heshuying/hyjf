package com.hyjf.admin.manager.config.bankrecharge;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.config.bankconfig.BankConfigService;
import com.hyjf.admin.promotion.utm.UtmController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetterUtil;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.auto.BankRechargeLimitConfig;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

/**
 * 手续费配置
 * 
 * @author
 *
 */
@Controller
@RequestMapping(value = BankRechargeDefine.REQUEST_MAPPING)
public class BankRechargeController extends BaseController {

	@Autowired
	private BankRechargeService bankRechargeService;
	@Autowired
	private BankConfigService bankConfigService;

	/**
	 * 列表维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BankRechargeDefine.INIT)
	@RequiresPermissions(BankRechargeDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(BankRechargeDefine.BANKRECHARGE_FORM) BankRechargeBean form) {
		LogUtil.startLog(BankRechargeController.class.toString(), BankRechargeDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BankRechargeDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BankRechargeController.class.toString(), BankRechargeDefine.INIT);
		return modelAndView;
	}

	/**
	 * 分页机能 页面初始化
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BankRechargeBean form) {
		List<BankRechargeLimitConfig> recordList = this.bankRechargeService.getRecordList(new BankRechargeLimitConfig(), -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = this.bankRechargeService.getRecordList(new BankRechargeLimitConfig(), paginator.getOffset(),
					paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(BankRechargeDefine.BANKRECHARGE_FORM, form);
		}
		//银行列表
		modelAndView.addObject("bankList", bankConfigService.getBankRecordList());
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BankRechargeDefine.INFO_ACTION)
	@RequiresPermissions(value = { BankRechargeDefine.PERMISSIONS_INFO, BankRechargeDefine.PERMISSIONS_ADD,BankRechargeDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr,@ModelAttribute(BankRechargeDefine.BANKRECHARGE_FORM) BankRechargeBean form) {
		LogUtil.startLog(BankRechargeController.class.toString(), BankRechargeDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BankRechargeDefine.INFO_PATH);

		if (StringUtils.isNotEmpty(form.getIds())) {
			Integer id = Integer.valueOf(form.getIds());
			BankRechargeLimitConfig record = this.bankRechargeService.getRecord(id);
			modelAndView.addObject(BankRechargeDefine.BANKRECHARGE_FORM, record);
		}
		//银行列表
		modelAndView.addObject("bankList", bankConfigService.getBankRecordList());
		LogUtil.endLog(BankRechargeController.class.toString(), BankRechargeDefine.INIT);
		return modelAndView;
	}

	/**
	 * 添加
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = BankRechargeDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(BankRechargeDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, BankRechargeBean form) {
		LogUtil.startLog(BankRechargeController.class.toString(), BankRechargeDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(BankRechargeDefine.INFO_PATH);

		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// 数据插入
		this.bankRechargeService.insertRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(BankRechargeDefine.SUCCESS, BankRechargeDefine.SUCCESS);
		LogUtil.endLog(BankRechargeController.class.toString(), BankRechargeDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = BankRechargeDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(BankRechargeDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, BankRechargeBean form) {
		LogUtil.startLog(BankRechargeController.class.toString(), BankRechargeDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(BankRechargeDefine.INFO_PATH);

		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// // 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
			return modelAndView;
		}
		// 更新
		this.bankRechargeService.updateRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(BankRechargeDefine.SUCCESS, BankRechargeDefine.SUCCESS);
		LogUtil.endLog(BankRechargeController.class.toString(), BankRechargeDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 删除配置信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BankRechargeDefine.DELETE_ACTION)
	@RequiresPermissions(BankRechargeDefine.PERMISSIONS_DELETE)
	public ModelAndView deleteRecordAction(HttpServletRequest request, String ids) {
		LogUtil.startLog(BankRechargeController.class.toString(), BankRechargeDefine.DELETE_ACTION);

		ModelAndView modelAndView = new ModelAndView(BankRechargeDefine.RE_LIST_PATH);
		// 解析json字符串
		List<Integer> recordList = JSONArray.parseArray(ids, Integer.class);
		this.bankRechargeService.deleteRecord(recordList);
		LogUtil.endLog(BankRechargeController.class.toString(), BankRechargeDefine.DELETE_ACTION);
		return modelAndView;
	}

	/**
	 * 调用校验表单方法
	 * 
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView validatorFieldCheck(ModelAndView modelAndView, BankRechargeBean form) {
		// 字段校验(非空判断和长度判断)
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "bankId", form.getBankId().toString())) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "bankId", String.valueOf(form.getBankId()), 11, true)) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "singleQuota", String.valueOf(form.getSingleQuota()))) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "singleQuota", String.valueOf(form.getSingleQuota()), 13, true)) {
			return modelAndView;
		}
		return null;
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(BankRechargeDefine.EXPORT_ACTION)
	@RequiresPermissions(BankRechargeDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, BankRechargeBean form) throws Exception {
		LogUtil.startLog(BankRechargeController.class.toString(), BankRechargeDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "快捷充值限额配置";
		BankRechargeLimitConfig bankRecharge = new BankRechargeLimitConfig();
		//列表
		List<BankRechargeLimitConfig> resultList  = this.bankRechargeService.exportRecordList(bankRecharge);
		//银行
		List<BankConfig> bankList = bankConfigService.getBankRecordList();
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
		String[] titles = new String[] {"序号", "银行", "接入方式","银行卡类型","单笔充值限额（元）","单卡单日累计限额（元）","状态" };
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
					BankRechargeLimitConfig pInfo = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					else if (celLength == 1) {
					bank: for(int j = 0;j < bankList.size();j++){
							if(pInfo.getBankId() == bankList.get(j).getId()){
								cell.setCellValue(bankList.get(j).getName());
								break bank;
							}
						}
					}
					else if (celLength == 2) {
						if(pInfo.getAccessCode() == null){
							cell.setCellValue("");
						}else if(pInfo.getAccessCode() == 0){
							cell.setCellValue("全国");
						}
					}
					else if (celLength == 3) {
						if(pInfo.getBankType() == null){
							cell.setCellValue("");
						}else if(pInfo.getBankType() == 0){
							cell.setCellValue("借记卡");
						}
					}
					else if (celLength == 4) {
						if(pInfo.getSingleQuota() == null){
							cell.setCellValue("无限额");
						}else{
							cell.setCellValue(String.valueOf(pInfo.getSingleQuota()));
						}
					}
					else if (celLength == 5) {
						if(pInfo.getSingleCardQuota() == null){
							cell.setCellValue("无限额");
						}else{
							cell.setCellValue(String.valueOf(pInfo.getSingleCardQuota()));
						}
					}
					else if (celLength == 6) {
						if(pInfo.getStatus() == null){
							cell.setCellValue("");
						}else if(pInfo.getStatus() == 0){
							cell.setCellValue("启用");
						}else{
							cell.setCellValue("禁用");
						}
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(BankRechargeController.class.toString(), BankRechargeDefine.EXPORT_ACTION);
	}
	
	/**
	 * 检查编号唯一性
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = BankRechargeDefine.CHECK_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(value = { BankRechargeDefine.PERMISSIONS_ADD, BankRechargeDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public String checkAction(HttpServletRequest request) {
		LogUtil.startLog(UtmController.class.toString(), BankRechargeDefine.CHECK_ACTION);

        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String param = request.getParameter("param");
        
        JSONObject ret = new JSONObject();
        // 检查银行卡是否重复
        if ("bankId".equals(name)) {
            int cnt = bankRechargeService.bankIsExists(GetterUtil.getInteger(param), GetterUtil.getInteger(id));
            if (cnt > 0) {
                String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
                message = message.replace("{label}", "银行");
                ret.put(BankRechargeDefine.JSON_VALID_INFO_KEY, message);
            }
        }
		// 没有错误时,返回y
		if (!ret.containsKey(BankRechargeDefine.JSON_VALID_INFO_KEY)) {
			ret.put(BankRechargeDefine.JSON_VALID_STATUS_KEY, BankRechargeDefine.JSON_VALID_STATUS_OK);
		}
		LogUtil.endLog(UtmController.class.toString(), BankRechargeDefine.CHECK_ACTION);
		return ret.toString();
	}
	
	
	/**
	 * 汇付查询限额配置信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BankRechargeDefine.QUERY_ACTION)
	@RequiresPermissions(BankRechargeDefine.PERMISSIONS_SEARCH)
	public ModelAndView queryAction(HttpServletRequest request) {
		LogUtil.startLog(BankRechargeController.class.toString(), BankRechargeDefine.QUERY_ACTION);
		ModelAndView modelAndView = new ModelAndView(BankRechargeDefine.RE_LIST_PATH);
		//查询汇付信息
		ChinapnrBean retBean = new ChinapnrBean();
		try {
			ChinapnrBean querybean = new ChinapnrBean();
			querybean.setVersion(ChinaPnrConstant.VERSION_10);// 接口版本号
			querybean.setCmdId(ChinaPnrConstant.CMDID_QUERY_PAY_QUOTA); // 消息类型
//			querybean.setOpenBankId("");//银行卡
			querybean.setGateBusiId(ChinaPnrConstant.PARAM_QP);
			querybean.setBgRetUrl(ChinapnrUtil.getBgRetUrl());
			//调用接口
		    retBean =  ChinapnrUtil.callApiBg(querybean);
		    //调用成功
		    if(ChinaPnrConstant.RESPCODE_SUCCESS.equals(retBean.getRespCode())){
		    	String payQutaList = retBean.getPayQuotaDetails();
		    	JSONArray array = JSONObject.parseArray(payQutaList);
		    	if(array != null && array.size() > 0){
		    		//更新数据
		    		for(int i = 0;i < array.size();i++){
			    		JSONObject obj = array.getJSONObject(i);
						this.bankRechargeService.updateBankRechargeConfig(obj.getString("OpenBankId"), obj.getString("SingleTransQuota"), obj.getString("CardDailyTransQuota"));
		    		}
		    	}
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		LogUtil.endLog(BankRechargeController.class.toString(), BankRechargeDefine.QUERY_ACTION);
		return modelAndView;
	}
	
}
