package com.hyjf.admin.manager.user.loancover;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.mapper.auto.LoanSubjectCertificateAuthorityMapper;
import com.hyjf.mybatis.mapper.auto.MspAbnormalCreditDetailMapper;
import com.hyjf.mybatis.mapper.auto.MspAbnormalCreditMapper;
import com.hyjf.mybatis.mapper.auto.MspApplyDetailsMapper;
import com.hyjf.mybatis.mapper.auto.MspBlackDataMapper;
import com.hyjf.mybatis.mapper.auto.MspFqzMapper;
import com.hyjf.mybatis.mapper.auto.MspNormalCreditDetailMapper;
import com.hyjf.mybatis.mapper.auto.MspQueryDetailMapper;
import com.hyjf.mybatis.mapper.auto.MspTitleMapper;
import com.hyjf.mybatis.model.auto.CertificateAuthority;
import com.hyjf.mybatis.model.auto.CertificateAuthorityExample;
import com.hyjf.mybatis.model.auto.LoanSubjectCertificateAuthority;
import com.hyjf.mybatis.model.auto.LoanSubjectCertificateAuthorityExample;
import com.hyjf.mybatis.model.auto.LoanSubjectCertificateAuthorityExample.Criteria;
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
public class LoanCoverServiceImpl extends BaseServiceImpl implements LoanCoverService {

	/**
	 * 获取列表列表
	 * 
	 * @return
	 */
	public List<LoanSubjectCertificateAuthority> getRecordList(LoanSubjectCertificateAuthority lsca, int limitStart,
			int limitEnd, int createStart, int createEnd) {
		LoanSubjectCertificateAuthorityExample example = new LoanSubjectCertificateAuthorityExample();
		example.setOrderByClause(" id desc");
		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		Criteria criteria = example.createCriteria();
		// 条件查询
		if (StringUtils.isNotEmpty(lsca.getName())) {
			criteria.andNameEqualTo(lsca.getName());
		}
		if (StringUtils.isNotEmpty(lsca.getMobile())) {
			criteria.andMobileEqualTo(lsca.getMobile());
		}
		if (StringUtils.isNotEmpty(lsca.getIdNo())) {
			criteria.andIdNoEqualTo(lsca.getIdNo());
		}
		if (createStart != 0 || createEnd != 0) {
			criteria.andCreateTimeBetween(createStart, createEnd);
		}
		 if (lsca.getIdType()!=null) {
		 criteria.andIdTypeEqualTo(lsca.getIdType());
		 }
		if (StringUtils.isNotEmpty(lsca.getCustomerId())) {
			criteria.andCustomerIdEqualTo(lsca.getCustomerId());
		}
		if (StringUtils.isNotEmpty(lsca.getCode())) {
			if(lsca.getCode().equals("2001")) {
				List list = new ArrayList();
				list.add("2001");
				list.add("2002");
				list.add("2003");
				criteria.andCodeIn(list);
			}
			if(lsca.getCode().equals("2002")) {
				criteria.andCodeEqualTo("");
			}
			if(lsca.getCode().equals("1000")) {
				criteria.andCodeEqualTo(lsca.getCode());	
			}
			
		}

		return loanSubjectCertificateAuthorityMapper.selectByExample(example);
	}

	/**
	 * 获取单个维护
	 * 
	 * @return
	 */
	public LoanSubjectCertificateAuthority getRecord(Integer record) {
		LoanSubjectCertificateAuthority FeeConfig = loanSubjectCertificateAuthorityMapper.selectByPrimaryKey(record);
		return FeeConfig;
	}

	/**
	 * 根据主键判断维护中数据是否存在
	 * 
	 * @return
	 */
	public boolean isExistsRecord(String record) {
		if (record == null) {
			return false;
		}
		LoanSubjectCertificateAuthorityExample example = new LoanSubjectCertificateAuthorityExample();
		example.or().andIdNoEqualTo(record);
		List<LoanSubjectCertificateAuthority> lll = loanSubjectCertificateAuthorityMapper.selectByExample(example);
		if(lll.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * 维护插入
	 * 
	 * @param record
	 */
	public void insertRecord(LoanSubjectCertificateAuthority  record) {
		loanSubjectCertificateAuthorityMapper.insertSelective(record);
	}

	/**
	 * 维护更新
	 * 
	 * @param record
	 */
	public void updateRecord(LoanSubjectCertificateAuthority record) {
		loanSubjectCertificateAuthorityMapper.updateByPrimaryKeySelective(record);
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

	@Override
	public String isExistsAuthority(String idno, String tureName) {
		CertificateAuthorityExample example=new CertificateAuthorityExample();
		com.hyjf.mybatis.model.auto.CertificateAuthorityExample.Criteria criteria = example.createCriteria();
		criteria.andTrueNameEqualTo(tureName);
		criteria.andIdNoEqualTo(idno);
		List<CertificateAuthority> cam = certificateAuthorityMapper.selectByExample(example);
		if(!cam.isEmpty()&&cam.get(0).getTrueName().equals(tureName)&&cam.get(0).getIdNo().equals(idno)&&!cam.get(0).getCustomerId().isEmpty()) {
			return cam.get(0).getCustomerId();
		}else {
			return "";
		}
	}

	

}
