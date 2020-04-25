/**
 * Description:用户开户列表前端显示查询所用po
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年11月11日 下午2:17:31
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.api.aems.repay;

import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.mybatis.model.customize.web.AemsWebUserRepayProjectListCustomize;

import java.util.List;

/**
 * @author cwyang
 */

public class AemsUserRepayProjectBean extends BaseResultBean {


	/**
	 * 
	 */
	private static final long serialVersionUID = -4205305842872752342L;

	private List<AemsWebUserRepayProjectListCustomize> detailList;

	public List<AemsWebUserRepayProjectListCustomize> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<AemsWebUserRepayProjectListCustomize> detailList) {
		this.detailList = detailList;
	}
}
