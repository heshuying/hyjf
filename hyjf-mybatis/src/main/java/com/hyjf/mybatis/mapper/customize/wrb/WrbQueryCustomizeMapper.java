package com.hyjf.mybatis.mapper.customize.wrb;

import com.hyjf.mybatis.model.auto.BorrowRecoverPlan;
import com.hyjf.mybatis.model.customize.wrb.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author xiasq
 * @version WrbQueryCustomizeMapper, v0.1 2018/3/8 14:04
 */
public interface WrbQueryCustomizeMapper {
    /**
     * 风车理财根据出借订单号查询出借信息
     * @param nid
     * @param userId
     * @return
     */
	WrbTenderNotifyCustomize selectWrbTenderInfo(@Param("nid") String nid, @Param("userId") Integer userId);

    /**
     * 风车理财根据标的号和出借时间查询出借明细
     * @param borrowNid
     * @param investTime 大于出借时间：秒
     * @return
     */
    List<WrbBorrowTenderCustomize> selectWrbBorrowTender(@Param("borrowNid") String borrowNid, @Param("investTime") int investTime);

    /**
     * 风车理财根据标的号和出借时间查询汇总数据
     * @param borrowNid
     * @param investTime 大于出借时间：秒
     * @return
     */
    WrbBorrowTenderSumCustomize selectWrbBorrowSumTender(@Param("borrowNid") String borrowNid, @Param("investTime") int investTime);

    /**
     * 获取某天出借情况汇总
     * @param timeStart
     * @param timeEnd
     * @return
     */
    WrbDaySumCustomize getDaySum(@Param("timeStart")Integer timeStart, @Param("timeEnd") Integer timeEnd);

    /**
     * 根据标的ID查询标的列表
     * @param params
     * @return
     */
	List<WrbBorrowListCustomize> searchBorrowListByNid(Map<String, Object> params);

    /**
     * 获取还款计划按回款时间排序(待回款)
     * @param borrowNid
     * @return
     */
    List<BorrowRecoverPlan> selectRNextecoverPlan(String borrowNid);

    /**
     * 获取回款计划
     * @param userId
     * @param investRecordId
     * @param borrowNid
     * @return
     */
    List<BorrowRecoverPlan> selectRecoverPlanByNid(@Param("userId")String userId,
                                                   @Param("investRecordId")String investRecordId,
                                                   @Param("borrowNid")String borrowNid);

    /**
     * 获取出借记录
     * @param params
     * @return
     */
    List<WrbInvestRecordCustomize> getInvestRecord(Map<String, Object> params);

    /**
     * 获取还款信息
     * @param nid
     * @return
     */
    List<WrbRecoverCustomize> getRecover(String nid);

    /**
     * 获取分期还款信息
     * @param nid
     * @return
     */
    List<WrbRecoverCustomize> getRecoverPlan(String nid);
}
