package com.hyjf.server.module.wkcd.repay;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Map;

import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.server.BaseService;

public interface WkcdRepayService extends BaseService{


	/**
	 * 查询待还款项目信息
	 * 
	 * @param userId
	 * @param borrowNid
	 * @return
	 */
	Borrow searchRepayProject(int userId, String borrowNid);
	/**
	 * 统计用户的相应的还款总额 单期
	 * 
	 * @param userId
	 * @param borrowNid
	 * @param borrowApr
	 * @param borrowStyle
	 * @param periodTotal
	 * @return
	 * @throws ParseException
	 */
	BigDecimal searchRepayTotal(int userId, String borrowNid, BigDecimal borrowApr, String borrowStyle, int periodTotal)
			throws ParseException;

    /**
     * 统计用户的相应还款总额，分期
     * 
     * @param userId
     * @param borrowNid
     * @param borrowApr
     * @param borrowStyle
     * @param periodTotal
     * @return
     * @throws ParseException
     */
    BigDecimal searchRepayByTermTotal(int userId, String borrowNid, BigDecimal borrowApr, String borrowStyle,
            int periodTotal) throws ParseException;
    
    /**
     * 计算相应的分期还款信息
     * 
     * @param userId
     * @param borrowNid
     * @param borrowApr
     * @param borrowStyle
     * @param periodTotal
     * @return
     * @throws ParseException
     */
    RepayByTermBean calculateRepayByTerm(int userId, String borrowNid, BigDecimal borrowApr, String borrowStyle,
            int periodTotal) throws ParseException;

	/**
	 * 计算相应的未分期还款信息
	 * 
	 * @param userId
	 * @param borrowNid
	 * @param borrowApr
	 * @param borrowStyle
	 * @param periodTotal
	 * @return
	 * @throws ParseException
	 */
	RepayByTermBean calculateRepay(int userId, String borrowNid, BigDecimal borrowApr, String borrowStyle,
			int periodTotal) throws ParseException;


	/**
	 * 用户还款
	 * 
	 * @param repay
	 */
	public Map<String, Object> updateRepayMoney(RepayByTermBean repay);
	
	AccountChinapnr getChinapnrUserInfo(Integer userId);

	BigDecimal getUserBalance(Long chinapnrUsrcustid);
	
}



