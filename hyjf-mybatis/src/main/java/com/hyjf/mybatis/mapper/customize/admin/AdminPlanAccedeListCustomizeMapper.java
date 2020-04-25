package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.admin.AdminPlanAccedeListCustomize;

/**
 * 汇计划加入明细
 * 
 * @ClassName AdminPlanAccedeListCustomizeMapper
 * @author LIBIN
 * @date 2017年8月16日 上午11:24:46
 */
public interface AdminPlanAccedeListCustomizeMapper {

	/**
	 * 检索加入明细件数
	 * 
	 * @Title countAccedeRecord
	 * @param param
	 * @return
	 */
	int countAccedeRecord(Map<String, Object> param);
	
	/**
	 * 检索加入明细列表
	 * 
	 * @Title selectAccedeRecordList
	 * @param param
	 * @return
	 */         
	List<AdminPlanAccedeListCustomize> selectAccedeRecordList(Map<String, Object> param);

	/**
	 * 检索加入明细异常件数
	 * 
	 * @Title countAccedeRecord
	 * @param param
	 * @return
	 */
	int countAccedeExceptionRecord(Map<String, Object> param);
	
	/**
	 * 检索加入明细异常列表
	 * 
	 * @Title selectAccedeRecordList
	 * @param param
	 * @return
	 */         
	List<AdminPlanAccedeListCustomize> selectAccedeExceptionList(Map<String, Object> param);
	/**
	 * 检索加入明细列表导出
	 * 
	 * @Title selectAccedeRecordList
	 * @param param
	 * @return
	 */         
	List<AdminPlanAccedeListCustomize> selectAccedeRecordListExport(Map<String, Object> param);
	/**
	 * 金额值合计
	 * 
	 * @Title sumAccedeRecord
	 * @param param
	 * @return
	 */
	AdminPlanAccedeListCustomize sumAccedeRecord(Map<String, Object> param);
}
