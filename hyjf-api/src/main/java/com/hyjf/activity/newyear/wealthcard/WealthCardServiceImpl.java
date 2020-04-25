package com.hyjf.activity.newyear.wealthcard;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.CouponConfig;
import com.hyjf.mybatis.model.auto.CouponConfigExample;
import com.hyjf.mybatis.model.auto.NewyearCaisheCardUser;
import com.hyjf.mybatis.model.auto.NewyearCaishenCardQuantity;
import com.hyjf.mybatis.model.auto.NewyearCaishenCardQuantityExample;
import com.hyjf.mybatis.model.auto.NewyearPrizeConfig;
import com.hyjf.mybatis.model.auto.NewyearPrizeConfigExample;
import com.hyjf.mybatis.model.auto.NewyearPrizeUser;
import com.hyjf.mybatis.model.auto.NewyearSendPrize;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.soa.apiweb.CommonParamBean;
import com.hyjf.soa.apiweb.CommonSoaUtils;

@Service
public class WealthCardServiceImpl extends BaseServiceImpl implements WealthCardService{

    /**
     * 
     * 获取用户财神卡数量
     * @author hsy
     * @param userId
     * @return
     */
    @Override
    public NewyearCaishenCardQuantity getUserCardQuantity(String userId){
        NewyearCaishenCardQuantityExample example = new NewyearCaishenCardQuantityExample();
        example.createCriteria().andUserIdEqualTo(Integer.parseInt(userId));
        List<NewyearCaishenCardQuantity> cardQuantity = newyearCaishenCardQuantityMapper.selectByExample(example);
        
        if(cardQuantity == null || cardQuantity.isEmpty()){
            return null;
        }
        
        return cardQuantity.get(0);
    }
    
    /**
     * 
     * 校验是否可以将卡片赠送好友
     * @author hsy
     * @param userId
     * @param cardType
     * @return
     */
    @Override
    public boolean checkCanGive(String userId, int cardType){
        NewyearCaishenCardQuantity cardQuantity = this.getUserCardQuantity(userId);
        if(cardQuantity == null){
            return false;
        }
        
        if(cardType == WealthCardDefine.CARD_TYPE_JIN && cardQuantity.getCardJinQuantity() >0){
            return true;
        }
        if(cardType == WealthCardDefine.CARD_TYPE_JI && cardQuantity.getCardJiQuantity() >0){
            return true;
        }
        if(cardType == WealthCardDefine.CARD_TYPE_NA && cardQuantity.getCardNaQuantity() >0){
            return true;
        }
        if(cardType == WealthCardDefine.CARD_TYPE_FU && cardQuantity.getCardFuQuantity() >0){
            return true;
        }
        
        return false;
    }
    
    /**
     * 
     * 检测是否已经集齐财神卡
     * @author hsy
     * @param userId
     * @return
     */
    @Override
    public boolean checkCanOpenPrize(String userId){
        NewyearCaishenCardQuantity cardQuantity = this.getUserCardQuantity(userId);
        if(cardQuantity == null){
            return false;
        }
        
        if(cardQuantity.getCardJinQuantity()>=1 && cardQuantity.getCardJiQuantity() >=1
                && cardQuantity.getCardNaQuantity()>=1 && cardQuantity.getCardFuQuantity() >=1){
            return  true;
        }
        
        return false;
    }
    
    /**
     * 
     * 根据手机号获取用户详情信息
     * @author hsy
     * @param phoneNum
     * @return
     */
    @Override
    public UsersInfo getUserInfoByPhone(String phoneNum){
        if(StringUtils.isEmpty(phoneNum)){
            return null;
        }
        
        UsersExample uExample = new UsersExample();
        uExample.createCriteria().andMobileEqualTo(phoneNum);
        List<Users> users = usersMapper.selectByExample(uExample);
        
        if(users == null || users.isEmpty()){
            return null;
        }
        
        UsersInfoExample iExample = new UsersInfoExample();
        iExample.createCriteria().andUserIdEqualTo(users.get(0).getUserId());
        List<UsersInfo> userInfoes = usersInfoMapper.selectByExample(iExample);
        
        return userInfoes.get(0);
    }
    
