package com.hyjf.admin.coupon.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.util.CreateUUID;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowProjectTypeExample;
import com.hyjf.mybatis.model.auto.CouponConfig;
import com.hyjf.mybatis.model.auto.CouponConfigExample;
import com.hyjf.mybatis.model.auto.CouponOperationHistoryWithBLOBs;
import com.hyjf.mybatis.model.auto.CouponUser;
import com.hyjf.mybatis.model.auto.CouponUserExample;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigExoportCustomize;

@Service
public class CouponConfigServiceImpl extends BaseServiceImpl implements
		CouponConfigService {

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	@Override
	public List<CouponConfigCustomize> getRecordList(
			CouponConfigCustomize couponConfigCustomize) {
		return couponConfigCustomizeMapper
				.selectCouponConfigList(couponConfigCustomize);
	}

	/**
	 * 获取单个维护
	 * 
	 * @return
	 */
	@Override
	public CouponConfig getRecord(String record) {
		CouponConfig coupon = couponConfigMapper.selectByPrimaryKey(Integer
				.parseInt(record));
		return coupon;
	}

	/**
	 * 获得记录数
	 * 
	 * @param CouponConfigCustomize
	 * @return
	 */
	@Override
	public Integer countRecord(CouponConfigCustomize couponConfigCustomize) {
		return couponConfigCustomizeMapper
				.countCouponConfig(couponConfigCustomize);
	}

	/**
	 * 维护插入
	 * 
	 * @param record
	 */
	@Override
	public void insertRecord(CouponConfigBean couponConfigBean) {
		Integer nowDate = GetDate.getNowTime10();
		CouponConfig record = new CouponConfig();
		record.setCouponName(couponConfigBean.getCouponName());
		record.setCouponQuantity(couponConfigBean.getCouponQuantity());
		record.setCouponQuota(couponConfigBean.getCouponQuota());
		
		record.setCouponSystem(couponConfigBean.getCouponSystem());
		if(couponConfigBean.getCouponSystemAll()==null){
		    record.setCouponSystem(couponConfigBean.getCouponSystem());
		}else{
		    record.setCouponSystem("-1,"+couponConfigBean.getCouponSystem());
		}
		
		record.setCouponType(couponConfigBean.getCouponType());
		record.setDelFlg(CustomConstants.FALG_NOR);
		if (StringUtils.isNotEmpty(couponConfigBean.getExpirationDateStr())) {
			record.setExpirationDate(Integer.parseInt(GetDate
					.get10Time(couponConfigBean.getExpirationDateStr())));
		}
		
		if(Validator.isNotNull(couponConfigBean.getCouponProfitTime())){
		    record.setCouponProfitTime(couponConfigBean.getCouponProfitTime());
		}
		
		if(Validator.isNotNull(couponConfigBean.getAddFlg())){
		    record.setAddFlg(couponConfigBean.getAddFlg());
		}
		
		record.setExpirationLengthDay(couponConfigBean.getExpirationLengthDay());
		record.setExpirationLength(couponConfigBean.getExpirationLength());
		record.setExpirationType(couponConfigBean.getExpirationType());
		record.setProjectExpirationLength(couponConfigBean
				.getProjectExpirationLength());
		record.setProjectExpirationLengthMax(couponConfigBean
				.getProjectExpirationLengthMax());
		record.setProjectExpirationLengthMin(couponConfigBean
				.getProjectExpirationLengthMin());
		record.setProjectExpirationType(couponConfigBean
				.getProjectExpirationType());
		
		
		
		if(couponConfigBean.getProjectTypeAll()==null){
		    record.setProjectType(couponConfigBean.getProjectType());
        }else{
            record.setProjectType("-1,"+couponConfigBean.getProjectType());
        }
		
		record.setTenderQuota(couponConfigBean.getTenderQuota());
		record.setTenderQuotaMax(couponConfigBean.getTenderQuotaMax());
		record.setTenderQuotaMin(couponConfigBean.getTenderQuotaMin());
		record.setTenderQuotaType(couponConfigBean.getTenderQuotaType());
		record.setAddTime(nowDate);
		record.setAddUser(ShiroUtil.getLoginUserId());
		record.setUpdateTime(nowDate);
		record.setUpdateUser(ShiroUtil.getLoginUserId());
		record.setCouponCode(GetCode.getCouponCode(couponConfigBean
				.getCouponType()));
		record.setContent(couponConfigBean.getContent());
		record.setStatus(1);
		record.setRepayTimeConfig(couponConfigBean.getRepayTimeConfig());
		
		couponConfigMapper.insertSelective(record);
		this.operationLog(record, CustomConstants.OPERATION_CODE_INSERT);

	}

	/**
	 * 操作履历
	 * 
	 * @param couponTo
	 * @param operationCode
	 */
	private void operationLog(CouponConfig couponTo, int operationCode) {
		CouponOperationHistoryWithBLOBs co = new CouponOperationHistoryWithBLOBs();
		// 编号
		co.setUuid(CreateUUID.getUUID());
		// 优惠券编号
		co.setCouponCode(couponTo.getCouponCode());
		// 操作编号
		co.setOperationCode(operationCode);
		// 取得上传更新前的数据
		CouponConfig recordFrom = couponConfigMapper
				.selectByPrimaryKey(couponTo.getId());
		// 更新，删除的场合
		if (operationCode != CustomConstants.OPERATION_CODE_INSERT) {
			// 更新前的json数据
			co.setOperationContentFrom(JSONObject
					.toJSONString(recordFrom, true));
		}
		// 更新后的json数据
		co.setOperationContentTo(JSONObject.toJSONString(couponTo, true));

		// 操作者
		co.setAddUser(ShiroUtil.getLoginUserId());
		// 操作时间
		co.setAddTime(GetDate.getNowTime10());
		couponOperationHistoryMapper.insertSelective(co);

	}

	/**
	 * 更新
	 * 
	 * @param record
	 */
	@Override
	public void updateRecord(CouponConfigBean form) {
		Integer nowDate = GetDate.getNowTime10();
		String userId = ShiroUtil.getLoginUserId();
		CouponConfig record = new CouponConfig();
		BeanUtils.copyProperties(form, record);
		record.setUpdateUser(userId);
		record.setUpdateTime(nowDate);
		couponConfigMapper.updateByPrimaryKeySelective(record);
		this.operationLog(record, CustomConstants.OPERATION_CODE_MODIFY);
	}

	/**
	 * 审核
	 * 
	 * @param record
	 */
	@Override
	public void auditRecord(CouponConfig record) {
		Integer nowDate = GetDate.getNowTime10();
		String userId = ShiroUtil.getLoginUserId();
		record.setUpdateUser(userId);
		record.setUpdateTime(nowDate);
		record.setAuditUser(userId);
		record.setAuditTime(nowDate);
		couponConfigMapper.updateByPrimaryKeySelective(record);
		this.operationLog(record, CustomConstants.OPERATION_CODE_MODIFY);
	}

	/**
	 * 根据主键删除
	 * 
	 * @param recordList
	 */
	@Override
	public void deleteRecord(String recordId) {
		if (StringUtils.isNotEmpty(recordId)) {
			CouponConfig record = couponConfigMapper.selectByPrimaryKey(Integer
					.parseInt(recordId));
			record.setDelFlg(CustomConstants.FALG_DEL);
			couponConfigMapper.updateByPrimaryKeySelective(record);
			this.operationLog(record, CustomConstants.OPERATION_CODE_DELETE);
		}
	}

	/**
	 * 获取项目类别
	 * 
	 * @return
	 * @author Michael
	 */
	@Override
	public List<BorrowProjectType> getProjectTypeList() {
		BorrowProjectTypeExample example = new BorrowProjectTypeExample();
		BorrowProjectTypeExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(CustomConstants.FLAG_NORMAL);
		// 不查询融通宝相关
		cra.andBorrowNameNotEqualTo(CustomConstants.RTB);
		cra.andBorrowNameNotEqualTo("HXF");
		cra.andBorrowNameNotEqualTo("ZXH");
		return this.borrowProjectTypeMapper.selectByExample(example);
	}

	/**
     * 获取项目类别
     * pcc20160715
     * @return
     * @author Michael
     */
    @Override
    public List<BorrowProjectType> getCouponProjectTypeList() {
      
        List<BorrowProjectType> list=new ArrayList<BorrowProjectType>();
        BorrowProjectType borrowProjectType1=new BorrowProjectType();
        borrowProjectType1.setBorrowCd("1");
        borrowProjectType1.setBorrowName("散标");
        list.add(borrowProjectType1);
        
//        BorrowProjectType borrowProjectType2=new BorrowProjectType();
//        borrowProjectType2.setBorrowCd("2");
//        borrowProjectType2.setBorrowName("汇消费");
//        list.add(borrowProjectType2);
        
        BorrowProjectType borrowProjectType3=new BorrowProjectType();
        borrowProjectType3.setBorrowCd("3");
        borrowProjectType3.setBorrowName("新手");
        list.add(borrowProjectType3);
        
//        BorrowProjectType borrowProjectType4=new BorrowProjectType();
//        borrowProjectType4.setBorrowCd("4");
//        borrowProjectType4.setBorrowName("尊享汇");
//        list.add(borrowProjectType4);
        
//        BorrowProjectType borrowProjectType5=new BorrowProjectType();
//        borrowProjectType5.setBorrowCd("5");
//        borrowProjectType5.setBorrowName("汇添金");
//        list.add(borrowProjectType5);
        
        BorrowProjectType borrowProjectType6=new BorrowProjectType();
        borrowProjectType6.setBorrowCd("6");
        borrowProjectType6.setBorrowName("智投");
        list.add(borrowProjectType6);
        return list;
    }

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * 
	 * @param couponCode
	 * @return
	 * @author Michael
	 */

	@Override
	public Integer getIssueNumber(String couponCode) {
		CouponUserExample example = new CouponUserExample();
		CouponUserExample.Criteria cra = example.createCriteria();
		if (StringUtils.isNotEmpty(couponCode)) {
			cra.andCouponCodeEqualTo(couponCode);
		}
		cra.andDelFlagEqualTo(CustomConstants.FALG_NOR);
		return this.couponUserMapper.countByExample(example);
	}

	/**
	 * 导出列表
	 * 
	 * @param couponConfigCustomize
	 * @return
	 * @author Michael
	 */
	@Override
	public List<CouponConfigExoportCustomize> exoportRecordList(
			CouponConfigCustomize couponConfigCustomize) {
		return couponConfigCustomizeMapper
				.exportCouponConfigList(couponConfigCustomize);
	}

	/**
	 * 校验该优惠券已发放了多少张
	 */
	@Override
	public int getCouponUsedCount(int couponId) {
		return this.couponUserCustomizeMapper.getCouponUsedCount(couponId);
	}
	
	/**
     * 校验优惠券的已发行数量
     * 
     * @return
     */
    @Override
    public boolean checkSendNum(String couponCode, String amount) {
        if(amount==null||"".equals(amount)){
            amount="0";
        }
        CouponUserExample couponUserExample = new CouponUserExample();
        couponUserExample.createCriteria().andCouponCodeEqualTo(couponCode).andDelFlagEqualTo(0);
        List<CouponUser> couponUserList = this.couponUserMapper
                .selectByExample(couponUserExample);
        // 取得已发行优惠券数量
        int sendedCouponSum = couponUserList != null
                && couponUserList.size() > 0 ? couponUserList.size() : 0;

        CouponConfigExample couponConfigExample = new CouponConfigExample();
        couponConfigExample.createCriteria().andCouponCodeEqualTo(couponCode);
        List<CouponConfig> couponConfigList = this.couponConfigMapper
                .selectByExample(couponConfigExample);
        // 取得配置的优惠券
        CouponConfig couponConfig = couponConfigList != null
                && couponConfigList.size() > 0 ? couponConfigList.get(0) : null;

        return couponConfig != null
                && couponConfig.getCouponQuantity() >= sendedCouponSum+new Integer(amount) ? true
                : false;
    }
}
