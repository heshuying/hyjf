package com.hyjf.admin.manager.borrow.batchcenter.batchborrowrepay;

import java.math.BigDecimal;
import java.util.ArrayList;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
@RequestMapping(value = BatchBorrowRepayDefine.REQUEST_MAPPING)
public class BatchBorrowRepayController extends BaseController {
    /** FROM */
    public static final String NAME_CLASS = "REPAY_STATUS";

	@Autowired
	private BatchBorrowRepayService batchBorrowRepayService;


	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BatchBorrowRepayDefine.INIT)
	@RequiresPermissions(BatchBorrowRepayDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, BatchBorrowRepayBean form) {
		LogUtil.startLog(BatchBorrowRepayController.class.toString(), BatchBorrowRepayDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BatchBorrowRepayDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BatchBorrowRepayController.class.toString(), BatchBorrowRepayDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BatchBorrowRepayDefine.SEARCH_ACTION)
	@RequiresPermissions(BatchBorrowRepayDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, BatchBorrowRepayBean form) {
		LogUtil.startLog(BatchBorrowRepayController.class.toString(), BatchBorrowRepayDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(BatchBorrowRepayDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BatchBorrowRepayController.class.toString(), BatchBorrowRepayDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BatchBorrowRepayBean form) {

	    BatchCenterCustomize batchCenterCustomize = new BatchCenterCustomize();

		// 项目编号 检索条件
	    batchCenterCustomize.setBorrowNid(form.getBorrowNid());
		// 项目名称 检索条件
	    batchCenterCustomize.setBatchNo(form.getBatchNo());
		// 出借人 检索条件
	    batchCenterCustomize.setStatus(form.getStatus());
	    batchCenterCustomize.setApiType(1);
	    batchCenterCustomize.setNameClass(NAME_CLASS);
	    batchCenterCustomize.setSubmitTimeStartSrch(form.getSubmitTimeStartSrch());
        batchCenterCustomize.setSubmitTimeEndSrch(form.getSubmitTimeEndSrch());
		// 放款状态
		List<ParamName> recoverStatusList = this.batchBorrowRepayService.getParamNameList(NAME_CLASS);
		modelAndView.addObject("recoverStatusList", recoverStatusList);
		/*--------------add by LSY START-------------------*/
		// 资金来源 检索条件
		batchCenterCustomize.setInstCodeSrch(form.getInstCodeSrch());
		// 资金来源
		List<HjhInstConfig> hjhInstConfigList = this.batchBorrowRepayService.hjhInstConfigList("");
		modelAndView.addObject("hjhInstConfigList", hjhInstConfigList);
		/*--------------add by LSY END-------------------*/

		Long count = this.batchBorrowRepayService.countBatchCenter(batchCenterCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			batchCenterCustomize.setLimitStart(paginator.getOffset());
			batchCenterCustomize.setLimitEnd(paginator.getLimit());
			List<BatchCenterCustomize> recordList = this.batchBorrowRepayService.selectBatchCenterList(batchCenterCustomize);
			form.setPaginator(paginator);
			formatRecordList(recordList);
			modelAndView.addObject("recordList", recordList);
			/*------add by LSY START---------*/
			BatchCenterCustomize sumObject = this.batchBorrowRepayService.sumBatchCenter(batchCenterCustomize);
			modelAndView.addObject("sumObject", sumObject);
			/*------add by LSY END---------*/
		}

		modelAndView.addObject(BatchBorrowRepayDefine.BORROW_FORM, form);
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
    @RequestMapping(BatchBorrowRepayDefine.EXPORT_ACTION)
    @RequiresPermissions(BatchBorrowRepayDefine.PERMISSIONS_EXPORT)
    public void exportAction(HttpServletRequest request, HttpServletResponse response, BatchBorrowRepayBean form) throws Exception {
        LogUtil.startLog(BatchBorrowRepayController.class.toString(), BatchBorrowRepayDefine.EXPORT_ACTION);
        // 表格sheet名称
        String sheetName = "批次还款列表";

        BatchCenterCustomize batchCenterCustomize = new BatchCenterCustomize();

        // 项目编号 检索条件
        batchCenterCustomize.setBorrowNid(form.getBorrowNid());
        // 项目名称 检索条件
        batchCenterCustomize.setBatchNo(form.getBatchNo());
        // 出借人 检索条件
        batchCenterCustomize.setStatus(form.getStatus());
        batchCenterCustomize.setApiType(1);
        batchCenterCustomize.setNameClass(NAME_CLASS);
        batchCenterCustomize.setSubmitTimeStartSrch(form.getSubmitTimeStartSrch());
        batchCenterCustomize.setSubmitTimeEndSrch(form.getSubmitTimeEndSrch());
        /*----------add by LSY START------------------*/
        batchCenterCustomize.setInstCodeSrch(form.getInstCodeSrch());
        /*----------add by LSY END------------------*/
        batchCenterCustomize.setLimitStart(-1);
        batchCenterCustomize.setLimitEnd(-1);
        List<BatchCenterCustomize> recordList = this.batchBorrowRepayService.selectBatchCenterList(batchCenterCustomize);

        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        // 序号   项目编号    批次号 还款角色    还款用户名   当前还款期数  总期数 借款金额    应收服务费   应还款 已还款 总笔数 成功笔数    成功金额    失败笔数    失败金额    提交时间    更新时间    批次状态    银行回执说明
        /*-------upd by LSY START-----------------*/
        //String[] titles = new String[] {"序号 ","项目编号","批次号","还款角色","还款用户名","当前还款期数","总期数","借款金额","应收服务费",
        String[] titles = new String[] {"序号 ","项目编号","资产来源","批次号","还款角色","还款用户名","当前还款期数","总期数","借款金额","还款服务费",
        /*-------upd by LSY START-----------------*/
        		"应还款","已还款","总笔数","成功笔数","成功金额","失败笔数","失败金额","提交时间","更新时间","批次状态","银行回执说明"};
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
                    /*-------add by LSY START-----------------*/
                    // 资产来源
                    else if (celLength == 2) {
                        cell.setCellValue(customize.getInstName());
                    }
                    /*-------add by LSY END-----------------*/
                    // 批次号 
                    else if (celLength == 3) {
                        cell.setCellValue(customize.getBatchNo());
                    }
                    // 还款角色
                    else if (celLength == 4) {
                        cell.setCellValue("0".equals(customize.getIsRepayOrgFlag())?"借款人":"担保机构");
                    }
                    // 还款用户名   
                    else if (celLength == 5) {
                        cell.setCellValue(customize.getUserName());
                    }
                    // 当前还款期数  
                    else if (celLength == 6) {
                        cell.setCellValue(customize.getPeriodNow());
                    }
                    // 总期数 
                    else if (celLength == 7) {
                        cell.setCellValue(customize.getBorrowPeriod());
                    }
                    // 借款金额    
                    else if (celLength == 8) {
                        cell.setCellValue(customize.getBorrowAccount().toString());
                    }
                    // 应收服务费   
                    else if (celLength == 9) {
                        cell.setCellValue(customize.getBatchServiceFee().toString());
                    }
                    // 应还款 
                    else if (celLength == 10) {
                        cell.setCellValue(customize.getBatchAmount().toString());
                    }
                    // 已还款 
                    else if (celLength == 11) {
                        cell.setCellValue(customize.getSucAmount().toString());
                    }
                    // 总笔数 
                    else if (celLength == 12) {
                        cell.setCellValue(customize.getBatchCounts());
                    }
                    // 成功笔数    
                    else if (celLength == 13) {
                        cell.setCellValue(customize.getSucCounts());
                    }
                    // 成功金额    
                    else if (celLength == 14) {
                        cell.setCellValue(customize.getSucAmount().toString());
                    }
                    // 失败笔数   
                    else if (celLength == 15) {
                        cell.setCellValue(customize.getFailCounts());
                    }
                    //  失败金额    
                    else if (celLength == 16) {
                        cell.setCellValue(customize.getFailAmount().toString());
                    }
                    // 提交时间    
                    else if (celLength == 17) {
                        cell.setCellValue(customize.getCreateTime());
                    }
                    // 更新时间    
                    else if (celLength == 18) {
                        cell.setCellValue(customize.getUpdateTime());
                    }
                    // 批次状态    
                    else if (celLength == 19) {
                        cell.setCellValue(customize.getStatusStr());
                    }
                    // 银行回执说明 
                    else if (celLength == 20) {
                        cell.setCellValue(customize.getData());
                    }
                    
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);

        LogUtil.endLog(BatchBorrowRepayController.class.toString(), BatchBorrowRepayDefine.EXPORT_ACTION);
    }
    
    /**
	 * 查询批次交易明细 add by cwyang 2017-7-21
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BatchBorrowRepayDefine.QUERY_BATCH_DETAILS_ACTION)
	@RequiresPermissions(BatchBorrowRepayDefine.PERMISSIONS_QUERY_BATCH_DETAILS)
	public ModelAndView queryBatchDetailClkAction(HttpServletRequest request, @ModelAttribute BatchBorrowRepayBean form) {
		LogUtil.startLog(BatchBorrowRepayController.class.toString(), BatchBorrowRepayDefine.QUERY_BATCH_DETAILS_ACTION);
		ModelAndView modelAndView = new ModelAndView(BatchBorrowRepayDefine.QUERY_BATCH_DETAILS_PATH);
		String id = form.getDeatilBatchNo();
		if (StringUtils.isNotBlank(id)) {
			//调用接口查询交易批次明细
			List<BankCallBean> resultBeans = batchBorrowRepayService.queryBatchDetails(id);
			//转换页面展示列表
			List<BatchQueryDetailsBean> detailList = getDetailList(resultBeans);
			modelAndView.addObject("detailList", detailList);
		}else{
			logger.info("==========-----------批次交易明细状态查询失败:batchNo is null!-----------");
		}
		LogUtil.endLog(BatchBorrowRepayController.class.toString(), BatchBorrowRepayDefine.QUERY_BATCH_DETAILS_ACTION);
		modelAndView.addObject(BatchBorrowRepayDefine.BORROW_FORM, form);
		return modelAndView;
	}
	
	/**
	 * 获得页面需要的展示列表
	 * @param resultBeans
	 * @return
	 */
	private List<BatchQueryDetailsBean> getDetailList(List<BankCallBean> resultBeans) {
		List<BatchQueryDetailsBean> detailList = new ArrayList<>();
		if (Validator.isNotNull(resultBeans) && resultBeans.size() > 0) {
			for (int i = 0; i < resultBeans.size(); i++) {
				BankCallBean resultBean = resultBeans.get(i);
				String subPacks = resultBean.getSubPacks();
				if (StringUtils.isNotBlank(subPacks)) {
					JSONArray loanDetails = JSONObject.parseArray(subPacks);
					/*--------add by LSY START--------------*/
					BigDecimal sumTxAmount = BigDecimal.ZERO;
					/*--------add by LSY END--------------*/
					for (int j = 0; j < loanDetails.size(); j++) {
						JSONObject loanDetail = loanDetails.getJSONObject(j);
						BatchQueryDetailsBean info = new BatchQueryDetailsBean();
						info.setAuthCode(loanDetail.getString(BankCallConstant.PARAM_AUTHCODE));// 授权码
						info.setTxState(loanDetail.getString(BankCallConstant.PARAM_TXSTATE));// 交易状态
						info.setOrderId(loanDetail.getString(BankCallConstant.PARAM_ORDERID));// 订单号
						info.setTxAmount(loanDetail.getBigDecimal(BankCallConstant.PARAM_TXAMOUNT));// 操作金额
						info.setForAccountId(loanDetail.getString(BankCallConstant.PARAM_FORACCOUNTID));// 借款人银行账户
						info.setProductId(loanDetail.getString(BankCallConstant.PARAM_PRODUCTID));// 标的号
						info.setFileMsg(loanDetail.getString(BankCallConstant.PARAM_FAILMSG));//错误提示
						detailList.add(info);
						/*-------add by LSY START-----------*/
						sumTxAmount = sumTxAmount.add(loanDetail.getBigDecimal(BankCallConstant.PARAM_TXAMOUNT));
						/*-------add by LSY END-----------*/
					}
					/*--------add by LSY START--------------*/
					if (detailList != null && !detailList.isEmpty()) {
					    BatchQueryDetailsBean temp = detailList.get(0);
					    temp.setSumTxAmount(sumTxAmount);
					}
					/*--------add by LSY END--------------*/
				}
			}
		}
		
		return detailList;
	}
}