    /**
     * 
     * 卡片发送给好友
     * @author hsy
     * @param userId
     * @param cardType
     * @param phoneNum
     */
    @Override
    public boolean insertCardSend(String userId, int cardType, String phoneNum, Integer cardSendUpdateTime){
        Users userSend = this.getUsers(Integer.parseInt(userId));
        UsersInfo userGet = this.getUserInfoByPhone(phoneNum);
        
        // 加载发送方卡片数据
        NewyearCaishenCardQuantity cardQSend = this.getUserCardQuantity(userId);
        
        // 发送方卡片数量更新
        if(cardType == WealthCardDefine.CARD_TYPE_JIN && cardQSend.getCardJinQuantity() >0){
            cardQSend.setCardJinQuantity(cardQSend.getCardJinQuantity()-1);
        }
        if(cardType == WealthCardDefine.CARD_TYPE_JI && cardQSend.getCardJiQuantity() >0){
            cardQSend.setCardJiQuantity(cardQSend.getCardJiQuantity()-1);
        }
        if(cardType == WealthCardDefine.CARD_TYPE_NA && cardQSend.getCardNaQuantity() >0){
            cardQSend.setCardNaQuantity(cardQSend.getCardNaQuantity()-1);
        }
        if(cardType == WealthCardDefine.CARD_TYPE_FU && cardQSend.getCardFuQuantity() >0){
            cardQSend.setCardFuQuantity(cardQSend.getCardFuQuantity()-1);
        }
        
        if(!cardQSend.getUpdateTime().equals(cardSendUpdateTime)){
            throw new RuntimeException("并发问题，卡片数量已经更新，本次更新失败，UserId：" + userId);
        }
        
        int result = newyearCaishenCardQuantityMapper.updateByPrimaryKeySelective(cardQSend);
        
        if(result != 1){
            throw new RuntimeException("卡片数量更新失败，UserId：" + userId);
        }
        
        // 发送方增加一条使用记录
        NewyearCaisheCardUser cardUserSend = new NewyearCaisheCardUser();
        cardUserSend.setUserId(Integer.parseInt(userId));
        cardUserSend.setCardType(cardType);
        cardUserSend.setOperateType(WealthCardDefine.OPERATE_TYPE_USE);
        cardUserSend.setCardSource(WealthCardDefine.CARD_SOURCE_GIVE);
        cardUserSend.setCardJinQuantity(cardQSend.getCardJinQuantity());
        cardUserSend.setCardJiQuantity(cardQSend.getCardJiQuantity());
        cardUserSend.setCardNaQuantity(cardQSend.getCardNaQuantity());
        cardUserSend.setCardFuQuantity(cardQSend.getCardFuQuantity());
        cardUserSend.setAddTime(GetDate.getNowTime10());
        cardUserSend.setRemark(phoneNum);
        cardUserSend.setDelFlg(0);
        
        result = newyearCaisheCardUserMapper.insertSelective(cardUserSend);
        
        if(result != 1){
            throw new RuntimeException("卡片使用记录增加失败，UserId：" + userId);
        }
        
        // 加载接收方卡片数据
        NewyearCaishenCardQuantity cardQReceive = this.getUserCardQuantity(String.valueOf(userGet.getUserId()));

        // 接收方卡片数量更新
        if(cardQReceive == null){
            // 添加
            cardQReceive = new NewyearCaishenCardQuantity();
            cardQReceive.setUserId(userGet.getUserId());
            cardQReceive.setAddTime(GetDate.getNowTime10());
            cardQReceive.setUpdateTime(GetDate.getNowTime10());
            if(cardType == WealthCardDefine.CARD_TYPE_JIN){
                cardQReceive.setCardJinQuantity(1);
            }
            if(cardType == WealthCardDefine.CARD_TYPE_JI){
                cardQReceive.setCardJiQuantity(1);
            }
            if(cardType == WealthCardDefine.CARD_TYPE_NA){
                cardQReceive.setCardNaQuantity(1);
            }
            if(cardType == WealthCardDefine.CARD_TYPE_FU){
                cardQReceive.setCardFuQuantity(1);
            }
            result = newyearCaishenCardQuantityMapper.insertSelective(cardQReceive);
            
        }else {
            // 更新
            if(cardType == WealthCardDefine.CARD_TYPE_JIN){
                cardQReceive.setCardJinQuantity(cardQReceive.getCardJinQuantity()+1);
            }
            if(cardType == WealthCardDefine.CARD_TYPE_JI){
                cardQReceive.setCardJiQuantity(cardQReceive.getCardJiQuantity()+1);
            }
            if(cardType == WealthCardDefine.CARD_TYPE_NA){
                cardQReceive.setCardNaQuantity(cardQReceive.getCardNaQuantity()+1);
            }
            if(cardType == WealthCardDefine.CARD_TYPE_FU){
                cardQReceive.setCardFuQuantity(cardQReceive.getCardFuQuantity()+1);
            }
            result = newyearCaishenCardQuantityMapper.updateByPrimaryKeySelective(cardQReceive);
            
        }
        
        if(result != 1){
            throw new RuntimeException("接收方卡片数量更新失败，UserId：" + cardQReceive.getUserId());
        }
        
        
        
        // 接收方增加一条获得记录
        NewyearCaisheCardUser cardUserGet = new NewyearCaisheCardUser();
        cardUserGet.setUserId(userGet.getUserId());
        cardUserGet.setCardType(cardType);
        cardUserGet.setOperateType(WealthCardDefine.OPERATE_TYPE_GET);
        cardUserGet.setCardSource(WealthCardDefine.CARD_SOURCE_GET);
        cardUserGet.setCardJinQuantity(cardQReceive.getCardJinQuantity());
        cardUserGet.setCardJiQuantity(cardQReceive.getCardJiQuantity());
        cardUserGet.setCardNaQuantity(cardQReceive.getCardNaQuantity());
        cardUserGet.setCardFuQuantity(cardQReceive.getCardFuQuantity());
        cardUserGet.setAddTime(GetDate.getNowTime10());
        cardUserGet.setRemark(userSend.getMobile());
        cardUserGet.setDelFlg(0);
        
        result = newyearCaisheCardUserMapper.insertSelective(cardUserGet);
        if(result != 1){
            throw new RuntimeException("卡片获得记录增加失败，UserId：" + userGet.getUserId());
        }
        
        return true;
        
    }
    
