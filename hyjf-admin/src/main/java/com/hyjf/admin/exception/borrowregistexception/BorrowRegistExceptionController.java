/**
 * Description:可以删除异常项目的列表,只查看初审中和待发布项目信息
 * Copyright: Copyright (HYJF Corporation)2016
 * Company: HYJF Corporation
 * @author: zhuxiaodong
 * @version: 1.0
 * Created at: 2016年3月8日 上午9:06:32
 * Modification History:
 * Modified by : 
 */
package com.hyjf.admin.exception.borrowregistexception;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonService;
import com.hyjf.admin.manager.borrow.borrowregist.BorrowRegistService;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.BorrowRegistCustomize;

/**
 * @package com.hyjf.admin.exception.borrowexception
 * @author wangkun
 * @date 2016/03/08 9:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = BorrowRegistExceptionDefine.REQUEST_MAPPING)
public class BorrowRegistExceptionController extends BaseController {

	@Autowired
	private BorrowRegistExceptionService borrowRegistExceptionService;

	@Autowired
	private BorrowCommonService borrowCommonService;
	
	@Autowired
	private BorrowRegistService borrowRegistService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowRegistExceptionDefine.INIT_ACTION)
	@RequiresPermissions(BorrowRegistExceptionDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, BorrowRegistExceptionBean form) {
		LogUtil.startLog(BorrowRegistExceptionController.class.toString(), BorrowRegistExceptionDefine.INIT_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowRegistExceptionDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BorrowRegistExceptionController.class.toString(), BorrowRegistExceptionDefine.INIT_ACTION);
		return modelAndView;
	}

	/**
	 * 按钮查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowRegistExceptionDefine.SEARCH_ACTION)
	@RequiresPermissions(BorrowRegistExceptionDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, BorrowRegistExceptionBean form) {
		LogUtil.startLog(BorrowRegistExceptionController.class.toString(), BorrowRegistExceptionDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowRegistExceptionDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BorrowRegistExceptionController.class.toString(), BorrowRegistExceptionDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BorrowRegistExceptionBean form) {

		// 项目类型
		List<BorrowProjectType> borrowProjectTypeList = this.borrowCommonService.borrowProjectTypeList("");
		modelAndView.addObject("borrowProjectTypeList", borrowProjectTypeList);

		// 还款方式
		List<BorrowStyle> borrowStyleList = this.borrowCommonService.borrowStyleList("");
		modelAndView.addObject("borrowStyleList", borrowStyleList);

		BorrowCommonCustomize corrowCommonCustomize = new BorrowCommonCustomize();
		// 借款编码
		corrowCommonCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
		// 项目名称
		corrowCommonCustomize.setBorrowNameSrch(form.getBorrowNameSrch());
		// 借 款 人
		corrowCommonCustomize.setUsernameSrch(form.getUserNameSrch());
		// 项目类型
		corrowCommonCustomize.setProjectTypeSrch(form.getProjectTypeSrch());
		// 还款方式
		corrowCommonCustomize.setBorrowStyleSrch(form.getBorrowStyleSrch());
		// 添加时间
		corrowCommonCustomize.setTimeStartSrch(form.getTimeStartSrch());
		// 添加时间
		corrowCommonCustomize.setTimeEndSrch(form.getTimeEndSrch());
		Long count = this.borrowRegistExceptionService.countBorrow(corrowCommonCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			corrowCommonCustomize.setLimitStart(paginator.getOffset());
			corrowCommonCustomize.setLimitEnd(paginator.getLimit());
			List<BorrowRegistCustomize> recordList = this.borrowRegistExceptionService.selectBorrowList(corrowCommonCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(BorrowRegistExceptionDefine.BORROW_REGIST_EXCEPTION_FORM, form);
	}

	/**
	 * 标的备案异常处理
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(BorrowRegistExceptionDefine.BORROW_REGIST_EXCEPTION)
	@RequiresPermissions(BorrowRegistExceptionDefine.PERMISSIONS_DEBTREGISTEXCEP)
	public String borrowRegistException(HttpServletRequest request, @RequestBody BorrowRegistExceptionBean form) {
		LogUtil.startLog(BorrowRegistExceptionController.class.toString(), BorrowRegistExceptionDefine.BORROW_REGIST_EXCEPTION);
		// 返回结果
		JSONObject result = new JSONObject();
		// 项目编号
		String borrowNid = form.getBorrowNidSrch();
		
		/**合规改版新增借款人 缴费授权 + 还款授权 校验 start 注意：跟产品确认过手动备案不需要发短信，目前两个需求全校验**/
		
		/**因业务需求暂时注掉各种授权校验代码 */
