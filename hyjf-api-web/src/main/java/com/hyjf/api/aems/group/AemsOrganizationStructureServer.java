package com.hyjf.api.aems.group;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.aems.borrowlist.AemsBorrowListService;
import com.hyjf.api.web.BaseController;
import com.hyjf.common.enums.utils.MsgEnum;
import com.hyjf.common.result.ResultApiBean;
import com.hyjf.common.validator.CheckUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.customize.web.OrganizationStructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/***
* @author Zha Daojian
* @date 2018/9/13 10:21
* @param 
* @return
**/

@RestController
@RequestMapping(value = AemsOrganizationStructureDefine.REQUEST_MAPPING)
public class AemsOrganizationStructureServer extends BaseController {

	private Logger logger = LoggerFactory.getLogger(AemsOrganizationStructureServer.class);
	
	@Autowired
	private AemsBorrowListService investableBorrowService;
	
	/**
	 * 获取集团组织架构信息
	 * @return
	 */
	@RequestMapping(value = AemsOrganizationStructureDefine.ORGANIZATION_LIST)
	public ResultApiBean<List<OrganizationStructure>> organizationList(@RequestBody AemsOrganizationStructureBean bean) {
		CheckUtil.check(Validator.isNotNull(bean.getInstCode()), MsgEnum.STATUS_CE000001);

		logger.info("bean:{}", JSONObject.toJSONString(bean));

		// 验签
		CheckUtil.check(this.AEMSVerifyRequestSign(bean, AemsOrganizationStructureDefine.REQUEST_MAPPING+AemsOrganizationStructureDefine.ORGANIZATION_LIST), MsgEnum.ERR_SIGN);

		// 返回查询结果
		return new ResultApiBean<List<OrganizationStructure>>(investableBorrowService.searchOrganizationList(bean));
	}

}
