package com.hyjf.api.server.group;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.server.borrow.borrowlist.BorrowListService;
import com.hyjf.api.web.BaseController;
import com.hyjf.common.enums.utils.MsgEnum;
import com.hyjf.common.result.ResultApiBean;
import com.hyjf.common.validator.CheckUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.customize.web.Empinfo;
import com.hyjf.mybatis.model.customize.web.OrganizationStructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lm
 * @version OrganizationStructureServer, v0.1 2018/1/17 9:31 第三方查询出借信息
 */

@RestController
@RequestMapping(value = OrganizationStructureDefine.REQUEST_MAPPING)
public class OrganizationStructureServer extends BaseController {

	private Logger logger = LoggerFactory.getLogger(OrganizationStructureServer.class);
	
	@Autowired
	private BorrowListService investableBorrowService;
	
	/**
	 * 获取集团组织架构信息
	 * @return
	 */
	@RequestMapping(value = OrganizationStructureDefine.ORGANIZATION_LIST)
	public ResultApiBean<List<OrganizationStructure>> organizationList(@RequestBody OrganizationStructureBean bean) {
		CheckUtil.check(Validator.isNotNull(bean.getInstCode()), MsgEnum.STATUS_CE000001);

		logger.info("bean:{}", JSONObject.toJSONString(bean));

		// 验签
		CheckUtil.check(this.verifyRequestSign(bean, OrganizationStructureDefine.ORGANIZATION_LIST), MsgEnum.ERR_SIGN);

		// 返回查询结果
		return new ResultApiBean<List<OrganizationStructure>>(investableBorrowService.searchOrganizationList(bean));
	}
	
	/**
	 * 获取员工信息
	 * @return
	 */
	@RequestMapping(value = OrganizationStructureDefine.EMPINFO_LIST)
	public ResultApiBean<List<Empinfo>> empinfoList(@RequestBody OrganizationStructureBean bean) {
		// 验证
		CheckUtil.check(Validator.isNotNull(bean.getInstCode()), MsgEnum.STATUS_CE000001);
		
		logger.info("bean:{}", JSONObject.toJSONString(bean));
		
		// 验签
		CheckUtil.check(this.verifyRequestSign(bean, OrganizationStructureDefine.EMPINFO_LIST), MsgEnum.ERR_SIGN);
		
		// 返回查询结果
		return new ResultApiBean<List<Empinfo>>(investableBorrowService.searchEmpInfoList(bean));
	}
}
