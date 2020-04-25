
package com.hyjf.web.platdata;

import java.util.List;
import java.util.Map;


public interface PlatDataService {
    /**
     * 
     * @param day 
     * @method: selectDataInfo
     * @description: 查询平台数据         
     */
    Map<String, Object> selectDataInfo(String day);
    /**
     * 
     * @method: selectAllDataCenterInfo
     * @description: 分组查询平台出借数据			
     * @return: List<Map<String,Object>>
     */
    List<Map<String, Object>> selectDataTenderList();
    /**
     * 
     * @method: selectDataCreditList
     * @description: 分组查询平台债权出借数据	
     * @return 
     * @return: List<Map<String,Object>>
     */
    List<Map<String, Object>> selectDataCreditList();
    /**
     * 
     * @method: selectPeriodInfo
     * @description: 融资期限分布     
     * @return: Map<String,Object>
     */
    Map<String, Object> selectPeriodInfo();
    /**
     * 
     * @method: selectTendMoneyInfo
     * @description: 出借金额占比		
     * @return: Map<String,Object>
     */
    Map<String, Object> selectTendMoneyInfo();
    /**
     * 
     * @method: selectData
     * @description: 搜索出借统计相关信息			
     * @return: Map<String,Object>
     */
	Map<String, Object> selectData();
	/**
	 * 
	 * @method: selectTenderListMap
	 * @description: 	搜索月出借金额		
	 * @param type 0出借  1债转出借
	 * @return: List<Map<String,Object>>
	 */
	List<Map<String, Object>> selectTenderListMap(int type);

}
