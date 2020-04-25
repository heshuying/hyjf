package com.hyjf.batch;

import java.math.BigDecimal;

import com.hyjf.common.util.GetOrderIdUtils;
import java.util.List;

import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.ChinapnrLog;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

/**
 * <p>
 * BaseService
 * </p>
 *
 * @author gogtz
 * @version 1.0.0
 */
public interface BaseService {

    /**
     * 发送短信添加数据
     *
     * @param content
     * @param mobile
     * @param checkCode
     * @param remark
     * @param status
     * @return
     */
    public boolean AddMessage(String content, String mobile, String checkCode, String remark, int status);

    /**
     * 写入日志
     *
     * @return
     */
    public int insertChinapnrLog(ChinapnrLog log);

    /**
     * 获取用户在汇付天下的账号信息
     *
     * @return
     */
    public AccountChinapnr getChinapnrUserInfo(Integer userId);

    /**
     * 根据用户ID取得用户信息
     * @param userId
     * @return
     */
    public Users getUsersByUserId(Integer userId);

    /**
     * 根据用户ID取得用户信息
     * @param userId
     * @return
     */
    public UsersInfo getUsersInfoByUserId(Integer userId);
	/**
	 * 获取银行开户信息
	 * 
	 * @param userId
	 * @return
	 */
	public BankOpenAccount getBankOpenAccount(Integer userId);

	/**
	 * 获取相应的标的详情
	 * @param borrowNid
	 * @return
	 */
	BorrowWithBLOBs getBorrowByNid(String borrowNid);
	
	
	/**
	 * 取得江西银行接口返回响应码的信息描述
	 * @param retCode
	 * @return
	 * @author LiuBin
	 * @date 2017年8月1日 下午3:58:47
	 */
	public String getBankRetMsg(String retCode);

	BigDecimal getBankBalance(Integer userId, String accountId);

	SpreadsUsers getSpreadsUsersByUserId(Integer userId);

	ActivityList getActivityListById(Integer activityId);
	
    /**
     * 根据借款编号查询资产信息
     * 
     * @param borrowNid
     * @return
     */
    HjhPlanAsset getHjhPlanAsset(String borrowNid);

	/**
	 * 压缩文件
	 *
	 * @param sb
	 * @param zipName
	 * @return
	 */
	boolean writeZip(StringBuffer sb, String zipName);
}
