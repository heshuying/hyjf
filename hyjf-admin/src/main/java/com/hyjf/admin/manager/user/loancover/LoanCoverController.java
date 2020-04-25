package com.hyjf.admin.manager.user.loancover;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.maintenance.config.ConfigController;
import com.hyjf.admin.maintenance.config.ConfigDefine;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonService;
import com.hyjf.admin.manager.user.msp.util.ParamUtil;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.http.HttpClientUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.CertificateAuthority;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecord;
import com.hyjf.mybatis.model.auto.LoanSubjectCertificateAuthority;
import com.hyjf.mybatis.model.auto.MspAbnormalCredit;
import com.hyjf.mybatis.model.auto.MspAbnormalCreditDetail;
import com.hyjf.mybatis.model.auto.MspApply;
import com.hyjf.mybatis.model.auto.MspApplyDetails;
import com.hyjf.mybatis.model.auto.MspConfigure;
import com.hyjf.mybatis.model.auto.MspQueryDetail;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.fadada.bean.DzqzCallBean;
import com.hyjf.pay.lib.fadada.util.DzqzCallUtil;
import com.hyjf.pay.lib.fadada.util.DzqzConstant;

/**
 * 
 * 
 * @author
 *
 */
@Controller
@RequestMapping(value = LoanCoverDefine.REQUEST_MAPPING)
public class LoanCoverController extends BaseController {

	Logger _log = LoggerFactory.getLogger("LoanCoveController");

	@Autowired
	private LoanCoverService loanCoverService;

