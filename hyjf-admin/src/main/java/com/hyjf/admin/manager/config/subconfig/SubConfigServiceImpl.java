package com.hyjf.admin.manager.config.subconfig;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.mybatis.model.auto.BorrowFinmanNewCharge;
import com.hyjf.mybatis.model.auto.BorrowFinmanNewChargeExample;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowProjectTypeExample;
import com.hyjf.mybatis.model.auto.FeerateModifyLog;
import com.hyjf.mybatis.model.auto.FeerateModifyLogExample;
import com.hyjf.mybatis.model.auto.SubCommissionListConfig;
import com.hyjf.mybatis.model.customize.AdminSystem;
import com.hyjf.mybatis.model.customize.BorrowFinmanNewChargeCustomize;
import com.hyjf.mybatis.model.customize.SubCommissionListConfigCustomize;
import com.hyjf.mybatis.model.customize.SubCommissionListConfigCustomizeExample;

/**
 * 
 * 融资管理费Service
 * @author liuyang
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年6月13日
 * @see 上午10:39:37
 */
@Service
public class SubConfigServiceImpl extends BaseServiceImpl implements SubConfigService {

	/**
     * 根据用户名查询分账名单是否存在
     */
	@Override
    public List<SubCommissionListConfigCustomize> subconfig(String username){
//		SubCommissionListConfigCustomizeExample example =new SubCommissionListConfigCustomizeExample();
//		SubCommissionListConfigCustomizeExample.Criteria cra=example.createCriteria();
//		if (StringUtils.isNoneEmpty(username)) {
//			cra.andUsernameEqualTo(username);
//		}
		SubCommissionListConfigCustomize subCommissionListConfigCustomize=new SubCommissionListConfigCustomize();
		subCommissionListConfigCustomize.setUsername(username);
		return this.subCommissionListConfigCustomizeMapper.selectByExampleUsername(subCommissionListConfigCustomize);
    }
	
    /**
     * 项目类型
     * 
     * @return
     * @author Administrator
     */
    @Override
    public List<BorrowProjectType> borrowProjectTypeList(String projectTypeCd) {
        BorrowProjectTypeExample example = new BorrowProjectTypeExample();
        BorrowProjectTypeExample.Criteria cra = example.createCriteria();
        if (StringUtils.isNotEmpty(projectTypeCd)) {
            cra.andBorrowProjectTypeEqualTo(projectTypeCd);
        }

        return this.borrowProjectTypeMapper.selectByExample(example);
    }

    @Override
    public int getRecordCount(Map<String, Object> conditionMap) {
        
        return subCommissionListConfigCustomizeMapper.getRecordCount(conditionMap);
    }

    @Override
    public List<SubCommissionListConfigCustomize> getRecordList(Map<String, Object> conditionMap, int limitStart, int limitEnd) {
    	if (limitStart == 0 || limitStart > 0) {
			conditionMap.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			conditionMap.put("limitEnd", limitEnd);
		}
    	return subCommissionListConfigCustomizeMapper.getRecordList(conditionMap);
    }

	/**
	 * 获取分账名单配置中的所有用户的用户名、真实姓名、银行账号
	 * @return
	 * @author wgx
	 */
	@Override
	public List<SubCommissionListConfigCustomize> getSimpleList() {
		return subCommissionListConfigCustomizeMapper.getSimpleList();
	}

	@Override
    public int insertRecord(SubConfigBean form) {
        SubCommissionListConfigCustomize record = new SubCommissionListConfigCustomize();
        BeanUtils.copyProperties(form, record);
        Integer nowTime = GetDate.getNowTime10();
        record.setCreateTime(nowTime.toString());
        return subCommissionListConfigCustomizeMapper.insert(record);
    }

    @Override
    public int updateRecord(SubConfigBean form) {
    	SubCommissionListConfigCustomize record = new SubCommissionListConfigCustomize();
        BeanUtils.copyProperties(form, record);
        Integer nowTime = GetDate.getNowTime10();
        // 更新时间
        record.setCreateTime(nowTime.toString());
        return subCommissionListConfigCustomizeMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int deleteRecord(SubConfigBean form) {
        int ret = 0;
        if (form.getId()!=0) {
            ret = subCommissionListConfigCustomizeMapper.deleteByPrimaryKey(form.getId());
        }
        return ret;
    }

	

	@Override
	public SubCommissionListConfigCustomize getRecordInfo(Integer manChargeCd) {
		
		SubCommissionListConfigCustomize sub=subCommissionListConfigCustomizeMapper.selectByPrimaryKey(manChargeCd);
		return sub;
	}

	@Override
	public Map<String, Object> userMap(SubConfigBean form) {
		if (form.getUsername()!=null) {
			Map<String, Object> subCommissionListConfigCustomize=subCommissionListConfigCustomizeMapper.getUserInfo(form.getUsername());
			return subCommissionListConfigCustomize;
		}else {
			return null;
		}
	}
	
	@Override
	public Map<String, Object> getUserInfo(SubConfigBean form) {
		if (form.getUserId()!=null) {
			Map<String, Object> subCommissionListConfigCustomize=subCommissionListConfigCustomizeMapper.getUserIdInfo(form.getUserId());
			return subCommissionListConfigCustomize;
		}else {
			return null;
		}
	}

	@Override
	public int countRecordByProjectType(String manChargeType,
			Integer manChargeTime, String instCode, Integer assetType) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateLogRecord(SubConfigBean form) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteLogRecord(SubConfigBean form) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int countRecordByItems(Integer manChargeTime, String instCode,
			Integer assetType) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertLogRecord(SubConfigBean form) {
		// TODO Auto-generated method stub
		return 0;
	}

}
