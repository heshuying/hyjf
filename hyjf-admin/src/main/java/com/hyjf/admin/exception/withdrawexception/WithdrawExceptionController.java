package com.hyjf.admin.exception.withdrawexception;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetterUtil;
import com.hyjf.mybatis.model.customize.WithdrawCustomize;

/**
 * @package com.hyjf.admin.finance.Withdraw
 * @author GOGTZ-T
 * @date 2015/11/29 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = WithdrawExceptionDefine.REQUEST_MAPPING)
public class WithdrawExceptionController extends BaseController {
    /**
     * 类名
     */
    private static final String THIS_CLASS = WithdrawExceptionController.class.getName();

    @Autowired
    private WithdrawExceptionService withdrawExceptionService;

    /**
     * 返现管理画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(WithdrawExceptionDefine.INIT)
    @RequiresPermissions(WithdrawExceptionDefine.PERMISSIONS_WITHDRAW_VIEW)
    public ModelAndView init(HttpServletRequest request, WithdrawExceptionBean form) {
        LogUtil.startLog(THIS_CLASS, WithdrawExceptionDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(WithdrawExceptionDefine.WITHDRAW_LIST_PATH);

        modelAndView = new ModelAndView(WithdrawExceptionDefine.WITHDRAW_LIST_PATH);
        // 创建分页
        this.createWithdrawPage(request, modelAndView, form);

        LogUtil.endLog(THIS_CLASS, WithdrawExceptionDefine.INIT);
        return modelAndView;
    }

    // ***********************************************提现画面Start****************************************************

    /**
     * 提现管理画面查询
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(WithdrawExceptionDefine.SEARCH_WITHDRAW_ACTION)
    @RequiresPermissions(WithdrawExceptionDefine.PERMISSIONS_WITHDRAW_VIEW)
    public ModelAndView searchWithdraw(HttpServletRequest request, WithdrawExceptionBean form) {
        LogUtil.startLog(THIS_CLASS, WithdrawExceptionDefine.SEARCH_WITHDRAW_ACTION);
        ModelAndView modelAndView = new ModelAndView(WithdrawExceptionDefine.WITHDRAW_LIST_PATH);

        // 创建分页
        this.createWithdrawPage(request, modelAndView, form);
        LogUtil.endLog(THIS_CLASS, WithdrawExceptionDefine.SEARCH_WITHDRAW_ACTION);
        return modelAndView;
    }

    /**
     * 创建提现管理分页机能
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createWithdrawPage(HttpServletRequest request, ModelAndView modelAndView, WithdrawExceptionBean form) {
    	//汇付提现异常处理
    	form.setBankFlagSrch("0");
        int cnt = this.withdrawExceptionService.getWithdrawRecordCount(form);
        if (cnt > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), cnt);
            form.setLimitStart(paginator.getOffset());
            form.setLimitEnd(paginator.getLimit());
            List<WithdrawCustomize> recordList = this.withdrawExceptionService.getWithdrawRecordList(form);
            form.setPaginator(paginator);
            form.setRecordList(recordList);
        }
        // 提现平台列表
        modelAndView.addObject(WithdrawExceptionDefine.CLIENT_LIST, withdrawExceptionService.getParamNameList(CustomConstants.CLIENT));
        // 提现状态列表
        modelAndView.addObject(WithdrawExceptionDefine.STATUS_LIST, withdrawExceptionService.getParamNameList(CustomConstants.WITHDRAW_STATUS));

        modelAndView.addObject(WithdrawExceptionDefine.WITHDRAW_FORM, form);
    }

    /**
     * 提现确认
     *
     * @param request
     * @param form
     * @return
     */
    @ResponseBody
    @RequestMapping(WithdrawExceptionDefine.CONFIRM_WITHDRAW_ACTION)
    @RequiresPermissions(WithdrawExceptionDefine.PERMISSIONS_WITHDRAW_CONFIRM)
    public String confirmWithdrawAction(HttpServletRequest request, @RequestBody WithdrawExceptionBean form) {
        LogUtil.startLog(THIS_CLASS, WithdrawExceptionDefine.CONFIRM_WITHDRAW_ACTION);

        WithdrawExceptionBean bean = new WithdrawExceptionBean();
        // 提现ID
        Integer id = GetterUtil.getInteger(form.getIds());
        bean.setId(id);
        // 设置IP地址
        String ip = CustomUtil.getIpAddr(request);
        bean.setIp(ip);

        // 提现确认数据库操作
        String ret = withdrawExceptionService.updateWithdrawConfirm(bean, true);

        LogUtil.endLog(THIS_CLASS, WithdrawExceptionDefine.CONFIRM_WITHDRAW_ACTION);
        return ret;
    }
    // ***********************************************提现画面End****************************************************

}
