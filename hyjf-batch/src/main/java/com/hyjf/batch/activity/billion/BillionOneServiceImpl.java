package com.hyjf.batch.activity.billion;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.ActivityBillionOne;
import com.hyjf.mybatis.model.auto.ActivityBillionSecond;
import com.hyjf.mybatis.model.auto.ActivityBillionSecondExample;
import com.hyjf.mybatis.model.auto.ActivityBillionSecondTime;
import com.hyjf.mybatis.model.auto.ActivityBillionSecondTimeExample;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.BorrowTenderExample;
import com.hyjf.mybatis.model.auto.CalculateInvestInterest;
import com.hyjf.mybatis.model.auto.CalculateInvestInterestExample;
import com.hyjf.mybatis.model.auto.CreditTender;
import com.hyjf.mybatis.model.auto.CreditTenderExample;
import com.hyjf.mybatis.model.auto.DebtInvest;
import com.hyjf.mybatis.model.auto.DebtInvestExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.BillionSecondCustomize;
import com.hyjf.soa.apiweb.CommonParamBean;
import com.hyjf.soa.apiweb.CommonSoaUtils;

/**
 * 满心满亿
 * @author Michael
 */
@Service
public class BillionOneServiceImpl extends BaseServiceImpl implements BillionOneService {

	
	/**
	 * 获取统计数据
	 * @return
	 * @author Michael
	 */
	@Override
	public CalculateInvestInterest getCalculateRecord() {
		List<CalculateInvestInterest> recordList =  calculateInvestInterestMapper.selectByExample(new CalculateInvestInterestExample());
		if(recordList != null && recordList.size() > 0){
			return recordList.get(0);
		}
		return null;
	}

