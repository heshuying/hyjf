package com.hyjf.mqreceiver.hgdatareport.cert.olddata.transferstatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.common.util.GetDate;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportServiceImpl;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallConstant;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowCredit;
import com.hyjf.mybatis.model.auto.BorrowCreditExample;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.BorrowRepayExample;
import com.hyjf.mybatis.model.auto.CertBorrow;
import com.hyjf.mybatis.model.auto.CertBorrowExample;
import com.hyjf.mybatis.model.auto.CreditTender;
import com.hyjf.mybatis.model.auto.CreditTenderExample;
import com.hyjf.mybatis.model.auto.HjhDebtCredit;
import com.hyjf.mybatis.model.auto.HjhDebtCreditExample;
import com.hyjf.mybatis.model.auto.HjhDebtCreditTender;
import com.hyjf.mybatis.model.auto.HjhDebtCreditTenderExample;
import com.hyjf.mybatis.model.customize.cert.CertBorrowUpdate;
/**
 * @author pcc
 */

@Service
public class OldCertTransferStatusServiceImpl extends BaseHgCertReportServiceImpl implements OldCertTransferStatusService {
	@Override
	public List<CertBorrow> insertCertBorrowEntityList() {
		CertBorrowExample example = new CertBorrowExample();
        CertBorrowExample.Criteria cra = example.createCriteria();
        //1 代表已上报,0:未上报,99:上报错误
        // 用户信息已上传
        cra.andIsUserInfoEqualTo(1);
        // 散标数据已上报
        cra.andIsScatterEqualTo(1);
        //散标状态已上报
        cra.andIsStatusEqualTo(1);
        //是否上送转让项目
        cra.andIsTransferEqualTo(1);
        //是否上送转让状态
        cra.andIsTransferStatusEqualTo(0);
        example.setLimitStart(0);
        example.setLimitEnd(1000);
        List<CertBorrow> listStatus = certBorrowMapper.selectByExample(example);
        return listStatus;
	}


