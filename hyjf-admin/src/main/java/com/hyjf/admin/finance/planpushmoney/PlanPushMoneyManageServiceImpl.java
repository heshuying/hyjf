package com.hyjf.admin.finance.planpushmoney;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.jpush.api.utils.StringUtils;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetterUtil;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.AppMsMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountWebList;
import com.hyjf.mybatis.model.auto.AccountWebListExample;
import com.hyjf.mybatis.model.auto.DebtAccedeCommission;
import com.hyjf.mybatis.model.auto.DebtAccedeCommissionExample;
import com.hyjf.mybatis.model.auto.DebtCommissionConfig;
import com.hyjf.mybatis.model.auto.DebtCommissionConfigExample;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;
import com.hyjf.mybatis.model.auto.DebtPlanAccedeExample;
import com.hyjf.mybatis.model.auto.DebtPlanExample;
import com.hyjf.mybatis.model.auto.DebtPlanWithBLOBs;
import com.hyjf.mybatis.model.auto.SpreadsLog;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.AdminSystem;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.mybatis.model.customize.UserInfoCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminPlanPushMoneyDetailCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;

/**
 * 汇添金提成管理Service实现类
 * 
 * @ClassName PlanPushMoneyManageServiceImpl
 * @author liuyang
 * @date 2016年10月24日 上午9:33:42
 */
@Service
public class PlanPushMoneyManageServiceImpl extends BaseServiceImpl implements PlanPushMoneyManageService {

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	@Qualifier("appMsProcesser")
	private MessageProcesser appMsProcesser;

