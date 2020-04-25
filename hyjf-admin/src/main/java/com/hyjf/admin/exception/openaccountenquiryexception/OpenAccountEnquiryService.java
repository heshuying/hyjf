package com.hyjf.admin.exception.openaccountenquiryexception;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.BankOpenAccountLog;
import com.hyjf.mybatis.model.customize.admin.OpenAccountEnquiryBean;

public interface OpenAccountEnquiryService extends BaseService {
    //通过手机号和身份证查询用户信息
	OpenAccountEnquiryBean accountEnquiry(Map<String, String> openAccountEnquiryBean);
	
	List<BankOpenAccountLog> bankOpenAccountLogSelect(BankOpenAccountLog bankOpenAccountLog);

    /**
     * 开户同步数据库
     * @param userid
     * @param accountId
     * @param roleId
     * @param idNo
     * @param name
     * @param regTimeEnd
     * @param isSetPassword
     */
    void updateUserAccountData(String userid, String accountId, Integer roleId, String idNo, String name, String regTimeEnd, Integer isSetPassword);
}
