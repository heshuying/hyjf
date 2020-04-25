package com.hyjf.batch.mgm10.blackuser;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.BorrowTenderExample;
import com.hyjf.mybatis.model.auto.InviteInfo;
import com.hyjf.mybatis.model.auto.InviteInfoExample;
import com.hyjf.mybatis.model.auto.InviteRecommend;
import com.hyjf.mybatis.model.auto.InviteRecommendExample;

/**
 * 10月份活动--筛选黑名单用户
 * 
 * @author zhangjinpeng
 * 
 */
@Service
public class Mgm10BlackUserServiceImpl extends BaseServiceImpl implements Mgm10BlackUserService {

	private static final String THIS_CLASS = Mgm10BlackUserServiceImpl.class.getName();

	Logger _log = LoggerFactory.getLogger(Mgm10BlackUserServiceImpl.class);

	/**
	 * 10月份活动--筛选黑名单用户--更新黑名单用户状态
	 */
	@Override
	public void updateBlackUser() throws Exception {
		String methodName = "selectBlackUser";
		Integer nowTime = GetDate.getNowTime10();
		InviteRecommendExample example = new InviteRecommendExample();
		InviteRecommendExample.Criteria criteria = example.createCriteria();
		criteria.andBlackUserEqualTo(1);
		criteria.andDelFlgEqualTo(0);
		// 查询所有的黑名单用户
		List<InviteRecommend> recommendList = this.inviteRecommendMapper.selectByExample(example);
		Integer blackUserCount = recommendList == null ? 0 : recommendList.size();
		_log.info(THIS_CLASS + "==>" + methodName + "==>10月份MGM活动-成功筛选黑名单用户数量：" + blackUserCount);
		if (recommendList != null && recommendList.size() > 0) {
			// 遍历黑名单用户
			for (InviteRecommend recommend : recommendList) {
				InviteInfoExample inviteInfoExample = new InviteInfoExample();
				InviteInfoExample.Criteria inviteInfoCriteria = inviteInfoExample.createCriteria();
				inviteInfoCriteria.andInviteUserEqualTo(recommend.getUserId());
				// 查询黑名单用户所有邀请到的用户
				List<InviteInfo> inviteInfoList = this.inviteInfoMapper.selectByExample(inviteInfoExample);
				boolean isBlackUser = true;
				// 1.遍历当前黑名单用户是否有有效邀请
				for (InviteInfo info : inviteInfoList) {
					if (2 == info.getRecommendSource()) {
						// 如有 有效邀请
						isBlackUser = false;
						recommend.setBlackUser(0);
						recommend.setUpdateTime(nowTime);
						// 更新当前用户为白名单
						this.inviteRecommendMapper.updateByPrimaryKeySelective(recommend);
						_log.info(THIS_CLASS + "==>" + methodName + "==>10月份MGM活动-成功更新黑名单用户：" + recommend.getUserId());
						// 跳出循环
						break;
					}
				}
				// 2.如果还是黑名单用户，则继续判断该用户是否有过任意出借
				if (isBlackUser) {
					for (InviteInfo info : inviteInfoList) {
						BorrowTenderExample tenderExample = new BorrowTenderExample();
						BorrowTenderExample.Criteria tenderCriteria = tenderExample.createCriteria();
						tenderCriteria.andUserIdEqualTo(info.getInviteByUser());
						List<BorrowTender> borrowTenderList = this.borrowTenderMapper.selectByExample(tenderExample);
						if (borrowTenderList != null && borrowTenderList.size() > 0) {
							// 如有任意出借，则更新成白名单用户
							recommend.setBlackUser(0);
							recommend.setUpdateTime(nowTime);
							this.inviteRecommendMapper.updateByPrimaryKeySelective(recommend);
							_log.info(THIS_CLASS + "==>" + methodName + "==>10月份MGM活动-成功更新黑名单用户：" + recommend.getUserId());
							// 跳出循环
							break;
						}

					}
				}

			}
		}

	}

}
