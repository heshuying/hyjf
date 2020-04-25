package com.hyjf.activity.mgm10.prizechange;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.InviteRecommend;
import com.hyjf.mybatis.model.auto.InviteRecommendExample;
import com.hyjf.mybatis.model.auto.InviteRecommendPrize;
import com.hyjf.mybatis.model.customize.admin.PrizeGetCustomize;
import com.hyjf.soa.apiweb.CommonParamBean;
import com.hyjf.soa.apiweb.CommonSoaUtils;

@Service("prizeChangeService")
public class PrizeChangeServiceImpl extends BaseServiceImpl implements PrizeChangeService {

    /**
     * 获取奖品列表
     * @return
     */
    @Override
    public List<PrizeGetCustomize> getPrizeList(Map<String, Object> paraMap) {
       return prizeGetCustomizeMapper.selectPrizeConfList(paraMap);
    }
    
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
    
    /**
     * 
     * 根据groupcode获取奖品信息
     * @author hsy
     * @param groupCode
     * @return
     */
    @Override
    public PrizeGetCustomize getPrizeConfByGroup(String groupCode){
        Map<String,Object> paraMap = new HashMap<String,Object>();
        paraMap.put("prizeGroupCode", groupCode);
        paraMap.put("fileurl", PropUtils.getSystem("file.domain.url"));
        List<PrizeGetCustomize> prizeConf = prizeGetCustomizeMapper.selectPrizeConfList(paraMap);
        if(prizeConf == null || prizeConf.isEmpty()){
            return null;
        }
        return prizeConf.get(0);
    }
    
    /**
     * 
     * 更新奖品发放状态
     * @author hsy
     * @param id
     * @param sendStatus
     * @return
     */
    @Override
    public int updatePrizeSendById(InviteRecommendPrize recommendPrize){
        return inviteRecommendPrizeMapper.updateByPrimaryKeySelective(recommendPrize);
    }
    
    /**
     * 
     * 奖品兑换
     * @author hsy
     * @param recommendPrize
     * @return
     */
    @Override
    public boolean insertPrizeChange(InviteRecommendPrize recommendPrize, int recommendCost, int prizeCount){
        String methodName = "insertPrizeChange";
        
        if(Validator.isNull(recommendPrize.getUserId()) || Validator.isNull(recommendPrize.getPrizeGroup())){
            LogUtil.errorLog(this.getClass().getName(), methodName, null);
            return false;
        }
        
        //插入推荐星兑奖表
        recommendPrize.setAddTime(GetDate.getNowTime10());
        recommendPrize.setUpdateTime(GetDate.getNowTime10());
        recommendPrize.setDelFlg(CustomConstants.FALG_NOR);
        recommendPrize.setPrizeKind(Integer.parseInt(CustomConstants.CONF_PRIZE_KIND_CHANGE));
        if(recommendPrize.getPrizeType().equals(Integer.parseInt(CustomConstants.CONF_PRIZE_TYPE_ENTITY))){
            //实物奖品更新为人工发放
            recommendPrize.setPrizeSendFlag(Integer.parseInt(CustomConstants.PRIZE_SEND_FLAG_MANUAL));
        }else{
            recommendPrize.setPrizeSendFlag(Integer.parseInt(CustomConstants.PRIZE_SEND_FLAG_YES));
        }
        int result = inviteRecommendPrizeMapper.insertSelective(recommendPrize);
        
        if(result <= 0){
            LogUtil.errorLog(this.getClass().getName(), methodName, "更新用户剩余推荐星失败，用户id：" + recommendPrize.getUserId(), null);
            throw new RuntimeException("更新用户剩余推荐星失败，用户id：" + recommendPrize.getUserId());
        }
        
        //更新用户推荐星表
        InviteRecommendExample example = new InviteRecommendExample();
        example.createCriteria().andUserIdEqualTo(recommendPrize.getUserId());
        List<InviteRecommend> recommends = inviteRecommendMapper.selectByExample(example);
        if(recommends == null || recommends.isEmpty()){
            LogUtil.errorLog(this.getClass().getName(), methodName, "未检索到用户推荐星记录，用户id：" + recommendPrize.getUserId(), null);
            throw new RuntimeException("未检索到用户推荐星记录，用户id：" + recommendPrize.getUserId());
        }
        
        InviteRecommend recommend = recommends.get(0);
        recommend.setPrizeUsedCount(recommend.getPrizeUsedCount() + recommendCost);
        result = inviteRecommendMapper.updateByPrimaryKey(recommend);
        
        if(result <= 0){
            LogUtil.errorLog(this.getClass().getName(), methodName, "更新奖品剩余数量失败，奖品GroupCode：" + recommendPrize.getPrizeGroup(), null);
            throw new RuntimeException("更新奖品剩余数量失败，奖品GroupCode：" + recommendPrize.getPrizeGroup());
        }
        
        //更新奖品剩余数量
        Map<String,Object> paraMap = new HashMap<String,Object>();
        paraMap.put("prizeGroupCode", recommendPrize.getPrizeGroup());
        paraMap.put("prizeCount", prizeCount);
        result = prizeGetCustomizeMapper.updatePrizeCountUsed(paraMap);
        
        return true;
    }
    
    /**
     * 
     * 发放优惠券
     * @author hsy
     * @param userId
     * @param groupCode
     * @return
     */
    @Override
    public JSONObject sendPrizeCoupon(String userId, String groupCode, int sendCount){
        CommonParamBean paramBean = new CommonParamBean();
        paramBean.setUserId(userId);
        paramBean.setPrizeGroupCode(groupCode);
        paramBean.setSendCount(sendCount);
        paramBean.setSendFlg(7);
        // 调用发放优惠券接口
        String jsonStr = CommonSoaUtils.sendUserCoupon(paramBean);
        JSONObject sendResult = JSONObject.parseObject(jsonStr);

        return sendResult;
    }
    
    
}
