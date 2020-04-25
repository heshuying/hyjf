package com.hyjf.mqreceiver.hgdatareport.cert.olddata.transact.repay;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RedisConstants;
import com.hyjf.mongo.hgdatareport.dao.CertAccountListDao;
import com.hyjf.mongo.hgdatareport.entity.CertAccountList;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportServiceImpl;
import com.hyjf.mqreceiver.hgdatareport.cert.transact.CertTradeTypeEnum;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallConstant;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallUtil;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.BorrowRepayExample;
import com.hyjf.mybatis.model.auto.BorrowRepayPlan;
import com.hyjf.mybatis.model.auto.BorrowRepayPlanExample;
import com.hyjf.mybatis.model.auto.CertBorrow;
import com.hyjf.mybatis.model.auto.CertBorrowExample;
import com.hyjf.mybatis.model.auto.CertUser;
import com.hyjf.mybatis.model.auto.CertUserExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.CertAccountListCustomize;
/**
 * @author pcc
 */

@Service
public class OldCertTransactRepaySuccessServiceImpl extends BaseHgCertReportServiceImpl implements OldCertTransactRepaySuccessService {

	
	Logger logger = LoggerFactory.getLogger(OldCertTransactRepaySuccessServiceImpl.class);

    private String thisMessName = "历史数据易明细信息(还款信息)";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_CERT + " " + thisMessName + "】";
	


