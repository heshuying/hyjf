package com.hyjf.admin.manager.user.protocols;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonService;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.CertificateAuthority;
import com.hyjf.mybatis.model.auto.FddTemplet;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.FddTempletCustomize;
import com.hyjf.pay.lib.fadada.bean.DzqzCallBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 友情连接
 *
 * @author qingbing
 */
@Controller
@RequestMapping(value = ProtocolsDefine.REQUEST_MAPPING)
public class ProtocolsController extends BaseController {

    @Autowired
    private ProtocolsService protocolsService;
    @Autowired
    private BorrowCommonService borrowCommonService;
    /**
     * 画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ProtocolsDefine.INIT)
    @RequiresPermissions(ProtocolsDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, ProtocolsBean form) {
        LogUtil.startLog(ProtocolsController.class.toString(), ProtocolsDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(ProtocolsDefine.LIST_PATH);

        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(ProtocolsController.class.toString(), ProtocolsDefine.INIT);
        return modelAndView;
    }

    /**
     *
     * 列表检索Action
     * @author liubin
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ProtocolsDefine.SEARCH_ACTION)
    @RequiresPermissions(ProtocolsDefine.PERMISSIONS_SEARCH)
    public ModelAndView search(HttpServletRequest request, ProtocolsBean form) {
        LogUtil.startLog(ProtocolsController.class.toString(), ProtocolsDefine.SEARCH_ACTION);
        ModelAndView modelAndView = new ModelAndView(ProtocolsDefine.LIST_PATH);
        // 分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(ProtocolsController.class.toString(), ProtocolsDefine.SEARCH_ACTION);
        return modelAndView;
    }

    /**
     * 分页
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, ProtocolsBean form) {
        int recordTotal = this.protocolsService.countRecord();
        if (recordTotal > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
            List<FddTempletCustomize> recordList = this.protocolsService.getRecordList(paginator.getOffset(), paginator.getLimit());
            form.setRecordList(recordList);
            form.setPaginator(paginator);
        }
        modelAndView.addObject(ProtocolsDefine.PROTOCOLS_FORM, form);
    }

    /**
     *
     * 画面迁移(含有id更新，不含有id添加)
     * @author liubin
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ProtocolsDefine.INFO_ACTION)
    public ModelAndView info(HttpServletRequest request, ProtocolsBean form) {
        LogUtil.startLog(ProtocolsController.class.toString(), ProtocolsDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(ProtocolsDefine.INFO_PATH);
        FddTemplet record = new FddTemplet();
        if (form.getId() != null) {
            record = this.protocolsService.getRecordInfo(form.getId());
        }
        modelAndView.addObject(ProtocolsDefine.PROTOCOLS_FORM, record);

        // 协议类型下拉
        List<ParamName> paramNameList = this.protocolsService.getParamNameList("PROTOCOL_TYPE");
        modelAndView.addObject("protocolTypeList", paramNameList);

        LogUtil.endLog(ProtocolsController.class.toString(), ProtocolsDefine.INFO_ACTION);
        return modelAndView;
    }

    /**
     * 下拉联动(取得最新的协议模板编号)
     *
     * @param request
     * @return 进入资产列表页面
     */
    @RequestMapping(ProtocolsDefine.PROTOCOLTYPE_ACTION)
    @RequiresPermissions(ProtocolsDefine.PERMISSIONS_VIEW)
    @ResponseBody
    public String protocolTypeAction(HttpServletRequest request, RedirectAttributes attr,
                                     String protocolType) {
        LogUtil.startLog(ProtocolsController.class.toString(), ProtocolsDefine.PROTOCOLTYPE_ACTION);
        String templetId = this.protocolsService.getNewTempletId(Integer.parseInt(protocolType));
        LogUtil.endLog(ProtocolsController.class.toString(), ProtocolsDefine.PROTOCOLTYPE_ACTION);
        return templetId;
    }

