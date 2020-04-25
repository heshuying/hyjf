package com.hyjf.mqreceiver.hgdatareport.cert.transferproject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hyjf.common.util.GetDate;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportServiceImpl;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallConstant;
import com.hyjf.mybatis.model.auto.BorrowCredit;
import com.hyjf.mybatis.model.auto.BorrowCreditExample;
import com.hyjf.mybatis.model.auto.HjhDebtCredit;
import com.hyjf.mybatis.model.auto.HjhDebtCreditExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
/**
 * @author pcc
 */

@Service
public class CertTransferProjectServiceImpl extends BaseHgCertReportServiceImpl implements CertTransferProjectService {

	@Override
	public JSONArray createDate(String creditNid,String flag) {
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if("1".equals(flag)){
			BorrowCreditExample example=new BorrowCreditExample();
			example.createCriteria().andCreditNidEqualTo(Integer.parseInt(creditNid));
			List<BorrowCredit> creditList=borrowCreditMapper.selectByExample(example);
			if(creditList!=null&&creditList.size()>0){
				BorrowCredit credit=creditList.get(0);
				UsersInfo usersInfo=this.getUsersInfoByUserId(credit.getCreditUserId());
				try {
					for (int i = 0; i < 1; i++) {
						Map<String, Object> param = new LinkedHashMap<String, Object>();
						//接口版本号
						param.put("version", CertCallConstant.CERT_CALL_VERSION);
						//平台编号
						param.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
						//转让编号
						param.put("transferId", credit.getCreditNid()+"");
						//存管银行债权编号
						param.put("bankNo", "-1");// 选填
						//债权来源
						param.put("fromType", "1");// 选填
						//债权项目编号或承接项目编码
						param.put("finClaimId", credit.getTenderNid());// 选填
						//投资人用户标示Hash
						param.put("userIdcardHash", tool.idCardHash(usersInfo.getIdcard()));
						//原散标编号
						param.put("sourceProductCode",credit.getBidNid());// 选填
						//原产品信息编号
						param.put("sourceFinancingCode", "-1");// 选填
						//计划转让本金(元)
						param.put("transferAmount", credit.getCreditCapital());
						//计划转让利息
						param.put("transferInterest", credit.getCreditInterest());
						//浮动金额   0-(credit_capital*credit_discount/100)
						param.put("floatMoney", 
								BigDecimal.ZERO.subtract(credit.getCreditCapital().multiply(credit.getCreditDiscount().divide(new BigDecimal(100)))));
						//转让项目发布的日期
						param.put("transferDate", GetDate.times10toStrYYYYMMDD(credit.getAddTime()));
						//转让债权信息的链接URL
						param.put("sourceProductUrl", CertCallConstant.CERT_WEB_HOST + "/bank/user/credit/webcredittender.do?creditNid="+credit.getCreditNid());
						list.add(param);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else{
			HjhDebtCreditExample example=new HjhDebtCreditExample();
			example.createCriteria().andCreditNidEqualTo(creditNid);
			List<HjhDebtCredit> hjhDebtCreditList=hjhDebtCreditMapper.selectByExample(example);
			if(hjhDebtCreditList!=null&&hjhDebtCreditList.size()>0){
				HjhDebtCredit hjhDebtCredit=hjhDebtCreditList.get(0);
				UsersInfo usersInfo=this.getUsersInfoByUserId(hjhDebtCredit.getUserId());
				try {
					for (int i = 0; i < 1; i++) {
						Map<String, Object> param = new LinkedHashMap<String, Object>();
						//接口版本号
						param.put("version", CertCallConstant.CERT_CALL_VERSION);
						//平台编号
						param.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
						//转让编号
						param.put("transferId", hjhDebtCredit.getCreditNid());
						//存管银行债权编号
						param.put("bankNo", "-1");// 选填
						//债权来源
						param.put("fromType", hjhDebtCredit.getSellOrderId().equals(hjhDebtCredit.getInvestOrderId())?"1":"2");// 选填
						//债权项目编号或承接项目编码
						param.put("finClaimId", hjhDebtCredit.getSellOrderId());// 选填
						//投资人用户标示Hash
						param.put("userIdcardHash", tool.idCardHash(usersInfo.getIdcard()));
						//原散标编号
						param.put("sourceProductCode",hjhDebtCredit.getBorrowNid());// 选填
						//原产品信息编号
						param.put("sourceFinancingCode", "-1");// 选填
						//计划转让本金(元)
						param.put("transferAmount", hjhDebtCredit.getCreditCapital());
						//计划转让利息
						param.put("transferInterest", hjhDebtCredit.getCreditInterest());
						//浮动金额
						param.put("floatMoney", "0");
						//转让项目发布的日期
						param.put("transferDate", GetDate.times10toStrYYYYMMDD(hjhDebtCredit.getCreateTime()));
						//转让债权信息的链接URL
						param.put("sourceProductUrl", "-1");
						list.add(param);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return JSONArray.parseArray(JSON.toJSONString(list));
	}

}

