package com.hyjf.batch.chinapnr;

import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.ChinapnrExclusiveLogWithBLOBs;
import com.hyjf.mybatis.model.auto.ChinapnrLog;
import com.hyjf.mybatis.model.auto.ChinapnrSendlogWithBLOBs;
@Service
public class ChinapnrServiceImpl extends BaseServiceImpl implements ChinapnrService {

    /**
     * 写入发送日志
     * 
     * @return
     */
    public int insertChinapnrSendLog(ChinapnrSendlogWithBLOBs chinapnrSendlog) {
        return chinapnrSendlogMapper.insertSelective(chinapnrSendlog);
    }

    /**
     * 写入检证日志
     * 
     * @return
     */
    public int insertChinapnrExclusiveLog(ChinapnrExclusiveLogWithBLOBs record) {
        return chinapnrExclusiveLogMapper.insertSelective(record);
    }

    /**
     * 更新检证日志
     * 
     * @return
     */
    public int updateChinapnrExclusiveLog(ChinapnrExclusiveLogWithBLOBs record) {
        return chinapnrExclusiveLogMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 查询检证日志
     * 
     * @return
     */
    public ChinapnrExclusiveLogWithBLOBs selectChinapnrExclusiveLog(long id) {
        return chinapnrExclusiveLogMapper.selectByPrimaryKey(id);
    }

    /**
     * 更新检证状态
     * 
     * @return
     */
    public int updateChinapnrExclusiveLogStatus(long uuid, String status) {
        ChinapnrExclusiveLogWithBLOBs record = new ChinapnrExclusiveLogWithBLOBs();
        record.setId(uuid);
        record.setStatus(status);
        return chinapnrExclusiveLogMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 写入返回日志
     * 
     * @return
     */
    public int insertChinapnrLog(ChinapnrLog chinapnrLog) {
        return chinapnrLogMapper.insertSelective(chinapnrLog);
    }
}
