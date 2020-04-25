package com.hyjf.wechat.api.common;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.common.validator.CheckUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BindUsers;
import com.hyjf.mybatis.model.auto.BindUsersExample;
import com.hyjf.wechat.api.base.ApiBaseServiceImpl;
import com.hyjf.wechat.api.bind.ApiUserPostBean;
import com.hyjf.wechat.api.fclc.FclcPostBean;

/**
 * @author limeng
 */

@Service("ApiCommonService")
public class ApiCommonServiceImpl extends ApiBaseServiceImpl implements ApiCommonService{

	@Override
	public void checkFclcPostBean(FclcPostBean bean) {
		//传入信息验证
        CheckUtil.check(Validator.isNotNull(bean.getFrom()), "Object.required", "from");
        CheckUtil.check(Validator.isNotNull(bean.getTicket()), "Object.required", "ticket");
        CheckUtil.check(Validator.isNotNull(bean.getTarget_url()), "Object.required", "target_url");
	}

	@Override
	public Integer getUserIdByBind(String bindUserId, Integer bindPlatFromId,String userId) {
				//检索条件
				BindUsersExample example = new BindUsersExample();
				BindUsersExample.Criteria cra = example.createCriteria();
				cra.andBindUniqueIdEqualTo(bindUserId+"");
				cra.andBindPlatformIdEqualTo(bindPlatFromId);
				cra.andUserIdEqualTo(Integer.parseInt(userId));
				cra.andDelFlgEqualTo(0);//未删除
				//检索
				List<BindUsers> list = bindUsersMapper.selectByExample(example);
				//无记录时，未绑定汇盈金服
				CheckUtil.check(list != null && list.size() > 0, "user.unbound");
				return list.get(0).getUserId();
	}

	@Override
	public void checkPostBeanOfWeb(ApiUserPostBean apiUserPostBean) {
		//传入信息验证
        CheckUtil.check(Validator.isNotNull(apiUserPostBean.getFrom()), "Object.required", "from");
        CheckUtil.check(Validator.isNotNull(apiUserPostBean.getWrb_user_id()), "Object.required", "wrb_user_id");
        CheckUtil.check(Validator.isNotNull(apiUserPostBean.getMobile()), "Object.required", "mobile");		
	}


}
