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
	
package com.hyjf.mqreceiver.hgdatareport.bifa.borrowstatus;

import java.util.List;

import com.hyjf.mongo.hgdatareport.entity.BifaBorrowStatusEntity;
import com.hyjf.mqreceiver.hgdatareport.BaseHgDateReportService;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowTender;


/**
 * @author jijun
 */

public interface BifaBorrowStatusService extends BaseHgDateReportService {


    BifaBorrowStatusEntity getBifaBorrowStatusFromMongoDB(String borrowNid, Integer status);

    List<BorrowTender> selectBorrowTenders(String borrowNid);

    boolean convertBorrowStatus(Borrow borrow, List<BorrowTender> borrowTenders, BifaBorrowStatusEntity bifaBorrowStatusEntity);

    void insertReportData(BifaBorrowStatusEntity bifaBorrowStatusEntity);

    void checkBifaBorrowStatusIsReported(Borrow borrow) throws Exception;
}