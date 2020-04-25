package com.hyjf.api.web;

import java.math.BigDecimal;
import java.util.List;

import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.ChinapnrExclusiveLogWithBLOBs;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
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
    public Account getAccount(Integer userId);

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
    
    /**
     * 校验是否有效的合作渠道
     * @param fromCode
     * @return
     * @throws Exception
     */
    public int countUserPlat(String fromCode) throws Exception;
    
    


    /**
     * 获取用户开户信息
     * 
     * @param userId
     * @return
     */
    BankOpenAccount getBankOpenAccount(Integer userId);

    /** 返回码错误信息 */
    public String getBankRetMsg(String retCode);
    /**
     * 为加强版发送验证码
     * 
     * @param channelPc
     * @param channelPc2
     *
     * @param request
     * @param form
     * @return Map<String, Object> {success: 1,message:调用验证码接口成功,srvTxCode:
     *         srvTxCode}
     */
    public BankCallBean sendSms(Integer userId, String srvTxCode, String mobile, String channelPc);

    /**
     * 根据项目编号获取项目标的
     * 
     * @param borrowNid
     * @return
     */
    public BorrowWithBLOBs getBorrowByNid(String borrowNid);

    /**
     * 查询用户余额
     * 
     * @param userId
     * @param accountId
     * @return
     */
    public BigDecimal getBankBalance(Integer userId, String accountId);

    boolean checkNewUser(Object userId);
    
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
    public String getBankNameById(String bankId);
    
    /**
     * 调用江西银行查询联行号
     * @param cardNo
     * @return
     */
    public BankCallBean payAllianceCodeQuery(String cardNo,Integer userId);
    
    /**
     * 根据银行Id查询本地存的银联行号
     * @param bankId
     * @return
     */
    public String getPayAllianceCodeByBankId(String bankId);
}