    /**
     * 模板上传
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = ProtocolsDefine.UPLOAD_FILE, method = RequestMethod.POST)
    @ResponseBody
//    public DzqzCallBean uploadFile(@RequestParam(value = "file", required = false) MultipartFile file,
//                             @RequestParam(value = "templetId") String templetId) throws Exception {
    public DzqzCallBean uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LogUtil.startLog(ProtocolsController.class.toString(), ProtocolsDefine.UPLOAD_FILE);
        // 错误信息对象
        DzqzCallBean errDzqzCallBean = new DzqzCallBean();
        // request转换为MultipartHttpServletRequest
        ShiroHttpServletRequest shiroRequest = (ShiroHttpServletRequest) request;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        MultipartHttpServletRequest multipartRequest = commonsMultipartResolver.resolveMultipart((HttpServletRequest) shiroRequest.getRequest());

        String templetId = multipartRequest.getParameter("templetId");
        // ======上传校验=======
        if (templetId.isEmpty()){
            errDzqzCallBean.setErrMsg("协议类型必须选择。");
            return errDzqzCallBean;
        }
        //从request中取得MultipartFile列表
        List<MultipartFile> multipartFileList = this.protocolsService.getMultipartFileList(multipartRequest);
        if (multipartFileList == null || multipartFileList.size() <= 0){
            errDzqzCallBean.setErrMsg("获取上传文件失败！");
            return errDzqzCallBean;
        }
        if (multipartFileList.size() > 1){
            errDzqzCallBean.setErrMsg("不可同时上传多个文件");
            return errDzqzCallBean;
        }
        //从MultipartFile列表中取得唯一的Multipart
        MultipartFile file = multipartFileList.get(0);
        if (file == null){
            errDzqzCallBean.setErrMsg("获取上传模板失败！");
            return errDzqzCallBean;
        }
        String fileName = file.getOriginalFilename();
        if (!(fileName.substring(fileName.length()-4)).toUpperCase().equals(".PDF")){
            errDzqzCallBean.setErrMsg("上传的模板必须是PDF格式。");
            return errDzqzCallBean;
        }
        // ======调模板上传FTP服务器=======
        String httpUrl = this.protocolsService.uploadTempletToFtp(file, "FddTemplet", 0);
        if (StringUtils.isBlank(httpUrl)){
            errDzqzCallBean.setErrMsg("上传FTP服务器失败。");
            return errDzqzCallBean;
        }
        //调用发大大模板上传接口
//        DzqzCallBean dzqzCallBean = this.protocolsService.uploadtemplateDZApi(file, templetId);
        DzqzCallBean dzqzCallBean = this.protocolsService.uploadtemplateDZApi(httpUrl, templetId);
        if (dzqzCallBean == null) {
            errDzqzCallBean.setErrMsg("返回结果为空，上传模板失败。");
            return errDzqzCallBean;
        }

        // 成功后地址更新到页面
        dzqzCallBean.setFileUrl(httpUrl);

        LogUtil.endLog(ProtocolsController.class.toString(), ProtocolsDefine.UPLOAD_FILE);
        return dzqzCallBean;
    }

    /**
     *
     * 插入操作
     * @author liubin
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ProtocolsDefine.INSERT_ACTION)
    @RequiresPermissions(ProtocolsDefine.PERMISSIONS_ADD)
    public ModelAndView insertAction(HttpServletRequest request, ProtocolsBean form) {
        LogUtil.startLog(ProtocolsController.class.toString(), ProtocolsDefine.INSERT_ACTION);
        ModelAndView modelAndView = new ModelAndView(ProtocolsDefine.INFO_PATH);

        // enctype="multipart/form-data"
        // request转换为MultipartHttpServletRequest
        ShiroHttpServletRequest shiroRequest = (ShiroHttpServletRequest) request;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        MultipartHttpServletRequest multipartRequest = commonsMultipartResolver.resolveMultipart((HttpServletRequest) shiroRequest.getRequest());
        // 表单项绑定到Bean
        requestToProtocolsBean(form, multipartRequest);

        // 表单校验
        this.validatorFieldCheck(modelAndView, form);
        if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
            // 协议类型下拉
            List<ParamName> paramNameList = this.protocolsService.getParamNameList("PROTOCOL_TYPE");
            modelAndView.addObject("protocolTypeList", paramNameList);
            modelAndView.addObject(ProtocolsDefine.PROTOCOLS_FORM, form);
            LogUtil.errorLog(ProtocolsController.class.toString(), ProtocolsDefine.INSERT_ACTION, "输入内容验证失败", null);
            return modelAndView;
        }

        // 插入
        this.protocolsService.insertRecord(form);
        modelAndView.addObject(ProtocolsDefine.SUCCESS, ProtocolsDefine.SUCCESS);
        LogUtil.endLog(ProtocolsController.class.toString(), ProtocolsDefine.INSERT_ACTION);
        return modelAndView;
    }

    /**
     *
     * 数据更新
     * @author liubin
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ProtocolsDefine.UPDATE_ACTION)
    @RequiresPermissions(ProtocolsDefine.PERMISSIONS_MODIFY)
    public ModelAndView updateAction(HttpServletRequest request, ProtocolsBean form) {
        LogUtil.startLog(ProtocolsController.class.toString(), ProtocolsDefine.UPDATE_ACTION);
        ModelAndView modelAndView = new ModelAndView(ProtocolsDefine.INFO_PATH);

        // enctype="multipart/form-data"
        // request转换为MultipartHttpServletRequest
        ShiroHttpServletRequest shiroRequest = (ShiroHttpServletRequest) request;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        MultipartHttpServletRequest multipartRequest = commonsMultipartResolver.resolveMultipart((HttpServletRequest) shiroRequest.getRequest());
        // 表单项绑定到Bean
        requestToProtocolsBean(form, multipartRequest);

        // 数据更新
        this.protocolsService.updateRecord(form);
        modelAndView.addObject(ProtocolsDefine.SUCCESS, ProtocolsDefine.SUCCESS);
        LogUtil.endLog(ProtocolsController.class.toString(), ProtocolsDefine.UPDATE_ACTION);
        return modelAndView;
    }

    /**
     * 表单项绑定到Bean
     * @param form
     * @param multipartRequest
     */
    private void requestToProtocolsBean(ProtocolsBean form, MultipartHttpServletRequest multipartRequest) {
        form.setTempletId(multipartRequest.getParameter("templetId"));
        form.setRemark(multipartRequest.getParameter("remark"));
        form.setId(DigitalUtils.toInteger(multipartRequest.getParameter("id")));
        form.setProtocolType(DigitalUtils.toInteger(multipartRequest.getParameter("protocolType")));
        form.setIsActive(DigitalUtils.toInteger(multipartRequest.getParameter("isActive")));
        form.setCaFlag(DigitalUtils.toInteger(multipartRequest.getParameter("caFlag")));
        form.setFileUrl(multipartRequest.getParameter("fileUrl"));
    }

