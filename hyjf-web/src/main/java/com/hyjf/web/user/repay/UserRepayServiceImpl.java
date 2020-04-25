package com.hyjf.web.user.repay;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hyjf.common.calculate.UnnormalRepayUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountChinapnrExample;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountListExample;
import com.hyjf.mybatis.model.auto.AccountLog;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.BorrowApicronExample;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRecoverExample;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlan;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlanExample;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.BorrowRepayExample;
import com.hyjf.mybatis.model.auto.BorrowRepayPlan;
import com.hyjf.mybatis.model.auto.BorrowRepayPlanExample;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.BorrowTenderExample;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserInvestListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserRepayProjectListCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import com.hyjf.web.BaseServiceImpl;

@Service
public class UserRepayServiceImpl extends BaseServiceImpl implements UserRepayService {

	/**
	 * 查询用户的待还款项目列表信息
	 */
	@Override
	public List<WebUserRepayProjectListCustomize> searchUserRepayList(UserRepayProjectListBean form, int limitStart,
			int limitEnd) {
		Map<String, Object> params = new HashMap<String, Object>();
		String userId = StringUtils.isNotEmpty(form.getUserId()) ? form.getUserId() : null;
		String roleId = StringUtils.isNotEmpty(form.getRoleId()) ? form.getRoleId() : null;
		String status = StringUtils.isNotEmpty(form.getStatus()) ? form.getStatus() : null;
		String borrowNid = StringUtils.isNotEmpty(form.getBorrowNid()) ? form.getBorrowNid() : null;
		params.put("userId", userId);
		params.put("roleId", roleId);
		params.put("status", status);
		params.put("borrowNid", borrowNid);
		if (limitStart == 0 || limitStart > 0) {
			params.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			params.put("limitEnd", limitEnd);
		}
		List<WebUserRepayProjectListCustomize> list = null;
		if (roleId != null && "3".equals(roleId)) {// 垫付机构
			list = webUserRepayListCustomizeMapper.selectOrgRepayProjectList(params);
		} else {// 借款人
			list = webUserRepayListCustomizeMapper.selectUserRepayProjectList(params);
		}
		return list;
	}

	/**
	 * 统计用户的还款项目的列表数量
	 */
	@Override
	public int countUserRepayRecordTotal(UserRepayProjectListBean form) {
		Map<String, Object> params = new HashMap<String, Object>();
		String userId = StringUtils.isNotEmpty(form.getUserId()) ? form.getUserId() : null;
		String roleId = StringUtils.isNotEmpty(form.getRoleId()) ? form.getRoleId() : null;
		String status = StringUtils.isNotEmpty(form.getStatus()) ? form.getStatus() : null;
		String borrowNid = StringUtils.isNotEmpty(form.getBorrowNid()) ? form.getBorrowNid() : null;
		params.put("userId", userId);
		params.put("roleId", roleId);
		params.put("status", status);
		params.put("borrowNid", borrowNid);
		int total = 0;
		if (roleId != null && "3".equals(roleId)) {// 垫付机构
			total = webUserRepayListCustomizeMapper.countOrgRepayProjectRecordTotal(params);
		} else {// 借款人
			total = webUserRepayListCustomizeMapper.countUserRepayProjectRecordTotal(params);
		}

		return total;
	}

