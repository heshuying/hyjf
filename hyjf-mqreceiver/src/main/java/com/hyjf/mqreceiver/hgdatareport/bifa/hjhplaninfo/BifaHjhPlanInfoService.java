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
	
package com.hyjf.mqreceiver.hgdatareport.bifa.hjhplaninfo;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import com.hyjf.mongo.hgdatareport.entity.BifaHjhPlanInfoEntity;
import com.hyjf.mqreceiver.hgdatareport.BaseHgDateReportService;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.auto.UsersInfo;


/**
 * 智投数据上报北互金service
 * @author jijun
 */

public interface BifaHjhPlanInfoService extends BaseHgDateReportService {


    BifaHjhPlanInfoEntity getBifaHjhPlanInfoFromMongoDB(String planNid);

    HjhPlan selectHjhPlanInfo(String planNid);

    boolean convertBifaHjhPlanInfo(HjhPlan hjhplan, BifaHjhPlanInfoEntity bifaHjhPlanInfoEntity);

    void insertReportData(BifaHjhPlanInfoEntity bifaHjhPlanInfoEntity);

    boolean convertBifaHjhPlanInfo(HjhPlan hjhplan, Borrow borrow, Map<String, String> borrowUserInfo, BifaHjhPlanInfoEntity bifaHjhPlanInfoEntity);

    List<BifaHjhPlanInfoEntity> getCountFromMongoDB();

    List<HjhPlan> selectHjhPlanInfoList();

    BifaHjhPlanInfoEntity checkRelaHjhPlanIsReported(String planNid) throws Exception;

    BifaHjhPlanInfoEntity removeBorrowNid(BifaHjhPlanInfoEntity bifaHjhPlanInfoEntity) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException;
}