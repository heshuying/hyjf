package com.hyjf.wechat.service.weekly;

import java.util.List;

import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.BorrowTenderCpn;
import com.hyjf.mybatis.model.auto.CreditRepay;
import com.hyjf.mybatis.model.auto.CreditTender;
import com.hyjf.mybatis.model.auto.Events;
import com.hyjf.mybatis.model.auto.HjhAccede;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.WeeklyReport;


public interface WeeklyService extends BaseService{

	//获取用户信息
	Users getUsers(int userid);
	//获取用户信息
	UsersInfo getUsersInfo(int userid);
    //获取用户总资产
	Account getAccount(int userid);
	
	//获取出借金额和预期金额
	List<BorrowTender> getBorrowTender(int userid,int begin,int end);
	//获取出借金额和预期金额
	List<CreditTender> getCreditTender(int userid,String begin,String end);
	//获取还款总额
	List<BorrowRecover> getBorrowRecover(int userid,String begin,String end);
	//获取还款总额
	List<CreditRepay> getCreditRepay(int userid,int begin,int end);
	//获取债转总额
	List<CreditRepay> getCreditRepayToCredit(int userid,int begin,int end);
	//获取用户可用优惠券
	boolean coupon(int userid);
	//获取活动列表
	List<ActivityList> getActivity(int day);
	//获取公司纪事
	List<Events> getEvents(int begin,int end,int year);
	//获取公司纪事
	Events getEventsAll(int begin,int end);
	//查询出借百分比
	Events selectPercentage(int percentage,int begin,int end,int userId);
	//查询是否存在数据
	List<WeeklyReport> getWeeklyReport(int userId,String beginDate);
	//插入数据
	void inWeeklyReport(WeeklyReport weeklyReport);
	
	//获取汇计划预计
	List<HjhAccede> getAccede(int userid,int begin,int end);
	//获取优惠券 预期金额
	List<BorrowTenderCpn> getBorrowTenderCPN(int userid,int begin,int end);
}
