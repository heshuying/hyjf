package com.hyjf.activity.actdoubleeleven.bargain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.ActJanBargain;
import com.hyjf.mybatis.model.auto.ActJanBargainExample;
import com.hyjf.mybatis.model.auto.ActJanPrize;
import com.hyjf.mybatis.model.auto.ActJanPrizewinList;
import com.hyjf.mybatis.model.auto.ActJanPrizewinListExample;
import com.hyjf.mybatis.model.auto.SmsCode;
import com.hyjf.mybatis.model.auto.SmsCodeExample;
import com.hyjf.mybatis.model.auto.SmsConfig;
import com.hyjf.mybatis.model.auto.SmsConfigExample;
import com.hyjf.mybatis.model.customize.act.ActNovBargainCustomize;
import com.hyjf.mybatis.model.customize.act.ActNovPrizeCustomize;
import com.hyjf.mybatis.model.customize.act.ActNovPrizeDetailCustomize;

@Service
public class BargainServiceImpl extends BaseServiceImpl implements BargainService {
	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;
	
	/**
	 * 获取奖品列表
	 * @return
	 */
	@Override
	public List<ActNovPrizeCustomize> getPrizeList(Map<String,Object> paraMap){
		return actNovBargainCustomizeMapper.selectPrizeList(paraMap);
	}
	
	/**
	 * 查询砍价用户列表
	 * @param prizeId
	 * @param wechatId
	 */
	@Override
	public List<ActNovBargainCustomize> selectBargainList(Map<String,Object> paraMap){
		return actNovBargainCustomizeMapper.selectBargainList(paraMap);
	}
	
	/**
	 * 查询砍价用户总记录数
	 * @param prizeId
	 * @param wechatId
	 * @return
	 */
	@Override
	public Integer selectBargainCount(Map<String,Object> paraMap){
		return actNovBargainCustomizeMapper.countBargainList(paraMap);
	}
	
	/**
	 * 获取奖品详情信息
	 * @param prizeId
	 * @return
	 */
	@Override
	public ActNovPrizeDetailCustomize getPrizeDetail(Map<String,Object> paraMap){
		List<ActNovPrizeDetailCustomize> prizeList = actNovBargainCustomizeMapper.selectPrizeDetail(paraMap);
		
		if(prizeList != null && !prizeList.isEmpty()){
			return prizeList.get(0);
		}
		
		return null;
	}
	
	/**
	 * 根据奖品id查询奖品信息
	 * @param prizeId
	 * @return
	 */
	@Override
	public ActJanPrize getPrizeById(Integer prizeId){
		if(prizeId == null){
			return null;
		}
		
		return actJanPrizeMapper.selectByPrimaryKey(prizeId);
	}
	
	
	/**
	 * 获取当前奖品当前用户砍价次数
	 * @param prizeId
	 * @param wechatId
	 * @return
	 */
	@Override
	public Integer getCurrentBargainCount(Integer prizeId, String wechatId){
		if(prizeId == null || StringUtils.isEmpty(wechatId)){
			return 0;
		}
		
		ActJanBargainExample example = new ActJanBargainExample();
		example.createCriteria().andPrizeIdEqualTo(prizeId).andWechatNameEqualTo(wechatId);
		
		return actJanBargainMapper.countByExample(example);
	}
	
	/**
	 * 获取当前已砍价金额
	 */
	@Override
	public BigDecimal getCurrentBargainMoney(Integer prizeId, String wechatId){
		Map<String,Object> paraMap = new HashMap<String,Object>();
		paraMap.put("prizeId", prizeId);
		paraMap.put("wechatId", wechatId);
		
		return actNovBargainCustomizeMapper.selectCurrentBargainMoney(paraMap);
	}
	
