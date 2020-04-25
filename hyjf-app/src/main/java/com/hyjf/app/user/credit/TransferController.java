package com.hyjf.app.user.credit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.project.ProjectService;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.app.AppProjectDetailCustomize;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fuqiang
 */
@Controller
public class TransferController {

    private final String TOKEN_ISINVALID_STATUS = "Token失效，请重新登录";

    Logger logger = LoggerFactory.getLogger(AppTenderCreditBorrowController.class);

    @Autowired
    private AppTenderCreditService appTenderCreditService;
    //法大大
    @Autowired
    private FddGenerateContractService fddGenerateContractService;

    @Autowired
    private ProjectService projectService;

    private static DecimalFormat DF_FOR_VIEW = new DecimalFormat("#,##0.00");


    /**
     * 获取债转详情
     * @param transferId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AppTenderCreditDefine.TRANSFER_MAPPING + AppTenderCreditDefine.TRANSFER_ACTION)
    public JSONObject searchTenderCreditDetail(@PathVariable("transferId") String transferId, HttpServletRequest request) {
        LogUtil.startLog(AppTenderCreditDefine.TRANSFER_CLASS, AppTenderCreditDefine.TRANSFER_ACTION);
        JSONObject jsonObject = new JSONObject();
        String token = request.getParameter("token");
        String sign = request.getParameter("sign");
        BorrowCredit credit = appTenderCreditService.selectCreditTenderByCreditNid(transferId);
        // 原出借订单号
        String orderId = credit.getTenderNid();
        // 原标id
        String borrowNid = credit.getBidNid();
        AppProjectDetailCustomize borrow = projectService.selectProjectDetail(borrowNid);

        /**
         * 验证用户是否登录
         */
        if (StringUtils.isEmpty(token)) {
            jsonObject.put("status", AppTenderCreditBorrowDefine.FAIL);
            jsonObject.put("statusDesc", AppTenderCreditBorrowDefine.FAIL_MSG);
            jsonObject.put("projectName",borrow==null?"":borrow.getBorrowName());
            jsonObject.put("projectDetail", new ArrayList<>());
            jsonObject.put("repayPlan", new ArrayList<>());
            jsonObject.put("transferInfo", null);
            jsonObject.put("creditStatus", null);
            return jsonObject;
        }

        /**
         * 如果标不存在，则返回
         */
        if(borrow == null){
            jsonObject.put("status", AppTenderCreditBorrowDefine.FAIL);
            jsonObject.put("statusDesc", AppTenderCreditBorrowDefine.FAIL_BORROW_MSG);
            jsonObject.put("projectName","");
            jsonObject.put("projectDetail", new ArrayList<>());
            jsonObject.put("repayPlan", new ArrayList<>());
            jsonObject.put("transferInfo", null);
            jsonObject.put("creditStatus", null);
            return jsonObject;
        }

        Integer userId = null;
        try {
            userId = SecretUtil.getUserId(sign);
        } catch (Exception e2) {
            logger.error("用户sign异常，sign:"+sign,e2);
            jsonObject.put("status", AppTenderCreditBorrowDefine.FAIL);
            jsonObject.put("statusDesc", "用户sign异常，sign:"+sign);
            jsonObject.put("projectName","");
            jsonObject.put("projectDetail", new ArrayList<>());
            jsonObject.put("repayPlan", new ArrayList<>());
            jsonObject.put("transferInfo", null);
            jsonObject.put("creditStatus", null);
            return jsonObject;
        }

        jsonObject.put("projectName", borrow.getBorrowNid());

        BorrowTender borrowTender = projectService.selectBorrowTenderByNid(orderId);

        List<TenderProjectDetailBean> detailBeansList = new ArrayList<>();

        List<TenderCreditBorrowBean> borrowBeansList = new ArrayList<>();
        preckCredit(borrowBeansList, "历史年回报率", borrow.getBorrowApr()+"%");
        if("endday".equals(borrow.getBorrowStyle())){
            preckCredit(borrowBeansList, "项目期限", borrow.getBorrowPeriod() + "天");
        }else{
            preckCredit(borrowBeansList, "项目期限", borrow.getBorrowPeriod() + "个月");
        }
        preckCredit(borrowBeansList, "回款方式", borrow.getRepayStyle());

