package com.hyjf.admin.exception.offline.recharge;

import java.math.BigDecimal;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.user.manageruser.ManageUsersDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.customize.admin.OfflineRechargeCustomize;
import com.hyjf.soa.apiweb.CommonSoaUtils;

/**
 * 根据查询条件取得用户线下充值列表
 * 
 * @author zhangjinpeng
 * 
 */
@Controller
@RequestMapping(value = OfflineRechargeDefine.REQUEST_MAPPING)
public class OfflineRechargeController extends BaseController {
	/**
	 * 类名
	 */
	private static final String THIS_CLASS = OfflineRechargeController.class.getName();
	
	/**
	 * 线下充值
	 */
	@Autowired
	private OfflineRechargeService offlineRechargeService;

	/**
	 * 取得用户线下充值列表列表 初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(OfflineRechargeDefine.INIT)
	@RequiresPermissions(OfflineRechargeDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, OfflineRechargeBean form) {
		LogUtil.startLog(THIS_CLASS, OfflineRechargeDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(OfflineRechargeDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, OfflineRechargeDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(OfflineRechargeDefine.SEARCH_ACTION)
	@RequiresPermissions(OfflineRechargeDefine.PERMISSIONS_VIEW)
	public ModelAndView search(HttpServletRequest request, OfflineRechargeBean form) {
		LogUtil.startLog(THIS_CLASS, OfflineRechargeDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(OfflineRechargeDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, OfflineRechargeDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 分页
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, OfflineRechargeBean form) {
		// 根据查询条件，远程调用即信接口查询用户的线下充值列表
		JSONObject result = this.getOfflineRechargeList(form);
		if(null != result && result.getInteger("status") != 1){
			// 将返回的json格式的字符串转换为充值明细列表
			List<ResultBean> recordList = JSONArray.parseArray(result.getString("data"), ResultBean.class);
			form.setRecordList(recordList);
			modelAndView.addObject("truename", result.getString("truename"));
			modelAndView.addObject("mobile", result.getString("mobile"));
		}
		modelAndView.addObject("rechargelistForm", form);
		
	}
	
	/**
	 * 根据查询条件，远程调用即信接口查询用户的线下充值列表
	 * @param form
	 * @return
	 */
	private JSONObject getOfflineRechargeList(OfflineRechargeBean form){
		JSONObject result = null;
		List<OfflineRechargeCustomize> usersAccountList = offlineRechargeService.selectUserAccount(form);
		OfflineRechargeCustomize userAccount = null;
		// 取得唯一用户
		if(usersAccountList != null && usersAccountList.size() == 1){
			userAccount = usersAccountList.get(0);
		}else{
			// 用户不存在或多个用户
			return result;
		}
		Map<String,String> searchCon = new HashMap<String,String>();
		// 用户编号
		searchCon.put("userId", userAccount.getUserId());
		// 用户名
		searchCon.put("username", userAccount.getUsername());
		// 用户开户电子账号
		searchCon.put("bankOpenAccount", userAccount.getAccount());
		// 开始时间
		if(StringUtils.isNotEmpty(form.getStartTimeSrch())){
			searchCon.put("startTimeSrch", form.getStartTimeSrch());
		}
		// 结束时间
		if(StringUtils.isNotEmpty(form.getEndTimeSrch())){
			searchCon.put("endTimeSrch", form.getEndTimeSrch());
		}
		// 如果检索条件为空则不进行查询
		if(searchCon.isEmpty()){
			return result;
		}
		result = CommonSoaUtils.offlineRechargeList(searchCon);
		result.put("truename", userAccount.getTruename());
		result.put("mobile", userAccount.getMobile());
		return result;
		
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
	@RequestMapping(OfflineRechargeDefine.EXPORT_OFFLINE_RECHARGE_ACTION)
	@RequiresPermissions(OfflineRechargeDefine.PERMISSIONS_EXPORT)
	public void exportOfflineRecharge(@ModelAttribute OfflineRechargeBean form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		LogUtil.startLog(OfflineRechargeDefine.THIS_CLASS, OfflineRechargeDefine.EXPORT_OFFLINE_RECHARGE_ACTION);
		// 表格sheet名称
		String sheetName = "用户线下充值明细列表";
		// 文件名称
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;
		
		// 将返回的json格式的字符串转换为充值明细列表
		List<ResultBean> recordList = null;
		JSONObject obj = this.getOfflineRechargeList(form);
		if(null != obj && obj.getInteger("status") != 1){
			recordList = JSONArray.parseArray(obj.getString("data"), ResultBean.class);
		}
		
		String[] titles = new String[] { "序号", "真实姓名", "手机号码", "电子账号", "交易金额", "交易后余额", "交易日期", "交易时间", "状态", "交易描述"};
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
					ResultBean result = recordList.get(i);
					String truename = obj.getString("truename");
					String mobile = obj.getString("mobile");
					// 电子账号
					String accountId = StringUtils.isEmpty(result.getAccountId())?StringUtils.EMPTY:result.getAccountId();
					// 交易金额
					BigDecimal txAmount = result.getTxAmount()==null?BigDecimal.ZERO:result.getTxAmount();
					// 交易后余额
					BigDecimal currBal = result.getCurrBal()==null?BigDecimal.ZERO:result.getCurrBal();
					// 交易日期
					String inpDate = StringUtils.isEmpty(result.getInpDate())?StringUtils.EMPTY:result.getInpDate();
					// 交易时间
					String inpTime = StringUtils.isEmpty(result.getInpTime())?StringUtils.EMPTY:result.getInpTime();
					// 状态 O-原始交易 R-已经冲正或者撤销
					String orFlag = result.getOrFlag();
					// 交易描述
					String describe = result.getDescribe();
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {// 序号
						cell.setCellValue(i + 1);
					} else if (celLength == 1) { // 真实姓名
						cell.setCellValue(truename);
					} else if (celLength == 2) { // 手机号码
						cell.setCellValue(mobile);
					} else if (celLength == 3) { // 电子账号
						cell.setCellValue(accountId);
					} else if (celLength == 4) { // 交易金额
                        cell.setCellValue(txAmount.toString());
                    } else if (celLength == 5) { // 交易后余额
						cell.setCellValue(currBal.toString());
					} else if (celLength == 6) { // 交易日期
						cell.setCellValue(inpDate);
					} else if (celLength == 7) {// 交易时间
						cell.setCellValue(inpTime);
					} else if (celLength == 8) {// 状态 O-原始交易 R-已经冲正或者撤销
						if(StringUtils.equals("O", orFlag)){
							cell.setCellValue("原始交易");
						}else if(StringUtils.equals("R", orFlag)){
							cell.setCellValue("冲正或撤销");
						}else{
							cell.setCellValue(StringUtils.EMPTY);
						}
						
					} else if (celLength == 9) {// 交易描述
						cell.setCellValue(describe);
					} 
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.EXPORT_USERS_ACTION);
	}

}
