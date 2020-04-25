package com.hyjf.web.htl.invest;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.Product;
import com.hyjf.mybatis.model.auto.ProductChinapnrLog;
import com.hyjf.mybatis.model.auto.ProductChinapnrSendLogWithBLOBs;
import com.hyjf.mybatis.model.auto.ProductErrorLog;
import com.hyjf.mybatis.model.auto.ProductExample;
import com.hyjf.mybatis.model.auto.ProductInterest;
import com.hyjf.mybatis.model.auto.ProductList;
import com.hyjf.mybatis.model.auto.ProductListExample;
import com.hyjf.mybatis.model.auto.ProductListLog;
import com.hyjf.mybatis.model.auto.ProductListLogExample;
import com.hyjf.mybatis.model.auto.ProductRedeem;
import com.hyjf.mybatis.model.auto.ProductRedeemFail;
import com.hyjf.mybatis.model.auto.ProductRedeemFailExample;
import com.hyjf.mybatis.model.auto.ProductRedeemList;
import com.hyjf.mybatis.model.auto.ProductRedeemListExample;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.mybatis.model.customize.ProductRedeemDetailCustomize;
import com.hyjf.mybatis.model.customize.ProductSearchForPage;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import com.hyjf.web.BaseServiceImpl;
import com.hyjf.web.htl.listener.ProductConstants;
import com.hyjf.web.htl.listener.ProductUtils;

@Service
public class HtlCommonServiceImpl extends BaseServiceImpl implements HtlCommonService {

	@Autowired
    @Qualifier("smsProcesser")
    private MessageProcesser smsProcesser;

	/**
	 * 汇天利购买逻辑
	 * 1、插入汇天利购买日志表，huiyingdai_product_list_log
	 * 2、插入汇天利购买明细表，huiyingdai_product_list
	 * 3、调用汇付接口后，插入汇付日志表，huiyingdai_product_chinapnr_send_log   
	 * 4、汇付返回日志表 ，huiyingdai_product_chinapnr_log
	 * 5、修改产品信息表 product  当前出借总额字段
	 * 6、插入资产明细表huiyingdai_account_list
	 * 7、更新资产表huiyingdai_account
	 * (公司资管账户与用户相反)
	 */
	public int insertBuyProduct(ChinapnrBean bean,ProductList productList) {
		int pid = 0;
		
		/**redis 锁 */
//		if(StringUtils.isNotEmpty(RedisUtils.get("htl_inv:"+productList.getUserId()))){
//			return pid;
//		}else{
//			RedisUtils.set("htl_inv:"+productList.getUserId(),String.valueOf(productList.getAmount()) ,30);
//		}
		
		boolean reslut = RedisUtils.tranactionSet("htl_inv:"+productList.getUserId() ,30);
		// 如果没有设置成功，说明有请求来设置过
		if(!reslut){
			return pid;
		}
		/**********购买明细表************/
		//获得用户对象
		Users users = this.usersMapper.selectByPrimaryKey(productList.getUserId());
		if(users != null){
			//获取推荐人信息
			Integer refUserId = users.getReferrer();
			SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
			SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
			spreadsUsersExampleCriteria.andUserIdEqualTo(productList.getUserId());
			List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
			if (sList != null && !sList.isEmpty()) {
				refUserId = sList.get(0).getSpreadsUserid();
			}
			// 获取出借人属性
			UsersInfo userInfo = getUserInfo(productList.getUserId());
			// 用户属性  0=>无主单                           1=>有主单                     2=>线下员工                    3=>线上员工
			Integer attribute = null;
			if (userInfo != null) {
				attribute = userInfo.getAttribute();
			}
			if (attribute != null) {
				// 如果是线上员工或线下员工，推荐人的userId和username不插
				if (attribute == 2 || attribute == 3) {
					// 查找用户部门信息
					EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(productList.getUserId());
					if (employeeCustomize != null) {
						productList.setReferee(0);
						productList.setArea(employeeCustomize.getRegionId());
						productList.setCompany(employeeCustomize.getBranchId());
						productList.setDepartment(employeeCustomize.getDepartmentId());
					}else{
						productList.setReferee(0);
					}
				}else if (attribute == 1) {// 如果是有主单，全插推荐人信息
					// 查找用户推荐人
					EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
					if (employeeCustomize != null) {
						productList.setReferee(users.getReferrer());
						productList.setArea(employeeCustomize.getRegionId());
						productList.setCompany(employeeCustomize.getBranchId());
						productList.setDepartment(employeeCustomize.getDepartmentId());
					}else{
						if(users.getReferrer() != null){
							productList.setReferee(refUserId);
						}
					}
				}else if (attribute == 0) {// 如果是无主单
					productList.setReferee(refUserId);
				}
			}
		}
		if(productList.getArea() == null){
			productList.setArea(0);
		}
		if(productList.getCompany() == null){
			productList.setCompany(0);;
		}
		if(productList.getDepartment() == null){
			productList.setDepartment(0);
		}
		if(productList.getReferee() == null){
			productList.setReferee(0);
		}
		//插入信息默认字段
		productList.setInterest(new BigDecimal(0));
		productList.setOrderId(productList.getOrderId());
		productList.setOrderDate(GetDate.date2Str(GetDate.yyyyMMdd));
		productList.setInvestTime(GetDate.getNowTime10());
		productList.setInterestTime(GetDate.getTomorrowTimeInMillis());
		productList.setValidDays(0);
		productList.setRedeemed(BigDecimal.ZERO);
		productList.setRestAmount(productList.getAmount());
		productList.setStatus(1);
		productList.setIsNew(1);//中商储
		productList.setInvestStatus(1);//出借状态：0成功，1未付款，2失败 
		productList.setProductId(getProduct().getId()); //产品id
		//插入购买表
		this.productListMapper.insertSelective(productList);
		pid = productList.getId();
		/**********购买日志表************/
		ProductListLog  productListLog = new ProductListLog();
		productListLog.setUserId(productList.getUserId());
		productListLog.setAmount(productList.getAmount());
		productListLog.setProductId(productList.getProductId());
		productListLog.setClient(productList.getClient());
		productListLog.setReferee(productList.getReferee());
		productListLog.setInterest(productList.getInterest());
		productListLog.setOrderId(productList.getOrderId());
		productListLog.setOrderDate(productList.getOrderDate());
		productListLog.setInterestTime(productList.getInterestTime());
		productListLog.setInvestTime(productList.getInvestTime());
		productListLog.setValidDays(productList.getValidDays());
		productListLog.setRedeemed(productList.getRedeemed());
		productListLog.setRestAmount(productList.getRestAmount());
		productListLog.setStatus(productList.getStatus());
		productListLog.setTenderStatus("初始出借");
		productListLog.setArea(productList.getArea());
		productListLog.setDepartment(productList.getDepartment());
		productListLog.setCompany(productList.getCompany());
		productListLog.setIsNew(1);//中商储
		this.productListLogMapper.insertSelective(productListLog);
		
		/**********调用汇付日志表************/
		ProductChinapnrSendLogWithBLOBs productChinapnrSendLog = new ProductChinapnrSendLogWithBLOBs();
		productChinapnrSendLog.setOrdid(bean.getOrdId());
		productChinapnrSendLog.setRemark("主动投标,汇天利购买调用");
		productChinapnrSendLog.setOrddate(bean.getOrdDate());
		productChinapnrSendLog.setMsgType("InitiativeTender");
		productChinapnrSendLog.setClient(productList.getClient());
		productChinapnrSendLog.setCreateTime(GetDate.getNowTime10());
		productChinapnrSendLog.setUserId(productList.getUserId());
		productChinapnrSendLog.setContent("");
		productChinapnrSendLog.setChkvalue(bean.getChkValue());
		productChinapnrSendLog.setMsgdata(JSON.toJSONString(bean.getAllParams()));
		this.productChinapnrSendLogMapper.insertSelective(productChinapnrSendLog);
		return pid;
	}

