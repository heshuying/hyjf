package com.hyjf.admin.manager.activity.newyear2016;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.invite.GetRecommend.GetRecommendDefine;
import com.hyjf.admin.manager.user.manageruser.ManageUsersDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.admin.Newyear2016UserCardCustomize;
import com.hyjf.mybatis.model.customize.admin.Newyear2016UserPrizeCustomize;
import com.hyjf.mybatis.model.customize.admin.Newyear2016UserYuanXiaoCustomize;

/**
 * 新年活动发放奖品列表
 * 
 * @author zhangjp
 *
 */
@Controller
@RequestMapping(value = NewyearDefine.REQUEST_MAPPING)
public class NewyearController extends BaseController {

	@Autowired
	private NewyearService newyearService;
	
	/*********************************************财神来敲我家门用户财神卡列表**************************************/
	/**
	 * 财神敲门活动画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(NewyearDefine.CAI_SHEN_LIST_INIT)
	@RequiresPermissions(NewyearDefine.PERMISSIONS_VIEW)
	public ModelAndView caiShenListInit(HttpServletRequest request, UserCardListBean form) {
		LogUtil.startLog(NewyearController.class.toString(), NewyearDefine.CAI_SHEN_LIST_INIT);
		ModelAndView modelAndView = new ModelAndView(NewyearDefine.CAI_SHEN_LIST_PATH);

		// 创建分页
		this.createPageCai(request, modelAndView, form);
		LogUtil.endLog(NewyearController.class.toString(), NewyearDefine.CAI_SHEN_LIST_INIT);
		return modelAndView;
	}

	/**
	 * 财神敲门活动画面查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(NewyearDefine.SEARCH_CAI_SHEN_ACTION)
	@RequiresPermissions(NewyearDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchCaiShenAction(HttpServletRequest request, UserCardListBean form) {
		LogUtil.startLog(NewyearController.class.toString(), NewyearDefine.SEARCH_CAI_SHEN_ACTION);
		ModelAndView modelAndView = new ModelAndView(NewyearDefine.CAI_SHEN_LIST_PATH);
		// 创建分页
		this.createPageCai(request, modelAndView, form);
		LogUtil.endLog(NewyearController.class.toString(), NewyearDefine.SEARCH_CAI_SHEN_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能(财神敲门)
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPageCai(HttpServletRequest request, ModelAndView modelAndView, UserCardListBean form) {
		// 财神卡操作途径
		List<ParamName> cardSourceList = this.newyearService.getParamNameList("NEWYEAR_CARD_SOURCE");
		modelAndView.addObject("cardSourceList", cardSourceList);
		// 财神卡类别
		List<ParamName> cardTypeList = this.newyearService.getParamNameList("NEWYEAR_CARD_TYPE");
		modelAndView.addObject("cardTypeList", cardTypeList);
		Integer count = this.newyearService.selectUserCardRecordCount(form);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			form.setLimitStart(paginator.getOffset());
			form.setLimitEnd(paginator.getLimit());
			List<Newyear2016UserCardCustomize> recordList = this.newyearService.selectUserCardRecordList(form);
			form.setPaginator(paginator);
			form.setRecordList(recordList);
		}

		modelAndView.addObject(NewyearDefine.USER_CARD_LIST_FORM, form);
	}
	
	/***********************************************红红火火闹元宵猜灯谜列表******************************************/
	/**
	 * 红红火火闹元宵猜灯谜列表初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(NewyearDefine.YUAN_XIAO_LIST_INIT)
	@RequiresPermissions(NewyearDefine.PERMISSIONS_VIEW)
	public ModelAndView yuanXiaoListInit(HttpServletRequest request, UserYuanXiaoListBean form) {
		LogUtil.startLog(NewyearController.class.toString(), NewyearDefine.YUAN_XIAO_LIST_INIT);
		ModelAndView modelAndView = new ModelAndView(NewyearDefine.YUAN_XIAO_LIST_PATH);

		// 创建分页
		this.createPageYuanXiao(request, modelAndView, form);
		LogUtil.endLog(NewyearController.class.toString(), NewyearDefine.YUAN_XIAO_LIST_INIT);
		return modelAndView;
	}

	/**
	 * 红红火火闹元宵猜灯谜列表查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(NewyearDefine.SEARCH_YUAN_XIAO_ACTION)
	@RequiresPermissions(NewyearDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchYuanXiaoAction(HttpServletRequest request, UserYuanXiaoListBean form) {
		LogUtil.startLog(NewyearController.class.toString(), NewyearDefine.SEARCH_YUAN_XIAO_ACTION);
		ModelAndView modelAndView = new ModelAndView(NewyearDefine.YUAN_XIAO_LIST_PATH);
		// 创建分页
		this.createPageYuanXiao(request, modelAndView, form);
		LogUtil.endLog(NewyearController.class.toString(), NewyearDefine.SEARCH_YUAN_XIAO_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能(红红火火闹元宵猜灯谜列表)
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPageYuanXiao(HttpServletRequest request, ModelAndView modelAndView, UserYuanXiaoListBean form) {
		Integer count = this.newyearService.selectUserYuanXiaoRecordCount(form);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			form.setLimitStart(paginator.getOffset());
			form.setLimitEnd(paginator.getLimit());
			List<Newyear2016UserYuanXiaoCustomize> recordList = this.newyearService.selectUserYuanXiaoRecordList(form);
			form.setPaginator(paginator);
			form.setRecordList(recordList);
		}

		modelAndView.addObject(NewyearDefine.USER_YUAN_XIAO_LIST_FORM, form);
	}
	
	/*************************************************用户奖品发放列表******************************************/
	/**
	 * 用户奖品发放列表页面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(NewyearDefine.USER_PRIZE_LIST_INIT)
	@RequiresPermissions(NewyearDefine.PERMISSIONS_VIEW)
	public ModelAndView userPrizeListInit(HttpServletRequest request, UserPrizeListBean form) {
		LogUtil.startLog(NewyearController.class.toString(), NewyearDefine.USER_PRIZE_LIST_INIT);
		ModelAndView modelAndView = new ModelAndView(NewyearDefine.USER_PRIZE_LIST_PATH);

		// 创建分页
		this.createPagePrize(request, modelAndView, form);
		LogUtil.endLog(NewyearController.class.toString(), NewyearDefine.USER_PRIZE_LIST_INIT);
		return modelAndView;
	}

	/**
	 * 用户奖品发放列表页面查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(NewyearDefine.SEARCH_USER_PRIZE_ACTION)
	@RequiresPermissions(NewyearDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchUserPrizeAction(HttpServletRequest request, UserPrizeListBean form) {
		LogUtil.startLog(NewyearController.class.toString(), NewyearDefine.SEARCH_USER_PRIZE_ACTION);
		ModelAndView modelAndView = new ModelAndView(NewyearDefine.USER_PRIZE_LIST_PATH);
		// 创建分页
		this.createPagePrize(request, modelAndView, form);
		LogUtil.endLog(NewyearController.class.toString(), NewyearDefine.SEARCH_USER_PRIZE_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能(用户奖品发放列表)
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPagePrize(HttpServletRequest request, ModelAndView modelAndView, UserPrizeListBean form) {
		Integer count = this.newyearService.selectUserPrizeRecordCount(form);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			form.setLimitStart(paginator.getOffset());
			form.setLimitEnd(paginator.getLimit());
			List<Newyear2016UserPrizeCustomize> recordList = this.newyearService.selectUserPrizeRecordList(form);
			form.setPaginator(paginator);
			form.setRecordList(recordList);
		}

		modelAndView.addObject(NewyearDefine.USER_PRIZE_LIST_FORM, form);
	}

	/**
	 * 数据导出(财神敲门活动)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(NewyearDefine.EXPORT_CAI_SHEN_ACTION)
	@RequiresPermissions(NewyearDefine.PERMISSIONS_EXPORT)
	public void exportCaiShenAction(HttpServletRequest request, HttpServletResponse response, UserCardListBean form)
			throws Exception {

		LogUtil.startLog(GetRecommendDefine.THIS_CLASS, GetRecommendDefine.EXPORT_INVITE_USER_ACTION);
		// 表格sheet名称
		String sheetName = "用户财神卡明细列表";
		// 文件名称
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;

		List<Newyear2016UserCardCustomize> recordList = this.newyearService.selectUserCardRecordList(form);
		String[] titles = new String[] { "序号", "用户名", "姓名", "手机号", "财神卡操作", "操作途径", "财神卡", "金", "鸡" , "纳", "福" , "操作时间", "备注" };
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
					Newyear2016UserCardCustomize prize = recordList.get(i);
					String userName = StringUtils.isEmpty(prize.getUsername()) ? StringUtils.EMPTY
							: prize.getUsername();
					String trueName = StringUtils.isEmpty(prize.getTruename()) ? StringUtils.EMPTY
							: prize.getTruename();
					String mobile = StringUtils.isEmpty(prize.getMobile()) ? StringUtils.EMPTY : prize.getMobile();
					String operateTypeStr = StringUtils.isEmpty(prize.getOperateTypeStr()) ? StringUtils.EMPTY
							: prize.getOperateTypeStr();
					String cardSourceStr = StringUtils.isEmpty(prize.getCardSourceStr()) ? StringUtils.EMPTY
							: prize.getCardSourceStr();
					String cardTypeStr = StringUtils.isEmpty(prize.getCardTypeStr()) ? StringUtils.EMPTY
							: prize.getCardTypeStr();
					Integer cardJinQuantity = prize.getCardJinQuantity() == null ? 0 : prize.getCardJinQuantity();
					Integer cardJiQuantity = prize.getCardJiQuantity() == null ? 0 : prize.getCardJiQuantity();
					Integer cardNaQuantity = prize.getCardNaQuantity() == null ? 0 : prize.getCardNaQuantity();
					Integer cardFuQuantity = prize.getCardFuQuantity() == null ? 0 : prize.getCardFuQuantity();
					String operateTime = StringUtils.isEmpty(prize.getOperateTime()) ? StringUtils.EMPTY
							: prize.getOperateTime();
					String remark = StringUtils.isEmpty(prize.getRemark()) ? StringUtils.EMPTY
							: prize.getRemark();
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {// 序号
						cell.setCellValue(i + 1);
					} else if (celLength == 1) { // 用户名
						cell.setCellValue(userName);
					} else if (celLength == 2) { // 姓名
						cell.setCellValue(trueName);
					} else if (celLength == 3) { // 手机号
						cell.setCellValue(mobile);
					} else if (celLength == 4) { // 财神卡操作
						cell.setCellValue(operateTypeStr);
					} else if (celLength == 5) {// 操作途径
						cell.setCellValue(cardSourceStr);
					} else if (celLength == 6) {// 财神卡类别
						cell.setCellValue(cardTypeStr);
					} else if (celLength == 7) {// 财神卡（金）数量
						cell.setCellValue(cardJinQuantity);
					} else if (celLength == 8) {// 财神卡（鸡）数量
						cell.setCellValue(cardJiQuantity);
					} else if (celLength == 9) {// 财神卡（纳）数量
						cell.setCellValue(cardNaQuantity);
					} else if (celLength == 10) {// 财神卡（福）数量
						cell.setCellValue(cardFuQuantity);
					} else if (celLength == 11) {// 操作时间
						cell.setCellValue(operateTime);
					} else if (celLength == 12) {// 备注
						cell.setCellValue(remark);
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.EXPORT_USERS_ACTION);
	}
	
	/**
	 * 数据导出(红红火火闹元宵猜灯谜列表)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(NewyearDefine.EXPORT_YUAN_XIAO_ACTION)
	@RequiresPermissions(NewyearDefine.PERMISSIONS_EXPORT)
	public void exportYuanXiaoAction(HttpServletRequest request, HttpServletResponse response, UserYuanXiaoListBean form)
			throws Exception {

		LogUtil.startLog(GetRecommendDefine.THIS_CLASS, GetRecommendDefine.EXPORT_INVITE_USER_ACTION);
		// 表格sheet名称
		String sheetName = "用户猜灯谜明细列表";
		// 文件名称
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;

		List<Newyear2016UserYuanXiaoCustomize> recordList = this.newyearService.selectUserYuanXiaoRecordList(form);
		String[] titles = new String[] { "序号", "用户名", "姓名", "手机号", "答题序号", "答题时间", "当前奖励" };
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
					Newyear2016UserYuanXiaoCustomize prize = recordList.get(i);
					String userName = StringUtils.isEmpty(prize.getUsername()) ? StringUtils.EMPTY
							: prize.getUsername();
					String trueName = StringUtils.isEmpty(prize.getTruename()) ? StringUtils.EMPTY
							: prize.getTruename();
					String mobile = StringUtils.isEmpty(prize.getMobile()) ? StringUtils.EMPTY : prize.getMobile();
					Integer questionNum = prize.getQuestionNum()==null?0:prize.getQuestionNum();
					String questionTime = prize.getQuestionTime();
					String viewName = prize.getViewName();
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {// 序号
						cell.setCellValue(i + 1);
					} else if (celLength == 1) { // 用户名
						cell.setCellValue(userName);
					} else if (celLength == 2) { // 姓名
						cell.setCellValue(trueName);
					} else if (celLength == 3) { // 手机号
						cell.setCellValue(mobile);
					} else if (celLength == 4) { // 问题序号
						cell.setCellValue(questionNum);
					} else if (celLength == 5) {// 答题时间
						cell.setCellValue(questionTime);
					} else if (celLength == 6) {// 奖励
						cell.setCellValue(viewName);
					} 
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.EXPORT_USERS_ACTION);
	}

	/**
	 * 数据导出(用户奖品发放列表)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(NewyearDefine.EXPORT_USER_PRIZE_ACTION)
	@RequiresPermissions(NewyearDefine.PERMISSIONS_EXPORT)
	public void exportPrizeAction(HttpServletRequest request, HttpServletResponse response, UserPrizeListBean form)
			throws Exception {

		LogUtil.startLog(GetRecommendDefine.THIS_CLASS, GetRecommendDefine.EXPORT_INVITE_USER_ACTION);
		// 表格sheet名称
		String sheetName = "用户奖励发放明细列表";
		// 文件名称
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;

		List<Newyear2016UserPrizeCustomize> recordList = this.newyearService.selectUserPrizeRecordList(form);
		String[] titles = new String[] { "序号", "用户名", "姓名", "手机号", "活动来源", "优惠券编号", "优惠券面额", "发放时间", "状态" };
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
					Newyear2016UserPrizeCustomize prize = recordList.get(i);
					String userName = StringUtils.isEmpty(prize.getUsername()) ? StringUtils.EMPTY
							: prize.getUsername();
					String trueName = StringUtils.isEmpty(prize.getTruename()) ? StringUtils.EMPTY
							: prize.getTruename();
					String mobile = StringUtils.isEmpty(prize.getMobile()) ? StringUtils.EMPTY : prize.getMobile();
					String activity = StringUtils.isEmpty(prize.getActivity()) ? StringUtils.EMPTY
							: prize.getActivity();
					String couponCode = StringUtils.isEmpty(prize.getCouponCode()) ? StringUtils.EMPTY
							: prize.getCouponCode();
					Integer couponJine = prize.getCouponJine();
					String sendTime = prize.getSendTime();
					String sendStatus = prize.getSendStatusStr();
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {// 序号
						cell.setCellValue(i + 1);
					} else if (celLength == 1) { // 用户名
						cell.setCellValue(userName);
					} else if (celLength == 2) { // 姓名
						cell.setCellValue(trueName);
					} else if (celLength == 3) { // 手机号
						cell.setCellValue(mobile);
					} else if (celLength == 4) { // 所属活动
						cell.setCellValue(activity);
					} else if (celLength == 5) {// 优惠券编号
						cell.setCellValue(couponCode);
					} else if (celLength == 6) {// 优惠券面额
						cell.setCellValue(couponJine);
					} else if (celLength == 7) {// 发放时间
						cell.setCellValue(sendTime);
					} else if (celLength == 8) {// 状态
						cell.setCellValue(sendStatus);
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.EXPORT_USERS_ACTION);
	}
}