	@Override
	public List<Map<String, Object>> createList(String borrowNid) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Borrow borrow=this.getBorrowByBorrowNid(borrowNid);
		if(borrow.getPlanNid()==null||borrow.getPlanNid().length()==0){
			BorrowCreditExample borrowCreditExample=new BorrowCreditExample();
			borrowCreditExample.createCriteria().andBidNidEqualTo(borrowNid);
			List<BorrowCredit> creditList=borrowCreditMapper.selectByExample(borrowCreditExample);
			if(creditList!=null&&creditList.size()>0){
				try {
					for (BorrowCredit credit : creditList) {
						
						//募集初始状态
						Map<String, Object> param0 = new LinkedHashMap<String, Object>();
						//接口版本号
						param0.put("version", CertCallConstant.CERT_CALL_VERSION);
						//平台编号
						param0.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
						//转让项目编号
						param0.put("transferId", credit.getCreditNid()+"");
						//状态编码
						param0.put("transferStatus", "0");
						//成功转让本金金额(元)
						param0.put("amount", "0.00");
						//成功转让利息金额 (元)
						param0.put("interest", "0.00");
						//成功转让浮动金额 (元)
						param0.put("floatMoney", "0.00");
						//状态更新时间
						param0.put("productDate", GetDate.timestamptoStrYYYYMMDDHHMMSS((credit.getAddTime()+"")));
						list.add(param0);
						//开始募集状态
						Map<String, Object> param1 = new LinkedHashMap<String, Object>();
						//接口版本号
						param1.put("version", CertCallConstant.CERT_CALL_VERSION);
						//平台编号
						param1.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
						//转让项目编号
						param1.put("transferId", credit.getCreditNid()+"");
						//状态编码
						param1.put("transferStatus", "1");
						//成功转让本金金额(元)
						param1.put("amount", "0.00");
						//成功转让利息金额 (元)
						param1.put("interest", "0.00");
						//成功转让浮动金额 (元)
						param1.put("floatMoney", "0.00");
						//状态更新时间
						param1.put("productDate", GetDate.timestamptoStrYYYYMMDDHHMMSS((credit.getAddTime()+"")));
						list.add(param1);
						
						
						if(credit.getCreditStatus()==0){
							continue;
						}

						if(BigDecimal.ZERO.compareTo(credit.getCreditCapitalAssigned())==0){
							//募集失败状态
							Integer now=GetDate.getNowTime10();
							Map<String, Object> param3 = new LinkedHashMap<String, Object>();
							//接口版本号
							param3.put("version", CertCallConstant.CERT_CALL_VERSION);
							//平台编号
							param3.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
							//转让项目编号
							param3.put("transferId", credit.getCreditNid()+"");
							//状态编码
							param3.put("transferStatus", "3");
							//成功转让本金金额(元)
							param3.put("amount", "0.00");
							//成功转让利息金额 (元)
							param3.put("interest", "0.00");
							//成功转让浮动金额 (元)
							param3.put("floatMoney", "0.00");
							//状态更新时间
							param3.put("productDate", GetDate.timestamptoStrYYYYMMDDHHMMSS((now<=credit.getEndTime()?now:credit.getEndTime())+""));
							list.add(param3);
							
						}else{
							//募集成功状态
							CreditTenderExample creditTenderExample=new CreditTenderExample();
							creditTenderExample.createCriteria().andCreditNidEqualTo(credit.getCreditNid()+"");
							List<CreditTender> creditTenderList=creditTenderMapper.selectByExample(creditTenderExample);
							Map<String, Object> param2 = new LinkedHashMap<String, Object>();
							//接口版本号
							param2.put("version", CertCallConstant.CERT_CALL_VERSION);
							//平台编号
							param2.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
							//转让项目编号
							param2.put("transferId", credit.getCreditNid()+"");
							//状态编码
							param2.put("transferStatus", "2");
							//成功转让本金金额(元)
							param2.put("amount", credit.getCreditCapitalAssigned());
							//成功转让利息金额 (元)
							param2.put("interest", credit.getCreditInterestAssigned());
							//成功转让浮动金额 (元)
							param2.put("floatMoney", BigDecimal.ZERO.subtract(credit.getCreditCapitalAssigned().
									multiply(credit.getCreditDiscount().divide(new BigDecimal(100)))).setScale(2, BigDecimal.ROUND_HALF_DOWN));
							
							if(credit.getCreditCapitalAssigned().compareTo(credit.getCreditCapital())==0){
								//状态更新时间
								param2.put("productDate",  GetDate.timestamptoStrYYYYMMDDHHMMSS((creditTenderList.get(creditTenderList.size()-1).getAddTime()+"")));
							}else{
								//状态更新时间
								param2.put("productDate",  GetDate.timestamptoStrYYYYMMDDHHMMSS(credit.getEndTime()+""));
							}

							list.add(param2);

							//开始计息中状态
							Map<String, Object> param4 = new LinkedHashMap<String, Object>();
							//接口版本号
							param4.put("version", CertCallConstant.CERT_CALL_VERSION);
							//平台编号
							param4.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
							//转让项目编号
							param4.put("transferId", credit.getCreditNid()+"");
							//状态编码
							param4.put("transferStatus", "4");
							//成功转让本金金额(元)
							param4.put("amount", "0.00");
							//成功转让利息金额 (元)
							param4.put("interest", "0.00");
							//成功转让浮动金额 (元)
							param4.put("floatMoney", "0.00");
							//状态更新时间
							if(credit.getCreditCapitalAssigned().compareTo(credit.getCreditCapital())==0){
								//状态更新时间
								param4.put("productDate",  GetDate.timestamptoStrYYYYMMDDHHMMSS((creditTenderList.get(creditTenderList.size()-1).getAddTime()+"")));
							}else{
								//状态更新时间
								param4.put("productDate",  GetDate.timestamptoStrYYYYMMDDHHMMSS(credit.getEndTime()+""));
							}
							list.add(param4);
						}
						BorrowRepayExample borrowRepayExample=new BorrowRepayExample();
						borrowRepayExample.createCriteria().andBorrowNidEqualTo(borrowNid);
						List<BorrowRepay> borrowRepays=borrowRepayMapper.selectByExample(borrowRepayExample);
						BorrowRepay borrowRepay=borrowRepays.get(0);
						if(borrowRepay.getRepayStatus()==1){
							//开始计息中状态
							Map<String, Object> param5 = new LinkedHashMap<String, Object>();
							//接口版本号
							param5.put("version", CertCallConstant.CERT_CALL_VERSION);
							//平台编号
							param5.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
							//转让项目编号
							param5.put("transferId", credit.getCreditNid()+"");
							//状态编码
							param5.put("transferStatus", "5");
							//成功转让本金金额(元)
							param5.put("amount", "0.00");
							//成功转让利息金额 (元)
							param5.put("interest", "0.00");
							//成功转让浮动金额 (元)
							param5.put("floatMoney", "0.00");
							//状态更新时间
							param5.put("productDate", GetDate.timestamptoStrYYYYMMDDHHMMSS(borrowRepay.getRepayActionTime()));

							list.add(param5);
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else{
			HjhDebtCreditExample example=new HjhDebtCreditExample();
			example.createCriteria().andBorrowNidEqualTo(borrowNid).andDelFlagEqualTo(0);
			List<HjhDebtCredit> hjhDebtCreditList=hjhDebtCreditMapper.selectByExample(example);
			if(hjhDebtCreditList!=null&&hjhDebtCreditList.size()>0){
				try {
					for (HjhDebtCredit hjhDebtCredit : hjhDebtCreditList) {
						//募集初始状态
						Map<String, Object> param0 = new LinkedHashMap<String, Object>();
						//接口版本号
						param0.put("version", CertCallConstant.CERT_CALL_VERSION);
						//平台编号
						param0.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
						//转让项目编号
						param0.put("transferId", hjhDebtCredit.getCreditNid()+"");
						//状态编码
						param0.put("transferStatus", "0");
						//成功转让本金金额(元)
						param0.put("amount", "0.00");
						//成功转让利息金额 (元)
						param0.put("interest", "0.00");
						//成功转让浮动金额 (元)
						param0.put("floatMoney", "0.00");
						//状态更新时间
						param0.put("productDate", GetDate.timestamptoStrYYYYMMDDHHMMSS((hjhDebtCredit.getCreateTime()+"")));
						list.add(param0);
						//开始募集状态
						Map<String, Object> param1 = new LinkedHashMap<String, Object>();
						//接口版本号
						param1.put("version", CertCallConstant.CERT_CALL_VERSION);
						//平台编号
						param1.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
						//转让项目编号
						param1.put("transferId", hjhDebtCredit.getCreditNid()+"");
						//状态编码
						param1.put("transferStatus", "1");
						//成功转让本金金额(元)
						param1.put("amount", "0.00");
						//成功转让利息金额 (元)
						param1.put("interest", "0.00");
						//成功转让浮动金额 (元)
						param1.put("floatMoney", "0.00");
						//状态更新时间
						param1.put("productDate", GetDate.timestamptoStrYYYYMMDDHHMMSS((hjhDebtCredit.getCreateTime()+"")));
						list.add(param1);
						
						
						if(hjhDebtCredit.getCreditStatus()==0){
							continue;
						}

						if(BigDecimal.ZERO.compareTo(hjhDebtCredit.getCreditCapitalAssigned())==0){
							//募集失败状态
							Integer now=GetDate.getNowTime10();
							Map<String, Object> param3 = new LinkedHashMap<String, Object>();
							//接口版本号
							param3.put("version", CertCallConstant.CERT_CALL_VERSION);
							//平台编号
							param3.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
							//转让项目编号
							param3.put("transferId", hjhDebtCredit.getCreditNid()+"");
							//状态编码
							param3.put("transferStatus", "3");
							//成功转让本金金额(元)
							param3.put("amount", "0.00");
							//成功转让利息金额 (元)
							param3.put("interest", "0.00");
							//成功转让浮动金额 (元)
							param3.put("floatMoney", "0.00");
							//状态更新时间
							param3.put("productDate", GetDate.timestamptoStrYYYYMMDDHHMMSS((now<=hjhDebtCredit.getEndTime()?now:hjhDebtCredit.getEndTime())+""));
							list.add(param3);
							
						}else{
							//募集成功状态
							
							HjhDebtCreditTenderExample hjhDebtCreditTenderExample=new HjhDebtCreditTenderExample();
							hjhDebtCreditTenderExample.createCriteria().andCreditNidEqualTo(hjhDebtCredit.getCreditNid()+"");
							List<HjhDebtCreditTender> hjhDebtCreditTenderList=hjhDebtCreditTenderMapper.selectByExample(hjhDebtCreditTenderExample);
							
							Map<String, Object> param2 = new LinkedHashMap<String, Object>();
							//接口版本号
							param2.put("version", CertCallConstant.CERT_CALL_VERSION);
							//平台编号
							param2.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
							//转让项目编号
							param2.put("transferId", hjhDebtCredit.getCreditNid()+"");
							//状态编码
							param2.put("transferStatus", "2");
							//成功转让本金金额(元)
							param2.put("amount", hjhDebtCredit.getCreditCapitalAssigned());
							//成功转让利息金额 (元)
							param2.put("interest", hjhDebtCredit.getCreditInterestAssigned());
							//成功转让浮动金额 (元)
							param2.put("floatMoney", "0.00");
							
							if(hjhDebtCredit.getCreditCapitalAssigned().compareTo(hjhDebtCredit.getCreditCapital())==0){
								//状态更新时间
								param2.put("productDate",  GetDate.timestamptoStrYYYYMMDDHHMMSS((hjhDebtCreditTenderList.get(hjhDebtCreditTenderList.size()-1).getCreateTime())+""));
							}else{
								//状态更新时间
								param2.put("productDate",  GetDate.timestamptoStrYYYYMMDDHHMMSS(
										(hjhDebtCredit.getEndTime()==0?hjhDebtCredit.getUpdateTime():hjhDebtCredit.getEndTime())+""));
							}
							list.add(param2);

							//开始计息中状态
							Map<String, Object> param4 = new LinkedHashMap<String, Object>();
							//接口版本号
							param4.put("version", CertCallConstant.CERT_CALL_VERSION);
							//平台编号
							param4.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
							//转让项目编号
							param4.put("transferId", hjhDebtCredit.getCreditNid()+"");
							//状态编码
							param4.put("transferStatus", "4");
							//成功转让本金金额(元)
							param4.put("amount", "0.00");
							//成功转让利息金额 (元)
							param4.put("interest", "0.00");
							//成功转让浮动金额 (元)
							param4.put("floatMoney", "0.00");
							
							if(hjhDebtCredit.getCreditCapitalAssigned().compareTo(hjhDebtCredit.getCreditCapital())==0){
								//状态更新时间
								param4.put("productDate",  GetDate.timestamptoStrYYYYMMDDHHMMSS((hjhDebtCreditTenderList.get(hjhDebtCreditTenderList.size()-1).getCreateTime())+""));
							}else{
								//状态更新时间
								param4.put("productDate",  GetDate.timestamptoStrYYYYMMDDHHMMSS(
										(hjhDebtCredit.getEndTime()==0?hjhDebtCredit.getUpdateTime():hjhDebtCredit.getEndTime())+""));
							}
							list.add(param4);
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		for (Map<String, Object> map : list) {
			String productDate=(String) map.get("productDate");
			String[] arg=productDate.split("-");
			map.put("groupByDate", arg[0]+"-"+arg[1]);
		}
		return list;
	}


	/**
     * 批量修改状态
     *
     * @param update
     */
    @Override
    public int updateCertBorrowStatusBatch(CertBorrowUpdate update) {

        CertBorrowExample example = new CertBorrowExample();
        CertBorrowExample.Criteria cra = example.createCriteria();
        cra.andIdIn(update.getIds());

        return certBorrowMapper.updateByExampleSelective(update.getCertBorrow(),example);
    }

	

}

