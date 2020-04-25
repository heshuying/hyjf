package com.hyjf.admin.manager.debt.debtborrowfull;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.mybatis.model.auto.DebtApicron;
import com.hyjf.mybatis.model.auto.DebtApicronExample;
import com.hyjf.mybatis.model.auto.DebtBorrowExample;
import com.hyjf.mybatis.model.auto.DebtBorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.DebtInvest;
import com.hyjf.mybatis.model.auto.DebtInvestExample;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanExample;
import com.hyjf.mybatis.model.customize.AdminSystem;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowFullCustomize;

@Service
public class DebtBorrowFullServiceImpl extends BaseServiceImpl implements DebtBorrowFullService {

	/**
	 * 复审记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 * @author Administrator
	 */

	@Override
	public Long countBorrowFull(DebtBorrowCommonCustomize debtBorrowCommonCustomize) {
		return this.debtBorrowFullCustomizeMapper.countBorrowFull(debtBorrowCommonCustomize);

	}

	/**
	 * 复审记录
	 * 
	 * @param borrowCustomize
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<DebtBorrowFullCustomize> selectBorrowFullList(DebtBorrowCommonCustomize debtBorrowCommonCustomize) {
		return this.debtBorrowFullCustomizeMapper.selectBorrowFullList(debtBorrowCommonCustomize);

	}

	/**
	 * 获取复审状态
	 * 
	 * @param record
	 */
	@Override
	public boolean isBorrowStatus16(DebtBorrowFullBean borrowBean) {
		String borrowNid = borrowBean.getBorrowNid();
		if (StringUtils.isNotEmpty(borrowNid)) {
			DebtBorrowExample debtBorrowExample = new DebtBorrowExample();
			DebtBorrowExample.Criteria borrowCra = debtBorrowExample.createCriteria();
			borrowCra.andBorrowNidEqualTo(borrowNid);
			List<DebtBorrowWithBLOBs> debtBorrowList = this.debtBorrowMapper.selectByExampleWithBLOBs(debtBorrowExample);
			if (debtBorrowList != null && debtBorrowList.size() == 1) {
				DebtBorrowWithBLOBs borrow = debtBorrowList.get(0);
				if (borrow.getStatus() == 16) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 更新复审 6个表放入一个事物
	 * 
	 * @param record
	 */
	@Override
	public void updateReverifyRecord(DebtBorrowFullBean borrowBean) {
		Date systemNowDate = new Date();
		String borrowNid = borrowBean.getBorrowNid();
		Long systemNowDateLong = systemNowDate.getTime() / 1000;
		Integer time = Integer.valueOf(String.valueOf(systemNowDateLong));
		if (StringUtils.isNotEmpty(borrowNid)) {

			DebtBorrowExample borrowExample = new DebtBorrowExample();
			DebtBorrowExample.Criteria borrowCra = borrowExample.createCriteria();
			borrowCra.andBorrowNidEqualTo(borrowNid);

			List<DebtBorrowWithBLOBs> borrowList = this.debtBorrowMapper.selectByExampleWithBLOBs(borrowExample);

			if (borrowList != null && borrowList.size() == 1) {
				AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
				DebtBorrowWithBLOBs borrow = borrowList.get(0);

				String nid = borrow.getBorrowNid() + "_" + borrow.getUserId() + "_1";
				DebtApicronExample example = new DebtApicronExample();
				DebtApicronExample.Criteria crt = example.createCriteria();
				crt.andNidEqualTo(nid);
				crt.andApiTypeEqualTo(0);
				List<DebtApicron> borrowApicrons = debtApicronMapper.selectByExample(example);
				if (borrowApicrons == null || borrowApicrons.size() == 0) {
					// 更新huiyingdai_borrow的如下字段：
					// 复审时间
					borrow.setReverifyTime(String.valueOf(systemNowDateLong));
					// 复审人ID
					borrow.setReverifyUserid(adminSystem.getId());// Auto
					// 复审状态
					borrow.setReverifyStatus(3);
					// 复审状态（复审通过）
					borrow.setStatus(3);
					// 复审备注
					borrow.setReverifyRemark(borrowBean.getReverifyRemark());
					// 借款成功时间
					borrow.setBorrowSuccessTime(time);
					// 满标审核状态
					borrow.setBorrowFullStatus(1);
					// 更新时间
					borrow.setUpdatetime(systemNowDate);

					this.debtBorrowMapper.updateByExampleSelective(borrow, borrowExample);

					// 放款任务表
					DebtApicron borrowApicron = new DebtApicron();
					// 交易凭证号 生成规则：BorrowNid_userid_期数
					borrowApicron.setNid(nid);
					// 借款人编号
					borrowApicron.setUserId(borrow.getUserId());
					// 项目编号
					borrowApicron.setBorrowNid(borrow.getBorrowNid());
					// Status
					borrowApicron.setStatus(0);
					// ApiType
					borrowApicron.setApiType(0);
					// 创建时间
					borrowApicron.setCreateTime(time);
					//
					borrowApicron.setWebStatus(0);
					// 更新时间
					borrowApicron.setUpdateTime(time);
					// 汇租赁当前期数
					borrowApicron.setPeriodNow(0);
					// 债转还款状态
					borrowApicron.setCreditRepayStatus(0);

					borrowApicron.setCreateUserId(Integer.parseInt(adminSystem.getId()));
					borrowApicron.setCreateUserName(adminSystem.getUsername());
					this.debtApicronMapper.insertSelective(borrowApicron);
				}
			}
		}
	}

	/**
	 * 重新放款
	 * 
	 * @param record
	 */
	@Override
	public void updateBorrowApicronRecord(DebtBorrowFullBean borrowBean) {
		Date systemNowDate = new Date();
		String borrowNid = borrowBean.getBorrowNid();
		Long systemNowDateLong = systemNowDate.getTime() / 1000;
		Integer time = Integer.valueOf(String.valueOf(systemNowDateLong));
		if (StringUtils.isNotEmpty(borrowNid)) {
			DebtApicronExample borrowExample = new DebtApicronExample();
			DebtApicronExample.Criteria borrowCra = borrowExample.createCriteria();
			borrowCra.andBorrowNidEqualTo(borrowNid);
			// 放款任务表
			DebtApicron borrowApicron = new DebtApicron();
			// Status
			borrowApicron.setStatus(0);
			// 更新时间
			borrowApicron.setUpdateTime(time);

			this.debtApicronMapper.updateByExampleSelective(borrowApicron, borrowExample);
		}
	}

	/**
	 * 复审详细中的列表
	 * 
	 * @param record
	 */
	@Override
	public List<DebtBorrowFullCustomize> selectFullList(String borrowNid, int limitStart, int limitEnd) {
		return this.debtBorrowFullCustomizeMapper.selectFullList(borrowNid, limitStart, limitEnd);
	}

	/**
	 * 复审详细
	 * 
	 * @param borrowFullCustomize
	 * @return
	 */
	public DebtBorrowFullCustomize selectFullInfo(String borrowNid) {
		return this.debtBorrowFullCustomizeMapper.selectFullInfo(borrowNid);
	}

	/**
	 * 合计
	 * 
	 * @param borrowFullCustomize
	 * @return
	 */
	public DebtBorrowFullCustomize sumAmount(String borrowNid) {
		return this.debtBorrowFullCustomizeMapper.sumAmount(borrowNid);
	}

	/**
	 * 流标
	 * 
	 * @param record
	 */
	public void updateBorrowRecordOver(DebtBorrowFullBean borrowBean) {
		Date systemNowDate = new Date();
		String borrowNid = borrowBean.getBorrowNid();
		if (StringUtils.isNotEmpty(borrowNid)) {
			DebtBorrowExample borrowExample = new DebtBorrowExample();
			DebtBorrowExample.Criteria borrowCra = borrowExample.createCriteria();
			borrowCra.andBorrowNidEqualTo(borrowNid);

			List<DebtBorrowWithBLOBs> borrowList = this.debtBorrowMapper.selectByExampleWithBLOBs(borrowExample);

			if (borrowList != null && borrowList.size() == 1) {
				AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
				DebtBorrowWithBLOBs borrow = borrowList.get(0);
				// 复审人ID
				borrow.setReverifyUserid(adminSystem.getId());// Auto
				// 复审状态（流标）
				borrow.setStatus(2);
				// 复审备注
				borrow.setReverifyRemark(GetDate.getServerDateTime(6, new Date()) + " " + adminSystem.getId() + " 复审流标");

				// 更新时间
				borrow.setUpdatetime(systemNowDate);

				this.debtBorrowMapper.updateByExampleSelective(borrow, borrowExample);
			}
		}
	}

	@Override
	public List<DebtInvest> selectDebtInvestListByBorrowNid(String borrowNid, int status) {
		DebtInvestExample example = new DebtInvestExample();
		DebtInvestExample.Criteria cri = example.createCriteria();
		cri.andBorrowNidEqualTo(borrowNid);
		cri.andStatusEqualTo(status);
		return debtInvestMapper.selectByExample(example);
	}

	@Override
	public DebtPlan selectDebtPlanByPlanNid(String planNid) {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cri = example.createCriteria();
		cri.andDebtPlanNidEqualTo(planNid);
		List<DebtPlan> planList = debtPlanMapper.selectByExample(example);
		if (planList != null && planList.size() > 0) {
			return planList.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 根据计划编号检索是否有清算中的计划
	 */
	@Override
	public int countDebtInvestListByBorrowNid(String borrowNid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("borrow_nid", borrowNid);
		return this.debtBorrowFullCustomizeMapper.countDebtInvestListByBorrowNid(params);
	}
}
