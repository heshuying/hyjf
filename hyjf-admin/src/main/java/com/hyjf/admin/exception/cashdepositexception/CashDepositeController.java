package com.hyjf.admin.exception.cashdepositexception;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.manager.asset.assetlist.AssetListBean;
import com.hyjf.admin.manager.asset.assetlist.AssetListController;
import com.hyjf.admin.manager.asset.assetlist.AssetListService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.customize.AssetListCustomize;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lisheng
 * @version CashDepositeController, v0.1 2018/5/22 15:41
 */
@Controller
@RequestMapping(value = CashDepositeDefine.REQUEST_MAPPING)
public class CashDepositeController  {

    @Autowired
    private AssetListService assetListService;
    @Autowired
    private CashDepositeService cashDepositeService;
    Logger _log = LoggerFactory.getLogger(CashDepositeController.class);
    private static final String THIS_CLASS = CashDepositeController.class.getName();

    /**
     * 画面初始化
     * @param request
     * @return 进入资产列表页面
     */
    @RequestMapping(CashDepositeDefine.INIT_ACTION)
    public ModelAndView init(HttpServletRequest request, AssetListBean form) {
        LogUtil.startLog(AssetListController.class.toString(), CashDepositeDefine.INIT_ACTION);
        ModelAndView modelAndView = new ModelAndView(CashDepositeDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(AssetListController.class.toString(), CashDepositeDefine.INIT_ACTION);
        return modelAndView;
    }

    /**
     * 检索
     * @param request
     * @return 进入资产列表页面
     */
    @RequestMapping(CashDepositeDefine.SEARCH_ACTION)
    public ModelAndView search(HttpServletRequest request,  AssetListBean form) {
        LogUtil.startLog(AssetListController.class.toString(), CashDepositeDefine.SEARCH_ACTION);
        ModelAndView modelAndView = new ModelAndView(CashDepositeDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(AssetListController.class.toString(), CashDepositeDefine.SEARCH_ACTION);
        return modelAndView;
    }

    /**
     * 转跳到选择处理方式的页面
     * @param request
     * @param form 用户名
     */
    @RequestMapping(CashDepositeDefine.TO_CASH_ACTION)
    public ModelAndView toAddMobileSynch(HttpServletRequest request, AssetListBean form){
        ModelAndView modelAndView = new ModelAndView(CashDepositeDefine.TO_MODIFY_LIST);
        modelAndView.addObject(CashDepositeDefine.ASSET_LIST_FORM, form);
        return modelAndView;
    }

    /**
     * 根据选择的方式，处理标的（重新校验保证金，或者流标）
     * @param request
     * @param form
     * @param menuHide  1  重新验证保证金 0 流标
     * @return
     */
    @RequestMapping(CashDepositeDefine.MODIFY_ACTION)
    @ResponseBody
    private JSONObject modifyAction(HttpServletRequest request,AssetListBean form,String menuHide){
        JSONObject ret = new JSONObject();
        String assetId = form.getAssetId();
        String[] split = assetId.split(",");
        Boolean flag=true;
        for (String asid : split) {
            try {
                this.cashDepositeService.updateCashDepositeStatus(asid, menuHide);
            }catch (Exception e){
                _log.info(THIS_CLASS +  "==>" + "合作额度不足处理失败（资产编号assetId）："+asid+"处理方式（1重验，0 流标）："+menuHide);
                flag=false;
            }
        }
        if(flag){
            ret.put("success",true);
            ret.put("msg","合作额度不足处理成功");
        }else{
            ret.put("success",false);
            ret.put("msg","合作额度不足处理失败");
        }
        return ret;
    }

    /**
     * 创建权限维护分页机能
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, AssetListBean form) {
        // 资金来源   界面下拉框显示
        List<HjhInstConfig> hjhInstConfigList = this.assetListService.hjhInstConfigList("");
        modelAndView.addObject("hjhInstConfigList", hjhInstConfigList);
        // 封装查询条件
        Map<String, Object> conditionMap = setCondition(form);
        Integer count = this.assetListService.getRecordCount(conditionMap);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            List<AssetListCustomize> recordList = this.assetListService.getRecordList(conditionMap,
                    paginator.getOffset(), paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(recordList);
        }
        modelAndView.addObject(CashDepositeDefine.ASSET_LIST_FORM, form);
    }

    /**
     * 生成查询条件
     * @param form
     * @return
     */
    private Map<String, Object> setCondition(AssetListBean form) {
        String assetIdSrch = StringUtils.isNotEmpty(form.getAssetIdSrch()) ? form.getAssetIdSrch() : null;//资产编号
        String instCodeSrch = StringUtils.isNotEmpty(form.getInstCodeSrch()) ? form.getInstCodeSrch() : null;//资产来源
        String borrowNidSrch = StringUtils.isNotEmpty(form.getBorrowNidSrch()) ? form.getBorrowNidSrch() : null;//项目编号
        String planNidSrch = StringUtils.isNotEmpty(form.getPlanNidSrch()) ? form.getPlanNidSrch() : null;//计划编号
        String userNameSrch = StringUtils.isNotEmpty(form.getUserNameSrch()) ? form.getUserNameSrch() : null;//用户名

        Map<String, Object> conditionMap = new HashMap<String, Object>();
        conditionMap.put("assetIdSrch", assetIdSrch);
        conditionMap.put("instCodeSrch", instCodeSrch);
        conditionMap.put("borrowNidSrch", borrowNidSrch);
        conditionMap.put("planNidSrch", planNidSrch);
        conditionMap.put("userNameSrch", userNameSrch);
        // 1 保证金不足 21 合作额度不足 22 在贷余额额度不足 23 日推标额度不足 24 月推标额度不足
        conditionMap.put("statusSrch", new int[]{21,23,24});
        return conditionMap;
    }
}
