package com.hyjf.admin.manager.activity.returncash;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountWebList;
import com.hyjf.mybatis.model.auto.AccountWebListExample;
import com.hyjf.mybatis.model.auto.ActivityReturncash;
import com.hyjf.mybatis.model.auto.ActivityReturncashExample;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminActivityReturncashCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;

@Service
public class ActivityReturncashServiceImpl extends BaseServiceImpl implements ActivityReturncashService {

	/**
	 * 获取待返现列表数量
	 *
	 * @param form
	 * @return
	 */
	@Override
	public int getReturncashRecordCount(Map<String, Object> paraMap) {
		return adminActivityReturncashCustomizeMapper.selectReturncashCount(paraMap);
	}

	/**
	 * 获取待返现列表
	 *
	 * @return
	 */
	@Override
	public List<AdminActivityReturncashCustomize> getReturncashRecordList(Map<String, Object> paraMap) {
		List<AdminActivityReturncashCustomize> recordList = adminActivityReturncashCustomizeMapper.selectReturncashList(paraMap);

		for (AdminActivityReturncashCustomize record : recordList) {
			record.setRewardTotal(getRewardMoney(record, record.getUserType()));

			if (StringUtils.isNotEmpty(record.getUserType()) && record.getUserType().equals("1") && record.getInvestTotalActivity().compareTo(new BigDecimal(5000)) != -1) {
				record.setHasLostreward("1");

			} else {
				record.setHasLostreward("0");
			}
		}

		return recordList;
	}

	/**
	 * 获取累计待返金额
	 * 
	 * @param paraMap
	 * @return
	 */
	@Override
	public BigDecimal getReturncashAmountTotal(Map<String, Object> paraMap) {
		List<AdminActivityReturncashCustomize> recordList = adminActivityReturncashCustomizeMapper.selectReturncashList(paraMap);
		BigDecimal totalReward = new BigDecimal(0);
		for (AdminActivityReturncashCustomize record : recordList) {
			totalReward = totalReward.add(getRewardMoney(record, record.getUserType()));

		}

		return totalReward;
	}

	/**
	 * 获取已返现列表数量
	 *
	 * @param form
	 * @return
	 */
	@Override
	public int getReturnedcashRecordCount(Map<String, Object> paraMap) {
		return adminActivityReturncashCustomizeMapper.selectReturnedcashCount(paraMap);
	}

	/**
	 * 获取已返现列表
	 *
	 * @return
	 */
	@Override
	public List<AdminActivityReturncashCustomize> getReturnedcashRecordList(Map<String, Object> paraMap) {
		return adminActivityReturncashCustomizeMapper.selectReturnedcashList(paraMap);
	}

	/**
	 * 检查是否返现过
	 */
	@Override
	public boolean checkReturnCashStatus(Integer userId) {
		ActivityReturncashExample returncashExample = new ActivityReturncashExample();
		ActivityReturncashExample.Criteria returncashCriteria = returncashExample.createCriteria();
		returncashCriteria.andUserIdEqualTo(userId);
		returncashCriteria.andStatusEqualTo(1);
		List<ActivityReturncash> returncashList = activityReturncashMapper.selectByExample(returncashExample);

		if (returncashList == null || returncashList.isEmpty()) {
			return false;
		}

		return true;
	}

