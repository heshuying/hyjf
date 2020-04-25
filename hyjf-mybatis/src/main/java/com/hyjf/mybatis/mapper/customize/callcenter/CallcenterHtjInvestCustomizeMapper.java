/**
 * Description:按照用户名/手机号查询出借明细（汇添金）Mapper类
 * Copyright: (HYJF Corporation) 2017
 * Company: HYJF Corporation
 * @author: pcc
 * @version: 1.0
 * Created at: 2017年7月19日 上午11:01:57
 * Modification History:
 * Modified by : 
 * */
package com.hyjf.mybatis.mapper.customize.callcenter;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.callcenter.CallcenterHtjInvestCustomize;

public interface CallcenterHtjInvestCustomizeMapper {

    /**
     * 按照用户名/手机号查询出借明细（汇添金）
     * 同步另外文件BorrowInvestCustomizeMapper
     * @param CallcenterHztInvestCustomize
     * @return
     */
    List<CallcenterHtjInvestCustomize> selectBorrowInvestList(Map<String, Object> paraMap);
}