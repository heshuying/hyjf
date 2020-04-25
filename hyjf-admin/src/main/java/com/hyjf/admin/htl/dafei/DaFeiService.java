/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: Michael
 * @version: 1.0
 * Created at: 2015年12月7日 下午6:37:46
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.admin.htl.dafei;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael
 */

public class DaFeiService {
	
	
	/**
	 * 获取合同信息
	 * param:searchDate  yyyy-MM-dd
	 * @throws Exception 
	 */
	public List<ConsumeInfo> getConsumeInfo(String searchDate) throws Exception{
		List<ConsumeInfo>  consumeList = new ArrayList<ConsumeInfo>();
		consumeList = DaFeiHttpRequest.getConsumeList(searchDate);
		return consumeList;
	}
	
	/**
	 * 拒绝合同
	 * 
	 *  000000 成功
	 * 	999999 失败
	 *  100001 数据校验失败
	 * 	100002 超出抽查率
	 * 	100003 参数不匹配
	 * 	100005 找不到对应的接口
	 * @return  返回code 码
	 * @throws Exception 
	 */
	public String refuseContract(List<RefuseInfo> refuseList) throws Exception{
		String msg = "";
		msg = DaFeiHttpRequest.refuseContract(refuseList);
		return msg;
	}
}

	