	/**
	 * 返现处理
	 *
	 * @param form
	 * @return
	 */
	@Override
	public int insertReturncashRecord(Integer userId, String ip, ChinapnrBean bean) {
		int ret = 0;

		// 功能描述
		String note = "活动奖励";
		// 增加时间
		Integer time = GetDate.getMyTimeInMillis();
		// 操作者用户名
		String operator = ShiroUtil.getLoginUsername();

		// 更新账户信息
		AccountExample accountExample = new AccountExample();
		AccountExample.Criteria accountCriteria = accountExample.createCriteria();
		accountCriteria.andUserIdEqualTo(userId);
		Account account = accountMapper.selectByExample(accountExample).get(0);
		BigDecimal money = new BigDecimal(bean.getTransAmt());

		account.setTotal(account.getTotal().add(money)); // 用户总资产=总资产+返现金额
		account.setBalance(account.getBalance().add(money)); // 用户资金总额=资金总额+返现金额
		account.setIncome(account.getIncome().add(money)); // 用户收入=收入+返现金额
		ret += this.accountMapper.updateByExampleSelective(account, accountExample);

		ActivityReturncashExample returncashExample = new ActivityReturncashExample();
		ActivityReturncashExample.Criteria returncashCriteria = returncashExample.createCriteria();
		returncashCriteria.andUserIdEqualTo(userId);
		List<ActivityReturncash> returncashList = activityReturncashMapper.selectByExample(returncashExample);

		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("userId", userId);
		List<AdminActivityReturncashCustomize> result = getReturncashRecordList(paraMap);

		if (returncashList == null || returncashList.isEmpty()) {
			// 插入返现数据
			ActivityReturncash returncashBean = new ActivityReturncash();
			returncashBean.setUserId(userId);
			returncashBean.setDelFlg(0);
			returncashBean.setAddTime(time);
			returncashBean.setAddUser(operator);
			returncashBean.setHasLostreward(Integer.parseInt(result.get(0).getHasLostreward()));
			returncashBean.setInvestTotal(result.get(0).getInvestTotalActivity());
			returncashBean.setIslost(0);
			returncashBean.setOrderId(bean.getOrdId());
			returncashBean.setPhoneNum(result.get(0).getMobile());
			// 加上之前已发放的奖励
			if (userId == 4388) {
				returncashBean.setRewardTotal(money.add(new BigDecimal(20)));
			} else {
				returncashBean.setRewardTotal(money);
			}
			returncashBean.setStatus(1);
			returncashBean.setTrueName(result.get(0).getTruename());
			returncashBean.setUpdateTime(time);
			returncashBean.setUpdateUser(operator);
			returncashBean.setUserName(result.get(0).getUsername());
			activityReturncashMapper.insertSelective(returncashBean);

		} else {
			ActivityReturncash returncash = returncashList.get(0);
			if (returncash.getStatus().equals(0)) {
				// 更新状态为已返现
				returncash.setUserId(userId);
				returncash.setHasLostreward(Integer.parseInt(result.get(0).getHasLostreward()));
				returncash.setInvestTotal(result.get(0).getInvestTotalActivity());
				returncash.setOrderId(bean.getOrdId());
				// 加上之前已发放的奖励
				if (userId == 4388) {
					returncash.setRewardTotal(money.add(new BigDecimal(20)));
				} else {
					returncash.setRewardTotal(money);
				}
				returncash.setStatus(1);
				returncash.setUpdateTime(time);
				returncash.setUpdateUser(operator);

				activityReturncashMapper.updateByPrimaryKeySelective(returncash);
			}
		}

		// 写入收支明细
		AccountList accountList = new AccountList();
		accountList.setNid(bean.getOrdId());
		accountList.setUserId(userId);
		accountList.setAmount(money);
		accountList.setType(1);
		accountList.setTrade("borrowactivity");
		accountList.setTradeCode("balance");
		accountList.setTotal(account.getTotal());
		accountList.setBalance(account.getBalance());
		accountList.setFrost(account.getFrost());
		accountList.setAwait(account.getAwait());
		accountList.setRepay(account.getRepay());
		accountList.setRemark(note);
		accountList.setCreateTime(time);
		accountList.setOperator(operator);
		accountList.setIp(ip);
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
		accountWebList.setTrade("ACTIVITY"); // 充值返现
		accountWebList.setTradeType("活动"); // 充值返现
		accountWebList.setRemark("活动奖励"); // 充值手续费返还
		accountWebList.setCreateTime(time);
		ret += insertAccountWebList(accountWebList);

		// 发送短信
		// Map<String, String> params = new HashMap<String, String>();
		// params.put("val_amount", money.toString());
		// SmsUtil.sendMessages(userId, CustomConstants.PARAM_TPL_SDSXFFX,
		// params);

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

	private static BigDecimal getRewardMoney(AdminActivityReturncashCustomize record, String userType) {
		BigDecimal tenderTotal = record.getInvestTotalActivity();
		if (tenderTotal == null) {
			tenderTotal = BigDecimal.ZERO;
		}
		BigDecimal rewardSub = BigDecimal.ZERO;
		if (!record.getUserId().equals("4388")) {
			// 大于等于1万 小于5万
			if (tenderTotal.compareTo(new BigDecimal(10000)) >= 0 && tenderTotal.compareTo(new BigDecimal(50000)) < 0) {
				rewardSub = rewardSub.add(new BigDecimal(20));
			} else if (tenderTotal.compareTo(new BigDecimal(50000)) >= 0 && tenderTotal.compareTo(new BigDecimal(100000)) < 0) {
				// 大于等于5万 小于10万
				rewardSub = rewardSub.add(new BigDecimal(120));
			} else if (tenderTotal.compareTo(new BigDecimal(100000)) >= 0 && tenderTotal.compareTo(new BigDecimal(150000)) < 0) {
				// 大于等于10万 小于15万
				rewardSub = rewardSub.add(new BigDecimal(240));
			} else if (tenderTotal.compareTo(new BigDecimal(150000)) >= 0 && tenderTotal.compareTo(new BigDecimal(300000)) < 0) {
				// 大于等于15万 小于30万
				rewardSub = rewardSub.add(new BigDecimal(380));
			} else if (tenderTotal.compareTo(new BigDecimal(300000)) >= 0 && tenderTotal.compareTo(new BigDecimal(500000)) < 0) {
				// 大于等于30万 小于50万
				rewardSub = rewardSub.add(new BigDecimal(800));
			} else if (tenderTotal.compareTo(new BigDecimal(500000)) >= 0 && tenderTotal.compareTo(new BigDecimal(1000000)) < 0) {
				// 大于等于50万 小于100万
				rewardSub = rewardSub.add(new BigDecimal(1300));
				BigDecimal jian = tenderTotal.subtract(new BigDecimal(500000));
				int money = jian.intValue() / 50000 * 120;
				rewardSub = rewardSub.add(new BigDecimal(money));
			} else if (tenderTotal.compareTo(new BigDecimal(1000000)) >= 0 && tenderTotal.compareTo(new BigDecimal(1500000)) < 0) {
				// 大于等于100万 小于150万
				rewardSub = rewardSub.add(new BigDecimal(2700));
				BigDecimal jian = tenderTotal.subtract(new BigDecimal(1000000));
				int money = jian.intValue() / 100000 * 300;
				rewardSub = rewardSub.add(new BigDecimal(money));
			} else if (tenderTotal.compareTo(new BigDecimal(1500000)) >= 0) {
				// 大于等于150万
				rewardSub = rewardSub.add(new BigDecimal(4200));
				BigDecimal jian = tenderTotal.subtract(new BigDecimal(1500000));
				rewardSub = rewardSub.add(jian.multiply(new BigDecimal(0.0035)).setScale(0, BigDecimal.ROUND_HALF_UP));
			}
		}

		if (StringUtils.isNotEmpty(userType) && userType.equals("1") && tenderTotal.compareTo(new BigDecimal(5000)) >= 0) {
			rewardSub = rewardSub.add(new BigDecimal(100));

		}

		return rewardSub;

	}

}
