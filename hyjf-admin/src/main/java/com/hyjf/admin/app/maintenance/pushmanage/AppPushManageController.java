package com.hyjf.admin.app.maintenance.pushmanage;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.AppPushManage;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * App 推送管理
 * @Author : huanghui
 */
@Controller
@RequestMapping(value = AppPushManageDefine.REQUEST_MAPPING)
public class AppPushManageController extends BaseController {

    @Autowired
    private AppPushManageService appPushManageService;

    /**
     * 页面初始化
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = AppPushManageDefine.INIT)
    @RequiresPermissions(AppPushManageDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(AppPushManageDefine.APP_PUSH_MANAGE_FORM) AppPushManageBean form){
        LogUtil.startLog(AppPushManageController.class.toString(), AppPushManageDefine.INIT);

        ModelAndView modelAndView = new ModelAndView(AppPushManageDefine.LIST_PATH);

        //创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(AppPushManageController.class.toString(), AppPushManageDefine.INIT);
        return modelAndView;
    }

    /**
     * 画面迁移,含ID更新数据,不含ID添加数据
     * @param request
     * @return
     */
    @RequestMapping(value = AppPushManageDefine.INFO_ACTION)
    @RequiresPermissions(value = {AppPushManageDefine.PERMISSIONS_INFO, AppPushManageDefine.PERMISSIONS_ADD, AppPushManageDefine.PERMISSIONS_MODIFY}, logical = Logical.OR)
    public ModelAndView infoAction(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(AppPushManageDefine.APP_PUSH_MANAGE_FORM) AppPushManageBean form){
        LogUtil.startLog(AppPushManageController.class.toString(), AppPushManageDefine.INFO_ACTION);

        ModelAndView modelAndView = new ModelAndView(AppPushManageDefine.INFO_PATH);

        AppPushManage appPushManageInfo = new AppPushManage();

        if (StringUtils.isNotEmpty(form.getIds())){
            Integer id = Integer.valueOf(form.getIds());

            appPushManageInfo = this.appPushManageService.getAppPushManageInfo(id);
        }

        BeanUtils.copyProperties(appPushManageInfo, form);
        modelAndView.addObject(AppPushManageDefine.APP_PUSH_MANAGE_FORM, form);

        LogUtil.endLog(AppPushManageController.class.toString(), AppPushManageDefine.INFO_ACTION);
        return modelAndView;
    }

    private void createPage(HttpServletRequest request, ModelAndView modelAndView, AppPushManageBean form){
        List<AppPushManage> recordList = this.appPushManageService.selectRecordList(form, -1, -1);

        if (recordList != null){
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
            recordList = this.appPushManageService.selectRecordList(form, paginator.getOffset(), paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(recordList);
            modelAndView.addObject(AppPushManageDefine.APP_PUSH_MANAGE_FORM, form);
        }
    }

    @RequestMapping(value = AppPushManageDefine.SEARCH_ACTION)
    @RequiresPermissions(AppPushManageDefine.PERMISSIONS_SEARCH)
    public ModelAndView  searchAction(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(AppPushManageDefine.APP_PUSH_MANAGE_FORM) AppPushManageBean form){
        //开始日志
        LogUtil.startLog(AppPushManageController.class.toString(), AppPushManageDefine.SEARCH_ACTION);
        ModelAndView modelAndView = new ModelAndView(AppPushManageDefine.LIST_PATH);

        this.createPage(request, modelAndView, form);

        LogUtil.endLog(AppPushManageController.class.toString(), AppPushManageDefine.SEARCH_ACTION);
        return modelAndView;
    }

    /**
     * 插入数据
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = AppPushManageDefine.INSERT_ACTION)
    @RequiresPermissions(AppPushManageDefine.PERMISSIONS_ADD)
    public ModelAndView insertAction(HttpServletRequest request, AppPushManageBean form) throws ParseException {
        //开始日志
        LogUtil.startLog(AppPushManageController.class.toString(), AppPushManageDefine.INSERT_ACTION);
        ModelAndView modelAndView = new ModelAndView(AppPushManageDefine.INFO_PATH);

        //将日期转换为时间戳
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date timeStart = format.parse(form.getTimeStartDiy());
        Date timeEnd = format.parse(form.getTimeEndDiy());

        //将日期转换为Long型时间戳
        Long longTimeStart = timeStart.getTime()/1000;
        Long longTimeEnd = timeEnd.getTime()/1000;

        //将Long型时间戳转换为Integer型
        Integer intTimeStart = longTimeStart.intValue();
        Integer intTimeEnd = longTimeEnd.intValue();

        form.setTimeStart(intTimeStart);
        form.setTimeEnd(intTimeEnd);

        /**
         * 当选择原生时强制设置跳转内容值为 0
         * 选择H5时,赋为选择值+1
         * 原生和H5 URL 时,推送内容和缩略图为空
         * H5 自定义是url内容为空
         */
        if (form.getJumpType() == 0){
            form.setJumpContent(0);
            form.setContent("");
            form.setThumb("");
        }else {
            if (form.getJumpContent() == 0){
                form.setContent("");
                form.setThumb("");
            }else{
                form.setJumpUrl("");
            }
            form.setJumpContent((form.getJumpContent()+1));
        }

        Integer result = this.appPushManageService.insertRecord(form);

        modelAndView.addObject(AppPushManageDefine.SUCCESS, AppPushManageDefine.SUCCESS);
        //结束日志
        LogUtil.endLog(AppPushManageController.class.toString(), AppPushManageDefine.INSERT_ACTION);
        return modelAndView;
    }