	/**
	 * 获取锁定中的计划列表
	 * 
	 * @Title selectLockPlanList
	 * @return
	 */
	@Override
	public List<DebtPlan> selectLockPlanList(PlanPushMoneyManageBean form) {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		// 计划状态:锁定中
		cra.andDebtPlanStatusEqualTo(CustomConstants.DEBT_PLAN_STATUS_5);
		// 计划编号
		if (StringUtils.isNotEmpty(form.getDebtPlanNidSrch())) {
			cra.andDebtPlanNidLike("%" + form.getDebtPlanNidSrch() + "%");
		}
		// 计划名称
		if (StringUtils.isNotEmpty(form.getDebtPlanNameSrch())) {
			cra.andDebtPlanNameLike("%" + form.getDebtPlanNameSrch() + "%");
		}
		// 进入锁定期时间
		if (StringUtils.isNotEmpty(form.getPlanLockStartTimeSrch())) {
			cra.andPlanLockTimeGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(form.getPlanLockStartTimeSrch())));
		}
		if (StringUtils.isNotEmpty(form.getPlanLockEndTimeSrch())) {
			cra.andPlanLockTimeLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayEnd(form.getPlanLockEndTimeSrch())));
		}
		example.setOrderByClause(" plan_lock_time DESC ");
		if (form.getLimitStart() >= 0) {
			example.setLimitStart(form.getLimitStart());
			example.setLimitEnd(form.getLimitEnd());
		}
		return this.debtPlanMapper.selectByExample(example);
	}

	/**
	 * 检索锁定中计划列表
	 * 
	 * @Title countLockPlanList
	 * @param form
	 * @return
	 */
	@Override
	public int countLockPlanList(PlanPushMoneyManageBean form) {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		// 计划状态:锁定中
		cra.andDebtPlanStatusEqualTo(CustomConstants.DEBT_PLAN_STATUS_5);
		// 计划编号
		if (StringUtils.isNotEmpty(form.getDebtPlanNidSrch())) {
			cra.andDebtPlanNidLike("%" + form.getDebtPlanNidSrch() + "%");
		}
		// 计划名称
		if (StringUtils.isNotEmpty(form.getDebtPlanNameSrch())) {
			cra.andDebtPlanNameLike("%" + form.getDebtPlanNameSrch() + "%");
		}
		// 进入锁定期时间
		if (StringUtils.isNotEmpty(form.getPlanLockStartTimeSrch())) {
			cra.andPlanLockTimeGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(form.getPlanLockStartTimeSrch())));
		}
		if (StringUtils.isNotEmpty(form.getPlanLockEndTimeSrch())) {
			cra.andPlanLockTimeLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayEnd(form.getPlanLockEndTimeSrch())));
		}
		return this.debtPlanMapper.countByExample(example);
	}

	/**
	 * 根据计划编号检索计划
	 * 
	 * @Title selectDebtPlanByDebtPlanNid
	 * @param debtPlanNid
	 * @return
	 */
	@Override
	public DebtPlan selectDebtPlanByDebtPlanNid(String debtPlanNid) {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		if (StringUtils.isNotEmpty(debtPlanNid)) {
			cra.andDebtPlanNidEqualTo(debtPlanNid);
			List<DebtPlan> list = this.debtPlanMapper.selectByExample(example);
			if (list != null && list.size() != 0) {
				return list.get(0);
			}
		}

		return null;
	}

	/**
	 * 更新汇添金提成表
	 * 
	 * @Title insertAccedeCommissionRecord
	 * @param form
	 * @return
	 */
	@Override
	public int insertAccedeCommissionRecord(PlanPushMoneyManageBean form) {

		int ret = -1;
		// 计划编号
		String debtPlanNid = form.getDebtPlanNid();

		// 提成总额
		BigDecimal commissionTotal = new BigDecimal(0);

		// 根据计划编号取得plan表的信息
		DebtPlanExample planExample = new DebtPlanExample();
		DebtPlanExample.Criteria planCra = planExample.createCriteria();
		planCra.andDebtPlanNidEqualTo(debtPlanNid);
		List<DebtPlanWithBLOBs> list = this.debtPlanMapper.selectByExampleWithBLOBs(planExample);
		if (list == null || list.size() == 0) {
			return ret;
		}
		DebtPlanWithBLOBs plan = list.get(0);
		// 取得计划的加入记录
		DebtPlanAccedeExample accedeExample = new DebtPlanAccedeExample();
		DebtPlanAccedeExample.Criteria accedeCra = accedeExample.createCriteria();
		accedeCra.andPlanNidEqualTo(debtPlanNid);
		List<DebtPlanAccede> accedeList = this.debtPlanAccedeMapper.selectByExample(accedeExample);
		if (accedeList == null || accedeList.size() == 0) {
			return ret;
		}

		for (DebtPlanAccede debtPlanAccede : accedeList) {
			boolean is51 = false;
			// 插入提成数据
			DebtAccedeCommission debtAccedeCommission = new DebtAccedeCommission();
			// 加入人userId
			debtAccedeCommission.setAccedeUserId(debtPlanAccede.getUserId());
			// 加入人用户名
			debtAccedeCommission.setAccedeUserName(debtPlanAccede.getUserName());
			// 加入金额
			debtAccedeCommission.setAccedeAccount(debtPlanAccede.getAccedeAccount());
			// 计划编号
			debtAccedeCommission.setPlanNid(debtPlanAccede.getPlanNid());
			// 计划期限
			debtAccedeCommission.setPlanLockPeriod(plan.getDebtLockPeriod());
			// 加入订单号
			debtAccedeCommission.setAccedeOrderId(debtPlanAccede.getAccedeOrderId());
			// 加入时间
			debtAccedeCommission.setAccedeTime(debtPlanAccede.getCreateTime());
			// 状态 0：未发放；1：已发放
			debtAccedeCommission.setStatus(0);
			// 备注
			debtAccedeCommission.setRemark("3");
			// 计算时间
			debtAccedeCommission.setComputeTime(GetDate.getMyTimeInMillis());

			// 获得提成的地区名
			debtAccedeCommission.setRegionName(debtPlanAccede.getInviteRegionName());
			debtAccedeCommission.setRegionId(debtPlanAccede.getInviteRegionId());
			// 获得提成的分公司
			debtAccedeCommission.setBranchName(debtPlanAccede.getInviteBranchName());
			debtAccedeCommission.setBranchId(debtPlanAccede.getInviteBranchId());
			// 获得提成的部门
			debtAccedeCommission.setDepartmentName(debtPlanAccede.getInviteDepartmentName());
			debtAccedeCommission.setDepartmentId(debtPlanAccede.getInviteDepartmentId());

			UsersInfo accedeUsersInfo = getUsersInfoByUserId(debtPlanAccede.getUserId());
			int tenderIs51 = 0; // 1 是
			if (accedeUsersInfo != null && accedeUsersInfo.getIs51() != null) {
				tenderIs51 = accedeUsersInfo.getIs51();
			}
			// 提成人id
			if (debtPlanAccede.getUserAttribute() == 3) {
				// 出借时出借人是线上员工时，提成人是自己
				debtAccedeCommission.setUserId(debtPlanAccede.getUserId());
				if (tenderIs51 == 1) {
					is51 = true;
				}
			} else {
				UsersInfo refererUsersInfo = getUsersInfoByUserId(debtPlanAccede.getInviteUserId());
				if (debtPlanAccede.getInviteUserAttribute() != null && debtPlanAccede.getInviteUserAttribute() == 3) {
					// 出借时推荐人的用户属性是线上员工，提成人是推荐人
					debtAccedeCommission.setUserId(debtPlanAccede.getInviteUserId());
					if (refererUsersInfo != null && refererUsersInfo.getIs51() != null && refererUsersInfo.getIs51() == 1) {
						is51 = true;
					}
				} else if (debtPlanAccede.getInviteUserAttribute() != null && debtPlanAccede.getInviteUserAttribute() < 2) {
					// 出借时推荐人不是员工，且推荐人是51老用户，提成人是推荐人
					if (refererUsersInfo != null && refererUsersInfo.getIs51() != null && refererUsersInfo.getIs51() == 1) {
						debtAccedeCommission.setUserId(debtPlanAccede.getInviteUserId());
						is51 = true;
					}
				}
			}
			// 提成人用户名
			Users user = this.getUsersByUserId(debtAccedeCommission.getUserId());
			if (user != null) {
				debtAccedeCommission.setUserName(user.getUsername());
			}
			// 是51老用户
			if (is51) {
				debtAccedeCommission.setIs51(1);
			} else {
				// 不是51老用户
				debtAccedeCommission.setIs51(0);
			}
			if (debtAccedeCommission.getUserId() == null || debtAccedeCommission.getUserId() == 0) {
				// 如果没有提成人，返回
				continue;
			}
			this.calculateCommission(debtAccedeCommission, debtPlanAccede.getAccedeAccount(), plan);
			if (debtAccedeCommission.getCommission() != null && debtAccedeCommission.getCommission().compareTo(BigDecimal.ZERO) > 0) {
				DebtAccedeCommissionExample accedeCommissionExample = new DebtAccedeCommissionExample();
				DebtAccedeCommissionExample.Criteria accedeCommissionCra = accedeCommissionExample.createCriteria();
				accedeCommissionCra.andAccedeOrderIdEqualTo(debtPlanAccede.getAccedeOrderId()).andPlanNidEqualTo(plan.getDebtPlanNid());
				if (this.debtAccedeCommissionMapper.countByExample(accedeCommissionExample) == 0) {
					commissionTotal = commissionTotal.add(debtAccedeCommission.getCommission());
					// 执行插入
					ret += this.debtAccedeCommissionMapper.insertSelective(debtAccedeCommission);
				} else {
					ret++;
				}
			}
		}
		// 更新plan表的提成计算状态
		plan.setCommissionStatus(1);
		plan.setCommissionTotal(commissionTotal);
		ret += this.debtPlanMapper.updateByPrimaryKeyWithBLOBs(plan);
		return ret;
	}

	/**
	 * 计算提成
	 * 
	 * @Title calculateCommission
	 * @param debtAccedeCommission
	 * @param accedeAccount
	 * @param plan
	 */
	private void calculateCommission(DebtAccedeCommission debtAccedeCommission, BigDecimal accedeAccount, DebtPlan plan) {
		BigDecimal commission = accedeAccount.multiply(new BigDecimal(this.getScales())).setScale(2, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(plan.getDebtLockPeriod()))
				.setScale(2, BigDecimal.ROUND_DOWN);
		debtAccedeCommission.setCommission(commission);
	}

	/**
	 * 获取提成配置中的费率
	 * 
	 * @Title getScales
	 * @return
	 */
	private String getScales() {
		DebtCommissionConfigExample example = new DebtCommissionConfigExample();
		DebtCommissionConfigExample.Criteria cra = example.createCriteria();
		// 提成类型:0:普通提成,1:超额提成'
		cra.andCommissionTypeEqualTo(0);
		// 用户类型:0:线上员工
		cra.andUserTypeEqualTo(0);
		// 状态 0关闭 1启用
		cra.andStatusEqualTo(1);
		// 删除状态 1:删除 0:未删除
		cra.andDelFlagEqualTo(0);
		example.setOrderByClause(" create_time desc");
		List<DebtCommissionConfig> list = this.debtCommissionConfigMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0).getRate();
		}
		// 检索不到
		return "0.002";
	}

	/**
	 * 检索提成明细列表
	 * 
	 * @Title selectDebtAccedeCommission
	 * @param form
	 * @return
	 */
	@Override
	public List<AdminPlanPushMoneyDetailCustomize> selectDebtAccedeCommission(PlanPushMoneyManageBean form) {
		Map<String, Object> param = new HashMap<String, Object>();
		// 计划编号
		param.put("debtPlanNidSrch", StringUtils.isNotEmpty(form.getDebtPlanNidSrch()) ? form.getDebtPlanNidSrch() : null);
		// 计划订单号
		param.put("accedeOrderIdSrch", StringUtils.isNotEmpty(form.getAccedeOrderIdSrch()) ? form.getAccedeOrderIdSrch() : null);
		// 提成用户名
		param.put("userNameSrch", StringUtils.isNotEmpty(form.getUserNameSrch()) ? form.getUserNameSrch() : null);
		// 部门
		if (Validator.isNotNull(form.getCombotreeSrch())) {
			if (form.getCombotreeSrch().contains(StringPool.COMMA)) {
				String[] list = form.getCombotreeSrch().split(StringPool.COMMA);
				form.setCombotreeListSrch(list);
			} else {
				form.setCombotreeListSrch(new String[] { form.getCombotreeSrch() });
			}
		}
		// 提成人部门
		param.put("combotreeListSrch", form.getCombotreeListSrch());
		// 出借人用户名
		param.put("accedeUserNameSrch", StringUtils.isNotEmpty(form.getAccedeUserNameSrch()) ? form.getAccedeUserNameSrch() : null);
		// 发放状态
		param.put("status", StringUtils.isNotEmpty(form.getStatusSrch()) ? form.getStatusSrch() : null);
		// 出借开始时间
		param.put("accedeStartTimeSearch", StringUtils.isNotEmpty(form.getAccedeStartTimeSearch()) ? form.getAccedeStartTimeSearch() : null);
		// 出借结束时间
		param.put("accedeEndTimeSearch", StringUtils.isNotEmpty(form.getAccedeEndTimeSearch()) ? form.getAccedeEndTimeSearch() : null);
		// 返现开始时间
		param.put("returnStartTimeSearch", StringUtils.isNotEmpty(form.getReturnStartTimeSearch()) ? form.getReturnStartTimeSearch() : null);
		// 返现结束时间
		param.put("returnEndTimeSearch", StringUtils.isNotEmpty(form.getReturnEndTimeSearch()) ? form.getReturnEndTimeSearch() : null);
		//
		if (form.getLimitStart() >= 0) {
			param.put("limitStart", form.getLimitStart());
			param.put("limitEnd", form.getLimitEnd());
		}

		return this.adminPlanPushMoneyDetailCustomizeMapper.selectCommissionList(param);
	}

	/**
	 * 检索提成明细件数
	 * 
	 * @Title countRecordTotal
	 * @param form
	 * @return
	 */
	@Override
	public int countRecordTotal(PlanPushMoneyManageBean form) {
		Map<String, Object> param = new HashMap<String, Object>();
		// 计划编号
		param.put("debtPlanNidSrch", StringUtils.isNotEmpty(form.getDebtPlanNidSrch()) ? form.getDebtPlanNidSrch() : null);
		// 计划订单号
		param.put("accedeOrderIdSrch", StringUtils.isNotEmpty(form.getAccedeOrderIdSrch()) ? form.getAccedeOrderIdSrch() : null);
		// 提成用户名
		param.put("userNameSrch", StringUtils.isNotEmpty(form.getUserNameSrch()) ? form.getUserNameSrch() : null);

		// 部门
		if (Validator.isNotNull(form.getCombotreeSrch())) {
			if (form.getCombotreeSrch().contains(StringPool.COMMA)) {
				String[] list = form.getCombotreeSrch().split(StringPool.COMMA);
				form.setCombotreeListSrch(list);
			} else {
				form.setCombotreeListSrch(new String[] { form.getCombotreeSrch() });
			}
		}
		// 提成人部门
		param.put("combotreeListSrch", form.getCombotreeListSrch());

		// 出借人用户名
		param.put("accedeUserNameSrch", StringUtils.isNotEmpty(form.getAccedeUserNameSrch()) ? form.getAccedeUserNameSrch() : null);
		// 发放状态
		param.put("status", StringUtils.isNotEmpty(form.getStatusSrch()) ? form.getStatusSrch() : null);
		// 出借开始时间
		param.put("accedeStartTimeSearch", StringUtils.isNotEmpty(form.getAccedeStartTimeSearch()) ? form.getAccedeStartTimeSearch() : null);
		// 出借结束时间
		param.put("accedeEndTimeSearch", StringUtils.isNotEmpty(form.getAccedeEndTimeSearch()) ? form.getAccedeEndTimeSearch() : null);
		// 返现开始时间
		param.put("returnStartTimeSearch", StringUtils.isNotEmpty(form.getReturnStartTimeSearch()) ? form.getReturnStartTimeSearch() : null);
		// 返现结束时间
		param.put("returnEndTimeSearch", StringUtils.isNotEmpty(form.getReturnEndTimeSearch()) ? form.getReturnEndTimeSearch() : null);
		if (form.getLimitStart() >= 0) {
			param.put("limitStart", form.getLimitStart());
			param.put("limitEnd", form.getLimitEnd());
		}
		return this.adminPlanPushMoneyDetailCustomizeMapper.countRecordTotal(param);
	}

	/**
	 * 根据id,加入订单号检索提成信息
	 * 
	 * @Title selectAccedeCommissionByIdAndAccedeOrderId
	 * @param ids
	 * @param accedeOrderId
	 * @return
	 */
	@Override
	public DebtAccedeCommission selectAccedeCommissionByIdAndAccedeOrderId(String ids, String accedeOrderId) {
		if (StringUtils.isNotEmpty(ids) && StringUtils.isNotEmpty(accedeOrderId)) {
			DebtAccedeCommissionExample example = new DebtAccedeCommissionExample();
			DebtAccedeCommissionExample.Criteria cra = example.createCriteria();
			cra.andIdEqualTo(Integer.parseInt(ids));
			cra.andAccedeOrderIdEqualTo(accedeOrderId);
			List<DebtAccedeCommission> list = this.debtAccedeCommissionMapper.selectByExample(example);
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		return null;
	}

	/**
	 * 根据用户id查询其在crm中的员工属性
	 * 
	 * @param id
	 * @return
	 */
	public Integer queryCrmCuttype(Integer userid) {

		Integer cuttype = this.employeeCustomizeMapper.queryCuttype(userid);
		return cuttype;
	}

	/**
	 * 发提成处理
	 * 
	 * @Title updateAccedeCommissionRecord
	 * @param commission
	 * @param chinapnrBean
	 * @return
	 */
	public int updateAccedeCommissionRecord(DebtAccedeCommission commission, ChinapnrBean chinapnrBean) {
		int ret = 0;
		Integer nowTime = GetDate.getMyTimeInMillis();
		// 发提成订单ID
		String orderId = chinapnrBean.getOrdId();
		//
		AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
		// 返现操作人
		String returnUserId = adminSystem.getId();
		// 返现操作人用户名
		String returnUserName = adminSystem.getUsername();
		// 操作者用户名
		String operator = ShiroUtil.getLoginUsername();

		// 更新发放状态
		// 已发放
		commission.setOrderId(orderId);
		commission.setStatus(1);
		commission.setReturnTime(nowTime);
		commission.setReturnUserId(Integer.parseInt(returnUserId));
		commission.setReturnUserName(returnUserName);
		ret += this.debtAccedeCommissionMapper.updateByPrimaryKeySelective(commission);

		// 写入发放记录表
		SpreadsLog spreadsLog = new SpreadsLog();
		spreadsLog.setUserId(commission.getAccedeUserId());
		spreadsLog.setSpreadsUserid(commission.getUserId());
		spreadsLog.setNid(chinapnrBean.getOrdId());
		// 汇添金
		spreadsLog.setType("HTJ");
		// 汇添金加入
		spreadsLog.setSpreadsType("accede");
		// 本金
		spreadsLog.setAccountType("capital");
		// 利率
		spreadsLog.setScales(this.getScales());
		// 汇添金计划编号
		spreadsLog.setBorrowNid(commission.getPlanNid());
		// 加入者ID
		spreadsLog.setTenderId(commission.getAccedeUserId());
		spreadsLog.setRepayId(0);
		spreadsLog.setAccountAll(BigDecimal.ZERO);
		// 加入本金
		spreadsLog.setAccountCapital(commission.getAccedeAccount());
		spreadsLog.setAccountInterest(BigDecimal.ZERO);
		spreadsLog.setAccount(commission.getCommission());
		spreadsLog.setRemark("汇添金计划提成");
		spreadsLog.setAddtime(String.valueOf(nowTime));
		spreadsLog.setAddip(chinapnrBean.getLogIp());
		spreadsLog.setPayStatus(1);
		spreadsLog.setIsValid(1);
		ret += this.spreadsLogMapper.insertSelective(spreadsLog);

		// 更新账户信息
		AccountExample accountExample = new AccountExample();
		AccountExample.Criteria accountCriteria = accountExample.createCriteria();
		accountCriteria.andUserIdEqualTo(commission.getUserId());
		Account account = accountMapper.selectByExample(accountExample).get(0);
		BigDecimal money = new BigDecimal(chinapnrBean.getTransAmt());// 提成
		account.setTotal(account.getTotal().add(money)); // 累加到账户总资产
		account.setBalance(account.getBalance().add(money)); // 累加可用余额
		account.setIncome(account.getIncome().add(money));// 累加到总收入
		ret += this.accountMapper.updateByExampleSelective(account, accountExample);

		account = accountMapper.selectByExample(accountExample).get(0);
		// 写入收支明细
		AccountList accountList = new AccountList();

		accountList.setNid(commission.getOrderId());
		accountList.setUserId(commission.getUserId());
		accountList.setAmount(money);
		accountList.setType(1);// 1收入2支出3冻结
		accountList.setTrade("plan_spreads_accede");
		accountList.setTradeCode("balance");
		accountList.setTotal(account.getTotal());
		accountList.setBalance(account.getBalance());
		accountList.setFrost(account.getFrost());
		accountList.setAwait(account.getAwait());
		accountList.setRepay(account.getRepay());
		accountList.setPlanBalance(account.getPlanBalance());
		accountList.setPlanFrost(account.getPlanFrost());
		accountList.setRemark(chinapnrBean.getLogRemark());
		accountList.setCreateTime(nowTime);
		accountList.setOperator(operator);
		accountList.setIp(chinapnrBean.getLogIp());
		accountList.setIsUpdate(0);
		accountList.setBaseUpdate(0);
		accountList.setInterest(null);
		accountList.setWeb(2);
		ret += this.accountListMapper.insertSelective(accountList);

		// 插入网站收支明细记录
		AccountWebList accountWebList = new AccountWebList();
		accountWebList.setOrdid(accountList.getNid());// 订单号
		accountWebList.setUserId(accountList.getUserId()); // 出借者
		accountWebList.setAmount(accountList.getAmount()); // 管理费
		accountWebList.setType(CustomConstants.TYPE_OUT); // 类型1收入 2支出
		accountWebList.setTrade(CustomConstants.TRADE_HTJTC); // 汇添金计划提成
		accountWebList.setTradeType(CustomConstants.TRADE_HTJTC_NM); // 汇添金计划提成
		accountWebList.setRemark(CustomConstants.TRADE_HTJTC_REMARK); // 汇添金计划提成
		accountWebList.setCreateTime(GetterUtil.getInteger(accountList.getCreateTime()));
		ret += insertAccountWebList(accountWebList);

		// 纯发短信接口
		Map<String, String> replaceMap = new HashMap<String, String>();
		replaceMap.put("val_amount", commission.getCommission().toString());
		SmsMessage smsMessage = new SmsMessage(commission.getUserId(), replaceMap, null, null, MessageDefine.SMSSENDFORUSER, null, CustomConstants.PARAM_TPL_SDTGTC, CustomConstants.CHANNEL_TYPE_NORMAL);
		smsProcesser.gather(smsMessage);
		//
		UserInfoCustomize userInfo = this.userInfoCustomizeMapper.selectUserInfoByUserId(commission.getUserId());
		if (userInfo != null) {
			Map<String, String> param = new HashMap<String, String>();
			if (userInfo.getTrueName() != null && userInfo.getTrueName().length() > 1) {
				param.put("val_name", userInfo.getTrueName().substring(0, 1));
			} else {
				param.put("val_name", userInfo.getTrueName());
			}
			if ("1".equals(userInfo.getSex())) {
				param.put("val_sex", "先生");
			} else if ("2".equals(userInfo.getSex())) {
				param.put("val_sex", "女士");
			} else {
				param.put("val_sex", "");
			}
			param.put("val_amount", commission.getCommission().toString());
			AppMsMessage appMsMessage = new AppMsMessage(null, param, userInfo.getMobile(), MessageDefine.APPMSSENDFORMOBILE, CustomConstants.JYTZ_TPL_SDTGTC);
			appMsProcesser.gather(appMsMessage);
		}
		return ret;
	}

	/**
	 * 插入网站收支记录
	 *
	 * @param nid
	 * @return
	 */
	private int insertAccountWebList(AccountWebList accountWebList) {
		if (countAccountWebList(accountWebList.getOrdid(), accountWebList.getTrade()) == 0) {
			// 设置部门名称
			setDepartments(accountWebList);
			// 插入
			return this.accountWebListMapper.insertSelective(accountWebList);
		}
		return 0;
	}

	/**
	 * 判断网站收支是否存在
	 *
	 * @param nid
	 * @return
	 */
	private int countAccountWebList(String nid, String trade) {
		AccountWebListExample example = new AccountWebListExample();
		example.createCriteria().andOrdidEqualTo(nid).andTradeEqualTo(trade);
		return this.accountWebListMapper.countByExample(example);
	}

	/**
	 * 设置部门名称
	 *
	 * @param accountWebList
	 */
	private void setDepartments(AccountWebList accountWebList) {
		if (accountWebList != null) {
			Integer userId = accountWebList.getUserId();
			UsersInfo usersInfo = getUsersInfoByUserId(userId);

			if (usersInfo != null) {

				Integer attribute = usersInfo.getAttribute();

				if (attribute != null) {
					// 查找用户的的推荐人
					Users users = getUsersByUserId(userId);

					Integer refUserId = users.getReferrer();
					SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
					SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
					spreadsUsersExampleCriteria.andUserIdEqualTo(userId);
					List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
					if (sList != null && !sList.isEmpty()) {
						refUserId = sList.get(0).getSpreadsUserid();
					}

					// 如果是线上员工或线下员工，推荐人的userId和username不插
					if (users != null && (attribute == 2 || attribute == 3)) {
						// 查找用户信息
						EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(userId);
						if (employeeCustomize != null) {
							accountWebList.setRegionName(employeeCustomize.getRegionName());
							accountWebList.setBranchName(employeeCustomize.getBranchName());
							accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
						}
					}
					// 如果是无主单，全插
					else if (users != null && (attribute == 1)) {
						// 查找用户推荐人
						EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
						if (employeeCustomize != null) {
							accountWebList.setRegionName(employeeCustomize.getRegionName());
							accountWebList.setBranchName(employeeCustomize.getBranchName());
							accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
						}
					}
					// 如果是有主单
					else if (users != null && (attribute == 0)) {
						// 查找用户推荐人
						EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
						if (employeeCustomize != null) {
							accountWebList.setRegionName(employeeCustomize.getRegionName());
							accountWebList.setBranchName(employeeCustomize.getBranchName());
							accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
						}
					}
				}
				accountWebList.setTruename(usersInfo.getTruename());
				accountWebList.setFlag(1);
			}
		}

	}

	@Override
	public int updateAccedeCommissoinRecordError(DebtAccedeCommission commission, ChinapnrBean chinapnrBean) {
		int ret = 0;
		Integer nowTime = GetDate.getMyTimeInMillis();
		// 发提成订单ID
		String orderId = chinapnrBean.getOrdId();
		//
		AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
		// 返现操作人
		String returnUserId = adminSystem.getId();
		// 返现操作人用户名
		String returnUserName = adminSystem.getUsername();
		// 更新发放状态
		// 已发放
		commission.setOrderId(orderId);
		// 发送失败
		commission.setStatus(2);
		commission.setReturnTime(nowTime);
		commission.setReturnUserId(Integer.parseInt(returnUserId));
		commission.setReturnUserName(returnUserName);
		ret += this.debtAccedeCommissionMapper.updateByPrimaryKeySelective(commission);
		return ret;
	}
}
