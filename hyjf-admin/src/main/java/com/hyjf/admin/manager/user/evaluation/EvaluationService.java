/**
 * Description:用户信息管理业务处理类接口
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 逄成超
 * @version: 1.0
 * Created at: 2015年11月11日 上午11:05:26
 * Modification History:
 * Modified by : 
 */

package com.hyjf.admin.manager.user.evaluation;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminUserEvalationCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminUserEvalationResultCustomize;

/**
 * @author 逄成超
 */

public interface EvaluationService extends BaseService {

    int countRecordTotal(Map<String, Object> user);

    List<AdminUserEvalationResultCustomize> getRecordList(Map<String, Object> user,  int limitStart, int limitEnd);

    UserEvalationResultCustomize selectUserEvalationResultByUserId(int userId);

    List<AdminUserEvalationCustomize> getUserEvalation(Integer id);

    List<AdminUserEvalationResultCustomize> exoportRecordList(Map<String, Object> user);

	

}
