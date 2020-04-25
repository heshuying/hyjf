package com.hyjf.api.server.user.openaccountplus;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.hyjf.api.web.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankCardExample;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BankOpenAccountExample;
import com.hyjf.mybatis.model.auto.BindUsers;
import com.hyjf.mybatis.model.auto.BindUsersExample;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.auto.HjhUserAuthExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;

/**
 * Created by yaoyong on 2017/11/30.
 */
@Service
public class BindUserServiceImpl extends BaseServiceImpl implements BindUserService {

	@Override
	public Boolean bindUser(Integer userId, String bindUniqueId, String platForm) {
		int nowTime = GetDate.getNowTime10();
		BindUsers bindUsers = new BindUsers();
		bindUsers.setUserId(userId);
		bindUsers.setBindUniqueId(bindUniqueId);
		bindUsers.setBindPlatformId(Integer.parseInt(platForm));
		bindUsers.setCreateTime(nowTime);
		return bindUsersMapper.insertSelective(bindUsers) > 0 ? true : false;
	}

    /**
     * 根据第三方用户id查询绑定关系
     * @param bindUniqueId
     * @param bind_platform_id
     * @return
     */
    @Override
	public BindUsers getUsersByUniqueId(Long bindUniqueId, Integer bind_platform_id) {

		BindUsersExample example = new BindUsersExample();
		BindUsersExample.Criteria cra = example.createCriteria();
		cra.andBindUniqueIdEqualTo(bindUniqueId+"").andBindPlatformIdEqualTo(bind_platform_id);
		List<BindUsers> bindUsers = bindUsersMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(bindUsers))
			return null;
		return bindUsers.get(0);
	}

	/**
	 * 获取用户电子账号
	 * @param userId
	 * @return
	 */
	@Override
	public String getAccountId(Integer userId) {
		BankOpenAccountExample example = new BankOpenAccountExample();
		BankOpenAccountExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		List<BankOpenAccount> accounts = bankOpenAccountMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(accounts)) {
			return null;
		}
		return accounts.get(0).getAccount();
	}

	/**
	 * 获取银联行号
	 * @param userId
	 * @return
	 */
	@Override
	public String getPayAllianceCode(Integer userId) {
		BankCardExample example = new BankCardExample();
		BankCardExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		List<BankCard> bankCards = bankCardMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(bankCards)) {
			return null;
		}
		return bankCards.get(0).getPayAllianceCode();
	}

	/**
	 * 获取自动投标授权状态
	 * @param userId
	 * @return
	 */
	@Override
	public String getAutoInvesStatus(Integer userId) {
		HjhUserAuthExample example = new HjhUserAuthExample();
		example.createCriteria().andUserIdEqualTo(userId).andDelFlgEqualTo(0);
		List<HjhUserAuth> hjhUserAuthList = hjhUserAuthMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(hjhUserAuthList)) {
			return null;
		} else {
			return String.valueOf(hjhUserAuthList.get(0).getAutoInvesStatus());
		}
	}

	/**
	 * 获取用户真实姓名
	 * @param idNo
	 * @return
     */
	@Override
	public String getTrueName(String idNo) {
		UsersInfoExample example = new UsersInfoExample();
		example.createCriteria().andIdcardEqualTo(idNo);
		List<UsersInfo> userInfo = usersInfoMapper.selectByExample(example);
		if (userInfo != null && userInfo.size() > 0) {
			return userInfo.get(0).getTruename();
		}
		return null;
	}
}
