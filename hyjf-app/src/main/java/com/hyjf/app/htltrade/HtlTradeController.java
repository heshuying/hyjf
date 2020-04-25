package com.hyjf.app.htltrade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.customize.ProductIntoRecordCustomize;
import com.hyjf.mybatis.model.customize.ProductRedeemCustomize;

@Controller
@RequestMapping(value = HtlTradeDefine.REQUEST_MAPPING)
public class HtlTradeController extends BaseController {
	/** 类名 */
	public static final String THIS_CLASS = HtlTradeController.class.getName();
	@Autowired
	public HtlTradeService htlTradeService;

	
	/**
	 * 获取我的账单
	 * @param request
	 * @param response
	 * @return
	 */
    @ResponseBody
    @RequestMapping(HtlTradeDefine.HTLTRADE_ACTION)
    public JSONObject getHtlList(HttpServletRequest request, HttpServletResponse response) {
    	LogUtil.startLog(THIS_CLASS, HtlTradeDefine.HTLTRADE_ACTION);
    	JSONObject ret = new JSONObject();
    	ret.put("request", HtlTradeDefine.RETURN_REQUEST);
        // 版本号
        String version = request.getParameter("version");
        // 网络状态
        String netStatus = request.getParameter("netStatus");
        // 平台
        String platform = request.getParameter("platform");
        // 唯一标识
        String sign = request.getParameter("sign");
        //账单类型：1全部，2购入，3赎回
        String tradeType= request.getParameter("tradeType");

        // 检查参数正确性
        if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform) || Validator.isNull(tradeType) ||  Validator.isNull(sign)) {
            ret.put("status", "1");
            ret.put("statusDesc", "请求参数非法");
            return ret;
        }

//        // 判断sign是否存在
//        boolean isSignExists = SecretUtil.isExists(sign);
//        if (!isSignExists) {
//            ret.put("status", "1");
//            ret.put("statusDesc", "请求参数非法");
//            return ret;
//        }

        // 取得加密用的Key
        String key = SecretUtil.getKey(sign);
        if (Validator.isNull(key)) {
            ret.put("status", "1");
            ret.put("statusDesc", "请求参数非法");
            return ret;
        }

        // 取得用户iD
        Integer userId = SecretUtil.getUserId(sign);

		List<ProductIntoRecordCustomize> tenders;//购入
		int tenderSize = 0;
		List<ProductRedeemCustomize> redeems;//赎回
		int redeemSize = 0;
		List<HtlTradeBean> htlList= new ArrayList<HtlTradeBean>();//汇总
		int count=0;
		
        //汇天利购买记录
		if ("1".equals(tradeType) || "2".equals(tradeType)) {
			ProductIntoRecordCustomize productList = new ProductIntoRecordCustomize();
			int page = Integer.valueOf(request.getParameter("page"));
			int pageSize = Integer.valueOf(request.getParameter("pageSize"));
			try {
				productList.setUserId(userId);
				productList.setLimitStart(pageSize * (page - 1));
				productList.setLimitEnd(pageSize);
				productList.setInvestStatus(0);//0表示成功状态
				tenderSize = htlTradeService.countHtlIntoRecord(productList);
				count+=tenderSize;
				tenders = htlTradeService.getIntoRecordList(productList);
				
				for(ProductIntoRecordCustomize tender: tenders){
					HtlTradeBean htltradebean= new HtlTradeBean();
//					htltradebean.setAmount(tender.getAmount());
					htltradebean.setAmount(CustomConstants.DF_FOR_VIEW.format(tender.getAmount()));
					htltradebean.setTradeType("2");
					htltradebean.setCreateTime(tender.getInvestTime());
					htlList.add(htltradebean);
				}
			} catch (Exception e) {
				ret.put("status", "1");
				ret.put("statusDesc", "获取购买列表失败");
				return ret;
			}
		}
		//汇天利赎回记录
		if ("1".equals(tradeType) || "3".equals(tradeType)) {
			ProductRedeemCustomize rList = new ProductRedeemCustomize();
			int page = Integer.valueOf(request.getParameter("page"));
			int pageSize = Integer.valueOf(request.getParameter("pageSize"));
			try {
				rList.setUserId(userId);
				rList.setLimitStart(pageSize * (page - 1));
				rList.setLimitEnd(pageSize);
				rList.setStatus(0);//成功状态
				redeemSize = htlTradeService.countProductRedeemRecord(rList);
				count+=redeemSize;
				redeems = htlTradeService.getRedeemRecordList(rList);

				for(ProductRedeemCustomize redeem: redeems){
					HtlTradeBean htltradebean= new HtlTradeBean();
					htltradebean.setAmount(CustomConstants.DF_FOR_VIEW.format(redeem.getAmount()));
					htltradebean.setTradeType("3");
					htltradebean.setCreateTime(redeem.getRedeemTime());
					htlList.add(htltradebean);
				}
			} catch (Exception e) {
				ret.put("status", "1");
				ret.put("statusDesc", "获取赎回列表失败");
				return ret;
			}
		}
		//按时间排序
		Collections.sort(htlList, new Comparator<HtlTradeBean>(){
			@Override
			public int compare(HtlTradeBean o1, HtlTradeBean o2) {
				return o2.getCreateTime().compareTo(o1.getCreateTime());
			}
		});

		ret.put("status", "0");
		ret.put("statusDesc", "成功");
		ret.put("count", count+"");
		ret.put("htlList", htlList);
		
		return ret;
    }
	
}




