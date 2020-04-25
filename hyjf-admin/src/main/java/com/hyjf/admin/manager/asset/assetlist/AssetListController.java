/**
 * Description:资产列表
 * Copyright: (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: liubin
 * @version: 1.0
 * Created at: 2017年108月11日 上午11:01:57
 * Modification History:
 * Modified by :
 * */

package com.hyjf.admin.manager.asset.assetlist;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.HjhAssetType;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.AssetDetailCustomize;
import com.hyjf.mybatis.model.customize.AssetListCustomize;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping(value = AssetListDefine.REQUEST_MAPPING)
public class AssetListController extends BaseController {

	@Autowired
	private AssetListService assetListService;

	/**
	 * 画面初始化
	 *
	 * @param request
	 * @return 进入资产列表页面
	 */
	@RequestMapping(AssetListDefine.INIT)
	@RequiresPermissions(AssetListDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
							 @ModelAttribute(AssetListDefine.ASSET_LIST_FORM) AssetListBean form) {
		LogUtil.startLog(AssetListController.class.toString(), AssetListDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(AssetListDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(AssetListController.class.toString(), AssetListDefine.INIT);
		return modelAndView;
	}

	/**
	 * 检索
	 *
	 * @param request
	 * @return 进入资产列表页面
	 */
	@RequestMapping(AssetListDefine.SEARCH_ACTION)
	@RequiresPermissions(AssetListDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, RedirectAttributes attr,
							   @ModelAttribute(AssetListDefine.ASSET_LIST_FORM) AssetListBean form) {
		LogUtil.startLog(AssetListController.class.toString(), AssetListDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(AssetListDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(AssetListController.class.toString(), AssetListDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建权限维护分页机能
	 *
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, AssetListBean form) {

		// 资金来源
		List<HjhInstConfig> hjhInstConfigList = this.assetListService.hjhInstConfigList("");
		modelAndView.addObject("hjhInstConfigList", hjhInstConfigList);
		// 产品类型
		List<HjhAssetType> assetTypeList = this.assetListService.hjhAssetTypeList(form.getInstCodeSrch());
		modelAndView.addObject("assetTypeList", assetTypeList);
		// 开户状态
		List<ParamName> accountStatus = this.assetListService.getParamNameList("ACCOUNT_STATUS");
		modelAndView.addObject("accountStatus", accountStatus);
		// 审核状态
		List<ParamName> verifyStatusList = this.assetListService.getParamNameList("ASSET_APPLY_STATUS");
		modelAndView.addObject("verifyStatusList", verifyStatusList);
		// 项目状态
		List<ParamName> statusList = this.assetListService.getParamNameList("ASSET_STATUS");
		modelAndView.addObject("statusList", statusList);
		// 借款类型
		List<ParamName> userTypes = this.assetListService.getParamNameList("USER_TYPE");
		modelAndView.addObject("userTypes", userTypes);

		// 封装查询条件
		Map<String, Object> conditionMap = setCondition(form);

		Integer count = this.assetListService.getRecordCount(conditionMap);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			List<AssetListCustomize> recordList = this.assetListService.getRecordList(conditionMap,
					paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			/*---------add by LSY START---------------*/
			BigDecimal sumAccount = this.assetListService.sumAccount(conditionMap);
			form.setSumAccount(sumAccount);
			/*---------add by LSY END---------------*/
		}
		modelAndView.addObject(AssetListDefine.ASSET_LIST_FORM, form);
	}

	/**
	 * 封装查询条件
	 *
	 * @param form
	 * @return
	 */
	private Map<String, Object> setCondition(AssetListBean form) {
		String assetIdSrch = StringUtils.isNotEmpty(form.getAssetIdSrch()) ? form.getAssetIdSrch() : null;
		String instCodeSrch = StringUtils.isNotEmpty(form.getInstCodeSrch()) ? form.getInstCodeSrch() : null;
		String assetTypeSrch = StringUtils.isNotEmpty(form.getAssetTypeSrch()) ? form.getAssetTypeSrch() : null;
		String borrowNidSrch = StringUtils.isNotEmpty(form.getBorrowNidSrch()) ? form.getBorrowNidSrch() : null;
		String planNidSrch = StringUtils.isNotEmpty(form.getPlanNidSrch()) ? form.getPlanNidSrch() : null;
		String userNameSrch = StringUtils.isNotEmpty(form.getUserNameSrch()) ? form.getUserNameSrch() : null;
		String labelNameSrch = StringUtils.isNotEmpty(form.getLabelNameSrch()) ? form.getLabelNameSrch() : null;
		String userType = StringUtils.isNotEmpty(form.getUserType()) ? form.getUserType() : null;
		String bankOpenAccountSrch = StringUtils.isNotEmpty(form.getBankOpenAccountSrch())
				? form.getBankOpenAccountSrch() : null;
		String verifyStatusSrch = StringUtils.isNotEmpty(form.getVerifyStatusSrch()) ? form.getVerifyStatusSrch()
				: null;
		String statusSrch = StringUtils.isNotEmpty(form.getStatusSrch()) ? form.getStatusSrch() : null;
		String recieveTimeStartSrch = StringUtils.isNotEmpty(form.getRecieveTimeStartSrch())
				? form.getRecieveTimeStartSrch() : null;
		String recieveTimeEndSrch = StringUtils.isNotEmpty(form.getRecieveTimeEndSrch()) ? form.getRecieveTimeEndSrch()
				: null;

		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("assetIdSrch", assetIdSrch);
		conditionMap.put("instCodeSrch", instCodeSrch);
		conditionMap.put("assetTypeSrch", assetTypeSrch);
		conditionMap.put("borrowNidSrch", borrowNidSrch);
		conditionMap.put("planNidSrch", planNidSrch);
		conditionMap.put("userNameSrch", userNameSrch);
		conditionMap.put("labelNameSrch", labelNameSrch);
		conditionMap.put("bankOpenAccountSrch", bankOpenAccountSrch);
		conditionMap.put("verifyStatusSrch", verifyStatusSrch);
		if(StringUtils.isNotBlank(statusSrch)){
			conditionMap.put("statusSrch", new int[]{Integer.parseInt(statusSrch)});
		}else{
			conditionMap.put("statusSrch", null);
		}
		conditionMap.put("recieveTimeStartSrch", recieveTimeStartSrch);
		conditionMap.put("recieveTimeEndSrch", recieveTimeEndSrch);
		conditionMap.put("userType", userType);
		return conditionMap;
	}

	/**
	 * 下拉联动
	 *
	 * @param request
	 * @return 进入资产列表页面
	 */
	@RequestMapping(AssetListDefine.ASSETTYPE_ACTION)
	@RequiresPermissions(AssetListDefine.PERMISSIONS_VIEW)
	@ResponseBody
	public List<Map<String, Object>> assetTypeAction(HttpServletRequest request, RedirectAttributes attr,
													 String instCode) {
		LogUtil.startLog(AssetListController.class.toString(), AssetListDefine.ASSETTYPE_ACTION);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		// 根据资金来源取得产品类型
		List<HjhAssetType> hjhAssetTypeList = this.assetListService.hjhAssetTypeList(instCode);
		if (hjhAssetTypeList != null && hjhAssetTypeList.size() > 0) {
			for (HjhAssetType hjhAssetType : hjhAssetTypeList) {
				Map<String, Object> mapTemp = new HashMap<String, Object>();
				mapTemp.put("id", hjhAssetType.getAssetType());
				mapTemp.put("text", hjhAssetType.getAssetTypeName());
				resultList.add(mapTemp);
			}
		}
		LogUtil.endLog(AssetListController.class.toString(), AssetListDefine.ASSETTYPE_ACTION);
		return resultList;
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
	@RequestMapping(AssetListDefine.EXPORT_ACTION)
	@RequiresPermissions(AssetListDefine.PERMISSIONS_EXPORT)
	public void exportExcel(@ModelAttribute AssetListBean form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LogUtil.startLog(AssetListController.class.toString(), AssetListDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "资产列表";
		// 文件名称
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
		// 需要输出的结果列表

		// 封装查询条件
		Map<String, Object> conditionMap = setCondition(form);

		List<AssetListCustomize> recordList = this.assetListService.getRecordList(conditionMap, -1, -1);
		String[] titles = new String[] { "序号", "资产编号", "资产来源", "产品类型", "项目编号", "智投编号", "用户名", "手机号", "银行电子账号", "借款类型", "开户状态", "姓名", "身份证号", "借款金额（元）", "借款期限", "还款方式", "审核状态", "项目状态", "标的标签", "推送时间" };
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
					AssetListCustomize data = recordList.get(i);
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {// 序号
						cell.setCellValue(i + 1);
					} else if (celLength == 1) {// 资产编号
						cell.setCellValue(data.getAssetId());
					} else if (celLength == 2) {// 资产来源
						cell.setCellValue(data.getInstName());
					} else if (celLength == 3) {// 产品类型
						cell.setCellValue(data.getAssetTypeName());
					} else if (celLength == 4) {// 项目编号
						cell.setCellValue(data.getBorrowNid());
					} else if (celLength == 5) {// 计划编号
						cell.setCellValue(data.getPlanNid());
					} else if (celLength == 6) {// 用户名
						cell.setCellValue(data.getUserName());
					} else if (celLength == 7) {// 手机号
						cell.setCellValue(data.getMobile());
					} else if (celLength == 8) {// 银行电子账号
						cell.setCellValue(data.getAccountId());
					} else if (celLength == 9) {// 借款类型
						cell.setCellValue(data.getUserType());
					} else if (celLength == 10) {// 开户状态
						cell.setCellValue(data.getBankOpenAccount());
					} else if (celLength == 11) {// 姓名
						cell.setCellValue(data.getTruename());
					} else if (celLength == 12) {// 身份证号
						cell.setCellValue(data.getIdcard());
					} else if (celLength == 13) {// 借款金额（元）
						cell.setCellValue(data.getAccount());
					} else if (celLength == 14) {// 借款期限
						cell.setCellValue(data.getBorrowPeriod());
					} else if (celLength == 15) {// 还款方式
						cell.setCellValue(data.getBorrowStyleName());
					} else if (celLength == 16) {// 审核状态
						cell.setCellValue(data.getVerifyStatus());
					} else if (celLength == 17) {// 项目状态
						cell.setCellValue(data.getStatus());
					} else if (celLength == 18) {// 标的标签
						cell.setCellValue(data.getLabelName());
					} else if (celLength == 19) {// 推送时间
						cell.setCellValue(data.getRecieveTime());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(AssetListController.class.toString(), AssetListDefine.EXPORT_ACTION);
	}

	/**
	 * 详情
	 *
	 * @param request
	 * @return 进入详情页面
	 */
	@RequestMapping(value = AssetListDefine.DETAIL_ACTION, method = RequestMethod.GET)
	@RequiresPermissions(AssetListDefine.PERMISSIONS_INFO)
	public ModelAndView searchUserDetail(@PathVariable("assetId") String assetId, @PathVariable("instCode") String instCode) {
		LogUtil.startLog(AssetListController.class.toString(), AssetListDefine.DETAIL_ACTION);
		ModelAndView modelAndView = new ModelAndView();
		// 根据id查询详情信息
		AssetDetailCustomize assetDetaul = assetListService.getDetailById(assetId, instCode);
		if (assetDetaul.getBorrowCompanyName() == null || assetDetaul.getBorrowCompanyName().equals("")){
			//个人详情
			modelAndView = new ModelAndView(AssetListDefine.DETAIL_PATH);
		}else{
			//企业详情
			modelAndView = new ModelAndView(AssetListDefine.COMPANY_DETAIL_PATH);
		}
		modelAndView.addObject(AssetListDefine.DETAIL_FORM, assetDetaul);
		LogUtil.endLog(AssetListController.class.toString(), AssetListDefine.DETAIL_ACTION);
		return modelAndView;
	}

}
