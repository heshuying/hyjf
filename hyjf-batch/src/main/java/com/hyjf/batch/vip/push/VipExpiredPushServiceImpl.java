package com.hyjf.batch.vip.push;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.messageprocesser.AppMsMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.auto.VipInfo;

/**
 * 
 * 会员过期发送push消息
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年8月22日
 * @see 上午11:43:23
 */
@Service
public class VipExpiredPushServiceImpl extends BaseServiceImpl implements VipExpiredPushService {

    private static final String THIS_CLASS = VipExpiredPushServiceImpl.class.getName();
    
    @Autowired
    @Qualifier("appMsProcesser")
    private MessageProcesser appMsProcesser;
    
    /**
     * 检查并发送push消息
     */
	@Override
	public void sendExpiredMsgAct() {
		String methodName = "sendExpiredMsgAct";
		LogUtil.startLog(THIS_CLASS, methodName, "会员即将过期push消息提醒开始");
		
		//yyyy-MM-dd 的时间戳
		int nowBeginDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf));
		int nowEndDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf, 1));
		
		//两天后
		int towBeginDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf, 2));
        int towEndDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf, 3));
        
        //三天后
        int threeBeginDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf, 3));
        int threeEndDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf, 4));
        
        //七天后
        int sevenBeginDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf, 7));
        int sevenEndDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf, 8));
        
        //前一天
        int yestodayBeginDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf, -1));
        int yestodayEndDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf));
		
		// 取得体验金出借（无真实出借）的还款列表
		UsersInfoExample example = new UsersInfoExample();
		UsersInfoExample.Criteria criteria = example.createCriteria();
		criteria.andVipIdIsNotNull();
		criteria.andVipExpDateGreaterThanOrEqualTo(nowBeginDate);
		criteria.andVipExpDateLessThan(nowEndDate);
		
		UsersInfoExample.Criteria criteria2 = example.createCriteria();
		criteria2.andVipIdIsNotNull();
        criteria2.andVipExpDateGreaterThanOrEqualTo(towBeginDate);
        criteria2.andVipExpDateLessThan(towEndDate);
        example.or(criteria2);
        
        UsersInfoExample.Criteria criteria3 = example.createCriteria();
        criteria3.andVipIdIsNotNull();
        criteria3.andVipExpDateGreaterThanOrEqualTo(threeBeginDate);
        criteria3.andVipExpDateLessThan(threeEndDate);
        example.or(criteria3);
        
        UsersInfoExample.Criteria criteria7 = example.createCriteria();
        criteria7.andVipIdIsNotNull();
        criteria7.andVipExpDateGreaterThanOrEqualTo(sevenBeginDate);
        criteria7.andVipExpDateLessThan(sevenEndDate);
        example.or(criteria7);
        
        List<UsersInfo> userInfoList = usersInfoMapper.selectByExample(example);
        
        for(UsersInfo userInfo: userInfoList){
            VipInfo info = vipInfoMapper.selectByPrimaryKey(userInfo.getVipId());
            if(info == null){
                LogUtil.errorLog(this.getClass().getName(), methodName, "根据会员等级id没有查询到对应的等级信息，id：" + userInfo.getVipId(), null);
                continue;
            }
            
            LogUtil.infoLog(this.getClass().getName(), methodName, "即将过期的会员真实用户名：" + userInfo.getTruename() + " 有效期截止日： " + GetDate.formatDate(Long.parseLong(userInfo.getVipExpDate()+"000")));
            
            
            //发送push消息
            Map<String, String> param = new HashMap<String, String>();
            if(StringUtils.isEmpty(userInfo.getTruename())){
                param.put("val_name", "");
            }else if(userInfo.getTruename().length() > 1){
                param.put("val_name", userInfo.getTruename().substring(0, 1));
            }else{
                param.put("val_name", userInfo.getTruename());
            }
            
            if(userInfo.getSex() == 1){
                param.put("val_sex", "先生");
            }else if(userInfo.getSex() == 2){
                param.put("val_sex", "女士");
            }else{
                param.put("val_sex", "");
            }
            param.put("val_vip_grade", info.getVipName());
            param.put("val_vip_deadline", GetDate.formatDate(Long.parseLong(userInfo.getVipExpDate()+"000")));
            AppMsMessage appMsMessage = new AppMsMessage(userInfo.getUserId(), param, null, MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_VIP_DEADLINE);
            appMsProcesser.gather(appMsMessage);
        }
        
        //昨天过期的
        UsersInfoExample exampleY = new UsersInfoExample();
        UsersInfoExample.Criteria criteriaY = exampleY.createCriteria();
        criteriaY.andVipIdIsNotNull();
        criteriaY.andVipExpDateGreaterThanOrEqualTo(yestodayBeginDate);
        criteriaY.andVipExpDateLessThan(yestodayEndDate);
        List<UsersInfo> userInfoListExp = usersInfoMapper.selectByExample(exampleY);
        for(UsersInfo userInfo: userInfoListExp){
            VipInfo info = vipInfoMapper.selectByPrimaryKey(userInfo.getVipId());
            if(info == null){
                LogUtil.errorLog(this.getClass().getName(), methodName, "根据会员等级id没有查询到对应的等级信息，id：" + userInfo.getVipId(), null);
                continue;
            }
            
            LogUtil.infoLog(this.getClass().getName(), methodName, "昨天过期的会员真实用户名：" + userInfo.getTruename() + " 有效期截止日： " + GetDate.formatDate(Long.parseLong(userInfo.getVipExpDate()+"000")));
            
            //发送push消息
            Map<String, String> param = new HashMap<String, String>();
            if(StringUtils.isEmpty(userInfo.getTruename())){
                param.put("val_name", "");
            }else if(userInfo.getTruename().length() > 1){
                param.put("val_name", userInfo.getTruename().substring(0, 1));
            }else{
                param.put("val_name", userInfo.getTruename());
            }
            
            if(userInfo.getSex() == 1){
                param.put("val_sex", "先生");
            }else if(userInfo.getSex() == 2){
                param.put("val_sex", "女士");
            }else{
                param.put("val_sex", "");
            }
            param.put("val_vip_grade", info.getVipName());
            AppMsMessage appMsMessage = new AppMsMessage(userInfo.getUserId(), param, null, MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_VIP_INVALIDED);
            appMsProcesser.gather(appMsMessage);
        }
        
		LogUtil.endLog(THIS_CLASS, methodName, "会员即将过期push消息提醒结束");
	}

	public static void main(String[] args) {
	    int yestodayBeginDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf, -1));
        int yestodayEndDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf));
        System.out.println(GetDate.formatDate(Long.parseLong(1504281599+"000")));
	    System.out.println(yestodayBeginDate);
	    System.out.println(yestodayEndDate);
    }
    
}
