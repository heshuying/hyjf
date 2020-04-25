package com.hyjf.admin.manager.plan.joindetail;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.AdminPlanAccedeDetailCustomize;

/**
 * 汇添金加入明细Service
 * 
 * @ClassName HtjJoinDetailService
 * @author liuyang
 * @date 2016年9月29日 上午9:47:35
 */
public interface HtjJoinDetailService extends BaseService {

	/**
	 * 检索加入明细件数
	 * 
	 * @Title countAccedeRecord
	 * @param form
	 * @return
	 */
	public int countAccedeRecord(HtjJoinDetailBean form);

	/**
	 * 检索加入明细列表
	 * 
	 * @Title selectAccedeRecordList
	 * @param form
	 * @return
	 */
	public List<AdminPlanAccedeDetailCustomize> selectAccedeRecordList(HtjJoinDetailBean form);

	/**
	 * 检索总计加入金额
	 * 
	 * @Title sumJoinAccount
	 * @param form
	 * @return
	 */
	public String sumJoinAccount(HtjJoinDetailBean form);

}
