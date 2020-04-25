package com.hyjf.web.api.base;

import java.util.List;

import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.ChinapnrExclusiveLogWithBLOBs;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;

/**
 * <p>
 * BaseService
 * </p>
 *
 * @author gogtz
 * @version 1.0.0
 */
public interface ApiBaseService {

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
}
