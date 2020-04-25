package com.hyjf.bank.service.borrow.record;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import redis.clients.jedis.JedisPool;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.borrow.AssetServiceImpl;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.HjhAssetBorrowType;
import com.hyjf.mybatis.model.auto.HjhAssetBorrowTypeExample;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;
import com.hyjf.mybatis.model.auto.HjhPlanAssetExample;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.auto.HjhUserAuthExample;
import com.hyjf.mybatis.model.auto.STZHWhiteList;
import com.hyjf.mybatis.model.auto.STZHWhiteListExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

@Service
public class AutoRecordServiceImpl extends AssetServiceImpl implements AutoRecordService {
	
	Logger _log = LoggerFactory.getLogger(AutoRecordServiceImpl.class);

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;
	

	public static JedisPool pool = RedisUtils.getPool();
	/**
	 * 汇消费的项目类型编号
	 */
	public static String PROJECT_TYPE_HXF = "8";
	
	/**
	 * 查询已经初审中状态的资产
	 * 
	 * @param accountManageBean
	 * @return
	 */
	@Override
	public List<HjhPlanAsset> selectAutoRecordList() {
		
		HjhPlanAssetExample example = new HjhPlanAssetExample();
		HjhPlanAssetExample.Criteria crt = example.createCriteria();
        crt.andVerifyStatusEqualTo(1);
        crt.andStatusEqualTo(3);//备案中
        
        List<HjhPlanAsset> list = this.hjhPlanAssetMapper.selectByExample(example);
		
		return list;
	}
	
	/**
	 * 资产自动备案-自动初审
	 * 
	 * @param hjhPlanAsset
	 * @return
	 */

	@Override
	public boolean updateRecordBorrow(HjhPlanAsset hjhPlanAsset,HjhAssetBorrowType hjhAssetBorrowType) {
		
		// 备案，需要更新资产表
		JSONObject jsonObject = debtRegist(hjhPlanAsset.getBorrowNid());

		//更新资产表为初审中
		if("0".equals(jsonObject.get("success"))){
			
			HjhPlanAsset hjhPlanAssetnew = new HjhPlanAsset();
			hjhPlanAssetnew.setId(hjhPlanAsset.getId());
			
			// 受托支付，更新为待授权
			if(hjhPlanAsset.getEntrustedFlg() != null && hjhPlanAsset.getEntrustedFlg().intValue() ==1){
				hjhPlanAssetnew.setStatus(4);//4 待授权
			}else{
				hjhPlanAssetnew.setStatus(5);//初审中
			}
			//获取当前时间
			int nowTime = GetDate.getNowTime10();
			hjhPlanAssetnew.setUpdateTime(nowTime);
			hjhPlanAssetnew.setUpdateUserId(1);
			boolean borrowFlag = this.hjhPlanAssetMapper.updateByPrimaryKeySelective(hjhPlanAssetnew)>0?true:false;
			if(borrowFlag){
				return true;
			}
		
		// 重复失败的情况
		}else if("2".equals(jsonObject.get("success"))) {
			_log.info("备案失败 "+hjhPlanAsset.getBorrowNid() + " 原因："+jsonObject.get("msg"));
		}else{
			_log.info("备案失败 "+hjhPlanAsset.getBorrowNid()+ " 原因："+jsonObject.get("msg"));
			// 备案失败发送短信//TODO: 没有配合模板
			// 您好，有一笔标的备案失败，请及时关注！
	        Map<String, String> replaceStrs = new HashMap<String, String>();
	        replaceStrs.put("val_title", hjhPlanAsset.getBorrowNid());
	        SmsMessage smsMessage = new SmsMessage(null, replaceStrs, null, null, MessageDefine.SMSSENDFORMANAGER, null, CustomConstants.PARAM_TPL_NOTICE_BORROW_RECORD_FAIL, CustomConstants.CHANNEL_TYPE_NORMAL);
	        smsProcesser.gather(smsMessage);
		}
		
		
		return false;
	}

