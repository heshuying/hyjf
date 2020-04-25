package com.hyjf.admin.manager.borrow.borrowcredit;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.borrow.borrow.BorrowController;
import com.hyjf.admin.manager.borrow.borrowfirst.BorrowFirstDefine;
import com.hyjf.bank.service.mq.MqService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.*;
import com.hyjf.mybatis.messageprocesser.AppMsMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.BorrowCredit;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.BorrowCreditCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditRecordDetailCustomize;
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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = BorrowCreditDefine.REQUEST_MAPPING)
public class BorrowCreditController extends BaseController {
	@Autowired
	private MqService mqService;
	@Autowired
	private BorrowCreditService borrowCreditService;
	@Autowired
	@Qualifier("appMsProcesser")
	private MessageProcesser appMsProcesser;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowCreditDefine.INIT)
	@RequiresPermissions(BorrowCreditDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("borrowfirstForm") BorrowCreditBean form) {
		LogUtil.startLog(BorrowCreditController.class.toString(), BorrowCreditDefine.INIT);
		ModelAndView modeAndView = new ModelAndView(BorrowCreditDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form, null);
		LogUtil.endLog(BorrowCreditController.class.toString(), BorrowCreditDefine.INIT);
		return modeAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowCreditDefine.SEARCH_ACTION)
	@RequiresPermissions(BorrowCreditDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, HttpServletResponse response, BorrowCreditBean form) {
		LogUtil.startLog(BorrowCreditController.class.toString(), BorrowCreditDefine.SEARCH_ACTION);
		ModelAndView modeAndView = new ModelAndView(BorrowCreditDefine.LIST_PATH);
		String export = request.getParameter("export");
		// 创建分页
		this.createPage(request, modeAndView, form, export);
		if (export != null && !export.equals("") && !export.equals("undefind")) {
			List<BorrowCreditCustomize> recordList = (List<BorrowCreditCustomize>) modeAndView.getModel().get(
					"recordList");
			// 导出列表
			String[] keys = new String[] { "债转编号", "项目编号", "用户名", "债权本金", "转让本金", "折让率", "转让价格", "已转让金额", "发布时间",
					"还款时间", "转让状态", "发起平台" };
			String tmarray = ",creditNid,username,bidNid,creditCapital,creditCapitalPrice,"
					+ "creditDiscount,creditPrice,creditCapitalAssigned,addTime,repayLastTime,creditStatusName,client,";
			JxlExcelUtils.exportexcle(response, "债权转让列表", recordList, "work", keys, BorrowCreditCustomize.class, null,
					tmarray);
			return null;
		}
		LogUtil.endLog(BorrowCreditController.class.toString(), BorrowCreditDefine.SEARCH_ACTION);
		return modeAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modeAndView, BorrowCreditBean borrowCreditBean,
			String export) {

		BorrowCreditCustomize borrowCreditCustomize = new BorrowCreditCustomize();
		// 用户名
		borrowCreditCustomize.setUsernameSrch(borrowCreditBean.getUsernameSrch());
		// 债转编号
		borrowCreditCustomize.setCreditNidSrch(borrowCreditBean.getCreditNidSrch());
		// 项目编号
		borrowCreditCustomize.setBidNidSrch(borrowCreditBean.getBidNidSrch());
		// 转让状态
		borrowCreditCustomize.setCreditStatusSrch(borrowCreditBean.getCreditStatusSrch());
		// 时间
		borrowCreditCustomize.setTimeStartSrch(borrowCreditBean.getTimeStartSrch());
		// 时间
		borrowCreditCustomize.setTimeEndSrch(borrowCreditBean.getTimeEndSrch());
		// 客户端
		borrowCreditCustomize.setClient(borrowCreditBean.getClient());
		// 转让状态
		modeAndView.addObject("creditStatusList",
				this.borrowCreditService.getParamNameList(CustomConstants.CREDIT_STATUS));

		Integer count = this.borrowCreditService.countBorrowCredit(borrowCreditCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(borrowCreditBean.getPaginatorPage(), count);
			borrowCreditCustomize.setLimitStart(paginator.getOffset());
			if (export != null && !export.equals("") && !export.equals("undefind")) {
				borrowCreditCustomize.setLimitEnd(9999999);
			} else {
				borrowCreditCustomize.setLimitEnd(paginator.getLimit());
			}
			List<BorrowCreditCustomize> recordList = this.borrowCreditService
					.selectBorrowCreditList(borrowCreditCustomize);
			borrowCreditBean.setPaginator(paginator);
			modeAndView.addObject("recordList", recordList);
			/*----add by LSY START---------*/
			BorrowCreditCustomize sumCredit = this.borrowCreditService.sumBorrowCredit(borrowCreditCustomize);
			modeAndView.addObject("sumCredit", sumCredit);
			/*----add by LSY END---------*/
		}
		modeAndView.addObject(BorrowCreditDefine.BORROW_FORM, borrowCreditBean);
	}

	/**
	 * 详细画面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = BorrowCreditDefine.INFO_ACTION)
	@RequiresPermissions(BorrowCreditDefine.PERMISSIONS_INFO)
	public ModelAndView bailInfoAction(HttpServletRequest request, BorrowCreditBean form) {
		LogUtil.startLog(BorrowCreditController.class.toString(), BorrowCreditDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowCreditDefine.INFO_PATH);
		// 创建分页
		form.setCreditNidSrch(form.getCreditNid());
		this.createInfoPage(request, modelAndView, form);

		LogUtil.endLog(BorrowCreditController.class.toString(), BorrowCreditDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createInfoPage(HttpServletRequest request, ModelAndView modeAndView, BorrowCreditBean borrowCreditBean) {

		BorrowCreditCustomize borrowCreditCustomize = new BorrowCreditCustomize();
		// 债转编号
		borrowCreditCustomize.setCreditNidSrch(borrowCreditBean.getCreditNidSrch());
		// 项目编号
		borrowCreditCustomize.setBidNidSrch(borrowCreditBean.getBidNidSrch());

		Integer count = this.borrowCreditService.countBorrowCreditInfoList(borrowCreditCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(borrowCreditBean.getPaginatorPage(), count);
			borrowCreditCustomize.setLimitStart(paginator.getOffset());
			borrowCreditCustomize.setLimitEnd(paginator.getLimit());
			List<BorrowCreditCustomize> recordList = this.borrowCreditService
					.selectBorrowCreditInfoList(borrowCreditCustomize);
			borrowCreditBean.setPaginator(paginator);
			modeAndView.addObject("recordList", recordList);
			/*-----add by LSY START--------*/
			BorrowCreditCustomize sumCreditInfo = this.borrowCreditService.sumBorrowCreditInfo(borrowCreditCustomize);
			modeAndView.addObject("sumCreditInfo", sumCreditInfo);
			/*-----add by LSY END--------*/
		}
		modeAndView.addObject(BorrowCreditDefine.BORROW_FORM, borrowCreditBean);
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(BorrowCreditDefine.EXPORT_ACTION)
	@RequiresPermissions(BorrowCreditDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, BorrowCreditBean borrowCreditBean)
			throws Exception {
		LogUtil.startLog(BorrowController.class.toString(), BorrowCreditDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "汇转让列表";

		BorrowCreditCustomize borrowCreditCustomize = new BorrowCreditCustomize();
		// 用户名
		borrowCreditCustomize.setUsernameSrch(borrowCreditBean.getUsernameSrch());
		// 债转编号
		borrowCreditCustomize.setCreditNidSrch(borrowCreditBean.getCreditNidSrch());
		// 项目编号
		borrowCreditCustomize.setBidNidSrch(borrowCreditBean.getBidNidSrch());
		// 转让状态
		borrowCreditCustomize.setCreditStatusSrch(borrowCreditBean.getCreditStatusSrch());
		// 时间
		borrowCreditCustomize.setTimeStartSrch(borrowCreditBean.getTimeStartSrch());
		// 时间
		borrowCreditCustomize.setTimeEndSrch(borrowCreditBean.getTimeEndSrch());

		List<BorrowCreditCustomize> resultList = this.borrowCreditService.exportBorrowCreditList(borrowCreditCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "债转编号", "用户名", "项目编号", "债权本金", "转让本金", "折让率", "转让价格", "已转让金额", "转让状态",
				"还款状态", "发布时间", "还款时间" };
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
					sheet = ExportExcel
							.createHSSFWorkbookTitle(workbook, titles, (sheetName + "_第" + sheetCount + "页"));
					rowNum = 1;
				}

				// 新建一行
				Row row = sheet.createRow(rowNum);
				// 循环数据
				for (int celLength = 0; celLength < titles.length; celLength++) {
					BorrowCreditCustomize borrowCommonCustomize = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 债转编号
					else if (celLength == 1) {
						cell.setCellValue(borrowCommonCustomize.getCreditNid());
					}
					// 用户名
					else if (celLength == 2) {
						cell.setCellValue(borrowCommonCustomize.getUsername());
					}
					// 项目编号
					else if (celLength == 3) {
						cell.setCellValue(borrowCommonCustomize.getBidNid());
					}
					// 债权本金
					else if (celLength == 4) {
						cell.setCellValue(borrowCommonCustomize.getCreditCapital());
					}
					// 转让本金
					else if (celLength == 5) {
						cell.setCellValue(borrowCommonCustomize.getCreditCapitalPrice());
					}
					// 折让率
					else if (celLength == 6) {
						cell.setCellValue(borrowCommonCustomize.getCreditDiscount());
					}
					// 转让价格
					else if (celLength == 7) {
						cell.setCellValue(borrowCommonCustomize.getCreditPrice());
					}
					// 已转让金额
					else if (celLength == 8) {
						cell.setCellValue(borrowCommonCustomize.getCreditCapitalAssigned());
					}
					// 转让状态
					else if (celLength == 9) {
						cell.setCellValue(borrowCommonCustomize.getCreditStatusName());
					}
					// 还款状态
					else if (celLength == 10) {
						cell.setCellValue(borrowCommonCustomize.getRepayStatusName());
					}
					// 发布时间
					else if (celLength == 11) {
						cell.setCellValue(borrowCommonCustomize.getAddTime());
					}
					// 还款时间
					else if (celLength == 12) {
						cell.setCellValue(borrowCommonCustomize.getRepayLastTime());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(BorrowController.class.toString(), BorrowCreditDefine.EXPORT_ACTION);
	}

	/**
	 * 详细画面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = BorrowCreditDefine.CANCEL_INFO_ACTION)
	@RequiresPermissions(BorrowCreditDefine.PERMISSIONS_CANCEL)
	public ModelAndView cancelInfoAction(HttpServletRequest request, BorrowCreditBean form) {
		LogUtil.startLog(BorrowCreditController.class.toString(), BorrowCreditDefine.CANCEL_INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowCreditDefine.CANCEL_PATH);

		String creditNid = form.getCreditNid();
		BorrowCredit borrowCredit = new BorrowCredit();
		borrowCredit.setCreditNid(Integer.valueOf(creditNid));

		borrowCredit = this.borrowCreditService.getBorrowCredit(borrowCredit);
		Users users = this.borrowCreditService.getUsers(borrowCredit.getCreditUserId());
		// 出让人
		modelAndView.addObject("userName", users.getUsername());
		// 债权编号
		modelAndView.addObject("creditNid", borrowCredit.getCreditNid());
		// 项目编号
		modelAndView.addObject("bidNid", borrowCredit.getBidNid());
		// 债转本金
		modelAndView.addObject("creditCapital", borrowCredit.getCreditCapital());
		// 出让时间
		modelAndView.addObject("addTime",
				GetDate.timestamptoStrYYYYMMDDHHMMSS(String.valueOf(borrowCredit.getAddTime())));

		LogUtil.endLog(BorrowCreditController.class.toString(), BorrowCreditDefine.CANCEL_INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 取消
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = BorrowCreditDefine.CANCEL_ACTION)
	@RequiresPermissions(BorrowCreditDefine.PERMISSIONS_INFO)
	public ModelAndView cancelAction(HttpServletRequest request, BorrowCreditBean form) {
		LogUtil.startLog(BorrowCreditController.class.toString(), BorrowCreditDefine.CANCEL_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowCreditDefine.CANCEL_PATH);

		String creditnId = form.getCreditNid();
		BorrowCredit borrowCredit = new BorrowCredit();
		borrowCredit.setCreditNid(Integer.valueOf(creditnId));
		borrowCredit.setCreditStatus(1);

		this.borrowCreditService.updateBorrowCredit(borrowCredit);

		borrowCredit = this.borrowCreditService.getBorrowCredit(borrowCredit);
		// add 合规数据上报 埋点 liubin 20181122 start
		//停止债转并且没有被承接过
		if (borrowCredit.getCreditCapitalAssigned().compareTo(BigDecimal.ZERO) == 0) {
			JSONObject params = new JSONObject();
			params.put("creditNid", borrowCredit.getCreditNid()+"");
			params.put("flag", "1");//1（散）2（智投）
			params.put("status", "3"); //3承接（失败）
			// 推送数据到MQ 承接（失败）散
			mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.UNDERTAKE_ALL_FAIL_DELAY_KEY, JSONObject.toJSONString(params));
		}else{
			// 推送数据到MQ 承接（完全）散
            JSONObject params = new JSONObject();
			params.put("creditNid", borrowCredit.getCreditNid()+"");
			params.put("flag", "1"); //1（散）2（智投）
			params.put("status", "2"); //2承接（成功）
			this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.UNDERTAKE_ALL_SUCCESS_DELAY_KEY, JSONObject.toJSONString(params));
		}
		// add 合规数据上报 埋点 liubin 20181122 end

		UsersInfo usersInfo = this.borrowCreditService.getUsersInfoByUserId(borrowCredit.getCreditUserId());
		Map<String, String> param = new HashMap<String, String>();
		if (usersInfo.getTruename() != null && usersInfo.getTruename().length() > 1) {
			param.put("val_name", usersInfo.getTruename().substring(0, 1));
		} else {
			param.put("val_name", usersInfo.getTruename());
		}
		if (usersInfo.getSex() == 1) {
			param.put("val_sex", "先生");
		} else if (usersInfo.getSex() == 2) {
			param.put("val_sex", "女士");
		} else {
			param.put("val_sex", "");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		// 用户id
		params.put("userId", usersInfo.getUserId());
		// 债转编号
		params.put("creditNid", creditnId);
		AppTenderCreditRecordDetailCustomize price = borrowCreditService.selectTenderCreditRecordDetail(params);
		param.put("val_amount", price.getAssignPay() + "");
		param.put("val_profit", borrowCredit.getCreditInterestAssigned() + "");
		AppMsMessage appMsMessage = new AppMsMessage(borrowCredit.getCreditUserId(), param, null,
				MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_TPL_ZHUANRANGJIESHU);
		appMsProcesser.gather(appMsMessage);

		modelAndView.addObject(BorrowFirstDefine.SUCCESS, BorrowFirstDefine.SUCCESS);
		LogUtil.endLog(BorrowCreditController.class.toString(), BorrowCreditDefine.CANCEL_ACTION);
		return modelAndView;
	}
}
