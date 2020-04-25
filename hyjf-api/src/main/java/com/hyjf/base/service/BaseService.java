package com.hyjf.base.service;

import java.util.List;

import com.hyjf.mybatis.model.auto.*;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

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
     * 取得AppKey
     *
     */
    public String getAppKey(String appId);

    /**
     * 获取用户
     *
     * @param userId
     * @return
     */
    public Users getUsers(Integer userId);

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
     * 获取用户的汇付信息
     *
     * @param userId
     * @return 用户的汇付信息
     */
    public AccountChinapnr getAccountChinapnr(Integer userId);

    /**
     * 获取用户信息
     *
     * @param username
     * @param password
     * @return 获取用户信息
     */
    public Users getUsers(String username, String password);

    /**
     * 获取用户的账户信息
     *
     * @param userId
     * @return 用户的身份证号
     */
    public com.hyjf.mybatis.model.auto.Account getAccount(Integer userId);

    /**
     * 获取系统配置
     *
     * @return
     */
    public String getBorrowConfig(String configCd);

    /**
     * 查询检证日志
     *
     * @return
     */
    public ChinapnrExclusiveLogWithBLOBs selectChinapnrExclusiveLog(long id);

    /**
     * 更新log日志（判断重复）
     *
     * @param record
     * @return
     */
    int updateChinapnrExclusiveLog(ChinapnrExclusiveLogWithBLOBs record);

    /**
     * 更新检证状态
     *
     * @return
     */
    public int updateChinapnrExclusiveLogStatus(long uuid, String status);

    /**
     * 根据汇付账户查询user_id
     *
     * @param chinapnrUsrcustid
     * @return
     */
    public Integer selectUserIdByUsrcustid(Long chinapnrUsrcustid);

    /**
     * 根据用户ID取得用户信息
     *
     * @param userId
     * @return
     */
    public Users getUsersByUserId(Integer userId);

    /**
     * 根据用户ID取得用户信息
     *
     * @param userId
     * @return
     */
    public UsersInfo getUsersInfoByUserId(Integer userId);

    /**
     * 获取数据字典表的下拉列表
     *
     * @return
     */
    public List<ParamName> getParamNameList(String nameClass);

    /**
     * 获取数据字典名称
     *
     * @return
     */
    public String getParamName(String nameClass, String nameCd);

    /**
     * 特殊字符编码
     * @param str
     * @return
     */
    public String strEncode(String str);

    BankOpenAccount getBankOpenAccount(Integer userId);

    /** 返回码错误信息 */
    public String getBankRetMsg(String retCode);



    /**
     * 根据银行卡号查询银行ID
     *
     * @param cardNo
     * @return
     */
    public String getBankIdByCardNo(String cardNo);

    /**
     * 根据银行Id查询所属银行名称
     * @param bankId
     * @return
     */
    String getBankNameById(String bankId);

    /**
     * 调用江西银行查询联行号
     * @param cardNo
     * @return
     */
    BankCallBean payAllianceCodeQuery(String cardNo,Integer userId);

    /**
     * 根据银行Id查询本地存的银联行号
     * @param bankId
     * @return
     */
    String getPayAllianceCodeByBankId(String bankId);

    /**
     * 插入开户日志表
     *
     * @param userId
     * @param userName
     * @param mobile
     * @param orderId
     * @return
     */
    public boolean updateUserAccountLog(Integer userId, String userName, String mobile, String orderId);

    /**
     * 根据用户ID,短信发送订单号查询用户开户记录
     *
     * @param userId
     * @param orderId
     * @return
     */
    BankOpenAccountLog checkBankOpenAccountLog(Integer userId, String orderId);
	/**
	 * 验证签名
	 * @param chkValue 调用方传入签名
	 * @param params 调用方传入验签参数
	 * @return
	 * @author liubin
	 */
    boolean checkSign(String chkValue, Object...params);
    
    /**
     * 
     * 根据手机号查询用户信息
     * @author pcc
     * @param mobile
     * @return
     */
    Users getUsersByMobile(String mobile);
    
    /**
     * 根据机构编号检索机构信息
     *
     * @param instCode
     * @return
     */
    HjhInstConfig selectInstConfigByInstCode(String instCode);

	ActivityList getActivityById(Integer actId);

	Integer checkActivityStatus(String activityId);
	/**
	 * 
	 * 页面开户插入开户记录
	 * @author sunss
	 * @param userId
	 * @param userName
	 * @param mobile
	 * @param orderId
	 * @param name
	 * @param idno
	 * @return
	 */
	public boolean updateUserAccountLogPage(Integer userId, String userName, String mobile, String orderId,String name,String idno) ;


    /**
     * 根据标的编号查询借款信息
     *
     * @param borrowNid
     * @return
     */
    Borrow selectBorrowInfoByBorrowNid(String borrowNid);
}
