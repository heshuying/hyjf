package com.hyjf.api.aems.tradelist;

import com.hyjf.api.aems.common.AemsCommonSvrChkService;
import com.hyjf.api.server.common.CommonSvrChkService;
import com.hyjf.api.server.tradelist.TradeListBean;
import com.hyjf.api.server.tradelist.TradeListService;
import com.hyjf.api.web.BaseController;
import com.hyjf.common.enums.utils.MsgEnum;
import com.hyjf.common.result.ResultApiBean;
import com.hyjf.common.validator.CheckUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.customize.AccountDetailCustomize;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author jijun
 */
@RestController
@RequestMapping(value = AemsTradeListDefine.REQUEST_MAPPING)
public class AemsTradeListServer extends BaseController {
	
	@Autowired
	private AemsTradeListService tradeListService;
	@Autowired
	private AemsCommonSvrChkService commonSvrChkService;
	
	/**
	 * 获取用户交易明细
	 * @param form
	 * @return
	 */
    @RequestMapping(method = RequestMethod.POST,value = AemsTradeListDefine.TRADELIST_ACTION)
    public ResultApiBean<List<AccountDetailCustomize>> srchTradeList(@RequestBody AemsTradeListBean bean){
    	/**必传为空校验 start*/
    	// 常规参数验证
    	CheckUtil.check(Validator.isNotNull(bean.getTimestamp()), MsgEnum.STATUS_CE000001);
		CheckUtil.check(Validator.isNotNull(bean.getInstCode()), MsgEnum.STATUS_CE000001);
		CheckUtil.check(Validator.isNotNull(bean.getChkValue()), MsgEnum.STATUS_CE000001);
		// 自定义参数验证(暂时)
		CheckUtil.check(Validator.isNotNull(bean.getAccountId()), MsgEnum.STATUS_CE000001);
		CheckUtil.check(Validator.isNotNull(bean.getPhone()), MsgEnum.STATUS_CE000001);
		CheckUtil.check(Validator.isNotNull(bean.getStartDate()), MsgEnum.STATUS_CE000001);
		CheckUtil.check(Validator.isNotNull(bean.getEndDate()), MsgEnum.STATUS_CE000001);
		/**必传为空校验 end*/
		
		/**传入参数逻辑校验 start*/
		// 传入时间格式校验:yyyy-MM-dd
		CheckUtil.check(isValidDate(bean.getStartDate()), MsgEnum.STATUS_CE000008);
		CheckUtil.check(isValidDate(bean.getEndDate()), MsgEnum.STATUS_CE000008);
		CheckUtil.check(compareDate(bean.getStartDate(),bean.getEndDate()), MsgEnum.STATUS_CE000009);//日期比較
		CheckUtil.check(Validator.isMobile(bean.getPhone()), MsgEnum.STATUS_CE000010);//手機號格式
		CheckUtil.check(tradeListService.existPhone(bean.getPhone()), MsgEnum.STATUS_CE000011);//手機號不存在
		CheckUtil.check(tradeListService.existAccountId(bean.getPhone(),bean.getAccountId()), MsgEnum.STATUS_CE000012);//用戶電子賬號不存在
		commonSvrChkService.checkLimit(bean.getLimitStart(), bean.getLimitEnd());
		if(StringUtils.isNotEmpty(bean.getTradeStatus())){
			CheckUtil.check(isTradeStatus(bean.getTradeStatus().trim()), MsgEnum.ERR_OBJECT_UNMATCH, "交易状态");
		}
		if(StringUtils.isNotEmpty(bean.getTypeSearch())){
			CheckUtil.check(isTypeSearch(bean.getTypeSearch().trim()), MsgEnum.ERR_OBJECT_UNMATCH, "收支类型");
		}
		if(StringUtils.isNotEmpty(bean.getTradeTypeSearch())){
			CheckUtil.check(isGetTradeTypeSearch(bean.getTradeTypeSearch().trim()), MsgEnum.ERR_OBJECT_UNMATCH, "交易类型ID");
		}
		/**传入参数逻辑校验 end*/
		// 验签(Timestamp + InstCode + Phone + AccountId)
		CheckUtil.check(this.AEMSVerifyRequestSign(bean, AemsTradeListDefine.REQUEST_MAPPING+AemsTradeListDefine.TRADELIST_ACTION),MsgEnum.ERR_SIGN);
    	// 返回查询结果
    	return new ResultApiBean<List<AccountDetailCustomize>>(tradeListService.searchTradeList(bean));
    }
    
    /** 
     * 判断时间格式 格式必须为“yyyy-MM-dd” 
     * 2004-2-30 是无效的 
     * 2003-2-29 是无效的 
     * @param sDate 
     * @return 
     */  
    public boolean isValidDate(String str) {  
        //String str = "2017-10-01";  
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
        try{  
            Date date = (Date)formatter.parse(str);   
            return str.equals(formatter.format(date));  
        }catch(Exception e){  
            return false;  
        }
    }
    
    /** 
     * 判断时间先后
     * @return 
     */ 
    public boolean compareDate(String startDate, String endDate) {
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try{
        	Date dt1 = df.parse(startDate);
        	Date dt2 = df.parse(endDate);
        	if (dt1.getTime() > dt2.getTime()) {
                System.out.println("开始日期大于结束日期");
                return false;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("开始日期小于结束日期");
                return true;
            } else {
            	return true;
            }  
        }catch(Exception e){  
            return false;  
        } 
    }
    
    /** 
     * 判断交易状态
     * @return 
     */  
    public boolean isTradeStatus(String str) { 
    	if(StringUtils.isNotEmpty(str)){
    		if(str.equals("0") || str.equals("1") || str.equals("2")){
    			return true;
    		} else {
    			return false;
    		}
    	}
    	return true;
    }
    
    /** 
     * 判断收支类型
     * @return 
     */  
    public boolean isTypeSearch(String str) { 
    	if(StringUtils.isNotEmpty(str)){
    		if(str.equals("1") || str.equals("2") || str.equals("3")|| str.equals("4")){
    			return true;
    		} else {
    			return false;
    		}
    	}
    	return true;
    }
    
    /** 
     * 交易类型ID
     * @return 
     */  
    public boolean isGetTradeTypeSearch(String str) { 
    	if(StringUtils.isNotEmpty(str)){
    		if(str.equals("6") || str.equals("228") || str.equals("233")|| str.equals("237")
    				|| str.equals("297")|| str.equals("723")|| str.equals("724")|| str.equals("727")
    				|| str.equals("763")|| str.equals("485")|| str.equals("706")|| str.equals("757")){
    			return true;
    		} else {
    			return false;
    		}
    	}
    	return true;
    }

}
