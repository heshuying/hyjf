package com.hyjf.admin.finance.subcommission;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.finance.recharge.RechargeController;
import com.hyjf.admin.finance.recharge.RechargeService;
import com.hyjf.admin.manager.config.subconfig.SubConfigBean;
import com.hyjf.admin.manager.config.subconfig.SubConfigService;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.auto.SubCommission;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.SubCommissionListConfigCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商户账户分佣Controller
 *
 * @author liuyang
 */
@Controller
@RequestMapping(SubCommissionDefine.REQUEST_MAPPING)
public class SubCommissionController extends BaseController {

	@Autowired
	private SubCommissionService subCommissionService;
	@Autowired
    private SubConfigService subConfigService;
	@Autowired
	private RechargeService rechargeService;

	Logger _log = LoggerFactory.getLogger(SubCommissionController.class);

	/**
	 * 画面初始化
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(SubCommissionDefine.INIT_ACTION)
	@RequiresPermissions(SubCommissionDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, SubCommissionBean form) {
		LogUtil.startLog(SubCommissionDefine.THIS_CLASS, SubCommissionDefine.INIT_ACTION);
		ModelAndView modelAndView = new ModelAndView(SubCommissionDefine.LIST_PATH);
		// 转账状态
		List<ParamName> transferStatus = this.subCommissionService.getParamNameList("FS_TRANSFER_STATUS");
		modelAndView.addObject("transferStatus", transferStatus);
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(SubCommissionDefine.THIS_CLASS, SubCommissionDefine.INIT_ACTION);
		return modelAndView;
	}

	/**
	 * 画面检索
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(SubCommissionDefine.SEARCH_ACTION)
	@RequiresPermissions(SubCommissionDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request, SubCommissionBean form) {
		LogUtil.startLog(SubCommissionDefine.THIS_CLASS, SubCommissionDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(SubCommissionDefine.LIST_PATH);
		List<ParamName> transferStatus = this.subCommissionService.getParamNameList("FS_TRANSFER_STATUS");
		modelAndView.addObject("transferStatus", transferStatus);
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(SubCommissionDefine.THIS_CLASS, SubCommissionDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 分页技能
	 *
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, SubCommissionBean form) {
		Integer count = this.subCommissionService.countSubCommissionList(form);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			form.setPaginator(paginator);
			List<SubCommission> subCommissionList = this.subCommissionService.searchSubCommissionList(form, paginator.getOffset(), paginator.getLimit());
			form.setRecordList(subCommissionList);
		}
		modelAndView.addObject(SubCommissionDefine.FORM, form);
	}

	/**
	 * 添加按钮,跳转添加详情画面
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(SubCommissionDefine.DETAIL_ACTION)
	@RequiresPermissions(SubCommissionDefine.PERMISSIONS_ADD)
	public ModelAndView detailAction(HttpServletRequest request, SubCommissionBean form) {
		LogUtil.startLog(SubCommissionDefine.THIS_CLASS, SubCommissionDefine.DETAIL_ACTION);
		ModelAndView modelAndView = new ModelAndView(SubCommissionDefine.DETAIL_PATH);
		// 转出方用户电子账户号
		form.setAccountId(CustomConstants.HYJF_BANK_MERS_ACCOUNT);
		// TODO 转入方用户ID
//		List<SubCommissionListBean> list = new ArrayList<SubCommissionListBean>();
//		SubCommissionListBean bean = new  SubCommissionListBean();
//		// 线上
//		bean.setUserId("337707");
//		bean.setUserName("hyjf809861");
//		bean.setAccountId("6212461910001923405");
		// 第三套环境
//		bean.setUserId("804");
//		bean.setUserName("hyjf090218");
//		bean.setAccountId("6212461910000119492");

//		SubCommissionListBean bean1 = new  SubCommissionListBean();
//		// 线上
//		bean1.setUserId("332778");
//		bean1.setUserName("hyjf18657138102");
//		bean1.setAccountId("6212461910001923397");
		// 第三套环境
//		bean1.setUserId("803");
//		bean1.setUserName("hyjf090217");
//		bean1.setAccountId("6212461910000009503");


//		list.add(bean);
//		list.add(bean1);

//		// Integer receiveUserId = 779;
//		Integer receiveUserId = 337707;
//		// 根据用户ID检索用户名
//		Users receiveUser = this.subCommissionService.getUsersByUserId(receiveUserId);
//		// 根据用户ID查询用户开户信息
//		BankOpenAccount receiveUserAccount = this.subCommissionService.getBankOpenAccount(receiveUserId);
//		// 收款方用户ID
//		form.setReceiveUserId(receiveUserId);
//		// 收款方用户名
//		form.setReceiveUserName(receiveUser == null ? "" : receiveUser.getUsername());
//		// 收款方用户电子账户号
//		form.setReceiveAccountId(receiveUserAccount == null ? "" : receiveUserAccount.getAccount());
		List<SubCommissionListConfigCustomize> userNameList=subCommissionService.users();
		modelAndView.addObject("userNameList", userNameList);
		// 余额
		BigDecimal balance = this.subCommissionService.getBankBalance(Integer.parseInt(ShiroUtil.getLoginUserId()), CustomConstants.HYJF_BANK_MERS_ACCOUNT);
		form.setBalance(balance.toString());// 账户余额
		modelAndView.addObject(SubCommissionDefine.FORM, form);
		LogUtil.endLog(SubCommissionDefine.THIS_CLASS, SubCommissionDefine.DETAIL_ACTION);
		return modelAndView;
	}

	/**
	 * 佣金分账
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = SubCommissionDefine.TRANSFER_ACTION)
	@RequiresPermissions(SubCommissionDefine.PERMISSIONS_ADD)
	public ModelAndView transferAction(HttpServletRequest request, SubCommissionBean form) {
		LogUtil.startLog(SubCommissionDefine.THIS_CLASS, SubCommissionDefine.TRANSFER_ACTION);
		ModelAndView modelAndView = new ModelAndView(SubCommissionDefine.DETAIL_PATH);
		// 登陆用户ID
		Integer loginUserId = Integer.parseInt(ShiroUtil.getLoginUserId());
		// 转出用户电子账户号
		String accountId = form.getAccountId();
		// 转入用户电子账户号
		String receiveAccountId = form.getReceiveAccountId();
		// 分佣金额
		String txAmount = form.getTxAmount();
		// 余额
		BigDecimal balance = this.subCommissionService.getBankBalance(loginUserId, accountId);
		form.setBalance(balance.toString());// 账户余额
		//根据id获取用户信息
		Users users=this.subCommissionService.getUsersByUserId(form.getReceiveUserId());
		if (users!=null) {
			form.setReceiveUserName(users.getUsername());
		}
		this.subCommissionService.checkTransferParam(modelAndView, form);
		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.addObject(SubCommissionDefine.FORM, form);
			return modelAndView;
		} else {
			// 调用江西银行接口分佣
			try {
				BankCallBean bean = new BankCallBean();
				String channel = BankCallConstant.CHANNEL_PC;
				String orderDate = GetOrderIdUtils.getOrderDate();
				String txDate = GetOrderIdUtils.getTxDate();
				String txTime = GetOrderIdUtils.getTxTime();
				String seqNo = GetOrderIdUtils.getSeqNo(6);
				bean.setVersion(BankCallConstant.VERSION_10);// 版本号
				bean.setTxCode(BankCallConstant.TXCODE_FEE_SHARE);// 交易代码
				bean.setTxDate(txDate);
				bean.setTxTime(txTime);
				bean.setSeqNo(seqNo);
				bean.setChannel(channel);
				bean.setAccountId(accountId);// 电子账号
				bean.setCurrency(BankCallConstant.CURRENCY_TYPE_RMB);// 币种
				bean.setTxAmount(CustomUtil.formatAmount(txAmount)); // 分账金额
				bean.setForAccountId(receiveAccountId);// 对手电子账号
				bean.setDescription("手续费分账");
				bean.setLogOrderId(GetOrderIdUtils.getOrderId2(loginUserId));
				bean.setLogUserId(String.valueOf(loginUserId));
				bean.setLogOrderDate(orderDate);
				bean.setLogRemark("手续费分账");
				// 请求前插入记录表
				boolean insertFlag = this.subCommissionService.insetSubCommissionLog(bean, form);
				if (insertFlag) {
					// 调用银行接口
					BankCallBean resultBean = BankCallUtils.callApiBg(bean);
					if (resultBean == null) {
						_log.info("调用银行接口失败,银行返回空.订单号:[" + bean.getLogOrderId() + "].");
						this.subCommissionService.updateSubCommission(bean);
						ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "txAmount", "feeshare.transfer.error", "调用银行接口失败");
						modelAndView.addObject(SubCommissionDefine.FORM, form);
						return modelAndView;
					}
					// 银行返回响应代码
					String retCode = resultBean == null ? "" : resultBean.getRetCode();
					if("CA51".equals(retCode)){
						// 更新订单状态:失败
						this.subCommissionService.updateSubCommission(resultBean);
						// 转账成功，更新状态失败
						ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "txAmount", "feeshare.transfer.txamount.error", "账户余额不足");
						modelAndView.addObject(SubCommissionDefine.FORM, form);
						return modelAndView;
					}
					// 调用银行接口失败
					if (!BankCallStatusConstant.RESPCODE_SUCCESS.equals(retCode)) {
						// 更新订单状态:失败
						this.subCommissionService.updateSubCommission(resultBean);
						// 转账成功，更新状态失败
						ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "txAmount", "feeshare.transfer.error", "调用银行接口失败");
						modelAndView.addObject(SubCommissionDefine.FORM, form);
						return modelAndView;
					}
					// 银行返回成功
					// 更新订单,用户账户等信息
					boolean updateFlag = this.subCommissionService.updateSubCommissionSuccess(resultBean, form);
					if (!updateFlag) {
						_log.info("调用银行成功后,更新数据失败");
						// 转账成功，更新状态失败
						ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "txAmount", "feeshare.transfer.success", "调用银行成功后,更新数据失败");
						modelAndView.addObject(SubCommissionDefine.FORM, form);
						return modelAndView;
					}
				}
			} catch (Exception e) {
				_log.info("转账发生异常:异常信息:[" + e.getMessage() + "].");
				ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "txAmount", "feeshare.transfer.exception", "转账发生异常");
				modelAndView.addObject(SubCommissionDefine.FORM, form);
				return modelAndView;
			}
			modelAndView.addObject(SubCommissionDefine.FORM, form);
			modelAndView.addObject(SubCommissionDefine.SUCCESS, SubCommissionDefine.SUCCESS);
			LogUtil.endLog(SubCommissionDefine.THIS_CLASS, SubCommissionDefine.TRANSFER_ACTION);
			return modelAndView;
		}
	}

	/**
	 * 数据导出
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(SubCommissionDefine.EXPORT_ACTION)
	@RequiresPermissions(SubCommissionDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, SubCommissionBean form) throws Exception {
		LogUtil.startLog(SubCommissionDefine.THIS_CLASS, SubCommissionDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "账户分佣明细";
		List<SubCommission> subCommissionList = this.subCommissionService.searchSubCommissionList(form, -1, -1);
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
		String[] titles = new String[] { "序号", "转账订单号", "转出电子账户号", "转账金额", "转入用户名","转入姓名", "转入电子账户号", "转账状态", "转账时间", "操作人", "备注" ,"发送日期" ,"发送时间" ,"系统跟踪号" };
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");
		if (subCommissionList != null && subCommissionList.size() > 0) {
			int sheetCount = 1;
			int rowNum = 0;
			for (int i = 0; i < subCommissionList.size(); i++) {
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
					SubCommission record = subCommissionList.get(i);
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					} else if (celLength == 1) {
						cell.setCellValue(StringUtils.isEmpty(record.getOrderId()) ? StringUtils.EMPTY : record.getOrderId());
					} else if (celLength == 2) {
						cell.setCellValue(StringUtils.isEmpty(record.getAccountId()) ? StringUtils.EMPTY : record.getAccountId());
					} else if (celLength == 3) {
						cell.setCellValue(record.getAccount() == null ? "0" : record.getAccount().toString());
					} else if (celLength == 4) {
						cell.setCellValue(StringUtils.isEmpty(record.getReceiveUserName()) ? StringUtils.EMPTY : record.getReceiveUserName());
					} else if (celLength == 5) {
						cell.setCellValue(StringUtils.isEmpty(record.getTruename()) ? StringUtils.EMPTY : record.getTruename());
					} else if (celLength == 6) {
						cell.setCellValue(StringUtils.isEmpty(record.getReceiveAccountId()) ? StringUtils.EMPTY : record.getReceiveAccountId());
					} else if (celLength == 7) {
						// 转账状态
						List<ParamName> transferStatus = this.subCommissionService.getParamNameList("FS_TRANSFER_STATUS");
						for (int j = 0; j < transferStatus.size(); j++) {
							if (transferStatus.get(j).getNameCd().equals(String.valueOf(record.getTradeStatus()))) {
								cell.setCellValue(transferStatus.get(j).getName());
							}
						}
					} else if (celLength == 8) {
						if (record.getCreateTime() == null) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(GetDate.timestamptoStrYYYYMMDDHHMMSS(record.getCreateTime()));
						}
					} else if (celLength == 9) {
						cell.setCellValue(StringUtils.isEmpty(record.getCreateUserName()) ? StringUtils.EMPTY : record.getCreateUserName());
					} else if (celLength == 10) {
						cell.setCellValue(StringUtils.isEmpty(record.getRemark()) ? StringUtils.EMPTY : record.getRemark());
					} else if (celLength == 11) {
						String tmpTxDate = record.getTxDate().toString();
						cell.setCellValue(StringUtils.isEmpty(record.getTxDate().toString()) ? StringUtils.EMPTY : tmpTxDate.substring(0, 4) + "-" + tmpTxDate.substring(4, 6) + "-" + tmpTxDate.substring(6, 8));
					} else if (celLength == 12) {
						String tmpTxTime = String.format("%06d", record.getTxTime());
						cell.setCellValue(StringUtils.isEmpty(record.getTxTime().toString()) ? StringUtils.EMPTY : tmpTxTime.substring(0, 2) + ":" + tmpTxTime.substring(2, 4) + ":" + tmpTxTime.substring(4, 6));
					} else if (celLength == 13) {
						cell.setCellValue(StringUtils.isEmpty(record.getSeqNo()) ? StringUtils.EMPTY : record.getSeqNo());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(request, response, workbook, titles, fileName);
		LogUtil.endLog(SubCommissionDefine.THIS_CLASS, SubCommissionDefine.EXPORT_ACTION);

	}
	/**
	 * 
	 */
	 @ResponseBody
     @RequestMapping(SubCommissionDefine.USERNAME)
     public  Map<String, Object> usernameInfo(HttpServletRequest request, HttpServletResponse response,SubConfigBean from) {
		 LogUtil.startLog(RechargeController.class.toString(), SubCommissionDefine.USERNAME);
		 Map<String, Object> userMapNullMap=new HashMap();
		 Map<String, Object> userMap=subConfigService.getUserInfo(from);
		 
			if (userMap!=null) {
				userMap.put("status", "y");
				return userMap;
			}else {
				userMapNullMap.put("info", "您输入的用户名无对应信息，请重新输入");
				userMapNullMap.put("status", "n");
				return userMapNullMap;
			}
	 }

}
