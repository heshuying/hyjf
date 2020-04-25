package com.hyjf.admin.manager.config.authconfig;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.mybatis.model.auto.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

/**
 * 授权配置service实现类
 */
@Service
public class AuthConfigServiceImpl extends BaseServiceImpl implements AuthConfigService {


	@Override
	public List<HjhUserAuthConfigCustomize> getAuthConfigList(int limitStart, int limitEnd) {

		List<HjhUserAuthConfigCustomize> list = hjhUserAuthConfigCustomizeMapper.selectCustomizeAuthConfigList(limitStart,limitEnd);
		return list;
	}

	@Override
	public HjhUserAuthConfig getAuthConfigById(Integer id) {
		return hjhUserAuthConfigMapper.selectByPrimaryKey(id);
	}


	@Override
	public int getAuthConfigCount() {
		HjhUserAuthConfigExample example = new HjhUserAuthConfigExample();
		int count=hjhUserAuthConfigMapper.countByExample(example);
		return count;
	}


	@Override
	public int updateRecord(HjhUserAuthConfig form, HttpServletRequest request) {
		int updateResult = hjhUserAuthConfigMapper.updateByPrimaryKeySelective(form);
		int updateResult1;
		if(updateResult==0){
			throw new RuntimeException("更新授权配置表失败!");
		}else{
			HjhUserAuthConfigLog log = new HjhUserAuthConfigLog();
			log.setAuthConfigId(form.getId());
			log.setAuthType(form.getAuthType());
			log.setPersonalMaxAmount(form.getPersonalMaxAmount());
			log.setEnterpriseMaxAmount(form.getEnterpriseMaxAmount());
			log.setAuthPeriod(form.getAuthPeriod());
			log.setEnabledStatus(form.getEnabledStatus());
			log.setRemark(form.getRemark());
			log.setIp(GetCilentIP.getIpAddr(request));
			log.setCreateUser(form.getUpdateUser());
			log.setCreateTime(form.getUpdateTime());
			updateResult1=hjhUserAuthConfigLogMapper.insertSelective(log);
		}
		if (updateResult1==0){
			throw new RuntimeException("新增授权配置操作记录失败!");
		}
		return updateResult1;
	}


	@Override
	public int getAuthConfigLogCount() {
		HjhUserAuthConfigLogExample example = new HjhUserAuthConfigLogExample();
		int count=hjhUserAuthConfigLogMapper.countByExample(example);
		return count;
	}


	@Override
	public List<HjhUserAuthConfigLogCustomize> getAuthConfigLogList(int limitStart, int limitEnd) {
		List<HjhUserAuthConfigLogCustomize> list=hjhUserAuthConfigLogMapper.selectCustomizeAuthConfigLogList(limitStart,limitEnd);
		return list;
	}
}