    /**
     * 
     * 获取奖品配置
     * @author hsy
     * @param prizeId
     * @return
     */
    @Override
    public NewyearPrizeConfig getPrizeById(int prizeId){
        return newyearPrizeConfigMapper.selectByPrimaryKey(prizeId);
    }
    
    /**
     * 
     * 查询优惠券已发行数量
     * @author hsy
     * @param couponCode
     * @return
     */
    public boolean checkIsPublishedOver(String couponCode){
        CouponConfigExample example = new CouponConfigExample();
        example.createCriteria().andCouponCodeEqualTo(couponCode);
        List<CouponConfig> configs = couponConfigMapper.selectByExample(example);
        
        if(configs == null || configs.isEmpty()){
            return true;
        }
        
        int result = couponConfigCustomizeMapper.selectCouponPublishedCount(couponCode);
        
        if(configs.get(0).getCouponQuantity() <= result){
            return true;
        }
        
        return false;
    }
    
    /**
     * 
     * 获取优惠券面值
     * @author hsy
     * @param couponCode
     * @return
     */
    public BigDecimal getCouponQuota(String couponCode){
        CouponConfigExample example = new CouponConfigExample();
        example.createCriteria().andCouponCodeEqualTo(couponCode);
        List<CouponConfig> configs = couponConfigMapper.selectByExample(example);
        
        if(configs == null || configs.isEmpty()){
            return BigDecimal.ZERO;
        }
        
        return configs.get(0).getCouponQuota();
    }
    
