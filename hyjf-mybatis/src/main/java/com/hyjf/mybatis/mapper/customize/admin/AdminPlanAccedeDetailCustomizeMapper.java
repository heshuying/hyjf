package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.admin.AdminPlanAccedeDetailCustomize;

/**
 * 汇添金加入明细
 * 
 * @ClassName AdminPlanAccedeDetailCustomizeMapper
 * @author liuyang
 * @date 2016年9月29日 上午11:24:46
 */
public interface AdminPlanAccedeDetailCustomizeMapper {

	/**
	 * 检索加入明细件数
	 * 
	 * @Title countAccedeRecord
	 * @param param
	 * @return
	 */
	public int countAccedeRecord(Map<String, Object> param);

	/**
	 * 检索加入明细列表
	 * 
	 * @Title selectAccedeRecordList
	 * @param param
	 * @return
	 */
	public List<AdminPlanAccedeDetailCustomize> selectAccedeRecordList(Map<String, Object> param);

	/**
	 * 检索加入金额总计
	 * 
	 * @Title sumJoinAccount
	 * @param param
	 * @return
	 */
	public String sumJoinAccount(Map<String, Object> param);

}
