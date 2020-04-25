package com.hyjf.admin.exception.openaccountexception;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.ChinapnrLog;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.admin.AdminOpenAccountExceptionCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;

public interface OpenAccountService extends BaseService {

	int countRecordTotal(Map<String, Object> accountUser);

	List<AdminOpenAccountExceptionCustomize> getRecordList(Map<String, Object> accountUser, int offset, int limit);

    int insertOpenAccountRecord(ChinapnrBean bean);

	int insertOpenAccountRecord(ChinapnrBean bean, String accountId);

	Users getUserByUserId(String userId);

	AccountChinapnr getAccountChinapnr(String usrId);

	List<ChinapnrLog> getOpenAccountLog(String userId);

}