    /**
     * 更新数据
     * @param request
     * @param form
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = AppPushManageDefine.UPDATE_ACTION)
    @RequiresPermissions(AppPushManageDefine.PERMISSIONS_MODIFY)
    public ModelAndView updateAction(HttpServletRequest request, AppPushManageBean form) throws ParseException{
        //开始日志
        LogUtil.startLog(AppPushManageController.class.toString(), AppPushManageDefine.UPDATE_ACTION);

        ModelAndView modelAndView = new ModelAndView(AppPushManageDefine.RE_LIST_PATH);

        //将日期转换为时间戳
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date timeStart = format.parse(form.getTimeStartDiy());
        Date timeEnd = format.parse(form.getTimeEndDiy());

        //将日期转换为Long型时间戳
        Long longTimeStart = timeStart.getTime()/1000;
        Long longTimeEnd = timeEnd.getTime()/1000;

        //将Long型时间戳转换为Integer型
        Integer intTimeStart = longTimeStart.intValue();
        Integer intTimeEnd = longTimeEnd.intValue();

        form.setTimeStart(intTimeStart);
        form.setTimeEnd(intTimeEnd);

        if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())){
            return modelAndView;
        }

        //当选择原生时强制设置跳转内容值为 0
        //选择H5时,赋为选择值+1
        //原生和H5 URL 时,推送内容和缩略图为空
        //H5 自定义是url内容为空
        if (form.getJumpType() == 0){
            form.setJumpContent(0);
            form.setContent("");
            form.setThumb("");
        }else {
            if (form.getJumpContent() == 0){
                form.setContent("");
                form.setThumb("");
            }else{
                form.setJumpUrl("");
            }
            form.setJumpContent((form.getJumpContent()+1));
        }

        int result = this.appPushManageService.updateRecord(form);

        modelAndView.addObject(AppPushManageDefine.SUCCESS, AppPushManageDefine.SUCCESS);
        LogUtil.endLog(AppPushManageController.class.toString(), AppPushManageDefine.UPDATE_ACTION);
        return  modelAndView;
    }

    /**
     * 更新推送状态
     * @param request
     * @param attr
     * @param form
     * @return
     */
    @RequestMapping(value = AppPushManageDefine.STATUS_ACTION, method = RequestMethod.POST)
    @RequiresPermissions(AppPushManageDefine.PERMISSIONS_MODIFY)
    public ModelAndView updateStatus(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(AppPushManageDefine.APP_PUSH_MANAGE_FORM) AppPushManageBean form){
        //开始日志
        LogUtil.startLog(AppPushManageController.class.toString(), AppPushManageDefine.STATUS_ACTION);

        ModelAndView modelAndView = new ModelAndView(AppPushManageDefine.RE_LIST_PATH);

        if (StringUtils.isNotEmpty(form.getIds())){
            Integer id = Integer.valueOf(form.getIds());
            AppPushManage record = this.appPushManageService.getRecord(id);
            if (record.getStatus() == 1){
                record.setStatus(0);
            }else {
                record.setStatus(1);
            }

            this.appPushManageService.updateRecordNoVoid(record);
        }

        attr.addFlashAttribute(AppPushManageDefine.APP_PUSH_MANAGE_FORM, form);
        LogUtil.endLog(AppPushManageController.class.toString(), AppPushManageDefine.STATUS_ACTION);
        return modelAndView;
    }

    /**
     * 删除信息操作
     * @param request
     * @param ids
     * @return
     */
    @RequestMapping(value = AppPushManageDefine.DELETE_ACTION)
    @RequiresPermissions(AppPushManageDefine.PERMISSIONS_DELETE)
    public ModelAndView deleteRecordAction(HttpServletRequest request, String ids){
        //开始日志
        LogUtil.startLog(AppPushManageController.class.toString(), AppPushManageDefine.DELETE_ACTION);

        ModelAndView modelAndView = new ModelAndView(AppPushManageDefine.RE_LIST_PATH);

        List<Integer> recordList = JSONArray.parseArray(ids, Integer.class);
        this.appPushManageService.deleteRecord(recordList);

        LogUtil.endLog(AppPushManageController.class.toString(), AppPushManageDefine.DELETE_ACTION);
        return modelAndView;
    }

    @RequestMapping(value = AppPushManageDefine.UPLOAD_FILE, method = RequestMethod.POST)
    @ResponseBody
    public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception{
        LogUtil.startLog(AppPushManageController.class.toString(), AppPushManageDefine.UPLOAD_FILE);
        String files = this.appPushManageService.uploadFile(request, response);
        LogUtil.endLog(AppPushManageController.class.toString(), AppPushManageDefine.UPLOAD_FILE);
        return files;
    }

    private ModelAndView validatorFieldCheck(ModelAndView modelAndView, AppPushManage form){

        return null;
    }


}