	@Override
	public List<Map<String, Object>> getTransactMap(Map<String, Object> param) {
		List<CertAccountListCustomize> accountLists=accountDetailCustomizeMapper.queryOldCertAccountListByRepaySuccess(param);
		if(accountLists==null||accountLists.size()==0){
			RedisUtils.set("oldCertTransactRepaySuccessMqStop","1");
			return null;
		}
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		
		for (CertAccountListCustomize accountList : accountLists) {
			try {
				createParam(accountList,list);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		if(list==null||list.size()==0){
			return null;
		}
		for (Map<String, Object> map1 : list) {
			try {
				String productDate=(String) map1.get("transDate");
				String[] arg=productDate.split("-");
				map1.put("groupByDate", arg[0]+"-"+arg[1]);
			} catch (Exception e) {
				continue;
			}
		}
		
		return list;
	}

	
	private void createParam(CertAccountListCustomize accountList,List<Map<String, Object>> list) throws Exception {
		
		
		
			//还款 发送18还款本金  19还款利息  5交易手续费（还款服务费）
			Borrow borrow = this.getBorrowByBorrowNid(accountList.getRemark());
			if(borrow==null||borrow.getBorrowNid()==null){
				return;
			}
			if(existBorrow(accountList.getRemark())){
				return;
			}
			borrow= this.getBorrowByBorrowNid(accountList.getRemark());
			String period = borrow.getBorrowNid();
			
			CertUserExample certUserExample = new CertUserExample();
			certUserExample.createCriteria().
			andBorrowNidEqualTo(accountList.getRemark()).
			andUserIdEqualTo(borrow.getUserId());
			List<CertUser> certUser = certUserMapper.selectByExample(certUserExample);
			if(certUser==null||certUser.size()==0){
				certUser=new ArrayList<CertUser>();
				CertUser cUser=new CertUser();
				UsersInfo uInfo=this.getUsersInfoByUserId(borrow.getUserId());
				cUser.setUserIdCardHash(tool.idCardHash(uInfo.getIdcard()));
				certUser.add(cUser);
			}
			
			
			
			if("end".equals(borrow.getBorrowStyle())||"endday".equals(borrow.getBorrowStyle())){
				BigDecimal repayCapitalYes=BigDecimal.ZERO;
				BigDecimal repayInterestYes=BigDecimal.ZERO;
				BigDecimal repayFee=BigDecimal.ZERO;
				BorrowRepayExample borrowRepayExample = new BorrowRepayExample();
				borrowRepayExample.createCriteria().andNidEqualTo(accountList.getNid()).andBorrowNidEqualTo(accountList.getRemark());
				List<BorrowRepay> borrowRepays = borrowRepayMapper.selectByExample(borrowRepayExample);
				repayCapitalYes=borrowRepays.get(0).getRepayCapitalYes();
				repayInterestYes=borrowRepays.get(0).getRepayInterestYes();
				repayFee=borrowRepays.get(0).getRepayFee();
				
				period=period+"-"+borrowRepays.get(0).getRepayPeriod();
				Map<String, Object> param = new HashMap<String, Object>();
				Map<String, Object> param1 = new HashMap<String, Object>();
				Map<String, Object> param2 = new HashMap<String, Object>();
				/****************** 发18还款本金5交易手续费******************/
				//接口版本号
				param.put("version", CertCallConstant.CERT_CALL_VERSION);
				//交易流水时间
				param.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList.getCreateTime()+""));
				//	平台交易流水号
				param.put("transId", accountList.getNid());
				//平台编号
				param.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
				//原散标编号
				param.put("sourceProductCode", borrow.getBorrowNid());
				//原散标名称
				param.put("sourceProductName", borrow.getName());
				//交易类型
				param.put("transType", "18");
				//交易方式
				param.put("transPayment", "a");
				//交易类型描述
				param.put("transTypeDec", CertTradeTypeEnum.getName("18"));
				//交易金额
				param.put("transMoney", repayCapitalYes);
				//	交易日期
				param.put("transDate", GetDate.times10toStrYYYYMMDD(accountList.getCreateTime()));
				//交易人员银行（或三方支付名称）
				param.put("transBank", "江西银行");
				//用户标示哈希
				param.put("userIdcardHash", certUser.get(0).getUserIdCardHash());
				//存管银行流水编号	
				param.put("bankTransId", accountList.getSeqNo());
				//原产品信息编号
				param.put("sourceFinancingCode", "-1");
				//原产品信息名称
				param.put("sourceFinancingName", "-1");
				//债权编号
				param.put("finClaimId", "-1");
				//转让项目编号
				param.put("transferId", "-1");
				//	还款计划编号
				param.put("replanId", period);
				list.add(param);
				
				/****************** 发送19还款利息******************/

				//接口版本号
				param1.put("version", CertCallConstant.CERT_CALL_VERSION);
				//交易流水时间
				param1.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList.getCreateTime()+""));
				//	平台交易流水号
				param1.put("transId", accountList.getNid());
				//平台编号
				param1.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
				//原散标编号
				param1.put("sourceProductCode", borrow.getBorrowNid());
				//原散标名称
				param1.put("sourceProductName", borrow.getName());
				//交易类型
				param1.put("transType", "19");
				//交易方式
				param1.put("transPayment", "a");
				//交易类型描述
				param1.put("transTypeDec", CertTradeTypeEnum.getName("19"));
				//交易金额
				param1.put("transMoney", repayInterestYes);
				//	交易日期
				param1.put("transDate", GetDate.times10toStrYYYYMMDD(accountList.getCreateTime()));
				//交易人员银行（或三方支付名称）
				param1.put("transBank", "江西银行");
				//用户标示哈希
				param1.put("userIdcardHash", certUser.get(0).getUserIdCardHash());
				//存管银行流水编号	
				param1.put("bankTransId", accountList.getSeqNo());
				//原产品信息编号
				param1.put("sourceFinancingCode", "-1");
				//原产品信息名称
				param1.put("sourceFinancingName", "-1");
				//债权编号
				param1.put("finClaimId", "-1");
				//转让项目编号
				param1.put("transferId", "-1");
				//	还款计划编号
				param1.put("replanId", period);
				list.add(param1);	
				
