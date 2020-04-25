package com.hyjf.batch.creditrepair;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.calculate.AverageCapitalPlusInterestUtils;
import com.hyjf.common.calculate.AverageCapitalUtils;
import com.hyjf.common.calculate.BeforeInterestAfterPrincipalUtils;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.CreditRepay;
import com.hyjf.mybatis.model.auto.CreditTender;

public class CreditRepairTask {

	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	CreditRepairService creditRepairService;

	public void run() {
		// 调用还款接口
		credtiRepair();
	}

	/**
	 * 调用还款接口
	 *
	 * @return
	 */
	private boolean credtiRepair() {

		if (isRun == 0) {
			isRun = 1;
			try {
				// 1.查询所有的债转未还款的数据credit_repay表相关的数据
				List<CreditRepay> creditRepayList = this.creditRepairService.selectCreditRepayList();
				if (creditRepayList != null && creditRepayList.size() > 0) {
					for (int i = 0; i < creditRepayList.size(); i++) {
						try {
							CreditRepay creditRepay = creditRepayList.get(i);
							// 项目编号
							String borrowNid = creditRepay.getBidNid();
							// 债转承接订单号
							String assignNid = creditRepay.getAssignNid();
							// 还款期数
							int repayPeriod = creditRepay.getRecoverPeriod();
							// 查询相应的borrow表的数据
							BorrowWithBLOBs borrow = this.creditRepairService.selectBorrow(borrowNid);
							if (Validator.isNotNull(borrow)) {
								//还款方式
								String borrowStyle = borrow.getBorrowStyle();
								//年华收益率
								BigDecimal yearRate = borrow.getBorrowApr().divide(new BigDecimal(100), 12, BigDecimal.ROUND_DOWN);
								// 项目总期数
								int borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
								// 2.根据相应的承接订单号，查询相应的出借金额
								CreditTender creditTender = this.creditRepairService.selectCreditTender(assignNid);
								if (Validator.isNotNull(creditTender)) {
									//出借本金
									BigDecimal creditCapital = creditTender.getAssignCapital();
									// 3。根据出借金额、出借期数计算相应的还款本息
									//债转本息
									BigDecimal creditAccount = BigDecimal.ZERO;
									//债转期全部利息
									BigDecimal creditInterest = BigDecimal.ZERO;
									//按天计息，到期还本还息
									if(borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)){
				                        //债转期全部利息
				                        creditInterest = DuePrincipalAndInterestUtils.getDayInterest(creditCapital, yearRate, borrowPeriod); 
				                        //债转本息
				                        creditAccount = DuePrincipalAndInterestUtils.getDayPrincipalInterest(creditCapital, yearRate, borrowPeriod);
									}
									//按月计息,到期还本还息
									else if(borrowStyle.equals(CalculatesUtil.STYLE_END)){
				                        //债转期全部利息
				                        creditInterest = DuePrincipalAndInterestUtils.getMonthInterest(creditCapital, yearRate, borrowPeriod);
				                        //债转本息
				                        creditAccount = DuePrincipalAndInterestUtils.getMonthPrincipalInterest(creditCapital, yearRate, borrowPeriod);
									}
									//等额本息
									else if(borrowStyle.equals(CalculatesUtil.STYLE_MONTH) ){
										//获取每月的利息
										Map<Integer, BigDecimal> interests = AverageCapitalPlusInterestUtils.getPerMonthInterest(creditCapital, yearRate, borrowPeriod);
										//债转期全部利息
										creditInterest = interests.get(repayPeriod);
										//债转本息
										creditAccount =AverageCapitalPlusInterestUtils.getPerMonthPrincipalInterest(creditCapital, yearRate, borrowPeriod);
									}
									//等额本金
									else if(borrowStyle.equals(CalculatesUtil.STYLE_PRINCIPAL)){
										//获取每月的利息
										Map<Integer, BigDecimal> interests = AverageCapitalUtils.getPerMonthInterest(creditCapital, yearRate, borrowPeriod);
										//债转期全部利息
										creditInterest = interests.get(repayPeriod);
										//获取每月本息
										Map<Integer, BigDecimal> principalInterest = AverageCapitalUtils.getPerMonthPrincipalInterest(creditCapital, yearRate, borrowPeriod);
										//债转本息
										creditAccount = principalInterest.get(repayPeriod);
									}
									//先息后本
									else if(borrowStyle.equals(CalculatesUtil.STYLE_ENDMONTH)){
										//债转期全部利息
										creditInterest = BeforeInterestAfterPrincipalUtils.getPerTermInterest(creditCapital, yearRate, borrowPeriod, borrowPeriod);
										//最后一期
										if(borrowPeriod == repayPeriod){
											//债转本息
											creditAccount = creditCapital.add(creditInterest);
										}else{
											//债转本息
											creditAccount = creditInterest;
										}
									}
									creditRepay.setAssignAccount(creditAccount);
									creditRepay.setAssignInterest(creditInterest);
									// 4.更新credit_repay表的数据
									boolean flag = this.creditRepairService.updateCreditRepay(creditRepay) > 0 ? true:false;
									if(!flag){
										throw new Exception("更新creditrepay表失败，标号:" + borrowNid + "-------" + "承接订单号:" + assignNid + "还款期数：" + repayPeriod);
									}
								} else {
									throw new Exception("未查询到相应的credittender信息，标号:" + borrowNid + "-------" + "承接订单号:" + assignNid);
								}
							} else {
								throw new Exception("未查询到相应的borrow表信息，标号:" + borrowNid + "-------" + "承接订单号:" + assignNid + "还款期数：" + repayPeriod);
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRun = 0;
			}
		}
		return false;

	}

}
