package com.hyjf.mqreceiver.hgdatareport.cert.olddata.transferproject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hyjf.common.util.GetDate;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportServiceImpl;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallConstant;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowCredit;
import com.hyjf.mybatis.model.auto.BorrowCreditExample;
import com.hyjf.mybatis.model.auto.CertBorrow;
import com.hyjf.mybatis.model.auto.CertBorrowExample;
import com.hyjf.mybatis.model.auto.HjhDebtCredit;
import com.hyjf.mybatis.model.auto.HjhDebtCreditExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.cert.CertBorrowUpdate;
/**
 * @author pcc
 */

@Service
public class OldCertTransferProjectServiceImpl extends BaseHgCertReportServiceImpl implements OldCertTransferProjectService {
	Logger logger = LoggerFactory.getLogger(OldCertTransferProjectServiceImpl.class);
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
        cra.andIsTransferEqualTo(0);

        example.setLimitStart(0);
        example.setLimitEnd(3000);
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
						UsersInfo usersInfo=this.getUsersInfoByUserId(credit.getCreditUserId());
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
			example.createCriteria().andBorrowNidEqualTo(borrowNid).andDelFlagEqualTo(0);
			List<HjhDebtCredit> hjhDebtCreditList=hjhDebtCreditMapper.selectByExample(example);
			if(hjhDebtCreditList!=null&&hjhDebtCreditList.size()>0){
				try {
					for (HjhDebtCredit hjhDebtCredit : hjhDebtCreditList) {
						UsersInfo usersInfo=this.getUsersInfoByUserId(hjhDebtCredit.getUserId());
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
		for (Map<String, Object> map : list) {
			String productDate=(String) map.get("transferDate");
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

