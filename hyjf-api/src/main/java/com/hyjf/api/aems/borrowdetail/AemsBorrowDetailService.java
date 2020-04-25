package com.hyjf.api.aems.borrowdetail;

import com.hyjf.mybatis.model.auto.BorrowCarinfo;
import com.hyjf.mybatis.model.auto.BorrowHouses;
import com.hyjf.mybatis.model.auto.BorrowManinfo;
import com.hyjf.mybatis.model.auto.BorrowUsers;
import com.hyjf.mybatis.model.customize.web.BorrowDetailCustomize;

import java.util.List;

/**
 * 标的详情service
 * jijun 20180910
 */

public interface AemsBorrowDetailService {
    /**
     * 根据项目标号查询项目详情
     * @param borrowNid
     * @return
     */
    BorrowDetailCustomize selectProjectDetail(String borrowNid);

    //借款人企业信息
    BorrowUsers getBorrowUsersByNid(String borrowNid);

    //借款人信息
    BorrowManinfo getBorrowManinfoByNid(String borrowNid);

    //房产抵押信息
    List<BorrowHouses> getBorrowHousesByNid(String borrowNid);

    //车辆抵押信息
    List<BorrowCarinfo> getBorrowCarinfoByNid(String borrowNid);

    String getParamName(String houses_type, String val);
}
