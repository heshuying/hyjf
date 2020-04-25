package com.hyjf.admin.exception.userparamexception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.BorrowTenderExample;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.SpreadsUsersLog;
import com.hyjf.mybatis.model.auto.SpreadsUsersLogExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.customize.admin.AdminEmployeeLeaveCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminEmployeeUserCustomize;

@Service
public class UserTenderExceptionServiceImpl extends BaseServiceImpl implements UserTenderExceptionService {

	/**
	 * 查询固定时间间隔的用户出借列表
	 */
	@Override
	public List<BorrowTender> selectBorrowTenderList(String repairStartDate, String repairEndDate) {
		BorrowTenderExample example = new BorrowTenderExample();
		BorrowTenderExample.Criteria crt = example.createCriteria();
		crt.andAddtimeGreaterThanOrEqualTo(GetDate.strYYYYMMDD2Timestamp2(repairStartDate));
		crt.andAddtimeLessThanOrEqualTo(GetDate.strYYYYMMDD2Timestamp2(repairEndDate));
		List<BorrowTender> borrowTenderList = this.borrowTenderMapper.selectByExample(example);
		return borrowTenderList;
	}

	/**
	 * 更新用户的出借记录
	 */
	@Override
	public void updateUserTender(BorrowTender borrowTender, String repairStartDate, String repairEndDate) {

		// 获取出借用户信息
		int userId = borrowTender.getUserId();
		// 出借人信息
		Users users = getUsers(userId);
		if (users != null) {
			// 出借用户名
			borrowTender.setTenderUserName(users.getUsername());
			// 获取出借人属性
			UsersInfo userInfo = getUserInfo(userId);
			// 用户属性 0=>无主单 1=>有主单 2=>线下员工 3=>线上员工
			Integer attribute = null;
			if (userInfo != null) {
				// 获取出借用户的用户属性
				attribute = userInfo.getAttribute();
				if (attribute != null) {
					attribute = getUserAttribute(userId, attribute, borrowTender.getAddtime(), repairStartDate,
							repairEndDate);
					// 出借人用户属性
					borrowTender.setTenderUserAttribute(attribute);
					// 如果是线上员工或线下员工，推荐人的userId和username不插
					if (attribute == 2 || attribute == 3) {
						AdminEmployeeUserCustomize employeeCustomize = employeeUserCustomizeMapper
								.selectEmployeeByUserId(userId);
						if (employeeCustomize != null) {
							borrowTender.setInviteRegionId(employeeCustomize.getRegionId());
							borrowTender.setInviteRegionName(employeeCustomize.getRegionName());
							borrowTender.setInviteBranchId(employeeCustomize.getBranchId());
							borrowTender.setInviteBranchName(employeeCustomize.getBranchName());
							borrowTender.setInviteDepartmentId(employeeCustomize.getDepartmentId());
							borrowTender.setInviteDepartmentName(employeeCustomize.getDepartmentName());
						} else {
							borrowTender.setInviteRegionId(0);
							borrowTender.setInviteRegionName("");
							borrowTender.setInviteBranchId(0);
							borrowTender.setInviteBranchName("");
							borrowTender.setInviteDepartmentId(0);
							borrowTender.setInviteDepartmentName("");
						}
						borrowTender.setInviteUserId(0);
						borrowTender.setInviteUserName("");
						borrowTender.setInviteUserAttribute(0);
					} else if (attribute == 1) {
						SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
						SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
						spreadsUsersExampleCriteria.andUserIdEqualTo(userId);
						List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
						if (sList != null && sList.size() == 1) {
							int refUserId = sList.get(0).getSpreadsUserid();
							refUserId = getUserRefererId(userId, refUserId, borrowTender.getAddtime(), repairStartDate,
									repairEndDate);
							// 查找用户推荐人
							Users userss = getUsers(refUserId);
							if (userss != null) {
								borrowTender.setInviteUserId(userss.getUserId());
								borrowTender.setInviteUserName(userss.getUsername());
							} else {
								borrowTender.setInviteUserId(0);
								borrowTender.setInviteUserName("");
							}
							// 推荐人信息
							UsersInfo refUsers = getUserInfo(refUserId);
							// 推荐人用户属性
							if (refUsers != null) {
								int refererAttribute = refUsers.getAttribute();
								refererAttribute = getUserAttribute(refUserId, refererAttribute,
										borrowTender.getAddtime(), repairStartDate, repairEndDate);
								borrowTender.setInviteUserAttribute(refererAttribute);
							} else {
								borrowTender.setInviteUserAttribute(0);
							}
							// 查找用户推荐人部门
							AdminEmployeeUserCustomize employeeCustomize = employeeUserCustomizeMapper
									.selectEmployeeByUserId(refUserId);
							if (employeeCustomize != null) {
								borrowTender.setInviteRegionId(employeeCustomize.getRegionId());
								borrowTender.setInviteRegionName(employeeCustomize.getRegionName());
								borrowTender.setInviteBranchId(employeeCustomize.getBranchId());
								borrowTender.setInviteBranchName(employeeCustomize.getBranchName());
								borrowTender.setInviteDepartmentId(employeeCustomize.getDepartmentId());
								borrowTender.setInviteDepartmentName(employeeCustomize.getDepartmentName());
							} else {
								borrowTender.setInviteRegionId(0);
								borrowTender.setInviteRegionName("");
								borrowTender.setInviteBranchId(0);
								borrowTender.setInviteBranchName("");
								borrowTender.setInviteDepartmentId(0);
								borrowTender.setInviteDepartmentName("");
							}
						} else {
							int refUserId = getUserRefererId(userId, 0, borrowTender.getAddtime(), repairStartDate,
									repairEndDate);
							// 查找用户推荐人
							Users userss = getUsers(refUserId);
							if (userss != null) {
								borrowTender.setInviteUserId(userss.getUserId());
								borrowTender.setInviteUserName(userss.getUsername());
							} else {
								borrowTender.setInviteUserId(0);
								borrowTender.setInviteUserName("");
							}
							// 推荐人信息
							UsersInfo refUsers = getUserInfo(refUserId);
							// 推荐人用户属性
							if (refUsers != null) {
								int refererAttribute = refUsers.getAttribute();
								refererAttribute = getUserAttribute(refUserId, refererAttribute,
										borrowTender.getAddtime(), repairStartDate, repairEndDate);
								borrowTender.setInviteUserAttribute(refererAttribute);
							} else {
								borrowTender.setInviteUserAttribute(0);
							}
							// 查找用户推荐人部门
							AdminEmployeeUserCustomize employeeCustomize = employeeUserCustomizeMapper
									.selectEmployeeByUserId(refUserId);
							if (employeeCustomize != null) {
								borrowTender.setInviteRegionId(employeeCustomize.getRegionId());
								borrowTender.setInviteRegionName(employeeCustomize.getRegionName());
								borrowTender.setInviteBranchId(employeeCustomize.getBranchId());
								borrowTender.setInviteBranchName(employeeCustomize.getBranchName());
								borrowTender.setInviteDepartmentId(employeeCustomize.getDepartmentId());
								borrowTender.setInviteDepartmentName(employeeCustomize.getDepartmentName());
							} else {
								borrowTender.setInviteRegionId(0);
								borrowTender.setInviteRegionName("");
								borrowTender.setInviteBranchId(0);
								borrowTender.setInviteBranchName("");
								borrowTender.setInviteDepartmentId(0);
								borrowTender.setInviteDepartmentName("");
							}
						}
					} else if (attribute == 0) {
						SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
						SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
						spreadsUsersExampleCriteria.andUserIdEqualTo(userId);
						List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
						if (sList != null && sList.size() == 1) {
							int refUserId = sList.get(0).getSpreadsUserid();
							refUserId = getUserRefererId(userId, refUserId, borrowTender.getAddtime(), repairStartDate,
									repairEndDate);
							// 查找推荐人
							Users userss = getUsers(refUserId);
							if (userss != null) {
								borrowTender.setInviteUserId(userss.getUserId());
								borrowTender.setInviteUserName(userss.getUsername());
							} else {
								borrowTender.setInviteUserId(0);
								borrowTender.setInviteUserName("");
							}
							// 推荐人信息
							UsersInfo refUsers = getUserInfo(refUserId);
							// 推荐人用户属性
							if (refUsers != null) {
								int refererAttribute = refUsers.getAttribute();
								refererAttribute = getUserAttribute(refUserId, refererAttribute,
										borrowTender.getAddtime(), repairStartDate, repairEndDate);
								borrowTender.setInviteUserAttribute(refererAttribute);
							} else {
								borrowTender.setInviteUserAttribute(0);
							}
							// 更新其他的属性
							borrowTender.setInviteRegionId(0);
							borrowTender.setInviteRegionName("");
							borrowTender.setInviteBranchId(0);
							borrowTender.setInviteBranchName("");
							borrowTender.setInviteDepartmentId(0);
							borrowTender.setInviteDepartmentName("");
						} else {
							int refUserId = getUserRefererId(userId, 0, borrowTender.getAddtime(), repairStartDate,
									repairEndDate);
							// 查找推荐人
							Users userss = getUsers(refUserId);
							if (userss != null) {
								borrowTender.setInviteUserId(userss.getUserId());
								borrowTender.setInviteUserName(userss.getUsername());
							} else {
								borrowTender.setInviteUserId(0);
								borrowTender.setInviteUserName("");
							}
							// 推荐人信息
							UsersInfo refUsers = getUserInfo(refUserId);
							// 推荐人用户属性
							if (refUsers != null) {
								int refererAttribute = refUsers.getAttribute();
								refererAttribute = getUserAttribute(refUserId, refererAttribute,
										borrowTender.getAddtime(), repairStartDate, repairEndDate);
								borrowTender.setInviteUserAttribute(refererAttribute);
							} else {
								borrowTender.setInviteUserAttribute(0);
							}
							// 更新其他的属性
							borrowTender.setInviteRegionId(0);
							borrowTender.setInviteRegionName("");
							borrowTender.setInviteBranchId(0);
							borrowTender.setInviteBranchName("");
							borrowTender.setInviteDepartmentId(0);
							borrowTender.setInviteDepartmentName("");
						}
					}
				}
			}
			this.borrowTenderMapper.updateByPrimaryKey(borrowTender);
		}
	}

