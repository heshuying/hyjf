package com.hyjf.admin.exception.assetexception;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.exception.manualreverseexception.ManualReverseDefine;
import com.hyjf.admin.manager.config.bailconfig.BailConfigDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.*;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowDelete;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.customize.AdminSystem;
import com.hyjf.mybatis.model.customize.admin.AssetExceptionCustomize;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @author PC-LIUSHOUYI
 * @version AssetExceptionCustomize, v0.1 2018/8/2 9:39
 */
@Controller
@RequestMapping(value = AssetExceptionDefine.REQUEST_MAPPING)
public class AssetExceptionController extends BaseController {

    @Autowired
    private AssetExceptionService assetExceptionService;

    /**
     * 画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AssetExceptionDefine.INIT)
    @RequiresPermissions(AssetExceptionDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, AssetExceptionBean form) {
        ModelAndView modelAndView = new ModelAndView(AssetExceptionDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        return modelAndView;
    }

    /**
     * 画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AssetExceptionDefine.SEARCH_ACTION)
    @RequiresPermissions(AssetExceptionDefine.PERMISSIONS_SEARCH)
    public ModelAndView searchAction(HttpServletRequest request, AssetExceptionBean form) {
        ModelAndView modelAndView = new ModelAndView(AssetExceptionDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        return modelAndView;
    }

    /**
     * 创建分页机能
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, AssetExceptionBean form) {

        // 资产来源
        List<HjhInstConfig> hjhInstConfigList = this.assetExceptionService.hjhInstConfigList("");
        modelAndView.addObject("hjhInstConfigList", hjhInstConfigList);

        AssetExceptionCustomize assetExceptionCustomize = new AssetExceptionCustomize();

        // 项目编号
        assetExceptionCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
        // 资产来源
        assetExceptionCustomize.setInstCodeSrch(form.getInstCodeSrch());
        // 添加时间
        assetExceptionCustomize.setTimeStartSrch(form.getTimeStartSrch());
        // 添加时间
        assetExceptionCustomize.setTimeEndSrch(form.getTimeEndSrch());

        Integer count = this.assetExceptionService.countBorrowDelete(assetExceptionCustomize);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            assetExceptionCustomize.setLimitStart(paginator.getOffset());
            assetExceptionCustomize.setLimitEnd(paginator.getLimit());
            List<AssetExceptionCustomize> recordList = this.assetExceptionService.selectBorrowDeleteList(assetExceptionCustomize);
            form.setPaginator(paginator);
            modelAndView.addObject("recordList", recordList);
        }
        modelAndView.addObject(AssetExceptionDefine.ASSET_FORM, form);
    }

    /**
     * 画面迁移(含有id更新，不含有id添加)
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AssetExceptionDefine.INFO_ACTION)
    @RequiresPermissions(value = {AssetExceptionDefine.PERMISSIONS_INFO, AssetExceptionDefine.PERMISSIONS_ADD, AssetExceptionDefine.PERMISSIONS_MODIFY}, logical = Logical.OR)
    public ModelAndView infoAction(HttpServletRequest request, @ModelAttribute(AssetExceptionDefine.ASSET_FORM) BorrowDelete form) {
        ModelAndView modelAndView = new ModelAndView(AssetExceptionDefine.INFO_PATH);
        if (null != form.getId()) {
            Integer id = form.getId();
            BorrowDelete borrowDelete = this.assetExceptionService.selectBorrowDeleteById(id);

            AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
            form.setCreateUserId(Integer.parseInt(adminSystem.getId()));
            form.setCreateTime(new Date());
            modelAndView.addObject(AssetExceptionDefine.ASSET_FORM, borrowDelete);
        }
        return modelAndView;
    }

    /**
     * 删除
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AssetExceptionDefine.DELETE_ACTION)
    @RequiresPermissions(AssetExceptionDefine.PERMISSIONS_DELETE)
    public ModelAndView deleteAction(HttpServletRequest request, AssetExceptionBean form) {
        ModelAndView modelAndView = new ModelAndView(AssetExceptionDefine.RE_LIST_PATH);
        BorrowDelete borrowDelete = this.assetExceptionService.selectBorrowDeleteById(form.getId());
        if (null != borrowDelete) {
            if(this.assetExceptionService.deleteBorrowDeleteById(form.getId())){
                Borrow borrow = this.assetExceptionService.getBorrowByNid(borrowDelete.getBorrowNid());
                // 成功更新保证金
                this.assetExceptionService.updateSubtractBail(borrow.getInstCode(),borrow.getAccount());
            }
        }
        return modelAndView;
    }

    /**
     * 添加
     *
     * @param form
     * @return
     */
    @RequestMapping(AssetExceptionDefine.INSERT_ACTION)
    @RequiresPermissions(AssetExceptionDefine.PERMISSIONS_ADD)
    public ModelAndView insertAction(AssetExceptionBean form) {
        ModelAndView modelAndView = new ModelAndView(AssetExceptionDefine.INFO_PATH);
        // 获取操作人员id
        AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
        form.setCreateUserId(Integer.parseInt(adminSystem.getId()));
        form.setUpdateTime(new Date());

        if(this.assetExceptionService.insertBorrowDelete(form)){
            Borrow borrow = this.assetExceptionService.getBorrowByNid(form.getBorrowNid());
            // 成功更新保证金
            this.assetExceptionService.updateAddBail(borrow.getInstCode(),borrow.getAccount());
        }
        modelAndView.addObject(AssetExceptionDefine.SUCCESS, AssetExceptionDefine.SUCCESS);
        return modelAndView;
    }

