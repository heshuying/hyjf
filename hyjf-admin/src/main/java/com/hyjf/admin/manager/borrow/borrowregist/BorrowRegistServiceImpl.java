package com.hyjf.admin.manager.borrow.borrowregist;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
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
import com.hyjf.mybatis.model.customize.AdminSystem;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.BorrowRegistCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

@Service
public class BorrowRegistServiceImpl extends BaseServiceImpl implements BorrowRegistService {

    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;
    
	/**
	 * COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	@Override
	public Integer countBorrowRegist(BorrowCommonCustomize corrowCommonCustomize) {
		return this.borrowRegistCustomizeMapper.countBorrowRegist(corrowCommonCustomize);
	}

	/**
	 * 借款列表
	 * 
	 * @return
	 */
	@Override
	public List<BorrowRegistCustomize> selectBorrowRegistList(BorrowCommonCustomize corrowCommonCustomize) {
		return this.borrowRegistCustomizeMapper.selectBorrowRegistList(corrowCommonCustomize);
	}

	@Override
	public JSONObject debtRegist(String borrowNid, JSONObject result) {

		// 获取相应的标的详情
		BorrowExample borrowExample = new BorrowExample();
		BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
		borrowCra.andBorrowNidEqualTo(borrowNid);
		List<BorrowWithBLOBs> borrowList = this.borrowMapper.selectByExampleWithBLOBs(borrowExample);// 获取相应的标的信息
		if (borrowList != null && borrowList.size() == 1) {
			BorrowWithBLOBs borrow = borrowList.get(0);
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
						// 调用备案接口
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
						
						//备案接口(EntrustFlag和ReceiptAccountId要么都传，要么都不传)
						if(borrow.getEntrustedFlg()==1){
							STZHWhiteListExample sTZHWhiteListExample = new STZHWhiteListExample();
							STZHWhiteListExample.Criteria sTZHCra = sTZHWhiteListExample.createCriteria();
							sTZHCra.andStUserIdEqualTo(borrow.getEntrustedUserId());
							sTZHCra.andInstcodeEqualTo(borrow.getInstCode().trim());
							sTZHCra.andDelFlgEqualTo(0);
							sTZHCra.andStateEqualTo(1);
							List<STZHWhiteList> sTZHWhiteList = this.sTZHWhiteListMapper.selectByExample(sTZHWhiteListExample);
							if (sTZHWhiteList != null && sTZHWhiteList.size() >= 1) {
								STZHWhiteList stzf = sTZHWhiteList.get(0);
								debtRegistBean.setEntrustFlag(borrow.getEntrustedFlg().toString());
								debtRegistBean.setReceiptAccountId(stzf.getStAccountid());
							} else {
								this.updateBorrowRegist(borrow, 0, 4);
								result.put("error", "1");
								result.put("msg", "受托白名单查询为空！");
								return result;
							}		
						} 
						try {
							BankCallBean registResult = BankCallUtils.callApiBg(debtRegistBean);
							String retCode = StringUtils.isNotBlank(registResult.getRetCode()) ? registResult.getRetCode() : "";
							if (BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
							
								//new added by 受托支付备案
								if(borrow.getEntrustedFlg()==1){
									boolean debtEntrustedRegistedFlag = this.updateEntrustedBorrowRegist(borrow, 7, 2);
									if (debtEntrustedRegistedFlag) {
										result.put("success", "0");
										result.put("msg", "备案成功！");
										
									} else {
										result.put("success", "1");
										result.put("msg", "备案成功后，更新相应的状态失败,请联系客服！");
									}
								} else {
									boolean debtRegistedFlag = this.updateBorrowRegist(borrow, 1, 2);
									if (debtRegistedFlag) {
										result.put("success", "0");
										result.put("msg", "备案成功！");
										
									} else {
										result.put("success", "1");
										result.put("msg", "备案成功后，更新相应的状态失败,请联系客服！");
									}
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
		}
		return result;
	}

	@Override
	public String sumBorrowRegistAccount(BorrowCommonCustomize corrowCommonCustomize) {
		String sumAccount = this.borrowRegistCustomizeMapper.sumBorrowRegistAccount(corrowCommonCustomize);
		return sumAccount;
	}

	private boolean updateBorrowRegist(BorrowWithBLOBs borrow, int status, int registStatus) {
		Date nowDate = new Date();
		AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
		BorrowExample example = new BorrowExample();
		example.createCriteria().andIdEqualTo(borrow.getId()).andStatusEqualTo(borrow.getStatus()).andRegistStatusEqualTo(borrow.getRegistStatus());
		borrow.setRegistStatus(registStatus);
		borrow.setStatus(status);
		// add by liuyang 合规自查 20181119 start
		borrow.setVerifyStatus(1);//跳过已交保证金状态
		// add by liuyang 合规自查 20181119 end
		borrow.setRegistUserId(Integer.parseInt(adminSystem.getId()));
		borrow.setRegistUserName(adminSystem.getUsername());
		borrow.setRegistTime(nowDate);
		boolean flag = this.borrowMapper.updateByExampleSelective(borrow, example) > 0 ? true : false;
		return flag;
	}

	/**
	 * 获取资产项目类型
	 * 
	 * @return
	 */
	private HjhAssetBorrowType selectAssetBorrowType(String instCode,Integer assetType) {
		HjhAssetBorrowTypeExample example = new HjhAssetBorrowTypeExample();
		HjhAssetBorrowTypeExample.Criteria cra = example.createCriteria();
		cra.andInstCodeEqualTo(instCode);
		cra.andAssetTypeEqualTo(assetType);
		cra.andIsOpenEqualTo(1);

		List<HjhAssetBorrowType> list = this.hjhAssetBorrowTypeMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		
		return null;
		
	}
	
	private boolean updateEntrustedBorrowRegist(BorrowWithBLOBs borrow, int status, int registStatus) {
		Date nowDate = new Date();
		AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
		BorrowExample example = new BorrowExample();
		example.createCriteria().andIdEqualTo(borrow.getId());
		borrow.setRegistStatus(registStatus);
		borrow.setStatus(status);
		borrow.setRegistUserId(Integer.parseInt(adminSystem.getId()));
		borrow.setRegistUserName(adminSystem.getUsername());
		borrow.setRegistTime(nowDate);
		boolean flag = this.borrowMapper.updateByExampleSelective(borrow, example) > 0 ? true : false;
		return flag;
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

	@Override
	public List<HjhPlanAsset> getAssetListByBorrowNid(String borrowNid) {
		HjhPlanAssetExample example = new HjhPlanAssetExample();
		HjhPlanAssetExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);
		List<HjhPlanAsset> list = this.hjhPlanAssetMapper.selectByExample(example);
		return list;
	}
	
	/**
	 * 推送消息到MQ
	 * 
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param borrowNid
	 * @param routingKey
	 * @author PC-LIUSHOUYI
	 */
    @Override
    public void sendToMQ(String nid,String InstCode,String routingKey){
		// 加入到消息队列 
        Map<String, String> params = new HashMap<String, String>();
        params.put("mqMsgId", GetCode.getRandomCode(10));
        params.put("instCode", InstCode);
        if ("10000000".equals(InstCode)) {
        	params.put("borrowNid", nid);
        } else {
        	params.put("assetId", nid);
        }
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, routingKey, JSONObject.toJSONString(params));
	}
    
    /**
     * 资产备案成功后更新资产表
     * 
     * @param hjhPlanAsset
     * @param hjhAssetBorrowType
     * @return
     * @author PC-LIUSHOUYI
     */
    @Override
	public boolean updateRecordBorrow(HjhPlanAsset hjhPlanAsset,HjhAssetBorrowType hjhAssetBorrowType) {
		
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
		boolean result =  this.hjhPlanAssetMapper.updateByPrimaryKeySelective(hjhPlanAssetnew)>0?true:false;

		return result;
    }
}
