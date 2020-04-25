package com.hyjf.admin.exception.bankrepayfreezeorg;


import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.BankRepayOrgFreezeLog;
import com.hyjf.mybatis.model.customize.admin.BankRepayFreezeOrgCustomize;

import java.util.List;

public interface BankRepayFreezeOrgService extends BaseService {

	Integer selectCount(BankRepayFreezeOrgBean form);

	List<BankRepayFreezeOrgCustomize> selectList(BankRepayFreezeOrgBean form);

    BankRepayOrgFreezeLog getFreezeLogById(Integer id);

    BankRepayOrgFreezeLog getFreezeLog(String orderId, String borrowNid);

    int deleteFreezeLogById(Integer id);
}
