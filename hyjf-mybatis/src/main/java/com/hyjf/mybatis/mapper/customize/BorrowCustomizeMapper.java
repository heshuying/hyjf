package com.hyjf.mybatis.mapper.customize;

import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.UsersPortrait;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowCustomize;
import com.hyjf.mybatis.model.customize.BorrowSynCustomize;
import com.hyjf.mybatis.model.customize.batch.BatchActivityQixiCustomize;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface BorrowCustomizeMapper {

	/**
	 * 获取借款列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	List<BorrowCustomize> selectBorrowList(BorrowCommonCustomize borrowCommonCustomize);
	
	/**
     * 根据项目编号获取项目标的
     * 
     * @param borrowNid
     * @return
     */
    public Borrow getBorrowByNid(String borrowNid);
	
	/**
	 * 获取借款列表
	 * @param borrowCommonCustomize
	 * @return
	 */
	List<BorrowCustomize> searchBorrowList(BorrowCommonCustomize borrowCommonCustomize);

	/**
	 * COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	Long countBorrow(BorrowCommonCustomize borrowCommonCustomize);

	/**
	 * 总额合计
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	BigDecimal sumAccount(BorrowCommonCustomize borrowCommonCustomize);

	/**
	 * 借款预编码
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	String getBorrowPreNid(@Param("mmdd") String mmdd);

	/**
	 * 列表导出
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	List<BorrowCommonCustomize> exportBorrowList(BorrowCommonCustomize borrowCommonCustomize);

	/**
	 * 查询出到期但是未满标的标的,给其发短信
	 * @return
	 */
	List<BorrowCommonCustomize> searchNotFullBorrowMsg();
	
	/**
	 * 查询出汇计划到期但是未满标的标的,给其发短信
	 * @return
	 */
	List<BorrowCommonCustomize> searchHjhNotFullBorrowMsg();
	
	 /**
     * @param updateOfLoansBorrow
     * @return
     */
    int updateOfBorrow(Map<String, Object> borrow);

    /**
     * @param updateOfRepayTender
     * @return
     */
    int updateOfFullBorrow(Map<String, Object> borrow);
    /**
     * @param updateOfBorrowAppoint
     * @return
     */
	int updateOfBorrowAppoint(Map<String, Object> borrow);
	 /**
     * @param updateOfFullBorrowAppoint
     * @return
     */
	int updateOfFullBorrowAppoint(Map<String, Object> borrowFull);
	
	 /**
     * @param updateOfLoansBorrow
     * @return
     */
    int updateCancelOfBorrowAppoint(Map<String, Object> borrow);
    
    /**
     * 融东风标的信息同步
     * @param borrowNid
     * @return
     */
    BorrowSynCustomize borrowRecordSyn(String borrowNid);
    
    /**
     * 融东风-取得标的还款计划待还管理费
     * @param borrowNid
     * @return
     */
    BigDecimal getBorrowRepayFee(Map<String, Object> paramMap);

    /**
     * 现金贷获取借款预编号
     * @param mmdd
     * @return
     */
	String getXJDBorrowPreNid(@Param("mmdd") String mmdd);

    /**
     * 获取用户累计出借
     * @param userId
     * @return
     */
    Integer countInvest(Integer userId);

	/**
	 * 获取用户累计收益
	 * @param userId
	 * @return
	 */
    BigDecimal getInterestSum(Integer userId);

	/**
	 * 获取用户累计充值金额
	 * @param userId
	 * @return
	 */
	BigDecimal getRechargeSum(Integer userId);

	/**
	 * 获取用户累计提现金额
	 * @param userId
	 * @return
	 */
	BigDecimal getWithdrawSum(Integer userId);

	/**
	 * 获取用户累计交易笔数
	 * @param userId
	 * @return
	 */
	int getTradeNumber(Integer userId);

	/**
	 * 获取用户年化出借金额
	 * @param userId
	 * @return
	 */
    BigDecimal getInvestSum(Integer userId);

    /**
     * 获取计划标年化收益
     * @param userId
     * @return
     */
	BigDecimal getPlanSum(Integer userId);

    int insertUsersPortrait(UsersPortrait usersPortrait);

    /**
     * 获取待复审项目借款列表add by liushouyi
     *
     * @return
     */
    List<BorrowWithBLOBs> selectAutoReviewBorrowNidList();

    /**
     * 获取待复审项目借款列表add by liushouyi
     * 
     * @return
     */
    List<BorrowWithBLOBs> selectAutoReviewHJHBorrowNidList();
    
	/**
	 * 查询计划还款日前一天，处于出借中和复审中的原始标的，发送邮件预警
	 */
	List<BorrowCustomize> selectUnDealBorrowBeforeLiquidate();
	
	/**
	 * 获取手动录标的自动备案、初审的标的编号和状态
	 * @return
	 */
	List<BorrowWithBLOBs> selectAutoBorrowNidList();

	/**
	 * 获取用户散标和计划出借总额
	 * @param userId
	 * @return
	 */
	BigDecimal selectInvestSum(@Param("userId") Integer userId, @Param("startTime") int startTime,
			@Param("endTime") int endTime);

	/**
	 * 获取活动期间内的username和累计出借总额(散标+计划)
	 * @param map
	 * @return
	 */
	List<BatchActivityQixiCustomize> selectInvestUserName(Map<String,Object> map);

	/**
	 * 获取近七天添加的标的信息
	 * @param daySubSeven
	 * @return
	 */
	List<BorrowCustomize> selectBorrowUserInfo(@Param("startDate") Integer startDate, @Param("endDate") Integer endDate);

	/**
	 * 合规数据上报 CERT 国家互联网应急中心 获得所有在贷的标的
	 * @return
	 */
	List<String> getCertBorrowNotInit();
}