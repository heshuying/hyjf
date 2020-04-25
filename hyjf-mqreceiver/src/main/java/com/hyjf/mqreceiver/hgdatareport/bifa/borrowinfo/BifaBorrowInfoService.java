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
	
package com.hyjf.mqreceiver.hgdatareport.bifa.borrowinfo;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import com.hyjf.mongo.hgdatareport.entity.BifaBorrowInfoEntity;
import com.hyjf.mqreceiver.hgdatareport.BaseHgDateReportService;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowCarinfo;
import com.hyjf.mybatis.model.auto.BorrowHouses;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.UsersInfo;


/**
 * @author jijun
 */

public interface BifaBorrowInfoService extends BaseHgDateReportService {

    BifaBorrowInfoEntity getBifaBorrowInfoFromMongoDB(String borrowNid);

    Borrow selectBorrowInfo(String borrowNid);

    List<BorrowCarinfo> selectBorrowCarInfo(String borrowNid);

    List<BorrowHouses> selectBorrowHouseInfo(String borrowNid);

    boolean convertBifaBorrowInfo(Borrow borrow, Map<String, String> borrowUserInfo,BorrowRepay borrowRepay,List<BorrowCarinfo> borrowCarsinfo, List<BorrowHouses> borrowHouses, BifaBorrowInfoEntity bifaBorrowInfoEntity) throws UnsupportedEncodingException, NoSuchAlgorithmException;

    boolean convertBifaBorrowInfo(Borrow borrow, UsersInfo usersInfo,BorrowRepay borrowRepay,List<BorrowCarinfo> borrowCarsinfo, List<BorrowHouses> borrowHouses, BifaBorrowInfoEntity bifaBorrowInfoEntity) throws UnsupportedEncodingException, NoSuchAlgorithmException;
    
    boolean insertReportData(BifaBorrowInfoEntity bifaBorrowInfoEntity);

	Map<String, String> getBorrowUserInfo(String borrowNid, String companyOrPersonal);

    Borrow checkBorrowInfoIsReported(String borrowNid) throws Exception;
}