	/**
	 * 标的自动备案
	 * 
	 * @param hjhPlanAsset
	 * @return
	 * @author PC-LIUSHOUYI
	 */
	@Override
	public boolean updateRecordBorrow(Borrow borrow) {
		
		// 备案，需要更新资产表
		JSONObject jsonObject = debtRegist(borrow.getBorrowNid());

		//更新资产表为初审中
		if("0".equals(jsonObject.get("success"))){
			return true;
		// 重复失败的情况
		}else if("2".equals(jsonObject.get("success"))) {
			_log.info("备案失败 "+borrow.getBorrowNid() + " 原因："+jsonObject.get("msg"));
		}else{
			_log.info("备案失败 "+borrow.getBorrowNid()+ " 原因："+jsonObject.get("msg"));
			// 备案失败发送短信//TODO: 没有配合模板
			// 您好，有一笔标的备案失败，请及时关注！
	        Map<String, String> replaceStrs = new HashMap<String, String>();
	        replaceStrs.put("val_title", borrow.getBorrowNid());
	        SmsMessage smsMessage = new SmsMessage(null, replaceStrs, null, null, MessageDefine.SMSSENDFORMANAGER, null, CustomConstants.PARAM_TPL_NOTICE_BORROW_RECORD_FAIL, CustomConstants.CHANNEL_TYPE_NORMAL);
	        smsProcesser.gather(smsMessage);
		}
		
		return false;
		
	}
	
