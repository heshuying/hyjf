package com.hyjf.api.wdzj.borrowdata;

import java.util.List;
import java.util.Map;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.customize.apiweb.wdzj.BorrowListCustomize;
import com.hyjf.mybatis.model.customize.apiweb.wdzj.PreapysListCustomize;

public interface BorrowDataService extends BaseService {

	List<BorrowListCustomize> selectBorrowList(Map<String,Object> paraMap);

	int countBorrowList(Map<String,Object> paraMap);

	String selectBorrowAmountSum(Map<String,Object> paraMap);
	
	List<PreapysListCustomize> selectPreapysList(Map<String,Object> paraMap);

	int countPreapysList(Map<String,Object> paraMap);

}
