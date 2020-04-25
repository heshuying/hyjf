
package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.admin.AdminCouponRepayMonitorCustomize;

/**
 * 
 * 此处为类说明
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月12日
 * @see 下午4:52:16
 */
public interface AdminCouponRepayMonitorCustomizeMapper {

	/**
	 * 
	 * 列表查询
	 * @author hsy
	 * @param param
	 * @return
	 */
	List<AdminCouponRepayMonitorCustomize> selectRecordList(Map<String, Object> param);

	/**
	 * 
	 * 总记录数统计
	 * @author hsy
	 * @param param
	 * @return
	 */
	int countRecordTotal(Map<String, Object> param);
	
	/**
	 * 
	 * 统计
	 * @author hsy
	 * @param param
	 * @return
	 */
	List<AdminCouponRepayMonitorCustomize> selectInterestSum(Map<String, Object> param);
	
	


}
