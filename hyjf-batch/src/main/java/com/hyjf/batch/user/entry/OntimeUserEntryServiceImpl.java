package com.hyjf.batch.user.entry;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;

@Service
public class OntimeUserEntryServiceImpl extends BaseServiceImpl implements OntimeUserEntryService {

	/**
	 * 查询符合条件的员工 列表
	 * 
	 * @param ontime
	 * @return
	 */
	public List<UsersInfo> queryEmployeeEntryList() {
		List<UsersInfo> users = this.ontimeUserEntryCustomizeMapper.queryEmployeeList();
		return users;
	}

	/**
	 * 修改用户属性信息
	 * 
	 * @param record
	 * @return
	 */
	public int updateEmployeeByExampleSelective(UsersInfo record) {
		UsersInfoExample usersInfoExample = new UsersInfoExample();
		UsersInfoExample.Criteria uCriteria = usersInfoExample.createCriteria();
		uCriteria.andUserIdEqualTo(record.getUserId());
		int result = this.usersInfoMapper.updateByExampleSelective(record, usersInfoExample);
		return result;
	}

	/**
	 * 入职员工要删除推荐人
	 * 
	 * @param example
	 * @return
	 * @throws Exception 
	 */
	public boolean deleteReferrer(Integer userId) throws Exception {
		
		Users user = this.usersMapper.selectByPrimaryKey(userId);
		if(user!=null){
			user.setReferrer(null);
			user.setReferrerUserName(null);
			boolean userFlag = this.usersMapper.updateByPrimaryKey(user)>0?true:false;
			if(userFlag){
				SpreadsUsersExample example = new SpreadsUsersExample();
				SpreadsUsersExample.Criteria crt = example.createCriteria();
				crt.andUserIdEqualTo(userId);
				this.spreadsUsersMapper.deleteByExample(example);
				return true;
			}else{
				throw new Exception("更新用户的推荐人错误---userId"+ userId);
			}
		}
		return false;
	}

	/**
	 * 客户变员工后，其所推荐客户变为‘有主单’
	 * 
	 * @param referrer
	 * @return
	 */
	public int updateSpreadAttribute(Integer referrer) {
		int result = this.ontimeUserEntryCustomizeMapper.updateSpreadAttribute(referrer);
		return result;
	}

}
