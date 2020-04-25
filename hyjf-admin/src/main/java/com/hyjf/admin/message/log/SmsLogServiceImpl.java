package com.hyjf.admin.message.log;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.SmsOntimeWithBLOBs;
import com.hyjf.mybatis.model.customize.SmsLogCustomize;
import com.hyjf.mybatis.model.customize.SmsOntimeCustomize;

@Service
public class SmsLogServiceImpl extends BaseServiceImpl implements SmsLogService {

	@Override
	public List<SmsLogCustomize> queryLog(SmsLogCustomize sm) {
		return smsLogCustomizeMapper.queryLog(sm);
	}

	@Override
	public Integer queryLogCount(SmsLogCustomize sm) {
		return smsLogCustomizeMapper.queryLogCount(sm);
	}

	@Override
	public Integer queryTimeCount(SmsOntimeCustomize smsOntimeCustomize) {
		return smsLogCustomizeMapper.queryTimeCount(smsOntimeCustomize);
	}
	@Override
	public Integer sumContent(SmsLogCustomize sm) {
		return smsLogCustomizeMapper.sumContent(sm);
	}

	@Override
	public List<SmsOntimeCustomize> queryTime(SmsOntimeCustomize smsOntimeCustomize) {
		return smsLogCustomizeMapper.queryTime(smsOntimeCustomize);
	}
	@Override
	public List<SmsLogCustomize> queryInitSmsCount(SmsLogCustomize sm){
		return smsLogCustomizeMapper.queryInitSmsCount(sm);
	}
	@Override
	public int updateSmsOnTime(Integer id, int status) {
		SmsOntimeWithBLOBs record = new SmsOntimeWithBLOBs();
		record.setId(id);
		record.setStatus(status);
		record.setRemark("删除未发送定时短信");
		record.setStarttime(GetDate.getMyTimeInMillis());
		return this.smsOntimeMapper.updateByPrimaryKeySelective(record);
	}
}