        preck(detailBeansList, "资产信息", borrowBeansList);

        if(borrowTender != null ){

            List<TenderCreditBorrowBean> borrowBeansList1 = new ArrayList<>();
            preckCredit(borrowBeansList1, "出借本金", DF_FOR_VIEW.format(borrowTender.getAccount()) + "元");
            preckCredit(borrowBeansList1, "已收本息", DF_FOR_VIEW.format(borrowTender.getRecoverAccountYes()) + "元");
            preckCredit(borrowBeansList1, "待收本金", DF_FOR_VIEW.format(borrowTender.getRecoverAccountCapitalWait()) + "元");
            preckCredit(borrowBeansList1, "待收本息", DF_FOR_VIEW.format(borrowTender.getRecoverAccountInterestWait()) + "元");
            if(borrowTender.getAddtime() != null){
                String strDate = GetDate.timestamptoStrYYYYMMDDHHMM(String.valueOf(borrowTender.getAddtime()));
                logger.info("出借时间:"+strDate);
                preckCredit(borrowBeansList1, "出借时间", strDate);
            }else{
                preckCredit(borrowBeansList1, "出借时间", "");
            }

            preck(detailBeansList, "出借信息", borrowBeansList1);
        }else {
            preck(detailBeansList, "出借信息", new ArrayList<TenderCreditBorrowBean>());
        }


        jsonObject.put("projectDetail", detailBeansList);
        if(credit.getCreditCapital().compareTo(credit.getCreditCapitalAssigned())>0){
            if(CommonUtils.isStageRepay(borrow.getBorrowStyle())){
                // 3.回款计划(本金出借 - 分期)
                this.setRepayPlanByStagesToResult(jsonObject, orderId);
            } else {
                // 3.回款计划(本金出借 - 不分期)
                this.setRepayPlanToResult(jsonObject, orderId);
            }
        }
        List<TenderAgreement> tenderAgreementsNid= fddGenerateContractService.selectByExample(orderId+"");//债转协议
        if(tenderAgreementsNid!=null && tenderAgreementsNid.size()>0){
            TenderAgreement tenderAgreement = tenderAgreementsNid.get(0);
            Integer fddStatus = tenderAgreement.getStatus();
            //法大大协议生成状态：0:初始,1:成功,2:失败，3下载成功
            //System.out.println("******************1法大大协议状态："+tenderAgreement.getStatus());
            if(fddStatus.equals(3)){
                jsonObject.put("fddStatus", 1);
            }else {
                //隐藏下载按钮
                //System.out.println("******************2法大大协议状态：0");
                jsonObject.put("fddStatus", 0);
            }
        }else {
            //下载老版本协议
            //System.out.println("******************3法大大协议状态：2");
            jsonObject.put("fddStatus", 1);;
        }
        // 4. 转让信息
        if (StringUtils.isNotBlank(orderId)) {
            List<BorrowCredit> borrowCreditList = projectService.getBorrowList(orderId,userId);
            JSONArray jsonArray = new JSONArray();
            for (BorrowCredit borrowCredit : borrowCreditList) {
            	JSONObject js = new JSONObject();
                Integer creditNid = borrowCredit.getCreditNid();
            	js.put("date", GetDate.times10toStrYYYYMMDD(borrowCredit.getAddTime()));
            	js.put("transferPrice", CommonUtils.formatAmount(borrowCredit.getCreditCapital()));
            	js.put("discount", CommonUtils.formatAmount(borrowCredit.getCreditDiscount()));
            	js.put("remainTime", borrowCredit.getCreditTerm());
            	js.put("realAmount", CommonUtils.formatAmount(borrowCredit.getCreditPrice()));
            	js.put("serviceCharge", CommonUtils.formatAmount(this.projectService.getCreditTender(String.valueOf(creditNid))));
            	js.put("hadTransfer", CommonUtils.formatAmount(borrowCredit.getCreditCapitalAssigned()));
            	jsonArray.add(js);
			}
            jsonObject.put("transferInfo", jsonArray);
        } else {
            jsonObject.put("transferInfo", null);
        }

