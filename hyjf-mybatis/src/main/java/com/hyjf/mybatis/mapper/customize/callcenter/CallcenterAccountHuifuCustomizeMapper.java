/**
 * Description:按照用户名/手机号查询汇付绑卡关系Mapper类
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
import java.util.Map;

import com.hyjf.mybatis.model.customize.callcenter.CallcenterAccountHuifuCustomize;



public interface CallcenterAccountHuifuCustomizeMapper {

	/**
	 * 取得汇付绑卡关系
	 * 同步另外文件AdminUsersCustomizeMapper
	 * @param user
	 * @return
	 */
	List<CallcenterAccountHuifuCustomize> selectBankCardList(Map<String, Object> bankCardUser);
}

	