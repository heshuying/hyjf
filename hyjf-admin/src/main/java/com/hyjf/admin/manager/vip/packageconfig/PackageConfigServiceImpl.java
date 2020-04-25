package com.hyjf.admin.manager.vip.packageconfig;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.VipAuthExample;
import com.hyjf.mybatis.model.customize.admin.VipAuthCustomize;

@Service
public class PackageConfigServiceImpl extends BaseServiceImpl implements PackageConfigService {
   

    /**
     * 获取列表列表
     * 
     * @return
     */
    public List<VipAuthCustomize> getRecordList(VipAuthCustomize vipAuthCustomize) {
        return vipAuthCustomizeMapper.getRecordList(vipAuthCustomize);
    }

    /**
     * 获取单个维护
     * 
     * @return
     */
    public VipAuthCustomize getRecord(Integer record) {
        VipAuthCustomize vipAuthCustomize= vipAuthCustomizeMapper.selectByPrimaryKey(record);
        return vipAuthCustomize;
    }

    @Override
    public Integer countRecord(PackageConfigBean form) {
        VipAuthExample example=new VipAuthExample();
        example.createCriteria().andDelFlgEqualTo(CustomConstants.FALG_NOR);
        return vipAuthMapper.selectByExample(example).size();
    }



   /**
     * 维护插入
     * 
     * @param record
     */
    public void insertRecord(PackageConfigBean record) {
        
        record.setAddTime(GetDate.getNowTime10());
        record.setUpdateTime(GetDate.getNowTime10());
        record.setDelFlg(CustomConstants.FALG_NOR);
        vipAuthMapper.insertSelective(record);
    }
    /**
     * 维护更新
     * 
     * @param record
     */
    public void updateRecord(PackageConfigBean record) {
        record.setUpdateTime(GetDate.getNowTime10());
//        record.setVipName("VIP"+record.getVipLevel());
        vipAuthMapper.updateByPrimaryKeySelective(record);
    }



@Override
public int getVipAuthByIdAndCode(Integer id,Integer vipId, String couponCode) {
    
    VipAuthExample example=new VipAuthExample();
    example.createCriteria().andDelFlgEqualTo(CustomConstants.FALG_NOR).
        andVipIdEqualTo(vipId).andCouponCodeEqualTo(couponCode);
    
    if(id!=null&&id!=0){
        example=new VipAuthExample();
        example.createCriteria().andDelFlgEqualTo(CustomConstants.FALG_NOR).
        andVipIdEqualTo(vipId).andCouponCodeEqualTo(couponCode).andIdNotEqualTo(id); 
    }
    return vipAuthMapper.selectByExample(example).size();
}
    
    
}
