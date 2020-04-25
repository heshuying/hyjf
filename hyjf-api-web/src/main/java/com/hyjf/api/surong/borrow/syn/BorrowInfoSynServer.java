package com.hyjf.api.surong.borrow.syn;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyjf.api.surong.borrow.status.BorrowInfoSynDefine;
import com.hyjf.api.surong.borrow.status.BorrowInfoSynParamBean;
import com.hyjf.api.surong.borrow.status.BorrowInfoSynResultBean;
import com.hyjf.api.surong.borrow.status.BorrowInfoSynService;
import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.auto.AccountBorrow;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.BorrowRepayPlan;
import com.hyjf.mybatis.model.customize.BorrowSynCustomize;

/**
 * 融东风-标的信息同步
 * @author zhangjinpeng
 *
 */
@Controller
@RequestMapping(BorrowInfoSynDefine.REQUEST_MAPPING)
public class BorrowInfoSynServer extends BaseController {

    Logger _log = LoggerFactory.getLogger(BorrowInfoSynServer.class);
    private static final String THIS_CLASS = BorrowInfoSynServer.class.getName();
    
    @Autowired
    BorrowInfoSynService borrowInfoSynService;
    /**
     * 标的信息同步
     * @param request
     * @param paramBean
     * @return
     */
    @ResponseBody
    @RequestMapping(value = BorrowInfoSynDefine.BORROW_INFO_SYN_MAPPING, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public BaseResultBean borrowInfoSyn(HttpServletRequest request,BorrowInfoSynParamBean paramBean){
        //_log.info(THIS_CLASS+"---标的信息同步开始。。。"+"标的编号："+paramBean.getBorrowNid());
        BorrowInfoSynResultBean resultBean = new BorrowInfoSynResultBean();
        //验签
        if(!this.checkSign(paramBean, BaseDefine.METHOD_BORROW_STATUS_SYN)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        if(StringUtils.isEmpty(paramBean.getBorrowNid())){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("借款编号不能为空");
            return resultBean;
        }
        BorrowSynCustomize borrow = borrowInfoSynService.getBorrow(paramBean.getBorrowNid());
        if(null == borrow){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("借款不存在");
            return resultBean;
        }
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc("标的信息同步成功");
        // 借款金额
        resultBean.setAccount(borrow.getAccount().toString());
        // 标的收益率
        resultBean.setBorrowApr(borrow.getBorrowApr().toString());
        // 标的编号
        resultBean.setBorrowNid(paramBean.getBorrowNid());
        // 借款期限
        resultBean.setBorrowPeriod(borrow.getBorrowPeriod());
        // 标的状态
        resultBean.setBorrowStatus(borrow.getStatus().toString());
        // 标的还款方式
        resultBean.setBorrowStyle(borrow.getBorrowStyle());
        // 标的服务费率
        resultBean.setServiceFeeRate(borrow.getServiceFeeRate());
        // 待还总额
        resultBean.setRepayAccountWait(borrow.getRepayAccountWait());
        // 待还总息
        resultBean.setRepayAccountInterestWait(borrow.getRepayAccountInterestWait());
        // 待还本金
        resultBean.setRepayAccountCapitalWait(borrow.getRepayAccountCapitalWait());
        
        AccountBorrow accountBorrow = borrowInfoSynService.getAccountBorrow(paramBean.getBorrowNid());
        if(null != accountBorrow){
            // 借贷人实际到账金额（本金-服务费）
            resultBean.setBorrowAccountReal(accountBorrow.getBalance());
        }

        BorrowApicron borrowApicron = borrowInfoSynService.getBorrowApicron(paramBean.getBorrowNid());
        if(borrowApicron != null){
            resultBean.setBorrowApicronStatus(borrowApicron.getStatus());
        }
        
        
        
        // 同步标的还款总信息
        BorrowRepay borrowRepay = this.borrowInfoSynService.getBorrowRepay(paramBean.getBorrowNid());
        if(null == borrowRepay){
            _log.info(THIS_CLASS+"---标的还款信息不存在。。。"+"标的编号："+paramBean.getBorrowNid());
            return resultBean;
        }else{
            resultBean.setBorrowRepay(borrowRepay);
        }
        //设置RepayAccountAll值
        if (null != borrowRepay.getRepayAccount() && null != borrow.getRepayFee()) {
            borrowRepay.setRepayAccountAll(borrowRepay.getRepayAccount().add(borrow.getRepayFee()));
        }
        // 同步标的还款计划
        List<BorrowRepayPlan> repayPlanList = this.borrowInfoSynService.getBorrowRepayPlan(paramBean.getBorrowNid());
        if(null == repayPlanList || repayPlanList.size()==0){
            // _log.info(THIS_CLASS+"---标的还款计划不存在。。。"+"标的编号："+paramBean.getBorrowNid());
        }else{
            /*List<BorrowRepayPlanResultBean> resultPlanList = new ArrayList<BorrowRepayPlanResultBean>();
            for(BorrowRepayPlan plan:repayPlanList){
                BorrowRepayPlanResultBean planBean = new BorrowRepayPlanResultBean();
                planBean.setBorrowNid(paramBean.getBorrowNid());
                if (null != plan.getRepayAccount() && null != plan.getRepayFee()) {
                    planBean.setRepayAccountAll(plan.getRepayAccount().add(plan.getRepayFee()));
                }
                planBean.setRepayPeriod(plan.getRepayPeriod());
                planBean.setRepayTime(plan.getRepayTime());
                planBean.setRepayStatus(plan.getRepayStatus());
                resultPlanList.add(plan);
            }*/
            for(BorrowRepayPlan plan:repayPlanList){
                plan.setRepayAccountAll(plan.getRepayAccount().add(plan.getRepayFee()));
            }
            
            resultBean.setRepayPlanList(repayPlanList);
        }
        
        
        // 一次性还款,不分期的标的
        // 按天计息，到期还本还息
        // 按月计息，到期还本还息
        if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrow.getBorrowStyle())
                || CustomConstants.BORROW_STYLE_END.equals(borrow.getBorrowStyle())) {
            // 总的还款期数
            resultBean.setRepayPeriodAll(1);
            
            if(borrowRepay.getRepayStatus() == 1){
                // 待还管理费
                resultBean.setRepayFeeWait(BigDecimal.ZERO);
            }else{
                // 待还管理费
                resultBean.setRepayFeeWait(borrow.getRepayFee());
            }
        }else{
            // 总的还款期数
            resultBean.setRepayPeriodAll(Integer.valueOf(borrow.getBorrowPeriod()));
            
            // 分期的标的
            if(borrowRepay.getRepayStatus() == 1){
                // 待还管理费
                resultBean.setRepayFeeWait(BigDecimal.ZERO);
            }else{
                // 待还管理费
                BigDecimal feeWait = this.borrowInfoSynService.getBorrowRepayFee(paramBean.getBorrowNid(), 0);
                resultBean.setRepayFeeWait(feeWait);
            }
        }
        
        //_log.info(THIS_CLASS+"---标的信息同步结束。。。"+"标的编号："+paramBean.getBorrowNid());
        return resultBean;
    }
}