    /**
     * 
     * 抽奖算法
     * @author hsy
     * @return
     */
    @Override
    public int generatePrize(){
        String methodName = "generatePrize";
        
        // 奖品列表加载
        NewyearPrizeConfigExample example = new NewyearPrizeConfigExample();
        example.createCriteria().andActivityFlgEqualTo(WealthCardDefine.ACTIVITY_FLG_NEWYEAR_A)
                                .andDelFlgEqualTo(Integer.parseInt(CustomConstants.FLAG_NORMAL));
        List<NewyearPrizeConfig> prizeConfigs = newyearPrizeConfigMapper.selectByExample(example);
        if(prizeConfigs == null || prizeConfigs.isEmpty()){
            return 0;
        }
        
        //奖品中奖几率校验
        BigDecimal probabilityCheck = BigDecimal.ZERO;
        for(NewyearPrizeConfig prize : prizeConfigs){
            probabilityCheck = probabilityCheck.add(prize.getPrizeProbability());
        }
        if(probabilityCheck.compareTo(new BigDecimal(100)) != 0){
            LogUtil.errorLog(this.getClass().getName(), methodName, "抽奖失败，所有奖品的中奖几率之和不为1", null);
            return 0;
        }
        
        int prizeIdMin = 0;
        BigDecimal quotaMin = new BigDecimal(10000000);
        BigDecimal probabilityDie = BigDecimal.ZERO;
        for(NewyearPrizeConfig prize : prizeConfigs){
            if((prize.getPrizeOnline() == 1 && prize.getPrizeQuantity() <= prize.getPrizeDrawedCount())
                           || (StringUtils.isNotEmpty(prize.getPrizeCouponCode()) && this.checkIsPublishedOver(prize.getPrizeCouponCode()))){
                probabilityDie = probabilityDie.add(prize.getPrizeProbability());
                System.out.println("奖品已经不足，奖品名称：" + prize.getPrizeName());
                System.out.println("当前待分配的中奖几率：" + probabilityDie.toString());
            }else if(prize.getPrizeOnline() != 1){
                BigDecimal quota = this.getCouponQuota(prize.getPrizeCouponCode());
                if(quotaMin.compareTo(quota) > 0){
                    quotaMin = quota;
                    prizeIdMin = prize.getId();
                }
            }
            
        }
        
        //生成奖品的中奖区间
        int rangeStart = 0;
        int rangeEnd = 0;
        BigDecimal probabilitySum = BigDecimal.ZERO;
        List<PrizeGenerateBean> prizeBeans = new ArrayList<PrizeGenerateBean>();
        for(NewyearPrizeConfig prize : prizeConfigs){
            if((prize.getPrizeOnline() == 1 && prize.getPrizeQuantity() <= prize.getPrizeDrawedCount())
                    || (StringUtils.isNotEmpty(prize.getPrizeCouponCode()) && this.checkIsPublishedOver(prize.getPrizeCouponCode()))){
                continue;
            }
            
            BigDecimal prizeProbability = prize.getPrizeProbability();
            
            if(probabilityDie.compareTo(BigDecimal.ZERO) > 0 && prize.getId() == prizeIdMin){
                System.out.println("中奖几率重新分配给奖品：" + prize.getPrizeName());
                prizeProbability = prizeProbability.add(probabilityDie);
            }
            
            probabilitySum = probabilitySum.add(prizeProbability);
            rangeStart = (probabilitySum.subtract(prizeProbability)).multiply(new BigDecimal(PrizeGenerateBean.RANGE_BASE_NUM)).intValue();
            rangeEnd = probabilitySum.multiply(new BigDecimal(PrizeGenerateBean.RANGE_BASE_NUM)).intValue()-1;
            PrizeGenerateBean prizeBean = new PrizeGenerateBean();
            prizeBean.setPrizeProbability(prizeProbability);
            prizeBean.setRangeStart(rangeStart);
            prizeBean.setRangeEnd(rangeEnd);
            prizeBean.setPrizeId(prize.getId());
            
            prizeBeans.add(prizeBean);
        }
        
        if(rangeEnd != PrizeGenerateBean.RANGE_BASE_NUM*100 - 1){
            LogUtil.errorLog(this.getClass().getName(), methodName, "抽奖失败，中奖区间生成失败", null);
            return 0;
        }

        // 生成中奖随机数
        Random random = new Random();
        int genResult = random.nextInt(PrizeGenerateBean.RANGE_BASE_NUM*100 - 1);
        System.out.println("生成的中奖码：" + genResult);

        // 查找对应的奖品分组编号
        for(PrizeGenerateBean bean : prizeBeans){
            if(genResult >= bean.getRangeStart() && genResult <= bean.getRangeEnd()){
                LogUtil.infoLog(this.getClass().getName(), methodName, "中奖随机数 " + genResult + " 对应的奖品ID为："  + bean.getPrizeId());
                System.out.println("中奖随机数 " + genResult + " 对应的奖品ID为："  + bean.getPrizeId());
                return bean.getPrizeId();
            }
        }

        return 0;
    }
    
