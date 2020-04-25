package com.hyjf.admin.manager.user.msp;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.mapper.auto.MspAbnormalCreditDetailMapper;
import com.hyjf.mybatis.mapper.auto.MspAbnormalCreditMapper;
import com.hyjf.mybatis.mapper.auto.MspApplyDetailsMapper;
import com.hyjf.mybatis.mapper.auto.MspBlackDataMapper;
import com.hyjf.mybatis.mapper.auto.MspFqzMapper;
import com.hyjf.mybatis.mapper.auto.MspNormalCreditDetailMapper;
import com.hyjf.mybatis.mapper.auto.MspQueryDetailMapper;
import com.hyjf.mybatis.mapper.auto.MspTitleMapper;
import com.hyjf.mybatis.model.auto.MspAbnormalCredit;
import com.hyjf.mybatis.model.auto.MspAbnormalCreditDetail;
import com.hyjf.mybatis.model.auto.MspAbnormalCreditDetailExample;
import com.hyjf.mybatis.model.auto.MspAbnormalCreditExample;
import com.hyjf.mybatis.model.auto.MspAnliInfos;
import com.hyjf.mybatis.model.auto.MspAnliInfosExample;
import com.hyjf.mybatis.model.auto.MspApply;
import com.hyjf.mybatis.model.auto.MspApplyDetails;
import com.hyjf.mybatis.model.auto.MspApplyDetailsExample;
import com.hyjf.mybatis.model.auto.MspApplyExample;
import com.hyjf.mybatis.model.auto.MspBlackData;
import com.hyjf.mybatis.model.auto.MspBlackDataExample;
import com.hyjf.mybatis.model.auto.MspConfigure;
import com.hyjf.mybatis.model.auto.MspConfigureExample;
import com.hyjf.mybatis.model.auto.MspFqz;
import com.hyjf.mybatis.model.auto.MspFqzExample;
import com.hyjf.mybatis.model.auto.MspNormalCreditDetail;
import com.hyjf.mybatis.model.auto.MspNormalCreditDetailExample;
import com.hyjf.mybatis.model.auto.MspQueryDetail;
import com.hyjf.mybatis.model.auto.MspQueryDetailExample;
import com.hyjf.mybatis.model.auto.MspRegion;
import com.hyjf.mybatis.model.auto.MspRegionExample;
import com.hyjf.mybatis.model.auto.MspShixinInfos;
import com.hyjf.mybatis.model.auto.MspShixinInfosExample;
import com.hyjf.mybatis.model.auto.MspTitle;
import com.hyjf.mybatis.model.auto.MspTitleExample;
import com.hyjf.mybatis.model.auto.MspZhixingInfos;
import com.hyjf.mybatis.model.auto.MspZhixingInfosExample;

@Service
public class MspApplyServiceImpl extends BaseServiceImpl implements MspApplyService {
   

    /**
     * 获取列表列表
     * 
     * @return
     */
    public List<MspApply> getRecordList(MspApply mspApply, int limitStart, int limitEnd,int createStart,int createEnd) {
    	MspApplyExample example = new MspApplyExample();
    	example.setOrderByClause(" id desc");
        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        com.hyjf.mybatis.model.auto.MspApplyExample.Criteria criteria = example.createCriteria();
        // 条件查询
        if (StringUtils.isNotEmpty(mspApply.getName())) {
            criteria.andNameEqualTo(mspApply.getName());
        }
        if (StringUtils.isNotEmpty(mspApply.getCreateUser())) {
            criteria.andCreateUserEqualTo(mspApply.getCreateUser());
        }
        if (StringUtils.isNotEmpty(mspApply.getIdentityCard())) {
            criteria.andIdentityCardEqualTo(mspApply.getIdentityCard());
        }if (createStart !=0||createEnd!=0) {
        	criteria.andCreateTimeBetween(createStart, createEnd);
        }
        
        return mspApplyMapper.selectByExample(example);
    }

    /**
     * 获取单个维护
     * 
     * @return
     */
    public MspApply getRecord(Integer record) {
    	MspApply FeeConfig = mspApplyMapper.selectByPrimaryKey(record);
        return FeeConfig;
    }

