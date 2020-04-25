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

import com.hyjf.mybatis.model.auto.BorrowTenderTmp;
import com.hyjf.mybatis.model.customize.admin.TenderCancelQueryParam;

public interface AdminBorrowTenderTmpMapper {

	/**
	 * 查询出借中的
	 * @param example
	 * @return
	 */
	List<BorrowTenderTmp> selectByExample(TenderCancelQueryParam example);
}
