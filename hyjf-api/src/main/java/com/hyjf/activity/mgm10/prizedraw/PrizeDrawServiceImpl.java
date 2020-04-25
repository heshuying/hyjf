package com.hyjf.activity.mgm10.prizedraw;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.PropertiesConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.InviteRecommend;
import com.hyjf.mybatis.model.auto.InviteRecommendExample;
import com.hyjf.mybatis.model.auto.InviteRecommendPrize;
import com.hyjf.mybatis.model.customize.admin.PrizeGetCustomize;
import com.hyjf.soa.apiweb.CommonParamBean;
import com.hyjf.soa.apiweb.CommonSoaUtils;

@Service("prizeDrawService")
public class PrizeDrawServiceImpl extends BaseServiceImpl implements PrizeDrawService {

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
     * 获取已中奖的用户列表
     * @author hsy
     * @return
     */
    @Override
    public List<Map<String,Object>> getPrizeWinList(){
        Map<String,Object> paraMap = new HashMap<String,Object>();
        
        List<Map<String,Object>> winList =  prizeGetCustomizeMapper.selectPrizeWinList(paraMap);
        if(winList == null){
            winList = new ArrayList<Map<String,Object>>();
        }
        
        List<Map<String,Object>> initData = getInitData();
        winList.addAll(initData);
        
        return winList;
    }
    
