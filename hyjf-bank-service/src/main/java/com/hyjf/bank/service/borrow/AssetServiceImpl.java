package com.hyjf.bank.service.borrow;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.bank.service.borrow.issue.MQBorrow;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.HjhAllocationEngine;
import com.hyjf.mybatis.model.auto.HjhAllocationEngineExample;
import com.hyjf.mybatis.model.auto.HjhDebtCredit;
import com.hyjf.mybatis.model.auto.HjhLabel;
import com.hyjf.mybatis.model.auto.HjhLabelExample;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;
import com.hyjf.mybatis.model.auto.HjhPlanAssetExample;

public class AssetServiceImpl extends BaseServiceImpl implements AssetService {
	
	Logger _log = LoggerFactory.getLogger(AssetServiceImpl.class);
	
    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;
	
	/**
	 * 查询单个资产根据资产ID
	 * 
	 * @return
	 */
	@Override
	public HjhPlanAsset selectPlanAsset(String assetId, String instCode) {
		HjhPlanAsset resultAsset = null;
		HjhPlanAssetExample example = new HjhPlanAssetExample();
		HjhPlanAssetExample.Criteria crt = example.createCriteria();
		crt.andAssetIdEqualTo(assetId);
		crt.andInstCodeEqualTo(instCode);
        
        List<HjhPlanAsset> list = this.hjhPlanAssetMapper.selectByExample(example);
        
        if(list != null && list.size() > 0){
        	resultAsset = list.get(0);
        }
		
		return resultAsset;
	}
	
	/**
	 * 查询单个资产根据标的编号
	 * 
	 * @return
	 */
	@Override
	public HjhPlanAsset selectPlanAssetByBorrowNid(String borrowNid, String instCode) {
		HjhPlanAsset resultAsset = null;
		HjhPlanAssetExample example = new HjhPlanAssetExample();
		HjhPlanAssetExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid);
		crt.andInstCodeEqualTo(instCode);
        
        List<HjhPlanAsset> list = this.hjhPlanAssetMapper.selectByExample(example);
        
        if(list != null && list.size() > 0){
        	resultAsset = list.get(0);
        }
		
