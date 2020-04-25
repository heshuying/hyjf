package com.hyjf.admin.promotion.tenderdetail;

import java.math.BigDecimal;
import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.UserTenderDetailCustomize;

public interface UserTenderDetailService extends BaseService {

	List<UserTenderDetailCustomize> getRecordList(UserTenderDetailCustomize userTenderDetailCustomize);
	
	Integer getRecordTotal(UserTenderDetailCustomize userTenderDetailCustomize);
	
	BigDecimal getTenderAccountTotal(UserTenderDetailCustomize userTenderDetailCustomize);
}
