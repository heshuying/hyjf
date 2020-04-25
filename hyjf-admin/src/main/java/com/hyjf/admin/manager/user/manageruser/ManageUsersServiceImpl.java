/**
 * Description:用户信息管理业务处理类接口实现
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年11月11日 上午11:05:56
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.admin.manager.user.manageruser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hyjf.bank.service.mq.MqService;
import com.hyjf.common.util.*;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import com.hyjf.mybatis.model.customize.UserEvalationResultExampleCustomize;
import com.hyjf.mybatis.model.customize.admin.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.manager.user.changelog.ChangeLogDefine;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.customize.AdminSystem;
import com.hyjf.mybatis.model.customize.UserUpdateParamCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

/**
 * @author 王坤
 */
@Service("usersService")
public class ManageUsersServiceImpl extends BaseServiceImpl implements ManageUsersService {
	@Autowired
	private MqService mqService;

	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;

	Logger _log = LoggerFactory.getLogger(ManageUsersServiceImpl.class);

	/**
	 * 获取权限列表
	 * 
	 * @return
	 */
	public List<AdminUserListCustomize> getRecordList(Map<String, Object> user, int limitStart, int limitEnd) {
		// add by liubin Sql性能优化 start
		// 设定有无limit外的检索条件Flag
		user.put("whereFlag", getWhereFlag(user));
		// add by liubin Sql性能优化 end

		if (limitStart == 0 || limitStart > 0) {
			user.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			user.put("limitEnd", limitEnd);
		}
		// 查询用户列表
		List<AdminUserListCustomize> users = adminUsersCustomizeMapper.selectUserList(user);
		return users;
	}

	/**
	 * 当分账类型选择按出借人分账时，需选择出借人分公司，从会员中心-会员管理-分公司字段读取所有分公司
	 * @return
	 * @author wgx
	 */
	@Override
	public List<Map<String,String>> selectRegionNameList() {
		List<Map<String,String>> regionNameList = adminUsersCustomizeMapper.selectRegionNameList();
		return regionNameList;
	}

	/**
	 * 更新用户信息
	 * 
	 * @param userUpdate
	 * @return
	 * @author 王坤
	 * @version: 1.0 Created at: 2015年11月11日 下午2:13:46 Modification History:
	 *           Modified by :
	 */

	@Override
	public int updateUser(AdminUserUpdateCustomize userUpdate) {
		// 初始化用户操作日志信息
		UsersChangeLog changeLog = new UsersChangeLog();
		List<UserInfoForLogCustomize> users = usersCustomizeMapper
				.selectUserByUserId(Integer.parseInt(userUpdate.getUserId()));
		UserInfoForLogCustomize logRecord = users.get(0);
		changeLog.setUserId(logRecord.getUserId());
		changeLog.setUsername(logRecord.getUserName());
		changeLog.setAttribute(logRecord.getAttribute());
		changeLog.setIs51(logRecord.getIs51());
		changeLog.setRealName(logRecord.getRealName());
		changeLog.setRecommendUser(logRecord.getRecommendName());
		changeLog.setChangeType(ChangeLogDefine.CHANGELOG_TYPE_USERINFO);
		changeLog.setMobile(logRecord.getMobile());
		changeLog.setRole(logRecord.getUserRole());
		changeLog.setStatus(logRecord.getUserStatus());
		changeLog.setIdcard(logRecord.getIdCard());

		UsersChangeLogExample logExample = new UsersChangeLogExample();
		UsersChangeLogExample.Criteria logCriteria = logExample.createCriteria();
		logCriteria.andUserIdEqualTo(Integer.parseInt(userUpdate.getUserId()));
		int count = usersChangeLogMapper.countByExample(logExample);
		if (count <= 0) {
			// 如果从来没有添加过操作日志，则将原始信息插入修改日志中
			if (users != null && !users.isEmpty()) {
				changeLog.setRemark("初始注册");
				changeLog.setChangeUser("system");
				changeLog.setChangeTime(logRecord.getRegTime());
				usersChangeLogMapper.insertSelective(changeLog);
			}
		}

		// 根据主键查询用户信息
		Users user = usersMapper.selectByPrimaryKey(Integer.parseInt(userUpdate.getUserId()));
		// 更新响应的用户的信息
		user.setStatus(Integer.parseInt(userUpdate.getStatus()));
		user.setMobile(userUpdate.getMobile());
		int usersUpdateFlag = usersMapper.updateByPrimaryKey(user);
		// 查询用户详情
		UsersInfoExample usersInfoE = new UsersInfoExample();
		UsersInfoExample.Criteria uipec = usersInfoE.createCriteria();
		uipec.andUserIdEqualTo(user.getUserId());
		List<UsersInfo> usersInfoList = usersInfoMapper.selectByExample(usersInfoE);
		// 更新用户详情信息
		if (usersInfoList != null && usersInfoList.size() == 1) {
			UsersInfo userInfo = usersInfoList.get(0);
			userInfo.setRoleId(Integer.parseInt(userUpdate.getUserRole()));
			userInfo.setBorrowerType(userUpdate.getBorrowerType());
			// 更新用户详情信息
			usersInfoMapper.updateByPrimaryKey(userInfo);

			// 保存用户信息修改日志
			AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
			// 插入一条用户信息修改日志
			changeLog.setMobile(userUpdate.getMobile());
			changeLog.setStatus(Integer.parseInt(userUpdate.getStatus()));
			changeLog.setRole(Integer.parseInt(userUpdate.getUserRole()));
			changeLog.setChangeUser(adminSystem.getUsername());
			changeLog.setChangeUserid(Integer.parseInt(adminSystem.getId()));
			changeLog.setRemark(userUpdate.getRemark());
			changeLog.setChangeTime(GetDate.getNowTime10());
			changeLog.setBorrowerType(userUpdate.getBorrowerType());
			usersChangeLogMapper.insertSelective(changeLog);

		}


		// 修改手机号
		return usersUpdateFlag;
	}

	/**
	 * 根据用户id查询用户详情
	 * 
	 * @param userId
	 * @return
	 * @author 王坤
	 */

	@Override
	public AdminUserDetailCustomize searchUserDetailById(int userId) {
		List<AdminUserDetailCustomize> userDetails = this.adminUsersCustomizeMapper.selectUserDetailById(userId);
		if (userDetails != null && userDetails.size() > 0) {
			return userDetails.get(0);
		}
		return new AdminUserDetailCustomize();
	}

	/**
	 * 通过用户ID 关联用户所在的渠道
	 * @param userId
	 * @return
	 */
	@Override
	public AdminUserDetailCustomize selectUserUtmInfo(int userId) {
		return adminUsersCustomizeMapper.selectUserUtmInfo(userId);
	}

	/**
	 * 查询用户更新信息
	 * 
	 * @param userId
	 * @return
	 * @author 王坤
	 */

