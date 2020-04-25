package com.hyjf.batch.htj.statis;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.DebtPlanInfoStatic;

/**
 * 平台数据
 *
 * @author Administrator
 *
 */
@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class PlanDataInfoServiceImpl extends BaseServiceImpl implements PlanDataInfoService {

	@Override
	public int insertDataInfo() {
		DebtPlanInfoStatic debtPlanInfoStatic = new DebtPlanInfoStatic();
		// 待成交资产-专属项目：
		BigDecimal waitInvest = dataCustomizeMapper.selectDebtBorrowWaitMoney();
		// 待成交资产-债权转让:
		BigDecimal waitCredit = dataCustomizeMapper.selectDebtCreditWaitMoney();
		debtPlanInfoStatic.setWaitInvest(waitInvest);
		debtPlanInfoStatic.setWaitCredit(waitCredit);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("HH");
		String strdate = sdf.format(date);
		if (strdate.equals("01")) {
			// 计划持有债权数量-专属资产 1小时
			Long yesInvest = dataCustomizeMapper.selectDebtDetailYesCount(0);
			// 计划持有债权数量-债权数量 1小时
			Long yesCredit = dataCustomizeMapper.selectDebtDetailYesCount(1);
			// 计划持有债权待还总额 1小时
			BigDecimal waitRepay = dataCustomizeMapper.selectDebtDetailRepayMoney(0);
			// 计划持有债权已还总额 1小时
			BigDecimal yesRepay = dataCustomizeMapper.selectDebtDetailRepayMoney(1);
			// 计划应还款总额
			BigDecimal planRepayWait = dataCustomizeMapper.selectPlanRepayWait();
			// 已还款总额
			BigDecimal planRepayYes = dataCustomizeMapper.selectPlanRepayYes();
			// 出借人加入总额
			BigDecimal planAccedeAll = dataCustomizeMapper.selectPlanAccedeAll();
			// 到期公允价值
			BigDecimal expireFairValue = dataCustomizeMapper.selectPlanExpireFairValue();

			debtPlanInfoStatic.setPlanRepayWait(planRepayWait);
			debtPlanInfoStatic.setPlanRepayYes(planRepayYes);
			debtPlanInfoStatic.setPlanAccedeAll(planAccedeAll);
			debtPlanInfoStatic.setExpireFairValue(expireFairValue);
			debtPlanInfoStatic.setYesInvest(Integer.parseInt(yesInvest + ""));
			debtPlanInfoStatic.setYesCredit(Integer.parseInt(yesCredit + ""));
			debtPlanInfoStatic.setWaitRepay(waitRepay);
			debtPlanInfoStatic.setYesRepay(yesRepay);
			// 更新加入分布饼状图表hyjf_debt_plan_info_static_count
			// Integer
			// staticCount=dataCustomizeMapper.selectPlanInfoStaticCount();
			dataCustomizeMapper.detelePlanInfoStaticCount();
			dataCustomizeMapper.insertPlanInfoStaticCount();
		}
		// 待成交专属资产期限分布
		Map<String, Object> borrowPeriodMap = dataCustomizeMapper.selectDebtBorrowPeriodInfo();
		// 待成交债权转让-期限分布
		Map<String, Object> creditPeriodMap = dataCustomizeMapper.selectDebtCreditPeriodInfo();
		if (borrowPeriodMap != null) {
			Long one = (Long) borrowPeriodMap.get("one");
			Long twoFour = (Long) borrowPeriodMap.get("twoFour");
			Long fiveEight = (Long) borrowPeriodMap.get("fiveEight");
			Long nineTwel = (Long) borrowPeriodMap.get("nineTwel");
			Long twelTf = (Long) borrowPeriodMap.get("twelTf");
			Long tf = (Long) borrowPeriodMap.get("tf");
			debtPlanInfoStatic.setInvestPeriodOne(Integer.parseInt(one + ""));
			debtPlanInfoStatic.setInvestPeriodTwoFour(Integer.parseInt(twoFour + ""));
			debtPlanInfoStatic.setInvestPeriodFiveEight(Integer.parseInt(fiveEight + ""));
			debtPlanInfoStatic.setInvestPeriodNineTwel(Integer.parseInt(nineTwel + ""));
			debtPlanInfoStatic.setInvestPeriodTwelTf(Integer.parseInt(twelTf + ""));
			debtPlanInfoStatic.setInvestPeriodTf(Integer.parseInt(tf + ""));
		}
		if (creditPeriodMap != null) {
			Long onec = (Long) creditPeriodMap.get("one");
			Long twoFourc = (Long) creditPeriodMap.get("twoFour");
			Long fiveEightc = (Long) creditPeriodMap.get("fiveEight");
			Long nineTwelc = (Long) creditPeriodMap.get("nineTwel");
			Long twelTfc = (Long) creditPeriodMap.get("twelTf");
			Long tfc = (Long) creditPeriodMap.get("tf");
			debtPlanInfoStatic.setCreditPeriodOne(Integer.parseInt(onec + ""));
			debtPlanInfoStatic.setCreditPeriodTwoFour(Integer.parseInt(twoFourc + ""));
			debtPlanInfoStatic.setCreditPeriodFiveEight(Integer.parseInt(fiveEightc + ""));
			debtPlanInfoStatic.setCreditPeriodNineTwel(Integer.parseInt(nineTwelc + ""));
			debtPlanInfoStatic.setCreditPeriodTwelTf(Integer.parseInt(twelTfc + ""));
			debtPlanInfoStatic.setCreditPeriodTf(Integer.parseInt(tfc + ""));
		}
		SimpleDateFormat sdfd = GetDate.date_sdf;
		SimpleDateFormat sdfm = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat sdfh = new SimpleDateFormat("yyyy-MM-dd HH");
		String dataDate = sdfd.format(new Date());
		String dataHour = sdfh.format(new Date());
		String dataMonth = sdfm.format(new Date());
		Integer dateTime = GetDate.getNowTime10();
		debtPlanInfoStatic.setCreateTime(dateTime);
		debtPlanInfoStatic.setDataDate(dataDate);
		debtPlanInfoStatic.setDataHour(dataHour);
		debtPlanInfoStatic.setDataMonth(dataMonth);
		return debtPlanInfoStaticMapper.insertSelective(debtPlanInfoStatic);

	}

	public static void main(String[] args) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("HH");
		String strdate = sdf.format(date);
		System.out.println(strdate);
	}

}