    /**
     * 
     * 中奖处理
     * @author hsy
     * @param userId
     * @param prize
     * @return
     */
    @Override
    public boolean insertPrizeDraw(String userId, NewyearPrizeConfig prize, Integer cardUpdateTime){
        String methodName = "insertPrizeDraw";
        
        // 更新用户财神卡数量
        NewyearCaishenCardQuantity cardQuantity = this.getUserCardQuantity(userId);
        boolean isCanOpen = this.checkCanOpenPrize(userId);
        if(cardQuantity == null || !isCanOpen){
            return false;
        }
        
        // 添加财神卡使用记录
        NewyearCaisheCardUser cardUserSend = new NewyearCaisheCardUser();
        cardUserSend.setUserId(Integer.parseInt(userId));
        cardUserSend.setCardType(WealthCardDefine.CARD_TYPE_JJNF);
        cardUserSend.setOperateType(WealthCardDefine.OPERATE_TYPE_USE);
        cardUserSend.setCardSource(WealthCardDefine.CARD_SOURCE_PRIZEDRAW);
        cardUserSend.setCardJinQuantity(cardQuantity.getCardJinQuantity() -1);
        cardUserSend.setCardJiQuantity(cardQuantity.getCardJiQuantity() -1);
        cardUserSend.setCardNaQuantity(cardQuantity.getCardNaQuantity() -1);
        cardUserSend.setCardFuQuantity(cardQuantity.getCardFuQuantity() -1);
        cardUserSend.setAddTime(GetDate.getNowTime10());
        cardUserSend.setRemark(prize.getPrizeName());
        cardUserSend.setDelFlg(0);
        
        int result = newyearCaisheCardUserMapper.insertSelective(cardUserSend);
        
        if(result != 1){
            throw new RuntimeException("卡片使用记录增加失败，UserId：" + userId);
        }
        
        // 添加到用户中奖表
        NewyearPrizeUser prizeUser = new NewyearPrizeUser();
        prizeUser.setDelFlg(0);
        prizeUser.setDrawTime(GetDate.getNowTime10());
        prizeUser.setPrizeId(prize.getId());
        prizeUser.setUserId(Integer.parseInt(userId));
        
        result = newyearPrizeUserMapper.insertSelective(prizeUser);
        
        if(result != 1){
            throw new RuntimeException("添加中奖几率失败，UserId：" + userId + " prizeId:" + prize.getId());
        }
        
        // 更新奖品配置表
        prize.setPrizeDrawedCount(prize.getPrizeDrawedCount() + 1);
        
        result = newyearPrizeConfigMapper.updateByPrimaryKeySelective(prize);
        
        if(result != 1){
            throw new RuntimeException("更新奖品数量失败，UserId：" + userId + " prizeId:" + prize.getId());
        }
        
        
        // 更新用户财神卡数量
        cardQuantity = this.getUserCardQuantity(userId);
        if(!cardQuantity.getUpdateTime().equals(cardUpdateTime)){
            throw new RuntimeException("并发错误，卡片数量已经被更新，本次更新失败，UserId：" + userId);
        }
        cardQuantity.setUpdateTime(GetDate.getNowTime10());
        cardQuantity.setCardJinQuantity(cardQuantity.getCardJinQuantity()-1);
        cardQuantity.setCardJiQuantity(cardQuantity.getCardJiQuantity()-1);
        cardQuantity.setCardNaQuantity(cardQuantity.getCardNaQuantity()-1);
        cardQuantity.setCardFuQuantity(cardQuantity.getCardFuQuantity()-1);

        result = newyearCaishenCardQuantityMapper.updateByPrimaryKeySelective(cardQuantity);
        
        if(result != 1){
            throw new RuntimeException("卡片数量更新失败，UserId：" + userId);
        }
        
        // 如果是优惠券则调用接口发放优惠券
        if(prize.getPrizeOnline() == 0){
            try {
                boolean sendResult = this.sendPrizeCoupon(userId, 1, prizeUser.getId(), prize.getPrizeCouponCode());
                if(!sendResult){
                    LogUtil.errorLog(this.getClass().getName(), methodName, "发送优惠券失败，优惠券编号：" + prize.getPrizeCouponCode() + " userId: " + userId, null);
                }
            } catch (Exception e) {
                LogUtil.errorLog(this.getClass().getName(), methodName, "发送优惠券失败，优惠券编号：" + prize.getPrizeCouponCode() + " userId: " + userId, null);
                e.printStackTrace();
            }
        }
        
        return true;
        
    }
    