	/**
	 * 获取用户的历史推荐人
	 * 
	 * @param borrowTender
	 * @param spreadUserId
	 * @param repairStartDate
	 * @param repairEndDate
	 * @return
	 */
	public int getUserRefererId(int userId, int spreadUserId, int addTime, String repairStartDate,
			String repairEndDate) {
		// 获取用户的推荐人修改记录
		List<SpreadsUsersLog> spreadsUsersLogList = this.searchSpreadUsersLog(userId, repairStartDate, repairEndDate);
		if (spreadsUsersLogList != null && spreadsUsersLogList.size() > 0) {
			for (int i = 0; i < spreadsUsersLogList.size(); i++) {
				// 下一个推荐人修改记录
				SpreadsUsersLog spreadUsersLog = spreadsUsersLogList.get(i);
				// 获取用户的推荐人的首次修改时间
				int modTimeStart = Integer.parseInt(spreadUsersLog.getAddtime());
				// 用户出借的时间之前用户的推荐人被修改过
				if (addTime <= modTimeStart) {
					// log数据错误，如果只有一条数据，则为0
					/*
					 * if (i == 0) { spreadUserId = 0; } else { // 如果是多于两条
					 * spreadUserId = spreadsUsersLogList.get(i -
					 * 1).getSpreadsUserid() == null ? 0 :
					 * spreadsUsersLogList.get(i - 1).getSpreadsUserid(); }
					 */
					spreadUserId = spreadUsersLog.getOldSpreadsUserid() == null ? 0
							: spreadUsersLog.getOldSpreadsUserid();
					break;
				} else {
					if ((i + 1) == spreadsUsersLogList.size()) {
						spreadUserId = spreadUsersLog.getSpreadsUserid() == null ? 0
								: spreadUsersLog.getSpreadsUserid();
					}
				}
			}
		}
		return spreadUserId;
	}

