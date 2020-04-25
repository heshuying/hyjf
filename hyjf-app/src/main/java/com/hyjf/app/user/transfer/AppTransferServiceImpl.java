package com.hyjf.app.user.transfer;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import redis.clients.jedis.JedisPool;

import com.hyjf.app.BaseServiceImpl;
import com.hyjf.app.project.RepayPlanBean;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.calculate.InterestInfo;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.customize.app.AppTenderToCreditDetailCustomize;

/**
 * 
 * 债权转让Service
 * 
 * @author liuyang
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月28日
 * @see 下午5:20:17
 */
@Service
public class AppTransferServiceImpl extends BaseServiceImpl implements AppTransferService {

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	@Qualifier("appMsProcesser")
	private MessageProcesser appMsProcesser;

	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailProcesser;

	public static JedisPool pool = RedisUtils.getConnection();

	private static DecimalFormat DF_FOR_VIEW = new DecimalFormat("#,##0.00");
	private static String oldOrNewDate = "2016-12-27 20:00:00";

	   @Override
	    public AppTenderToCreditDetailCustomize selectCreditTenderDetail(String creditNid) {

	        Map<String, Object> params = new HashMap<String, Object>();
	        params.put("creditNid", creditNid);
	        List<AppTenderToCreditDetailCustomize> list = appTenderCreditCustomizeMapper.selectCreditTenderDetail(params);
	        AppTenderToCreditDetailCustomize appTenderToCreditDetailCustomize = null;
	        if (list != null && list.size() > 0) {
	            appTenderToCreditDetailCustomize = list.get(0);
	        } else {
	            return null;
	        }
	        return appTenderToCreditDetailCustomize;
	   }
	   /**
	     * 计算还款计划
	     * 
	     * @param borrowNid
	     * @return
	     */
	    @Override
	    public List<RepayPlanBean> getRepayPlan(String borrowNid) {
	        DecimalFormat df = CustomConstants.DF_FOR_VIEW;
	        Borrow borrow = this.getBorrowByNid(borrowNid);
	        String borrowStyle = borrow.getBorrowStyle();
	        Integer projectType = borrow.getProjectType();
	        BigDecimal yearRate = borrow.getBorrowApr();
	        Integer totalMonth = borrow.getBorrowPeriod();
	        BigDecimal account = borrow.getAccount();
	        Integer time = borrow.getBorrowSuccessTime();
	        if (time == null) {
	            time = (int) (System.currentTimeMillis() / 1000);
	        }
	        List<RepayPlanBean> repayPlans = new ArrayList<RepayPlanBean>();
	        // 月利率(算出管理费用[上限])
	        BigDecimal borrowMonthRate = Validator.isNull(borrow.getManageFeeRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getManageFeeRate());
	        // 月利率(算出管理费用[下限])
	        BigDecimal borrowManagerScaleEnd = Validator.isNull(borrow.getBorrowManagerScaleEnd()) ? BigDecimal.ZERO : new BigDecimal(borrow.getBorrowManagerScaleEnd());
	        // 差异费率
	        BigDecimal differentialRate = Validator.isNull(borrow.getDifferentialRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getDifferentialRate());
	        // 初审时间
	        int borrowVerifyTime = Validator.isNull(borrow.getVerifyTime()) ? 0 : Integer.parseInt(borrow.getVerifyTime());
	        // 按月计息到期还本还息和按天计息，到期还本还息
	        if (borrowStyle.equals(CalculatesUtil.STYLE_END) || borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {
	            InterestInfo info = CalculatesUtil.getInterestInfo(account, totalMonth, yearRate, borrowStyle, time, borrowMonthRate, borrowManagerScaleEnd, projectType, differentialRate,
	                    borrowVerifyTime);
	            if (info != null) {
	                String repayTime = "-";
	                // 通过复审
	                if (borrow.getReverifyStatus() == 6) {
	                    repayTime = GetDate.formatDate(GetDate.getDate(info.getRepayTime() * 1000L));
	                }
	                RepayPlanBean plan = new RepayPlanBean(repayTime, df.format(info.getRepayAccountCapital().add(info.getRepayAccountInterest())), "第1期");
	                repayPlans.add(plan);
	            }
	        } else {// 等额本息和等额本金和先息后本
	            InterestInfo info = CalculatesUtil.getInterestInfo(account, totalMonth, yearRate, borrowStyle, time, borrowMonthRate, borrowManagerScaleEnd, projectType, differentialRate,
	                    borrowVerifyTime);
	            if (info.getListMonthly() != null) {
	                String repayTime = "-";
	                for (int i = 0; i < info.getListMonthly().size(); i++) {
	                    InterestInfo sub = info.getListMonthly().get(i);
	                    // 通过复审
	                    if (borrow.getReverifyStatus() == 6) {
	                        repayTime = GetDate.formatDate(GetDate.getDate(sub.getRepayTime() * 1000L));
	                    }
	                    
	                    RepayPlanBean plan = new RepayPlanBean(repayTime, df.format(sub.getRepayAccount()), "第"+(i+1)+"期");
	                    repayPlans.add(plan);
	                }
	            }
	        }
	        return repayPlans;
	    }

	    
}
