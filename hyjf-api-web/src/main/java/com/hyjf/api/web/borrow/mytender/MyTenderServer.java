package com.hyjf.api.web.borrow.mytender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseBean;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.borrow.mytender.MyTenderDefine;
import com.hyjf.borrow.mytender.MyTenderListResultBean;
import com.hyjf.borrow.mytender.MyTenderService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.customize.app.AppAlreadyRepayListCustomize;
import com.hyjf.mybatis.model.customize.app.AppInvestListCustomize;
import com.hyjf.mybatis.model.customize.app.AppRepayListCustomize;

@Controller
@RequestMapping(value = MyTenderDefine.REQUEST_MAPPING)
public class MyTenderServer extends BaseController {

    @Autowired
    private MyTenderService investProjectService;

    /**
     * 查询用户的出借项目列表还款中
     * 
     * @param form
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = MyTenderDefine.REPAY_LIST_ACTION, produces = "application/json; charset=utf-8")
    public MyTenderListResultBean searchRepayList(@ModelAttribute BaseBean form, HttpServletRequest request,
        HttpServletResponse response) {
        
        LogUtil.startLog(MyTenderServer.class.getName(), MyTenderDefine.REPAY_LIST_ACTION);
        MyTenderListResultBean resultBean = new MyTenderListResultBean();
        //验证请求参数
        if (Validator.isNull(form.getUserId())) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
        
        String host = Validator.isNull(request.getParameter("host"))?"":request.getParameter("host");
        String hostContact = Validator.isNull(request.getParameter("hostContact"))?"":request.getParameter("hostContact");
        
        //验签
        if(!this.checkSign(form, BaseDefine.METHOD_MYTENDER_REPAY_LIST)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }

        
        this.createRepayListPage(form, resultBean, host, hostContact);
        
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);

        LogUtil.endLog(MyTenderServer.class.getName(), MyTenderDefine.REPAY_LIST_ACTION);
        return resultBean;
    }

    /**
     * 查询还款中的出借项目列表
     * 
     * @param info
     * @param form
     */
    private void createRepayListPage(BaseBean form, MyTenderListResultBean resultBean, String host, String hostContact) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", form.getUserId());
        params.put("hostContact", hostContact);
        params.put("host", host);

        // 统计相应的用户出借项目总数
        int recordTotal = this.investProjectService.countRepayListRecordTotal(params);
        if (recordTotal > 0) {

            // 查询相应的汇直投列表数据
            int limit = form.getPageSize();
            int page = form.getPage();
            int offSet = (page - 1) * limit;
            if (offSet == 0 || offSet > 0) {
                params.put("limitStart", offSet);
            }
            if (limit > 0) {
                params.put("limitEnd", limit);
            }
            List<AppRepayListCustomize> recordList = investProjectService.selectRepayList(params);
            resultBean.setData(recordList);
            resultBean.setProjectTotal(String.valueOf(recordTotal));
        } else {
            resultBean.setData(new ArrayList<AppRepayListCustomize>());
            resultBean.setProjectTotal("0");
        }
    }

    /**
     * 查询用户的出借项目列表出借中
     * 
     * @param form
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = MyTenderDefine.INVEST_LIST_ACTION, produces = "application/json; charset=utf-8")
    public MyTenderListResultBean searchInvestList(@ModelAttribute BaseBean form, HttpServletRequest request,
        HttpServletResponse response) {

        LogUtil.startLog(MyTenderServer.class.getName(), MyTenderDefine.INVEST_LIST_ACTION);
        MyTenderListResultBean resultBean = new MyTenderListResultBean();
        //验证请求参数
        if (Validator.isNull(form.getUserId())) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
        
        String host = Validator.isNull(request.getParameter("host"))?"":request.getParameter("host");
        
        //验签
        if(!this.checkSign(form, BaseDefine.METHOD_MYTENDER_INVEST_LIST)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        
        this.createInvestListPage(form, resultBean, host);
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        LogUtil.endLog(MyTenderServer.class.getName(), MyTenderDefine.INVEST_LIST_ACTION);
        return resultBean;
    }

    /**
     * 查询用户的出借项目列表出借中
     * 
     * @param info
     * @param form
     */
    private void createInvestListPage(BaseBean form, MyTenderListResultBean resultBean, String host) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", form.getUserId());
        params.put("host", host);
        
        // 统计相应的用户出借项目总数
        int recordTotal = this.investProjectService.countInvestListRecordTotal(params);
        if (recordTotal > 0) {
            // 查询相应的汇直投列表数据
            int limit = form.getPageSize();
            int page = form.getPage();
            int offSet = (page - 1) * limit;
            if (offSet == 0 || offSet > 0) {
                params.put("limitStart", offSet);
            }
            if (limit > 0) {
                params.put("limitEnd", limit);
            }
            // 查询相应的用户出借项目列表
            List<AppInvestListCustomize> recordList = investProjectService.selectInvestList(params);
            resultBean.setData(recordList);
            resultBean.setProjectTotal(String.valueOf(recordTotal));
        } else {
            resultBean.setData(new ArrayList<AppInvestListCustomize>());
            resultBean.setProjectTotal("0");
        }
    }

    /**
     * 查询用户的出借项目列表已回款
     * 
     * @param form
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = MyTenderDefine.ALREADY_REPAY_LIST_ACTION, produces = "application/json; charset=utf-8")
    public MyTenderListResultBean searchAlreadyRepayList(@ModelAttribute BaseBean form, HttpServletRequest request,
        HttpServletResponse response) {

        LogUtil.startLog(MyTenderServer.class.getName(), MyTenderDefine.ALREADY_REPAY_LIST_ACTION);
        MyTenderListResultBean resultBean = new MyTenderListResultBean();
        //验证请求参数
        if (Validator.isNull(form.getUserId())) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
        
        String host = Validator.isNull(request.getParameter("host"))?"":request.getParameter("host");
        
        //验签
        if(!this.checkSign(form, BaseDefine.METHOD_MYTENDER_REPAYED_LIST)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        
        this.createAlreadyRepayListPage(form, resultBean, host);
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        LogUtil.endLog(MyTenderServer.class.getName(), MyTenderDefine.ALREADY_REPAY_LIST_ACTION);
        return resultBean;
    }

    /**
     * 查询用户的出借项目列表
     * 
     * @param request
     * @param info
     * @param form
     */
    private void createAlreadyRepayListPage(BaseBean form, MyTenderListResultBean resultBean, String host) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", form.getUserId());
        params.put("host", host);
        
        // 统计相应的用户出借项目总数
        int recordTotal = this.investProjectService.countAlreadyRepayListRecordTotal(params);
        if (recordTotal > 0) {

            // 查询相应的汇直投列表数据
            int limit = form.getPageSize();
            int page = form.getPage();
            int offSet = (page - 1) * limit;
            if (offSet == 0 || offSet > 0) {
                params.put("limitStart", offSet);
            }
            if (limit > 0) {
                params.put("limitEnd", limit);
            }
            // 查询相应的用户出借项目列表
            List<AppAlreadyRepayListCustomize> recordList = investProjectService.selectAlreadyRepayList(params);
            resultBean.setData(recordList);
            resultBean.setProjectTotal(String.valueOf(recordTotal));
        } else {
            resultBean.setData(new ArrayList<AppAlreadyRepayListCustomize>());
            resultBean.setProjectTotal("0");
        }
    }
    

}