/*		if(StringUtils.isNotEmpty(borrowNid)){
			List<HjhPlanAsset> assetList = this.borrowRegistService.getAssetListByBorrowNid(borrowNid);
			if(assetList != null && assetList.size() > 0 ){
				
				//1.当资产表有数据时-->资产来源是 第三方
				//(1)通过borrowNid获取 借款人id，担保机构id,(收款人)受托人id，然后分别去做校验
				Borrow borrow1 = this.borrowRegistService.getBorrowByNid(borrowNid);
				if(borrow1 != null){
					
					// (1.1)担保机构id可以为空,不为空时校验，为空不校验
					if(borrow1.getRepayOrgUserId() != null && borrow1.getRepayOrgUserId() != 0 ){
						HjhUserAuth hjhUserAuth2 = borrowRegistService.getHjhUserAuthByUserID(borrow1.getRepayOrgUserId());
						if(hjhUserAuth2 == null){
							result.put("success", "1");
							result.put("msg", "担保机构未做缴费授权，请联系担保机构授权！");
							return result.toString();
						} else {
							// 还款授权状态 DB 默认为 0
							if (hjhUserAuth2.getAutoRepayStatus() == null || hjhUserAuth2.getAutoRepayStatus().toString().equals("0")) {
								result.put("success", "1");
								result.put("msg", "担保机构未做还款授权，请联系担保机构授权！");
								return result.toString();
							}
							// 缴费授权状态 DB 默认为 0
							if (hjhUserAuth2.getAutoPaymentStatus() == null || hjhUserAuth2.getAutoPaymentStatus().toString().equals("0")) {
								result.put("success", "1");
								result.put("msg", "担保机构未做缴费授权，请联系担保机构授权！");
								return result.toString();
							}	
						}
					}
					
					// (1.2)受托人id可以为空,不为空时校验
					if(borrow1.getEntrustedUserId() != null){
						HjhUserAuth hjhUserAuth3 = borrowRegistService.getHjhUserAuthByUserID(borrow1.getEntrustedUserId());
						if(hjhUserAuth3 == null){
							result.put("success", "1");
							result.put("msg", "收款人未做缴费授权，请联系收款人授权！");
							return result.toString();
						} else {
							// 缴费授权状态 DB 默认为 0
							if (hjhUserAuth3.getAutoPaymentStatus() == null || hjhUserAuth3.getAutoPaymentStatus().toString().equals("0")) {
								result.put("success", "1");
								result.put("msg", "收款人未做缴费授权，请联系收款人授权！");
								return result.toString();
							}	
						}
					}
					
					// (1.3)借款人id必须非空
					if(borrow1.getUserId() != null){
						HjhUserAuth hjhUserAuth1 = borrowRegistService.getHjhUserAuthByUserID(borrow1.getUserId());
						if(hjhUserAuth1 == null){
							result.put("success", "1");
							result.put("msg", "借款人未做缴费授权，请联系借款人授权！");
							return result.toString();
						} else {
							// 缴费授权状态 DB 默认为 0
							if (hjhUserAuth1.getAutoPaymentStatus() == null || hjhUserAuth1.getAutoPaymentStatus().toString().equals("0")) {
								result.put("success", "1");
								result.put("msg", "借款人未做缴费授权，请联系借款人授权！");
								return result.toString();
							}
							
							// 是否可用担保机构还款(0:否,1:是) DB默认为0
							if(borrow1.getIsRepayOrgFlag() != null && borrow1.getIsRepayOrgFlag() == 1){
								如果是担保机构还款，还款授权可以不做
							} else {
								// 还款授权状态 DB 默认为 0
								if (hjhUserAuth1.getAutoRepayStatus() == null || hjhUserAuth1.getAutoRepayStatus().toString().equals("0")) {
									result.put("success", "1");
									result.put("msg", "借款人未做还款授权，请联系借款人授权！");
									return result.toString();
								}
							}
						}
					} else {
						result.put("success", "1");
						result.put("msg", "该借款人不存在！");
					}

				} else {
					result.put("success", "1");
					result.put("msg", "该借款项目不存在！");
				}	

			} else {
				//2.当资产表无数据时-->资产来源是 汇盈平台
				//(1)通过borrowNid获取 借款人id，担保机构id,(收款人)受托人id，然后分别去做校验
				Borrow borrow = this.borrowRegistService.getBorrowByNid(borrowNid);
				if(borrow != null){
					// (1.1)借款人id必须非空
					if(borrow.getUserId() != null){
						HjhUserAuth hjhUserAuth1 = borrowRegistService.getHjhUserAuthByUserID(borrow.getUserId());
						if(hjhUserAuth1 == null){
							result.put("success", "1");
							result.put("msg", "借款人未做缴费授权，请联系借款人授权！");
							return result.toString();
						} else {
							// 还款授权状态 DB 默认为 0
							if (hjhUserAuth1.getAutoRepayStatus() == null || hjhUserAuth1.getAutoRepayStatus().toString().equals("0")) {
								result.put("success", "1");
								result.put("msg", "借款人未做还款授权，请联系借款人授权！");
								return result.toString();
							}
							// 缴费授权状态 DB 默认为 0
							if (hjhUserAuth1.getAutoPaymentStatus() == null || hjhUserAuth1.getAutoPaymentStatus().toString().equals("0")) {
								result.put("success", "1");
								result.put("msg", "借款人未做缴费授权，请联系借款人授权！");
								return result.toString();
							}	
						}
					} else {
						result.put("success", "1");
						result.put("msg", "该借款人不存在！");
					}
					
					// (1.2)担保机构id可以为空,不为空时校验，为空不校验
					if(borrow.getRepayOrgUserId() != null && borrow.getRepayOrgUserId() != 0){
						HjhUserAuth hjhUserAuth2 = borrowRegistService.getHjhUserAuthByUserID(borrow.getRepayOrgUserId());
						if(hjhUserAuth2 == null){
							result.put("success", "1");
							result.put("msg", "担保机构未做缴费授权，请联系担保机构授权！");
							return result.toString();
						} else {
							// 还款授权状态 DB 默认为 0
							if (hjhUserAuth2.getAutoRepayStatus() == null || hjhUserAuth2.getAutoRepayStatus().toString().equals("0")) {
								result.put("success", "1");
								result.put("msg", "担保机构未做还款授权，请联系担保机构授权！");
								return result.toString();
							}
							// 缴费授权状态 DB 默认为 0
							if (hjhUserAuth2.getAutoPaymentStatus() == null || hjhUserAuth2.getAutoPaymentStatus().toString().equals("0")) {
								result.put("success", "1");
								result.put("msg", "担保机构未做缴费授权，请联系担保机构授权！");
								return result.toString();
							}	
						}
					}
					
					// (1.3)受托人id可以为空,不为空时校验
					if(borrow.getEntrustedUserId() != null){
						HjhUserAuth hjhUserAuth3 = borrowRegistService.getHjhUserAuthByUserID(borrow.getEntrustedUserId());
						if(hjhUserAuth3 == null){
							result.put("success", "1");
							result.put("msg", "收款人(受托人)未做缴费授权，请联系收款人(受托人)授权！");
							return result.toString();
						} else {
							// 缴费授权状态 DB 默认为 0
							if (hjhUserAuth3.getAutoPaymentStatus() == null || hjhUserAuth3.getAutoPaymentStatus().toString().equals("0")) {
								result.put("success", "1");
								result.put("msg", "收款人(受托人)未做缴费授权，请联系收款人(受托人)授权！");
								return result.toString();
							}	
						}
					} 
				} else {
					result.put("success", "1");
					result.put("msg", "该借款项目不存在！");
				}	
			}
		} else {
			result.put("success", "1");
			result.put("msg", "项目编号为空！");
		}*/
		/**合规改版新增借款人 缴费授权 + 还款授权 校验 end*/
		
		
		if (StringUtils.isBlank(borrowNid)) {
			result.put("success", "1");
			result.put("msg", "项目编号为空！");
		} else {
			int loginUserId = Integer.parseInt(ShiroUtil.getLoginUserId());
			result = this.borrowRegistExceptionService.debtRegistSearch(borrowNid, result, loginUserId);
		}
		LogUtil.endLog(BorrowRegistExceptionController.class.toString(), BorrowRegistExceptionDefine.BORROW_REGIST_EXCEPTION);
		return result.toString();
	}

	/**
	 * 项目异常页导出
	 * 
	 * @param request
	 * @param response
	 * @param form
	 */
	@RequestMapping(BorrowRegistExceptionDefine.EXPORT_ACTION)
	@RequiresPermissions(BorrowRegistExceptionDefine.PERMISSIONS_EXPORT)
	public void exportBorrowExceptionExcel(HttpServletRequest request, HttpServletResponse response, BorrowRegistExceptionBean form) {
		// 表格sheet名称
		String sheetName = "项目异常列表";
		BorrowCommonCustomize corrowCommonCustomize = new BorrowCommonCustomize();
		// 借款编码
		corrowCommonCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
		// 借 款 人
		corrowCommonCustomize.setUsernameSrch(form.getUserNameSrch());
		// 项目类型
		corrowCommonCustomize.setProjectTypeSrch(form.getProjectTypeSrch());
		// 还款方式
		corrowCommonCustomize.setBorrowStyleSrch(form.getBorrowStyleSrch());
		// 添加时间
		corrowCommonCustomize.setTimeStartSrch(form.getTimeStartSrch());
		// 添加时间
		corrowCommonCustomize.setTimeEndSrch(form.getTimeEndSrch());
		// 取得数据
		corrowCommonCustomize.setLimitStart(-1);
		corrowCommonCustomize.setLimitEnd(-1);
		List<BorrowRegistCustomize> recordList = this.borrowRegistExceptionService.selectBorrowList(corrowCommonCustomize);
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + ".xls";

		String[] titles = new String[] { "序号", "项目编号", "项目名称", "借款人", "项目类型", "借款金额", "借款期限", "出借利率", "还款方式", "备案状态", "添加时间" };

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
					BorrowRegistCustomize record = recordList.get(i);
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 项目编号
					else if (celLength == 1) {
						cell.setCellValue(record.getBorrowNid());
					}
					// 项目名称
					else if (celLength == 2) {
						cell.setCellValue(record.getBorrowName());
					}
					// 借款人
					else if (celLength == 3) {
						cell.setCellValue(record.getUsername());
					}
					// 项目类型
					else if (celLength == 4) {
						cell.setCellValue(record.getProjectTypeName());
					}
					// 借款金额
					else if (celLength == 5) {
						cell.setCellValue(record.getAccount());
					}
					// 借款期限
					else if (celLength == 6) {
						cell.setCellValue(record.getBorrowPeriod());
					}
					// 出借利率
					else if (celLength == 7) {
						cell.setCellValue(record.getBorrowApr());
					}
					// 还款方式
					else if (celLength == 8) {
						cell.setCellValue(record.getBorrowStyleName());
					}
					// 备案状态
					else if (celLength == 9) {
						cell.setCellValue(record.getRegistStatusName());
					}
					// 添加时间
					else if (celLength == 10) {
						cell.setCellValue(record.getAddtime());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
	}

}
