package com.hyjf.activity.mgm10.recommend;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.InviteRecommend;
import com.hyjf.mybatis.model.auto.InviteRecommendExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.customize.recommend.InviteInfoCustomize;
import com.hyjf.mybatis.model.customize.recommend.InviteRecommendPrizeCustomize;

@Service("recommendService")
public class RecommendServiceImpl extends BaseServiceImpl implements RecommendService {

    /**
     * 
     * 获取推荐星信息
     * @author hsy
     * @param paraMap
     * @return
     */
    @Override
    public InviteRecommend getRecommendInfo(Map<String, Object> paraMap){
        if(Validator.isNull(paraMap.get("userId"))){
            return null;
        }
        
        InviteRecommendExample example = new InviteRecommendExample();
        example.createCriteria().andUserIdEqualTo((Integer)paraMap.get("userId"));
        List<InviteRecommend> recommend = inviteRecommendMapper.selectByExample(example);
        
        if(recommend == null || recommend.isEmpty()){
            return null;
        }
        
        return recommend.get(0);
    }

    @Override
    public List<InviteInfoCustomize> getUserRecommendStarPrizeList(Map<String, Object> paraMap) {
        if(Validator.isNull(paraMap.get("userId"))){
            return null;
        }
        List<InviteInfoCustomize> inviteInfoCustomizes=recommendCustomizeMapper.getUserRecommendStarPrizeList(paraMap);
        for (InviteInfoCustomize inviteInfoCustomize : inviteInfoCustomizes) {
            if(inviteInfoCustomize.getSource()==3){
                paraMap.put("groupCode", inviteInfoCustomize.getGroupCode());
                List<String> inviteUserNames=recommendCustomizeMapper.getInviteUserName(paraMap);
                inviteInfoCustomize.setInviteUserName(inviteUserNames);
            }
        }
        return inviteInfoCustomizes;
    }

    @Override
    public List<InviteRecommendPrizeCustomize> getUserRecommendStarUsedPrizeList(Map<String, Object> paraMap) {
        if(Validator.isNull(paraMap.get("userId"))){
            return null;
        }
        
        List<InviteRecommendPrizeCustomize> inviteRecommendPrizeCustomizes= recommendCustomizeMapper.getUserRecommendStarUsedPrizeList(paraMap);
        
        return inviteRecommendPrizeCustomizes;
    }

    @Override
    public void getUserFlag(UserFlagResultBean resultBean, Integer userId) {
        
        
        // 参与活动的用户不能为公司内部员工，即只能为有主单或无主单
        UsersInfoExample userExample = new UsersInfoExample();
        UsersInfoExample.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andUserIdEqualTo(userId);
        List<UsersInfo> usersInfoList = this.usersInfoMapper.selectByExample(userExample);
        if(usersInfoList!=null&&usersInfoList.size()>0){
            UsersInfo userInfo = usersInfoList.get(0);
            if(userInfo.getAttribute()!=0&&userInfo.getAttribute()!=1){
                //可以参加
                resultBean.setIsStaff("0");
            }else{
                //不能参加
                resultBean.setIsStaff("1");
            }
        }
        
        UsersExample usersExample=new UsersExample();
        usersExample.createCriteria().andUserIdEqualTo(userId);
        List<Users> users=usersMapper.selectByExample(usersExample);
        if(users.size()!=0){
            Users user = users.get(0);
            if(user.getInvestflag()==1){
              //已出借
                resultBean.setIsInvest("1");
            }else{
              //未出借
                resultBean.setIsInvest("0");
            }
        }
    }

  
}
