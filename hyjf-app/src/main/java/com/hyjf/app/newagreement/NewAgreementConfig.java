package com.hyjf.app.newagreement;

import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.PropUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置标的展示的协议类型
 * 
 * @author admin
 *
 */
public class NewAgreementConfig {

	private final static Map<String, List<NewAgreementBean>> aggrementMap = new HashMap<String, List<NewAgreementBean>>();
	// 计划
	public final static String agreementType_HJH = "HJH";
	// 债转
	public final static String agreementType_HZR = "HZR";
	// 散标
	public final static String agreementType_Common = "Common";

	public static final NewAgreementBean VENTUREAGREEMENT = new NewAgreementBean("散标风险揭示书",
	        PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + NewAgreementDefine.CONFIRM_INVEST_RISK_AGREEMENT_PATH);

	public static final NewAgreementBean JUJIANAGREEMENT = new NewAgreementBean("居间服务借款协议",
	        PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + NewAgreementDefine.SERVICE_LOAN_AGREEMENT_PATH);

	
	public static final NewAgreementBean ZHUANYANGAGREEMENT = new NewAgreementBean("债权转让协议",
	        PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + NewAgreementDefine.TRANS_FER_AGREEMENT_PATH);

	public static final NewAgreementBean HJH_INVEST_SERVICE_AGREEMENT = new NewAgreementBean("智投服务协议",
	        PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + NewAgreementDefine.HJH_INVEST_SERVICE_AGREEMENT_PATH);
	

	static {
		List<NewAgreementBean> hjh = new ArrayList<NewAgreementBean>();
		hjh.add(VENTUREAGREEMENT);
		hjh.add(HJH_INVEST_SERVICE_AGREEMENT);
		aggrementMap.put(agreementType_HJH, hjh);

		List<NewAgreementBean> hzr = new ArrayList<NewAgreementBean>();
		hzr.add(VENTUREAGREEMENT);
		hzr.add(ZHUANYANGAGREEMENT);
		aggrementMap.put(agreementType_HZR, hzr);
		
		List<NewAgreementBean> common = new ArrayList<NewAgreementBean>();
		common.add(VENTUREAGREEMENT);
		common.add(JUJIANAGREEMENT);
		aggrementMap.put(agreementType_Common, common);
	}
	
	public static Map<String, List<NewAgreementBean>> getAgreementMap(){
		return aggrementMap;
	}
}
