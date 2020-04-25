package com.hyjf.admin.finance.merchant.transfer;

import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

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
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.MerchantAccount;
import com.hyjf.mybatis.model.auto.MerchantTransfer;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.AdminSystem;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

/**
 * @package com.hyjf.admin.finance.transfer
 * @author wangkun
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = MerchantTransferDefine.REQUEST_MAPPING)
public class MerchantTransferController extends BaseController {

	@Autowired
	private MerchantTransferService transferService;

	/**
	 * 用户转账列表
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MerchantTransferDefine.TRANSFER_LIST_ACTION)
	@RequiresPermissions(MerchantTransferDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(MerchantTransferDefine.TRANSFER_LIST_FORM) MerchantTransferListBean form) {
		LogUtil.startLog(MerchantTransferDefine.THIS_CLASS, MerchantTransferDefine.TRANSFER_LIST_ACTION);
		ModelAndView modelAndView = new ModelAndView(MerchantTransferDefine.TRANSFER_LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(MerchantTransferDefine.THIS_CLASS, MerchantTransferDefine.TRANSFER_LIST_ACTION);
		return modelAndView;
	}

	/**
	 * 转账列表分页
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, MerchantTransferListBean form) {
		// 转账状态
		List<ParamName> transferStatus = this.transferService.getParamNameList("MER_TRANS_STATUS");
		modelAndView.addObject("transferStatus", transferStatus);
		// 交易类型
		List<ParamName> transferTypes = this.transferService.getParamNameList("MER_TRANS_TYPE");
		modelAndView.addObject("transferTypes", transferTypes);
		// 子账户列表
		List<MerchantAccount> merchantAccountListOut = this.transferService.selectMerchantAccountList(null);
		modelAndView.addObject("merchantAccountListOut", merchantAccountListOut);
		// 子账户列表
		List<MerchantAccount> merchantAccountListIn = this.transferService.selectMerchantAccountList(null);
		modelAndView.addObject("merchantAccountListIn", merchantAccountListIn);
		
		int recordTotal = this.transferService.queryRecordTotal(form);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			List<MerchantTransfer> recordList = this.transferService.selectRecordList(form,paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
		}
		modelAndView.addObject(MerchantTransferDefine.TRANSFER_LIST_FORM, form);
	}

	/**
	 * 初始化用户转账画面
	 *
	 * @param request
	 * @return 进入用户详情页面
	 */
	@RequestMapping(MerchantTransferDefine.INIT_TRANSFER_ACTION)
	@RequiresPermissions(MerchantTransferDefine.PERMISSION_ADD)
	public ModelAndView initTransfer(HttpServletRequest request) {
		LogUtil.startLog(MerchantTransferDefine.THIS_CLASS, MerchantTransferDefine.INIT_TRANSFER_ACTION);
		ModelAndView modelAndView = new ModelAndView(MerchantTransferDefine.INIT_TRANSFER_PATH);
		// 子账户列表
		List<MerchantAccount> merchantAccountListOut = this.transferService.selectMerchantAccountList(0);
		modelAndView.addObject("merchantAccountListOut", merchantAccountListOut);
		// 子账户列表
		List<MerchantAccount> merchantAccountListIn = this.transferService.selectMerchantAccountList(1);
		modelAndView.addObject("merchantAccountListIn", merchantAccountListIn);
		LogUtil.endLog(MerchantTransferDefine.THIS_CLASS, MerchantTransferDefine.INIT_TRANSFER_ACTION);
		return modelAndView;
	}

	/**
	 * 校验用户转账的结果
	 *
	 * @param request
	 * @return 进入用户详情页面
	 */
	@ResponseBody
	@RequiresPermissions(MerchantTransferDefine.PERMISSION_ADD)
	@RequestMapping(value = MerchantTransferDefine.CHECK_TRANSFER_ACTION, method = RequestMethod.POST)
	public String checkTransfer(HttpServletRequest request) {

		LogUtil.startLog(MerchantTransferDefine.THIS_CLASS, MerchantTransferDefine.CHECK_TRANSFER_ACTION);
		String outAccountId = request.getParameter("outAccountId");
		String transferAmount = request.getParameter("param");
		JSONObject ret = new JSONObject();
		this.transferService.checkMerchantTransfer(outAccountId,transferAmount, ret);
		LogUtil.endLog(MerchantTransferDefine.THIS_CLASS, MerchantTransferDefine.CHECK_TRANSFER_ACTION);
		return ret.toString();
	}

	/**
	 * 初始化用户转账画面
	 *
	 * @param request
	 * @return 进入用户详情页面
	 */
	@RequestMapping(MerchantTransferDefine.ADD_TRANSFER_ACTION)
	@RequiresPermissions(MerchantTransferDefine.PERMISSION_ADD)
	public ModelAndView addTransfer(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute MerchantTransferCustomizeBean form) {
		LogUtil.startLog(MerchantTransferDefine.THIS_CLASS, MerchantTransferDefine.ADD_TRANSFER_ACTION);
		ModelAndView modelAndView = new ModelAndView(MerchantTransferDefine.INIT_TRANSFER_PATH);
		this.transferService.checkMerchantTransferParam(modelAndView, form);
		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.addObject(MerchantTransferDefine.TRANSFER_FORM, form);
			// 子账户列表
			List<MerchantAccount> merchantAccountListOut = this.transferService.selectMerchantAccountList(0);
			modelAndView.addObject("merchantAccountListOut", merchantAccountListOut);
			// 子账户列表
			List<MerchantAccount> merchantAccountListIn = this.transferService.selectMerchantAccountList(1);
			modelAndView.addObject("merchantAccountListIn", merchantAccountListIn);
			LogUtil.errorLog(MerchantTransferDefine.THIS_CLASS, MerchantTransferDefine.ADD_TRANSFER_ACTION, "输入内容验证失败", null);
		} else {
			// 生成订单
			String orderId = GetOrderIdUtils.getOrderId2(Integer.valueOf(form.getOutAccountId()));
			AdminSystem adminSystem = ShiroUtil.getLoginUserInfo();
			form.setCreateUserId(Integer.parseInt(adminSystem.getId()));
			form.setCreateUserName(adminSystem.getUsername());
			form.setOrderId(orderId);
			boolean flag = this.transferService.insertTransfer(form);
			if (flag) {
				//调用商户转账接口进行转账
				String custId=PropUtils.getSystem("hyjf.chinapnr.mercustid");
				ChinapnrBean chinapnrBean = new ChinapnrBean();
				// 接口版本号
				chinapnrBean.setVersion(ChinaPnrConstant.VERSION_10);
				// 消息类型(主动投标)
				chinapnrBean.setCmdId(ChinaPnrConstant.CMDID_TRANSFER);
				// 订单号(必须)
				chinapnrBean.setOrdId(orderId);
				// 出账客户号
				chinapnrBean.setOutCustId(custId);
				// 出账子账户
				chinapnrBean.setOutAcctId(form.getOutAccountCode());
				// 交易金额(必须)
				chinapnrBean.setTransAmt(CustomUtil.formatAmount(form.getTransferAmount().toString()));
				// 入账客户号
				chinapnrBean.setInCustId(custId);
				//入账子账户
				chinapnrBean.setInAcctId(form.getInAccountCode());
				// log用户
				chinapnrBean.setLogUserId(form.getCreateUserId());
				// 日志类型
				chinapnrBean.setType(ChinaPnrConstant.CMDID_TRANSFER);
				try {
					// 发送请求获取结果
					ChinapnrBean resultBean = ChinapnrUtil.callApiBg(chinapnrBean);
					String respCode = resultBean == null ? "" : resultBean.getRespCode();
					if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)) {
						boolean afterFlag = this.transferService.updateMerchantTransfer(orderId,1,null)>0?true:false;
						if(afterFlag){
							//转账成功
							System.out.println("转账成功,订单号："+orderId);
							modelAndView.addObject(MerchantTransferDefine.SUCCESS, MerchantTransferDefine.SUCCESS);
							LogUtil.endLog(MerchantTransferDefine.THIS_CLASS, MerchantTransferDefine.ADD_TRANSFER_ACTION);
						}else{
							//转账成功，更新状态失败
							ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "transferAmount", "merchant.transfer.success","更新转账成功状态失败!");
							modelAndView.addObject(MerchantTransferDefine.TRANSFER_FORM, form);
							System.out.println("更新转账成功状态失败,订单号："+orderId);
							LogUtil.errorLog(MerchantTransferDefine.THIS_CLASS, MerchantTransferDefine.ADD_TRANSFER_ACTION, "更新转账成功状态失败", null);
						}
					} else {
						String  respDesc = ""; 
						if(StringUtils.isNotEmpty(resultBean.getRespDesc())){
							respDesc =URLDecoder.decode(resultBean.getRespDesc(), "UTF-8");
						}
						boolean afterFlag = this.transferService.updateMerchantTransfer(orderId,2,"转账失败，失败原因:"+respDesc)>0?true:false;
						if(afterFlag){
							System.out.println("转账失败,订单号："+orderId);
						}else{
							System.out.println("更新转账失败状态失败,订单号："+orderId);
						}
						ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "transferAmount", "merchant.transfer.fail","调用子账户转账接口失败!");
						modelAndView.addObject(MerchantTransferDefine.TRANSFER_FORM, form);
						LogUtil.errorLog(MerchantTransferDefine.THIS_CLASS, MerchantTransferDefine.ADD_TRANSFER_ACTION, "调用子账户转账接口失败", null);
					}
				} catch (Exception e) {
					e.printStackTrace();
					boolean afterFlag = this.transferService.updateMerchantTransfer(orderId,2,"调用子账户转账接口异常")>0?true:false;
					if(afterFlag){
						System.out.println("调用子账户转账接口异常，转账失败,订单号："+orderId);
					}else{
						System.out.println("调用子账户转账接口异常，更新转账失败状态失败,订单号："+orderId);
					}
					ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "transferAmount", "merchant.transfer.exception","调用子账户转账接口异常!");
					modelAndView.addObject(MerchantTransferDefine.TRANSFER_FORM, form);
					LogUtil.errorLog(MerchantTransferDefine.THIS_CLASS, MerchantTransferDefine.ADD_TRANSFER_ACTION, "调用子账户转账接口异常", null);
				}
			} else {
				System.out.println("数据预插入失败,订单号："+orderId);
				ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "transferAmount", "merchant.transfer.error","数据预插入失败!");
				modelAndView.addObject(MerchantTransferDefine.TRANSFER_FORM, form);
				LogUtil.errorLog(MerchantTransferDefine.THIS_CLASS, MerchantTransferDefine.ADD_TRANSFER_ACTION, "数据预插入失败", null);
			}
		}
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
	@RequestMapping(MerchantTransferDefine.EXPORT_TRANSFER_ACTION)
	@RequiresPermissions(MerchantTransferDefine.PERMISSIONS_EXPORT)
	public void exportExcel(@ModelAttribute MerchantTransferListBean form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		LogUtil.startLog(MerchantTransferDefine.THIS_CLASS, MerchantTransferDefine.EXPORT_TRANSFER_ACTION);
		// 表格sheet名称
		String sheetName = "平台子账户转账记录";
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
		List<MerchantTransfer> recordList = this.transferService.selectRecordList(form,-1,-1);
		String[] titles = new String[] { "序号", "订单号","转出子账户","转出子账户代号", "转入子账户","转入子账户代号","转账金额","备注", "转账状态", "操作人", "转账类型","转账时间" };
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
					MerchantTransfer transfer = recordList.get(i);
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {// 序号
						cell.setCellValue(i + 1);
					} else if (celLength == 1) {// 订单号
						cell.setCellValue(transfer.getOrderId());
					} else if (celLength == 2) {// 转出子账户
						cell.setCellValue(transfer.getOutAccountName());
					} else if (celLength == 3) {// 转出子账户代号
						cell.setCellValue(transfer.getOutAccountCode());
					} else if (celLength == 4) {// 转入子账户
						cell.setCellValue(transfer.getInAccountName());
					} else if (celLength == 5) {// 转入子账户代号
						cell.setCellValue(transfer.getInAccountCode());
					} else if (celLength == 6) {// 转账金额
						cell.setCellValue(String.valueOf(transfer.getTransferAmount()));
					} else if (celLength == 7) {// 备注
						cell.setCellValue(transfer.getRemark());
					} else if (celLength == 8) { // 转账状态
						// 转账状态
						List<ParamName> transferStatus = this.transferService.getParamNameList("MER_TRANS_STATUS");
						for(int j=0;j<transferStatus.size();j++){
							if(transferStatus.get(j).getNameCd().equals(String.valueOf(transfer.getStatus()))){
								cell.setCellValue(transferStatus.get(j).getName());
							}
						}
					}else if (celLength == 9 ) {// 操作人
						cell.setCellValue(transfer.getCreateUserName());
					}else if (celLength == 10 ) {//转账类型
						// 交易类型
						List<ParamName> transferTypes = this.transferService.getParamNameList("MER_TRANS_TYPE");
						for(int j=0;j<transferTypes.size();j++){
							if(transferTypes.get(j).getNameCd().equals(String.valueOf(transfer.getTransferType()))){
								cell.setCellValue(transferTypes.get(j).getName());
							}
						}
					}else if (celLength == 11) {// 转账时间
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
		LogUtil.endLog(MerchantTransferDefine.THIS_CLASS, MerchantTransferDefine.EXPORT_TRANSFER_ACTION);
	}
}
