package com.hyjf.mybatis.mapper.customize;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.WithdrawCustomize;
import com.hyjf.mybatis.model.customize.thirdparty.UserWithdrawRecordCustomize;

public interface WithdrawCustomizeMapper {

    /**
     * 获取提现列表数量
     * 
     * @param WithdrawCustomize
     * @return
     */
    int selectWithdrawCount(WithdrawCustomize withdrawCustomize);

    /**
     * 获取提现列表
     * 
     * @param WithdrawCustomize
     * @return
     */
    List<WithdrawCustomize> selectWithdrawList(WithdrawCustomize withdrawCustomize);
    /**
     * 
     * 第三方获取提现列表
     * @author pcc
     * @param param
     * @return
     */
    List<UserWithdrawRecordCustomize> getThirdPartyUserWithdrawRecord(Map<String, Object> param);
}