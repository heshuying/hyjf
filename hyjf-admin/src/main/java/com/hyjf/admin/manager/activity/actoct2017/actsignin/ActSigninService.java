package com.hyjf.admin.manager.activity.actoct2017.actsignin;

import java.util.List;

import com.hyjf.mybatis.model.auto.ActSignin;

public interface ActSigninService {

    /**
     * 获取手续费列表列表
     * 
     * @return
     */
    public List<ActSignin> getRecordList(ActSignin ActSignin, int limitStart, int limitEnd);

    /**
     * 获取单个手续费列表维护
     * 
     * @return
     */
    public List<ActSignin> getRecord(int id, int limitStart, int limitEnd);

   
    
}