package com.hyjf.server.module.server;

import java.util.List;

import com.hyjf.mybatis.model.auto.ServerApplication;

public interface ServerApplicationService {

    /**
     * 
     * 根据appId第三方接口的对象
     * @author yyc
     * @return
     */
    public ServerApplication getServerApplicationByAppid(String appId);
    
    /**
     * 
     * 获取所有第三方接口的对象
     * @author yyc
     * @return
     */
    public List<ServerApplication> getServerApplicationBList();

}