	/**
	 * 汇天利购买 调用汇付接口之后操作
	 * @param productList
	 * @author Michael
	 */
	public void insertBuyProductReturn(ChinapnrBean bean,int productListId,String ip) {
		ProductList productList = this.productListMapper.selectByPrimaryKey(productListId);
		//返回
		if(productList == null ){
			return;
		}
		/**redis 锁 */
//		if(StringUtils.isNotEmpty(RedisUtils.get("htl:"+productListId))){
//			return ;
//		}else{
//			RedisUtils.set("htl:"+productListId,String.valueOf(productListId) ,30);
//		}
		
		boolean reslut = RedisUtils.tranactionSet("htl:"+productListId ,30);
		// 如果没有设置成功，说明有请求来设置过
		if(!reslut){
			return ;
		}
		
		//判断当前是否已经放款（已经放款的不处理）
		if(StringUtils.isNotEmpty(productList.getLoansId())){
			return;
		}
		//调用放款接口
    	ChinapnrBean retBean = new ChinapnrBean();
    	boolean isLoan = false; //是否放款成功
    	try {
    	    AccountChinapnr accountChinapnrReceiver = null;
    	    if(productList.getIsNew() == 1){
    	    	accountChinapnrReceiver = this.getAccountChinapnr(ProductConstants.BAI_USERID);//白海燕账户，
    	    }else{
    	    	accountChinapnrReceiver = this.getAccountChinapnr(ProductConstants.PUB_USERID);//对公账户
    	    }
    		ChinapnrBean loansbean = new ChinapnrBean();
    		loansbean.setVersion(ChinaPnrConstant.VERSION_10);// 接口版本号
    		loansbean.setCmdId(ChinaPnrConstant.CMDID_LOANS); // 消息类型(放款)
    		loansbean.setOrdId(GetOrderIdUtils.getHtlOrderId()); // 订单号(必须)
    		loansbean.setOrdDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
    		loansbean.setOutCustId(bean.get(ChinaPnrConstant.PARAM_USRCUSTID));
    		loansbean.setTransAmt(bean.get(ChinaPnrConstant.PARAM_TRANSAMT));// 交易金额(必须)
    		loansbean.setFee("0.00");
    		loansbean.setSubOrdId(bean.get(ChinaPnrConstant.PARAM_ORDID));
    		loansbean.setSubOrdDate(bean.get(ChinaPnrConstant.PARAM_ORDDATE));
    		loansbean.setInCustId(String.valueOf(accountChinapnrReceiver.getChinapnrUsrcustid()));
    		loansbean.setIsDefault("N");
    		loansbean.setBgRetUrl(ChinapnrUtil.getBgRetUrl());
    		//调用接口
    	    retBean =  ChinapnrUtil.callApiBg(loansbean);
    	    
    		/**********调用汇付日志表（放款）************/
    		ProductChinapnrSendLogWithBLOBs productChinapnrSendLog = new ProductChinapnrSendLogWithBLOBs();
    		productChinapnrSendLog.setOrdid(loansbean.getOrdId());
    		productChinapnrSendLog.setRemark("汇天利放款");
    		productChinapnrSendLog.setOrddate(loansbean.getOrdDate());
    		productChinapnrSendLog.setMsgType("Loans");
    		productChinapnrSendLog.setClient(productList.getClient());
    		productChinapnrSendLog.setCreateTime(GetDate.getNowTime10());
    		productChinapnrSendLog.setUserId(productList.getUserId());
    		productChinapnrSendLog.setContent("");
    		productChinapnrSendLog.setChkvalue(loansbean.getChkValue());
    		productChinapnrSendLog.setMsgdata(JSON.toJSONString(loansbean.getAllParams()));
    		this.productChinapnrSendLogMapper.insertSelective(productChinapnrSendLog);
        	//放款成功，重复放款
        	if(retBean.getRespCode().equals("000")){
        		isLoan = true;
        	}else{
                //错误日志
                ProductErrorLog productErrorLog = new ProductErrorLog();
                productErrorLog.setAmount(productList.getAmount());
                productErrorLog.setUserId(productList.getUserId());
                productErrorLog.setDate(GetDate.getDate("yyyy-MM-dd HH:mm:ss"));
                productErrorLog.setInvestTime(GetDate.getNowTime10());
                productErrorLog.setOrderId(retBean.getOrdId());
                productErrorLog.setRemark("汇天利放款失败,返回码："+retBean.getRespCode());
                productErrorLog.setIsSms(0);
                this.insertProductErrorLog(productErrorLog);
        	}
		} catch (Exception e1) {
			isLoan = false;
            //错误日志
            ProductErrorLog productErrorLog = new ProductErrorLog();
            productErrorLog.setAmount(productList.getAmount());
            productErrorLog.setUserId(productList.getUserId());
            productErrorLog.setDate(GetDate.getDate("yyyy-MM-dd HH:mm:ss"));
            productErrorLog.setInvestTime(GetDate.getNowTime10());
            productErrorLog.setOrderId(productList.getOrderId());
            productErrorLog.setRemark("汇天利放款失败"+e1.getCause());
            productErrorLog.setIsSms(0);
            this.insertProductErrorLog(productErrorLog);
            e1.printStackTrace();
		}
		//更新日志表
		ProductListLogExample example = new ProductListLogExample();
		ProductListLogExample.Criteria  cr = example.createCriteria();
		cr.andUserIdEqualTo(productList.getUserId());
		cr.andOrderIdEqualTo(productList.getOrderId());
	    List<ProductListLog> list = this.productListLogMapper.selectByExample(example);
	    ProductListLog productListLog = null;
	    if(list != null){
		    productListLog = list.get(0);
		}
		//汇付调用成功并且放款成功
		if(ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE)) && isLoan){
			/**********更新表 productList 出借状态************/
			productList.setStatus(0);
			productList.setInvestStatus(0);
			productList.setBalance(productList.getBalance().add(productList.getAmount()));
			/**********更新表 productListlog日志 出借状态************/
			productListLog.setTenderStatus("成功");
			/**********修改产品信息表 product 当前出借总额字段************/
			Product product = getProduct();
		    product.setTotal(product.getTotal().add(productList.getAmount()));//获得新的统计数据
			this.productMapper.updateByPrimaryKey(product);
			
			/**********获取(出借人)huiyingdai_account************/
	        AccountExample accountExample = new AccountExample();
	        AccountExample.Criteria accountCriteria = accountExample.createCriteria();
	        accountCriteria.andUserIdEqualTo(productList.getUserId());
	        Account account = accountMapper.selectByExample(accountExample).get(0);
			
			/**********插入资产明细表（出借人）huiyingdai_account_list************/
			AccountList accountList = new AccountList();
			accountList.setNid(productList.getOrderId());//订单号  与购买记录一致？
			accountList.setUserId(productList.getUserId());
			accountList.setAmount(productList.getAmount());
			accountList.setType(2);//收支类型1收入2支出3冻结
			accountList.setTrade("tender_huitianli");//交易类型
			accountList.setTradeCode("balance");//操作识别码 balance余额操作 frost冻结操作 await待收操作
			accountList.setTotal(account.getTotal());//资金总额
			accountList.setBalance(account.getBalance().subtract(productList.getAmount()));//可用金额
			accountList.setFrost(account.getFrost());
			accountList.setAwait(account.getAwait().add(productList.getAmount()));//待收金额
			accountList.setRepay(account.getRepay());
			accountList.setRemark("购买汇天利");
			accountList.setCreateTime(GetDate.getNowTime10());
			accountList.setBaseUpdate(0);
			accountList.setIsUpdate(0);
			accountList.setWeb(0);
			this.accountListMapper.insertSelective(accountList);
			
			/**********更新资产表(出借人)huiyingdai_account************/
			account.setBalance(account.getBalance().subtract(productList.getAmount()));//可用金额减少
			account.setAwait(account.getAwait().add(productList.getAmount()));//待收金额增加
			this.accountMapper.updateByPrimaryKeySelective(account);
			
			/**********获取(白海燕)huiyingdai_account************/
	        AccountExample accountbaiExample = new AccountExample();
	        AccountExample.Criteria accountbaiCriteria = accountbaiExample.createCriteria();
	        if(productList.getIsNew() == 1){
	        	accountbaiCriteria.andUserIdEqualTo(ProductConstants.BAI_USERID);
	        }else{
	        	accountbaiCriteria.andUserIdEqualTo(ProductConstants.PUB_USERID);
	        }
	        Account accountbai = accountMapper.selectByExample(accountbaiExample).get(0);
			
			/**********插入资产明细表（白海燕）huiyingdai_account_list************/
			AccountList accountListbai = new AccountList();
			accountListbai.setNid(productList.getOrderId());//订单号  与购买记录一致？
			if(productList.getIsNew() == 1){
				accountListbai.setUserId(ProductConstants.BAI_USERID);
			}else{
				accountListbai.setUserId(ProductConstants.PUB_USERID);
			}
			accountListbai.setAmount(productList.getAmount());
			accountListbai.setType(1);//收支类型1收入2支出3冻结
			accountListbai.setTrade("tender_huitianli");//交易类型
			accountListbai.setTradeCode("balance");//操作识别码 balance余额操作 frost冻结操作 await待收操作
			accountListbai.setTotal(accountbai.getTotal().add(accountListbai.getAmount()));//资金总额
			accountListbai.setBalance(accountbai.getBalance().add(accountListbai.getAmount()));//可用金额
			accountListbai.setFrost(accountbai.getFrost());
			accountListbai.setAwait(accountbai.getAwait());
			accountListbai.setRepay(accountbai.getRepay());
			accountListbai.setRemark("购买汇天利");
			accountListbai.setCreateTime(GetDate.getNowTime10());
			accountListbai.setIsUpdate(0);
			accountListbai.setBaseUpdate(0);
			accountListbai.setWeb(0);
			this.accountListMapper.insertSelective(accountListbai);
			
			/**********更新资产表(白海燕)huiyingdai_account************/
	        accountbai.setBalance(accountbai.getBalance().add(productList.getAmount()));//可用金额增加
	        accountbai.setTotal(accountbai.getTotal().add(productList.getAmount()));//资金总额增加
			this.accountMapper.updateByPrimaryKeySelective(accountbai);

		}else{
			/**********更新表 productList 出借状态************/
			productList.setStatus(1);
			productList.setInvestStatus(2); //付款失败
			/**********更新表 productListlog日志 出借状态************/
			productListLog.setTenderStatus("失败");
		}
		productList.setLoansId(retBean.getOrdId()); //放款id
		productList.setLoansDate(retBean.getOrdDate()); //放款时间
		//更新
		this.productListMapper.updateByPrimaryKey(productList);
		this.productListLogMapper.updateByPrimaryKey(productListLog);

		/**********汇付返回日志表************/
		ProductChinapnrLog productChinapnrLog = new ProductChinapnrLog();
		productChinapnrLog.setIsbg(0);//不知道是啥？
		productChinapnrLog.setUserId(productList.getUserId());
		productChinapnrLog.setOrdid(bean.get(ChinaPnrConstant.PARAM_ORDID));
		productChinapnrLog.setRespCode(bean.get(ChinaPnrConstant.PARAM_RESPCODE));
		productChinapnrLog.setRespDesc(bean.get(ChinaPnrConstant.PARAM_RESPDESC));
		productChinapnrLog.setMsgType(ChinaPnrConstant.CMDID_INITIATIVE_TENDER);
		productChinapnrLog.setTrxid(bean.get(ChinaPnrConstant.PARAM_TRXID));
		productChinapnrLog.setAddtime(GetDate.getDate("yyyyMMddHHmmss"));
		productChinapnrLog.setRemark("汇天利购买返回");
		productChinapnrLog.setMsgdata(JSON.toJSONString(bean.getAllParams()));
		productChinapnrLog.setIp(ip);
		productChinapnrLog.setClient(productList.getClient());
		this.productChinapnrLogMapper.insertSelective(productChinapnrLog);
		
		/**********end************/
	}
	
	/**
	 * 汇天利赎回逻辑（找到购买记录，逐一赎回，每条购买记录记录一个赎回明细）
	 * 1、插入汇天利赎回明细表，huiyingdai_product_redeem_list
	 * 2、插入汇天利赎回表，huiyingdai_product_redeem
	 * 3、调用汇付接口后，插入汇付日志表，huiyingdai_product_chinapnr_send_log   
	 */
	public String insertProductRedeem(AccountChinapnr accountChinapnrReceiver,AccountChinapnr accountChinapnrTender,AccountChinapnr accountChinapnrTenderPub,ProductRedeem productRedeem,String ip) {
		String res = "成功";
		try {
			/**redis 锁 */
//			if(StringUtils.isNotEmpty(RedisUtils.get("htl:"+productRedeem.getUserId()))){
//				res = "重复的订单请求";
//				return res;
//			}else{
//				RedisUtils.set("htl:"+productRedeem.getUserId(),productRedeem.getAmount().toString() ,10);
//			}
			
			boolean reslut = RedisUtils.tranactionSet("htl:"+productRedeem.getUserId() ,10);
			// 如果没有设置成功，说明有请求来设置过
			if(!reslut){
				res = "重复的订单请求";
				return res;
			}
			
			/*******************插入汇天利赎回表huiyingdai_product_redeem*******************/
			productRedeem.setOrderId(GetOrderIdUtils.getHtlOrderId());
			productRedeem.setRedeemTime(GetDate.getNowTime10());
			productRedeem.setInterest(BigDecimal.ZERO);
			productRedeem.setTotal(BigDecimal.ZERO);
			productRedeem.setStatus(1);//默认失败
			//获得用户对象
			Users users = this.usersMapper.selectByPrimaryKey(productRedeem.getUserId());
			if(users != null){
				//获取推荐人信息
				Integer refUserId = users.getReferrer();
				SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
				SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
				spreadsUsersExampleCriteria.andUserIdEqualTo(productRedeem.getUserId());
				List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
				if (sList != null && !sList.isEmpty()) {
					refUserId = sList.get(0).getSpreadsUserid();
				}

				// 获取出借人属性
				UsersInfo userInfo = getUserInfo(productRedeem.getUserId());
				// 用户属性  0=>无主单                           1=>有主单                     2=>线下员工                    3=>线上员工
				Integer attribute = null;
				if (userInfo != null) {
					attribute = userInfo.getAttribute();
				}
				if (attribute != null) {
					// 如果是线上员工或线下员工，推荐人的userId和username不插
					if (attribute == 2 || attribute == 3) {
						// 查找用户部门信息
						EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(productRedeem.getUserId());
						if (employeeCustomize != null) {
							productRedeem.setReferee(0);
							productRedeem.setArea(employeeCustomize.getRegionId());
							productRedeem.setCompany(employeeCustomize.getBranchId());
							productRedeem.setDepartment(employeeCustomize.getDepartmentId());
						}else{
							productRedeem.setReferee(0);
						}
					}else if (attribute == 1) {// 如果是有主单，全插推荐人信息
						// 查找用户推荐人
						EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
						if (employeeCustomize != null) {
							productRedeem.setReferee(users.getReferrer());
							productRedeem.setArea(employeeCustomize.getRegionId());
							productRedeem.setCompany(employeeCustomize.getBranchId());
							productRedeem.setDepartment(employeeCustomize.getDepartmentId());
						}else{
							if(users.getReferrer() != null){
								productRedeem.setReferee(refUserId);
							}
						}
					}else  if(attribute == 0){// 如果是无主单
						productRedeem.setReferee(refUserId);
					}
				}

			}
			if(productRedeem.getArea() == null){
				productRedeem.setArea(0);
			}
			if(productRedeem.getCompany() == null){
				productRedeem.setCompany(0);
			}
			if(productRedeem.getDepartment() == null){
				productRedeem.setDepartment(0);
			}
			if(productRedeem.getReferee() == null){
				productRedeem.setReferee(0);
			}
			//插入赎回主表
			this.productRedeemMapper.insertSelective(productRedeem);
		   
			/*******************插入汇天利赎回明细表，huiyingdai_product_redeem_list*******************/
			//获取未赎回的购买记录
			ProductListExample productListExample = new ProductListExample();
			ProductListExample.Criteria cra = productListExample.createCriteria();
			cra.andStatusEqualTo(0);
			cra.andInvestStatusEqualTo(0);
			cra.andUserIdEqualTo(productRedeem.getUserId());
			List<ProductList> productList = this.productListMapper.selectByExample(productListExample);
			//对公账号赎回金额
			BigDecimal pubAmount = BigDecimal.ZERO;
			//白海燕账号赎回金额
			BigDecimal priAmount = BigDecimal.ZERO;
			//对公账号赎回利息
			BigDecimal pubInterest = BigDecimal.ZERO;
			//白海燕账号赎回利息
			BigDecimal priInterest = BigDecimal.ZERO;
			//赎回金额
			BigDecimal redemAmount = productRedeem.getAmount();
			if(productList.size()>0){
				for(int i = 0;i<productList.size();i++){
					if(redemAmount.compareTo(BigDecimal.ZERO) == 0){
						break;
					}
					ProductList plinfo = productList.get(i);
					
					//查找赎回明细  根据订单号  获取操作金额
					BigDecimal yetAmount = getYetAmount(plinfo.getOrderId());

					//查询该订单已赎回金额 和赎回明细是否相同 相同则订单正常，不同则有问题
					if(plinfo.getRedeemed().compareTo(yetAmount) == 0){
						/**
						 * 剩余金额大于赎回金额  ，只有一条记录
						 * 剩余金额等于赎回金额，只有一条记录，更新购买表status字段
						 * 剩余金额小于赎回金额，多条记录，更新购买表status字段
						 */
						ProductRedeemList productRedeemList = new ProductRedeemList();
						
						if(plinfo.getRestAmount().compareTo(redemAmount) > 0 ){
							productRedeemList.setAmount(redemAmount);//赎回金额
							//更新购买记录
							plinfo.setStatus(0);//订单赎回状态：1 全部赎回 0 未赎回或部分赎回
							plinfo.setRedeemed(plinfo.getRedeemed().add(redemAmount));//赎回金额
							plinfo.setRestAmount(plinfo.getRestAmount().subtract(redemAmount));//剩余金额
							//重新赋值为0 
							redemAmount = BigDecimal.ZERO;
						}else if(plinfo.getRestAmount().compareTo(redemAmount) == 0){
							productRedeemList.setAmount(redemAmount);//赎回金额
							//更新购买记录
							plinfo.setStatus(1);//订单赎回状态：1 全部赎回 0 未赎回或部分赎回
							plinfo.setRedeemed(plinfo.getRedeemed().add(redemAmount));//赎回金额
							plinfo.setRestAmount(plinfo.getRestAmount().subtract(redemAmount));//剩余金额
							//重新赋值为0 
							redemAmount = BigDecimal.ZERO;
						}else{
							productRedeemList.setAmount(plinfo.getRestAmount());//赎回金额
							//重新赋值
							redemAmount = redemAmount.subtract(plinfo.getRestAmount());
							//更新购买记录
							plinfo.setStatus(1);//订单赎回状态：1 全部赎回 0 未赎回或部分赎回
							plinfo.setRedeemed(plinfo.getAmount());//赎回金额
							plinfo.setRestAmount(BigDecimal.ZERO);//剩余金额                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
						}
						//赎回记录共同部分
						productRedeemList.setUserId(productRedeem.getUserId());  // 用户id
						productRedeemList.setListId(productRedeem.getOrderId()); //赎回订单id
						productRedeemList.setOrderId(plinfo.getOrderId());		 //转入订单号
						productRedeemList.setClient(productRedeem.getClient());	 //操作客户端
						productRedeemList.setReferee(productRedeem.getReferee());       //推荐人
						productRedeemList.setRedeemTime(GetDate.getNowTime10()); //赎回时间
						productRedeemList.setInterest(ProductUtils.getInterest(productRedeemList.getAmount(),GetDate.getDateMyTimeInMillis(plinfo.getInvestTime())));//利息,需要一个汇天利利息共同方法
						productRedeemList.setTotal(productRedeemList.getAmount().add(productRedeemList.getInterest())); //赎回总额  本金+利息
						productRedeemList.setArea(0);
						productRedeemList.setCompany(0);
						productRedeemList.setDepartment(0);
						//汇付返回bean
						ChinapnrBean retBean = new ChinapnrBean();
						/**判断 redeemlist 表中 是否有重复赎回 */
						boolean isrepeat = isHaveRedeem(plinfo.getOrderId(), productRedeemList.getAmount());
						if(!isrepeat){
							/********************调用汇付接口 start**************************/
					        // 调用汇付接口(4.3.11. 自动扣款还款)
					        String orderid = GetOrderIdUtils.getHtlOrderId();
					        String orderdate =  GetOrderIdUtils.getOrderDate();
							ChinapnrBean bean = new ChinapnrBean();
							bean.setVersion(ChinaPnrConstant.VERSION_10);// 接口版本号
							bean.setCmdId(ChinaPnrConstant.CMDID_REPAYMENT); // 消息类型(自动还款)
							bean.setOrdId(orderid); // 订单号(必须)
							bean.setOrdDate(orderdate);// 订单时间(必须)格式为yyyyMMdd，例如：20130307
							if(plinfo.getIsNew() == 0){
								bean.setOutCustId(String.valueOf(accountChinapnrTenderPub.getChinapnrUsrcustid()));
							}else{
								bean.setOutCustId(String.valueOf(accountChinapnrTender.getChinapnrUsrcustid()));
							}
							bean.setSubOrdId(plinfo.getOrderId());
							bean.setSubOrdDate(plinfo.getOrderDate());
							bean.setTransAmt(new DecimalFormat("############0.00").format((productRedeemList.getAmount().add(productRedeemList.getInterest())).doubleValue()));// 交易金额 赎回本金+利息(必须)
							bean.setFee("0.00");
							bean.setInCustId(String.valueOf(accountChinapnrReceiver.getChinapnrUsrcustid()));
							bean.setBgRetUrl(ChinapnrUtil.getBgRetUrl());
							
							/**********调用汇付日志表************/
							ProductChinapnrSendLogWithBLOBs productChinapnrSendLog = new ProductChinapnrSendLogWithBLOBs();
							productChinapnrSendLog.setOrdid(orderid);
							productChinapnrSendLog.setRemark("汇天利赎回调用");
							productChinapnrSendLog.setOrddate(orderdate);
							productChinapnrSendLog.setMsgType(ChinaPnrConstant.CMDID_REPAYMENT);
							productChinapnrSendLog.setClient(productRedeem.getClient());
							productChinapnrSendLog.setCreateTime(GetDate.getNowTime10());
							productChinapnrSendLog.setUserId(productRedeem.getUserId());
							productChinapnrSendLog.setContent("");
							productChinapnrSendLog.setChkvalue(bean.getChkValue());
							productChinapnrSendLog.setMsgdata(JSON.toJSONString(bean.getAllParams()));
							this.inserProductChinapnrSendLog(productChinapnrSendLog);
							//调用接口
						    retBean =  ChinapnrUtil.callApiBg(bean);
							/**********汇付返回日志表************/
							ProductChinapnrLog productChinapnrLog = new ProductChinapnrLog();
							productChinapnrLog.setIsbg(0);//不知道是啥？
							productChinapnrLog.setUserId(productRedeem.getUserId());
							productChinapnrLog.setOrdid(orderid);
							productChinapnrLog.setRespCode(retBean.getRespCode());
							productChinapnrLog.setRespDesc(retBean.getRespDesc());
							productChinapnrLog.setMsgType(ChinaPnrConstant.CMDID_REPAYMENT);
							productChinapnrLog.setTrxid(retBean.getTrxId());
							productChinapnrLog.setAddtime(GetDate.getDate("yyyyMMddHHmmss"));
							productChinapnrLog.setRemark("汇天利赎回返回");
							productChinapnrLog.setMsgdata(JSON.toJSONString(retBean.getAllParams()));
							productChinapnrLog.setIp(ip);
							productChinapnrLog.setClient(productRedeem.getClient());
							this.inserProductChinapnrLog(productChinapnrLog);
						}else{
							res = "重复的赎回操作，请联系客服";
							/*******************插入汇天利赎回失败日志表 huiyingdai_product_redeem_fail*******************/
							this.insertRedeemFail(productRedeemList, res, productRedeem.getAmount());
							continue;
						}
						/********************调用汇付接口 end**************************/
						/*****成功******/
						if(retBean.getRespCode().equals("000")){
							productRedeemList.setStatus(0);//赎回成功
							if(plinfo.getIsNew() == 1){
								priAmount = priAmount.add(productRedeemList.getAmount());
								priInterest = priInterest.add(productRedeemList.getInterest());
							}else{
								pubAmount = pubAmount.add(productRedeemList.getAmount());
								pubInterest = pubInterest.add(productRedeemList.getInterest());
							}
							//更新购买记录
							this.productListMapper.updateByPrimaryKeySelective(plinfo);
						}else{
							productRedeemList.setStatus(1);//赎回失败
							/*******************插入汇天利赎回失败日志表 huiyingdai_product_redeem_fail*******************/
							this.insertRedeemFail(productRedeemList, retBean.getRespDesc(), productRedeem.getAmount());
						}
						//插入赎回记录
						this.productRedeemListMapper.insertSelective(productRedeemList);
					}else{
						res = "订单异常，已赎回金额与赎回明细不符,请联系客服";
						continue;
					}
			  }
				//调用
				insertProductRedeemSuccessOrNot(productRedeem.getId(),priAmount,priInterest,pubAmount,pubInterest);
	    	}
		} catch (Exception e) {
			res = "汇天利赎回失败";
            //错误日志
            ProductErrorLog productErrorLog = new ProductErrorLog();
            productErrorLog.setAmount(productRedeem.getAmount());
            productErrorLog.setUserId(productRedeem.getUserId());
            productErrorLog.setDate(GetDate.getDate("yyyy-MM-dd HH:mm:ss"));
            productErrorLog.setInvestTime(GetDate.getNowTime10());
            productErrorLog.setOrderId(productRedeem.getOrderId());
            productErrorLog.setRemark("汇天利赎回失败"+e.getCause());
            productErrorLog.setIsSms(0);
            this.insertProductErrorLog(productErrorLog);
            e.printStackTrace();
	   }
		return res;
	}

	/**
	 * 4、汇付返回日志表 ，huiyingdai_product_chinapnr_log
	 * 5、修改产品信息表 product  当前出借总额字段
	 * 6、插入资产明细表huiyingdai_account_list（两条，本金一条，赎回一条）
	 * 7、更新资产表huiyingdai_account
	 * 8、插入汇天利利息表 ，huiyingdai_product_interest
	 * 9、如果赎回失败，插入汇天利赎回失败日志表 huiyingdai_product_redeem_fail
	 * (公司资管账户与用户相反)
	 * @author Michael
	 */
	public void insertProductRedeemSuccessOrNot(int productRedeemId,BigDecimal priAmount,BigDecimal priInterest,BigDecimal pubAmount,BigDecimal pubInterest) {
		//获取主表信息
		ProductRedeem productRedeem = this.productRedeemMapper.selectByPrimaryKey(productRedeemId);
		/*******************获得明细表统计信息，存入赎回主表*******************/
		ProductRedeemDetailCustomize productRedeemDetailCustomize = new ProductRedeemDetailCustomize();
		productRedeemDetailCustomize.setListId(productRedeem.getOrderId());
		List<ProductRedeemDetailCustomize> dlist = this.productRedeemDetailCustomizeMapper.selectRedeemListSumByListId(productRedeemDetailCustomize);
		if (dlist != null && dlist.size() > 0) {
			productRedeemDetailCustomize = dlist.get(0);
			if (productRedeemDetailCustomize != null) {
				// 更新主表
				productRedeem.setAmount(productRedeemDetailCustomize.getAmount());
				productRedeem.setInterest(productRedeemDetailCustomize.getInterest());
				productRedeem.setTotal(productRedeemDetailCustomize.getTotal());
				if (productRedeem.getAmount().compareTo(BigDecimal.ZERO) > 0) {
					productRedeem.setStatus(0);
				} else {
					productRedeem.setStatus(1);
				}
			} else {
				productRedeem.setStatus(1);
			}
		} else {
			productRedeem.setStatus(1);
		}
		//更新主表
		this.productRedeemMapper.updateByPrimaryKey(productRedeem);
		//更新成功  ，更新表数据
		if(productRedeem.getStatus() == 0){
			//利息大于0
			if(productRedeem.getInterest().compareTo(BigDecimal.ZERO) > 0){
				/*******************插入汇天利利息表 huiyingdai_product_interest*******************/
				ProductInterest productInterest = new ProductInterest();
				productInterest.setUserId(productRedeem.getUserId());
				productInterest.setOrderId(productRedeem.getOrderId());
				productInterest.setAmount(productRedeem.getAmount());
				productInterest.setInterestDays(0);
				productInterest.setInterestRate(ProductConstants.INTEREST_RATE);
				productInterest.setInterest(productRedeem.getInterest());
				productInterest.setInterestTime(GetDate.getNowTime10());
				this.productInterestMapper.insertSelective(productInterest);
			}
			/*******************修改产品信息表 huiyingdai_product当前出借总额字段*******************/
			Product productinfo = getProduct();
			productinfo.setTotal(productinfo.getTotal().subtract(productRedeem.getAmount()));
			this.productMapper.updateByPrimaryKey(productinfo);

			/**********更新资产表(出借人)huiyingdai_account************/
	        AccountExample accountExample = new AccountExample();
	        AccountExample.Criteria accountCriteria = accountExample.createCriteria();
	        accountCriteria.andUserIdEqualTo(productRedeem.getUserId());
	        Account account = accountMapper.selectByExample(accountExample).get(0);
			
			/**********插入资产明细表（出借人）huiyingdai_account_list************/
	        //赎回本金
			AccountList accountList = new AccountList();
			accountList.setNid(productRedeem.getOrderId());
			accountList.setUserId(productRedeem.getUserId());
			accountList.setAmount(productRedeem.getAmount());
			accountList.setType(1);//收支类型1收入2支出3冻结
			accountList.setTrade("redeem_huitianli");//交易类型
			accountList.setTradeCode("balance");//操作识别码 balance余额操作 frost冻结操作 await待收操作
			accountList.setTotal(account.getTotal());//资金总额
			accountList.setBalance(account.getBalance().add(accountList.getAmount()));//可用金额增加
			accountList.setFrost(account.getFrost());
			accountList.setAwait(account.getAwait().subtract(accountList.getAmount()));//待收金额减少
			accountList.setRepay(account.getRepay());
			accountList.setRemark("赎回汇天利");
			accountList.setCreateTime(GetDate.getNowTime10());
			accountList.setInterest(productRedeem.getInterest());
			accountList.setBaseUpdate(0);
			accountList.setIsUpdate(0);
			accountList.setWeb(0);
			this.accountListMapper.insertSelective(accountList);
			
			/**********更新资产表(出借人)huiyingdai_account 本金************/
			account.setBalance(accountList.getBalance());//可用金额增加(本金+利息)
			account.setAwait(accountList.getAwait());//待收金额减少
			this.accountMapper.updateByPrimaryKeySelective(account);
			
			//赎回利息
			if(productRedeem.getInterest().compareTo(BigDecimal.ZERO) > 0){
				AccountList accountListinterest = new AccountList();
				accountListinterest.setNid(productRedeem.getOrderId());
				accountListinterest.setUserId(productRedeem.getUserId());
				accountListinterest.setAmount(productRedeem.getInterest());
				accountListinterest.setType(1);//收支类型1收入2支出3冻结
				accountListinterest.setTrade("interest_huitianli");//交易类型
				accountListinterest.setTradeCode("balance");//操作识别码 balance余额操作 frost冻结操作 await待收操作
				accountListinterest.setTotal(account.getTotal().add(accountListinterest.getAmount()));//资金总额
				accountListinterest.setBalance(account.getBalance().add(accountListinterest.getAmount()));//可用金额
				accountListinterest.setFrost(account.getFrost());
				accountListinterest.setAwait(account.getAwait());
				accountListinterest.setRepay(account.getRepay());
				accountListinterest.setRemark("汇天利利息");
				accountListinterest.setCreateTime(GetDate.getNowTime10());
				accountListinterest.setInterest(productRedeem.getInterest());//利息
				accountListinterest.setBaseUpdate(0);
				accountListinterest.setIsUpdate(0);
				accountListinterest.setWeb(0);
				this.accountListMapper.insertSelective(accountListinterest);
				/**********更新资产表(出借人)huiyingdai_account 利息************/
				account.setBalance(accountListinterest.getBalance());//可用金额增加(利息)
				account.setTotal(accountListinterest.getTotal()); //总金额增加  （利息）
				this.accountMapper.updateByPrimaryKeySelective(account);
			}
			
			//白海燕账户赎回
			if(priAmount.compareTo(BigDecimal.ZERO) > 0){
				/**********更新资产表(白海燕)huiyingdai_account************/
		        AccountExample accountbaiExample = new AccountExample();
		        AccountExample.Criteria accountbaiCriteria = accountbaiExample.createCriteria();
		        accountbaiCriteria.andUserIdEqualTo(ProductConstants.BAI_USERID);
		        Account accountbai = accountMapper.selectByExample(accountbaiExample).get(0);
				
				/**********插入资产明细表（白海燕）huiyingdai_account_list************/
				AccountList accountListbai = new AccountList();
				accountListbai.setNid(productRedeem.getOrderId());//订单号  与购买记录一致？
				accountListbai.setUserId(ProductConstants.BAI_USERID);
				accountListbai.setAmount(priAmount);
				accountListbai.setType(2);//收支类型1收入2支出3冻结
				accountListbai.setTrade("redeem_huitianli");//交易类型
				accountListbai.setTradeCode("balance");//操作识别码 balance余额操作 frost冻结操作 await待收操作
				accountListbai.setTotal(accountbai.getTotal().subtract(accountListbai.getAmount()));//资金总额
				accountListbai.setBalance(accountbai.getBalance().subtract(accountListbai.getAmount()));//可用金额
				accountListbai.setFrost(accountbai.getFrost());
				accountListbai.setAwait(accountbai.getAwait());
				accountListbai.setRepay(accountbai.getRepay());
				accountListbai.setRemark("赎回汇天利");
				accountListbai.setCreateTime(GetDate.getNowTime10());
				accountListbai.setInterest(productRedeem.getInterest());
				accountListbai.setBaseUpdate(0);
				accountListbai.setIsUpdate(0);
				accountListbai.setWeb(0);
				this.accountListMapper.insertSelective(accountListbai);
				
		        /**********更新资产表(白海燕)huiyingdai_account 本金************/
		        accountbai.setBalance(accountListbai.getBalance());//可用金额减少
		        accountbai.setTotal(accountListbai.getTotal());//总金额减少
				this.accountMapper.updateByPrimaryKeySelective(accountbai);
				if(priInterest.compareTo(BigDecimal.ZERO) > 0){
					//利息
					AccountList accountListbaiInterest = new AccountList();
					accountListbaiInterest.setNid(productRedeem.getOrderId());//订单号  与购买记录一致？
					accountListbaiInterest.setUserId(ProductConstants.BAI_USERID);
					accountListbaiInterest.setAmount(priInterest);
					accountListbaiInterest.setType(2);//收支类型1收入2支出3冻结
					accountListbaiInterest.setTrade("interest_huitianli");//交易类型
					accountListbaiInterest.setTradeCode("balance");//操作识别码 balance余额操作 frost冻结操作 await待收操作
					accountListbaiInterest.setTotal(accountbai.getTotal().subtract(accountListbaiInterest.getAmount()));//资金总额
					accountListbaiInterest.setBalance(accountbai.getBalance().subtract(accountListbaiInterest.getAmount()));//可用金额
					accountListbaiInterest.setFrost(accountbai.getFrost());
					accountListbaiInterest.setAwait(accountbai.getAwait());
					accountListbaiInterest.setRepay(accountbai.getRepay());
					accountListbaiInterest.setRemark("汇天利利息");
					accountListbaiInterest.setCreateTime(GetDate.getNowTime10());
					accountListbaiInterest.setInterest(productRedeem.getInterest());
					accountListbaiInterest.setBaseUpdate(0);
					accountListbaiInterest.setIsUpdate(0);
					accountListbaiInterest.setWeb(0);
					this.accountListMapper.insertSelective(accountListbaiInterest);
					
			        /**********更新资产表(白海燕)huiyingdai_account 利息************/
			        accountbai.setBalance(accountListbaiInterest.getBalance());//可用金额减少
			        accountbai.setTotal(accountListbaiInterest.getTotal());//总金额减少
					this.accountMapper.updateByPrimaryKeySelective(accountbai);

					}
				}
			if(pubAmount.compareTo(BigDecimal.ZERO) > 0) {
				/**********更新资产表(对公账户)huiyingdai_account************/
		        AccountExample accountpubExample = new AccountExample();
		        AccountExample.Criteria accountpubCriteria = accountpubExample.createCriteria();
		        accountpubCriteria.andUserIdEqualTo(ProductConstants.PUB_USERID);
		        Account accountpub = accountMapper.selectByExample(accountpubExample).get(0);
				
				/**********插入资产明细表（对公账户）huiyingdai_account_list************/
				AccountList accountListpub = new AccountList();
				accountListpub.setNid(productRedeem.getOrderId());//订单号  与购买记录一致？
				accountListpub.setUserId(ProductConstants.PUB_USERID);
				accountListpub.setAmount(pubAmount);
				accountListpub.setType(2);//收支类型1收入2支出3冻结
				accountListpub.setTrade("redeem_huitianli");//交易类型
				accountListpub.setTradeCode("balance");//操作识别码 balance余额操作 frost冻结操作 await待收操作
				accountListpub.setTotal(accountpub.getTotal().subtract(accountListpub.getAmount()));//资金总额
				accountListpub.setBalance(accountpub.getBalance().subtract(accountListpub.getAmount()));//可用金额
				accountListpub.setFrost(accountpub.getFrost());
				accountListpub.setAwait(accountpub.getAwait());
				accountListpub.setRepay(accountpub.getRepay());
				accountListpub.setRemark("赎回汇天利");
				accountListpub.setCreateTime(GetDate.getNowTime10());
				accountListpub.setInterest(productRedeem.getInterest());
				accountListpub.setBaseUpdate(0);
				accountListpub.setIsUpdate(0);
				accountListpub.setWeb(0);
				this.accountListMapper.insertSelective(accountListpub);
				
		        /**********更新资产表(对公账号)huiyingdai_account 本金************/
				accountpub.setBalance(accountListpub.getBalance());//可用金额减少
				accountpub.setTotal(accountListpub.getTotal());//总金额减少
				this.accountMapper.updateByPrimaryKeySelective(accountpub);
				//有利息
				if(pubInterest.compareTo(BigDecimal.ZERO)>0){
					//利息
					AccountList accountListpubInterest = new AccountList();
					accountListpubInterest.setNid(productRedeem.getOrderId());//订单号  与购买记录一致？
					accountListpubInterest.setUserId(ProductConstants.PUB_USERID);
					accountListpubInterest.setAmount(pubInterest);
					accountListpubInterest.setType(2);//收支类型1收入2支出3冻结
					accountListpubInterest.setTrade("interest_huitianli");//交易类型
					accountListpubInterest.setTradeCode("balance");//操作识别码 balance余额操作 frost冻结操作 await待收操作
					accountListpubInterest.setTotal(accountpub.getTotal().subtract(accountListpubInterest.getAmount()));//资金总额
					accountListpubInterest.setBalance(accountpub.getBalance().subtract(accountListpubInterest.getAmount()));//可用金额
					accountListpubInterest.setFrost(accountpub.getFrost());
					accountListpubInterest.setAwait(accountpub.getAwait());
					accountListpubInterest.setRepay(accountpub.getRepay());
					accountListpubInterest.setRemark("汇天利利息");
					accountListpubInterest.setCreateTime(GetDate.getNowTime10());
					accountListpubInterest.setInterest(productRedeem.getInterest());
					accountListpubInterest.setBaseUpdate(0);
					accountListpubInterest.setIsUpdate(0);
					accountListpubInterest.setWeb(0);
					this.accountListMapper.insertSelective(accountListpubInterest);
					
			        /**********更新资产表(对公账号)huiyingdai_account利息************/
					accountpub.setBalance(accountListpubInterest.getBalance());//可用金额减少
					accountpub.setTotal(accountListpubInterest.getTotal());//总金额减少
					this.accountMapper.updateByPrimaryKeySelective(accountpub);
				}

			}
		}
		
	}
	
	
	/**
	 * 获取汇天利转入记录
	 * @param productList
	 * @return
	 * @author Michael
	 */
	public List<ProductSearchForPage> getProductList(ProductSearchForPage  productSearchForPage) {
		return this.htlCommonCustomizeMapper.selectBuyRecordPage( productSearchForPage);
	}

	/**
	 * 获取汇天利转出记录
	 * @param productRedeem
	 * @return
	 * @author Michael
	 */
	public List<ProductSearchForPage> getProductRedeem(ProductSearchForPage productSearchForPage) {
		return this.htlCommonCustomizeMapper.selectRedeemRecordPage(productSearchForPage);
	}

	/**
	 * 获取汇天利收益记录
	 * @param productInterest
	 * @return
	 * @author Michael
	 */
	public  List<ProductSearchForPage> getProductInterestRecords(ProductSearchForPage productSearchForPage) {
		return this.htlCommonCustomizeMapper.selectInterestRecordPage(productSearchForPage);
	}

	/**
	 * 获得汇天利产品信息
	 * @return
	 */
	public Product getProduct(){
		Product product = new Product();
		List<Product> productinfoList = this.productMapper.selectByExample(new ProductExample());
		if(productinfoList.size()>0){
			product = productinfoList.get(0);
		}
		return product;
	}


	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param productChinapnrSendLogWithBLOBs
	 * @author Michael
	 */
		
	public void inserProductChinapnrSendLog(ProductChinapnrSendLogWithBLOBs productChinapnrSendLogWithBLOBs) {
		if(productChinapnrSendLogWithBLOBs != null){
			this.productChinapnrSendLogMapper.insertSelective(productChinapnrSendLogWithBLOBs);
		}
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param productChinapnrLog
	 * @author Michael
	 */
		
	public void inserProductChinapnrLog(ProductChinapnrLog productChinapnrLog) {
		if(productChinapnrLog != null){
			this.productChinapnrLogMapper.insertSelective(productChinapnrLog);
		}
	}

	/**
	 * 获取出借人本金信息
	 * @param productSearchForPage
	 * @return
	 * @author Michael
	 */
		
	public ProductSearchForPage selectUserPrincipal(ProductSearchForPage productSearchForPage) {
		productSearchForPage = this.htlCommonCustomizeMapper.selectUserPrincipal(productSearchForPage);
		return productSearchForPage;
			
	}

	/**
	 *	获取已提取收益
	 * @param productSearchForPage
	 * @return
	 * @author Michael
	 */
	public ProductSearchForPage selectUserAlreadyInterest(ProductSearchForPage productSearchForPage) {
		productSearchForPage = this.htlCommonCustomizeMapper.selectUserAlreadyInterest(productSearchForPage);
		return productSearchForPage;
	}

	/**
	 * 获取汇天利未赎回记录
	 * @param productSearchForPage
	 * @return
	 * @author Michael
	 */
	public List<ProductSearchForPage> selectUserNotRedeemRecord(ProductSearchForPage productSearchForPage) {
		// TODO Auto-generated method stub
		return this.htlCommonCustomizeMapper.selectUserNotRedeemRecord(productSearchForPage);
			
	}
	
	/**
	 * 获取用户信息
	 * @param userId
	 * @return
	 * @author b
	 */
	public UsersInfo getUserInfo(Integer userId) {
		UsersInfoExample usersInfoExample = new UsersInfoExample();
		UsersInfoExample.Criteria usersInfoExampleCriteria = usersInfoExample.createCriteria();
		usersInfoExampleCriteria.andUserIdEqualTo(userId);
		List<UsersInfo> userInfoList = usersInfoMapper.selectByExample(usersInfoExample);
		UsersInfo usersInfo = null;
		if (userInfoList != null && !userInfoList.isEmpty()) {
			usersInfo = userInfoList.get(0);
		}
		return usersInfo;
	}

	/**
	 * 汇天利错误日志
	 * @param productErrorLog
	 * @author Michael
	 */
	public void insertProductErrorLog(ProductErrorLog productErrorLog) {
		if(productErrorLog != null){
			this.productErrorLogMapper.insertSelective(productErrorLog);
			//发送短信到异常处理者
			try {
				SmsMessage smsMessage = new SmsMessage(null, null, null, null, MessageDefine.SMSSENDFORMANAGER, "【汇盈金服】", CustomConstants.PARAM_TPL_HTLYICHANG, CustomConstants.CHANNEL_TYPE_NORMAL);
        		smsProcesser.gather(smsMessage);
			} catch (Exception e) {
				LogUtil.debugLog(this.getClass().toString(), "insertProductErrorLog", e.getMessage());
			}
		}
	}
	

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param productSearchForPage
	 * @return
	 * @author Michael
	 */
	public Integer countRedeemRecordPage(ProductSearchForPage productSearchForPage) {
		return this.htlCommonCustomizeMapper.countRedeemRecordPage(productSearchForPage);
			
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param productSearchForPage
	 * @return
	 * @author Michael
	 */
	public Integer countInterestRecordPage(ProductSearchForPage productSearchForPage) {
		return this.htlCommonCustomizeMapper.countInterestRecordPage(productSearchForPage);
			
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param productSearchForPage
	 * @return
	 * @author Michael
	 */
	public Integer countBuyRecordPage(ProductSearchForPage productSearchForPage) {
		return this.htlCommonCustomizeMapper.countBuyRecordPage(productSearchForPage);
			
	}

	/**
	 * 参数校验
	 * @param userId
	 * @param transAmt
	 * @param flag
	 * @return
	 * @author Michael
	 */
	public Map<String, String> check(Integer userId, String transAmt, int flag) {
		Map<String, String> map = new HashMap<String, String>();
    	Product product = this.getProduct();
    	ProductSearchForPage productSearchForPage = new ProductSearchForPage();
    	productSearchForPage.setUserId(userId);
    	productSearchForPage = this.selectUserPrincipal(productSearchForPage);
    	
        // 取得用户当前余额
        Account account = this.getAccount(userId);
    	// 检查用户是否登录
        if (Validator.isNull(userId)) {
			map.put("error", "1");
			map.put("data", "您没有登录，请登录后再进行操作");
			return map;
        }
        // 检查参数(交易金额是否数字)
        if (Validator.isNull(transAmt) || !NumberUtils.isNumber(transAmt)) {
			map.put("error", "1");
			map.put("data", "操作金额不可为空");
			return map;
        }
        // 检查参数(交易金额是否大于0)
        BigDecimal amount = new BigDecimal(transAmt);
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
			map.put("error", "1");
			map.put("data", "操作金额不能为负数");
			return map;
        }
        //是否为整数
        if(!ProductUtils.isIntegerValue(amount)){
			map.put("error", "1");
			map.put("data", "请输入整数位金额");
			return map;
        }
        //购买校验
        if(flag == 1){
        	/*查找转入记录里是否有当前时间的记录*/
        	ProductListExample example = new ProductListExample();
        	ProductListExample.Criteria cri = example.createCriteria();
        	cri.andUserIdEqualTo(userId);
        	example.setOrderByClause("invest_time desc");
        	List<ProductList> plist = this.productListMapper.selectByExample(example);
        	if(plist != null && plist.size() > 0){
        		ProductList pinfo = plist.get(0);
        		if((GetDate.getNowTime10() - pinfo.getInvestTime()) < 30){
        			map.put("error", "1");
        			map.put("data", "请30秒之后再出借");
        			return map;
        		}
        	}
        	
        	//不可转入
        	if(product.getIsTender() == 1){
    			map.put("error", "1");
    			map.put("data", product.getErrorRemark());
    			return map;
        	}
            //起投下限
            if(amount.compareTo(product.getPlower()) < 0){
    			map.put("error", "1");
    			map.put("data", product.getPlower()+"元起投");
    			return map;
            }
            //获取出借人信息
            if(productSearchForPage != null){
                //出借人上线
                if((productSearchForPage.getPrincipal().add(amount)).compareTo(product.getPupper()) > 0){
        			map.put("error", "1");
        			map.put("data", "出借人上限为"+product.getPupper()+"元");
        			return map;
                }
            }else{
                //出借人上线
                if(amount.compareTo(product.getPupper()) > 0){
        			map.put("error", "1");
        			map.put("data", "出借人上限为"+product.getPupper()+"元");
        			return map;
                }
            }
            //是否达到标的上线
            if((amount.add(product.getTotal())).compareTo(product.getAllpupper()) > 0){
    			map.put("error", "1");
    			map.put("data", "汇天利出借额已满");
    			return map;
            }
            AccountChinapnr accountChinapnrTender = this.getAccountChinapnr(Integer.valueOf(userId));
    		if (accountChinapnrTender == null) {
    			map.put("error", "1");
    			map.put("data","请先开通汇付天下账号");
    			return map;
    		}
            // 投标金额大于可用余额
            if (account == null || amount.compareTo(account.getBalance()) > 0) {
    			map.put("error", "1");
    			map.put("data","出借金额大于可用余额");
    			return map;
            }

            
        }else{
        	//不可赎回
        	if(product.getIsRedeem() == 1){
    			map.put("error", "1");
    			map.put("data","汇付天下通道不稳定正在紧急修复请耐心等待");
    			return map;
        	}
        	//赎回失败,本次还款金额加上已还款金额超过还款总额
        	ProductRedeemFailExample fexample = new ProductRedeemFailExample();
        	ProductRedeemFailExample.Criteria fcri = fexample.createCriteria();
        	fcri.andUserIdEqualTo(userId);
        	fcri.andRemarkLike("赎回失败,本次还款金额加上已还款金额超过还款总额");
        	List<ProductRedeemFail> flist = this.productRedeemFailMapper.selectByExample(fexample);
        	if(flist != null && flist.size() > 0){
        			map.put("error", "1");
        			map.put("data", "订单异常，为保障您的资金安全请联系客服");
        			return map;
        	}
        	
        	/*查找转出记录里是否有当前时间的记录*/
        	ProductRedeemListExample rexample = new ProductRedeemListExample();
        	ProductRedeemListExample.Criteria rcri = rexample.createCriteria();
        	rcri.andUserIdEqualTo(userId);
        	rexample.setOrderByClause("redeem_time desc");
        	List<ProductRedeemList> rlist = this.productRedeemListMapper.selectByExample(rexample);
        	if(rlist != null && rlist.size() > 0){
        		ProductRedeemList pinfo = rlist.get(0);
        		if((GetDate.getNowTime10() - pinfo.getRedeemTime()) < 30){
        			map.put("error", "1");
        			map.put("data", "请30秒之后再赎回");
        			return map;
        		}
        	}
    		if(productSearchForPage.getPrincipal().compareTo(amount) < 0){
    			map.put("error", "1");
    			map.put("data", "赎回金额大于可用本金");
    			return map;
    		}
        	
        }
        return map;
			
	}
	/**
	 * 判断30秒内是否有重复赎回
	 * @param orderid
	 * @param transAmt
	 * @return
	 */
	public boolean isHaveRedeem(String orderid,BigDecimal transAmt){
		boolean flag = false;
    	//判断是否有重复订单
    	ProductRedeemListExample rexample = new ProductRedeemListExample();
    	ProductRedeemListExample.Criteria rcri = rexample.createCriteria();
    	rcri.andOrderIdEqualTo(orderid);
    	//rcri.andStatusEqualTo(0);
    	rcri.andAmountEqualTo(transAmt);
    	rcri.andRedeemTimeBetween(GetDate.getNowTime10()-30, GetDate.getNowTime10());
    	List<ProductRedeemList> rlist = this.productRedeemListMapper.selectByExample(rexample);
    	if(rlist != null && rlist.size() > 0){
    		flag = true;
    	}
    	return flag;
	}
	
	/**
	 * 查找赎回明细  根据订单号  获取操作金额
	 * @return
	 */
	public BigDecimal getYetAmount(String orderid){
		BigDecimal yetAmount = BigDecimal.ZERO;
    	ProductRedeemListExample rexample = new ProductRedeemListExample();
    	ProductRedeemListExample.Criteria rcri = rexample.createCriteria();
    	rcri.andOrderIdEqualTo(orderid);
    	rcri.andStatusEqualTo(0);
    	List<ProductRedeemList> rlist = this.productRedeemListMapper.selectByExample(rexample);
    	if(rlist != null && rlist.size() > 0){
    		for(int j = 0;j<rlist.size();j++){
    			yetAmount = yetAmount.add(rlist.get(j).getAmount());
    		}
    	}
    	return yetAmount;
	}
	/**
	 * 插入赎回失败日志
	 * @param productRedeemList
	 * @param remark
	 */
	public void insertRedeemFail(ProductRedeemList productRedeemList, String remark,BigDecimal amountAll){
		/*******************插入汇天利赎回失败日志表 huiyingdai_product_redeem_fail*******************/
		ProductRedeemFail productRedeemFail = new ProductRedeemFail();
		productRedeemFail.setUserId(productRedeemList.getUserId());
		productRedeemFail.setListId(productRedeemList.getListId());
		productRedeemFail.setOrderId(productRedeemList.getOrderId());
		productRedeemFail.setAmount(productRedeemList.getAmount());
		productRedeemFail.setRedeemTime(productRedeemList.getRedeemTime());
		productRedeemFail.setInterest(productRedeemList.getInterest());
		productRedeemFail.setTotal(productRedeemList.getTotal());
		productRedeemFail.setReferee(productRedeemList.getReferee());
		productRedeemFail.setRemark("赎回失败,"+remark);
		productRedeemFail.setAmountAll(amountAll);
		productRedeemFail.setArea(0);
		productRedeemFail.setCompany(0);
		productRedeemFail.setDepartment(0);
		this.productRedeemFailMapper.insertSelective(productRedeemFail);
	}
	
	
	/**
	 * 获取赎回时利息
	 * @param userid
	 * @param amount
	 * @return
	 */
	public BigDecimal getRedeemInterest(Integer userid , BigDecimal amount){
		/*******************插入汇天利赎回明细表，huiyingdai_product_redeem_list*******************/
		//获取未赎回的购买记录
		ProductListExample productListExample = new ProductListExample();
		ProductListExample.Criteria cra = productListExample.createCriteria();
		cra.andStatusEqualTo(0);
		cra.andInvestStatusEqualTo(0);
		cra.andUserIdEqualTo(userid);
		List<ProductList> productList = this.productListMapper.selectByExample(productListExample);
		//赎回金额
		BigDecimal redemAmount = amount;
		BigDecimal userInterest =  BigDecimal.ZERO;
		if(productList.size()>0){
			for(int i = 0;i<productList.size();i++){
				if(redemAmount.compareTo(BigDecimal.ZERO) == 0){
					break;
				}
				ProductList plinfo = productList.get(i);
				BigDecimal oneAmount = BigDecimal.ZERO;
					/**
					 * 剩余金额大于赎回金额  ，只有一条记录
					 * 剩余金额等于赎回金额，只有一条记录，更新购买表status字段
					 * 剩余金额小于赎回金额，多条记录，更新购买表status字段
					 */
					if(plinfo.getRestAmount().compareTo(redemAmount) > 0 ){
						oneAmount = redemAmount;
						//重新赋值为0 
						redemAmount = BigDecimal.ZERO;
					}else if(plinfo.getRestAmount().compareTo(redemAmount) == 0){
						oneAmount = redemAmount;
						//重新赋值为0 
						redemAmount = BigDecimal.ZERO;
					}else{
						//重新赋值
						redemAmount = redemAmount.subtract(plinfo.getRestAmount());
						oneAmount = plinfo.getRestAmount();
					}
					//利息
					userInterest = 	userInterest.add(ProductUtils.getInterest(oneAmount,GetDate.getDateMyTimeInMillis(plinfo.getInvestTime()))) ;
		  }
	}
	return userInterest;
 }	
	
	
	
}
