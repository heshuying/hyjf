package com.hyjf.admin.manager.config.protocol;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.enums.utils.ProtocolEnum;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.ProtocolTemplate;
import com.hyjf.mybatis.model.auto.ProtocolVersion;
import org.apache.commons.lang.StringUtils;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 协议模板管理
 * Created by xiehuili on 2018/5/25.
 */
@Controller
@RequestMapping(value = ProtocolTemplateDefine.REQUEST_MAPPING)
public class ProtocolController extends BaseController {
    @Autowired
    private ProtocolService protocolService;

    /**
     * 列表初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ProtocolTemplateDefine.INIT)
    @RequiresPermissions(ProtocolTemplateDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
                             @ModelAttribute(ProtocolTemplateDefine.FORM) ProtocolTemplateBean form) {
        LogUtil.startLog(ProtocolController.class.toString(), ProtocolTemplateDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(ProtocolTemplateDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(ProtocolController.class.toString(), ProtocolTemplateDefine.INIT);
        return modelAndView;
    }

    private void createPage(HttpServletRequest request, ModelAndView modelAndView, ProtocolTemplateBean form) {
        List<ProtocolTemplateCommon> recordList = null;
        Integer count = protocolService.countRecord(-1, -1);
        if (count.intValue()>0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            recordList = protocolService.getRecordList(form, paginator.getOffset(), paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(recordList);
            modelAndView.addObject(ProtocolTemplateDefine.FORM, form);
            String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
            modelAndView.addObject("fileDomainUrl",fileDomainUrl);
        }
    }


    /**
     * 画面迁移(含有id更新，不含有id添加)
     *
     * @param request
     * @return
     */
    @RequestMapping(ProtocolTemplateDefine.INFO_ACTION)
    @RequiresPermissions(ProtocolTemplateDefine.PERMISSIONS_VIEW)
    public ModelAndView info(HttpServletRequest request,@ModelAttribute(ProtocolTemplateDefine.FORM) ProtocolTemplateBean form) {
        LogUtil.startLog(ProtocolTemplateDefine.class.toString(), ProtocolTemplateDefine.INFO_ACTION);
        //下拉框初始化
        List<ProtocolSelectBean> selectList = initSelect();
        ModelAndView modelAndView = new ModelAndView(ProtocolTemplateDefine.INFO_PATH);
        ProtocolTemplateCommon protocolTemplateCommon =null;
        String fileDomainUrl=null;
        String id=form.getIds();
        if (StringUtils.isNotBlank(id)) {
            protocolTemplateCommon = protocolService.getProtocolTemplateById(Integer.valueOf(id));
            fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
        }
        modelAndView.addObject(ProtocolTemplateDefine.FORM, protocolTemplateCommon);
        modelAndView.addObject("fileDomainUrl",fileDomainUrl);
        modelAndView.addObject("selectList",selectList);
        LogUtil.endLog(ProtocolTemplateDefine.class.toString(), ProtocolTemplateDefine.INIT);
        return modelAndView;
    }

