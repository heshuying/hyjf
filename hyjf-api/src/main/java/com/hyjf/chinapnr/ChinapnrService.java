package com.hyjf.chinapnr;

import com.hyjf.mybatis.model.auto.ChinapnrExclusiveLogWithBLOBs;
import com.hyjf.mybatis.model.auto.ChinapnrLog;
import com.hyjf.mybatis.model.auto.ChinapnrSendlogWithBLOBs;

/**
 * <p>
 * ChinaPnrService
 * </p>
 *
 * @author gogtz-t
 * @version 1.0.0
 */
public interface ChinapnrService {

    /**
     * 写入发送日志
     * 
     * @return
     */
    public int insertChinapnrSendLog(ChinapnrSendlogWithBLOBs log);

    /**
     * 写入检证日志
     * 
     * @return
     */
    public int insertChinapnrExclusiveLog(ChinapnrExclusiveLogWithBLOBs log);

    /**
     * 更新检证日志
     * 
     * @return
     */
    public int updateChinapnrExclusiveLog(ChinapnrExclusiveLogWithBLOBs log);

    /**
     * 查询检证日志
     * 
     * @return
     */
    public ChinapnrExclusiveLogWithBLOBs selectChinapnrExclusiveLog(long id);

    /**
     * 更新检证状态
     * 
     * @return
     */
    public int updateChinapnrExclusiveLogStatus(long uuid, String status);

    /**
     * 写入返回日志
     * 
     * @return
     */
    public int insertChinapnrLog(ChinapnrLog log);
    /**
     * 根据汇付账户查询user_id
     * @param chinapnrUsrcustid
     * @return
     */
    public Integer selectUserIdByUsrcustid(Long chinapnrUsrcustid);
}
