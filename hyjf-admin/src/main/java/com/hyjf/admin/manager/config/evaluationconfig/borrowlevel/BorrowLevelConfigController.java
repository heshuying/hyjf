/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.config.evaluationconfig.borrowlevel;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.EvaluationConfig;
import com.hyjf.mybatis.model.auto.EvaluationConfigLog;
import com.hyjf.mybatis.model.auto.ParamName;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 风险测评配置- 信用等级配置Controller
 *
 * @author liuyang
 * @version BorrowLevelConfigController, v0.1 2018/11/28 17:05
 */
@Controller
@RequestMapping(BorrowLevelConfigDefine.REQUEST_MAPPING)
public class BorrowLevelConfigController extends BaseController {

    @Autowired
    private BorrowLevelConfigService borrowLevelConfigService;

    /**
     * 画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BorrowLevelConfigDefine.INIT)
    @RequiresPermissions(BorrowLevelConfigDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(BorrowLevelConfigDefine.BORROW_LEVEL_CONFIG_FORM) BorrowLevelConfigBean form) {
        LogUtil.startLog(BorrowLevelConfigController.class.toString(), BorrowLevelConfigDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(BorrowLevelConfigDefine.LIST_PATH);
        // 创建分页
        this.createPage(modelAndView, form);
        LogUtil.endLog(BorrowLevelConfigController.class.toString(), BorrowLevelConfigDefine.INIT);
        return modelAndView;
    }

    /**
     * 创建分页技能
     *
     * @param modelAndView
     * @param form
     */
    private void createPage(ModelAndView modelAndView, BorrowLevelConfigBean form) {
        List<EvaluationConfig> recordList = this.borrowLevelConfigService.getRecordList();
        if (recordList != null) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
            form.setPaginator(paginator);
            form.setRecordList(recordList);
            modelAndView.addObject(BorrowLevelConfigDefine.BORROW_LEVEL_CONFIG_FORM, form);
        }
    }

    /**
     * 画面迁移(含有id更新，不含有id添加)
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BorrowLevelConfigDefine.INFO_ACTION)
    @RequiresPermissions(value = {BorrowLevelConfigDefine.PERMISSIONS_INFO, BorrowLevelConfigDefine.PERMISSIONS_MODIFY})
    public ModelAndView info(HttpServletRequest request, BorrowLevelConfigBean form) {
        LogUtil.startLog(BorrowLevelConfigController.class.toString(), BorrowLevelConfigDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(BorrowLevelConfigDefine.INFO_PATH);
        // 风险测评投资等级
        List<ParamName> investLevelList = this.borrowLevelConfigService.getParamNameList(CustomConstants.INVEST_LEVEL);
        modelAndView.addObject("investLevelList", investLevelList);
        if (form.getId() != null) {
            Integer id = Integer.valueOf(form.getId());
            EvaluationConfig record = this.borrowLevelConfigService.getRecord(id);
            modelAndView.addObject(BorrowLevelConfigDefine.BORROW_LEVEL_CONFIG_FORM, record);
        }
        LogUtil.endLog(BorrowLevelConfigController.class.toString(), BorrowLevelConfigDefine.INIT);
        return modelAndView;
    }


    /**
     * 修改
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = BorrowLevelConfigDefine.UPDATE_ACTION, method = RequestMethod.POST)
    @RequiresPermissions(BorrowLevelConfigDefine.PERMISSIONS_MODIFY)
    public ModelAndView updateAction(HttpServletRequest request, BorrowLevelConfigBean form) {
        LogUtil.startLog(BorrowLevelConfigController.class.toString(), BorrowLevelConfigDefine.UPDATE_ACTION);
        ModelAndView modelAndView = new ModelAndView(BorrowLevelConfigDefine.INFO_PATH);
        // // 根据id更新
        if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
            return modelAndView;
        }
        // 当前登录用户id
        String username = ShiroUtil.getLoginUsername();
        EvaluationConfig evaluationConfig = this.borrowLevelConfigService.getRecord(form.getId());
        evaluationConfig.setBbbEvaluationProposal(form.getBbbEvaluationProposal());
        evaluationConfig.setaEvaluationProposal(form.getaEvaluationProposal());
        evaluationConfig.setAa0EvaluationProposal(form.getAa0EvaluationProposal());
        evaluationConfig.setAa1EvaluationProposal(form.getAa1EvaluationProposal());
        evaluationConfig.setAa2EvaluationProposal(form.getAa2EvaluationProposal());
        evaluationConfig.setAaaEvaluationProposal(form.getAaaEvaluationProposal());
        evaluationConfig.setUpdateUser(username);
        // 更新
        int re = this.borrowLevelConfigService.updateRecord(evaluationConfig);
        if (re > 0) {
            //新增日志表
            EvaluationConfigLog log = new EvaluationConfigLog();
            BeanUtils.copyProperties(evaluationConfig, log);
            // IP地址
            String ip = CustomUtil.getIpAddr(request);
            log.setIp(ip);
            log.setStatus(3);
            log.setCreateTime(GetDate.getDate());
            borrowLevelConfigService.insetRecord(log);
        }
        // 跳转页面用（info里面有）
        modelAndView.addObject(BorrowLevelConfigDefine.SUCCESS, BorrowLevelConfigDefine.SUCCESS);
        LogUtil.endLog(BorrowLevelConfigController.class.toString(), BorrowLevelConfigDefine.UPDATE_ACTION);
        return modelAndView;
    }
}
