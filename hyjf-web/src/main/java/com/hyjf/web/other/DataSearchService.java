package com.hyjf.web.other;

import com.hyjf.mybatis.model.customize.DataSearchCustomize;

import java.util.List;
import java.util.Map;

/**
 * @author lisheng
 * @version DataSearchService, v0.1 2018/7/4 11:50
 */

public interface DataSearchService {
     List<DataSearchCustomize> findDataList(DataSearchBean dataSearchBean);

     List<DataSearchCustomize> findExportDataList(DataSearchBean dataSearchBean);

     Integer findDataTotal(DataSearchBean dataSearchBean);

     Map<String ,Object> findMoney(DataSearchBean dataSearchBean);

     boolean checkMobile(String mobile);

    int checkMobileCode(String phone, String code);



}
