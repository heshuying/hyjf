package com.hyjf.admin.manager.borrow.batchcenter.batchborrowrecover;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.admin.BatchCenterCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = BatchBorrowRecoverDefine.REQUEST_MAPPING)
public class BatchBorrowRecoverController extends BaseController {
    /** FROM */
    public static final String NAME_CLASS = "REVERIFY_STATUS";

	@Autowired
	private BatchBorrowRecoverService batchBorrowRecoverService;


	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BatchBorrowRecoverDefine.INIT)
	@RequiresPermissions(BatchBorrowRecoverDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, BatchBorrowRecoverBean form) {
		LogUtil.startLog(BatchBorrowRecoverController.class.toString(), BatchBorrowRecoverDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BatchBorrowRecoverDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BatchBorrowRecoverController.class.toString(), BatchBorrowRecoverDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BatchBorrowRecoverDefine.SEARCH_ACTION)
	@RequiresPermissions(BatchBorrowRecoverDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, BatchBorrowRecoverBean form) {
		LogUtil.startLog(BatchBorrowRecoverController.class.toString(), BatchBorrowRecoverDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(BatchBorrowRecoverDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BatchBorrowRecoverController.class.toString(), BatchBorrowRecoverDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BatchBorrowRecoverBean form) {

	    BatchCenterCustomize batchCenterCustomize = new BatchCenterCustomize();

		// 项目编号 检索条件
	    batchCenterCustomize.setBorrowNid(form.getBorrowNid());
		// 项目名称 检索条件
	    batchCenterCustomize.setBatchNo(form.getBatchNo());
		// 出借人 检索条件
	    batchCenterCustomize.setStatus(form.getStatus());
	    batchCenterCustomize.setApiType(0);
	    batchCenterCustomize.setNameClass(NAME_CLASS);
	    batchCenterCustomize.setSubmitTimeStartSrch(form.getSubmitTimeStartSrch());
	    batchCenterCustomize.setSubmitTimeEndSrch(form.getSubmitTimeEndSrch());
		// 放款状态
		List<ParamName> recoverStatusList = this.batchBorrowRecoverService.getParamNameList(NAME_CLASS);
		modelAndView.addObject("recoverStatusList", recoverStatusList);

		/*--------------add by LSY START-------------------*/
		// 资金来源 检索条件
		batchCenterCustomize.setInstCodeSrch(form.getInstCodeSrch());
		// 资金来源
		List<HjhInstConfig> hjhInstConfigList = this.batchBorrowRecoverService.hjhInstConfigList("");
		modelAndView.addObject("hjhInstConfigList", hjhInstConfigList);
		/*--------------add by LSY END-------------------*/
		Long count = this.batchBorrowRecoverService.countBatchCenter(batchCenterCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			batchCenterCustomize.setLimitStart(paginator.getOffset());
			batchCenterCustomize.setLimitEnd(paginator.getLimit());
			List<BatchCenterCustomize> recordList = this.batchBorrowRecoverService.selectBatchCenterList(batchCenterCustomize);
			formatRecordList(recordList);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
			/*------add by LSY START---------*/
			BatchCenterCustomize sumObject = this.batchBorrowRecoverService.sumBatchCenter(batchCenterCustomize);
			modelAndView.addObject("sumObject", sumObject);
			/*------add by LSY END---------*/
		}

		modelAndView.addObject(BatchBorrowRecoverDefine.BORROW_FORM, form);
	}

	/**
	 * 格式化加息内容 add by cwyang 20180809
	 * @param recordList
	 */
	private void formatRecordList(List<BatchCenterCustomize> recordList) {
		if(recordList != null && recordList.size() > 0){
			for (BatchCenterCustomize info: recordList
				 ) {
				if("0".equals(info.getIncreaseInterestFlag())){//不加息
					info.setIncreaseInterestFlag("不加息");
					info.setExtraYieldStatus("");
					info.setExtraYieldRepayStatus("");
				}else if("1".equals(info.getIncreaseInterestFlag())){
					info.setIncreaseInterestFlag("加息");
					if("0".equals(info.getExtraYieldStatus())){
						info.setExtraYieldStatus("待放款");
					}else if("1".equals(info.getExtraYieldStatus())){
						info.setExtraYieldStatus("放款完成");
					}
					if("0".equals(info.getExtraYieldRepayStatus())){
						info.setExtraYieldRepayStatus("待还款");
					}else if("1".equals(info.getExtraYieldRepayStatus())){
						info.setExtraYieldRepayStatus("还款完成");
					}
				}
			}
		}

	}

	/**
     * 导出功能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    @RequestMapping(BatchBorrowRecoverDefine.EXPORT_ACTION)
    @RequiresPermissions(BatchBorrowRecoverDefine.PERMISSIONS_EXPORT)
    public void exportAction(HttpServletRequest request, HttpServletResponse response, BatchBorrowRecoverBean form) throws Exception {
        LogUtil.startLog(BatchBorrowRecoverController.class.toString(), BatchBorrowRecoverDefine.EXPORT_ACTION);
        // 表格sheet名称
        String sheetName = "批次放款列表";

        BatchCenterCustomize batchCenterCustomize = new BatchCenterCustomize();

        // 项目编号 检索条件
        batchCenterCustomize.setBorrowNid(form.getBorrowNid());
        // 项目名称 检索条件
        batchCenterCustomize.setBatchNo(form.getBatchNo());
        // 出借人 检索条件
        batchCenterCustomize.setStatus(form.getStatus());
        batchCenterCustomize.setApiType(0);
        /*----------add by LSY START---------------------*/
        // 资产来源 检索条件
        batchCenterCustomize.setInstCodeSrch(form.getInstCodeSrch());
        /*----------add by LSY END---------------------*/
        batchCenterCustomize.setNameClass(NAME_CLASS);
        batchCenterCustomize.setSubmitTimeStartSrch(form.getSubmitTimeStartSrch());
        batchCenterCustomize.setSubmitTimeEndSrch(form.getSubmitTimeEndSrch());
        
        batchCenterCustomize.setLimitStart(-1);
        batchCenterCustomize.setLimitEnd(-1);
        List<BatchCenterCustomize> recordList = this.batchBorrowRecoverService.selectBatchCenterList(batchCenterCustomize);

        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        // 序号 项目编号 批次号 借款金额 应收服务费 应放款 已放款 总笔数 成功笔数 失败笔数 更新时间 批次状态 操作
        /*----------upd by LSY START---------------------*/
        //String[] titles = new String[] { "序号","项目编号","批次号", "借款金额","应收服务费","应放款","已放款","总笔数","成功笔数","失败笔数","更新时间","批次状态"};
        String[] titles = new String[] { "序号","项目编号","资产来源","批次号", "借款金额","放款服务费","应放款","已放款","总笔数","成功笔数","失败笔数","更新时间","批次状态"};
        /*----------upd by LSY END---------------------*/
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
                    BatchCenterCustomize  customize= recordList.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);

                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    // 项目编号
                    else if (celLength == 1) {
                        cell.setCellValue(customize.getBorrowNid());
                    }
                    /*----------add by LSY START---------------------*/
                    // 项目编号
                    else if (celLength == 2) {
                        cell.setCellValue(customize.getInstName());
                    }
                    /*----------add by LSY END---------------------*/
                    // 批次号
                    else if (celLength == 3) {
                        cell.setCellValue(customize.getBatchNo());
                    }
                    // 借款金额
                    else if (celLength == 4) {
                        cell.setCellValue(customize.getBorrowAccount().toString());
                    }
                    // 应收服务费 
                    else if (celLength == 5) {
                        cell.setCellValue(customize.getBatchServiceFee().toString());
                    }
                    // 应放款
                    else if (celLength == 6) {
                        cell.setCellValue(customize.getBatchAmount().toString());
                    }
                    // 已放款 
                    else if (celLength == 7) {
                        cell.setCellValue(customize.getSucAmount().toString());
                    }
                    // 总笔数）
                    else if (celLength == 8) {
                        cell.setCellValue(customize.getBatchCounts());
                    }
                    // 成功笔数
                    else if (celLength == 9) {
                        cell.setCellValue(customize.getSucCounts());
                    }
                    // 失败笔数
                    else if (celLength == 10) {
                        cell.setCellValue(customize.getFailCounts());
                    }
                    // 更新时间
                    else if (celLength == 11) {
                        cell.setCellValue(customize.getUpdateTime());
                    }
                    // 批次状态
                    else if (celLength == 12) {
                        cell.setCellValue(customize.getStatusStr());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);

        LogUtil.endLog(BatchBorrowRecoverController.class.toString(), BatchBorrowRecoverDefine.EXPORT_ACTION);
    }
    

	/**
	 * 查询批次交易明细 add by cwyang 2017-7-21
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BatchBorrowRecoverDefine.QUERY_BATCH_DETAILS_ACTION)
	@RequiresPermissions(BatchBorrowRecoverDefine.PERMISSIONS_QUERY_BATCH_DETAILS)
	public ModelAndView queryBatchDetailClkAction(HttpServletRequest request, @ModelAttribute BatchBorrowRecoverBean form) {
		LogUtil.startLog(BatchBorrowRecoverController.class.toString(), BatchBorrowRecoverDefine.QUERY_BATCH_DETAILS_ACTION);
		ModelAndView modelAndView = new ModelAndView(BatchBorrowRecoverDefine.QUERY_BATCH_DETAILS_PATH);
		String borowNid = form.getDeatilBorrowNid();
		if (StringUtils.isNotBlank(borowNid)) {
			try {
				//调用接口查询交易批次明细
				BankCallBean resultBeans = batchBorrowRecoverService.queryBatchDetails(borowNid);
				//转换页面展示列表
				BatchQueryDetailsBean detailList = getDetailList(resultBeans);
				modelAndView.addObject("detailList", detailList);
			}catch (Exception e){
					e.printStackTrace();
			}
		}else{
			logger.info("==========borrowNid is null!");
		}
		LogUtil.endLog(BatchBorrowRecoverController.class.toString(), BatchBorrowRecoverDefine.QUERY_BATCH_DETAILS_ACTION);
		modelAndView.addObject(BatchBorrowRecoverDefine.BORROW_FORM, form);
		return modelAndView;
	}
	
	/**
	 * 获得页面需要的展示列表
	 * @param resultBeans
	 * @return
	 */
	private BatchQueryDetailsBean getDetailList(BankCallBean resultBeans) {
		BatchQueryDetailsBean detailList = new BatchQueryDetailsBean();
		if (Validator.isNotNull(resultBeans)) {
			// 借款人电子账户号
			detailList.setForAccountId(resultBeans.getAccountId());
			// 借款人姓名
			detailList.setName(resultBeans.getName());
			// 响应代码
			detailList.setRetCode(resultBeans.getRetCode());
			// 错误描述
			detailList.setFileMsg(resultBeans.getRetMsg());
			// 标的编号
			detailList.setProductId(resultBeans.getProductId());
			// 借款人入账金额
			detailList.setTxAmount(resultBeans.getTxAmount());
			// 手续费金额
			detailList.setFeeAmount(resultBeans.getFeeAmount());
			// 风险准备金
			detailList.setRiskAmount(resultBeans.getRiskAmount());
			// 交易状态
			detailList.setTxState(BankCallConstant.RESPCODE_SUCCESS.equals(resultBeans.getRetCode()) ? "成功" : "失败");
		}
		return detailList;
	}
}
