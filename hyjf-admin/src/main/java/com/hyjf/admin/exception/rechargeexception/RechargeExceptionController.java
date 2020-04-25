package com.hyjf.admin.exception.rechargeexception;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.finance.pushMoney.PushMoneyManageDefine;
import com.hyjf.admin.manager.user.bankcard.BankcardService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.customize.RechargeCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;

/**
 * 充值管理
 *
 * @author HBZ
 */
@Controller
@RequestMapping(value = RechargeExceptionDefine.REQUEST_MAPPING)
public class RechargeExceptionController extends BaseController {
    @Autowired
    private RechargeExceptionService rechargeExceptionService;

    @Autowired
    private BankcardService bankcardService;

    /**
     * 账户管理 列表
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(RechargeExceptionDefine.INIT)
    @RequiresPermissions(RechargeExceptionDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, RechargeExceptionBean form) {
        LogUtil.startLog(RechargeExceptionController.class.toString(), RechargeExceptionDefine.INIT);
        ModelAndView modeAndView = new ModelAndView(RechargeExceptionDefine.LIST_PATH);

        // 创建分页
        this.createPage(request, modeAndView, form);
        LogUtil.endLog(RechargeExceptionController.class.toString(), RechargeExceptionDefine.INIT);
        return modeAndView;
    }

    /**
     * 分页
     *
     * @param request
     * @param modeAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modeAndView, RechargeExceptionBean form) {
        RechargeCustomize rechargeCustomize = new RechargeCustomize();
        BeanUtils.copyProperties(form, rechargeCustomize);

        // 银行列表
        List<BankConfig> banks = this.bankcardService.getBankcardList();
        form.setBankList(banks);
        rechargeCustomize.setStatus("1");
        Integer count = this.rechargeExceptionService.queryRechargeCount(rechargeCustomize);
        if (count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            form.setPaginator(paginator);

            rechargeCustomize.setLimitStart(paginator.getOffset());
            rechargeCustomize.setLimitEnd(paginator.getLimit());
            rechargeCustomize.setStatus("1");
            List<RechargeCustomize> rechargeCustomizes =
                    this.rechargeExceptionService.queryRechargeList(rechargeCustomize);
            form.setRecordList(rechargeCustomizes);

            modeAndView.addObject(RechargeExceptionDefine.RECHARGE_FORM, form);
        }
    }

    /**
     * 账户管理 查询条件
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(RechargeExceptionDefine.RECHARGE_LIST_WITHQ)
    @RequiresPermissions(RechargeExceptionDefine.PERMISSIONS_SEARCH)
    public ModelAndView initWithQ(HttpServletRequest request, RechargeExceptionBean form) {
        LogUtil.startLog(RechargeExceptionController.class.toString(), RechargeExceptionDefine.INIT);
        ModelAndView modeAndView = new ModelAndView(RechargeExceptionDefine.LIST_PATH);

        // 创建分页
        this.createPage(request, modeAndView, form);

        LogUtil.endLog(RechargeExceptionController.class.toString(), RechargeExceptionDefine.INIT);
        return modeAndView;
    }

    /**
     * 充值掉单修复操作
     *
     * @param request
     * @param form
     * @return
     */
    @ResponseBody
    @RequestMapping(RechargeExceptionDefine.RECHARGE_FIX)
    @RequiresPermissions(RechargeExceptionDefine.PERMISSIONS_RECHARGE_EXCEPTION)
    public String rechargeFix(HttpServletRequest request, @RequestBody RechargeExceptionBean form) {
        LogUtil.startLog(RechargeExceptionController.class.toString(), RechargeExceptionDefine.RECHARGE_FIX);
        JSONObject ret = new JSONObject();
        // 查询该条充值记录的数据库信息
        RechargeCustomize rechargeCustomize = this.rechargeExceptionService.queryRechargeById(form.getId());
        if (null != rechargeCustomize) {
            if ("1".equals(rechargeCustomize.getStatus())) {// 开始数据修复处理
                ChinapnrBean bean = new ChinapnrBean();
                // 构建请求参数
                bean.setVersion(ChinaPnrConstant.VERSION_10); // 版本号(必须)
                bean.setCmdId(ChinaPnrConstant.CMDID_QUERY_TRANS_DETAIL); // 消息类型(必须)
                bean.setMerCustId(PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID)); // 商户客户号
                bean.setOrdId(rechargeCustomize.getNid()); // 订单号
                bean.setQueryTransType("SAVE"); // 交易查询类型
                HashMap<String, String> resultMap;
                try {
                    // 执行修复
                    resultMap =
                            this.rechargeExceptionService.handleRechargeStatus(bean,
                                    rechargeCustomize.getUserId(), rechargeCustomize.getFeeFrom());
                    ret.put(RechargeExceptionDefine.JSON_STATUS_KEY,
                            resultMap.get(RechargeExceptionDefine.JSON_STATUS_KEY));
                    ret.put(RechargeExceptionDefine.JSON_RESULT_KEY,
                            resultMap.get(RechargeExceptionDefine.JSON_RESULT_KEY));
                } catch (Exception e) {
                    ret.put(RechargeExceptionDefine.JSON_STATUS_KEY, PushMoneyManageDefine.JSON_STATUS_NG);
                    ret.put(RechargeExceptionDefine.JSON_RESULT_KEY, "系统异常，请刷新后重试");
                    LogUtil.errorLog(RechargeExceptionController.class.toString(),
                            RechargeExceptionDefine.RECHARGE_FIX, e);
                }
            }
        } else {
            ret.put(RechargeExceptionDefine.JSON_STATUS_KEY, PushMoneyManageDefine.JSON_STATUS_NG);
            ret.put(RechargeExceptionDefine.JSON_RESULT_KEY, "异常纪录，请刷新后重试");
        }
        LogUtil.endLog(RechargeExceptionController.class.toString(), RechargeExceptionDefine.RECHARGE_FIX);
        return ret.toString();
    }
}
