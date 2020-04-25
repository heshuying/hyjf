package com.hyjf.app.user.myasset;

import java.text.DecimalFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountAccurate;
import com.hyjf.mybatis.model.auto.UsersInfo;

@Controller
@RequestMapping(value = MyAssetDefine.REQUEST_MAPPING)
public class MyAssetController extends BaseController {
	/** 类名 */
	public static final String THIS_CLASS = MyAssetController.class.getName();
	@Autowired
	private MyAssetService myAssetService;

	/**
	 * 获取我的财富 接口逻辑完全参照php
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = MyAssetDefine.MYASSET_ACTION, method = RequestMethod.POST)
	public JSONObject getMyAsset(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(THIS_CLASS, MyAssetDefine.MYASSET_ACTION);
		JSONObject ret = new JSONObject();
		ret.put("request", MyAssetDefine.RETURN_REQUEST);

		// 版本号
		String version = request.getParameter("version");
		// 网络状态
		String netStatus = request.getParameter("netStatus");
		// 平台
		String platform = request.getParameter("platform");
		// 唯一标识
		String sign = request.getParameter("sign");
		// 金额显示格式
		DecimalFormat moneyFormat = null;

		// 检查参数正确性
		if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform) || Validator.isNull(sign)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}

		// // 判断sign是否存在
		// boolean isSignExists = SecretUtil.isExists(sign);
		// if (!isSignExists) {
		// ret.put("status", "1");
		// ret.put("statusDesc", "请求参数非法");
		// return ret;
		// }

		// 取得加密用的Key
		String key = SecretUtil.getKey(sign);
		if (Validator.isNull(key)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}

		// 判断选择哪种金融样式
		if (version.contains(CustomConstants.APP_VERSION_NUM)) {
			moneyFormat = CustomConstants.DF_FOR_VIEW_V1; // 按照满超新需求，我的财富接口用这种金融格式。
			// moneyFormat =CustomConstants.DF_FOR_VIEW;
		} else {
			moneyFormat = CustomConstants.DF_FOR_VIEW;
		}

		// 取得用户iD
		Integer userId = SecretUtil.getUserId(sign);
		// 取得用户信息
		UsersInfo userinfo = this.myAssetService.getUsersInfoByUserId(userId);
		Account account = this.myAssetService.getAccount(userId);
		try {
			if (userinfo != null && account != null) {
				// 用户资产 银行存管 liuyang删除start
				// // 债转统计
				// WebPandectCreditTenderCustomize creditTender =
				// myAssetService.queryCreditInfo(userId);
				// if (creditTender == null) {
				// creditTender = new WebPandectCreditTenderCustomize();
				// }
				// // 去掉已债转
				// WebPandectBorrowRecoverCustomize recoverYesInfo =
				// myAssetService.queryRecoverInfo(userId, 1);
				// if (recoverYesInfo == null) {
				// recoverYesInfo = new WebPandectBorrowRecoverCustomize();
				// }
				// // 去掉待收已债转
				// WebPandectBorrowRecoverCustomize recoverWaitInfo =
				// myAssetService.queryRecoverInfo(userId, 0);
				// if (recoverWaitInfo == null) {
				// recoverWaitInfo = new WebPandectBorrowRecoverCustomize();
				// }
				// BigDecimal planInterestWait = BigDecimal.ZERO;
				// if (account.getPlanInterestWait() != null) {
				// planInterestWait = account.getPlanInterestWait();
				// }
				// BigDecimal planCapitalWait = BigDecimal.ZERO;
				// if (account.getPlanCapitalWait() != null) {
				// planCapitalWait = account.getPlanCapitalWait();
				// }
				// BigDecimal planTotal = BigDecimal.ZERO;
				// if (account.getPlanAccedeTotal() != null) {
				// planTotal = account.getPlanAccedeTotal();
				// }
				// // 总资产
				// AssetBean asset = myAssetService.queryAsset(userId);
				// // 总出借额 = 总出借额+ 支付金额 -已债转总利息（含垫付）-已债转金额 -待收已债转 -待收已债转利息
				// /*
				// * BigDecimal investTotal =
				// * asset.getInvestTotal().add(creditTender.getCreditAssign())
				// *
				// .subtract(recoverYesInfo.getCreditInterestAmount()).subtract(
				// * recoverYesInfo.getCreditAmount())
				// * .subtract(recoverWaitInfo.getCreditAmount
				// * ()).subtract(recoverWaitInfo.getCreditInterestAmount());
				// */
				// BigDecimal investTotal =
				// asset.getInvestTotal().add(creditTender.getCreditCapital()).add(planTotal);
				// // 优惠券总收益 add by hesy 优惠券相关 start
				// BigDecimal couponInterestTotalWaitDec = BigDecimal.ZERO;
				// String couponInterestTotalWait =
				// myAssetService.queryCouponInterestWait(userId);
				// LogUtil.infoLog(this.getClass().getName(), "getMyAsset",
				// "优惠券待收收益：" + couponInterestTotalWait);
				// if
				// (org.apache.commons.lang3.StringUtils.isNotEmpty(couponInterestTotalWait))
				// {
				// couponInterestTotalWaitDec = new
				// BigDecimal(couponInterestTotalWait);
				// }
				// // add by hesy 优惠券相关 end
				//
				// // 待收利息
				// BigDecimal waitInterest =
				// asset.getWaitInterest().add(creditTender.getCreditInterestWait()).add(couponInterestTotalWaitDec).subtract(recoverWaitInfo.getCreditInterestAmount())
				// .add(planInterestWait);
				// // ret.put("asset", asset);
				// // 汇天利余额
				// BigDecimal htlRestAmount =
				// myAssetService.queryHtlSumRestAmount(userId);
				// if (htlRestAmount == null) {
				// htlRestAmount = new BigDecimal(0);
				// }
				// // 待收本金
				// BigDecimal waitCapital =
				// asset.getWaitCapital().add(htlRestAmount).add(creditTender.getCreditCapitalWait()).subtract(recoverWaitInfo.getCreditAmount()).add(planCapitalWait);
				// // 待收金额
				// BigDecimal accountWait =
				// asset.getWaitCapital().add(htlRestAmount).add(asset.getWaitInterest());
				// // 总额
				// // BigDecimal total=
				// account.setTotal(waitInterest.add(waitCapital).add(account.getBalance()).add(account.getFrost()));
				//
				// // 汇天利当天收益 = 汇天利出借金额 * 历史年回报率 / 360
				// // Product product = myAssetService.queryHtlConfig();
				// // BigDecimal interesttoday
				// //
				// =htlRestAmount.multiply(ProductConstants.INTEREST_RATE).divide(new
				// // BigDecimal(360)); //(new BigDecimal("0.00016438"));
				// // 汇天利总收益
				// BigDecimal interestall =
				// myAssetService.queryHtlSumInterest(userId);
				// if (interestall == null) {
				// interestall = new BigDecimal(0);
				// }
				// // add by hesy 优惠券相关 start
				// BigDecimal couponInterestTotalDec = BigDecimal.ZERO;
				// String couponInterestTotal =
				// myAssetService.queryCouponInterestTotal(userId);
				// LogUtil.infoLog(this.getClass().getName(), "getMyAsset",
				// "优惠券已得收益：" + couponInterestTotal);
				// if
				// (org.apache.commons.lang3.StringUtils.isNotEmpty(couponInterestTotal))
				// {
				// couponInterestTotalDec = new BigDecimal(couponInterestTotal);
				// }
				//
				// // add by hesy 优惠券相关 end
				//
				// // 已回收的利息
				// BigDecimal recoverInterest =
				// asset.getRecoverInterest().add(interestall).add(couponInterestTotalDec).add(creditTender.getCreditInterestYes())
				// .subtract(recoverYesInfo.getCreditInterestAmount());
				// 用户资产 银行存管 liuyang删除end

