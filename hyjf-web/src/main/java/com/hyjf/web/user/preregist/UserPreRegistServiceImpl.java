package com.hyjf.web.user.preregist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.PreRegist;
import com.hyjf.mybatis.model.auto.PreRegistExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.Utm;
import com.hyjf.mybatis.model.auto.UtmExample;
import com.hyjf.web.BaseServiceImpl;

@Service
public class UserPreRegistServiceImpl extends BaseServiceImpl implements UserPreRegistService {

	/**
	 * 根据推荐人手机号或userId判断推荐人是否存在
	 */
	@Override
	public Map<String, Object> savePreregist(UserPreRegistBean userPreRegistBean) {
	    Map<String, Object> resultMap = new HashMap<String, Object>();
		UsersExample example = new UsersExample();
		UsersExample.Criteria criteria = example.createCriteria();
		criteria.andMobileEqualTo(userPreRegistBean.getMobile());
		List<Users> usersList = usersMapper.selectByExample(example);
		
		PreRegistExample preRegistExample = new PreRegistExample();
		PreRegistExample.Criteria preRegistCriteria = preRegistExample.createCriteria();
		preRegistCriteria.andMobileEqualTo(userPreRegistBean.getMobile());
        List<PreRegist> preRegistList = preRegistMapper.selectByExample(preRegistExample);
        //校验推荐人
        if(!StringUtils.isEmpty(userPreRegistBean.getFrom())){
            UsersExample usersexample = new UsersExample();
            UsersExample.Criteria cra = usersexample.createCriteria();
            cra.andUserIdEqualTo(Integer.parseInt(userPreRegistBean.getFrom()));
            List<Users> usersFromList = usersMapper.selectByExample(usersexample);
            if(usersFromList!=null && usersFromList.size()>0){
            }else{
                resultMap.put("success", false);
                resultMap.put("msg", "推荐人不存在");
                return resultMap;
            }
        }
        //校验关键字ID
        Utm utm = null;
        if(!StringUtils.isEmpty(userPreRegistBean.getUtmId())){
            UtmExample utmexample = new UtmExample();
            UtmExample.Criteria utmCra = utmexample.createCriteria();
            utmCra.andUtmIdEqualTo(Integer.parseInt(userPreRegistBean.getUtmId()));
            List<Utm> utmList = utmMapper.selectByExample(utmexample);
            if(utmList!=null && utmList.size()>0){
                utm = utmList.get(0);
            }else{
                resultMap.put("success", false);
                resultMap.put("msg", "渠道关键字不存在");
                return resultMap;
            }
        }
        //判断是否已经注册
		if(usersList!=null && usersList.size()>0){
		    resultMap.put("success", true);
		    resultMap.put("msg", "该手机号已经注册成功了,不能预注册");
		    return resultMap;
		}else if(preRegistList!=null && preRegistList.size()>0){//判断是否已经提交预注册
            for(PreRegist preRegist : preRegistList){
                preRegist.setReferrer(userPreRegistBean.getFrom());
                preRegist.setUtmId(Integer.parseInt(userPreRegistBean.getUtmId()));
                //根据关键词ID(utmId)判获取渠道ID
                if(utm != null){
                    //渠道的ID
                    preRegist.setSourceId(utm.getSourceId()!=null?utm.getSourceId():null);
                }
                preRegist.setPlatformName(userPreRegistBean.getPlatform());
                preRegist.setPreRegistTime(GetDate.getNowTime10());
                preRegistMapper.updateByPrimaryKeySelective(preRegist);
            }
            resultMap.put("success", true);
            resultMap.put("msg", "该手机号预注册提交成功");
            return resultMap;
		}else{//预注册提交保存
		    PreRegist preRegist = new PreRegist();
		    preRegist.setMobile(userPreRegistBean.getMobile());
		    preRegist.setReferrer(userPreRegistBean.getFrom());
		    //根据关键词ID(utmId)判获取渠道ID
            if(utm != null){
                //渠道的ID
                preRegist.setUtmId(utm.getUtmId());
                preRegist.setSourceId(utm.getSourceId()!=null?utm.getSourceId():null);
            }
		    preRegist.setPreRegistTime(GetDate.getNowTime10());
		    preRegist.setRegistFlag(0);
		    preRegist.setPlatformName(userPreRegistBean.getPlatform());
		    preRegist.setRemark(null);
		    preRegist.setCreateTime(GetDate.getNowTime10());
		    preRegistMapper.insertSelective(preRegist);
		    resultMap.put("success", true);
            resultMap.put("msg", "该手机号预注册提交成功");
		}
		return resultMap;
	}
}
