package com.hyjf.batch.recommend.send;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountChinapnrExample;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.InviteInfo;
import com.hyjf.mybatis.model.auto.InviteInfoExample;
import com.hyjf.mybatis.model.auto.InviteRecommend;
import com.hyjf.mybatis.model.auto.InviteRecommendExample;
import com.hyjf.mybatis.model.auto.Users;

/**
 * 发放推荐星
 * 
 * @author zhangjinpeng
 * 
 */
@Service
public class RecommendSendServiceImpl extends BaseServiceImpl implements RecommendSendService {

	private static final String THIS_CLASS = RecommendSendServiceImpl.class.getName();
	/**
	 * 出借满3000元 算作一次有效邀请
	 */
	private static final BigDecimal TENDER_ACCOUNT = new BigDecimal(3000);

	/**
	 * 3次有效邀请 送一颗推荐星
	 */
	private static final int RECOMMEND_TIMES = 3;
	Logger _log = LoggerFactory.getLogger(RecommendSendServiceImpl.class);

	/**
	 * 注册开户发放1颗推荐星
	 */
	@Override
	public void updateOpenAccountSend() throws Exception {
		String methodName = "updateOpenAccountSend";
		Integer nowTime = GetDate.getNowTime10();
		try {
			Properties properties = PropUtils.getSystemResourcesProperties();
			String activityId = properties.getProperty(CustomConstants.MGM10_ACTIVITY_ID).trim();
			// 取得当前活动
			ActivityList activity = this.activityListMapper.selectByPrimaryKey(Integer.valueOf(activityId));
			
			InviteInfoExample example = new InviteInfoExample();
			InviteInfoExample.Criteria criteria = example.createCriteria();
			criteria.andRecommendSourceEqualTo(1);
			criteria.andSendFlagEqualTo(0);
			// 检索所有已注册但未发放推荐星的数据
			List<InviteInfo> inviteInfoList = this.inviteInfoMapper.selectByExample(example);
			for (InviteInfo inviteInfo : inviteInfoList) {
				// 检索相应的被邀请用户是否已开户
				Users user = this.usersMapper.selectByPrimaryKey(inviteInfo.getInviteByUser());
				// 用户是否已开户
				if (user.getOpenAccount() == 1) {
					// 用户已开户 则比较开户时间是否在活动期内
					AccountChinapnrExample exampleAccount = new AccountChinapnrExample();
					exampleAccount.createCriteria().andUserIdEqualTo(inviteInfo.getInviteByUser());
					// 取得用户开户信息
					List<AccountChinapnr> accountList = this.accountChinapnrMapper.selectByExample(exampleAccount);
					Integer openAccountTime = Integer.MIN_VALUE;
					if (accountList != null && accountList.size() > 0) {
						// 取得开户时间
						openAccountTime = Integer.valueOf(accountList.get(0).getAddtime());
						if (openAccountTime < activity.getTimeStart() || openAccountTime > activity.getTimeEnd()) {
							// 开户时间不在活动期内，则跳出本次循环
							continue;
						}
					}
					// 发放状态-已发放
					inviteInfo.setSendFlag(1);
					// 出借状态-未满3000元
					inviteInfo.setTenderStatus(0);
					inviteInfo.setUpdateTime(nowTime);
					inviteInfo.setSendTime(nowTime);
					this.inviteInfoMapper.updateByPrimaryKeySelective(inviteInfo);
					// 更新用户推荐星数量
					this.updateRecommentCount(inviteInfo.getInviteUser(), 1);
					_log.info(THIS_CLASS + "==>" + methodName + "==>" + "用户：" + inviteInfo.getInviteUser()
							+ "邀请用户注册开户成功,发放1颗推荐星！");
				}
			}
		} catch (Exception e) {
			_log.info(THIS_CLASS + "==>" + methodName + "==>" + "发放推荐星异常！");
			throw new RuntimeException("发放推荐星异常！");
		}
	}

	/**
	 * 有效邀请发放2颗推荐星
	 */
	@Override
	public void updateTenderSend() throws Exception {
		String methodName = "updateTenderSend";
		Integer nowTime = GetDate.getNowTime10();
		BigDecimal tenderAccount = BigDecimal.ZERO;
		try {
			InviteInfoExample example = new InviteInfoExample();
			InviteInfoExample.Criteria criteria = example.createCriteria();
			criteria.andRecommendSourceEqualTo(1);
			criteria.andSendFlagEqualTo(1);
			criteria.andTenderStatusEqualTo(0);
			// 检索所有已注册开户且已发放推荐星且出借不满3000元的数据
			List<InviteInfo> inviteInfoList = this.inviteInfoMapper.selectByExample(example);
			// 遍历符合条件的注册开户数据
			for (InviteInfo inviteInfo : inviteInfoList) {
				tenderAccount = BigDecimal.ZERO;
				// 自注册之日起30天内累计出借达3000元
				Date endDateTemp = GetDate.countDate(GetDate.timestamptoStrYYYYMMDDHHMMSS(inviteInfo.getAddTime()), 5,
						30);
				Integer endDate = (int) (endDateTemp.getTime() / 1000);
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("userId", inviteInfo.getInviteByUser());
				paramMap.put("startDate", inviteInfo.getAddTime());
				paramMap.put("endDate", endDate);
				tenderAccount = this.mgm10CustomizeMapper.getTenderAccountSum(paramMap);
				// 自注册之日起30天内累计出借达3000元的发放2颗推荐星
				if (tenderAccount != null && tenderAccount.compareTo(TENDER_ACCOUNT) != -1) {
					InviteInfo newInviteInfo = new InviteInfo();
					BeanUtils.copyProperties(newInviteInfo, inviteInfo);
					// 取得推荐星来源-有效邀请
					newInviteInfo.setRecommendSource(2);
					// 获取推荐星数量-2颗
					newInviteInfo.setRecommendCount(2);
					// 发放状态-已发放
					newInviteInfo.setSendFlag(1);
					// 出借状态-已满3000
					newInviteInfo.setTenderStatus(1);
					newInviteInfo.setSendTime(nowTime);
					newInviteInfo.setAddTime(nowTime);
					newInviteInfo.setUpdateTime(nowTime);
					newInviteInfo.setDelFlg(0);
					// 新生成一条有效邀请数据
					this.inviteInfoMapper.insertSelective(newInviteInfo);
					// 更新出借状态
					inviteInfo.setTenderStatus(1);
					this.inviteInfoMapper.updateByPrimaryKeySelective(inviteInfo);
					// 更新用户推荐星数量
					this.updateRecommentCount(inviteInfo.getInviteUser(), 2);
					_log.info(THIS_CLASS + "==>" + methodName + "==>" + "用户：" + inviteInfo.getInviteUser()
							+ "一次有效邀请,发放2颗推荐星！");
				}
			}
		} catch (Exception e) {
			_log.info(THIS_CLASS + "==>" + methodName + "==>" + "发放推荐星异常！");
			throw new RuntimeException("发放推荐星异常！");
		}

	}

