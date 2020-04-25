package com.hyjf.mybatis.mapper.customize;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.FinserChargeCustomize;

public interface FinserChargeCustomizeMapper {
	 /**
	  * 查询所有服务费率
	  * @return
	  */
     public List<FinserChargeCustomize> selectAll(Map<String, Object> param);
     /**
      * 查询总个数
      * @return
      */
     public int countRecordTotal();
}
