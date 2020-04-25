package com.hyjf.batch.hjh.borrow.plancapital;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.HjhAccountBalance;
import com.hyjf.mybatis.model.auto.HjhAccountBalanceExample;
import com.hyjf.mybatis.model.auto.HjhPlanCapital;
import com.hyjf.mybatis.model.auto.HjhPlanCapitalExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PlanCapitalServiceImpl extends BaseServiceImpl implements PlanCapitalService {
	
	Logger _log = LoggerFactory.getLogger(PlanCapitalServiceImpl.class);

	/**
	 * 获取该日期的实际债转和复投金额
	 * @param date
	 * @return
	 */
	@Override
	public List<HjhPlanCapital> getPlanCapitalForActList(Date date) {
		List<HjhPlanCapital> list = this.hjhPlanCapitalCustomizeMapper.selectPlanCapitalForActList(date);
		return list;
	}

	/**
	 * 获取该期间的预估债转和复投金额
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	@Override
	public List<HjhPlanCapital> getPlanCapitalForProformaList(Date fromDate, Date toDate) {
		List<HjhPlanCapital> list = this.hjhPlanCapitalCustomizeMapper.selectPlanCapitalForProformaList(fromDate, toDate);
		return list;
	}

	@Override
	public Boolean updatePlanCapital(HjhPlanCapital hjhPlanCapital) {
		Boolean result = false;
		// 判断数据是否已存在
		List<HjhPlanCapital> list = this.selectHjhPlanCapitalList(hjhPlanCapital);
		if (list == null ) {
			throw new RuntimeException("取得汇计划资本按天统计及预估表的数据失败。");
		}
		if (list.size()>0){
			// 存在，更新记录（汇计划资本按天统计及预估表）
			result = this.updateHjhPlanCapital(hjhPlanCapital) > 0 ? true : false;
		}else{
			// 不存在，插入记录（汇计划资本按天统计及预估表）
			result = this.insertHjhPlanCapital(hjhPlanCapital) > 0 ? true : false;
		}
		return result;
	}

	/**
	 *获取该期间的汇计划日交易量
	 * @param date
	 * @return
	 */
	@Override
	public List<HjhAccountBalance> getHjhAccountBalanceForActList(Date date) {
		List<HjhAccountBalance> list = this.hjhAccountBalanceCustomizeMapper.selectHjhAccountBalanceForActList(date);
		return list;
	}

	/**
	 * 插入更新汇计划按日对账统计表
	 * @param hjhAccountBalance
	 * @return
	 */
	@Override
	public Boolean updateAccountBalance(HjhAccountBalance hjhAccountBalance) {
		Boolean result = false;
		// 判断数据是否已存在
		List<HjhAccountBalance> list = this.selectHjhAccountBalanceList(hjhAccountBalance);
		if (list == null ) {
			throw new RuntimeException("取得汇计划资本按天统计及预估表的数据失败。");
		}
		if (list.size()>0){
			// 存在，更新记录（汇计划资本按天统计及预估表）
			result = this.updateHjhAccountBalance(hjhAccountBalance) > 0 ? true : false;
		}else{
			// 不存在，插入记录（汇计划资本按天统计及预估表）
			result = this.insertHjhAccountBalance(hjhAccountBalance) > 0 ? true : false;
		}
		return result;
	}

	/**
	 * 检索 汇计划按日对账统计表
	 * @param hjhAccountBalance
	 * @return
	 */
	private List<HjhAccountBalance> selectHjhAccountBalanceList(HjhAccountBalance hjhAccountBalance) {
		HjhAccountBalanceExample example = new HjhAccountBalanceExample();
		HjhAccountBalanceExample.Criteria crt = example.createCriteria();
		crt.andDateEqualTo(hjhAccountBalance.getDate());
		List<HjhAccountBalance> list = this.hjhAccountBalanceMapper.selectByExample(example);
		return list;
	}

	/**
	 * 检索 汇计划资本按天统计及预估表
	 * @param hjhPlanCapital
	 * @return
	 */
	private List<HjhPlanCapital> selectHjhPlanCapitalList(HjhPlanCapital hjhPlanCapital) {
		HjhPlanCapitalExample example = new HjhPlanCapitalExample();
		HjhPlanCapitalExample.Criteria crt = example.createCriteria();
		crt.andDateEqualTo(hjhPlanCapital.getDate());
		crt.andPlanNidEqualTo(hjhPlanCapital.getPlanNid());
		List<HjhPlanCapital> list = this.hjhPlanCapitalMapper.selectByExample(example);
		return list;
	}

	/**
	 * 插入 汇计划资本按天统计及预估表
	 * @param hjhPlanCapital
	 * @return
	 */
	private int insertHjhPlanCapital(HjhPlanCapital hjhPlanCapital) {
		hjhPlanCapital.setCreateTime(GetDate.getNowTime10());
		hjhPlanCapital.setDelFlg(0);
		return this.hjhPlanCapitalMapper.insertSelective(hjhPlanCapital);
	}

	/**
	 * 更新 汇计划资本按天统计及预估表
	 * @param hjhPlanCapital
	 * @return
	 */
	private int updateHjhPlanCapital(HjhPlanCapital hjhPlanCapital) {
		HjhPlanCapitalExample example = new HjhPlanCapitalExample();
		HjhPlanCapitalExample.Criteria crt = example.createCriteria();
		crt.andDateEqualTo(hjhPlanCapital.getDate());
		crt.andPlanNidEqualTo(hjhPlanCapital.getPlanNid());

		hjhPlanCapital.setUpdateTime(GetDate.getNowTime10());
		hjhPlanCapital.setDelFlg(0);
		return this.hjhPlanCapitalMapper.updateByExampleSelective(hjhPlanCapital, example);
	}

	/**
	 * 插入记录（汇计划资本按天统计及预估表）
	 * @param hjhAccountBalance
	 * @return
	 */
	private int insertHjhAccountBalance(HjhAccountBalance hjhAccountBalance) {
		hjhAccountBalance.setCreateTime(GetDate.getNowTime10());
		hjhAccountBalance.setDelFlg(0);
		return this.hjhAccountBalanceMapper.insertSelective(hjhAccountBalance);
	}

	/**
	 * 更新记录（汇计划资本按天统计及预估表)
	 * @param hjhAccountBalance
	 * @return
	 */
	private int updateHjhAccountBalance(HjhAccountBalance hjhAccountBalance) {
		HjhAccountBalanceExample example = new HjhAccountBalanceExample();
		HjhAccountBalanceExample.Criteria crt = example.createCriteria();
		crt.andDateEqualTo(hjhAccountBalance.getDate());
		hjhAccountBalance.setUpdateTime(GetDate.getNowTime10());
		hjhAccountBalance.setDelFlg(0);
		return this.hjhAccountBalanceMapper.updateByExampleSelective(hjhAccountBalance, example);
	}


	@Override
	public Boolean deleteHjhPlanCapital(Date dateFrom, Date dateTo) {
		return this.hjhPlanCapitalCustomizeMapper.deleteForDates(dateFrom, dateTo) > 0 ? true : false;
	}

}