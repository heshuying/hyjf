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

import java.util.List;

/**
 * 还款详情列表
 * @author Zha Daojian
 * @date 2018/9/25 17:45
 * @param
 * @return
 **/

public class AemsUserRepayInfoBean extends BaseResultBean {


	/**
	 *
	 */
	private static final long serialVersionUID = -4205305842872752342L;

	/**
	 * 还款详情列表
	 */
	private List<AemsProjectRepayListBean> detailList;
	/**
	 * 构造方法
	 */
	public AemsUserRepayInfoBean() {
		super();
	}
	
	public List<AemsProjectRepayListBean> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<AemsProjectRepayListBean> detailList) {
		this.detailList = detailList;
	}

}
