/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: Michael
 * @version: 1.0
 * Created at: 2015年12月4日 上午9:26:28
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.batch.htl;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.ProductInfo;
import com.hyjf.mybatis.model.customize.ProductInfoCustomize;
/**
 * @author Michael
 * 定时器，定时插入produc_info 表数据，每天一条数据（在29分、59分执行一次）
 */
public class TaskJobProduct { 
	
	@Autowired
	private HtlCommonService htlCommonService;
	@Autowired
	private ProductInfoService productInfoService;

	public void insertProductTask(){
		System.out.println("----------------汇天利每日报表任务-------------"+GetDate.date2Str(GetDate.datetimeFormat));
		     ProductInfoCustomize productInfoCustomize = new ProductInfoCustomize();
		     ProductInfo productInfo = new ProductInfo();
		     productInfo.setDataDate(GetDate.date2Str(GetDate.date_sdf));
        	 productInfoCustomize.setTimeStart(GetDate.strYYYYMMDDHHMMSS2Timestamp(GetDate.getDayStart(productInfo.getDataDate())));
        	 productInfoCustomize.setTimeEnd(GetDate.strYYYYMMDDHHMMSS2Timestamp(GetDate.getDayEnd(productInfo.getDataDate())));
        	 //是否存在当前天的记录，存在则更新，不存在则插入
        	 int pid = productInfoService.isExistsRecordByDataDate(productInfo);
             if(pid != 0){
            	 productInfo = htlCommonService.getCreateProductInfo(productInfoCustomize);
            	 productInfo.setId(pid);
            	 productInfoService.updateRecord(productInfo);
             }else{
            	 productInfo = htlCommonService.getCreateProductInfo(productInfoCustomize);
            	 productInfo.setCreateTime(GetDate.getNowTime10());
            	 productInfo.setDataMonth(GetDate.getMonthDay());
            	 productInfo.setDataDate(GetDate.date2Str(GetDate.date_sdf));
            	 productInfoService.insertRecord(productInfo);
             }

	}
	


}  	