package com.hyjf.admin.manager.user.userauth;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.hyjf.admin.manager.user.account.AccountService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.admin.AdminUserAuthLogListCustomize;

/**
 * @author wangqi
 * @version V1.0  
 * @package com.hyjf.admin.manager.user.userauth.UserauthLogController
 * @date 2017/8/14
 */
@Controller
@RequestMapping(value = UserauthLogDefine.REQUEST_MAPPING)
public class UserauthLogController extends BaseController {

    @Autowired
    private UserauthService userauthService;
    @Autowired
    private AccountService accountService;

    /**
     * 权限维护画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(UserauthLogDefine.USERAUTH_LIST_ACTION)
    @RequiresPermissions(UserauthLogDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(UserauthLogDefine.USERAUTH_LIST_FORM) UserauthLogListCustomizeBean form) {
        LogUtil.startLog(UserauthLogDefine.THIS_CLASS, UserauthLogDefine.USERAUTH_LIST_ACTION);
        ModelAndView modelAndView = new ModelAndView(UserauthLogDefine.USER_AUTH_LIST_PATH);
        // 创建分页8
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(UserauthLogDefine.THIS_CLASS, UserauthLogDefine.USERAUTH_LIST_ACTION);
        return modelAndView;
    }

    /**
     * 创建权限维护分页机能
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, UserauthLogListCustomizeBean form) {
        // 开户平台
        List<ParamName> openAccPlat = this.accountService.getParamNameList("CLIENT");
        modelAndView.addObject("openAccPlat", openAccPlat);
        // 类型
        List<ParamName> autoInverType = this.accountService.getParamNameList("AUTO_INVER_TYPE");
        modelAndView.addObject("autoInverType", autoInverType);
        // 封装查询条件
        Map<String, Object> authUser = new HashMap<String, Object>();
        authUser.put("userName", form.getUserName());
        authUser.put("authType", form.getAuthType());
        authUser.put("orderId", form.getOrderId());
        authUser.put("addTimeStart", form.getAddTimeStart());
        authUser.put("addTimeEnd", form.getAddTimeEnd());
        authUser.put("orderStatus", form.getOrderStatus());

        int recordTotal = this.userauthService.countRecordTotalLog(authUser);
        if (recordTotal > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
            List<AdminUserAuthLogListCustomize> recordList = this.userauthService.getRecordListLog(authUser, paginator.getOffset(), paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(recordList);
            modelAndView.addObject(UserauthLogDefine.USERAUTH_LIST_FORM, form);
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
    @RequestMapping(UserauthLogDefine.EXPORT_USERAUTH_ACTION)
    @RequiresPermissions(UserauthLogDefine.PERMISSIONS_EXPORT)
    public void exportExcel(@ModelAttribute UserauthLogListCustomizeBean form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        LogUtil.startLog(UserauthLogDefine.THIS_CLASS, UserauthLogDefine.EXPORT_USERAUTH_ACTION);
        // 表格sheet名称
        String sheetName = "授权记录";
        // 文件名称
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        //解决IE浏览器导出列表中文乱码问题
        String userAgent = request.getHeader("user-agent").toLowerCase();
        if (userAgent.contains("msie") || userAgent.contains("like gecko")) {
            // win10 ie edge 浏览器 和其他系统的ie
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } else {
            fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
        }
        // 封装查询条件
        Map<String, Object> authUser = new HashMap<String, Object>();
        authUser.put("userName", form.getUserName());
        authUser.put("authType", form.getAuthType());
        authUser.put("orderId", form.getOrderId());
        authUser.put("addTimeStart", form.getAddTimeStart());
        authUser.put("addTimeEnd", form.getAddTimeEnd());

        List<AdminUserAuthLogListCustomize> recordList = this.userauthService.getRecordListLog(authUser, -1, -1);
        String[] titles = new String[]{"序号", "订单号", "类型", "用户名", "操作平台", "订单状态", "授权时间"};
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
                    AdminUserAuthLogListCustomize user = recordList.get(i);
                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    if (celLength == 0) {// 序号
                        cell.setCellValue(i + 1);
                    } else if (celLength == 1) {// 订单号
                        cell.setCellValue(user.getOrderId());
                    } else if (celLength == 2) {// 类型
                        cell.setCellValue(user.getAuthType());
                    } else if (celLength == 3) {// 用户名
                        cell.setCellValue(user.getUserName());
                    } else if (celLength == 4) {// 操作平台
                        cell.setCellValue(user.getOperateEsb());
                    } else if (celLength == 5) {// 订单状态
                        cell.setCellValue(user.getOrderStatus());
                    } else if (celLength == 6) {// 授权时间
                        cell.setCellValue(user.getCreditTime());
                    }

                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
        LogUtil.endLog(UserauthLogDefine.THIS_CLASS, UserauthLogDefine.EXPORT_USERAUTH_ACTION);
    }

}