    /**
     * 
     * 假数据初始化
     * @author hsy
     * @return
     */
    public List<Map<String,Object>> getInitData(){
        List<Map<String,Object>> initData = new ArrayList<Map<String,Object>>();
        Map<String,Object> data1 = (Map<String,Object>)new HashMap<String,Object>();
        data1.put("userName", "李***x"); data1.put("prizeName", "10元代金券");
        initData.add(data1);
        Map<String,Object> data2 = (Map<String,Object>)new HashMap<String,Object>();
        data2.put("userName", "h***1"); data2.put("prizeName", "1.2%加息券");
        initData.add(data2);
        Map<String,Object> data3 = (Map<String,Object>)new HashMap<String,Object>();
        data3.put("userName", "z***h"); data3.put("prizeName", "小米移动电源");
        initData.add(data3);
        Map<String,Object> data4 = (Map<String,Object>)new HashMap<String,Object>();
        data4.put("userName", "h***2"); data4.put("prizeName", "50元话费");
        initData.add(data4);
        Map<String,Object> data5 = (Map<String,Object>)new HashMap<String,Object>();
        data5.put("userName", "h***9"); data5.put("prizeName", "1.5%加息券");
        initData.add(data5);
        Map<String,Object> data6 = (Map<String,Object>)new HashMap<String,Object>();
        data6.put("userName", "张***一"); data6.put("prizeName", "10元代金券");
        initData.add(data6);
        Map<String,Object> data7 = (Map<String,Object>)new HashMap<String,Object>();
        data7.put("userName", "h***7"); data7.put("prizeName", "20元代金券");
        initData.add(data7);
        Map<String,Object> data8 = (Map<String,Object>)new HashMap<String,Object>();
        data8.put("userName", "H***睿"); data8.put("prizeName", "1.5%加息券");
        initData.add(data8);
        Map<String,Object> data9 = (Map<String,Object>)new HashMap<String,Object>();
        data9.put("userName", "h***1"); data9.put("prizeName", "1%加息券");
        initData.add(data9);
        Map<String,Object> data10 = (Map<String,Object>)new HashMap<String,Object>();
        data10.put("userName", "Y***Y"); data10.put("prizeName", "小米扫地机器人");
        initData.add(data10);
        Map<String,Object> data11 = (Map<String,Object>)new HashMap<String,Object>();
        data11.put("userName", "孙***样"); data11.put("prizeName", "20元代金券");
        initData.add(data11);
        Map<String,Object> data12 = (Map<String,Object>)new HashMap<String,Object>();
        data12.put("userName", "赵***L"); data12.put("prizeName", "1.2%加息券");
        initData.add(data12);
        Map<String,Object> data13 = (Map<String,Object>)new HashMap<String,Object>();
        data13.put("userName", "h***8");  data13.put("prizeName", "10元代金券");
        initData.add(data13);
        Map<String,Object> data14 = (Map<String,Object>)new HashMap<String,Object>();
        data14.put("userName", "王***丹"); data14.put("prizeName", "1.5%加息券");
        initData.add(data14);
        
        return initData;
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
     * 抽奖算法
     * @author hsy
     * @return 成功返回groupcode 抽奖失败失败返回 0
     */
    @Override
    public String generatePrize(Integer blackUser){
        String methodName = "generatePrize";
        //获取奖品列表
        Map<String,Object> paraMap = new HashMap<String,Object>();
        paraMap.put("prizeKind", CustomConstants.CONF_PRIZE_KIND_DRAW);
        paraMap.put("prizeStatus", CustomConstants.CONF_PRIZE_STATUS_ON);
        List<PrizeGetCustomize> prizeList = prizeGetCustomizeMapper.selectPrizeConfList(paraMap);
        
        //奖品中奖几率校验
        BigDecimal probabilityCheck = BigDecimal.ZERO;
        for(PrizeGetCustomize prize : prizeList){
            probabilityCheck = probabilityCheck.add(prize.getPrizeProbability());
        }
        if(probabilityCheck.compareTo(new BigDecimal(100)) != 0){
            LogUtil.errorLog(this.getClass().getName(), methodName, "抽奖失败，所有奖品的中奖几率之和不为1", null);
            return "0";
        }
        
        //生成奖品的中奖区间
        int rangeStart = 0;
        int rangeEnd = 0;
        BigDecimal probabilitySum = BigDecimal.ZERO;
        List<PrizeGenerateBean> prizeBeans = new ArrayList<PrizeGenerateBean>();
        for(PrizeGetCustomize prize : prizeList){
            probabilitySum = probabilitySum.add(prize.getPrizeProbability());
            rangeStart = (probabilitySum.subtract(prize.getPrizeProbability())).multiply(new BigDecimal(PrizeGenerateBean.RANGE_BASE_NUM)).intValue();
            rangeEnd = probabilitySum.multiply(new BigDecimal(PrizeGenerateBean.RANGE_BASE_NUM)).intValue()-1;
            PrizeGenerateBean prizeBean = new PrizeGenerateBean();
            prizeBean.setGroupCode(prize.getPrizeGroupCode());
            prizeBean.setPrizeProbability(prize.getPrizeProbability());
            prizeBean.setPrizeRemaindCount(prize.getPrizeReminderQuantity());
            prizeBean.setPrizeType(prize.getPrizeType());
            prizeBean.setRangeStart(rangeStart);
            prizeBean.setRangeEnd(rangeEnd);
            
            prizeBeans.add(prizeBean);
        }
        
        if(rangeEnd != PrizeGenerateBean.RANGE_BASE_NUM*100 - 1){
            LogUtil.errorLog(this.getClass().getName(), methodName, "抽奖失败，中奖区间生成失败", null);
            return "0";
        }
        
        // 生成中奖随机数
        Random random = new Random();
        int genResult = random.nextInt(PrizeGenerateBean.RANGE_BASE_NUM*100 - 1);
        System.out.println("生成的中奖随机数：" + genResult);
        
        // 查找对应的奖品分组编号
        for(PrizeGenerateBean bean : prizeBeans){
            if(genResult >= bean.getRangeStart() && genResult <= bean.getRangeEnd()){
                if(Validator.isNull(bean.getPrizeRemaindCount()) || bean.getPrizeRemaindCount() <= 0){
                    System.out.println("中奖随机数 " + genResult + " 对应的奖品分组编号为："  + bean.getGroupCode());
                    System.out.println("奖品已经抽完，开始重新抽奖...");
                    break;
                }
                if(blackUser==1 && bean.getPrizeType().equals("1")){
                    System.out.println("中奖随机数 " + genResult + " 对应的奖品分组编号为："  + bean.getGroupCode());
                    System.out.println("用户是黑名单用户不能抽中实物奖品，开始重新抽奖...");
                    break;
                }
                LogUtil.infoLog(this.getClass().getName(), methodName, "中奖随机数 " + genResult + " 对应的奖品分组编号为："  + bean.getGroupCode());
                System.out.println("中奖随机数 " + genResult + " 对应的奖品分组编号为："  + bean.getGroupCode());
                return bean.getGroupCode();
            }
        }
        
        return "0";
        
    }
    /**
     * 
     * 抽奖数据表操作
     * @author hsy
     * @param recommendPrize
     * @return
     */
    @Override
    public boolean insertPrizeDraw(InviteRecommendPrize recommendPrize){
        String methodName = "insertPrizeDraw";
        if(Validator.isNull(recommendPrize.getUserId()) || Validator.isNull(recommendPrize.getPrizeGroup())){
            LogUtil.errorLog(this.getClass().getName(), methodName, null);
            return false;
        }
        
        //插入推荐星兑奖表
        recommendPrize.setAddTime(GetDate.getNowTime10());
        recommendPrize.setUpdateTime(GetDate.getNowTime10());
        recommendPrize.setDelFlg(CustomConstants.FALG_NOR);
        recommendPrize.setPrizeKind(Integer.parseInt(CustomConstants.CONF_PRIZE_KIND_DRAW));
        if(recommendPrize.getPrizeType().equals(Integer.parseInt(CustomConstants.CONF_PRIZE_TYPE_ENTITY))){
            //实物奖品更新为人工发放
            recommendPrize.setPrizeSendFlag(Integer.parseInt(CustomConstants.PRIZE_SEND_FLAG_MANUAL));
        }else{
            recommendPrize.setPrizeSendFlag(Integer.parseInt(CustomConstants.PRIZE_SEND_FLAG_YES));
        }

        int result = inviteRecommendPrizeMapper.insertSelective(recommendPrize);
        
        if(result <= 0){
            throw new RuntimeException("更新用户剩余推荐星失败，用户id：" + recommendPrize.getUserId());
        }
        
        //更新用户推荐星表
        InviteRecommendExample example = new InviteRecommendExample();
        example.createCriteria().andUserIdEqualTo(recommendPrize.getUserId());
        List<InviteRecommend> recommends = inviteRecommendMapper.selectByExample(example);
        if(recommends == null || recommends.isEmpty()){
            LogUtil.errorLog(this.getClass().getName(), methodName, null);
            throw new RuntimeException("未检索到用户推荐星记录，用户id：" + recommendPrize.getUserId());
        }
        
        Integer needCount = Integer.parseInt(PropUtils.getSystem(PropertiesConstants.PRIZE_DRAW_RECOMMEND_COUNT));
        InviteRecommend recommend = recommends.get(0);
        recommend.setPrizeUsedCount(recommend.getPrizeUsedCount() + needCount);
        result = inviteRecommendMapper.updateByPrimaryKey(recommend);
        
        if(result <= 0){
            throw new RuntimeException("更新奖品剩余数量失败，奖品GroupCode：" + recommendPrize.getPrizeGroup());
        }
        
        //更新奖品剩余数量
        Map<String,Object> paraMap = new HashMap<String,Object>();
        paraMap.put("prizeCount", 1);
        paraMap.put("prizeGroupCode", recommendPrize.getPrizeGroup());
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
