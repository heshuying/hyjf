package com.hyjf.bank.service.user.register;

import java.util.Map;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.common.http.HttpDeal;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;

/**
 * 用户注册Service实现类
 * 
 * @author xiaojohn
 *
 */
@Service
public class SyncIpServiceImpl extends BaseServiceImpl implements SyncIpService {

	private Logger _log = LoggerFactory.getLogger(SyncIpServiceImpl.class);
	
	@Override
	public UsersInfo getIpInfo(Users users) {
		
		UsersInfo userInfo = new UsersInfo();
		
		int result = getAddress(users.getRegIp(),userInfo);
		// 获取成功后更新
		if(result ==1 ) {
			userInfo.setUserId(users.getUserId());
		}
		
		return userInfo;
	}

	@Override
	public int updateIpInfo(Users users, UsersInfo usersInfo) {
		int result = 0;
		if(usersInfo.getUserId() != null) {
			_log.info(usersInfo.getUserId()+" 更新ip信息");
			

	        UsersInfoExample example = new UsersInfoExample();
	        UsersInfoExample.Criteria criteria = example.createCriteria();
	        criteria.andUserIdEqualTo(usersInfo.getUserId());
			
	        return this.usersInfoMapper.updateByExampleSelective(usersInfo, example);
		}
		
		
	   return result;
	}

	
    /**
     * 
     * 根据ip地址获取注册所在地
     * @author hsy
     * @param ip
     * @param usersInfo
     */
    public int getAddress(String ip, UsersInfo usersInfo){
        if(StringUtils.isEmpty(ip)){
            return 0;
        }
        
        try {
//            String ipInfoUrl = PropUtils.getSystem("hyjf.ip.taobo.url");
        	String ipInfoUrl = "http://ip.taobao.com/service/getIpInfo.php";
            
//            System.out.println("根据ip获取注册地请求url：" + ipInfoUrl + "?ip=" + ip);
            String result = "";
            try {
            	result = HttpDeal.getIpWIthUserAgent(ipInfoUrl + "?ip=" + ip);
			} catch (Exception e) {
				e.printStackTrace();
			}
           
//            System.out.println("根据ip获取注册地返回结果：" + result);
            
            JSONObject resultObj = (JSONObject)JSONObject.parse(result);
            
            String resultCode = (resultObj!=null)?resultObj.getString("code"):null;
            
            if(resultCode != null && resultCode.equals("0")){ // 查询成功
                String region = resultObj.getJSONObject("data").getString("region");
                String city = resultObj.getJSONObject("data").getString("city");
                String county = resultObj.getJSONObject("data").getString("county");
                
                usersInfo.setProvince(StringUtils.isEmpty(region) ? "" : region);
                usersInfo.setCity(StringUtils.isEmpty(city) ? "" : city);
                usersInfo.setArea(StringUtils.isEmpty(county) ? "" : county);
                return 1;
            }else {
            	_log.info("根据ip地址获取所在地失败，ip：" + ip + " 返回信息：" + result);
            }
        } catch (Exception e) {
        	_log.error(e.getMessage());
        	e.printStackTrace();
        }
        
        return 0;
    }
	
    
    public static void main(String[] args) {
		
//    	UsersInfo info = new UsersInfo();
////    	getAddress("219.147.28.242", info);
//    	JSONObject resultObj = (JSONObject)JSONObject.parse("");
//        
//        String resultCode = (resultObj!=null)?resultObj.getString("code"):null;
//    	
//        System.out.println(resultCode);
//    	
	}
	
}