    /**
     * 
     * 优惠券奖品发放
     * @author hsy
     * @param userId
     * @param sendCount
     * @param userPrizeId
     * @param couponCode
     * @return
     */
    public boolean sendPrizeCoupon(String userId, int sendCount, int userPrizeId, String couponCode){
        CouponConfigExample example = new CouponConfigExample();
        example.createCriteria().andCouponCodeEqualTo(couponCode);
        List<CouponConfig> configs = couponConfigMapper.selectByExample(example);
        
        CommonParamBean paramBean = new CommonParamBean();
        paramBean.setUserId(userId);
        paramBean.setSendCount(sendCount);
        paramBean.setCouponCode(couponCode);
        paramBean.setSendFlg(10);
        // 调用发放优惠券接口
        String jsonStr = CommonSoaUtils.sendUserCoupon(paramBean);
        JSONObject sendResult = JSONObject.parseObject(jsonStr);

        
        // 发放是否成功状态
        int sendStatus = sendResult.getIntValue("status");
        
        // 发放优惠券的数量
        sendCount = sendResult.getIntValue("couponCount");
        @SuppressWarnings("unchecked")
        List<String> couponUserCodeList = (List<String>)sendResult.get("retCouponUserCodes");
        if (sendStatus == 0 && sendCount > 0 && !couponUserCodeList.isEmpty()) {
            NewyearSendPrize sendPrize = new NewyearSendPrize();
            sendPrize.setActivityFlg(1);
            sendPrize.setAddTime(GetDate.getNowTime10());
            sendPrize.setCouponCode(couponUserCodeList.get(0));
            sendPrize.setCouponJine(configs.get(0).getCouponQuota().intValue());
            sendPrize.setDelFlg(0);
            sendPrize.setSendStatus(1);
            sendPrize.setUserId(Integer.parseInt(userId));
            sendPrize.setUserPrizeId(userPrizeId);
            newyearSendPrizeMapper.insertSelective(sendPrize);
            
            return true;
        }else{
            NewyearSendPrize sendPrize = new NewyearSendPrize();
            sendPrize.setActivityFlg(1);
            sendPrize.setAddTime(GetDate.getNowTime10());
            sendPrize.setCouponCode("");
            sendPrize.setCouponJine(configs.get(0).getCouponQuota().intValue());
            sendPrize.setDelFlg(0);
            sendPrize.setSendStatus(0);
            sendPrize.setUserId(Integer.parseInt(userId));
            sendPrize.setUserPrizeId(userPrizeId);
            newyearSendPrizeMapper.insertSelective(sendPrize);
            
            return false;
        }
        
    }
    
    /**
     * 
     * 获取卡片名字
     * @author hsy
     * @param cardIdentity
     * @return
     */
    @Override
    public String getCardName(int cardIdentity){
        if(cardIdentity == WealthCardDefine.CARD_TYPE_JIN){
            return "金";
        }else if(cardIdentity == WealthCardDefine.CARD_TYPE_JI){
            return "鸡";
        }else if(cardIdentity == WealthCardDefine.CARD_TYPE_NA){
            return "纳";
        }else if(cardIdentity == WealthCardDefine.CARD_TYPE_FU){
            return "福";
        }
        
        return "";
                
    }
    
}
