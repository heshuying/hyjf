package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;

import com.hyjf.mybatis.model.auto.BankMerchantAccount;
import com.hyjf.mybatis.model.customize.admin.BankMerchantAccountListCustomize;


/**
 * 
 * @author cwyang
 * add by 2017/4/7
 *  银行对账mybaties查询接口
 */
public interface BankMerchantAccountListCustomizeMapper {

    List<BankMerchantAccountListCustomize> selectRecordList(BankMerchantAccountListCustomize form);

    int queryRecordTotal(BankMerchantAccountListCustomize form);

    int updateMerchantByAccountCode(BankMerchantAccount record);
}