	/**
	 * 查询相应的项目的出借信息
	 */
	@Override
	public List<WebUserInvestListCustomize> selectUserInvestList(String borrowNid, int limitStart, int limitEnd) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("borrowNid", borrowNid);
		if (limitStart == 0 || limitStart > 0) {
			params.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			params.put("limitEnd", limitEnd);
		}
		List<WebUserInvestListCustomize> list = webUserInvestListCustomizeMapper.selectUserInvestList(params);
		return list;
	}

	/**
	 * 统计相应的项目的出借信息总数
	 */
	@Override
	public int countUserInvestRecordTotal(String borrowNid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("borrowNid", borrowNid);
		int total = webUserInvestListCustomizeMapper.countUserInvestRecordTotal(params);
		return total;
	}

	/**
	 * 获取还款人信息
	 */
	@Override
	public Users searchRepayUser(int userId) {
		Users user = usersMapper.selectByPrimaryKey(userId);
		return user;
	}

	/**
	 * 根据还款人id，项目编号查询相应的项目
	 * 
	 * @param userId
	 * @param userName
	 * @param roleId
	 * @param borrowNid
	 * @return
	 */
	@Override
	public Borrow searchRepayProject(int userId, String userName, String roleId, String borrowNid) {
		// 获取当前的用户还款的项目
		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria borrowCrt = example.createCriteria();
		borrowCrt.andBorrowNidEqualTo(borrowNid);
		if (StringUtils.isNotEmpty(roleId) && "3".equals(roleId)) {// 如果是垫付机构
			borrowCrt.andRepayOrgUserIdEqualTo(userId);
		} else {// 普通借款人
			borrowCrt.andUserIdEqualTo(userId);
		}
		List<Borrow> borrows = borrowMapper.selectByExample(example);
		if (borrows != null && borrows.size() == 1) {
			return borrows.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 根据用户id查询相应的用户账户
	 */
	@Override
	public Account searchRepayUserAccount(int userId) {
		AccountExample accountExample = new AccountExample();
		AccountExample.Criteria crt = accountExample.createCriteria();
		crt.andUserIdEqualTo(userId);
		List<Account> accounts = accountMapper.selectByExample(accountExample);
		if (accounts != null && accounts.size() == 1) {
			return accounts.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 查询用户的还款详情
	 */
	@Override
	public UserRepayProjectBean searchRepayProjectDetail(UserRepayProjectBean form)
			throws NumberFormatException, ParseException {

		String userId = StringUtils.isNotEmpty(form.getUserId()) ? form.getUserId() : null;
		String borrowNid = StringUtils.isNotEmpty(form.getBorrowNid()) ? form.getBorrowNid() : null;
		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid);
		if (StringUtils.isNotEmpty(form.getRoleId()) && "3".equals(form.getRoleId())) {// 垫付机构
			crt.andRepayOrgUserIdEqualTo(Integer.parseInt(userId));
		} else {// 普通借款人
			crt.andUserIdEqualTo(Integer.parseInt(userId));
		}
		List<Borrow> projects = borrowMapper.selectByExample(example);// 查询相应的用户还款项目
		if (projects != null && projects.size() > 0) {
			Borrow borrow = projects.get(0);
			// userId 改成借款人的userid！！！
			userId = borrow.getUserId().toString();

			form.settType("0");// 设置为非汇添金专属项目
			// 设置相应的项目名称
			form.setBorrowName(borrow.getName());
			// 获取相应的项目还款方式
			String borrowStyle = StringUtils.isNotEmpty(borrow.getBorrowStyle()) ? borrow.getBorrowStyle() : null;
			form.setBorrowStyle(borrowStyle);
			// Map<String, Object> params = new HashMap<String, Object>();
			// params.put("borrowNid", borrowNid);
			// params.put("userId", userId);
			// 还款总期数
			int periodTotal = borrow.getBorrowPeriod();
			if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)
					|| CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
				RepayByTermBean repay = this.calculateRepay(Integer.parseInt(userId), borrow);
				form.setBorrowPeriod("0");
				form.setBorrowFee(repay.getRepayFee().toString());
				// 计算的是还款总额
				form.setBorrowTotal(repay.getRepayAccountAll().add(repay.getRepayFee()).toString());
				form.setBorrowAccount(repay.getRepayAccount().toString());
				form.setBorrowCapital(repay.getRepayCapital().toString());
				form.setBorrowInterest(repay.getRepayInterest().toString());
				// 判断当前期是否在还款
				BorrowApicronExample exampleBorrowApicron = new BorrowApicronExample();
				BorrowApicronExample.Criteria crtBorrowApicron = exampleBorrowApicron.createCriteria();
				crtBorrowApicron.andBorrowNidEqualTo(borrowNid);
				crtBorrowApicron.andApiTypeEqualTo(1);
				crtBorrowApicron.andPeriodNowEqualTo(1);
				List<BorrowApicron> borrowApicrons = borrowApicronMapper.selectByExample(exampleBorrowApicron);

				if (borrowApicrons != null && borrowApicrons.size() > 0) {
					BorrowApicron borrowApicron = borrowApicrons.get(0);
					if (borrowApicron.getRepayStatus().intValue() != 1
							|| borrowApicron.getCreditRepayStatus().intValue() != 1) {
						// 用户还款当前期
						form.setBorrowStatus("1");
					} else {// 用户未还款当前期
						form.setBorrowStatus("0");
					}
				} else {// 用户未还款当前期
					form.setBorrowStatus("0");
				}
				form.setAdvanceStatus(String.valueOf(repay.getAdvanceStatus()));
				form.setChargeDays(repay.getChargeDays().toString());
				form.setChargeInterest(repay.getChargeInterest().multiply(new BigDecimal("-1")).toString());
				form.setDelayDays(repay.getDelayDays().toString());
				form.setDelayInterest(repay.getDelayInterest().toString());
				form.setLateDays(repay.getLateDays().toString());
				form.setLateInterest(repay.getLateInterest().toString());
				List<UserRepayBean> userRepayList = new ArrayList<UserRepayBean>();
				UserRepayBean userRepayBean = new UserRepayBean();
				// 此处是本息和
				userRepayBean.setRepayAccount(repay.getRepayAccount().toString());
				userRepayBean.setRepayCapital(repay.getRepayCapital().toString());
				userRepayBean.setRepayInterest(repay.getRepayInterest().toString());
				userRepayBean.setChargeDays(repay.getChargeDays().toString());
				userRepayBean.setChargeInterest(repay.getChargeInterest().multiply(new BigDecimal("-1")).toString());
				userRepayBean.setDelayDays(repay.getDelayDays().toString());
				userRepayBean.setDelayInterest(repay.getDelayInterest().toString());
				userRepayBean.setFinanceManage(repay.getRepayFee().toString());
				userRepayBean.setLateDays(repay.getLateDays().toString());
				userRepayBean.setLateInterest(repay.getLateInterest().toString());
				userRepayBean.setRepayTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(repay.getRepayTime())));
				userRepayBean.setRepayTotal(repay.getRepayAccountAll().toString());
				userRepayBean.setStatus(repay.getRepayStatus().toString());
				userRepayBean.setUserId(repay.getUserId().toString());
				userRepayBean.setRepayPeriod("1");
				userRepayBean.setAdvanceStatus(repay.getAdvanceStatus().toString());
				List<BorrowRecover> userRecovers = repay.getRecoverList();
				if (userRecovers != null && userRecovers.size() > 0) {
					List<UserRepayDetailBean> userRepayDetails = new ArrayList<UserRepayDetailBean>();
					for (int i = 0; i < userRecovers.size(); i++) {
						BorrowRecover userRecover = userRecovers.get(i);
						UserRepayDetailBean userRepayDetail = new UserRepayDetailBean();
						userRepayDetail.setRepayAccount(userRecover.getRecoverAccount().toString());
						userRepayDetail.setRepayCapital(userRecover.getRecoverCapital().toString());
						userRepayDetail.setRepayInterest(userRecover.getRecoverInterest().toString());
						userRepayDetail.setChargeDays(userRecover.getChargeDays().toString());
						userRepayDetail.setChargeInterest(
								userRecover.getChargeInterest().multiply(new BigDecimal("-1")).toString());
						userRepayDetail.setDelayDays(userRecover.getDelayDays().toString());
						userRepayDetail.setDelayInterest(userRecover.getDelayInterest().toString());
						userRepayDetail.setFinanceManage(userRecover.getRecoverFee().toString());
						userRepayDetail.setLateDays(userRecover.getLateDays().toString());
						userRepayDetail.setLateInterest(userRecover.getLateInterest().toString());
						userRepayDetail.setAdvanceStatus(userRecover.getAdvanceStatus().toString());
						userRepayDetail.setRepayTime(
								GetDate.getDateMyTimeInMillis(Integer.parseInt(userRecover.getRecoverTime())));
						BigDecimal total = new BigDecimal("0");
						if (userRecover.getRecoverStatus() == 1) {
							total = userRecover.getRecoverAccountYes().add(userRecover.getRecoverFee());
						} else {
							// recover中account未更新
							total = userRecover.getRecoverAccount().add(userRecover.getRecoverFee())
									.add(userRecover.getChargeInterest()).add(userRecover.getDelayInterest())
									.add(userRecover.getLateInterest());
						}
						userRepayDetail.setRepayTotal(total.toString());
						userRepayDetail.setStatus(userRecover.getRecoverStatus().toString());
						userRepayDetail.setUserId(userRecover.getUserId().toString());
						String userName = this.searchUserNameById(userRecover.getUserId());
						String userNameStr = userName.substring(0, 1).concat("**");
						userRepayDetail.setUserName(userNameStr);
						userRepayDetails.add(userRepayDetail);
					}
					userRepayBean.setUserRepayDetailList(userRepayDetails);
					userRepayList.add(userRepayBean);
				}
				form.setUserRepayList(userRepayList);
			} else {
				// 计算分期的项目还款信息
				RepayByTermBean repayByTerm = this.calculateRepayByTerm(Integer.parseInt(userId), borrow);
				System.err.println("++++++++++++++++++++++++++++++++++++++" + repayByTerm);
				System.err.println("++++++++++++++++++++++++++++++++++++++" + periodTotal);
				System.err.println("++++++++++++++++++++++++++++++++++++++" + repayByTerm.getRepayPeriod());
				// 计算当前还款期数
				int repayPeriod = periodTotal - repayByTerm.getRepayPeriod() + 1;
				// 如果用户不是还款最后一期
				if (repayPeriod <= periodTotal) {
					BorrowApicronExample exampleBorrowApicron = new BorrowApicronExample();
					BorrowApicronExample.Criteria crtBorrowApicron = exampleBorrowApicron.createCriteria();
					crtBorrowApicron.andBorrowNidEqualTo(borrowNid);
					crtBorrowApicron.andPeriodNowEqualTo(repayPeriod);
					crtBorrowApicron.andApiTypeEqualTo(1);
					List<BorrowApicron> borrowApicrons = borrowApicronMapper.selectByExample(exampleBorrowApicron);
					// 正在还款当前期
					if (borrowApicrons != null && borrowApicrons.size() > 0) {
						BorrowApicron borrowApicron = borrowApicrons.get(0);
						if (borrowApicron.getRepayStatus().intValue() != 1
								|| borrowApicron.getCreditRepayStatus().intValue() != 1) {
							// 用户还款当前期
							form.setBorrowStatus("1");
						} else {// 用户当前期正在还款
							form.setBorrowStatus("0");
						}
					} else {// 用户未还款当前期
						form.setBorrowStatus("0");
					}
				} else {// 用户正在还款最后一期
					form.setBorrowStatus("1");
				}
				// 设置当前的还款期数
				form.setBorrowPeriod(String.valueOf(repayPeriod));
				// 获取统计的用户还款计划列表
				List<RepayplanRecoverplanBean> userRepayPlans = repayByTerm.getRepayPlanList();
				if (userRepayPlans != null && userRepayPlans.size() > 0) {
					List<UserRepayBean> recoverList = new ArrayList<UserRepayBean>();
					// 遍历计划还款信息，拼接数据
					for (int i = 0; i < userRepayPlans.size(); i++) {
						// 获取用户的还款信息
						RepayplanRecoverplanBean userRepayPlan = userRepayPlans.get(i);
						// 声明需拼接数据的实体
						UserRepayBean userRepayBean = new UserRepayBean();
						// 设置本期的用户本息和
						userRepayBean.setRepayAccount(userRepayPlan.getRepayAccount().toString());
						// 设置本期的用户本金
						userRepayBean.setRepayCapital(userRepayPlan.getRepayCapital().toString());
						// 设置本期的用户利息
						userRepayBean.setRepayInterest(userRepayPlan.getRepayInterest().toString());
						if (userRepayPlan.getRepayStatus() == 1) {// 如果本期已经还款完成
							// 获取本期的用户已还款本息
							userRepayBean.setRepayTotal(userRepayPlan.getRepayAccountYes().toString());
						} else {// 用户未还款本息
								// 此处分期计算的是本息和
							userRepayBean.setRepayTotal(userRepayPlan.getRepayAccount().toString());
						}
						userRepayBean.setUserId(userRepayPlan.getUserId().toString());
						userRepayBean.setRepayPeriod(userRepayPlan.getRepayPeriod().toString());
						userRepayBean.setAdvanceStatus(userRepayPlan.getAdvanceStatus().toString());
						userRepayBean.setStatus(userRepayPlan.getRepayStatus().toString());
						userRepayBean.setRepayTime(
								GetDate.getDateMyTimeInMillis(Integer.parseInt(userRepayPlan.getRepayTime())));
						userRepayBean.setChargeDays(userRepayPlan.getChargeDays().toString());
						userRepayBean.setChargeInterest(
								userRepayPlan.getChargeInterest().multiply(new BigDecimal("-1")).toString());
						userRepayBean.setDelayDays(userRepayPlan.getDelayDays().toString());
						userRepayBean.setDelayInterest(userRepayPlan.getDelayInterest().toString());
						userRepayBean.setFinanceManage(userRepayPlan.getRepayFee().toString());
						userRepayBean.setLateDays(userRepayPlan.getLateDays().toString());
						userRepayBean.setLateInterest(userRepayPlan.getLateInterest().toString());

						if (repayPeriod == userRepayPlan.getRepayPeriod()) {
							form.setBorrowFee(userRepayPlan.getRepayFee().toString());
							// 此处计算的是还款总额包含管理费
							form.setBorrowTotal(
									userRepayPlan.getRepayAccountAll().add(userRepayPlan.getRepayFee()).toString());
							form.setBorrowAccount(userRepayPlan.getRepayAccount().toString());
							form.setBorrowCapital(userRepayPlan.getRepayCapital().toString());
							form.setBorrowInterest(userRepayPlan.getRepayInterest().toString());
							form.setAdvanceStatus(userRepayPlan.getAdvanceStatus().toString());
							form.setChargeDays(userRepayPlan.getChargeDays().toString());
							form.setChargeInterest(
									userRepayPlan.getChargeInterest().multiply(new BigDecimal("-1")).toString());
							form.setDelayDays(userRepayPlan.getDelayDays().toString());
							form.setDelayInterest(userRepayPlan.getDelayInterest().toString());
							form.setLateDays(userRepayPlan.getLateDays().toString());
							form.setLateInterest(userRepayPlan.getLateInterest().toString());
						}
						List<BorrowRecoverPlan> userRecoversDetails = userRepayPlan.getRecoverPlanList();
						List<UserRepayDetailBean> userRepayDetails = new ArrayList<UserRepayDetailBean>();
						for (int j = 0; j < userRecoversDetails.size(); j++) {
							BorrowRecoverPlan userRecoverPlan = userRecoversDetails.get(j);
							UserRepayDetailBean userRepayDetail = new UserRepayDetailBean();
							userRepayDetail.setRepayAccount(userRecoverPlan.getRecoverAccount().toString());
							userRepayDetail.setRepayCapital(userRecoverPlan.getRecoverCapital().toString());
							userRepayDetail.setRepayInterest(userRecoverPlan.getRecoverInterest().toString());
							userRepayDetail.setAdvanceStatus(userRecoverPlan.getAdvanceStatus().toString());
							userRepayDetail.setChargeDays(userRecoverPlan.getChargeDays().toString());
							userRepayDetail.setChargeInterest(
									userRecoverPlan.getChargeInterest().multiply(new BigDecimal("-1")).toString());
							userRepayDetail.setDelayDays(userRecoverPlan.getDelayDays().toString());
							userRepayDetail.setDelayInterest(userRecoverPlan.getDelayInterest().toString());
							userRepayDetail.setFinanceManage(userRecoverPlan.getRecoverFee().toString());
							userRepayDetail.setLateDays(userRecoverPlan.getLateDays().toString());
							userRepayDetail.setLateInterest(userRecoverPlan.getLateInterest().toString());
							userRepayDetail.setRepayTime(
									GetDate.getDateMyTimeInMillis(Integer.parseInt(userRecoverPlan.getRecoverTime())));
							BigDecimal total = new BigDecimal("0");
							if (userRecoverPlan.getRecoverStatus() == 1) {
								total = userRecoverPlan.getRecoverAccountYes().add(userRecoverPlan.getRecoverFee());
							} else {
								// 因recover_plan未进行account字段更新
								total = userRecoverPlan.getRecoverAccount().add(userRecoverPlan.getRecoverFee())
										.add(userRecoverPlan.getChargeInterest())
										.add(userRecoverPlan.getDelayInterest()).add(userRecoverPlan.getLateInterest());
							}
							userRepayDetail.setRepayTotal(total.toString());
							userRepayDetail.setStatus(userRecoverPlan.getRecoverStatus().toString());
							userRepayDetail.setUserId(userRecoverPlan.getUserId().toString());
							String userName = this.searchUserNameById(userRecoverPlan.getUserId());
							String userNameStr = userName.substring(0, 1).concat("**");
							userRepayDetail.setUserName(userNameStr);
							userRepayDetails.add(userRepayDetail);
						}
						userRepayBean.setUserRepayDetailList(userRepayDetails);
						recoverList.add(userRepayBean);
					}
					form.setUserRepayList(recoverList);
				}
			}
			return form;

		} else {
			return null;
		}
	}

	private String searchUserNameById(Integer userId) {
		Users user = usersMapper.selectByPrimaryKey(userId);
		return user.getUsername();
	}

	public BorrowRepay searchRepay(int userId, String borrowNid) {
		// 获取还款总表数据
		BorrowRepayExample borrowRepayExample = new BorrowRepayExample();
		BorrowRepayExample.Criteria borrowRepayCrt = borrowRepayExample.createCriteria();
		borrowRepayCrt.andUserIdEqualTo(userId);
		borrowRepayCrt.andBorrowNidEqualTo(borrowNid);
		List<BorrowRepay> borrowRepays = borrowRepayMapper.selectByExample(borrowRepayExample);
		if (borrowRepays != null && borrowRepays.size() == 1) {
			return borrowRepays.get(0);
		} else {
			return null;
		}
	}

	public List<BorrowRepayPlan> searchRepayPlan(int userId, String borrowNid) {
		BorrowRepayPlanExample borrowRepayPlanExample = new BorrowRepayPlanExample();
		BorrowRepayPlanExample.Criteria borrowRepayPlanCrt = borrowRepayPlanExample.createCriteria();
		borrowRepayPlanCrt.andUserIdEqualTo(userId);
		borrowRepayPlanCrt.andBorrowNidEqualTo(borrowNid);
		List<BorrowRepayPlan> borrowRepayPlans = borrowRepayPlanMapper.selectByExample(borrowRepayPlanExample);
		return borrowRepayPlans;
	}

	public BorrowRepayPlan searchRepayPlan(int userId, String borrowNid, int period) {
		BorrowRepayPlanExample borrowRepayPlanExample = new BorrowRepayPlanExample();
		BorrowRepayPlanExample.Criteria borrowRepayPlanCrt = borrowRepayPlanExample.createCriteria();
		borrowRepayPlanCrt.andUserIdEqualTo(userId);
		borrowRepayPlanCrt.andBorrowNidEqualTo(borrowNid);
		borrowRepayPlanCrt.andRepayPeriodEqualTo(period);
		List<BorrowRepayPlan> borrowRepayPlans = borrowRepayPlanMapper.selectByExample(borrowRepayPlanExample);
		if (borrowRepayPlans != null && borrowRepayPlans.size() == 1) {
			return borrowRepayPlans.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 根据项目id查询相应的用户的待还款信息
	 * 
	 * @param borrowNid
	 * @return
	 */
	private List<BorrowRecover> searchBorrowRecover(String borrowNid) {
		BorrowRecoverExample example = new BorrowRecoverExample();
		BorrowRecoverExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid);
		List<BorrowRecover> borrowRecovers = borrowRecoverMapper.selectByExample(example);
		return borrowRecovers;
	}

	/**
	 * 查询出借用户分期的详情
	 * 
	 * @param borrowNid
	 * @param period
	 * @return
	 */
	private List<BorrowRecoverPlan> searchBorrowRecoverPlan(String borrowNid, int period) {
		BorrowRecoverPlanExample example = new BorrowRecoverPlanExample();
		BorrowRecoverPlanExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid);
		crt.andRecoverPeriodEqualTo(period);
		List<BorrowRecoverPlan> borrowRecovers = borrowRecoverPlanMapper.selectByExample(example);
		return borrowRecovers;
	}

	/**
	 * 计算单期的总的还款信息
	 * 
	 * @param userId
	 * @param borrowNid
	 * @param borrowStyle
	 * @param borrowApr
	 * @return
	 * @throws ParseException
	 */
	@Override
	public RepayByTermBean calculateRepay(int userId, Borrow borrow) throws ParseException {

		RepayByTermBean repay = new RepayByTermBean();
		// 获取还款总表数据
		BorrowRepay borrowRepay = this.searchRepay(userId, borrow.getBorrowNid());
		// 判断是否存在还款数据
		if (borrowRepay != null) {
			// 获取相应的还款信息
			BeanUtils.copyProperties(borrowRepay, repay);
			// 计划还款时间
			String repayTimeStr = borrowRepay.getRepayTime();
			// 获取用户申请的延期天数
			int delayDays = borrowRepay.getDelayDays().intValue();
			repay.setBorrowPeriod(String.valueOf(borrow.getBorrowPeriod()));
			// 未分期默认传分期为0
			this.calculateRecover(repay, borrow, repayTimeStr, delayDays);
		}
		return repay;
	}

	/**
	 * 计算单期的用户的还款信息
	 * 
	 * @param repay
	 * @param borrowNid
	 * @param borrowStyle
	 * @param borrowApr
	 * @param repayTimeStr
	 * @param delayDays
	 * @throws ParseException
	 */
	private void calculateRecover(RepayByTermBean repay, Borrow borrow, String repayTimeStr, int delayDays)
			throws ParseException {
		int time = GetDate.getNowTime10();
		// 用户计划还款时间
		String repayTime = GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(repayTimeStr));
		// 用户实际还款时间
		String factRepayTime = GetDate.getDateTimeMyTimeInMillis(time);
		int distanceDays = GetDate.daysBetween(factRepayTime, repayTime);
		if (distanceDays < 0) {// 用户延期或者逾期了
			int lateDays = delayDays + distanceDays;
			if (lateDays >= 0) {// 用户延期还款
				delayDays = -distanceDays;
				this.calculateRecoverTotalDelay(repay, borrow, delayDays);
			} else {// 用户逾期还款
				lateDays = -lateDays;
				this.calculateRecoverTotalLate(repay, borrow, delayDays, lateDays);
			}
		} else {// 用户正常或者提前还款
			// 获取提前还款的阀值
			String repayAdvanceDay = this.getBorrowConfig("REPAY_ADVANCE_TIME");
			int advanceDays = distanceDays;
			// 用户提前还款
			if ((borrow.getProjectType() == 13 && borrow.getBorrowStyle().equals(CustomConstants.BORROW_STYLE_ENDDAY)
					&& advanceDays > 0)
					|| (borrow.getProjectType() == 13
							&& borrow.getBorrowStyle().equals(CustomConstants.BORROW_STYLE_END)
							&& Integer.parseInt(repayAdvanceDay) < advanceDays)
					|| (borrow.getProjectType() != 13 && Integer.parseInt(repayAdvanceDay) < advanceDays)) {
				// 计算用户实际还款总额
				this.calculateRecoverTotalAdvance(repay, borrow, advanceDays);
			} else {// 用户正常还款
					// 计算用户实际还款总额
				this.calculateRecoverTotal(repay, borrow, advanceDays);
			}
		}
	}

	/**
	 * 统计单期还款用户提前还款的总标
	 * 
	 * @param repay
	 * @param borrowNid
	 * @param borrowApr
	 * @param interestDay
	 * @throws ParseException
	 */
	private void calculateRecoverTotalAdvance(RepayByTermBean repay, Borrow borrow, int interestDay)
			throws ParseException {

		// 用户提前还款
		// 用户实际还款额
		BigDecimal userAccountTotal = new BigDecimal(0);
		// 提前还款利息
		BigDecimal repayChargeInterest = new BigDecimal(0);
		List<BorrowRecover> borrowRecovers = searchBorrowRecover(borrow.getBorrowNid());
		if (borrowRecovers != null && borrowRecovers.size() > 0) {
			for (int i = 0; i < borrowRecovers.size(); i++) {
				BorrowRecover borrowRecover = borrowRecovers.get(i);
				String recoverTime = GetDate
						.getDateTimeMyTimeInMillis(Integer.parseInt(borrowRecover.getRecoverTime()));
				String createTime = GetDate.getDateTimeMyTimeInMillis(borrowRecover.getCreateTime());
				// 获取这两个时间之间有多少天
				int totalDays = GetDate.daysBetween(createTime, recoverTime);
				// 获取未还款前用户能够获取的本息和
				BigDecimal userAccount = borrowRecover.getRecoverAccount();
				// 获取用户出借项目分期后的出借本金
				BigDecimal userCapital = borrowRecover.getRecoverCapital();
				if (borrowRecovers.get(i).getCreditAmount().intValue() == 0) {
					// 计算用户实际获得的本息和
					BigDecimal userAccountFact = new BigDecimal(0);
					// 计算用户提前还款减少的的利息
					BigDecimal userChargeInterest = new BigDecimal(0);
					// 如果项目类型为融通宝，调用新的提前还款利息计算公司
					if (borrow.getProjectType() == 13
							&& borrow.getBorrowStyle().equals(CustomConstants.BORROW_STYLE_ENDDAY)) {
						// 提前还款不应该大于本次计息时间
						if (totalDays < interestDay) {
							// 计算出借用户实际获得的本息和
							userAccountFact = UnnormalRepayUtils.aheadRTBRepayPrincipalInterest(userAccount,
									userCapital, borrow.getBorrowApr(), totalDays);
							// 用户提前还款减少的利息
							userChargeInterest = UnnormalRepayUtils.aheadRTBRepayChargeInterest(userCapital,
									borrow.getBorrowApr(), totalDays);
						} else {
							// 计算出借用户实际获得的本息和
							userAccountFact = UnnormalRepayUtils.aheadRTBRepayPrincipalInterest(userAccount,
									userCapital, borrow.getBorrowApr(), interestDay);
							// 用户提前还款减少的利息
							userChargeInterest = UnnormalRepayUtils.aheadRTBRepayChargeInterest(userCapital,
									borrow.getBorrowApr(), interestDay);
						}
					} else {
						// 提前还款不应该大于本次计息时间
						if (totalDays < interestDay) {
							// 计算出借用户实际获得的本息和
							userAccountFact = UnnormalRepayUtils.aheadRepayPrincipalInterest(userAccount, userCapital,
									borrow.getBorrowApr(), totalDays);

							// 用户提前还款减少的利息
							userChargeInterest = UnnormalRepayUtils.aheadRepayChargeInterest(userCapital,
									borrow.getBorrowApr(), totalDays);
						} else {
							// 计算出借用户实际获得的本息和
							userAccountFact = UnnormalRepayUtils.aheadRepayPrincipalInterest(userAccount, userCapital,
									borrow.getBorrowApr(), interestDay);
							// 用户提前还款减少的利息
							userChargeInterest = UnnormalRepayUtils.aheadRepayChargeInterest(userCapital,
									borrow.getBorrowApr(), interestDay);
						}
					}
					borrowRecovers.get(i).setChargeInterest(userChargeInterest.multiply(new BigDecimal(-1)));
					// 统计本息总和
					userAccountTotal = userAccountTotal.add(userAccountFact);
					// 统计提前还款减少的利息
					repayChargeInterest = repayChargeInterest.add(userChargeInterest);
				} else {
					borrowRecovers.get(i).setChargeInterest(new BigDecimal("0"));
					// 统计本息总和
					userAccountTotal = userAccountTotal.add(userAccount);
					// 统计提前还款减少的利息
					repayChargeInterest = repayChargeInterest.add(new BigDecimal("0"));
				}
				borrowRecovers.get(i).setAdvanceStatus(1);
				borrowRecovers.get(i).setChargeDays(interestDay);
			}
			repay.setRecoverList(borrowRecovers);
		}
		repay.setRepayAccount(userAccountTotal);
		repay.setRepayAccountAll(userAccountTotal);
		repay.setChargeDays(interestDay);
		repay.setChargeInterest(repayChargeInterest.multiply(new BigDecimal(-1)));
		repay.setAdvanceStatus(1);

	}

	/**
	 * 统计单期还款用户正常还款的总标
	 * 
	 * @param repay
	 * @param borrowNid
	 * @param borrowStyle
	 * @param borrowApr
	 * @param interestDay
	 * @throws ParseException
	 */
	private void calculateRecoverTotal(RepayByTermBean repay, Borrow borrow, int interestDay) throws ParseException {

		// 正常还款
		List<BorrowRecover> borrowRecovers = searchBorrowRecover(borrow.getBorrowNid());
		if (borrowRecovers != null && borrowRecovers.size() > 0) {
			for (int i = 0; i < borrowRecovers.size(); i++) {
				borrowRecovers.get(i).setChargeDays(interestDay);
				borrowRecovers.get(i).setAdvanceStatus(0);
			}
			repay.setRecoverList(borrowRecovers);
		}
		repay.setChargeDays(interestDay);
		repay.setRepayAccountAll(repay.getRepayAccount());
		repay.setAdvanceStatus(0);
	}

	/**
	 * 统计单期还款用户延期还款的总标
	 * 
	 * @param repay
	 * @param borrowNid
	 * @param borrowApr
	 * @param delayDays
	 * @throws ParseException
	 */
	private void calculateRecoverTotalDelay(RepayByTermBean repay, Borrow borrow, int delayDays) throws ParseException {

		// 用户延期
		// 统计借款用户还款总额
		BigDecimal userAccountTotal = new BigDecimal(0);
		// 统计借款用户总延期利息
		BigDecimal userDelayInterestTotal = new BigDecimal(0);
		List<BorrowRecover> borrowRecovers = searchBorrowRecover(borrow.getBorrowNid());
		if (borrowRecovers != null && borrowRecovers.size() > 0) {
			for (int i = 0; i < borrowRecovers.size(); i++) {
				// 获取未还款前用户能够获取的本息和
				BigDecimal userAccount = borrowRecovers.get(i).getRecoverAccount();
				// 获取用户出借项目分期后的出借本金
				BigDecimal userCapital = borrowRecovers.get(i).getRecoverCapital();
				if (borrowRecovers.get(i).getCreditAmount().intValue() == 0) {
					// 计算用户实际获得的本息和
					BigDecimal userAccountFact = UnnormalRepayUtils.delayRepayPrincipalInterest(userAccount,
							userCapital, borrow.getBorrowApr(), delayDays);
					// 计算用户延期利息
					BigDecimal userDelayInterest = UnnormalRepayUtils.delayRepayInterest(userCapital,
							borrow.getBorrowApr(), delayDays);
					borrowRecovers.get(i).setDelayInterest(userDelayInterest);
					// 统计总和
					userAccountTotal = userAccountTotal.add(userAccountFact);
					userDelayInterestTotal = userDelayInterestTotal.add(userDelayInterest);
				} else {
					borrowRecovers.get(i).setDelayInterest(new BigDecimal(0));
					// 统计总和
					userAccountTotal = userAccountTotal.add(userAccount);
					userDelayInterestTotal = userDelayInterestTotal.add(new BigDecimal(0));
				}
				// 用户延期还款
				borrowRecovers.get(i).setAdvanceStatus(2);
				borrowRecovers.get(i).setDelayDays(delayDays);
			}
			repay.setRecoverList(borrowRecovers);
		}
		repay.setRepayAccountAll(userAccountTotal);
		repay.setRepayAccount(userAccountTotal);
		repay.setDelayDays(delayDays);
		repay.setDelayInterest(userDelayInterestTotal);
		repay.setAdvanceStatus(2);
	}

	/**
	 * 统计单期还款用户逾期还款的总标
	 * 
	 * @param repay
	 * @param borrowNid
	 * @param borrowApr
	 * @param delayDays
	 * @param lateDays
	 * @throws ParseException
	 */
	private void calculateRecoverTotalLate(RepayByTermBean repay, Borrow borrow, int delayDays, int lateDays)
			throws ParseException {

		// 统计借款用户还款总额
		BigDecimal userAccountTotal = new BigDecimal(0);
		// 统计借款用户总延期利息
		BigDecimal userDelayInterestTotal = new BigDecimal(0);
		// 统计借款用户总逾期利息
		BigDecimal userOverdueInterestTotal = new BigDecimal(0);
		List<BorrowRecover> borrowRecovers = searchBorrowRecover(borrow.getBorrowNid());
		if (borrowRecovers != null && borrowRecovers.size() > 0) {
			for (int i = 0; i < borrowRecovers.size(); i++) {
				// 获取未还款前用户能够获取的本息和
				BigDecimal userAccount = borrowRecovers.get(i).getRecoverAccount();
				// 获取用户出借项目分期后的出借本金
				BigDecimal userCapital = borrowRecovers.get(i).getRecoverCapital();
				if (borrowRecovers.get(i).getCreditAmount().intValue() == 0) {
					// 计算用户实际获得的本息和
					BigDecimal userAccountFact = UnnormalRepayUtils.overdueRepayPrincipalInterest(userAccount,
							userCapital, borrow.getBorrowApr(), delayDays, lateDays);
					// 计算用户逾期利息
					BigDecimal userOverdueInterest = UnnormalRepayUtils.overdueRepayOverdueInterest(userAccount,
							lateDays);
					// 计算用户延期利息
					BigDecimal userDelayInterest = UnnormalRepayUtils.overdueRepayDelayInterest(userCapital,
							borrow.getBorrowApr(), delayDays);

					borrowRecovers.get(i).setDelayInterest(userDelayInterest);
					borrowRecovers.get(i).setLateInterest(userOverdueInterest);
					// 统计总和
					userAccountTotal = userAccountTotal.add(userAccountFact);
					userDelayInterestTotal = userDelayInterestTotal.add(userDelayInterest);
					userOverdueInterestTotal = userOverdueInterestTotal.add(userOverdueInterest);
				} else {
					borrowRecovers.get(i).setDelayInterest(new BigDecimal(0));
					borrowRecovers.get(i).setLateInterest(new BigDecimal(0));
					// 统计总和
					userAccountTotal = userAccountTotal.add(userAccount);
					userDelayInterestTotal = userDelayInterestTotal.add(new BigDecimal(0));
					userOverdueInterestTotal = userOverdueInterestTotal.add(new BigDecimal(0));
				}
				borrowRecovers.get(i).setDelayDays(delayDays);
				borrowRecovers.get(i).setLateDays(lateDays);
				borrowRecovers.get(i).setAdvanceStatus(3);
			}
			repay.setRecoverList(borrowRecovers);
		}
		repay.setRepayAccountAll(userAccountTotal);
		repay.setRepayAccount(userAccountTotal);
		repay.setDelayDays(delayDays);
		repay.setDelayInterest(userDelayInterestTotal);
		repay.setLateDays(lateDays);
		repay.setLateInterest(userOverdueInterestTotal);
		repay.setAdvanceStatus(3);
	}

	/**
	 * 计算多期的总的还款信息
	 * 
	 * @param userId
	 * @param borrowNid
	 * @param borrowStyle
	 * @param borrowApr
	 * @return
	 * @throws ParseException
	 */
	@Override
	public RepayByTermBean calculateRepayByTerm(int userId, Borrow borrow) throws ParseException {

		RepayByTermBean repay = new RepayByTermBean();
		// 获取还款总表数据
		BorrowRepay borrowRepay = this.searchRepay(userId, borrow.getBorrowNid());
		// 判断用户的余额是否足够还款
		if (borrowRepay != null) {
			// 获取相应的还款信息
			BeanUtils.copyProperties(borrowRepay, repay);
			repay.setBorrowPeriod(String.valueOf(borrow.getBorrowPeriod()));
			int period = borrow.getBorrowPeriod() - repay.getRepayPeriod() + 1;
			this.calculateRepayPlan(repay, borrow, period);
		}
		return repay;
	}

	/***
	 * 计算用户分期还款本期应还金额
	 * 
	 * @param repay
	 * @param borrowNid
	 * @param borrowApr
	 * @param repayTimeStr
	 * @param delayDays
	 * @throws ParseException
	 */
	private BigDecimal calculateRepayPlan(RepayByTermBean repay, Borrow borrow, int period) throws ParseException {

		List<RepayplanRecoverplanBean> borrowRepayPlanDeails = new ArrayList<RepayplanRecoverplanBean>();
		List<BorrowRepayPlan> borrowRepayPlans = searchRepayPlan(repay.getUserId(), borrow.getBorrowNid());
		BigDecimal repayAccountAll = new BigDecimal("0");
		if (borrowRepayPlans != null && borrowRepayPlans.size() > 0) {
			// 用户实际还款额
			for (int i = 0; i < borrowRepayPlans.size(); i++) {
				RepayplanRecoverplanBean repayPlanDetail = new RepayplanRecoverplanBean();
				BorrowRepayPlan borrowRepayPlan = borrowRepayPlans.get(i);
				if (period == borrowRepayPlan.getRepayPeriod()) {
					String repayTimeStart = null;
					if (i == 0) {
						repayTimeStart = borrowRepayPlan.getCreateTime().toString();
					} else {
						repayTimeStart = borrowRepayPlans.get(i - 1).getRepayTime();
					}
					// 计算还款期的数据
					BeanUtils.copyProperties(borrowRepayPlan, repayPlanDetail);
					this.calculateRecoverPlan(repayPlanDetail, borrow, period, repayTimeStart);
					borrowRepayPlanDeails.add(repayPlanDetail);
					repay.setRepayAccount(repayPlanDetail.getRepayAccount());
					repay.setRepayAccountAll(repayPlanDetail.getRepayAccountAll());
					repay.setRepayInterest(repayPlanDetail.getRepayInterest());
					repay.setRepayCapital(repayPlanDetail.getRepayCapital());
					repay.setRepayFee(repayPlanDetail.getRepayFee());
					repay.setChargeDays(repayPlanDetail.getChargeDays());
					repay.setChargeInterest(repayPlanDetail.getChargeInterest());
					repay.setDelayDays(repayPlanDetail.getDelayDays());
					repay.setDelayInterest(repayPlanDetail.getDelayInterest());
					repay.setLateDays(repayPlanDetail.getLateDays());
					repay.setLateInterest(repayPlanDetail.getLateInterest());
					repayAccountAll = repayPlanDetail.getRepayAccountAll().add(repayPlanDetail.getRepayFee());
				} else {
					borrowRepayPlan.setRepayAccountAll(borrowRepayPlan.getRepayAccount());
					BeanUtils.copyProperties(borrowRepayPlan, repayPlanDetail);
					List<BorrowRecoverPlan> borrowRecoverPlans = searchBorrowRecoverPlan(borrow.getBorrowNid(),
							borrowRepayPlan.getRepayPeriod());
					repayPlanDetail.setRecoverPlanList(borrowRecoverPlans);
					borrowRepayPlanDeails.add(repayPlanDetail);
				}

			}
			repay.setRepayPlanList(borrowRepayPlanDeails);
		}
		return repayAccountAll;
	}

	/***
	 * 计算用户分期还款本期应还金额
	 * 
	 * @param repay
	 * @param borrowNid
	 * @param borrowApr
	 * @param repayTimeStr
	 * @param delayDays
	 * @throws ParseException
	 */
	private void calculateRecoverPlan(RepayplanRecoverplanBean borrowRepayPlan, Borrow borrow, int period,
			String repayTimeStart) throws ParseException {

		int delayDays = borrowRepayPlan.getDelayDays().intValue();
		String repayTimeStr = borrowRepayPlan.getRepayTime();
		int time = GetDate.getNowTime10();
		// 用户计划还款时间
		String repayTime = GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(repayTimeStr));
		// 用户实际还款时间
		String factRepayTime = GetDate.getDateTimeMyTimeInMillis(time);
		// 获取实际还款同计划还款时间的时间差
		int distanceDays = GetDate.daysBetween(factRepayTime, repayTime);
		if (distanceDays < 0) {// 用户延期或者逾期了
			int lateDays = delayDays + distanceDays;
			if (lateDays >= 0) {// 用户延期还款
				delayDays = -distanceDays;
				this.calculateRecoverPlanDelay(borrowRepayPlan, borrow, delayDays);
			} else {// 用户逾期还款
				lateDays = -lateDays;
				this.calculateRecoverPlanLate(borrowRepayPlan, borrow, delayDays, lateDays);
			}
		} else {// 用户正常或者提前还款
				// 获取提前还款的阀值
			String repayAdvanceDay = this.getBorrowConfig("REPAY_ADVANCE_TIME");
			int advanceDays = distanceDays;
			if (Integer.parseInt(repayAdvanceDay) < advanceDays) {// 用户提前还款
				// 计算用户实际还款总额
				this.calculateRecoverPlanAdvance(borrowRepayPlan, borrow, advanceDays, repayTimeStart);
			} else {// 用户正常还款
					// 计算用户实际还款总额
				this.calculateRecoverPlan(borrowRepayPlan, borrow, advanceDays);
			}
		}
	}

	/**
	 * 统计分期还款用户提前还款的总标
	 * 
	 * @param repay
	 * @param borrowNid
	 * @param borrowApr
	 * @param interestDay
	 * @return
	 * @throws ParseException
	 */
	private void calculateRecoverPlanAdvance(RepayplanRecoverplanBean borrowRepayPlan, Borrow borrow, int advanceDays,
			String repayTimeStart) throws ParseException {

		int repayPeriod = borrowRepayPlan.getRepayPeriod();
		List<BorrowRecover> borrowRecoverList = this.selectBorrowRecoverList(borrow.getBorrowNid());
		List<BorrowRecoverPlan> borrowRecoverPlans = searchBorrowRecoverPlan(borrow.getBorrowNid(), repayPeriod);
		// 用户实际还款额
		BigDecimal repayTotal = new BigDecimal(0);
		BigDecimal repayChargeInterest = new BigDecimal(0);
		if (borrowRecoverList != null && borrowRecoverList.size() > 0) {
			if (borrowRecoverPlans != null && borrowRecoverPlans.size() > 0) {
				for (int i = 0; i < borrowRecoverList.size(); i++) {
					BorrowRecover borrowRecover = borrowRecoverList.get(i);
					for (int j = 0; j < borrowRecoverPlans.size(); j++) {
						BorrowRecoverPlan borrowRecoverPlan = borrowRecoverPlans.get(j);
						if (borrowRecover.getNid().equals(borrowRecoverPlan.getNid())
								&& borrowRecover.getUserId().intValue() == borrowRecoverPlan.getUserId().intValue()
								&& borrowRecover.getBorrowNid().equals(borrowRecoverPlan.getBorrowNid())) {
							String recoverTime = GetDate
									.getDateTimeMyTimeInMillis(Integer.parseInt(borrowRecoverPlan.getRecoverTime()));
							String repayStartTime = GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(repayTimeStart));
							// 获取这两个时间之间有多少天
							int totalDays = GetDate.daysBetween(repayStartTime, recoverTime);
							// 获取未还款前用户能够获取的本息和
							BigDecimal userAccount = borrowRecoverPlan.getRecoverAccount();
							// 获取用户出借项目分期后的出借本金
							BigDecimal userCapital = borrowRecoverPlan.getRecoverCapital();
							//当期利息
							BigDecimal userInterest = borrowRecoverPlan.getRecoverInterest();
							if (borrowRecover.getCreditAmount().intValue() == 0) {
								// 用户获得的利息
								// 计算用户实际获得的本息和
								BigDecimal userAccountFact = new BigDecimal(0);
								// 计算用户提前还款减少的的利息
								BigDecimal userChargeInterest = new BigDecimal(0);
								boolean isStyle = CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrow.getBorrowStyle());
								// 提前还款不应该大于本次计息时间
								if (totalDays < advanceDays) {
									// 计算出借用户实际获得的本息和
									userAccountFact = UnnormalRepayUtils.aheadRepayPrincipalInterest(userAccount,
											userCapital, borrow.getBorrowApr(), totalDays);

									userChargeInterest = UnnormalRepayUtils.aheadRepayChargeInterest(userCapital,
												borrow.getBorrowApr(), totalDays);

								} else {
									// 计算出借用户实际获得的本息和
									userAccountFact = UnnormalRepayUtils.aheadRepayPrincipalInterest(userAccount,
											userCapital, borrow.getBorrowApr(), advanceDays);

									userChargeInterest = UnnormalRepayUtils.aheadRepayChargeInterest(userCapital,
												borrow.getBorrowApr(), advanceDays);

								}
								if(isStyle){
									if(advanceDays >= 30){
										userChargeInterest = UnnormalRepayUtils.aheadEndMonthRepayChargeInterest(userInterest,totalDays);
									}else{
										userChargeInterest = UnnormalRepayUtils.aheadEndMonthRepayChargeInterest(userInterest,advanceDays);
									}
								}
								borrowRecoverPlans.get(j)
										.setChargeInterest(userChargeInterest.multiply(new BigDecimal(-1)));
								repayTotal = repayTotal.add(userAccountFact);
								repayChargeInterest = repayChargeInterest.add(userChargeInterest);
							} else {
								borrowRecoverPlans.get(j).setChargeInterest(new BigDecimal(0));
								repayTotal = repayTotal.add(userAccount);
								repayChargeInterest = repayChargeInterest.add(new BigDecimal(0));
							}
							borrowRecoverPlans.get(j).setAdvanceStatus(1);
							borrowRecoverPlans.get(j).setChargeDays(advanceDays);
						}
					}
				}
				borrowRepayPlan.setRecoverPlanList(borrowRecoverPlans);
			}
		}
		borrowRepayPlan.setChargeDays(advanceDays);
		borrowRepayPlan.setChargeInterest(repayChargeInterest.multiply(new BigDecimal(-1)));
		borrowRepayPlan.setRepayAccount(repayTotal);
		borrowRepayPlan.setRepayAccountAll(repayTotal);
		borrowRepayPlan.setAdvanceStatus(1);
	}

	/**
	 * 统计分期还款用户正常还款的总标
	 * 
	 * @param repay
	 * @param borrowNid
	 * @param borrowStyle
	 * @param borrowApr
	 * @param interestDay
	 * @throws ParseException
	 */
	private void calculateRecoverPlan(RepayplanRecoverplanBean borrowRepayPlan, Borrow borrow, int interestDay)
			throws ParseException {

		List<BorrowRecoverPlan> borrowRecoverPlans = searchBorrowRecoverPlan(borrow.getBorrowNid(),
				borrowRepayPlan.getRepayPeriod());
		if (borrowRecoverPlans != null && borrowRecoverPlans.size() > 0) {
			for (int j = 0; j < borrowRecoverPlans.size(); j++) {
				borrowRecoverPlans.get(j).setChargeDays(interestDay);
				borrowRecoverPlans.get(j).setAdvanceStatus(0);
			}
			borrowRepayPlan.setRecoverPlanList(borrowRecoverPlans);
		}
		borrowRepayPlan.setChargeDays(interestDay);
		borrowRepayPlan.setRepayAccountAll(borrowRepayPlan.getRepayAccount());
		borrowRepayPlan.setAdvanceStatus(0);
	}

	/**
	 * 统计分期还款用户延期还款的总标
	 * 
	 * @param repay
	 * @param borrowNid
	 * @param borrowApr
	 * @param delayDays
	 * @throws ParseException
	 */
	private void calculateRecoverPlanDelay(RepayplanRecoverplanBean borrowRepayPlan, Borrow borrow, int delayDays)
			throws ParseException {

		List<BorrowRecover> borrowRecoverList = this.selectBorrowRecoverList(borrow.getBorrowNid());
		List<BorrowRecoverPlan> borrowRecoverPlans = searchBorrowRecoverPlan(borrow.getBorrowNid(),
				borrowRepayPlan.getRepayPeriod());
		// 统计借款用户还款总额
		BigDecimal userAccountTotal = new BigDecimal(0);
		// 统计借款用户总延期利息
		BigDecimal userDelayInterestTotal = new BigDecimal(0);
		if (borrowRecoverList != null && borrowRecoverList.size() > 0) {
			if (borrowRecoverPlans != null && borrowRecoverPlans.size() > 0) {
				for (int i = 0; i < borrowRecoverList.size(); i++) {
					BorrowRecover borrowRecover = borrowRecoverList.get(i);
					for (int j = 0; j < borrowRecoverPlans.size(); j++) {
						BorrowRecoverPlan borrowRecoverPlan = borrowRecoverPlans.get(j);
						if (borrowRecover.getNid().equals(borrowRecoverPlan.getNid())
								&& borrowRecover.getUserId().intValue() == borrowRecoverPlan.getUserId().intValue()
								&& borrowRecover.getBorrowNid().equals(borrowRecoverPlan.getBorrowNid())) {
							// 获取未还款前用户能够获取的本息和
							BigDecimal userAccount = borrowRecoverPlan.getRecoverAccount();
							// 获取用户出借项目分期后的出借本金
							BigDecimal userCapital = borrowRecoverPlan.getRecoverCapital();
							if (borrowRecover.getCreditAmount().intValue() == 0) {
								// 计算用户实际获得的本息和
								BigDecimal userAccountFact = UnnormalRepayUtils.delayRepayPrincipalInterest(userAccount,
										userCapital, borrow.getBorrowApr(), delayDays);
								// 计算用户延期利息
								BigDecimal userDelayInterest = UnnormalRepayUtils.delayRepayInterest(userCapital,
										borrow.getBorrowApr(), delayDays);
								borrowRecoverPlans.get(j).setDelayInterest(userDelayInterest);
								// 统计总和
								userAccountTotal = userAccountTotal.add(userAccountFact);
								userDelayInterestTotal = userDelayInterestTotal.add(userDelayInterest);
							} else {
								borrowRecoverPlans.get(j).setDelayInterest(new BigDecimal(0));
								// 统计总和
								userAccountTotal = userAccountTotal.add(userAccount);
								userDelayInterestTotal = userDelayInterestTotal.add(new BigDecimal(0));
							}
							borrowRecoverPlans.get(j).setDelayDays(delayDays);
							borrowRecoverPlans.get(j).setAdvanceStatus(2);
						}
					}
				}
				borrowRepayPlan.setRecoverPlanList(borrowRecoverPlans);
			}
		}
		borrowRepayPlan.setRepayAccountAll(userAccountTotal);
		borrowRepayPlan.setRepayAccount(userAccountTotal);
		borrowRepayPlan.setDelayDays(delayDays);
		borrowRepayPlan.setDelayInterest(userDelayInterestTotal);
		borrowRepayPlan.setAdvanceStatus(2);
	}

	/**
	 * 统计分期还款用户逾期还款的总标
	 * 
	 * @param repay
	 * @param borrowNid
	 * @param borrowApr
	 * @param delayDays
	 * @param lateDays
	 * @throws ParseException
	 */
	private void calculateRecoverPlanLate(RepayplanRecoverplanBean borrowRepayPlan, Borrow borrow, int delayDays,
			int lateDays) throws ParseException {

		List<BorrowRecover> borrowRecoverList = this.selectBorrowRecoverList(borrow.getBorrowNid());
		List<BorrowRecoverPlan> borrowRecoverPlans = searchBorrowRecoverPlan(borrow.getBorrowNid(),
				borrowRepayPlan.getRepayPeriod());
		// 统计借款用户还款总额
		BigDecimal userAccountTotal = new BigDecimal(0);
		// 统计借款用户总延期利息
		BigDecimal userDelayInterestTotal = new BigDecimal(0);
		// 统计借款用户总逾期利息
		BigDecimal userOverdueInterestTotal = new BigDecimal(0);
		if (borrowRecoverList != null && borrowRecoverList.size() > 0) {
			if (borrowRecoverPlans != null && borrowRecoverPlans.size() > 0) {
				for (int i = 0; i < borrowRecoverList.size(); i++) {
					BorrowRecover borrowRecover = borrowRecoverList.get(i);
					for (int j = 0; j < borrowRecoverPlans.size(); j++) {
						BorrowRecoverPlan borrowRecoverPlan = borrowRecoverPlans.get(j);
						if (borrowRecover.getNid().equals(borrowRecoverPlan.getNid())
								&& borrowRecover.getUserId().intValue() == borrowRecoverPlan.getUserId().intValue()
								&& borrowRecover.getBorrowNid().equals(borrowRecoverPlan.getBorrowNid())) {
							// 获取未还款前用户能够获取的本息和
							BigDecimal userAccount = borrowRecoverPlan.getRecoverAccount();
							// 获取用户出借项目分期后的出借本金
							BigDecimal userCapital = borrowRecoverPlan.getRecoverCapital();
							if (borrowRecover.getCreditAmount().intValue() == 0) {
								// 计算用户实际获得的本息和
								BigDecimal userAccountFact = UnnormalRepayUtils.overdueRepayPrincipalInterest(
										userAccount, userCapital, borrow.getBorrowApr(), delayDays, lateDays);
								// 计算用户逾期利息
								BigDecimal userOverdueInterest = UnnormalRepayUtils
										.overdueRepayOverdueInterest(userAccount, lateDays);
								// 计算用户延期利息
								BigDecimal userDelayInterest = UnnormalRepayUtils.overdueRepayDelayInterest(userCapital,
										borrow.getBorrowApr(), delayDays);
								// 保存相应的延期数据
								borrowRecoverPlans.get(j).setDelayInterest(userDelayInterest);
								borrowRecoverPlans.get(j).setLateInterest(userOverdueInterest);
								// 统计总和
								userAccountTotal = userAccountTotal.add(userAccountFact);
								userDelayInterestTotal = userDelayInterestTotal.add(userDelayInterest);
								userOverdueInterestTotal = userOverdueInterestTotal.add(userOverdueInterest);
							} else {
								// 保存相应的延期数据
								borrowRecoverPlans.get(j).setDelayInterest(new BigDecimal(0));
								borrowRecoverPlans.get(j).setLateInterest(new BigDecimal(0));
								// 统计总和
								userAccountTotal = userAccountTotal.add(userAccount);
								userDelayInterestTotal = userDelayInterestTotal.add(new BigDecimal(0));
								userOverdueInterestTotal = userOverdueInterestTotal.add(new BigDecimal(0));
							}
							borrowRecoverPlans.get(j).setDelayDays(delayDays);
							borrowRecoverPlans.get(j).setLateDays(lateDays);
							borrowRecoverPlans.get(j).setAdvanceStatus(3);
						}
					}
				}
				borrowRepayPlan.setRecoverPlanList(borrowRecoverPlans);
			}
		}
		borrowRepayPlan.setRepayAccountAll(userAccountTotal);
		borrowRepayPlan.setRepayAccount(userAccountTotal);
		borrowRepayPlan.setDelayDays(delayDays);
		borrowRepayPlan.setDelayInterest(userDelayInterestTotal);
		borrowRepayPlan.setLateDays(lateDays);
		borrowRepayPlan.setLateInterest(userOverdueInterestTotal);
		borrowRepayPlan.setAdvanceStatus(3);
	}

	@Override
	public BigDecimal searchRepayTotal(int userId, Borrow borrow) throws ParseException {
		RepayByTermBean RepayBean = this.calculateRepay(userId, borrow);
		return RepayBean.getRepayAccount().add(RepayBean.getRepayFee());
	}

	@Override
	public BigDecimal searchRepayByTermTotal(int userId, Borrow borrow, BigDecimal borrowApr, String borrowStyle,
			int periodTotal) throws ParseException {
		BorrowRepay borrowRepay = this.searchRepay(userId, borrow.getBorrowNid());
		BigDecimal repayPlanTotal = new BigDecimal(0);
		// 判断用户的余额是否足够还款
		if (borrowRepay != null) {
			RepayByTermBean repayByTerm = new RepayByTermBean();
			// 获取相应的还款信息
			BeanUtils.copyProperties(borrowRepay, repayByTerm);
			// 计算当前还款期数
			int period = periodTotal - borrowRepay.getRepayPeriod() + 1;
			repayPlanTotal = calculateRepayPlan(repayByTerm, borrow, period);
		}
		return repayPlanTotal;
	}

	/**
	 * 用户还款
	 */
	@Override
	public boolean updateRepayMoney(RepayByTermBean repay, Integer roleId, Integer repayUserId) {

		int time = GetDate.getNowTime10();
		String borrowNid = repay.getBorrowNid();
		String periodtotal = repay.getBorrowPeriod();
		int remainRepayPeriod = repay.getRepayPeriod();
		int period = Integer.parseInt(periodtotal) - remainRepayPeriod + 1;
		int userId = repay.getUserId();// 借款人id
		BigDecimal account = repay.getRepayAccount();// 用户实际还款本金
		BigDecimal fee = repay.getRepayFee();// 用户实际还款管理费
		BigDecimal repayAccount = new BigDecimal("0");// 用户应还款金额
		String nid = "";
		Boolean repayFlag = false;
		int errorCount = 0;
		// 不分期还款
		List<BorrowRecover> recoverList = repay.getRecoverList();
		if (recoverList != null && recoverList.size() > 0) {
			// 获取用户本次应还的金额
			BorrowRepay borrowRepay = this.searchRepay(userId, borrowNid);
			repayAccount = borrowRepay.getRepayAccount();
			BorrowApicronExample example = new BorrowApicronExample();
			BorrowApicronExample.Criteria crt = example.createCriteria();
			crt.andBorrowNidEqualTo(borrowNid);
			crt.andApiTypeEqualTo(1); // 0放款1还款
			List<BorrowApicron> borrowApicrons = borrowApicronMapper.selectByExample(example);
			if (borrowApicrons != null && borrowApicrons.size() > 0) {
				BorrowApicron borrowApicron = borrowApicrons.get(0);
				int updateTime = borrowApicron.getUpdateTime();
				if (borrowApicron.getRepayStatus() == null) {
					boolean borrowRecoverFlag = true;
					for (int i = 0; i < recoverList.size(); i++) {
						BorrowRecover borrowRecover = recoverList.get(i);
						BorrowRecover borrowRecoverOld = borrowRecoverMapper.selectByPrimaryKey(borrowRecover.getId());
						borrowRecoverOld.setAdvanceStatus(borrowRecover.getAdvanceStatus());
						borrowRecoverOld.setChargeDays(borrowRecover.getChargeDays());
						borrowRecoverOld.setChargeInterest(borrowRecover.getChargeInterest());
						borrowRecoverOld.setDelayDays(borrowRecover.getDelayDays());
						borrowRecoverOld.setDelayInterest(borrowRecover.getDelayInterest());
						borrowRecoverOld.setLateDays(borrowRecover.getLateDays());
						borrowRecoverOld.setLateInterest(borrowRecover.getLateInterest());
						boolean flag = borrowRecoverMapper.updateByPrimaryKey(borrowRecoverOld) > 0 ? true : false;
						if (!flag) {
							errorCount = errorCount + 1;
						}
						borrowRecoverFlag = borrowRecoverFlag && flag;
					}
					if (borrowRecoverFlag) {
						borrowApicron.setPeriodNow(1);
						borrowApicron.setRepayStatus(0);
						borrowApicron.setUserId(repayUserId);
						if (roleId == 3) {
							borrowApicron.setIsRepayOrgFlag(1);
						} else {
							borrowApicron.setIsRepayOrgFlag(0);
						}
						borrowApicron.setUpdateTime(GetDate.getNowTime10());
						crt.andUpdateTimeEqualTo(updateTime);
						boolean apicronFlag = borrowApicronMapper.updateByExampleWithBLOBs(borrowApicron, example) > 0
								? true : false;
						if (!apicronFlag) {
							throw new RuntimeException("重复还款");
						} else {
							repayFlag = true;
						}
					} else {
						throw new RuntimeException("还款失败！" + "失败数量【" + errorCount + "】");
					}
				} else {
					repayFlag = true;
				}
			} else {// 如果没有还款计划
				boolean borrowRecoverFlag = true;
				for (int i = 0; i < recoverList.size(); i++) {
					BorrowRecover borrowRecover = recoverList.get(i);
					BorrowRecover borrowRecoverOld = borrowRecoverMapper.selectByPrimaryKey(borrowRecover.getId());
					// 出借人信息
					Integer tenderUserId = borrowRecoverOld.getUserId();
					Users users = getUsers(tenderUserId);
					if (users != null) {
						// 获取出借人属性
						UsersInfo userInfo = getUserInfo(tenderUserId);
						// 用户属性 0=>无主单 1=>有主单 2=>线下员工 3=>线上员工
						Integer attribute = null;
						if (userInfo != null) {
							// 获取出借用户的用户属性
							attribute = userInfo.getAttribute();
							if (attribute != null) {
								// 出借人用户属性
								borrowRecoverOld.setTenderUserAttribute(attribute);
								// 如果是线上员工或线下员工，推荐人的userId和username不插
								if (attribute == 2 || attribute == 3) {
									EmployeeCustomize employeeCustomize = employeeCustomizeMapper
											.selectEmployeeByUserId(tenderUserId);
									if (employeeCustomize != null) {
										borrowRecoverOld.setInviteRegionId(employeeCustomize.getRegionId());
										borrowRecoverOld.setInviteRegionName(employeeCustomize.getRegionName());
										borrowRecoverOld.setInviteBranchId(employeeCustomize.getBranchId());
										borrowRecoverOld.setInviteBranchName(employeeCustomize.getBranchName());
										borrowRecoverOld.setInviteDepartmentId(employeeCustomize.getDepartmentId());
										borrowRecoverOld.setInviteDepartmentName(employeeCustomize.getDepartmentName());
									}
								} else if (attribute == 1) {
									SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
									SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample
											.createCriteria();
									spreadsUsersExampleCriteria.andUserIdEqualTo(borrowRecoverOld.getUserId());
									List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
									if (sList != null && sList.size() == 1) {
										int refUserId = sList.get(0).getSpreadsUserid();
										// 查找用户推荐人
										Users userss = getUsers(refUserId);
										if (userss != null) {
											borrowRecoverOld.setInviteUserId(userss.getUserId());
											borrowRecoverOld.setInviteUserName(userss.getUsername());
										}
										// 推荐人信息
										UsersInfo refUsers = getUserInfo(refUserId);
										// 推荐人用户属性
										if (refUsers != null) {
											borrowRecoverOld.setInviteUserAttribute(refUsers.getAttribute());
										}
										// 查找用户推荐人部门
										EmployeeCustomize employeeCustomize = employeeCustomizeMapper
												.selectEmployeeByUserId(refUserId);
										if (employeeCustomize != null) {
											borrowRecoverOld.setInviteRegionId(employeeCustomize.getRegionId());
											borrowRecoverOld.setInviteRegionName(employeeCustomize.getRegionName());
											borrowRecoverOld.setInviteBranchId(employeeCustomize.getBranchId());
											borrowRecoverOld.setInviteBranchName(employeeCustomize.getBranchName());
											borrowRecoverOld.setInviteDepartmentId(employeeCustomize.getDepartmentId());
											borrowRecoverOld
													.setInviteDepartmentName(employeeCustomize.getDepartmentName());
										}
									}
								} else if (attribute == 0) {
									SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
									SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample
											.createCriteria();
									spreadsUsersExampleCriteria.andUserIdEqualTo(tenderUserId);
									List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
									if (sList != null && sList.size() == 1) {
										int refUserId = sList.get(0).getSpreadsUserid();
										// 查找推荐人
										Users userss = getUsers(refUserId);
										if (userss != null) {
											borrowRecoverOld.setInviteUserId(userss.getUserId());
											borrowRecoverOld.setInviteUserName(userss.getUsername());
										}
										// 推荐人信息
										UsersInfo refUsers = getUserInfo(refUserId);
										// 推荐人用户属性
										if (refUsers != null) {
											borrowRecoverOld.setInviteUserAttribute(refUsers.getAttribute());
										}
									}
								}
							}
						}
					}
					borrowRecoverOld.setAdvanceStatus(borrowRecover.getAdvanceStatus());
					borrowRecoverOld.setChargeDays(borrowRecover.getChargeDays());
					borrowRecoverOld.setChargeInterest(borrowRecover.getChargeInterest());
					borrowRecoverOld.setDelayDays(borrowRecover.getDelayDays());
					borrowRecoverOld.setDelayInterest(borrowRecover.getDelayInterest());
					borrowRecoverOld.setLateDays(borrowRecover.getLateDays());
					borrowRecoverOld.setLateInterest(borrowRecover.getLateInterest());
					boolean flag = borrowRecoverMapper.updateByPrimaryKey(borrowRecoverOld) > 0 ? true : false;
					if (!flag) {
						errorCount = errorCount + 1;
					}
					borrowRecoverFlag = borrowRecoverFlag && flag;
				}
				if (borrowRecoverFlag) {
					BorrowApicronExample borrowApicronExample = new BorrowApicronExample();
					BorrowApicronExample.Criteria cra = borrowApicronExample.createCriteria();
					cra.andApiTypeEqualTo(1);// 0:放款,1:还款
					cra.andBorrowNidEqualTo(borrowNid);
					int borrowApicronCount = this.borrowApicronMapper.countByExample(borrowApicronExample);
					// 还款任务>0件
					if (borrowApicronCount > 0) {
						throw new RuntimeException("重复还款");
					}
					int nowTime = GetDate.getNowTime10();
					BorrowApicron borrowApicron = new BorrowApicron();
					borrowApicron.setUserId(repayUserId);
					if (roleId == 3) {
						borrowApicron.setIsRepayOrgFlag(1);
					} else {
						borrowApicron.setIsRepayOrgFlag(0);
					}
					borrowApicron.setBorrowNid(borrowNid);
					nid = repay.getBorrowNid() + "_" + repay.getUserId() + "_1";
					borrowApicron.setNid(nid);
					borrowApicron.setApiType(1);
					borrowApicron.setPeriodNow(1);
					borrowApicron.setRepayStatus(0);
					borrowApicron.setStatus(1);
					borrowApicron.setCreditRepayStatus(0);
					borrowApicron.setCreateTime(nowTime);
					borrowApicron.setUpdateTime(nowTime);
					borrowApicron.setExtraYieldStatus(0);
					borrowApicron.setExtraYieldRepayStatus(0);
					boolean apiCronFlag = borrowApicronMapper.insertSelective(borrowApicron) > 0 ? true : false;
					if (apiCronFlag) {

						// 添加借款表repay还款来源、实际还款人
						BorrowRepay borrowRepay66 = new BorrowRepay();
						borrowRepay66.setId(borrowRepay.getId());
						if (roleId == 3) { // repayUserId不为空，表示垫付机构还款
							borrowRepay66.setRepayMoneySource(2);
							Users u66 = this.getUsers(repayUserId);
							borrowRepay66.setRepayUsername(u66.getUsername());
						} else {
							borrowRepay66.setRepayMoneySource(1);
							Users u66 = this.getUsers(userId);
							borrowRepay66.setRepayUsername(u66.getUsername());
						}
						boolean borrowRepayFlag = borrowRepayMapper.updateByPrimaryKeySelective(borrowRepay66) > 0
								? true : false;
						if (borrowRepayFlag) {
							repayFlag = true;
						} else {
							throw new RuntimeException("还款失败！" + "失败数量【" + errorCount + "】");
						}
					}
				} else {
					throw new RuntimeException("还款失败！！" + "失败数量【" + errorCount + "】");
				}
			}
		}
		List<RepayplanRecoverplanBean> repayPLanList = repay.getRepayPlanList();
		// 分期还款
		if (repayPLanList != null && repayPLanList.size() > 0) {
			for (int i = 0; i < repayPLanList.size(); i++) {
				RepayplanRecoverplanBean borrowRepayPlan = repayPLanList.get(i);
				if (borrowRepayPlan.getRepayPeriod() == period) {
					BorrowRepayPlan borrowRepay = this.searchRepayPlan(userId, borrowNid, period);
					repayAccount = borrowRepay.getRepayAccount();
					BorrowApicronExample example = new BorrowApicronExample();
					BorrowApicronExample.Criteria crt = example.createCriteria();
					crt.andBorrowNidEqualTo(borrowNid);
					crt.andApiTypeEqualTo(1);
					crt.andPeriodNowEqualTo(period);
					List<BorrowApicron> borrowApicrons = borrowApicronMapper.selectByExample(example);
					if (borrowApicrons != null && borrowApicrons.size() > 0) {
						BorrowApicron borrowApicron = borrowApicrons.get(0);
						int updateTime = borrowApicron.getUpdateTime();
						if (borrowApicron.getRepayStatus() == null) {
							boolean borrowRecoverPlanFlag = true;
							List<BorrowRecoverPlan> borrowRecoverPLans = borrowRepayPlan.getRecoverPlanList();
							if (borrowRecoverPLans != null && borrowRecoverPLans.size() > 0) {
								for (int j = 0; j < borrowRecoverPLans.size(); j++) {
									BorrowRecoverPlan borrowRecoverPlan = borrowRecoverPLans.get(j);
									BorrowRecoverPlan borrowRecoverPlanOld = borrowRecoverPlanMapper
											.selectByPrimaryKey(borrowRecoverPlan.getId());

									borrowRecoverPlanOld.setAdvanceStatus(borrowRecoverPlan.getAdvanceStatus());
									borrowRecoverPlanOld.setChargeDays(borrowRecoverPlan.getChargeDays());
									borrowRecoverPlanOld.setChargeInterest(borrowRecoverPlan.getChargeInterest());
									borrowRecoverPlanOld.setDelayDays(borrowRecoverPlan.getDelayDays());
									borrowRecoverPlanOld.setDelayInterest(borrowRecoverPlan.getDelayInterest());
									borrowRecoverPlanOld.setLateDays(borrowRecoverPlan.getLateDays());
									borrowRecoverPlanOld.setLateInterest(borrowRecoverPlan.getLateInterest());
									boolean flag = borrowRecoverPlanMapper.updateByPrimaryKey(borrowRecoverPlanOld) > 0
											? true : false;
									if (!flag) {
										errorCount = errorCount + 1;
									}
									borrowRecoverPlanFlag = borrowRecoverPlanFlag && flag;
								}
							}
							if (borrowRecoverPlanFlag) {
								borrowApicron.setPeriodNow(period);
								borrowApicron.setRepayStatus(0);
								borrowApicron.setUserId(repayUserId);
								if (roleId == 3) {
									borrowApicron.setIsRepayOrgFlag(1);
								} else {
									borrowApicron.setIsRepayOrgFlag(0);
								}
								borrowApicron.setUpdateTime(GetDate.getNowTime10());
								crt.andUpdateTimeEqualTo(updateTime);
								boolean apiCronFlag = borrowApicronMapper.updateByExampleWithBLOBs(borrowApicron,
										example) > 0 ? true : false;
								if (apiCronFlag) {

									// 添加借款表repayplan还款来源、实际还款人
									BorrowRepayPlan borrowRepayPlan66 = new BorrowRepayPlan();
									borrowRepayPlan66.setId(borrowRepayPlan.getId());
									if (roleId == 3) { // 表示垫付机构还款
										borrowRepayPlan66.setRepayMoneySource(2);
										Users u66 = this.getUsers(repayUserId);
										borrowRepayPlan66.setRepayUsername(u66.getUsername());
									} else {
										borrowRepayPlan66.setRepayMoneySource(1);
										Users u66 = this.getUsers(userId);
										borrowRepayPlan66.setRepayUsername(u66.getUsername());
									}
									boolean borrowRepayPlanFlag = borrowRepayPlanMapper
											.updateByPrimaryKeySelective(borrowRepayPlan66) > 0 ? true : false;
									if (borrowRepayPlanFlag) {
										repayFlag = true;
									} else {
										throw new RuntimeException("还款失败！" + "失败数量【" + errorCount + "】");
									}
								} else {
									throw new RuntimeException("重复还款");
								}
							} else {
								throw new RuntimeException("还款失败！！" + "失败数量【" + errorCount + "】");
							}
						} else {
							repayFlag = true;
						}
					} else {
						boolean borrowRecoverPlanFlag = true;
						List<BorrowRecoverPlan> borrowRecoverPLans = borrowRepayPlan.getRecoverPlanList();
						if (borrowRecoverPLans != null && borrowRecoverPLans.size() > 0) {
							for (int j = 0; j < borrowRecoverPLans.size(); j++) {
								BorrowRecoverPlan borrowRecoverPlan = borrowRecoverPLans.get(j);
								BorrowRecoverPlan borrowRecoverPlanOld = borrowRecoverPlanMapper
										.selectByPrimaryKey(borrowRecoverPlan.getId());

								// 出借人信息
								Integer tenderUserId = borrowRecoverPlanOld.getUserId();
								Users users = getUsers(tenderUserId);
								if (users != null) {
									// 获取出借人属性
									UsersInfo userInfo = getUserInfo(tenderUserId);
									// 用户属性 0=>无主单 1=>有主单 2=>线下员工 3=>线上员工
									Integer attribute = null;
									if (userInfo != null) {
										// 获取出借用户的用户属性
										attribute = userInfo.getAttribute();
										if (attribute != null) {
											// 出借人用户属性
											borrowRecoverPlanOld.setTenderUserAttribute(attribute);
											// 如果是线上员工或线下员工，推荐人的userId和username不插
											if (attribute == 2 || attribute == 3) {
												EmployeeCustomize employeeCustomize = employeeCustomizeMapper
														.selectEmployeeByUserId(tenderUserId);
												if (employeeCustomize != null) {
													borrowRecoverPlanOld
															.setInviteRegionId(employeeCustomize.getRegionId());
													borrowRecoverPlanOld
															.setInviteRegionName(employeeCustomize.getRegionName());
													borrowRecoverPlanOld
															.setInviteBranchId(employeeCustomize.getBranchId());
													borrowRecoverPlanOld
															.setInviteBranchName(employeeCustomize.getBranchName());
													borrowRecoverPlanOld
															.setInviteDepartmentId(employeeCustomize.getDepartmentId());
													borrowRecoverPlanOld.setInviteDepartmentName(
															employeeCustomize.getDepartmentName());
												}
											} else if (attribute == 1) {
												SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
												SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample
														.createCriteria();
												spreadsUsersExampleCriteria.andUserIdEqualTo(tenderUserId);
												List<SpreadsUsers> sList = spreadsUsersMapper
														.selectByExample(spreadsUsersExample);
												if (sList != null && sList.size() == 1) {
													int refUserId = sList.get(0).getSpreadsUserid();
													// 查找用户推荐人
													Users userss = getUsers(refUserId);
													if (userss != null) {
														borrowRecoverPlanOld.setInviteUserId(userss.getUserId());
														borrowRecoverPlanOld.setInviteUserName(userss.getUsername());
													}
													// 推荐人信息
													UsersInfo refUsers = getUserInfo(refUserId);
													// 推荐人用户属性
													if (refUsers != null) {
														borrowRecoverPlanOld
																.setInviteUserAttribute(refUsers.getAttribute());
													}
													// 查找用户推荐人部门
													EmployeeCustomize employeeCustomize = employeeCustomizeMapper
															.selectEmployeeByUserId(refUserId);
													if (employeeCustomize != null) {
														borrowRecoverPlanOld
																.setInviteRegionId(employeeCustomize.getRegionId());
														borrowRecoverPlanOld
																.setInviteRegionName(employeeCustomize.getRegionName());
														borrowRecoverPlanOld
																.setInviteBranchId(employeeCustomize.getBranchId());
														borrowRecoverPlanOld
																.setInviteBranchName(employeeCustomize.getBranchName());
														borrowRecoverPlanOld.setInviteDepartmentId(
																employeeCustomize.getDepartmentId());
														borrowRecoverPlanOld.setInviteDepartmentName(
																employeeCustomize.getDepartmentName());
													}
												}
											} else if (attribute == 0) {
												SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
												SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample
														.createCriteria();
												spreadsUsersExampleCriteria.andUserIdEqualTo(tenderUserId);
												List<SpreadsUsers> sList = spreadsUsersMapper
														.selectByExample(spreadsUsersExample);
												if (sList != null && sList.size() == 1) {
													int refUserId = sList.get(0).getSpreadsUserid();
													// 查找推荐人
													Users userss = getUsers(refUserId);
													if (userss != null) {
														borrowRecoverPlanOld.setInviteUserId(userss.getUserId());
														borrowRecoverPlanOld.setInviteUserName(userss.getUsername());
													}
													// 推荐人信息
													UsersInfo refUsers = getUserInfo(refUserId);
													// 推荐人用户属性
													if (refUsers != null) {
														borrowRecoverPlanOld
																.setInviteUserAttribute(refUsers.getAttribute());
													}
												}
											}
										}
									}
								}
								borrowRecoverPlanOld.setAdvanceStatus(borrowRecoverPlan.getAdvanceStatus());
								borrowRecoverPlanOld.setChargeDays(borrowRecoverPlan.getChargeDays());
								borrowRecoverPlanOld.setChargeInterest(borrowRecoverPlan.getChargeInterest());
								borrowRecoverPlanOld.setDelayDays(borrowRecoverPlan.getDelayDays());
								borrowRecoverPlanOld.setDelayInterest(borrowRecoverPlan.getDelayInterest());
								borrowRecoverPlanOld.setLateDays(borrowRecoverPlan.getLateDays());
								borrowRecoverPlanOld.setLateInterest(borrowRecoverPlan.getLateInterest());
								boolean flag = borrowRecoverPlanMapper.updateByPrimaryKey(borrowRecoverPlanOld) > 0
										? true : false;
								if (!flag) {
									errorCount = errorCount + 1;
								}
								borrowRecoverPlanFlag = borrowRecoverPlanFlag && flag;
							}
						}
						if (borrowRecoverPlanFlag) {
							BorrowApicronExample borrowApicronExample = new BorrowApicronExample();
							BorrowApicronExample.Criteria cra = borrowApicronExample.createCriteria();
							cra.andApiTypeEqualTo(1);// 0:放款,1:还款
							cra.andBorrowNidEqualTo(borrowNid);
							cra.andPeriodNowEqualTo(period);
							int borrowApicronCount = this.borrowApicronMapper.countByExample(borrowApicronExample);
							// 还款任务>0件
							if (borrowApicronCount > 0) {
								throw new RuntimeException("重复还款");
							}
							int nowTime = GetDate.getNowTime10();
							BorrowApicron borrowApicron = new BorrowApicron();
							borrowApicron.setUserId(repayUserId);
							if (roleId == 3) {
								borrowApicron.setIsRepayOrgFlag(1);
							} else {
								borrowApicron.setIsRepayOrgFlag(0);
							}
							nid = repay.getBorrowNid() + "_" + repay.getUserId() + "_" + period;
							borrowApicron.setNid(nid);
							borrowApicron.setBorrowNid(borrowNid);
							borrowApicron.setApiType(1);
							borrowApicron.setPeriodNow(period);
							borrowApicron.setRepayStatus(0);
							borrowApicron.setStatus(1);
							borrowApicron.setCreditRepayStatus(0);
							borrowApicron.setExtraYieldStatus(0);
							borrowApicron.setExtraYieldRepayStatus(0);
							borrowApicron.setCreateTime(nowTime);
							borrowApicron.setUpdateTime(nowTime);
							boolean apiCronFlag = borrowApicronMapper.insertSelective(borrowApicron) > 0 ? true : false;
							if (apiCronFlag) {
								repayFlag = true;
							} else {
								throw new RuntimeException("重复还款");
							}
						} else {
							throw new RuntimeException("还款失败！" + "失败数量【" + errorCount + "】");
						}
					}
				}
			}
		}
		if (repayFlag) {
			if (countRepayAccountListByNid(nid) == 0) {
				// 更新account表
				BigDecimal frost = new BigDecimal(0);// 冻结金额
				BigDecimal balance = new BigDecimal(0);// 可用金额
				BigDecimal total = new BigDecimal(0);// 账户总额
				BigDecimal expand = new BigDecimal(0);// 账户总支出
				BigDecimal repayMoney = new BigDecimal(0);// 账户还款总额
				AccountExample accountExample = new AccountExample();
				AccountExample.Criteria criteria = accountExample.createCriteria();
				criteria.andUserIdEqualTo(repayUserId);
				List<Account> accountlist = accountMapper.selectByExample(accountExample);
				if (accountlist != null && accountlist.size() > 0) {
					Account accountBean = accountlist.get(0);
					if (account.add(fee).compareTo(accountBean.getBalance()) == 0
							|| account.add(fee).compareTo(accountBean.getBalance()) == -1) {
						AccountChinapnr accountChinapnr = this.getChinapnrUserInfo(repayUserId);
						if (accountChinapnr != null) {
							BigDecimal userBalance = this.getUserBalance(accountChinapnr.getChinapnrUsrcustid());
							if (account.add(fee).compareTo(userBalance) == 0
									|| account.add(fee).compareTo(userBalance) == -1) {
								// ** 用户符合还款条件，可以还款 *//*
								total = accountBean.getTotal().subtract(account.add(fee));// 减去账户总资产
								balance = accountBean.getBalance().subtract(account.add(fee)); // 减去可用余额
								expand = accountBean.getExpend().add(account.add(fee));// 累加到总支出
								if (roleId != 3) {
									// 借款人还款
									repayMoney = accountBean.getRepay().subtract(repayAccount);// 减去待还金额(提前还款利息)
								} else {
									// 垫付机构还款
									repayMoney = accountBean.getRepay();
								}
								accountBean.setTotal(total);
								accountBean.setBalance(balance);
								accountBean.setExpend(expand);
								accountBean.setRepay(repayMoney);
								System.out.println(
										"用户:" + repayUserId + "***********************************扣除相应的还款金额account："
												+ JSON.toJSONString(accountBean));
								boolean accountFlag = accountMapper.updateByPrimaryKey(accountBean) > 0 ? true : false;
								if (accountFlag) {
									// 插入huiyingdai_account_list表
									AccountList accountListRecord = new AccountList();
									// 生成规则BorrowNid_userid_期数
									accountListRecord.setNid(borrowNid + "_" + repay.getUserId() + "_" + period);
									// 借款人/垫付机构 id
									accountListRecord.setUserId(repayUserId);
									// 操作金额
									accountListRecord.setAmount(account.add(fee));
									// 收支类型1收入2支出3冻结
									accountListRecord.setType(2);
									// 交易类型
									accountListRecord.setTrade("repay_success");
									// 操作识别码
									accountListRecord.setTradeCode("balance");
									// 资金总额
									accountListRecord.setTotal(accountBean.getTotal());
									// 可用金额
									accountListRecord.setBalance(accountBean.getBalance());
									// 冻结金额
									accountListRecord.setFrost(accountBean.getFrost());
									// 待收金额
									accountListRecord.setAwait(accountBean.getAwait());
									// 待还金额
									accountListRecord.setRepay(accountBean.getRepay());
									// 创建时间
									accountListRecord.setCreateTime(time);
									// 操作员
									accountListRecord.setOperator(CustomConstants.OPERATOR_AUTO_REPAY);
									accountListRecord.setRemark(borrowNid);
									// 操作IP
									accountListRecord.setIp(repay.getIp());
									accountListRecord.setBaseUpdate(0);
									accountListRecord.setWeb(0);
									System.out.println(
											"用户:" + repayUserId + "***********************************预插入accountList："
													+ JSON.toJSONString(accountListRecord));
									boolean accountListFlag = this.accountListMapper.insertSelective(accountListRecord) > 0
											? true : false;
									if (accountListFlag) {
										// 写入account_log日志
										AccountLog accountLog = new AccountLog();
										accountLog.setUserId(repayUserId);// 操作用户id
										accountLog.setNid(
												"repay_freeze" + "_" + borrowNid + "_" + repayUserId + "_" + period);
										accountLog.setTotalOld(BigDecimal.ZERO);
										accountLog.setCode("borrow");
										accountLog.setCodeType("repay_freeze");
										accountLog.setCodeNid(borrowNid + "_" + userId + "_" + period);
										accountLog.setBorrowNid(borrowNid);// 收入
										accountLog.setIncomeOld(BigDecimal.ZERO);
										accountLog.setIncomeNew(BigDecimal.ZERO);
										accountLog.setAccountWebStatus(0);
										accountLog.setAccountUserStatus(0);
										accountLog.setAccountType("");
										accountLog.setMoney(account);// 操作金额
										accountLog.setIncome(BigDecimal.ZERO);// 收入
										accountLog.setExpend(BigDecimal.ZERO);// 支出
										accountLog.setExpendNew(BigDecimal.ZERO);
										accountLog.setBalanceOld(BigDecimal.ZERO);
										accountLog.setBalanceNew(BigDecimal.ZERO);
										accountLog.setBalanceCash(BigDecimal.ZERO);
										accountLog.setBalanceCashNew(BigDecimal.ZERO);
										accountLog.setBalanceCashOld(BigDecimal.ZERO);
										accountLog.setExpendOld(BigDecimal.ZERO);
										accountLog.setBalanceCash(BigDecimal.ZERO);// 可提现金额
										accountLog.setBalanceFrost(account.multiply(new BigDecimal(-1)));// 不可提现金额
										accountLog.setFrost(frost);// 冻结金额
										accountLog.setFrostOld(BigDecimal.ZERO);
										accountLog.setFrostNew(BigDecimal.ZERO);
										accountLog.setAwait(BigDecimal.ZERO);// 待收金额
										accountLog.setRepay(BigDecimal.ZERO);// 待还金额
										accountLog.setRepayOld(BigDecimal.ZERO);
										accountLog.setRepayNew(BigDecimal.ZERO);
										accountLog.setAwait(BigDecimal.ZERO);
										accountLog.setAwaitNew(BigDecimal.ZERO);
										accountLog.setAwaitOld(BigDecimal.ZERO);
										accountLog.setType("repay_freeze");// 类型
										accountLog.setToUserid(userId); // 付给谁
										accountLog.setRemark("还款[" + borrowNid + "]扣除资金");// 备注
										accountLog.setAddtime(String.valueOf(time));
										accountLog.setAddip(repay.getIp());
										accountLog.setBalanceFrostNew(BigDecimal.ZERO);
										accountLog.setBalanceFrostOld(BigDecimal.ZERO);
										System.out.println("用户:" + repayUserId
												+ "***********************************预插入accountLog："
												+ JSON.toJSONString(accountLog));
										boolean accountLogFlag = this.accountLogMapper.insertSelective(accountLog) > 0
												? true : false;
										if (accountLogFlag) {
											return true;
										} else {
											throw new RuntimeException("还款失败！" + "插入借款人交易明细日志表accountLog失败！");
										}
									} else {
										throw new RuntimeException("还款失败！" + "插入借款人交易明细表AccountList失败！");
									}
								} else {
									throw new RuntimeException("还款失败！" + "更新借款人账户余额表Account失败！");
								}
							} else {
								throw new RuntimeException("用户汇付账户余额不足!");
							}
						} else {
							throw new RuntimeException("用户开户信息不存在!");
						}
					} else {
						throw new RuntimeException("用户余额不足,还款失败");
					}
				} else {
					throw new RuntimeException("未查询到用户的账户信息，account表查询失败");
				}
			} else {
				throw new RuntimeException("此笔还款的交易明细已存在,请勿重复还款");
			}
		} else {
			throw new RuntimeException("还款失败！" + "失败数量【" + errorCount + "】");
		}
	}

	/**
	 * 查询客户在汇付的余额
	 *
	 * @param usrCustId
	 * @return
	 */
	public BigDecimal getUserBalance(Long usrCustId) {

		BigDecimal balance = BigDecimal.ZERO;
		// 调用汇付接口(交易状态查询)
		ChinapnrBean bean = new ChinapnrBean();
		// 版本号(必须)
		bean.setVersion(ChinaPnrConstant.VERSION_10);
		// 消息类型(必须)
		bean.setCmdId(ChinaPnrConstant.CMDID_QUERY_BALANCE_BG);
		// 用户客户号(必须)
		bean.setUsrCustId(String.valueOf(usrCustId));
		// 写log用参数
		bean.setLogUserId(0);
		// 备注
		bean.setLogRemark("用户余额查询");
		// PC
		bean.setLogClient("0");
		// 调用汇付接口
		ChinapnrBean chinapnrBean = ChinapnrUtil.callApiBg(bean);
		if (chinapnrBean != null) {
			try {
				balance = new BigDecimal(chinapnrBean.getAvlBal().replace(",", ""));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return balance;
	}

	/**
	 * 获取用户在汇付天下的账号信息
	 *
	 * @return
	 */
	@Override
	public AccountChinapnr getChinapnrUserInfo(Integer userId) {
		if (userId != null) {
			AccountChinapnrExample example = new AccountChinapnrExample();
			AccountChinapnrExample.Criteria criteria = example.createCriteria();
			criteria.andUserIdEqualTo(userId);
			List<AccountChinapnr> list = this.accountChinapnrMapper.selectByExample(example);
			if (list != null && list.size() == 1) {
				return list.get(0);
			}
		}
		return null;
	}

	/**
	 * 获取用户在汇付天下的账号信息
	 *
	 * @return
	 */
	@Override
	public BorrowTender getBorrowTenderInfo(Integer UserId, String borrowNid) {
		if (borrowNid != null) {
			BorrowTenderExample example = new BorrowTenderExample();
			BorrowTenderExample.Criteria criteria = example.createCriteria();
			criteria.andBorrowNidEqualTo(borrowNid);
			criteria.andUserIdEqualTo(UserId);
			List<BorrowTender> list = this.borrowTenderMapper.selectByExample(example);
			if (list != null && list.size() == 1) {
				return list.get(0);
			}
		}
		return null;
	}

	/**
	 * 根据项目编号，出借用户，订单号获取用户的放款总记录
	 * 
	 * @param borrowNid
	 * @param userId
	 * @param nid
	 * @return
	 */
	private List<BorrowRecover> selectBorrowRecoverList(String borrowNid) {
		BorrowRecoverExample example = new BorrowRecoverExample();
		BorrowRecoverExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid);
		List<BorrowRecover> borrowRecoverList = this.borrowRecoverMapper.selectByExample(example);
		return borrowRecoverList;
	}

	/**
	 * 获取用户属性信息
	 *
	 * @param userId
	 * @return
	 * @author b
	 */
	public UsersInfo getUserInfo(Integer userId) {
		UsersInfoExample usersInfoExample = new UsersInfoExample();
		UsersInfoExample.Criteria usersInfoExampleCriteria = usersInfoExample.createCriteria();
		usersInfoExampleCriteria.andUserIdEqualTo(userId);
		List<UsersInfo> userInfoList = usersInfoMapper.selectByExample(usersInfoExample);
		UsersInfo usersInfo = null;
		if (userInfoList != null && !userInfoList.isEmpty()) {
			usersInfo = userInfoList.get(0);
		}
		return usersInfo;

	}

	/**
	 * 判断该收支明细是否存在
	 *
	 * @param accountList
	 * @return
	 */
	private int countRepayAccountListByNid(String nid) {
		AccountListExample accountListExample = new AccountListExample();
		accountListExample.createCriteria().andNidEqualTo(nid).andTradeEqualTo("repay_success");
		return this.accountListMapper.countByExample(accountListExample);
	}

	@Override
	public List<WebUserInvestListCustomize> selectUserDebtInvestList(String borrowNid, String orderId, int limitStart,
			int limitEnd) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("borrowNid", borrowNid);
		if (StringUtils.isNotBlank(orderId)) {
			params.put("nid", orderId);
		}
		if (limitStart == 0 || limitStart > 0) {
			params.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			params.put("limitEnd", limitEnd);
		}
		return webUserInvestListCustomizeMapper.selectUserDebtInvestList(params);
	}

	/**
	 * 查询垫付机构的未还款金额
	 * 
	 * @param userId
	 * @return
	 */
	public BigDecimal getRepayOrgRepaywait(Integer userId) {
		BigDecimal result = this.webUserRepayListCustomizeMapper.selectRepayOrgRepaywait(userId);
		return result;
	}
}
