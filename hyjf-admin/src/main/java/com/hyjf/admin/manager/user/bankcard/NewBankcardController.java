package com.hyjf.admin.manager.user.bankcard;

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
import com.hyjf.mybatis.model.customize.admin.AdminBankcardListCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = NewBankcardDefine.REQUEST_MAPPING)
public class NewBankcardController extends BaseController {

	@Autowired
	private BankcardService bankcardService;

	
	
	/*******************************************银行存管 银行卡管理  pcc******************************************************/
	/**
     * 权限维护画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(NewBankcardDefine.NEW_BANKCARD_LIST_ACTION)
    @RequiresPermissions(NewBankcardDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(NewBankcardDefine.BANKCARD_LIST_FORM) BankcardListCustomizeBean form) {
        LogUtil.startLog(NewBankcardDefine.THIS_CLASS, NewBankcardDefine.NEW_BANKCARD_LIST_ACTION);
        ModelAndView modelAndView = new ModelAndView(NewBankcardDefine.NEW_BANK_CARD_LIST_PATH);
        // 创建分页
        this.createPageNew(request, modelAndView, form);
        LogUtil.endLog(NewBankcardDefine.THIS_CLASS, NewBankcardDefine.NEW_BANKCARD_LIST_ACTION);
        return modelAndView;
    }

    /**
     * 创建权限维护分页机能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPageNew(HttpServletRequest request, ModelAndView modelAndView, BankcardListCustomizeBean form) {
        // 封装查询条件
        Map<String, Object> bankCardUser = new HashMap<String, Object>();
        bankCardUser.put("userName", form.getUserName());
        bankCardUser.put("mobile", form.getMobile());
        bankCardUser.put("realName", form.getRealName());
        bankCardUser.put("addTimeStart", StringUtils.isNotBlank(form.getAddTimeStart())?form.getAddTimeStart():null);
        bankCardUser.put("addTimeEnd", StringUtils.isNotBlank(form.getAddTimeEnd())?form.getAddTimeEnd():null);
        int recordTotal = this.bankcardService.countRecordTotalNew(bankCardUser);
        if (recordTotal > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
            List<AdminBankcardListCustomize> recordList = this.bankcardService.getRecordListNew(bankCardUser, paginator.getOffset(), paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(recordList);
            modelAndView.addObject(NewBankcardDefine.BANKCARD_LIST_FORM, form);
        }
    }
    
    
    /**
     * 
     * 导出方法
     * @author pcc
     * @param form
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(NewBankcardDefine.EXPORT_NEW_BANKCARD_ACTION)
    @RequiresPermissions(NewBankcardDefine.PERMISSIONS_EXPORT)
    public void exportExcelNew(@ModelAttribute BankcardListCustomizeBean form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        LogUtil.startLog(NewBankcardDefine.THIS_CLASS, NewBankcardDefine.EXPORT_NEW_BANKCARD_ACTION);
        // 表格sheet名称
        String sheetName = "银行卡管理";
        // 文件名称
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        // 需要输出的结果列表

        // 封装查询条件
        Map<String, Object> bankCardUser = new HashMap<String, Object>();
        bankCardUser.put("userName", form.getUserName());
        bankCardUser.put("mobile", form.getMobile());
        bankCardUser.put("realName", form.getRealName());
        bankCardUser.put("addTimeStart", StringUtils.isNotBlank(form.getAddTimeStart())?form.getAddTimeStart():null);
        bankCardUser.put("addTimeEnd", StringUtils.isNotBlank(form.getAddTimeEnd())?form.getAddTimeEnd():null);

        List<AdminBankcardListCustomize> recordList = this.bankcardService.getRecordListNew(bankCardUser, -1, -1);
        //序号、用户名、当前手机号、姓名、身份证号、银行卡号、绑卡时间
        String[] titles = new String[] { "序号", "用户名", "当前手机号", "姓名", "身份证号", "银行卡号", "绑卡时间" };
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
                    AdminBankcardListCustomize user = recordList.get(i);
                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    if (celLength == 0) {// 序号
                        cell.setCellValue(i + 1);
                    } else if (celLength == 1) {// 用户名
                        cell.setCellValue(user.getUserName());
                    } else if (celLength == 2) {// 当前手机号
                        cell.setCellValue(user.getMobile());
                    } else if (celLength == 3) {// 姓名
                        cell.setCellValue(user.getRealName());
                    } else if (celLength == 4) {// 身份证号
                        cell.setCellValue(user.getIdcard());
                    } else if (celLength == 5) {// 银行卡号
                        cell.setCellValue(user.getAccount());
                    } else if (celLength == 6) {// 绑卡时间
                        cell.setCellValue(user.getAddTime());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
        LogUtil.endLog(NewBankcardDefine.THIS_CLASS, NewBankcardDefine.EXPORT_NEW_BANKCARD_ACTION);
    }
}
