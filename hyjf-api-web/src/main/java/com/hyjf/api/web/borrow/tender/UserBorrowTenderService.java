package com.hyjf.api.web.borrow.tender;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.web.BaseService;
import com.hyjf.mybatis.model.auto.Users;

public interface UserBorrowTenderService extends BaseService {

	JSONObject getUserBorrowTender(UserBorrowTenderBean paramBean,Users user) throws Exception;
	
	Users checkLoginUser(UserBorrowTenderBean paramBean) throws Exception;
	
}
