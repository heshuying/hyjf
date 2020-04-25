package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.MsgPushCommonCustomize;

public interface MsgPushCommonCustomizeMapper {

	/**
	 * 通过手机号获取设备标识码
	 * 
	 * @param mobile
	 * @return
	 */
	MsgPushCommonCustomize getMobileCodeByNumber(String mobile);

	/**
	 * 
	 * 通过用户id获取设备标识码
	 * @author hsy
	 * @param userId
	 * @return
	 */
	MsgPushCommonCustomize getMobileCodeByUserId(Integer userId);
	
	/**
	 * 通过手机号获取设备标识码
	 * 
	 * @param msgPushCommonCustomize
	 * @return
	 */
	List<MsgPushCommonCustomize> getMobileCodeByMobiles(MsgPushCommonCustomize msgPushCommonCustomize);


}