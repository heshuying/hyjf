package com.hyjf.web.user.rechargefee;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.chinapnr.MerPriv;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.ChinapnrExclusiveLogWithBLOBs;
import com.hyjf.mybatis.model.auto.RechargeFeeReconciliation;
import com.hyjf.mybatis.model.customize.RechargeCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import com.hyjf.web.BaseController;
import com.hyjf.web.chinapnr.ChinapnrService;
import com.hyjf.web.util.WebUtils;

/**
 * 充值手续费对账
 * @author 李深强
 */
@Controller
@RequestMapping(value = RechargeFeeDefine.REQUEST_MAPPING)
public class RechargeFeeController extends BaseController {

    /** THIS_CLASS */
    private static final String THIS_CLASS = RechargeFeeController.class.getName();

    @Autowired
    private ChinapnrService chinapnrService;

    @Autowired
    private RechargeFeeService rechargeFeeService;

	/**
	 * 
	 *	跳转到充值手续费对账页面
	 * 
	 * @author 李深强
	 * @return
	 */
	@RequestMapping(value = RechargeFeeDefine.PAGE_MAPPING)
	public ModelAndView rechargePage() {
		ModelAndView modelAndView = new ModelAndView(RechargeFeeDefine.JSP_PAGE);
		return modelAndView;
	}


	
	/**
	 * 充值手续费列表
	 * 
	 * @param rechargeFee
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = RechargeFeeDefine.RECHARGE_FEE_LIST_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public RechargeFeeBean rechargeFeeList(@ModelAttribute RechargeFeeBean rechargeFee, HttpServletRequest request,
			HttpServletResponse response) {
		LogUtil.startLog(THIS_CLASS, RechargeFeeDefine.RECHARGE_FEE_LIST_ACTION);
		Integer userId = WebUtils.getUserId(request); 
		rechargeFee.setUserId(userId);
		this.createRechargeFeeListPage(request, rechargeFee);
		rechargeFee.success();
		LogUtil.endLog(THIS_CLASS, RechargeFeeDefine.RECHARGE_FEE_LIST_ACTION);
		return rechargeFee;
	}

	/**
	 * 充值手续费列表分页数据
	 * 
	 * @param request
	 * @param info
	 * @param form
	 */
	private void createRechargeFeeListPage(HttpServletRequest request,RechargeFeeBean form) {
		// 记录数
		int recordTotal = this.rechargeFeeService.countFeeRecordTotal(form);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			// 列表
			List<RechargeFeeReconciliation> rechargeFeeList = rechargeFeeService.queryRechargeFeeList(form, paginator.getOffset(), paginator.getLimit());
			form.setRechargeFeeList(rechargeFeeList);
			form.setPaginator(paginator);
		} else {
			form.setRechargeFeeList(new ArrayList<RechargeFeeReconciliation>());
			form.setPaginator(new Paginator(form.getPaginatorPage(), 0));
		}
	}

	
	
	
	/**
	 * 用户转账
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RechargeFeeDefine.PAY_ACTION)
	public ModelAndView rechargeFeePay(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(THIS_CLASS, RechargeFeeDefine.PAY_ACTION);
		ModelAndView modelAndView = new ModelAndView();
		try {
			String recordId = request.getParameter("recordId");
			JSONObject ret = new JSONObject();
			RechargeFeeReconciliation rechargeFee = rechargeFeeService.checkParam(recordId, ret);
			if (ret.get("error") != null && ret.get("error").equals("1")) {
				modelAndView = new ModelAndView(RechargeFeeDefine.PAGE_ERROR);
				modelAndView.addObject(RechargeFeeDefine.MESSAGE, ret.getString("data"));
				return modelAndView;
			}
			// 获取转账用户
			int userId = rechargeFee.getUserId();
			AccountChinapnr chinapnrTransfer = this.rechargeFeeService.getAccountChinapnr(userId);
			if (chinapnrTransfer == null) {
				modelAndView = new ModelAndView(RechargeFeeDefine.PAGE_ERROR);
				modelAndView.addObject(RechargeFeeDefine.MESSAGE, "您未开户,请登陆后开户");
				return modelAndView;
			}
			// 转账用户汇付客户号
			long transferUsrcustid = chinapnrTransfer.getChinapnrUsrcustid();
			// 用户转账金额
			BigDecimal transferAmount = rechargeFee.getRechargeFee();
			//订单编号
			String orderId = GetOrderIdUtils.getOrderId2(userId);
			// 更新转账为进行中
			Boolean flag = rechargeFeeService.updateRechargeFee(rechargeFee,orderId);
			// 成功后调用出借接口
			if (flag) {
				// 回调路径
				String returl = PropUtils.getSystem("hyjf.web.host").trim() + RechargeFeeDefine.REQUEST_MAPPING
						+ RechargeFeeDefine.PAY_RETURN_ACTION;
				// 商户后台应答地址(必须)
				String bgRetUrl = PropUtils.getSystem("http.hyjf.web.host").trim() + RechargeFeeDefine.REQUEST_MAPPING
						+ RechargeFeeDefine.PAY_RETURN_BG_ACTION;
				ChinapnrBean chinapnrBean = new ChinapnrBean();
				// 接口版本号
				chinapnrBean.setVersion(ChinaPnrConstant.VERSION_10);
				// 消息类型(用户账户支付)
				chinapnrBean.setCmdId(ChinaPnrConstant.CMDID_USR_ACCT_PAY);
				// 订单号(必须)
				chinapnrBean.setOrdId(orderId);
				// 交易金额(必须)
				chinapnrBean.setTransAmt(transferAmount.toString());
				// 用户客户号
				chinapnrBean.setUsrCustId(String.valueOf(transferUsrcustid));
				//入账账户
				chinapnrBean.setInAcctId(PropUtils.getSystem(ChinaPnrConstant.PROP_MERACCT09).trim());
				// 入账账户类型
				chinapnrBean.setInAcctType("MERDT");
				// 商户私有域
				MerPriv merPriv = new MerPriv();
				merPriv.setRecordId(Integer.valueOf(recordId)); //表记录id
				chinapnrBean.setMerPrivPo(merPriv);
				// 页面返回
				chinapnrBean.setRetUrl(returl);
				// 商户后台应答地址(必须)
				chinapnrBean.setBgRetUrl(bgRetUrl);
				// log用户
				chinapnrBean.setLogUserId(userId);
				// 日志类型
				chinapnrBean.setType("user_recharge_fee");
				try {
					modelAndView = ChinapnrUtil.callApi(chinapnrBean);
					LogUtil.endLog(THIS_CLASS, RechargeFeeDefine.PAY_ACTION);
				} catch (Exception e) {
					e.printStackTrace();
					modelAndView = new ModelAndView(RechargeFeeDefine.PAGE_ERROR);
					modelAndView.addObject(RechargeFeeDefine.MESSAGE, "付款失败！");
				}
			} else {
				modelAndView = new ModelAndView(RechargeFeeDefine.PAGE_ERROR);
				modelAndView.addObject(RechargeFeeDefine.MESSAGE, "付款失败！");
			}

		} catch (Exception e1) {
			e1.printStackTrace();
			modelAndView = new ModelAndView(RechargeFeeDefine.PAGE_ERROR);
			modelAndView.addObject(RechargeFeeDefine.MESSAGE, "付款失败");
		}
		return modelAndView;
	}

	/**
	 * 用户付款同步回调
	 * @param request
	 * @param response
	 * @param bean
	 * @return
	 */
	@RequestMapping(RechargeFeeDefine.PAY_RETURN_ACTION)
	public ModelAndView rechargeFeePayReturn(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute ChinapnrBean bean) {
		ModelAndView modelAndView = new ModelAndView();
		bean.convert();
		// 错误消息
		String data = "";
		// 获取当前记录id
		MerPriv merPriv = bean.getMerPrivPo();
		Integer recordId = merPriv.getRecordId();
		if (recordId == null) {
			modelAndView = new ModelAndView(RechargeFeeDefine.PAGE_ERROR);
			modelAndView.addObject(RechargeFeeDefine.MESSAGE, "汇付回调时私有域为空");
			return modelAndView;
		}
		// 订单id
		String ordId = bean.getOrdId();
		// 校验此笔订单
//		JSONObject ret = new JSONObject();
//		Boolean flag = this.rechargeFeeService.checkRecordIsPay(recordId, ordId, ret);
//		if(!flag){
//			modelAndView = new ModelAndView(RechargeFeeDefine.PAGE_ERROR);
//			modelAndView.addObject(RechargeFeeDefine.MESSAGE, ret.getString("data"));
//			return modelAndView;
//		}
		// excusive的uuid
		String uuid = request.getParameter("uuid");
		// 状态返回码
		String respCode = bean.getRespCode();
		boolean updateFlag = false;
		if (StringUtils.isNotBlank(uuid)) {
			// 取得检证数据
			ChinapnrExclusiveLogWithBLOBs record = chinapnrService.selectChinapnrExclusiveLog(Long.parseLong(uuid));
			// 如果检证数据状态为未发送
			if (record != null) {
				respCode = record.getRespcode();
				// 检证通过时
				if (ChinaPnrConstant.STATUS_VERTIFY_OK.equals(record.getStatus())) {
					// 将状态更新成[2:处理中]
					record.setId(Long.parseLong(uuid));
					record.setResult(bean.getAllParams().toString());
					record.setStatus(ChinaPnrConstant.STATUS_RUNNING);
					int cnt = this.chinapnrService.updateChinapnrExclusiveLog(record);
					if (cnt > 0) {
						updateFlag = true;
					}
				}
			}
		}
		// 发送状态
		String status = ChinaPnrConstant.STATUS_VERTIFY_OK;
		//返回结果
		boolean successFlag = false;
		if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))
				|| ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)) {
			successFlag = true;
		}
		// 未调用过异步请求
		if (updateFlag) {
			if (successFlag) {
				boolean afterDealFlag = false;
				// 付款成功，写各个表
				try {
					String ip =GetCilentIP.getIpAddr(request);
					afterDealFlag = this.rechargeFeeService.updateRecordSuccess(recordId, ordId,ip);
					if (afterDealFlag) {
						data = "交易成功";
						status = ChinaPnrConstant.STATUS_SUCCESS;
					} else {
						data = "交易失败，更新过程中出错";
						status = ChinaPnrConstant.STATUS_FAIL;
						this.rechargeFeeService.updateRecordFail(recordId, ordId);
					}
				} catch (Exception e) {
					data = "交易失败，更新数据出现异常";
					status = ChinaPnrConstant.STATUS_FAIL;
					e.printStackTrace();
				}
			} else {
				data = bean.getRespDesc();
				status = ChinaPnrConstant.STATUS_FAIL;
				this.rechargeFeeService.updateRecordFail(recordId, ordId);
			}
		} else {
			//成功
			if(successFlag){
				status = ChinaPnrConstant.STATUS_SUCCESS;
			}else{
				data = bean.getRespDesc();
				status = ChinaPnrConstant.STATUS_FAIL;
				this.rechargeFeeService.updateRecordFail(recordId, ordId);
			}
		}
		// 更新状态记录
		if (updateFlag && Validator.isNotNull(uuid)) {
			this.chinapnrService.updateChinapnrExclusiveLogStatus(Long.parseLong(uuid), status);
		}
		if (status.equals(ChinaPnrConstant.STATUS_FAIL)) {
			modelAndView = new ModelAndView(RechargeFeeDefine.PAGE_ERROR);
			modelAndView.addObject(RechargeFeeDefine.MESSAGE, data);
		} else {
			modelAndView = new ModelAndView(RechargeFeeDefine.PAGE_SUCCESS);
			modelAndView.addObject("balance",bean.getTransAmt());
		}
		return modelAndView;
	}
	// pay模块异步回调此方法
	@ResponseBody
	@RequestMapping(RechargeFeeDefine.PAY_RETURN_BG_ACTION)
	public String rechargeFeePayReturnBg(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute ChinapnrBean bean) {
		bean.convert();
		// 错误消息
		String data = "ERROR";
		// 获取当前记录id
		MerPriv merPriv = bean.getMerPrivPo();
		Integer recordId = merPriv.getRecordId();
		//汇付回调时私有域为空
		if (recordId == null) {
			return data;
		}
		// 订单id
		String orderId = bean.getOrdId();
		// 校验此笔订单
//		JSONObject ret = new JSONObject();
//		boolean flag = this.rechargeFeeService.checkRecordIsPay(recordId, orderId, ret);
//		if (!flag) {
//			return data;
//		}
		// excusive的uuid
		String uuid = request.getParameter("uuid");
		// 状态返回码
		String respCode = bean.getRespCode();
		boolean updateFlag = false;
		if (StringUtils.isNotBlank(uuid)) {
			// 取得检证数据
			ChinapnrExclusiveLogWithBLOBs record = chinapnrService.selectChinapnrExclusiveLog(Long.parseLong(uuid));
			// 如果检证数据状态为未发送
			if (record != null) {
				respCode = record.getRespcode();
				// 检证通过时
				if (ChinaPnrConstant.STATUS_VERTIFY_OK.equals(record.getStatus())) {
					// 将状态更新成[2:处理中]
					record.setId(Long.parseLong(uuid));
					record.setResult(bean.getAllParams().toString());
					record.setStatus(ChinaPnrConstant.STATUS_RUNNING);
					int cnt = this.chinapnrService.updateChinapnrExclusiveLog(record);
					if (cnt > 0) {
						updateFlag = true;
					}
				}
			}
		}
		// 发送状态
		String status = ChinaPnrConstant.STATUS_VERTIFY_OK;
		//返回结果
		boolean successFlag = false;
		if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))
				|| ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)) {
			successFlag = true;
		}
		// 未调用过异步请求
		if (updateFlag) {
			if (successFlag) {
				boolean afterDealFlag = false;
				// 成功，写各个表
				try {
					String ip =GetCilentIP.getIpAddr(request);
					afterDealFlag = this.rechargeFeeService.updateRecordSuccess(recordId, orderId, ip);
					if (afterDealFlag) {
						data = "OK";
						status = ChinaPnrConstant.STATUS_SUCCESS;
					} else {
						data="ERROR";
						status = ChinaPnrConstant.STATUS_FAIL;
						this.rechargeFeeService.updateRecordFail(recordId, orderId);
					}
				} catch (Exception e) {
					data="ERROR";
					status = ChinaPnrConstant.STATUS_FAIL;
					e.printStackTrace();
				}
			} else {
				data="ERROR";
				status = ChinaPnrConstant.STATUS_FAIL;
				this.rechargeFeeService.updateRecordFail(recordId, orderId);
			}
		} 
		// 更新状态记录
		if (updateFlag && Validator.isNotNull(uuid)) {
			this.chinapnrService.updateChinapnrExclusiveLogStatus(Long.parseLong(uuid), status);
		}
		LogUtil.endLog(THIS_CLASS, RechargeFeeDefine.PAY_RETURN_BG_ACTION, "[交易完成后,回调结束]");
		return data;
	}
	
	
	/**
	 * 数据导出
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RechargeFeeDefine.EXPORT_ACTION)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, RechargeFeeBean form) throws Exception {
		LogUtil.startLog(THIS_CLASS, RechargeFeeDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "充值管理详情数据";
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		int userId = WebUtils.getUserId(request);
		RechargeCustomize rechargeCustomize = new RechargeCustomize();
		if(StringUtils.isNotEmpty(startDate)){
			rechargeCustomize.setStartDate(startDate);
		}
		if(StringUtils.isNotEmpty(endDate)){
			rechargeCustomize.setEndDate(endDate);
		}
		rechargeCustomize.setStatusSearch("1");//成功
		rechargeCustomize.setUserId(userId);
		List<RechargeCustomize> rechargeCustomizes = this.rechargeFeeService.queryRechargeList(rechargeCustomize);
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "订单号", "用户名", "充值渠道", "充值类型", "充值银行", "银行卡号", "充值金额", "手续费", "垫付手续费", "到账金额", "充值状态", "充值平台", "充值时间" };
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();

		// 生成一个表格
		HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");

		if (rechargeCustomizes != null && rechargeCustomizes.size() > 0) {

			int sheetCount = 1;
			int rowNum = 0;

			for (int i = 0; i < rechargeCustomizes.size(); i++) {
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
					RechargeCustomize record = rechargeCustomizes.get(i);
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					} else if (celLength == 1) {
						cell.setCellValue(record.getNid());
					} else if (celLength == 2) {
						cell.setCellValue(record.getUsername());
					} else if (celLength == 3) {
						cell.setCellValue(record.getType());// 充值渠道
					} else if (celLength == 4) {
						if (record.getGateType() != null && "B2C".equals(record.getGateType())) {
							cell.setCellValue("个人网银充值"); // 充值类型
						} else if (record.getGateType() != null && "B2B".equals(record.getGateType())) {
							cell.setCellValue("企业网银充值"); // 充值类型
						} else if (record.getGateType() != null && "QP".equals(record.getGateType())) {
							cell.setCellValue("快捷充值"); // 充值类型
						} else {
							cell.setCellValue(record.getGateType()); // 充值类型
						}
					} else if (celLength == 5) {
						cell.setCellValue(record.getBankName());
					} else if (celLength == 6) {
						cell.setCellValue(record.getCardid());
					} else if (celLength == 7) {
						cell.setCellValue(record.getMoney() + "");
					} else if (celLength == 8) {
						cell.setCellValue(record.getFee() != null ? (record.getFee() + "") : (0 + ""));
					} else if (celLength == 9) {
						cell.setCellValue(record.getDianfuFee() != null ? (record.getDianfuFee() + "") : (0 + ""));
					} else if (celLength == 10) {
						cell.setCellValue(record.getBalance() + "");
					} else if (celLength == 11) {
						cell.setCellValue(record.getStatus());
					} else if (celLength == 12) {
						cell.setCellValue(record.getClient());
					} else if (celLength == 13) {
						cell.setCellValue(record.getCreateTime());
					}
					// 以下都是空
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(THIS_CLASS, RechargeFeeDefine.EXPORT_ACTION);

	}
}
