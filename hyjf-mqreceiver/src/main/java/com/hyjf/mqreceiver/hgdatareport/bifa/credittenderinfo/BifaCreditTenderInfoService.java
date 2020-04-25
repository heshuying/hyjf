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
	
package com.hyjf.mqreceiver.hgdatareport.bifa.credittenderinfo;

import com.hyjf.mongo.hgdatareport.entity.BifaCreditTenderInfoEntity;
import com.hyjf.mqreceiver.hgdatareport.BaseHgDateReportService;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowCredit;
import com.hyjf.mybatis.model.auto.HjhDebtCredit;
import com.hyjf.mybatis.model.auto.UsersInfo;


/**
 * 智投数据上报北互金service
 * @author jijun
 */

public interface BifaCreditTenderInfoService extends BaseHgDateReportService {


    BifaCreditTenderInfoEntity getBifaBorrowCreditInfoFromMongDB(String creditNid);

    BorrowCredit selectBorrowCreditInfo(String creditNid);

    boolean convertBifaBorrowCreditInfo(BorrowCredit borrowCredit, Borrow borrow, UsersInfo creditUserInfo, BifaCreditTenderInfoEntity bifaCreditInfoEntity);

    boolean insertReportData(BifaCreditTenderInfoEntity bifaCreditInfoEntity);

    HjhDebtCredit selectHjhDebtCreditInfo(String creditNid);

    boolean convertBifaHjhCreditInfo(HjhDebtCredit hjhDebtCredit, Borrow borrow, UsersInfo creditUserInfo, BifaCreditTenderInfoEntity bifaCreditInfoEntity);

}