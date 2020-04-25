package com.hyjf.admin.manager.borrow.credit.tender;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.borrow.borrow.BorrowController;
import com.hyjf.admin.manager.borrow.credittender.CreditTenderController;
import com.hyjf.admin.manager.hjhplan.planlist.PlanListDefine;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractBean;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractConstant;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.*;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.admin.HjhDebtCreditTenderCustomize;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = HjhDebtCreditTenderDefine.REQUEST_MAPPING)
public class HjhDebtCreditTenderController extends BaseController {

	@Autowired
	private HjhDebtCreditTenderService planCreditTenderService;

	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;

	/**
	 * 画面初始化
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HjhDebtCreditTenderDefine.INIT)
	//@RequiresPermissions(HjhDebtCreditTenderDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, HttpServletResponse response,@ModelAttribute HjhDebtCreditTenderBean form) {
		LogUtil.startLog(HjhDebtCreditTenderDefine.CONTROLLER_NAME, HjhDebtCreditTenderDefine.INIT);
		ModelAndView modeAndView = new ModelAndView(HjhDebtCreditTenderDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(HjhDebtCreditTenderDefine.CONTROLLER_NAME, HjhDebtCreditTenderDefine.INIT);
		return modeAndView;
	}

	/**
	 * 查询
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HjhDebtCreditTenderDefine.SEARCH_ACTION)
	//@RequiresPermissions(HjhDebtCreditTenderDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request, HttpServletResponse response,@ModelAttribute HjhDebtCreditTenderBean form) {
		LogUtil.startLog(HjhDebtCreditTenderDefine.CONTROLLER_NAME, HjhDebtCreditTenderDefine.SEARCH_ACTION);
		ModelAndView modeAndView = new ModelAndView(HjhDebtCreditTenderDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(HjhDebtCreditTenderDefine.CONTROLLER_NAME, HjhDebtCreditTenderDefine.SEARCH_ACTION);
		return modeAndView;
	}

	/**
	 * 创建分页机能
	 *
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, HjhDebtCreditTenderBean form) {

		// 还款方式
		List<BorrowStyle> borrowStyleList = this.planCreditTenderService.selectBorrowStyleList("");
		modelAndView.addObject("borrowStyleList", borrowStyleList);
		// 承接方式
		modelAndView.addObject("assignTypeList",this.planCreditTenderService.getParamNameList(CustomConstants.CREDIT_ASSIGN_TYPE));
		Map<String, Object> params = new HashMap<String, Object>();
		//是否从加入明细列表跳转 1:是 0:否
		if(form.getIsAccedelist()!=1){
		    form.setIsAccedelist(0);
		}
		params.put("assignPlanNid", form.getAssignPlanNid());
		params.put("assignPlanOrderId", form.getAssignPlanOrderId());
		params.put("assignUserName", form.getAssignUserName());
		params.put("creditUserName", form.getCreditUserName());
		params.put("creditNid", form.getCreditNid());
		params.put("borrowNid", form.getBorrowNid());
		params.put("repayStyle", form.getRepayStyle());
		params.put("assignType", form.getAssignType());
		params.put("assignTimeStart", StringUtils.isNotBlank(form.getAssignTimeStart())?form.getAssignTimeStart():null);
		params.put("assignTimeEnd", StringUtils.isNotBlank(form.getAssignTimeEnd())?form.getAssignTimeEnd():null);
		params.put("tenderType",form.getTenderType());
		Integer count = this.planCreditTenderService.countDebtCreditTenderList(params);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			params.put("limitStart", paginator.getOffset());
			params.put("limitEnd", paginator.getLimit());
			List<HjhDebtCreditTenderCustomize> recordList = this.planCreditTenderService.selectDebtCreditTenderList(params);
			form.setPaginator(paginator);
			Map<String,Object> sumTotal = this.planCreditTenderService.selectSumTotal(params);
			modelAndView.addObject("sum",sumTotal);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(HjhDebtCreditTenderDefine.FORM, form);
	}


	/**
	 * 导出功能
	 *
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(HjhDebtCreditTenderDefine.EXPORT_ACTION)
	@RequiresPermissions(HjhDebtCreditTenderDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, HjhDebtCreditTenderBean form) throws Exception {
		LogUtil.startLog(BorrowController.class.toString(), HjhDebtCreditTenderDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "智投服务承接记录";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("assignPlanNid", form.getAssignPlanNid());
		params.put("assignPlanOrderId", form.getAssignPlanOrderId());
		params.put("assignUserName", form.getAssignUserName());
		params.put("creditUserName", form.getCreditUserName());
		params.put("creditNid", form.getCreditNid());
		params.put("borrowNid", form.getBorrowNid());
		params.put("repayStyle", form.getRepayStyle());
		params.put("assignType", form.getAssignType());
		params.put("assignTimeStart", StringUtils.isNotBlank(form.getAssignTimeStart())?form.getAssignTimeStart():null);
		params.put("assignTimeEnd", StringUtils.isNotBlank(form.getAssignTimeEnd())?form.getAssignTimeEnd():null);
		List<HjhDebtCreditTenderCustomize> resultList = this.planCreditTenderService.selectDebtCreditTenderList(params);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号","承接人","承接智投编号","承接智投订单号","出让人","债转编号","原项目编号","还款方式","承接本金(元)","垫付利息(元)","实际支付金额(元)"," 承接时间","债转服务费率","债转服务费(元)","复投承接(是/否)","项目期数"};

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
				    HjhDebtCreditTenderCustomize debtCreditTender = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 承接人
					else if (celLength == 1) {
						cell.setCellValue(debtCreditTender.getAssignUserName());
					}

					// 承接计划编号
					else if (celLength == 2) {
						cell.setCellValue(debtCreditTender.getAssignPlanNid());
					}
					// 承接计划订单号
					else if (celLength == 3) {
						cell.setCellValue(debtCreditTender.getAssignPlanOrderId());
//						cell.setCellValue(debtCreditTender.getAssignOrderId());
					}
					// 出让人
					else if (celLength == 4) {
						cell.setCellValue(debtCreditTender.getCreditUserName());
					}
					// 债转编号
					else if (celLength == 5) {
						cell.setCellValue(debtCreditTender.getCreditNid());
					}
					// 原项目编号
					else if (celLength == 6) {
						cell.setCellValue(debtCreditTender.getBorrowNid());
					}
					// 还款方式
					else if (celLength == 7) {
						cell.setCellValue(debtCreditTender.getRepayStyleName());
					}
					// 承接本金
					else if (celLength == 8) {
						cell.setCellValue(debtCreditTender.getAssignCapital());
					}
					// 垫付利息
					else if (celLength == 9) {
						cell.setCellValue(debtCreditTender.getAssignInterestAdvance());
					}
					// 实际支付金额
					else if (celLength == 10) {
						cell.setCellValue(debtCreditTender.getAssignPay());
					}
					// 承接时间
					else if (celLength == 11) {
						cell.setCellValue(debtCreditTender.getAssignTime());
					}
					// 债转服务费率
					else if (celLength == 12){
						cell.setCellValue(debtCreditTender.getAssignServiceApr());
					}
					// 债转服务费
					else if (celLength == 13){
						cell.setCellValue(debtCreditTender.getAssignServiceFee());
					}
					/*updte  by  zhangyk 修改导出数据表格 start*/
					// 是否复投承接
					/*else if (celLength == 14){
						cell.setCellValue(debtCreditTender.getTenderType());
					}*/

