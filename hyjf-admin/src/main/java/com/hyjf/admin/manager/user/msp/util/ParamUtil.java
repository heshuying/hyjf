package com.hyjf.admin.manager.user.msp.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.mybatis.model.auto.MspApply;

public class ParamUtil {

    /**
     * 
     * 安融查询接口参数
     * @author sss
     * @param form
     * @return
     */
    public static Map<String, String> getQueryUserParm(MspApply form) {
        String customerName = form.getName();
        String paperNumber = form.getIdentityCard();
        String loanType = form.getLoanType();
        String creditAddress = form.getCreditAddress();
        BigDecimal loanMoney = form.getLoanMoney();
        Integer loanTimeLimit = form.getLoanTimeLimit();
        String applyDate = form.getApplyDate();
        String logUserId = ShiroUtil.getLoginUserId();
        
        Map<String, String> parm = new HashMap<String, String>();
        parm.put("customerName", customerName);
        parm.put("paperNumber", paperNumber);
        parm.put("loanType", loanType);
        parm.put("loanCreditCity", creditAddress);
        parm.put("creditAddress", creditAddress); 
        parm.put("loanMoney", loanMoney.toString());
        parm.put("loanTimeLimit", loanTimeLimit+"");
        parm.put("applyDate", applyDate);
        parm.put("logUserId", logUserId);
        return parm;
    }

    /**
     * 
     * 安融共享 接口参数
     * @author sss
     * @param form
     * @return
     */
    public static Map<String, String> getSendParm(MspApply form) {
        String logUserId = ShiroUtil.getLoginUserId();
        
        Map<String, String> parm = new HashMap<String, String>();
        parm.put("customerName", form.getName());
        parm.put("paperNumber", form.getIdentityCard());
        parm.put("loanType", form.getLoanType());
     //   parm.put("creditAddress", form.getCreditAddress());
      
//        parm.put("applyLoanMoney", form.getLoanMoney()==null?"":form.getLoanMoney().toString());
   //     parm.put("loanTimeLimit", form.getLoanTimeLimit()+"");
 //       parm.put("applyDate", form.getApplyDate());
        parm.put("logUserId", logUserId);
        parm.put("bizType", form.getServiceType());
        
        parm.put("loanId", form.getApplyId());
        parm.put("checkResultTime", form.getApprovalDate());
        parm.put("checkResult", form.getApprovalResult());
        
        if(form.getApprovalResult().equals("01")) {
        	parm.put("loanMoney", form.getLoanMoney()==null?"":form.getLoanMoney().toString());
            parm.put("loanStartDate", form.getContractBegin());
            parm.put("loanEndDate", form.getContractEnd());
            parm.put("loanCreditCity", form.getCreditAddress());
            parm.put("loanAssureType", form.getGuaranteeType());
            parm.put("loanPeriods", form.getLoanTimeLimit()+"");
            // 债权信息
            parm.put("nbsMoney", form.getUnredeemedMoney()==null?"":form.getUnredeemedMoney().toString());
            parm.put("state", form.getRepaymentStatus());
            parm.put("nbMoney", form.getOverdueAmount()==null?"":form.getOverdueAmount().toString());
            parm.put("overdueStartDate", form.getOverdueDate());
            parm.put("overdueDays", form.getOverdueLength());
            parm.put("overdueReason", form.getOverdueReason());
        }

        
        return parm;
    }
}
