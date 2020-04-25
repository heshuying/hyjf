package com.hyjf.wechat.service.weekly;

import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.auto.BorrowTenderExample.Criteria;
import com.hyjf.mybatis.model.customize.ContentEventsCustomize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("weeklyService")
public class WeeklyServiceImpl extends BaseServiceImpl implements WeeklyService{

	@Override
	public Users getUsers(int userid) {
		return usersMapper.selectByPrimaryKey(userid);
	}

	@Override
	public Account getAccount(int userid) {
		AccountExample  example = new AccountExample();
		example.or().andUserIdEqualTo(userid);
		return accountMapper.selectByExample(example).get(0);
	}

	@Override
	public List<BorrowTender> getBorrowTender(int userid, int begin, int end) {
		BorrowTenderExample example = new BorrowTenderExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userid);
		criteria.andStatusEqualTo(1);
		criteria.andAddtimeBetween(begin, end);
		criteria.andAccedeOrderIdIsNull();
		example.setOrderByClause(" addtime asc ");
		return borrowTenderMapper.selectByExample(example);
	}

	@Override
	public List<CreditTender> getCreditTender(int userid, String begin, String end) {
		CreditTenderExample example = new CreditTenderExample();
		com.hyjf.mybatis.model.auto.CreditTenderExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userid);
//		criteria.andStatusEqualTo(1);
		criteria.andAddTimeBetween(begin, end);
		example.setOrderByClause(" add_time asc ");
		return creditTenderMapper.selectByExample(example);
	}

	@Override
	public List<BorrowRecover> getBorrowRecover(int userid, String begin, String end) {
		BorrowRecoverExample example = new BorrowRecoverExample();
		com.hyjf.mybatis.model.auto.BorrowRecoverExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userid);
		criteria.andRecoverYestimeBetween(begin, end);
		example.setOrderByClause(" recover_yestime asc ");
		return borrowRecoverMapper.selectByExample(example);
	}

	@Override
	public List<CreditRepay> getCreditRepay(int userid, int begin, int end) {
		CreditRepayExample example = new CreditRepayExample();
		com.hyjf.mybatis.model.auto.CreditRepayExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userid);
		criteria.andAssignRepayYesTimeBetween(begin, end);
		example.setOrderByClause(" assign_repay_yes_time asc ");
		return creditRepayMapper.selectByExample(example);
	}

	@Override
	public boolean coupon(int userid) {
		CouponUserExample example = new CouponUserExample();
		com.hyjf.mybatis.model.auto.CouponUserExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userid);
		criteria.andUsedFlagEqualTo(0);
		if(couponUserMapper.selectByExample(example).size()!=0) {
			return true;
		}else {
			return false;
		}
		
	}

	@Override
	public List<ActivityList> getActivity(int day) {
		ActivityListExample example = new ActivityListExample();
		com.hyjf.mybatis.model.auto.ActivityListExample.Criteria criteria = example.createCriteria();
		criteria.andTimeStartLessThan(day);
		criteria.andTimeEndGreaterThan(day);
		example.setOrderByClause(" create_time desc ");
		return activityListMapper.selectByExample(example);
	}

	@Override
	public List<Events> getEvents(int begin, int end,int year) {
		 ContentEventsCustomize contentEventsCustomize=new ContentEventsCustomize();
		 contentEventsCustomize.setStartCreate(begin);
		 contentEventsCustomize.setEndCreate(end);
		 contentEventsCustomize.setEventYear(year);
		return contentEventsCustomizeMapper.selectContentEvents(contentEventsCustomize);
	}

	@Override
	public Events getEventsAll(int begin, int end) {
		 ContentEventsCustomize contentEventsCustomize=new ContentEventsCustomize();
		 contentEventsCustomize.setStartCreate(begin);
		 contentEventsCustomize.setEndCreate(end);
		return contentEventsCustomizeMapper.selectZong(contentEventsCustomize);
	}

	@Override
	public Events selectPercentage(int percentage,int begin,int end,int userId) {
		 ContentEventsCustomize contentEventsCustomize=new ContentEventsCustomize();
		 contentEventsCustomize.setEventYear(percentage);
		 contentEventsCustomize.setActTime(userId);
		 contentEventsCustomize.setStartCreate(begin);
		 contentEventsCustomize.setEndCreate(end);
		return contentEventsCustomizeMapper.selectPercentage(contentEventsCustomize);
	}

	@Override
	public List<WeeklyReport> getWeeklyReport(int userId,String beginDate) {
		WeeklyReportExample example=new WeeklyReportExample();
	    com.hyjf.mybatis.model.auto.WeeklyReportExample.Criteria criteria = example.createCriteria();
	    criteria.andUserIdEqualTo(userId);
	    criteria.andBeginDateEqualTo(beginDate);
		return weeklyReportMapper.selectByExample(example);
	}

	@Override
	public void inWeeklyReport(WeeklyReport weeklyReport) {
		weeklyReportMapper.insert(weeklyReport);
	}

	@Override
	public List<HjhAccede> getAccede(int userid, int begin,int end) {
		HjhAccedeExample example =new HjhAccedeExample();
		com.hyjf.mybatis.model.auto.HjhAccedeExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userid);
		criteria.andAddTimeBetween(begin, end);
		
		return hjhAccedeMapper.selectByExample(example);
	}

	@Override
	public List<CreditRepay> getCreditRepayToCredit(int userid, int begin, int end) {
		CreditRepayExample example = new CreditRepayExample();
		com.hyjf.mybatis.model.auto.CreditRepayExample.Criteria criteria = example.createCriteria();
		criteria.andCreditUserIdEqualTo(userid);
		criteria.andAssignRepayYesTimeBetween(begin, end);
		return creditRepayMapper.selectByExample(example);
	}
	@Override
	public List<BorrowTenderCpn> getBorrowTenderCPN(int userid, int begin, int end) {
		BorrowTenderCpnExample example = new BorrowTenderCpnExample();
		com.hyjf.mybatis.model.auto.BorrowTenderCpnExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userid);
		criteria.andAddtimeBetween(begin, end);
		example.setOrderByClause(" addtime asc ");
		return borrowTenderCpnMapper.selectByExample(example);
	}

	@Override
	public UsersInfo getUsersInfo(int userid) {
		UsersInfoExample example =new UsersInfoExample();
		example.or().andUserIdEqualTo(userid);
		return usersInfoMapper.selectByExample(example ).get(0);
	}




}
