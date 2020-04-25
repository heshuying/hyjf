package com.hyjf.admin.manager.user.certificateauthority;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.CertificateAuthority;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * CA认证记录Controller
 *
 * @author liuyang
 */
@Controller
@RequestMapping(FddCertificateAuthorityDefine.REQUEST_MAPPING)
public class FddCertificateAuthorityController extends BaseController {
    // 类名
    private static final String THIS_CLASS = FddCertificateAuthorityController.class.toString();

    Logger _log = LoggerFactory.getLogger(FddCertificateAuthorityController.class);

    @Autowired
    private FddCertificateAuthorityService fddCertificateAuthorityService;



    /**
     * 画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(FddCertificateAuthorityDefine.INIT)
    @RequiresPermissions(FddCertificateAuthorityDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, FddCertificateAuthorityBean form) {
        LogUtil.startLog(THIS_CLASS, FddCertificateAuthorityDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(FddCertificateAuthorityDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(THIS_CLASS, FddCertificateAuthorityDefine.INIT);
        return modelAndView;
    }

    /**
     * 画面检索
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(FddCertificateAuthorityDefine.SEARCH)
    @RequiresPermissions(FddCertificateAuthorityDefine.PERMISSIONS_SEARCH)
    public ModelAndView search(HttpServletRequest request, FddCertificateAuthorityBean form) {
        LogUtil.startLog(THIS_CLASS, FddCertificateAuthorityDefine.SEARCH);
        ModelAndView modelAndView = new ModelAndView(FddCertificateAuthorityDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(THIS_CLASS, FddCertificateAuthorityDefine.SEARCH);
        return modelAndView;
    }

    /**
     * 创建分页机能
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, FddCertificateAuthorityBean form) {
        Integer counts = this.fddCertificateAuthorityService.countCertificateAuthorityList(form);
        if (counts > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), counts);
            form.setPaginator(paginator);
            List<CertificateAuthority> recordList = this.fddCertificateAuthorityService.getCertificateAuthorityList(form, paginator.getOffset(), paginator.getLimit());
            modelAndView.addObject("recordList", recordList);
        }
        modelAndView.addObject(FddCertificateAuthorityDefine.FORM, form);
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
    @RequestMapping(FddCertificateAuthorityDefine.EXPORT_ACTION)
    @RequiresPermissions(FddCertificateAuthorityDefine.PERMISSIONS_EXPORT)
    public void exportExcel(@ModelAttribute FddCertificateAuthorityBean form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        LogUtil.startLog(THIS_CLASS, FddCertificateAuthorityDefine.EXPORT_ACTION);
        // 表格sheet名称
        String sheetName = "CA认证记录";
        // 文件名称
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        // 需要输出的结果列表
        List<CertificateAuthority> recordList = this.fddCertificateAuthorityService.getCertificateAuthorityList(form, -1, -1);
        String[] titles = new String[] { "序号", "用户名", "CA认证手机号", "姓名/名称","证件号码" ,"用户类型", "邮箱", "客户编号", "状态", "申请时间", "备注" };
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
                    CertificateAuthority data = recordList.get(i);
                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    if (celLength == 0) {// 序号
                        cell.setCellValue(i + 1);
                    } else if (celLength == 1) {// 用户名
                        cell.setCellValue(data.getUserName());
                    } else if (celLength == 2) {// ca认证手机号
                        cell.setCellValue(data.getMobile());
                    } else if (celLength == 3) {// 姓名/名称
                        cell.setCellValue(data.getTrueName());
                    }   else if (celLength == 4){
                        cell.setCellValue(StringUtils.isBlank(data.getIdNo())?"":data.getIdNo());
                    } else if (celLength == 5) {// 用户类型
                        cell.setCellValue(data.getIdType().compareTo(0) == 0?"个人":"企业");
                    }else if (celLength == 6) {// 邮箱
                        cell.setCellValue(data.getEmail());
                    } else if (celLength == 7) {// 客户编号
                        cell.setCellValue(data.getCustomerId());
                    } else if (celLength == 8) {// 状态
                        cell.setCellValue(data.getCode().equals("1000")?"认证成功":"未认证或认证失败");
                    } else if (celLength == 9) {// 申请时间
                        cell.setCellValue(GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(data.getCreateTime()));
                    } else if (celLength == 10) {// 备注
                        cell.setCellValue(data.getRemark());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
        LogUtil.endLog(THIS_CLASS, FddCertificateAuthorityDefine.EXPORT_ACTION);
    }
}
