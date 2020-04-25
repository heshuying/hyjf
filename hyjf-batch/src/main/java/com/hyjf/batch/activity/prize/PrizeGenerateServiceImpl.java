package com.hyjf.batch.activity.prize;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PrizeCodeUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.ActivityListExample;
import com.hyjf.mybatis.model.auto.PrizeList;
import com.hyjf.mybatis.model.auto.PrizeListExample;
import com.hyjf.mybatis.model.auto.UserPrizeCode;
import com.hyjf.mybatis.model.auto.UserPrizeCodeExample;

/**
 * 
 * 生成中奖用户
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月27日
 * @see 上午9:54:28
 */
@Service
public class PrizeGenerateServiceImpl extends BaseServiceImpl implements PrizeGenerateService {

    private static final String THIS_CLASS = PrizeGenerateServiceImpl.class.getName();

	@Override
	public void updatePrizeGenerate() throws Exception {
		String methodName = "prizeGenerateService";
		LogUtil.startLog(THIS_CLASS, methodName, "生成中奖用户开始");
		//获取出借夺宝活动id
		int activityId = Integer.valueOf(PropUtils.getSystem(CustomConstants.TENDER_PRIZE_ACTIVITY_ID));
		ActivityListExample example = new ActivityListExample();
		ActivityListExample.Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(activityId);
		
		List<ActivityList> activityList = activityListMapper.selectByExample(example);
		if(activityList ==null || activityList.isEmpty()){
		    throw new RuntimeException("未获取到出借夺宝活动数据，活动配置ID：" + activityId);
		}
		
		//校验活动是否过期
		ActivityList activity = activityList.get(0);
		int nowTime = GetDate.getNowTime10();
		if(nowTime < activity.getTimeStart()){
		    LogUtil.infoLog(THIS_CLASS, methodName, "出借夺宝活动尚未开始，开始时间：" + GetDate.formatDate(activity.getTimeStart()*1000));
		    return;
		}
		
		//获取奖品状态为未开奖的奖品列表
		PrizeListExample prizeExample = new PrizeListExample();
        PrizeListExample.Criteria cria = prizeExample.createCriteria();
        cria.andPrizeStatusEqualTo(0);
        cria.andDelFlgEqualTo(0);
        List<PrizeList> prizeList = prizeListMapper.selectByExample(prizeExample);
        
		if(nowTime <= activity.getTimeEnd()){
		    //活动进行中
		    for(PrizeList prize : prizeList){ 
		        if(prize.getJoinedPersonCount() > 0 &&prize.getJoinedPersonCount() >= prize.getAllPersonCount()){
		            prize(prize);
		        }
	        }
		}else {
		    //活动已结束
		    for(PrizeList prize : prizeList){ 
		        if(prize.getJoinedPersonCount() > 0){
		            prize(prize);
		        }
	        }
		    
		}
		
		LogUtil.endLog(THIS_CLASS, methodName, "生成中奖用户结束");
	}

	/**
	 * 
	 * 中奖处理
	 * @author hsy
	 * @param prize
	 * @throws Exception
	 */
    public void prize(PrizeList prize) throws Exception {
        String methodName = "prize";
        
        Long stampSum = 0L;
        UserPrizeCodeExample codeExample = new UserPrizeCodeExample();
        UserPrizeCodeExample.Criteria cria2 = codeExample.createCriteria();
        cria2.andPrizeIdEqualTo(prize.getId());
        cria2.andDelFlgEqualTo(0);
        codeExample.setOrderByClause(" add_time asc ");
        List<UserPrizeCode> codeList = userPrizeCodeMapper.selectByExample(codeExample);
        if(codeList !=null && !codeList.isEmpty()){
            //后二十位夺宝用户夺宝时间和
            for(int i=codeList.size()-1; i>=0; i--){
                stampSum += codeList.get(i).getAddTime();
                if(i+19 <= codeList.size()-1){
                    break;
                }
            }
            //生成中奖码
            String sysPrizeCode = PrizeCodeUtils.getSystemPrizeCode(stampSum, prize.getJoinedPersonCount(), prize.getPrizeSelfCode());
            LogUtil.infoLog(THIS_CLASS, methodName, "奖品：" + prize.getPrizeName() + " 中奖码：" + sysPrizeCode);
        
            //保存生成的中奖码
            prize.setPrizeCode(sysPrizeCode);
            prize.setPrizeStatus(1);
            prize.setUpdateTime(GetDate.getNowTime10());
            prize.setUpdateUser(CustomConstants.OPERATOR_AUTO_REPAY);
            int result = prizeListMapper.updateByPrimaryKeySelective(prize);
            
            if(result != 1){
                throw new RuntimeException("更新中奖表失败，奖品id：" + prize.getId());
            }
            //更新用户中奖状态
            UserPrizeCode prizeCode = new UserPrizeCode();
            prizeCode.setPrizeFlg(1);
            prizeCode.setUpdateTime(GetDate.getNowTime10());
            prizeCode.setUpdateUser(CustomConstants.OPERATOR_AUTO_REPAY);
            
            UserPrizeCodeExample codeExample3 = new UserPrizeCodeExample();
            UserPrizeCodeExample.Criteria cria3 = codeExample3.createCriteria();
            cria3.andPrizeIdEqualTo(prize.getId());
            cria3.andPrizeCodeEqualTo(sysPrizeCode);
            result = userPrizeCodeMapper.updateByExampleSelective(prizeCode, codeExample3);
            if(result != 1){
                throw new RuntimeException("更新幸运码表失败，中奖码：" + sysPrizeCode);
            }
        }else {
            throw new RuntimeException("该奖品没有夺宝用户，奖品id：" + prize.getId());
        }
    }

    
}
