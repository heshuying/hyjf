package com.hyjf.admin.manager.huixiaofei;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.Consume;
import com.hyjf.mybatis.model.auto.ConsumeList;

/**
 * 活动列表页
 * 
 * @author qingbing
 *
 */
@Controller
@RequestMapping(value = HuixiaofeiDefine.REQUEST_MAPPING)
public class HuixiaofeiController extends BaseController {

	@Autowired
	private HuixiaofeiService huixiaofeiService;

	/**
	 * 汇消费首页
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HuixiaofeiDefine.INIT)
	@RequiresPermissions(HuixiaofeiDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(HuixiaofeiDefine.HUIXIAOFEI_FORM) HuixiaofeiBean form) {
		LogUtil.startLog(HuixiaofeiController.class.toString(), HuixiaofeiDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(HuixiaofeiDefine.LIST_PATH);

		// 创建分页
		createPage(request, modelAndView, form);
		LogUtil.endLog(HuixiaofeiController.class.toString(), HuixiaofeiDefine.INIT);
		return modelAndView;
	}

	/**
	 * 合作伙伴维护条件查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HuixiaofeiDefine.SEARCH_ACTION)
	@RequiresPermissions(HuixiaofeiDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(HuixiaofeiDefine.HUIXIAOFEI_FORM) HuixiaofeiBean form) {
		LogUtil.startLog(HuixiaofeiController.class.toString(), HuixiaofeiDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(HuixiaofeiDefine.LIST_PATH);
		// 创建分页
		createPage(request, modelAndView, form);
		LogUtil.endLog(HuixiaofeiController.class.toString(), HuixiaofeiDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 汇消费 页面初始化
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, HuixiaofeiBean form) {
		if (form.getStatus() != null) {
			if (form.getStatus() == 3) {
				form.setStatus(null);
				form.setRelease(HuixiaofeiDefine.CONSUME_RELEASE_1);
			} else {
				form.setRelease(HuixiaofeiDefine.CONSUME_RELEASE_0);
			}
		}
		// 此处查分页的方法这么垃圾
		List<Consume> recordList = huixiaofeiService.getConsumeRecordList(form, -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = huixiaofeiService.getConsumeRecordList(form, paginator.getOffset(), paginator.getLimit());
			// 格式化时间
			for (int i = 0; i < recordList.size(); i++) {
				if (StringUtils.isNotEmpty(recordList.get(i).getInsertTime())) {
					try {
						recordList.get(i).setInsertTime(
								GetDate.getDateTimeMyTime(Integer.parseInt(recordList.get(i).getInsertTime())));
					} catch (Exception e) {
						// 时间转换出错
					}
				}
			}
			form.setPaginator(paginator);
			// form.setForBack(list);
			// 获取广告类型列表
			form.setRecordList(recordList);
			modelAndView.addObject(HuixiaofeiDefine.HUIXIAOFEI_FORM, form);
		}
	}

	/**
	 * 跳转到编辑查看页面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HuixiaofeiDefine.INFO_ACTION)
	@RequiresPermissions(HuixiaofeiDefine.PERMISSIONS_INFO)
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(HuixiaofeiDefine.HUIXIAOFEI_FORM) HuixiaofeiBean form) {
		LogUtil.startLog(HuixiaofeiController.class.toString(), HuixiaofeiDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(HuixiaofeiDefine.INFO_PATH);
		if (form.getId() != null) {
			Consume consume = null;
			// 第一步:查询consume
			{
				consume = huixiaofeiService.getRecordByID(form.getId());
				modelAndView.addObject(HuixiaofeiDefine.HUIXIAOFEICONSUME, consume);
			}
			// 第二步:查询consume下的consumeList
			{
				// 组装查询条件
				if (consume != null) {
					HuixiaofeiListBean bean = new HuixiaofeiListBean();
					bean.setConsumeId(consume.getConsumeId());
					bean.setStatus(form.getStatus());
					bean.setInsertTime(consume.getInsertTime());
					List<ConsumeList> recordList = huixiaofeiService.getConsumeListByCondition(bean, -1, -1);
					if (recordList != null) {
						Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
						recordList = huixiaofeiService.getConsumeListByCondition(bean, paginator.getOffset(),
								paginator.getLimit());
						form.setPaginator(paginator);
						modelAndView.addObject(HuixiaofeiDefine.HUIXIAOFEILISTList, recordList);
					}
					// 第三步:查询是否可以显示处理按钮
					{
						Boolean showChuli = huixiaofeiService.isAllOver(consume.getConsumeId(), consume.getInsertDay());
						modelAndView.addObject("showChuli", showChuli);
					}
				}
			}
			// {
			modelAndView.addObject(HuixiaofeiDefine.HUIXIAOFEI_FORM, form);
			// {
			// // 转换时间
			// if (StringUtils.isNotEmpty(consume.getInsertTime())) {
			// try {
			// consume.setInsertTime(
			// GetDate.getDateTimeMyTime(Integer.parseInt(consume.getInsertTime())));
			// } catch (Exception e) {
			// // 时间转换出错
			// }
			// }
			// }
			// }
		}
		LogUtil.endLog(HuixiaofeiController.class.toString(), HuixiaofeiDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 跳转到黑名单页面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HuixiaofeiDefine.TOHEIMINGDAN)
	@RequiresPermissions(HuixiaofeiDefine.PERMISSIONS_INFO)
	public ModelAndView toHeimingdanAction(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(HuixiaofeiDefine.HUIXIAOFEI_FORM) HuixiaofeiBean form) {
		LogUtil.startLog(HuixiaofeiController.class.toString(), HuixiaofeiDefine.TOHEIMINGDAN);
		ModelAndView modelAndView = new ModelAndView(HuixiaofeiDefine.HEIMINGDAN_PATH);
		HuixiaofeiListBean bean = new HuixiaofeiListBean();
		bean.setConsumeId(form.getConsumeId());
		bean.setStatus(HuixiaofeiDefine.CONSUMELIST_STATUS_3);
		List<ConsumeList> recordList = huixiaofeiService.getConsumeListByCondition(bean, -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = huixiaofeiService.getConsumeListByCondition(bean, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			modelAndView.addObject(HuixiaofeiDefine.HUIXIAOFEILISTList, recordList);
		}
		form.setStatus(3);
		modelAndView.addObject(HuixiaofeiDefine.HUIXIAOFEI_FORM, form);
		LogUtil.endLog(HuixiaofeiController.class.toString(), HuixiaofeiDefine.TOHEIMINGDAN);
		return modelAndView;
	}

	/**
	 * 数据导出
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HuixiaofeiDefine.HEIMINGDANEXPORT_ACTION)
	@RequiresPermissions(HuixiaofeiDefine.PERMISSIONS_EXPORT)
	public void heimingdanExportAction(HttpServletRequest request, HttpServletResponse response, HuixiaofeiBean form)
			throws Exception {
		LogUtil.startLog(HuixiaofeiController.class.toString(), HuixiaofeiDefine.HEIMINGDANEXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "达飞数据导出";
		// 第二步:查询consume下的consumeList
		{
			// 组装查询条件
			HuixiaofeiListBean bean = new HuixiaofeiListBean();
			bean.setConsumeId(form.getConsumeId());
			bean.setStatus(HuixiaofeiDefine.CONSUMELIST_STATUS_3);
			bean.setInsertTime(form.getInsertTime());
			List<ConsumeList> recordList = huixiaofeiService.getConsumeListByCondition(bean, -1, -1);
			String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
					+ CustomConstants.EXCEL_EXT;

			// String[] titles = new String[] { "序号", "编号", "姓名", "身份证号",
			// "贷款日期", "贷款金额", "首付", "期数", "联系方式",
			// "身份证号过期日", "发证机关", "工作单位/学校", "开户行", "银行账号", "月收入", "地址",
			// "插入日期时间戳", "状态", "性别", "打包状态",
			// "插入日期" };
			String[] titles = new String[] { "贷款金额", "贷款期限","周期类型", "贷款日期", "姓名", "身份证号", "手机号码", "座机号码", "银行卡号", "QQ号码",
					"邮箱地址", "学历", "婚姻", "户籍地址", "家庭地址", "工作单位", "单位地址", "通讯地址", "职位", "公司行业", "用户承诺3个月内是否在它平台申请借款",
					"身份证与姓名是否核实", "第一联系人姓名", "第一联系人手机号码", "第一联系人家庭地址", "第一联系人工作单位", "第一联系人单位地址", "第二联系人姓名", "第二联系人手机号码",
					"第二联系人家庭地址", "第二联系人工作单位", "第二联系人单位地址", "第三联系人姓名", "第三联系人手机号码", "第三联系人家庭地址", "第三联系人工作单位",
					"第三联系人单位地址" };
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
						ConsumeList record = recordList.get(i);

						// 创建相应的单元格
						Cell cell = row.createCell(celLength);

						// // 序号
						// if (celLength == 0) {
						// cell.setCellValue(i + 1);
						// }
						// // 编号
						// else if (celLength == 1) {
						// cell.setCellValue(record.getConsumeId());
						// }
						// // 姓名
						// else if (celLength == 2) {
						// cell.setCellValue(record.getPersonName());
						// }
						// // 身份证号
						// else if (celLength == 3) {
						// cell.setCellValue(record.getIdent() + " ");
						// }
						// // 贷款日期
						// else if (celLength == 4) {
						// cell.setCellValue(record.getLoanDate());
						// }
						// // 贷款金额
						// else if (celLength == 5) {
						// cell.setCellValue(record.getCreditAmount().doubleValue());
						// }
						// // 首付
						// else if (celLength == 6) {
						// cell.setCellValue(record.getInitPay());
						// }
						// // 期数
						// else if (celLength == 7) {
						// cell.setCellValue(record.getInstalmentNum());
						// }
						// // 联系方式
						// else if (celLength == 8) {
						// cell.setCellValue(record.getMobile());
						// }
						//
						// // 身份证过期日
						// else if (celLength == 9) {
						// cell.setCellValue(record.getIdentExp());
						// }
						// // 发证机关
						// else if (celLength == 10) {
						// cell.setCellValue(record.getIdentAuth());
						// }
						// // 工作单位/学校
						// else if (celLength == 11) {
						// cell.setCellValue(record.getCompany());
						// }
						// // 开户行
						// else if (celLength == 12) {
						// cell.setCellValue(record.getBankName());
						// }
						// // 银行账号
						// else if (celLength == 13) {
						// cell.setCellValue(record.getAccountNo());
						// }
						// // 月收入
						// else if (celLength == 14) {
						// cell.setCellValue(record.getIncome().doubleValue());
						// }
						// // 地址
						// else if (celLength == 15) {
						// cell.setCellValue(record.getAddress());
						// }
						// // 插入时间戳
						// else if (celLength == 16) {
						// cell.setCellValue(record.getInsertTime());
						// }
						// // 状态
						// else if (celLength == 17) {
						// switch (record.getStatus()) {
						// case 0:
						// cell.setCellValue("通过");
						// break;
						// case 1:
						// cell.setCellValue("不通过");
						// break;
						// }
						// }
						// // 性别
						// else if (celLength == 18) {
						// switch (record.getSex()) {
						// case "f":
						// cell.setCellValue("女");
						// break;
						// case "m":
						// cell.setCellValue("男");
						// break;
						// }
						// }
						// // 打包状态
						// else if (celLength == 19) {
						// switch (record.getRelease()) {
						// case 0:
						// cell.setCellValue("未打包");
						// break;
						// case 1:
						// cell.setCellValue("已打包");
						// break;
						// }
						// }
						// // 插入日期
						// else if (celLength == 20) {
						// cell.setCellValue(record.getInsertDay());
						// }

						// 贷款金额
						if (celLength == 0) {
							cell.setCellValue(record.getCreditAmount().doubleValue());
						}
						// 贷款期限
						else if (celLength == 1) {
							cell.setCellValue(record.getInstalmentNum());
						}
						// 周期类型
						else if (celLength == 2) {
							cell.setCellValue("月");
						}
						// 贷款日期
						else if (celLength == 3) {
							cell.setCellValue(record.getLoanDate());
						}
						// 姓名
						else if (celLength == 4) {
							cell.setCellValue(record.getPersonName());
						}
						// 身份证号
						else if (celLength == 5) {
							cell.setCellValue(record.getIdent());
						}
						// 手机号码
						else if (celLength == 6) {
							cell.setCellValue(record.getMobile());
						}
						// 座机号码
						else if (celLength == 7) {
							cell.setCellValue("");
						}
						// 银行卡号
						else if (celLength == 8) {
							cell.setCellValue(record.getAccountNo());
						}
						// QQ号码
						else if (celLength == 9) {
							cell.setCellValue("");
						}
						// 邮箱地址
						else if (celLength == 10) {
							cell.setCellValue("");
						}
						// 学历
						else if (celLength == 11) {
							cell.setCellValue("");
						}
						// 婚姻
						else if (celLength == 12) {
							cell.setCellValue("");
						}
						// 户籍地址
						else if (celLength == 13) {
							cell.setCellValue(record.getAddress());
						}
						// 家庭地址
						else if (celLength == 14) {
							cell.setCellValue(record.getAddress());
						}
						// 工作单位
						else if (celLength == 15) {
							cell.setCellValue(record.getCompany());
						}
						// 以下都是空

					}
				}
			}
			// 导出
			ExportExcel.writeExcelFile(response, workbook, titles, fileName);
			LogUtil.endLog(HuixiaofeiController.class.toString(), HuixiaofeiDefine.HEIMINGDANEXPORT_ACTION);
		}
	}

	@ResponseBody
	@RequestMapping(value = HuixiaofeiDefine.VALIDPACKAGEFORM)
	@RequiresPermissions(HuixiaofeiDefine.PERMISSIONS_PACKAGE)
	public Map<String, Object> validPackageFormAction(HttpServletRequest request) throws ParseException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", false);
		String id = request.getParameter("id");
		LogUtil.startLog(HuixiaofeiController.class.toString(), HuixiaofeiDefine.VALIDPACKAGEFORM);
		{
			if (StringUtils.isEmpty(id) || id.equals("null")) {
				resultMap.put("msg", "打包id不可为空");
				return resultMap;
			}
		}
		{
			Consume consume = huixiaofeiService.getRecordByID(Integer.parseInt(id));
			if (consume == null) {
				resultMap.put("msg", "无效的打包ID");
				return resultMap;
			}
			if (consume.getRelease() == HuixiaofeiDefine.CONSUMELIST_RELEASE_1) {
				resultMap.put("msg", "该包已打包,不可重复打包");
				return resultMap;
			}
			resultMap.put("consumeId", consume.getConsumeId());
		}
		LogUtil.endLog(HuixiaofeiController.class.toString(), HuixiaofeiDefine.VALIDPACKAGEFORM);
		resultMap.put("success", true);
		return resultMap;
	}

	@ResponseBody
	@RequestMapping(value = HuixiaofeiDefine.DOWNLOADDATA)
	@RequiresPermissions(HuixiaofeiDefine.PERMISSIONS_EXPORT)
	public Map<String, Object> downLoadDataAction(HttpServletRequest request) throws ParseException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", false);
		LogUtil.startLog(HuixiaofeiController.class.toString(), HuixiaofeiDefine.DOWNLOADDATA);
		{
			try {
				huixiaofeiService.downLoadDataAction();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				resultMap.put("msg", e.getMessage());
				LogUtil.errorLog(HuixiaofeiController.class.toString(), HuixiaofeiDefine.DOWNLOADDATA, e);
				return resultMap;
			}
		}
		LogUtil.endLog(HuixiaofeiController.class.toString(), HuixiaofeiDefine.DOWNLOADDATA);
		resultMap.put("success", true);
		return resultMap;
	}

	@RequestMapping(value = HuixiaofeiDefine.SHENHEINFO)
	@RequiresPermissions(HuixiaofeiDefine.PERMISSIONS_AUDIT)
	public ModelAndView shenheInfo(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		LogUtil.startLog(HuixiaofeiController.class.toString(), HuixiaofeiDefine.SHENHEINFO);
		ModelAndView modelAndView = new ModelAndView(HuixiaofeiDefine.SHENHEINFO_PATH);
		String ids = request.getParameter("ids");
		modelAndView.addObject("ids", ids);
		LogUtil.endLog(HuixiaofeiController.class.toString(), HuixiaofeiDefine.SHENHEINFO);
		return modelAndView;
	}

	@ResponseBody
	@RequestMapping(value = HuixiaofeiDefine.SHENHE)
	@RequiresPermissions(HuixiaofeiDefine.PERMISSIONS_AUDIT)
	public Map<String, Object> shenhe(HttpServletRequest request) throws ParseException {
		LogUtil.startLog(HuixiaofeiController.class.toString(), HuixiaofeiDefine.SHENHE);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", false);
		String ids = request.getParameter("ids");
		String updatestatus = request.getParameter("updatestatus");
		if (StringUtils.isEmpty(ids) || ids.equals("null")) {
			resultMap.put("msg", "请选择正确的审核对象");
			return resultMap;
		}
		if (StringUtils.isEmpty(updatestatus) || updatestatus.equals("null")) {
			resultMap.put("msg", "请选择正确的审核结果");
			return resultMap;
		}
		{
			List<Integer> idList = new ArrayList<Integer>();
			for (String id : ids.split(",")) {
				idList.add(Integer.parseInt(id));
			}
			if (idList.size() != 0) {
				huixiaofeiService.shenhe(idList, Integer.parseInt(updatestatus));
			} else {
				resultMap.put("msg", "请选择正确的审核对象");
				return resultMap;
			}
		}
		LogUtil.endLog(HuixiaofeiController.class.toString(), HuixiaofeiDefine.SHENHE);
		resultMap.put("success", true);
		return resultMap;
	}

	/**
	 * 数据导出
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HuixiaofeiDefine.EXPORT_ACTION)
	@RequiresPermissions(HuixiaofeiDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, HuixiaofeiBean form)
			throws Exception {
		LogUtil.startLog(HuixiaofeiController.class.toString(), HuixiaofeiDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "汇消费用户详情数据";

		String paramid = request.getParameter("id");
		if (StringUtils.isNotEmpty(paramid)) {
			Consume consume = null;
			// 第一步:查询consume
			{
				Integer id = Integer.valueOf(paramid);
				consume = huixiaofeiService.getRecordByID(id);
			}
			// 第二步:查询consume下的consumeList
			{
				// 组装查询条件
				if (consume != null) {
					HuixiaofeiListBean bean = new HuixiaofeiListBean();
					bean.setConsumeId(consume.getConsumeId());
					bean.setStatus(form.getStatus());
					bean.setInsertTime(consume.getInsertTime());
					List<ConsumeList> recordList = huixiaofeiService.getConsumeListByCondition(bean, -1, -1);
					String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
							+ CustomConstants.EXCEL_EXT;

					String[] titles = new String[] { "贷款金额", "贷款期限","周期类型", "贷款日期", "姓名", "身份证号", "手机号码", "座机号码", "银行卡号",
							"QQ号码", "邮箱地址", "学历", "婚姻", "户籍地址", "家庭地址", "工作单位", "单位地址", "通讯地址", "职位", "公司行业",
							"用户承诺3个月内是否在它平台申请借款", "身份证与姓名是否核实", "第一联系人姓名", "第一联系人手机号码", "第一联系人家庭地址", "第一联系人工作单位",
							"第一联系人单位地址", "第二联系人姓名", "第二联系人手机号码", "第二联系人家庭地址", "第二联系人工作单位", "第二联系人单位地址", "第三联系人姓名",
							"第三联系人手机号码", "第三联系人家庭地址", "第三联系人工作单位", "第三联系人单位地址" };
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
								ConsumeList record = recordList.get(i);

								// 创建相应的单元格
								Cell cell = row.createCell(celLength);
								// 贷款金额
								if (celLength == 0) {
									cell.setCellValue(record.getCreditAmount().doubleValue());
								}
								// 贷款期限
								else if (celLength == 1) {
									cell.setCellValue(record.getInstalmentNum());
								}
								// 周期类型
								else if (celLength == 2) {
									cell.setCellValue("月");
								}
								// 贷款日期
								else if (celLength == 3) {
									cell.setCellValue(record.getLoanDate());
								}
								// 姓名
								else if (celLength == 4) {
									cell.setCellValue(record.getPersonName());
								}
								// 身份证号
								else if (celLength == 5) {
									cell.setCellValue(record.getIdent());
								}
								// 手机号码
								else if (celLength == 6) {
									cell.setCellValue(record.getMobile());
								}
								// 座机号码
								else if (celLength == 7) {
									cell.setCellValue("");
								}
								// 银行卡号
								else if (celLength == 8) {
									cell.setCellValue(record.getAccountNo());
								}
								// QQ号码
								else if (celLength == 9) {
									cell.setCellValue("");
								}
								// 邮箱地址
								else if (celLength == 10) {
									cell.setCellValue("");
								}
								// 学历
								else if (celLength == 11) {
									cell.setCellValue("");
								}
								// 婚姻
								else if (celLength == 12) {
									cell.setCellValue("");
								}
								// 户籍地址
								else if (celLength == 13) {
									cell.setCellValue(record.getAddress());
								}
								// 家庭地址
								else if (celLength == 14) {
									cell.setCellValue(record.getAddress());
								}
								// 工作单位
								else if (celLength == 15) {
									cell.setCellValue(record.getCompany());
								}
								// 以下都是空
							}
						}
					}
					// 导出
					ExportExcel.writeExcelFile(response, workbook, titles, fileName);
				}
			}
		}
		LogUtil.endLog(HuixiaofeiController.class.toString(), HuixiaofeiDefine.EXPORT_ACTION);
	}

	@ResponseBody
	@RequestMapping(value = HuixiaofeiDefine.CHULI)
	@RequiresPermissions(HuixiaofeiDefine.PERMISSIONS_AUDIT)
	public Map<String, Object> chuliAction(HttpServletRequest request) throws ParseException {
		LogUtil.startLog(HuixiaofeiController.class.toString(), HuixiaofeiDefine.CHULI);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", false);
		String id = request.getParameter("id");
		if (StringUtils.isEmpty(id) || id.equals("null")) {
			resultMap.put("msg", "请选择正确的处理对象");
			return resultMap;
		}
		{
			try {
				String returnMsg = huixiaofeiService.chuli(Integer.parseInt(id));
				resultMap.put("msg", returnMsg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				resultMap.put("msg", e);
				LogUtil.errorLog(HuixiaofeiController.class.toString(), HuixiaofeiDefine.DOWNLOADDATA, e);
				return resultMap;
			}
		}
		LogUtil.endLog(HuixiaofeiController.class.toString(), HuixiaofeiDefine.CHULI);
		resultMap.put("success", true);
		return resultMap;
	}
}