    /**
     * 生成中奖用户
     * num  1:100亿
     * 		2:101亿
     * 		3:102亿
     * 		4:103亿
     * 		5:104亿
     * 		6:105亿
     */
	@Override
	public void prizeGenerate(int num, Date updateTime) {
		if(num == 0){
			return;
		}
		int uTime = 0;
		//格式化时间
		if(updateTime != null){
			uTime = GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.dateToString(updateTime));
		}else{
			return;
		}
		//存储满亿时间
		ActivityBillionSecondTime timeRecord = this.activityBillionSecondTimeMapper.selectByPrimaryKey(num);
		//已经更新了时间，不要重复更新
		if(timeRecord.getAccordTime() != null ){
			return;
		}else{
			timeRecord.setAccordTime(uTime);
			this.activityBillionSecondTimeMapper.updateByPrimaryKeySelective(timeRecord);
		}
		//达到105亿时  只更新时间不存中奖信息
		if(num == 6){
			return;
		}
		//获取奖品记录
		ActivityBillionOne record = this.activityBillionOneMapper.selectByPrimaryKey(num);
		if(record == null){
			return;
		}
		//已生成完奖品
		if(record.getStatus() == 2){
			return;
		}
		//根据时间获取borrow_tender 记录(汇直投)
		BorrowTender borrowTender = getBorrowRecords(uTime);
		//根据时间获取credit_tender 记录(汇转让)
		CreditTender creditTender = getCreditRecords(uTime);
		//根据时间获取hyjf_debt_invest 记录(汇添金)
		DebtInvest debtInvest = getDebtInvestRecords(uTime);
		/**
		 * 更新中奖记录
		 * 有相同中奖用户  选择出借金额大的
		 */
		boolean flagBorrow = false;
		boolean flagCredit = false;
		boolean flagDebt = false;
		if(borrowTender != null){
			flagBorrow = true;
		}
		if(creditTender != null ){
			flagCredit = true;
		}
		if(debtInvest != null){
			flagDebt = true;
		}
		//取最大值用户
		if(flagBorrow && flagCredit && flagDebt){
			if(borrowTender.getAccount().compareTo(creditTender.getAssignAccount()) > 0){
				if(borrowTender.getAccount().compareTo(debtInvest.getAccount()) > 0){
					updateBorrowRecord(record, borrowTender);
				}else{
					updateDebtInvestRecord(record, debtInvest);
				}
			}else{
				if(creditTender.getAssignAccount().compareTo(debtInvest.getAccount()) > 0){
					updateCreditRecord(record, creditTender);
				}else{
					updateDebtInvestRecord(record, debtInvest);
				}
			}
		}else if(flagBorrow && flagCredit && !flagDebt){
			if(borrowTender.getAccount().compareTo(creditTender.getAssignAccount()) > 0){
				updateBorrowRecord(record, borrowTender);
			}else{
				updateCreditRecord(record, creditTender);
			}
		}else if(flagBorrow && !flagCredit && flagDebt ){
			if(borrowTender.getAccount().compareTo(debtInvest.getAccount()) > 0){
				updateBorrowRecord(record, borrowTender);
			}else{
				updateDebtInvestRecord(record, debtInvest);
			}
		}else if(!flagBorrow && flagCredit && flagDebt ){
			if(creditTender.getAssignAccount().compareTo(debtInvest.getAccount()) > 0){
				updateCreditRecord(record, creditTender);
			}else{
				updateDebtInvestRecord(record, debtInvest);
			}
		}else if(flagBorrow && !flagCredit && !flagDebt){
			updateBorrowRecord(record, borrowTender);
		}else if(!flagBorrow && flagCredit && !flagDebt){
			updateCreditRecord(record, creditTender);
		}else if(!flagBorrow && !flagCredit && flagDebt){
			updateDebtInvestRecord(record, debtInvest);
		}else {//在时间点没有取到数据
			//都为null，取最近一条数据
			BorrowTenderExample  example = new BorrowTenderExample();
			BorrowTenderExample.Criteria cra = example.createCriteria();
			cra.andAddtimeLessThanOrEqualTo(uTime);
			example.setOrderByClause("addtime desc");
			example.setLimitStart(0);
			example.setLimitEnd(1);
			List<BorrowTender> list = this.borrowTenderMapper.selectByExample(example);
			if(list != null && list.size() > 0){
				borrowTender = list.get(0);
			}
			updateBorrowRecord(record, borrowTender);
		}
		//更新中奖记录
		this.activityBillionOneMapper.updateByPrimaryKeySelective(record);
		//更新下个阶段状态
		if(num < 5){
			ActivityBillionOne nextRecord =  this.activityBillionOneMapper.selectByPrimaryKey(num + 1);
			if(nextRecord != null){
				nextRecord.setStatus(1);//下个阶段为开奖中
				this.activityBillionOneMapper.updateByPrimaryKeySelective(nextRecord);
			}
		}
		
	}

	/**
	 * 获取直投记录
	 * @param updateTime
	 * @return
	 */
    public BorrowTender getBorrowRecords(int updateTime){
		BorrowTenderExample  example = new BorrowTenderExample();
		BorrowTenderExample.Criteria cra = example.createCriteria();
		cra.andAddtimeEqualTo(updateTime);
		example.setOrderByClause("account desc");
		example.setLimitStart(0);
		example.setLimitEnd(1);
		List<BorrowTender> list = this.borrowTenderMapper.selectByExample(example);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
    }
    
	/**
	 * 获取汇转让记录
	 * @param updateTime
	 * @return
	 */
    public CreditTender getCreditRecords(int updateTime){
    	CreditTenderExample  example = new CreditTenderExample();
    	CreditTenderExample.Criteria cra = example.createCriteria();
		cra.andAddTimeEqualTo(String.valueOf(updateTime));
		example.setOrderByClause("assign_account desc");
		example.setLimitStart(0);
		example.setLimitEnd(1);
		List<CreditTender> list = this.creditTenderMapper.selectByExample(example);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
    }
	/**
	 * 获取汇添金记录
	 * @param updateTime
	 * @return
	 */
    public DebtInvest getDebtInvestRecords(int updateTime){
    	DebtInvestExample  example = new DebtInvestExample();
    	DebtInvestExample.Criteria cra = example.createCriteria();
		cra.andCreateTimeEqualTo(updateTime);
		example.setOrderByClause("account desc");
		example.setLimitStart(0);
		example.setLimitEnd(1);
		List<DebtInvest> list = this.debtInvestMapper.selectByExample(example);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
    }
    
    /**
     * 更新中奖用户
     * @param record
     * @param borrowTender
     */
    public void updateBorrowRecord(ActivityBillionOne record,BorrowTender borrowTender ){
		Users user = this.getUsersByUserId(borrowTender.getUserId());
		UsersInfo userInfo = this.getUsersInfoByUserId(borrowTender.getUserId());
		record.setUserId(borrowTender.getUserId());
		record.setUserName(user.getUsername());
		record.setTrueName(userInfo.getTruename());
		record.setMobile(user.getMobile());
		record.setTenderTime(borrowTender.getAddtime());
		record.setTenderMoney(borrowTender.getAccount());
		record.setBorrowNid(borrowTender.getBorrowNid());
		record.setStatus(2);//已开奖
		record.setCreateTime(GetDate.getNowTime10());
		String nid = borrowTender.getBorrowNid();
		nid = nid.substring(0, 3);
		if("NEW".equals(nid)){
			record.setProjectTypeName("新手汇");
		}else if("ZXH".equals(nid)){
			record.setProjectTypeName("尊享汇");
		}else{
			record.setProjectTypeName("汇直投");
		}
    }
    
    /**
     * 更新中奖用户
     * @param record
     * @param borrowTender
     */
    public void updateDebtInvestRecord(ActivityBillionOne record,DebtInvest debtInvest ){
		Users user = this.getUsersByUserId(debtInvest.getUserId());
		UsersInfo userInfo = this.getUsersInfoByUserId(debtInvest.getUserId());
		record.setUserId(debtInvest.getUserId());
		record.setUserName(user.getUsername());
		record.setTrueName(userInfo.getTruename());
		record.setMobile(user.getMobile());
		record.setTenderTime(debtInvest.getCreateTime());
		record.setTenderMoney(debtInvest.getAccount());
		record.setBorrowNid(debtInvest.getBorrowNid());
		record.setStatus(2);//已开奖
		record.setCreateTime(GetDate.getNowTime10());
		record.setProjectTypeName("汇添金");
    }
    
    /**
     * 更新中奖用户
     * @param record
     * @param creditTender
     */
    public void updateCreditRecord(ActivityBillionOne record,CreditTender creditTender){
		Users user = this.getUsersByUserId(creditTender.getUserId());
		UsersInfo userInfo = this.getUsersInfoByUserId(creditTender.getUserId());
		record.setUserId(creditTender.getUserId());
		record.setUserName(user.getUsername());
		record.setTrueName(userInfo.getTruename());
		record.setMobile(user.getMobile());
		record.setTenderTime(Integer.valueOf(creditTender.getAddTime()));
		record.setTenderMoney(creditTender.getAssignCapital());
		record.setBorrowNid(creditTender.getCreditNid());
		record.setStatus(2);//已开奖
		record.setCreateTime(GetDate.getNowTime10());
		record.setProjectTypeName("汇转让");
    }

	/**
	 * 获取所有到达满亿时间点
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<ActivityBillionSecondTime> getAccordTimes() {
		ActivityBillionSecondTimeExample example = new ActivityBillionSecondTimeExample();
		ActivityBillionSecondTimeExample.Criteria cra = example.createCriteria();
		cra.andAccordTimeIsNotNull();
		return this.activityBillionSecondTimeMapper.selectByExample(example);
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param startTime
	 * @param endTime
	 * @author Michael
	 */
		
	@Override
	public void createBillionSecondPrize(int startTime, int endTime) {
		if(startTime == 0 || endTime == 0){
			return;
		}
		//已生成中奖用户 不执行
		Integer count = this.activityBillionSecondMapper.countByExample(new ActivityBillionSecondExample());
		if(count > 0){
			return;
		}
		BillionSecondCustomize billionSecondCustomize = new BillionSecondCustomize();
		billionSecondCustomize.setStartTime(startTime);
		billionSecondCustomize.setEndTime(endTime);
		//获取用户出借金额
		List<BillionSecondCustomize> userTenderList = billionSecondCustomizeMapper.selectUserTenderList(billionSecondCustomize);
		if(userTenderList == null || userTenderList.size() == 0){
			return;
		}
		//发送对应优惠券
		CommonParamBean couponParamBean = new CommonParamBean();
		BigDecimal oneCount = new BigDecimal("1000000");
		BigDecimal twoCount = new BigDecimal("300000");
		BigDecimal threeCount = new BigDecimal("50000");
		int sendFlg = 8;
		int subFlg = 0;
		String couponId = "";
		String couponCode = "";
		
		for(int i = 0 ; i < userTenderList.size();i++){
			BillionSecondCustomize userTenderRecord = userTenderList.get(i);
			if(userTenderRecord.getAccount().compareTo(oneCount) >= 0){
				subFlg = 1;
			}else if(userTenderRecord.getAccount().compareTo(twoCount) >= 0 && userTenderRecord.getAccount().compareTo(oneCount) < 0){
				subFlg = 2;
			}else if(userTenderRecord.getAccount().compareTo(threeCount) >= 0 && userTenderRecord.getAccount().compareTo(twoCount) < 0){
				subFlg = 3;
			}else if(userTenderRecord.getAccount().compareTo(threeCount) < 0){
				subFlg = 4;
			}
			couponParamBean.setUserId(String.valueOf(userTenderRecord.getUserId()));
			couponParamBean.setSendFlg(sendFlg);
			couponParamBean.setSubFlg(subFlg);
			String result = CommonSoaUtils.sendUserCoupon(couponParamBean);
			
			JSONObject obj = JSONObject.parseObject(result);
			int couponCount = obj.getIntValue("couponCount");
			@SuppressWarnings("unchecked")
			List<String> couponUserCodeList = (List<String>)obj.get("retCouponUserCodes");
			// 备注：用户优惠券编号
			if(couponUserCodeList.size() == 1){
				// 发放一张优惠券
				couponId = couponUserCodeList.get(0);
			}
	         @SuppressWarnings("unchecked")
	         List<String> couponCodeList = (List<String>)obj.get("couponCode");
	            // 备注：用户优惠券编号
	            if(couponCodeList.size() == 1){
	                // 发放一张优惠券
	                couponCode = couponCodeList.get(0);
	            }
			
			int status = 0; 
			if(couponCount > 0){
				status = 1;
			}
			//插入活动发奖记录
			insertBillionSecondRecord(userTenderRecord.getUserId(), status, couponId,subFlg,userTenderRecord.getAccount(),couponCode);
		}
		
	}
	/**
	 * 插入活动发奖记录
	 * @param userId
	 * @param status 是否发放(0 成功  1失败)
	 * @param couponId
	 */
	public void insertBillionSecondRecord(Integer userId,Integer status,String couponId,int subFlg,BigDecimal tenderMoney,String couponCode){
		ActivityBillionSecond record = new ActivityBillionSecond();
		if(userId != null){
			Users users = this.getUsersByUserId(userId);
			UsersInfo usersInfo = this.getUsersInfoByUserId(userId);
			record.setUserName(users.getUsername());
			record.setTrueName(usersInfo.getTruename());
			record.setMobile(users.getMobile());
		}
		record.setUserId(userId);
		record.setPrizeId(subFlg);
		record.setTenderMoney(tenderMoney);
		record.setIsSend(status);
		record.setCouponId(couponId);
		record.setCouponCode(couponCode);
		this.activityBillionSecondMapper.insertSelective(record);
	}
	
}
