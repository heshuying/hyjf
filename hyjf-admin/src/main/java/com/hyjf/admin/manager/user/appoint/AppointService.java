package com.hyjf.admin.manager.user.appoint;

import java.util.List;

import com.hyjf.mybatis.model.customize.AppointmentAuthLogCustomize;
import com.hyjf.mybatis.model.customize.AppointmentRecodLogCustomize;

public interface AppointService {
	
/**
 * 
 * @method: countRecordTotal
 * @description: 	记录总数		
 *  @param form
 *  @return 
 * @return: int
* @mender: zhouxiaoshuai
 * @date:   2016年7月28日 下午4:30:51
 */
public int countAppointRecordTotalNum(AppointBean form);
/**
 * 
 * @method: getRecordList
 * @description: 	查询的记录		
 *  @param form
 *  @param offset
 *  @param limit
 *  @return 
 * @return: List<AppointmentAuthLog>
* @mender: zhouxiaoshuai
 * @date:   2016年7月28日 下午4:32:50
 */
public List<AppointmentAuthLogCustomize> getAppointRecordList(AppointBean form, int offset,
		int limit);
/**
 * 
 * @method: countAuthAppointRecordTotalNum
 * @description: 详情明细页面		
 *  @param form
 *  @return 
 * @return: int
* @mender: zhouxiaoshuai
 * @date:   2016年7月29日 上午10:14:46
 */
public int countAuthAppointRecordTotalNum(AppointBean form);

/**
 * 
 * @method: countAuthAppointRecordTotalNum
 * @description: 详情明细页面		
 *  @param form
 *  @return 
 * @return: int
* @mender: zhouxiaoshuai
 * @date:   2016年7月29日 上午10:14:46
 */
public List<AppointmentAuthLogCustomize> getAuthAppointRecordList(
		AppointBean form, int offset, int limit);
/**
 * 
 * @method: countAppointRecordTotalNumMain
 * @description: 	违约分值		
 *  @param form
 *  @return 
 * @return: int
* @mender: zhouxiaoshuai
 * @date:   2016年7月29日 下午5:13:10
 */
public int countAppointRecordTotalNumMain(AppointBean form);
/**
 * 
 * @method: getAppointRecordListMain
 * @description: 违约分值		
 *  @param form
 *  @return 
 * @return: int
* @mender: zhouxiaoshuai
 * @date:   2016年7月29日 下午5:13:10
 */
public List<AppointmentAuthLogCustomize> getAppointRecordListMain(
		AppointBean form, int offset, int limit);

/**
 * 
 * @method: getAppointRecordListMain
 * @description: 违约分值	明细记录	
 *  @param form
 *  @return 
 * @return: int
* @mender: zhouxiaoshuai
 * @date:   2016年7月29日 下午5:13:10
 */
public int countAppointRecordTotalRecordNumMain(AppointBean form);

/**
 * 
 * @method: getAppointRecordListMain
 * @description: 违约分值	明细记录	
 *  @param form
 *  @return 
 * @return: int
* @mender: zhouxiaoshuai
 * @date:   2016年7月29日 下午5:13:10
 */
public List<AppointmentRecodLogCustomize> getAppointRecordListRecordMain(
		AppointBean form, int offset, int limit);
}