					// 承接方式
					else if (celLength == 14) {
						cell.setCellValue(debtCreditTender.getTenderType());
					}
					// 项目期数
					else if (celLength == 15) {
						cell.setCellValue(debtCreditTender.getAssignPeriod()+"/"+debtCreditTender.getBorrowPeriod());
					}
					// 承接时所在期数
					/*
					else if (celLength == 16) {
						cell.setCellValue(debtCreditTender.getAssignPeriod());
					}*/

					/*updte  by  zhangyk 修改导出数据表格 end*/

				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(BorrowController.class.toString(), HjhDebtCreditTenderDefine.EXPORT_ACTION);
	}


	/**
	 * PDF脱敏图片预览
	 * @param request
	 * @return
	 */
	@RequestMapping(HjhDebtCreditTenderDefine.PDF_PREVIEW_ACTION)
	@RequiresPermissions(HjhDebtCreditTenderDefine.PERMISSIONS_PDF_PREVIEW)
	public ModelAndView pdfPreviewAction(HttpServletRequest request) {
		LogUtil.startLog(CreditTenderController.class.toString(), HjhDebtCreditTenderDefine.PDF_PREVIEW_ACTION);
		ModelAndView modelAndView = new ModelAndView(HjhDebtCreditTenderDefine.PDF_PREVIEW_PATH);
		String nid = request.getParameter("nid");
		// 根据订单号查询用户出借协议记录表
		TenderAgreement tenderAgreement = this.planCreditTenderService.selectTenderAgreement(nid);
		if (tenderAgreement != null && StringUtils.isNotBlank(tenderAgreement.getImgUrl())) {
			String imgUrl = tenderAgreement.getImgUrl();
			String[] imgs = imgUrl.split(";");
			List<String> imgList = Arrays.asList(imgs);
			modelAndView.addObject("imgList",imgList);
			// 文件服务器
			String fileDomainUrl = PropUtils.getSystem("hyjf.ftp.url") + PropUtils.getSystem("hyjf.ftp.basepath.img");
			modelAndView.addObject("fileDomainUrl",fileDomainUrl);
		}
		LogUtil.endLog(HjhDebtCreditTenderController.class.toString(), HjhDebtCreditTenderDefine.PDF_PREVIEW_ACTION);
		return modelAndView;
	}


	/**
	 * PDF文件签署
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions(HjhDebtCreditTenderDefine.PERMISSIONS_PDF_SIGN)
	@RequestMapping(value = HjhDebtCreditTenderDefine.PDF_SIGN_ACTION, produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	public String pdfSignAction(HttpServletRequest request) {
        LogUtil.startLog(HjhDebtCreditTenderController.class.toString(), HjhDebtCreditTenderDefine.PDF_SIGN_ACTION);
        JSONObject ret = new JSONObject();
        // 用户ID
        String userId = request.getParameter("userIdHidden");
        // 标的编号
        String borrowNid = request.getParameter("borrowNidHidden");
        // 承接订单号
        String assignNid = request.getParameter("assignNidHidden");
        // 债转编号
        String creditNid = request.getParameter("creditNidHidden");

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(borrowNid) || StringUtils.isBlank(assignNid) || StringUtils.isBlank(creditNid)) {
            ret.put(HjhDebtCreditTenderDefine.JSON_RESULT_KEY, "参数非法");
            ret.put(HjhDebtCreditTenderDefine.JSON_STATUS_KEY, HjhDebtCreditTenderDefine.JSON_STATUS_NG);
            return ret.toString();
        }
        // 标的详情
        Borrow borrow = this.planCreditTenderService.getBorrowByNid(borrowNid);
        if (borrow == null) {
            ret.put(HjhDebtCreditTenderDefine.JSON_RESULT_KEY, "标的不存在");
            ret.put(HjhDebtCreditTenderDefine.JSON_STATUS_KEY, HjhDebtCreditTenderDefine.JSON_STATUS_NG);
            return ret.toString();
        }
        // 获取承接记录
        HjhDebtCreditTender ct = this.planCreditTenderService.selectHjhCreditTenderRecord(userId, borrowNid, assignNid, creditNid);
        if (ct == null) {
            ret.put(HjhDebtCreditTenderDefine.JSON_RESULT_KEY, "获取承接记录失败");
            ret.put(HjhDebtCreditTenderDefine.JSON_STATUS_KEY, HjhDebtCreditTenderDefine.JSON_STATUS_NG);
            return ret.toString();
        }
        // 承接人
        Users users = this.planCreditTenderService.getUsersByUserId(Integer.parseInt(userId));
        if (users == null) {
            ret.put(HjhDebtCreditTenderDefine.JSON_RESULT_KEY, "获取用户信息异常");
            ret.put(HjhDebtCreditTenderDefine.JSON_STATUS_KEY, HjhDebtCreditTenderDefine.JSON_STATUS_NG);
            return ret.toString();
        }
        // 获取出借协议记录
        TenderAgreement tenderAgreement = this.planCreditTenderService.selectTenderAgreement(assignNid);
        // 如果签署成功,下载失败
        if (tenderAgreement != null && tenderAgreement.getStatus() == 2) {
            // PDF脱敏加下载处理发送MQ
            this.planCreditTenderService.updateSaveSignInfo(tenderAgreement, tenderAgreement.getBorrowNid(), FddGenerateContractConstant.FDD_TRANSTYPE_PLAN_CRIDET, borrow.getInstCode());
        } else {
            FddGenerateContractBean bean = new FddGenerateContractBean();
            bean.setTransType(4);
            bean.setTenderType(3);
            bean.setAssignNid(assignNid);
            bean.setOrdid(assignNid);
            rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
        }
        ret.put(HjhDebtCreditTenderDefine.JSON_RESULT_KEY, "操作成功,签署MQ已发送");
        ret.put(HjhDebtCreditTenderDefine.JSON_STATUS_KEY, HjhDebtCreditTenderDefine.JSON_STATUS_OK);
        LogUtil.endLog(HjhDebtCreditTenderController.class.toString(), HjhDebtCreditTenderDefine.PDF_SIGN_ACTION);
        return ret.toString();
	}


	/**
	 * 运营记录 -承接明细
	 * @param request
	 * @param response
	 * @param form
     * @return
     */
	@RequestMapping(HjhDebtCreditTenderDefine.OPT_ACTION_INIT)
	//@RequiresPermissions(HjhDebtCreditTenderDefine.PERMISSIONS_VIEW)
	public ModelAndView tenderDetail(HttpServletRequest request, HttpServletResponse response,@ModelAttribute HjhDebtCreditTenderBean form) {
		LogUtil.startLog(HjhDebtCreditTenderDefine.CONTROLLER_NAME, HjhDebtCreditTenderDefine.OPT_ACTION_INIT);
		ModelAndView modeAndView = new ModelAndView(PlanListDefine.OPT_TENDER_DETAIL_LIST_PATH);
		// 创建分页
		/*if (!"1".equals(form.getIsSearch())){*/
			if (!"1".equals(form.getIsOptFlag())){
				form.setAssignTimeStart(GetDate.date2Str(new Date(),new SimpleDateFormat("yyyy-MM-dd")));
			}
		/*}*/
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(HjhDebtCreditTenderDefine.CONTROLLER_NAME, HjhDebtCreditTenderDefine.OPT_ACTION_INIT);
		return modeAndView;
	}

	/**
	 * 运营记录 -承接明细
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	@RequestMapping(HjhDebtCreditTenderDefine.OPT_ACTION_SEARCH)
	//@RequiresPermissions(HjhDebtCreditTenderDefine.PERMISSIONS_VIEW)
	public ModelAndView tenderDetailSearch(HttpServletRequest request, HttpServletResponse response,@ModelAttribute HjhDebtCreditTenderBean form) {
		LogUtil.startLog(HjhDebtCreditTenderDefine.CONTROLLER_NAME, HjhDebtCreditTenderDefine.OPT_ACTION_SEARCH);
		ModelAndView modeAndView = new ModelAndView(PlanListDefine.OPT_TENDER_DETAIL_LIST_PATH);
		// 创建分页
		/*if (!"1".equals(form.getIsSearch())){
			if (!"1".equals(form.getIsOptFlag())){
				form.setAssignTimeStart(GetDate.date2Str(new Date(),new SimpleDateFormat("yyyy-MM-dd")));
			}
		}*/
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(HjhDebtCreditTenderDefine.CONTROLLER_NAME, HjhDebtCreditTenderDefine.OPT_ACTION_SEARCH);
		return modeAndView;
	}

}
