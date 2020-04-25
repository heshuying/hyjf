package com.hyjf.admin.manager.user.preregistcea;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.PreRegistChannelExclusiveActivity;
import com.hyjf.mybatis.model.auto.PreRegistChannelExclusiveActivityExample;
import com.hyjf.mybatis.model.customize.admin.AdminPreRegistChannelExclusiveActivityCustomize;

@Service
public class PreRegistChannelExclusiveActivityServiceImpl extends BaseServiceImpl implements PreRegistChannelExclusiveActivityService {

    /**
     * 初始化预注册数据
     * map参数需要传入preRegChannelExclusiveActivityTimeStart,preRegChannelExclusiveActivityTimeEnd两个时间
     * @param form
     * @return
     */
    @Override
    public List<AdminPreRegistChannelExclusiveActivityCustomize> iniPreRegistChannelExclusiveActivity(Map<String, Object> map){
        return adminPreRegistChannelExclusiveActivityCustomizeMapper.iniPreRegistChannelExclusiveActivity(map);
    }
    
    /**
     * 初始化预注册数据更新 表数据
     * @param form
     * @return
     */
    @Override
    public void iniUpdatePreRegistChannelExclusiveActivity(List<AdminPreRegistChannelExclusiveActivityCustomize> preRegistChannelExclusiveActivityList){
        if(preRegistChannelExclusiveActivityList!=null && preRegistChannelExclusiveActivityList.size()>0){
            for(AdminPreRegistChannelExclusiveActivityCustomize preRegistChannelExclusiveActivity : preRegistChannelExclusiveActivityList){
                if(new BigDecimal(preRegistChannelExclusiveActivity.getTenderSingle()).compareTo(BigDecimal.valueOf(50000))>=0){
                    preRegistChannelExclusiveActivity.setReward("上海迪士尼门票");
                }else if(new BigDecimal(preRegistChannelExclusiveActivity.getTenderSingle()).compareTo(BigDecimal.valueOf(50000))<0 && new BigDecimal(preRegistChannelExclusiveActivity.getTenderSingle()).compareTo(BigDecimal.valueOf(10000))>=0){
                    preRegistChannelExclusiveActivity.setReward("50元话费");
                }else{
                    preRegistChannelExclusiveActivity.setReward(null);
                }
                PreRegistChannelExclusiveActivityExample example = new PreRegistChannelExclusiveActivityExample();
                PreRegistChannelExclusiveActivityExample.Criteria cra = example.createCriteria();
                cra.andMobileEqualTo(preRegistChannelExclusiveActivity.getMobile());
                List<PreRegistChannelExclusiveActivity> preRegistList = preRegistChannelExclusiveActivityMapper.selectByExample(example);
                //奖励已经记录时更新操作
                if(preRegistList!=null && preRegistList.size()>0){
                    PreRegistChannelExclusiveActivity preRegist = preRegistList.get(0);
                    preRegist.setTenderTotal(new BigDecimal(preRegistChannelExclusiveActivity.getTenderTotal()));
                    preRegist.setTenderSingle(new BigDecimal(preRegistChannelExclusiveActivity.getTenderSingle()));
                    preRegist.setReward(preRegistChannelExclusiveActivity.getReward());
                    preRegist.setUpdateTime(GetDate.getNowTime10());
                    preRegistChannelExclusiveActivityMapper.updateByPrimaryKey(preRegist);
                }else{
                   //奖励记录不存在进行插入操作
                   PreRegistChannelExclusiveActivity preRegist = new PreRegistChannelExclusiveActivity();
                   preRegist.setUserId(Integer.parseInt(preRegistChannelExclusiveActivity.getUserId()));
                   preRegist.setUsername(preRegistChannelExclusiveActivity.getUserName());
                   preRegist.setMobile(preRegistChannelExclusiveActivity.getMobile());
                   preRegist.setReferrer(preRegistChannelExclusiveActivity.getReferrer()!=null?Integer.parseInt(preRegistChannelExclusiveActivity.getReferrer()):null);
                   preRegist.setReferrerUserName(preRegistChannelExclusiveActivity.getReferrerUserName());
                   preRegist.setPreRegistTime(preRegistChannelExclusiveActivity.getPreRegistTime()!=null?Integer.parseInt(preRegistChannelExclusiveActivity.getPreRegistTime()):null);
                   preRegist.setRegistTime(preRegistChannelExclusiveActivity.getRegistTime()!=null?Integer.parseInt(preRegistChannelExclusiveActivity.getRegistTime()):null);
                   preRegist.setUtmId(preRegistChannelExclusiveActivity.getUtmId()!=null?Integer.parseInt(preRegistChannelExclusiveActivity.getUtmId()):null);
                   preRegist.setUtmTerm(preRegistChannelExclusiveActivity.getUtmTerm());
                   preRegist.setSourceId(preRegistChannelExclusiveActivity.getSourceId()!=null?Integer.parseInt(preRegistChannelExclusiveActivity.getSourceId()):null);
                   preRegist.setUtmSource(preRegistChannelExclusiveActivity.getUtmSource());
                   preRegist.setTenderTotal(new BigDecimal(preRegistChannelExclusiveActivity.getTenderTotal()));
                   preRegist.setTenderSingle(new BigDecimal(preRegistChannelExclusiveActivity.getTenderSingle()));
                   preRegist.setReward(preRegistChannelExclusiveActivity.getReward());
                   preRegist.setRemark(preRegistChannelExclusiveActivity.getRemark());
                   preRegist.setCreateTime(GetDate.getNowTime10());
                   preRegist.setUpdateTime(null);
                   preRegistChannelExclusiveActivityMapper.insertSelective(preRegist);
                }
            }
        }
    }
    
    /**
     * 获取预注册渠道专属活动条数
     * 
     * @param form
     * @return
     * @author Administrator
     */

    @Override
    public int countRecordTotal(Map<String, Object> map) {
        return adminPreRegistChannelExclusiveActivityCustomizeMapper.countRecordTotal(map);

    }
    
    /**
     * 获取预注册渠道专属活动数据列表
     * 
     * @return
     */
    public List<AdminPreRegistChannelExclusiveActivityCustomize> getRecordList(Map<String, Object> map, int limitStart, int limitEnd) {
        // 封装查询条件
        if (limitStart == 0 || limitStart > 0) {
            map.put("limitStart", limitStart);
        }
        if (limitEnd > 0) {
            map.put("limitEnd", limitEnd);
        }
        List<AdminPreRegistChannelExclusiveActivityCustomize> preRegistChannelExclusiveActivity = adminPreRegistChannelExclusiveActivityCustomizeMapper.selectRecordList(map);
        return preRegistChannelExclusiveActivity;
    }
}
