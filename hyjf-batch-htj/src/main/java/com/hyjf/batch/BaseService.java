package com.hyjf.batch;

import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.ChinapnrLog;
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
}
