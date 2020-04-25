package com.hyjf.admin.manager.activity.actoct2017.actsignin;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.ActSignin;

@Service
public class ActSigninServiceImpl extends BaseServiceImpl implements ActSigninService {
   

    /**
     * 获取列表列表
     * 
     * @return
     */
    public List<ActSignin> getRecordList(ActSignin ActSignin, int limitStart, int limitEnd) {
    	HashMap<String,Object> params = new HashMap<String,Object>();
        if (limitStart != -1) {
        	params.put("limitStart", limitStart);
        	params.put("limitEnd", limitEnd);
        }
        // 条件查询
        if (ActSignin.getUserName() != null) {
            if( !ActSignin.getUserName().isEmpty()){
                params.put("userName", ActSignin.getUserName());
            }
                
        }
        if (ActSignin.getMobile() != null) {
            if(!ActSignin.getMobile().isEmpty()){
                params.put("mobile", ActSignin.getMobile());
            }
            
        }
        if (ActSignin.getRemark() != null) {
            if(!ActSignin.getRemark().isEmpty()){
                params.put("count", ActSignin.getRemark());
            }
            
        }
        return actSigninCustomizeMapper.selectByExample(params);
    }

    /**
     * 获取单个维护
     * 
     * @return
     */
    public List<ActSignin> getRecord(int id, int limitStart, int limitEnd) {
    	HashMap<String,Object> params = new HashMap<String,Object>();
        if (limitStart != -1) {
        	params.put("limitStart", limitStart);
        	params.put("limitEnd", limitEnd);
        }
        params.put("userId", id);
        return actSigninCustomizeMapper.selectByPrimaryKey(params);
    }

  
}
