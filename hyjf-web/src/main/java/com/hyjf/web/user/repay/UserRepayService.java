package com.hyjf.web.user.repay;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.web.WebUserInvestListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserRepayProjectListCustomize;
import com.hyjf.web.BaseService;

public interface UserRepayService extends BaseService {

	/**
	 * 获取用户还款列表
	 * 
	 * @param form
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<WebUserRepayProjectListCustomize> searchUserRepayList(UserRepayProjectListBean form, int offset, int limit);

	/**
	 * 统计用户还款列表总条数
	 * 
	 * @param form
	 * @return
	 */
	int countUserRepayRecordTotal(UserRepayProjectListBean form);

	/**
	 * 查询项目的用户出借信息
	 * 
	 * @param borrowNid
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	List<WebUserInvestListCustomize> selectUserInvestList(String borrowNid, int limitStart, int limitEnd);

	/**
	 * 统计用户出借列表总条数
	 * 
	 * @param borrowNid
	 * @return
	 */
	int countUserInvestRecordTotal(String borrowNid);

	/**
	 * 查询用户还款详情
	 * 
	 * @param form
	 * @return
	 * @throws ParseException
	 * @throws NumberFormatException
	 */
	UserRepayProjectBean searchRepayProjectDetail(UserRepayProjectBean form)
			throws NumberFormatException, ParseException;

	/**
	 * 查询还款用户信息
	 * 
	 * @param userId
	 * @return
	 */
	Users searchRepayUser(int userId);

	/**
	 * 查询还款用户的账户金额
	 * 
	 * @param userId
	 * @return
	 */
	Account searchRepayUserAccount(int userId);

	/**
	 * 查询待还款项目信息
	 * @param userId
	 * @param userName
	 * @param roleId
	 * @param borrowNid
	 * @return
	 */
	Borrow searchRepayProject(int userId,String userName,String roleId, String borrowNid);

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
	BigDecimal searchRepayByTermTotal(int userId, Borrow borrow, BigDecimal borrowApr, String borrowStyle,
			int periodTotal) throws ParseException;

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
	BigDecimal searchRepayTotal(int userId, Borrow borrow)
			throws ParseException;

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
	RepayByTermBean calculateRepayByTerm(int userId, Borrow borrow) throws ParseException;

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
	RepayByTermBean calculateRepay(int userId, Borrow borrow) throws ParseException;

	/**
	 * 用户还款
	 * 
	 * @param repay
	 */
	public boolean updateRepayMoney(RepayByTermBean repay,Integer roleId,Integer repayUserId);

	AccountChinapnr getChinapnrUserInfo(Integer userId);

	BigDecimal getUserBalance(Long chinapnrUsrcustid);

    BorrowTender getBorrowTenderInfo(Integer UserId, String borrowNid);

    List<WebUserInvestListCustomize> selectUserDebtInvestList(String borrowNid, String orderId, int i, int j);
    /**
     * 查询垫付机构的未还款金额
     * @param userId
     * @return
     */
    BigDecimal getRepayOrgRepaywait(Integer userId);

}
