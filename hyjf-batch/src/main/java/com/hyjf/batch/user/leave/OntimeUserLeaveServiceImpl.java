package com.hyjf.batch.user.leave;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.customize.UserUpdateParamCustomize;

@Service
public class OntimeUserLeaveServiceImpl extends BaseServiceImpl implements OntimeUserLeaveService {

	/**
	 * 查询符合条件的员工 列表
	 * 
	 * @param ontime
	 * @return
	 */
	public List<Users> queryEmployeeList() {
		List<Users> users = this.ontimeUserLeaveCustomizeMapper.queryEmployeeList();
		return users;
	}

	/**
	 * 修改用户属性信息
	 * 
	 * @param record
	 * @return
	 * @throws Exception
	 */
	public boolean updateEmployeeByExampleSelective(Users employee) throws Exception {

		// 当前用户的userId
		int userId = employee.getUserId();
		Users user = this.usersMapper.selectByPrimaryKey(userId);
		if(Validator.isNotNull(user)){
			// 推荐人用户id
			Integer spreadUserId = null;
			//
			String spreadUserName = null;
			// 用户属性
			int attribute = 0;
			// 查找推荐人
			SpreadsUsersExample example = new SpreadsUsersExample();
			SpreadsUsersExample.Criteria crt = example.createCriteria();
			crt.andUserIdEqualTo(userId);
			List<SpreadsUsers> spreadUsers = spreadsUsersMapper.selectByExample(example);
			if (spreadUsers != null && spreadUsers.size() > 0) {
				if (spreadUsers.size() == 1) {
					SpreadsUsers spreadUser = spreadUsers.get(0);
					//获取推荐人的用户id
					spreadUserId = spreadUser.getSpreadsUserid();
					Users sprUser =this.usersMapper.selectByPrimaryKey(spreadUserId);
					if(Validator.isNotNull(sprUser)){
						spreadUserName = sprUser.getUsername();
						// 从oa表中查询线上线下部门属性
						List<UserUpdateParamCustomize> userUpdateParamList = ontimeUserLeaveCustomizeMapper.queryUserAndDepartment(spreadUserId);
						if (userUpdateParamList != null && userUpdateParamList.size() > 0) {
							if (userUpdateParamList.size() == 1) {
								UserUpdateParamCustomize userParam = userUpdateParamList.get(0);
								if (userParam.getCuttype() != null) {
									if (userParam.getCuttype().equals("1")) {// 线上
										attribute = 1;
									} else if (userParam.getCuttype().equals("2")) {// 线下
										attribute = 1;
									}
								} else {
									throw new Exception("推荐人的cuttype为空！userId:" + userId);
								}
							} else {
								throw new Exception("数据错误，查询到多条部门信息！userId:" + userId);
							}
						} else {
							attribute = 0;
						}
					}else{
						throw new Exception("用户推荐人users不存在，数据错误！推荐人userId:" + spreadUserId);
					}
				} else {
					throw new Exception("spreadUsers推荐人记录有多条，数据错误！userId:" + userId);
				}
			}
			user.setReferrer(spreadUserId);
			user.setReferrerUserName(spreadUserName);
			boolean userFlag = this.usersMapper.updateByPrimaryKey(user) > 0 ? true : false;
			if (userFlag) {
				// 初始化用户属性对象
				UsersInfo usersInfo = new UsersInfo();
				// 用户属性
				usersInfo.setAttribute(attribute);
				UsersInfoExample usersInfoExample = new UsersInfoExample();
				UsersInfoExample.Criteria uCriteria = usersInfoExample.createCriteria();
				uCriteria.andUserIdEqualTo(userId);
				boolean usersInfoFlag = this.usersInfoMapper.updateByExampleSelective(usersInfo, usersInfoExample) > 0 ? true : false;
				if (usersInfoFlag) {
					return true;
				} else {
					throw new Exception("usersInfo更新用户的属性错误错误！userId:" + userId);
				}
			} else {
				throw new Exception("users更新用户的推荐人错误！userId:" + userId);
			}
		}else{
			return false;
		}
		
	}

	/**
	 * 员工离职后，其所推荐客户变为‘无主单’
	 * 
	 * @param referrer
	 * @return
	 */
	public int updateSpreadAttribute(Integer referrer) {
		int result = this.ontimeUserLeaveCustomizeMapper.updateSpreadAttribute(referrer);
		return result;
	}

}
