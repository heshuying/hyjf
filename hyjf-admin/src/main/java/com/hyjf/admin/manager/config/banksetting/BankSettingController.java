package com.hyjf.admin.manager.config.banksetting;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.maintenance.config.ConfigController;
import com.hyjf.admin.maintenance.config.ConfigDefine;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonService;
import com.hyjf.admin.manager.config.bankrecharge.BankRechargeBean;
import com.hyjf.admin.manager.config.bankrecharge.BankRechargeController;
import com.hyjf.admin.manager.config.bankrecharge.BankRechargeDefine;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.BanksConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 手续费配置
 * 
 * @author
 *
 */
@Controller
@RequestMapping(value = BankSettingDefine.REQUEST_MAPPING)
public class BankSettingController extends BaseController {

	@Autowired
	private BankSettingService bankSettingService;
	@Autowired
	private BorrowCommonService borrowCommonService;

	/**
	 * 列表维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BankSettingDefine.INIT)
	@RequiresPermissions(BankSettingDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(BankSettingDefine.CONFIGBANK_FORM) BankSettingBean form) {
		LogUtil.startLog(BankSettingController.class.toString(), BankSettingDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BankSettingDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BankSettingController.class.toString(), BankSettingDefine.INIT);
		return modelAndView;
	}

	/**
	 * 文章列表维护分页机能 页面初始化
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BankSettingBean form) {
		List<BanksConfig> recordList = this.bankSettingService.getRecordList(new BanksConfig(), -1, -1);
		if (recordList != null) {
			for(BanksConfig banksConfig : recordList) {
				// 不支持快捷支付
				if (0 == banksConfig.getQuickPayment()) {
					banksConfig.setMonthCardQuota(new BigDecimal(0));
				}
			}
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			BanksConfig bc=new BanksConfig();
			bc.setBankName(form.getBankName());
			bc.setPayAllianceCode(form.getPayAllianceCode());
			recordList = this.bankSettingService.getRecordList(bc, paginator.getOffset(),
					paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(BankSettingDefine.CONFIGBANK_FORM, form);
			String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
			modelAndView.addObject("fileDomainUrl",fileDomainUrl);
		}
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BankSettingDefine.INFO_ACTION)
	@RequiresPermissions(value = { BankSettingDefine.PERMISSIONS_INFO, BankSettingDefine.PERMISSIONS_ADD,
			BankSettingDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(BankSettingDefine.CONFIGBANK_FORM) BankSettingBean form) {
		LogUtil.startLog(BankSettingController.class.toString(), BankSettingDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BankSettingDefine.INFO_PATH);

		if (StringUtils.isNotEmpty(form.getIds())) {
			Integer id = Integer.valueOf(form.getIds());
			BanksConfig record = this.bankSettingService.getRecord(id);
			modelAndView.addObject(BankSettingDefine.CONFIGBANK_FORM, record);
			String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
			modelAndView.addObject("fileDomainUrl",fileDomainUrl);
		}
		LogUtil.endLog(BankSettingController.class.toString(), BankSettingDefine.INIT);
		return modelAndView;
	}

	/**
	 * 添加活动信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = BankSettingDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(BankSettingDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, BanksConfig form) {
		LogUtil.startLog(BankSettingController.class.toString(), BankSettingDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(BankSettingDefine.INFO_PATH);

		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// form.setLogo("1");
		BanksConfig bank = new BanksConfig();
		bank.setBankName(form.getBankName());
		List<BanksConfig> banks = bankSettingService.getRecordList(bank, -1, -1);
		if (banks.size() == 0) {
		   
		    
			// 数据插入
			this.bankSettingService.insertRecord(form);
		}
		// 跳转页面用（info里面有）
		modelAndView.addObject(BankSettingDefine.SUCCESS, BankSettingDefine.SUCCESS);
		LogUtil.endLog(BankSettingController.class.toString(), BankSettingDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改活动维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = BankSettingDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(BankSettingDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, BanksConfig form) {
		LogUtil.startLog(BankSettingController.class.toString(), BankSettingDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(BankSettingDefine.INFO_PATH);

		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// // 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
			return modelAndView;
		}
		// form.setLogo("1");

		// 更新
		this.bankSettingService.updateRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(BankSettingDefine.SUCCESS, BankSettingDefine.SUCCESS);
		LogUtil.endLog(BankSettingController.class.toString(), BankSettingDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 删除配置信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ConfigDefine.DELETE_ACTION)
	@RequiresPermissions(BankSettingDefine.PERMISSIONS_DELETE)
	public ModelAndView deleteRecordAction(HttpServletRequest request, String ids) {
		LogUtil.startLog(ConfigController.class.toString(), ConfigDefine.DELETE_ACTION);

		ModelAndView modelAndView = new ModelAndView(BankSettingDefine.RE_LIST_PATH);
		// 解析json字符串
		List<Integer> recordList = JSONArray.parseArray(ids, Integer.class);
		this.bankSettingService.deleteRecord(recordList);
		LogUtil.endLog(ConfigController.class.toString(), ConfigDefine.DELETE_ACTION);
		return modelAndView;
	}

	/**
	 * 调用校验表单方法
	 * 
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView validatorFieldCheck(ModelAndView modelAndView, BanksConfig form) {
		// 字段校验(非空判断和长度判断)
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "name", form.getBankName())) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "name", form.getBankName(), 50, true)) {
			return modelAndView;
		}
//		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "code", form.getBankCode())) {
//			return modelAndView;
//		}
//		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "code", form.getBankCode(), 10, true)) {
//			return modelAndView;
//		}
		return null;
	}

	@ResponseBody
	@RequestMapping(value = BankSettingDefine.VALIDATEBEFORE)
	@RequiresPermissions(BankSettingDefine.PERMISSIONS_VIEW)
	public Map<String, Object> validateBeforeAction(HttpServletRequest request, BanksConfig form) {
		LogUtil.startLog(BankSettingController.class.toString(), BankSettingDefine.VALIDATEBEFORE);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<BanksConfig> list = bankSettingService.getRecordList(form, -1, -1);
		if (list != null && list.size() != 0) {
			if (form.getId() != null) {
				Boolean hasnot = true;
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getId() == form.getId()) {
						hasnot = false;
						break;
					}
				}
				if (hasnot) {
					resultMap.put("success", false);
					resultMap.put("msg", "银行名称或银行代码不可重复添加");
				} else {
					resultMap.put("success", true);
				}
			} else {
				resultMap.put("success", false);
				resultMap.put("msg", "银行名称或银行代码不可重复添加");
			}
		} else {
			resultMap.put("success", true);
		}
		LogUtil.endLog(BankSettingController.class.toString(), BankSettingDefine.VALIDATEBEFORE);
		return resultMap;
	}

	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = BankSettingDefine.UPLOAD_FILE, method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { BankSettingDefine.PERMISSIONS_ADD,
			BankSettingDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(BankSettingController.class.toString(), BankSettingDefine.UPLOAD_FILE);
		String files = borrowCommonService.uploadFile(request, response);
		LogUtil.endLog(BankSettingController.class.toString(), BankSettingDefine.UPLOAD_FILE);
		return files;
	}
	/**
     * 导出功能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    @RequestMapping(BankSettingDefine.EXPORT_ACTION)
    @RequiresPermissions(BankSettingDefine.PERMISSIONS_EXPORT)
    public void exportAction(HttpServletRequest request, HttpServletResponse response, BankRechargeBean form) throws Exception {
        LogUtil.startLog(BankSettingController.class.toString(), BankSettingDefine.EXPORT_ACTION);
        // 表格sheet名称
        String sheetName = "银行配置";
        BanksConfig bankRecharge = new BanksConfig();
        //列表
        List<BanksConfig> resultList  =this.bankSettingService.getRecordList(new BanksConfig(), -1, -1);
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        String[] titles = new String[] {"序号", "银行名称", "银行联行号","银行ICON","LOGO","支持快捷支付","快捷支付单笔限额","快捷充值单日限额","提现手续费"};
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
                    BanksConfig pInfo = resultList.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);

                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    else if (celLength == 1) {
                        cell.setCellValue(pInfo.getBankName());
                    }
                    else if (celLength == 2) {
                        cell.setCellValue(pInfo.getPayAllianceCode());
                    }
                    else if (celLength == 3) {
                        cell.setCellValue(pInfo.getBankIcon());
                    }
                    else if (celLength == 4) {
                        cell.setCellValue(pInfo.getBankLogo());
                    }
                    else if (celLength == 5) {
                        if(pInfo.getQuickPayment()==1){
                            cell.setCellValue("是");
                        }else{
                            cell.setCellValue("否");
                        }
                    }
                    else if (celLength == 6) {
                        cell.setCellValue(pInfo.getSingleQuota().doubleValue());
                    }
                    else if (celLength == 7) {
                        cell.setCellValue(pInfo.getSingleCardQuota().doubleValue());
                    }
                    else if (celLength == 8) {
                        cell.setCellValue(pInfo.getFeeWithdraw().doubleValue());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);

        LogUtil.endLog(BankRechargeController.class.toString(), BankRechargeDefine.EXPORT_ACTION);
    }
}
