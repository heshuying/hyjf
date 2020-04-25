package com.hyjf.api.web.i4;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.web.BaseService;
import com.hyjf.mybatis.model.auto.Idfa;

public interface I4Service extends BaseService {

    List<Idfa> selectIdfas(String idfa);
    
    /**
     * 添加安装设备信息
     * @param paramBean
     * @return
     */
    public JSONObject insertInstallNotice(I4Bean paramBean);
	
}
