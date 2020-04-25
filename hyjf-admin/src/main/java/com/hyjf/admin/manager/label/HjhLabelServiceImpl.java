package com.hyjf.admin.manager.label;

import java.util.List;

import com.hyjf.common.validator.Validator;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.manager.allocationengine.AllocationEngineBean;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowProjectTypeExample;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.BorrowStyleExample;
import com.hyjf.mybatis.model.auto.HjhAllocationEngine;
import com.hyjf.mybatis.model.auto.HjhAllocationEngineExample;
import com.hyjf.mybatis.model.auto.HjhLabel;
import com.hyjf.mybatis.model.auto.HjhLabelExample;
import com.hyjf.mybatis.model.customize.HjhLabelCustomize;
import com.hyjf.mybatis.model.customize.HjhLabelCustomizeExample;

@Service
public class HjhLabelServiceImpl extends BaseServiceImpl implements HjhLabelService {

    /**
     * 获取取惠天利配置列表
     * 
     * @return
     */
    public List<HjhLabelCustomize> getRecordList(HjhLabelBean form, int limitStart, int limitEnd) {
        HjhLabelCustomizeExample example = new HjhLabelCustomizeExample();
        HjhLabelCustomizeExample.Criteria criteria = example.createCriteria();
        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        /******************** 条件查询 **************************************/

        //标签名称搜索
        if (StringUtils.isNotEmpty(form.getLabelNameSrch())) {
            criteria.andLabelNameLike("%" + form.getLabelNameSrch() + "%");
        }

        //智投编号搜索
        if (StringUtils.isNotEmpty(form.getPlanNidSrch())){
            criteria.andPlanNidEqualTo(form.getPlanNidSrch());
        }

        //资产来源
        if (StringUtils.isNotEmpty(form.getInstCodeSrch())) {
            criteria.andInstCodeEqualTo(form.getInstCodeSrch());
        }
        //产品类型   
        if (form.getAssetTypeSrch()!=null) {
            criteria.andAssetTypeEqualTo(form.getAssetTypeSrch());
        }
        //项目类型  
        if (form.getProjectTypeSrch()!=null) {
            criteria.andProjectTypeEqualTo(form.getProjectTypeSrch());
        }
        //还款方式
        if (Validator.isNotNull(form.getBorrowStyleSrch())) {
            criteria.andBorrowStyleEqualTo(form.getBorrowStyleSrch());
        }
        //标签状态  
        if (form.getLabelStateSrch()!=null) {
            criteria.andLabelStateEqualTo(form.getLabelStateSrch());
        }
        //使用状态
        if (form.getEngineIdSrch()!=null){
        	if(form.getEngineIdSrch().equals(0)){
        		criteria.andEngineIdIsNull();
        	}
    		if(form.getEngineIdSrch().equals(1)){
    			criteria.andEngineIdIsNotNull();
        	}
        }
        if (StringUtils.isNotEmpty(form.getCreateTimeStartSrch()) && StringUtils.isNotEmpty(form.getCreateTimeEndSrch())) {
            criteria.andCreateTimeGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(form.getCreateTimeStartSrch())));
        }
        if(StringUtils.isNotEmpty(form.getCreateTimeEndSrch())){
            criteria.andCreateTimeLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayEnd(form.getCreateTimeEndSrch())));
        }

        if (StringUtils.isNotEmpty(form.getUpdateTimeStartSrch())) {
            criteria.andUpdateTimeGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(form.getUpdateTimeStartSrch())));
        }
        if(StringUtils.isNotEmpty(form.getUpdateTimeEndSrch())){
            criteria.andUpdateTimeLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayEnd(form.getUpdateTimeEndSrch())));
        }

        return hjhLabelCustomizeMapper.selectByExample(example);
    }
    /**
     * 项目类型
     * 
     * @return
     * @author Administrator
     */
    @Override
    public List<BorrowProjectType> borrowProjectTypeList(String borrowTypeCd) {
        BorrowProjectTypeExample example = new BorrowProjectTypeExample();
        BorrowProjectTypeExample.Criteria cra = example.createCriteria();
        cra.andStatusEqualTo(CustomConstants.FLAG_NORMAL);
        if (StringUtils.isNotEmpty(borrowTypeCd)) {
            cra.andBorrowProjectTypeEqualTo(borrowTypeCd);
        }
		// 不查询融通宝相关
		cra.andBorrowNameNotEqualTo(CustomConstants.RTB);
        return this.borrowProjectTypeMapper.selectByExample(example);
    }
    /**
     * 还款方式
     * 
     * @param nid
     * @return
     * @author Administratorji
     */
    @Override
    public List<BorrowStyle> borrowStyleList(String nid) {
        BorrowStyleExample example = new BorrowStyleExample();
        BorrowStyleExample.Criteria cra = example.createCriteria();
        cra.andStatusEqualTo(Integer.valueOf(CustomConstants.FLAG_NORMAL));
        if (StringUtils.isNotEmpty(nid)) {
            cra.andNidEqualTo(nid);
        }
        return this.borrowStyleMapper.selectByExample(example);
    }
    /**
     * 获取单个取惠天利配置维护
     * 
     * @return
     */
    public HjhLabelCustomize getRecord(Integer record) {
        HjhLabelCustomize hjhLabel = hjhLabelCustomizeMapper.selectByPrimaryKey(record);
        return hjhLabel;
    }
    
   /* public HjhLabelCustomize getRecord(Integer record) {
        HjhLabelCustomize hjhLabel = hjhLabelCustomizeMapper.selectByPrimaryKey(record);
        //EngineId不为空是，表示改标签被使用
        if(hjhLabel!=null){
            //hyjf_hjh_allocation_engine
            HjhAllocationEngineExample example = new HjhAllocationEngineExample();
            HjhAllocationEngineExample.Criteria criteria = example.createCriteria();
            criteria.andLabelIdEqualTo(hjhLabel.getId());
            criteria.andLabelStatusEqualTo(1);
            List<HjhAllocationEngine> hjhAllocationEnginList = hjhAllocationEngineMapper.selectByExample(example);
            if(hjhAllocationEnginList!=null && hjhAllocationEnginList.size()>0){
                hjhLabel.setEngineId(hjhAllocationEnginList.get(0).getId());
            }
        }
        return hjhLabel;
    }*/

    /**
     * 根据主键判断配置中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(HjhLabel record) {
        HjhLabelExample example = new HjhLabelExample();
        HjhLabelExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(record.getId());
        List<HjhLabel> hjhLabelList = hjhLabelMapper.selectByExample(example);
        if (hjhLabelList != null && hjhLabelList.size() > 0) {
            return true;
        }
        return false;
    }
    /**
     * 根据标签名称判断配置中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecordByName(HjhLabel record) {
        HjhLabelExample example = new HjhLabelExample();
        HjhLabelExample.Criteria criteria = example.createCriteria();
        criteria.andLabelNameEqualTo(record.getLabelName());
        if(record.getId()!=null){//添加，新增
            criteria.andIdNotEqualTo(record.getId());
        }
        List<HjhLabel> hjhLabelList = hjhLabelMapper.selectByExample(example);
        if (hjhLabelList != null && hjhLabelList.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 根据主键判断配置中数据是否存在
     * 
     * @return
     */
    public boolean isExistsPermission(HjhLabel record) {
        HjhLabelExample example = new HjhLabelExample();
        HjhLabelExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(record.getId());
        if (record.getId() != null) {
            criteria.andIdEqualTo(record.getId());
        }
        List<HjhLabel> hjhLabelList = hjhLabelMapper.selectByExample(example);
        if (hjhLabelList != null && hjhLabelList.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 配置插入
     * 
     * @param record
     */
    public void insertRecord(HjhLabel record) {
        record.setCreateTime(GetDate.getNowTime10());
        record.setUpdateTime(GetDate.getNowTime10());
        int userId = Integer.valueOf(ShiroUtil.getLoginUserId());
        record.setCreateUserId(userId);
        record.setUpdateUserId(userId);
        if(StringUtils.isEmpty(record.getBorrowStyle())){
            record.setBorrowStyleName("");
        }
        if(record.getAssetType()==null){
            record.setAssetTypeName("");
        }
        if(record.getProjectType()==null){
            record.setProjectTypeName("");
        }
        if(StringUtils.isEmpty(record.getInstCode())){
            record.setInstName("");
        }
        hjhLabelMapper.insertSelective(record);
    }

    /**
     * 配置更新
     * 
     * @param record
     */
    public void updateRecord(HjhLabel record) {
        record.setUpdateTime(GetDate.getNowTime10());
        int userId = Integer.valueOf(ShiroUtil.getLoginUserId());
        record.setUpdateUserId(userId);
        if(StringUtils.isEmpty(record.getBorrowStyle())){
            record.setBorrowStyleName("");
        }
        if(record.getAssetType()==null){
            record.setAssetTypeName("");
        }
        if(record.getProjectType()==null){
            record.setProjectTypeName("");
        }
        if(StringUtils.isEmpty(record.getInstCode())){
            record.setInstName("");
        }
        hjhLabelCustomizeMapper.updateByPrimaryKeySelective(record);
    }
    /**
     * 根据ID获取单个配置
     * 
     * @return
     */
    public HjhLabel getRecordById(Integer recordID) {
        HjhLabel result =hjhLabelMapper.selectByPrimaryKey(recordID);
        return  result;
    }
    @Override
	public List<HjhAllocationEngine> getAllocationEngineRecordList(AllocationEngineBean bean) {
		HjhAllocationEngineExample example = new HjhAllocationEngineExample(); 
		HjhAllocationEngineExample.Criteria criteria = example.createCriteria();
		// 条件查询1--标签名称
		if (StringUtils.isNotEmpty(bean.getLabelName())) {
			criteria.andLabelNameEqualTo(bean.getLabelName());
		}
		return hjhAllocationEngineMapper.selectByExample(example);
	}
    @Override
	public void updatePlanConfigRecord(HjhAllocationEngine record) {
		record.setUpdateTime(GetDate.getNowTime10());
		hjhAllocationEngineMapper.updateByPrimaryKeySelective(record);
	}
}