	/**
	 * 备案
	 * @param borrowNid
	 * @return
	 */
	public JSONObject debtRegist(String borrowNid) {
		// 返回结果
		JSONObject result = new JSONObject();
		// 获取相应的标的详情
		BorrowExample borrowExample = new BorrowExample();
		BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
		borrowCra.andBorrowNidEqualTo(borrowNid);
		List<BorrowWithBLOBs> borrowList = this.borrowMapper.selectByExampleWithBLOBs(borrowExample);// 获取相应的标的信息
		if (borrowList != null && borrowList.size() == 1) {
			BorrowWithBLOBs borrow = borrowList.get(0);
			
			// 检查是否备案失败，如果是，跳过
			if(borrow.getStatus()==0 && borrow.getRegistStatus()==4){
				_log.info("标的："+borrowNid+" 自动备案失败过");
				result.put("success", "2");
				result.put("msg", "自动备案失败过！");
				return result;
			}
			
			
			String borrowStyle = borrow.getBorrowStyle();// 項目还款方式
			// 是否月标(true:月标, false:天标)
			boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
					|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
			int userId = borrow.getUserId();// 借款人userId
			Users user = this.getUsersByUserId(userId);// 借款人用户
			if (Validator.isNotNull(user)) {
				BankOpenAccount bankOpenAccount = this.getBankOpenAccount(userId);
				if (Validator.isNotNull(bankOpenAccount)) {
					// 更新相应的标的状态为备案中
					boolean debtRegistingFlag = this.updateBorrowRegist(borrow, 0, 1);
					if (debtRegistingFlag) {
						// 获取共同参数
						String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
						String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
						String channel = BankCallConstant.CHANNEL_PC;
						String orderId = GetOrderIdUtils.getOrderId2(user.getUserId());
						String orderDate = GetOrderIdUtils.getOrderDate();
						String txDate = GetOrderIdUtils.getTxDate();
						String txTime = GetOrderIdUtils.getTxTime();
						String seqNo = GetOrderIdUtils.getSeqNo(6);
						// 调用开户接口
						BankCallBean debtRegistBean = new BankCallBean();
						debtRegistBean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
						debtRegistBean.setTxCode(BankCallConstant.TXCODE_DEBT_REGISTER);// 消息类型(用户开户)
						debtRegistBean.setInstCode(instCode);// 机构代码
						debtRegistBean.setBankCode(bankCode);
						debtRegistBean.setTxDate(txDate);
						debtRegistBean.setTxTime(txTime);
						debtRegistBean.setSeqNo(seqNo);
						debtRegistBean.setChannel(channel);
						debtRegistBean.setAccountId(bankOpenAccount.getAccount());// 借款人电子账号
						debtRegistBean.setProductId(borrowNid);// 标的表id
						debtRegistBean.setProductDesc(borrow.getName());// 标的名称
						debtRegistBean.setRaiseDate(borrow.getBankRaiseStartDate());// 募集日,标的保存时间
						debtRegistBean.setRaiseEndDate(borrow.getBankRaiseEndDate());// 募集结束日期
						if (isMonth) {
							debtRegistBean.setIntType(BankCallConstant.DEBT_INTTYPE_UNCERTAINDATE);// 付息方式没有不确定日期
						} else {
							debtRegistBean.setIntType(BankCallConstant.DEBT_INTTYPE_EXPIREDATE);
						}
						debtRegistBean.setDuration(String.valueOf(borrow.getBankBorrowDays()));// (借款期限,天数）
						debtRegistBean.setTxAmount(String.valueOf(borrow.getAccount()));// 交易金额
						debtRegistBean.setRate(String.valueOf(borrow.getBorrowApr()));// 年华利率
						
						// 受托支付 
						/*receiptAccountId	收款人电子帐户	A	19	C	当entrustFlag不为空时必填
						entrustFlag	受托支付标志	A	1	C	为空时单一借款人模式
						0：非受托支付业务类别
						1：受托支付业务类别*/
						if(borrow.getEntrustedFlg()!= null && borrow.getEntrustedFlg().intValue() ==1){
							STZHWhiteListExample sTZHWhiteListExample = new STZHWhiteListExample();
							STZHWhiteListExample.Criteria sTZHCra = sTZHWhiteListExample.createCriteria();
							sTZHCra.andStUserIdEqualTo(borrow.getEntrustedUserId());
							sTZHCra.andInstcodeEqualTo(borrow.getInstCode());
							/*----------------upd by liushouyi HJH3 Start-----------------------*/
							sTZHCra.andDelFlgEqualTo(0);
							sTZHCra.andStateEqualTo(1);
							/*----------------upd by liushouyi HJH3 End-----------------------*/
							List<STZHWhiteList> sTZHWhiteList = this.sTZHWhiteListMapper.selectByExample(sTZHWhiteListExample);
							
							if (sTZHWhiteList != null && sTZHWhiteList.size() >= 1) {
								STZHWhiteList stzf = sTZHWhiteList.get(0);
								debtRegistBean.setEntrustFlag(borrow.getEntrustedFlg().toString());
								debtRegistBean.setReceiptAccountId(stzf.getStAccountid());
							/*----------------upd by liushouyi HJH3 Start-----------------------*/
							} else {
								//原逻辑三方资产此处未处理、推送资产时已校验过白名单、手动录标的时候白名单依然做校验处理
								this.updateBorrowRegist(borrow, 0, 4);
								result.put("error", "1");
								result.put("msg", "受托白名单查询为空！");
								return result;
							}
							/*----------------upd by liushouyi HJH3 End-----------------------*/
							
						}
						
						if (Validator.isNotNull(borrow.getRepayOrgUserId())) {
							BankOpenAccount account = this.getBankOpenAccount(borrow.getRepayOrgUserId());
							if (Validator.isNotNull(account)) {
								debtRegistBean.setBailAccountId(account.getAccount());
							}
						}
						debtRegistBean.setLogOrderId(orderId);
						debtRegistBean.setLogOrderDate(orderDate);
						debtRegistBean.setLogUserId(String.valueOf(user.getUserId()));
						debtRegistBean.setLogRemark("借款人标的登记");
						debtRegistBean.setLogClient(0);
						try {
							BankCallBean registResult = BankCallUtils.callApiBg(debtRegistBean);
							String retCode = StringUtils.isNotBlank(registResult.getRetCode()) ? registResult.getRetCode() : "";
							if (BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
								
								// 如果是受托支付 备案成功时更改状态
								int status = 1;
								if(borrow.getEntrustedFlg()!= null && borrow.getEntrustedFlg().intValue() ==1){
									status = 7;
								}
								
								boolean debtRegistedFlag = this.updateBorrowRegist(borrow, status, 2);
								if (debtRegistedFlag) {
									result.put("success", "0");
									result.put("msg", "备案成功！");
								} else {
									result.put("success", "1");
									result.put("msg", "备案成功后，更新相应的状态失败,请联系客服！");
								}
							} else {
								this.updateBorrowRegist(borrow, 0, 4);
								String message = registResult.getRetMsg();
								result.put("success", "1");
								result.put("msg", StringUtils.isNotBlank(message) ? message : "银行备案接口调用失败！");
							}
						} catch (Exception e) {
							e.printStackTrace();
							this.updateBorrowRegist(borrow, 0, 4);
							result.put("success", "1");
							result.put("msg", "银行备案接口调用失败！");
						}
					} else {
						result.put("success", "1");
						result.put("msg", "更新相应的标的信息失败,请稍后再试！");
					}
				}
			} else {
				result.put("success", "1");
				result.put("msg", "借款人信息错误！");
			}
		} else {
			result.put("success", "1");
			result.put("msg", "项目编号为空！");
			_log.info("标的："+borrowNid+" 项目编号为空");
		}
		return result;
	}

