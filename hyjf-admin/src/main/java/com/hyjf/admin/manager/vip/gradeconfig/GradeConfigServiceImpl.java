package com.hyjf.admin.manager.vip.gradeconfig;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.auto.ParamNameExample;
import com.hyjf.mybatis.model.auto.VipInfo;
import com.hyjf.mybatis.model.auto.VipInfoExample;

@Service
public class GradeConfigServiceImpl extends BaseServiceImpl implements GradeConfigService {
   

    /**
     * 获取列表列表
     * 
     * @return
     */
    public List<VipInfo> getRecordList(GradeConfigBean from, int limitStart, int limitEnd) {
        
        VipInfoExample example=new VipInfoExample();

        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        example.createCriteria().andDelFlgEqualTo(CustomConstants.FALG_NOR);
        example.setOrderByClause("vip_level");
        return vipInfoMapper.selectByExample(example);
    }

    /**
     * 获取单个维护
     * 
     * @return
     */
    public VipInfo getRecord(Integer record) {
        VipInfo vipInfo= vipInfoMapper.selectByPrimaryKey(record);
        return vipInfo;
    }



    /**
     * 维护插入
     * 
     * @param record
     */
    public void insertRecord(GradeConfigBean record) {
        
        
        ParamName paramName=getVipParamName(record.getVipLevel()+"");
        
        record.setAddTime(GetDate.getNowTime10());
        record.setUpdateTime(GetDate.getNowTime10());
        record.setDelFlg(CustomConstants.FALG_NOR);
        record.setVipLevel(new Integer(paramName.getNameCd()));
        record.setVipName(paramName.getName());
        vipInfoMapper.insertSelective(record);
    }
    

    /**
     * 维护更新
     * 
     * @param record
     */
    public void updateRecord(GradeConfigBean record) {
        ParamName paramName=getVipParamName(record.getVipLevel()+"");
        record.setUpdateTime(GetDate.getNowTime10());
        record.setVipLevel(new Integer(paramName.getNameCd()));
        record.setVipName(paramName.getName());
        vipInfoMapper.updateByPrimaryKeySelective(record);
    }



    @Override
    public int getVipInfoByVipLevel(GradeConfigBean record) {
        VipInfoExample example=new VipInfoExample();
        if(record.getId()!=null&&record.getId()!=0){
            example.createCriteria().andDelFlgEqualTo(CustomConstants.FALG_NOR).
            andVipLevelEqualTo(record.getVipLevel()).andIdNotEqualTo(record.getId());
        }else{
            example.createCriteria().andDelFlgEqualTo(CustomConstants.FALG_NOR).
            andVipLevelEqualTo(record.getVipLevel()); 
        }
        return vipInfoMapper.selectByExample(example).size();
    }
    
    private ParamName getVipParamName(String vipLevel) {
        ParamNameExample example = new ParamNameExample();
        ParamNameExample.Criteria cra = example.createCriteria();
        cra.andNameClassEqualTo("VIP_GRADE");
        cra.andDelFlagEqualTo(CustomConstants.FLAG_NORMAL);
        cra.andNameCdEqualTo(vipLevel);
        List<ParamName> list=paramNameMapper.selectByExample(example);
        return list.get(0);
    }
}