				// 维护 huiyingdai_account_accurate 表数据
				AccountAccurate aa = new AccountAccurate();
				aa.setAwait(account.getBankAwait());
				aa.setTotal(account.getBankTotal());
				aa.setRecoverInterest(account.getBankInterestSum());
				aa.setInvestTotal(account.getBankInvestSum());
				aa.setWaitInterest(account.getBankAwaitInterest());
				aa.setWaitCapital(account.getBankAwaitCapital());
				aa.setBalance(account.getBankBalance());
				aa.setFrost(account.getBankFrost());

				List<AccountAccurate> aas = myAssetService.queryAccountAccurateByUserid(userId);
				if (aas != null) {
					myAssetService.updateAccountAccurateByUserid(aa, userId);
				} else {
					aa.setUserId(userId);
					myAssetService.insertAccountAccurate(aa);
				}

				ret.put("status", "0");
				ret.put("statusDesc", "成功");
				ret.put("balance", moneyFormat.format(account.getBankBalance()));
				ret.put("frost", moneyFormat.format(account.getBankFrost()));
				ret.put("waitCapital", moneyFormat.format(account.getBankAwaitCapital().add(account.getPlanCapitalWait())));
				ret.put("await", moneyFormat.format(account.getBankAwait().add(account.getPlanAccountWait())));
				ret.put("recoverInterest", moneyFormat.format(account.getBankInterestSum()));
				ret.put("waitInterest", moneyFormat.format(account.getBankAwaitInterest().add(account.getPlanInterestWait())));
				ret.put("investTotal", moneyFormat.format(account.getBankInvestSum()));
				if (userinfo.getRoleId() == 1) {// 出借人
					ret.put("accountType", "1");
					// TODO
					ret.put("total", moneyFormat.format(account.getBankTotal()));
				} else if (userinfo.getRoleId() == 2) {// 借款人
					ret.put("accountType", "2");
					ret.put("repay", moneyFormat.format(account.getBankWaitRepay()));
					ret.put("total", moneyFormat.format(account.getBankTotal()));
				}

			} else {
				ret.put("status", "2");
				ret.put("statusDesc", "用户账户信息异常");
				return ret;
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 获取用户账户信息
			// Account account= this.myAssetService.getAccount(userId);
			// if(userinfo!=null && account!=null){
			// ret.put("status", "0");
			// ret.put("statusDesc", "成功");
			// ret.put("balance", String.valueOf(account.getBalance()));
			// ret.put("frost", String.valueOf(account.getFrost()));
			// ret.put("await", String.valueOf(account.getAwait()));
			// if(userinfo.getRoleId()==1){//出借人
			// ret.put("accountType", "1");
			// ret.put("total",
			// String.valueOf(account.getBalance().add(account.getFrost()).add(account.getAwait())));
			// }else if(userinfo.getRoleId()==2){//借款人
			// ret.put("accountType", "2");
			// ret.put("repay", String.valueOf(account.getRepay()));
			// ret.put("total",
			// String.valueOf(account.getBalance().add(account.getFrost()).add(account.getAwait()).subtract(account.getRepay())));
			// }
			// }else{
			// ret.put("status", "2");
			// ret.put("statusDesc", "用户账户信息异常");
			// return ret;
			// }
		}
		System.out.println("******:" + ret);
		LogUtil.endLog(THIS_CLASS, MyAssetDefine.MYASSET_ACTION);
		return ret;
	}

	/**
	 * 获取我的财富 Editor By Zhuxiaodong at 20160302
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	/*
	 * @ResponseBody
	 * 
	 * @RequestMapping(value = MyAssetDefine.MYASSET_ACTION, method =
	 * RequestMethod.POST) public JSONObject getMyAsset(HttpServletRequest
	 * request, HttpServletResponse response) { LogUtil.startLog(THIS_CLASS,
	 * MyAssetDefine.MYASSET_ACTION); JSONObject ret = new JSONObject();
	 * ret.put("request", MyAssetDefine.RETURN_REQUEST);
	 * 
	 * // 版本号 String version = request.getParameter("version"); // 网络状态 String
	 * netStatus = request.getParameter("netStatus"); // 平台 String platform =
	 * request.getParameter("platform"); // 唯一标识 String sign =
	 * request.getParameter("sign"); //金额显示格式 DecimalFormat moneyFormat =null;
	 * 
	 * // 检查参数正确性 if (Validator.isNull(version) || Validator.isNull(netStatus)
	 * || Validator.isNull(platform) || Validator.isNull(sign)) {
	 * ret.put("status", "1"); ret.put("statusDesc", "请求参数非法"); return ret; }
	 * 
	 * // // 判断sign是否存在 // boolean isSignExists = SecretUtil.isExists(sign); //
	 * if (!isSignExists) { // ret.put("status", "1"); // ret.put("statusDesc",
	 * "请求参数非法"); // return ret; // }
	 * 
	 * // 取得加密用的Key String key = SecretUtil.getKey(sign); if
	 * (Validator.isNull(key)) { ret.put("status", "1"); ret.put("statusDesc",
	 * "请求参数非法"); return ret; }
	 * 
	 * //判断选择哪种金融样式 if(version.contains(CustomConstants.APP_VERSION_NUM)){
	 * moneyFormat =CustomConstants.DF_FOR_VIEW_V1; //按照满超新需求，我的财富接口用这种金融格式。 //
	 * moneyFormat =CustomConstants.DF_FOR_VIEW; }else{ moneyFormat
	 * =CustomConstants.DF_FOR_VIEW; }
	 * 
	 * // 取得用户iD Integer userId = SecretUtil.getUserId(sign); //取得用户信息 UsersInfo
	 * userinfo= this.myAssetService.getUsersInfoByUserId(userId); try{ String
	 * accountPhpResult = null; String phpKey = "587643154876431646654873";
	 * String encodeData = ""; if(userinfo!=null){ try {
	 * //对key采用非MD5加密方式进行三重加密算法 encodeData =
	 * ThreeDESUtils.Encrypt3DESWithOutMD5(phpKey, String.valueOf(userId));
	 * encodeData = URLEncoder.encode(encodeData, "UTF-8"); } catch (Exception
	 * e) { e.printStackTrace(); ret.put("status", "1"); ret.put("statusDesc",
	 * "userId加密时出现异常"); return ret; }
	 * 
	 * //调用PHP接口接收数据并返回给移动端 // accountPhpResult =
	 * HttpRequest.sendGet(PropUtils.getSystem("hyjf.php.host")+"/account_syn",
	 * "access_key="+encodeData); //LogUtil.warnLog(this.getClass().toString(),
	 * "getMyAsset", "请求PHP接口:"+accountPhpResult); // accountPhpResult =
	 * HttpRequest.sendGet("http://new.hyjf.com/account_syn",
	 * "access_key="+encodeData); // accountPhpResult =
	 * HttpClientUtils.get("https://new.hyjf.com/account_syn?access_key="
	 * +encodeData, new HashMap<String, String>());
	 * 
	 * String phpurl= PropUtils.getSystem("hyjf.php.host")+"/account_syn";
	 * if(phpurl.startsWith("https")){ accountPhpResult =
	 * HttpClientUtils.get(phpurl+"?access_key="+encodeData, new HashMap<String,
	 * String>()); }else{ accountPhpResult = HttpRequest.sendGet(phpurl,
	 * "access_key="+encodeData); }
	 * 
	 * 
	 * if(StringUtils.isEmptyOrWhitespaceOnly(accountPhpResult)){
	 * ret.put("status", "1"); ret.put("statusDesc", "获取信息为空"); return ret;
	 * }else{ JSONObject accountPhpJson = JSON.parseObject(accountPhpResult);
	 * ret.put("status", "0"); ret.put("statusDesc", "成功"); ret.put("balance",
	 * moneyFormat.format(new
	 * BigDecimal(String.valueOf(accountPhpJson.get("balance")))));
	 * ret.put("frost", moneyFormat.format(new
	 * BigDecimal(String.valueOf(accountPhpJson.get("frost")))));
	 * ret.put("await", moneyFormat.format(new
	 * BigDecimal(String.valueOf(accountPhpJson.get("wait_interest"))).add(new
	 * BigDecimal(String.valueOf(accountPhpJson.get("wait_capital"))))));
	 * if(userinfo.getRoleId()==1){//出借人 ret.put("accountType", "1");
	 * ret.put("total", moneyFormat.format(new
	 * BigDecimal(String.valueOf(accountPhpJson.get("total"))))); }else
	 * if(userinfo.getRoleId()==2){//借款人 ret.put("accountType", "2");
	 * ret.put("repay", moneyFormat.format(new
	 * BigDecimal(String.valueOf(accountPhpJson.get("repay")))));
	 * ret.put("total", moneyFormat.format(new
	 * BigDecimal(String.valueOf(accountPhpJson.get("total"))))); } } }else{
	 * ret.put("status", "2"); ret.put("statusDesc", "用户账户信息异常"); return ret; }
	 * }catch(Exception e){ e.printStackTrace(); //获取用户账户信息 // Account account=
	 * this.myAssetService.getAccount(userId); // if(userinfo!=null &&
	 * account!=null){ // ret.put("status", "0"); // ret.put("statusDesc",
	 * "成功"); // ret.put("balance", String.valueOf(account.getBalance())); //
	 * ret.put("frost", String.valueOf(account.getFrost())); // ret.put("await",
	 * String.valueOf(account.getAwait())); // if(userinfo.getRoleId()==1){//出借人
	 * // ret.put("accountType", "1"); // ret.put("total",
	 * String.valueOf(account
	 * .getBalance().add(account.getFrost()).add(account.getAwait()))); // }else
	 * if(userinfo.getRoleId()==2){//借款人 // ret.put("accountType", "2"); //
	 * ret.put("repay", String.valueOf(account.getRepay())); // ret.put("total",
	 * String
	 * .valueOf(account.getBalance().add(account.getFrost()).add(account.getAwait
	 * ()).subtract(account.getRepay()))); // } // }else{ // ret.put("status",
	 * "2"); // ret.put("statusDesc", "用户账户信息异常"); // return ret; // } }
	 * LogUtil.endLog(THIS_CLASS, MyAssetDefine.MYASSET_ACTION); return ret; }
	 */

}