        Integer status = credit.getCreditStatus();
        if (status == 0) {
            jsonObject.put("creditStatus", "转让中");
        } else if (credit.getCreditCapitalAssigned().compareTo(credit.getCreditCapital()) == 0) {
            jsonObject.put("creditStatus", "已完成");
        } else {
            jsonObject.put("creditStatus", "已结束");
        }

        jsonObject.put("status", AppTenderCreditDefine.SUCCESS);
        jsonObject.put("statusDesc", AppTenderCreditDefine.SUCCESS_MSG);
        // 获取债转详细的参数
        LogUtil.endLog(AppTenderCreditDefine.TRANSFER_CLASS, AppTenderCreditDefine.TRANSFER_ACTION);
        return jsonObject;
    }

    /**
     * 出借还款计划 --不分期
     * @param jsonObject
     * @param tenderNid
     */
    private void setRepayPlanToResult(JSONObject jsonObject, String tenderNid) {
        BorrowRecover borrowRecover = projectService.selectBorrowRecoverByNid(tenderNid);
        JSONArray jsonArray = new JSONArray();
        if (borrowRecover != null) {
            JSONObject js = new JSONObject();
            js.put("time", GetDate.times10toStrYYYYMMDD(Integer.parseInt(borrowRecover.getRecoverTime())));
            js.put("number", "1");
            js.put("account", DF_FOR_VIEW.format(borrowRecover.getRecoverAccount()));
            if (borrowRecover.getRecoverStatus() == 0) {
                js.put("status", "未回款");
            } else {
                js.put("status", "已回款");
            }
            jsonArray.add(js);
        }
        jsonObject.put("repayPlan", jsonArray);
    }

    /**
     * 出借还款计划 --分期
     * @param jsonObject
     * @param tenderNid
     */
    private void setRepayPlanByStagesToResult(JSONObject jsonObject, String tenderNid) {
        List<BorrowRecoverPlan> recoverPlanList = projectService.selectBorrowRecoverPlanByNid(tenderNid);
        JSONArray jsonArray = new JSONArray();
        if (!CollectionUtils.isEmpty(recoverPlanList)) {
            for (BorrowRecoverPlan plan : recoverPlanList) {
                JSONObject js = new JSONObject();
                js.put("time", GetDate.times10toStrYYYYMMDD(Integer.parseInt(plan.getRecoverTime())));
                js.put("number", plan.getRecoverPeriod());
                js.put("account", DF_FOR_VIEW.format(plan.getRecoverAccount()));
                if (plan.getRecoverStatus() == 0) {
                    js.put("status", "未回款");
                } else {
                    js.put("status", "已回款");
                }
                jsonArray.add(js);
            }
        }
        jsonObject.put("repayPlan", jsonArray);
    }

    private void preck(List<TenderProjectDetailBean> jsonObject,String keyName,List<TenderCreditBorrowBean> msg){
        TenderProjectDetailBean detailBean = new TenderProjectDetailBean();
        detailBean.setId("");
        detailBean.setTitle(keyName);
        detailBean.setMsg(msg);
        jsonObject.add(detailBean);
    }

    /**
     * 封装TenderCreditBorrowBean对象，放入list中
     * @param borrowBeansList
     * @param key 字段名
     * @param val 字段值
     */
    private void preckCredit(List<TenderCreditBorrowBean> borrowBeansList,String key, String val){
        if(!StringUtils.isEmpty(key)){
            TenderCreditBorrowBean borrowBean = new TenderCreditBorrowBean();
            borrowBean.setId("");
            borrowBean.setKey(key);
            borrowBean.setVal(val);
            borrowBeansList.add(borrowBean);
        }
    }

}
