package com.hyjf.admin.manager.config.borrow.borrowflow;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.mybatis.model.auto.HjhAssetBorrowType;
import com.hyjf.mybatis.model.auto.HjhAssetBorrowTypeExample;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowProjectTypeExample;
import com.hyjf.mybatis.model.customize.AdminSystem;
import com.hyjf.mybatis.model.customize.HjhAssetBorrowTypeCustomize;

/**
 * 
 * 标的流程Service
 * @author liubin
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年6月13日
 * @see 上午10:39:37
 */
@Service
public class BorrowFlowServiceImpl extends BaseServiceImpl implements BorrowFlowService {

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
        cra.andStatusEqualTo(CustomConstants.FLAG_NORMAL);
        if (StringUtils.isNotEmpty(projectTypeCd)) {
            cra.andBorrowProjectTypeEqualTo(projectTypeCd);
        }
		// 不查询融通宝相关
		cra.andBorrowNameNotEqualTo(CustomConstants.RTB);
        return this.borrowProjectTypeMapper.selectByExample(example);
    }

    @Override
    public int countRecord(BorrowFlowBean form) {
        HjhAssetBorrowTypeCustomize hjhAssetBorrowTypeCustomize = new HjhAssetBorrowTypeCustomize();
        BeanUtils.copyProperties(form, hjhAssetBorrowTypeCustomize);
        return hjhAssetBorrowTypeCustomizeMapper.countRecord(hjhAssetBorrowTypeCustomize);
    }

    @Override
    public List<HjhAssetBorrowTypeCustomize> getRecordList(BorrowFlowBean form, int limitStart, int limitEnd) {
        HjhAssetBorrowTypeCustomize hjhAssetBorrowTypeCustomize = new HjhAssetBorrowTypeCustomize();
        BeanUtils.copyProperties(form, hjhAssetBorrowTypeCustomize);
        if (limitStart != -1) {
            hjhAssetBorrowTypeCustomize.setLimitStart(limitStart);
            hjhAssetBorrowTypeCustomize.setLimitEnd(limitEnd);
        }
        return hjhAssetBorrowTypeCustomizeMapper.getRecordList(hjhAssetBorrowTypeCustomize);
    }

    @Override
    public HjhAssetBorrowType getRecordInfo(Integer id) {
        return hjhAssetBorrowTypeMapper.selectByPrimaryKey(id);
    }

    @Override
    public int countRecordByPK(String instCode, Integer assetType) {
        HjhAssetBorrowTypeExample example = new HjhAssetBorrowTypeExample();
        HjhAssetBorrowTypeExample.Criteria cra = example.createCriteria();
        cra.andInstCodeEqualTo(instCode);
        cra.andAssetTypeEqualTo(assetType);
        return hjhAssetBorrowTypeMapper.countByExample(example);
    }

    @Override
    public int insertRecord(BorrowFlowBean form) {
    	AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
        HjhAssetBorrowType record = new HjhAssetBorrowType();
        BeanUtils.copyProperties(form, record);
        int nowTime = GetDate.getNowTime10();
        record.setCreateTime(nowTime);
        record.setCreateUser(Integer.parseInt(adminSystem.getId()));
        record.setUpdateTime(nowTime);
        record.setUpdateUser(Integer.parseInt(adminSystem.getId()));
        return hjhAssetBorrowTypeMapper.insertSelective(record);
    }

    @Override
    public int updateRecord(BorrowFlowBean form) {
    	AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
        HjhAssetBorrowType record = new HjhAssetBorrowType();
        BeanUtils.copyProperties(form, record);
        int nowTime = GetDate.getNowTime10();
        // 更新时间
        record.setUpdateTime(nowTime);
        record.setUpdateUser(Integer.parseInt(adminSystem.getId()));
        return hjhAssetBorrowTypeMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int deleteRecord(BorrowFlowBean form) {
        int ret = 0;
        if (null != form.getId()) {
            ret = hjhAssetBorrowTypeMapper.deleteByPrimaryKey(form.getId());
        }
        return ret;
    }
}