		return resultAsset;
	}
	
    /**
     * 推送消息到MQ
     */
    @Override
    public void sendToMQ(HjhPlanAsset hjhPlanAsset,String routingKey){

		// 加入到消息队列 
        Map<String, String> params = new HashMap<String, String>();
        params.put("mqMsgId", GetCode.getRandomCode(10));
        /*-----------------upd by liushouyi HJH3 Start---------------------------*/
        if (StringUtils.isNotBlank(hjhPlanAsset.getInstCode()) && ("10000000").equals(hjhPlanAsset.getInstCode())) {
        	params.put("borrowNid", hjhPlanAsset.getBorrowNid());
            params.put("instCode", hjhPlanAsset.getInstCode());
            //TODO 消息队列名称需要新建指向手动录标的保证金审核
            rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, routingKey, JSONObject.toJSONString(params));
        } else {
        	params.put("assetId", hjhPlanAsset.getAssetId());
            params.put("instCode", hjhPlanAsset.getInstCode());
            rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, routingKey, JSONObject.toJSONString(params));
        }
        /*-----------------upd by liushouyi HJH3 End---------------------------*/
	}
    
    /**
     * 手动录标推送消息到MQ
     */
    @Override
    public void sendToMQ(Borrow borrow,String routingKey){

		// 加入到消息队列 
        Map<String, String> params = new HashMap<String, String>();
        params.put("mqMsgId", GetCode.getRandomCode(10));
        params.put("borrowNid", borrow.getBorrowNid());
        params.put("instCode", "10000000");
        
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, routingKey, JSONObject.toJSONString(params));
        
	}
    /**
     * 推送消息到MQ
     */
    @Override
    public void sendToMQ(MQBorrow mqBorrow,String routingKey){

		// 加入到消息队列 
        Map<String, String> params = new HashMap<String, String>();
        params.put("mqMsgId", GetCode.getRandomCode(10));
        if (mqBorrow.getBorrowNid() != null) {
            params.put("borrowNid", mqBorrow.getBorrowNid());
		}else if (mqBorrow.getCreditNid() != null) {
	        params.put("creditNid", mqBorrow.getCreditNid());
		}
        
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, routingKey, JSONObject.toJSONString(params));
        
	}
	
	/**
	 * 标的匹配标签,取匹配最优的标签
	 * @param borrow
	 * @return
	 */
	@Override
	public HjhLabel getLabelId(BorrowWithBLOBs borrow, HjhPlanAsset hjhPlanAsset) {
		
		HjhLabel resultLabel = null; 
		
		HjhLabelExample example = new HjhLabelExample();
		HjhLabelExample.Criteria cra = example.createCriteria();
		
		cra.andDelFlgEqualTo(0);
		cra.andLabelStateEqualTo(1);
		cra.andBorrowStyleEqualTo(borrow.getBorrowStyle());
		cra.andIsCreditEqualTo(0); // 原始标
		cra.andIsLateEqualTo(0); // 是否逾期
		example.setOrderByClause(" update_time desc ");
		
		List<HjhLabel> list = this.hjhLabelMapper.selectByExample(example);
		if (list != null && list.size() <= 0) {
			_log.info(borrow.getBorrowStyle()+" 该原始标还款方式 没有一个标签");
			return resultLabel;
		}
		// continue过滤输入了但是不匹配的标签，如果找到就是第一个
		for (HjhLabel hjhLabel : list) {
			// 标的期限
//			int score = 0;
			if(hjhLabel.getLabelTermEnd() != null && hjhLabel.getLabelTermEnd().intValue()>0 && hjhLabel.getLabelTermStart()!=null
					&& hjhLabel.getLabelTermStart().intValue()>0){
				if(borrow.getBorrowPeriod() >= hjhLabel.getLabelTermStart() && borrow.getBorrowPeriod() <= hjhLabel.getLabelTermEnd()){
//					score = score+1;
				}else{
					continue;
				}
			}else if ((hjhLabel.getLabelTermEnd() != null && hjhLabel.getLabelTermEnd().intValue()>0) ||
					(hjhLabel.getLabelTermStart()!=null && hjhLabel.getLabelTermStart().intValue()>0)) {
				if(borrow.getBorrowPeriod() == hjhLabel.getLabelTermStart() || borrow.getBorrowPeriod() == hjhLabel.getLabelTermEnd()){
//					score = score+1;
				}else{
					continue;
				}
			}else{
				continue;
			}
			// 标的实际利率
			if(hjhLabel.getLabelAprStart() != null && hjhLabel.getLabelAprStart().compareTo(BigDecimal.ZERO)>0 &&
					hjhLabel.getLabelAprEnd()!=null && hjhLabel.getLabelAprEnd().compareTo(BigDecimal.ZERO)>0){
				if(borrow.getBorrowApr().compareTo(hjhLabel.getLabelAprStart())>=0 && borrow.getBorrowApr().compareTo(hjhLabel.getLabelAprEnd())<=0){
//					score = score+1;
				}else{
					continue;
				}
			}else if (hjhLabel.getLabelAprStart() != null && hjhLabel.getLabelAprStart().compareTo(BigDecimal.ZERO)>0) {
				if(borrow.getBorrowApr().compareTo(hjhLabel.getLabelAprStart())==0 ){
//					score = score+1;
				}else{
					continue;
				}
				
			}else if (hjhLabel.getLabelAprEnd()!=null && hjhLabel.getLabelAprEnd().compareTo(BigDecimal.ZERO)>0 ) {
				if(borrow.getBorrowApr().compareTo(hjhLabel.getLabelAprEnd())==0){
//					score = score+1;
				}else {
					continue;
				}
			}
			// 标的实际支付金额
			if(hjhLabel.getLabelPaymentAccountStart() != null && hjhLabel.getLabelPaymentAccountStart().compareTo(BigDecimal.ZERO)>0 &&
					hjhLabel.getLabelPaymentAccountEnd()!=null && hjhLabel.getLabelPaymentAccountEnd().compareTo(BigDecimal.ZERO)>0){
				if(borrow.getAccount().compareTo(hjhLabel.getLabelPaymentAccountStart())>=0 && borrow.getAccount().compareTo(hjhLabel.getLabelPaymentAccountEnd())<=0){
//					score = score+1;
				}else{
					continue;
				}
			}else if (hjhLabel.getLabelPaymentAccountStart() != null && hjhLabel.getLabelPaymentAccountStart().compareTo(BigDecimal.ZERO)>0) {
				if(borrow.getAccount().compareTo(hjhLabel.getLabelPaymentAccountStart())==0 ){
//					score = score+1;
				}else{
					continue;
				}
				
			}else if (hjhLabel.getLabelPaymentAccountEnd()!=null && hjhLabel.getLabelPaymentAccountEnd().compareTo(BigDecimal.ZERO)>0 ) {
				if(borrow.getAccount().compareTo(hjhLabel.getLabelPaymentAccountEnd())==0){
//					score = score+1;
				}else{
					continue;
				}
			}
			// 资产来源
			if(StringUtils.isNotBlank(hjhLabel.getInstCode())){
				if(hjhLabel.getInstCode().equals(borrow.getInstCode())){
//					score = score+1;
				}else{
					continue;
				}
			}
			// 产品类型
			if(hjhLabel.getAssetType() != null && hjhLabel.getAssetType().intValue() >= 0){
				if(hjhLabel.getAssetType().equals(borrow.getAssetType())){
					;
				}else{
					continue;
				}
			}
			// 项目类型
			if(hjhLabel.getProjectType() != null && hjhLabel.getProjectType().intValue() >= 0){
				if(hjhLabel.getProjectType().equals(borrow.getProjectType())){
					;
				}else{
					continue;
				}
			}
			
			// 推送时间节点
			if(hjhPlanAsset != null && hjhPlanAsset.getRecieveTime() != null && hjhPlanAsset.getRecieveTime().intValue() > 0){
				Date reciveDate = GetDate.getDate(hjhPlanAsset.getRecieveTime());
				
				if(hjhLabel.getPushTimeStart() != null && hjhLabel.getPushTimeEnd()!=null){
					if(reciveDate.getTime() >= hjhLabel.getPushTimeStart().getTime() &&
							reciveDate.getTime() <= hjhLabel.getPushTimeEnd().getTime()){
//						score = score+1;
					}else{
						continue;
					}
				}else if (hjhLabel.getPushTimeStart() != null) {
					if(reciveDate.getTime() == hjhLabel.getPushTimeStart().getTime() ){
//						score = score+1;
					}else{
						continue;
					}
					
				}else if (hjhLabel.getPushTimeEnd()!=null) {
					if(reciveDate.getTime() == hjhLabel.getPushTimeEnd().getTime() ){
//						score = score+1;
					}else{
						continue;
					}
				}
				
			}
			
			// 如果找到返回最近的一个
			return hjhLabel;
			
		}
		
		return resultLabel;
	}
	
	/**
	 * 债转标的匹配标签
	 * @param borrow
	 * @return
	 */
	@Override
	public HjhLabel getLabelId(HjhDebtCredit credit) {
		
		HjhLabel resultLabel = null; 
		
		HjhLabelExample example = new HjhLabelExample();
		HjhLabelExample.Criteria cra = example.createCriteria();
		
		cra.andDelFlgEqualTo(0);
		cra.andLabelStateEqualTo(1);
		cra.andBorrowStyleEqualTo(credit.getBorrowStyle());
		cra.andIsCreditEqualTo(1); // 债转标
		example.setOrderByClause(" update_time desc ");
		List<HjhLabel> list = this.hjhLabelMapper.selectByExample(example);
		
		if (list != null && list.size() <= 0) {
			_log.info(credit.getBorrowStyle()+" 该债转还款方式 没有一个标签");
			return resultLabel;
		}
		
		// continue过滤输入了但是不匹配的标签，如果找到就是第一个
		for (HjhLabel hjhLabel : list) {
			
			// 标的是否逾期 ,此为必须字段
			if(hjhLabel.getIsLate()!= null && hjhLabel.getIsLate().intValue()==1){
				if(credit.getIsLateCredit()!=null && credit.getIsLateCredit() == 1 ){
					;
				}else{
					continue;
				}
			}else if(hjhLabel.getIsLate()!= null && hjhLabel.getIsLate().intValue()==0){
				if(credit.getIsLateCredit()!=null && credit.getIsLateCredit() == 0 ){
					;
				}else{
					continue;
				}
			}
			// 标的期限
//			int score = 0;
			if(hjhLabel.getLabelTermEnd() != null && hjhLabel.getLabelTermEnd().intValue()>0 && hjhLabel.getLabelTermStart()!=null
					&& hjhLabel.getLabelTermStart().intValue()>0){
				if(credit.getBorrowPeriod() >= hjhLabel.getLabelTermStart() && credit.getBorrowPeriod() <= hjhLabel.getLabelTermEnd()){
//					score = score+1;
				}else{
					continue;
				}
			}else if ((hjhLabel.getLabelTermEnd() != null && hjhLabel.getLabelTermEnd().intValue()>0) ||
					(hjhLabel.getLabelTermStart()!=null && hjhLabel.getLabelTermStart().intValue()>0)) {
				if(credit.getBorrowPeriod() == hjhLabel.getLabelTermStart() || credit.getBorrowPeriod() == hjhLabel.getLabelTermEnd()){
//					score = score+1;
				}else{
					continue;
				}
			}
			// 标的实际利率
			if(hjhLabel.getLabelAprStart() != null && hjhLabel.getLabelAprStart().compareTo(BigDecimal.ZERO)>0 &&
					hjhLabel.getLabelAprEnd()!=null && hjhLabel.getLabelAprEnd().compareTo(BigDecimal.ZERO)>0){
				if(credit.getActualApr().compareTo(hjhLabel.getLabelAprStart())>=0 && credit.getActualApr().compareTo(hjhLabel.getLabelAprEnd())<=0){
					;
				}else{
					continue;
				}
			}else if (hjhLabel.getLabelAprStart() != null && hjhLabel.getLabelAprStart().compareTo(BigDecimal.ZERO)>0) {
				if(credit.getActualApr().compareTo(hjhLabel.getLabelAprStart())==0 ){
//					score = score+1;
				}else{
					continue;
				}
				
			}else if (hjhLabel.getLabelAprEnd()!=null && hjhLabel.getLabelAprEnd().compareTo(BigDecimal.ZERO)>0 ) {
				if(credit.getActualApr().compareTo(hjhLabel.getLabelAprEnd())==0){
					;
				}else{
					continue;
				}
			}
			// 标的实际支付金额
			if(hjhLabel.getLabelPaymentAccountStart() != null && hjhLabel.getLabelPaymentAccountStart().compareTo(BigDecimal.ZERO)>0 &&
					hjhLabel.getLabelPaymentAccountEnd()!=null && hjhLabel.getLabelPaymentAccountEnd().compareTo(BigDecimal.ZERO)>0){
				if(credit.getLiquidationFairValue().compareTo(hjhLabel.getLabelPaymentAccountStart())>=0 && credit.getLiquidationFairValue().compareTo(hjhLabel.getLabelPaymentAccountEnd())<=0){
					;
				}else{
					continue;
				}
			}else if (hjhLabel.getLabelPaymentAccountStart() != null && hjhLabel.getLabelPaymentAccountStart().compareTo(BigDecimal.ZERO)>0) {
				if(credit.getLiquidationFairValue().compareTo(hjhLabel.getLabelPaymentAccountStart())==0 ){
					;
				}else{
					continue;
				}
				
			}else if (hjhLabel.getLabelPaymentAccountEnd()!=null && hjhLabel.getLabelPaymentAccountEnd().compareTo(BigDecimal.ZERO)>0 ) {
				if(credit.getLiquidationFairValue().compareTo(hjhLabel.getLabelPaymentAccountEnd())==0){
					;
				}else{
					continue;
				}
			}
			// 资产来源
			if(StringUtils.isNotBlank(hjhLabel.getInstCode())){
				if(hjhLabel.getInstCode().equals(credit.getInstCode())){
//					score = score+1;
				}else{
					continue;
				}
			}
			// 产品类型
			if(hjhLabel.getAssetType() != null && hjhLabel.getAssetType().intValue() >= 0){
				if(hjhLabel.getAssetType().equals(credit.getAssetType())){
					;
				}else{
					continue;
				}
			}
			// 项目类型
			if(hjhLabel.getProjectType() != null && hjhLabel.getProjectType().intValue() >= 0){
				if(hjhLabel.getProjectType().equals(credit.getProjectType())){
					;
				}else{
					continue;
				}
			}
			
			// 剩余天数
			if(hjhLabel.getRemainingDaysEnd() != null && hjhLabel.getRemainingDaysEnd().intValue()>=0 && hjhLabel.getRemainingDaysStart()!=null
					&& hjhLabel.getRemainingDaysStart().intValue()>=0){
				if(credit.getRemainDays() != null && credit.getRemainDays() >= hjhLabel.getRemainingDaysStart() && credit.getRemainDays() <= hjhLabel.getRemainingDaysEnd()){
					;
				}else{
					continue;
				}
			}else if ((hjhLabel.getRemainingDaysEnd() != null && hjhLabel.getRemainingDaysEnd().intValue()>=0) ||
					(hjhLabel.getRemainingDaysStart()!=null && hjhLabel.getRemainingDaysStart().intValue()>=0)) {
				if(credit.getRemainDays() != null && credit.getRemainDays() == hjhLabel.getRemainingDaysStart() || credit.getRemainDays() == hjhLabel.getRemainingDaysEnd()){
					;
				}else{
					continue;
				}
			}
			
			// 找出即为最新的标签
			return hjhLabel;
			
		}
		
		return resultLabel;
	}
	
	/**
	 * 根据标签id,取计划编号
	 * @param borrow
	 * @return
	 */
	@Override
	public String getPlanNid(Integer labelId) {
		
		HjhAllocationEngineExample example = new HjhAllocationEngineExample();
		HjhAllocationEngineExample.Criteria cra = example.createCriteria();
		
		cra.andDelFlgEqualTo(0);
		cra.andLabelIdEqualTo(labelId);
		cra.andConfigStatusEqualTo(1);
		cra.andLabelStatusEqualTo(1);
		List<HjhAllocationEngine> list = this.hjhAllocationEngineMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0).getPlanNid();
		}
		
		return null;
	}
	
	
}
