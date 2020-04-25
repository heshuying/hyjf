package com.hyjf.admin.manager.activity.actdec2017.corps;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.ActdecCorps;
import com.hyjf.mybatis.model.auto.ActdecCorpsExample;
import com.hyjf.mybatis.model.auto.ActdecCorpsExample.Criteria;
import com.hyjf.mybatis.model.auto.ActdecWinning;
import com.hyjf.mybatis.model.auto.ActdecWinningExample;


@Service
public class CorpsServiceImpl extends BaseServiceImpl implements CorpsService {
	



	@Override
	public int countRecordListDetail(CorpsBean cb) {
		
		ActdecWinningExample example = new ActdecWinningExample();
		ActdecWinningExample.Criteria creteria = example.createCriteria();
		
		if(StringUtils.isNotEmpty(cb.getUsernameSrch())){
			creteria.andUserNameEqualTo(cb.getUsernameSrch());
		}
		if(StringUtils.isNotEmpty(cb.getMobileSrch())){
			creteria.andMobileEqualTo(cb.getMobileSrch());
		}
		if(StringUtils.isNotEmpty(cb.getWxNameSrch())){
			creteria.andWinningNameEqualTo(cb.getWxNameSrch());
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
			creteria.andTypeEqualTo(Integer.valueOf(cb.getTypeSrch()));
		}
		if(StringUtils.isNotEmpty(cb.getCouponNameSrch())){
			creteria.andWinningTypeEqualTo(Integer.valueOf(cb.getCouponNameSrch()));
		}
		return actdecWinningMapper.countByExample(example);
	}

	@Override
	public List<ActdecWinning> selectRecordListDetail(CorpsBean cb, int limitStart, int limitEnd) {
		ActdecWinningExample example = new ActdecWinningExample();
		ActdecWinningExample.Criteria creteria = example.createCriteria();

		
		if(StringUtils.isNotEmpty(cb.getUsernameSrch())){
			creteria.andUserNameEqualTo(cb.getUsernameSrch());
		}
		if(StringUtils.isNotEmpty(cb.getMobileSrch())){
			creteria.andMobileEqualTo(cb.getMobileSrch());
		}
		if(StringUtils.isNotEmpty(cb.getWxNameSrch())){
			creteria.andWinningNameEqualTo(cb.getWxNameSrch());
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
			creteria.andTypeEqualTo(Integer.valueOf(cb.getTypeSrch()));
		}
		if(StringUtils.isNotEmpty(cb.getCouponNameSrch())){
			creteria.andWinningTypeEqualTo(Integer.valueOf(cb.getCouponNameSrch()));
		}
//		if(StringUtils.isNotEmpty(cb.getTimeStartSrch())||StringUtils.isNotEmpty(cb.getTimeEndSrch())){
//			creteria.andCreateTimeBetween(Integer.valueOf(cb.getTimeStartSrch()), Integer.valueOf(cb.getTimeEndSrch()));
//		}
		example.setLimitStart(limitStart);
        example.setLimitEnd(limitEnd);
        example.setOrderByClause("create_time desc");
		return actdecWinningMapper.selectByExample(example);
	}

	@Override
	public int updateRecordListD(int type) {

		ActdecCorpsExample example =new ActdecCorpsExample();
		Criteria creteria = example.createCriteria();
		
		if(type==1) {
			example.setOrderByClause("RAND() LIMIT 100");
			creteria.andTeamTypeEqualTo(3);
			creteria.andPrizeTypeEqualTo(0);
			ActdecCorps record=new ActdecCorps();
			record.setPrizeType(1);
			return actdecCorpsCustomizeMapper.updateByExampleSelective(record, example);
		}
		if(type==2) {
			example.setOrderByClause("RAND() LIMIT 1");
			creteria.andTeamTypeEqualTo(3);
			creteria.andPrizeTypeEqualTo(0);
			ActdecCorps record=new ActdecCorps();
			record.setPrizeType(2);
			return actdecCorpsCustomizeMapper.updateByExampleSelective(record, example);
		}
		return 0;

	}

}