    /**
     * 修改
     *
     * @param form
     * @return
     */
    @RequestMapping(AssetExceptionDefine.UPDATE_ACTION)
    @RequiresPermissions(AssetExceptionDefine.PERMISSIONS_MODIFY)
    public ModelAndView updateAction(AssetExceptionBean form) {
        ModelAndView modelAndView = new ModelAndView(AssetExceptionDefine.INFO_PATH);

        AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
        form.setUpdateUserId(Integer.parseInt(adminSystem.getId()));
        form.setUpdateTime(new Date());
        this.assetExceptionService.updateBorrowDelete(form);
        modelAndView.addObject(AssetExceptionDefine.SUCCESS, AssetExceptionDefine.SUCCESS);
        return modelAndView;
    }

    /**
     * 项目编号是否存在
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AssetExceptionDefine.ISEXISTSNID_ACTION, method = RequestMethod.POST)
    public String isExistsUser(HttpServletRequest request) {
        String message = this.assetExceptionService.isExistsBorrow(request);
        return message;
    }

    /**
     * 项目异常页导出
     *
     * @param request
     * @param response
     * @param form
     */
    @RequestMapping(AssetExceptionDefine.EXPORT_ACTION)
    @RequiresPermissions(AssetExceptionDefine.PERMISSIONS_EXPORT)
    public void exportBorrowExceptionExcel(HttpServletRequest request, HttpServletResponse response,
                                           AssetExceptionBean form) {
        // 表格sheet名称
        String sheetName = "项目异常列表";

        AssetExceptionCustomize assetExceptionCustomize = new AssetExceptionCustomize();

        // 项目编号
        assetExceptionCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
        // 资产来源
        assetExceptionCustomize.setInstCodeSrch(form.getInstCodeSrch());
        // 添加时间
        assetExceptionCustomize.setTimeStartSrch(form.getTimeStartSrch());
        // 添加时间
        assetExceptionCustomize.setTimeEndSrch(form.getTimeEndSrch());
        List<AssetExceptionCustomize> recordList = this.assetExceptionService.selectBorrowDeleteList(assetExceptionCustomize);
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + ".xls";

        String[] titles = new String[]{"序号", "资产来源", "项目编号", "借款金额", "异常类型", "异常原因", "项目状态", "异常时间"};

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
                    sheet = ExportExcel
                            .createHSSFWorkbookTitle(workbook, titles, (sheetName + "_第" + sheetCount + "页"));
                    rowNum = 1;
                }

                // 新建一行
                Row row = sheet.createRow(rowNum);
                // 循环数据
                for (int celLength = 0; celLength < titles.length; celLength++) {
                    AssetExceptionCustomize record = recordList.get(i);
                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    // 资产来源
                    else if (celLength == 1) {
                        cell.setCellValue(record.getInstName());
                    }
                    // 项目编号
                    else if (celLength == 2) {
                        cell.setCellValue(record.getBorrowNid());
                    }
                    // 借款金额
                    else if (celLength == 3) {
                        cell.setCellValue(record.getAccount().toString());
                    }
                    // 异常类型
                    else if (celLength == 4) {
                        cell.setCellValue(record.getExceptionType() == 0 ?"流标":"删标");
                    }
                    // 异常原因
                    else if (celLength == 5) {
                        cell.setCellValue(record.getExceptionRemark());
                    }
                    // 项目状态
                    else if (celLength == 6) {
                        cell.setCellValue(record.getStatus());
                    }
                    // 异常时间
                    else if (celLength == 7) {
                        cell.setCellValue(record.getExceptionTime());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
    }
}
