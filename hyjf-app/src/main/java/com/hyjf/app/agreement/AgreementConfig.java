package com.hyjf.app.agreement;

import com.hyjf.app.user.credit.AppTenderCreditDefine;

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
public class AgreementConfig {

	private final static Map<String, List<AgreementBean>> aggrementMap = new HashMap<String, List<AgreementBean>>();
	// 计划
	public final static String agreementType_HJH = "HJH";
	// 债转
	public final static String agreementType_HZR = "HZR";
	// 散标
	public final static String agreementType_Common = "Common";

	public static final AgreementBean ventureAgreement = new AgreementBean("散标风险揭示书",
			AgreementDefine.REQUEST_MAPPING + AgreementDefine.CONFIRMATION_OF_INVESTMENT_RISK_ACTION);

	public static final AgreementBean jujianAgreement = new AgreementBean("居间服务协议",
			AgreementDefine.REQUEST_MAPPING + AgreementDefine.HJH_MEDIACY_CONTRACT);

	public static final AgreementBean serviceAgreement = new AgreementBean("智投服务协议",
			AgreementDefine.REQUEST_MAPPING + AgreementDefine.HJH_INFO_AGREEMENT);
	
	public static final AgreementBean zhuanyangAgreement = new AgreementBean("债权转让协议",
			AppTenderCreditDefine.REQUEST_MAPPING + AppTenderCreditDefine.CREDIT_CONTRACT);

	
	

	static {
		List<AgreementBean> hjh = new ArrayList<AgreementBean>();
		hjh.add(ventureAgreement);
		hjh.add(serviceAgreement);
		aggrementMap.put(agreementType_HJH, hjh);

		List<AgreementBean> hzr = new ArrayList<AgreementBean>();
		hzr.add(ventureAgreement);
		hzr.add(zhuanyangAgreement);
		aggrementMap.put(agreementType_HZR, hzr);
		
		List<AgreementBean> common = new ArrayList<AgreementBean>();
		common.add(ventureAgreement);
		common.add(jujianAgreement);
		aggrementMap.put(agreementType_Common, common);
	}
	
	public static Map<String, List<AgreementBean>> getAgreementMap(){
		return aggrementMap;
	}
}