	/**
	 * 获取用户的历史属性
	 * 
	 * @param borrowTender
	 * @param attribute
	 * @param repairStartDate
	 * @param repairEndDate
	 * @return
	 */
	private int getUserAttribute(int userId, int attribute, int addTime, String repairStartDate, String repairEndDate) {

		SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
		SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
		spreadsUsersExampleCriteria.andUserIdEqualTo(userId);
		List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
		// 用户现在有推荐人
		if (sList != null && sList.size() == 1) {
			// 获取现在的推荐人id
			int refererId = sList.get(0).getSpreadsUserid();
			// 获取用户的历史推荐人
			refererId = this.getUserRefererId(userId, refererId, addTime, repairStartDate, repairEndDate);
			if (refererId == 0) {
				// 假设用户是员工，查询用户的离职记录
				List<AdminEmployeeLeaveCustomize> userLeaveList = this.selectUserLeaveList(userId);
				if (userLeaveList != null && userLeaveList.size() == 1) {
					AdminEmployeeLeaveCustomize userLeaveTemp = userLeaveList.get(0);
					// 用户的离职时间
					String entryDateStr = userLeaveTemp.getEntryDate();
					// 入职时间
					int entryTime = GetDate.strYYYYMMDD2Timestamp2(entryDateStr);
					// 用户出借的时间之后用户入职
					if (addTime <= entryTime) {
						attribute = 0;
					} else {
						// 用户的推荐人在此段时间内没有离职记录
						if (StringUtils.isNotBlank(userLeaveTemp.getEndTime())) {
							// 用户的离职时间
							String leaveEndTimeStartStr = userLeaveTemp.getEndTime();
							int leaveEndTimeStart = GetDate.strYYYYMMDD2Timestamp2(leaveEndTimeStartStr);
							// 用户出借的时间之后用户发生过离职
							if (addTime <= leaveEndTimeStart) {
								int staffType = Integer.parseInt(userLeaveTemp.getStaffType());
								if (staffType == 1) {
									// 线上
									attribute = 3;
								} else if (staffType == 2) {
									// 线下
									attribute = 2;
								}
							} else {
								// 用户出借的时间之前用户发生过离职
								if (addTime > leaveEndTimeStart) {
									attribute = 0;
								}
							}
						} else {
							int staffType = Integer.parseInt(userLeaveTemp.getStaffType());
							if (staffType == 1) {
								// 线上
								attribute = 3;
							} else if (staffType == 2) {
								// 线下
								attribute = 2;
							}
						}
					}
				} else {
					attribute = 0;
				}
			} else {
				// 查询用户的离职记录
				List<AdminEmployeeLeaveCustomize> userRefererLeaveList = this.selectUserLeaveList(refererId);
				if (userRefererLeaveList != null && userRefererLeaveList.size() == 1) {
					AdminEmployeeLeaveCustomize userRefererLeaveTemp = userRefererLeaveList.get(0);
					// 用户的离职时间
					String entryDateStr = userRefererLeaveTemp.getEntryDate();
					// 入职时间
					int entryTime = GetDate.strYYYYMMDD2Timestamp2(entryDateStr);
					// 用户出借的时间之后用户入职
					if (addTime <= entryTime) {
						attribute = 0;
					} else {
						// 用户的推荐人在此段时间内没有离职记录
						if (StringUtils.isNotBlank(userRefererLeaveTemp.getEndTime())) {
							// 用户的推荐人的离职时间
							String leaveEndTimeStartStr = userRefererLeaveTemp.getEndTime();
							int leaveEndTimeStart = GetDate.strYYYYMMDD2Timestamp2(leaveEndTimeStartStr);
							// 用户出借的时间之后用户的推荐人发生过离职
							if (addTime <= leaveEndTimeStart) {
								// 用户为有主单
								attribute = 1;
							} else {
								// 用户出借的时间之前用户的推荐人发生过离职
								if (addTime > leaveEndTimeStart) {
									attribute = 0;
								}
							}
						} else {
							// 用户未有离职
							attribute = 1;
						}
					}
				} else {
					// 用户的推荐人不是员工
					attribute = 0;
				}
			}
		} else {
			// 获取用户的推荐人
			int refererId = this.getUserRefererId(userId, 0, addTime, repairStartDate, repairEndDate);
			if (refererId == 0) {
				// 假设用户是员工，查询用户的离职记录
				List<AdminEmployeeLeaveCustomize> userLeaveList = this.selectUserLeaveList(userId);
				if (userLeaveList != null && userLeaveList.size() == 1) {
					AdminEmployeeLeaveCustomize userLeaveTemp = userLeaveList.get(0);
					// 用户的离职时间
					String entryDateStr = userLeaveTemp.getEntryDate();
					// 入职时间
					int entryTime = GetDate.strYYYYMMDD2Timestamp2(entryDateStr);
					// 用户出借的时间之后用户入职
					if (addTime <= entryTime) {
						attribute = 0;
					} else {
						// 用户的推荐人在此段时间内没有离职记录
						if (StringUtils.isNotBlank(userLeaveTemp.getEndTime())) {
							// 用户的离职时间
							String leaveEndTimeStartStr = userLeaveTemp.getEndTime();
							int leaveEndTimeStart = GetDate.strYYYYMMDD2Timestamp2(leaveEndTimeStartStr);
							// 用户出借的时间之后用户发生过离职
							if (addTime <= leaveEndTimeStart) {
								int staffType = Integer.parseInt(userLeaveTemp.getStaffType());
								if (staffType == 1) {
									// 线上
									attribute = 3;
								} else if (staffType == 2) {
									// 线下
									attribute = 2;
								}
							} else {
								// 用户出借的时间之前用户发生过离职
								if (addTime > leaveEndTimeStart) {
									attribute = 0;
								}
							}
						} else {
							int staffType = Integer.parseInt(userLeaveTemp.getStaffType());
							if (staffType == 1) {
								// 线上
								attribute = 3;
							} else if (staffType == 2) {
								// 线下
								attribute = 2;
							}
						}
					}
				}
			} else {
				// 查询用户的离职记录
				List<AdminEmployeeLeaveCustomize> userRefererLeaveList = this.selectUserLeaveList(refererId);
				if (userRefererLeaveList != null && userRefererLeaveList.size() == 1) {
					AdminEmployeeLeaveCustomize userRefererLeaveTemp = userRefererLeaveList.get(0);
					// 用户的离职时间
					String entryDateStr = userRefererLeaveTemp.getEntryDate();
					// 入职时间
					int entryTime = GetDate.strYYYYMMDD2Timestamp2(entryDateStr);
					// 用户出借的时间之后用户入职
					if (addTime <= entryTime) {
						attribute = 0;
					} else {
						// 用户的推荐人在此段时间内没有离职记录
						if (StringUtils.isNotBlank(userRefererLeaveTemp.getEndTime())) {
							// 用户的推荐人的离职时间
							String leaveEndTimeStartStr = userRefererLeaveTemp.getEndTime();
							int leaveEndTimeStart = GetDate.strYYYYMMDD2Timestamp2(leaveEndTimeStartStr);
							// 用户出借的时间之后用户的推荐人发生过离职
							if (addTime <= leaveEndTimeStart) {
								// 用户为有主单
								attribute = 1;
							} else {
								// 用户出借的时间之前用户的推荐人发生过离职
								if (addTime > leaveEndTimeStart) {
									attribute = 0;
								}
							}
						} else {
							// 用户的推荐人未有离职
							attribute = 1;
						}
					}
				} else {
					// 用户的推荐人不是员工
					attribute = 0;
				}
			}
		}
		return attribute;
	}

