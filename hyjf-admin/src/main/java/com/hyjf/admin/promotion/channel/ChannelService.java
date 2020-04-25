package com.hyjf.admin.promotion.channel;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.Utm;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.customize.ChannelCustomize;

public interface ChannelService {

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public Integer countList(ChannelCustomize channelCustomize);

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public List<ChannelCustomize> getRecordList(ChannelCustomize channelCustomize);

	/**
	 * 获取单表
	 * 
	 * @return
	 */
	public Utm getRecord(String id);

	/**
	 * 获取单表
	 * 
	 * @return
	 */
	public Utm getRecord(String utmSource, String utmTerm);

	/**
	 * 插入
	 * 
	 * @param record
	 */
	public void insertRecord(ChannelCustomize record);

	/**
	 * 维护插入
	 * 
	 * @param record
	 */
	public void insertRecord(List<Utm> utmList);

	/**
	 * 更新
	 * 
	 * @param record
	 */
	public void updateRecord(ChannelCustomize record);

	/**
	 * 删除
	 * 
	 * @param record
	 */
	public void deleteRecord(String sendCd);

	/**
	 * 取pc渠道
	 * 
	 * @return
	 */
	public List<UtmPlat> getUtmPlat(String sourceId);

	/**
	 * 推荐人用户名
	 * 
	 * @param userId
	 * @return
	 * @author Administrator
	 */
	public int checkUtmReferrer(String username);

	/**
	 * 获取用户
	 * 
	 * @param utmReferrer
	 * @return
	 */
	public Users getUser(String utmReferrer, String userId);

	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public JSONArray uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
