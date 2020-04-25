package com.hyjf.mybatis.mapper.customize;

import com.hyjf.mybatis.model.auto.HjhPlanCapital;
import com.hyjf.mybatis.model.auto.HjhPlanCapitalExample;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author liubin
 */
public interface HjhPlanCapitalCustomizeMapper {


    /**
     * 获取该日期的实际债转和复投金额
     * @param date
     * @return
     */
    List<HjhPlanCapital> selectPlanCapitalForActList(Date date);

    /**
     * 获取该期间的预估债转和复投金额
     * @param fromDate
     * @param toDate
     * @return
     */
    List<HjhPlanCapital> selectPlanCapitalForProformaList(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    /**
     * 获取总计
     * @param example
     * @return
     */
    Map<String, Object> selectSumRecord(HjhPlanCapitalExample example);


    /**
     * 删除一个期间的数据
     * @param dateFrom
     * @param dateTo
     * @return
     */
    int deleteForDates(@Param("dateFrom")Date dateFrom, @Param("dateTo")Date dateTo);
}