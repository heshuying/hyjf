package com.hyjf.mybatis.mapper.customize;

import com.hyjf.mybatis.model.customize.QpCardInfo;

public interface AccountBankCustomizeMapper {

    QpCardInfo selectQpCardInfoByUserId(Integer userId);

}
