/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.hgdatareport.bifa.indexdata;

import com.hyjf.batch.hgdatareport.BaseHgDateReportService;
import com.hyjf.mybatis.model.customize.bifa.BifaIndexUserInfoBean;
import com.hyjf.mybatis.model.customize.bifa.UserIdAccountSumBean;

import java.util.List;

/**
 * 北互金索引数据上报
 * @author jun
 */
public interface BifaIndexDataReportService extends BaseHgDateReportService {


    List<UserIdAccountSumBean> getBorrowTenderAccountSum(Integer startDate, Integer endDate);

    BifaIndexUserInfoBean selectUserInfoByUserId(Integer key);

    void executeIndexDataReport(Integer startDate, Integer endDate) throws Exception;
}
