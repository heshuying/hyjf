/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2017
 * Company: HYJF Corporation
 * @author: lb
 * @version: 1.0
 * Created at: 2017年9月15日 上午9:43:49
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.batch.hgdatareport;


import com.hyjf.bank.service.BaseService;
import com.hyjf.mongo.hgdatareport.base.BaseHgDataReportEntity;
import com.hyjf.mongo.hgdatareport.entity.UserInfoSHA256Entity;

/**
 * @author liubin
 */

public interface BaseHgDateReportService extends BaseService {

    /**
     * 调用webservice接口并返回数据
     *
     * @param methodName
     * @param encmsg
     * @return
     */
    String webService(String methodName, String encmsg);
    /**
     * 上报北互金
     * @param methodName
     * @param data
     * @param <T>
     * @return
     */
    <T extends BaseHgDataReportEntity> T reportData(String methodName, T data);

    /**
     * 获取用户索引信息
     *
     * @param userId
     * @param trueName
     * @param idCard
     * @return
     */
    UserInfoSHA256Entity selectUserIdToSHA256(Integer userId,String trueName,String idCard);

}

	