	private List<AdminEmployeeLeaveCustomize> selectUserLeaveList(int userId) {
		// 封装查询条件
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		List<AdminEmployeeLeaveCustomize> userLeaveList = employeeLeaveCustomizeMapper.selectUserLeaveByUserId(params);
		return userLeaveList;
	}

	/**
	 * 查询此段时间的用户推荐人的修改记录
	 * 
	 * @param userId
	 * @param repairStartDate
	 * @param repairEndDate
	 * @return
	 */
	private List<SpreadsUsersLog> searchSpreadUsersLog(int userId, String repairStartDate, String repairEndDate) {
		SpreadsUsersLogExample example = new SpreadsUsersLogExample();
		SpreadsUsersLogExample.Criteria crt = example.createCriteria();
		crt.andUserIdEqualTo(userId);
		crt.andAddtimeGreaterThanOrEqualTo(GetDate.strYYYYMMDD2Timestamp2(repairStartDate) + "");
		crt.andAddtimeLessThanOrEqualTo(GetDate.strYYYYMMDD2Timestamp2(repairEndDate) + "");
		example.setOrderByClause("addtime ASC");
		List<SpreadsUsersLog> SpreadsUsersLogList = this.spreadsUsersLogMapper.selectByExample(example);
		return SpreadsUsersLogList;
	}

	/**
	 * 根据userId查询用户表
	 *
	 * @param userId
	 * @return
	 * @author b
	 */
	private Users getUsers(Integer userId) {
		if (userId == null) {
			return null;
		}
		// 查找用户
		UsersExample usersExample = new UsersExample();
		UsersExample.Criteria criteria2 = usersExample.createCriteria();
		criteria2.andUserIdEqualTo(userId);
		List<Users> userList = usersMapper.selectByExample(usersExample);
		Users users = null;
		if (userList != null && !userList.isEmpty()) {
			users = userList.get(0);

		}
		return users;

	}

	/**
	 * 根据userId查询用户详情信息
	 *
	 * @param userId
	 * @return
	 * @author b
	 */
	private UsersInfo getUserInfo(Integer userId) {
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
}
