package com.hyjf.batch.htl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Product;
import com.hyjf.mybatis.model.auto.ProductExample;
import com.hyjf.mybatis.model.auto.ProductInfo;
import com.hyjf.mybatis.model.customize.ProductInfoCustomize;

@Service
public class HtlCommonServiceImpl extends BaseServiceImpl implements HtlCommonService {
	 	@Autowired
	    @Qualifier("smsProcesser")
	    private MessageProcesser smsProcesser;  
	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * 
	 * @return
	 * @author Michael
	 */
	public ProductInfo getCreateProductInfo(ProductInfoCustomize productInfoCustomize) {
		ProductInfoBean pinfo = new ProductInfoBean();
		ProductInfo p1 = htlCommonCustomizeMapper.getCreateProductInfo_productlist(productInfoCustomize);
		ProductInfo p2 = htlCommonCustomizeMapper.getCreateProductInfo_productredeem(productInfoCustomize);
		ProductInfo p3 = htlCommonCustomizeMapper.getCreateProductInfo_productinterest(productInfoCustomize);
		ProductInfo p5 = htlCommonCustomizeMapper.getCreateProductInfo_restamount(productInfoCustomize);
		productInfoCustomize.setUserId(Integer.parseInt(getProduct().getPnumber()));
		ProductInfo p4 = htlCommonCustomizeMapper.getCreateProductInfo_account(productInfoCustomize);
		productInfoCustomize.setUserId(Integer.parseInt(getProduct().getPnumberNew()));
		ProductInfo p6 = htlCommonCustomizeMapper.getCreateProductInfo_account(productInfoCustomize);
		if (p1 != null) {
			if (p1.getInAmount() != null) {
				pinfo.setInAmount(p1.getInAmount());
			} else {
				pinfo.setInAmount(BigDecimal.ZERO);
			}
			if (p1.getInCount() != null) {
				pinfo.setInCount(p1.getInCount());
			} else {
				pinfo.setInCount(0);
			}
		} else {
			pinfo.setInAmount(BigDecimal.ZERO);
			pinfo.setInCount(0);
		}
		if (p2 != null) {
			if (p2.getOutAmount() != null) {
				pinfo.setOutAmount(p2.getOutAmount());
			} else {
				pinfo.setOutAmount(BigDecimal.ZERO);
			}
			if (p2.getOutCount() != null) {
				pinfo.setOutCount(p2.getOutCount());
			} else {
				pinfo.setOutCount(0);
			}
		} else {
			pinfo.setOutAmount(BigDecimal.ZERO);
			pinfo.setOutCount(0);
		}
		if (p3 != null) {
			if (p3.getOutInterest() != null) {
				pinfo.setOutInterest(p3.getOutInterest());
			} else {
				pinfo.setOutInterest(BigDecimal.ZERO);
			}
		} else {
			pinfo.setOutInterest(BigDecimal.ZERO);
		}
		BigDecimal loanAmount = BigDecimal.ZERO;
		if (p4 != null) {
			if (p4.getLoanBalance() != null) {
				loanAmount = p4.getLoanBalance();
				// 白海燕账户小于100万 发送短信提醒
				if (loanAmount.compareTo(new BigDecimal(1000000)) <= 0) {
					try {
					    SmsMessage smsMessage =
	                              new SmsMessage(null, null, null, null, MessageDefine.SMSSENDFORMANAGER, null,
	                              		CustomConstants.PARAM_TPL_ZJYEXYYBW, CustomConstants.CHANNEL_TYPE_NORMAL);
					    smsProcesser.gather(smsMessage);
					} catch (Exception e) {
						LogUtil.debugLog(this.getClass().toString(), "getCreateProductInfo", e.getMessage());
//						e.printStackTrace();
					}
				}
			}
		}
		if (p6 != null && p6.getLoanBalance() != null) {
			loanAmount = loanAmount.add(p6.getLoanBalance());
		}
		pinfo.setLoanBalance(loanAmount);
		if (p5 != null) {
			if (p5.getInvestAmount() != null) {
				pinfo.setInvestAmount(p5.getInvestAmount());
			} else {
				pinfo.setInvestAmount(BigDecimal.ZERO);
			}
		} else {
			pinfo.setInvestAmount(BigDecimal.ZERO);
		}
		return pinfo;

	}

	/**
	 * 获得汇天利产品信息
	 * 
	 * @return
	 */
	public Product getProduct() {
		Product product = new Product();
		List<Product> productinfoList = this.productMapper.selectByExample(new ProductExample());
		if (productinfoList.size() > 0) {
			product = productinfoList.get(0);
		}
		return product;
	}

}
