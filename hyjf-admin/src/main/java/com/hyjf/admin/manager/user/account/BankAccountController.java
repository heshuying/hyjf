package com.hyjf.admin.manager.user.account;

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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.admin.AdminAccountListCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = BankAccountDefine.REQUEST_MAPPING)
public class BankAccountController extends BaseController {

	@Autowired
	private AccountService accountService;

	
	/**************************************银行存管    pcc  *******************************************/
	
	/**
     * 权限维护画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BankAccountDefine.BANK_ACCOUNT_LIST_ACTION)
    @RequiresPermissions(BankAccountDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
            @ModelAttribute(BankAccountDefine.ACCOUNT_LIST_FORM) AccountListCustomizeBean form) {
        LogUtil.startLog(BankAccountDefine.THIS_CLASS, BankAccountDefine.BANK_ACCOUNT_LIST_ACTION);
        ModelAndView modelAndView = new ModelAndView(BankAccountDefine.BANK_ACCOUNT_LIST_PATH);
        // 创建分页
        this.bankCreatePage(request, modelAndView, form);
        LogUtil.endLog(BankAccountDefine.THIS_CLASS, BankAccountDefine.BANK_ACCOUNT_LIST_ACTION);
        return modelAndView;
    }

    /**
     * 创建权限维护分页机能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    private void bankCreatePage(HttpServletRequest request, ModelAndView modelAndView, AccountListCustomizeBean form) {

        // 开户状态
        List<ParamName> accountStatus = this.accountService.getParamNameList("ACCOUNT_STATUS");
        modelAndView.addObject("accountStatus", accountStatus);
        // 开户平台
        List<ParamName> openAccPlat = this.accountService.getParamNameList("CLIENT");
        modelAndView.addObject("openAccPlat", openAccPlat);
        Map<String, Object> accountUser = new HashMap<String, Object>();
        accountUser.put("customerAccount", form.getCustomerAccount());
        accountUser.put("mobile", form.getMobile());
        accountUser.put("openAccountPlat", form.getOpenAccountPlat());
        accountUser.put("userName", form.getUserName());
        accountUser.put("idCard", form.getIdCard());
        accountUser.put("realName", form.getRealName());
        accountUser.put("openTimeStart", StringUtils.isNotBlank(form.getOpenTimeStart())?form.getOpenTimeStart():null);
        accountUser.put("openTimeEnd", StringUtils.isNotBlank(form.getOpenTimeEnd())?form.getOpenTimeEnd():null);

        int recordTotal = this.accountService.countBankRecordTotal(accountUser);
        if (recordTotal > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
            List<AdminAccountListCustomize> recordList = this.accountService.getBankRecordList(accountUser,
                    paginator.getOffset(), paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(recordList);
            modelAndView.addObject(BankAccountDefine.ACCOUNT_LIST_FORM, form);
        }
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
    @RequestMapping(BankAccountDefine.EXPORT_BANK_ACCOUNT_ACTION)
    @RequiresPermissions(BankAccountDefine.PERMISSIONS_EXPORT)
    public void exportBankExcel(@ModelAttribute AccountListCustomizeBean form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        LogUtil.startLog(BankAccountDefine.THIS_CLASS, BankAccountDefine.EXPORT_BANK_ACCOUNT_ACTION);
        // 表格sheet名称
        String sheetName = "江西银行开户记录";
        // 文件名称
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
                + CustomConstants.EXCEL_EXT;

        Map<String, Object> accountUser = new HashMap<String, Object>();
        accountUser.put("account", form.getAccount());
        accountUser.put("openAccountPlat", form.getOpenAccountPlat());
        accountUser.put("mobile", form.getMobile());
        accountUser.put("userName", form.getUserName());
        accountUser.put("idCard", form.getIdCard());
        accountUser.put("realName", form.getRealName());
        accountUser.put("openTimeStart", form.getOpenTimeStart());
        accountUser.put("openTimeEnd", form.getOpenTimeEnd());

        // 需要输出的结果列表
        List<AdminAccountListCustomize> recordList = this.accountService.getBankRecordList(accountUser, -1, -1);
        
        String[] titles = new String[] { "序号", "用户名","当前手机号", "姓名", "身份证号码","银行卡号", "银行电子账户", "开户平台", "开户时间" };
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
                    AdminAccountListCustomize user = recordList.get(i);
                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                  //序号、用户名、当前手机号、姓名、身份证号、银行卡号、银行电子账户、开户平台、开户时间
                    if (celLength == 0) {// 序号
                        cell.setCellValue(i + 1);
                    } else if (celLength == 1) {// 用户名
                        cell.setCellValue(user.getUserName());
                    } else if (celLength == 2) {// 用户名
                        cell.setCellValue(user.getMobile());
                    } else if (celLength == 3) {// 真实姓名
                        cell.setCellValue(user.getRealName());
                    } else if (celLength == 4) {// 身份证号码
                        cell.setCellValue(user.getIdCard());
                    } else if (celLength == 5) {// 开户状态
                        cell.setCellValue(user.getAccount());
                    } else if (celLength == 6) {// 汇付账号
                        cell.setCellValue(user.getCustomerAccount());
                    } else if (celLength == 7) {// 开户平台
                        cell.setCellValue(user.getOpenAccountPlat());
                    } else if (celLength == 8) {// 开户时间
                        cell.setCellValue(user.getOpenTime());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
        LogUtil.endLog(BankAccountDefine.THIS_CLASS, BankAccountDefine.EXPORT_BANK_ACCOUNT_ACTION);
    }
    
    
	
}
