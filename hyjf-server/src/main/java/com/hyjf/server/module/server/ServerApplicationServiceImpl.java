package com.hyjf.server.module.server;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.mybatis.model.auto.ServerApplication;
import com.hyjf.mybatis.model.auto.ServerApplicationExample;
import com.hyjf.server.BaseServiceImpl;

@Service
public class ServerApplicationServiceImpl extends BaseServiceImpl implements ServerApplicationService {

	/**
	 * 平台对接第三方接口安全验证
	 * @return
	 * @author Michael
	 */
	@Override
	public ServerApplication getServerApplicationByAppid(String appId){
	    ServerApplicationExample example = new ServerApplicationExample();
	    ServerApplicationExample.Criteria cra = example.createCriteria();
	    cra.andAppidEqualTo(appId);
        List<ServerApplication> serverApplicationList = serverApplicationMapper.selectByExample(example);
        if(serverApplicationList != null && serverApplicationList.size() > 0){
        	return serverApplicationList.get(0);
        }
        return null;
	}

    /**
     * 
     * 获取所有第三方接口的对象
     * @author yyc
     * @return
     */
    @Override
    public List<ServerApplication> getServerApplicationBList() {
        ServerApplicationExample example = new ServerApplicationExample();
        ServerApplicationExample.Criteria cra = example.createCriteria();
        List<ServerApplication> serverApplicationList = serverApplicationMapper.selectByExample(example);
        return serverApplicationList;
    }
}
