package com.hyjf.mybatis.mapper.customize;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.AppointmentAuthLogCustomize;
import com.hyjf.mybatis.model.customize.AppointmentRecodLogCustomize;


public interface AppointmentAuthLogCustomizeMapper {
	int countAppointRecordTotalNum(AppointmentAuthLogCustomize appointmentAuthLogCustomize);

	List<AppointmentAuthLogCustomize> getAppointRecordList(
			AppointmentAuthLogCustomize appoint);

	int countAuthAppointRecordTotalNum(AppointmentAuthLogCustomize appoint);

	List<AppointmentAuthLogCustomize> getAuthAppointRecordList(
			AppointmentAuthLogCustomize appoint);

	int countAppointRecordTotalNumMain(Map<String, Object> params);

	List<AppointmentAuthLogCustomize> getAppointRecordListMain(
			Map<String, Object> params);

	List<AppointmentRecodLogCustomize> getAppointRecordListRecordMain(
			Map<String, Object> params);

	int countAppointRecordTotalRecordNumMain(Map<String, Object> params);

}
