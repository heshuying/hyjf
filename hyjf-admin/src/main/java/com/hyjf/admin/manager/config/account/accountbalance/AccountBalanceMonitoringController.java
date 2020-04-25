package com.hyjf.admin.manager.config.account.accountbalance;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.auto.MerchantAccount;

/**
 * 
 * 余额监控Controller
 * @author liuyang
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月6日
 * @see 下午5:41:52
 */
@Controller
@RequestMapping(value = AccountBalanceMonitoringDefine.REQUEST_MAPPING)
public class AccountBalanceMonitoringController extends BaseController {

    @Autowired
    private AccountBalanceMonitoringService accountBalanceMonitoringService;

    /**
     * 
     * 余额监控画面初期化
     * @author liuyang
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AccountBalanceMonitoringDefine.INIT)
    @RequiresPermissions(AccountBalanceMonitoringDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, AccountBalanceMonitoringBean form) {
        LogUtil.startLog(AccountBalanceMonitoringDefine.THIS_CLASS, AccountBalanceMonitoringDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(AccountBalanceMonitoringDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        // 子账户类型
        modelAndView.addObject("subaccountType",
                this.accountBalanceMonitoringService.getParamNameList(CustomConstants.SUB_ACCOUNT_CLASS));
        LogUtil.endLog(AccountBalanceMonitoringDefine.THIS_CLASS, AccountBalanceMonitoringDefine.INIT);
        return modelAndView;
    }

    /**
     * 
     * 余额监控检索
     * @author liuyang
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AccountBalanceMonitoringDefine.SEARCH_ACTION)
    @RequiresPermissions(AccountBalanceMonitoringDefine.PERMISSIONS_SEARCH)
    public ModelAndView search(HttpServletRequest request, AccountBalanceMonitoringBean form) {
        LogUtil.startLog(AccountBalanceMonitoringDefine.THIS_CLASS, AccountBalanceMonitoringDefine.SEARCH_ACTION);
        ModelAndView modelAndView = new ModelAndView(AccountBalanceMonitoringDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        // 子账户类型
        modelAndView.addObject("subaccountType",
                this.accountBalanceMonitoringService.getParamNameList(CustomConstants.SUB_ACCOUNT_CLASS));
        LogUtil.endLog(AccountBalanceMonitoringDefine.THIS_CLASS, AccountBalanceMonitoringDefine.SEARCH_ACTION);
        return modelAndView;
    }

    /**
     * 
     * 画面迁移
     * @author liuyang
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AccountBalanceMonitoringDefine.INFO_ACTION)
    public ModelAndView info(HttpServletRequest request, AccountBalanceMonitoringBean form) {
        LogUtil.startLog(AccountBalanceMonitoringDefine.THIS_CLASS, AccountBalanceMonitoringDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(AccountBalanceMonitoringDefine.INFO_PATH);
        List<MerchantAccount> recordList = this.accountBalanceMonitoringService.searchAllSubAccountList(form, -1, -1);
        form.setAccountBalanceList(this.forback(recordList));
        modelAndView.addObject(AccountBalanceMonitoringDefine.ACCOUNT_BALANCE_FORM, form);
        LogUtil.endLog(AccountBalanceMonitoringDefine.THIS_CLASS, AccountBalanceMonitoringDefine.INFO_ACTION);
        return modelAndView;
    }

    /**
     * 
     * 更新
     * @author liuyang
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AccountBalanceMonitoringDefine.UPDATE_ACTION)
    @RequiresPermissions(AccountBalanceMonitoringDefine.PERMISSIONS_MODIFY)
    public String updateAction(HttpServletRequest request, AccountBalanceMonitoringBean form) {
        LogUtil.startLog(AccountBalanceMonitoringDefine.THIS_CLASS, AccountBalanceMonitoringDefine.UPDATE_ACTION);
        ModelAndView modelAndView = new ModelAndView(AccountBalanceMonitoringDefine.INFO_PATH);
        // 画面传来的值
        List<SubAccountListResult> updateDataList = this.setPageListInfo(modelAndView, form);
        // 数据库检索的数据
        List<MerchantAccount> list = this.accountBalanceMonitoringService.searchSubAccountList(form, -1, -1);

        List<SubAccountListResult> resultList = this.forback(list);
        // 判断数据是否更新
        List<SubAccountListResult> updateList = this.compareResultList(resultList, updateDataList);
        // 数据更新
        this.accountBalanceMonitoringService.updateSubAccountList(updateList);
        LogUtil.endLog(AccountBalanceMonitoringDefine.THIS_CLASS, AccountBalanceMonitoringDefine.UPDATE_ACTION);
        return "redirect:" + AccountBalanceMonitoringDefine.REQUEST_MAPPING + "/"
                + AccountBalanceMonitoringDefine.SEARCH_ACTION;
    }

    /**
     * 余额监控分页技能
     * @author liuyang
     * @param request
     * @param modeAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, AccountBalanceMonitoringBean form) {
        int total = this.accountBalanceMonitoringService.countSubAccountList(form);
        if (total > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), total);
            List<MerchantAccount> recordList =
                    this.accountBalanceMonitoringService.searchSubAccountList(form, paginator.getOffset(),
                            paginator.getLimit());
            List<SubAccountListResult> result = this.forback(recordList);
            form.setAccountBalanceList(result);
            form.setPaginator(paginator);
        }
        modelAndView.addObject(AccountBalanceMonitoringDefine.ACCOUNT_BALANCE_FORM, form);
    }

    /**
     * 
     * 画面项目反馈
     * @author liuyang
     * @param recordList
     * @return
     */
    private List<SubAccountListResult> forback(List<MerchantAccount> recordList) {
        List<SubAccountListResult> result = new ArrayList<SubAccountListResult>();
        for (int i = 0; i < recordList.size(); i++) {
            SubAccountListResult record = new SubAccountListResult();
            BeanUtils.copyProperties(recordList.get(i), record);
            record.setIds(String.valueOf(recordList.get(i).getId()));
            result.add(record);
        }
        return result;
    }

