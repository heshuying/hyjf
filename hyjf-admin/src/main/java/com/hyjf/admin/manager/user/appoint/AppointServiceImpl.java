package com.hyjf.admin.manager.user.appoint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.AppointmentAuthLogCustomize;
import com.hyjf.mybatis.model.customize.AppointmentRecodLogCustomize;

@Service
public class AppointServiceImpl extends BaseServiceImpl implements AppointService {
	@Override
	public int countAppointRecordTotalNum(AppointBean form) {
		AppointmentAuthLogCustomize appoint=new AppointmentAuthLogCustomize();
		appoint.setAddTime(form.getAddTime());
		appoint.setUsername(form.getUsername());
		appoint.setMobile(form.getMobile());
		return appointmentAuthLogCustomizeMapper.countAppointRecordTotalNum(appoint);
	}
	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public List<AppointmentAuthLogCustomize> getAppointRecordList(AppointBean config, int limitStart, int limitEnd) {
		AppointmentAuthLogCustomize appoint=new AppointmentAuthLogCustomize();
		appoint.setAddTime(config.getAddTime());
		appoint.setUsername(config.getUsername());
		appoint.setMobile(config.getMobile());
		appoint.setLimitStart(limitStart);
		appoint.setLimitEnd(limitEnd);
		return  appointmentAuthLogCustomizeMapper.getAppointRecordList(appoint);
	}
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
	@Override
	public int countAuthAppointRecordTotalNum(AppointBean form) {
		AppointmentAuthLogCustomize appoint=new AppointmentAuthLogCustomize();
		appoint.setAddTime(form.getAddTime());
		appoint.setUsername(form.getUsername());
		if (form.getUserId()!=null) {
			appoint.setUserId(Integer.parseInt(form.getUserId()));	
		}
		return appointmentAuthLogCustomizeMapper.countAuthAppointRecordTotalNum(appoint);
	}
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
	@Override
	public List<AppointmentAuthLogCustomize> getAuthAppointRecordList(
			AppointBean form, int offset, int limit) {
		AppointmentAuthLogCustomize appoint=new AppointmentAuthLogCustomize();
		appoint.setAddTime(form.getAddTime());
		appoint.setUsername(form.getUsername());
		appoint.setLimitStart(offset);
		appoint.setLimitEnd(limit);
		if (form.getUserId()!=null) {
			appoint.setUserId(Integer.parseInt(form.getUserId()));	
		}
		return  appointmentAuthLogCustomizeMapper.getAuthAppointRecordList(appoint);
	}
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
	@Override
	public int countAppointRecordTotalNumMain(AppointBean form) {
		Map<String , Object> params=new HashMap<String , Object>();
		params.put("username", form.getUsername());
		params.put("recodNid", form.getRecodNid());
		params.put("addTime", form.getAddTime());
		params.put("addTimeEnd", form.getAddTimeEnd());
		return appointmentAuthLogCustomizeMapper.countAppointRecordTotalNumMain(params);
	}
	/**
	 * 
	 * @method: getAppointRecordListMain
	 * @description: 	违约分值			
	 *  @param form
	 *  @return 
	 * @return: int
	* @mender: zhouxiaoshuai
	 * @date:   2016年7月29日 下午5:13:10
	 */
	@Override
	public List<AppointmentAuthLogCustomize> getAppointRecordListMain(
			AppointBean form, int offset, int limit) {
		Map<String , Object> params=new HashMap<String , Object>();
		params.put("username", form.getUsername());
		params.put("mobile", form.getMobile());
		params.put("recodNid", form.getRecodNid());
		params.put("addTime", form.getAddTime());
		params.put("addTimeEnd", form.getAddTimeEnd());
		params.put("limitStart", offset);
		params.put("limitEnd", limit);
		return appointmentAuthLogCustomizeMapper.getAppointRecordListMain(params);
	}
	
	/**
	 * 
	 * @method: getAppointRecordListMain
	 * @description: 	违约分值	明细记录		
	 *  @param form
	 *  @return 
	 * @return: int
	* @mender: zhouxiaoshuai
	 * @date:   2016年7月29日 下午5:13:10
	 */
	@Override
	public int countAppointRecordTotalRecordNumMain(AppointBean form) {
		Map<String , Object> params=new HashMap<String , Object>();
		params.put("userId", form.getUserId());
		params.put("username", form.getUsername());
		params.put("recodNid", form.getRecodNid());
		params.put("addTime", form.getAddTime());
		params.put("addTimeEnd", form.getAddTimeEnd());
		return appointmentAuthLogCustomizeMapper.countAppointRecordTotalRecordNumMain(params);
	}
	/**
	 * 
	 * @method: getAppointRecordListMain
	 * @description: 	违约分值	明细记录		
	 *  @param form
	 *  @return 
	 * @return: int
	* @mender: zhouxiaoshuai
	 * @date:   2016年7月29日 下午5:13:10
	 */
	@Override
	public List<AppointmentRecodLogCustomize> getAppointRecordListRecordMain(
			AppointBean form, int offset, int limit) {
		 Map<String , Object> params=new HashMap<String , Object>();
		 	params.put("userId", form.getUserId());
			params.put("username", form.getUsername());
			params.put("recodNid", form.getRecodNid());
			params.put("addTime", form.getAddTime());
			params.put("addTimeEnd", form.getAddTimeEnd());
			params.put("limitStart", offset);
			params.put("limitEnd", limit);
			return appointmentAuthLogCustomizeMapper.getAppointRecordListRecordMain(params);
	}


	

}
