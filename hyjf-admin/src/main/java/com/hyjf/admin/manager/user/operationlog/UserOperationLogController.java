/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.user.operationlog;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.config.operationlog.OperationLogService;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yaoyong
 * @version UserOperationLogController, v0.1 2018/10/9 15:57
 */
@RestController
@RequestMapping(value = UserOperationLogDefine.REQUEST_MAPPING)
public class UserOperationLogController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(UserOperationLogController.class);

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 页面初始化
     *
     * @param form
     * @return
     */
    @RequestMapping(value = UserOperationLogDefine.OPERATIONLOG_LIST_ACTION)
    public ModelAndView init(@ModelAttribute(UserOperationLogDefine.OPERATIONLOG_LIST_FORM) UserOperationLogBean form) {
        logger.info("会员操作日志 form ：{}" + form);
        ModelAndView modelAndView = new ModelAndView(UserOperationLogDefine.OPERATIONLOG_LIST_PATH);
        // 创建分页
        this.createPage(modelAndView, form);
        return modelAndView;
    }

    private void createPage(ModelAndView modelAndView, UserOperationLogBean form) {
        //封装查询条件
        Map<String, Object> operationLog = this.buildQueryCondition(form);
        int recordTotal = operationLogService.countOperationLog(operationLog);
        if (recordTotal > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
            List<UserOperationLogEntity> recordList = operationLogService.getOperationLogList(operationLog,
                    paginator.getOffset(), paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(recordList);
        }
        modelAndView.addObject("obj", form);
    }

    /**
     * 会员操作日志列表导出
     *
     * @param form
     * @param request
     * @param response
     */
    @RequestMapping(value = UserOperationLogDefine.EXPORT_OPERATIONLOG_ACTION)
    public void exportExcel(@ModelAttribute UserOperationLogBean form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("会员操作日志表格导出");
        //表格sheet名称
        String sheetName = "会员操作日志";
        // 文件名称
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
                + CustomConstants.EXCEL_EXT;
        //解决IE浏览器导出列表中文乱码问题
        String userAgent = request.getHeader("user-agent").toLowerCase();
        if (userAgent.contains("msie") || userAgent.contains("like gecko")) {
            // win10 ie edge 浏览器 和其他系统的ie
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } else {
            fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
        }

        // 封装查询条件
        Map<String, Object> param = this.buildQueryCondition(form);
        List<UserOperationLogEntity> recordList = operationLogService.getOperationLogList(param, -1, -1);
        String[] titles = new String[]{"活动类型", "用户角色", "用户名", "操作平台", "备注", "IP", "操作时间"};
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
                try {
                    for (int celLength = 0; celLength < titles.length; celLength++) {
                        UserOperationLogEntity operationLogEntity = recordList.get(i);
                        // 创建相应的单元格
                        Cell cell = row.createCell(celLength);
                        if (celLength == 0) {
                            if (operationLogEntity.getOperationType() == 1) {
                                cell.setCellValue("登录");
                            }else if (operationLogEntity.getOperationType() == 2) {
                                cell.setCellValue("登出");
                            }else if (operationLogEntity.getOperationType() == 3) {
                                cell.setCellValue("开户");
                            }else if (operationLogEntity.getOperationType() == 4) {
                                cell.setCellValue("出借确认");
                            }else if (operationLogEntity.getOperationType() == 5) {
                                cell.setCellValue("转让确认");
                            }else if (operationLogEntity.getOperationType() == 6) {
                                cell.setCellValue("修改交易密码");
                            }else if (operationLogEntity.getOperationType() == 7) {
                                cell.setCellValue("修改登录密码");
                            }else if (operationLogEntity.getOperationType() == 8) {
                                cell.setCellValue("绑定邮箱");
                            }else if (operationLogEntity.getOperationType() == 9) {
                                cell.setCellValue("修改邮箱");
                            }else if (operationLogEntity.getOperationType() == 10) {
                                cell.setCellValue("绑定银行卡");
                            }else if (operationLogEntity.getOperationType() == 11) {
                                cell.setCellValue("解绑银行卡");
                            }else if (operationLogEntity.getOperationType() == 12) {
                                cell.setCellValue("风险测评");
                            }
                        }else if (celLength == 1) {
                            if (operationLogEntity.getUserRole().equals("1")) {
                                cell.setCellValue("出借人");
                            }else if (operationLogEntity.getUserRole().equals("2")) {
                                cell.setCellValue("借款人");
                            }else if (operationLogEntity.getUserRole().equals("3")) {
                                cell.setCellValue("担保机构");
                            }
                        }else if (celLength == 2) {
                            cell.setCellValue(operationLogEntity.getUserName());
                        }else if (celLength == 3) {
                            if (operationLogEntity.getPlatform() == 0) {
                                cell.setCellValue("PC");
                            }else if (operationLogEntity.getPlatform() == 1) {
                                cell.setCellValue("wechat");
                            }else if (operationLogEntity.getPlatform() == 2) {
                                cell.setCellValue("Andriod");
                            }else if (operationLogEntity.getPlatform() == 3) {
                                cell.setCellValue("IOS");
                            }
                        }else if (celLength == 4) {
                            cell.setCellValue(operationLogEntity.getRemark());
                        }else if (celLength == 5) {
                            cell.setCellValue(operationLogEntity.getIp());
                        }else if (celLength == 6) {
                            cell.setCellValue(GetDate.dateToString(operationLogEntity.getOperationTime()));
                        }
                    }
                } catch (Exception e) {
                    logger.info("导出表格错误 e : {}", e);
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
    }

    //构建查询条件
    private Map<String, Object> buildQueryCondition(UserOperationLogBean form) {
        Map<String, Object> operationLog = new HashMap<>();
        String userName = StringUtils.isNotBlank(form.getUserName()) ? form.getUserName() : null;
        Integer operationType = form.getOperationType() != null ? form.getOperationType() : null;
        String userRole = StringUtils.isNotBlank(form.getUserRole()) ? form.getUserRole() : null;
        String operationTimeStart = StringUtils.isNotBlank(form.getOperationTimeStart()) ? form.getOperationTimeStart() + " 00:00:00" : null;
        String operationTimeEnd = StringUtils.isNotBlank(form.getOperationTimeEnd()) ? form.getOperationTimeEnd() + " 23:59:59" : null;
        operationLog.put("userName", userName);
        operationLog.put("operationType", operationType);
        operationLog.put("userRole", userRole);
        operationLog.put("operationTimeStart", operationTimeStart);
        operationLog.put("operationTimeEnd", operationTimeEnd);
        return operationLog;
    }
}