    /**
     * 
     * 判断数据是否有变化
     * @author liuyang
     * @param oldList
     * @param newList
     */
    private List<SubAccountListResult> compareResultList(List<SubAccountListResult> oldList,
        List<SubAccountListResult> newList) {
        List<SubAccountListResult> resultList = new ArrayList<SubAccountListResult>();
        for (int i = 0; i < oldList.size(); i++) {
            SubAccountListResult oldRecord = oldList.get(i);
            SubAccountListResult newRecord = newList.get(i);
            // 判断数据是否有更新
            if (oldRecord.getIds().equals(newRecord.getIds())) {
                Long oldData =
                        oldRecord.getBalanceLowerLimit() == null ? new Long(0) : oldRecord.getBalanceLowerLimit();
                Long newData =
                        newRecord.getBalanceLowerLimit() == null ? new Long(0) : newRecord.getBalanceLowerLimit();
                // 数据没有变更的情况
                if (oldData.equals(newData)
                        && oldRecord.getAutoTransferInto() == newRecord.getAutoTransferInto()
                        && oldRecord.getAutoTransferOut() == newRecord.getAutoTransferOut()
                        && (oldRecord.getTransferIntoRatio() == null ? 0 : oldRecord.getTransferIntoRatio()) == (newRecord
                                .getTransferIntoRatio() == null ? 0 : newRecord.getTransferIntoRatio())) {
                    newRecord.setUpdateFlg(false);
                } else {
                    // 数据变更的情况
                    newRecord.setUpdateFlg(true);
                }
            }
            resultList.add(newRecord);
        }

        return resultList;
    }

    /**
     * 
     * json数据转换成实体
     * @author liuyang
     * @param modelAndView
     * @param form
     * @return
     */
    public List<SubAccountListResult> setPageListInfo(ModelAndView modelAndView, AccountBalanceMonitoringBean form) {
        List<SubAccountListResult> subAccountList = new ArrayList<SubAccountListResult>();
        subAccountList = JSONArray.parseArray(form.getBalanceDataJson(), SubAccountListResult.class);
        for (SubAccountListResult subAccountListResult : subAccountList) {
            subAccountListResult.setId(Integer.parseInt(subAccountListResult.getIds()));
        }
        return subAccountList;
    }

}
