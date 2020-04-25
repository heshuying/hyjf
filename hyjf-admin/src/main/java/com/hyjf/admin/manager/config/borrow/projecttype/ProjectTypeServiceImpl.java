package com.hyjf.admin.manager.config.borrow.projecttype;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.BorrowProjectRepay;
import com.hyjf.mybatis.model.auto.BorrowProjectRepayExample;
import com.hyjf.mybatis.model.auto.BorrowProjectRepayExample.Criteria;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowProjectTypeExample;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.BorrowStyleExample;
import com.hyjf.mybatis.model.auto.HjhAssetType;
import com.hyjf.mybatis.model.auto.HjhAssetTypeExample;
import com.hyjf.mybatis.model.customize.ProjectTypeCustomize;

@Service
public class ProjectTypeServiceImpl extends BaseServiceImpl implements ProjectTypeService {

	/**
	 * 获取汇直投项目类型列表
	 * 
	 * @return
	 */
	@Override
	public List<ProjectTypeCustomize> getProjectTypeList(ProjectTypeCustomize projectTypeCustomize) {
		return this.projectTypeCustomizeMapper.getProjectTypeList(projectTypeCustomize);
	}

	/**
	 * 获取单个汇直投项目类型维护
	 * 
	 * @return
	 */
	public BorrowProjectType getRecord(BorrowProjectType record) {
		BorrowProjectTypeExample example = new BorrowProjectTypeExample();
		BorrowProjectTypeExample.Criteria cra = example.createCriteria();
		cra.andBorrowCdEqualTo(record.getBorrowCd());
		List<BorrowProjectType> BorrowTypeList = borrowProjectTypeMapper.selectByExample(example);
		if (BorrowTypeList != null && BorrowTypeList.size() > 0) {
			return BorrowTypeList.get(0);
		}
		return new BorrowProjectType();
	}

