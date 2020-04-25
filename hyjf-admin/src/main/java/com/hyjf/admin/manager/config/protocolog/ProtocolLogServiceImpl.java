package com.hyjf.admin.manager.config.protocolog;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.Admin;
import com.hyjf.mybatis.model.auto.ProtocolLog;
import com.hyjf.mybatis.model.auto.ProtocolLogExample;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiehuili on 2018/6/4.
 */
@Service
public class ProtocolLogServiceImpl extends BaseServiceImpl implements ProtocolLogService{

    /**
     * 统计全部个数
     * @return
     */
    @Override
    public Integer countRecord(int limitStart, int limitEnd){
        Integer count=0;
        ProtocolLogExample protocolLogExample =new ProtocolLogExample();
        ProtocolLogExample.Criteria criteria = protocolLogExample.createCriteria();
        List<ProtocolLog> protocolLogs=protocolLogMapper.selectByExample(protocolLogExample);
        if(!CollectionUtils.isEmpty(protocolLogs)){
            count=protocolLogs.size();
        }
        return count;
    }
    /**
     * 获取全部列表
     *
     * @return
     * */
    @Override
    public List<ProtocolLog> getRecordList(ProtocolLogBean form, int limitStart, int limitEnd) {
        List<ProtocolLog> recordList=new ArrayList<ProtocolLog>();
        //查询所有协议
        ProtocolLogExample example=new ProtocolLogExample();
        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        ProtocolLogExample.Criteria criteria = example.createCriteria();
        // 条件查询
        example.setOrderByClause("`id` Desc,`create_time` ASC");
        List<ProtocolLog> protocolLogs=protocolLogMapper.selectByExample(example);
        if(!CollectionUtils.isEmpty(protocolLogs)){
            for(int i=0;i< protocolLogs.size() ;i++){
                //时间显示转换
                Integer operation= protocolLogs.get(i).getOperation();
                String time ="";
                Integer updateUserId=0;
                if(operation.intValue() == 0){
                    time = GetDate.dateToString2(protocolLogs.get(i).getCreateTime(), "yyyy-MM-dd HH:mm:ss");
                    updateUserId =protocolLogs.get(i).getCreateUserId();
                }else if(operation.intValue() == 1){
                    time = GetDate.dateToString2(protocolLogs.get(i).getUpdateTime(), "yyyy-MM-dd HH:mm:ss");
                    updateUserId =protocolLogs.get(i).getUpdateUserId();
                }else{
                    if(protocolLogs.get(i).getDeleteTime() != null){
                        time = GetDate.dateToString2(protocolLogs.get(i).getDeleteTime(), "yyyy-MM-dd HH:mm:ss");
                        updateUserId =protocolLogs.get(i).getDeleteUserId();
                    }
                }
                Admin admin = adminMapper.selectByPrimaryKey(updateUserId);
                protocolLogs.get(i).setUserName(admin.getUsername());
                protocolLogs.get(i).setTime(time);
                recordList.add(protocolLogs.get(i));
            }
        }
        return recordList;
    }




}
