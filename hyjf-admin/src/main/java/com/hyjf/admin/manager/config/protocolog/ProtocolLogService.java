package com.hyjf.admin.manager.config.protocolog;

import com.hyjf.mybatis.model.auto.ProtocolLog;

import java.util.List;

/**
 * Created by DELL on 2018/6/4.
 */
public interface ProtocolLogService {

    /**
     * 获取全部列表
     *
     * @return
     */
    public List<ProtocolLog> getRecordList(ProtocolLogBean form, int limitStart, int limitEnd);

    /**
     * 统计全部个数
     *
     * @return
     */
    public Integer countRecord(int limitStart, int limitEnd);

}
