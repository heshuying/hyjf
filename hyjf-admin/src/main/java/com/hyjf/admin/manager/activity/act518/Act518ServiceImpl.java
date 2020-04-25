package com.hyjf.admin.manager.activity.act518;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.ActdecFinancing;
import com.hyjf.mybatis.model.auto.ActdecFinancingExample;
import com.hyjf.mybatis.model.customize.ActdecFinancingCustomizeExample;


@Service
public class Act518ServiceImpl extends BaseServiceImpl implements Act518Service {
	



	@Override
	public int countRecordListDetail(Act518Bean cb) {
		
		ActdecFinancingExample example = new ActdecFinancingExample();
		ActdecFinancingExample.Criteria creteria = example.createCriteria();
		
		if(StringUtils.isNotEmpty(cb.getUsernameSrch())){
			creteria.andUserNameEqualTo(cb.getUsernameSrch());
		}
		if(StringUtils.isNotEmpty(cb.getMobileSrch())){
			creteria.andMobileEqualTo(cb.getMobileSrch());
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if(StringUtils.isNotEmpty(cb.getTimeStartSrch())){
				Date date = simpleDateFormat.parse(cb.getTimeStartSrch());
				creteria.andCreateTimeGreaterThan((int) (date.getTime()/1000));
			}
			if(StringUtils.isNotEmpty(cb.getTimeEndSrch())){
				Date date = simpleDateFormat.parse(cb.getTimeEndSrch());
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				cal.add(Calendar.DATE, 1);
				creteria.andCreateTimeLessThan((int) ((cal.getTime()).getTime()/1000));
			}

		} catch (ParseException e) {
		}

		if(StringUtils.isNotEmpty(cb.getTypeSrch())){
			creteria.andFaceValueEqualTo(cb.getTypeSrch());
		}
		return actdecFinancingMapper.countByExample(example);
	}

	@Override
	public List<ActdecFinancing> selectRecordListDetail(Act518Bean cb, int limitStart, int limitEnd) {
		ActdecFinancingCustomizeExample example = new ActdecFinancingCustomizeExample();
		ActdecFinancingCustomizeExample.Criteria creteria = example.createCriteria();

		
		if(StringUtils.isNotEmpty(cb.getUsernameSrch())){
			creteria.andUserNameEqualTo(cb.getUsernameSrch());
		}
		if(StringUtils.isNotEmpty(cb.getMobileSrch())){
			creteria.andMobileEqualTo(cb.getMobileSrch());
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if(StringUtils.isNotEmpty(cb.getTimeStartSrch())){
				Date date = simpleDateFormat.parse(cb.getTimeStartSrch());
				creteria.andCreateTimeGreaterThan((int) (date.getTime()/1000));
			}
			if(StringUtils.isNotEmpty(cb.getTimeEndSrch())){
				Date date = simpleDateFormat.parse(cb.getTimeEndSrch());
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				cal.add(Calendar.DATE, 1);
				creteria.andCreateTimeLessThan((int) ((cal.getTime()).getTime()/1000));
			}

		} catch (ParseException e) {
		}

		if(StringUtils.isNotEmpty(cb.getTypeSrch())){
			creteria.andFaceValueEqualTo(cb.getTypeSrch());
		}
		example.setLimitStart(limitStart);
        example.setLimitEnd(limitEnd);
        example.setOrderByClause("hyjf_actdec_financing.create_time desc");
		return actdecFinancingCustomizeMapper.selectByExample(example);
	}


}
