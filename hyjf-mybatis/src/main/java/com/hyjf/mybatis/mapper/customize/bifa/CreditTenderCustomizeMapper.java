package com.hyjf.mybatis.mapper.customize.bifa;

import com.hyjf.mybatis.model.customize.bifa.UserIdAccountSumBean;

import java.util.List;

public interface CreditTenderCustomizeMapper {

    List<UserIdAccountSumBean> getCreditTenderAccountSum(Integer daySubSeven);

    List<UserIdAccountSumBean> getHjhCreditTenderAccountSum(Integer daySubSeven);
}