	/**
	 * 列表维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(LoanCoverDefine.INIT)
	@RequiresPermissions(LoanCoverDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(LoanCoverDefine.CONFIGBANK_FORM) LoanCoverBean form) {
		LogUtil.startLog(LoanCoverController.class.toString(), LoanCoverDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(LoanCoverDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(LoanCoverController.class.toString(), LoanCoverDefine.INIT);
		return modelAndView;
	}

	/**
	 * 文章列表维护分页机能 页面初始化
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 * @throws ParseException
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, LoanCoverBean form) {
		LoanSubjectCertificateAuthority ma = new LoanSubjectCertificateAuthority();
		ma.setName(form.getName());
		ma.setIdNo(form.getIdNo());
		ma.setMobile(form.getMobile());
		ma.setCode(form.getCode());
		ma.setCustomerId(form.getCustomerId());
		ma.setIdType(form.getIdType());
		int start = 0;
		int end = 0;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (form.getStartCreate() != null) {
			Date date;
			try {
				date = simpleDateFormat.parse(form.getStartCreate());

				start = (int) (date.getTime() / 1000);
			} catch (ParseException e) {
				_log.info("借款盖章用户返回日期格式化异常：" + e.getMessage());
			}

		}
		if (form.getEndCreate() != null) {
			Date date;
			try {
				date = simpleDateFormat.parse(form.getEndCreate());
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				cal.add(Calendar.DATE, 1);

				end = (int) ((cal.getTime()).getTime() / 1000);
			} catch (ParseException e) {
				_log.info("借款盖章用户返回日期格式化异常：" + e.getMessage());
			}

		}
		List<LoanSubjectCertificateAuthority> recordList = this.loanCoverService.getRecordList(ma, -1, -1, start, end);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = this.loanCoverService.getRecordList(ma, paginator.getOffset(), paginator.getLimit(), start,
					end);
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(LoanCoverDefine.CONFIGBANK_FORM, form);
			String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
			modelAndView.addObject("fileDomainUrl", fileDomainUrl);
		}
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(LoanCoverDefine.INFO_ACTION)
	@RequiresPermissions(value = { LoanCoverDefine.PERMISSIONS_INFO, LoanCoverDefine.PERMISSIONS_ADD,
			LoanCoverDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(LoanCoverDefine.CONFIGBANK_FORM) LoanCoverBean form) {
		LogUtil.startLog(LoanCoverController.class.toString(), LoanCoverDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(LoanCoverDefine.INFO_PATH);

		modelAndView.addObject(LoanCoverDefine.CONFIGBANK_FORM, form);

		LogUtil.endLog(LoanCoverController.class.toString(), LoanCoverDefine.INIT);
		return modelAndView;
	}

	/**
	 * 添加用户
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = LoanCoverDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(LoanCoverDefine.PERMISSIONS_ADD)
	@ResponseBody
	public JSONObject insertAction(HttpServletRequest request, LoanSubjectCertificateAuthority form) {
		LogUtil.startLog(LoanCoverController.class.toString(), LoanCoverDefine.INSERT_ACTION);
		JSONObject result = new JSONObject();
		form.setCreateUserId(Integer.valueOf(ShiroUtil.getLoginUserId()));
		form.setCreateUserName(ShiroUtil.getLoginUsername());
		form.setRemark("0");
		// 调用校验
		if (!validatorParm(form)) {
			// 失败返回
			result.put(LoanCoverDefine.SUCCESS, false);
			result.put(LoanCoverDefine.MSG, "参数错误");
			return result;
		}
		if(!loanCoverService.isExistsRecord(form.getIdNo())) {
			// 失败返回
			result.put(LoanCoverDefine.SUCCESS, false);
			result.put(LoanCoverDefine.MSG, "该输入统一社会信用代码或身份证已存在!");
			return result;
		}
		form.setCreateTime(GetDate.getNowTime10());
		// 数据插入
		this.loanCoverService.insertRecord(form);
		result.put(LoanCoverDefine.SUCCESS, true);
		return result;
	}

	private boolean validatorParm(LoanSubjectCertificateAuthority form) {
		if (Validator.isNull(form.getName())) {
			return false;
		}
		if (form.getName().length() > 50) {
			return false;
		}
		return true;
	}

	/**
	 * 修改活动维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = LoanCoverDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(LoanCoverDefine.PERMISSIONS_MODIFY)
	@ResponseBody
	public JSONObject updateAction(HttpServletRequest request, LoanSubjectCertificateAuthority form) {
		LogUtil.startLog(LoanCoverController.class.toString(), LoanCoverDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(LoanCoverDefine.INFO_PATH);
		JSONObject result = new JSONObject();
		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			result.put(LoanCoverDefine.SUCCESS, false);
			result.put(LoanCoverDefine.MSG, "参数错误");
			return result;
		}
		// // 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
			result.put(LoanCoverDefine.SUCCESS, false);
			result.put(LoanCoverDefine.MSG, "参数错误");
			return result;
		}
		// form.setLogo("1");

		LoanSubjectCertificateAuthority ma = this.loanCoverService.getRecord(form.getId());
		if (ma.getStatus().equals("success")) {
			if (!ma.getMobile().equals(form.getMobile()) || !ma.getEmail().equals(form.getEmail())) {
				DzqzCallBean bean = new DzqzCallBean();
				bean.setUserId(0);
				bean.setTxCode("infochange");
				bean.setApp_id(DzqzConstant.HYJF_FDD_APP_ID);
				bean.setV(DzqzConstant.HYJF_FDD_VERSION);
				bean.setTimestamp(GetDate.getDate("yyyyMMddHHmmss"));
				bean.setCustomer_id(ma.getCustomerId());// 客户编号
				bean.setEmail(form.getEmail());// 电子邮箱
				bean.setMobile(form.getMobile());// 手机号

				DzqzCallBean resultt = DzqzCallUtil.callApiBg(bean);
				if (resultt != null && "success".equals(resultt.getResult())) {
					// 更新
					this.loanCoverService.updateRecord(form);
					result.put(LoanCoverDefine.SUCCESS, true);
					return result;
				} else {
					result.put(LoanCoverDefine.SUCCESS, false);
					result.put(LoanCoverDefine.MSG, "更新失败");
					return result;
				}
			}
		}

		// 更新
		this.loanCoverService.updateRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(LoanCoverDefine.SUCCESS, LoanCoverDefine.SUCCESS);
		LogUtil.endLog(LoanCoverController.class.toString(), LoanCoverDefine.UPDATE_ACTION);
		result.put(LoanCoverDefine.SUCCESS, true);
		return result;
	}

	/**
	 * 调用校验表单方法
	 * 
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView validatorFieldCheck(ModelAndView modelAndView, LoanSubjectCertificateAuthority form) {
		// 字段校验(非空判断和长度判断)
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "name", form.getName())) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "name", form.getName(), 50, true)) {
			return modelAndView;
		}
		// if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "code",
		// form.getCode())) {
		// return modelAndView;
		// }
		// if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "code",
		// form.getCode(), 10, true)) {
		// return modelAndView;
		// }
		return null;
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(LoanCoverDefine.EXPORT_ACTION)
	@RequiresPermissions(LoanCoverDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute(LoanCoverDefine.CONFIGBANK_FORM) LoanCoverBean form) throws Exception {
		LogUtil.startLog(LoanCoverDefine.class.toString(), LoanCoverDefine.EXPORT_ACTION);

		LoanSubjectCertificateAuthority ma = new LoanSubjectCertificateAuthority();
		int start = 0;
		int end = 0;
		ma.setName(form.getName());
		ma.setIdNo(form.getIdNo());
		ma.setMobile(form.getMobile());
		ma.setCode(form.getCode());
		ma.setCustomerId(form.getCustomerId());
		ma.setIdType(form.getIdType());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (form.getStartCreate() != null) {
			Date date;
			try {
				date = simpleDateFormat.parse(form.getStartCreate());
				start = (int) (date.getTime() / 1000);
			} catch (ParseException e) {
				_log.info("借款返回日期格式化异常：" + e.getMessage());
			}

		}
		if (form.getEndCreate() != null) {
			Date date;
			try {
				date = simpleDateFormat.parse(form.getEndCreate());
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				cal.add(Calendar.DATE, 1);

				end = (int) ((cal.getTime()).getTime() / 1000);
			} catch (ParseException e) {
				_log.info("借款返回日期格式化异常：" + e.getMessage());
			}

		}
		List<LoanSubjectCertificateAuthority> recordList = this.loanCoverService.getRecordList(ma, -1, -1, start, end);

		// 表格sheet名称
		String sheetName = "借款盖章用户查询";

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;
		String[] titles = new String[] { "序号", "手机号", "名称", "证件号", "用户类型", "邮箱", "客户编号", "状态", "状态码", "添加时间", "申请时间" };
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();

		// 生成一个表格
		HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
					LoanSubjectCertificateAuthority pInfo = recordList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					} else if (celLength == 1) {
						cell.setCellValue(pInfo.getMobile());
					} else if (celLength == 2) {
						cell.setCellValue(pInfo.getName());
					} else if (celLength == 3) {
						cell.setCellValue(pInfo.getIdNo());
					} else if (celLength == 4) {
						if(pInfo.getIdType()==0) {
							cell.setCellValue("个人");
						}else {
							cell.setCellValue("企业");
						}
					} else if (celLength == 5) {
						cell.setCellValue(pInfo.getEmail());
					} else if (celLength == 6) {
						cell.setCellValue(pInfo.getCustomerId());
					} else if (celLength == 7) {
						if(pInfo.getStatus().equals("success")) {
							cell.setCellValue("成功");
						}else if(pInfo.getStatus().equals("error")) {
							cell.setCellValue("失败");
						}else {
							cell.setCellValue(pInfo.getStatus());
						}
						
					} else if (celLength == 8) {
						cell.setCellValue(pInfo.getCode());
					} else if (celLength == 9) {
						Long time1 = new Long(pInfo.getCreateTime());
						String d = format.format(time1 * 1000);
						cell.setCellValue(d);
					} else if (celLength == 10) {
						if(pInfo.getUpdateTime()!=null) {
							Long time1 = new Long(pInfo.getUpdateTime());
							String d = format.format(time1 * 1000);
							cell.setCellValue(d);
						}

					}

				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(LoanCoverController.class.toString(), LoanCoverDefine.EXPORT_ACTION);
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(LoanCoverDefine.APPLY_ACTION)
	@RequiresPermissions(value = { LoanCoverDefine.PERMISSIONS_INFO, LoanCoverDefine.PERMISSIONS_ADD,
			LoanCoverDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView applyInfo(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(LoanCoverDefine.CONFIGBANK_FORM) LoanCoverBean form) {
		LogUtil.startLog(LoanCoverController.class.toString(), LoanCoverDefine.APPLY_ACTION);
		ModelAndView modelAndView = new ModelAndView(LoanCoverDefine.INFO_PATH);

		LoanSubjectCertificateAuthority ma = this.loanCoverService.getRecord(Integer.valueOf(form.getIds()));

		modelAndView.addObject(LoanCoverDefine.CONFIGBANK_FORM, ma);

		LogUtil.endLog(LoanCoverController.class.toString(), LoanCoverDefine.INIT);
		return modelAndView;
	}

	// 认证
	@RequestMapping(value = LoanCoverDefine.SHARE_USER_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(LoanCoverDefine.PERMISSIONS_ADD)
	@ResponseBody
	public JSONObject shareUser(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(LoanCoverController.class.toString(), LoanCoverDefine.SHARE_USER_ACTION);
		JSONObject result = new JSONObject();
		
		LoanSubjectCertificateAuthority ma = this.loanCoverService
				.getRecord(Integer.valueOf(request.getParameter("id")));
		String custId=loanCoverService.isExistsAuthority(ma.getIdNo(),ma.getName());
		if(custId!="") {
			
			ma.setCode("1000");
			ma.setCustomerId(custId);
			ma.setStatus("success");
			ma.setUpdateTime(GetDate.getNowTime10());
			loanCoverService.updateRecord(ma);
			result.put(LoanCoverDefine.SUCCESS, true);
			return result;
		}
		// 法大大开始
		DzqzCallBean bean = new DzqzCallBean();
		bean.setUserId(0);
		bean.setLogordid("0");
		bean.setApp_id(DzqzConstant.HYJF_FDD_APP_ID);
		bean.setV(DzqzConstant.HYJF_FDD_VERSION);
		bean.setTimestamp(GetDate.getDate("yyyyMMddHHmmss"));
		bean.setCustomer_name(ma.getName());// 客户姓名
		bean.setEmail(ma.getEmail());// 电子邮箱
		bean.setIdCard(ma.getIdNo());// 组织机构代码
		bean.setMobile(ma.getMobile());// 手机号

		if (ma.getIdType() == 0) {
			bean.setIdent_type("0");// 证件类型
			bean.setTxCode("syncPerson_auto");
		} else {
			bean.setTxCode("syncCompany_auto");
		}
		// 调用接口
		DzqzCallBean resultt = DzqzCallUtil.callApiBg(bean);
		_log.info("法大大返回报文" + resultt.toString());
		if (resultt != null) {
			_log.info("CA认证成功:用户ID:[" + ma.getName() + "].");
			if ("success".equals(resultt.getResult())) {
				ma.setCode(resultt.getCode());
				ma.setCustomerId(resultt.getCustomer_id());
				ma.setStatus("success");
				ma.setUpdateTime(GetDate.getNowTime10());
				loanCoverService.updateRecord(ma);
				result.put(LoanCoverDefine.SUCCESS, true);
				return result;
			} else {
				ma.setCode(resultt.getCode());
				ma.setStatus("error");
				ma.setUpdateTime(GetDate.getNowTime10());
				loanCoverService.updateRecord(ma);
				result.put(LoanCoverDefine.SUCCESS, false);
				result.put(LoanCoverDefine.MSG,resultt.getMsg());
				return result;
			}

		}
		result.put(LoanCoverDefine.SUCCESS, false);
		result.put(LoanCoverDefine.MSG, "请求法大大失败");
		return result;
	}


}
