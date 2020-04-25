package com.hyjf.activity.actdoubleeleven.bargain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.ActJanPrize;
import com.hyjf.mybatis.model.auto.SmsConfig;
import com.hyjf.mybatis.model.customize.act.ActNovBargainCustomize;
import com.hyjf.mybatis.model.customize.act.ActNovPrizeCustomize;
import com.hyjf.mybatis.model.customize.act.ActNovPrizeDetailCustomize;

public interface BargainService extends BaseService {

	List<ActNovPrizeCustomize> getPrizeList(Map<String,Object> paraMap);

	ActNovPrizeDetailCustomize getPrizeDetail(Map<String,Object> paraMap);

	Integer selectBargainCount(Map<String,Object> paraMap);

	List<ActNovBargainCustomize> selectBargainList(Map<String,Object> paraMap);

	BigDecimal getCurrentBargainMoney(Integer prizeId, String wechatId);

	Integer getCurrentBargainCount(Integer prizeId, String wechatId);

	BigDecimal getRandomBargainMoney(Integer level);

	ActJanPrize getPrizeById(Integer prizeId);

	Integer insertBargainRecord(BargainRequestBean bargainRequestBean, BigDecimal bargainMoney, String clientIp, ActJanPrize prize);

	BigDecimal updateBargainDouble(Integer idBargain, String mobile);

	int updatePrizeBuy(PrizeBuyRequestBean requestBean);

	SmsConfig getSmsConfig();

	void sendSms(String mobile, String reason) throws Exception;

	int saveSmsCode(String mobile, String verificationCode, String verificationType, Integer status, String platform);

	int updateCheckMobileCode(String mobile, String verificationCode, String verificationType, String platform, Integer searchStatus, Integer updateStatus);

	boolean checkHaveHelpedBargain(Integer prizeId, String wechatId, String wechatIdHelp);

	Integer getBargainLevel(Integer prizeId, double price, double currBarganMoney);

	boolean checkHaveBought(Integer prizeId, String wechatId);
	
}