    /**
     * 画面校验
     *
     * @param modelAndView
     * @param form
     */
    private void validatorFieldCheck(ModelAndView modelAndView, ProtocolsBean form) {
        //协议类型
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "protocolType", StringUtil.toString(form.getProtocolType()));
        //模版编号
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "templetId", form.getTempletId());
        //CA认证
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "caFlag", StringUtil.toString(form.getCaFlag()));
        //状态
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "isActive", StringUtil.toString(form.getIsActive()));
        //自动备案
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "remark", form.getRemark());

        // 检查唯一性
        int cnt =
                this.protocolsService.countRecordByPK(form.getTempletId());
        if (cnt > 0) {
            ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "templetId", "errors.protocols.pk.repeat");
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
    @RequestMapping(ProtocolsDefine.EXPORT_ACTION)
    @RequiresPermissions(ProtocolsDefine.PERMISSIONS_EXPORT)
    public void exportExcel(@ModelAttribute ProtocolsBean form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        LogUtil.startLog(ProtocolsController.class.toString(), ProtocolsDefine.EXPORT_ACTION);
        // 表格sheet名称
        String sheetName = "协议管理";
        // 文件名称
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        // 需要输出的结果列表
        List<FddTempletCustomize> recordList = this.protocolsService.getRecordList(-1, -1);
        String[] titles = new String[] { "序号", "模版编号", "协议类型", "启用状态", "CA认证", "认证时间", "操作人", "操作时间", "备注" };
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
                    FddTempletCustomize data = recordList.get(i);
                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    if (celLength == 0) {// 序号
                        cell.setCellValue(i + 1);
                    } else if (celLength == 1) {// 模版编号
                        cell.setCellValue(data.getTempletId());
                    } else if (celLength == 2) {// 协议类型
                        cell.setCellValue(data.getProtocolTypeName());
                    } else if (celLength == 3) {// 启用状态
                        cell.setCellValue(data.getIsActive().compareTo(1) == 0 ? "启用" : "关闭");
                    } else if (celLength == 4) {// CA认证
                        cell.setCellValue(data.getCaFlagName());
                    } else if (celLength == 5) {// 认证时间
                        if (data.getCertificateTime() == null){
                            cell.setCellValue("");
                        }else{
                            cell.setCellValue(GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(data.getCertificateTime()));
                        }
                    } else if (celLength == 6) {// 操作人
                        cell.setCellValue(data.getCreateUserName());
                    } else if (celLength == 7) {// 操作时间
                        cell.setCellValue(GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(data.getCreateTime()));
                    } else if (celLength == 8) {// 备注
                        cell.setCellValue(data.getRemark());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
        LogUtil.endLog(ProtocolsController.class.toString(), ProtocolsDefine.EXPORT_ACTION);
    }
}
