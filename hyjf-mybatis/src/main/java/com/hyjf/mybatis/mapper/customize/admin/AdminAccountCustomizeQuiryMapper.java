/**
 * Description:会员用户开户记录初始化列表查询
 * Copyright: (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 上午11:01:57
 * Modification History:
 * Modified by :
 * */

package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.auto.BankOpenAccountLog;
import com.hyjf.mybatis.model.customize.admin.OpenAccountEnquiryBean;

public interface AdminAccountCustomizeQuiryMapper {
    /**
     * 通过手机号或者身份证号查询开户掉单
     * 此处为方法说明
     * @author yyc
     * @param record
     * @return
     */
	OpenAccountEnquiryBean accountEnquiry(Map<String, String> record);
    /**
     * 通过手机号或者身份证号查询用户开户掉单log表
     * 此处为方法说明
     * @author yyc
     * @param bankOpenAccountLog
     * @return
     */
    List<BankOpenAccountLog> bankOpenAccountLogSelect(BankOpenAccountLog bankOpenAccountLog);
    
}
