package com.hyjf.admin.finance.transfer;

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
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.auto.UserTransfer;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.AdminSystem;

/**
 * @package com.hyjf.admin.finance.transfer
 * @author wangkun
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = TransferDefine.REQUEST_MAPPING)
public class TransferController extends BaseController {

	@Autowired
	private TransferService transferService;
	
	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;

	/**
	 * 用户转账列表
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(TransferDefine.TRANSFER_LIST_ACTION)
	@RequiresPermissions(TransferDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(TransferDefine.TRANSFER_LIST_FORM) TransferListBean form) {
		LogUtil.startLog(TransferDefine.THIS_CLASS, TransferDefine.TRANSFER_LIST_ACTION);
		ModelAndView modelAndView = new ModelAndView(TransferDefine.TRANSFER_LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(TransferDefine.THIS_CLASS, TransferDefine.TRANSFER_LIST_ACTION);
		return modelAndView;
	}

	/**
	 * 转账列表分页
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, TransferListBean form) {
		// 转账状态
		List<ParamName> transferStatus = this.transferService.getParamNameList("TRANSFER_STATUS");
		modelAndView.addObject("transferStatus", transferStatus);
		
		// 交易类型
		List<ParamName> transferTypes = this.transferService.getParamNameList("TRANSFER_TYPE");
		modelAndView.addObject("transferTypes", transferTypes);
		
		int recordTotal = this.transferService.countRecordTotal(form);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			List<UserTransfer> recordList = this.transferService.getRecordList(form,
					paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
		}
		modelAndView.addObject(TransferDefine.TRANSFER_LIST_FORM, form);
	}

	/**
	 * 初始化用户转账画面
	 *
	 * @param request
	 * @return 进入用户详情页面
	 */
	@RequestMapping(TransferDefine.INIT_TRANSFER_ACTION)
	@RequiresPermissions(TransferDefine.PERMISSION_ADD)
	public ModelAndView initTransfer(HttpServletRequest request) {
		LogUtil.startLog(TransferDefine.THIS_CLASS, TransferDefine.INIT_TRANSFER_ACTION);
		ModelAndView modelAndView = new ModelAndView(TransferDefine.INIT_TRANSFER_PATH);
		LogUtil.endLog(TransferDefine.THIS_CLASS, TransferDefine.INIT_TRANSFER_ACTION);
		return modelAndView;
	}

	/**
	 * 校验用户转账的结果
	 *
	 * @param request
	 * @return 进入用户详情页面
	 */
	@ResponseBody
	@RequiresPermissions(TransferDefine.PERMISSION_ADD)
	@RequestMapping(value = TransferDefine.CHECK_TRANSFER_ACTION, method = RequestMethod.POST)
	public String checkTransfer(HttpServletRequest request) {

		LogUtil.startLog(TransferDefine.THIS_CLASS, TransferDefine.CHECK_TRANSFER_ACTION);
		String outUserName = request.getParameter("param");
		JSONObject ret = new JSONObject();
		if (StringUtils.isNotBlank(outUserName)) {
			this.transferService.checkTransfer(outUserName, ret);
		} else {
			ret.put(TransferDefine.JSON_VALID_INFO_KEY, "用户账号不能为空!");
		}
		LogUtil.endLog(TransferDefine.THIS_CLASS, TransferDefine.CHECK_TRANSFER_ACTION);
		return ret.toString();
	}

	/**
	 * 获取用户的余额
	 *
	 * @param request
	 * @return 进入用户详情页面
	 */
	@ResponseBody
	@RequiresPermissions(TransferDefine.PERMISSION_ADD)
	@RequestMapping(value = TransferDefine.SEARCH_BALANCE_ACTION, method = RequestMethod.POST)
	public String searchUserBalance(HttpServletRequest request) {

		LogUtil.startLog(TransferDefine.THIS_CLASS, TransferDefine.SEARCH_BALANCE_ACTION);
		String outUserName = request.getParameter("outUserName");
		JSONObject ret = new JSONObject();
		if (StringUtils.isNotBlank(outUserName)) {
			this.transferService.searchBalance(outUserName, ret);
		} else {
			ret.put(TransferDefine.JSON_STATUS_KEY, TransferDefine.JSON_STATUS_NG);
			ret.put(TransferDefine.JSON_RESULT_KEY, "用户账号不能为空!");
		}
		LogUtil.endLog(TransferDefine.THIS_CLASS, TransferDefine.SEARCH_BALANCE_ACTION);
		return ret.toString();
	}

	/**
	 * 初始化用户转账画面
	 *
	 * @param request
	 * @return 进入用户详情页面
	 */
	@RequestMapping(TransferDefine.ADD_TRANSFER_ACTION)
	@RequiresPermissions(TransferDefine.PERMISSION_ADD)
	public ModelAndView addTransfer(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute TransferCustomizeBean form) {
		LogUtil.startLog(TransferDefine.THIS_CLASS, TransferDefine.ADD_TRANSFER_ACTION);
		ModelAndView modelAndView = new ModelAndView(TransferDefine.INIT_TRANSFER_PATH);
		this.checkTransferParam(modelAndView, form);
		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.addObject(TransferDefine.TRANSFER_FORM, form);
			LogUtil.errorLog(TransferDefine.THIS_CLASS, TransferDefine.ADD_TRANSFER_ACTION, "输入内容验证失败", null);
		} else {
			AdminSystem adminSystem = ShiroUtil.getLoginUserInfo();
			form.setCreateUserId(Integer.parseInt(adminSystem.getId()));
			form.setCreateUserName(adminSystem.getUsername());
			boolean flag = this.transferService.insertTransfer(form);
			if (flag) {
				modelAndView.addObject(TransferDefine.SUCCESS, TransferDefine.SUCCESS);
				LogUtil.endLog(TransferDefine.THIS_CLASS, TransferDefine.ADD_TRANSFER_ACTION);
			} else {
				modelAndView.addObject(TransferDefine.TRANSFER_FORM, form);
				LogUtil.errorLog(TransferDefine.THIS_CLASS, TransferDefine.ADD_TRANSFER_ACTION, "数据插入失败", null);
			}
		}
		return modelAndView;
	}

	/**
	 * 发送用户转账邮件
	 *
	 * @param request
	 * @return 进入用户详情页面
	 */
	@ResponseBody
	@RequestMapping(TransferDefine.TRANSFER_SEND_MAIL_ACTION)
	@RequiresPermissions(TransferDefine.PERMISSION_TRANSFER_SEND_EMAIL)
	public String sendTransferEmail(HttpServletRequest request, HttpServletResponse response) {
		
		String id = request.getParameter("id");
		JSONObject ret = new JSONObject();
		if(StringUtils.isNotBlank(id)){
			UserTransfer userTransfer = this.transferService.searchUserTransferById(Integer.parseInt(id));
			if(userTransfer==null){
				ret.put(TransferDefine.JSON_STATUS_KEY, TransferDefine.JSON_STATUS_NG);
				ret.put(TransferDefine.JSON_RESULT_KEY, "未查询到转账信息!");
			}else{
				Users user = this.transferService.searchUserByUserId(userTransfer.getOutUserId());
				if (StringUtils.isNotBlank(user.getEmail())) {
					Map<String, String> replaceMap = new HashMap<String, String>();
					replaceMap.put("val_name", userTransfer.getOutUserName());
					replaceMap.put("val_amount", userTransfer.getTransferAmount().toString());
					replaceMap.put("remark", userTransfer.getRemark());
					replaceMap.put("val_url", userTransfer.getTransferUrl());
					String[] email = { user.getEmail() };
					MailMessage messageMail = new MailMessage(null, replaceMap, "用户转账",null,null, email,CustomConstants.PARAM_TPL_TRANSFER, MessageDefine.MAILSENDFORMAILINGADDRESS);
		            mailMessageProcesser.gather(messageMail);
					ret.put(TransferDefine.JSON_STATUS_KEY, TransferDefine.JSON_STATUS_OK);
					ret.put(TransferDefine.JSON_RESULT_KEY, "邮件发送成功!");
				}else{
					ret.put(TransferDefine.JSON_STATUS_KEY, TransferDefine.JSON_STATUS_NG);
					ret.put(TransferDefine.JSON_RESULT_KEY, "用户邮箱为空不能发送邮件!");
				}
			}
		}else{
			ret.put(TransferDefine.JSON_STATUS_KEY, TransferDefine.JSON_STATUS_NG);
			ret.put(TransferDefine.JSON_RESULT_KEY, "参数错误无法发送邮件!");
		}
		return ret.toString();
	}

	private void checkTransferParam(ModelAndView modelAndView, TransferCustomizeBean form) {

		this.transferService.checkTransferParam(modelAndView, form);
		// 说明
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "remark", form.getRemark(), 60, true);
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
	@RequestMapping(TransferDefine.EXPORT_TRANSFER_ACTION)
	@RequiresPermissions(TransferDefine.PERMISSIONS_EXPORT)
	public void exportExcel(@ModelAttribute TransferListBean form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		LogUtil.startLog(TransferDefine.THIS_CLASS, TransferDefine.EXPORT_TRANSFER_ACTION);
		// 表格sheet名称
		String sheetName = "转账记录";
		// 文件名称
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;

		//设置默认查询时间
		if(StringUtils.isEmpty(form.getTransferTimeStart())){
			form.setTransferTimeStart(GetDate.getDate("yyyy-MM-dd"));
		}
		if(StringUtils.isEmpty(form.getTransferTimeEnd())){
			form.setTransferTimeEnd(GetDate.getDate("yyyy-MM-dd"));
		}
		// 需要输出的结果列表
		List<UserTransfer> recordList = this.transferService.exportRecordList(form);
		String[] titles = new String[] { "序号", "订单号","交易类型", "转出账户", "转入账户", "转账金额","对账标识", "转账状态", "转账链接", "操作员", "操作时间", "转账时间" };
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
					UserTransfer transfer = recordList.get(i);
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {// 序号
						cell.setCellValue(i + 1);
					} else if (celLength == 1) {// 订单号
						cell.setCellValue(transfer.getOrderId());
					} else if (celLength == 2) {// 交易类型
						// 交易类型
						List<ParamName> transferTypes = this.transferService.getParamNameList("TRANSFER_TYPE");
						for(int j=0;j<transferTypes.size();j++){
							if(transferTypes.get(j).getNameCd().equals(String.valueOf(transfer.getTransferType()))){
								cell.setCellValue(transferTypes.get(j).getName());
							}
						}
					} else if (celLength == 3) {// 转出账户
						cell.setCellValue(transfer.getOutUserName());
					} else if (celLength == 4) {// 转入账户
						cell.setCellValue("平台");
					} else if (celLength == 5) {// 转账金额
						cell.setCellValue(String.valueOf(transfer.getTransferAmount()));
					} else if (celLength == 6) {// 对账标识
						if(transfer.getReconciliationId() == null){
							cell.setCellValue("");
						}else{
							cell.setCellValue(transfer.getReconciliationId());
						}
					} else if (celLength == 7) {// 转账状态
						// 转账状态
						List<ParamName> transferStatus = this.transferService.getParamNameList("TRANSFER_STATUS");
						for(int j=0;j<transferStatus.size();j++){
							if(transferStatus.get(j).getNameCd().equals(String.valueOf(transfer.getStatus()))){
								cell.setCellValue(transferStatus.get(j).getName());
							}
						}
					}else if (celLength == 8) {// 转账链接
						cell.setCellValue(transfer.getTransferUrl());
					}else if (celLength == 9) {// 操作员
						cell.setCellValue(transfer.getCreateUserName());
					} else if (celLength == 10) {// 操作时间
						if(transfer.getCreateTime() == null){
							cell.setCellValue("");
						}else{
							cell.setCellValue(GetDate.date2Str(transfer.getCreateTime(), GetDate.datetimeFormat));
						}
					} else if (celLength == 11) {// 转账时间
						if(transfer.getTransferTime() == null){
							cell.setCellValue("");
						}else{
							cell.setCellValue(GetDate.date2Str(transfer.getTransferTime(), GetDate.datetimeFormat));
						}
					} 
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(TransferDefine.THIS_CLASS, TransferDefine.EXPORT_TRANSFER_ACTION);
	}
}
