package com.hyjf.mqreceiver.hgdatareport.cert.transferstatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportServiceImpl;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallConstant;
import com.hyjf.mybatis.model.auto.BorrowCredit;
import com.hyjf.mybatis.model.auto.BorrowCreditExample;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.BorrowRepayExample;
import com.hyjf.mybatis.model.auto.CreditTender;
import com.hyjf.mybatis.model.auto.CreditTenderExample;
import com.hyjf.mybatis.model.auto.HjhDebtCredit;
import com.hyjf.mybatis.model.auto.HjhDebtCreditExample;
import com.hyjf.mybatis.model.auto.HjhDebtCreditTender;
import com.hyjf.mybatis.model.auto.HjhDebtCreditTenderExample;
/**
 * @author pcc
 */

@Service
public class CertTransferStatusServiceImpl extends BaseHgCertReportServiceImpl implements CertTransferStatusService {
	Logger logger = LoggerFactory.getLogger(CertTransferStatusServiceImpl.class);

    private String thisMessName = "转让状态信息上报";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_CERT + " " + thisMessName + "】";


	@Override
	public JSONArray createDate(Map<String, Object> map, String flag) {
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(!"5".equals((String)map.get("transferStatus"))){
			try {
				for (int i = 0; i <1; i++) {
					Map<String, Object> param = new LinkedHashMap<String, Object>();
					//接口版本号
					param.put("version", CertCallConstant.CERT_CALL_VERSION);
					//平台编号
					param.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
					//转让项目编号
					param.put("transferId", map.get("transferId"));
					//状态编码
					param.put("transferStatus", map.get("transferStatus"));
					//成功转让本金金额(元)
					param.put("amount", map.get("amount"));
					//成功转让利息金额 (元)
					param.put("interest", map.get("interest"));
					//成功转让浮动金额 (元)
					param.put("floatMoney", map.get("floatMoney"));
					//状态更新时间
					param.put("productDate", map.get("productDate"));
					list.add(param);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			String borrowNid=(String) map.get("borrowNid");
			//Borrow borrow=this.getBorrowByBorrowNid(borrowNid);
			try {
				if("1".equals(flag)){
					BorrowCreditExample example=new BorrowCreditExample();
					example.createCriteria().andBidNidEqualTo(borrowNid);
					List<BorrowCredit> creditList=borrowCreditMapper.selectByExample(example);
					if(creditList==null||creditList.size()==0){
						return null;
					}
					for (BorrowCredit borrowCredit : creditList) {
						Map<String, Object> param = new LinkedHashMap<String, Object>();
						//接口版本号
						param.put("version", CertCallConstant.CERT_CALL_VERSION);
						//平台编号
						param.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
						//转让项目编号
						param.put("transferId", borrowCredit.getCreditNid()+"");
						//状态编码
						param.put("transferStatus", map.get("transferStatus"));
						//成功转让本金金额(元)
						param.put("amount", map.get("amount"));
						//成功转让利息金额 (元)
						param.put("interest", map.get("interest"));
						//成功转让浮动金额 (元)
						param.put("floatMoney", map.get("floatMoney"));
						//状态更新时间
						param.put("productDate", map.get("productDate"));
						/*if(borrowCredit.getCreditCapitalAssigned().compareTo(BigDecimal.ZERO)==0){
							//状态更新时间
							param.put("productDate", GetDate.timestamptoStrYYYYMMDDHHMMSS((borrow.getUpdatetime()+"")));
						}else{
							//状态更新时间
							param.put("productDate", GetDate.timestamptoStrYYYYMMDDHHMMSS((borrowCredit.getCreditRepayYesTime()+"")));
						}*/
						
						list.add(param);
					}
				}else{
					HjhDebtCreditExample example=new HjhDebtCreditExample();
					example.createCriteria().andBorrowNidEqualTo(borrowNid);
					List<HjhDebtCredit> hjhDebtCreditList=hjhDebtCreditMapper.selectByExample(example);
					if(hjhDebtCreditList==null||hjhDebtCreditList.size()==0){
						return null;
					}
					for (HjhDebtCredit hjhDebtCredit : hjhDebtCreditList) {
						Map<String, Object> param = new LinkedHashMap<String, Object>();
						//接口版本号
						param.put("version", CertCallConstant.CERT_CALL_VERSION);
						//平台编号
						param.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
						//转让项目编号
						param.put("transferId", hjhDebtCredit.getCreditNid()+"");
						//状态编码
						param.put("transferStatus", map.get("transferStatus"));
						//成功转让本金金额(元)
						param.put("amount", map.get("amount"));
						//成功转让利息金额 (元)
						param.put("interest", map.get("interest"));
						//成功转让浮动金额 (元)
						param.put("floatMoney", map.get("floatMoney"));
						//状态更新时间
						param.put("productDate", map.get("productDate"));
						/*if(hjhDebtCredit.getCreditCapitalAssigned().compareTo(BigDecimal.ZERO)==0){
							//状态更新时间
							param.put("productDate", GetDate.dateToString(borrow.getUpdatetime()));
						}else{
							//状态更新时间
							param.put("productDate", GetDate.timestamptoStrYYYYMMDDHHMMSS((hjhDebtCredit.getCreditRepayYesTime()+"")));
						}*/
						
						list.add(param);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		if(list.size()==0){
			return null;
		}
		return JSONArray.parseArray(JSON.toJSONString(list));
	}

	@Override
	public Map<String, Object> getMap(String creditNid, String flag,
			String status, String borrowNid) {
		Map<String, Object> map=new HashMap<String, Object>();
		if("1".equals(flag)){
			if("5".equals(status)){
				BorrowRepayExample borrowRepayExample=new BorrowRepayExample();
				borrowRepayExample.createCriteria().andBorrowNidEqualTo(borrowNid);
				List<BorrowRepay> list=borrowRepayMapper.selectByExample(borrowRepayExample);
				//	转让项目编号
				map.put("borrowNid", borrowNid);
				//状态编码
				map.put("transferStatus", "5");
				//成功转让本金金额(元)
				map.put("amount", "0.00");
				//成功转让利息金额 (元)
				map.put("interest", "0.00");
				//成功转让浮动金额 (元)
				map.put("floatMoney", "0.00");
				//	状态更新时间
				map.put("productDate", GetDate.timestamptoStrYYYYMMDDHHMMSS((list.get(0).getRepayActionTime()+"")));
				return map;
			}
			BorrowCreditExample example=new BorrowCreditExample();
			example.createCriteria().andCreditNidEqualTo(Integer.parseInt(creditNid));
			List<BorrowCredit> creditList=borrowCreditMapper.selectByExample(example);
			if(creditList==null||creditList.size()==0){
				return map;
			}
			BorrowCredit credit=creditList.get(0);
			switch (status) {
			case "0":
				//转让项目编号
				map.put("transferId", credit.getCreditNid()+"");
				//状态编码
				map.put("transferStatus", "0");
				//	成功转让本金金额(元)
				map.put("amount", "0.00");
				//成功转让利息金额 (元)
				map.put("interest", "0.00");
				//成功转让浮动金额 (元)
				map.put("floatMoney", "0.00");
				//	状态更新时间
				map.put("productDate", GetDate.timestamptoStrYYYYMMDDHHMMSS((credit.getAddTime()+"")));
				break;
			case "1":
				//转让项目编号
				map.put("transferId", credit.getCreditNid()+"");
				//状态编码
				map.put("transferStatus", "1");
				//	成功转让本金金额(元)
				map.put("amount", "0.00");
				//成功转让利息金额 (元)
				map.put("interest", "0.00");
				//成功转让浮动金额 (元)
				map.put("floatMoney", "0.00");
				//	状态更新时间
				map.put("productDate", GetDate.timestamptoStrYYYYMMDDHHMMSS((credit.getAddTime()+"")));
				break;
			case "2":
			case "3":
				if(BigDecimal.ZERO.compareTo(credit.getCreditCapitalAssigned())==0){
					Integer now=GetDate.getNowTime10();
					//转让项目编号
					map.put("transferId", credit.getCreditNid()+"");
					//状态编码
					map.put("transferStatus", "3");
					//成功转让本金金额(元)
					map.put("amount", "0.00");
					//成功转让利息金额 (元)
					map.put("interest", "0.00");
					//成功转让浮动金额 (元)
					map.put("floatMoney", "0.00");
					//	状态更新时间
					map.put("productDate",  GetDate.timestamptoStrYYYYMMDDHHMMSS((now<=credit.getEndTime()?now:credit.getEndTime())+""));
				}else{
					CreditTenderExample creditTenderExample=new CreditTenderExample();
					creditTenderExample.createCriteria().andCreditNidEqualTo(creditNid);
					List<CreditTender> list=creditTenderMapper.selectByExample(creditTenderExample);
					//转让项目编号
					map.put("transferId", credit.getCreditNid()+"");
					//	状态编码
					map.put("transferStatus", "2");
					//成功转让本金金额(元)
					map.put("amount", credit.getCreditCapitalAssigned());
					//成功转让利息金额 (元)
					map.put("interest", credit.getCreditInterestAssigned());
					//成功转让浮动金额 (元)
					map.put("floatMoney", BigDecimal.ZERO.subtract(credit.getCreditCapitalAssigned().
							multiply(credit.getCreditDiscount().divide(new BigDecimal(100)))).setScale(2, BigDecimal.ROUND_HALF_DOWN));
					if(credit.getCreditCapitalAssigned().compareTo(credit.getCreditCapital())==0){
						//状态更新时间
						map.put("productDate",  GetDate.timestamptoStrYYYYMMDDHHMMSS((list.get(list.size()-1).getAddTime()+"")));
					}else{
						//状态更新时间
						map.put("productDate",  GetDate.timestamptoStrYYYYMMDDHHMMSS(credit.getEndTime()+""));
					}
					
				}
				break;
			default:
				break;
			}
		}else{
			
			if("5".equals(status)){
				BorrowRepayExample borrowRepayExample=new BorrowRepayExample();
				borrowRepayExample.createCriteria().andBorrowNidEqualTo(borrowNid);
				List<BorrowRepay> list=borrowRepayMapper.selectByExample(borrowRepayExample);
				//转让项目编号
				map.put("borrowNid", borrowNid);
				//状态编码
				map.put("transferStatus", "5");
				//成功转让本金金额(元)
				map.put("amount", "0.00");
				//成功转让利息金额 (元)
				map.put("interest", "0.00");
				//成功转让浮动金额 (元)
				map.put("floatMoney", "0.00");
				//状态更新时间
				map.put("productDate", GetDate.timestamptoStrYYYYMMDDHHMMSS((list.get(0).getRepayActionTime()+"")));
				return map;
			}
			
			HjhDebtCreditExample example=new HjhDebtCreditExample();
			example.createCriteria().andCreditNidEqualTo(creditNid);
			List<HjhDebtCredit> hjhDebtCreditList=hjhDebtCreditMapper.selectByExample(example);
			if(hjhDebtCreditList==null||hjhDebtCreditList.size()==0){
				return map;
			}
			HjhDebtCredit hjhDebtCredit=hjhDebtCreditList.get(0);
			switch (status) {
			case "0":
				
				//转让项目编号
				map.put("transferId", hjhDebtCredit.getCreditNid()+"");
				//状态编码
				map.put("transferStatus", "0");
				//成功转让本金金额(元)
				map.put("amount", "0.00");
				//成功转让利息金额 (元)
				map.put("interest", "0.00");
				//成功转让浮动金额 (元)
				map.put("floatMoney", "0.00");
				//状态更新时间
				map.put("productDate", GetDate.timestamptoStrYYYYMMDDHHMMSS((hjhDebtCredit.getCreateTime()+"")));
				break;
			case "1":
				
				//转让项目编号
				map.put("transferId", hjhDebtCredit.getCreditNid()+"");
				//状态编码
				map.put("transferStatus", "1");
				//成功转让本金金额(元)
				map.put("amount", "0.00");
				//成功转让利息金额 (元)
				map.put("interest", "0.00");
				//成功转让浮动金额 (元)
				map.put("floatMoney", "0.00");
				//状态更新时间
				map.put("productDate", GetDate.timestamptoStrYYYYMMDDHHMMSS((hjhDebtCredit.getCreateTime()+"")));
				break;
			case "2":
			case "3":
				if(BigDecimal.ZERO.compareTo(hjhDebtCredit.getCreditCapitalAssigned())==0){
					Integer now=GetDate.getNowTime10();
					//转让项目编号
					map.put("transferId", hjhDebtCredit.getCreditNid()+"");
					//状态编码
					map.put("transferStatus", "3");
					//成功转让本金金额(元)
					map.put("amount", "0.00");
					//成功转让利息金额 (元)
					map.put("interest", "0.00");
					//成功转让浮动金额 (元)
					map.put("floatMoney", "0.00");
					Integer endTime=hjhDebtCredit.getEndTime()==0?hjhDebtCredit.getUpdateTime():hjhDebtCredit.getEndTime();
					//状态更新时间
					map.put("productDate",  GetDate.timestamptoStrYYYYMMDDHHMMSS((now<=endTime?now:endTime)+""));
				}else{
					HjhDebtCreditTenderExample hjhDebtCreditTenderExample=new HjhDebtCreditTenderExample();
					hjhDebtCreditTenderExample.createCriteria().andCreditNidEqualTo(creditNid);
					List<HjhDebtCreditTender> list=hjhDebtCreditTenderMapper.selectByExample(hjhDebtCreditTenderExample);
					//转让项目编号
					map.put("transferId", hjhDebtCredit.getCreditNid()+"");
					//状态编码
					map.put("transferStatus", "2");
					//成功转让本金金额(元)
					map.put("amount", hjhDebtCredit.getCreditCapitalAssigned());
					//成功转让利息金额 (元)
					map.put("interest", hjhDebtCredit.getCreditInterestAssigned());
					//成功转让浮动金额 (元)
					map.put("floatMoney", "0.00");
					
					
					if(hjhDebtCredit.getCreditCapitalAssigned().compareTo(hjhDebtCredit.getCreditCapital())==0){
						//状态更新时间
						map.put("productDate",  GetDate.timestamptoStrYYYYMMDDHHMMSS((list.get(list.size()-1).getCreateTime())+""));
					}else{
						//状态更新时间
						map.put("productDate",  GetDate.timestamptoStrYYYYMMDDHHMMSS(
								(hjhDebtCredit.getEndTime()==0?hjhDebtCredit.getUpdateTime():hjhDebtCredit.getEndTime())+""));
					}
				}
				break;

			default:
				break;
			}
		}
		return map;
	}

}

