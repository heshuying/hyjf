/**
 * Description:按照用户名/手机号查询出借明细（直投产品）Mapper类
 * Copyright: (HYJF Corporation) 2017
 * Company: HYJF Corporation
 * @author: 刘彬
 * @version: 1.0
 * Created at: 2017年7月19日 上午11:01:57
 * Modification History:
 * Modified by : 
 * */
package com.hyjf.mybatis.mapper.customize.callcenter;

import java.util.List;

import com.hyjf.mybatis.model.customize.callcenter.CallcenterHztInvestCustomize;

public interface CallcenterHztInvestCustomizeMapper {

	/**
	 * 取得汇直投出借信息
	 * 同步另外文件BorrowInvestCustomizeMapper
	 * @param CallcenterHztInvestCustomize
	 * @return
	 */
	List<CallcenterHztInvestCustomize> selectBorrowInvestList(CallcenterHztInvestCustomize callcenterHztInvestCustomize);
}