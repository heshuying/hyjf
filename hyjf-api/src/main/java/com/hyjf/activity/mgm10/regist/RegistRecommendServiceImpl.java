
package com.hyjf.activity.mgm10.regist;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.InviteInfo;
import com.hyjf.mybatis.model.auto.InviteInfoExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;

/**
 * 注册开户送推荐星
 * @author zhangjinpeng
 *
 */
@Service
public class RegistRecommendServiceImpl extends BaseServiceImpl implements RegistRecommendService {

	/**
	 * 业务校验
	 */
	@Override
	public JSONObject validateService(String id, Integer inviteUser, Integer inviteByUser) {
		Integer activityId = Integer.valueOf(id);
		// 检查活动有效期
		ActivityList activity = this.activityListMapper.selectByPrimaryKey(activityId);
		Integer now = GetDate.getNowTime10();
		if(now<activity.getTimeStart()||now>activity.getTimeEnd()){
			return jsonMessage("不在活动有效期内!","1");
		}
		// 同一个被邀请人不能被同一个人重复邀请
		InviteInfoExample example = new InviteInfoExample();
		InviteInfoExample.Criteria  criteria = example.createCriteria();
		criteria.andInviteUserEqualTo(inviteUser);
		criteria.andInviteByUserEqualTo(inviteByUser);
		Integer inviteCount = this.inviteInfoMapper.countByExample(example);
		if(inviteCount > 1 ){
			return jsonMessage("对于同一个被邀请人，不能重复邀请!","2");
		}
		// 参与活动的用户不能为公司内部员工，即只能为有主单或无主单
		UsersInfoExample userExample = new UsersInfoExample();
		UsersInfoExample.Criteria userCriteria = userExample.createCriteria();
		userCriteria.andUserIdEqualTo(inviteUser);
		List<UsersInfo> usersInfoList = this.usersInfoMapper.selectByExample(userExample);
		if(usersInfoList!=null&&usersInfoList.size()>0){
			UsersInfo userInfo = usersInfoList.get(0);
			if(userInfo.getAttribute()!=0&&userInfo.getAttribute()!=1){
				return jsonMessage("公司内部员工不能参加本次活动!","3");
			}
		}
		// 邀请人是否在平台出借过
		Users user = this.usersMapper.selectByPrimaryKey(inviteUser);
		if(user.getInvestflag() != 1){
			return jsonMessage("没有在平台出借过的用户不能参加本次活动!","4");
		}
		return jsonMessage("校验成功","0");
	}
	

    
	/**
	 * 组成返回信息
	 * 
	 * @param message
	 * @param status
	 * @return
	 */
	public JSONObject jsonMessage(String data, String error) {
		JSONObject jo = null;
		if (Validator.isNotNull(data)) {
			jo = new JSONObject();
			jo.put("error", error);
			jo.put("data", data);
		}
		return jo;
	}



	/**
	 * 增加一条推荐信息
	 */
	@Override
	public void insertInviteInfo(Integer inviteUser, Integer inviteByUser) throws Exception {
		Integer now = GetDate.getNowTime10();
		InviteInfo record = new InviteInfo();
		record.setInviteUser(inviteUser);
		record.setInviteByUser(inviteByUser);
		record.setRecommendSource(1);
		record.setRecommendCount(1);
		record.setSendFlag(0);
		//record.setSendTime(now);
		record.setAddTime(now);
		record.setUpdateTime(now);
		record.setDelFlg(0);
		this.inviteInfoMapper.insertSelective(record);
		
	}
  
}