	private boolean updateBorrowRegist(BorrowWithBLOBs borrow, int status, int registStatus) {
		Date nowDate = new Date();
//		AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
		BorrowExample example = new BorrowExample();
		example.createCriteria().andIdEqualTo(borrow.getId()).andStatusEqualTo(borrow.getStatus()).andRegistStatusEqualTo(borrow.getRegistStatus());
		borrow.setRegistStatus(registStatus);
		borrow.setStatus(status);
		borrow.setRegistUserId(1);//TODO:id写死1
		borrow.setRegistUserName("AutoRecord");
		borrow.setRegistTime(nowDate);
		boolean flag = this.borrowMapper.updateByExampleSelective(borrow, example) > 0 ? true : false;
		return flag;
	}

	/**
	 * 获取资产项目类型
	 * 
	 * @return
	 */
	public HjhAssetBorrowType selectAssetBorrowType(HjhPlanAsset hjhPlanAsset) {
		HjhAssetBorrowTypeExample example = new HjhAssetBorrowTypeExample();
		HjhAssetBorrowTypeExample.Criteria cra = example.createCriteria();
		cra.andInstCodeEqualTo(hjhPlanAsset.getInstCode());
		cra.andAssetTypeEqualTo(hjhPlanAsset.getAssetType());
		cra.andIsOpenEqualTo(1);

		List<HjhAssetBorrowType> list = this.hjhAssetBorrowTypeMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		
		return null;
		
	}
	
	/**
	 * 获取标的自动流程配置
	 * 
	 * @param borrow
	 * @return
	 * @author PC-LIUSHOUYI
	 */
	public HjhAssetBorrowType selectAssetBorrowType(Borrow borrow) {
		HjhAssetBorrowTypeExample example = new HjhAssetBorrowTypeExample();
		HjhAssetBorrowTypeExample.Criteria cra = example.createCriteria();
		cra.andInstCodeEqualTo(borrow.getInstCode());
		cra.andAssetTypeEqualTo(borrow.getAssetType());
		cra.andIsOpenEqualTo(1);

		List<HjhAssetBorrowType> list = this.hjhAssetBorrowTypeMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		
		return null;
		
	}
	
	@Override
	public HjhUserAuth getHjhUserAuthByUserId(Integer userId) {
        HjhUserAuthExample example = new HjhUserAuthExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<HjhUserAuth> list=hjhUserAuthMapper.selectByExample(example);
        if(list!=null&& list.size()>0){
            return list.get(0);
        }else{
            return null;    
        }
	}

	@Override
	public HjhUserAuth getHjhUserAuthByUserID(Integer userId) {
        HjhUserAuthExample example = new HjhUserAuthExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<HjhUserAuth> list=hjhUserAuthMapper.selectByExample(example);
        if(list!=null&& list.size()>0){
            return list.get(0);
        }else{
            return null;    
        }
	}
}
