package com.hyjf.admin.exception.fdd.certificateauthorityexception;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.exception.bankdebtend.BankDebtEndDefine;
import com.hyjf.admin.exception.hjhcreditendexception.HjhCreditEndExceptionDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.CertificateAuthority;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * CA认证异常
 *
 * @author liuyang
 */
@Controller
@RequestMapping(CertificateAuthorityExceptionDefine.REQUEST_MAPPING)
public class CertificateAuthorityExceptionController extends BaseController {

    //类名
    private static final String THIS_CLASS = CertificateAuthorityExceptionController.class.toString();

    Logger _log = LoggerFactory.getLogger(CertificateAuthorityExceptionController.class);

    @Autowired
    private CertificateAuthorityExceptionService certificateAuthorityExceptionService;

    /**
     * 画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(CertificateAuthorityExceptionDefine.INIT)
    @RequiresPermissions(CertificateAuthorityExceptionDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, CertificateAuthorityExceptionBean form) {
        LogUtil.startLog(THIS_CLASS, CertificateAuthorityExceptionDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(CertificateAuthorityExceptionDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(THIS_CLASS, CertificateAuthorityExceptionDefine.INIT);
        return modelAndView;
    }

    /**
     * 画面检索
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(CertificateAuthorityExceptionDefine.SEARCH)
    @RequiresPermissions(CertificateAuthorityExceptionDefine.PERMISSIONS_SEARCH)
    public ModelAndView search(HttpServletRequest request, CertificateAuthorityExceptionBean form) {
        LogUtil.startLog(THIS_CLASS, CertificateAuthorityExceptionDefine.SEARCH);
        ModelAndView modelAndView = new ModelAndView(CertificateAuthorityExceptionDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(THIS_CLASS, CertificateAuthorityExceptionDefine.SEARCH);
        return modelAndView;
    }

    /**
     * 异常处理更新Action
     *
     * @param request
     * @param form
     * @return
     */
    @ResponseBody
    @RequiresPermissions(CertificateAuthorityExceptionDefine.PERMISSIONS_MODIFY)
    @RequestMapping(method = RequestMethod.POST, value = CertificateAuthorityExceptionDefine.MODIFY, produces = "application/json; charset=UTF-8")
    public String modifyAction(HttpServletRequest request, CertificateAuthorityExceptionBean form) {
        LogUtil.startLog(THIS_CLASS, CertificateAuthorityExceptionDefine.MODIFY);
        JSONObject ret = new JSONObject();
        // 用户ID
        if (Validator.isNull(form.getUserId())) {
            ret.put(CertificateAuthorityExceptionDefine.JSON_STATUS_KEY, CertificateAuthorityExceptionDefine.JSON_STATUS_NG);
            ret.put(CertificateAuthorityExceptionDefine.JSON_RESULT_KEY, "用户ID为空");
            return ret.toString();
        }
       // 发送CA认证MQ
        this.certificateAuthorityExceptionService.updateUserCAMQ(form.getUserId());
        ret.put(BankDebtEndDefine.JSON_STATUS_KEY, BankDebtEndDefine.JSON_STATUS_OK);
        ret.put(BankDebtEndDefine.JSON_RESULT_KEY, "债权结束成功");
        LogUtil.endLog(THIS_CLASS, CertificateAuthorityExceptionDefine.MODIFY);
        return ret.toJSONString();
    }

    /**
     * 创建分页机能
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, CertificateAuthorityExceptionBean form) {
        Integer counts = this.certificateAuthorityExceptionService.countCAExceptionList(form);
        if (counts > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), counts);
            form.setPaginator(paginator);
            List<CertificateAuthority> recordList = this.certificateAuthorityExceptionService.getCAExceptionList(form, paginator.getOffset(), paginator.getLimit());
            modelAndView.addObject("recordList", recordList);
        }
        modelAndView.addObject(CertificateAuthorityExceptionDefine.FORM, form);
    }
}
