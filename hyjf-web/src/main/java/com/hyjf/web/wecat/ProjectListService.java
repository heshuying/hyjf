/**
 * Description:汇直投查询service接口
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.wecat;

import java.util.List;

import com.hyjf.mybatis.model.customize.wecat.WecatProjectListCustomize;
import com.hyjf.web.BaseService;

public interface ProjectListService extends BaseService {

	/**
	 * 查询汇直投相应的列表
	 * 
	 * @param hzt
	 * @param i
	 * @param pageSize
	 * @param status 
	 * @return
	 */
	List<WecatProjectListCustomize> searchProjectList(ProjectListBean hzt, int i, int pageSize, String status);

	/**
	 * 统计汇直投列表数据总数
	 * 
	 * @param form
	 * @param status 
	 * @return
	 */
	int countProjectListRecordTotal(ProjectListBean form, String status);

}
