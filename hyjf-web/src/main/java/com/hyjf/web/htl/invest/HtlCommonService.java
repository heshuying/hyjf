package com.hyjf.web.htl.invest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.Product;
import com.hyjf.mybatis.model.auto.ProductChinapnrLog;
import com.hyjf.mybatis.model.auto.ProductChinapnrSendLogWithBLOBs;
import com.hyjf.mybatis.model.auto.ProductErrorLog;
import com.hyjf.mybatis.model.auto.ProductList;
import com.hyjf.mybatis.model.auto.ProductRedeem;
import com.hyjf.mybatis.model.customize.ProductSearchForPage;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.web.BaseService;

public interface HtlCommonService extends BaseService {

      
	/**
	 * 汇天利购买逻辑(调用汇付接口之前操作)
	 * 1、插入汇天利购买日志表，huiyingdai_product_list_log
	 * 2、插入汇天利购买明细表，huiyingdai_product_list
	 * 3、调用汇付接口后，插入汇付日志表，huiyingdai_product_chinapnr_send_log   
	 */
    public int insertBuyProduct(ChinapnrBean bean,ProductList productList);
    
	/**
	 * 汇天利购买逻辑(调用汇付接口之后操作) 
	 * 4、汇付返回日志表 ，huiyingdai_product_chinapnr_log
	 * 5、修改产品信息表 product  当前出借总额字段
	 * 6、插入资产明细表huiyingdai_account_list
	 * 7、更新资产表huiyingdai_account
	 * (公司资管账户与用户相反)
	 */
    public void insertBuyProductReturn(ChinapnrBean bean,int productListId,String ip);
    
	/**
	 * 汇天利赎回逻辑（找到购买记录，逐一赎回，每条购买记录记录一个赎回明细）
	 * 1、插入汇天利赎回明细表，huiyingdai_product_redeem_list
	 * 2、插入汇天利赎回表，huiyingdai_product_redeem
	 * 3、调用汇付接口后，插入汇付日志表，huiyingdai_product_chinapnr_send_log   
	 * param  收款人、付款人、赎回信息
	 */
	public String insertProductRedeem(AccountChinapnr accountChinapnrReceiver,AccountChinapnr accountChinapnrTender,AccountChinapnr accountChinapnrTenderPub,ProductRedeem productRedeem,String ip);

	
	/**
	 * 汇天利赎回逻辑（找到购买记录，逐一赎回，每条购买记录记录一个赎回明细）
	 * 4、汇付返回日志表 ，huiyingdai_product_chinapnr_log
	 * 5、修改产品信息表 product  当前出借总额字段
	 * 6、插入资产明细表huiyingdai_account_list（两条，本金一条，赎回一条）
	 * 7、更新资产表huiyingdai_account
	 * 8、插入汇天利利息表 ，huiyingdai_product_interest
	 * 9、如果赎回失败，插入汇天利赎回失败日志表 huiyingdai_product_redeem_fail
	 * (公司资管账户与用户相反)
	 */
//	public void insertProductRedeemReturn(ChinapnrBean bean, int productRedeemId,String ip);
	/**
	 * 获取汇天利转入记录
	 * @return
	 */
	public  List<ProductSearchForPage> getProductList(ProductSearchForPage productSearchForPage);
	/**
	 * 获取汇天利转出记录
	 * @param productRedeem
	 * @return
	 */
	public  List<ProductSearchForPage> getProductRedeem(ProductSearchForPage productSearchForPage);
	/**
	 * 获取汇天利收入明细
	 * @param ProductInterest
	 * @return
	 */
	public  List<ProductSearchForPage> getProductInterestRecords(ProductSearchForPage productSearchForPage);

	/**
	 * 获取汇天利产品信息
	 * @return
	 */
	public Product getProduct();
	
	/**
	 * 插入调用汇付日志
	 */
	public void inserProductChinapnrSendLog(ProductChinapnrSendLogWithBLOBs productChinapnrSendLogWithBLOBs);
	
	/**
	 * 插入调用汇付返回日志
	 */
	public void inserProductChinapnrLog(ProductChinapnrLog productChinapnrLog);
	/**
	 * 获取出借人本金信息
	 */
	public ProductSearchForPage  selectUserPrincipal(ProductSearchForPage productSearchForPage);
	/**
	 * 获取出借人已提取收益
	 */
	public ProductSearchForPage  selectUserAlreadyInterest(ProductSearchForPage productSearchForPage);
	/**
	 * 获取汇天利未赎回记录
	 * @param ProductInterest
	 * @return
	 */
	public  List<ProductSearchForPage> selectUserNotRedeemRecord(ProductSearchForPage productSearchForPage);

	/**
	 * 汇天利错误日志
	 * @param productErrorLog
	 */
	public void insertProductErrorLog(ProductErrorLog productErrorLog);
	
	
	/**
	 * 获取赎回记录数
	 * @param productSearchForPage
	 * @return
	 */
	public Integer countRedeemRecordPage(ProductSearchForPage productSearchForPage);
	/**
	 * 获取利息记录数
	 * @param productSearchForPage
	 * @return
	 */
	public Integer countInterestRecordPage(ProductSearchForPage productSearchForPage);
	/**
	 * 获取购买记录数
	 * @param productSearchForPage
	 * @return
	 */
	public Integer countBuyRecordPage(ProductSearchForPage productSearchForPage);
	
	/**
	 * 校验参数
	 * @param userId  用户id
	 * @param transAmt 交易金额
	 * @param flag 标示  
	 * @return
	 */
	public Map<String, String> check(Integer userId, String transAmt,int flag);
	
	/**
	 * 获取用户赎回 可得利息
	 * @param userid
	 * @param amount
	 * @return
	 */
	public BigDecimal getRedeemInterest(Integer userid , BigDecimal amount);
}
