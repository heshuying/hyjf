package com.hyjf.admin.manager.activity.actnov2017.bargain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.security.utils.BASE64;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.ActJanBargain;
import com.hyjf.mybatis.model.auto.ActJanBargainExample;
import com.hyjf.mybatis.model.auto.ActJanPrizewinList;
import com.hyjf.mybatis.model.auto.ActJanPrizewinListExample;

@Service
public class ActBargainServiceImpl extends BaseServiceImpl implements ActBargainService {

	@Override
	public List<ActJanPrizewinList> selectPrizeWinList(int limitStart, int limitEnd) {
        ActJanPrizewinListExample example = new ActJanPrizewinListExample();
        example.setLimitStart(limitStart);
        example.setLimitEnd(limitEnd);
        example.setOrderByClause("update_time desc");
        
        List<ActJanPrizewinList> resultList = actJanPrizewinListMapper.selectByExample(example);
        
        if(resultList == null){
        	return new ArrayList<ActJanPrizewinList>();
        }
        
		return resultList;
    }
	
	/**
	 * 获取中奖纪录总数
	 * @return
	 */
	@Override
	public int countPrizeWinList(){
		ActJanPrizewinListExample example = new ActJanPrizewinListExample();
		
		return actJanPrizewinListMapper.countByExample(example);
	}
	
	/**
	 * 获取砍价列表
	 * @param form
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	@Override
	public List<ActJanBargain> selectBargainList(ActBargainBean form, int limitStart, int limitEnd){
		ActJanBargainExample example = new ActJanBargainExample();
		ActJanBargainExample.Criteria creteria = example.createCriteria();
		
		if(StringUtils.isNotEmpty(form.getWechatNameSrch())){
			creteria.andWechatNameEqualTo(form.getWechatNameSrch());
		}
		if(StringUtils.isNotEmpty(form.getWechatNickNameSrch())){
			creteria.andWechatNicknameEqualTo(BASE64.encode(form.getWechatNickNameSrch().getBytes()));
		}
		if(StringUtils.isNotEmpty(form.getWechatNameHelpSrch())){
			creteria.andWechatNameHelpEqualTo(form.getWechatNameHelpSrch());
		}
		if(StringUtils.isNotEmpty(form.getWechatNickNameHelpSrch())){
			creteria.andWechatNicknameHelpEqualTo(BASE64.encode(form.getWechatNickNameHelpSrch().getBytes()));
		}
		if(StringUtils.isNotEmpty(form.getMobileSrch())){
			creteria.andMobileEqualTo(form.getMobileSrch());
		}
		if(StringUtils.isNotEmpty(form.getBargainMoneySrch())){
			creteria.andMoneyBargainEqualTo(new BigDecimal(form.getBargainMoneySrch()));
		}
		if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
			creteria.andUpdateTimeGreaterThan(GetDate.getDayStart10(form.getTimeStartSrch()));
		}
		if(StringUtils.isNotEmpty(form.getTimeEndSrch())){
			creteria.andUpdateTimeLessThan(GetDate.getDayEnd10(form.getTimeEndSrch()));
		}
		
		example.setLimitStart(limitStart);
        example.setLimitEnd(limitEnd);
        example.setOrderByClause("update_time desc");
        List<ActJanBargain> resultList = actJanBargainMapper.selectByExample(example);
		
        if(resultList == null){
        	return new ArrayList<ActJanBargain>();
        }
        
		return resultList;
	}
	
	/**
	 * 获取砍价列表纪录总数
	 * @param form
	 * @return
	 */
	@Override
	public int countBargainList(ActBargainBean form){
		ActJanBargainExample example = new ActJanBargainExample();
		ActJanBargainExample.Criteria creteria = example.createCriteria();
		
		if(StringUtils.isNotEmpty(form.getWechatNameSrch())){
			creteria.andWechatNameEqualTo(form.getWechatNameSrch());
		}
		if(StringUtils.isNotEmpty(form.getWechatNickNameSrch())){
			creteria.andWechatNicknameEqualTo(form.getWechatNickNameSrch());
		}
		if(StringUtils.isNotEmpty(form.getWechatNameHelpSrch())){
			creteria.andWechatNameHelpEqualTo(form.getWechatNameHelpSrch());
		}
		if(StringUtils.isNotEmpty(form.getWechatNickNameHelpSrch())){
			creteria.andWechatNicknameHelpEqualTo(form.getWechatNickNameHelpSrch());
		}
		if(StringUtils.isNotEmpty(form.getMobileSrch())){
			creteria.andMobileEqualTo(form.getMobileSrch());
		}
		if(StringUtils.isNotEmpty(form.getBargainMoneySrch())){
			creteria.andMoneyBargainEqualTo(new BigDecimal(form.getBargainMoneySrch()));
		}
		if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
			creteria.andUpdateTimeGreaterThan(GetDate.getDayStart10(form.getTimeStartSrch()));
		}
		if(StringUtils.isNotEmpty(form.getTimeEndSrch())){
			creteria.andUpdateTimeLessThan(GetDate.getDayEnd10(form.getTimeEndSrch()));
		}

		return actJanBargainMapper.countByExample(example);
	}
    
    

}
