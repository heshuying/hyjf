package com.hyjf.activity.actten.act1;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.ActRewardList;
import com.hyjf.mybatis.model.auto.ActRewardListExample;
import com.hyjf.mybatis.model.auto.ActRewardListExample.Criteria;
import com.hyjf.mybatis.model.auto.ActSignin;
import com.hyjf.mybatis.model.auto.ActSigninExample;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;


@Service
public class ActSigninServiceImpl extends BaseServiceImpl implements ActSigninService{
    
	SimpleDateFormat df = new SimpleDateFormat("dd");

	@Override
	public List<ActSignin> getActSignin(String userid) {
		ActSigninExample example = new ActSigninExample();
		example.or().andUserIdEqualTo(new Integer(userid));
		example.setOrderByClause(" sign_time asc ");
		return actSigninMapper.selectByExample(example);
	}

	@Override
	public  List<ActRewardList> getActRewardList(String userid) {
		ActRewardListExample example =new ActRewardListExample();
		example.createCriteria().andUserIdEqualTo(new Integer(userid)).andActTypeEqualTo(9);
		example.setOrderByClause("act1_reward_type asc ");
		return actRewardListMapper.selectByExample(example);
	}

	@Override
	public boolean setActSignin(String userid, String userName, String mobile) {

		if(StringUtils.isEmpty(userid)){
			return false;
		}
		
		UsersInfoExample infoexample=new UsersInfoExample();
		infoexample.createCriteria().andUserIdEqualTo(new Integer(userid));
		List<UsersInfo> usi= usersInfoMapper.selectByExample(infoexample);
		if(usi ==null || usi.isEmpty()){
			return false;
		}
		
		ActSigninExample example = new ActSigninExample();
		example.setOrderByClause(" sign_time desc ");
		example.or().andUserIdEqualTo(new Integer(userid));
//		example.or().andSignTimeEqualTo(new Integer(df.format(new Date())));
		List<ActSignin> asn= actSigninMapper.selectByExample(example);
		//如果为空
		if(asn.isEmpty()){
			ActSignin record=new ActSignin();
			if(!usi.isEmpty()){
				record.setTruename(usi.get(0).getTruename());
			}
			record.setUserId(new Integer(userid));
			record.setUserName(userName);
			record.setMobile(mobile);
			record.setSignTime(new Integer(df.format(new Date())));
			record.setCurrentSignNum(1);
			record.setCreateTime(GetDate.getNowTime10());
			actSigninMapper.insert(record);
			return true;
		}
		//判断今天是否签到
		else if (!asn.get(0).getSignTime().equals(new Integer(df.format(new Date())))){
			ActSignin record=new ActSignin();
			record.setTruename(asn.get(0).getTruename());
			record.setUserId(new Integer(userid));
			record.setUserName(userName);
			record.setMobile(mobile);
			record.setSignTime(new Integer(df.format(new Date())));
			record.setCurrentSignNum(asn.get(0).getCurrentSignNum()+1);
			record.setCreateTime(GetDate.getNowTime10());
			actSigninMapper.insert(record);
			return true;
		}else{
			return false;
		}
		
	}

	@Override
	public boolean setActRewardList(String userid, String userName, String mobile,int type) {
		ActSigninExample example = new ActSigninExample();
		example.or().andUserIdEqualTo(new Integer(userid));
		example.setOrderByClause(" sign_time desc ");
		List<ActSignin> asn=actSigninMapper.selectByExample(example);
		
		ActRewardListExample example1 =new ActRewardListExample();
		 Criteria criteria = example1.createCriteria();
		 criteria.andUserIdEqualTo(new Integer(userid));
		 criteria.andAct1RewardTypeEqualTo(type);
		 criteria.andActTypeEqualTo(9);
		List<ActRewardList> asl= actRewardListMapper.selectByExample(example1);
		//没有签到
		if(asn.isEmpty()){
			return false;
		}
		//已经领取该类型优惠券
		if(!asl.isEmpty()){
			return false;
		}
		if(type==1){
			if(asn.size()>=2){
				ActRewardList record=new ActRewardList();
				record.setUserId(new Integer(userid));
				record.setUserName(userName);
				record.setActType(9);
				record.setAct1RewardType(type);
				record.setSendFlg(0);
				record.setCreateTime(GetDate.getNowTime10());
				actRewardListMapper.insert(record);
				return true;
			}
		}
		if(type==2){
			if(asn.size()>=9){
				ActRewardList record=new ActRewardList();
				record.setUserId(new Integer(userid));
				record.setUserName(userName);
				record.setActType(9);
				record.setAct1RewardType(type);
				record.setSendFlg(0);
				record.setCreateTime(GetDate.getNowTime10());
				actRewardListMapper.insert(record);
				return true;
			}
		}
		if(type==3){
			if(asn.size()>=23){
				ActRewardList record=new ActRewardList();
				record.setUserId(new Integer(userid));
				record.setUserName(userName);
				record.setActType(9);
				record.setAct1RewardType(type);
				record.setSendFlg(0);
				record.setCreateTime(GetDate.getNowTime10());
				actRewardListMapper.insert(record);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean updateActRewardList(String userid, int type,String couponCode,Integer state) {
		ActRewardListExample example =new ActRewardListExample();
		 Criteria criteria = example.createCriteria();
		 criteria.andUserIdEqualTo(new Integer(userid));
		 criteria.andAct1RewardTypeEqualTo(type);
		 criteria.andActTypeEqualTo(9);
		List<ActRewardList> asl=actRewardListMapper.selectByExample(example);
		ActRewardList asl1=asl.get(0);
		asl1.setCouponCode(couponCode);
		asl1.setSendFlg(state);
		asl1.setUpdateTime(GetDate.getNowTime10());
		actRewardListMapper.updateByPrimaryKey(asl1);
		return true;
	}
	
    /**
     * 获取活动时间
     * 
     * @return
     */
	@Override
    public ActivityList getActivityDate(int id) {
    	
        return activityListMapper.selectByPrimaryKey(id);
    }



}
