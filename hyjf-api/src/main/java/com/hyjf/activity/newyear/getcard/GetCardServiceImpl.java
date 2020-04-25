package com.hyjf.activity.newyear.getcard;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.AccountChinapnrExample;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;
import com.hyjf.mybatis.model.auto.DebtPlanAccedeExample;
import com.hyjf.mybatis.model.auto.NewyearCaisheCardUser;
import com.hyjf.mybatis.model.auto.NewyearCaishenCardQuantity;
import com.hyjf.mybatis.model.auto.NewyearCaishenCardQuantityExample;
import com.hyjf.mybatis.model.auto.NewyearGetCard;
import com.hyjf.mybatis.model.auto.NewyearGetCardExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.customize.apiweb.BorrowTenderInfoCustomize;

/**
 * 2016新年活动 获取财神卡
 * 
 * @author Administrator
 * 
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class GetCardServiceImpl extends BaseServiceImpl implements GetCardService {
	Logger _log = LoggerFactory.getLogger(GetCardServiceImpl.class);
	private static final int TEN_THOUSAND = 10000;
	private static final String STR_JIN = "金";
	private static final String STR_JI = "鸡";
	private static final String STR_NA = "纳";
	private static final String STR_FU = "福";
	private static final Integer ACTIVITY_START_TIME = 1484841600;
	private static final Integer ACTIVITY_END_TIME = 1486223999;
	
	//private static final Integer ACTIVITY_START_TIME = 1484496942;
	//private static final Integer ACTIVITY_END_TIME = 1484612051;

	/**
	 * 出借送财神卡
	 */
	@Override
	public void updateGetCardTender(GetCardBean paramBean) throws Exception {
		Integer nowTime = GetDate.getNowTime10();
		String tenderNid = paramBean.getTenderNid();
		Integer userId = Integer.MIN_VALUE;
		BigDecimal tenderAccount = BigDecimal.ZERO;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tenderNid", tenderNid);
		// 判断是否汇直投和新手汇项目
		BorrowTenderInfoCustomize tenderInfo = this.getCardCustomizeMapper.getBorrowTender(paramMap);

		if (tenderInfo == null) {
			// 不是汇直投或新手汇项目
			// 判断是否汇添金
			DebtPlanAccedeExample debtExample = new DebtPlanAccedeExample();
			DebtPlanAccedeExample.Criteria debtCriteria = debtExample.createCriteria();
			// 出借编号
			debtCriteria.andAccedeOrderIdEqualTo(tenderNid);
			// 活动期范围
			debtCriteria.andCreateTimeBetween(ACTIVITY_START_TIME, ACTIVITY_END_TIME);
			// 查询是否存在该汇添金出借
			List<DebtPlanAccede> debtList = this.debtPlanAccedeMapper.selectByExample(debtExample);
			if (debtList == null || debtList.size() == 0) {
				// 不是汇直投、新手汇、汇添金
				_log.info("该出借不是汇直投、新手汇、汇添金或是出借时间不在活动期内！");
				return;
			} else {
				// 活动期内的汇添金出借
				tenderAccount = debtList.get(0).getAccedeAccount();
				userId = debtList.get(0).getUserId();
			}

		} else if (tenderInfo != null
				&& (tenderInfo.getAddtime() < ACTIVITY_START_TIME || tenderInfo.getAddtime() > ACTIVITY_END_TIME)) {
			// 是汇直投或新手汇项目，判断是否在活动期内
			_log.info("出借时间不在活动期内!");
			return;
		} else {
			// 活动期内的汇直投或新手汇出借
			tenderAccount = tenderInfo.getTenderAccount();
			userId = tenderInfo.getUserId();
		}
		// 每出借10000 发一张财神卡
		int sendCount = tenderAccount.divide(new BigDecimal(TEN_THOUSAND)).setScale(0, RoundingMode.DOWN).intValue();
		if (sendCount == 0) {
			_log.info("该出借金额不满10000不符合发放财神卡条件！");
			return;
		}
		List<String> getCardList = getRandomCard(sendCount);
		// 新年活动获取财神卡记录
		NewyearGetCard record = new NewyearGetCard();
		record.setAddTime(nowTime);
		record.setDelFlg(0);
		record.setGetCardType(1);
		record.setTenderNid(tenderNid);
		record.setUserId(userId);
		record.setGetFlg(1);
		this.newyearGetCardMapper.insertSelective(record);
		// 更新用户财富卡
		this.updateCardQuantity(userId, getCardList, nowTime, 1, tenderNid, StringUtils.EMPTY);
		_log.info("出借用户：" + userId + "，获得财神卡:" + getCardList.toString());
	}

	/**
	 * 发放财神卡
	 */
	@Override
	public void updateSendCard() throws Exception {
		Integer nowTime = GetDate.getNowTime10();
		NewyearGetCardExample example = new NewyearGetCardExample();
		// 未发放奖品
		example.createCriteria().andGetFlgEqualTo(0);
		// 查询所有活动期内注册但未发放奖品的用户
		List<NewyearGetCard> getCardBeanList = this.newyearGetCardMapper.selectByExample(example);
		for (NewyearGetCard getCardBean : getCardBeanList) {
			// 注册用户编号
			Integer userId = getCardBean.getUserId();
			// 邀请人
			Integer inviteUserId = getCardBean.getInviteUserId();
			Users inviteUser = null;
			Users user = this.usersMapper.selectByPrimaryKey(userId);
			if (user.getOpenAccount() == 0 || !checkExpiryDate(userId)) {
				_log.info("新注册用户尚未开户或开户时间不在活动期内！");
				continue;
			}
			// 有邀请人
			if (null != inviteUserId) {
				inviteUser = this.usersMapper.selectByPrimaryKey(inviteUserId);
			}
			// 注册人发放财富卡
			List<String> getCardList = getRandomCard(1);
			// 活动期内新用户注册且开户
			Integer getSource = 3;
			this.updateCardQuantity(userId, getCardList, nowTime, 3, StringUtils.EMPTY, StringUtils.EMPTY);
			_log.info("财神来敲我家门活动，新注册用户：" + userId + "，获得财神卡（" + getCardList.get(0) + "）");

			// 邀请人发放财富卡
			if (inviteUser != null) {
				getCardList = getRandomCard(1);
				// 被邀请用户的电话号
				String mobile = user == null ? StringUtils.EMPTY : user.getMobile();
				// 2:邀请用户
				getSource = 2;
				this.updateCardQuantity(inviteUserId, getCardList, nowTime, getSource, StringUtils.EMPTY, mobile);
				_log.info("财神来敲我家门活动，作为邀请人：" + userId + "，获得财神卡（" + getCardList.get(0) + "）");
			}
			// 已发放
			getCardBean.setGetFlg(1);
			// 更新状态
			this.newyearGetCardMapper.updateByPrimaryKeySelective(getCardBean);

		}

	}

	/**
	 * 活动期内注册或邀请好友注册
	 */
	@Override
	public void updateGetCardRegist(GetCardBean paramBean) throws Exception {
		Integer nowTime = GetDate.getNowTime10();
		// 注册用户编号
		Integer userId = paramBean.getUserId();
		// 邀请人
		String inviteUserId = paramBean.getInviteUserId();
		Users user = this.usersMapper.selectByPrimaryKey(userId);
		if (user == null) {
			// 用户不存在
			_log.info("用户不存在！");
			return;
		} else if (user.getRegTime() > ACTIVITY_END_TIME || user.getRegTime() < ACTIVITY_START_TIME) {
			// 判断注册时间
			_log.info("不在活动期内注册！");
			return;
		}
		if (StringUtils.isNotEmpty(inviteUserId)) {
			// 有邀请人
			Users inviteUser;
			if(inviteUserId.length() < 10){
			    inviteUser = this.usersMapper.selectByPrimaryKey(Integer.parseInt(inviteUserId));
			    if(inviteUser == null){
                    // 用户不存在
                    _log.info("邀请人不存在");
                    return;
                }
			}
			else {
			    UsersExample example = new UsersExample();
			    example.createCriteria().andMobileEqualTo(inviteUserId);
			    List<Users> inviteUsers = this.usersMapper.selectByExample(example);
			    if(inviteUsers == null || inviteUsers.isEmpty()){
			        // 用户不存在
			        _log.info("邀请人不存在");
			        return;
			    }
			    inviteUserId = String.valueOf(inviteUsers.get(0).getUserId());
			}
		}

		// 新年活动获取财神卡记录
		// 活动期内邀请用户注册发放一张财神卡
		NewyearGetCard registUser = new NewyearGetCard();
		registUser.setUserId(userId);
		registUser.setAddTime(nowTime);
		registUser.setDelFlg(0);
		// 新注册
		registUser.setGetCardType(3);
		// 邀请人
		if (StringUtils.isNotEmpty(inviteUserId)) {
		    registUser.setInviteUserId(Integer.parseInt(inviteUserId));
		}
		this.newyearGetCardMapper.insertSelective(registUser);
		_log.info("财神来敲我家门活动，新注册用户：" + userId + "，推荐人:" + (inviteUserId == null ? "无" : inviteUserId));
	}

	/**
	 * 更新用户财富卡
	 * 
	 * @param userId
	 * @param getCardList
	 * @param nowTime
	 * @param getSource
	 * @param tenderNid
	 * @param mobile
	 */
	private synchronized void updateCardQuantity(Integer userId, List<String> getCardList, Integer nowTime, Integer getSource,
			String tenderNid, String mobile) {
		for (String card : getCardList) {
			// 用户财神卡总数量表
			NewyearCaishenCardQuantityExample example = new NewyearCaishenCardQuantityExample();
			example.createCriteria().andUserIdEqualTo(userId);
			List<NewyearCaishenCardQuantity> cardQuantityList = this.newyearCaishenCardQuantityMapper
					.selectByExample(example);
			NewyearCaishenCardQuantity cardQuantity = null;
			// 第一次获取财神卡
			if (cardQuantityList == null || cardQuantityList.size() == 0) {
				cardQuantity = new NewyearCaishenCardQuantity();
				cardQuantity.setUserId(userId);
				if (StringUtils.equals(STR_JIN, card)) {
					// 财神卡（金）
					cardQuantity.setCardJinQuantity(1);
				} else if (StringUtils.equals(STR_JI, card)) {
					// 财神卡（鸡）
					cardQuantity.setCardJiQuantity(1);
				} else if (StringUtils.equals(STR_NA, card)) {
					// 财神卡（纳）
					cardQuantity.setCardNaQuantity(1);
				} else {
					// 财神卡（福）
					cardQuantity.setCardFuQuantity(1);
				}
				cardQuantity.setAddTime(nowTime);
				cardQuantity.setUpdateTime(nowTime);
				// 插入一条数据
				this.newyearCaishenCardQuantityMapper.insertSelective(cardQuantity);
			} else {
				// 已经获取过财神卡
				cardQuantity = cardQuantityList.get(0);
				if (StringUtils.equals(STR_JIN, card)) {
					// 财神卡（金）
					cardQuantity.setCardJinQuantity(cardQuantity.getCardJinQuantity() + 1);
				} else if (StringUtils.equals(STR_JI, card)) {
					// 财神卡（鸡）
					cardQuantity.setCardJiQuantity(cardQuantity.getCardJiQuantity() + 1);
				} else if (StringUtils.equals(STR_NA, card)) {
					// 财神卡（纳）
					cardQuantity.setCardNaQuantity(cardQuantity.getCardNaQuantity() + 1);
				} else {
					// 财神卡（福）
					cardQuantity.setCardFuQuantity(cardQuantity.getCardFuQuantity() + 1);
				}
				cardQuantity.setUpdateTime(nowTime);
				// 更新数据
				this.newyearCaishenCardQuantityMapper.updateByPrimaryKeySelective(cardQuantity);
			}

			// 用户财神卡记录表
			NewyearCaisheCardUser cardUser = new NewyearCaisheCardUser();
			cardUser.setUserId(userId);
			if (StringUtils.equals(STR_JIN, card)) {
				// 财神卡（金）
				cardUser.setCardType(1);
			} else if (StringUtils.equals(STR_JI, card)) {
				// 财神卡（鸡）
				cardUser.setCardType(2);
			} else if (StringUtils.equals(STR_NA, card)) {
				// 财神卡（纳）
				cardUser.setCardType(3);
			} else {
				// 财神卡（福）
				cardUser.setCardType(4);
			}
			// 财神卡（金）数量
			cardUser.setCardJinQuantity(cardQuantity.getCardJinQuantity());
			// 财神卡（鸡）数量
			cardUser.setCardJiQuantity(cardQuantity.getCardJiQuantity());
			// 财神卡（纳）数量
			cardUser.setCardNaQuantity(cardQuantity.getCardNaQuantity());
			// 财神卡（福）数量
			cardUser.setCardFuQuantity(cardQuantity.getCardFuQuantity());
			
			// 获得
			cardUser.setOperateType(1);
			if (getSource == 1) {
				// 获取来源-出借
				cardUser.setCardSource(4);
				// 出借编号
				cardUser.setRemark(tenderNid);
			} else if (getSource == 2) {
				// 获取来源-邀请好友
				cardUser.setCardSource(1);
				// 出借编号
				cardUser.setRemark(mobile);
			} else {
				// 获取来源-注册且开户
				cardUser.setCardSource(6);
			}

			cardUser.setAddTime(nowTime);
			cardUser.setDelFlg(0);
			this.newyearCaisheCardUserMapper.insertSelective(cardUser);
		}
	}

	/**
	 * 生成金鸡纳福财神卡
	 * 
	 * @param count
	 * @return
	 */
	private List<String> getRandomCard(int count) {
		List<String> getCardList = new ArrayList<String>();
		String[] sourceCode = new String[] { STR_JIN, STR_JI, STR_NA, STR_FU };
		List<String> list = Arrays.asList(sourceCode);
		for (int i = 0; i < count; i++) {
			Collections.shuffle(list);
			String card = list.get(0);
			getCardList.add(card);
		}
		return getCardList;
	}

	/**
	 * 判断是否有效期内注册用户
	 * 
	 * @param userId
	 * @return
	 */
	private boolean checkExpiryDate(int userId) {
		AccountChinapnrExample example = new AccountChinapnrExample();
		AccountChinapnrExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		criteria.andAddtimeGreaterThanOrEqualTo(ACTIVITY_START_TIME.toString());
		criteria.andAddtimeLessThanOrEqualTo(ACTIVITY_END_TIME.toString());
		int count = this.accountChinapnrMapper.countByExample(example);
		if (count == 1) {
			return true;
		}
		return false;
	}

}
