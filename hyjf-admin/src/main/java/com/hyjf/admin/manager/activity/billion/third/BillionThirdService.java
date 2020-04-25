package com.hyjf.admin.manager.activity.billion.third;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.ActivityBillionThird;

public interface BillionThirdService extends BaseService {

	public Integer selectRecordCount(BillionThirdBean form);

	public List<ActivityBillionThird> getRecordList(BillionThirdBean form, int limitStart, int limitEnd);

    
}