    /**
     * 查看页面画面迁移
     *
     * @param request
     * @return
     */
    @RequestMapping(ProtocolTemplateDefine.INFOINFO_ACTION)
    @RequiresPermissions(ProtocolTemplateDefine.PERMISSIONS_VIEW)
    public ModelAndView infoInfo(HttpServletRequest request,@ModelAttribute(ProtocolTemplateDefine.FORM) ProtocolTemplateBean form) {
        LogUtil.startLog(ProtocolTemplateDefine.class.toString(), ProtocolTemplateDefine.INFOINFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(ProtocolTemplateDefine.INFOINFO_PATH);
        ProtocolTemplateCommon protocolTemplateCommon =null;
        String fileDomainUrl=null;
        String id=form.getIds();
        if (StringUtils.isNotBlank(id)) {
            protocolTemplateCommon = protocolService.getProtocolTemplateById(Integer.valueOf(id));
            fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
        }
        modelAndView.addObject(ProtocolTemplateDefine.FORM, protocolTemplateCommon);
        modelAndView.addObject("fileDomainUrl",fileDomainUrl);
        LogUtil.endLog(ProtocolTemplateDefine.class.toString(), ProtocolTemplateDefine.INFOINFO_ACTION);
        return modelAndView;
    }
    /**
     * 添加协议模板
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = ProtocolTemplateDefine.INSERT_ACTION, method = RequestMethod.POST)
    @RequiresPermissions(ProtocolTemplateDefine.PERMISSIONS_ADD)
    public ModelAndView insertAction(HttpServletRequest request, HttpServletResponse response, ProtocolTemplateCommon form) {
        LogUtil.startLog(ProtocolTemplateDefine.class.toString(), ProtocolTemplateDefine.INSERT_ACTION);
        ProtocolTemplateBean from = new ProtocolTemplateBean();
        HashMap<String, Object> map = new HashMap<String, Object>();
        ModelAndView modelAndView = new ModelAndView(ProtocolTemplateDefine.RE_LIST_PATH);
        if(form != null){
            protocolService.insertProtocolTemplate(form);
            RedisUtils.del(RedisConstants.PROTOCOL_PARAMS);
            List<ProtocolTemplate> list = protocolService.getNewInfo();
            JSONObject jsonObject = new JSONObject();
            //是否在枚举中有定义
            for (ProtocolTemplate p : list) {
                String protocolType = p.getProtocolType();
                String alia = ProtocolEnum.getAlias(protocolType);
                if (alia != null){
                    map.put(alia, p.getDisplayName());
                }
            }
            jsonObject.put("status","000");
            jsonObject.put("statusDesc","成功");
            jsonObject.put("displayName",map);
            RedisUtils.set(RedisConstants.PROTOCOL_PARAMS,jsonObject.toString());
        }
        LogUtil.endLog(ProtocolTemplateDefine.class.toString(), ProtocolTemplateDefine.INSERT_ACTION);
        return modelAndView;
    }

    /**
     * 修改协议模板
     *
     * @param form
     * @return
     */
    @RequestMapping(value = ProtocolTemplateDefine.UPDATE_ACTION, method = RequestMethod.POST)
    @RequiresPermissions(ProtocolTemplateDefine.PERMISSIONS_MODIFY)
    public ModelAndView updateAction(ProtocolTemplateCommon form) {
        LogUtil.startLog(ProtocolTemplateDefine.class.toString(), ProtocolTemplateDefine.UPDATE_ACTION);
        ProtocolTemplateBean from = new ProtocolTemplateBean();
        HashMap<String, Object> map = new HashMap<String, Object>();
        ModelAndView modelAndView = new ModelAndView(ProtocolTemplateDefine.RE_LIST_PATH);
        if(form != null){
            protocolService.updateProtocolTemplate(form);
            RedisUtils.del(RedisConstants.PROTOCOL_PARAMS);
            List<ProtocolTemplate> list = protocolService.getNewInfo();
            JSONObject jsonObject = new JSONObject();
            //是否在枚举中有定义
            for (ProtocolTemplate p : list) {
                String protocolType = p.getProtocolType();
                String alia = ProtocolEnum.getAlias(protocolType);
                if (alia != null){
                    map.put(alia, p.getDisplayName());
                }
            }
            jsonObject.put("status","000");
            jsonObject.put("statusDesc","成功");
            jsonObject.put("displayName",map);
            RedisUtils.set(RedisConstants.PROTOCOL_PARAMS,jsonObject.toString());
        }
        LogUtil.endLog(ProtocolTemplateDefine.class.toString(), ProtocolTemplateDefine.UPDATE_ACTION);
        return modelAndView;
    }

    /**
     * 修改已经存在的协议模板
     *
     * @param form
     * @return
     */
    @RequestMapping(value = ProtocolTemplateDefine.UPDATE_EXIST_ACTION, method = RequestMethod.POST)
    @RequiresPermissions(ProtocolTemplateDefine.PERMISSIONS_MODIFY)
    public ModelAndView updateExistAction(ProtocolVersion form) {
        LogUtil.startLog(ProtocolTemplateDefine.class.toString(), ProtocolTemplateDefine.UPDATE_EXIST_ACTION);
        ProtocolTemplateBean from = new ProtocolTemplateBean();
        HashMap<String, Object> map = new HashMap<String, Object>();
        ModelAndView modelAndView = new ModelAndView(ProtocolTemplateDefine.RE_LIST_PATH);
        if(form != null){
            protocolService.updateExistAction(form);
            RedisUtils.del(RedisConstants.PROTOCOL_PARAMS);
            List<ProtocolTemplate> list = protocolService.getNewInfo();
            JSONObject jsonObject = new JSONObject();
            //是否在枚举中有定义
            for (ProtocolTemplate p : list) {
                String protocolType = p.getProtocolType();
                String alia = ProtocolEnum.getAlias(protocolType);
                if (alia != null){
                    map.put(alia, p.getDisplayName());
                }
            }
            jsonObject.put("status","000");
            jsonObject.put("statusDesc","成功");
            jsonObject.put("displayName",map);
            RedisUtils.set(RedisConstants.PROTOCOL_PARAMS,jsonObject.toString());
        }
        LogUtil.endLog(ProtocolTemplateDefine.class.toString(), ProtocolTemplateDefine.UPDATE_EXIST_ACTION);
        return modelAndView;
    }

    /**
     * 删除协议模板
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ProtocolTemplateDefine.DELETE_ACTION)
    @RequiresPermissions(ProtocolTemplateDefine.PERMISSIONS_DELETE)
    public ModelAndView deleteProtocolAction(HttpServletRequest request, @ModelAttribute(ProtocolTemplateDefine.FORM) ProtocolTemplateBean form) {
        LogUtil.startLog(ProtocolTemplateDefine.class.toString(), ProtocolTemplateDefine.DELETE_ACTION);
        HashMap<String, Object> map = new HashMap<String, Object>();
        ModelAndView modelAndView = new ModelAndView(ProtocolTemplateDefine.RE_LIST_PATH);
        String id=form.getIds();
        if (StringUtils.isNotBlank(id)) {
            protocolService.deleteProtocolTemplate(Integer.valueOf(id));
            //删除redis相应记录
            RedisUtils.del(RedisConstants.PROTOCOL_PARAMS);
            //在redis中添加最新记录
            List<ProtocolTemplate> list = protocolService.getNewInfo();
            JSONObject jsonObject = new JSONObject();
            //是否在枚举中有定义
            for (ProtocolTemplate p : list) {
                String protocolType = p.getProtocolType();
                String alia = ProtocolEnum.getAlias(protocolType);
                if (alia != null){
                    map.put(alia, p.getDisplayName());
                }
            }
            jsonObject.put("status","000");
            jsonObject.put("statusDesc","成功");
            jsonObject.put("displayName",map);
            RedisUtils.set(RedisConstants.PROTOCOL_PARAMS,jsonObject.toString());
        }
        LogUtil.endLog(ProtocolTemplateDefine.class.toString(), ProtocolTemplateDefine.DELETE_ACTION);
        return modelAndView;
    }

    /**
     * 校验表单字段是否唯一
     *
     * @return
     */
    @RequestMapping(value = ProtocolTemplateDefine.VALIDATION_ACTION, method = RequestMethod.POST)
    @ResponseBody
    public JSONObject validatFieldCheck(HttpServletRequest request) {
        //获取校验参数
        String protocolName = request.getParameter("protocolName");//协议模板名称
        String protocolType = request.getParameter("protocolType");//协议类别
        String versionNumber = request.getParameter("versionNumber");//版本号
        String displayName = request.getParameter("displayName");//前台展示名称
        String protocolUrl = request.getParameter("protocolUrl");//协议上传路径
        String oldDisplayName = request.getParameter("oldDisplayName");//原前台展示名称
        String flag = request.getParameter("flag");//原前台展示名称
        return  protocolService.validatorFieldCheck(protocolName,versionNumber,displayName,protocolUrl,protocolType,oldDisplayName,flag);
    }

    /**
     * 资料上传
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = ProtocolTemplateDefine.UPLOAD_FILE, method = RequestMethod.POST)
    @ResponseBody
//    @RequiresPermissions(ProtocolTemplateDefine.PERMISSIONS_VIEW)
    public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LogUtil.startLog(ProtocolTemplateDefine.class.toString(), ProtocolTemplateDefine.UPLOAD_FILE);
        String files = protocolService.uploadFile(request, response);
        LogUtil.endLog(ProtocolTemplateDefine.class.toString(), ProtocolTemplateDefine.UPLOAD_FILE);
        return files;
    }

    public List<ProtocolSelectBean> initSelect() {
        List<ProtocolSelectBean> selectList = new ArrayList<ProtocolSelectBean>();
        ProtocolSelectBean bean1 = new ProtocolSelectBean();
        bean1.setCode("《注册协议》");
        bean1.setName("《注册协议》");
        selectList.add(bean1);
        ProtocolSelectBean bean2 = new ProtocolSelectBean();
        bean2.setCode("《平台隐私条款》");
        bean2.setName("《平台隐私条款》");
        selectList.add(bean2);
        ProtocolSelectBean bean3 = new ProtocolSelectBean();
        bean3.setCode("《出借咨询与管理服务协议》");
        bean3.setName("《出借咨询与管理服务协议》");
        selectList.add(bean3);
        ProtocolSelectBean bean4 = new ProtocolSelectBean();
        bean4.setCode("《用户授权协议》");
        bean4.setName("《用户授权协议》");
        selectList.add(bean4);
        ProtocolSelectBean bean5 = new ProtocolSelectBean();
        bean5.setCode("《居间服务借款协议》");
        bean5.setName("《居间服务借款协议》");
        selectList.add(bean5);
        ProtocolSelectBean bean6 = new ProtocolSelectBean();
        bean6.setCode("《散标风险揭示书》");
        bean6.setName("《散标风险揭示书》");
        selectList.add(bean6);
        ProtocolSelectBean bean7 = new ProtocolSelectBean();
        bean7.setCode("《债权转让协议》");
        bean7.setName("《债权转让协议》");
        selectList.add(bean7);
        ProtocolSelectBean bean9 = new ProtocolSelectBean();
        bean9.setCode("《开户协议》");
        bean9.setName("《开户协议》");
        selectList.add(bean9);
        ProtocolSelectBean bean10 = new ProtocolSelectBean();
        bean10.setCode("《智投服务协议》");
        bean10.setName("《智投服务协议》");
        selectList.add(bean10);
        ProtocolSelectBean bean11 = new ProtocolSelectBean();
        bean11.setCode("《智投风险揭示书》");
        bean11.setName("《智投风险揭示书》");
        selectList.add(bean11);
        ProtocolSelectBean bean12 = new ProtocolSelectBean();
        bean12.setCode("《自动投标授权》");
        bean12.setName("《自动投标授权》");
        selectList.add(bean12);
        ProtocolSelectBean bean13 = new ProtocolSelectBean();
        bean13.setCode("《自动债转授权》");
        bean13.setName("《自动债转授权》");
        selectList.add(bean13);
        ProtocolSelectBean bean14 = new ProtocolSelectBean();
        bean14.setCode("《缴费授权》");
        bean14.setName("《缴费授权》");
        selectList.add(bean14);
        ProtocolSelectBean bean15 = new ProtocolSelectBean();
        bean15.setCode("《还款授权》");
        bean15.setName("《还款授权》");
        selectList.add(bean15);
        return selectList;
    }

    /**
     * 协议模板下拉框
     */
    public class ProtocolSelectBean implements Serializable {

        private  String code;
        private  String name;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}