	@Override
	public AdminUserUpdateCustomize searchUserUpdateById(int userId) {

		List<AdminUserUpdateCustomize> userDetails = adminUsersCustomizeMapper.selectUserUpdateById(userId);
		if (userDetails != null && userDetails.size() > 0) {
			return userDetails.get(0);
		}
		return new AdminUserUpdateCustomize();

	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * 
	 * @param user
	 * @return
	 * @author Administrator
	 */

	@Override
	public int countRecordTotal(Map<String, Object> user) {
		// add by liubin Sql性能优化 start
		// 设定有无limit外的检索条件Flag
		user.put("whereFlag", getWhereFlag(user));
		// add by liubin Sql性能优化 end

		// 查询用户列表
		int count = adminUsersCustomizeMapper.countRecordTotal(user);
		return count;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * 
	 * @param userId
	 * @return
	 * @author Administrator
	 */

	@Override
	public AdminUserRecommendCustomize searchUserRecommend(int userId) {
		List<AdminUserRecommendCustomize> userRecommends = adminUsersCustomizeMapper.searchUserRecommend(userId);
		if (userRecommends != null && userRecommends.size() > 0) {
			return userRecommends.get(0);
		}
		return new AdminUserRecommendCustomize();

	}

	/**
	 * 更新userInfo的主单与非主单信息
	 * 
	 * @param userId
	 *            用户ID
	 * @author 孙亮
	 * @since 2015年12月31日 上午9:15:34
	 */
	@Override
	public void updateUserParam(Integer userId) {

		Users users = usersMapper.selectByPrimaryKey(userId);
		if (users != null) {
			UsersInfoExample uie = new UsersInfoExample();
			UsersInfoExample.Criteria uipec = uie.createCriteria();
			uipec.andUserIdEqualTo(userId);
			List<UsersInfo> usersInfoList = usersInfoMapper.selectByExample(uie);
			if (usersInfoList != null && usersInfoList.size() == 1) {
				// 如果userinfo不为空
				UsersInfo userInfo = usersInfoList.get(0);
				userInfo.setAttribute(0);// 默认为无主单
				{
					// 从oa表中查询线上线下部门属性
					List<UserUpdateParamCustomize> userUpdateParamList = ontimeUserLeaveCustomizeMapper
							.queryUserAndDepartment(userInfo.getUserId());
					if (userUpdateParamList != null && userUpdateParamList.size() > 0) {
						if (userUpdateParamList.get(0).getCuttype() != null) {
							if (userUpdateParamList.get(0).getCuttype().equals("1")) {
								// 线上
								userInfo.setAttribute(3);
							}
							if (userUpdateParamList.get(0).getCuttype().equals("2")) {
								// 线下
								userInfo.setAttribute(2);
							}
						}
					}
				}
				{
					// 更新attribute
					if (userInfo.getAttribute() != 2 && userInfo.getAttribute() != 3) {
						if (Validator.isNotNull(users.getReferrer())) {
							UsersInfoExample puie = new UsersInfoExample();
							UsersInfoExample.Criteria puipec = puie.createCriteria();
							puipec.andUserIdEqualTo(users.getReferrer());
							List<UsersInfo> pUsersInfoList = usersInfoMapper.selectByExample(puie);
							if (pUsersInfoList != null && pUsersInfoList.size() == 1) {
								// 如果该用户的上级不为空
								UsersInfo parentInfo = pUsersInfoList.get(0);
								if (Validator.isNotNull(parentInfo) && Validator.isNotNull(parentInfo.getAttribute())) {
									if (Validator.equals(parentInfo.getAttribute(), new Integer(2))
											|| Validator.equals(parentInfo.getAttribute(), new Integer(3))) {
										// 有推荐人且推荐人为员工(Attribute=2或3)时才设置为有主单
										userInfo.setAttribute(1);
									}
								}
							}
						}
					}
				}
				usersInfoMapper.updateByPrimaryKey(userInfo);
			}
		}
	}

	/**
	 * 更新全部用户属性
	 * 
	 * @author 孙亮
	 * @since 2016年1月13日 上午9:14:16
	 */
	@Override
	public void updateAllUserParam() {
		List<Users> userList = usersMapper.selectByExample(new UsersExample());
		for (int i = 0; i < userList.size(); i++) {
			// System.out.println("正在处理第" + (i + 1) + "个用户,总数:" +
			// userList.size());
			updateUserParam(userList.get(i).getUserId());
		}
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * 
	 * @param form
	 * @author Administrator
	 */
	@Override
	public void updateUserRe(AdminUserRecommendCustomize form) {
		// 根据推荐人用户名查询用户
		UsersExample ue = new UsersExample();
		UsersExample.Criteria criteria = ue.createCriteria();
		criteria.andUsernameEqualTo(form.getRecommendName());
		List<Users> userRecommends = usersMapper.selectByExample(ue);
		String oldRecommendUser = "";
		AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
		int time = GetDate.getNowTime10();
		if (userRecommends != null && userRecommends.size() > 0) {
			// 获取新推荐人
			Users userRecommendNew = userRecommends.get(0);
			// 根据主键查询用户信息
			Users user = usersMapper.selectByPrimaryKey(Integer.parseInt(form.getUserId()));
			oldRecommendUser = user.getReferrerUserName();
			// 更新推荐人
			user.setReferrer(userRecommendNew.getUserId());
			user.setReferrerUserName(userRecommendNew.getUsername());
			usersMapper.updateByPrimaryKey(user);
			// 更新userInfo的主单与非主单信息 2015年12月30日18:27:22 孙亮
			updateUserParam(user.getUserId());

			SpreadsUsersExample example = new SpreadsUsersExample();
			example.createCriteria().andUserIdEqualTo(Integer.parseInt(form.getUserId()));

			List<SpreadsUsers> spreadUsers = spreadsUsersMapper.selectByExample(example);
			Integer oldSpreadUserId = null;
			if (spreadUsers != null && spreadUsers.size() > 0) {
				SpreadsUsers spreadUser = spreadUsers.get(0);
				oldSpreadUserId = spreadUser.getSpreadsUserid();
				spreadUser.setSpreadsUserid(userRecommendNew.getUserId());
				spreadUser.setOperation(adminSystem.getUsername());
				// 保存用户推荐人信息
				spreadsUsersMapper.updateByPrimaryKeyWithBLOBs(spreadUser);

				SpreadsUsersLog spreadsUsersLog = new SpreadsUsersLog();
				spreadsUsersLog.setOldSpreadsUserid(oldSpreadUserId);
				spreadsUsersLog.setUserId(Integer.parseInt(form.getUserId()));
				spreadsUsersLog.setSpreadsUserid(userRecommendNew.getUserId());
				spreadsUsersLog.setType("web");
				spreadsUsersLog.setOpernote(form.getRemark());
				spreadsUsersLog.setOperation(adminSystem.getUsername());
				spreadsUsersLog.setAddtime(String.valueOf(time));
				spreadsUsersLog.setAddip(form.getIp());
				// 保存相应的更新日志信息
				spreadsUsersLogMapper.insertSelective(spreadsUsersLog);
			} else {
				SpreadsUsers spreadUser = new SpreadsUsers();
				spreadUser.setUserId(Integer.parseInt(form.getUserId()));
				spreadUser.setSpreadsUserid(userRecommendNew.getUserId());
				spreadUser.setAddip(form.getIp());
				spreadUser.setAddtime(String.valueOf(time));
				spreadUser.setType("web");
				spreadUser.setOpernote("web");
				spreadUser.setOperation(adminSystem.getUsername());
				// 插入推荐人
				spreadsUsersMapper.insertSelective(spreadUser);

				SpreadsUsersLog spreadsUsersLog = new SpreadsUsersLog();
				spreadsUsersLog.setOldSpreadsUserid(null);
				spreadsUsersLog.setUserId(Integer.parseInt(form.getUserId()));
				spreadsUsersLog.setSpreadsUserid(userRecommendNew.getUserId());
				spreadsUsersLog.setType("web");
				spreadsUsersLog.setOpernote(form.getRemark());
				spreadsUsersLog.setOperation(adminSystem.getUsername());
				spreadsUsersLog.setAddtime(String.valueOf(time));
				spreadsUsersLog.setAddip(form.getIp());
				// 保存相应的更新日志信息
				spreadsUsersLogMapper.insertSelective(spreadsUsersLog);
			}

		} else {
			// 根据主键查询用户信息
			Users user = usersMapper.selectByPrimaryKey(Integer.parseInt(form.getUserId()));
			// 更新推荐人
			user.setReferrer(null);
			user.setReferrerUserName(null);
			usersMapper.updateByPrimaryKey(user);
			// 更新userInfo的主单与非主单信息 2015年12月30日18:27:22 孙亮
			updateUserParam(user.getUserId());
			SpreadsUsersExample example = new SpreadsUsersExample();
			example.createCriteria().andUserIdEqualTo(Integer.parseInt(form.getUserId()));
			List<SpreadsUsers> spreadUsers = spreadsUsersMapper.selectByExample(example);
			if (spreadUsers != null && spreadUsers.size() > 0) {
				SpreadsUsers spreadUser = spreadUsers.get(0);
				// 删除用户的推荐人
				spreadsUsersMapper.deleteByPrimaryKey(spreadUser.getId());

				SpreadsUsersLog spreadsUsersLog = new SpreadsUsersLog();
				spreadsUsersLog.setOldSpreadsUserid(spreadUser.getSpreadsUserid());
				spreadsUsersLog.setUserId(Integer.parseInt(form.getUserId()));
				spreadsUsersLog.setSpreadsUserid(null);
				spreadsUsersLog.setType("web");
				spreadsUsersLog.setOpernote(form.getRemark());
				spreadsUsersLog.setOperation(adminSystem.getUsername());
				spreadsUsersLog.setAddtime(String.valueOf(time));
				spreadsUsersLog.setAddip(form.getIp());
				// 保存相应的更新日志信息
				spreadsUsersLogMapper.insertSelective(spreadsUsersLog);
			}
		}

		// 保存用户信息修改日志
		List<UserInfoForLogCustomize> users = usersCustomizeMapper
				.selectUserByUserId(Integer.parseInt(form.getUserId()));
		if (users != null && !users.isEmpty()) {
			UserInfoForLogCustomize customize = users.get(0);
			UsersChangeLog changeLog = new UsersChangeLog();
			changeLog.setUserId(customize.getUserId());
			changeLog.setUsername(customize.getUserName());
			changeLog.setAttribute(customize.getAttribute());
			changeLog.setIs51(customize.getIs51());
			changeLog.setRole(customize.getUserRole());
			changeLog.setMobile(customize.getMobile());
			changeLog.setRealName(customize.getRealName());
			changeLog.setRecommendUser(oldRecommendUser);
			changeLog.setStatus(customize.getUserStatus());
			changeLog.setChangeType(ChangeLogDefine.CHANGELOG_TYPE_RECOMMEND);
			changeLog.setIdcard(customize.getIdCard());

			UsersChangeLogExample logExample = new UsersChangeLogExample();
			UsersChangeLogExample.Criteria logCriteria = logExample.createCriteria();
			logCriteria.andUserIdEqualTo(Integer.parseInt(form.getUserId()));
			int count = usersChangeLogMapper.countByExample(logExample);
			if (count <= 0) {
				// 如果从来没有添加过操作日志，则将原始信息插入修改日志中
				changeLog.setRemark("初始注册");
				changeLog.setChangeUser("system");
				changeLog.setChangeTime(customize.getRegTime());
				usersChangeLogMapper.insertSelective(changeLog);
			}

			// 插入一条用户信息修改日志
			changeLog.setChangeUser(adminSystem.getUsername());
			changeLog.setChangeUserid(Integer.parseInt(adminSystem.getId()));
			changeLog.setRecommendUser(form.getRecommendName());
			changeLog.setRemark(form.getRemark());
			changeLog.setChangeTime(GetDate.getNowTime10());
			usersChangeLogMapper.insertSelective(changeLog);
		}

	}

	@Override
	public int countUserByMobile(int userId, String mobile) {
		UsersExample example = new UsersExample();
		UsersExample.Criteria criteria = example.createCriteria();
		if (Validator.isNotNull(userId)) {
			criteria.andUserIdNotEqualTo(userId);
		}
		criteria.andMobileEqualTo(mobile);
		int cnt = usersMapper.countByExample(example);
		return cnt;
	}

	/**
	 * 获取指定时间内修改的推荐人信息
	 * 
	 * @param userRecommendCustomize
	 * @return
	 */
	public void querySpreadsUsersLog(AdminUserRecommendCustomize userRecommendCustomize) {

		String startTime = userRecommendCustomize.getStartTime();
		String endTime = userRecommendCustomize.getEndTime();

		List<AdminUserRecommendCustomize> userRecommendCustomizeList = this.adminUsersCustomizeMapper
				.querySpreadsUsersLog(userRecommendCustomize);

		if (userRecommendCustomizeList != null && userRecommendCustomizeList.size() != 0) {
			for (AdminUserRecommendCustomize userRecord : userRecommendCustomizeList) {
				// 获取用户信息
				SpreadsUsersLogExample example = new SpreadsUsersLogExample();
				SpreadsUsersLogExample.Criteria craSUser = example.createCriteria();
				craSUser.andUserIdEqualTo(Integer.valueOf(userRecord.getUserId()));
				craSUser.andAddtimeGreaterThanOrEqualTo(userRecommendCustomize.getStartTime());
				craSUser.andAddtimeLessThanOrEqualTo(userRecommendCustomize.getEndTime());
				example.setOrderByClause(" addtime ASC ");

				List<SpreadsUsersLog> spreadsUsersLogList = this.spreadsUsersLogMapper.selectByExample(example);
				if (spreadsUsersLogList != null && spreadsUsersLogList.size() > 0) {
					for (int i = 0; i < spreadsUsersLogList.size(); i++) {
						SpreadsUsersLog spreadsUsersLog = spreadsUsersLogList.get(i);
						// 修改推荐人的时间
						Integer addTime = Integer.valueOf(spreadsUsersLog.getAddtime());
						Integer userId = spreadsUsersLog.getUserId();

						if (i == 0) {

							// 更新开始时间
							BorrowTenderExample tenderExample = new BorrowTenderExample();
							BorrowTenderExample.Criteria cra = tenderExample.createCriteria();
							cra.andUserIdEqualTo(userId);
							cra.andAddtimeGreaterThanOrEqualTo(Integer.valueOf(startTime));
							cra.andAddtimeLessThanOrEqualTo(addTime);

							BorrowTender record = new BorrowTender();
							record.setUserId(userId);
							record.setInviteUserId(spreadsUsersLog.getOldSpreadsUserid());

							Users user = this.usersMapper.selectByPrimaryKey(record.getInviteUserId());
							if (user != null) {
								record.setInviteUserName(user.getUsername());
								this.borrowTenderMapper.updateByExampleSelective(record, tenderExample);
							}
						} else {
							// 更新开始时间
							BorrowTenderExample tenderExample = new BorrowTenderExample();
							BorrowTenderExample.Criteria cra = tenderExample.createCriteria();
							cra.andUserIdEqualTo(userId);
							cra.andAddtimeGreaterThanOrEqualTo(
									Integer.valueOf(spreadsUsersLogList.get(i - 1).getAddtime()));
							cra.andAddtimeLessThanOrEqualTo(addTime);

							BorrowTender record = new BorrowTender();
							record.setUserId(userId);
							record.setInviteUserId(spreadsUsersLog.getOldSpreadsUserid());

							Users user = this.usersMapper.selectByPrimaryKey(record.getInviteUserId());
							if (user != null) {
								record.setInviteUserName(user.getUsername());
								this.borrowTenderMapper.updateByExampleSelective(record, tenderExample);
							}
						}
						if (i + 1 == spreadsUsersLogList.size()) {

							// 更新开始时间
							BorrowTenderExample tenderExample = new BorrowTenderExample();
							BorrowTenderExample.Criteria cra = tenderExample.createCriteria();
							cra.andUserIdEqualTo(userId);
							cra.andAddtimeGreaterThanOrEqualTo(Integer.valueOf(spreadsUsersLog.getAddtime()));
							cra.andAddtimeLessThanOrEqualTo(Integer.valueOf(endTime));

							BorrowTender record = new BorrowTender();
							record.setUserId(userId);
							record.setInviteUserId(spreadsUsersLog.getSpreadsUserid());

							Users user = this.usersMapper.selectByPrimaryKey(record.getInviteUserId());
							if (user != null) {
								record.setInviteUserName(user.getUsername());
								this.borrowTenderMapper.updateByExampleSelective(record, tenderExample);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 校验推荐人
	 */
	@Override
	public int checkRecommend(String userId, String recommendName) {

		UsersExample ue = new UsersExample();
		UsersExample.Criteria criteria = ue.createCriteria();
		criteria.andUsernameEqualTo(recommendName);
		List<Users> userRecommends = usersMapper.selectByExample(ue);
		if (userRecommends != null && userRecommends.size() == 1) {
			Users user = userRecommends.get(0);
			if (user.getUserId() == Integer.parseInt(userId)) {
				return 2;
			} else {
				return 0;
			}
		} else {
			return 1;
		}

	}

	/**
	 * 根据用户名获取用户
	 */
	@Override
	public Users getUsersByUserName(String userName) {
		if (StringUtils.isNotEmpty(userName)) {
			UsersExample example = new UsersExample();
			example.createCriteria().andUsernameEqualTo(userName);
			List<Users> usersList = this.usersMapper.selectByExample(example);
			if (usersList != null && usersList.size() > 0) {
				return usersList.get(0);
			}
		}
		return null;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * 
	 * @param vipId
	 * @return
	 * @author Michael
	 */

	@Override
	public VipInfo getVipInfoById(int vipId) {
		return this.vipInfoMapper.selectByPrimaryKey(vipId);
	}

	@Override
	public int countUserById(String userId) {
		int ret = 0;
		if (StringUtils.isNotEmpty(userId)) {
			UsersExample example = new UsersExample();
			UsersExample.Criteria cra = example.createCriteria();
			cra.andUserIdEqualTo(Integer.parseInt(userId));
			ret = this.usersMapper.countByExample(example);
		}
		return ret;
	}

	@Override
	public int updateSpreadsUsers(String userId, String spreadsUserId, String operation, String ip) {
		int ret = 0;
		int currentTime = GetDate.getNowTime10();
		// 旧推荐人名称
		String oldRecommendUser = "";
		// 新推荐人用户名称
		String newRecommendUser = "";
		// 更新推荐人
		Users users = this.usersMapper.selectByPrimaryKey(Integer.parseInt(userId));
		if (users != null) {
			// 旧推荐人名称
			oldRecommendUser = users.getReferrerUserName();
			// 推荐人id
			users.setReferrer(Integer.parseInt(spreadsUserId));
			// 推荐人用户名获取
			Users spreadsUser = this.usersMapper.selectByPrimaryKey(Integer.parseInt(spreadsUserId));
			// 新推荐人用户名
			newRecommendUser = spreadsUser.getUsername();
			// 推荐人用户名
			users.setReferrerUserName(newRecommendUser);
			// 更新Users表
			ret += usersMapper.updateByPrimaryKeySelective(users);
		}

		// 更新userInfo的主单与非主单信息
		updateUserParam(Integer.parseInt(userId));
		// 更新推荐人关系表
		SpreadsUsersExample example = new SpreadsUsersExample();
		SpreadsUsersExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(Integer.parseInt(userId));
		// 根据用户id检索推荐人关系表
		List<SpreadsUsers> spreadUsers = spreadsUsersMapper.selectByExample(example);
		// 旧推荐人ID
		Integer oldSpreadUserId = null;

		if (spreadUsers != null && spreadUsers.size() > 0) {
			// 更新推荐人关系表
			SpreadsUsers spreadUser = spreadUsers.get(0);
			oldSpreadUserId = spreadUser.getSpreadsUserid();
			// 设置新的推荐人id
			spreadUser.setSpreadsUserid(Integer.parseInt(spreadsUserId));
			// 操作者
			spreadUser.setOperation(operation);
			// 保存推荐人信息
			ret += spreadsUsersMapper.updateByPrimaryKeyWithBLOBs(spreadUser);

			SpreadsUsersLog spreadsUsersLog = new SpreadsUsersLog();
			spreadsUsersLog.setOldSpreadsUserid(oldSpreadUserId);
			spreadsUsersLog.setUserId(Integer.parseInt(userId));
			spreadsUsersLog.setSpreadsUserid(Integer.parseInt(spreadsUserId));
			spreadsUsersLog.setType("crm");
			spreadsUsersLog.setOpernote("crm");
			spreadsUsersLog.setOperation(operation);
			spreadsUsersLog.setAddtime(String.valueOf(currentTime));
			spreadsUsersLog.setAddip(ip);
			// 保存相应的更新日志信息
			ret += spreadsUsersLogMapper.insertSelective(spreadsUsersLog);
		} else {
			SpreadsUsers spreadsUsers = new SpreadsUsers();
			// 用户id
			spreadsUsers.setUserId(Integer.parseInt(userId));
			// 推荐人id
			spreadsUsers.setSpreadsUserid(Integer.parseInt(spreadsUserId));
			spreadsUsers.setType("crm");
			spreadsUsers.setOpernote("crm");
			spreadsUsers.setAddtime(String.valueOf(currentTime));
			spreadsUsers.setOperation(operation);
			spreadsUsers.setAddip(ip);

			// 插入推荐人
			ret += spreadsUsersMapper.insertSelective(spreadsUsers);

			SpreadsUsersLog spreadsUsersLog = new SpreadsUsersLog();
			spreadsUsersLog.setOldSpreadsUserid(null);
			spreadsUsersLog.setUserId(Integer.parseInt(userId));
			spreadsUsersLog.setSpreadsUserid(Integer.parseInt(spreadsUserId));
			spreadsUsersLog.setType("crm");
			spreadsUsersLog.setOpernote("crm");
			spreadsUsersLog.setOperation(operation);
			spreadsUsersLog.setAddtime(String.valueOf(currentTime));
			spreadsUsersLog.setAddip(ip);
			// 保存相应的更新日志信息
			ret += spreadsUsersLogMapper.insertSelective(spreadsUsersLog);
		}

		// 保存用户信息修改日志
		List<UserInfoForLogCustomize> usersLog = usersCustomizeMapper.selectUserByUserId(Integer.parseInt(userId));
		if (usersLog != null && !usersLog.isEmpty()) {
			UserInfoForLogCustomize customize = usersLog.get(0);
			UsersChangeLog changeLog = new UsersChangeLog();
			changeLog.setUserId(customize.getUserId());
			changeLog.setUsername(customize.getUserName());
			changeLog.setAttribute(customize.getAttribute());
			changeLog.setIs51(customize.getIs51());
			changeLog.setRole(customize.getUserRole());
			changeLog.setMobile(customize.getMobile());
			changeLog.setRealName(customize.getRealName());
			changeLog.setRecommendUser(oldRecommendUser);
			changeLog.setStatus(customize.getUserStatus());
			changeLog.setChangeType(ChangeLogDefine.CHANGELOG_TYPE_RECOMMEND);
			changeLog.setIdcard(customize.getIdCard());

			UsersChangeLogExample logExample = new UsersChangeLogExample();
			UsersChangeLogExample.Criteria logCriteria = logExample.createCriteria();
			logCriteria.andUserIdEqualTo(Integer.parseInt(userId));
			int count = usersChangeLogMapper.countByExample(logExample);
			if (count <= 0) {
				// 如果从来没有添加过操作日志，则将原始信息插入修改日志中
				changeLog.setRemark("初始注册");
				changeLog.setChangeUser("system");
				changeLog.setChangeTime(customize.getRegTime());
				usersChangeLogMapper.insertSelective(changeLog);
			}

			// 获取操作者id
			Integer operationId = 0;
			UsersExample usersExample = new UsersExample();
			UsersExample.Criteria usersCra = usersExample.createCriteria();
			usersCra.andUsernameEqualTo(operation);
			List<Users> operationUsers = this.usersMapper.selectByExample(usersExample);
			if (operationUsers != null && operationUsers.size() > 0) {
				Users operationUser = operationUsers.get(0);
				operationId = operationUser.getUserId();
			}
			// 插入一条用户信息修改日志
			changeLog.setChangeUser(operation);
			// 操作人
			changeLog.setChangeUserid(operationId);
			changeLog.setRecommendUser(newRecommendUser);
			changeLog.setRemark("crm");
			changeLog.setChangeTime(currentTime);
			ret += usersChangeLogMapper.insertSelective(changeLog);
		}
		return ret;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * 
	 * @param userId
	 * @return
	 * @author Michael
	 */

	@Override
	public UserEvalationResultCustomize getEvalationResultByUserId(int userId) {
		UserEvalationResultExampleCustomize example = new UserEvalationResultExampleCustomize();
		UserEvalationResultExampleCustomize.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(userId);
		List<UserEvalationResultCustomize> list = this.userEvalationResultCustomizeMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 
	 * @Description:通过身份证号获取户籍所在地
	 * @param idCard
	 * @return String
	 * @exception:
	 * @author: xulijie
	 * @time:2017年5月3日 下午4:26:43
	 */
	@Override
	public String getAreaByIdCard(String idCard) {
		if (StringUtils.isBlank(idCard) || idCard.length() < 15) {
			return "";
		}
		IdCardToAreaExample example = new IdCardToAreaExample();
		IdCardToAreaExample.Criteria criteria = example.createCriteria();
		Integer code = Integer.parseInt(idCard.substring(0, 6));
		// 条件查询
		if (Validator.isNotNull(idCard)) {
			criteria.andCardNumberEqualTo(code);
		}
		List<IdCardToArea> list = this.idCardToAreaMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0).getArea();
		}
		return "";
	}

	@Override
	public AdminUserBankOpenAccountCustomize selectBankOpenAccountById(int userId) {
		List<AdminUserBankOpenAccountCustomize> list = this.adminUsersCustomizeMapper.selectBankOpenAccountById(userId);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new AdminUserBankOpenAccountCustomize();
	}

	@Override
	public CompanyInfoBean queryCompanyInfoByAccoutnId(String accountId,JSONObject ret,String userId) {
		CompanyInfoBean info = new CompanyInfoBean();
		// 调用企业账户查询接口
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 版本号
		bean.setTxCode(BankCallMethodConstant.TXCODE_CORPRATION_QUERY);// 交易代码
		bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
		bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
		bean.setAccountId(accountId);// 电子账号
		bean.setLogUserId(userId);
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(Integer.parseInt(userId)));
		try {
			BankCallBean callBackBean = BankCallUtils.callApiBg(bean);
			if (callBackBean!=null) {
				if (BankCallStatusConstant.RESPCODE_SUCCESS.equals(callBackBean.getRetCode())) {
					info.setAccount(callBackBean.getCaccount());
					info.setBusId(callBackBean.getBusId());
					info.setIdType(callBackBean.getIdType());
					info.setIdNo(callBackBean.getIdNo());
					info.setMobile(callBackBean.getMobile());
					info.setName(callBackBean.getName());
					info.setTaxId(callBackBean.getTaxId());
					ret.put("status", "success");
					ret.put("result", "查询成功!");
					return info;
				}else{
					ret.put("status", "error");
					ret.put("result", "查询企业信息错误,请重新查询!");
					if (ManageUsersDefine.RESPCODE_CORPRATION_QUERY_EXIST.equals(callBackBean.getRetCode())) {
						ret.put("result", "卡号不存在!");
					}else if (ManageUsersDefine.RESPCODE_CORPRATION_QUERY_NOT_CORPRATION.equals(callBackBean.getRetCode())) {
						ret.put("result", "非企业账户!");
					}else if (ManageUsersDefine.RESPCODE_CORPRATION_QUERY_CHECEK_ERROR.equals(callBackBean.getRetCode())) {
						ret.put("result", "平台交易验证未通过!");
					}else if (ManageUsersDefine.RESPCODE_CORPRATION_QUERY_CORPRATION_MORE.equals(callBackBean.getRetCode())) {
						ret.put("result", "访问频率超限!");
					}
					return null;
				}
			}else{
				ret.put("status", "error");
				ret.put("result", "银行接口返回异常!");
				return null;
			}
		}catch(Exception e){
			ret.put("status", "error");
			ret.put("result", "银行接口查询异常!");
		}
		return null;
	}

	@Override
	public boolean updateCompanyInfo(CompanyInfoBean form, JSONObject ret) {
		try {
			Users user = this.getUsersByUserId(Integer.parseInt(form.getUserId()));
			if (user!=null && user.getUserType() != 1) {
				if (user.getBankOpenAccount() == 1) {//已开户
					ret.put("status", "error");
					ret.put("result", "用户已开户!");
					return false;
				}
			}
			int bankOpenFlag = user.getBankOpenAccount();
			if (user.getUserType() != 1) {
				boolean flag = checkBankOpenAccount(form,ret);
				if (!flag) {
					return false;
				}
			}
			if (StringUtils.isBlank(form.getIdType())) {
				form.setIdType("0");
			}
			CorpOpenAccountRecord record = null;
			if (bankOpenFlag == 1) {//修改信息
				CorpOpenAccountRecordExample example = new CorpOpenAccountRecordExample();
				example.createCriteria().andUserIdEqualTo(user.getUserId());
				List<CorpOpenAccountRecord> corpInfo = this.corpOpenAccountRecordMapper.selectByExample(example );
				if (corpInfo!=null&&corpInfo.size()>0) {
					record = corpInfo.get(0);
				}
			}else{
				record = new CorpOpenAccountRecord();
			}
					
			record.setUserId(user.getUserId());
			record.setUsername(user.getUsername());
			record.setBusiCode(form.getIdNo());
			record.setBusiName(form.getName());
			record.setStatus(6);//成功
			record.setAddTime(GetDate.getDate());
			record.setIsBank(1);//银行开户
			record.setCardType(Integer.parseInt(form.getIdType()));
			record.setTaxRegistrationCode(form.getTaxId());
			record.setBuseNo(form.getBusId());
			record.setRemark(form.getRemark());
			//保存企业信息
			int insertFlag = 0;
			if (bankOpenFlag == 1) {//修改信息
				int flag = this.corpOpenAccountRecordMapper.updateByPrimaryKey(record);
				if (flag > 0) {
					System.out.println("==================hyjf_corp_open_account_record 企业用户信息变更保存成功!======");
				}else{
					throw new Exception("============企业信息变更保存异常!========");
				}
			}else{
				insertFlag = this.corpOpenAccountRecordMapper.insertSelective(record);
				if (insertFlag > 0) {
					System.out.println("==================hyjf_corp_open_account_record 企业用户信息保存成功!======");
				}else{
					throw new Exception("============企业信息保存异常!========");
				}
			}
			
			//保存银行卡信息
			BankCard bankCard = null;
			if (bankOpenFlag == 1) {
				BankCardExample example = new BankCardExample();
				example.createCriteria().andUserIdEqualTo(user.getUserId());
				List<BankCard> bankCardList = bankCardMapper.selectByExample(example);
				if (bankCardList!=null && bankCardList.size()>0) {
					bankCard = bankCardList.get(0);
				}
			}else{
				bankCard = new BankCard();
			}
			bankCard.setUserId(user.getUserId());
			bankCard.setUserName(user.getUsername());
			bankCard.setCardNo(form.getAccount());
			bankCard.setCreateTime(GetDate.getDate());
			bankCard.setCreateUserId(user.getUserId());
			bankCard.setCreateUserName(user.getUsername());
			String bankId = getBankIdByCardNo(form.getAccount());
			if (StringUtils.isNotBlank(bankId)) {
				String bankName = getBankNameById(bankId);
				String payAllianceCode = null;
				BankCallBean callBean = payAllianceCodeQuery(form.getAccount(), user.getUserId());
				if (BankCallStatusConstant.RESPCODE_SUCCESS.equals(callBean.getRetCode())) {
					payAllianceCode = callBean.getPayAllianceCode();
					if (StringUtils.isBlank(payAllianceCode)) {
						payAllianceCode = getPayAllianceCodeByBankId(bankId);
					}
				}
				bankCard.setBankId(Integer.parseInt(bankId));
				bankCard.setBank(bankName);
				bankCard.setPayAllianceCode(payAllianceCode);
			}
			if (bankOpenFlag == 1) {
				int updateflag = bankCardMapper.updateByPrimaryKey(bankCard);
				if (updateflag > 0) {
					System.out.println("===============hyjf_bank_card 银行卡信息修改保存成功!==========");
				}else{
					throw new Exception("==========银行卡信息修改保存异常!============");
				}
			}else{
				int insertcard = this.bankCardMapper.insertSelective(bankCard );
				if (insertcard>0) {
					System.out.println("===============hyjf_bank_card 银行卡信息保存成功!==========");
				}else{
					throw new Exception("==========银行卡信息保存异常!============");
				}
			}
			
			//保存开户信息
			BankOpenAccount openAccount = new BankOpenAccount();
			openAccount.setUserId(user.getUserId());
			openAccount.setUserName(user.getUsername());
			openAccount.setAccount(form.getAccountId());
			openAccount.setCreateTime(GetDate.getDate());
			openAccount.setCreateUserId(user.getUserId());
			openAccount.setCreateUserName(user.getUsername());
			if (bankOpenFlag == 1) {//已开户
				BankOpenAccountExample example = new BankOpenAccountExample();
				example.createCriteria().andUserIdEqualTo(user.getUserId());
				List<BankOpenAccount> bankopenList = bankOpenAccountMapper.selectByExample(example );
				if (bankopenList!=null && bankopenList.size()>0) {
					openAccount.setId(bankopenList.get(0).getId());
					int openFlag = this.bankOpenAccountMapper.updateByPrimaryKey(openAccount);
					if (openFlag>0) {
						System.out.println("============hyjf_bank_open_account 银行开户修改信息保存成功!=============");
					}else{
						throw new Exception("============银行开户信息修改保存异常!==============");
					}
				}
			}else{
				int openFlag = this.bankOpenAccountMapper.insertSelective(openAccount);
				if (openFlag>0) {
					System.out.println("============hyjf_bank_open_account 银行开户信息保存成功!=============");
				}else{
					throw new Exception("============银行开户信息保存异常!==============");
				}
			}
			//替换企业信息名称
			UsersInfo userInfo = new UsersInfo();
			userInfo.setTruename(form.getName());
			UsersInfoExample example = new UsersInfoExample();
			example.createCriteria().andUserIdEqualTo(user.getUserId());
			int userInfoFlag = this.usersInfoMapper.updateByExampleSelective(userInfo, example);
			if (userInfoFlag>0) {
				System.out.println("==============huiyingdai_users_info 用户详细信息保存成功!==========");
			}else{
				throw new Exception("用户详细信息保存异常!");
			}
			if (bankOpenFlag != 1) {
				user.setBankAccountEsb(0);//开户平台,pc
				user.setUserType(1);//企业用户
				user.setBankOpenAccount(1);//已开户
				int userFlag = this.usersMapper.updateByPrimaryKey(user);
				if (userFlag>0) {
					System.out.println("=============huiyingdai_users 用户表信息保存成功!");
				}else{
					throw new Exception("用户表信息保存异常!");
				}
			}
			ret.put("status", "success");
			ret.put("result", "企业用户信息补录成功!");
			return true;
		} catch (Exception e) {
			System.out.println("===========保存企业用户信息异常!====异常信息" + e.getMessage());
			ret.put("status", "error");
			ret.put("result", "保存企业用户信息异常!");
			return false;
		}
	}

	/**
	 * 校验accountid是否已被使用
	 * @param form
	 * @param ret
	 * @return
	 */
	private boolean checkBankOpenAccount(CompanyInfoBean form, JSONObject ret) {
		BankOpenAccountExample openExample = new BankOpenAccountExample();
		openExample.createCriteria().andAccountEqualTo(form.getAccountId());
		List<BankOpenAccount> bankOpenAccount = this.bankOpenAccountMapper.selectByExample(openExample);
		if (bankOpenAccount!=null&&bankOpenAccount.size()>0) {
			BankOpenAccount info = bankOpenAccount.get(0);
			Integer openUserId = info.getUserId();
			UsersExample userExample = new UsersExample();
			userExample.createCriteria().andUserIdEqualTo(openUserId);
			List<Users> checkUser = this.usersMapper.selectByExample(userExample);
			if (checkUser!=null && checkUser.size()>0) {
				for (int i = 0; i < checkUser.size(); i++) {
					Integer openFlag = checkUser.get(i).getBankOpenAccount();
					if (openFlag == 1) {
						System.out.println("=======该电子账号已被使用,无法进行企业信息补录! userid is " + checkUser.get(i).getUserId());
						ret.put("status", "error");
						ret.put("result", "该电子账号已被使用,无法进行企业信息补录!");
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public CompanyInfoBean selectCompanyInfoByUserId(int userId) {
		CompanyInfoBean info = new CompanyInfoBean();
		CorpOpenAccountRecordExample example = new CorpOpenAccountRecordExample();
		example.createCriteria().andUserIdEqualTo(userId);
		List<CorpOpenAccountRecord> corpList = this.corpOpenAccountRecordMapper.selectByExample(example);
		BankCardExample cardExample = new BankCardExample();
		cardExample.createCriteria().andUserIdEqualTo(userId);
		List<BankCard> bankCardList = this.bankCardMapper.selectByExample(cardExample);
		String cardNo = null;
		if (bankCardList!=null && bankCardList.size()>0) {
			cardNo = bankCardList.get(0).getCardNo();
		}
		if (corpList !=null && corpList.size()>0) {
			String idType = null;
			Integer cardType = 0;
			if (corpList.get(0).getCardType()!=null) {
				cardType = corpList.get(0).getCardType(); 
			}
			if (20 == cardType) {//组织机构代码
				idType = "组织机构代码";
			}else if (25 == cardType) {
				idType = "社会信用号";
			}
			info.setCardType(cardType+"");
			info.setIdType(idType);
			info.setIdNo(corpList.get(0).getBusiCode());
			info.setName(corpList.get(0).getBusiName());
			info.setAccount(cardNo);
			info.setBusId(corpList.get(0).getBuseNo());
			info.setTaxId(corpList.get(0).getTaxRegistrationCode());
			info.setRemark(corpList.get(0).getRemark());
			return info;
		}
		
		return null;
	}

	/**
	 * 获取第三方平台绑定汇盈金服信息
	 */
    @Override
    public BindUsers queryBindUsers(int userId, Integer bindPlatformId) {
      //检索条件
        BindUsersExample example = new BindUsersExample();
        BindUsersExample.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(userId);
        cra.andBindPlatformIdEqualTo(bindPlatformId);
        cra.andDelFlgEqualTo(0);//未删除
        //检索
        List<BindUsers> list = bindUsersMapper.selectByExample(example);
        if(list.size()>0){
            return list.get(0);
        }
        return null;
       /* //无记录时，未绑定汇盈金服
        CheckUtil.check(list != null && list.size() > 0, "user.unbound");
        return list.get(0);*/
    }

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param userId
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public CertificateAuthority selectCertificateAuthorityByUserId(int userId) {
	    //检索条件
		CertificateAuthorityExample example = new CertificateAuthorityExample();
		CertificateAuthorityExample.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(userId);
        //检索
        List<CertificateAuthority> list = certificateAuthorityMapper.selectByExample(example);

        if(list.size()>0){
            return list.get(0);
        }
        return null;
	}

    @Override
    public void sendCAChangeMQ(AdminUserUpdateCustomize form) {
        // 加入到消息队列
        Map<String, String> params = new HashMap<String, String>();
        params.put("mqMsgId", GetCode.getRandomCode(10));
        params.put("userId", String.valueOf(form.getUserId()));
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.ROUTINGKEY_USER_INFO_CHANGE, JSONObject.toJSONString(params));
        // add by liuyang 20180227 修改手机号后 发送更新客户信息MQ end
    }
    
    @Override
    public void sendCAChangeMQ(AdminUserRecommendCustomize form) {
        // 加入到消息队列
        Map<String, String> params = new HashMap<String, String>();
        params.put("mqMsgId", GetCode.getRandomCode(10));
        params.put("userId", String.valueOf(form.getUserId()));
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.ROUTINGKEY_CERTIFICATE_AUTHORITY, JSONObject.toJSONString(params));
        // add by liushouyi 20180228 修改身份证号后 发送更新客户信息MQ end
    }

	/**
	 * 更新用户角色
	 * @param mobile
	 * @return
	 */
	@Override
	public JSONObject updateUserRoleId(Integer userId) {

		Users user=usersMapper.selectByPrimaryKey(userId);
        //获取共同参数start
        String bankInstCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
        String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
        String txDate = GetOrderIdUtils.getTxDate();
        String txTime = GetOrderIdUtils.getTxTime();
        String seqNo = GetOrderIdUtils.getSeqNo(6);
        //获取共同参数end

		BankCallBean selectbean = new BankCallBean();
        //共同参数封装start
		selectbean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        selectbean.setInstCode(bankInstCode);// 机构代码
        selectbean.setBankCode(bankCode);
        selectbean.setTxDate(txDate);
        selectbean.setTxTime(txTime);
        selectbean.setSeqNo(seqNo);
        //000001手机APP,000002网页,000003微信,000004柜面
        selectbean.setChannel("000002");
        //共同参数封装end

        //非共同参数封装start
        selectbean.setTxCode(BankCallMethodConstant.TXCODE_ACCOUNT_QUERY_BY_MOBILE);
        selectbean.setMobile(user.getMobile());
        //非共同参数封装end

		JSONObject result = new JSONObject();
		BankCallBean retBean;
		try {
			retBean=BankCallUtils.callApiBg(selectbean);
		}catch (Exception e){
			_log.error("请求银行接口出错!手机号:"+user.getMobile());
			result.put("code",BankCallStatusConstant.STATUS_FAIL);
			result.put("msg","请求银行接口出错!");
			return result;
		}
		if (Validator.isNull(retBean)){
			//获取银行账户信息失败
			result.put("code",BankCallStatusConstant.STATUS_FAIL);
			result.put("msg","获取银行账户信息失败!");
			return result;
		}


		UsersInfoExample userInfoExample = new UsersInfoExample();
		userInfoExample.createCriteria().andUserIdEqualTo(userId);
		List<UsersInfo> list = usersInfoMapper.selectByExample(userInfoExample);
		if (CollectionUtils.isNotEmpty(list)){
			UsersInfo userInfo = list.get(0);
			userInfo.setRoleId(Integer.parseInt(retBean.getIdentity()));
			userInfo.setUpdateTime(GetDate.getNowTime10());
			usersInfoMapper.updateByPrimaryKeySelective(userInfo);

		}else{
			result.put("code",BankCallStatusConstant.STATUS_FAIL);
			result.put("msg","查询不到对应的用户信息!");
			return result;
		}
		result.put("code",BankCallStatusConstant.STATUS_SUCCESS);
		result.put("msg","同步用户角色成功!");
		return result;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param form
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public void updateUserIdCard(AdminUserRecommendCustomize form) {
		// 初始化用户操作日志信息
		UsersChangeLog changeLog = new UsersChangeLog();
		List<UserInfoForLogCustomize> userInfos = usersCustomizeMapper
				.selectUserByUserId(Integer.parseInt(form.getUserId()));
		UserInfoForLogCustomize logRecord = userInfos.get(0);
		changeLog.setUserId(logRecord.getUserId());
		changeLog.setUsername(logRecord.getUserName());
		changeLog.setAttribute(logRecord.getAttribute());
		changeLog.setIs51(logRecord.getIs51());
		changeLog.setRealName(logRecord.getRealName());
		changeLog.setRecommendUser(logRecord.getRecommendName());
		changeLog.setChangeType(ChangeLogDefine.CHANGELOG_TYPE_IDCARD);
		changeLog.setMobile(logRecord.getMobile());
		changeLog.setRole(logRecord.getUserRole());
		changeLog.setStatus(logRecord.getUserStatus());
		changeLog.setIdcard(logRecord.getIdCard());
		changeLog.setRealName(logRecord.getRealName());

		UsersChangeLogExample logExample = new UsersChangeLogExample();
		UsersChangeLogExample.Criteria logCriteria = logExample.createCriteria();
		logCriteria.andUserIdEqualTo(Integer.parseInt(form.getUserId()));
		int count = usersChangeLogMapper.countByExample(logExample);
		if (count <= 0) {
			// 如果从来没有添加过操作日志，则将原始信息插入修改日志中
			if (userInfos != null && !userInfos.isEmpty()) {
				changeLog.setRemark("初始注册");
				changeLog.setChangeUser("system");
				changeLog.setChangeTime(logRecord.getRegTime());
				usersChangeLogMapper.insertSelective(changeLog);
			}
		}
		//更新userInfo表 ：根据userId更新用户真实姓名、身份证号
		// 根据推荐人用户名查询用户
		if (userInfos != null && !userInfos.isEmpty()) {
			Integer userId = userInfos.get(0).getUserId();
			// 查询用户详情
			UsersInfoExample usersInfoE = new UsersInfoExample();
			UsersInfoExample.Criteria uipec = usersInfoE.createCriteria();
			uipec.andUserIdEqualTo(userId);
			List<UsersInfo> usersInfoList = usersInfoMapper.selectByExample(usersInfoE);
			// 更新用户详情信息
			if (usersInfoList != null && usersInfoList.size() == 1) {
				UsersInfo userInfo = usersInfoList.get(0);
				userInfo.setTruename(form.getTrueName());
				userInfo.setIdcard(form.getIdCard());
				// 更新用户详情信息
				usersInfoMapper.updateByPrimaryKey(userInfo);
			}
		}
		
		//更新log表
		// 保存用户信息修改日志
		AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
		// 插入一条用户信息修改日志
		changeLog.setRealName(form.getTrueName());
		changeLog.setIdcard(form.getIdCard());
		changeLog.setChangeUser(adminSystem.getUsername());
		changeLog.setChangeUserid(Integer.parseInt(adminSystem.getId()));
		changeLog.setRemark(form.getRemark());
		changeLog.setChangeTime(GetDate.getNowTime10());
		usersChangeLogMapper.insertSelective(changeLog);
	}

	/**
	 * 保存用户基本信息
	 * @param userUpdate
	 * @return
	 */
	@Override
	public int updateUserInfos(AdminUserInfosUpdCustomize userUpdate) {
		// 初始化用户操作日志信息
		UsersChangeLog changeLog = new UsersChangeLog();
		List<UserInfoForLogCustomize> users = usersCustomizeMapper
				.selectUserByUserId(Integer.parseInt(userUpdate.getUserId()));
		//
		UsersInfo userInfoType = this.getUsersInfoByUserId(Integer.parseInt(userUpdate.getUserId()));
		UserInfoForLogCustomize logRecord = users.get(0);
		changeLog.setUserId(logRecord.getUserId());
		changeLog.setUsername(logRecord.getUserName());
		changeLog.setAttribute(logRecord.getAttribute());
		changeLog.setIs51(logRecord.getIs51());
		changeLog.setRealName(logRecord.getRealName());
		changeLog.setRecommendUser(logRecord.getRecommendName());
		changeLog.setMobile(logRecord.getMobile());
		changeLog.setRole(logRecord.getUserRole());
		changeLog.setStatus(logRecord.getUserStatus());
		changeLog.setIdcard(logRecord.getIdCard());
		changeLog.setEmail(logRecord.getEmail());
		changeLog.setMobile(logRecord.getMobile());
		changeLog.setBorrowerType(userInfoType.getBorrowerType());
		switch(userUpdate.getUpdFlg()){
			case "mobile":
				changeLog.setChangeType(ChangeLogDefine.CHANGELOG_TYPE_MOBILE);
				break;
			case "email":
				changeLog.setChangeType(ChangeLogDefine.CHANGELOG_TYPE_EMAIL);
				break;
			case "userRole":
				changeLog.setChangeType(ChangeLogDefine.CHANGELOG_TYPE_USERROLE);
				break;
		}
		UsersChangeLogExample logExample = new UsersChangeLogExample();
		UsersChangeLogExample.Criteria logCriteria = logExample.createCriteria();
		logCriteria.andUserIdEqualTo(Integer.parseInt(userUpdate.getUserId()));
		int count = usersChangeLogMapper.countByExample(logExample);
		if (count <= 0) {
			// 如果从来没有添加过操作日志，则将原始信息插入修改日志中
			if (users != null && !users.isEmpty()) {
				changeLog.setRemark("初始注册");
				changeLog.setChangeUser("system");
				changeLog.setChangeTime(logRecord.getRegTime());
				usersChangeLogMapper.insertSelective(changeLog);
			}
		}

		// 根据主键查询用户信息
		Users user = usersMapper.selectByPrimaryKey(Integer.parseInt(userUpdate.getUserId()));
		// 更新相应的用户的信息
		//用户状态
		if(StringUtils.isNotBlank(userUpdate.getStatus())){
			user.setStatus(Integer.parseInt(userUpdate.getStatus()));
		}
		//修改手机号
		if(StringUtils.isNotBlank(userUpdate.getMobile())){
			user.setMobile(userUpdate.getMobile());
		}
		//修改邮箱
		if(StringUtils.isNotBlank(userUpdate.getEmail())){
			user.setEmail(userUpdate.getEmail());
		}
		int usersUpdateFlag = usersMapper.updateByPrimaryKey(user);
		// 查询用户详情
		UsersInfoExample usersInfoE = new UsersInfoExample();
		UsersInfoExample.Criteria uipec = usersInfoE.createCriteria();
		uipec.andUserIdEqualTo(user.getUserId());
		List<UsersInfo> usersInfoList = usersInfoMapper.selectByExample(usersInfoE);
		// 更新用户详情信息
		if (usersInfoList != null && usersInfoList.size() == 1) {
			UsersInfo userInfo = usersInfoList.get(0);
			//修改角色
			if(StringUtils.isNotBlank(userUpdate.getUserRole())){
				userInfo.setRoleId(Integer.parseInt(userUpdate.getUserRole()));
			}

			// 更新用户详情信息
			usersInfoMapper.updateByPrimaryKey(userInfo);
			//设置log表的用户角色
			if(StringUtils.isNotBlank(userUpdate.getUserRole())){
				changeLog.setRole(Integer.parseInt(userUpdate.getUserRole()));
			}else{
				changeLog.setRole(userInfo.getRoleId());
			}
		}

		// 保存用户信息修改日志
		AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
		// 插入一条用户信息修改日志
		//用户状态
		if(StringUtils.isNotBlank(userUpdate.getStatus())){
			changeLog.setStatus(Integer.parseInt(userUpdate.getStatus()));
		}
		//手机号
		if(StringUtils.isNotBlank(userUpdate.getMobile())){
			changeLog.setMobile(userUpdate.getMobile());
		}
		//邮箱
		if(StringUtils.isNotBlank(userUpdate.getEmail())){
			changeLog.setEmail(userUpdate.getEmail());
		}
		changeLog.setChangeUser(adminSystem.getUsername());
		changeLog.setChangeUserid(Integer.parseInt(adminSystem.getId()));
		changeLog.setRemark(userUpdate.getRemark());
		changeLog.setChangeTime(GetDate.getNowTime10());
		changeLog.setBorrowerType(userInfoType.getBorrowerType());
		usersChangeLogMapper.insertSelective(changeLog);

		// add 合规数据上报 埋点 liubin 20181122 start
		// 推送数据到MQ 用户信息修改（修改手机号）
		JSONObject params = new JSONObject();
		params.put("userId", userUpdate.getUserId());
		this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.USERINFO_CHANGE_DELAY_KEY, JSONObject.toJSONString(params));
		// add 合规数据上报 埋点 liubin 20181122 end

		return usersUpdateFlag;
	}

	/**
	 * 根据用户id查找开户信息
	 * @param userId
	 * @return
	 */
	@Override
	public BankCard selectBankCardByUserId(Integer userId){
		BankCardExample example = new BankCardExample();
		example.createCriteria().andUserIdEqualTo(userId);
		List<BankCard> bankCardList = bankCardMapper.selectByExample(example);
		if (bankCardList!=null && bankCardList.size()>0) {
			return bankCardList.get(0);
		}
		return null;
	}

	@Override
	public int updateUserBankInfo(AdminUserInfosUpdCustomize userUpdate) {
		//更新银行卡信息
		BankCard bankCard = this.selectBankCardByUserId(Integer.parseInt(userUpdate.getUserId()));
		// 根据主键查询用户信息
		Users user = usersMapper.selectByPrimaryKey(Integer.parseInt(userUpdate.getUserId()));

		if(null!=bankCard) {
			//银行卡
			bankCard.setBank(userUpdate.getBank());
			//联行号
			bankCard.setPayAllianceCode(userUpdate.getPayAllianceCode());
			//银行卡号
			bankCard.setCardNo(userUpdate.getCardNo());
			//修改银行卡信息
			bankCardMapper.updateByPrimaryKey(bankCard);
		}
		//插入日志表
		BankAccountLog bankAccountLog = new BankAccountLog();
		bankAccountLog.setUserId(Integer.parseInt(userUpdate.getUserId()));
		bankAccountLog.setUserName(user.getUsername());
		bankAccountLog.setBankCode(String.valueOf(bankCard.getBankId()));
		bankAccountLog.setBankAccount(bankCard.getCardNo());
		// 获取银行卡姓名
		BanksConfig config = this.selectBankConfigBybankId(Integer.parseInt(bankAccountLog.getBankCode()));
		if (config != null) {
			bankAccountLog.setBankName(config.getBankName());
		} else {
			bankAccountLog.setBankName("");
		}
		bankAccountLog.setCardType(0);// 卡类型// 0普通提现卡1默认卡2快捷支付卡
		bankAccountLog.setOperationType(2);// 操作类型 0绑定 1删除 2:修改银行卡
		bankAccountLog.setStatus(0);// 成功
		bankAccountLog.setCreateTime(GetDate.getNowTime10());// 操作时间
		this.bankAccountLogMapper.insert(bankAccountLog);

		// add 合规数据上报 埋点 liubin 20181122 start
		// 推送数据到MQ 用户信息修改（修改银行卡号）
		JSONObject params = new JSONObject();
		params.put("userId", userUpdate.getUserId());
		this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.USERINFO_CHANGE_DELAY_KEY, JSONObject.toJSONString(params));
		// add 合规数据上报 埋点 liubin 20181122 end

		return 1;
	}

	/**
	 * 根据银行卡名查找银行卡配置
	 * @param bankName
	 * @return
	 */
	@Override
	public BanksConfig selectBankConfigBybankName(String bankName){
		BanksConfigExample example = new BanksConfigExample();
		example.createCriteria().andBankNameLike(bankName);
		List<BanksConfig> bankCardList = banksConfigMapper.selectByExample(example);
		if (bankCardList!=null && bankCardList.size()>0) {
			return bankCardList.get(0);
		}
		return null;
	}
	public BanksConfig selectBankConfigBybankId(int bankId){
		BanksConfigExample example = new BanksConfigExample();
		example.createCriteria().andBankIdEqualTo(bankId);
		List<BanksConfig> bankCardList = banksConfigMapper.selectByExample(example);
		if (bankCardList!=null && bankCardList.size()>0) {
			return bankCardList.get(0);
		}
		return null;
	}

	/**
	 * 查看银联号
	 * @param form
	 * @return
	 */
	@Override
	public  JSONObject searchPayAllianceCode(AdminUserInfosUpdCustomize form){
		JSONObject ret = new JSONObject();
		BankCallBean bankCallBean = this.payAllianceCodeQuery(form.getCardNo(),Integer.parseInt(form.getUserId()));
		if (null!=bankCallBean&&BankCallStatusConstant.RESPCODE_SUCCESS.equals(bankCallBean.getRetCode())) {
			//如果调用银行接口没有返回联行号,则查找本地联行号
			if (StringUtils.isBlank(bankCallBean.getPayAllianceCode())) {
				BanksConfig banksConfig = this.selectBankConfigBybankName(form.getBank());
				if(null!=banksConfig){
					ret.put("status","cSuccess");
					ret.put("message","未查询到分行联行号已填充总行联行号");
					ret.put("payAllianceCode",banksConfig.getPayAllianceCode());
					_log.info("============本地银联号为:",banksConfig.getPayAllianceCode());
				}else{
					ret.put("status","error");
					ret.put("message","未查询到联行号");
				}
			}else{
				//如果调用银行接口查找到银联号,则进行显示
				ret.put("status","bSuccess");
				ret.put("payAllianceCode",bankCallBean.getPayAllianceCode());
				_log.info("============银行查询银联号为:",bankCallBean.getPayAllianceCode());
			}
		}else{
			ret.put("status","error");
			ret.put("message","银行接口调用失败");
		}
		return ret;
	}
}
