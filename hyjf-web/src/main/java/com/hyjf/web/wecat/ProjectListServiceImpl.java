/**
 * Description:汇直投查询service实现
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.wecat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.mybatis.model.customize.wecat.WecatProjectListCustomize;
import com.hyjf.web.BaseServiceImpl;

@Service
public class ProjectListServiceImpl extends BaseServiceImpl implements ProjectListService {

	@Override
	public List<WecatProjectListCustomize> searchProjectList(ProjectListBean hzt, int limitStart, int limitEnd, String status) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (limitStart == 0 || limitStart > 0) {
			params.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			params.put("limitEnd", limitEnd);
		}
    		if (status!=null) {
    		    params.put("borrow_status", status);
            }
		
		  if (hzt.getType() != null && hzt.getType().equals("XSH")) {
              params.put("projectType", "HZT");
              params.put("type", "4");
          }else if (hzt.getType() != null && hzt.getType().equals("ZXH")) {
              params.put("projectType", "HZT");
              params.put("type", "11");
          }else if (hzt.getType() != null && hzt.getType().equals("RTB")) {
              params.put("projectType", "HZT");
              params.put("type", "13");
          }else if(hzt.getType() !=null && !hzt.getType().equals("")){
        	  params.put("projectType", hzt.getType()); 
          }
		List<WecatProjectListCustomize> list = wecatProjectListCustomizeMapper.selectProjectList(params);
		return list;
	}

	@Override
	public int countProjectListRecordTotal(ProjectListBean hzt, String status) {
		  Map<String, Object> params = new HashMap<String, Object>();
		
        	 if (status!=null) {
                    params.put("borrow_status", status);
                }
        	  if (hzt.getType()!=null&&hzt.getType().equals("XSH")) {
                  params.put("projectType", "HZT");
                  params.put("type", "4");
              }else if (hzt.getType()!=null&&hzt.getType().equals("ZXH")) {
                  params.put("projectType", "HZT");
                  params.put("type", "11");
              }else if (hzt.getType() != null && hzt.getType().equals("RTB")) {
                  params.put("projectType", "HZT");
                  params.put("type", "13");
              }else if(hzt.getType()!=null&&!hzt.getType().equals("")){
            	  params.put("projectType", hzt.getType()); 
              }
		int total = wecatProjectListCustomizeMapper.countProjectListRecordTotal(params);
		return total;
	}

}