	/**
	 * 校验用户是否已经帮看过该奖品
	 * @param prizeId
	 * @param wechatId
	 * @param wechatIdHelp
	 * @return
	 */
	@Override
	public boolean checkHaveHelpedBargain(Integer prizeId, String wechatId, String wechatIdHelp){
		if(prizeId == null || StringUtils.isBlank(wechatId) || StringUtils.isBlank(wechatIdHelp)){
			return true;
		}
		
		if(wechatId.equals(wechatIdHelp)){
			//自己砍价
			ActJanBargainExample example = new ActJanBargainExample();
			example.createCriteria().andPrizeIdEqualTo(prizeId).andWechatNameEqualTo(wechatId).andWechatNameHelpEqualTo(wechatIdHelp);
			int result = actJanBargainMapper.countByExample(example);
			return result > 0 ? true : false;
		}else{
			//朋友帮砍只能砍一次
			ActJanBargainExample example = new ActJanBargainExample();
			example.createCriteria().andWechatNameNotEqualTo(wechatIdHelp).andWechatNameHelpEqualTo(wechatIdHelp);
			int result = actJanBargainMapper.countByExample(example);
			return result > 0 ? true : false;

		}
		
	}
	
	/**
	 * 插入砍价记录
	 * @param bargainRequestBean
	 * @param bargainMoney
	 * @return
	 */
	@Override
	public Integer insertBargainRecord(BargainRequestBean bargainRequestBean, BigDecimal bargainMoney, String clientIp, ActJanPrize prize){
		ActJanBargain bargain = new ActJanBargain();
		
		bargain.setWechatName(bargainRequestBean.getWechatId());
		bargain.setWechatNickname(StringUtils.isNotEmpty(bargainRequestBean.getWechatNickName())?bargainRequestBean.getWechatNickName():"");
		bargain.setWechatNameHelp(bargainRequestBean.getWechatIdHelp());
		bargain.setWechatNicknameHelp(StringUtils.isNotEmpty(bargainRequestBean.getWechatNickNameHelp())?bargainRequestBean.getWechatNickNameHelp():"");
		bargain.setPrizeId(bargainRequestBean.getPrizeId());
		bargain.setPrizeName(prize.getPrizeName());
		bargain.setMoneyBargain(bargainMoney);
		bargain.setClientIp(clientIp);
		bargain.setCreateTime(GetDate.getNowTime10());
		bargain.setUpdateTime(GetDate.getNowTime10());
		bargain.setDelFlg(0);
		
		actJanBargainMapper.insertSelective(bargain);
		
		return bargain.getId();
	}
	
	/**
	 * 更新砍价翻倍金额
	 * @param idBargain
	 * @param mobile
	 * @return
	 */
	@Override
	public BigDecimal updateBargainDouble(Integer idBargain, String mobile){
		if(idBargain == null){
			return BigDecimal.ZERO;
		}
		
		ActJanBargain bargain = actJanBargainMapper.selectByPrimaryKey(idBargain);
		if(bargain == null){
			return BigDecimal.ZERO;
		}
		
		//判断是否已经翻倍过
		if(StringUtils.isNotEmpty(bargain.getMobile())){
			return BigDecimal.ZERO;
		}
		
		bargain.setMobile(mobile);
		bargain.setMoneyBargain(bargain.getMoneyBargain().multiply(getDoubleFactor()));
		bargain.setUpdateTime(GetDate.getNowTime10());
		actJanBargainMapper.updateByPrimaryKeySelective(bargain);
		
		return bargain.getMoneyBargain();
	}
	
