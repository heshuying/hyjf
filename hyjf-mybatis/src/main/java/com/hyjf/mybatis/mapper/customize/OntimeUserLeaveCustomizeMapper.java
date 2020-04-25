package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.UserUpdateParamCustomize;

/**
 * 定时 员工离职
 * @author HBZ
 */
public interface OntimeUserLeaveCustomizeMapper {

    /**
     * 查询符合条件的离职员工 列表
     * @param attribute
     * @return
     */
    public List<Users> queryEmployeeList();

    /**
     * 客户变员工后，其所推荐客户变为‘有主单’
     * @param referrer
     * @return
     */
    public int updateSpreadAttribute(@Param("referrer") Integer referrer);

    /**
     * 根据关联关系查询OA表的内容,得到部门的线上线下属性
     * @return
     */
    public List<UserUpdateParamCustomize> queryUserAndDepartment(@Param("userId") Integer userId);

    /**
     * 查询符合条件的离职员工
     * @param attribute
     * @return
     */
    public List<Users> queryEmployeeById(@Param("userId") String userId);
}