	/**
	 * 3次有效邀请发放1颗推荐星
	 */
	@Override
	public void updateThrInviteSend() throws Exception {
		String methodName = "updateThrInviteSend";
		Integer nowTime = GetDate.getNowTime10();
		try {
			List<Integer> userIdList = this.mgm10CustomizeMapper.selectThrUser();
			for (Integer userId : userIdList) {
				InviteInfoExample example = new InviteInfoExample();
				InviteInfoExample.Criteria criteria = example.createCriteria();
				// 邀请用户编号
				criteria.andInviteUserEqualTo(userId);
				// 有效邀请
				criteria.andRecommendSourceEqualTo(2);
				// 没有分组
				criteria.andGroupCodeIsNull();
				List<InviteInfo> inviteInfoList = this.inviteInfoMapper.selectByExample(example);
				// 取余计算 得到不满三次有效邀请的数量（或0或1或2）
				int noSendCount = inviteInfoList.size() % RECOMMEND_TIMES;
				InviteInfo inviteInfo = null;
				// 生成组编号
				String groupCode = this.getGroupCode();
				for (int i = 0; i < inviteInfoList.size() - noSendCount; i++) {
					if (i % RECOMMEND_TIMES == 0) {
						// 每满3次重新生成一个组编号
						groupCode = this.getGroupCode();
					}
					inviteInfo = inviteInfoList.get(i);
					inviteInfo.setGroupCode(groupCode);
					inviteInfo.setUpdateTime(nowTime);
					// 更新分组信息
					this.inviteInfoMapper.updateByPrimaryKeySelective(inviteInfo);
					if ((i + 1) % RECOMMEND_TIMES == 0) {
						// 发放一颗推荐星
						InviteInfo newInviteInfo = new InviteInfo();
						newInviteInfo.setInviteUser(inviteInfo.getInviteUser());
						newInviteInfo.setGroupCode(groupCode);
						newInviteInfo.setRecommendSource(3);
						newInviteInfo.setRecommendCount(1);
						newInviteInfo.setSendFlag(1);
						newInviteInfo.setSendTime(nowTime);
						newInviteInfo.setUpdateTime(nowTime);
						newInviteInfo.setAddTime(nowTime);
						newInviteInfo.setDelFlg(0);
						// 每三次有效邀请 发放1颗推荐星
						this.inviteInfoMapper.insertSelective(newInviteInfo);
						// 更新用户推荐星数量
						this.updateRecommentCount(inviteInfo.getInviteUser(), 1);
						_log.info(THIS_CLASS + "==>" + methodName + "==>" + "用户：" + newInviteInfo.getInviteUser()
								+ "三次有效邀请,赠送1颗推荐星！");
					}
				}
			}

		} catch (Exception e) {
			_log.info(THIS_CLASS + "==>" + methodName + "==>" + "发放推荐星异常！");
			throw new RuntimeException("发放推荐星异常！");
		}

	}

	/**
	 * 生成组编号（10位时间戳+6位随机数）
	 * 
	 * @return
	 */
	private String getGroupCode() {
		return GetDate.getNowTime10() + GetCode.getRandomCodeCoupon(6);
	}

	/**
	 * 更新用户推荐星的数量
	 * 
	 * @param userId
	 * @param recommendCount
	 */
	private void updateRecommentCount(Integer userId, Integer recommendCount) {
		Integer nowTime = GetDate.getNowTime10();
		InviteRecommendExample example = new InviteRecommendExample();
		InviteRecommendExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		List<InviteRecommend> recommendList = this.inviteRecommendMapper.selectByExample(example);
		// 用户已存在 则更新推荐星数量
		if (recommendList != null && recommendList.size() > 0) {
			// 更新用户推荐星数量
			Map<String, Object> paramMapCount = new HashMap<String, Object>();
			paramMapCount.put("userId", userId);
			// 一次有效邀请的给邀请人 发放2颗推荐星
			paramMapCount.put("recommendCount", recommendCount);

			this.mgm10CustomizeMapper.updateUserRecommend(paramMapCount);
		} else {
			// 用户不存在，则新插入数据
			InviteRecommend record = new InviteRecommend();
			record.setAddTime(nowTime);
			// 默认黑名单用户
			record.setBlackUser(1);
			record.setDelFlg(0);
			record.setPrizeAllCount(recommendCount);
			record.setPrizeUsedCount(0);
			record.setUpdateTime(nowTime);
			record.setUserId(userId);
			this.inviteRecommendMapper.insertSelective(record);
		}

	}
}
