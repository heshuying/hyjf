package com.hyjf.bank.service.act.act2017.balloon;

import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 *双十二气球活动业务类
 */
@Service
public class ActBalloonServiceImpl extends BaseServiceImpl implements ActBalloonService {
	Logger _log = LoggerFactory.getLogger(ActBalloonServiceImpl.class);
	
	@Override
	public void balloonTenderProcess(String tenderNid, String tenderType){
		BigDecimal accountTender = null;
		Integer tenderUserId = null;
		
		// 加载出借记录数据 
		BorrowTender tenderRecord;
		HjhAccede accedeRecord;
		if(tenderType.equals("0")){ 
			//直投
			tenderRecord = getBorrowTender(tenderNid);
			accountTender = tenderRecord.getAccount();
			tenderUserId = tenderRecord.getUserId();
		}else if(tenderType.equals("1")){ 
			//计划
			accedeRecord = getPlanTender(tenderNid);
			accountTender = accedeRecord.getAccedeAccount();
			tenderUserId = accedeRecord.getUserId();
		}
		
		Integer balloonCount = 0;
		
		Users tenderUser = getUsers(tenderUserId);
		UsersInfo userInfo = getUsersInfoByUserId(tenderUserId);
		if(userInfo.getAttribute() == 2 || userInfo.getAttribute() ==3){
			_log.info("集团员工不可参与双十二活动");
			return;
		}
		
		ActdecTenderBalloon tenderBalloon = getFirstBalloonTender(tenderUserId);
		ActdecTenderBalloon newTender = new ActdecTenderBalloon();
		
		// 计算本次出借所得气球数量
		boolean isFirstTender = checkIsFirstTender(tenderUserId, tenderNid);
		if(isFirstTender && accountTender.compareTo(new BigDecimal(5000))>=0){
			balloonCount = balloonCount +1 ;
			newTender.setIsFirstTender(0);
		}else{
			newTender.setIsFirstTender(1);
		}
		balloonCount = balloonCount + accountTender.intValue()/10000;
		_log.info("本次出借气球数量：" + balloonCount + " tendermoney:" + accountTender + " userId:" + tenderUserId);
		
		// 插入出借记录
		newTender.setUserId(tenderUserId);
		newTender.setUserName(tenderUser.getUsername());
		newTender.setTrueName(userInfo.getTruename());
		newTender.setMobile(tenderUser.getMobile());
		newTender.setTenderNid(tenderNid);
		newTender.setTenderMoney(accountTender);
		newTender.setBalloonCount(balloonCount);
		newTender.setTenderType(Integer.parseInt(tenderType));
		newTender.setTenderTime(GetDate.getNowTime10());
		newTender.setUpdateTime(GetDate.getNowTime10());
		if(tenderBalloon == null){
			newTender.setBallonCanReceive(balloonCount);
		}else{
			newTender.setRewardName(tenderBalloon.getRewardName());
			newTender.setBallonCanReceive(tenderBalloon.getBallonCanReceive() + balloonCount);
			newTender.setBallonReceived(tenderBalloon.getBallonReceived());
			
		}
		actdecTenderBalloonMapper.insertSelective(newTender);
		
		// 更新出借数据
		if(tenderBalloon != null){
			ActdecTenderBalloon tenderUpdate = new ActdecTenderBalloon();
			tenderUpdate.setBallonCanReceive(tenderBalloon.getBallonCanReceive() + balloonCount);
			tenderUpdate.setBallonReceived(tenderBalloon.getBallonReceived());
			tenderUpdate.setRewardName(tenderBalloon.getRewardName());
			tenderUpdate.setUpdateTime(GetDate.getNowTime10());
			
			ActdecTenderBalloonExample example = new ActdecTenderBalloonExample();
			example.createCriteria().andUserIdEqualTo(tenderUserId);
			
			actdecTenderBalloonMapper.updateByExampleSelective(tenderUpdate, example);
		}
	}
	
	/**
	 * 获取直投类出借记录
	 * @param tenderNid
	 * @return
	 */
	private BorrowTender getBorrowTender(String tenderNid){
		if(StringUtils.isBlank(tenderNid)){
			return null;
		}
		
		BorrowTenderExample example = new BorrowTenderExample();
		example.createCriteria().andNidEqualTo(tenderNid);
		List<BorrowTender> tenderList = borrowTenderMapper.selectByExample(example);
		
		if(tenderList != null && !tenderList.isEmpty()){
			return tenderList.get(0);
		}
		
		return null;
	}
	
	/**
	 * 获取计划类加入记录
	 * @param tenderNid
	 * @return
	 */
	private HjhAccede getPlanTender(String tenderNid){
		if(StringUtils.isBlank(tenderNid)){
			return null;
		}
		
		HjhAccedeExample example = new HjhAccedeExample();
		example.createCriteria().andAccedeOrderIdEqualTo(tenderNid);
		List<HjhAccede> tenderList =  hjhAccedeMapper.selectByExample(example);
		
		if(tenderList != null && !tenderList.isEmpty()){
			return tenderList.get(0);
		}
		
		return null;
	}
	
	/**
	 * 查询判断用户是否为首次出借
	 * @param userId
	 * @return
	 */
	private boolean checkIsFirstTender(Integer userId, String nid){
		String firstTenderNid = "";
		BorrowTenderExample example = new BorrowTenderExample();
		example.createCriteria().andUserIdEqualTo(userId).andAccedeOrderIdIsNull();
		example.setOrderByClause("addtime asc");
		List<BorrowTender> tenderList = borrowTenderMapper.selectByExample(example);
		_log.info("tenderList_zt:" + tenderList);
		
		HjhAccedeExample example2 = new HjhAccedeExample();
		example2.createCriteria().andUserIdEqualTo(userId);
		example2.setOrderByClause("create_time asc");
		List<HjhAccede> tenderList2 =  hjhAccedeMapper.selectByExample(example2);
		_log.info("tenderList_jh:" + tenderList2);
		
		
		if((tenderList == null || tenderList.isEmpty()) && (tenderList2 != null && !tenderList2.isEmpty())){
			firstTenderNid = tenderList2.get(0).getAccedeOrderId();
		}else if((tenderList2 == null || tenderList2.isEmpty()) && (tenderList != null && !tenderList.isEmpty())){
			firstTenderNid = tenderList.get(0).getNid();
		}else{
			if(tenderList.get(0).getAddtime() > tenderList.get(0).getAddtime()){
				firstTenderNid = tenderList.get(0).getNid();	
			}else{
				firstTenderNid = tenderList2.get(0).getAccedeOrderId();
			}
		}
		_log.info("firstTenderNid:" + firstTenderNid);
		
		return firstTenderNid.equals(nid) ? true : false;
	}
	
	/**
	 * 获取出借气球记录
	 * @param userId
	 * @return
	 */
	private ActdecTenderBalloon getFirstBalloonTender(Integer userId){
		ActdecTenderBalloonExample example = new ActdecTenderBalloonExample();
		example.createCriteria().andUserIdEqualTo(userId);
		example.setOrderByClause("id asc");
		
		List<ActdecTenderBalloon> balloonList = actdecTenderBalloonMapper.selectByExample(example);
		
		if(balloonList != null && !balloonList.isEmpty()){
			return balloonList.get(0);
		}
		
		return null;
	}
	

}
