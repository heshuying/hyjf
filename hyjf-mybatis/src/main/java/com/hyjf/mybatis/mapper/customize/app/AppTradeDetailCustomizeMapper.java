/**
 * Description:交易明细
 * Copyright: (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 上午11:01:57
 * Modification History:
 * Modified by : 
 * */

package com.hyjf.mybatis.mapper.customize.app;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.app.AppTradeListCustomize;

public interface AppTradeDetailCustomizeMapper {

	int countTradeDetailListRecordTotal(Map<String, Object> params);

	List<AppTradeListCustomize> searchTradeDetailList(Map<String, Object> params);


}