    /**
     * 根据主键判断维护中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(MspApply record) {
        if (record.getId() == null) {
            return false;
        }
//        BankConfigExample example = new BankConfigExample();
//        BankConfigExample.Criteria cra = example.createCriteria();
//        cra.andIdEqualTo(record.getId());
//        List<BankConfig> bankConfigs = bankConfigMapper.selectByExample(example);
//        if (bankConfigs != null && bankConfigs.size() > 0) {
//            return true;
//        }
        return false;
    }

    /**
     * 维护插入
     * 
     * @param record
     */
    public void insertRecord(MspApply record) {
        mspApplyMapper.insertSelective(record);
    }

    /**
     * 维护更新
     * 
     * @param record
     */
    public void updateRecord(MspApply record) {
        record.setUpdateTime(1);
//        record.setLogo("1");
        mspApplyMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据主键删除
     * 
     * @param recordList
     */
    @Override
    public void deleteRecord(List<Integer> recordList) {
        for (Integer id : recordList) {
        	mspApplyMapper.deleteByPrimaryKey(id);
        }
    }


	/**
	 * 获取城市列表 
	 * @return
	 * @author 
	 */
	@Override
	public List<MspRegion> getRegionList() {
	
		return mspRegionMapper.selectByExample(new MspRegionExample());
	}

	@Override
	public List<MspConfigure> getConfigureList() {
	
		return mspConfigureMapperAuto.selectByExample(new MspConfigureExample());
	}

	@Override
	public MspConfigure getConfigure(int id) {
	
		return mspConfigureMapperAuto.selectByPrimaryKey(id);
	}

	@Override
	public MspFqz getFqz(String applyId) {
	
		MspFqzExample me=new MspFqzExample();
		me.or().andApplyIdEqualTo(applyId);
		return  mspFqzMapper.selectByExample(me).get(0);
	}

	@Override
	public List<MspAnliInfos> getAnliInfos(String applyId) {
		MspAnliInfosExample me=new MspAnliInfosExample();
		me.or().andApplyIdEqualTo(applyId);
		return mspAnliInfosMapper.selectByExample(me);
	}

	@Override
	public List<MspShixinInfos> getShixinInfos(String applyId) {
		MspShixinInfosExample me =new MspShixinInfosExample();
		me.or().andApplyIdEqualTo(applyId);
		return mspShixinInfosMapper.selectByExample(me);
	}

	@Override
	public List<MspZhixingInfos> getZhixingInfos(String applyId) {
		MspZhixingInfosExample me =new MspZhixingInfosExample();
		me.or().andApplyIdEqualTo(applyId);
		return mspZhixingInfosMapper.selectByExample(me);
	}

	
	public MspTitle getTitle(String applyId) {
		MspTitleExample me =new MspTitleExample();
		me.or().andApplyIdEqualTo(applyId);
		return mspTitleMapper.selectByExample(me).get(0);
	}

	@Override
	public List<MspNormalCreditDetail> getNormalCreditDetail(String applyId) {
		MspNormalCreditDetailExample me =new MspNormalCreditDetailExample();
		me.or().andApplyIdEqualTo(applyId);
		return mspNormalCreditDetailMapper.selectByExample(me);
	}

	@Override
	public List<MspApplyDetails> getApplyDetails(String applyId) {
		MspApplyDetailsExample me =new MspApplyDetailsExample();
		me.or().andApplyIdEqualTo(applyId);
		return mspApplyDetailsMapper.selectByExample(me);
	}

	@Override
	public List<MspQueryDetail> getQueryDetail(String applyId) {
		MspQueryDetailExample me =new MspQueryDetailExample();
		me.or().andApplyIdEqualTo(applyId);
		return mspQueryDetailMapper.selectByExample(me);
	}

	@Override
	public List<MspBlackData> getBlackData(String applyId) {
		MspBlackDataExample me =new MspBlackDataExample();
		me.or().andApplyIdEqualTo(applyId);
		return mspBlackDataMapper.selectByExample(me);
	}

	@Override
	public List<MspAbnormalCreditDetail> getAbnormalCreditDetail(String applyId) {
		MspAbnormalCreditDetailExample me =new MspAbnormalCreditDetailExample();
		me.or().andAbcdIdEqualTo(applyId);
		return mspAbnormalCreditDetailMapper.selectByExample(me);
	}

	@Override
	public List<MspAbnormalCredit> getAbnormalCredit(String applyId) {
		MspAbnormalCreditExample me =new MspAbnormalCreditExample();
		me.or().andApplyIdEqualTo(applyId);
		return mspAbnormalCreditMapper.selectByExample(me);
	}



}