	/**
	 * 奖品购买业务处理
	 */
	@Override
	public int updatePrizeBuy(PrizeBuyRequestBean requestBean){
		ActJanPrize prize = this.getPrizeById(requestBean.getPrizeId());
		if(prize.getRemainCount()<=0){
			throw new RuntimeException("您来晚了，奖品已经购买完");
		}
		
		ActJanPrizewinList prizeWin = new ActJanPrizewinList();
		prizeWin.setPrizeId(requestBean.getPrizeId());
		prizeWin.setPrizeName(prize.getPrizeName());
		prizeWin.setWechatName(requestBean.getWechatId());
		prizeWin.setWechatNickname(requestBean.getWechatNickName());
		prizeWin.setBookingAddress(requestBean.getBookingAddress());
		prizeWin.setBookingMobile(requestBean.getBookingMobile());
		prizeWin.setBookingName(requestBean.getBookingName());
		prizeWin.setCreateTime(GetDate.getNowTime10());
		prizeWin.setUpdateTime(GetDate.getNowTime10());
		prizeWin.setDelFlg(0);
		
		int result = actJanPrizewinListMapper.insertSelective(prizeWin);
		if(result <= 0){
			throw new RuntimeException("插入购买记录失败");
		}
		
		prize.setRemainCount(prize.getRemainCount() -1);
		prize.setUpdateTime(GetDate.getNowTime10());
		
		return actJanPrizeMapper.updateByPrimaryKeySelective(prize);
		
	}
	
	/**
	 * 校验用户是否已经购买过改奖品
	 * @param prizeId
	 * @param wechatId
	 * @return
	 */
	@Override
	public boolean checkHaveBought(Integer prizeId, String wechatId){
		if(Validator.isNull(prizeId) || StringUtils.isEmpty(wechatId)){
			return false;
		}
		
		ActJanPrizewinListExample example = new ActJanPrizewinListExample();
		example.createCriteria().andPrizeIdEqualTo(prizeId).andWechatNameEqualTo(wechatId);
		
		List<ActJanPrizewinList> winList = actJanPrizewinListMapper.selectByExample(example);
		if(winList !=null && winList.size()>0){
			return true;
		}
		
		return false;
	}
	