				/****************** 发送5交易手续费******************/
				if(repayFee.compareTo(BigDecimal.ZERO)==1){
					//接口版本号
					param2.put("version", CertCallConstant.CERT_CALL_VERSION);
					//交易流水时间
					param2.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList.getCreateTime()+""));
					//	平台交易流水号
					param2.put("transId", accountList.getNid());
					//平台编号
					param2.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
					//原散标编号
					param2.put("sourceProductCode", borrow.getBorrowNid());
					//原散标名称
					param2.put("sourceProductName", borrow.getName());
					//交易类型
					param2.put("transType", "5");
					//交易方式
					param2.put("transPayment", "a");
					//交易类型描述
					param2.put("transTypeDec", CertTradeTypeEnum.getName("5"));
					//交易金额
					param2.put("transMoney", repayFee);
					//	交易日期
					param2.put("transDate", GetDate.times10toStrYYYYMMDD(accountList.getCreateTime()));
					//交易人员银行（或三方支付名称）
					param2.put("transBank", "江西银行");
					//用户标示哈希
					param2.put("userIdcardHash", certUser.get(0).getUserIdCardHash());
					//存管银行流水编号	
					param2.put("bankTransId", accountList.getSeqNo());
					//原产品信息编号
					param2.put("sourceFinancingCode", "-1");
					//原产品信息名称
					param2.put("sourceFinancingName", "-1");
					//债权编号
					param2.put("finClaimId", "-1");
					//转让项目编号
					param2.put("transferId", "-1");
					//	还款计划编号
					param2.put("replanId", period);
					list.add(param2);
				}
			}else{
				
				BorrowRepayPlanExample borrowRepayPlanExample=new BorrowRepayPlanExample();
				borrowRepayPlanExample.createCriteria().andRepayYestimeBetween(accountList.getCreateTime()-60+"", accountList.getCreateTime()+"").
				andBorrowNidEqualTo(accountList.getRemark());
				List<BorrowRepayPlan> borrowRepayPlans=borrowRepayPlanMapper.selectByExample(borrowRepayPlanExample);
				for (BorrowRepayPlan borrowRepayPlan : borrowRepayPlans) {
					BigDecimal repayCapitalYes=BigDecimal.ZERO;
					BigDecimal repayInterestYes=BigDecimal.ZERO;
					BigDecimal repayFee=BigDecimal.ZERO;
					repayCapitalYes=repayCapitalYes.add(borrowRepayPlan.getRepayCapitalYes());
					repayInterestYes=repayInterestYes.add(borrowRepayPlan.getRepayInterestYes());
					repayFee=repayFee.add(borrowRepayPlan.getRepayFee());
					String replanId=period+"-"+borrowRepayPlan.getRepayPeriod();
					
					Map<String, Object> param18 = new HashMap<String, Object>();
					Map<String, Object> param19 = new HashMap<String, Object>();
					Map<String, Object> param5 = new HashMap<String, Object>();
					/****************** 发18还款本金5交易手续费******************/
					//接口版本号
					param18.put("version", CertCallConstant.CERT_CALL_VERSION);
					//交易流水时间
					param18.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList.getCreateTime()+""));
					//	平台交易流水号
					param18.put("transId", accountList.getNid());
					//平台编号
					param18.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
					//原散标编号
					param18.put("sourceProductCode", borrow.getBorrowNid());
					//原散标名称
					param18.put("sourceProductName", borrow.getName());
					//交易类型
					param18.put("transType", "18");
					//交易方式
					param18.put("transPayment", "a");
					//交易类型描述
					param18.put("transTypeDec", CertTradeTypeEnum.getName("18"));
					//交易金额
					param18.put("transMoney", repayCapitalYes);
					//	交易日期
					param18.put("transDate", GetDate.times10toStrYYYYMMDD(accountList.getCreateTime()));
					//交易人员银行（或三方支付名称）
					param18.put("transBank", "江西银行");
					//用户标示哈希
					param18.put("userIdcardHash", certUser.get(0).getUserIdCardHash());
					//存管银行流水编号	
					param18.put("bankTransId", accountList.getSeqNo());
					//原产品信息编号
					param18.put("sourceFinancingCode", "-1");
					//原产品信息名称
					param18.put("sourceFinancingName", "-1");
					//债权编号
					param18.put("finClaimId", "-1");
					//转让项目编号
					param18.put("transferId", "-1");
					//	还款计划编号
					param18.put("replanId", replanId);
					list.add(param18);
					
					/****************** 发送19还款利息******************/

					//接口版本号
					param19.put("version", CertCallConstant.CERT_CALL_VERSION);
					//交易流水时间
					param19.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList.getCreateTime()+""));
					//	平台交易流水号
					param19.put("transId", accountList.getNid());
					//平台编号
					param19.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
					//原散标编号
					param19.put("sourceProductCode", borrow.getBorrowNid());
					//原散标名称
					param19.put("sourceProductName", borrow.getName());
					//交易类型
					param19.put("transType", "19");
					//交易方式
					param19.put("transPayment", "a");
					//交易类型描述
					param19.put("transTypeDec", CertTradeTypeEnum.getName("19"));
					//交易金额
					param19.put("transMoney", repayInterestYes);
					//	交易日期
					param19.put("transDate", GetDate.times10toStrYYYYMMDD(accountList.getCreateTime()));
					//交易人员银行（或三方支付名称）
					param19.put("transBank", "江西银行");
					//用户标示哈希
					param19.put("userIdcardHash", certUser.get(0).getUserIdCardHash());
					//存管银行流水编号	
					param19.put("bankTransId", accountList.getSeqNo());
					//原产品信息编号
					param19.put("sourceFinancingCode", "-1");
					//原产品信息名称
					param19.put("sourceFinancingName", "-1");
					//债权编号
					param19.put("finClaimId", "-1");
					//转让项目编号
					param19.put("transferId", "-1");
					//	还款计划编号
					param19.put("replanId", replanId);
					list.add(param19);	
					
					/****************** 发送5交易手续费******************/
					if(repayFee.compareTo(BigDecimal.ZERO)==1){
						//接口版本号
						param5.put("version", CertCallConstant.CERT_CALL_VERSION);
						//交易流水时间
						param5.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList.getCreateTime()+""));
						//	平台交易流水号
						param5.put("transId", accountList.getNid());
						//平台编号
						param5.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
						//原散标编号
						param5.put("sourceProductCode", borrow.getBorrowNid());
						//原散标名称
						param5.put("sourceProductName", borrow.getName());
						//交易类型
						param5.put("transType", "5");
						//交易方式
						param5.put("transPayment", "a");
						//交易类型描述
						param5.put("transTypeDec", CertTradeTypeEnum.getName("5"));
						//交易金额
						param5.put("transMoney", repayFee);
						//	交易日期
						param5.put("transDate", GetDate.times10toStrYYYYMMDD(accountList.getCreateTime()));
						//交易人员银行（或三方支付名称）
						param5.put("transBank", "江西银行");
						//用户标示哈希
						param5.put("userIdcardHash", certUser.get(0).getUserIdCardHash());
						//存管银行流水编号	
						param5.put("bankTransId", accountList.getSeqNo());
						//原产品信息编号
						param5.put("sourceFinancingCode", "-1");
						//原产品信息名称
						param5.put("sourceFinancingName", "-1");
						//债权编号
						param5.put("finClaimId", "-1");
						//转让项目编号
						param5.put("transferId", "-1");
						//	还款计划编号
						param5.put("replanId", replanId);
						list.add(param5);
					}
				}
			}
	}


	private boolean existBorrow(String borrowNid) {
		CertBorrowExample borrowExample=new CertBorrowExample();
		borrowExample.createCriteria().andBorrowNidEqualTo(borrowNid);
		List<CertBorrow> list=certBorrowMapper.selectByExample(borrowExample);
		return list.size()>0?false:true;
	}

	@Autowired
    protected CertAccountListDao certAccountListDao;
    @Override
    public void insertAndSendPostOld(CertAccountList bean) {
    	try {
            // 设置共通参数
            bean = this.setCommonParam(bean);
            bean.setIsSend(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 插入mongo
        this.certAccountListDao.insert(bean);
    }
    
    
    /**
     * 设置共通的参数
     * @param bean
     * @return
     */
    protected CertAccountList setCommonParam(CertAccountList bean) throws Exception {
        bean.setVersion(CertCallConstant.CERT_CALL_VERSION);
        JSONArray msg = bean.getDataList();
        long timestamp = System.currentTimeMillis();
        // seqId 规则  今天的递增字段+2位随机数
        String seqId = getBathNum() + CertCallUtil.getRandomNum(100);
        // 交易发生时间
        String tradeDate = "";
        if(bean.getTradeDate()!=null && !"".equals(bean.getTradeDate())){
            tradeDate = bean.getTradeDate();
        }else{
            tradeDate = GetDate.formatTime3();
        }
        bean.setTradeDate(tradeDate);
        // num 说明：如果推送 2017-01-01 到 2017-01-07 七天的数据，则 num 为 7。
        String dateNum =  bean.getDateNum()==null?"0":bean.getDateNum();
        // 批次号 规则  平台编码+交易时间+交易范围数+自增长字段+2位随机数
        String batchNum = tool.batchNumber(CertCallConstant.CERT_SOURCE_CODE, tradeDate ,dateNum,seqId);
        // 随机数
        String nonce = Integer.toHexString(new Random().nextInt());
        String token = CertCallUtil.getApiKey(CertCallConstant.CERT_API_KEY, CertCallConstant.CERT_SOURCE_CODE, bean.getVersion(), timestamp, nonce);
        String url = CertCallConstant.CERT_SEVER_PATH + CertCallUtil.getUrl(bean.getInfType());
        // 判断是否测试环境
        if (CertCallConstant.CERT_IS_TEST == null || "true".equals(CertCallConstant.CERT_IS_TEST)) {
            // 如果是测试环境
            url += CertCallConstant.CERT_TEST_URL;
            // 测试数据
            bean.setDataType("0");
        }else{
            // 正式数据
            bean.setDataType("1");
        }
        bean.setUrl(url);
        // 设置共通的值
        bean.setTimestamp(timestamp+"");
        bean.setNonce(nonce);
        bean.setSourceCode(CertCallConstant.CERT_SOURCE_CODE);
        bean.setApiKey(token);
        bean.setCheckCode(tool.checkCode(timestamp+""));
        bean.setTotalNum(msg.size()+"");
        bean.setSentTime(GetDate.formatTime2());
        bean.setBatchNum(batchNum);
        bean.setLogOrdId(bean.getInfType()+"_"+batchNum);
        // 设置初始状态
        bean.setReportStatus(CertCallConstant.CERT_RETURN_STATUS_INIT);
        return bean;
    }
    
    /**
     * 获得批次号
     * @return
     */
    private String getBathNum(){
        Jedis jedis = pool.getResource();
        // 操作redis
        while ("OK".equals(jedis.watch(RedisConstants.CERT_BATCH_NUMBER_SEQ_ID))) {
            String numberStr = RedisUtils.get(RedisConstants.CERT_BATCH_NUMBER_SEQ_ID);
            JSONObject number = JSONObject.parseObject(numberStr);
            String nowData = GetDate.formatTimeYYYYMM();
            if(nowData.equals(number.get("nowData"))) {
                Transaction tx = jedis.multi();
                // 如果日期相等就直接加1
                Integer seqId = number.getInteger("seqId");
                ++seqId;
                number.put("seqId",seqId);
                tx.set(RedisConstants.CERT_BATCH_NUMBER_SEQ_ID, number.toJSONString());
                List<Object> result1 = tx.exec();
                if (result1 == null || result1.isEmpty()) {
                    continue;
                } else {
                    // 成功
                    return seqId+"";
                }
            }else {
                // 重置为1
                Transaction tx = jedis.multi();
                number = new JSONObject();
                number.put("nowData",nowData);
                // 如果日期相等就直接加1
                number.put("seqId","1");
                tx.set(RedisConstants.CERT_BATCH_NUMBER_SEQ_ID, number.toJSONString());
                List<Object> result1 = tx.exec();
                if (result1 == null || result1.isEmpty()) {
                    continue;
                } else {
                    // 成功
                    return "1";
                }
            }

        }
        return null;
    }
    
}

