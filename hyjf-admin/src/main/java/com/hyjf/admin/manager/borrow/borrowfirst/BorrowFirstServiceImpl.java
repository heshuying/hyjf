package com.hyjf.admin.manager.borrow.borrowfirst;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.mybatis.model.auto.BorrowAppointExample;
import com.hyjf.mybatis.model.auto.BorrowBail;
import com.hyjf.mybatis.model.auto.BorrowBailExample;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.AdminSystem;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowFirstCustomize;

@Service
public class BorrowFirstServiceImpl extends BaseServiceImpl implements BorrowFirstService {
	
	@Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;
	
	Logger _log = LoggerFactory.getLogger(BorrowFirstServiceImpl.class);

	/**
	 * COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	@Override
	public Integer countBorrowFirst(BorrowCommonCustomize corrowCommonCustomize) {
		return this.borrowFirstCustomizeMapper.countBorrowFirst(corrowCommonCustomize);
	}

	/**
	 * 借款列表
	 * 
	 * @return
	 */
	@Override
	public List<BorrowFirstCustomize> selectBorrowFirstList(BorrowCommonCustomize corrowCommonCustomize) {
		return this.borrowFirstCustomizeMapper.selectBorrowFirstList(corrowCommonCustomize);
	}

	/**
	 * 已缴保证金
	 * 
	 * @param record
	 */
	@Override
	public boolean saveBailRecord(String borrowPreNid) {
		// 项目编号存在
		if (StringUtils.isNotEmpty(borrowPreNid)) {
			BorrowExample example = new BorrowExample();
			BorrowExample.Criteria cra = example.createCriteria();
			cra.andBorrowNidEqualTo(borrowPreNid);
			List<BorrowWithBLOBs> borrowList = this.borrowMapper.selectByExampleWithBLOBs(example);
			if (borrowList != null && borrowList.size() == 1) {
				BorrowWithBLOBs borrowWithBLOBs = borrowList.get(0);
				// 该项目编号没有交过保证金
				BorrowBailExample exampleBail = new BorrowBailExample();
				BorrowBailExample.Criteria craBail = exampleBail.createCriteria();
				craBail.andBorrowNidEqualTo(borrowWithBLOBs.getBorrowNid());
				List<BorrowBail> borrowBailList = this.borrowBailMapper.selectByExample(exampleBail);
				if (borrowBailList == null || borrowBailList.size() == 0) {
					AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
					BorrowBail borrowBail = new BorrowBail();
					// 借款人的ID
					borrowBail.setBorrowUid(borrowWithBLOBs.getUserId());
					// 操作人的ID
					borrowBail.setOperaterUid(Integer.valueOf(adminSystem.getId()));
					// 项目编号
					borrowBail.setBorrowNid(borrowWithBLOBs.getBorrowNid());
					// 保证金数值
					BigDecimal bailPercent = new BigDecimal(this.getBorrowConfig(CustomConstants.BORROW_BAIL_RATE));// 计算公式：保证金金额=借款金额×3％
					BigDecimal accountBail = (borrowWithBLOBs.getAccount()).multiply(bailPercent).setScale(2, BigDecimal.ROUND_DOWN);
					borrowBail.setBailNum(accountBail);
					// 10位系统时间（到秒）
					borrowBail.setUpdatetime(GetDate.getNowTime10());
					boolean bailFlag = this.borrowBailMapper.insertSelective(borrowBail) > 0 ? true : false;
					if (bailFlag) {
						borrowWithBLOBs.setVerifyStatus(1);
						boolean borrowFlag = this.borrowMapper.updateByPrimaryKeyWithBLOBs(borrowWithBLOBs) > 0 ? true : false;
						if (borrowFlag) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * 已交保证金
	 * 
	 * @param borrow
	 * @return
	 * @author Administrator
	 */

	@Override
	public boolean getBorrowBail(String borrowNid) {
		BorrowBailExample example = new BorrowBailExample();
		BorrowBailExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);
		List<BorrowBail> list = this.borrowBailMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return true;
		}

		return false;
	}

	/**
	 * 更新
	 * 
	 * @param record
	 */
	@Override
	public void updateOntimeRecord(BorrowFirstBean borrowBean,Integer count) {
		// 插入时间
		int systemNowDateLong = GetDate.getNowTime10();
		Date systemNowDate = GetDate.getDate(systemNowDateLong);
		String borrowNid = borrowBean.getBorrowNid();
		if (StringUtils.isNotEmpty(borrowNid)) {
			BorrowExample borrowExample = new BorrowExample();
			BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
			borrowCra.andBorrowNidEqualTo(borrowNid);
			List<BorrowWithBLOBs> borrowList = this.borrowMapper.selectByExampleWithBLOBs(borrowExample);
			if (borrowList != null && borrowList.size() == 1) {
				BorrowWithBLOBs borrow = borrowList.get(0);
				// 剩余的金额
				borrow.setBorrowAccountWait(borrow.getAccount());
				int time = systemNowDateLong;
				// 当发标状态为立即发标时插入系统时间
				if (borrowBean.getVerifyStatus() != null && StringUtils.isNotEmpty(borrowBean.getVerifyStatus())) {
					// 发标方式为”暂不发标2“或者”定时发标 3“时，项目状态变为”待发布“
					if (Integer.valueOf(borrowBean.getVerifyStatus()) == 2 || Integer.valueOf(borrowBean.getVerifyStatus()) == 3) {
						// 定时发标
						if (Integer.valueOf(borrowBean.getVerifyStatus()) == 3) {
							// 发标时间
							borrow.setOntime(GetDate.strYYYYMMDDHHMMSS2Timestamp(borrowBean.getOntime()));
						} else if (Integer.valueOf(borrowBean.getVerifyStatus()) == 2) {
							// 发标时间
							borrow.setOntime(0);
						}
						// 状态
						borrow.setStatus(1);
						// 初审状态
						borrow.setVerifyStatus(Integer.valueOf(borrowBean.getVerifyStatus()));
					}
					// 发标方式为”立即发标 2“时，项目状态变为”出借中
					else if (Integer.valueOf(borrowBean.getVerifyStatus()) == 4) {
						// 是否可以进行借款
						borrow.setBorrowStatus(1);
						// 初审时间
						borrow.setVerifyTime(String.valueOf(GetDate.getNowTime10()));
						// 发标的状态
						borrow.setVerifyStatus(Integer.valueOf(borrowBean.getVerifyStatus()));
						// 状态
						borrow.setStatus(2);
						// 借款到期时间
						borrow.setBorrowEndTime(String.valueOf(time + borrow.getBorrowValidTime() * 86400));
						
						// 根据此标的是否跑引擎操作redis ：0未使用 1使用
						if(count.equals(0)){
							// borrowNid，借款的borrowNid,account借款总额
							RedisUtils.set(borrowNid, borrow.getAccount().toString());
						}
						
						// upd by liushouyi HJH3
						if (!"10000000".equals(borrow.getInstCode())) {
							// 三方资产更新资产表状态
							HjhPlanAsset hjhPlanAssetnew = this.getHjhPlanAsset(borrowNid);
							// 受托支付，更新为待授权
							hjhPlanAssetnew.setStatus(7);//7 出借中
							//获取当前时间
							int nowTime = GetDate.getNowTime10();
							hjhPlanAssetnew.setUpdateTime(nowTime);
							hjhPlanAssetnew.setUpdateUserId(1);
							this.hjhPlanAssetMapper.updateByPrimaryKeySelective(hjhPlanAssetnew);
						}
					}
					// 更新时间
					borrow.setUpdatetime(systemNowDate);
					this.borrowMapper.updateByExampleSelective(borrow, borrowExample);
					//只有关联计划 且 立即发标才能给MQ发消息
/*					if(borrow.getIsEngineUsed().equals(1) && Integer.valueOf(borrowBean.getVerifyStatus()) == 4){
						// 成功后到关联计划队列
						this.sendToMQ(borrow,  RabbitMQConstants.ROUTINGKEY_BORROW_ISSUE);
						_log.info(borrowNid + "已发送至MQ");
					}*/
					// 删写redis的定时发标时间
					changeOntimeOfRedis(borrow);
				}
			}
		}
	}
	
	/**
	 * @param borrow
	 */
		
	private void changeOntimeOfRedis(BorrowWithBLOBs borrow) {
		if (borrow.getVerifyStatus() == 3){
			//定时发标 写定时发标时间 redis 有效期10天 
			RedisUtils.set(borrow.getBorrowNid()+CustomConstants.UNDERLINE+
					CustomConstants.REDIS_KEY_ONTIME, String.valueOf(borrow.getOntime()), 864000);
		} else{
			//非定时发标 删redis
			RedisUtils.del(borrow.getBorrowNid()+CustomConstants.UNDERLINE+
					CustomConstants.REDIS_KEY_ONTIME);
		}
	}
	/**
	 * 获取用户名
	 * 
	 * @param record
	 */
	@Override
	public String getUserName(Integer userId) {
		Users users = this.usersMapper.selectByPrimaryKey(userId);
		if (users != null && StringUtils.isNotEmpty(users.getUsername())) {
			return users.getUsername();
		} else {
			return "";
		}
	}

	@Override
	public String sumBorrowFirstAccount(BorrowCommonCustomize corrowCommonCustomize) {
		String sumAccount = this.borrowFirstCustomizeMapper.sumBorrowFirstAccount(corrowCommonCustomize);
		return sumAccount;
	}

	@Override
	public Boolean hasBookingRecord(String nid) {
		BorrowAppointExample example = new BorrowAppointExample();
		List<Integer> tempList = new ArrayList<Integer>();
		tempList.add(0);
		tempList.add(1);
		example.createCriteria().andBorrowNidEqualTo(nid).andAppointStatusIn(tempList);
		int countSize = this.borrowAppointMapper.countByExample(example);
		if (countSize != 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 推送消息到MQ
	 * 
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param borrowNid
	 * @param routingKey
	 * @author PC-LIUSHOUYI
	 */
    @Override
    public void sendToMQ(String borrowNid,String routingKey){
		// 加入到消息队列 
        Map<String, String> params = new HashMap<String, String>();
        params.put("mqMsgId", GetCode.getRandomCode(10));
    	params.put("borrowNid", borrowNid);
        params.put("instCode", "10000000");
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, routingKey, JSONObject.toJSONString(params));
	}
}