	/**
	 * 计算砍价金额
	 */
	@Override
	public BigDecimal getRandomBargainMoney(Integer level) {
		String[] sourceCode = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
		if(level == 1){
			sourceCode = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
		}else if(level == 2){
			sourceCode = new String[] { "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
		}else if(level == 3){
			sourceCode = new String[] { "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9"};
		}
		
		List<String> list = Arrays.asList(sourceCode);
		Collections.shuffle(list);
		
		return new BigDecimal(list.get(0));
	}
	
	/**
	 * 计算砍价级别
	 */
	@Override
	public Integer getBargainLevel(Integer prizeId, double price, double currBarganMoney){
		double levelOne = 0.4;
		double levelTow = 0.56;
		
		if(prizeId == 4){
			levelOne = 0.4;
			levelTow = 0.72;
		}else{
			levelOne = 0.4;
			levelTow = 0.56;
		}
		if(currBarganMoney/price <= levelOne){
			return 1;
		}else if(currBarganMoney/price <= levelTow){
			return 2;
		}else{
			return 3;
		}
	}
	
	/*public static void main(String[] args) {
		double moneyBargain =1;
		BigDecimal currBargainBig = BigDecimal.ZERO;
		BigDecimal moneyBargainBig = BigDecimal.ONE;
		double prize = 149;
		int max = 80000;
		for(int i=1; i<max; i++){
			if(moneyBargain/prize <= 0.4){
				currBargainBig = getRandomBargainMoneyTest(1);
			}else if(moneyBargain/prize <= 0.72){
				currBargainBig = getRandomBargainMoneyTest(2);
			}else{
				currBargainBig = getRandomBargainMoneyTest(3);
			}
			moneyBargainBig = moneyBargainBig.add(currBargainBig);
			moneyBargain = moneyBargainBig.doubleValue();
			
			System.out.println("当前次数：" + i + " moneyBargain:" + moneyBargain + " currentBargain:" + currBargainBig);
			if(moneyBargainBig.doubleValue() > prize){
				break;
			}
		}
		
		System.out.println(moneyBargainBig);
	}*/
	
	/**
	 * 获取翻倍倍率
	 * @return
	 */
	private BigDecimal getDoubleFactor(){
		String[] factors = new String[] {"2", "3"};
		
		List<String> list = Arrays.asList(factors);
		Collections.shuffle(list);
		
		return new BigDecimal(list.get(0));
	}
	
	/**
	 * 获取短信发送次数配置
	 * @return
	 */
	@Override
	public SmsConfig getSmsConfig() {
		SmsConfigExample example = new SmsConfigExample();
		List<SmsConfig> list = smsConfigMapper.selectByExample(example);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;

	}
	
	@Override
	public void sendSms(String mobile, String reason) throws Exception {
		Map<String, String> replaceStrs = new HashMap<String, String>();
		replaceStrs.put("var_phonenu", mobile);
		replaceStrs.put("val_reason", reason);
		SmsMessage smsMessage = new SmsMessage(null, replaceStrs, null, null, MessageDefine.SMSSENDFORMANAGER, null,
				CustomConstants.PARAM_TPL_DUANXINCHAOXIAN, CustomConstants.CHANNEL_TYPE_NORMAL);
		smsProcesser.gather(smsMessage);
	}
	
	/**
	 * 保存短信验证码
	 */
	@Override
	public int saveSmsCode(String mobile, String verificationCode, String verificationType, Integer status,
			String platform) {
		// 使之前的验证码无效
		SmsCodeExample example = new SmsCodeExample();
		SmsCodeExample.Criteria cra = example.createCriteria();
		cra.andMobileEqualTo(mobile);
		List<Integer> statusList = new ArrayList<Integer>();
		statusList.add(0);
		statusList.add(8);
		cra.andStatusIn(statusList);
		List<SmsCode> codeList = smsCodeMapper.selectByExample(example);
		if (codeList != null && codeList.size() > 0) {
			for (SmsCode smsCode : codeList) {
				// if (smsCode.getCheckfor().equals(MD5.toMD5Code(mobile +
				// verificationCode + verificationType + platform))) {
				smsCode.setStatus(7);// 失效7
				smsCodeMapper.updateByPrimaryKey(smsCode);
				// }
			}
		}
		// 保存新验证码到数据库
		SmsCode smsCode = new SmsCode();
		smsCode.setCheckfor(MD5.toMD5Code(mobile + verificationCode + verificationType + platform));
		smsCode.setMobile(mobile);
		smsCode.setCheckcode(verificationCode);
		smsCode.setPosttime(GetDate.getMyTimeInMillis());
		smsCode.setStatus(status);
		smsCode.setUserId(0);
		return smsCodeMapper.insertSelective(smsCode);
	}
	
	
	/**
	 * 验证短信验证码是否正确
	 */
	@Override
	public int updateCheckMobileCode(String mobile, String verificationCode, String verificationType, String platform,
			Integer searchStatus, Integer updateStatus) {
		int time = GetDate.getNowTime10();
		int timeAfter = time - 900;// 15分钟有效 900
		SmsCodeExample example = new SmsCodeExample();
		SmsCodeExample.Criteria cra = example.createCriteria();
		cra.andPosttimeGreaterThanOrEqualTo(timeAfter);
		cra.andPosttimeLessThanOrEqualTo(time);
		cra.andMobileEqualTo(mobile);
		cra.andCheckcodeEqualTo(verificationCode);
		List<Integer> status = new ArrayList<Integer>();
		status.add(0);
		status.add(searchStatus);
		cra.andStatusIn(status);
		List<SmsCode> codeList = smsCodeMapper.selectByExample(example);
		if (codeList != null && codeList.size() > 0) {
			for (SmsCode smsCode : codeList) {
				if (smsCode.getCheckfor().equals(MD5.toMD5Code(mobile + verificationCode + verificationType + platform))) {
					smsCode.setStatus(updateStatus);// 已验8或已读9
					smsCodeMapper.updateByPrimaryKey(smsCode);
					return 1;
				}
			}
			return 0;
		} else {
			return 0;
		}
	}

}