	/**
	 * 根据主键判断汇直投项目类型维护中数据是否存在
	 * 
	 * @return
	 */
	public boolean isExistsRecord(BorrowProjectType record) {
		if (StringUtils.isEmpty(record.getBorrowCd())) {
			return false;
		}
		BorrowProjectTypeExample example = new BorrowProjectTypeExample();
		BorrowProjectTypeExample.Criteria cra = example.createCriteria();
		cra.andBorrowCdEqualTo(record.getBorrowCd());
		List<BorrowProjectType> BorrowTypeList = borrowProjectTypeMapper.selectByExample(example);
		if (BorrowTypeList != null && BorrowTypeList.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 汇直投项目类型维护插入
	 * 
	 * @param record
	 */
	public void insertRecord(ProjectTypeBean form) {

		Date sysDate = new Date();
		String userId = ShiroUtil.getLoginUserId();
		BorrowProjectType record = new BorrowProjectType();
		BeanUtils.copyProperties(form, record);
		// 插入用户表
		record.setBorrowCd(form.getBorrowCd());
		record.setBorrowProjectType(form.getBorrowProjectType());
		record.setStatus(CustomConstants.FLAG_NORMAL);
		record.setCreateTime(sysDate);
		record.setCreateUserId(userId);
		record.setCreateGroupId(userId);
		record.setUpdateTime(sysDate);
		record.setUpdateGroupId(userId);
		record.setUpdateUserId(userId);
		borrowProjectTypeMapper.insertSelective(record);
		// 直接插入
		this.insertRepay(form);
		/*--------------------------add by LSY START-----------------------------------*/
		// 直接插入asset表
		this.insertAsset(form);
		/*--------------------------add by LSY END-----------------------------------*/
	}

	/**
	 * 汇直投项目类型维护更新
	 * 
	 * @param record
	 */
	public void updateRecord(ProjectTypeBean record) {
		Date sysDate = new Date();
		record.setUpdateTime(sysDate);
		BorrowProjectTypeExample example = new BorrowProjectTypeExample();
		BorrowProjectTypeExample.Criteria cra = example.createCriteria();
		cra.andBorrowCdEqualTo(record.getBorrowCd());
		if(record.getInterestCoupon()==null||record.getInterestCoupon().equals("")){
		    record.setInterestCoupon(0); 
		}
		if(record.getTasteMoney()==null||record.getTasteMoney().equals("")){
            record.setTasteMoney(0); 
        }
		this.borrowProjectTypeMapper.updateByExampleSelective(record, example);
		// 先删除再插入数据
		this.delectRepay(record.getBorrowClass());
		this.insertRepay(record);
		/*--------------------------add by LSY START-----------------------------------*/
		//asset表更新处理
		this.updateAsset(record);
		/*--------------------------add by LSY END-----------------------------------*/
	}

	/**
	 * 汇直投项目类型维护删除
	 * 
	 * @param record
	 */
	public void deleteRecord(String borrowCd) {
		BorrowProjectTypeExample example = new BorrowProjectTypeExample();
		BorrowProjectTypeExample.Criteria cra = example.createCriteria();
		cra.andBorrowCdEqualTo(borrowCd);
		borrowProjectTypeMapper.deleteByExample(example);
	}

	/**
	 * 项目编号是否存在
	 * 
	 * @param borrowCd
	 * @param borrowName
	 * @return
	 */
	@Override
	public int borrowCdIsExists(String borrowCd) {
		BorrowProjectTypeExample example = new BorrowProjectTypeExample();
		BorrowProjectTypeExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowCdEqualTo(borrowCd);
		return borrowProjectTypeMapper.countByExample(example);
	}

	@Override
	public List<BorrowProjectRepay> selectRepay(String str) {
		// 封装查询条件
		BorrowProjectRepayExample example = new BorrowProjectRepayExample();
		example.setLimitStart(-1);
		example.setLimitEnd(-1);
		Criteria criteria = example.createCriteria();
		criteria.andBorrowClassEqualTo(str);
		return borrowProjectRepayMapper.selectByExample(example);

	}

	@Override
	public List<BorrowStyle> selectStyles() {
		// 封装查询条件
		BorrowStyleExample example = new BorrowStyleExample();
		BorrowStyleExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(0);
		example.setLimitStart(-1);
		example.setLimitEnd(-1);
		return this.borrowStyleMapper.selectByExample(example);

	}

	@Override
	public void delectRepay(String borrowClass) {
		BorrowProjectRepayExample example = new BorrowProjectRepayExample();
		Criteria criteria = example.createCriteria();
		criteria.andBorrowClassEqualTo(borrowClass);
		borrowProjectRepayMapper.deleteByExample(example);

	}

	@Override
	public void insertRepay(ProjectTypeBean form) {
		BorrowProjectRepay repay = new BorrowProjectRepay();
		String[] split = form.getMethodName().split(",");
		for (String str : split) {
			repay.setRepayMethod(str);
			repay.setBorrowClass(form.getBorrowClass());
			// 查询存取
			BorrowStyleExample example = new BorrowStyleExample();
			com.hyjf.mybatis.model.auto.BorrowStyleExample.Criteria criteria = example.createCriteria();
			criteria.andNidEqualTo(str);
			List<BorrowStyle> list = this.borrowStyleMapper.selectByExample(example);
			repay.setMethodName(list.get(0).getName());
			repay.setDelFlag("0");
			// 插入数据
			borrowProjectRepayMapper.insertSelective(repay);
		}

	}
	

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param form
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public void insertAsset(ProjectTypeBean form) {
		HjhAssetType record = new HjhAssetType();
		//BeanUtils.copyProperties(form, record);

		//userid为int型CreateUser是Integer类型、用户id为字符串类型
		String userId = ShiroUtil.getLoginUserId();
		record.setCreateUser(Integer.parseInt(userId));
		//汇赢金服的类型
		record.setInstCode(CustomConstants.INST_CODE_HYJF);
		//项目类型里编号存放string类型、tinyint类型最大值存放127
		record.setAssetType(Integer.parseInt(form.getBorrowCd()));
		
		//名称
		record.setAssetTypeName(form.getBorrowName());
		//状态
		record.setStatus(CustomConstants.FLAG_STATUS_ENABLE);
		//时间戳是integer类型
		int nowTime = GetDate.getNowTime10();
		record.setCreateTime(nowTime);
		record.setDelFlg(CustomConstants.FLAG_STATUS_ENABLE);
		
		hjhAssetTypeMapper.insertSelective(record);
		
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param assetType
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public void deleteAsset(Integer assetType) {		
		HjhAssetTypeExample example = new HjhAssetTypeExample();
		HjhAssetTypeExample.Criteria cra = example.createCriteria();
		cra.andAssetTypeEqualTo(assetType);
		cra.andInstCodeEqualTo(CustomConstants.INST_CODE_HYJF);
		hjhAssetTypeMapper.deleteByExample(example);
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param form
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public void updateAsset(ProjectTypeBean form) {
		HjhAssetTypeExample example = new HjhAssetTypeExample();
		HjhAssetTypeExample.Criteria cra = example.createCriteria();
		cra.andAssetTypeEqualTo(Integer.parseInt(form.getBorrowCd()));
		cra.andInstCodeEqualTo(CustomConstants.INST_CODE_HYJF);
		
		HjhAssetType record = new HjhAssetType();
		String userId = ShiroUtil.getLoginUserId();
		//更新用户id
		record.setUpdateUser(Integer.parseInt(userId));
		int nowTime = GetDate.getNowTime10();
		//更新时间
		record.setUpdateTime(nowTime);
		//名称
		record.setAssetTypeName(form.getBorrowName());
		//状态
		record.setStatus(CustomConstants.FLAG_STATUS_ENABLE);
		//删除flag
		record.setDelFlg(CustomConstants.FLAG_STATUS_ENABLE);
		
		hjhAssetTypeMapper.updateByExampleSelective(record, example);
		
	}
}
