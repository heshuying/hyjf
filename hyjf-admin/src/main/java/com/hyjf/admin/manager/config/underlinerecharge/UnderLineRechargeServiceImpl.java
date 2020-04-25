package com.hyjf.admin.manager.config.underlinerecharge;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.UnderLineRecharge;
import com.hyjf.mybatis.model.auto.UnderLineRechargeExample;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnderLineRechargeServiceImpl extends BaseServiceImpl implements UnderLineRechargeService {

    @Override
    public int insertRecord(UnderLineRecharge record) {
        record.setAddTime(GetDate.getNowTime10());
        record.setUpdateTime(GetDate.getNowTime10());
        record.setStatus(0);
        return underLineRechargeMapper.insertSelective(record);
    }

    @Override
    public int underLineRechargeListInfoByCode(String ids, String code) {
        UnderLineRechargeExample example = new UnderLineRechargeExample();
        UnderLineRechargeExample.Criteria cra = example.createCriteria();
        if (StringUtils.isNotEmpty(code)){
            cra.andCodeEqualTo(code);
        }
        return underLineRechargeMapper.countByExample(example);
    }

    @Override
    public int countUnderLineRecharList(UnderLineRechargeBean form) {
        UnderLineRechargeExample underLineRechargeExample = new UnderLineRechargeExample();
        UnderLineRechargeExample.Criteria criteria = underLineRechargeExample.createCriteria();
        if (StringUtils.isNotEmpty(form.getCode())){
            criteria.andCodeEqualTo(form.getCode());
        }
        return underLineRechargeMapper.countByExample(underLineRechargeExample);
    }

    @Override
    public List<UnderLineRecharge> searchUnderLineRechargeList(UnderLineRechargeBean form) {
        UnderLineRechargeExample underLineRechargeExample = new UnderLineRechargeExample();
        UnderLineRechargeExample.Criteria criteria = underLineRechargeExample.createCriteria();

        if (StringUtils.isNotEmpty(form.getCode())){
            criteria.andCodeEqualTo(form.getCode());
        }



        underLineRechargeExample.setOrderByClause("`add_time` DESC");

        return underLineRechargeMapper.selectByExample(underLineRechargeExample);
    }

    /**
     * 根据ID删除指定条目
     * @param recordList
     */
    @Override
    public void deleteUnderLineRecharge(List<Integer> recordList) {
        for (Integer id : recordList){
            underLineRechargeMapper.deleteByPrimaryKey(id);
        }
    }

    /**
     * 根据ID 获取单条记录
     * @param ids
     * @return
     */
    @Override
    public UnderLineRecharge getUnderLineRechargeListInfo(String ids) {
        return underLineRechargeMapper.selectByPrimaryKey(Integer.parseInt(ids));
    }

    @Override
    public int updateUnderLineRechargeListInfo(UnderLineRechargeBean form) {
        UnderLineRecharge underLineRecharge = new UnderLineRecharge();
        BeanUtils.copyProperties(form, underLineRecharge);
        underLineRecharge.setUpdateTime(GetDate.getNowTime10());

        if (StringUtils.isNotEmpty(form.getIds())){
            underLineRecharge.setId(Integer.parseInt(form.getIds()));
        }
        return underLineRechargeMapper.updateByPrimaryKeySelective(underLineRecharge);
    }

    @Override
    public List<UnderLineRecharge> selectUnderLineRechargeList(UnderLineRechargeBean form) {
        UnderLineRechargeExample underLineRechargeExample = new UnderLineRechargeExample();
        UnderLineRechargeExample.Criteria criteria = underLineRechargeExample.createCriteria();

        underLineRechargeExample.setOrderByClause("`add_time` DESC");

        return underLineRechargeMapper.selectByExample(underLineRechargeExample);
    }
}
