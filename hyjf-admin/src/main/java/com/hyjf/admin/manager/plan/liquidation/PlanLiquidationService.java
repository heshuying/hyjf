package com.hyjf.admin.manager.plan.liquidation;

import java.math.BigDecimal;
import java.util.List;

import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.DebtDetail;
import com.hyjf.mybatis.model.auto.DebtFreeze;
import com.hyjf.mybatis.model.auto.DebtInvest;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;
import com.hyjf.mybatis.model.customize.PlanCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtCreditCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtPlanCustomize;

public interface PlanLiquidationService {

    /**
     * 
     * 获取锁定中和清算中的计划
     * @author renxingchen
     * @param planCommonCustomize
     * @return
     */
    public List<DebtPlan> selectPlanLiquidationList(PlanCommonCustomize planCommonCustomize);

    /**
     * 
     * 计算锁定中和清算中的计划的数据总和
     * @author renxingchen
     * @param planCommonCustomize
     * @return
     */
    public int countPlanLiquidation(PlanCommonCustomize planCommonCustomize);

    /**
     * 
     * 计划列表导出
     * @author renxingchen
     * @param planCommonCustomize
     * @return
     */
    public List<DebtPlan> exportPlanLiquidationList(PlanCommonCustomize planCommonCustomize);

    /**
     * 
     * 根据计划编号查询计划清算详情
     * @author renxingchen
     * @param planNid
     * @return
     */
    DebtPlanCustomize selectPlanLanLiquidationDetail(String planNid);

    /**
     * 
     * 查询计划出借列表
     * @author renxingchen
     * @param planCommonCustomize
     * @return
     */
    public List<DebtInvest> selectPlanInvestList(String planNid, Integer status);

    /**
     * 
     * 查询汇添金出借冻结记录
     * @author renxingchen
     * @param trxId
     * @param accedeOrderId
     * @return
     */
    public DebtFreeze selectDebtFreeze(String trxId, String accedeOrderId, Integer type);

    /**
     * 
     * 解冻汇添金出借订单
     * @author renxingchen
     * @param investUserId
     * @param orderId
     * @param trxId
     * @param orderDate
     * @param unfreezeOrderId
     * @param unfreezeOrderDate
     * @return
     */
    public boolean unFreezeOrder(Integer investUserId, String orderId, String trxId, String orderDate,
        String unfreezeOrderId, String unfreezeOrderDate) throws Exception;

    /**
     * 
     * 更新解冻后的交易记录
     * @author renxingchen
     * @param debtFreeze
     * @return
     */
    public boolean updateDebtAccountList(DebtFreeze debtFreeze) throws Exception;

    /**
     * 
     * 查询计划加入订单
     * @author renxingchen
     * @param planOrderId
     * @return
     */
    public DebtPlanAccede selectPlanAccede(String planOrderId);

    /**
     * 
     * 获取用户的汇付账户信息
     * @author renxingchen
     * @param userId
     * @return
     */
    public AccountChinapnr getAccountChinapnr(Integer userId);

    /**
     * 
     * 写入冻结临时日志
     * @author renxingchen
     * @param planNid
     * @param frzzeOrderId
     * @param frzzeOrderDate
     * @param userId
     * @param accedeBalance
     * @param tenderUsrcustid
     * @return
     */
    public Boolean updateBeforeChinaPnR(String planNid, String frzzeOrderId, String frzzeOrderDate, Integer userId,
        BigDecimal accedeBalance, String tenderUsrcustid);

    /**
     * 
     * 冻结
     * @author renxingchen
     * @param userId
     * @param accedeBalance
     * @param tenderUsrcustid
     * @param frzzeOrderId
     * @param frzzeOrderDate
     */
    public String freeze(Integer userId, BigDecimal accedeBalance, String tenderUsrcustid, String frzzeOrderId,
        String frzzeOrderDate);

    /**
     * 
     * 插入冻结日志
     * @author renxingchen
     * @param planNid
     * @param frzzeOrderId
     * @param frzzeOrderDate
     * @param userId
     * @param accedeBalance
     * @param tenderUsrcustid
     * @param client
     * @param trxIdFreeze
     * @param planOrderId
     * @param planAccede 
     * @param string 
     * @return
     * @throws Exception 
     */
    public Boolean updatefreezeLog(String planNid, String frzzeOrderId, String frzzeOrderDate, Integer userId,
        BigDecimal accedeBalance, String tenderUsrcustid, Integer client, String trxIdFreeze, String planOrderId, DebtPlanAccede planAccede, String borrowNid) throws Exception;

    /**
     * 
     * 更新汇添金加入计划
     * @author renxingchen
     * @param accedeBalance
     * @return
     */
    public boolean updateDebtPlanAccede(DebtPlanAccede accedeBalance);

    /**
     * 
     * 更新汇添金计划
     * @author renxingchen
     * @param debtPlan
     * @return
     */
    public boolean updateDebtPlan(DebtPlan debtPlan);

    /**
     * 
     * 债权清算
     * @author renxingchen
     * @param planNidSrch
     * @param actualApr
     * @param liquidationShouldTime
     * @return
     */
    public boolean updateLiquidation(String planNid, BigDecimal actualApr, Integer liquidationShouldTime);

    /**
     * 
     * 获取指定计划的债权详情
     * @author renxingchen
     * @param debtPlanNid
     * @return
     */
    public List<DebtDetail> selectDebtDetail(String debtPlanNid);

    /**
     * 
     * 计算此计划清算出来的债权总数
     * @author renxingchen
     * @param debtPlanNid
     * @return
     */
    public Integer queryDebtCreditCount(String debtPlanNid);

    /**
     * 
     * 查询根据计划订单号聚合的清算债权的分页信息
     * @author renxingchen
     * @param debtCreditCustomize
     * @return
     */
    public List<DebtCreditCustomize> selectDebtCreditForPages(DebtCreditCustomize debtCreditCustomize);

    /**
     * 
     * 查询清算出来的债权数据总和
     * @author renxingchen
     * @param debtCreditCustomize
     * @return
     */
    public DebtCreditCustomize selectDebtCreditForPagesSum(DebtCreditCustomize debtCreditCustomize);

    /**
     * 
     * 查询当前计划的出借未放款的订单，所属的专属标是否有已经满标的
     * @author renxingchen
     * @param planNid
     * @return
     */
    public int queryFullBorrow(String planNid);

    /**
     * 
     * 更新汇添金专属项目满标状态为0
     * @author renxingchen
     * @param borrowNid
     * @param account 
     * @param loanFee 
     * @param userName 
     * @param userId 
     * @param planNid 
     * @return
     */
    public int updateDebtBorrowFullStatus(String borrowNid, BigDecimal account, BigDecimal loanFee, String planNid, Integer userId, String userName);

}
